package com.example.chat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class HandelingEvents implements Runnable{
    public static ArrayList<HandelingEvents> handlingEvents = new ArrayList<>();
    public Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String client;
    public HandelingEvents(Socket socket, String client) {// the client's socket connection and client name
        try {
            this.socket = socket;
            // input and output streams initialized for socket connection
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.client = client;
            //add newly connected client to clients (handlingEvents) arraylist
            handlingEvents.add(this);
            //sends a message to all connected clients except the newly connected client
            sendMessage(client + " connected!");
        } catch (IOException e) {
            closeAll(socket, bufferedWriter, bufferedReader);
        }
        }   private void closeAll(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
            //if this method is called, the client removed from the client (handlingzEvents) arraylist
            removeClient();

        /*close all things (bfferReader, bufferWriter, socket) connected to client connection
        close() method in BufferReader, BufferedWriter is called to close them
        close() method in Socket Closes this socket.
        Any thread currently blocked in an I/O operation upon this socket will throw a SocketException.
        Once a socket has been closed, it is not available for further networking use (i.e. can't be reconnected or rebound).
        A new socket needs to be created.
        Closing this socket will also close the socket's InputStream and OutputStream.
        If this socket has an associated channel then the channel is closed as well.
        Throws:
        IOException – if an I/O error occurs when closing this socket.*/

            try {
                if (bufferedReader != null) {
                    bufferedReader.close();

                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }

                    if (socket != null) {
                        socket.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        // Send message to all connected clients except the sender
        private void sendMessage(String message) {
            //iterates through the arraylist list and writes the message to the bufferedWriter of each connected client.
            //writes message to bufferwriter of each connected client
            for (HandelingEvents handle : handlingEvents) {
                try {
                    if (!handle.client.equals(client)) {
                        handle.bufferedWriter.write(message);
                        handle.bufferedWriter.newLine();
                        handle.bufferedWriter.flush();
                    }
                } catch (IOException e) {
                    //If any exceptions occur during socket operations or message sending, close the socket
                    closeAll(socket, bufferedWriter, bufferedReader);
                }
            }
        }
/*If this thread was constructed using a separate Runnable run object,
then that Runnable object's run method is called; otherwise,
this method does nothing and returns.*/

    /*Thread class implements by  Runnable interface. Runnable is a functional interface.
    it contains an abstract method called public abstract void run();
    When we implement class with  Runnable interface we should override the method run()*/
        @Override

        /*Logic for handling clients msgs */
        public void run() {
            String clientMessage;

    /*It reads messages from the bufferedReader and checks if the message is a file download request.
    If it is, it calls the downloadFile() method to handle the file download. Otherwise,
    it calls the sendMessage() method to broadcast the message to all connected clients except the sender.*/

            while (socket.isConnected()) {
                try {
                    clientMessage = bufferedReader.readLine();
                    if (clientMessage.split(":")[0].equals("FILE")) {
                        downloadFile(clientMessage.split(":")[1]);
                    } else {
                        sendMessage(clientMessage);
                    }

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                /*By closing these resources, it ensures that any underlying network connections are
                 released and any buffers are flushed and closed properly. This helps prevent memory
                  leaks and ensures that the system resources are freed up for other processes.
                  Overall, calling the closeAll method in the run method is a good practice
                  to handle the cleanup and resource release when an exception occurs or when
                   the client's connection is terminated.
*/
                    closeAll(socket, bufferedWriter, bufferedReader);
                    break;
                }
            }

        }

        /*This method handles the file download process*/
/*It reads the file name and content from the DataInputStream,
creates a file with the given name, writes the file content to the file,
and then calls the sendFile() method to broadcast the downloaded file to all connected clients except the sender*/
        private void downloadFile(String name) throws IOException {
        /*We create a DataInputStream to read data from the input stream of the socket,
         which represents the client's connection. We read an integer value representing
         the length of the file name from the data input stream.*/
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            int fileNameLength = dataInputStream.readInt();
        /*check if the file name length > zero to sure for valid file name.
           a byte array creatd with the length of the file name and read the bytes from the data input stream into the array.
          convert the byte array to a string, representing the file name.*/
            if (fileNameLength > 0) {
                System.out.println(fileNameLength);
                byte[] fileNameByte = new byte[fileNameLength];
                /*readFully()-->Bytes for this operation are read from the contained input stream.*/
                dataInputStream.readFully(fileNameByte, 0, fileNameByte.length);
                /*convert the byte array to a string, representing the file name*/
                String fileName = new String(fileNameByte);
                System.out.println(fileName);
/*readInt()--> Bytes for this operation are read from the contained input stream.
Returns:
the next four bytes of this input stream, interpreted as an int.*/
                int fileContentLength = dataInputStream.readInt();
                if (fileContentLength > 0) {
                    byte[] fileContentByte = new byte[fileContentLength];
                    dataInputStream.readFully(fileContentByte, 0, fileContentLength);
                    /*creates a file with the given name, writes the file content to the file*/
                    File fileToDownload = new File(fileName);
                    try {
                        /*use FileOutputStream to write the file content to the file*/
                        FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
                        /*write the file content byte array to the file using the file output stream*/
                        fileOutputStream.write(fileContentByte);
                        /*close the file output stream to ensure that the data is properly flushed and saved.*/
                        fileOutputStream.close();
                        System.out.println("download");
                    /*call the sendFile method to send the downloaded file to all connected clients,by
                     passing the File object and the provided name.*/
                        sendFile(fileToDownload, name);
                    } catch (IOException e) {
                        System.out.println("download " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }

    /*call the sendFile method to send the downloaded file to all connected clients,
     passing the File object and the provided name.*/

        public void removeClient() {
            handlingEvents.remove(this);
            sendMessage(client + " left");
        }

    /*This method is responsible for sending a file to all connected clients except the specified client.
     It reads the file content, opens an output stream to the client's socket, writes the file name and
      content to the output stream using a DataOutputStream, and flushes the stream.*/


        private void sendFile(File file, String name) {
            for (HandelingEvents clients : handlingEvents) {
                try {
                    if (!clients.client.equals(name)) {
                        FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                        DataOutputStream dataOutputStream = new DataOutputStream(clients.socket.getOutputStream());
                        PrintWriter printWriter = new PrintWriter(clients.socket.getOutputStream());
                        printWriter.println("FILE:" + name);
                        printWriter.flush();

                        String fileName = file.getName();
                        byte[] fileNameBytes = fileName.getBytes();
                        byte[] fileContentBytes = new byte[(int) file.length()];
                        fileInputStream.read(fileContentBytes);

                        dataOutputStream.writeInt(fileNameBytes.length);
                        dataOutputStream.write(fileNameBytes);
                        dataOutputStream.writeInt(fileContentBytes.length);
                        dataOutputStream.write(fileContentBytes);
                        dataOutputStream.flush();
                        System.out.println(clients.client + " to send " + fileName);

                    }
                } catch (IOException e) {
                    System.out.println("upload " + e.getMessage());
                    closeAll(socket, bufferedWriter, bufferedReader);
                }
            }
        }
    }



package com.example.chat;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    String senderName;

    public Client(Socket socket, String name) throws IOException {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(name);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            // System.out.println("error on client side");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private static String getFileExtension(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    /*method sends a text message to the server by writing it to the BufferedWriter.*/
    public void sendMessageToServer(String messageToServer) {
        try {
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            // System.out.println("cant send to client");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /*method sends a text message to the server by writing it to the BufferedWriter.*/
    public void sendFileToServer(File file, String name) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println("FILE:" + name);
            printWriter.flush();

            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String fileName = file.getName();
            byte[] fileNameBytes = fileName.getBytes();
            byte[] fileContentBytes = new byte[(int) file.length()];
            fileInputStream.read(fileContentBytes);

            dataOutputStream.writeInt(fileNameBytes.length);
            dataOutputStream.write(fileNameBytes);
            dataOutputStream.writeInt(fileContentBytes.length);
            dataOutputStream.write(fileContentBytes);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /*method continuously reads messages from the server using the BufferedReader.
     If a message starts with "FILE:", it indicates a file is being received.
     The file name and content are read from the input stream and added to the UI.*/
// Other imports...

    public void receiveMessageFromServer(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String messageFromClient = bufferedReader.readLine();
                        if (messageFromClient.split(":")[0].equals("FILE")) {
                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            int fileNameLength = dataInputStream.readInt();
                            if (fileNameLength > 0) {
                                byte[] fileNameByte = new byte[fileNameLength];
                                dataInputStream.readFully(fileNameByte, 0, fileNameByte.length);
                                String fileName = new String(fileNameByte);
                                String senderName = messageFromClient.split(":")[1];

                                int fileContentLength = dataInputStream.readInt();
                                if (fileContentLength > 0) {
                                    byte[] fileContentByte = new byte[fileContentLength];
                                    dataInputStream.readFully(fileContentByte, 0, fileContentLength);

                                    Platform.runLater(() -> {
                                        Image image = new Image(new ByteArrayInputStream(fileContentByte));
                                        ImageView fileToDownload = new ImageView(image);
                                        ClientchatFormController.addImage(fileToDownload, vBox, senderName);
                                    });
                                }
                            }
                        } else {
                            String senderName = ""; // Set the sender name accordingly
                            Platform.runLater(() -> {
                                ClientchatFormController.addLabel(messageFromClient, vBox, senderName);
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error reading message from client");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*method is responsible for displaying a file chooser dialog and getting the selected file's extension.*/
    public void sendDocumentsToServer(File file, String name) {
        // Create a file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        // Show the file chooser dialog
        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Get the file extension
        if (selectedFile != null) {
            Path filePath = selectedFile.toPath();
            String fileExtension = getFileExtension(filePath);
            System.out.println("File Extension: " + fileExtension);
        }
    }
}

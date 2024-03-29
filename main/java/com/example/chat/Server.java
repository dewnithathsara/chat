package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
    private final ServerSocket serverSocket;
    public Server(ServerSocket serverSocket){
        this.serverSocket=serverSocket;
    }


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3000);
        Server server = new Server(serverSocket);
        server.startServer();

    }
    public void startServer(){
        try {
          //  serverSocket = new ServerSocket(3000);
            while(true) {
                System.out.println("Waiting for clients...");
                Socket socket = serverSocket.accept();
                System.out.println("Connected");
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader); /*Buffer the input reader for efficient reading*/
                String name = bufferedReader.readLine();
                HandelingEvents handelingEvents = new HandelingEvents(socket, name);
                Thread thread = new Thread(handelingEvents);// Create a new thread to handle the client's events
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(3000);
            while(true) {
                System.out.println("Waiting for clients...");
                socket = serverSocket.accept();
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

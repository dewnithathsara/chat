package com.example.chat;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerFormController {

    public AnchorPane pane;
    public TextArea textArea;
    public TextField textField;
    public Button btnSend;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    ServerSocket serverSocket;
    Socket socket;
    String message="";

    public void initialize(){

        try{
            serverSocket = new ServerSocket( 3000);
            textArea.appendText("started");
            socket=serverSocket.accept();
            textField.appendText("\nclient connected");
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream=new DataInputStream(socket.getInputStream());

            //txtArea.appendText("connected");
            String reply="";
            // String message="";
            while(!message.equals("exit")){
                // dataOutputStream.writeUTF(reply);
                message=  dataInputStream.readUTF();
                textArea.appendText("\nclient:  "+ message);
            }
            dataInputStream.close();
            dataOutputStream.close();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        String message=textField.getText();
        dataOutputStream.writeUTF("Server"+message);

        dataOutputStream.flush();
    }
}

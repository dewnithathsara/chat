package com.example.chat;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigation {
    public static AnchorPane pane;

    public static void navigate(Rote route, AnchorPane pane) throws IOException {
        Navigation.pane = pane;
        Navigation.pane.getChildren().clear();
        Stage window = (Stage) Navigation.pane.getScene().getWindow();
        switch (route) {
            case LOGIN:
                window.setTitle("Register Form");
                initUI("Login.fxml");
                break;
            case SIGNUP:
                window.setTitle("Register Form");
                initUI("SignUp.fxml");
                break;
            case CLIENT:
                window.setTitle("Client Form");
                initUI("ClientchatForm.fxml");
                break;
            default:
                System.out.println("Mukuth Natho");
        }
    }

    public static void initUI(String location) throws IOException {
        Navigation.pane.getChildren().add(FXMLLoader.load(Navigation.class.getResource("/com/example/chat/" + location)));
    }
}


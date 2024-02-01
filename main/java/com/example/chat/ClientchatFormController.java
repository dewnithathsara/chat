package com.example.chat;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static com.example.chat.LoginController.username;
import static com.example.chat.LoginController.host;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;

public class ClientchatFormController {
    public AnchorPane pane;
    public TextArea textArea;
    public TextField textField;
    public Button btnSend;
    public Button btnemoji;
    DataOutputStream dataOutputStream;
    public VBox txtClientArea;
    DataInputStream dataInputStream;
    Socket socket=null;
    String message="";
    public String host="localhost";
   private Client client;

    public void initialize() throws IOException {
        client = new Client(new Socket(host, 3000), username);

        textField.requestFocus();
        client.receiveMessageFromServer(txtClientArea);
    }
    public static void addLabel(String messageFromClient, VBox vBox, String sender) {
        Platform.runLater(() -> {
            HBox hBox = new HBox();
            Scene scene = new Scene(hBox);
            hBox.setSpacing(10); // Set the horizontal spacing within the HBox
            hBox.setPadding(new Insets(10));
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5, 10, 5, 5));
            Text text = new Text(messageFromClient);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: rgb(206,203,203);" + "-fx-background-radius: 10px");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            hBox.getChildren().add(textFlow);


            if (username!= null && username.equals(sender)) {
                // Sender's message, align to the right
                hBox.setAlignment(Pos.CENTER_LEFT);
            } else {
                // Receiver's message, align to the left
                hBox.setAlignment(Pos.CENTER_RIGHT);
            }
            vBox.getChildren().add(hBox);
        });
    }


    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        String clientMessage = textField.getText();
        Platform.runLater(() -> {
            if (!clientMessage.isEmpty()) {
                HBox pane = new HBox();
                pane.setPadding(new Insets(5, 10, 5, 10));
                pane.setLayoutX(320);
                Text text = new Text(clientMessage);
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-background-color: #7E308E;" + "-fx-background-radius: 10px");
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                text.setFill(Color.color(1, 1, 1));
                pane.getChildren().add(textFlow);
               // txtClientArea.getChildren().add(pane);
                textArea.appendText("me:"+ clientMessage);
                client.sendMessageToServer(username + " : " + clientMessage);
                textField.clear();
                pane.setSpacing(20); // Set the horizontal spacing within the HBox
                pane.setPadding(new Insets(10));
                pane.setStyle(" -fx-vgap: 10px;");
                // Set horizontal gap

            }
        });
    }

    public void btnSendEmojiOnAction(ActionEvent actionEvent) throws IOException {
        String meaning = textField.getText();
        String emoji = findEmoji(meaning);


        textArea.setText(emoji);
    }
    public String findEmoji(String s) {
        if (s.equals("angry")) {
            String angryEmoji = "😠";

            return angryEmoji;
        }
        else if (s.equals("sad")) {
            String sadEmoji = "\uD83D\uDE1E";
            return sadEmoji;
        }
        if (s.equals("happy")) {
            String happyEmoji = "😊";
            return happyEmoji;
        }
        if (s.equals("laugh")) {
            String laughEmoji = "\uD83D\uDE02";
            return laughEmoji;
        }
        if (s.equals("cry")) {
            String cryEmoji = "\uD83D\uDE22";
            return cryEmoji;
        }
        if (s.equals("heart")) {
            String heartEmoji = "\u2764\uFE0F";
            return  heartEmoji;
        }
        if (s.equals("love")) {
            String loveEmoji = "\uD83E\uDD70";

            return loveEmoji;
        }
        if (s.equals("dog")) {
            String dogEmoji = "\uD83D\uDC3E";
          return  dogEmoji;
        }
        if (s.equals("cat")) {
            String catEmoji = "\uD83D\uDC31";
          return catEmoji;
        }
        if (s.equals("wow")) {
            String wowEmoji = "\uD83D\uDE2E";
            return wowEmoji;
        } if (s.equals("flower")) {
            String flowersEmoji = "\uD83D\uDC90";
            return flowersEmoji;
        }
        return null;
    }
    public static void addImage(ImageView fileToDownload, VBox vBox, String senderName) {
        Platform.runLater(() -> {
            HBox hBox = new HBox();
            Scene scene = new Scene(hBox);

            VBox vBox1 = new VBox();
            hBox.setSpacing(5); // Set the horizontal spacing within the HBox
            hBox.setPadding(new Insets(10));
            vBox1.setStyle("-fx-background-color: rgb(198,194,194);" + "-fx-background-radius: 5px");
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5, 10, 5, 10));
            Text text = new Text(senderName);
            vBox1.getChildren().add(text);
            Image image = new Image("file:" + fileToDownload.getImage());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            vBox1.setPadding(new Insets(5, 10, 5, 10));
            vBox1.getChildren().add(imageView);
            hBox.getChildren().add(vBox1);

            if (senderName != null && senderName.equals(username)) {
                // Sender's message, align to the right
                hBox.setAlignment(Pos.CENTER_LEFT);
            } else {
                // Receiver's message, align to the left
                hBox.setAlignment(Pos.CENTER_RIGHT);
            }

            vBox.getChildren().add(hBox);
        });
    }
    private static String getFileExtension(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }
    public void fileClickOnAction(MouseEvent mouseEvent) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        Platform.runLater(() -> {
            if (file != null) {
                HBox hBox = new HBox();
                Scene scene = new Scene(hBox);

                hBox.setSpacing(5); // Set the horizontal spacing within the HBox
                hBox.setPadding(new Insets(10));
                hBox.setAlignment(Pos.BASELINE_LEFT);
                hBox.setPadding(new Insets(5, 10, 5, 10));
                Image image = new Image("file:" + file.getAbsolutePath());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                hBox.getChildren().add(imageView);
                txtClientArea.getChildren().add(hBox);
                client.sendFileToServer(file, username);

                System.out.println("send " + file.getName());

                if (file != null) {
                    Path filePath = file.toPath();
                    String fileExtension = getFileExtension(filePath);
                    Text text = new Text(file + fileExtension);
                    System.out.println("File Extension: " + fileExtension);
                    HBox hBox1 = new HBox();
                    Scene scene1 = new Scene(hBox1);

                    hBox1.setSpacing(5); // Set the horizontal spacing within the HBox
                    hBox1.setPadding(new Insets(10));
                    hBox1.setAlignment(Pos.CENTER_RIGHT);
                    hBox1.setPadding(new Insets(5, 10, 5, 10));
                    hBox1.getChildren().add(text);
                    txtClientArea.getChildren().add(hBox1);
                    client.sendDocumentsToServer(file, username);

                }
            }
        });
    }


}

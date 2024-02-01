package com.example.chat;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;



public class LoginController {
    public AnchorPane pane;
    public Label label;
    public JFXButton btnLogin;
    public Text text1;
    public Text txtSignUP;
    public PasswordField password;

    public TextField txtUsername;
    UserModel userModel=new UserModel();
    Stage stage=new Stage();

    public  static  String host;
   public static String username;

    public void btnLogInOnAction(ActionEvent actionEvent) throws ClassNotFoundException {
         username = txtUsername.getText();
        String pass = password.getText();

        if (username.trim().isEmpty() || pass.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Username and password can't be blank").show();
        } else {
            var dto = new UserDto(username, pass);
            try {
                boolean isLogin = userModel.userLogin(dto);
                System.out.println(isLogin);
                System.out.println(username);

                if (isLogin) {
                    // checkCredentials(username);
                    new Alert(Alert.AlertType.CONFIRMATION, "User Logged in").showAndWait();
                    openNewClient();

                } else {
                    new Alert(Alert.AlertType.ERROR, "Invalid credentials. Please try again.").show();
                }
            } catch (SQLException e) {
                System.out.println(" Meke SQl ex eka Mokdda: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    private void openNewClient() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerInitializer.class.getResource("ClientchatForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(username+"'s chat");
        stage.setScene(scene);
        stage.show();

    }

    private void checkCredentials(String username) {
    }

    public void SignupOnClick(MouseEvent mouseEvent) throws IOException {
        Navigation.navigate(Rote.SIGNUP,pane);
    }
}
package com.example.chat;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpFormController {
    public AnchorPane pane;
    public Label label;
    public TextField txtname;
    public PasswordField txtpassword;
    public JFXButton tnSignUp;
    public TextField txtemail;
    public Text text;
  UserModel usermodel=new UserModel();

    public void btnSignUpOnAction(ActionEvent actionEvent) throws SQLException, IOException {
      String username=  txtname.getText();
      String email=txtemail.getText();
      String password=txtpassword.getText();
      var dto=new UserDto(username,email,password);
      boolean isSaved=usermodel.usersave(dto);
      if(isSaved){
         new Alert(Alert.AlertType.CONFIRMATION,"user saved").showAndWait();
         Navigation.navigate(Rote.LOGIN,pane);
      }else{
          new Alert(Alert.AlertType.ERROR,"user not saved").show();
      }

    }

    public void txtLoginOnClick(MouseEvent mouseEvent) throws IOException {
        Navigation.navigate(Rote.LOGIN,pane);
    }
}

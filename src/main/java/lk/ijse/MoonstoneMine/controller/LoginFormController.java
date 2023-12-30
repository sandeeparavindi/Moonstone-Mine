package lk.ijse.MoonstoneMine.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.MoonstoneMine.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    void btnSignUpOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/signup_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("SignUp Form");
        stage.centerOnScreen();
        stage.show();
    }

    private final String adminUserName = "admin";
    private final String adminPassword = "a1234";

   @FXML
   void btnLoginOnAction(ActionEvent event) throws IOException {

    String enteredUserName = txtUserName.getText();
    String enteredPassword = txtPassword.getText();

    if(enteredUserName.equals(adminUserName) && enteredPassword.equals(adminPassword)){
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/admin_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Admin Form");
        stage.centerOnScreen();
        stage.show();

    }

       try {
           boolean useIsExist = UserModel.isExistUser(enteredUserName,enteredPassword);
           if (useIsExist){

               navigateToCashierWindow();
           }
       } catch (SQLException | ClassNotFoundException throwables) {
           throwables.printStackTrace();
       }
   }
    private void navigateToCashierWindow() throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/cashier_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Cashier Form");
        stage.centerOnScreen();
        stage.show();
    }
}

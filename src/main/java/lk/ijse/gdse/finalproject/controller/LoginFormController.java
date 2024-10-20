package lk.ijse.gdse.finalproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginFormController {

    @FXML
    private AnchorPane LoginPage;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    void LoginOnAction(ActionEvent event) throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.equals("Dineth") && password.equals("1234")){
            AnchorPane load = FXMLLoader.load(getClass().getResource("/view/DashBoard.fxml"));
            LoginPage.getChildren().clear();
            LoginPage.getChildren().add(load);
        }else {
            new Alert(Alert.AlertType.ERROR,"something wrong !").show();
        }

    }

}

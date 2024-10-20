package lk.ijse.gdse.finalproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashBoardController {

    @FXML
    private AnchorPane  ancBody;

    @FXML
    void LaborOnAction(ActionEvent event) throws IOException {
        AnchorPane laborpage = FXMLLoader.load(getClass().getResource("/view/Labor.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(laborpage);
    }
    @FXML
    void SupervisorOnAction(ActionEvent event) throws IOException {
        AnchorPane supervisorpage = FXMLLoader.load(getClass().getResource("/view/Supervisor.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(supervisorpage);
    }
    @FXML
    void AttendanceOnAction(ActionEvent event) throws IOException {
        AnchorPane AttendancePage = FXMLLoader.load(getClass().getResource("/view/Attendance.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(AttendancePage);
    }
    @FXML
    void ShiftOnAction(ActionEvent event) throws IOException {
        AnchorPane shiftpage = FXMLLoader.load(getClass().getResource("/view/Shift.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(shiftpage);
    }
    @FXML
    void FactoryOfficerOnAction(ActionEvent event) throws IOException {
        AnchorPane Officerpage = FXMLLoader.load(getClass().getResource("/view/FactoryOfficer.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(Officerpage);
    }
    @FXML
    void LeaveOnAction(ActionEvent event) throws IOException {
        AnchorPane Leavepage = FXMLLoader.load(getClass().getResource("/view/Leave.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(Leavepage);
    }
    @FXML
    void TrainingOnAction(ActionEvent event) throws IOException {
        AnchorPane Trainingpage = FXMLLoader.load(getClass().getResource("/view/Training.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(Trainingpage);
    }
    @FXML
    void AccidentOnAction(ActionEvent event) throws IOException {
        AnchorPane AccidentPage = FXMLLoader.load(getClass().getResource("/view/Accident.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(AccidentPage);
    }

}
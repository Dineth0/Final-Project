package lk.ijse.gdse.finalproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {

    @FXML
    private AnchorPane  ancBody;

    @FXML
    private AnchorPane content;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadDashboard();

    }
    private void loadDashboard() {
        try {
            AnchorPane laborpage = FXMLLoader.load(getClass().getResource("/view/DashBoard.fxml"));
            ancBody.getChildren().clear();
            ancBody.getChildren().add(laborpage);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

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
    @FXML
    void InsuranceOnAction(ActionEvent event) throws IOException {
        AnchorPane InsurancePage = FXMLLoader.load(getClass().getResource("/view/Insurance.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(InsurancePage);
    }
    @FXML
    void FeedbackOnAction(ActionEvent event) throws IOException {
        AnchorPane ComplaintPage = FXMLLoader.load(getClass().getResource("/view/Complaint.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(ComplaintPage);
    }
    @FXML
    void EquipmenOnAction(ActionEvent event) throws IOException {
        AnchorPane EquipmenPage = FXMLLoader.load(getClass().getResource("/view/Equipmen.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(EquipmenPage);
    }
    @FXML
    void MembershipOnAction(ActionEvent event) throws IOException {
        AnchorPane MemberPage = FXMLLoader.load(getClass().getResource("/view/Union-membership.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(MemberPage);
    }

    @FXML
    void SendMailOnAction(ActionEvent event) throws IOException {
        AnchorPane MailPage = FXMLLoader.load(getClass().getResource("/view/SendMailView.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(MailPage);
    }
    @FXML
    void PaymentOnAction(ActionEvent event) throws IOException {
        AnchorPane PaymentPage = FXMLLoader.load(getClass().getResource("/view/Payment.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(PaymentPage);
    }
    @FXML
    void RetirementOnAction(ActionEvent event) throws IOException {
        AnchorPane RetirementPage = FXMLLoader.load(getClass().getResource("/view/Retirement.fxml"));
        ancBody.getChildren().clear();
        ancBody.getChildren().add(RetirementPage);
    }


    public void DashboardOnAction(ActionEvent actionEvent) {
        loadDashboard();
    }
}
package lk.ijse.gdse.finalproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.gdse.finalproject.model.LaborModel;
import lk.ijse.gdse.finalproject.model.PaymentModel;
import lk.ijse.gdse.finalproject.model.RetirementModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    @FXML
    private AnchorPane Dashboard2;


    @FXML
    private Button seeaccident;

    @FXML
    private Button seeattend;

    @FXML
    private Button seeinsurance;

    @FXML
    private Button seelabor;

    @FXML
    private Button seeleave;

    @FXML
    private Button seepayment;

    @FXML
    private Button seeretirement;

    @FXML
    private Button seeshift;
    @FXML
    private Label lbltotallabor;

    @FXML
    private Label lbltotalretirement;

    @FXML
    private Label lbltime;
    @FXML
    private Label lbldate;
    @FXML
    private Label lblpayment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbldate.setText(LocalDate.now().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        lbltime.setText(LocalTime.now().format(formatter));
        loadLaborCount();
        loadRetirementCount();
        loadPaymentCount();
    }

    @FXML
    void SeeAccidentsOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SeeAccidents.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("See Accidents");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = seelabor.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }

    @FXML
    void SeeAttendancesOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SeeAttendances.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("See Attendances");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = seelabor.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }

    @FXML
    void SeeInsurancesOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SeeInsurances.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("See Insurances");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = seelabor.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }

    @FXML
    void SeeLaborOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SeeLabors.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("See Labors");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = seelabor.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }

    @FXML
    void SeeLeavesOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SeeLeaves.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("See Leaves");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = seelabor.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }

    @FXML
    void SeePaymentsOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SeePayments.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("See Payments");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = seelabor.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }

    @FXML
    void SeeRetirementsOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SeeRetirements.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("See Retirements");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = seelabor.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }

    @FXML
    void SeeShiftsOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SeeShifts.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("See Shifts");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = seelabor.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }

    private void loadLaborCount() {
        try {
            LaborModel laborModel = new LaborModel();
            int totalLabors = laborModel.getTotalLabors();
            lbltotallabor.setText(String.valueOf(totalLabors));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadRetirementCount() {
        try {
            RetirementModel retirementModel = new RetirementModel();
            int totalRetirements = retirementModel.getTotalRetirements();
            lbltotalretirement.setText(String.valueOf(totalRetirements));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPaymentCount() {
        try {
            PaymentModel paymentModel = new PaymentModel();
            int CompletePayment = paymentModel.getTotalPaymentCount();
            lblpayment.setText(String.valueOf(CompletePayment));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void AddOfficerOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FactoryOfficer.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("Add Factory Officer");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = seelabor.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }

    @FXML
    void AddSupervisorOAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Supervisor.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("Add Supervisor");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = seelabor.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }
}


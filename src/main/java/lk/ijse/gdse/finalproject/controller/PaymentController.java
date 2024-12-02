package lk.ijse.gdse.finalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.FactoryOfficerDto;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.LeaveDto;
import lk.ijse.gdse.finalproject.dto.PaymentDto;
import lk.ijse.gdse.finalproject.dto.tm.LeaveTM;
import lk.ijse.gdse.finalproject.dto.tm.PaymentTM;
import lk.ijse.gdse.finalproject.model.FactoryOfficerModel;
import lk.ijse.gdse.finalproject.model.LaborModel;
import lk.ijse.gdse.finalproject.model.LeaveModel;
import lk.ijse.gdse.finalproject.model.PaymentModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    @FXML
    private AnchorPane PaymentPage;


    @FXML
    private TableColumn<PaymentTM,String> collabor;

    @FXML
    private TableColumn<PaymentTM,Double> colmts;

    @FXML
    private TableColumn<PaymentTM,String> colname;

    @FXML
    private TableColumn<PaymentTM,String> colpayment;

    @FXML
    private TableColumn<PaymentTM,String> colstatus;


    @FXML
    private TableView<PaymentTM> paymenttable1;



    @FXML
    private Button addpayment;


    PaymentModel paymentModel = new PaymentModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colpayment.setCellValueFactory(new PropertyValueFactory<>("PaymentID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colmts.setCellValueFactory(new PropertyValueFactory<>("Monthly_Total_Salary"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        try {
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load PaymentID").show();
        }
    }

    public void refreshPage() throws SQLException {

        loadTableData();

    }





    private void loadTableData() throws SQLException {
        ArrayList<PaymentDto> paymentDtos = paymentModel.getAllPayments();
        ObservableList<PaymentTM> paymentTMS = FXCollections.observableArrayList();

        for(PaymentDto paymentDto : paymentDtos){
            PaymentTM paymentTM = new PaymentTM(
                    paymentDto.getPaymentID(),
                    paymentDto.getLaborID(),
                    paymentDto.getName(),
                    paymentDto.getOfficerID(),
                    paymentDto.getDay_Basic_Salary(),
                    paymentDto.getMonthly_Total_Salary(),
                    paymentDto.getStatus()


            );
            paymentTMS.add(paymentTM);
        }
        paymenttable1.setItems(paymentTMS);
    }
    @FXML
    void AddPaymnetOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddPayment.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("Add Labors");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = addpayment.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }

    @FXML
    void GeneratePaymentReportOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/report/PaymentReport.jrxml"
                            ));

            Connection connection = DBConnection.getInstance().getConnection();

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    null,
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to generate report...!").show();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "DB error...!").show();
        }
    }

    public void TableOnClicked(MouseEvent mouseEvent) {

    }
}

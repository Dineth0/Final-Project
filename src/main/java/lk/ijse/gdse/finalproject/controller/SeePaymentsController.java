package lk.ijse.gdse.finalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.finalproject.dto.PaymentDto;
import lk.ijse.gdse.finalproject.dto.tm.PaymentTM;
import lk.ijse.gdse.finalproject.model.PaymentModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SeePaymentsController implements Initializable {

    @FXML
    private TableColumn<PaymentTM,String> caolofficer;

    @FXML
    private TableColumn<PaymentTM,Double> coldbs;

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
    private final String[] Status = {"Payment Taken","Not Taken"};

    @FXML
    private TableView<PaymentTM> paymenttable1;

    PaymentModel paymentModel = new PaymentModel();

    @FXML
    void TableOnClicked(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // comstatus.getItems().addAll(Status);
        colpayment.setCellValueFactory(new PropertyValueFactory<>("PaymentID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        caolofficer.setCellValueFactory(new PropertyValueFactory<>("OfficerID"));
        coldbs.setCellValueFactory(new PropertyValueFactory<>("Day_Basic_Salary"));
        colmts.setCellValueFactory(new PropertyValueFactory<>("Monthly_Total_Salary"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        try {
            refreshPage();;
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private void refreshPage() throws SQLException {

        loadTableData();
    }
}

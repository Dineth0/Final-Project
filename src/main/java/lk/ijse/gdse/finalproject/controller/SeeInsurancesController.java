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
import lk.ijse.gdse.finalproject.dto.InsuranceDto;
import lk.ijse.gdse.finalproject.dto.tm.InsuranceTM;
import lk.ijse.gdse.finalproject.model.InsuranceModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SeeInsurancesController implements Initializable {

    @FXML
    private TableColumn<InsuranceTM,String> colacc;

    @FXML
    private TableColumn<InsuranceTM,String> colname;

    @FXML
    private TableColumn<InsuranceTM,String> colnumber;

    @FXML
    private TableColumn<InsuranceTM,String> colofficer;

    @FXML
    private TableColumn<InsuranceTM,Double> colpayment;

    @FXML
    private TableView<InsuranceTM> tableaddinsurance1;

    InsuranceModel insuranceModel = new InsuranceModel();

    @FXML
    void OnTableClicked(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colnumber.setCellValueFactory(new PropertyValueFactory<>("PolicyNumber"));
        colacc.setCellValueFactory(new PropertyValueFactory<>("AccidentID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("LaborName"));
        colofficer.setCellValueFactory(new PropertyValueFactory<>("OfficerID"));
        colpayment.setCellValueFactory(new PropertyValueFactory<>("InsurancePayment"));


        try {
            refreshPage();;
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadTableData() throws SQLException {
        ArrayList<InsuranceDto> insuranceDtos = insuranceModel.getAllInsurances();
        ObservableList<InsuranceTM> insuranceTMS = FXCollections.observableArrayList();

        for(InsuranceDto insuranceDto : insuranceDtos){
            InsuranceTM insuranceTM = new InsuranceTM(
                    insuranceDto.getPolicyNumber(),
                    insuranceDto.getAccidentID(),
                    insuranceDto.getName(),
                    insuranceDto.getOfficerID(),
                    insuranceDto.getInsurancePayment()


            );
            insuranceTMS.add(insuranceTM);
        }
        tableaddinsurance1.setItems(insuranceTMS);
    }
    private void refreshPage() throws SQLException {

        loadTableData();
    }
}

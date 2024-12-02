package lk.ijse.gdse.finalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.AccidentDto;
import lk.ijse.gdse.finalproject.dto.FactoryOfficerDto;
import lk.ijse.gdse.finalproject.dto.InsuranceDto;
import lk.ijse.gdse.finalproject.dto.tm.InsuranceTM;
import lk.ijse.gdse.finalproject.model.AccidentModel;
import lk.ijse.gdse.finalproject.model.FactoryOfficerModel;
import lk.ijse.gdse.finalproject.model.InsuranceModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddInsuranceController implements Initializable {

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<InsuranceTM,String> colacc;

    @FXML
    private TableColumn<InsuranceTM,String> colname;

    @FXML
    private TableColumn<InsuranceTM,String> colnumber;

    @FXML
    private TableColumn<InsuranceTM,String> colofficer;

    @FXML
    private TableColumn<InsuranceTM,String> colpayment;

    @FXML
    private ComboBox<String> comaccid;

    @FXML
    private ComboBox<String> comofficerid;

    @FXML
    private Label lblid;

    @FXML
    private Label lblname;

    @FXML
    private Label lblofficername;

    @FXML
    private TableView<InsuranceTM> tableaddinsurance;

    @FXML
    private TextField txtpayment;

    FactoryOfficerModel factoryOfficerModel = new FactoryOfficerModel();
    AccidentModel accidentModel = new AccidentModel();
    InsuranceModel insuranceModel = new InsuranceModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colnumber.setCellValueFactory(new PropertyValueFactory<>("PolicyNumber"));
        colacc.setCellValueFactory(new PropertyValueFactory<>("AccidentID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("LaborName"));
        colofficer.setCellValueFactory(new PropertyValueFactory<>("OfficerID"));
        colpayment.setCellValueFactory(new PropertyValueFactory<>("InsurancePayment"));


        try {
            loadNextPolicyNumber();
            loadAccidentIDs();
            loadOfficerIDs();
            loadTableData();
            refreshPage();;
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load InsuranceID").show();
        }
    }

    @FXML
    void Combo1OnAction(ActionEvent event) throws SQLException {
        String selectedOfficerID = comofficerid.getSelectionModel().getSelectedItem();
        FactoryOfficerDto factoryOfficerDto = factoryOfficerModel.findById(selectedOfficerID);

        if (factoryOfficerDto != null) {
            lblofficername.setText(factoryOfficerDto.getName());
        }
    }

    @FXML
    void ComboOnAction(ActionEvent event) throws SQLException {
        String selectedAccidentID = comaccid.getSelectionModel().getSelectedItem();
        AccidentDto accidentDto = accidentModel.findById(selectedAccidentID);

        if (accidentDto != null) {
            lblname.setText(accidentDto.getName());
        }
    }
    public void refreshPage() throws SQLException {
        loadNextPolicyNumber();
        loadTableData();

        btndelete.setDisable(true);
        btnsave.setDisable(false);
        btnupdate.setDisable(true);

        comaccid.getSelectionModel().clearSelection();
        lblname.setText("");
        comofficerid.getSelectionModel().clearSelection();
        txtpayment.setText("");

    }


    public void loadNextPolicyNumber() throws SQLException {
        String nextPolicyNumber = insuranceModel.getNextPolicyNumber();
        lblid.setText(nextPolicyNumber);
    }
    private void loadAccidentIDs() throws SQLException {
        ArrayList<String> AccidentIDs = accidentModel.getAllAccidentIDs();
        comaccid.getItems().addAll(AccidentIDs);
    }
    private void loadOfficerIDs() throws SQLException {
        ArrayList<String> OfficerIDs = factoryOfficerModel. getAllOfficerIds();
        comofficerid.getItems().addAll(OfficerIDs );
    }
    @FXML
    void SaveOnAction(ActionEvent event) throws SQLException {
        String PolicyNumber = lblid.getText();
        String AccidentID = comaccid.getValue();
        String Name = lblname.getText();
        String OfficerID = comofficerid.getValue();
        double InsurancePayment = Double.parseDouble(txtpayment.getText());


        InsuranceDto insuranceDto = new InsuranceDto(
                PolicyNumber,
                AccidentID,
                Name,
                OfficerID,
                InsurancePayment
        );
        boolean isSaved = insuranceModel.saveInsurance(insuranceDto);
        if(isSaved){
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION,"Insurance saved...!").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Fail to save Insurance...!").show();
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
        tableaddinsurance.setItems(insuranceTMS);
    }
    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        String PolicyNumber = lblid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            boolean isDeleted = insuranceModel.deleteInsurance(PolicyNumber );
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Insurance deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Insurance...!").show();
            }
        }
    }

    @FXML
    void OnTableClicked(MouseEvent event) {
        InsuranceTM insuranceTM = tableaddinsurance.getSelectionModel().getSelectedItem();
        if(insuranceTM != null){
            lblid.setText(insuranceTM.getPolicyNumber());
            comaccid.setValue(insuranceTM.getAccidentID());
            lblname.setText(insuranceTM.getLaborName());
            comofficerid.setValue(insuranceTM.getOfficerID());
            txtpayment.setText(String.valueOf(insuranceTM.getInsurancePayment()));


            btnsave.setDisable(true);

            btndelete.setDisable(false);
            btnupdate.setDisable(false);
        }
    }

    @FXML
    void ReoprtOnAction(ActionEvent event) throws SQLException {
 refreshPage();
    }

    @FXML
    void ResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }


    @FXML
    void UpdateOnAction(ActionEvent event) throws SQLException {
        String PolicyNumber = lblid.getText();
        String AccidentID = comaccid.getValue();
        String OfficerID = comofficerid.getValue();
        double InsurancePayment = Double.parseDouble(txtpayment.getText());


        InsuranceDto insuranceDto = new InsuranceDto(
                PolicyNumber,
                AccidentID,
                lblname.getText(),
                OfficerID,
                InsurancePayment

        );
        boolean isUpdate = InsuranceModel.updateInsurance(insuranceDto);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Insurance update...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update Insurance...!").show();
        }
    }
    @FXML
    void GenerateinsuranceReoprtOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/report/InsuranceReport.jrxml"
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

}

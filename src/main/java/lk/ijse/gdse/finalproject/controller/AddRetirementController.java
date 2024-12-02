package lk.ijse.gdse.finalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.finalproject.dto.FactoryOfficerDto;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.RetirementDto;
import lk.ijse.gdse.finalproject.dto.tm.RetirementTM;
import lk.ijse.gdse.finalproject.model.FactoryOfficerModel;
import lk.ijse.gdse.finalproject.model.LaborModel;
import lk.ijse.gdse.finalproject.model.RetirementModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddRetirementController implements Initializable {

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<RetirementTM, Date> coldate;

    @FXML
    private TableColumn<RetirementTM,Double> colfpayment;

    @FXML
    private TableColumn<RetirementTM,String> collabor;

    @FXML
    private TableColumn<RetirementTM,String> colname;

    @FXML
    private TableColumn<RetirementTM,String> colofficer;

    @FXML
    private TableColumn<RetirementTM,String> colretire;

    @FXML
    private TableColumn<RetirementTM,Integer> coltyw;

    @FXML
    private ComboBox<String> comlaborid;

    @FXML
    private ComboBox<String> comofficerid;

    @FXML
    private Label lbldate;

    @FXML
    private Label lblid;

    @FXML
    private Label lbllaborname;

    @FXML
    private Label lblofficername;

    @FXML
    private TableView<RetirementTM> tableaddretirement;

    @FXML
    private TextField txtfpayment;

    @FXML
    private TextField txttyw;

    LaborModel laborModel = new LaborModel();
    FactoryOfficerModel factoryOfficerModel = new FactoryOfficerModel();
    RetirementModel retirementModel = new RetirementModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbldate.setText(LocalDate.now().toString());
        colretire.setCellValueFactory(new PropertyValueFactory<>("RetirementID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colofficer.setCellValueFactory(new PropertyValueFactory<>("OfficerID"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("RetirementDate"));
        coltyw.setCellValueFactory(new PropertyValueFactory<>("TotalYearsWorked"));
        colfpayment.setCellValueFactory(new PropertyValueFactory<>("FundPayment"));

        try {
            loadNextRetirementID();
            loadLaborIDs();
            loadOfficerIDs();
            loadTableData();
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load RetirementID").show();
        }
    }

    @FXML
    void Combo1OnAction(ActionEvent event) throws SQLException {
        String selectedOfficerID = comofficerid.getSelectionModel().getSelectedItem();
        if (selectedOfficerID != null) {
            FactoryOfficerDto factoryOfficerDto = factoryOfficerModel.findById(selectedOfficerID);

            if (factoryOfficerDto != null) {
                lblofficername.setText(factoryOfficerDto.getName());
            }
        }
    }

    @FXML
    void ComboOnAction(ActionEvent event) throws SQLException {
        String selectedLaborID = comlaborid.getSelectionModel().getSelectedItem();
        if (selectedLaborID != null) {
            LaborDto laborDto = laborModel.findById(selectedLaborID);

            if (laborDto != null) {
                lbllaborname.setText(laborDto.getName());
            }
        }
    }

    public void refreshPage() throws SQLException {
        loadNextRetirementID();
        loadTableData();

        btndelete.setDisable(true);
        btnsave.setDisable(false);
        btnupdate.setDisable(true);

        comlaborid.getSelectionModel().clearSelection();
        comofficerid.getSelectionModel().clearSelection();
        lbllaborname.setText("");
        lblofficername.setText("");
        lbldate.setText(LocalDate.now().toString());
        txttyw.setText("");
        txtfpayment.setText("");

    }



    public void loadNextRetirementID() throws SQLException {
        String nextRetirementID = retirementModel.getNextRetirementID();
        lblid.setText(nextRetirementID);
    }
    private void loadLaborIDs() throws SQLException {
        ArrayList<String> laborIDs = laborModel.getAllLaborIDs();
        comlaborid.getItems().addAll(laborIDs);
    }

    private void loadOfficerIDs() throws SQLException {
        ArrayList<String> leaveIDs = factoryOfficerModel.getAllOfficerIds();
        comofficerid.getItems().addAll(leaveIDs);
    }
    @FXML
    void SaveOnAction(ActionEvent event) {
        try {
            lbldate.setText(LocalDate.now().toString());
            String RetirementID = lblid.getText();
            String LaborID = comlaborid.getValue();
            String Name = lbllaborname.getText();
            String OfficerID = comofficerid.getValue();
            Date RetirementDate =  Date.valueOf(lbldate.getText());
            int TotalYearsWorked = Integer.parseInt(txttyw.getText());
            double FundPayment = Double.parseDouble(txtfpayment.getText());

            RetirementDto retirementDto = new RetirementDto(
                    RetirementID,
                    LaborID,
                    Name,
                    OfficerID,
                    RetirementDate,
                    TotalYearsWorked,
                    FundPayment
            );

            boolean isSaved = retirementModel.saveRetirement(retirementDto);
            if (isSaved) {
                refreshPage();
                 loadTableData();
                new Alert(Alert.AlertType.INFORMATION, "Retirement saved...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save Retirement...!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadTableData() throws SQLException {
        ArrayList<RetirementDto> retirementDtos = retirementModel.getAllRetirements();
        ObservableList<RetirementTM> retirementTMS = FXCollections.observableArrayList();

        for (RetirementDto retirementDto : retirementDtos) {
            RetirementTM retirementTM = new RetirementTM(
                    retirementDto.getRetirementID(),
                    retirementDto.getLaborID(),
                    retirementDto.getName(),
                    retirementDto.getOfficerID(),
                    (Date.valueOf(String.valueOf(retirementDto.getRetirementDate()))),
                    retirementDto.getTotalYearsWorked(),
                    retirementDto.getFundPayment()

            );
            retirementTMS.add(retirementTM);
        }
        tableaddretirement.setItems(retirementTMS);
    }
    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        String RetirementID = lblid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = retirementModel.deleteRetirement(RetirementID);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Retirement deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Retirement...!").show();
            }
        }
    }

    @FXML
    void ResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }



    @FXML
    void TableOnClicked(MouseEvent event) {
        RetirementTM retirementTM = tableaddretirement.getSelectionModel().getSelectedItem();
        if (retirementTM != null) {
            lblid.setText(retirementTM.getRetirementID());
            comlaborid.setValue(retirementTM.getLaborID());
            lbllaborname.setText(retirementTM.getName());
            comofficerid.setValue(retirementTM.getOfficerID());
            lbldate.setText(retirementTM.getRetirementDate().toString());
            txttyw.setText(String.valueOf(retirementTM.getTotalYearsWorked()));
            txtfpayment.setText(String.valueOf(retirementTM.getFundPayment()));

            btnsave.setDisable(true);

            btndelete.setDisable(false);
            btnupdate.setDisable(false);
        }
    }

    @FXML
    void UpdateOnAction(ActionEvent event) throws SQLException {
        String RetirementID = lblid.getText();
        String LaborID = comlaborid.getValue();
        String Name = lbllaborname.getText();
        String OfficerID = comofficerid.getValue();
        Date RetirementDate =  Date.valueOf(lbldate.getText());
        int TotalYearsWorked = Integer.parseInt(txttyw.getText());
        double FundPayment = Double.parseDouble(txtfpayment.getText());

        RetirementDto retirementDto = new RetirementDto(
                RetirementID,
                LaborID,
                Name,
                OfficerID,
                RetirementDate,
                TotalYearsWorked,
                FundPayment
        );
        boolean isUpdate = retirementModel.updateRetirement(retirementDto);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Retirement update...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update Retirement...!").show();
        }
    }


}

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
import lk.ijse.gdse.finalproject.dto.AccidentDto;
import lk.ijse.gdse.finalproject.dto.FactoryOfficerDto;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.tm.AccidentTM;
import lk.ijse.gdse.finalproject.model.AccidentModel;
import lk.ijse.gdse.finalproject.model.FactoryOfficerModel;
import lk.ijse.gdse.finalproject.model.LaborModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccidentController implements Initializable {

    @FXML
    private AnchorPane AccidentPage;

    @FXML
    private TableView<AccidentTM> Accidenttable;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<AccidentTM,String> colacc;

    @FXML
    private TableColumn<AccidentTM, Date> coldate;

    @FXML
    private TableColumn<AccidentTM,String> coldes;

    @FXML
    private TableColumn<AccidentTM,String> collabor;

    @FXML
    private TableColumn<AccidentTM,String> colname;

    @FXML
    private TableColumn<AccidentTM,String> colofficer;

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
    private Button addinsurance;

    @FXML
    private TextField txtdes;
    LaborModel laborModel = new LaborModel();
    FactoryOfficerModel factoryOfficerModel = new FactoryOfficerModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbldate.setText(LocalDate.now().toString());
        colacc.setCellValueFactory(new PropertyValueFactory<>("AccidentID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colofficer.setCellValueFactory(new PropertyValueFactory<>("OfficerID"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("AccidentDate"));
        coldes.setCellValueFactory(new PropertyValueFactory<>("Description"));

        try {
            loadNextAccidentID();
            loadLaborIDs();
            loadOfficerIDs();
            loadTableData();
            refreshPage();;
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load  AccidentID").show();
        }
    }

    @FXML
    void ComboOnAction(ActionEvent event) throws SQLException {
        String selectedLaborID = String.valueOf(comlaborid.getSelectionModel().getSelectedItem());
        LaborDto laborDto = laborModel.findById(selectedLaborID);

        if (laborDto != null) {
            lbllaborname.setText(laborDto.getName());
        }
    }


    public void refreshPage() throws SQLException {
        loadNextAccidentID();
        loadTableData();

        btndelete.setDisable(true);
        btnsave.setDisable(false);
        btnupdate.setDisable(true);

        comlaborid.getSelectionModel().clearSelection();
        comofficerid.getSelectionModel().clearSelection();
        lbllaborname.setText("");
        lbldate.setText(LocalDate.now().toString());
        txtdes.setText("");

    }
    AccidentModel accidentModel = new AccidentModel();

    public void loadNextAccidentID() throws SQLException {
        String nextAccidentID = accidentModel.getNextAccidentID();
        lblid.setText(nextAccidentID);
    }
    @FXML
    void SaveOnAction(ActionEvent event) throws SQLException {
        lbldate.setText(LocalDate.now().toString());
        String AccidentID = lblid.getText();
        String LaborID = comlaborid.getValue();
        String Name = lbllaborname.getText();
        String OfficerID = comofficerid.getValue();
        Date AccidentDate =  Date.valueOf(lbldate.getText());
        String Description = txtdes.getText();


        txtdes.setStyle(txtdes.getStyle() + ";-fx-border-color:  #FFAD60;");


        String DescriptionPattern = "^[A-Za-z ]+$";
        boolean isValidDescription = Description.matches(DescriptionPattern);

        if (!isValidDescription) {
            txtdes.setStyle(txtdes.getStyle() + ";-fx-border-color: red;");
        }
        if (isValidDescription) {
            AccidentDto accidentDto = new AccidentDto(
                    AccidentID,
                    LaborID,
                    Name,
                    OfficerID,
                    AccidentDate,
                    Description
            );
            boolean isSaved = accidentModel.saveAccident(accidentDto);
            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Accident saved...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save Accident...!").show();
            }
        }
    }
    private void loadLaborIDs() throws SQLException {
        ArrayList<String> laborIDs = laborModel.getAllLaborIDs();
        comlaborid.getItems().addAll(laborIDs);
    }
    private void loadOfficerIDs() throws SQLException {
        ArrayList<String> OfficerIDs = factoryOfficerModel.getAllOfficerIds();
        comofficerid.getItems().addAll(OfficerIDs);
    }
    private void loadTableData() throws SQLException {
        ArrayList<AccidentDto> accidentDtos = accidentModel.getAllAccidents();
        ObservableList<AccidentTM> accidentTMS = FXCollections.observableArrayList();

        for(AccidentDto accidentDto : accidentDtos){
            AccidentTM accidentTM = new AccidentTM(
                    accidentDto.getAccidentID(),
                    accidentDto.getLaborID(),
                    accidentDto.getName(),
                    accidentDto.getOfficerID(),
                    (Date.valueOf(String.valueOf(accidentDto.getAccidentDate()))),
                    accidentDto.getDescription()


            );
            accidentTMS.add(accidentTM);
        }
        Accidenttable.setItems(accidentTMS);
    }
    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        String AccidentID = lblid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            boolean isDeleted = accidentModel.deleteAccident(AccidentID );
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Accident deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Accident...!").show();
            }
        }
    }

    @FXML
    void OnTableClicked(MouseEvent event) {
        AccidentTM accidentTM = Accidenttable.getSelectionModel().getSelectedItem();
        if(accidentTM != null){
            lblid.setText(accidentTM.getAccidentID());
            comlaborid.setValue(accidentTM.getLaborID());
            lbllaborname.setText(accidentTM.getName());
            comofficerid.setValue(accidentTM.getOfficerID());
            lblofficername.setText(accidentTM.getName());
            lbldate.setText(accidentTM.getAccidentDate().toString());
            txtdes.setText(accidentTM.getDescription());


            btnsave.setDisable(true);

            btndelete.setDisable(false);
            btnupdate.setDisable(false);
        }

    }
    @FXML
    void Combo1OnAction(ActionEvent event) throws SQLException {
        String selectedOfficerID = String.valueOf(comofficerid.getSelectionModel().getSelectedItem());
        FactoryOfficerDto factoryOfficerDto = factoryOfficerModel.findById(selectedOfficerID);

        if (factoryOfficerDto != null) {
            lblofficername.setText(factoryOfficerDto.getName());
        }
    }

    @FXML
    void ReportOnAction(ActionEvent event) {

    }

    @FXML
    void ResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }



    @FXML
    void UpdateOnAction(ActionEvent event) throws SQLException {
        String AccidentID = lblid.getText();
        String LaborID = comlaborid.getValue();
        String OfficerID = comofficerid.getValue();
        Date AccidentDate =  Date.valueOf(lbldate.getText());
        String Description = txtdes.getText();

        AccidentDto accidentDto = new AccidentDto(
                AccidentID,
                LaborID,
                lbllaborname.getText(),
                OfficerID,
                AccidentDate,
                Description

        );
        boolean isUpdate = AccidentModel.updateAccident(accidentDto);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Accident update...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update Accident...!").show();
        }
    }
    @FXML
    void AddInsuranceOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddInsurance.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("Add Union_Members");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = addinsurance.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }
    @FXML
    void GenerateAccidentOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/report/AccidentReport.jrxml"
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

package lk.ijse.gdse.finalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.FactoryOfficerDto;
import lk.ijse.gdse.finalproject.dto.SupervisorDto;
import lk.ijse.gdse.finalproject.dto.tm.FactoryOfficerTM;
import lk.ijse.gdse.finalproject.dto.tm.SupervisorTM;
import lk.ijse.gdse.finalproject.model.FactoryOfficerModel;
import lk.ijse.gdse.finalproject.model.SupervisorModel;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class FactoryOfficerController implements Initializable {

    @FXML
    private AnchorPane Officerpage;
    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<FactoryOfficerTM,String> colname;

    @FXML
    private TableColumn<FactoryOfficerTM,String> colofficer;

    @FXML
    private Label lblid;

    @FXML
    private TableView<FactoryOfficerTM> officertable;

    @FXML
    private TextField txtname;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colofficer.setCellValueFactory(new PropertyValueFactory<>("OfficerID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));

        try{
            loadNextOfficerID();
            refreshPage();
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load FactoryOfficer").show();
        }
    }
    private void refreshPage() throws SQLException {
        loadNextOfficerID();
        loadTableData();

        btnsave.setDisable(false);

        btnupdate.setDisable(true);
        btndelete.setDisable(true);

        txtname.setText("");

    }

    FactoryOfficerModel factoryOfficerModel = new FactoryOfficerModel();

    private void loadTableData() throws SQLException {
        ArrayList<FactoryOfficerDto> factoryOfficerDtos = factoryOfficerModel.getAllFactoryOfficers();

        ObservableList<FactoryOfficerTM> factoryOfficerTMS = FXCollections.observableArrayList();

        for (FactoryOfficerDto factoryOfficerDto : factoryOfficerDtos) {
            FactoryOfficerTM factoryOfficerTM = new FactoryOfficerTM(
                    factoryOfficerDto.getOfficerID(),
                    factoryOfficerDto.getName()

            );
            factoryOfficerTMS.add(factoryOfficerTM);
        }

        officertable.setItems(factoryOfficerTMS);
    }
    public void loadNextOfficerID() throws SQLException {
        String nextOfficerID = factoryOfficerModel.getNextOfficerID();
        lblid.setText(nextOfficerID);
    }
    @FXML
    void SaveOnAction(ActionEvent event) throws SQLException {
        String OfficerID = lblid.getText();
        String Name = txtname.getText();

        FactoryOfficerDto factoryOfficerDto = new FactoryOfficerDto(OfficerID,Name);

        boolean isSaved =  factoryOfficerModel.SaveOfficer(factoryOfficerDto);
        if(isSaved){
            loadNextOfficerID();
            refreshPage();
            txtname.setText("");

            new Alert(Alert.AlertType.INFORMATION,"Officer saved...!").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Fail to save Officer...!").show();
        }
    }

    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        String OfficerID = lblid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            boolean isDeleted = factoryOfficerModel.deleteOfficer(OfficerID);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Officer deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Officer...!").show();
            }
        }
    }

    @FXML
    void OnTablecliked(MouseEvent event) {
        FactoryOfficerTM factoryOfficerTM = officertable.getSelectionModel().getSelectedItem();
        if (factoryOfficerTM != null){
            lblid.setText(factoryOfficerTM.getOfficerID());
            txtname.setText(factoryOfficerTM.getName());


            btnsave.setDisable(true);

            btndelete.setDisable(false);
            btnupdate.setDisable(false);
        }
    }

    @FXML
    void ResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }



    @FXML
    void UpdateOnAction(ActionEvent event) throws SQLException {
        String OfficerID = lblid.getText();
        String name = txtname.getText();


        FactoryOfficerDto factoryOfficerDto = new FactoryOfficerDto(
                OfficerID,
                name

        );
        boolean isUpdate = factoryOfficerModel.updateOfficer(factoryOfficerDto);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Customer update...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update customer...!").show();
        }
    }
    public int getTotalLabors() throws SQLException {
        String query = "SELECT COUNT(*) FROM Labor";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1); // Get the total count
        }

        return 0; // If no records found, return 0
    }

}

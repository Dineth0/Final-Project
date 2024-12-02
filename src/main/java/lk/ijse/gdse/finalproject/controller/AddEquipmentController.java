package lk.ijse.gdse.finalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.finalproject.dto.AddEquipmentDto;

import lk.ijse.gdse.finalproject.dto.tm.AddEquipmentTM;

import lk.ijse.gdse.finalproject.model.AddEquipmentModel;


import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddEquipmentController implements Initializable {

    @FXML
    private TableView<AddEquipmentTM> Equipmenttable;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<AddEquipmentTM,String> colequip;

    @FXML
    private TableColumn<AddEquipmentTM,String> colequipname;

    @FXML
    private Label lblid;

    @FXML
    private TextField txtname;

    AddEquipmentModel addEquipmentModel = new AddEquipmentModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colequip.setCellValueFactory(new PropertyValueFactory<>("EquipmentID"));
        colequipname.setCellValueFactory(new PropertyValueFactory<>("EquipmentName"));

        try {
            loadNextEquipmenID();
            loadTableData();
            refreshPage();;


        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load EquipmentID").show();
        }
    }
    public void refreshPage() throws SQLException {
        loadNextEquipmenID();
        loadTableData();

        btndelete.setDisable(true);
        btnsave.setDisable(false);
        btnupdate.setDisable(true);


        txtname.setText("");

    }

    public void loadNextEquipmenID() throws SQLException {
        String nextEquipmenID = addEquipmentModel.getNextEquipmentID();
        lblid.setText(nextEquipmenID);
    }
    @FXML
    void SaveOnAction(ActionEvent event) throws SQLException {

        String EquipmentID = lblid.getText();

        String EquipmentName = txtname.getText();


        AddEquipmentDto addEquipmentDto = new AddEquipmentDto(
                EquipmentID,
                EquipmentName
        );
        boolean isSaved = addEquipmentModel.saveEquipment(addEquipmentDto);
        if(isSaved){
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION,"Equipment saved...!").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Fail to save Equipment...!").show();
        }
    }
    private void loadTableData() throws SQLException {
        ArrayList<AddEquipmentDto> addEquipmentDtos = addEquipmentModel.getAllEquipments();
        ObservableList<AddEquipmentTM> addEquipmentTMS = FXCollections.observableArrayList();

        for(AddEquipmentDto addEquipmentDto : addEquipmentDtos){
            AddEquipmentTM addEquipmentTM = new AddEquipmentTM(
                    addEquipmentDto.getEquipmentID(),
                    addEquipmentDto.getEquipmentName()


            );
            addEquipmentTMS.add(addEquipmentTM);
        }
        Equipmenttable.setItems(addEquipmentTMS);
    }
    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        String EquipmentID = lblid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            boolean isDeleted = addEquipmentModel.deleteEquipment(EquipmentID );
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Equipment deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Equipment...!").show();
            }
        }
    }

    @FXML
    void OnTableClicked(MouseEvent event) {
        AddEquipmentTM addEquipmentTM = Equipmenttable.getSelectionModel().getSelectedItem();
        if(addEquipmentTM != null){
            lblid.setText(addEquipmentTM.getEquipmentID());
            txtname.setText(addEquipmentTM.getEquipmentName());


            btnsave.setDisable(true);

            btndelete.setDisable(false);
            btnupdate.setDisable(false);
        }
    }


    @FXML
    void UpdateOnAction(ActionEvent event) throws SQLException {
        String EquipmentID = lblid.getText();
        String EquipmentName = txtname.getText();


        AddEquipmentDto addEquipmentDto = new AddEquipmentDto(
                EquipmentID,
                EquipmentName

        );
        boolean isUpdate = AddEquipmentModel.updateEquipment(addEquipmentDto);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Equipment update...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update Equipment...!").show();
        }
    }

    @FXML
    void restOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }


}

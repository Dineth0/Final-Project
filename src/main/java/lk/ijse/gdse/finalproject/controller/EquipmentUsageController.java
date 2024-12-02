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
import lk.ijse.gdse.finalproject.dto.EquipmentUsageDto;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.SupervisorDto;
import lk.ijse.gdse.finalproject.dto.tm.EquipmentUsageTM;
import lk.ijse.gdse.finalproject.model.AddEquipmentModel;
import lk.ijse.gdse.finalproject.model.EquipmentUsageModel;
import lk.ijse.gdse.finalproject.model.LaborModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EquipmentUsageController implements Initializable {

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<EquipmentUsageTM, Date> coldate;

    @FXML
    private TableColumn<EquipmentUsageTM,String> colequip;

    @FXML
    private TableColumn<EquipmentUsageTM,String> colequipname;

    @FXML
    private TableColumn<EquipmentUsageTM,String> collabor;

    @FXML
    private TableColumn<EquipmentUsageTM,String> colname;

    @FXML
    private ComboBox<String> comequipid;

    @FXML
    private ComboBox<String> comlaborid;

    @FXML
    private Label lbldate;

    @FXML
    private Label lblequipname;

    @FXML
    private Label lbllaborname;

    @FXML
    private TableView<EquipmentUsageTM> useequipmentable;

    LaborModel laborModel = new LaborModel();
    AddEquipmentModel addEquipmentModel = new AddEquipmentModel();
    EquipmentUsageModel equipmentUsageModel  = new EquipmentUsageModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbldate.setText(LocalDate.now().toString());
        colequip.setCellValueFactory(new PropertyValueFactory<>("EquipmentID"));
        colequipname.setCellValueFactory(new PropertyValueFactory<>("EquipmentName"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("LaborName"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("UseDate"));

        try {

            loadLaborIDs();
            loadEquipmentIDs();
            loadTableData();
            refreshPage();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load EquipmentID").show();
        }

    }

    @FXML
    void Combo1OnAction(ActionEvent event) throws SQLException {
        String selectedLaborID = comlaborid.getSelectionModel().getSelectedItem();
        LaborDto laborDto = laborModel.findById(selectedLaborID);

        if (laborDto != null) {
            lbllaborname.setText(laborDto.getName());
        }
    }

    @FXML
    void ComboOnAction(ActionEvent event) throws SQLException {
        String selectedEquipmentID = comequipid.getSelectionModel().getSelectedItem();
        AddEquipmentDto addEquipmentDto = addEquipmentModel.findById(selectedEquipmentID);

        if (addEquipmentDto != null) {
            lblequipname.setText(addEquipmentDto.getEquipmentName());
        }
    }
    public void refreshPage() throws SQLException {

        loadTableData();

        btndelete.setDisable(true);
        btnsave.setDisable(false);
        btnupdate.setDisable(true);

        comequipid.getSelectionModel().clearSelection();
        lblequipname.setText("");
        comlaborid.getSelectionModel().clearSelection();
        lbllaborname.setText("");
        lbldate.setText(LocalDate.now().toString());


    }
    @FXML
    void SaveOnAction(ActionEvent event) throws SQLException {
        lbldate.setText(LocalDate.now().toString());
        String EquipmentID = comequipid.getValue();
        String EquipmentName = lblequipname.getText();
        String LaborID = comlaborid.getValue();
        String Name = lbllaborname.getText();
        Date UseDate =  Date.valueOf(lbldate.getText());


        EquipmentUsageDto equipmentUsageDto = new EquipmentUsageDto(
                EquipmentID,
                EquipmentName,
                LaborID,
                Name,
                UseDate
        );
        boolean isSaved = equipmentUsageModel.saveUsage(equipmentUsageDto);
        if(isSaved){
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION,"Equipment Usage saved...!").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Fail to save Equipment Usage...!").show();
        }
    }
    private void loadLaborIDs() throws SQLException {
        ArrayList<String> laborIDs = laborModel.getAllLaborIDs();
        comlaborid.getItems().addAll(laborIDs);
    }
    private void loadEquipmentIDs() throws SQLException {
        ArrayList<String> equipmentIDs = addEquipmentModel. getAllAddEquipmentIds();
        comequipid.getItems().addAll(equipmentIDs );
    }
    private void loadTableData() throws SQLException {
        ArrayList<EquipmentUsageDto> equipmentUsageDtos = equipmentUsageModel.getAllUsages();
        ObservableList<EquipmentUsageTM> equipmentUsageTMS = FXCollections.observableArrayList();

        for(EquipmentUsageDto equipmentUsageDto : equipmentUsageDtos){
            EquipmentUsageTM equipmentUsageTM = new EquipmentUsageTM(
                    equipmentUsageDto.getEquipmentID(),
                    equipmentUsageDto.getEquipmentName(),
                    equipmentUsageDto.getLaborID(),
                    equipmentUsageDto.getLaborName(),
                    (Date.valueOf(String.valueOf(equipmentUsageDto.getUseDate())))



            );
            equipmentUsageTMS.add(equipmentUsageTM);
        }
        useequipmentable.setItems(equipmentUsageTMS);
    }
    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        Date UseDate =  Date.valueOf(lbldate.getText());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            boolean isDeleted = equipmentUsageModel.deleteEquipment(UseDate );
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Usage deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Usage...!").show();
            }
        }
    }

    

    @FXML
    void OnTableClicked(MouseEvent event) {
        EquipmentUsageTM equipmentUsageTM = useequipmentable.getSelectionModel().getSelectedItem();
        if(equipmentUsageTM != null){
            comequipid.setValue(equipmentUsageTM.getEquipmentID());
            lblequipname.setText(equipmentUsageTM.getEquipmentName());
            comlaborid.setValue(equipmentUsageTM.getLaborID());
            lbllaborname.setText(equipmentUsageTM.getLaborName());
            lbldate.setText(equipmentUsageTM.getUseDate().toString());



            btnsave.setDisable(true);

            btndelete.setDisable(false);
            btnupdate.setDisable(false);
        }
    }



    @FXML
    void UpdateOnAction(ActionEvent event) throws SQLException {
        String EquipmentID = comequipid.getValue();
        String LaborID = comlaborid.getValue();
        Date UseDate =  Date.valueOf(lbldate.getText());



        EquipmentUsageDto equipmentUsageDto = new EquipmentUsageDto(
                EquipmentID,
                lblequipname.getText(),
                LaborID,
                lbllaborname.getText(),
                UseDate

        );
        boolean isUpdate = equipmentUsageModel.updateUsage(equipmentUsageDto);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Usage update...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update Usage...!").show();
        }
    }

    @FXML
    void restOnAction(ActionEvent event) {

    }

}

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
import lk.ijse.gdse.finalproject.dto.TrainingDto;
import lk.ijse.gdse.finalproject.dto.tm.TrainingTM;
import lk.ijse.gdse.finalproject.model.FactoryOfficerModel;
import lk.ijse.gdse.finalproject.model.LaborModel;
import lk.ijse.gdse.finalproject.model.TrainingModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddTrainingController implements Initializable {

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<TrainingTM, Date> coldate;

    @FXML
    private TableColumn<TrainingTM,String> coldes;

    @FXML
    private TableColumn<TrainingTM,String> collabor;

    @FXML
    private TableColumn<TrainingTM,String> colofficer;

    @FXML
    private TableColumn<TrainingTM,String> coltraining;

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
    private TableView<TrainingTM> tableaddtraining;

    @FXML
    private TextField txtdes;

    LaborModel laborModel = new LaborModel();
    FactoryOfficerModel factoryOfficerModel = new FactoryOfficerModel();
    TrainingModel tmModel = new TrainingModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbldate.setText(LocalDate.now().toString());
        coltraining.setCellValueFactory(new PropertyValueFactory<>("TrainingID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        //colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colofficer.setCellValueFactory(new PropertyValueFactory<>("OfficerID"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("Description"));
        coldes.setCellValueFactory(new PropertyValueFactory<>("TrainingDate"));


        try {
            loadNextTrainingID();
            loadLaborIDs();
            loadOfficerIDs();
            loadTableData();
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load TrainingID").show();
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
        String selectedLaborID = comlaborid.getSelectionModel().getSelectedItem();
        LaborDto laborDto = laborModel.findById(selectedLaborID);

        if (laborDto != null) {
            lbllaborname.setText(laborDto.getName());
        }
    }
    public void refreshPage() throws SQLException {
        loadNextTrainingID();
        loadTableData();

        btndelete.setDisable(true);
        btnsave.setDisable(false);
        btnupdate.setDisable(true);

        comlaborid.getSelectionModel().clearSelection();
        comofficerid.getSelectionModel().clearSelection();
        lbllaborname.setText("");
        lblofficername.setText("");
        txtdes.setText("");
        lbldate.setText(LocalDate.now().toString());

    }
    TrainingModel trainingModel = new TrainingModel();

    public void loadNextTrainingID() throws SQLException {
        String nextTrainingID = trainingModel.getNextTrainingID();
        lblid.setText(nextTrainingID);
    }
    private void loadLaborIDs() throws SQLException {
        ArrayList<String> laborIDs = laborModel.getAllLaborIDs();
        comlaborid.getItems().addAll(laborIDs);
    }
    private void loadOfficerIDs() throws SQLException {
        ArrayList<String> leaveIDs = factoryOfficerModel. getAllOfficerIds();
        comofficerid.getItems().addAll(leaveIDs );
    }
    @FXML
    void SaveOnAction(ActionEvent event) throws SQLException {
        lbldate.setText(LocalDate.now().toString());
        String TrainingID = lblid.getText();
        String LaborID = comlaborid.getValue();
        //String Name = lbllaborname.getText();
        String OfficerID = comofficerid.getValue();
        String Description = txtdes.getText();
        Date TrainingDate =  Date.valueOf(lbldate.getText());


        TrainingDto trainingDto = new TrainingDto(
                TrainingID,
                LaborID,
                OfficerID,
                Description,
                TrainingDate

        );
        boolean isSaved = trainingModel.saveTraining(trainingDto);
        if(isSaved){
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION,"Training saved...!").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Fail to save Training...!").show();
        }
    }
    private void loadTableData() throws SQLException {
        ArrayList<TrainingDto> trainingDtos = trainingModel.getAllTrainings();
        ObservableList<TrainingTM> trainingTMS = FXCollections.observableArrayList();

        for(TrainingDto trainingDto : trainingDtos){
            TrainingTM trainingTM = new TrainingTM(
                    trainingDto.getTrainingID(),
                    trainingDto.getLaborID(),
                    //trainingDto.getName(),
                    trainingDto.getOfficerID(),
                    trainingDto.getDescription(),
                    (Date.valueOf(String.valueOf(trainingDto.getTrainingDate())))


            );
            trainingTMS.add(trainingTM);
        }
        tableaddtraining.setItems(trainingTMS);
    }
    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        String TrainingID = lblid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            boolean isDeleted = trainingModel.deleteTraining(TrainingID );
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Training deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Training...!").show();
            }
        }
    }

    @FXML
    void OnTableClicked(MouseEvent event) {
        TrainingTM trainingTM = tableaddtraining.getSelectionModel().getSelectedItem();
        if(trainingTM != null){
            lblid.setText(trainingTM.getTrainingID());
            comlaborid.setValue(trainingTM.getLaborID());
            //lbllaborname.setText(trainingTM.getName());
            comofficerid.setValue(trainingTM.getOfficerID());
            txtdes.setText(trainingTM.getDescription());
            lbldate.setText(trainingTM.getTrainingDate().toString());


            btnsave.setDisable(true);

            btndelete.setDisable(false);
            btnupdate.setDisable(false);
        }
    }

    @FXML
    void ResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();;
    }


    @FXML
    void UpdateOnAction(ActionEvent event) throws SQLException {
        String TrainingID = lblid.getText();
        String LaborID = comlaborid.getValue();
        String OfficerID = comofficerid.getValue();
        String Description = txtdes.getText();
        Date TrainingDate =  Date.valueOf(lbldate.getText());


        TrainingDto trainingDto = new TrainingDto(
                TrainingID,
                LaborID,
                OfficerID,
                Description,
                TrainingDate
        );
        boolean isUpdate = TrainingModel.updateTraining(trainingDto);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Training update...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update Training...!").show();
        }
    }


}


package lk.ijse.gdse.finalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.ShiftDto;
import lk.ijse.gdse.finalproject.dto.SupervisorDto;
import lk.ijse.gdse.finalproject.dto.tm.ShiftTM;
import lk.ijse.gdse.finalproject.model.LaborModel;
import lk.ijse.gdse.finalproject.model.ShiftModel;
import lk.ijse.gdse.finalproject.model.SupervisorModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddShiftController implements Initializable {

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<ShiftTM, Date> coldate;

    @FXML
    private TableColumn<ShiftTM,String> colend;

    @FXML
    private TableColumn<ShiftTM,String> collabor;

    @FXML
    private TableColumn<ShiftTM,String> colover;

    @FXML
    private TableColumn<ShiftTM,String> colshift;

    @FXML
    private TableColumn<ShiftTM,String> colstart;

    @FXML
    private TableColumn<ShiftTM,String> colsup;

    @FXML
    private ComboBox<String> comlaborid;

    @FXML
    private ComboBox<String> comsupid;

    @FXML
    private Label lbldate;

    @FXML
    private Label lblid;

    @FXML
    private Label lbllaborname;

    @FXML
    private Label lblsupname;

    @FXML
    private TableView<ShiftTM> tableaddshift;

    @FXML
    private TextField txtend;

    @FXML
    private TextField txtover;

    @FXML
    private TextField txtstrat;

    LaborModel laborModel = new LaborModel();
    SupervisorModel supervisorModel = new SupervisorModel();
    ShiftModel shiftModel = new ShiftModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbldate.setText(LocalDate.now().toString());
        colshift.setCellValueFactory(new PropertyValueFactory<>("ShiftID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colsup.setCellValueFactory(new PropertyValueFactory<>("SupervisorID"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("ShiftDate"));
        colstart.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        colend.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        colover.setCellValueFactory(new PropertyValueFactory<>("OverTime"));

        try {
            loadNextShiftID();
            loadLaborIDs();
            loadSupervisorIDs();
            loadTableData();
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load  ShiftID").show();
        }
    }

    @FXML
    void Combo1OnActon(ActionEvent event) throws SQLException {
        String selectedSupervisorID = comsupid.getSelectionModel().getSelectedItem();
        SupervisorDto supervisorDto = supervisorModel.findById(selectedSupervisorID);

        if (supervisorDto != null) {
            lblsupname.setText(supervisorDto.getName());
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
    private void loadLaborIDs() throws SQLException {
        ArrayList<String> laborIDs = laborModel.getAllLaborIDs();
        comlaborid.getItems().addAll(laborIDs);
    }

    private void loadSupervisorIDs() throws SQLException {
        ArrayList<String> supervisorIDs = supervisorModel. getAllSupervisorIds();
        comsupid.getItems().addAll(supervisorIDs );
    }
    private void refreshPage() throws SQLException {
        loadNextShiftID();
        loadTableData();

        btndelete.setDisable(true);
        btnsave.setDisable(false);
        btnupdate.setDisable(true);

        comlaborid.getSelectionModel().clearSelection();
        comsupid.getSelectionModel().clearSelection();
        lbllaborname.setText("");
        lblsupname.setText("");
        lbldate.setText(LocalDate.now().toString());
        txtstrat.setText("");
        txtend.setText("");
        txtover.setText("");
    }

    public void loadNextShiftID() throws SQLException {
        String nextShiftID = shiftModel.getNextShiftID();
        lblid.setText(nextShiftID);
    }
    @FXML
    void SaveONAction(ActionEvent event) throws SQLException {
        lbldate.setText(LocalDate.now().toString());
        String ShiftID = lblid.getText();
        String LaborID = comlaborid.getValue();
        String SupervisorID = comsupid.getValue();
        Date ShiftDate =  Date.valueOf(lbldate.getText());
        String StartTime = txtstrat.getText();
        String EndTime =  txtend.getText();
        String OverTime = txtover.getText();

        txtstrat.setStyle(txtstrat.getStyle() + ";-fx-border-color:   #343a40;");
        txtend.setStyle(txtend.getStyle() + ";-fx-border-color:   #343a40;");
        txtover.setStyle(txtover.getStyle() + ";-fx-border-color:   #343a40;");

        String StartTimePattern= "^([0-1]?[0-9]|2[0-3]):[0-5][0-9] (AM|PM)$";
        String EndTimePattern = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9] (AM|PM)$";
        String OverTimePattern = "^([1-9][0-9]* hour(s)?( [1-5]?[0-9] minute(s)?)?|[1-5]?[0-9] minute(s)?)$";
        boolean isValidStartTime = StartTime.matches(StartTimePattern);
        boolean isValidEndTime = EndTime.matches(EndTimePattern);
        boolean isValidOverTime = OverTime.matches(OverTimePattern);

        if (!isValidStartTime) {
            txtstrat.setStyle(txtstrat.getStyle() + ";-fx-border-color: red;");

        }
        if (!isValidEndTime) {
            txtend.setStyle(txtend.getStyle() + ";-fx-border-color: red;");

        }
        if (!isValidOverTime) {
            txtover.setStyle(txtover.getStyle() + ";-fx-border-color: red;");

        }
        if (isValidStartTime && isValidEndTime && isValidOverTime ) {
            ShiftDto shiftDto = new ShiftDto(
                    ShiftID,
                    LaborID,
                    SupervisorID,
                    ShiftDate,
                    StartTime,
                    EndTime,
                    OverTime);
            boolean isSaved = shiftModel.saveShift(shiftDto);
            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, " Shift saved...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save  Shift...!").show();
            }
        }
    }
    private void loadTableData() throws SQLException {
        ArrayList<ShiftDto> shiftDtos = shiftModel.getAllShifts();

        ObservableList<ShiftTM> shiftTMS = FXCollections.observableArrayList();

        for (ShiftDto shiftDto : shiftDtos) {
            ShiftTM shiftTM = new ShiftTM(
                    shiftDto.getShiftID(),
                    shiftDto.getLaborID(),
                    shiftDto.getSupervisorID(),
                    (Date.valueOf(shiftDto.getShiftDate().toLocalDate())),
                    shiftDto.getStartTime(),
                    shiftDto.getEndTime(),
                    shiftDto.getOverTime()
            );
            shiftTMS.add(shiftTM);
        }

        tableaddshift.setItems(shiftTMS);
    }

    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        String ShiftID = lblid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            boolean isDeleted = shiftModel.deleteShift(ShiftID );
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, " Shift deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete  Shift...!").show();
            }
        }
    }

    @FXML
    void OnClikedTable(MouseEvent event) {
        ShiftTM shiftTM = tableaddshift.getSelectionModel().getSelectedItem();
        if (shiftTM != null){
            lblid.setText(shiftTM.getShiftID());
            comlaborid.setValue(shiftTM.getLaborID());
            comsupid.setValue(shiftTM.getSupervisorID());
            lbldate.setText(shiftTM.getShiftDate().toString());
            txtstrat.setText(shiftTM.getStartTime());
            txtend.setText(shiftTM.getEndTime());
            txtover.setText(shiftTM.getOverTime());


            btnsave.setDisable(true);

            btndelete.setDisable(false);
            btnupdate.setDisable(false);
        }
    }


    @FXML
    void UpdateOnAction(ActionEvent event) throws SQLException {
        String ShiftID = lblid.getText();
        String LaborID = comlaborid.getValue();
        String SupervisorID = comsupid.getValue();
        Date ShiftDate =  Date.valueOf(lbldate.getText());
        String StartTime = txtstrat.getText();
        String EndTime = txtend.getText();
        String OverTime = txtover.getText();


            ShiftDto shiftDto = new ShiftDto(
                    ShiftID,
                    LaborID,
                    SupervisorID,
                    ShiftDate,
                    StartTime,
                    EndTime,
                    OverTime
            );

            boolean isUpdate = shiftModel.updateShift(shiftDto);
            if (isUpdate) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, " Shift update...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update  Shift...!").show();
            }

    }

    @FXML
    void restOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

}

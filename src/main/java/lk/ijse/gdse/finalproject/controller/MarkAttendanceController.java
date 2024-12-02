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
import lk.ijse.gdse.finalproject.dto.AttendanceDto;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.SupervisorDto;
import lk.ijse.gdse.finalproject.dto.tm.AttendanceTM;
import lk.ijse.gdse.finalproject.model.AttendanceModel;
import lk.ijse.gdse.finalproject.model.LaborModel;
import lk.ijse.gdse.finalproject.model.SupervisorModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MarkAttendanceController implements Initializable {

    @FXML
    private AnchorPane AttendancePage;

    @FXML
    private TableView<AttendanceTM> Attendancetable;;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<AttendanceTM,String> colLid;

    @FXML
    private TableColumn<AttendanceTM,String> colSid;

    @FXML
    private TableColumn<AttendanceTM, Date> coldate;

    @FXML
    private TableColumn<AttendanceTM,String> colid;

    @FXML
    private TableColumn<AttendanceTM,String> colstatus;

    @FXML
    private ComboBox<String> comlaborId;

    @FXML
    private ComboBox<String> comsupId;



    @FXML
    private Label lbldate;

    @FXML
    private Label lblid;

    @FXML
    private Label lbllaborname;

    @FXML
    private Label lblsupname;

    @FXML
    private ChoiceBox<String> statusbox;
    private final String[] Status = {"Present", "Absent"};

    LaborModel laborModel = new LaborModel();
    SupervisorModel supervisorModel = new SupervisorModel();
    AttendanceModel attendanceModel = new AttendanceModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusbox.getItems().addAll(Status);
        lbldate.setText(LocalDate.now().toString());

        colid.setCellValueFactory(new PropertyValueFactory<>("AttendanceID"));
        colLid.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colSid.setCellValueFactory(new PropertyValueFactory<>("SupervisorID"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("AttendDate"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        try {
            loadNextAttendanceID();
            loadLaborIDs();
            loadSupervisorIDs();
            loadTableData();
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load  AttendanceID").show();
        }

    }

    @FXML
    void Combo1OnAction(ActionEvent event) throws SQLException {
        String selectedSupervisorID = comsupId.getSelectionModel().getSelectedItem();
        SupervisorDto supervisorDto = supervisorModel.findById(selectedSupervisorID);

        if (supervisorDto != null) {
            lblsupname.setText(supervisorDto.getName());
        }
    }

    @FXML
    void ComboOnAction(ActionEvent event) throws SQLException {
        String selectedLaborID = comlaborId.getSelectionModel().getSelectedItem();
        LaborDto laborDto = laborModel.findById(selectedLaborID);

        if (laborDto != null) {
            lbllaborname.setText(laborDto.getName());
        }
    }
    @FXML
    void SaveOnAction(ActionEvent event) throws Exception {
        lbldate.setText(LocalDate.now().toString());
        String AttendanceID = lblid.getText();
        String LaborID = comlaborId.getValue();
        String SupervisorID = comsupId.getValue();
        Date AttendDate = Date.valueOf(lbldate.getText());
        String Status = statusbox.getValue();

        AttendanceDto attendanceDto = new AttendanceDto(
                AttendanceID,
                LaborID,
                SupervisorID,
                AttendDate,
                Status);
        boolean isSaved =  attendanceModel.saveAttendance(attendanceDto);
        if(isSaved){
            refreshPage();;
            new Alert(Alert.AlertType.INFORMATION," Attendance saved...!").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Fail to save  Attendance...!").show();
        }
    }
    private void refreshPage() throws Exception {
        loadNextAttendanceID();
        loadTableData();

        btndelete.setDisable(true);
        btnsave.setDisable(false);
        btnupdate.setDisable(true);

        comlaborId.getSelectionModel().clearSelection();
        comsupId.getSelectionModel().clearSelection();
        lbllaborname.setText("");
        lblsupname.setText("");
        lbldate.setText(LocalDate.now().toString());
        statusbox.setValue(null);
    }


    public void loadNextAttendanceID() throws Exception {
        String nextAttendanceID = attendanceModel.getNextAttendanceID();
        lblid.setText(nextAttendanceID);

    }
    private void loadLaborIDs() throws SQLException {
        ArrayList<String> laborIDs = laborModel.getAllLaborIDs();
        comlaborId.getItems().addAll(laborIDs);
    }

    private void loadSupervisorIDs() throws SQLException {
        ArrayList<String> supervisorIDs = supervisorModel. getAllSupervisorIds();
        comsupId.getItems().addAll(supervisorIDs );
    }

    private void loadTableData() throws SQLException {
        ArrayList<AttendanceDto> attendanceDtos = attendanceModel.getAllAttendances();

        ObservableList<AttendanceTM> attendanceTMS = FXCollections.observableArrayList();

        for (AttendanceDto attendanceDto : attendanceDtos) {
            AttendanceTM attendanceTM = new AttendanceTM(
                    attendanceDto.getAttendanceID(),
                    attendanceDto.getLaborID(),
                    attendanceDto.getSupervisorID(),
                    (Date.valueOf(String.valueOf(attendanceDto.getAttendDate()))),
                    attendanceDto.getStatus()
            );
            attendanceTMS.add(attendanceTM);
        }

        Attendancetable.setItems(attendanceTMS);
    }


    @FXML
    void DeleteOnAction(ActionEvent event) throws Exception {
        String AttendanceID = lblid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            boolean isDeleted = attendanceModel.deleteAttendance(AttendanceID);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, " Attendance deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete  Attendance...!").show();
            }
        }
    }

    @FXML
    void OnClickedTable(MouseEvent event) {
        AttendanceTM attendanceTM = Attendancetable.getSelectionModel().getSelectedItem();
        if (attendanceTM != null){
            lblid.setText(attendanceTM.getAttendanceID());
            comlaborId.setValue(attendanceTM.getLaborID());
            comsupId.setValue(attendanceTM.getSupervisorID());
            lbldate.setText(LocalDate.now().toString());
            statusbox.setValue(attendanceTM.getStatus());


            btnsave.setDisable(true);

            btndelete.setDisable(false);
            btnupdate.setDisable(false);
        }
    }



    @FXML
    void UpdateOnAction(ActionEvent event) throws Exception {
        String AttendanceID = lblid.getText();
        String LaborID = comlaborId.getValue();
        String SupervisorID = comsupId.getValue();
        Date AttendDate = Date.valueOf(lbldate.getText());
        String Status = statusbox.getValue();

        AttendanceDto attendanceDto = new AttendanceDto(
                AttendanceID,
                LaborID,
                SupervisorID,
                AttendDate,
                Status
        );

        boolean isUpdate = attendanceModel.updateAttendance(attendanceDto);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, " Attendance update...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update  Attendance...!").show();
        }
    }

    @FXML
    void restOnAction(ActionEvent event) throws Exception {
        refreshPage();
    }
    @FXML
    void GenerateCheckAttendReportOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/report/AttendanceCheckReport.jrxml"
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
//           e.printStackTrace();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "DB error...!").show();
        }
    }

}
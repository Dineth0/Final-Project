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
import lk.ijse.gdse.finalproject.dto.AttendanceDto;
import lk.ijse.gdse.finalproject.dto.tm.AttendanceTM;
import lk.ijse.gdse.finalproject.model.AttendanceModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SeeAttendancesController implements Initializable {

    @FXML
    private TableView<AttendanceTM> Attendancetable1;

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

    AttendanceModel attendanceModel = new AttendanceModel();

    @FXML
    void OnClickedTable(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colid.setCellValueFactory(new PropertyValueFactory<>("AttendanceID"));
        colLid.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colSid.setCellValueFactory(new PropertyValueFactory<>("SupervisorID"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("AttendDate"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        try {

            loadTableData();
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        Attendancetable1.setItems(attendanceTMS);
    }
    private void refreshPage() throws SQLException {

        loadTableData();
    }
}

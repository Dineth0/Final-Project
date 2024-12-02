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
import lk.ijse.gdse.finalproject.dto.LeaveDto;
import lk.ijse.gdse.finalproject.dto.tm.LeaveTM;
import lk.ijse.gdse.finalproject.model.LeaveModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SeeLeaveController implements Initializable {

    @FXML
    private TableColumn<LeaveTM, Date> coldate;

    @FXML
    private TableColumn<LeaveTM,String> collabor;

    @FXML
    private TableColumn<LeaveTM,String> colleave;

    @FXML
    private TableColumn<LeaveTM,String> colname;

    @FXML
    private TableColumn<LeaveTM,String> colofficer;

    @FXML
    private TableColumn<LeaveTM,String> colreason;

    @FXML
    private TableColumn<LeaveTM,String> colstatus;

    @FXML
    private TableView<LeaveTM> tableaddleave1;

    LeaveModel leaveModel = new LeaveModel();

    @FXML
    void OnTableClicked(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colleave.setCellValueFactory(new PropertyValueFactory<>("LeaveID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colofficer.setCellValueFactory(new PropertyValueFactory<>("OfficerID"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("LeaveDate"));
        colreason.setCellValueFactory(new PropertyValueFactory<>("Reason"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        try {
            refreshPage();;
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadTableData() throws SQLException {
        ArrayList<LeaveDto> leaveDtos = leaveModel.getAllLeaves();
        ObservableList<LeaveTM> leaveTMS = FXCollections.observableArrayList();

        for(LeaveDto leaveDto : leaveDtos){
            LeaveTM leaveTM = new LeaveTM(
                    leaveDto.getLeaveID(),
                    leaveDto.getLaborID(),
                    leaveDto.getName(),
                    leaveDto.getOfficerID(),
                    (Date.valueOf(String.valueOf(leaveDto.getLeaveDate()))),
                    leaveDto.getReason(),
                    leaveDto.getStatus()

            );
            leaveTMS.add(leaveTM);
        }
        tableaddleave1.setItems(leaveTMS);
    }
    private void refreshPage() throws SQLException {

        loadTableData();
    }
}

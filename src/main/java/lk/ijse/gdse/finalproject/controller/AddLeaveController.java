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
import lk.ijse.gdse.finalproject.dto.LeaveDto;
import lk.ijse.gdse.finalproject.dto.tm.LeaveTM;
import lk.ijse.gdse.finalproject.model.FactoryOfficerModel;
import lk.ijse.gdse.finalproject.model.LaborModel;
import lk.ijse.gdse.finalproject.model.LeaveModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddLeaveController implements Initializable {

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

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
    private ComboBox<String> combostatus;
    private final String[] Status = {"Approved","Rejected","Pending"};

    @FXML
    private ComboBox<String> comlaborid;

    @FXML
    private ComboBox<String> comofficerid;

    @FXML
    private DatePicker datepicker;

    @FXML
    private Label lbldate;

    @FXML
    private Label lblid;

    @FXML
    private Label lbllaborname;

    @FXML
    private Label lblofficername;

    @FXML
    private Label lblstatus;

    @FXML
    private TableView<LeaveTM> tableaddleave;

    @FXML
    private TextField txtreason;

    LaborModel laborModel = new LaborModel();
    FactoryOfficerModel factoryOfficerModel = new FactoryOfficerModel();
    LeaveModel leaveModel = new LeaveModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbldate.setText(LocalDate.now().toString());
        combostatus.getItems().addAll(Status);
        colleave.setCellValueFactory(new PropertyValueFactory<>("LeaveID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colofficer.setCellValueFactory(new PropertyValueFactory<>("OfficerID"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("LeaveDate"));
        colreason.setCellValueFactory(new PropertyValueFactory<>("Reason"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        try {
            loadNextLeaveID();
            loadLaborIDs();
            loadOfficerIDs();
            loadTableData();
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load LeaveID").show();
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
    void Combo2OnAction(ActionEvent event) {
        String SelectedValue = combostatus.getValue();
        lblstatus.setText(SelectedValue);
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
        loadNextLeaveID();
        loadTableData();

        btndelete.setDisable(true);
        btnsave.setDisable(false);
        btnupdate.setDisable(true);

        comlaborid.getSelectionModel().clearSelection();
        comofficerid.getSelectionModel().clearSelection();
        lbllaborname.setText("");
        lblofficername.setText("");
        lbldate.setText(LocalDate.now().toString());
        txtreason.setText("");
        combostatus.setValue(null);
    }


    public void loadNextLeaveID() throws SQLException {
        String nextLeaveID = leaveModel.getNextLeaveID();
        lblid.setText(nextLeaveID);
    }
    @FXML
    void SaveOnAction(ActionEvent event) throws SQLException {
        lbldate.setText(LocalDate.now().toString());
        String LeaveID = lblid.getText();
        String LaborID = comlaborid.getValue();
        String Name = lbllaborname.getText();
        String OfficerID = comofficerid.getValue();
        Date LeaveDate =  Date.valueOf(lbldate.getText());
        String Reason = txtreason.getText();
        String Status = combostatus.getValue();

        LeaveDto leaveDto = new LeaveDto(
                LeaveID,
                LaborID,
                Name,
                OfficerID,
                LeaveDate,
                Reason,
                Status
        );
        boolean isSaved = leaveModel.saveLeave(leaveDto);
        if(isSaved){
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION,"Leave saved...!").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Fail to save Leave...!").show();
        }
    }
    private void loadLaborIDs() throws SQLException {
        ArrayList<String> laborIDs = laborModel.getAllLaborIDs();
        comlaborid.getItems().addAll(laborIDs);
    }
    private void loadOfficerIDs() throws SQLException {
        ArrayList<String> leaveIDs = factoryOfficerModel. getAllOfficerIds();
        comofficerid.getItems().addAll(leaveIDs );
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
        tableaddleave.setItems(leaveTMS);
    }


    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        String LeaveID = lblid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            boolean isDeleted = leaveModel.deleteLeave(LeaveID );
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Leave deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Leave...!").show();
            }
        }
    }

    @FXML
    void OnTableClicked(MouseEvent event) {
        LeaveTM leaveTM = tableaddleave.getSelectionModel().getSelectedItem();
        if(leaveTM != null){
            lblid.setText(leaveTM.getLeaveID());
            comlaborid.setValue(leaveTM.getLaborID());
            lbllaborname.setText(leaveTM.getName());
            comofficerid.setValue(leaveTM.getOfficerID());
            lbldate.setText(leaveTM.getLeaveDate().toString());
            txtreason.setText(leaveTM.getReason());
            combostatus.setValue(leaveTM.getStatus());

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
        String LeaveID = lblid.getText();
        String LaborID = comlaborid.getValue();
        String OfficerID = comofficerid.getValue();
        Date LeaveDate =  Date.valueOf(lbldate.getText());
        String Reason = txtreason.getText();
        String Status = combostatus.getValue();

        LeaveDto leaveDto = new LeaveDto(
                LeaveID,
                LaborID,
                lbllaborname.getText(),
                OfficerID,
                LeaveDate,
                Reason,
                Status
        );
        boolean isUpdate = LeaveModel.updateLeave(leaveDto);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Leave update...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update Leave...!").show();
        }
    }

}

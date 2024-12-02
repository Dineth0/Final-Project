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
import lk.ijse.gdse.finalproject.dto.ComplaintDto;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.tm.ComplaintTM;
import lk.ijse.gdse.finalproject.model.ComplaintModel;
import lk.ijse.gdse.finalproject.model.LaborModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ComplaintController implements Initializable {

    @FXML
    private AnchorPane complaintPage;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<ComplaintTM,String> coldate;

    @FXML
    private TableColumn<ComplaintTM,String> coldes;

    @FXML
    private TableColumn<ComplaintTM,String> colcomplaint;

    @FXML
    private TableColumn<ComplaintTM,String> collabor;

    @FXML
    private TableColumn<ComplaintTM,String> colname;

    @FXML
    private TableColumn<ComplaintTM,String> colseen;

    @FXML
    private ComboBox<String> comlaborId;

    @FXML
    private ComboBox<String> comseen;
    private final String[] Manager_Seen = {"Yes","No"};

;

    @FXML
    private TableView<ComplaintTM> complainttable;

    @FXML
    private Label lbldate;

    @FXML
    private Label lblid;

    @FXML
    private Label lbllaborname;

    @FXML
    private Label lblseen;

    @FXML
    private TextField txtdes;

    LaborModel laborModel = new LaborModel();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbldate.setText(LocalDate.now().toString());
        comseen.getItems().addAll(Manager_Seen);
        colcomplaint.setCellValueFactory(new PropertyValueFactory<>("ComplaintID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        coldes.setCellValueFactory(new PropertyValueFactory<>("Description"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("ComplaintDate"));
        colseen.setCellValueFactory(new PropertyValueFactory<>("ManagerSeen"));


        try {
            loadNextComplaintID();
            loadLaborIDs();
            loadTableData();
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load ComplaintID").show();
        }
    }

    @FXML
    void Combo1OnAction(ActionEvent event) {
        String SelectedValue = comseen.getValue();
        lblseen.setText(SelectedValue);
    }

    @FXML
    void ComboOnAction(ActionEvent event) throws SQLException {
        String selectedLaborID = comlaborId.getSelectionModel().getSelectedItem();
        LaborDto laborDto = laborModel.findById(selectedLaborID);

        if (laborDto != null) {
            lbllaborname.setText(laborDto.getName());
        }
    }
    public void refreshPage() throws SQLException {
        loadNextComplaintID();
        loadTableData();

        btndelete.setDisable(true);
        btnsave.setDisable(false);
        btnupdate.setDisable(true);

        comlaborId.getSelectionModel().clearSelection();
        lbllaborname.setText("");
        txtdes.setText("");
        lbldate.setText(LocalDate.now().toString());
        comseen.setValue(null);
    }
    ComplaintModel complaintModel = new ComplaintModel();

    public void loadNextComplaintID() throws SQLException {
        String nextComplaintID = ComplaintModel.getNextComplaintID();
        lblid.setText(nextComplaintID);
    }
    @FXML
    void SaveONAction(ActionEvent event) throws SQLException {
        String ComplaintID = lblid.getText();
        String LaborID = comlaborId.getValue();
        String Name = lbllaborname.getText();
        String Description = txtdes.getText();
        Date ComplaintDate =  Date.valueOf(lbldate.getText());
        String Manager_Seen = comseen.getValue();

        ComplaintDto complaintDto = new ComplaintDto(
                ComplaintID,
                LaborID,
                Name,
                Description,
                ComplaintDate,
                Manager_Seen
        );
        boolean isSaved = complaintModel.saveComplaint(complaintDto);
        if(isSaved){
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION,"Complaint saved...!").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Fail to save Complaint...!").show();
        }
    }
    private void loadLaborIDs() throws SQLException {
        ArrayList<String> laborIDs = laborModel.getAllLaborIDs();
        comlaborId.getItems().addAll(laborIDs);
    }
    private void loadTableData() throws SQLException {
        ArrayList<ComplaintDto> complaintDtos = complaintModel.getAllComplaints();
        ObservableList<ComplaintTM> complaintTMS = FXCollections.observableArrayList();

        for(ComplaintDto complaintDto : complaintDtos){
            ComplaintTM complaintTM = new ComplaintTM(
                    complaintDto.getComplaintID(),
                    complaintDto.getLaborID(),
                    complaintDto.getName(),
                    complaintDto.getDescription(),
                    (Date.valueOf(String.valueOf(complaintDto.getComplaintDate()))),
                    complaintDto.getManager_Seen()


            );
            complaintTMS.add(complaintTM);
        }
        complainttable.setItems(complaintTMS);
    }
    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        String ComplaintID = lblid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            boolean isDeleted = complaintModel.deleteComplaint(ComplaintID );
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Complaint deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Complaint...!").show();
            }
        }
    }


    @FXML
    void TableOnClicked(MouseEvent event) {
        ComplaintTM complaintTM = complainttable.getSelectionModel().getSelectedItem();
        if(complaintTM != null){
            lblid.setText(complaintTM.getComplaintID());
            comlaborId.setValue(complaintTM.getLaborID());
            lbllaborname.setText(complaintTM.getName());
            txtdes.setText(complaintTM.getDescription());
            lbldate.setText(complaintTM.getComplaintDate().toString());
            comseen.setValue(complaintTM.getManagerSeen());

            btnsave.setDisable(false);

            btndelete.setDisable(false);
            btnupdate.setDisable(false);
        }
    }

    @FXML
    void UpdateOnAction(ActionEvent event) throws SQLException {
        String ComplaintID = lblid.getText();
        String LaborID = comlaborId.getValue();
        String Description = txtdes.getText();
        Date ComplaintDate =  Date.valueOf(lbldate.getText());
        String Manager_Seen = comseen.getValue();

        ComplaintDto complaintDto = new ComplaintDto(
                ComplaintID,
                LaborID,
                lbllaborname.getText(),
                Description,
                ComplaintDate,
                Manager_Seen
        );
        boolean isUpdate = ComplaintModel.updateComplaint(complaintDto);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Leave Complaint...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update Complaint...!").show();
        }
    }

    @FXML
    void restOnAction(ActionEvent event) {

    }


    public void GenerateComplaintReportOnAction(ActionEvent actionEvent) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/report/ComplaintReport.jrxml"
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

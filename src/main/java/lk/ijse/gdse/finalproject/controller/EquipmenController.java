package lk.ijse.gdse.finalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.gdse.finalproject.db.DBConnection;

import lk.ijse.gdse.finalproject.dto.EquipmentUsageDto;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.LeaveDto;
import lk.ijse.gdse.finalproject.dto.SupervisorDto;

import lk.ijse.gdse.finalproject.dto.tm.EquipmentUsageTM;
import lk.ijse.gdse.finalproject.dto.tm.LeaveTM;

import lk.ijse.gdse.finalproject.model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EquipmenController implements Initializable {

    @FXML
    private AnchorPane EquipmenPage;

    @FXML
    private TableColumn<EquipmentUsageTM, Date> coldate;

    @FXML
    private TableColumn<EquipmentUsageTM, String> colequipname;

    @FXML
    private TableColumn<EquipmentUsageTM, String> colname;

    @FXML
    private TableView<EquipmentUsageTM> equiptable;

    @FXML
    private Label lblcount;

    @FXML
    private ImageView backimage;
    @FXML
    private Button addid;


    @FXML
    private Button useid;

    EquipmentUsageModel equipmentUsageModel = new EquipmentUsageModel();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colname.setCellValueFactory(new PropertyValueFactory<>("LaborName"));
        colequipname.setCellValueFactory(new PropertyValueFactory<>("EquipmentName"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("UseDate"));

        loadEquipmentCount();

        try {

            refreshPage();
            ;
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load LeaveID").show();
        }
    }

    public void refreshPage() throws SQLException {

        //loadTableData();
        //refreshPage();


    }

    private void loadTableData() throws SQLException {
        ArrayList<EquipmentUsageDto> equipmentUsageDtos = equipmentUsageModel.getAllUsages();
        ObservableList<EquipmentUsageTM> equipmentUsageTMS = FXCollections.observableArrayList();

        for (EquipmentUsageDto equipmentUsageDto : equipmentUsageDtos) {
            EquipmentUsageTM equipmentUsageTM = new EquipmentUsageTM(
                    equipmentUsageDto.getEquipmentID(),
                    equipmentUsageDto.getEquipmentName(),
                    equipmentUsageDto.getLaborID(),
                    equipmentUsageDto.getLaborName(),
                    (Date.valueOf(String.valueOf(equipmentUsageDto.getUseDate())))


            );
            equipmentUsageTMS.add(equipmentUsageTM);
        }
        equiptable.setItems(equipmentUsageTMS);
    }

    @FXML
    void restOnAction(ActionEvent event) throws SQLException {
        loadTableData();
        refreshPage();
    }



    @FXML
    void AddEquipmentsOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddEquipment.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("Add Equipment");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = addid.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }

    @FXML
    void AddUsagesOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EquipmentUsage.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("Add Usage");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = addid.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }

    private void loadEquipmentCount() {
        try {
            AddEquipmentModel addEquipmentModel = new AddEquipmentModel();
            int equipmentCount = addEquipmentModel.getEquipmentCount();
            lblcount.setText(String.valueOf(equipmentCount));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void GenarateEquipmentUsageReportOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/report/EquipmentUsageReport.jrxml"
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



    @FXML
    void GenerateEquipmentOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/report/EquipmentReport.jrxml"
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

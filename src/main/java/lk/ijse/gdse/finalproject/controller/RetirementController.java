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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.FactoryOfficerDto;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.LeaveDto;
import lk.ijse.gdse.finalproject.dto.RetirementDto;
import lk.ijse.gdse.finalproject.dto.tm.LaborTM;
import lk.ijse.gdse.finalproject.dto.tm.LeaveTM;
import lk.ijse.gdse.finalproject.dto.tm.RetirementTM;
import lk.ijse.gdse.finalproject.model.FactoryOfficerModel;
import lk.ijse.gdse.finalproject.model.LaborModel;
import lk.ijse.gdse.finalproject.model.LeaveModel;
import lk.ijse.gdse.finalproject.model.RetirementModel;
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

public class RetirementController implements Initializable {

    @FXML
    private Button btnaddtable;
    @FXML
    private AnchorPane RetirementPage;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<RetirementTM, String> coldate;

    @FXML
    private TableColumn<RetirementTM, Double> colfpayment;

    @FXML
    private TableColumn<RetirementTM, String> collabor;

    @FXML
    private TableColumn<RetirementTM, String> colname;

    @FXML
    private TableColumn<RetirementTM, String> colofficer;

    @FXML
    private TableColumn<RetirementTM, String> colretire;

    @FXML
    private TableColumn<RetirementTM, Integer> coltyw;


    @FXML
    private TableView<RetirementTM> retiretable;



    @FXML
    private Button addretirement;

    LaborModel laborModel = new LaborModel();
    FactoryOfficerModel factoryOfficerModel = new FactoryOfficerModel();

    RetirementModel retirementModel =  new RetirementModel();





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colretire.setCellValueFactory(new PropertyValueFactory<>("RetirementID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));


        try {
            //loadNextRetirementID();
            refreshPage();;
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load RetirementID").show();
        }
    }



    @FXML


    public void refreshPage() throws SQLException {
//        loadNextRetirementID();
//        //loadTableData();


    }






    private void loadTableData() throws SQLException {
        ArrayList<RetirementDto> retirementDtos = retirementModel.getAllRetirements();
        ObservableList<RetirementTM> retirementTMS = FXCollections.observableArrayList();

        for (RetirementDto retirementDto : retirementDtos) {
            RetirementTM retirementTM = new RetirementTM(
                    retirementDto.getRetirementID(),
                    retirementDto.getLaborID(),
                    retirementDto.getName(),
                    retirementDto.getOfficerID(),
                    (Date.valueOf(String.valueOf(retirementDto.getRetirementDate()))),
                    retirementDto.getTotalYearsWorked(),
                    retirementDto.getFundPayment()

            );
            retirementTMS.add(retirementTM);
        }
        retiretable.setItems(retirementTMS);
    }



    @FXML
    void ResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
        loadTableData();
    }






    @FXML
    void AddRetirementOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddRetirement.fxml"));
        Parent load = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(load));
        stage.setTitle("Add Union_Members");

        stage.initModality(Modality.APPLICATION_MODAL);

        Window underWindow = addretirement.getScene().getWindow();
        stage.initOwner(underWindow);

        stage.showAndWait();
    }



    @FXML
    void GenarateRetirementReportOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/report/RetirementReport.jrxml"
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



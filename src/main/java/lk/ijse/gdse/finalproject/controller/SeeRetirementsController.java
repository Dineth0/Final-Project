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
import lk.ijse.gdse.finalproject.dto.RetirementDto;
import lk.ijse.gdse.finalproject.dto.tm.RetirementTM;
import lk.ijse.gdse.finalproject.model.RetirementModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SeeRetirementsController implements Initializable {

    @FXML
    private TableColumn<RetirementTM, Date> coldate;

    @FXML
    private TableColumn<RetirementTM,Double> colfpayment;

    @FXML
    private TableColumn<RetirementTM,String> collabor;

    @FXML
    private TableColumn<RetirementTM,String> colname;

    @FXML
    private TableColumn<RetirementTM,String> colofficer;

    @FXML
    private TableColumn<RetirementTM,String> colretire;

    @FXML
    private TableColumn<RetirementTM,Integer> coltyw;

    @FXML
    private TableView<RetirementTM> tableaddretirement1;

    RetirementModel retirementModel = new RetirementModel();

    @FXML
    void TableOnClicked(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colretire.setCellValueFactory(new PropertyValueFactory<>("RetirementID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colofficer.setCellValueFactory(new PropertyValueFactory<>("OfficerID"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("RetirementDate"));
        coltyw.setCellValueFactory(new PropertyValueFactory<>("TotalYearsWorked"));
        colfpayment.setCellValueFactory(new PropertyValueFactory<>("FundPayment"));

        try {
            refreshPage();;
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        tableaddretirement1.setItems(retirementTMS);
    }
    private void refreshPage() throws SQLException {

        loadTableData();
    }
}


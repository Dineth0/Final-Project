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
import lk.ijse.gdse.finalproject.dto.AccidentDto;
import lk.ijse.gdse.finalproject.dto.tm.AccidentTM;
import lk.ijse.gdse.finalproject.model.AccidentModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SeeAccidentsController implements Initializable {

    @FXML
    private TableView<AccidentTM> Accidenttable1;

    @FXML
    private TableColumn<AccidentTM,String> colacc;

    @FXML
    private TableColumn<AccidentTM, Date> coldate;

    @FXML
    private TableColumn<AccidentTM,String> coldes;

    @FXML
    private TableColumn<AccidentTM,String> collabor;

    @FXML
    private TableColumn<AccidentTM,String> colname;

    @FXML
    private TableColumn<AccidentTM,String> colofficer;

    AccidentModel accidentModel = new AccidentModel();

    @FXML
    void OnTableClicked(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colacc.setCellValueFactory(new PropertyValueFactory<>("AccidentID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colofficer.setCellValueFactory(new PropertyValueFactory<>("OfficerID"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("AccidentDate"));
        coldes.setCellValueFactory(new PropertyValueFactory<>("Description"));

        try {
            refreshPage();;
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    private void loadTableData() throws SQLException {
        ArrayList<AccidentDto> accidentDtos = accidentModel.getAllAccidents();
        ObservableList<AccidentTM> accidentTMS = FXCollections.observableArrayList();

        for(AccidentDto accidentDto : accidentDtos){
            AccidentTM accidentTM = new AccidentTM(
                    accidentDto.getAccidentID(),
                    accidentDto.getLaborID(),
                    accidentDto.getName(),
                    accidentDto.getOfficerID(),
                    (Date.valueOf(String.valueOf(accidentDto.getAccidentDate()))),
                    accidentDto.getDescription()


            );
            accidentTMS.add(accidentTM);
        }
        Accidenttable1.setItems(accidentTMS);
    }
    private void refreshPage() throws SQLException {

        loadTableData();
    }
}

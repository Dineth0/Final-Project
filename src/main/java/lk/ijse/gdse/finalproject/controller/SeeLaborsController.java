package lk.ijse.gdse.finalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.tm.LaborTM;
import lk.ijse.gdse.finalproject.model.LaborModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SeeLaborsController implements Initializable {

    @FXML
    private TableColumn<LaborTM,String> coladdress;

    @FXML
    private TableColumn<LaborTM,Integer> colage;

    @FXML
    private TableColumn<LaborTM,Integer> colconnumber;

    @FXML
    private TableColumn<LaborTM,String> colid;

    @FXML
    private TableColumn<LaborTM,String> colname;

    @FXML
    private TableView<LaborTM> labortable2;

    LaborModel laborModel = new LaborModel();

    @FXML
    void onTableClicked(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colid.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colage.setCellValueFactory(new PropertyValueFactory<>("Age"));
        coladdress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colconnumber.setCellValueFactory(new PropertyValueFactory<>("ContactNumber"));
        try{

            loadTableData();
            refreshPage();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    void loadTableData() throws SQLException {
        ArrayList<LaborDto> laborDtos = laborModel.getAllLabors();

        ObservableList<LaborTM> laborTMS = FXCollections.observableArrayList();

        for(LaborDto laborDto : laborDtos) {
            LaborTM laborTM = new LaborTM(
                    laborDto.getLaborID(),
                    laborDto.getName(),
                    laborDto.getAge(),
                    laborDto.getAddress(),
                    laborDto.getContactNumber()
            );
            laborTMS.add(laborTM);
        }
        labortable2.setItems(laborTMS);
    }
    private void refreshPage() throws SQLException {

        loadTableData();
    }
}
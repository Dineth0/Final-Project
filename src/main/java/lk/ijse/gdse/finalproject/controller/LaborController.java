package lk.ijse.gdse.finalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.tm.LaborTM;
import lk.ijse.gdse.finalproject.model.LaborModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class LaborController implements Initializable {

    @FXML
    private AnchorPane laborpage;

    @FXML
    private TableColumn<LaborTM, String> coladdress;

    @FXML
    private TableColumn<LaborTM, Integer> colage;

    @FXML
    private TableColumn<LaborTM, String> colconnumber;

    @FXML
    private TableColumn<LaborTM, String> colid;

    @FXML
    private TableColumn<LaborTM, String> colname;

    @FXML
    private TableView<LaborTM> labortable;

    @FXML
    private Label lblLaborID;

    @FXML
    private TextField txtaddress;

    @FXML
    private TextField txtage;

    @FXML
    private TextField txtconnumber;

    @FXML
    private TextField txtname;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colid.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colage.setCellValueFactory(new PropertyValueFactory<>("Age"));
        coladdress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colconnumber.setCellValueFactory(new PropertyValueFactory<>("ContactNumber"));

        /*LaborTM laborTM = new LaborTM("L001", "Nimal", 45, "No 15,dffg", "0775643678");

        ArrayList<LaborTM> labortmsArray = new ArrayList<>();
        labortmsArray.add(laborTM);

        ObservableList<LaborTM> laborTMS = FXCollections.observableArrayList();

        laborTMS.addAll(labortmsArray);
        labortable.setItems(laborTMS);*/

        try {
            loadNextLaborID();
            //loadTableData();
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load customer id").show();
        }
    }
    private void refreshPage() throws SQLException {
        loadNextLaborID();
        loadTableData();

        btndelete.setDisable(false);
        btnsave.setDisable(false);
        btnupdate.setDisable(true);

        txtname.setText("");
        txtage.setText("");
        txtaddress.setText("");
        txtconnumber.setText("");
    }

    LaborModel laborModel = new LaborModel();

    private void loadTableData() throws SQLException {
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
        labortable.setItems(laborTMS);
    }

    private void loadNextLaborID() throws SQLException {
        String nextLaborID = laborModel.getNextLaborID();
        lblLaborID.setText(nextLaborID);
    }

    @FXML
    void SaveOnAction(ActionEvent event) throws SQLException {
        String LaborID = lblLaborID.getText();
        String Name = txtname.getText();
        int Age = Integer.parseInt(txtage.getText());
        String Address = txtaddress.getText();
        String ContactNumber = txtconnumber.getText();

        txtname.setStyle(txtname.getStyle() + ";-fx-border-color:  #FFAD60;");
        txtage.setStyle(txtage.getStyle() + ";-fx-border-color:  #FFAD60;");
        txtaddress.setStyle(txtaddress.getStyle() + ";-fx-border-color:  #FFAD60;");
        txtconnumber.setStyle(txtconnumber.getStyle() + ";-fx-border-color:  #FFAD60;");

        String NamePattern = "^[A-Za-z ]+$";
        //Integer AgePattern = Integer.parseInt(Age + "^([3-9]|[1-6][0-9])$");
        String AddressPattern = "^[a-zA-Z0-9\s,.'-]{3,}$";
        String ContactNumberPattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";


        boolean isValidName = Name.matches(NamePattern);
       // boolean isValidAge = Age.matches(AgePattern);
        boolean isValidAddress = Address.matches(AddressPattern);
        boolean isValiContanctNumber = ContactNumber.matches(ContactNumberPattern);


        if (!isValidName) {
            System.out.println(txtname.getStyle());
            txtname.setStyle(txtname.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid name.............");
//            return;
        }



        if (!isValiContanctNumber) {
            txtconnumber.setStyle(txtconnumber.getStyle() + ";-fx-border-color: red;");
        }


        if (isValidName && isValidAddress && isValiContanctNumber) {
            LaborDto laborDto = new LaborDto(
                    LaborID,
                    Name,
                    Age,
                    Address,
                    ContactNumber
            );


            boolean isSaved = laborModel.saveLabor(laborDto);
            if (isSaved) {

                refreshPage();

                new Alert(Alert.AlertType.INFORMATION, "Customer saved...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save customer...!").show();
            }
        }
    }
    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        String LaborID = lblLaborID.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDelete = laborModel.DeleteLabor(LaborID);
            if (isDelete) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Labor deleted...!").show();

            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Labor...!").show();

            }
        }
    }

    @FXML
    void ResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }





    @FXML
    void UpdateOnAction(ActionEvent event) throws SQLException {
        String LaborID = lblLaborID.getText();
        String Name = txtname.getText();
        int Age = Integer.parseInt(txtage.getText());
        String Address = txtaddress.getText();
        String ContactNumber = txtconnumber.getText();

        LaborDto laborDto = new LaborDto(
            LaborID,
            Name,
            Age,
            Address,
            ContactNumber
        );

        boolean isUpdate = laborModel.UpdateLabor(laborDto);
        if(isUpdate){
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION,"Labor Updated...!").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Fail to Update Labor...!").show();
        }

    }


    @FXML
    public void onTableClicked(javafx.scene.input.MouseEvent mouseEvent) {
        LaborTM laborTM = labortable.getSelectionModel().getSelectedItem();
        if(laborTM != null){
            lblLaborID.setText(laborTM.getLaborID());
            txtname.setText(laborTM.getName());
            txtage.setText(String.valueOf(laborTM.getAge()));
            txtaddress.setText(laborTM.getAddress());
            txtconnumber.setText(laborTM.getContactNumber());

            btndelete.setDisable(false);
            btnsave.setDisable(true);
            btnupdate.setDisable(false);
        }
    }
    @FXML
    void GenarateReportOnAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/report/LaborReport.jrxml"
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




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
import lk.ijse.gdse.finalproject.dto.PaymentDto;
import lk.ijse.gdse.finalproject.dto.tm.PaymentTM;
import lk.ijse.gdse.finalproject.model.FactoryOfficerModel;
import lk.ijse.gdse.finalproject.model.LaborModel;
import lk.ijse.gdse.finalproject.model.PaymentModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPaymentController implements Initializable {

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<PaymentTM,String> caolofficer;

    @FXML
    private TableColumn<PaymentTM,Double> coldbs;

    @FXML
    private TableColumn<PaymentTM,String> collabor;

    @FXML
    private TableColumn<PaymentTM,Double> colmts;

    @FXML
    private TableColumn<PaymentTM,String> colname;

    @FXML
    private TableColumn<PaymentTM,String> colpayment;

    @FXML
    private TableColumn<PaymentTM,String> colstatus;

    @FXML
    private ComboBox<String> comlaborId;

    @FXML
    private ComboBox<String> comofficerId;

    @FXML
    private ComboBox<String> comstatus;
    private final String[] Status = {"Payment Taken","Not Taken"};

    @FXML
    private Label lblid;

    @FXML
    private Label lbllaborname;

    @FXML
    private Label lblofficername;

    @FXML
    private Label lblovertime;

    @FXML
    private Label lblstatus;

    @FXML
    private Label lblworkingdays;

    @FXML
    private TableView<PaymentTM> paymenttable;

    @FXML
    private TextField txtdbs;



    LaborModel laborModel = new LaborModel();
    FactoryOfficerModel factoryOfficerModel = new FactoryOfficerModel();
    PaymentModel paymentModel = new PaymentModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comstatus.getItems().addAll(Status);
        colpayment.setCellValueFactory(new PropertyValueFactory<>("PaymentID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        caolofficer.setCellValueFactory(new PropertyValueFactory<>("OfficerID"));
        coldbs.setCellValueFactory(new PropertyValueFactory<>("Day_Basic_Salary"));
        colmts.setCellValueFactory(new PropertyValueFactory<>("Monthly_Total_Salary"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        try {
            loadNextOPaymentID();
            loadLaborIDs();
            loadOfficerIDs();
            loadTableData();
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load PaymentID").show();
        }
    }

    @FXML
    void Combo1OnAction(ActionEvent event) throws SQLException {
        String selectedOfficerID = comofficerId.getSelectionModel().getSelectedItem();
        FactoryOfficerDto factoryOfficerDto = factoryOfficerModel.findById(selectedOfficerID);

        if (factoryOfficerDto != null) {
            lblofficername.setText(factoryOfficerDto.getName());
        }
    }

    @FXML
    void Combo2OnAction(ActionEvent event) {
        String SelectedValue = comstatus.getValue();
        lblstatus.setText(SelectedValue);
    }

    @FXML
    void ComboOnAction(ActionEvent event) throws SQLException {
        String selectedLaborID = comlaborId.getSelectionModel().getSelectedItem();
        LaborDto laborDto = laborModel.findById(selectedLaborID);

        if (laborDto != null) {
            lbllaborname.setText(laborDto.getName());
            int currentMonth = LocalDate.now().getMonthValue();
            int currentYear = LocalDate.now().getYear();

            int workingDays = laborModel.getWorkingDays(selectedLaborID, currentMonth, currentYear);
            lblworkingdays.setText(String.valueOf(workingDays));

            int OverTime = laborModel.getTotalOvertTime(selectedLaborID, currentMonth, currentYear);
            lblovertime.setText(String.valueOf(OverTime));
        }
    }
    private void loadLaborIDs() throws SQLException {
        ArrayList<String> laborIDs = laborModel.getAllLaborIDs();
        comlaborId.getItems().addAll(laborIDs);
    }
    private void loadOfficerIDs() throws SQLException {
        ArrayList<String> leaveIDs = factoryOfficerModel. getAllOfficerIds();
        comofficerId.getItems().addAll(leaveIDs );
    }
    public void refreshPage() throws SQLException {
        loadNextOPaymentID();
        loadTableData();

        btnsave.setDisable(false);
        btndelete.setDisable(true);
        btnupdate.setDisable(true);

        comlaborId.getSelectionModel().clearSelection();
        comofficerId.getSelectionModel().clearSelection();
        lbllaborname.setText("");
        lblofficername.setText("");
        lblworkingdays.setText("");
        lblovertime.setText("");
        txtdbs.setText("");
        comstatus.setValue(null);

    }

    private void loadNextOPaymentID() throws SQLException {
        String nextPaymentID = paymentModel.getNextPaymentID();
        lblid.setText(nextPaymentID);
    }
    @FXML
    void SaveONAction(ActionEvent event) throws SQLException {
        String PaymentID = lblid.getText();
        String LaborID = comlaborId.getValue();
        String Name = lbllaborname.getText();
        String OfficerID = comofficerId.getValue();
        double Day_Basic_Salary = Double.parseDouble(txtdbs.getText());
        String Status = comstatus.getValue();

        int workingDays = Integer.parseInt(lblworkingdays.getText());
        int OverTime = Integer.parseInt(lblovertime.getText());

        double firstTotal = Day_Basic_Salary * workingDays;
        double EPF = firstTotal * 0.1;
        double Monthly_Total_Salary = (firstTotal - EPF) + (OverTime * 210);


        PaymentDto paymentDto = new PaymentDto(
                PaymentID,
                LaborID,
                Name,
                OfficerID,
                Day_Basic_Salary,
                Monthly_Total_Salary,
                Status
        );
        boolean isSaved = paymentModel.savePayment(paymentDto);
        if(isSaved){
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION,"Payment saved...!").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Fail to save Payment...!").show();
        }
    }
    private void loadTableData() throws SQLException {
        ArrayList<PaymentDto> paymentDtos = paymentModel.getAllPayments();
        ObservableList<PaymentTM> paymentTMS = FXCollections.observableArrayList();

        for(PaymentDto paymentDto : paymentDtos){
            PaymentTM paymentTM = new PaymentTM(
                    paymentDto.getPaymentID(),
                    paymentDto.getLaborID(),
                    paymentDto.getName(),
                    paymentDto.getOfficerID(),
                    paymentDto.getDay_Basic_Salary(),
                    paymentDto.getMonthly_Total_Salary(),
                    paymentDto.getStatus()

            );
            paymentTMS.add(paymentTM);
        }
        paymenttable.setItems(paymentTMS);
    }
    @FXML
    void TableOnClicked(MouseEvent event) {
        PaymentTM paymentTM = paymenttable.getSelectionModel().getSelectedItem();
        if(paymentTM != null){
            lblid.setText(paymentTM.getPaymentID());
            comlaborId.setValue(paymentTM.getLaborID());
            lbllaborname.setText(paymentTM.getName());
            comofficerId.setValue(paymentTM.getOfficerID());
            txtdbs.setText(String.valueOf(paymentTM.getDay_Basic_Salary()));
            comstatus.setValue(paymentTM.getStatus());


            btnsave.setDisable(true);

            btndelete.setDisable(false);
            btnupdate.setDisable(false);
        }
    }
    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        String PaymentID = lblid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            boolean isDeleted = paymentModel.deletePayment(PaymentID );
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Payment deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Payment...!").show();
            }
        }
    }





    @FXML
    void UpdateOnAction(ActionEvent event) throws SQLException {
        String PaymentID = lblid.getText();
        String LaborID = comlaborId.getValue();
        String OfficerID = comofficerId.getValue();
        String Day_Basic_Salary = txtdbs.getText();
        //String Monthly_Total_Salary = txtmts.getText();
        String Status = comstatus.getValue();

        PaymentDto paymentDto = new PaymentDto(
                PaymentID,
                LaborID,
                OfficerID,
                Day_Basic_Salary,
                //Monthly_Total_Salary,
                Status
        );
        boolean isUpdate = PaymentModel.updatePayment(paymentDto);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Payment update...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update Payment...!").show();
        }
    }

    @FXML
    void restOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }


}
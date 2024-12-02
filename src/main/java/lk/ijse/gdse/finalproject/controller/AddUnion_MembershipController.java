package lk.ijse.gdse.finalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.Union_MembershipDto;
import lk.ijse.gdse.finalproject.dto.tm.Union_MembershipTM;
import lk.ijse.gdse.finalproject.model.LaborModel;
import lk.ijse.gdse.finalproject.model.Union_MembershipModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddUnion_MembershipController implements Initializable {

    @FXML
    private Button btndelete;

    @FXML
    private Button btnsave;

    @FXML
    private Button btnupdate;

    @FXML
    private TableColumn<Union_MembershipTM,String> collabor;

    @FXML
    private TableColumn<Union_MembershipTM,String> colmember;

    @FXML
    private TableColumn<Union_MembershipTM,String> colname;

    @FXML
    private TableColumn<Union_MembershipTM,String> colstatus;

    @FXML
    private ComboBox<String> comlaborid;

    @FXML
    private ComboBox<String> comstatus;
    private  final String [] Status = {"Active","Deactivated"};

    @FXML
    private Label lblid;

    @FXML
    private Label lbllaborname;

    @FXML
    private Label lblstatus;

    @FXML
    private TableView<Union_MembershipTM> tableaddmember;

    LaborModel laborModel = new LaborModel();
    Union_MembershipModel union_membershipModel = new Union_MembershipModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comstatus.getItems().addAll(Status);
        colmember.setCellValueFactory(new PropertyValueFactory<>("MembershipID"));
        collabor.setCellValueFactory(new PropertyValueFactory<>("LaborID"));
        colname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        try {
            loadNextMembershipID();
            loadLaborIDs();
            loadTableData();
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load MembershipID").show();
        }
    }

    @FXML
    void Combo1OnAction(ActionEvent event) {
        String SelectedValue = comstatus.getValue();
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
        loadNextMembershipID();
        loadTableData();

        btndelete.setDisable(true);
        btnsave.setDisable(false);
        btnupdate.setDisable(true);

        comlaborid.getSelectionModel().clearSelection();
        lbllaborname.setText("");
        comstatus.setValue(null);
    }


    public void loadNextMembershipID() throws SQLException {
        String nextMembershipID = union_membershipModel.getNextMembershipID();
        lblid.setText(nextMembershipID);
    }
    private void loadLaborIDs() throws SQLException {
        ArrayList<String> laborIDs = laborModel.getAllLaborIDs();
        comlaborid.getItems().addAll(laborIDs);
    }
    @FXML
    void SaveOnAction(ActionEvent event) throws SQLException {
        String MembershipID = lblid.getText();
        String LaborID = comlaborid.getValue();
        String Name = lbllaborname.getText();
        String Status = comstatus.getValue();

        Union_MembershipDto union_membershipDto = new Union_MembershipDto(
                MembershipID,
                LaborID,
                Name,
                Status
        );
        boolean isSaved = union_membershipModel.saveMember(union_membershipDto);
        if(isSaved){
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION,"Member saved...!").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Fail to save Member...!").show();
        }
    }
    private void loadTableData() throws SQLException {
        ArrayList<Union_MembershipDto> union_membershipDtos = union_membershipModel.getAllMembers();
        ObservableList<Union_MembershipTM> unionMembershipTMS = FXCollections.observableArrayList();

        for(Union_MembershipDto union_membershipDto : union_membershipDtos){
            Union_MembershipTM union_membershipTM = new Union_MembershipTM(
                    union_membershipDto.getMembershipID(),
                    union_membershipDto.getLaborID(),
                    union_membershipDto.getName(),
                    union_membershipDto.getStatus()

            );
            unionMembershipTMS.add(union_membershipTM);
        }
        tableaddmember.setItems(unionMembershipTMS);
    }
    @FXML
    void DeleteOnAction(ActionEvent event) throws SQLException {
        String MembershipID = lblid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){

            boolean isDeleted = union_membershipModel.deleteMember(MembershipID );
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Member deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Member...!").show();
            }
        }
    }

    @FXML
    void OnTableClicked(MouseEvent event) {
        Union_MembershipTM union_membershipTM = tableaddmember.getSelectionModel().getSelectedItem();
        if(union_membershipTM != null){
            lblid.setText(union_membershipTM.getMembershipID());
            comlaborid.setValue(union_membershipTM.getLaborID());
            lbllaborname.setText(union_membershipTM.getName());
            comstatus.setValue(union_membershipTM.getStatus());

            btnsave.setDisable(true);

            btndelete.setDisable(false);
            btnupdate.setDisable(false);
        }
    }



    @FXML
    void UpdateOnAction(ActionEvent event) throws SQLException {
        String MembershipID = lblid.getText();
        String LaborID = comlaborid.getValue();
        String Status = comstatus.getValue();

        Union_MembershipDto union_membershipDto = new Union_MembershipDto(
                MembershipID,
                LaborID,
                lbllaborname.getText(),
                Status
        );
        boolean isUpdate = Union_MembershipModel.updateMember(union_membershipDto);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Member update...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update Member...!").show();
        }
    }

    @FXML
    void restOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }


}

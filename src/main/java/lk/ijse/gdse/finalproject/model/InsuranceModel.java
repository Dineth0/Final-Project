package lk.ijse.gdse.finalproject.model;

import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.InsuranceDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class InsuranceModel {
    public String getNextPolicyNumber() throws SQLException {


        ResultSet resultSet = CrudUtil.execute("SELECT PolicyNumber FROM Insuranace ORDER BY PolicyNumber DESC LIMIT 1");

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i+1;


            return String.format("I%03d", newIdIndex);
        }

        return "I001";
    }
    public boolean saveInsurance(InsuranceDto insuranceDto) throws SQLException {


        boolean isSaved = CrudUtil.execute(
                "insert into Insuranace values(?,?,?,?,?)",
        insuranceDto.getPolicyNumber(),
        insuranceDto.getAccidentID(),
        insuranceDto.getName(),
        insuranceDto.getOfficerID(),
        insuranceDto.getInsurancePayment());



        return isSaved;
    }
    public ArrayList<InsuranceDto> getAllInsurances() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from Insuranace");

        ArrayList<InsuranceDto> insuranceDtos = new ArrayList<>();

        while (rst.next()){
            InsuranceDto insuranceDto = new InsuranceDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5)

            );
            insuranceDtos.add(insuranceDto);
        }
        return insuranceDtos;
    }
    public static boolean updateInsurance(InsuranceDto insuranceDto) throws SQLException {
        return CrudUtil.execute(
                "update Insuranace set AccidentID=?, OfficerID=?, InsurancePayment=? where PolicyNumber=?",

                insuranceDto.getAccidentID(),
                insuranceDto.getOfficerID(),
                insuranceDto.getInsurancePayment(),
                insuranceDto.getPolicyNumber()


        );
    }
    public boolean deleteInsurance(String PolicyNumber) throws SQLException {
        return CrudUtil.execute("delete from Insuranace where PolicyNumber=?",PolicyNumber);
    }
}


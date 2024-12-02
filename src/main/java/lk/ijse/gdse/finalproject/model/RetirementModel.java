package lk.ijse.gdse.finalproject.model;

import javafx.collections.FXCollections;
import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.RetirementDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static java.time.zone.ZoneRulesProvider.refresh;


public class RetirementModel {
    public String getNextRetirementID() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT RetirementID FROM Retirement ORDER BY RetirementID DESC LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i+1;


            return String.format("R%03d", newIdIndex);
        }

        return "R001";
    }
    public boolean saveRetirement(RetirementDto retirementDto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {

            connection.setAutoCommit(false);

            boolean isRetirementSaved = CrudUtil.execute(
                    "insert into Retirement values (?,?,?,?,?,?,?)",
                    retirementDto.getRetirementID(),
                    retirementDto.getLaborID(),
                    retirementDto.getName(),
                    retirementDto.getOfficerID(),
                    Date.valueOf(String.valueOf(retirementDto.getRetirementDate())),
                    retirementDto.getTotalYearsWorked(),
                    retirementDto.getFundPayment()
            );

            if (isRetirementSaved) {
                boolean isLaborDeleted = CrudUtil.execute(
                        "delete from Labor where LaborID =?",
                        retirementDto.getLaborID());
                if (isLaborDeleted) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            }

            connection.rollback();
            return false;
        } catch (Exception e) {

            connection.rollback();
            return false;
        } finally {

            connection.setAutoCommit(true);
        }

    }



public ArrayList<RetirementDto> getAllRetirements() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from Retirement");

        ArrayList<RetirementDto> retirementDtos = new ArrayList<>();

        while (rst.next()){
            RetirementDto retirementDto = new RetirementDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDate(5),
                    rst.getInt(6),
                    rst.getDouble(7)
            );
            retirementDtos.add(retirementDto);
        }
        return retirementDtos;
    }
    public boolean updateRetirement(RetirementDto retirementDto) throws SQLException {
        return CrudUtil.execute(
                "update Retirement set  LaborID=?, OfficerID=?, RetirementDate=?,TotalYearsWorked=?, FundPayment =? where RetirementID=?",
                retirementDto.getLaborID(),
                retirementDto.getOfficerID(),
                retirementDto.getRetirementDate(),
                retirementDto.getTotalYearsWorked(),
                retirementDto.getFundPayment(),
                retirementDto.getRetirementID()
        );
    }

    public boolean deleteRetirement(String RetirementID) throws SQLException {
        return CrudUtil.execute("delete from Retirement where RetirementID=?",RetirementID);
    }
    public int getTotalRetirements() throws SQLException{
        String query = "select count(*) from Retirement";
        ResultSet rst  = CrudUtil.execute(query);

        if(rst.next()){
            return rst.getInt(1);
        }
        return 0;
    }

}

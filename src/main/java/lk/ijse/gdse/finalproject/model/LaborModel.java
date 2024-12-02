package lk.ijse.gdse.finalproject.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.tm.LaborTM;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LaborModel {


    public String getNextLaborID() throws SQLException {

        ResultSet rst =  CrudUtil.execute("select LaborID from Labor order by LaborID desc limit 1");

        if (rst.next()){
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i+1;

            return String.format("L%03d",newIdIndex);
        }
        return  "L001";
    }
    public boolean saveLabor(LaborDto laborDto) throws SQLException {

        boolean isSaved = CrudUtil.execute(
                "insert into Labor values (?,?,?,?,?)",
                laborDto.getLaborID(),
                laborDto.getName(),
                laborDto.getAge(),
                laborDto.getAddress(),
                laborDto.getContactNumber()
                );
        return isSaved;
    }
    public ArrayList<LaborDto> getAllLabors() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from Labor");

        ArrayList<LaborDto> laborDtos = new ArrayList<>();

        while (rst.next()){
            LaborDto laborDto = new LaborDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getString(5)
            );
            laborDtos.add(laborDto);
        }
        return laborDtos;
    }

   public boolean UpdateLabor(LaborDto laborDto) throws SQLException {
        return CrudUtil.execute(
                "update Labor set  Name=?, Age=?, Address=?,ContactNumber=? where LaborID=?",
                laborDto.getName(),
                laborDto.getAge(),
                laborDto.getAddress(),
                laborDto.getContactNumber(),
                laborDto.getLaborID()
        );
    }

    public boolean DeleteLabor(String LaborID) throws SQLException {
        return CrudUtil.execute("delete from Labor where LaborID=?",LaborID);
    }

    public ArrayList<String> getAllLaborIDs() throws SQLException {
        ResultSet rst = CrudUtil.execute("select LaborID from Labor");

        ArrayList<String> LaborIDs = new ArrayList<>();

        while (rst.next()){
            LaborIDs.add(rst.getString(1));
        }

        return LaborIDs;
    }


    public LaborDto findById(String selectedLaborID) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from Labor where LaborID=?",selectedLaborID);
        if (rst.next()){
            return new LaborDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }

    public int getWorkingDays(String LaborID,int month,int year) throws SQLException {
        String query = "SELECT count(*) as WorkingDays FROM Attendance WHERE LaborID = ? AND MONTH(Date) = ? AND YEAR(Date) = ? AND status = 'Present'";

        ResultSet rst = CrudUtil.execute(query, LaborID, month, year);

        if (rst.next()) {
            return rst.getInt("WorkingDays");
        }
        return 0;
    }
    public int getTotalLabors() throws SQLException {
        String query = "select count(*) as TotalLabors FROM Labor";
        ResultSet rst = CrudUtil.execute(query);
        if (rst.next()) {
            return rst.getInt(1);
        }
        return 0;
    }
    public int getTotalOvertTime(String LaborID,int month,int year) throws SQLException {
        int OverTime = 0;
        String query = "select SUM(OverTime) as TotalOverTime from Shift where LaborID = ? AND Month(Date) = ? AND Year(Date) = ?";

        ResultSet rst = CrudUtil.execute(query, LaborID, month, year);

        if (rst.next()) {
            return rst.getInt("TotalOverTime");
        }
        return 0;
    }


    }


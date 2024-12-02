package lk.ijse.gdse.finalproject.model;

import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.ShiftDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ShiftModel {
    public String getNextShiftID() throws SQLException {


        ResultSet resultSet = CrudUtil.execute("SELECT ShiftID FROM Shift ORDER BY ShiftID DESC LIMIT 1");


        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i+1;


            return String.format("S%03d", newIdIndex);
        }

        return "S001";
    }
    public boolean saveShift(ShiftDto shiftDto) throws SQLException {

        boolean isSaved = CrudUtil.execute(
                "insert into Shift values(?,?,?,?,?,?,?)",
         shiftDto.getShiftID(),
         shiftDto.getLaborID(),
         shiftDto.getSupervisorID(),
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
         shiftDto.getShiftDate(),
        shiftDto.getStartTime(),
        shiftDto.getEndTime(),
        shiftDto.getOverTime());


        return isSaved;



    }
    public ArrayList<ShiftDto> getAllShifts() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from Shift");

        ArrayList<ShiftDto> shiftDtos = new ArrayList<>();

        while (rst.next()){
            ShiftDto shiftDto = new ShiftDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDate(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            );
            shiftDtos.add(shiftDto);
        }
        return shiftDtos;
    }
    public boolean updateShift(ShiftDto shiftDto) throws SQLException {
        return CrudUtil.execute(
                "update Shift set LaborID=?, SupervisorID=?, Date=?, StartTime=?, EndTime=?, OverTime=? where ShiftID=?",

                shiftDto.getLaborID(),
                shiftDto.getSupervisorID(),
                //String.format(String.valueOf(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
                shiftDto.getShiftDate(),
                shiftDto.getStartTime(),
                shiftDto.getEndTime(),
                shiftDto.getOverTime(),
                shiftDto.getShiftID()
        );
    }
    public boolean deleteShift(String ShiftID) throws SQLException {
        return CrudUtil.execute("delete from Shift where ShiftID=?",ShiftID);
    }
}

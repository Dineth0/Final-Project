package lk.ijse.gdse.finalproject.model;

import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.LeaveDto;
import lk.ijse.gdse.finalproject.dto.RetirementDto;
import lk.ijse.gdse.finalproject.dto.ShiftDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LeaveModel {


    public String getNextLeaveID() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT LeaveID FROM Vacation ORDER BY LeaveID DESC LIMIT 1");

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i+1;

            return String.format("L%03d", newIdIndex);
        }
        return "L001";
    }
    public boolean saveLeave(LeaveDto leaveDto) throws SQLException {


        boolean isSaved = CrudUtil.execute(
                "insert into Vacation values(?,?,?,?,?,?,?)",
        leaveDto.getLeaveID(),
        leaveDto.getLaborID(),
        leaveDto.getName(),
        leaveDto.getOfficerID(),
        leaveDto.getLeaveDate(),
        leaveDto.getReason(),
        leaveDto.getStatus());


        return isSaved;
    }
    public ArrayList<LeaveDto> getAllLeaves() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from Vacation");

        ArrayList<LeaveDto> leaveDtos = new ArrayList<>();

        while (rst.next()){
            LeaveDto leaveDto = new LeaveDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDate(5),
                    rst.getString(6),
                    rst.getString(7)
            );
            leaveDtos.add(leaveDto);
        }
        return leaveDtos;
    }

    public boolean deleteLeave(String LeaveID) throws SQLException {
        return CrudUtil.execute("delete from vacation where LeaveID=?",LeaveID);
    }
    public static boolean updateLeave(LeaveDto leaveDto) throws SQLException {
        return CrudUtil.execute(
                "update Vacation set  LaborID=?, OfficerID=?, Date=?,Reason=?, Status =? where LeaveID=?",
                leaveDto.getLaborID(),
                leaveDto.getOfficerID(),
                leaveDto.getLeaveDate(),
                leaveDto.getReason(),
                leaveDto.getStatus(),
                leaveDto.getLeaveID()

        );
    }
}

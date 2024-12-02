package lk.ijse.gdse.finalproject.model;

import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.AttendanceDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AttendanceModel {
    public String getNextAttendanceID() throws SQLException {

        ResultSet rst =  CrudUtil.execute("select AttendanceID from Attendance order by AttendanceID desc limit 1");
        if (rst.next()) {
            String lastId = rst.getString(1);
            if (lastId != null) {
                String substring = lastId.substring(1);
                int i = Integer.parseInt(substring);
                int newIdIndex = i + 1;

                return String.format("A%03d", newIdIndex);
            }
        }

        return "A001";
    }
    public boolean saveAttendance(AttendanceDto attendanceDto) throws SQLException {

        boolean isSaved = CrudUtil.execute("insert into Attendance values(?,?,?,?,?)",

        attendanceDto.getAttendanceID(),
        attendanceDto.getLaborID(),
        attendanceDto.getSupervisorID(),
                attendanceDto.getAttendDate(),
        attendanceDto.getStatus());


        return isSaved;



    }
    public ArrayList<AttendanceDto> getAllAttendances() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from Attendance");

        ArrayList<AttendanceDto> attendanceDtos = new ArrayList<>();

        while (rst.next()){
            AttendanceDto attendanceDto = new AttendanceDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDate(4),
                    rst.getString(5)
            );
           attendanceDtos.add(attendanceDto);
        }
        return attendanceDtos;
    }

    public boolean updateAttendance(AttendanceDto attendanceDto) throws SQLException {
        return CrudUtil.execute(
                "update Attendance set LaborID=?, SupervisorID=?, Date=?, Status=? where AttendanceID=?",

                attendanceDto.getLaborID(),
                attendanceDto.getSupervisorID(),
                attendanceDto.getAttendDate(),
                attendanceDto.getStatus(),
                attendanceDto.getAttendanceID()
        );
    }

    public boolean deleteAttendance(String AttendanceID) throws SQLException {
        return CrudUtil.execute("delete from Attendance where AttendanceID=?",AttendanceID);
    }

}


package lk.ijse.gdse.finalproject.model;

import lk.ijse.gdse.finalproject.dto.ComplaintDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComplaintModel {
    public static String getNextComplaintID() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("SELECT ComplaintID FROM Complaint ORDER BY ComplaintID DESC LIMIT 1");

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i+1;

            return String.format("C%03d", newIdIndex);
        }
        return "C001";
    }
    public boolean saveComplaint(ComplaintDto complaintDto) throws SQLException {


        boolean isSaved = CrudUtil.execute(
                "insert into Complaint values(?,?,?,?,?,?)",
                complaintDto.getComplaintID(),
        complaintDto.getLaborID(),
        complaintDto.getName(),
        complaintDto.getDescription(),
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        complaintDto.getComplaintDate(),
        complaintDto.getManager_Seen()
        );
        return isSaved;
    }
    public ArrayList<ComplaintDto> getAllComplaints() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from Complaint");

        ArrayList<ComplaintDto> complaintDtos = new ArrayList<>();

        while (rst.next()){
            ComplaintDto complaintDto = new ComplaintDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDate(5),
                    rst.getString(6)

            );
            complaintDtos.add(complaintDto);
        }
        return complaintDtos;
    }
    public static boolean updateComplaint(ComplaintDto complaintDto) throws SQLException {
        return CrudUtil.execute(
                "update Complaint set LaborID=?, Description =?, Date=?, Manager_Seen=? where ComplaintID=?",

                complaintDto.getLaborID(),
                complaintDto.getDescription(),
                complaintDto.getComplaintDate(),
                complaintDto.getManager_Seen(),
                complaintDto.getComplaintID()

        );
    }
    public boolean deleteComplaint(String ComplaintID) throws SQLException {
        return CrudUtil.execute("delete from Complaint where ComplaintID=?",ComplaintID);
    }
}

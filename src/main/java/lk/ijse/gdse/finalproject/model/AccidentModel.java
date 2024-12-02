package lk.ijse.gdse.finalproject.model;

import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.AccidentDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AccidentModel {
    public String getNextAccidentID() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("SELECT AccidentID FROM Accident ORDER BY AccidentID DESC LIMIT 1");

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i+1;

            return String.format("A%03d", newIdIndex);
        }
        return "A001";
    }
    public boolean saveAccident(AccidentDto accidentDto) throws SQLException {

        boolean isSaved = CrudUtil.execute(
                "insert into Accident values(?,?,?,?,?,?)",
                accidentDto.getAccidentID(),
                accidentDto.getLaborID(),
                accidentDto.getName(),
                accidentDto.getOfficerID(),

                accidentDto.getAccidentDate(),
                        accidentDto.getDescription()
        );
        return isSaved;
    }
    public ArrayList<AccidentDto> getAllAccidents() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from Accident");

        ArrayList<AccidentDto> accidentDtos = new ArrayList<>();

        while (rst.next()){
            AccidentDto accidentDto = new AccidentDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDate(5),
                    rst.getString(6)

            );
            accidentDtos.add(accidentDto);
        }
        return accidentDtos;
    }
    public static boolean updateAccident(AccidentDto accidentDto) throws SQLException {
        return CrudUtil.execute(
                "update Accident set LaborID=?, OfficerID=?, Date=?, Description=? where AccidentID=?",
                accidentDto.getLaborID(),
                accidentDto.getOfficerID(),

                accidentDto.getAccidentDate(),
                accidentDto.getDescription(),
                accidentDto.getAccidentID()


        );
    }
    public boolean deleteAccident(String AccidentID) throws SQLException {
        return CrudUtil.execute("delete from Accident where AccidentID=?",AccidentID);
    }
    public AccidentDto findById(String selectedAccidentID) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from Accident where AccidentID=?",selectedAccidentID);
        if (rst.next()){
            return new AccidentDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDate(5),
                    rst.getString(6)
            );
        }
        return null;
    }
    public ArrayList<String> getAllAccidentIDs() throws SQLException {
        ResultSet rst = CrudUtil.execute("select AccidentID from Accident");

        ArrayList<String> AccidentIDs = new ArrayList<>();

        while (rst.next()){
            AccidentIDs.add(rst.getString(1));
        }

        return AccidentIDs;
    }
}

package lk.ijse.gdse.finalproject.model;

import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.LeaveDto;
import lk.ijse.gdse.finalproject.dto.ShiftDto;
import lk.ijse.gdse.finalproject.dto.TrainingDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TrainingModel {
    public String getNextTrainingID() throws SQLException {


        ResultSet resultSet = CrudUtil.execute("select TrainingID from Training order by TrainingID desc limit 1");

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i+1;


            return String.format("T%03d", newIdIndex);
        }

        return "T001";
    }
    public boolean saveTraining(TrainingDto trainingDto) throws SQLException {

        boolean isSaved = CrudUtil.execute(
                "insert into Training values(?,?,?,?,?)",
                trainingDto.getTrainingID(),
         trainingDto.getLaborID(),
         trainingDto.getOfficerID(),
        trainingDto.getDescription(),
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
         trainingDto.getTrainingDate()

        );



        return isSaved;





    }
    public ArrayList<TrainingDto> getAllTrainings() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from Training");

        ArrayList<TrainingDto> trainingDtos = new ArrayList<>();

        while (rst.next()){
            TrainingDto trainingDto = new TrainingDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDate(5)
            );
            trainingDtos.add(trainingDto);
        }
        return trainingDtos;
    }
    public static boolean updateTraining(TrainingDto trainingDto) throws SQLException {
        return CrudUtil.execute(
                "update Training set LaborID=?, OfficerID=?, Description=?, Date=? where TrainingID=?",

                trainingDto.getLaborID(),
                trainingDto.getOfficerID(),
                trainingDto.getDescription(),
               // String.format(String.valueOf(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
                trainingDto.getTrainingDate(),
                trainingDto.getTrainingID()


        );
    }
    public boolean deleteTraining(String TrainingID) throws SQLException {
        return CrudUtil.execute("delete from Training where TrainingID=?",TrainingID);
    }
}

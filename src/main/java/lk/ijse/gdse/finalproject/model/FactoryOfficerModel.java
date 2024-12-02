package lk.ijse.gdse.finalproject.model;

import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.FactoryOfficerDto;
import lk.ijse.gdse.finalproject.dto.SupervisorDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FactoryOfficerModel {
    public String getNextOfficerID() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "select OfficerID from FactoryOfficer order by OfficerID desc limit 1";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            String lastId = resultSet.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i+1;
            return String.format("F%03d",newIdIndex);
        }
        return "F001";
    }
    public boolean SaveOfficer(FactoryOfficerDto factoryOfficerDto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "insert into FactoryOfficer values (?,?)";
        PreparedStatement pst = connection.prepareStatement(sql);

        pst.setObject(1, factoryOfficerDto.getOfficerID());
        pst.setObject(2,factoryOfficerDto.getName());

        int result = pst.executeUpdate();
        boolean isSaved = result>0;
        return isSaved;
    }


    public ArrayList<String> getAllOfficerIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select OfficerID from FactoryOfficer");

        ArrayList<String> OfficerIDs = new ArrayList<>();

        while (rst.next()){
            OfficerIDs.add(rst.getString(1));
        }

        return OfficerIDs;
    }

    public FactoryOfficerDto findById(String selectedOfficerID) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from FactoryOfficer where OfficerID=?",selectedOfficerID);
        if (rst.next()){
            return new  FactoryOfficerDto (
                    rst.getString(1),
                    rst.getString(2)

            );
        }
        return null;
    }
    public ArrayList<FactoryOfficerDto> getAllFactoryOfficers() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from FactoryOfficer");

        ArrayList<FactoryOfficerDto> factoryOfficerDtos = new ArrayList<>();

        while (rst.next()){
            FactoryOfficerDto factoryOfficerDto = new FactoryOfficerDto(
                    rst.getString(1),
                    rst.getString(2)

            );
            factoryOfficerDtos.add(factoryOfficerDto);
        }
        return factoryOfficerDtos;
    }
    public boolean updateOfficer(FactoryOfficerDto factoryOfficerDto) throws SQLException {
        return CrudUtil.execute(
                "update FactoryOfficer set Name=? where OfficerID=?",
                factoryOfficerDto.getName(),
                factoryOfficerDto.getOfficerID()
        );
    }

    public boolean deleteOfficer(String OfficerID) throws SQLException {
        return CrudUtil.execute("delete from FactoryOfficer where OfficerID =?",OfficerID);
    }

}

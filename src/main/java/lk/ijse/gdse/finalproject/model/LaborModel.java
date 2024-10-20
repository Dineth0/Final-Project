package lk.ijse.gdse.finalproject.model;

import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LaborModel {
    public String getNextLaborID() throws SQLException {
       // Connection connection = DBConnection.getInstance().getConnection();
       // String sql = "select LaborID from Labor order by LaborID desc limit 1";
       // PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rst =  CrudUtil.execute("select LaborID from Labor order by LaborID desc limit 1");
        //ResultSet rst = preparedStatement.executeQuery();
        if (rst.next()){
            String lastId = rst.getString(1); // C002
            String substring = lastId.substring(1); // 002
            int i = Integer.parseInt(substring); // 2
            int newIdIndex = i+1; // 3
//            String newId = ; // C003
            return String.format("L%03d",newIdIndex);
        }
        return  "L001";
    }
    public boolean saveLabor(LaborDto laborDto) throws SQLException {
        /*Connection connection = DBConnection.getInstance().getConnection();
        String sql = "insert into Labor values (?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(sql);

        pst.setObject(1,laborDto.getLaborId());
        pst.setObject(2,laborDto.getName());
        pst.setObject(3,laborDto.getAge());
        pst.setObject(4,laborDto.getAddress());
        pst.setObject(5,laborDto.getContactNumber());

        int result = pst.executeUpdate();*/
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

}

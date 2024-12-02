package lk.ijse.gdse.finalproject.model;

import lk.ijse.gdse.finalproject.dto.EquipmentUsageDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EquipmentUsageModel {
    public boolean saveUsage(EquipmentUsageDto equipmentUsageDto) throws SQLException {


        boolean isSaved = CrudUtil.execute(
                "insert into EquipmentUsage values(?,?,?,?,?)",
                equipmentUsageDto.getEquipmentID(),
                equipmentUsageDto.getEquipmentName(),
                equipmentUsageDto.getLaborID(),
                equipmentUsageDto.getLaborName(),
                equipmentUsageDto.getUseDate()
        );
        return isSaved;
    }
    public ArrayList<EquipmentUsageDto> getAllUsages() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from EquipmentUsage");

        ArrayList<EquipmentUsageDto> equipmentUsageDtos = new ArrayList<>();

        while (rst.next()){
            EquipmentUsageDto equipmentUsageDto = new EquipmentUsageDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDate(5)

            );
            equipmentUsageDtos.add(equipmentUsageDto);
        }
        return equipmentUsageDtos;
    }
    public static boolean updateUsage(EquipmentUsageDto equipmentUsageDto) throws SQLException {
        return CrudUtil.execute(
                "update EquipmentUsage set  LaborID=?,  UseDate=?  where EquipmentID=?",


                equipmentUsageDto.getLaborID(),
                equipmentUsageDto.getUseDate(),
                equipmentUsageDto.getEquipmentID()


        );
    }
    public boolean deleteEquipment(Date UseDate) throws SQLException {
        return CrudUtil.execute("delete from EquipmentUsage where UseDate=?",UseDate);
    }

}



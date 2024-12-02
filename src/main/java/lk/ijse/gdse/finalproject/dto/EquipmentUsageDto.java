package lk.ijse.gdse.finalproject.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class EquipmentUsageDto {
    private String EquipmentID;
    private String EquipmentName;
    private String LaborID;
    private String LaborName;
    private Date UseDate;

}
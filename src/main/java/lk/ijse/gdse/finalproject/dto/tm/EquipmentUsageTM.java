package lk.ijse.gdse.finalproject.dto.tm;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class EquipmentUsageTM {
    private String EquipmentID;
    private String EquipmentName;
    private String LaborID;
    private String LaborName;
    private Date UseDate;

}


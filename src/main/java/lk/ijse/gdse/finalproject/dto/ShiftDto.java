package lk.ijse.gdse.finalproject.dto;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ShiftDto {
    private String shiftID;
    private String LaborID;
    private String SupervisorID;
    private Date ShiftDate;
    private String StartTime;
    private String EndTime;
    private String OverTime;


}
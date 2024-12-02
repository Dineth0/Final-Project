package lk.ijse.gdse.finalproject.dto.tm;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ShiftTM {
    private String shiftID;
    private String LaborID;
    private String SupervisorID;
    private Date ShiftDate;
    private String StartTime;
    private String EndTime;
    private String OverTime;
}

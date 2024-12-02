package lk.ijse.gdse.finalproject.dto.tm;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttendanceTM {
    private String AttendanceID;
    private String LaborID;
    private String SupervisorID;
    private Date AttendDate;
    private String Status;


}

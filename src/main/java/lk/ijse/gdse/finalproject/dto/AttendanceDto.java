package lk.ijse.gdse.finalproject.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttendanceDto {
    private String AttendanceID;
    private String LaborID;
    private String SupervisorID;
    private Date AttendDate;  // Change from 'Data' to 'Date'
    private String Status;



}

package lk.ijse.gdse.finalproject.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class LeaveDto {
    private String LeaveID;
    private String LaborID;
    private String Name;
    private String OfficerID;
    private Date LeaveDate;
    private String Reason;
    private String Status;

    public LeaveDto(String leaveID, String laborID, String officerID, String date, String reason, String status) {
    }
}

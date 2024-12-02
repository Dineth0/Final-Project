package lk.ijse.gdse.finalproject.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ComplaintDto {
    private String ComplaintID;
    private String LaborID;
    private String Name;
    private String Description;
    private Date ComplaintDate;
    private String Manager_Seen;


    public ComplaintDto(String feedbackID, String laborID, String date, String description, String managerSeen) {
    }
}

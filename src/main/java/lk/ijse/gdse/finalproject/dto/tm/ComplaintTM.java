package lk.ijse.gdse.finalproject.dto.tm;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ComplaintTM {
    private String ComplaintID;
    private String LaborID;
    private String Name;
    private String Description;
    private Date ComplaintDate;
    private String ManagerSeen;

}

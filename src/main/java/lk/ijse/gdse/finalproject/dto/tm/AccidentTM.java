package lk.ijse.gdse.finalproject.dto.tm;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class AccidentTM {
    private String AccidentID;
    private String LaborID;
    private String Name;
    private String OfficerID;
    private Date AccidentDate;
    private String Description;
}

package lk.ijse.gdse.finalproject.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class AccidentDto {
    private String AccidentID;
    private String LaborID;
    private String Name;
    private String OfficerID;
    private Date AccidentDate;
    private String Description;

    public AccidentDto(String accidentID, String laborID, String date, String description) {
    }

    public AccidentDto(String accidentID, String laborID, String officerID, String date, String description) {
    }
}

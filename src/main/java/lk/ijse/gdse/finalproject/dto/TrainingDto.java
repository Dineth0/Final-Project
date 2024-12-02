package lk.ijse.gdse.finalproject.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class TrainingDto {
    private String TrainingID;
    private String LaborID;
    private String OfficerID;
    private String Description;
    private Date TrainingDate;
}
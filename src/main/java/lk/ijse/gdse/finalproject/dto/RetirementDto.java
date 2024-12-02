package lk.ijse.gdse.finalproject.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class RetirementDto {
    private String RetirementID;
    private String LaborID;
    private String Name;
    private String OfficerID;
    private Date RetirementDate;
    private int TotalYearsWorked;
    private double FundPayment;



}

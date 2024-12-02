package lk.ijse.gdse.finalproject.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class InsuranceDto {
    private String PolicyNumber ;
    private String AccidentID ;
    private String Name ;
    private String OfficerID ;
    private double InsurancePayment ;


    public InsuranceDto(String policyNumber, String accidentID, String officerID, double insurancePayment) {
    }
}


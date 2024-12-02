package lk.ijse.gdse.finalproject.dto;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class PaymentDto {
    private String PaymentID;
    private String LaborID;
    private String Name;
    private String OfficerID;
    private double Day_Basic_Salary;
    private double Monthly_Total_Salary;
    private String Status;

    public PaymentDto(String paymentID, String laborID, String officerID, String dayBasicSalary, String monthlyTotalSalary, String status) {
    }

    public PaymentDto(String paymentID, String laborID, String officerID, String dayBasicSalary, String status) {
    }
}

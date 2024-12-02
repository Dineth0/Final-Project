package lk.ijse.gdse.finalproject.dto.tm;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class PaymentTM {
    private String PaymentID;
    private String LaborID;
    private String Name;
    private String OfficerID;
    private double Day_Basic_Salary;
    private double Monthly_Total_Salary;
    private String Status;

    public PaymentTM(String paymentID, Object o, String laborID, String name, String officerID, double dayBasicSalary, double monthlyTotalSalary) {
    }
}

package lk.ijse.gdse.finalproject.dto.tm;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class InsuranceTM {
   private String PolicyNumber ;
   private String AccidentID ;
   private String LaborName ;
   private String OfficerID ;
   private double InsurancePayment ;
}

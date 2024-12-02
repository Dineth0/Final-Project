package lk.ijse.gdse.finalproject.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Union_MembershipDto {
    private String MembershipID;
    private String LaborID;
    private String Name;
    private String Status;

    public Union_MembershipDto(String membershipID, String laborID, String status) {
    }
}

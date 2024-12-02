package lk.ijse.gdse.finalproject.model;

import lk.ijse.gdse.finalproject.dto.LaborDto;
import lk.ijse.gdse.finalproject.dto.Union_MembershipDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Union_MembershipModel {
    public String getNextMembershipID() throws SQLException {

        ResultSet rst =  CrudUtil.execute("select MembershipID from Union_Membership order by MembershipID desc limit 1");

        if (rst.next()){
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i+1;

            return String.format("M%03d",newIdIndex);
        }
        return  "M001";
    }
    public boolean saveMember(Union_MembershipDto union_membershipDto) throws SQLException {

        boolean isSaved = CrudUtil.execute(
                "insert into Union_Membership values (?,?,?,?)",
                union_membershipDto.getMembershipID(),
                union_membershipDto.getLaborID(),
                union_membershipDto.getName(),
                union_membershipDto.getStatus()
        );
        return isSaved;
    }
    public ArrayList<Union_MembershipDto> getAllMembers() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from Union_Membership");

        ArrayList<Union_MembershipDto> union_membershipDtos = new ArrayList<>();

        while (rst.next()){
            Union_MembershipDto union_membershipDto = new Union_MembershipDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            );
            union_membershipDtos.add(union_membershipDto);
        }
        return union_membershipDtos;
    }

    public static boolean updateMember(Union_MembershipDto union_membershipDto) throws SQLException {
        return CrudUtil.execute(
                "update Union_Membership set  LaborID=?, Name=?, Status=? where MembershipID=?",

                union_membershipDto.getLaborID(),
                union_membershipDto.getName(),
                union_membershipDto.getStatus(),
                union_membershipDto.getMembershipID()
        );
    }

    public boolean deleteMember(String MembershipID) throws SQLException {
        return CrudUtil.execute("delete from Union_Membership where MembershipID=?",MembershipID);
    }

}

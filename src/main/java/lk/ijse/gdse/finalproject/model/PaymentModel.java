package lk.ijse.gdse.finalproject.model;

import lk.ijse.gdse.finalproject.db.DBConnection;
import lk.ijse.gdse.finalproject.dto.LeaveDto;
import lk.ijse.gdse.finalproject.dto.PaymentDto;
import lk.ijse.gdse.finalproject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentModel {
    public String getNextPaymentID() throws SQLException {


        ResultSet resultSet = CrudUtil.execute("SELECT PaymentID FROM Payment ORDER BY PaymentID DESC LIMIT 1");
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i+1;


            return String.format("P%03d", newIdIndex);
        }
        return "P001";
    }
    public boolean savePayment(PaymentDto paymentDto) throws SQLException {

        boolean isSaved = CrudUtil.execute(
                "insert into Payment values(?,?,?,?,?,?,?)",
        paymentDto.getPaymentID(),
        paymentDto.getLaborID(),
        paymentDto.getName(),
        paymentDto.getOfficerID(),
        paymentDto.getDay_Basic_Salary(),
        paymentDto.getMonthly_Total_Salary(),
        paymentDto.getStatus()

);
        return isSaved;
    }
    public ArrayList<PaymentDto> getAllPayments() throws SQLException {
        ResultSet rst =  CrudUtil.execute("select * from Payment");

        ArrayList<PaymentDto> paymentDtos = new ArrayList<>();

        while (rst.next()){
            PaymentDto paymentDto = new PaymentDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getDouble(6),
                    rst.getString(7)

            );
            paymentDtos.add(paymentDto);
        }
        return paymentDtos;
    }

    public boolean deletePayment(String PaymentID) throws SQLException {
        return CrudUtil.execute("delete from Payment where PaymentID=?",PaymentID);
    }
    public static boolean updatePayment(PaymentDto paymentDto) throws SQLException {
        return CrudUtil.execute(
                "update Payment set  LaborID=?, OfficerID=?, Day_Basic_Salary=?,Monthly_Total_Salary=?,Status =? where PaymentID=?",
                paymentDto.getLaborID(),
                paymentDto.getOfficerID(),
                paymentDto.getDay_Basic_Salary(),
                paymentDto.getMonthly_Total_Salary(),
                paymentDto.getStatus(),
                paymentDto.getPaymentID()
        );
    }
    public int getTotalPaymentCount() throws SQLException{
        String query = "select count(*) from Payment where Status='Payment Taken'";
        ResultSet rst = CrudUtil.execute(query);

        if(rst.next()){
            return rst.getInt(1);
        }
        return 0;
    }
}

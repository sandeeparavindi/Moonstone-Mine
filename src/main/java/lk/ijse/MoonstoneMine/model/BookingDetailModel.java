package lk.ijse.MoonstoneMine.model;

import lk.ijse.MoonstoneMine.db.DbConnection;
import lk.ijse.MoonstoneMine.dto.tm.CartTm;
import lk.ijse.MoonstoneMine.dto.tm.TicketCartTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BookingDetailModel {
    public boolean saveBookingDetail(String bookingId, List<TicketCartTm> tmList) throws SQLException {
        for (TicketCartTm cartTm : tmList) {
            if(!saveBookingDetail(bookingId, cartTm)) {
                return false;
            }
        }
        return true;
    }

    private boolean saveBookingDetail(String bookingId, TicketCartTm cartTm) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO booking_detail VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, bookingId);
        pstm.setString(2, cartTm.getCode());
        pstm.setInt(3, cartTm.getQty());
        pstm.setDouble(4, cartTm.getPrice());

        return pstm.executeUpdate() > 0;
    }
}

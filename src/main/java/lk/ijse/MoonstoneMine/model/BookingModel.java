package lk.ijse.MoonstoneMine.model;

import lk.ijse.MoonstoneMine.db.DbConnection;

import java.sql.*;
import java.time.LocalDate;

public class BookingModel {
    public static boolean saveBooking(String bookingId, String cusId, LocalDate date) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO booking VALUES(?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, bookingId);
        pstm.setString(2, cusId);
        pstm.setDate(3, Date.valueOf(date));

        return pstm.executeUpdate() > 0;
    }

    public String generateNextBookingId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT booking_id FROM booking ORDER BY booking_id DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String currentBookingId = null;

        if (resultSet.next()) {
            currentBookingId = resultSet.getString(1);
            return splitBookingId(currentBookingId);
        }
        return splitBookingId(null);
    }

    private String splitBookingId(String currentBookingId) {
        if (currentBookingId != null) {
            String[] split = currentBookingId.split("O");
            int id = Integer.parseInt(split[1]);
            id++;
            return "O00" + id;
        }
        return "O001";
    }
}

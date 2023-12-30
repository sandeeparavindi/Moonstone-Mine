package lk.ijse.MoonstoneMine.model;

import lk.ijse.MoonstoneMine.db.DbConnection;
import lk.ijse.MoonstoneMine.dto.PlaceOrderDto;
import lk.ijse.MoonstoneMine.dto.TicketBookingDto;

import java.sql.Connection;
import java.sql.SQLException;

public class TicketBookingModel {
    private final BookingModel bookingModel = new BookingModel();
    private final TicketModel ticketModel = new TicketModel();
    private final BookingDetailModel bookingDetailModel = new BookingDetailModel();

    public boolean ticketBooking(TicketBookingDto pDto) throws SQLException {
        boolean result = false;
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isBookingSaved = BookingModel.saveBooking(pDto.getBookingId(), pDto.getCusId(), pDto.getDate());
            if (isBookingSaved) {
                boolean isUpdated = ticketModel.updateTicket(pDto.getTmList());
                if(isUpdated) {
                    boolean isBookingDetailSaved = bookingDetailModel.saveBookingDetail(pDto.getBookingId(), pDto.getTmList());
                    if(isBookingDetailSaved) {
                        connection.commit();
                        result = true;
                    }
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
        return result;
    }
}

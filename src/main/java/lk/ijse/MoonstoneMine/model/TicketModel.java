package lk.ijse.MoonstoneMine.model;

import lk.ijse.MoonstoneMine.db.DbConnection;
import lk.ijse.MoonstoneMine.dto.ItemDto;
import lk.ijse.MoonstoneMine.dto.TicketDto;
import lk.ijse.MoonstoneMine.dto.tm.CartTm;
import lk.ijse.MoonstoneMine.dto.tm.TicketCartTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketModel {
    public boolean deleteTicket(String code) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM ticket WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, code);

        return pstm.executeUpdate() > 0;
    }

    public List<TicketDto> loadAllTickets() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM ticket";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<TicketDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            var dto = new TicketDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );

            dtoList.add(dto);
        }

        return dtoList;
    }
    public boolean addTicket(TicketDto ticketDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO ticket VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, ticketDto.getCode());
        pstm.setString(2, ticketDto.getType());
        pstm.setDouble(3, ticketDto.getPrice());
        pstm.setInt(4, ticketDto.getQtyOnHand());

        return pstm.executeUpdate() > 0;
    }
    public boolean updateTicket(TicketDto ticketDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE ticket SET type = ?, price = ?, qty_on_hand = ? WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, ticketDto.getType());
        pstm.setDouble(2, ticketDto.getPrice());
        pstm.setInt(3, ticketDto.getQtyOnHand());
        pstm.setString(4, ticketDto.getCode());

        return pstm.executeUpdate() > 0;
    }

    public boolean updateTicket(List<TicketCartTm> tmList) throws SQLException {
        for ( TicketCartTm cartTm : tmList) {
            if(!updateQty(cartTm)) {
                return false;
            }
        }
        return true;
    }
    private boolean updateQty(TicketCartTm cartTm) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE ticket SET qty_on_hand = qty_on_hand - ? WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, cartTm.getQty());
        pstm.setString(2, cartTm.getCode());

        return pstm.executeUpdate() > 0;
    }

    public TicketDto searchTicket(String code) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM ticket WHERE code = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, code);

        ResultSet resultSet = pstm.executeQuery();

        TicketDto dto = null;

        if(resultSet.next()) {
            dto = new TicketDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        }
        return dto;
    }

}

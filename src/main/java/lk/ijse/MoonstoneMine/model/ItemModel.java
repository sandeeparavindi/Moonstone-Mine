package lk.ijse.MoonstoneMine.model;

import lk.ijse.MoonstoneMine.db.DbConnection;
import lk.ijse.MoonstoneMine.dto.ItemDto;
import lk.ijse.MoonstoneMine.dto.tm.CartTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemModel {

    public boolean addItem(ItemDto itemDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO item VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, itemDto.getCode());
        pstm.setString(2, itemDto.getDescription());
        pstm.setDouble(3, itemDto.getUnitPrice());
        pstm.setInt(4, itemDto.getQtyOnHand());

        return pstm.executeUpdate() > 0;
    }

    public boolean deleteItem(String code) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM item WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, code);

        return pstm.executeUpdate() > 0;
    }

    public List<ItemDto> loadAllItems() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM item";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<ItemDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            var dto = new ItemDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );

            dtoList.add(dto);
        }

        return dtoList;
    }

    public boolean updateItem(ItemDto itemDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE item SET description = ?, unit_price = ?, qty_on_hand = ? WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, itemDto.getDescription());
        pstm.setDouble(2, itemDto.getUnitPrice());
        pstm.setInt(3, itemDto.getQtyOnHand());
        pstm.setString(4, itemDto.getCode());

        return pstm.executeUpdate() > 0;
    }

    public boolean updateItem(List<CartTm> tmList) throws SQLException {
        for (CartTm cartTm : tmList) {
            if(!updateQty(cartTm)) {
                return false;
            }
        }
        return true;
    }

    private boolean updateQty(CartTm cartTm) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE item SET qty_on_hand = qty_on_hand - ? WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, cartTm.getQty());
        pstm.setString(2, cartTm.getCode());

        return pstm.executeUpdate() > 0; //true
    }

   public ItemDto searchItem(String code) throws SQLException {
       Connection connection = DbConnection.getInstance().getConnection();
       String sql = "SELECT * FROM item WHERE code = ?";

       PreparedStatement pstm = connection.prepareStatement(sql);
       pstm.setString(1, code);

       ResultSet resultSet = pstm.executeQuery();

       ItemDto dto = null;

       if(resultSet.next()) {
           dto = new ItemDto(
                   resultSet.getString(1),
                   resultSet.getString(2),
                   resultSet.getDouble(3),
                   resultSet.getInt(4)
           );
       }
       return dto;
   }

    public String totalItemCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(*) AS ItemCount FROM item";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();


        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }
}

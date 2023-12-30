package lk.ijse.MoonstoneMine.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.MoonstoneMine.db.DbConnection;
import lk.ijse.MoonstoneMine.model.IncomeReport;
import lk.ijse.MoonstoneMine.model.IncomeTour;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MonthlyIncomeTourFormController implements Initializable {
    @FXML
    private TableColumn<IncomeTour, String> colBookingCount;

    @FXML
    private TableColumn<IncomeTour, String> colCost;

    @FXML
    private TableColumn<IncomeTour, String> colITicketSoldQty;

    @FXML
    private TableColumn<IncomeTour, String> colMonth;

    @FXML
    private TableView<IncomeTour> tblReport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colMonth.setCellValueFactory(new PropertyValueFactory<>("date"));
        colBookingCount.setCellValueFactory(new PropertyValueFactory<>("numberOfBooking"));
        colITicketSoldQty.setCellValueFactory(new PropertyValueFactory<>("numberOfSoldTicket"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("finalCost"));

        try {
            tblReport.setItems(loadMonthlyIncomeTour());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<IncomeTour> loadMonthlyIncomeTour() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT\n" +
                        "    EXTRACT(MONTH FROM o.date) AS month,\n" +
                        "    COUNT(o.booking_id) AS numberOfBooking,\n" +
                        "    SUM(bd.qty) AS numberOfSoldTicket,\n" +
                        "    SUM(bd.qty * t.price) AS finalCost\n" +
                        "FROM\n" +
                        "    booking o\n" +
                        "JOIN\n" +
                        "    booking_detail bd ON o.booking_id = bd.booking_id\n" +
                        "JOIN\n" +
                        "    ticket t ON bd.code = t.code\n" +
                        "GROUP BY\n" +
                        "    EXTRACT(MONTH FROM o.date)\n"
        ).executeQuery();
        ObservableList<IncomeTour> tempo = FXCollections.observableArrayList();

        while (resultSet.next()) {

            String date = resultSet.getString(1);
            int countBookingId = resultSet.getInt(3);
            int numberOfSoldTicket = resultSet.getInt(2);
            double sumOfTotal = resultSet.getDouble(4);

            tempo.add(new IncomeTour(date, countBookingId, numberOfSoldTicket, sumOfTotal));
        }
        return tempo;
    }
}

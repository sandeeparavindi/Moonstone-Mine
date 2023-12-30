package lk.ijse.MoonstoneMine.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.MoonstoneMine.db.DbConnection;
import lk.ijse.MoonstoneMine.model.IncomeReport;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MonthlyIncomeFormController implements Initializable {
    @FXML
    private TableColumn<IncomeReport, String> colCost;

    @FXML
    private TableColumn<IncomeReport, String> colMonth;

    @FXML
    private TableColumn<IncomeReport, String> colItemSoldQty;

    @FXML
    private TableColumn<IncomeReport,String> colOrderCount;

    @FXML
    private AnchorPane monthlyIncomeReportContext;

    @FXML
    private TableView<IncomeReport> tblReport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colMonth.setCellValueFactory(new PropertyValueFactory<>("date"));
        colOrderCount.setCellValueFactory(new PropertyValueFactory<>("numberOfOrders"));
        colItemSoldQty.setCellValueFactory(new PropertyValueFactory<>("numberOfSoldItem"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("finalCost"));

        try {
            tblReport.setItems(loadMonthlyIncomeReport());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<IncomeReport> loadMonthlyIncomeReport() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT EXTRACT(MONTH FROM o.date) AS month, " +
                        "COUNT(o.order_id) AS numberOfOrders, " +
                        "SUM(od.qty) AS numberOfSoldItem, " +
                        "SUM(od.qty * od.unit_price) AS finalCost " +
                        "FROM orders o " +
                        "JOIN order_detail od ON o.order_id = od.order_id " +
                        "GROUP BY EXTRACT(MONTH FROM o.date)"
        ).executeQuery();
        ObservableList<IncomeReport> tempo = FXCollections.observableArrayList();

        while (resultSet.next()) {

            String date = resultSet.getString(1);
            int countOrderId = resultSet.getInt(3);
            int numberOfSoldItem = resultSet.getInt(2);
            double sumOfTotal = resultSet.getDouble(4);

            tempo.add(new IncomeReport(date, countOrderId, numberOfSoldItem, sumOfTotal));
        }
        return tempo;
    }
}

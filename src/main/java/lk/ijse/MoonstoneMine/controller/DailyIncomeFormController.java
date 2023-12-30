package lk.ijse.MoonstoneMine.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.MoonstoneMine.db.DbConnection;
import lk.ijse.MoonstoneMine.model.IncomeReport;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DailyIncomeFormController implements Initializable {
    @FXML
    private TableColumn<IncomeReport, String> colOrderCount;

    @FXML
    private TableColumn<IncomeReport, String> colCost;

    @FXML
    private TableColumn<IncomeReport, String> colDate;

    @FXML
    private TableColumn<IncomeReport, String> colItemQty;

    @FXML
    private TableView<IncomeReport> tblReport;

    @FXML
    private AnchorPane dailyIncomeReportContext;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
    colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    //colOrderCount.setCellValueFactory(new PropertyValueFactory<>("numOfOrders"));
    colItemQty.setCellValueFactory(new PropertyValueFactory<>("numberOfSoldItem"));
    colCost.setCellValueFactory(new PropertyValueFactory<>("finalCost"));
        try {

            tblReport.setItems(loadDailyIncomeReport());
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            pieChartData.add(new PieChart.Data("Iphone 5S", 13));

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private ObservableList<IncomeReport> loadDailyIncomeReport() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT orders.date,count(orders.order_id),sum(order_detail.unit_price) FROM orders INNER JOIN order_detail ON orders.order_id = order_detail.order_id GROUP BY date").executeQuery();
        ObservableList<IncomeReport> obList = FXCollections.observableArrayList();
        ArrayList<IncomeReport> data = getCountItems();

        int i = 0;
        while (resultSet.next()) {

            String date = resultSet.getString(1);
            int countOrderId = resultSet.getInt(2);
            double sumOfTotal = resultSet.getDouble(3);

            obList.add(new IncomeReport(date, countOrderId, data.get(i).getNumberOfSoldItem(), sumOfTotal));
            i++;
        }
        return obList;
    }

    private ArrayList<IncomeReport> getCountItems() throws SQLException, ClassNotFoundException {
        ResultSet rest = DbConnection.getInstance().getConnection().prepareStatement("SELECT DISTINCT(orders.date),sum(order_detail.qty) FROM orders INNER JOIN order_detail ON  orders.order_id = order_detail.order_id " + "GROUP BY orders.date").executeQuery();
        ArrayList<IncomeReport> temp = new ArrayList<>();

        while (rest.next()) {

            temp.add(new IncomeReport(rest.getString(1), rest.getInt(2)));
        }
        return temp;
    }
}

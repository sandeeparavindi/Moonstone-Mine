package lk.ijse.MoonstoneMine.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.MoonstoneMine.db.DbConnection;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BarChartFormController {
    @FXML
    private AreaChart<?, ?> barChart;

    @FXML
    private AnchorPane barChartAnchorPane;

    /* ---------load all customer base growth---- */
    public void initialize() throws SQLException, ClassNotFoundException {

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("2023");

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT date, COUNT(*) FROM orders GROUP BY date");
        ResultSet rst = stm.executeQuery();
        String prevDay = "";

        while (rst.next()) {
            String date = rst.getString(1);
            int count = rst.getInt(2);
            series1.getData().add(new XYChart.Data<>(date, count));
            prevDay = date;
        }
        barChart.getData().addAll(series1);
    }

    @FXML
    void clickOnAction(ActionEvent event) {
        Navigation.adminORcashierUI("dailyIncome_form", barChartAnchorPane);
    }

    @FXML
    void monthlyClickOnAction(ActionEvent event) {
        Navigation.adminORcashierUI("monthlyIncome_form", barChartAnchorPane);
    }

}

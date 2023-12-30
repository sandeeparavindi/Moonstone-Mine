package lk.ijse.MoonstoneMine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class IncomeReport {
    private String date;
    private int numberOfOrders;
    private int numberOfSoldItem;
    private double finalCost;

    public IncomeReport(String date, int numberOfSoldItem) {
        this.date = date;
        this.numberOfSoldItem = numberOfSoldItem;
    }
}

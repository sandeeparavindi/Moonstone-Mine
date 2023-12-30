package lk.ijse.MoonstoneMine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class IncomeTour {

    private String date;
    private int numberOfBooking;
    private int numberOfSoldTicket;
    private double finalCost;

    public IncomeTour(String date, int numberOfSoldTicket) {
        this.date = date;
        this.numberOfSoldTicket = numberOfSoldTicket;
    }
}

package lk.ijse.MoonstoneMine.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TicketCartTm {
    private String code;
    private String type;
    private int qty;
    private double Price;
    private double tot;
    private Button btn;
}

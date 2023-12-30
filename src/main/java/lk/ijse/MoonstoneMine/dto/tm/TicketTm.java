package lk.ijse.MoonstoneMine.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TicketTm {
    private String code;
    private String type;
    private double Price;
    private int qtyOnHand;
    private Button btn;
}

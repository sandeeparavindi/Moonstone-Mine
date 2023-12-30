package lk.ijse.MoonstoneMine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TicketDto {
    private String code;
    private String type;
    private double Price;
    private int qtyOnHand;
}

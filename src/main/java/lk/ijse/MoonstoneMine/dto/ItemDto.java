package lk.ijse.MoonstoneMine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemDto {
    private String code;
    private String description;
    private double unitPrice;
    private int qtyOnHand;
}
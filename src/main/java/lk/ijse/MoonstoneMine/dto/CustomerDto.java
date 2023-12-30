package lk.ijse.MoonstoneMine.dto;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDto {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String email;
}
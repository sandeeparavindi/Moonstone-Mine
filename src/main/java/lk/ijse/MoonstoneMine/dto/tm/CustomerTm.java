package lk.ijse.MoonstoneMine.dto.tm;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerTm {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String email;
}
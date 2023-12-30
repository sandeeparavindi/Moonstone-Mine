package lk.ijse.MoonstoneMine.dto;

import lk.ijse.MoonstoneMine.dto.tm.CartTm;
import lk.ijse.MoonstoneMine.dto.tm.TicketCartTm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class TicketBookingDto {
    private String bookingId;
    private String cusId;
    private LocalDate date;
    private List<TicketCartTm> tmList = new ArrayList<>();
}

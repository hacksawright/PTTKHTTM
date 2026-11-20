package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Order8(int id, LocalDateTime orderedAt, String status, String shippingAddress,
                     BigDecimal shippingFee, BigDecimal totalAmount, String note,
                     Customer8 customer, DetailOrder8[] details, Payment8 payment) {
    public String code() { return "HD%03d".formatted(id); }
}

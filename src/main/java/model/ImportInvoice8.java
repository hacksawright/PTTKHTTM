package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ImportInvoice8(int id, LocalDate date, String status, Supplier8 supplier,
                             Manager8 createdBy, DetailImportInvoice8[] details,
                             int totalQuantity, BigDecimal totalAmount) {
    public String code() { return "PN%04d".formatted(id); }
}

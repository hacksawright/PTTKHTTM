package model;

import java.math.BigDecimal;

public record DetailImportInvoice8(int id, int quantity, BigDecimal unitPrice, Product8 product) {
    public BigDecimal amount() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}

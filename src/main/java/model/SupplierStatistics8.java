package model;

import java.math.BigDecimal;

public record SupplierStatistics8(Supplier8 supplier, int totalImportQuantity,
                                  BigDecimal totalImportAmount, int totalImportCount) {}

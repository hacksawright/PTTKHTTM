package model;

import java.math.BigDecimal;

public record Product8(int id, String name, String category, String unit, BigDecimal costPrice,
                       BigDecimal price, int stock, String description, String image) {}

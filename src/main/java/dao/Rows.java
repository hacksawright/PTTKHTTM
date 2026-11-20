package dao;

import model.Product8;
import model.Supplier8;
import java.sql.ResultSet;
import java.sql.SQLException;

final class Rows {
    static final String PRODUCT = "p.id productId, p.name productName, p.category, p.unit, "
            + "p.costPrice, p.price, p.stock, p.description, p.image";
    static final String SUPPLIER = "s.id supplierId, s.name supplierName, s.phone supplierPhone, "
            + "s.email supplierEmail, s.address supplierAddress, s.contactPerson";

    private Rows() {}

    static Product8 product(ResultSet r) throws SQLException {
        return new Product8(r.getInt("productId"), r.getString("productName"), r.getString("category"),
                r.getString("unit"), r.getBigDecimal("costPrice"), r.getBigDecimal("price"),
                r.getInt("stock"), r.getString("description"), r.getString("image"));
    }

    static Supplier8 supplier(ResultSet r) throws SQLException {
        return new Supplier8(r.getInt("supplierId"), r.getString("supplierName"), r.getString("supplierPhone"),
                r.getString("supplierEmail"), r.getString("supplierAddress"), r.getString("contactPerson"));
    }
}

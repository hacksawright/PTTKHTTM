package dao;

import model.DetailOrder8;
import model.Order8;
import model.Payment8;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderDAO8 extends DAO {
    public OrderDAO8() {}

    public Order8[] getInvoiceListByCustomer(int customerId, String statusFilter) throws SQLException {
        String sql = """
            SELECT o.*, o.shippingFee + COALESCE((
                SELECT SUM(d.quantity * d.unitPrice) FROM tbldetailorder8 d WHERE d.orderId=o.id
            ), 0) AS totalAmount
            FROM tblorder8 o
            WHERE o.customerId=? AND (?='ALL' OR o.status=?)
            ORDER BY o.orderedAt DESC, o.id DESC
            """;
        var orders = new ArrayList<Order8>();
        try (var con = openConnection(); var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.setString(2, statusFilter);
            stmt.setString(3, statusFilter);
            try (var r = stmt.executeQuery()) {
                while (r.next()) {
                    var payment = new Payment8(r.getString("paymentMethod"), r.getString("paymentStatus"),
                            r.getObject("paidAt", java.time.LocalDate.class));
                    orders.add(new Order8(r.getInt("id"), r.getObject("orderedAt", LocalDateTime.class),
                            r.getString("status"), r.getString("shippingAddress"), r.getBigDecimal("shippingFee"),
                            r.getBigDecimal("totalAmount"), r.getString("note"), null, new DetailOrder8[0], payment));
                }
            }
        }
        return orders.toArray(Order8[]::new);
    }
}

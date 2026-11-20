package dao;

import model.Customer8;
import model.DetailOrder8;
import model.Order8;
import model.Payment8;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DetailOrderDAO8 extends DAO {
    public DetailOrderDAO8() {}

    public Order8 getInvoiceDetail(int orderId, int customerId) throws SQLException {
        String sql = """
            SELECT o.*, m.fullname, m.phone, m.address, d.id lineId, d.quantity, d.unitPrice, %s
            FROM tblorder8 o
            JOIN tblmember8 m ON m.id=o.customerId
            LEFT JOIN tbldetailorder8 d ON d.orderId=o.id
            LEFT JOIN tblproduct8 p ON p.id=d.productId
            WHERE o.id=? AND o.customerId=?
            ORDER BY d.id
            """.formatted(Rows.PRODUCT);
        try (var con = openConnection(); var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, customerId);
            try (var r = stmt.executeQuery()) {
                if (!r.next()) return null;
                var orderedAt = r.getObject("orderedAt", LocalDateTime.class);
                var customer = new Customer8(customerId, r.getString("fullname"), r.getString("phone"), r.getString("address"));
                var payment = new Payment8(r.getString("paymentMethod"), r.getString("paymentStatus"),
                        r.getObject("paidAt", LocalDate.class));
                String shippingAddress = r.getString("shippingAddress"), status = r.getString("status"), note = r.getString("note");
                var shippingFee = r.getBigDecimal("shippingFee");
                var total = shippingFee;
                var lines = new ArrayList<DetailOrder8>();
                do {
                    if (r.getObject("lineId") != null) {
                        var line = new DetailOrder8(r.getInt("lineId"), r.getInt("quantity"), r.getBigDecimal("unitPrice"), Rows.product(r));
                        lines.add(line);
                        total = total.add(line.amount());
                    }
                } while (r.next());
                return new Order8(orderId, orderedAt, status, shippingAddress, shippingFee, total, note,
                        customer, lines.toArray(DetailOrder8[]::new), payment);
            }
        }
    }
}

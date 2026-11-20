package dao;

import model.SupplierStatistics8;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class SupplierDAO8 extends DAO {
    public SupplierDAO8() {}

    // Shared by summary and history so counts and rows use exactly the same rules.
    static final String IN_PERIOD = "i.status = 'COMPLETED' AND i.date BETWEEN ? AND ?";

    public SupplierStatistics8[] getSupplierStatisticsByImportQuantity(LocalDate startDate, LocalDate endDate)
            throws SQLException {
        String sql = """
            SELECT %s, COALESCE(SUM(d.quantity),0) totalQuantity,
                   COALESCE(SUM(d.quantity * d.unitPrice),0) totalAmount, COUNT(DISTINCT i.id) importCount
            FROM tblimportinvoice8 i
            JOIN tblsupplier8 s ON s.id=i.supplierId
            LEFT JOIN tbldetailimportinvoice8 d ON d.importInvoiceId=i.id
            WHERE %s GROUP BY s.id
            ORDER BY totalQuantity DESC, s.id
            """.formatted(Rows.SUPPLIER, IN_PERIOD);
        var rows = new ArrayList<SupplierStatistics8>();
        try (var con = openConnection(); var stmt = con.prepareStatement(sql)) {
            stmt.setObject(1, startDate);
            stmt.setObject(2, endDate);
            try (var r = stmt.executeQuery()) {
                while (r.next()) rows.add(new SupplierStatistics8(Rows.supplier(r), r.getInt("totalQuantity"),
                        r.getBigDecimal("totalAmount"), r.getInt("importCount")));
            }
        }
        return rows.toArray(SupplierStatistics8[]::new);
    }
}

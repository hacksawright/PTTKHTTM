package dao;

import model.DetailImportInvoice8;
import model.ImportInvoice8;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ImportInvoiceDAO8 extends DAO {
    public ImportInvoiceDAO8() {}

    public ImportInvoice8[] getImportInvoicesBySupplier(int supplierId, LocalDate startDate, LocalDate endDate)
            throws SQLException {
        String sql = """
            SELECT i.id, i.date, i.status, %s, COALESCE(SUM(d.quantity),0) totalQuantity,
                   COALESCE(SUM(d.quantity*d.unitPrice),0) totalAmount
            FROM tblimportinvoice8 i
            JOIN tblsupplier8 s ON s.id=i.supplierId
            LEFT JOIN tbldetailimportinvoice8 d ON d.importInvoiceId=i.id
            WHERE i.supplierId=? AND %s
            GROUP BY i.id, s.id ORDER BY i.date DESC, i.id DESC
            """.formatted(Rows.SUPPLIER, SupplierDAO8.IN_PERIOD);
        var invoices = new ArrayList<ImportInvoice8>();
        try (var con = openConnection(); var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, supplierId);
            stmt.setObject(2, startDate);
            stmt.setObject(3, endDate);
            try (var r = stmt.executeQuery()) {
                while (r.next()) invoices.add(new ImportInvoice8(r.getInt("id"),
                        r.getObject("date", LocalDate.class), r.getString("status"), Rows.supplier(r), null,
                        new DetailImportInvoice8[0], r.getInt("totalQuantity"), r.getBigDecimal("totalAmount")));
            }
        }
        return invoices.toArray(ImportInvoice8[]::new);
    }
}

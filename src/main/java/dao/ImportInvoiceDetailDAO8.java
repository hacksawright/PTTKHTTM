package dao;

import model.DetailImportInvoice8;
import model.ImportInvoice8;
import model.Manager8;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ImportInvoiceDetailDAO8 extends DAO {
    public ImportInvoiceDetailDAO8() {}

    public ImportInvoice8 getImportInvoiceDetail(int importInvoiceId) throws SQLException {
        String sql = """
            SELECT i.date,i.status,m.id creatorId,m.fullname,m.phone,m.address,st.position,
                   d.id lineId,d.quantity,d.unitPrice,%s,%s
            FROM tblimportinvoice8 i
            JOIN tblsupplier8 s ON s.id=i.supplierId
            JOIN tblstaff8 st ON st.id=i.createdById
            JOIN tblmember8 m ON m.id=st.id
            LEFT JOIN tbldetailimportinvoice8 d ON d.importInvoiceId=i.id
            LEFT JOIN tblproduct8 p ON p.id=d.productId
            WHERE i.id=? ORDER BY d.id
            """.formatted(Rows.SUPPLIER, Rows.PRODUCT);
        try (var con = openConnection(); var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, importInvoiceId);
            try (var r = stmt.executeQuery()) {
                if (!r.next()) return null;
                var date = r.getObject("date", LocalDate.class);
                var status = r.getString("status");
                var supplier = Rows.supplier(r);
                var creator = new Manager8(r.getInt("creatorId"), r.getString("fullname"), r.getString("phone"),
                        r.getString("address"), r.getString("position"));
                var details = new ArrayList<DetailImportInvoice8>();
                var amount = BigDecimal.ZERO;
                int quantity = 0;
                do {
                    if (r.getObject("lineId") != null) {
                        var line = new DetailImportInvoice8(r.getInt("lineId"), r.getInt("quantity"), r.getBigDecimal("unitPrice"), Rows.product(r));
                        details.add(line);
                        amount = amount.add(line.amount());
                        quantity += line.quantity();
                    }
                } while (r.next());
                return new ImportInvoice8(importInvoiceId, date, status, supplier, creator,
                        details.toArray(DetailImportInvoice8[]::new), quantity, amount);
            }
        }
    }
}

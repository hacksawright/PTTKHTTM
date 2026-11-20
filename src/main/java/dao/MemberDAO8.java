package dao;

import model.Customer8;
import model.Manager8;
import model.Member8;
import util.Passwords;
import java.sql.SQLException;

public final class MemberDAO8 extends DAO {
    public Member8 authenticate(String username, String password) throws SQLException {
        String sql = """
            SELECT m.id, m.fullname, m.phone, m.address, m.role, m.password, s.position
            FROM tblmember8 m LEFT JOIN tblstaff8 s ON s.id=m.id
            WHERE m.username=? AND (m.role='CUSTOMER' OR (m.role='MANAGER' AND s.id IS NOT NULL))
            """;
        try (var con = openConnection(); var stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (var r = stmt.executeQuery()) {
                if (!r.next() || !Passwords.matches(password, r.getString("password"))) return null;
                int id = r.getInt("id");
                String name = r.getString("fullname"), phone = r.getString("phone"), address = r.getString("address");
                return switch (r.getString("role")) {
                    case "CUSTOMER" -> new Customer8(id, name, phone, address);
                    case "MANAGER" -> new Manager8(id, name, phone, address, r.getString("position"));
                    default -> null;
                };
            }
        }
    }
}

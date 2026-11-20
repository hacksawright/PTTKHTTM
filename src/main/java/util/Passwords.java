package util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;

public final class Passwords {
    private Passwords() {}

    public static boolean matches(String password, String stored) {
        if (password == null || password.length() > 200 || stored == null) return false;
        var fields = stored.split("\\$", -1);
        if (fields.length != 3 || !"pbkdf2-sha256".equals(fields[0])) return false;
        PBEKeySpec spec = null;
        try {
            var decoder = Base64.getDecoder();
            byte[] expected = decoder.decode(fields[2]);
            if (expected.length != 32) return false;
            spec = new PBEKeySpec(password.toCharArray(), decoder.decode(fields[1]), 310000, 256);
            byte[] actual = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            return MessageDigest.isEqual(expected, actual);
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            return false;
        } finally {
            if (spec != null) spec.clearPassword();
        }
    }
}

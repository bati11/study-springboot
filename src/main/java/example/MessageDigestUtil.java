package example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtil {
    public static String sha256(String raw) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(raw.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : md.digest()) {
            String hex = String.format("%02x", b);
            sb.append(hex);
        }
        return sb.toString();
    }
}

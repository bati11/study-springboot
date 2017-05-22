package example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtil {
    public static String sha256(String raw) {
        return digest(raw, "SHA-256");
    }

    public static String md5(String raw) {
        return digest(raw, "MD5");
    }

    private static String digest(String raw, String algorithm) {
        if (raw == null || "".equals(raw)) return "";
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
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

package example.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URLEncodeUtil {
    public static String encode(String s) {
        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}

package eu.waldonia.mydeps;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @author sih
 */
public class ContentDecoder {

    public String decode(String encoded) {
        byte[] decoded = Base64.getDecoder().decode(encoded);
        return new String(decoded, Charset.forName("utf-8"));
    }
}

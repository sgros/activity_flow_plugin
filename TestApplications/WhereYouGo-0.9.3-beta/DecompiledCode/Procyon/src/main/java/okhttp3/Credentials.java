// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3;

import okio.ByteString;
import java.nio.charset.Charset;

public final class Credentials
{
    private Credentials() {
    }
    
    public static String basic(final String s, final String s2) {
        return basic(s, s2, Charset.forName("ISO-8859-1"));
    }
    
    public static String basic(String base64, final String str, final Charset charset) {
        base64 = ByteString.of((base64 + ":" + str).getBytes(charset)).base64();
        return "Basic " + base64;
    }
}

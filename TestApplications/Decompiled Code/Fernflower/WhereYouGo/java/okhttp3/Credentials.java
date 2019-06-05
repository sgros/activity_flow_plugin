package okhttp3;

import java.nio.charset.Charset;
import okio.ByteString;

public final class Credentials {
   private Credentials() {
   }

   public static String basic(String var0, String var1) {
      return basic(var0, var1, Charset.forName("ISO-8859-1"));
   }

   public static String basic(String var0, String var1, Charset var2) {
      var0 = ByteString.of((var0 + ":" + var1).getBytes(var2)).base64();
      return "Basic " + var0;
   }
}

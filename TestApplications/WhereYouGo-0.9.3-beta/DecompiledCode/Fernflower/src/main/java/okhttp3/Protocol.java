package okhttp3;

import java.io.IOException;

public enum Protocol {
   HTTP_1_0("http/1.0"),
   HTTP_1_1("http/1.1"),
   HTTP_2("h2"),
   SPDY_3("spdy/3.1");

   private final String protocol;

   private Protocol(String var3) {
      this.protocol = var3;
   }

   public static Protocol get(String var0) throws IOException {
      Protocol var1;
      if (var0.equals(HTTP_1_0.protocol)) {
         var1 = HTTP_1_0;
      } else if (var0.equals(HTTP_1_1.protocol)) {
         var1 = HTTP_1_1;
      } else if (var0.equals(HTTP_2.protocol)) {
         var1 = HTTP_2;
      } else {
         if (!var0.equals(SPDY_3.protocol)) {
            throw new IOException("Unexpected protocol: " + var0);
         }

         var1 = SPDY_3;
      }

      return var1;
   }

   public String toString() {
      return this.protocol;
   }
}

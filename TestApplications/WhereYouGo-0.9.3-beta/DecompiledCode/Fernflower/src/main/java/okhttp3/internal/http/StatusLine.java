package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Protocol;
import okhttp3.Response;

public final class StatusLine {
   public static final int HTTP_CONTINUE = 100;
   public static final int HTTP_PERM_REDIRECT = 308;
   public static final int HTTP_TEMP_REDIRECT = 307;
   public final int code;
   public final String message;
   public final Protocol protocol;

   public StatusLine(Protocol var1, int var2, String var3) {
      this.protocol = var1;
      this.code = var2;
      this.message = var3;
   }

   public static StatusLine get(Response var0) {
      return new StatusLine(var0.protocol(), var0.code(), var0.message());
   }

   public static StatusLine parse(String var0) throws IOException {
      int var1;
      byte var2;
      Protocol var3;
      if (var0.startsWith("HTTP/1.")) {
         if (var0.length() < 9 || var0.charAt(8) != ' ') {
            throw new ProtocolException("Unexpected status line: " + var0);
         }

         var1 = var0.charAt(7) - 48;
         var2 = 9;
         if (var1 == 0) {
            var3 = Protocol.HTTP_1_0;
         } else {
            if (var1 != 1) {
               throw new ProtocolException("Unexpected status line: " + var0);
            }

            var3 = Protocol.HTTP_1_1;
         }
      } else {
         if (!var0.startsWith("ICY ")) {
            throw new ProtocolException("Unexpected status line: " + var0);
         }

         var3 = Protocol.HTTP_1_0;
         var2 = 4;
      }

      if (var0.length() < var2 + 3) {
         throw new ProtocolException("Unexpected status line: " + var0);
      } else {
         try {
            var1 = Integer.parseInt(var0.substring(var2, var2 + 3));
         } catch (NumberFormatException var5) {
            throw new ProtocolException("Unexpected status line: " + var0);
         }

         String var4 = "";
         if (var0.length() > var2 + 3) {
            if (var0.charAt(var2 + 3) != ' ') {
               throw new ProtocolException("Unexpected status line: " + var0);
            }

            var4 = var0.substring(var2 + 4);
         }

         return new StatusLine(var3, var1, var4);
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      String var2;
      if (this.protocol == Protocol.HTTP_1_0) {
         var2 = "HTTP/1.0";
      } else {
         var2 = "HTTP/1.1";
      }

      var1.append(var2);
      var1.append(' ').append(this.code);
      if (this.message != null) {
         var1.append(' ').append(this.message);
      }

      return var1.toString();
   }
}

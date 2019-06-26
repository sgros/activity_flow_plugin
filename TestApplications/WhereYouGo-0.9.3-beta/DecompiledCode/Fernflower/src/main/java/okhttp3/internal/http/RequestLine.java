package okhttp3.internal.http;

import java.net.Proxy.Type;
import okhttp3.HttpUrl;
import okhttp3.Request;

public final class RequestLine {
   private RequestLine() {
   }

   public static String get(Request var0, Type var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(var0.method());
      var2.append(' ');
      if (includeAuthorityInRequestLine(var0, var1)) {
         var2.append(var0.url());
      } else {
         var2.append(requestPath(var0.url()));
      }

      var2.append(" HTTP/1.1");
      return var2.toString();
   }

   private static boolean includeAuthorityInRequestLine(Request var0, Type var1) {
      boolean var2;
      if (!var0.isHttps() && var1 == Type.HTTP) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static String requestPath(HttpUrl var0) {
      String var1 = var0.encodedPath();
      String var2 = var0.encodedQuery();
      String var3 = var1;
      if (var2 != null) {
         var3 = var1 + '?' + var2;
      }

      return var3;
   }
}

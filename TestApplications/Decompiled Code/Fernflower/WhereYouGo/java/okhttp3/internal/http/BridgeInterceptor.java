package okhttp3.internal.http;

import java.io.IOException;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

public final class BridgeInterceptor implements Interceptor {
   private final CookieJar cookieJar;

   public BridgeInterceptor(CookieJar var1) {
      this.cookieJar = var1;
   }

   private String cookieHeader(List var1) {
      StringBuilder var2 = new StringBuilder();
      int var3 = 0;

      for(int var4 = var1.size(); var3 < var4; ++var3) {
         if (var3 > 0) {
            var2.append("; ");
         }

         Cookie var5 = (Cookie)var1.get(var3);
         var2.append(var5.name()).append('=').append(var5.value());
      }

      return var2.toString();
   }

   public Response intercept(Interceptor.Chain var1) throws IOException {
      Request var2 = var1.request();
      Request.Builder var3 = var2.newBuilder();
      RequestBody var4 = var2.body();
      if (var4 != null) {
         MediaType var5 = var4.contentType();
         if (var5 != null) {
            var3.header("Content-Type", var5.toString());
         }

         long var6 = var4.contentLength();
         if (var6 != -1L) {
            var3.header("Content-Length", Long.toString(var6));
            var3.removeHeader("Transfer-Encoding");
         } else {
            var3.header("Transfer-Encoding", "chunked");
            var3.removeHeader("Content-Length");
         }
      }

      if (var2.header("Host") == null) {
         var3.header("Host", Util.hostHeader(var2.url(), false));
      }

      if (var2.header("Connection") == null) {
         var3.header("Connection", "Keep-Alive");
      }

      boolean var8 = false;
      boolean var9 = var8;
      if (var2.header("Accept-Encoding") == null) {
         var9 = var8;
         if (var2.header("Range") == null) {
            var9 = true;
            var3.header("Accept-Encoding", "gzip");
         }
      }

      List var14 = this.cookieJar.loadForRequest(var2.url());
      if (!var14.isEmpty()) {
         var3.header("Cookie", this.cookieHeader(var14));
      }

      if (var2.header("User-Agent") == null) {
         var3.header("User-Agent", Version.userAgent());
      }

      Response var10 = var1.proceed(var3.build());
      HttpHeaders.receiveHeaders(this.cookieJar, var2.url(), var10.headers());
      Response.Builder var12 = var10.newBuilder().request(var2);
      if (var9 && "gzip".equalsIgnoreCase(var10.header("Content-Encoding")) && HttpHeaders.hasBody(var10)) {
         GzipSource var13 = new GzipSource(var10.body().source());
         Headers var11 = var10.headers().newBuilder().removeAll("Content-Encoding").removeAll("Content-Length").build();
         var12.headers(var11);
         var12.body(new RealResponseBody(var11, Okio.buffer((Source)var13)));
      }

      return var12.build();
   }
}

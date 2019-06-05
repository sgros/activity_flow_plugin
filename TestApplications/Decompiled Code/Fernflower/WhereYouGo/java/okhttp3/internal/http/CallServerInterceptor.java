package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.connection.StreamAllocation;
import okio.BufferedSink;
import okio.Okio;

public final class CallServerInterceptor implements Interceptor {
   private final boolean forWebSocket;

   public CallServerInterceptor(boolean var1) {
      this.forWebSocket = var1;
   }

   public Response intercept(Interceptor.Chain var1) throws IOException {
      HttpCodec var2 = ((RealInterceptorChain)var1).httpStream();
      StreamAllocation var3 = ((RealInterceptorChain)var1).streamAllocation();
      Request var4 = var1.request();
      long var5 = System.currentTimeMillis();
      var2.writeRequestHeaders(var4);
      Object var7 = null;
      Response.Builder var8 = null;
      Response.Builder var10 = (Response.Builder)var7;
      if (HttpMethod.permitsRequestBody(var4.method())) {
         var10 = (Response.Builder)var7;
         if (var4.body() != null) {
            if ("100-continue".equalsIgnoreCase(var4.header("Expect"))) {
               var2.flushRequest();
               var8 = var2.readResponseHeaders(true);
            }

            var10 = var8;
            if (var8 == null) {
               BufferedSink var11 = Okio.buffer(var2.createRequestBody(var4, var4.body().contentLength()));
               var4.body().writeTo(var11);
               var11.close();
               var10 = var8;
            }
         }
      }

      var2.finishRequest();
      var8 = var10;
      if (var10 == null) {
         var8 = var2.readResponseHeaders(false);
      }

      Response var12 = var8.request(var4).handshake(var3.connection().handshake()).sentRequestAtMillis(var5).receivedResponseAtMillis(System.currentTimeMillis()).build();
      int var9 = var12.code();
      if (this.forWebSocket && var9 == 101) {
         var12 = var12.newBuilder().body(Util.EMPTY_RESPONSE).build();
      } else {
         var12 = var12.newBuilder().body(var2.openResponseBody(var12)).build();
      }

      if ("close".equalsIgnoreCase(var12.request().header("Connection")) || "close".equalsIgnoreCase(var12.header("Connection"))) {
         var3.noNewStreams();
      }

      if ((var9 == 204 || var9 == 205) && var12.body().contentLength() > 0L) {
         throw new ProtocolException("HTTP " + var9 + " had non-zero Content-Length: " + var12.body().contentLength());
      } else {
         return var12;
      }
   }
}

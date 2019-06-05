package okhttp3.internal.http;

import java.io.IOException;
import java.util.List;
import okhttp3.Connection;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.connection.StreamAllocation;

public final class RealInterceptorChain implements Interceptor.Chain {
   private int calls;
   private final Connection connection;
   private final HttpCodec httpCodec;
   private final int index;
   private final List interceptors;
   private final Request request;
   private final StreamAllocation streamAllocation;

   public RealInterceptorChain(List var1, StreamAllocation var2, HttpCodec var3, Connection var4, int var5, Request var6) {
      this.interceptors = var1;
      this.connection = var4;
      this.streamAllocation = var2;
      this.httpCodec = var3;
      this.index = var5;
      this.request = var6;
   }

   private boolean sameConnection(HttpUrl var1) {
      boolean var2;
      if (var1.host().equals(this.connection.route().address().url().host()) && var1.port() == this.connection.route().address().url().port()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public Connection connection() {
      return this.connection;
   }

   public HttpCodec httpStream() {
      return this.httpCodec;
   }

   public Response proceed(Request var1) throws IOException {
      return this.proceed(var1, this.streamAllocation, this.httpCodec, this.connection);
   }

   public Response proceed(Request var1, StreamAllocation var2, HttpCodec var3, Connection var4) throws IOException {
      if (this.index >= this.interceptors.size()) {
         throw new AssertionError();
      } else {
         ++this.calls;
         if (this.httpCodec != null && !this.sameConnection(var1.url())) {
            throw new IllegalStateException("network interceptor " + this.interceptors.get(this.index - 1) + " must retain the same host and port");
         } else if (this.httpCodec != null && this.calls > 1) {
            throw new IllegalStateException("network interceptor " + this.interceptors.get(this.index - 1) + " must call proceed() exactly once");
         } else {
            RealInterceptorChain var6 = new RealInterceptorChain(this.interceptors, var2, var3, var4, this.index + 1, var1);
            Interceptor var5 = (Interceptor)this.interceptors.get(this.index);
            Response var7 = var5.intercept(var6);
            if (var3 != null && this.index + 1 < this.interceptors.size() && var6.calls != 1) {
               throw new IllegalStateException("network interceptor " + var5 + " must call proceed() exactly once");
            } else if (var7 == null) {
               throw new NullPointerException("interceptor " + var5 + " returned null");
            } else {
               return var7;
            }
         }
      }
   }

   public Request request() {
      return this.request;
   }

   public StreamAllocation streamAllocation() {
      return this.streamAllocation;
   }
}

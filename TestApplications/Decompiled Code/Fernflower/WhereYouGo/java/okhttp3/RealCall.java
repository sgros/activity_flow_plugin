package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okhttp3.internal.platform.Platform;

final class RealCall implements Call {
   final OkHttpClient client;
   private boolean executed;
   final boolean forWebSocket;
   final Request originalRequest;
   final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;

   RealCall(OkHttpClient var1, Request var2, boolean var3) {
      this.client = var1;
      this.originalRequest = var2;
      this.forWebSocket = var3;
      this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(var1, var3);
   }

   private void captureCallStackTrace() {
      Object var1 = Platform.get().getStackTraceForCloseable("response.body().close()");
      this.retryAndFollowUpInterceptor.setCallStackTrace(var1);
   }

   public void cancel() {
      this.retryAndFollowUpInterceptor.cancel();
   }

   public RealCall clone() {
      return new RealCall(this.client, this.originalRequest, this.forWebSocket);
   }

   public void enqueue(Callback var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label136: {
         try {
            if (this.executed) {
               IllegalStateException var15 = new IllegalStateException("Already Executed");
               throw var15;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label136;
         }

         try {
            this.executed = true;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label136;
         }

         this.captureCallStackTrace();
         this.client.dispatcher().enqueue(new RealCall.AsyncCall(var1));
         return;
      }

      while(true) {
         Throwable var14 = var10000;

         try {
            throw var14;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public Response execute() throws IOException {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      Throwable var1;
      label311: {
         try {
            if (this.executed) {
               IllegalStateException var34 = new IllegalStateException("Already Executed");
               throw var34;
            }
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label311;
         }

         try {
            this.executed = true;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label311;
         }

         this.captureCallStackTrace();

         label312: {
            Response var32;
            try {
               this.client.dispatcher().executed(this);
               var32 = this.getResponseWithInterceptorChain();
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label312;
            }

            if (var32 != null) {
               this.client.dispatcher().finished(this);
               return var32;
            }

            label290:
            try {
               IOException var33 = new IOException("Canceled");
               throw var33;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               break label290;
            }
         }

         var1 = var10000;
         this.client.dispatcher().finished(this);
         throw var1;
      }

      while(true) {
         var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var27) {
            var10000 = var27;
            var10001 = false;
            continue;
         }
      }
   }

   Response getResponseWithInterceptorChain() throws IOException {
      ArrayList var1 = new ArrayList();
      var1.addAll(this.client.interceptors());
      var1.add(this.retryAndFollowUpInterceptor);
      var1.add(new BridgeInterceptor(this.client.cookieJar()));
      var1.add(new CacheInterceptor(this.client.internalCache()));
      var1.add(new ConnectInterceptor(this.client));
      if (!this.forWebSocket) {
         var1.addAll(this.client.networkInterceptors());
      }

      var1.add(new CallServerInterceptor(this.forWebSocket));
      return (new RealInterceptorChain(var1, (StreamAllocation)null, (HttpCodec)null, (Connection)null, 0, this.originalRequest)).proceed(this.originalRequest);
   }

   public boolean isCanceled() {
      return this.retryAndFollowUpInterceptor.isCanceled();
   }

   public boolean isExecuted() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.executed;
      } finally {
         ;
      }

      return var1;
   }

   String redactedUrl() {
      return this.originalRequest.url().redact();
   }

   public Request request() {
      return this.originalRequest;
   }

   StreamAllocation streamAllocation() {
      return this.retryAndFollowUpInterceptor.streamAllocation();
   }

   String toLoggableString() {
      StringBuilder var1 = new StringBuilder();
      String var2;
      if (this.isCanceled()) {
         var2 = "canceled ";
      } else {
         var2 = "";
      }

      var1 = var1.append(var2);
      if (this.forWebSocket) {
         var2 = "web socket";
      } else {
         var2 = "call";
      }

      return var1.append(var2).append(" to ").append(this.redactedUrl()).toString();
   }

   final class AsyncCall extends NamedRunnable {
      private final Callback responseCallback;

      AsyncCall(Callback var2) {
         super("OkHttp %s", RealCall.this.redactedUrl());
         this.responseCallback = var2;
      }

      protected void execute() {
         // $FF: Couldn't be decompiled
      }

      RealCall get() {
         return RealCall.this;
      }

      String host() {
         return RealCall.this.originalRequest.url().host();
      }

      Request request() {
         return RealCall.this.originalRequest;
      }
   }
}

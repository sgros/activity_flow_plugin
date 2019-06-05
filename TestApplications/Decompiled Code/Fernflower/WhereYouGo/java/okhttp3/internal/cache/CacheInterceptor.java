package okhttp3.internal.cache;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class CacheInterceptor implements Interceptor {
   final InternalCache cache;

   public CacheInterceptor(InternalCache var1) {
      this.cache = var1;
   }

   private Response cacheWritingResponse(final CacheRequest var1, Response var2) throws IOException {
      Response var3;
      if (var1 == null) {
         var3 = var2;
      } else {
         Sink var4 = var1.body();
         var3 = var2;
         if (var4 != null) {
            Source var5 = new Source(var2.body().source(), Okio.buffer(var4)) {
               boolean cacheRequestClosed;
               // $FF: synthetic field
               final BufferedSink val$cacheBody;
               // $FF: synthetic field
               final BufferedSource val$source;

               {
                  this.val$source = var2;
                  this.val$cacheBody = var4;
               }

               public void close() throws IOException {
                  if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                     this.cacheRequestClosed = true;
                     var1.abort();
                  }

                  this.val$source.close();
               }

               public long read(Buffer var1x, long var2) throws IOException {
                  try {
                     var2 = this.val$source.read(var1x, var2);
                  } catch (IOException var4) {
                     if (!this.cacheRequestClosed) {
                        this.cacheRequestClosed = true;
                        var1.abort();
                     }

                     throw var4;
                  }

                  if (var2 == -1L) {
                     if (!this.cacheRequestClosed) {
                        this.cacheRequestClosed = true;
                        this.val$cacheBody.close();
                     }

                     var2 = -1L;
                  } else {
                     var1x.copyTo(this.val$cacheBody.buffer(), var1x.size() - var2, var2);
                     this.val$cacheBody.emitCompleteSegments();
                  }

                  return var2;
               }

               public Timeout timeout() {
                  return this.val$source.timeout();
               }
            };
            var3 = var2.newBuilder().body(new RealResponseBody(var2.headers(), Okio.buffer(var5))).build();
         }
      }

      return var3;
   }

   private static Headers combine(Headers var0, Headers var1) {
      Headers.Builder var2 = new Headers.Builder();
      int var3 = 0;

      int var4;
      for(var4 = var0.size(); var3 < var4; ++var3) {
         String var5 = var0.name(var3);
         String var6 = var0.value(var3);
         if ((!"Warning".equalsIgnoreCase(var5) || !var6.startsWith("1")) && (!isEndToEnd(var5) || var1.get(var5) == null)) {
            Internal.instance.addLenient(var2, var5, var6);
         }
      }

      var3 = 0;

      for(var4 = var1.size(); var3 < var4; ++var3) {
         String var7 = var1.name(var3);
         if (!"Content-Length".equalsIgnoreCase(var7) && isEndToEnd(var7)) {
            Internal.instance.addLenient(var2, var7, var1.value(var3));
         }
      }

      return var2.build();
   }

   static boolean isEndToEnd(String var0) {
      boolean var1;
      if (!"Connection".equalsIgnoreCase(var0) && !"Keep-Alive".equalsIgnoreCase(var0) && !"Proxy-Authenticate".equalsIgnoreCase(var0) && !"Proxy-Authorization".equalsIgnoreCase(var0) && !"TE".equalsIgnoreCase(var0) && !"Trailers".equalsIgnoreCase(var0) && !"Transfer-Encoding".equalsIgnoreCase(var0) && !"Upgrade".equalsIgnoreCase(var0)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private CacheRequest maybeCache(Response var1, Request var2, InternalCache var3) throws IOException {
      Object var4 = null;
      CacheRequest var6;
      if (var3 == null) {
         var6 = (CacheRequest)var4;
      } else if (!CacheStrategy.isCacheable(var1, var2)) {
         var6 = (CacheRequest)var4;
         if (HttpMethod.invalidatesCache(var2.method())) {
            try {
               var3.remove(var2);
            } catch (IOException var5) {
               var6 = (CacheRequest)var4;
               return var6;
            }

            var6 = (CacheRequest)var4;
         }
      } else {
         var6 = var3.put(var1);
      }

      return var6;
   }

   private static Response stripBody(Response var0) {
      Response var1 = var0;
      if (var0 != null) {
         var1 = var0;
         if (var0.body() != null) {
            var1 = var0.newBuilder().body((ResponseBody)null).build();
         }
      }

      return var1;
   }

   public Response intercept(Interceptor.Chain var1) throws IOException {
      Response var2;
      if (this.cache != null) {
         var2 = this.cache.get(var1.request());
      } else {
         var2 = null;
      }

      CacheStrategy var3 = (new CacheStrategy.Factory(System.currentTimeMillis(), var1.request(), var2)).get();
      Request var4 = var3.networkRequest;
      Response var5 = var3.cacheResponse;
      if (this.cache != null) {
         this.cache.trackResponse(var3);
      }

      if (var2 != null && var5 == null) {
         Util.closeQuietly((Closeable)var2.body());
      }

      Response var9;
      if (var4 == null && var5 == null) {
         var9 = (new Response.Builder()).request(var1.request()).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(System.currentTimeMillis()).build();
      } else if (var4 == null) {
         var9 = var5.newBuilder().cacheResponse(stripBody(var5)).build();
      } else {
         boolean var7 = false;

         Response var10;
         try {
            var7 = true;
            var10 = var1.proceed(var4);
            var7 = false;
         } finally {
            if (var7) {
               if (true && var2 != null) {
                  Util.closeQuietly((Closeable)var2.body());
               }

            }
         }

         if (var10 == null && var2 != null) {
            Util.closeQuietly((Closeable)var2.body());
         }

         if (var5 != null) {
            if (var10.code() == 304) {
               var9 = var5.newBuilder().headers(combine(var5.headers(), var10.headers())).sentRequestAtMillis(var10.sentRequestAtMillis()).receivedResponseAtMillis(var10.receivedResponseAtMillis()).cacheResponse(stripBody(var5)).networkResponse(stripBody(var10)).build();
               var10.body().close();
               this.cache.trackConditionalCacheHit();
               this.cache.update(var5, var9);
               return var9;
            }

            Util.closeQuietly((Closeable)var5.body());
         }

         var2 = var10.newBuilder().cacheResponse(stripBody(var5)).networkResponse(stripBody(var10)).build();
         var9 = var2;
         if (HttpHeaders.hasBody(var2)) {
            var9 = this.cacheWritingResponse(this.maybeCache(var2, var10.request(), this.cache), var2);
         }
      }

      return var9;
   }
}

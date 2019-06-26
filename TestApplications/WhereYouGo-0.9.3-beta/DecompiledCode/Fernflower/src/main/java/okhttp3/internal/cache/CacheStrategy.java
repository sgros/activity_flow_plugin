package okhttp3.internal.cache;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.http.HttpDate;
import okhttp3.internal.http.HttpHeaders;

public final class CacheStrategy {
   public final Response cacheResponse;
   public final Request networkRequest;

   CacheStrategy(Request var1, Response var2) {
      this.networkRequest = var1;
      this.cacheResponse = var2;
   }

   public static boolean isCacheable(Response var0, Request var1) {
      boolean var2 = false;
      boolean var3;
      switch(var0.code()) {
      case 302:
      case 307:
         if (var0.header("Expires") == null && var0.cacheControl().maxAgeSeconds() == -1 && !var0.cacheControl().isPublic()) {
            var3 = var2;
            if (!var0.cacheControl().isPrivate()) {
               break;
            }
         }
      case 200:
      case 203:
      case 204:
      case 300:
      case 301:
      case 308:
      case 404:
      case 405:
      case 410:
      case 414:
      case 501:
         var3 = var2;
         if (!var0.cacheControl().noStore()) {
            var3 = var2;
            if (!var1.cacheControl().noStore()) {
               var3 = true;
            }
         }
         break;
      default:
         var3 = var2;
      }

      return var3;
   }

   public static class Factory {
      private int ageSeconds = -1;
      final Response cacheResponse;
      private String etag;
      private Date expires;
      private Date lastModified;
      private String lastModifiedString;
      final long nowMillis;
      private long receivedResponseMillis;
      final Request request;
      private long sentRequestMillis;
      private Date servedDate;
      private String servedDateString;

      public Factory(long var1, Request var3, Response var4) {
         this.nowMillis = var1;
         this.request = var3;
         this.cacheResponse = var4;
         if (var4 != null) {
            this.sentRequestMillis = var4.sentRequestAtMillis();
            this.receivedResponseMillis = var4.receivedResponseAtMillis();
            Headers var5 = var4.headers();
            int var6 = 0;

            for(int var7 = var5.size(); var6 < var7; ++var6) {
               String var8 = var5.name(var6);
               String var9 = var5.value(var6);
               if ("Date".equalsIgnoreCase(var8)) {
                  this.servedDate = HttpDate.parse(var9);
                  this.servedDateString = var9;
               } else if ("Expires".equalsIgnoreCase(var8)) {
                  this.expires = HttpDate.parse(var9);
               } else if ("Last-Modified".equalsIgnoreCase(var8)) {
                  this.lastModified = HttpDate.parse(var9);
                  this.lastModifiedString = var9;
               } else if ("ETag".equalsIgnoreCase(var8)) {
                  this.etag = var9;
               } else if ("Age".equalsIgnoreCase(var8)) {
                  this.ageSeconds = HttpHeaders.parseSeconds(var9, -1);
               }
            }
         }

      }

      private long cacheResponseAge() {
         long var1 = 0L;
         if (this.servedDate != null) {
            var1 = Math.max(0L, this.receivedResponseMillis - this.servedDate.getTime());
         }

         if (this.ageSeconds != -1) {
            var1 = Math.max(var1, TimeUnit.SECONDS.toMillis((long)this.ageSeconds));
         }

         return var1 + (this.receivedResponseMillis - this.sentRequestMillis) + (this.nowMillis - this.receivedResponseMillis);
      }

      private long computeFreshnessLifetime() {
         long var1 = 0L;
         CacheControl var3 = this.cacheResponse.cacheControl();
         long var4;
         if (var3.maxAgeSeconds() != -1) {
            var4 = TimeUnit.SECONDS.toMillis((long)var3.maxAgeSeconds());
         } else if (this.expires != null) {
            if (this.servedDate != null) {
               var4 = this.servedDate.getTime();
            } else {
               var4 = this.receivedResponseMillis;
            }

            var4 = this.expires.getTime() - var4;
            if (var4 <= 0L) {
               var4 = 0L;
            }
         } else {
            var4 = var1;
            if (this.lastModified != null) {
               var4 = var1;
               if (this.cacheResponse.request().url().query() == null) {
                  if (this.servedDate != null) {
                     var4 = this.servedDate.getTime();
                  } else {
                     var4 = this.sentRequestMillis;
                  }

                  long var6 = var4 - this.lastModified.getTime();
                  var4 = var1;
                  if (var6 > 0L) {
                     var4 = var6 / 10L;
                  }
               }
            }
         }

         return var4;
      }

      private CacheStrategy getCandidate() {
         CacheStrategy var1;
         if (this.cacheResponse == null) {
            var1 = new CacheStrategy(this.request, (Response)null);
         } else if (this.request.isHttps() && this.cacheResponse.handshake() == null) {
            var1 = new CacheStrategy(this.request, (Response)null);
         } else if (!CacheStrategy.isCacheable(this.cacheResponse, this.request)) {
            var1 = new CacheStrategy(this.request, (Response)null);
         } else {
            CacheControl var2 = this.request.cacheControl();
            if (!var2.noCache() && !hasConditions(this.request)) {
               long var3 = this.cacheResponseAge();
               long var5 = this.computeFreshnessLifetime();
               long var7 = var5;
               if (var2.maxAgeSeconds() != -1) {
                  var7 = Math.min(var5, TimeUnit.SECONDS.toMillis((long)var2.maxAgeSeconds()));
               }

               var5 = 0L;
               if (var2.minFreshSeconds() != -1) {
                  var5 = TimeUnit.SECONDS.toMillis((long)var2.minFreshSeconds());
               }

               long var9 = 0L;
               CacheControl var14 = this.cacheResponse.cacheControl();
               long var11 = var9;
               if (!var14.mustRevalidate()) {
                  var11 = var9;
                  if (var2.maxStaleSeconds() != -1) {
                     var11 = TimeUnit.SECONDS.toMillis((long)var2.maxStaleSeconds());
                  }
               }

               if (!var14.noCache() && var3 + var5 < var7 + var11) {
                  Response.Builder var17 = this.cacheResponse.newBuilder();
                  if (var3 + var5 >= var7) {
                     var17.addHeader("Warning", "110 HttpURLConnection \"Response is stale\"");
                  }

                  if (var3 > 86400000L && this.isFreshnessLifetimeHeuristic()) {
                     var17.addHeader("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
                  }

                  var1 = new CacheStrategy((Request)null, var17.build());
               } else {
                  String var15;
                  String var16;
                  if (this.etag != null) {
                     var15 = "If-None-Match";
                     var16 = this.etag;
                  } else if (this.lastModified != null) {
                     var15 = "If-Modified-Since";
                     var16 = this.lastModifiedString;
                  } else {
                     if (this.servedDate == null) {
                        var1 = new CacheStrategy(this.request, (Response)null);
                        return var1;
                     }

                     var15 = "If-Modified-Since";
                     var16 = this.servedDateString;
                  }

                  Headers.Builder var13 = this.request.headers().newBuilder();
                  Internal.instance.addLenient(var13, var15, var16);
                  var1 = new CacheStrategy(this.request.newBuilder().headers(var13.build()).build(), this.cacheResponse);
               }
            } else {
               var1 = new CacheStrategy(this.request, (Response)null);
            }
         }

         return var1;
      }

      private static boolean hasConditions(Request var0) {
         boolean var1;
         if (var0.header("If-Modified-Since") == null && var0.header("If-None-Match") == null) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      private boolean isFreshnessLifetimeHeuristic() {
         boolean var1;
         if (this.cacheResponse.cacheControl().maxAgeSeconds() == -1 && this.expires == null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public CacheStrategy get() {
         CacheStrategy var1 = this.getCandidate();
         CacheStrategy var2 = var1;
         if (var1.networkRequest != null) {
            var2 = var1;
            if (this.request.cacheControl().onlyIfCached()) {
               var2 = new CacheStrategy((Request)null, (Response)null);
            }
         }

         return var2;
      }
   }
}

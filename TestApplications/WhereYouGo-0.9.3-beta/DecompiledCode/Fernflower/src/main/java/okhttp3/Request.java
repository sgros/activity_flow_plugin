package okhttp3;

import java.net.URL;
import java.util.List;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpMethod;

public final class Request {
   final RequestBody body;
   private volatile CacheControl cacheControl;
   final Headers headers;
   final String method;
   final Object tag;
   final HttpUrl url;

   Request(Request.Builder var1) {
      this.url = var1.url;
      this.method = var1.method;
      this.headers = var1.headers.build();
      this.body = var1.body;
      Object var2;
      if (var1.tag != null) {
         var2 = var1.tag;
      } else {
         var2 = this;
      }

      this.tag = var2;
   }

   public RequestBody body() {
      return this.body;
   }

   public CacheControl cacheControl() {
      CacheControl var1 = this.cacheControl;
      if (var1 == null) {
         var1 = CacheControl.parse(this.headers);
         this.cacheControl = var1;
      }

      return var1;
   }

   public String header(String var1) {
      return this.headers.get(var1);
   }

   public List headers(String var1) {
      return this.headers.values(var1);
   }

   public Headers headers() {
      return this.headers;
   }

   public boolean isHttps() {
      return this.url.isHttps();
   }

   public String method() {
      return this.method;
   }

   public Request.Builder newBuilder() {
      return new Request.Builder(this);
   }

   public Object tag() {
      return this.tag;
   }

   public String toString() {
      StringBuilder var1 = (new StringBuilder()).append("Request{method=").append(this.method).append(", url=").append(this.url).append(", tag=");
      Object var2;
      if (this.tag != this) {
         var2 = this.tag;
      } else {
         var2 = null;
      }

      return var1.append(var2).append('}').toString();
   }

   public HttpUrl url() {
      return this.url;
   }

   public static class Builder {
      RequestBody body;
      Headers.Builder headers;
      String method;
      Object tag;
      HttpUrl url;

      public Builder() {
         this.method = "GET";
         this.headers = new Headers.Builder();
      }

      Builder(Request var1) {
         this.url = var1.url;
         this.method = var1.method;
         this.body = var1.body;
         this.tag = var1.tag;
         this.headers = var1.headers.newBuilder();
      }

      public Request.Builder addHeader(String var1, String var2) {
         this.headers.add(var1, var2);
         return this;
      }

      public Request build() {
         if (this.url == null) {
            throw new IllegalStateException("url == null");
         } else {
            return new Request(this);
         }
      }

      public Request.Builder cacheControl(CacheControl var1) {
         String var2 = var1.toString();
         Request.Builder var3;
         if (var2.isEmpty()) {
            var3 = this.removeHeader("Cache-Control");
         } else {
            var3 = this.header("Cache-Control", var2);
         }

         return var3;
      }

      public Request.Builder delete() {
         return this.delete(Util.EMPTY_REQUEST);
      }

      public Request.Builder delete(RequestBody var1) {
         return this.method("DELETE", var1);
      }

      public Request.Builder get() {
         return this.method("GET", (RequestBody)null);
      }

      public Request.Builder head() {
         return this.method("HEAD", (RequestBody)null);
      }

      public Request.Builder header(String var1, String var2) {
         this.headers.set(var1, var2);
         return this;
      }

      public Request.Builder headers(Headers var1) {
         this.headers = var1.newBuilder();
         return this;
      }

      public Request.Builder method(String var1, RequestBody var2) {
         if (var1 == null) {
            throw new NullPointerException("method == null");
         } else if (var1.length() == 0) {
            throw new IllegalArgumentException("method.length() == 0");
         } else if (var2 != null && !HttpMethod.permitsRequestBody(var1)) {
            throw new IllegalArgumentException("method " + var1 + " must not have a request body.");
         } else if (var2 == null && HttpMethod.requiresRequestBody(var1)) {
            throw new IllegalArgumentException("method " + var1 + " must have a request body.");
         } else {
            this.method = var1;
            this.body = var2;
            return this;
         }
      }

      public Request.Builder patch(RequestBody var1) {
         return this.method("PATCH", var1);
      }

      public Request.Builder post(RequestBody var1) {
         return this.method("POST", var1);
      }

      public Request.Builder put(RequestBody var1) {
         return this.method("PUT", var1);
      }

      public Request.Builder removeHeader(String var1) {
         this.headers.removeAll(var1);
         return this;
      }

      public Request.Builder tag(Object var1) {
         this.tag = var1;
         return this;
      }

      public Request.Builder url(String var1) {
         if (var1 == null) {
            throw new NullPointerException("url == null");
         } else {
            String var2;
            if (var1.regionMatches(true, 0, "ws:", 0, 3)) {
               var2 = "http:" + var1.substring(3);
            } else {
               var2 = var1;
               if (var1.regionMatches(true, 0, "wss:", 0, 4)) {
                  var2 = "https:" + var1.substring(4);
               }
            }

            HttpUrl var3 = HttpUrl.parse(var2);
            if (var3 == null) {
               throw new IllegalArgumentException("unexpected url: " + var2);
            } else {
               return this.url(var3);
            }
         }
      }

      public Request.Builder url(URL var1) {
         if (var1 == null) {
            throw new NullPointerException("url == null");
         } else {
            HttpUrl var2 = HttpUrl.get(var1);
            if (var2 == null) {
               throw new IllegalArgumentException("unexpected url: " + var1);
            } else {
               return this.url(var2);
            }
         }
      }

      public Request.Builder url(HttpUrl var1) {
         if (var1 == null) {
            throw new NullPointerException("url == null");
         } else {
            this.url = var1;
            return this;
         }
      }
   }
}

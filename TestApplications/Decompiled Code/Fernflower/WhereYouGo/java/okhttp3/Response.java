package okhttp3;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

public final class Response implements Closeable {
   final ResponseBody body;
   private volatile CacheControl cacheControl;
   final Response cacheResponse;
   final int code;
   final Handshake handshake;
   final Headers headers;
   final String message;
   final Response networkResponse;
   final Response priorResponse;
   final Protocol protocol;
   final long receivedResponseAtMillis;
   final Request request;
   final long sentRequestAtMillis;

   Response(Response.Builder var1) {
      this.request = var1.request;
      this.protocol = var1.protocol;
      this.code = var1.code;
      this.message = var1.message;
      this.handshake = var1.handshake;
      this.headers = var1.headers.build();
      this.body = var1.body;
      this.networkResponse = var1.networkResponse;
      this.cacheResponse = var1.cacheResponse;
      this.priorResponse = var1.priorResponse;
      this.sentRequestAtMillis = var1.sentRequestAtMillis;
      this.receivedResponseAtMillis = var1.receivedResponseAtMillis;
   }

   public ResponseBody body() {
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

   public Response cacheResponse() {
      return this.cacheResponse;
   }

   public List challenges() {
      String var1;
      List var2;
      if (this.code == 401) {
         var1 = "WWW-Authenticate";
      } else {
         if (this.code != 407) {
            var2 = Collections.emptyList();
            return var2;
         }

         var1 = "Proxy-Authenticate";
      }

      var2 = HttpHeaders.parseChallenges(this.headers(), var1);
      return var2;
   }

   public void close() {
      this.body.close();
   }

   public int code() {
      return this.code;
   }

   public Handshake handshake() {
      return this.handshake;
   }

   public String header(String var1) {
      return this.header(var1, (String)null);
   }

   public String header(String var1, String var2) {
      var1 = this.headers.get(var1);
      if (var1 != null) {
         var2 = var1;
      }

      return var2;
   }

   public List headers(String var1) {
      return this.headers.values(var1);
   }

   public Headers headers() {
      return this.headers;
   }

   public boolean isRedirect() {
      boolean var1;
      switch(this.code) {
      case 300:
      case 301:
      case 302:
      case 303:
      case 307:
      case 308:
         var1 = true;
         break;
      case 304:
      case 305:
      case 306:
      default:
         var1 = false;
      }

      return var1;
   }

   public boolean isSuccessful() {
      boolean var1;
      if (this.code >= 200 && this.code < 300) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public String message() {
      return this.message;
   }

   public Response networkResponse() {
      return this.networkResponse;
   }

   public Response.Builder newBuilder() {
      return new Response.Builder(this);
   }

   public ResponseBody peekBody(long var1) throws IOException {
      BufferedSource var3 = this.body.source();
      var3.request(var1);
      Buffer var4 = var3.buffer().clone();
      Buffer var5;
      if (var4.size() > var1) {
         var5 = new Buffer();
         var5.write(var4, var1);
         var4.clear();
      } else {
         var5 = var4;
      }

      return ResponseBody.create(this.body.contentType(), var5.size(), var5);
   }

   public Response priorResponse() {
      return this.priorResponse;
   }

   public Protocol protocol() {
      return this.protocol;
   }

   public long receivedResponseAtMillis() {
      return this.receivedResponseAtMillis;
   }

   public Request request() {
      return this.request;
   }

   public long sentRequestAtMillis() {
      return this.sentRequestAtMillis;
   }

   public String toString() {
      return "Response{protocol=" + this.protocol + ", code=" + this.code + ", message=" + this.message + ", url=" + this.request.url() + '}';
   }

   public static class Builder {
      ResponseBody body;
      Response cacheResponse;
      int code = -1;
      Handshake handshake;
      Headers.Builder headers;
      String message;
      Response networkResponse;
      Response priorResponse;
      Protocol protocol;
      long receivedResponseAtMillis;
      Request request;
      long sentRequestAtMillis;

      public Builder() {
         this.headers = new Headers.Builder();
      }

      Builder(Response var1) {
         this.request = var1.request;
         this.protocol = var1.protocol;
         this.code = var1.code;
         this.message = var1.message;
         this.handshake = var1.handshake;
         this.headers = var1.headers.newBuilder();
         this.body = var1.body;
         this.networkResponse = var1.networkResponse;
         this.cacheResponse = var1.cacheResponse;
         this.priorResponse = var1.priorResponse;
         this.sentRequestAtMillis = var1.sentRequestAtMillis;
         this.receivedResponseAtMillis = var1.receivedResponseAtMillis;
      }

      private void checkPriorResponse(Response var1) {
         if (var1.body != null) {
            throw new IllegalArgumentException("priorResponse.body != null");
         }
      }

      private void checkSupportResponse(String var1, Response var2) {
         if (var2.body != null) {
            throw new IllegalArgumentException(var1 + ".body != null");
         } else if (var2.networkResponse != null) {
            throw new IllegalArgumentException(var1 + ".networkResponse != null");
         } else if (var2.cacheResponse != null) {
            throw new IllegalArgumentException(var1 + ".cacheResponse != null");
         } else if (var2.priorResponse != null) {
            throw new IllegalArgumentException(var1 + ".priorResponse != null");
         }
      }

      public Response.Builder addHeader(String var1, String var2) {
         this.headers.add(var1, var2);
         return this;
      }

      public Response.Builder body(ResponseBody var1) {
         this.body = var1;
         return this;
      }

      public Response build() {
         if (this.request == null) {
            throw new IllegalStateException("request == null");
         } else if (this.protocol == null) {
            throw new IllegalStateException("protocol == null");
         } else if (this.code < 0) {
            throw new IllegalStateException("code < 0: " + this.code);
         } else {
            return new Response(this);
         }
      }

      public Response.Builder cacheResponse(Response var1) {
         if (var1 != null) {
            this.checkSupportResponse("cacheResponse", var1);
         }

         this.cacheResponse = var1;
         return this;
      }

      public Response.Builder code(int var1) {
         this.code = var1;
         return this;
      }

      public Response.Builder handshake(Handshake var1) {
         this.handshake = var1;
         return this;
      }

      public Response.Builder header(String var1, String var2) {
         this.headers.set(var1, var2);
         return this;
      }

      public Response.Builder headers(Headers var1) {
         this.headers = var1.newBuilder();
         return this;
      }

      public Response.Builder message(String var1) {
         this.message = var1;
         return this;
      }

      public Response.Builder networkResponse(Response var1) {
         if (var1 != null) {
            this.checkSupportResponse("networkResponse", var1);
         }

         this.networkResponse = var1;
         return this;
      }

      public Response.Builder priorResponse(Response var1) {
         if (var1 != null) {
            this.checkPriorResponse(var1);
         }

         this.priorResponse = var1;
         return this;
      }

      public Response.Builder protocol(Protocol var1) {
         this.protocol = var1;
         return this;
      }

      public Response.Builder receivedResponseAtMillis(long var1) {
         this.receivedResponseAtMillis = var1;
         return this;
      }

      public Response.Builder removeHeader(String var1) {
         this.headers.removeAll(var1);
         return this;
      }

      public Response.Builder request(Request var1) {
         this.request = var1;
         return this;
      }

      public Response.Builder sentRequestAtMillis(long var1) {
         this.sentRequestAtMillis = var1;
         return this;
      }
   }
}

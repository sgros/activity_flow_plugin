package okhttp3.internal.http2;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.http.RequestLine;
import okhttp3.internal.http.StatusLine;
import okio.ByteString;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Http2Codec implements HttpCodec {
   private static final ByteString CONNECTION = ByteString.encodeUtf8("connection");
   private static final ByteString ENCODING = ByteString.encodeUtf8("encoding");
   private static final ByteString HOST = ByteString.encodeUtf8("host");
   private static final List HTTP_2_SKIPPED_REQUEST_HEADERS;
   private static final List HTTP_2_SKIPPED_RESPONSE_HEADERS;
   private static final ByteString KEEP_ALIVE = ByteString.encodeUtf8("keep-alive");
   private static final ByteString PROXY_CONNECTION = ByteString.encodeUtf8("proxy-connection");
   private static final ByteString TE = ByteString.encodeUtf8("te");
   private static final ByteString TRANSFER_ENCODING = ByteString.encodeUtf8("transfer-encoding");
   private static final ByteString UPGRADE = ByteString.encodeUtf8("upgrade");
   private final OkHttpClient client;
   private final Http2Connection connection;
   private Http2Stream stream;
   final StreamAllocation streamAllocation;

   static {
      HTTP_2_SKIPPED_REQUEST_HEADERS = Util.immutableList((Object[])(CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, TE, TRANSFER_ENCODING, ENCODING, UPGRADE, Header.TARGET_METHOD, Header.TARGET_PATH, Header.TARGET_SCHEME, Header.TARGET_AUTHORITY));
      HTTP_2_SKIPPED_RESPONSE_HEADERS = Util.immutableList((Object[])(CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, TE, TRANSFER_ENCODING, ENCODING, UPGRADE));
   }

   public Http2Codec(OkHttpClient var1, StreamAllocation var2, Http2Connection var3) {
      this.client = var1;
      this.streamAllocation = var2;
      this.connection = var3;
   }

   public static List http2HeadersList(Request var0) {
      Headers var1 = var0.headers();
      ArrayList var2 = new ArrayList(var1.size() + 4);
      var2.add(new Header(Header.TARGET_METHOD, var0.method()));
      var2.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(var0.url())));
      String var3 = var0.header("Host");
      if (var3 != null) {
         var2.add(new Header(Header.TARGET_AUTHORITY, var3));
      }

      var2.add(new Header(Header.TARGET_SCHEME, var0.url().scheme()));
      int var4 = 0;

      for(int var5 = var1.size(); var4 < var5; ++var4) {
         ByteString var6 = ByteString.encodeUtf8(var1.name(var4).toLowerCase(Locale.US));
         if (!HTTP_2_SKIPPED_REQUEST_HEADERS.contains(var6)) {
            var2.add(new Header(var6, var1.value(var4)));
         }
      }

      return var2;
   }

   public static Response.Builder readHttp2HeadersList(List var0) throws IOException {
      StatusLine var1 = null;
      Headers.Builder var2 = new Headers.Builder();
      int var3 = 0;

      StatusLine var9;
      for(int var4 = var0.size(); var3 < var4; var1 = var9) {
         Header var5 = (Header)var0.get(var3);
         Headers.Builder var6;
         if (var5 == null) {
            var6 = var2;
            var9 = var1;
            if (var1 != null) {
               var6 = var2;
               var9 = var1;
               if (var1.code == 100) {
                  var9 = null;
                  var6 = new Headers.Builder();
               }
            }
         } else {
            ByteString var7 = var5.name;
            String var8 = var5.value.utf8();
            if (var7.equals(Header.RESPONSE_STATUS)) {
               var9 = StatusLine.parse("HTTP/1.1 " + var8);
               var6 = var2;
            } else {
               var6 = var2;
               var9 = var1;
               if (!HTTP_2_SKIPPED_RESPONSE_HEADERS.contains(var7)) {
                  Internal.instance.addLenient(var2, var7.utf8(), var8);
                  var6 = var2;
                  var9 = var1;
               }
            }
         }

         ++var3;
         var2 = var6;
      }

      if (var1 == null) {
         throw new ProtocolException("Expected ':status' header not present");
      } else {
         return (new Response.Builder()).protocol(Protocol.HTTP_2).code(var1.code).message(var1.message).headers(var2.build());
      }
   }

   public void cancel() {
      if (this.stream != null) {
         this.stream.closeLater(ErrorCode.CANCEL);
      }

   }

   public Sink createRequestBody(Request var1, long var2) {
      return this.stream.getSink();
   }

   public void finishRequest() throws IOException {
      this.stream.getSink().close();
   }

   public void flushRequest() throws IOException {
      this.connection.flush();
   }

   public ResponseBody openResponseBody(Response var1) throws IOException {
      Http2Codec.StreamFinishingSource var2 = new Http2Codec.StreamFinishingSource(this.stream.getSource());
      return new RealResponseBody(var1.headers(), Okio.buffer((Source)var2));
   }

   public Response.Builder readResponseHeaders(boolean var1) throws IOException {
      Response.Builder var2 = readHttp2HeadersList(this.stream.takeResponseHeaders());
      Response.Builder var3 = var2;
      if (var1) {
         var3 = var2;
         if (Internal.instance.code(var2) == 100) {
            var3 = null;
         }
      }

      return var3;
   }

   public void writeRequestHeaders(Request var1) throws IOException {
      if (this.stream == null) {
         boolean var2;
         if (var1.body() != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         List var3 = http2HeadersList(var1);
         this.stream = this.connection.newStream(var3, var2);
         this.stream.readTimeout().timeout((long)this.client.readTimeoutMillis(), TimeUnit.MILLISECONDS);
         this.stream.writeTimeout().timeout((long)this.client.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
      }

   }

   class StreamFinishingSource extends ForwardingSource {
      public StreamFinishingSource(Source var2) {
         super(var2);
      }

      public void close() throws IOException {
         Http2Codec.this.streamAllocation.streamFinished(false, Http2Codec.this);
         super.close();
      }
   }
}

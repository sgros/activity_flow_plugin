package okhttp3.internal.http1;

import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.http.RequestLine;
import okhttp3.internal.http.StatusLine;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingTimeout;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class Http1Codec implements HttpCodec {
   private static final int STATE_CLOSED = 6;
   private static final int STATE_IDLE = 0;
   private static final int STATE_OPEN_REQUEST_BODY = 1;
   private static final int STATE_OPEN_RESPONSE_BODY = 4;
   private static final int STATE_READING_RESPONSE_BODY = 5;
   private static final int STATE_READ_RESPONSE_HEADERS = 3;
   private static final int STATE_WRITING_REQUEST_BODY = 2;
   final OkHttpClient client;
   final BufferedSink sink;
   final BufferedSource source;
   int state = 0;
   final StreamAllocation streamAllocation;

   public Http1Codec(OkHttpClient var1, StreamAllocation var2, BufferedSource var3, BufferedSink var4) {
      this.client = var1;
      this.streamAllocation = var2;
      this.source = var3;
      this.sink = var4;
   }

   private Source getTransferStream(Response var1) throws IOException {
      Source var4;
      if (!HttpHeaders.hasBody(var1)) {
         var4 = this.newFixedLengthSource(0L);
      } else if ("chunked".equalsIgnoreCase(var1.header("Transfer-Encoding"))) {
         var4 = this.newChunkedSource(var1.request().url());
      } else {
         long var2 = HttpHeaders.contentLength(var1);
         if (var2 != -1L) {
            var4 = this.newFixedLengthSource(var2);
         } else {
            var4 = this.newUnknownLengthSource();
         }
      }

      return var4;
   }

   public void cancel() {
      RealConnection var1 = this.streamAllocation.connection();
      if (var1 != null) {
         var1.cancel();
      }

   }

   public Sink createRequestBody(Request var1, long var2) {
      Sink var4;
      if ("chunked".equalsIgnoreCase(var1.header("Transfer-Encoding"))) {
         var4 = this.newChunkedSink();
      } else {
         if (var2 == -1L) {
            throw new IllegalStateException("Cannot stream a request body without chunked encoding or a known content length!");
         }

         var4 = this.newFixedLengthSink(var2);
      }

      return var4;
   }

   void detachTimeout(ForwardingTimeout var1) {
      Timeout var2 = var1.delegate();
      var1.setDelegate(Timeout.NONE);
      var2.clearDeadline();
      var2.clearTimeout();
   }

   public void finishRequest() throws IOException {
      this.sink.flush();
   }

   public void flushRequest() throws IOException {
      this.sink.flush();
   }

   public boolean isClosed() {
      boolean var1;
      if (this.state == 6) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public Sink newChunkedSink() {
      if (this.state != 1) {
         throw new IllegalStateException("state: " + this.state);
      } else {
         this.state = 2;
         return new Http1Codec.ChunkedSink();
      }
   }

   public Source newChunkedSource(HttpUrl var1) throws IOException {
      if (this.state != 4) {
         throw new IllegalStateException("state: " + this.state);
      } else {
         this.state = 5;
         return new Http1Codec.ChunkedSource(var1);
      }
   }

   public Sink newFixedLengthSink(long var1) {
      if (this.state != 1) {
         throw new IllegalStateException("state: " + this.state);
      } else {
         this.state = 2;
         return new Http1Codec.FixedLengthSink(var1);
      }
   }

   public Source newFixedLengthSource(long var1) throws IOException {
      if (this.state != 4) {
         throw new IllegalStateException("state: " + this.state);
      } else {
         this.state = 5;
         return new Http1Codec.FixedLengthSource(var1);
      }
   }

   public Source newUnknownLengthSource() throws IOException {
      if (this.state != 4) {
         throw new IllegalStateException("state: " + this.state);
      } else if (this.streamAllocation == null) {
         throw new IllegalStateException("streamAllocation == null");
      } else {
         this.state = 5;
         this.streamAllocation.noNewStreams();
         return new Http1Codec.UnknownLengthSource();
      }
   }

   public ResponseBody openResponseBody(Response var1) throws IOException {
      Source var2 = this.getTransferStream(var1);
      return new RealResponseBody(var1.headers(), Okio.buffer(var2));
   }

   public Headers readHeaders() throws IOException {
      Headers.Builder var1 = new Headers.Builder();

      while(true) {
         String var2 = this.source.readUtf8LineStrict();
         if (var2.length() == 0) {
            return var1.build();
         }

         Internal.instance.addLenient(var1, var2);
      }
   }

   public Response.Builder readResponseHeaders(boolean var1) throws IOException {
      if (this.state != 1 && this.state != 3) {
         throw new IllegalStateException("state: " + this.state);
      } else {
         Response.Builder var3;
         label34: {
            EOFException var10000;
            label40: {
               boolean var10001;
               StatusLine var2;
               try {
                  var2 = StatusLine.parse(this.source.readUtf8LineStrict());
                  var3 = new Response.Builder();
                  var3 = var3.protocol(var2.protocol).code(var2.code).message(var2.message).headers(this.readHeaders());
               } catch (EOFException var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label40;
               }

               if (var1) {
                  try {
                     if (var2.code == 100) {
                        break label34;
                     }
                  } catch (EOFException var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label40;
                  }
               }

               try {
                  this.state = 4;
                  return var3;
               } catch (EOFException var4) {
                  var10000 = var4;
                  var10001 = false;
               }
            }

            EOFException var8 = var10000;
            IOException var7 = new IOException("unexpected end of stream on " + this.streamAllocation);
            var7.initCause(var8);
            throw var7;
         }

         var3 = null;
         return var3;
      }
   }

   public void writeRequest(Headers var1, String var2) throws IOException {
      if (this.state != 0) {
         throw new IllegalStateException("state: " + this.state);
      } else {
         this.sink.writeUtf8(var2).writeUtf8("\r\n");
         int var3 = 0;

         for(int var4 = var1.size(); var3 < var4; ++var3) {
            this.sink.writeUtf8(var1.name(var3)).writeUtf8(": ").writeUtf8(var1.value(var3)).writeUtf8("\r\n");
         }

         this.sink.writeUtf8("\r\n");
         this.state = 1;
      }
   }

   public void writeRequestHeaders(Request var1) throws IOException {
      String var2 = RequestLine.get(var1, this.streamAllocation.connection().route().proxy().type());
      this.writeRequest(var1.headers(), var2);
   }

   private abstract class AbstractSource implements Source {
      protected boolean closed;
      protected final ForwardingTimeout timeout;

      private AbstractSource() {
         this.timeout = new ForwardingTimeout(Http1Codec.this.source.timeout());
      }

      // $FF: synthetic method
      AbstractSource(Object var2) {
         this();
      }

      protected final void endOfInput(boolean var1) throws IOException {
         if (Http1Codec.this.state != 6) {
            if (Http1Codec.this.state != 5) {
               throw new IllegalStateException("state: " + Http1Codec.this.state);
            }

            Http1Codec.this.detachTimeout(this.timeout);
            Http1Codec.this.state = 6;
            if (Http1Codec.this.streamAllocation != null) {
               StreamAllocation var2 = Http1Codec.this.streamAllocation;
               if (!var1) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               var2.streamFinished(var1, Http1Codec.this);
            }
         }

      }

      public Timeout timeout() {
         return this.timeout;
      }
   }

   private final class ChunkedSink implements Sink {
      private boolean closed;
      private final ForwardingTimeout timeout;

      ChunkedSink() {
         this.timeout = new ForwardingTimeout(Http1Codec.this.sink.timeout());
      }

      public void close() throws IOException {
         synchronized(this){}

         Throwable var10000;
         label76: {
            boolean var1;
            boolean var10001;
            try {
               var1 = this.closed;
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label76;
            }

            if (var1) {
               return;
            }

            label67:
            try {
               this.closed = true;
               Http1Codec.this.sink.writeUtf8("0\r\n\r\n");
               Http1Codec.this.detachTimeout(this.timeout);
               Http1Codec.this.state = 3;
               return;
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label67;
            }
         }

         Throwable var2 = var10000;
         throw var2;
      }

      public void flush() throws IOException {
         synchronized(this){}

         Throwable var10000;
         label76: {
            boolean var1;
            boolean var10001;
            try {
               var1 = this.closed;
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label76;
            }

            if (var1) {
               return;
            }

            label67:
            try {
               Http1Codec.this.sink.flush();
               return;
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label67;
            }
         }

         Throwable var2 = var10000;
         throw var2;
      }

      public Timeout timeout() {
         return this.timeout;
      }

      public void write(Buffer var1, long var2) throws IOException {
         if (this.closed) {
            throw new IllegalStateException("closed");
         } else {
            if (var2 != 0L) {
               Http1Codec.this.sink.writeHexadecimalUnsignedLong(var2);
               Http1Codec.this.sink.writeUtf8("\r\n");
               Http1Codec.this.sink.write(var1, var2);
               Http1Codec.this.sink.writeUtf8("\r\n");
            }

         }
      }
   }

   private class ChunkedSource extends Http1Codec.AbstractSource {
      private static final long NO_CHUNK_YET = -1L;
      private long bytesRemainingInChunk = -1L;
      private boolean hasMoreChunks = true;
      private final HttpUrl url;

      ChunkedSource(HttpUrl var2) {
         super(null);
         this.url = var2;
      }

      private void readChunkSize() throws IOException {
         if (this.bytesRemainingInChunk != -1L) {
            Http1Codec.this.source.readUtf8LineStrict();
         }

         try {
            this.bytesRemainingInChunk = Http1Codec.this.source.readHexadecimalUnsignedLong();
            String var1 = Http1Codec.this.source.readUtf8LineStrict().trim();
            if (this.bytesRemainingInChunk < 0L || !var1.isEmpty() && !var1.startsWith(";")) {
               StringBuilder var3 = new StringBuilder();
               ProtocolException var2 = new ProtocolException(var3.append("expected chunk size and optional extensions but was \"").append(this.bytesRemainingInChunk).append(var1).append("\"").toString());
               throw var2;
            }
         } catch (NumberFormatException var4) {
            throw new ProtocolException(var4.getMessage());
         }

         if (this.bytesRemainingInChunk == 0L) {
            this.hasMoreChunks = false;
            HttpHeaders.receiveHeaders(Http1Codec.this.client.cookieJar(), this.url, Http1Codec.this.readHeaders());
            this.endOfInput(true);
         }

      }

      public void close() throws IOException {
         if (!this.closed) {
            if (this.hasMoreChunks && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
               this.endOfInput(false);
            }

            this.closed = true;
         }

      }

      public long read(Buffer var1, long var2) throws IOException {
         if (var2 < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + var2);
         } else if (this.closed) {
            throw new IllegalStateException("closed");
         } else {
            if (!this.hasMoreChunks) {
               var2 = -1L;
            } else {
               if (this.bytesRemainingInChunk == 0L || this.bytesRemainingInChunk == -1L) {
                  this.readChunkSize();
                  if (!this.hasMoreChunks) {
                     var2 = -1L;
                     return var2;
                  }
               }

               var2 = Http1Codec.this.source.read(var1, Math.min(var2, this.bytesRemainingInChunk));
               if (var2 == -1L) {
                  this.endOfInput(false);
                  throw new ProtocolException("unexpected end of stream");
               }

               this.bytesRemainingInChunk -= var2;
            }

            return var2;
         }
      }
   }

   private final class FixedLengthSink implements Sink {
      private long bytesRemaining;
      private boolean closed;
      private final ForwardingTimeout timeout;

      FixedLengthSink(long var2) {
         this.timeout = new ForwardingTimeout(Http1Codec.this.sink.timeout());
         this.bytesRemaining = var2;
      }

      public void close() throws IOException {
         if (!this.closed) {
            this.closed = true;
            if (this.bytesRemaining > 0L) {
               throw new ProtocolException("unexpected end of stream");
            }

            Http1Codec.this.detachTimeout(this.timeout);
            Http1Codec.this.state = 3;
         }

      }

      public void flush() throws IOException {
         if (!this.closed) {
            Http1Codec.this.sink.flush();
         }

      }

      public Timeout timeout() {
         return this.timeout;
      }

      public void write(Buffer var1, long var2) throws IOException {
         if (this.closed) {
            throw new IllegalStateException("closed");
         } else {
            Util.checkOffsetAndCount(var1.size(), 0L, var2);
            if (var2 > this.bytesRemaining) {
               throw new ProtocolException("expected " + this.bytesRemaining + " bytes but received " + var2);
            } else {
               Http1Codec.this.sink.write(var1, var2);
               this.bytesRemaining -= var2;
            }
         }
      }
   }

   private class FixedLengthSource extends Http1Codec.AbstractSource {
      private long bytesRemaining;

      public FixedLengthSource(long var2) throws IOException {
         super(null);
         this.bytesRemaining = var2;
         if (this.bytesRemaining == 0L) {
            this.endOfInput(true);
         }

      }

      public void close() throws IOException {
         if (!this.closed) {
            if (this.bytesRemaining != 0L && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
               this.endOfInput(false);
            }

            this.closed = true;
         }

      }

      public long read(Buffer var1, long var2) throws IOException {
         if (var2 < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + var2);
         } else if (this.closed) {
            throw new IllegalStateException("closed");
         } else {
            if (this.bytesRemaining == 0L) {
               var2 = -1L;
            } else {
               long var4 = Http1Codec.this.source.read(var1, Math.min(this.bytesRemaining, var2));
               if (var4 == -1L) {
                  this.endOfInput(false);
                  throw new ProtocolException("unexpected end of stream");
               }

               this.bytesRemaining -= var4;
               var2 = var4;
               if (this.bytesRemaining == 0L) {
                  this.endOfInput(true);
                  var2 = var4;
               }
            }

            return var2;
         }
      }
   }

   private class UnknownLengthSource extends Http1Codec.AbstractSource {
      private boolean inputExhausted;

      UnknownLengthSource() {
         super(null);
      }

      public void close() throws IOException {
         if (!this.closed) {
            if (!this.inputExhausted) {
               this.endOfInput(false);
            }

            this.closed = true;
         }

      }

      public long read(Buffer var1, long var2) throws IOException {
         if (var2 < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + var2);
         } else if (this.closed) {
            throw new IllegalStateException("closed");
         } else {
            if (this.inputExhausted) {
               var2 = -1L;
            } else {
               long var4 = Http1Codec.this.source.read(var1, var2);
               var2 = var4;
               if (var4 == -1L) {
                  this.inputExhausted = true;
                  this.endOfInput(true);
                  var2 = -1L;
               }
            }

            return var2;
         }
      }
   }
}

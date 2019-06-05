package okhttp3.internal.ws;

import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.StreamAllocation;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;

public final class RealWebSocket implements WebSocket, WebSocketReader.FrameCallback {
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   private static final long CANCEL_AFTER_CLOSE_MILLIS = 60000L;
   private static final long MAX_QUEUE_SIZE = 16777216L;
   private static final List ONLY_HTTP1;
   private Call call;
   private ScheduledFuture cancelFuture;
   private boolean enqueuedClose;
   private ScheduledExecutorService executor;
   private boolean failed;
   private final String key;
   final WebSocketListener listener;
   private final ArrayDeque messageAndCloseQueue = new ArrayDeque();
   private final Request originalRequest;
   int pingCount;
   int pongCount;
   private final ArrayDeque pongQueue = new ArrayDeque();
   private long queueSize;
   private final Random random;
   private WebSocketReader reader;
   private int receivedCloseCode = -1;
   private String receivedCloseReason;
   private RealWebSocket.Streams streams;
   private WebSocketWriter writer;
   private final Runnable writerRunnable;

   static {
      boolean var0;
      if (!RealWebSocket.class.desiredAssertionStatus()) {
         var0 = true;
      } else {
         var0 = false;
      }

      $assertionsDisabled = var0;
      ONLY_HTTP1 = Collections.singletonList(Protocol.HTTP_1_1);
   }

   public RealWebSocket(Request var1, WebSocketListener var2, Random var3) {
      if (!"GET".equals(var1.method())) {
         throw new IllegalArgumentException("Request must be GET: " + var1.method());
      } else {
         this.originalRequest = var1;
         this.listener = var2;
         this.random = var3;
         byte[] var4 = new byte[16];
         var3.nextBytes(var4);
         this.key = ByteString.of(var4).base64();
         this.writerRunnable = new Runnable() {
            public void run() {
               boolean var1;
               do {
                  try {
                     var1 = RealWebSocket.this.writeOneFrame();
                  } catch (IOException var3) {
                     RealWebSocket.this.failWebSocket(var3, (Response)null);
                     break;
                  }
               } while(var1);

            }
         };
      }
   }

   private void runWriter() {
      if (!$assertionsDisabled && !Thread.holdsLock(this)) {
         throw new AssertionError();
      } else {
         if (this.executor != null) {
            this.executor.execute(this.writerRunnable);
         }

      }
   }

   private boolean send(ByteString var1, int var2) {
      boolean var3 = false;
      synchronized(this){}
      boolean var4 = var3;

      Throwable var10000;
      label184: {
         boolean var10001;
         try {
            if (this.failed) {
               return var4;
            }

            var4 = this.enqueuedClose;
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label184;
         }

         if (var4) {
            var4 = var3;
            return var4;
         }

         label185: {
            try {
               if (this.queueSize + (long)var1.size() > 16777216L) {
                  this.close(1001, (String)null);
                  break label185;
               }
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break label184;
            }

            try {
               this.queueSize += (long)var1.size();
               ArrayDeque var5 = this.messageAndCloseQueue;
               RealWebSocket.Message var6 = new RealWebSocket.Message(var2, var1);
               var5.add(var6);
               this.runWriter();
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label184;
            }

            var4 = true;
            return var4;
         }

         var4 = var3;
         return var4;
      }

      Throwable var19 = var10000;
      throw var19;
   }

   void awaitTermination(int var1, TimeUnit var2) throws InterruptedException {
      this.executor.awaitTermination((long)var1, var2);
   }

   public void cancel() {
      this.call.cancel();
   }

   void checkResponse(Response var1) throws ProtocolException {
      if (var1.code() != 101) {
         throw new ProtocolException("Expected HTTP 101 response but was '" + var1.code() + " " + var1.message() + "'");
      } else {
         String var2 = var1.header("Connection");
         if (!"Upgrade".equalsIgnoreCase(var2)) {
            throw new ProtocolException("Expected 'Connection' header value 'Upgrade' but was '" + var2 + "'");
         } else {
            var2 = var1.header("Upgrade");
            if (!"websocket".equalsIgnoreCase(var2)) {
               throw new ProtocolException("Expected 'Upgrade' header value 'websocket' but was '" + var2 + "'");
            } else {
               var2 = var1.header("Sec-WebSocket-Accept");
               String var3 = ByteString.encodeUtf8(this.key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").sha1().base64();
               if (!var3.equals(var2)) {
                  throw new ProtocolException("Expected 'Sec-WebSocket-Accept' header value '" + var3 + "' but was '" + var2 + "'");
               }
            }
         }
      }
   }

   public boolean close(int var1, String var2) {
      return this.close(var1, var2, 60000L);
   }

   boolean close(int var1, String var2, long var3) {
      boolean var5 = true;
      synchronized(this){}

      Throwable var10000;
      label302: {
         boolean var10001;
         try {
            WebSocketProtocol.validateCloseCode(var1);
         } catch (Throwable var37) {
            var10000 = var37;
            var10001 = false;
            break label302;
         }

         ByteString var6 = null;
         if (var2 != null) {
            ByteString var7;
            try {
               var7 = ByteString.encodeUtf8(var2);
            } catch (Throwable var36) {
               var10000 = var36;
               var10001 = false;
               break label302;
            }

            var6 = var7;

            try {
               if ((long)var7.size() > 123L) {
                  StringBuilder var41 = new StringBuilder();
                  IllegalArgumentException var43 = new IllegalArgumentException(var41.append("reason.size() > 123: ").append(var2).toString());
                  throw var43;
               }
            } catch (Throwable var35) {
               var10000 = var35;
               var10001 = false;
               break label302;
            }
         }

         label287: {
            boolean var8;
            try {
               if (this.failed) {
                  break label287;
               }

               var8 = this.enqueuedClose;
            } catch (Throwable var38) {
               var10000 = var38;
               var10001 = false;
               break label302;
            }

            if (!var8) {
               try {
                  this.enqueuedClose = true;
                  ArrayDeque var40 = this.messageAndCloseQueue;
                  RealWebSocket.Close var42 = new RealWebSocket.Close(var1, var6, var3);
                  var40.add(var42);
                  this.runWriter();
                  return var5;
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label302;
               }
            }
         }

         var5 = false;
         return var5;
      }

      Throwable var39 = var10000;
      throw var39;
   }

   public void connect(OkHttpClient var1) {
      var1 = var1.newBuilder().protocols(ONLY_HTTP1).build();
      final int var2 = var1.pingIntervalMillis();
      final Request var3 = this.originalRequest.newBuilder().header("Upgrade", "websocket").header("Connection", "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build();
      this.call = Internal.instance.newWebSocketCall(var1, var3);
      this.call.enqueue(new Callback() {
         public void onFailure(Call var1, IOException var2x) {
            RealWebSocket.this.failWebSocket(var2x, (Response)null);
         }

         public void onResponse(Call var1, Response var2x) {
            try {
               RealWebSocket.this.checkResponse(var2x);
            } catch (ProtocolException var5) {
               RealWebSocket.this.failWebSocket(var5, var2x);
               Util.closeQuietly((Closeable)var2x);
               return;
            }

            StreamAllocation var3x = Internal.instance.streamAllocation(var1);
            var3x.noNewStreams();
            RealWebSocket.Streams var6 = var3x.connection().newWebSocketStreams(var3x);

            try {
               RealWebSocket.this.listener.onOpen(RealWebSocket.this, var2x);
               StringBuilder var7 = new StringBuilder();
               String var8 = var7.append("OkHttp WebSocket ").append(var3.url().redact()).toString();
               RealWebSocket.this.initReaderAndWriter(var8, (long)var2, var6);
               var3x.connection().socket().setSoTimeout(0);
               RealWebSocket.this.loopReader();
            } catch (Exception var4) {
               RealWebSocket.this.failWebSocket(var4, (Response)null);
            }

         }
      });
   }

   public void failWebSocket(Exception var1, Response var2) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label389: {
         try {
            if (this.failed) {
               return;
            }
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label389;
         }

         RealWebSocket.Streams var3;
         try {
            this.failed = true;
            var3 = this.streams;
            this.streams = null;
            if (this.cancelFuture != null) {
               this.cancelFuture.cancel(false);
            }
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label389;
         }

         try {
            if (this.executor != null) {
               this.executor.shutdown();
            }
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label389;
         }

         try {
            ;
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label389;
         }

         try {
            this.listener.onFailure(this, var1, var2);
            return;
         } finally {
            Util.closeQuietly((Closeable)var3);
         }
      }

      while(true) {
         Throwable var46 = var10000;

         try {
            throw var46;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            continue;
         }
      }
   }

   public void initReaderAndWriter(String var1, long var2, RealWebSocket.Streams var4) throws IOException {
      synchronized(this){}

      label281: {
         Throwable var10000;
         boolean var10001;
         label282: {
            try {
               this.streams = var4;
               WebSocketWriter var5 = new WebSocketWriter(var4.client, var4.sink, this.random);
               this.writer = var5;
               ScheduledThreadPoolExecutor var38 = new ScheduledThreadPoolExecutor(1, Util.threadFactory(var1, false));
               this.executor = var38;
            } catch (Throwable var35) {
               var10000 = var35;
               var10001 = false;
               break label282;
            }

            if (var2 != 0L) {
               try {
                  ScheduledExecutorService var39 = this.executor;
                  RealWebSocket.PingRunnable var36 = new RealWebSocket.PingRunnable();
                  var39.scheduleAtFixedRate(var36, var2, var2, TimeUnit.MILLISECONDS);
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label282;
               }
            }

            try {
               if (!this.messageAndCloseQueue.isEmpty()) {
                  this.runWriter();
               }
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label282;
            }

            label266:
            try {
               break label281;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label266;
            }
         }

         while(true) {
            Throwable var37 = var10000;

            try {
               throw var37;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               continue;
            }
         }
      }

      this.reader = new WebSocketReader(var4.client, var4.source, this);
   }

   public void loopReader() throws IOException {
      while(this.receivedCloseCode == -1) {
         this.reader.processNextFrame();
      }

   }

   public void onReadClose(int var1, String var2) {
      if (var1 == -1) {
         throw new IllegalArgumentException();
      } else {
         Object var3 = null;
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         Throwable var95;
         label887: {
            try {
               if (this.receivedCloseCode != -1) {
                  IllegalStateException var96 = new IllegalStateException("already closed");
                  throw var96;
               }
            } catch (Throwable var94) {
               var10000 = var94;
               var10001 = false;
               break label887;
            }

            try {
               this.receivedCloseCode = var1;
               this.receivedCloseReason = var2;
            } catch (Throwable var93) {
               var10000 = var93;
               var10001 = false;
               break label887;
            }

            RealWebSocket.Streams var4 = (RealWebSocket.Streams)var3;

            label888: {
               try {
                  if (!this.enqueuedClose) {
                     break label888;
                  }
               } catch (Throwable var92) {
                  var10000 = var92;
                  var10001 = false;
                  break label887;
               }

               var4 = (RealWebSocket.Streams)var3;

               try {
                  if (!this.messageAndCloseQueue.isEmpty()) {
                     break label888;
                  }

                  var4 = this.streams;
                  this.streams = null;
                  if (this.cancelFuture != null) {
                     this.cancelFuture.cancel(false);
                  }
               } catch (Throwable var91) {
                  var10000 = var91;
                  var10001 = false;
                  break label887;
               }

               try {
                  this.executor.shutdown();
               } catch (Throwable var90) {
                  var10000 = var90;
                  var10001 = false;
                  break label887;
               }
            }

            try {
               ;
            } catch (Throwable var89) {
               var10000 = var89;
               var10001 = false;
               break label887;
            }

            label850: {
               label890: {
                  try {
                     this.listener.onClosing(this, var1, var2);
                  } catch (Throwable var88) {
                     var10000 = var88;
                     var10001 = false;
                     break label890;
                  }

                  if (var4 == null) {
                     break label850;
                  }

                  label845:
                  try {
                     this.listener.onClosed(this, var1, var2);
                     break label850;
                  } catch (Throwable var87) {
                     var10000 = var87;
                     var10001 = false;
                     break label845;
                  }
               }

               var95 = var10000;
               Util.closeQuietly((Closeable)var4);
               throw var95;
            }

            Util.closeQuietly((Closeable)var4);
            return;
         }

         while(true) {
            var95 = var10000;

            try {
               throw var95;
            } catch (Throwable var86) {
               var10000 = var86;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void onReadMessage(String var1) throws IOException {
      this.listener.onMessage(this, (String)var1);
   }

   public void onReadMessage(ByteString var1) throws IOException {
      this.listener.onMessage(this, (ByteString)var1);
   }

   public void onReadPing(ByteString var1) {
      synchronized(this){}

      Throwable var10000;
      label92: {
         boolean var10001;
         label91: {
            boolean var2;
            try {
               if (this.failed) {
                  return;
               }

               if (!this.enqueuedClose) {
                  break label91;
               }

               var2 = this.messageAndCloseQueue.isEmpty();
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label92;
            }

            if (var2) {
               return;
            }
         }

         label84:
         try {
            this.pongQueue.add(var1);
            this.runWriter();
            ++this.pingCount;
            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label84;
         }
      }

      Throwable var9 = var10000;
      throw var9;
   }

   public void onReadPong(ByteString var1) {
      synchronized(this){}

      try {
         ++this.pongCount;
      } finally {
         ;
      }

   }

   int pingCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.pingCount;
      } finally {
         ;
      }

      return var1;
   }

   boolean pong(ByteString var1) {
      synchronized(this){}

      boolean var2;
      label108: {
         Throwable var10000;
         label119: {
            boolean var10001;
            label106: {
               try {
                  if (this.failed) {
                     break label108;
                  }

                  if (!this.enqueuedClose) {
                     break label106;
                  }

                  var2 = this.messageAndCloseQueue.isEmpty();
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label119;
               }

               if (var2) {
                  break label108;
               }
            }

            try {
               this.pongQueue.add(var1);
               this.runWriter();
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label119;
            }

            var2 = true;
            return var2;
         }

         Throwable var9 = var10000;
         throw var9;
      }

      var2 = false;
      return var2;
   }

   int pongCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.pongCount;
      } finally {
         ;
      }

      return var1;
   }

   boolean processNextFrame() throws IOException {
      boolean var1 = false;

      int var2;
      try {
         this.reader.processNextFrame();
         var2 = this.receivedCloseCode;
      } catch (Exception var4) {
         this.failWebSocket(var4, (Response)null);
         return var1;
      }

      if (var2 == -1) {
         var1 = true;
      }

      return var1;
   }

   public long queueSize() {
      synchronized(this){}

      long var1;
      try {
         var1 = this.queueSize;
      } finally {
         ;
      }

      return var1;
   }

   public Request request() {
      return this.originalRequest;
   }

   public boolean send(String var1) {
      if (var1 == null) {
         throw new NullPointerException("text == null");
      } else {
         return this.send(ByteString.encodeUtf8(var1), 1);
      }
   }

   public boolean send(ByteString var1) {
      if (var1 == null) {
         throw new NullPointerException("bytes == null");
      } else {
         return this.send(var1, 2);
      }
   }

   void tearDown() throws InterruptedException {
      if (this.cancelFuture != null) {
         this.cancelFuture.cancel(false);
      }

      this.executor.shutdown();
      this.executor.awaitTermination(10L, TimeUnit.SECONDS);
   }

   boolean writeOneFrame() throws IOException {
      // $FF: Couldn't be decompiled
   }

   void writePingFrame() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label168: {
         try {
            if (this.failed) {
               return;
            }
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label168;
         }

         WebSocketWriter var18;
         try {
            var18 = this.writer;
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label168;
         }

         try {
            var18.writePing(ByteString.EMPTY);
         } catch (IOException var14) {
            this.failWebSocket(var14, (Response)null);
         }

         return;
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            continue;
         }
      }
   }

   final class CancelRunnable implements Runnable {
      public void run() {
         RealWebSocket.this.cancel();
      }
   }

   static final class Close {
      final long cancelAfterCloseMillis;
      final int code;
      final ByteString reason;

      Close(int var1, ByteString var2, long var3) {
         this.code = var1;
         this.reason = var2;
         this.cancelAfterCloseMillis = var3;
      }
   }

   static final class Message {
      final ByteString data;
      final int formatOpcode;

      Message(int var1, ByteString var2) {
         this.formatOpcode = var1;
         this.data = var2;
      }
   }

   private final class PingRunnable implements Runnable {
      PingRunnable() {
      }

      public void run() {
         RealWebSocket.this.writePingFrame();
      }
   }

   public abstract static class Streams implements Closeable {
      public final boolean client;
      public final BufferedSink sink;
      public final BufferedSource source;

      public Streams(boolean var1, BufferedSource var2, BufferedSink var3) {
         this.client = var1;
         this.source = var2;
         this.sink = var3;
      }
   }
}

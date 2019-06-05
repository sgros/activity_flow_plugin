package okhttp3.internal.connection;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Address;
import okhttp3.Connection;
import okhttp3.ConnectionPool;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http1.Http1Codec;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.Http2Codec;
import okhttp3.internal.http2.Http2Connection;
import okhttp3.internal.http2.Http2Stream;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.ws.RealWebSocket;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public final class RealConnection extends Http2Connection.Listener implements Connection {
   public int allocationLimit = 1;
   public final List allocations = new ArrayList();
   private final ConnectionPool connectionPool;
   private Handshake handshake;
   private Http2Connection http2Connection;
   public long idleAtNanos = Long.MAX_VALUE;
   public boolean noNewStreams;
   private Protocol protocol;
   private Socket rawSocket;
   private final Route route;
   private BufferedSink sink;
   private Socket socket;
   private BufferedSource source;
   public int successCount;

   public RealConnection(ConnectionPool var1, Route var2) {
      this.connectionPool = var1;
      this.route = var2;
   }

   private void connectSocket(int var1, int var2) throws IOException {
      Proxy var3 = this.route.proxy();
      Address var4 = this.route.address();
      Socket var7;
      if (var3.type() != Type.DIRECT && var3.type() != Type.HTTP) {
         var7 = new Socket(var3);
      } else {
         var7 = var4.socketFactory().createSocket();
      }

      this.rawSocket = var7;
      this.rawSocket.setSoTimeout(var2);

      try {
         Platform.get().connectSocket(this.rawSocket, this.route.socketAddress(), var1);
      } catch (ConnectException var5) {
         ConnectException var6 = new ConnectException("Failed to connect to " + this.route.socketAddress());
         var6.initCause(var5);
         throw var6;
      }

      this.source = Okio.buffer(Okio.source(this.rawSocket));
      this.sink = Okio.buffer(Okio.sink(this.rawSocket));
   }

   private void connectTls(ConnectionSpecSelector param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void connectTunnel(int var1, int var2, int var3) throws IOException {
      Request var4 = this.createTunnelRequest();
      HttpUrl var5 = var4.url();
      int var6 = 0;

      while(true) {
         ++var6;
         if (var6 > 21) {
            throw new ProtocolException("Too many tunnel connections attempted: " + 21);
         }

         this.connectSocket(var1, var2);
         var4 = this.createTunnel(var2, var3, var4, var5);
         if (var4 == null) {
            return;
         }

         Util.closeQuietly(this.rawSocket);
         this.rawSocket = null;
         this.sink = null;
         this.source = null;
      }
   }

   private Request createTunnel(int var1, int var2, Request var3, HttpUrl var4) throws IOException {
      Object var5 = null;
      String var6 = "CONNECT " + Util.hostHeader(var4, true) + " HTTP/1.1";

      while(true) {
         Http1Codec var13 = new Http1Codec((OkHttpClient)null, (StreamAllocation)null, this.source, this.sink);
         this.source.timeout().timeout((long)var1, TimeUnit.MILLISECONDS);
         this.sink.timeout().timeout((long)var2, TimeUnit.MILLISECONDS);
         var13.writeRequest(var3.headers(), var6);
         var13.finishRequest();
         Response var7 = var13.readResponseHeaders(false).request(var3).build();
         long var8 = HttpHeaders.contentLength(var7);
         long var10 = var8;
         if (var8 == -1L) {
            var10 = 0L;
         }

         Source var12 = var13.newFixedLengthSource(var10);
         Util.skipAll(var12, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
         var12.close();
         Request var14;
         switch(var7.code()) {
         case 200:
            if (!this.source.buffer().exhausted()) {
               throw new IOException("TLS tunnel buffered too many bytes!");
            }

            var14 = (Request)var5;
            if (!this.sink.buffer().exhausted()) {
               throw new IOException("TLS tunnel buffered too many bytes!");
            }
            break;
         case 407:
            var14 = this.route.address().proxyAuthenticator().authenticate(this.route, var7);
            if (var14 == null) {
               throw new IOException("Failed to authenticate with proxy");
            }

            var3 = var14;
            if (!"close".equalsIgnoreCase(var7.header("Connection"))) {
               continue;
            }
            break;
         default:
            throw new IOException("Unexpected response code for CONNECT: " + var7.code());
         }

         return var14;
      }
   }

   private Request createTunnelRequest() {
      return (new Request.Builder()).url(this.route.address().url()).header("Host", Util.hostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
   }

   private void establishProtocol(ConnectionSpecSelector var1) throws IOException {
      if (this.route.address().sslSocketFactory() == null) {
         this.protocol = Protocol.HTTP_1_1;
         this.socket = this.rawSocket;
      } else {
         this.connectTls(var1);
         if (this.protocol == Protocol.HTTP_2) {
            this.socket.setSoTimeout(0);
            this.http2Connection = (new Http2Connection.Builder(true)).socket(this.socket, this.route.address().url().host(), this.source, this.sink).listener(this).build();
            this.http2Connection.start();
         }
      }

   }

   public static RealConnection testConnection(ConnectionPool var0, Route var1, Socket var2, long var3) {
      RealConnection var5 = new RealConnection(var0, var1);
      var5.socket = var2;
      var5.idleAtNanos = var3;
      return var5;
   }

   public void cancel() {
      Util.closeQuietly(this.rawSocket);
   }

   public void connect(int param1, int param2, int param3, boolean param4) {
      // $FF: Couldn't be decompiled
   }

   public Handshake handshake() {
      return this.handshake;
   }

   public boolean isEligible(Address var1) {
      boolean var2;
      if (this.allocations.size() < this.allocationLimit && var1.equals(this.route().address()) && !this.noNewStreams) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isHealthy(boolean var1) {
      boolean var2 = true;
      boolean var3;
      if (!this.socket.isClosed() && !this.socket.isInputShutdown() && !this.socket.isOutputShutdown()) {
         if (this.http2Connection != null) {
            var3 = var2;
            if (this.http2Connection.isShutdown()) {
               var3 = false;
            }
         } else {
            var3 = var2;
            if (var1) {
               label155: {
                  label156: {
                     label142: {
                        boolean var10001;
                        int var4;
                        try {
                           var4 = this.socket.getSoTimeout();
                        } catch (SocketTimeoutException var23) {
                           var10001 = false;
                           break label156;
                        } catch (IOException var24) {
                           var10001 = false;
                           break label142;
                        }

                        boolean var15 = false;

                        try {
                           var15 = true;
                           this.socket.setSoTimeout(1);
                           var1 = this.source.exhausted();
                           var15 = false;
                        } finally {
                           if (var15) {
                              try {
                                 this.socket.setSoTimeout(var4);
                              } catch (SocketTimeoutException var16) {
                                 var10001 = false;
                                 break label156;
                              } catch (IOException var17) {
                                 var10001 = false;
                                 break label142;
                              }
                           }
                        }

                        if (var1) {
                           label157: {
                              try {
                                 this.socket.setSoTimeout(var4);
                              } catch (SocketTimeoutException var18) {
                                 var10001 = false;
                                 break label156;
                              } catch (IOException var19) {
                                 var10001 = false;
                                 break label157;
                              }

                              var3 = false;
                              return var3;
                           }
                        } else {
                           try {
                              this.socket.setSoTimeout(var4);
                              break label155;
                           } catch (SocketTimeoutException var20) {
                              var10001 = false;
                              break label156;
                           } catch (IOException var21) {
                              var10001 = false;
                           }
                        }
                     }

                     var3 = false;
                     return var3;
                  }

                  var3 = var2;
                  return var3;
               }

               var3 = var2;
            }
         }
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isMultiplexed() {
      boolean var1;
      if (this.http2Connection != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public HttpCodec newCodec(OkHttpClient var1, StreamAllocation var2) throws SocketException {
      Object var3;
      if (this.http2Connection != null) {
         var3 = new Http2Codec(var1, var2, this.http2Connection);
      } else {
         this.socket.setSoTimeout(var1.readTimeoutMillis());
         this.source.timeout().timeout((long)var1.readTimeoutMillis(), TimeUnit.MILLISECONDS);
         this.sink.timeout().timeout((long)var1.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
         var3 = new Http1Codec(var1, var2, this.source, this.sink);
      }

      return (HttpCodec)var3;
   }

   public RealWebSocket.Streams newWebSocketStreams(final StreamAllocation var1) {
      return new RealWebSocket.Streams(true, this.source, this.sink) {
         public void close() throws IOException {
            var1.streamFinished(true, var1.codec());
         }
      };
   }

   public void onSettings(Http2Connection param1) {
      // $FF: Couldn't be decompiled
   }

   public void onStream(Http2Stream var1) throws IOException {
      var1.close(ErrorCode.REFUSED_STREAM);
   }

   public Protocol protocol() {
      return this.protocol;
   }

   public Route route() {
      return this.route;
   }

   public Socket socket() {
      return this.socket;
   }

   public String toString() {
      StringBuilder var1 = (new StringBuilder()).append("Connection{").append(this.route.address().url().host()).append(":").append(this.route.address().url().port()).append(", proxy=").append(this.route.proxy()).append(" hostAddress=").append(this.route.socketAddress()).append(" cipherSuite=");
      Object var2;
      if (this.handshake != null) {
         var2 = this.handshake.cipherSuite();
      } else {
         var2 = "none";
      }

      return var1.append(var2).append(" protocol=").append(this.protocol).append('}').toString();
   }
}

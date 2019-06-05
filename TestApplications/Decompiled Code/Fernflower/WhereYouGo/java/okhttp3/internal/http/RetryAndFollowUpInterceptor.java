package okhttp3.internal.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpRetryException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.Proxy.Type;
import java.security.cert.CertificateException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http2.ConnectionShutdownException;

public final class RetryAndFollowUpInterceptor implements Interceptor {
   private static final int MAX_FOLLOW_UPS = 20;
   private Object callStackTrace;
   private volatile boolean canceled;
   private final OkHttpClient client;
   private final boolean forWebSocket;
   private StreamAllocation streamAllocation;

   public RetryAndFollowUpInterceptor(OkHttpClient var1, boolean var2) {
      this.client = var1;
      this.forWebSocket = var2;
   }

   private Address createAddress(HttpUrl var1) {
      SSLSocketFactory var2 = null;
      HostnameVerifier var3 = null;
      CertificatePinner var4 = null;
      if (var1.isHttps()) {
         var2 = this.client.sslSocketFactory();
         var3 = this.client.hostnameVerifier();
         var4 = this.client.certificatePinner();
      }

      return new Address(var1.host(), var1.port(), this.client.dns(), this.client.socketFactory(), var2, var3, var4, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
   }

   private Request followUpRequest(Response var1) throws IOException {
      Proxy var2 = null;
      if (var1 == null) {
         throw new IllegalStateException();
      } else {
         RealConnection var3 = this.streamAllocation.connection();
         Route var8;
         if (var3 != null) {
            var8 = var3.route();
         } else {
            var8 = null;
         }

         int var4 = var1.code();
         String var5 = var1.request().method();
         Request var10;
         switch(var4) {
         case 307:
         case 308:
            if (!var5.equals("GET")) {
               var10 = var2;
               if (!var5.equals("HEAD")) {
                  break;
               }
            }
         case 300:
         case 301:
         case 302:
         case 303:
            var10 = var2;
            if (this.client.followRedirects()) {
               String var6 = var1.header("Location");
               var10 = var2;
               if (var6 != null) {
                  HttpUrl var11 = var1.request().url().resolve(var6);
                  var10 = var2;
                  if (var11 != null) {
                     if (!var11.scheme().equals(var1.request().url().scheme())) {
                        var10 = var2;
                        if (!this.client.followSslRedirects()) {
                           return var10;
                        }
                     }

                     Request.Builder var9 = var1.request().newBuilder();
                     if (HttpMethod.permitsRequestBody(var5)) {
                        boolean var7 = HttpMethod.redirectsWithBody(var5);
                        if (HttpMethod.redirectsToGet(var5)) {
                           var9.method("GET", (RequestBody)null);
                        } else {
                           RequestBody var12;
                           if (var7) {
                              var12 = var1.request().body();
                           } else {
                              var12 = null;
                           }

                           var9.method(var5, var12);
                        }

                        if (!var7) {
                           var9.removeHeader("Transfer-Encoding");
                           var9.removeHeader("Content-Length");
                           var9.removeHeader("Content-Type");
                        }
                     }

                     if (!this.sameConnection(var1, var11)) {
                        var9.removeHeader("Authorization");
                     }

                     var10 = var9.url(var11).build();
                  }
               }
            }
            break;
         case 401:
            var10 = this.client.authenticator().authenticate(var8, var1);
            break;
         case 407:
            if (var8 != null) {
               var2 = var8.proxy();
            } else {
               var2 = this.client.proxy();
            }

            if (var2.type() != Type.HTTP) {
               throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
            }

            var10 = this.client.proxyAuthenticator().authenticate(var8, var1);
            break;
         case 408:
            var10 = var2;
            if (!(var1.request().body() instanceof UnrepeatableRequestBody)) {
               var10 = var1.request();
            }
            break;
         default:
            var10 = var2;
         }

         return var10;
      }
   }

   private boolean isRecoverable(IOException var1, boolean var2) {
      boolean var3 = true;
      boolean var4 = false;
      if (var1 instanceof ProtocolException) {
         var2 = var4;
      } else if (var1 instanceof InterruptedIOException) {
         if (var1 instanceof SocketTimeoutException && !var2) {
            var2 = var3;
         } else {
            var2 = false;
         }
      } else {
         if (var1 instanceof SSLHandshakeException) {
            var2 = var4;
            if (var1.getCause() instanceof CertificateException) {
               return var2;
            }
         }

         var2 = var4;
         if (!(var1 instanceof SSLPeerUnverifiedException)) {
            var2 = true;
         }
      }

      return var2;
   }

   private boolean recover(IOException var1, boolean var2, Request var3) {
      boolean var4 = false;
      this.streamAllocation.streamFailed(var1);
      boolean var5;
      if (!this.client.retryOnConnectionFailure()) {
         var5 = var4;
      } else {
         if (var2) {
            var5 = var4;
            if (var3.body() instanceof UnrepeatableRequestBody) {
               return var5;
            }
         }

         var5 = var4;
         if (this.isRecoverable(var1, var2)) {
            var5 = var4;
            if (this.streamAllocation.hasMoreRoutes()) {
               var5 = true;
            }
         }
      }

      return var5;
   }

   private boolean sameConnection(Response var1, HttpUrl var2) {
      HttpUrl var4 = var1.request().url();
      boolean var3;
      if (var4.host().equals(var2.host()) && var4.port() == var2.port() && var4.scheme().equals(var2.scheme())) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public void cancel() {
      this.canceled = true;
      StreamAllocation var1 = this.streamAllocation;
      if (var1 != null) {
         var1.cancel();
      }

   }

   public Response intercept(Interceptor.Chain var1) throws IOException {
      Request var2 = var1.request();
      this.streamAllocation = new StreamAllocation(this.client.connectionPool(), this.createAddress(var2.url()), this.callStackTrace);
      int var3 = 0;
      Response var4 = null;

      while(!this.canceled) {
         Response var40;
         label525: {
            Throwable var10000;
            label526: {
               IOException var5;
               boolean var10001;
               label527: {
                  RouteException var39;
                  try {
                     try {
                        var40 = ((RealInterceptorChain)var1).proceed(var2, this.streamAllocation, (HttpCodec)null, (Connection)null);
                        break label525;
                     } catch (RouteException var34) {
                        var39 = var34;
                     } catch (IOException var35) {
                        var5 = var35;
                        break label527;
                     }
                  } catch (Throwable var36) {
                     var10000 = var36;
                     var10001 = false;
                     break label526;
                  }

                  try {
                     if (!this.recover(var39.getLastConnectException(), false, var2)) {
                        throw var39.getLastConnectException();
                     }
                  } catch (Throwable var31) {
                     var10000 = var31;
                     var10001 = false;
                     break label526;
                  }

                  if (false) {
                     this.streamAllocation.streamFailed((IOException)null);
                     this.streamAllocation.release();
                  }
                  continue;
               }

               boolean var6;
               label508: {
                  label507: {
                     try {
                        if (!(var5 instanceof ConnectionShutdownException)) {
                           break label507;
                        }
                     } catch (Throwable var33) {
                        var10000 = var33;
                        var10001 = false;
                        break label526;
                     }

                     var6 = false;
                     break label508;
                  }

                  var6 = true;
               }

               try {
                  if (!this.recover(var5, var6, var2)) {
                     throw var5;
                  }
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label526;
               }

               if (false) {
                  this.streamAllocation.streamFailed((IOException)null);
                  this.streamAllocation.release();
               }
               continue;
            }

            Throwable var37 = var10000;
            if (true) {
               this.streamAllocation.streamFailed((IOException)null);
               this.streamAllocation.release();
            }

            throw var37;
         }

         if (false) {
            this.streamAllocation.streamFailed((IOException)null);
            this.streamAllocation.release();
         }

         Response var38 = var40;
         if (var4 != null) {
            var38 = var40.newBuilder().priorResponse(var4.newBuilder().body((ResponseBody)null).build()).build();
         }

         Request var41 = this.followUpRequest(var38);
         if (var41 == null) {
            if (!this.forWebSocket) {
               this.streamAllocation.release();
            }

            return var38;
         }

         Util.closeQuietly((Closeable)var38.body());
         ++var3;
         if (var3 > 20) {
            this.streamAllocation.release();
            throw new ProtocolException("Too many follow-up requests: " + var3);
         }

         if (var41.body() instanceof UnrepeatableRequestBody) {
            this.streamAllocation.release();
            throw new HttpRetryException("Cannot retry streamed HTTP body", var38.code());
         }

         if (!this.sameConnection(var38, var41.url())) {
            this.streamAllocation.release();
            this.streamAllocation = new StreamAllocation(this.client.connectionPool(), this.createAddress(var41.url()), this.callStackTrace);
         } else if (this.streamAllocation.codec() != null) {
            throw new IllegalStateException("Closing the body of " + var38 + " didn't close its backing stream. Bad interceptor?");
         }

         var4 = var38;
         var2 = var41;
      }

      this.streamAllocation.release();
      throw new IOException("Canceled");
   }

   public boolean isCanceled() {
      return this.canceled;
   }

   public void setCallStackTrace(Object var1) {
      this.callStackTrace = var1;
   }

   public StreamAllocation streamAllocation() {
      return this.streamAllocation;
   }
}

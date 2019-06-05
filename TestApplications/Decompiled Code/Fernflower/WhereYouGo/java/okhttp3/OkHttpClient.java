package okhttp3;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteDatabase;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.internal.ws.RealWebSocket;

public class OkHttpClient implements Cloneable, Call.Factory, WebSocket.Factory {
   static final List DEFAULT_CONNECTION_SPECS;
   static final List DEFAULT_PROTOCOLS;
   final Authenticator authenticator;
   final Cache cache;
   final CertificateChainCleaner certificateChainCleaner;
   final CertificatePinner certificatePinner;
   final int connectTimeout;
   final ConnectionPool connectionPool;
   final List connectionSpecs;
   final CookieJar cookieJar;
   final Dispatcher dispatcher;
   final Dns dns;
   final boolean followRedirects;
   final boolean followSslRedirects;
   final HostnameVerifier hostnameVerifier;
   final List interceptors;
   final InternalCache internalCache;
   final List networkInterceptors;
   final int pingInterval;
   final List protocols;
   final Proxy proxy;
   final Authenticator proxyAuthenticator;
   final ProxySelector proxySelector;
   final int readTimeout;
   final boolean retryOnConnectionFailure;
   final SocketFactory socketFactory;
   final SSLSocketFactory sslSocketFactory;
   final int writeTimeout;

   static {
      DEFAULT_PROTOCOLS = Util.immutableList((Object[])(Protocol.HTTP_2, Protocol.HTTP_1_1));
      DEFAULT_CONNECTION_SPECS = Util.immutableList((Object[])(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT));
      Internal.instance = new Internal() {
         public void addLenient(Headers.Builder var1, String var2) {
            var1.addLenient(var2);
         }

         public void addLenient(Headers.Builder var1, String var2, String var3) {
            var1.addLenient(var2, var3);
         }

         public void apply(ConnectionSpec var1, SSLSocket var2, boolean var3) {
            var1.apply(var2, var3);
         }

         public int code(Response.Builder var1) {
            return var1.code;
         }

         public boolean connectionBecameIdle(ConnectionPool var1, RealConnection var2) {
            return var1.connectionBecameIdle(var2);
         }

         public Socket deduplicate(ConnectionPool var1, Address var2, StreamAllocation var3) {
            return var1.deduplicate(var2, var3);
         }

         public RealConnection get(ConnectionPool var1, Address var2, StreamAllocation var3) {
            return var1.get(var2, var3);
         }

         public HttpUrl getHttpUrlChecked(String var1) throws MalformedURLException, UnknownHostException {
            return HttpUrl.getChecked(var1);
         }

         public Call newWebSocketCall(OkHttpClient var1, Request var2) {
            return new RealCall(var1, var2, true);
         }

         public void put(ConnectionPool var1, RealConnection var2) {
            var1.put(var2);
         }

         public RouteDatabase routeDatabase(ConnectionPool var1) {
            return var1.routeDatabase;
         }

         public void setCache(OkHttpClient.Builder var1, InternalCache var2) {
            var1.setInternalCache(var2);
         }

         public StreamAllocation streamAllocation(Call var1) {
            return ((RealCall)var1).streamAllocation();
         }
      };
   }

   public OkHttpClient() {
      this(new OkHttpClient.Builder());
   }

   OkHttpClient(OkHttpClient.Builder var1) {
      this.dispatcher = var1.dispatcher;
      this.proxy = var1.proxy;
      this.protocols = var1.protocols;
      this.connectionSpecs = var1.connectionSpecs;
      this.interceptors = Util.immutableList(var1.interceptors);
      this.networkInterceptors = Util.immutableList(var1.networkInterceptors);
      this.proxySelector = var1.proxySelector;
      this.cookieJar = var1.cookieJar;
      this.cache = var1.cache;
      this.internalCache = var1.internalCache;
      this.socketFactory = var1.socketFactory;
      boolean var2 = false;
      Iterator var3 = this.connectionSpecs.iterator();

      while(true) {
         while(var3.hasNext()) {
            ConnectionSpec var4 = (ConnectionSpec)var3.next();
            if (!var2 && !var4.isTls()) {
               var2 = false;
            } else {
               var2 = true;
            }
         }

         if (var1.sslSocketFactory == null && var2) {
            X509TrustManager var5 = this.systemDefaultTrustManager();
            this.sslSocketFactory = this.systemDefaultSslSocketFactory(var5);
            this.certificateChainCleaner = CertificateChainCleaner.get(var5);
         } else {
            this.sslSocketFactory = var1.sslSocketFactory;
            this.certificateChainCleaner = var1.certificateChainCleaner;
         }

         this.hostnameVerifier = var1.hostnameVerifier;
         this.certificatePinner = var1.certificatePinner.withCertificateChainCleaner(this.certificateChainCleaner);
         this.proxyAuthenticator = var1.proxyAuthenticator;
         this.authenticator = var1.authenticator;
         this.connectionPool = var1.connectionPool;
         this.dns = var1.dns;
         this.followSslRedirects = var1.followSslRedirects;
         this.followRedirects = var1.followRedirects;
         this.retryOnConnectionFailure = var1.retryOnConnectionFailure;
         this.connectTimeout = var1.connectTimeout;
         this.readTimeout = var1.readTimeout;
         this.writeTimeout = var1.writeTimeout;
         this.pingInterval = var1.pingInterval;
         return;
      }
   }

   private SSLSocketFactory systemDefaultSslSocketFactory(X509TrustManager var1) {
      try {
         SSLContext var2 = SSLContext.getInstance("TLS");
         var2.init((KeyManager[])null, new TrustManager[]{var1}, (SecureRandom)null);
         SSLSocketFactory var4 = var2.getSocketFactory();
         return var4;
      } catch (GeneralSecurityException var3) {
         throw new AssertionError();
      }
   }

   private X509TrustManager systemDefaultTrustManager() {
      try {
         TrustManagerFactory var1 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
         var1.init((KeyStore)null);
         TrustManager[] var5 = var1.getTrustManagers();
         if (var5.length == 1 && var5[0] instanceof X509TrustManager) {
            X509TrustManager var6 = (X509TrustManager)var5[0];
            return var6;
         } else {
            StringBuilder var3 = new StringBuilder();
            IllegalStateException var2 = new IllegalStateException(var3.append("Unexpected default trust managers:").append(Arrays.toString(var5)).toString());
            throw var2;
         }
      } catch (GeneralSecurityException var4) {
         throw new AssertionError();
      }
   }

   public Authenticator authenticator() {
      return this.authenticator;
   }

   public Cache cache() {
      return this.cache;
   }

   public CertificatePinner certificatePinner() {
      return this.certificatePinner;
   }

   public int connectTimeoutMillis() {
      return this.connectTimeout;
   }

   public ConnectionPool connectionPool() {
      return this.connectionPool;
   }

   public List connectionSpecs() {
      return this.connectionSpecs;
   }

   public CookieJar cookieJar() {
      return this.cookieJar;
   }

   public Dispatcher dispatcher() {
      return this.dispatcher;
   }

   public Dns dns() {
      return this.dns;
   }

   public boolean followRedirects() {
      return this.followRedirects;
   }

   public boolean followSslRedirects() {
      return this.followSslRedirects;
   }

   public HostnameVerifier hostnameVerifier() {
      return this.hostnameVerifier;
   }

   public List interceptors() {
      return this.interceptors;
   }

   InternalCache internalCache() {
      InternalCache var1;
      if (this.cache != null) {
         var1 = this.cache.internalCache;
      } else {
         var1 = this.internalCache;
      }

      return var1;
   }

   public List networkInterceptors() {
      return this.networkInterceptors;
   }

   public OkHttpClient.Builder newBuilder() {
      return new OkHttpClient.Builder(this);
   }

   public Call newCall(Request var1) {
      return new RealCall(this, var1, false);
   }

   public WebSocket newWebSocket(Request var1, WebSocketListener var2) {
      RealWebSocket var3 = new RealWebSocket(var1, var2, new SecureRandom());
      var3.connect(this);
      return var3;
   }

   public int pingIntervalMillis() {
      return this.pingInterval;
   }

   public List protocols() {
      return this.protocols;
   }

   public Proxy proxy() {
      return this.proxy;
   }

   public Authenticator proxyAuthenticator() {
      return this.proxyAuthenticator;
   }

   public ProxySelector proxySelector() {
      return this.proxySelector;
   }

   public int readTimeoutMillis() {
      return this.readTimeout;
   }

   public boolean retryOnConnectionFailure() {
      return this.retryOnConnectionFailure;
   }

   public SocketFactory socketFactory() {
      return this.socketFactory;
   }

   public SSLSocketFactory sslSocketFactory() {
      return this.sslSocketFactory;
   }

   public int writeTimeoutMillis() {
      return this.writeTimeout;
   }

   public static final class Builder {
      Authenticator authenticator;
      Cache cache;
      CertificateChainCleaner certificateChainCleaner;
      CertificatePinner certificatePinner;
      int connectTimeout;
      ConnectionPool connectionPool;
      List connectionSpecs;
      CookieJar cookieJar;
      Dispatcher dispatcher;
      Dns dns;
      boolean followRedirects;
      boolean followSslRedirects;
      HostnameVerifier hostnameVerifier;
      final List interceptors = new ArrayList();
      InternalCache internalCache;
      final List networkInterceptors = new ArrayList();
      int pingInterval;
      List protocols;
      Proxy proxy;
      Authenticator proxyAuthenticator;
      ProxySelector proxySelector;
      int readTimeout;
      boolean retryOnConnectionFailure;
      SocketFactory socketFactory;
      SSLSocketFactory sslSocketFactory;
      int writeTimeout;

      public Builder() {
         this.dispatcher = new Dispatcher();
         this.protocols = OkHttpClient.DEFAULT_PROTOCOLS;
         this.connectionSpecs = OkHttpClient.DEFAULT_CONNECTION_SPECS;
         this.proxySelector = ProxySelector.getDefault();
         this.cookieJar = CookieJar.NO_COOKIES;
         this.socketFactory = SocketFactory.getDefault();
         this.hostnameVerifier = OkHostnameVerifier.INSTANCE;
         this.certificatePinner = CertificatePinner.DEFAULT;
         this.proxyAuthenticator = Authenticator.NONE;
         this.authenticator = Authenticator.NONE;
         this.connectionPool = new ConnectionPool();
         this.dns = Dns.SYSTEM;
         this.followSslRedirects = true;
         this.followRedirects = true;
         this.retryOnConnectionFailure = true;
         this.connectTimeout = 10000;
         this.readTimeout = 10000;
         this.writeTimeout = 10000;
         this.pingInterval = 0;
      }

      Builder(OkHttpClient var1) {
         this.dispatcher = var1.dispatcher;
         this.proxy = var1.proxy;
         this.protocols = var1.protocols;
         this.connectionSpecs = var1.connectionSpecs;
         this.interceptors.addAll(var1.interceptors);
         this.networkInterceptors.addAll(var1.networkInterceptors);
         this.proxySelector = var1.proxySelector;
         this.cookieJar = var1.cookieJar;
         this.internalCache = var1.internalCache;
         this.cache = var1.cache;
         this.socketFactory = var1.socketFactory;
         this.sslSocketFactory = var1.sslSocketFactory;
         this.certificateChainCleaner = var1.certificateChainCleaner;
         this.hostnameVerifier = var1.hostnameVerifier;
         this.certificatePinner = var1.certificatePinner;
         this.proxyAuthenticator = var1.proxyAuthenticator;
         this.authenticator = var1.authenticator;
         this.connectionPool = var1.connectionPool;
         this.dns = var1.dns;
         this.followSslRedirects = var1.followSslRedirects;
         this.followRedirects = var1.followRedirects;
         this.retryOnConnectionFailure = var1.retryOnConnectionFailure;
         this.connectTimeout = var1.connectTimeout;
         this.readTimeout = var1.readTimeout;
         this.writeTimeout = var1.writeTimeout;
         this.pingInterval = var1.pingInterval;
      }

      private static int checkDuration(String var0, long var1, TimeUnit var3) {
         if (var1 < 0L) {
            throw new IllegalArgumentException(var0 + " < 0");
         } else if (var3 == null) {
            throw new NullPointerException("unit == null");
         } else {
            long var4 = var3.toMillis(var1);
            if (var4 > 2147483647L) {
               throw new IllegalArgumentException(var0 + " too large.");
            } else if (var4 == 0L && var1 > 0L) {
               throw new IllegalArgumentException(var0 + " too small.");
            } else {
               return (int)var4;
            }
         }
      }

      public OkHttpClient.Builder addInterceptor(Interceptor var1) {
         this.interceptors.add(var1);
         return this;
      }

      public OkHttpClient.Builder addNetworkInterceptor(Interceptor var1) {
         this.networkInterceptors.add(var1);
         return this;
      }

      public OkHttpClient.Builder authenticator(Authenticator var1) {
         if (var1 == null) {
            throw new NullPointerException("authenticator == null");
         } else {
            this.authenticator = var1;
            return this;
         }
      }

      public OkHttpClient build() {
         return new OkHttpClient(this);
      }

      public OkHttpClient.Builder cache(Cache var1) {
         this.cache = var1;
         this.internalCache = null;
         return this;
      }

      public OkHttpClient.Builder certificatePinner(CertificatePinner var1) {
         if (var1 == null) {
            throw new NullPointerException("certificatePinner == null");
         } else {
            this.certificatePinner = var1;
            return this;
         }
      }

      public OkHttpClient.Builder connectTimeout(long var1, TimeUnit var3) {
         this.connectTimeout = checkDuration("timeout", var1, var3);
         return this;
      }

      public OkHttpClient.Builder connectionPool(ConnectionPool var1) {
         if (var1 == null) {
            throw new NullPointerException("connectionPool == null");
         } else {
            this.connectionPool = var1;
            return this;
         }
      }

      public OkHttpClient.Builder connectionSpecs(List var1) {
         this.connectionSpecs = Util.immutableList(var1);
         return this;
      }

      public OkHttpClient.Builder cookieJar(CookieJar var1) {
         if (var1 == null) {
            throw new NullPointerException("cookieJar == null");
         } else {
            this.cookieJar = var1;
            return this;
         }
      }

      public OkHttpClient.Builder dispatcher(Dispatcher var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("dispatcher == null");
         } else {
            this.dispatcher = var1;
            return this;
         }
      }

      public OkHttpClient.Builder dns(Dns var1) {
         if (var1 == null) {
            throw new NullPointerException("dns == null");
         } else {
            this.dns = var1;
            return this;
         }
      }

      public OkHttpClient.Builder followRedirects(boolean var1) {
         this.followRedirects = var1;
         return this;
      }

      public OkHttpClient.Builder followSslRedirects(boolean var1) {
         this.followSslRedirects = var1;
         return this;
      }

      public OkHttpClient.Builder hostnameVerifier(HostnameVerifier var1) {
         if (var1 == null) {
            throw new NullPointerException("hostnameVerifier == null");
         } else {
            this.hostnameVerifier = var1;
            return this;
         }
      }

      public List interceptors() {
         return this.interceptors;
      }

      public List networkInterceptors() {
         return this.networkInterceptors;
      }

      public OkHttpClient.Builder pingInterval(long var1, TimeUnit var3) {
         this.pingInterval = checkDuration("interval", var1, var3);
         return this;
      }

      public OkHttpClient.Builder protocols(List var1) {
         ArrayList var2 = new ArrayList(var1);
         if (!var2.contains(Protocol.HTTP_1_1)) {
            throw new IllegalArgumentException("protocols doesn't contain http/1.1: " + var2);
         } else if (var2.contains(Protocol.HTTP_1_0)) {
            throw new IllegalArgumentException("protocols must not contain http/1.0: " + var2);
         } else if (var2.contains((Object)null)) {
            throw new IllegalArgumentException("protocols must not contain null");
         } else {
            if (var2.contains(Protocol.SPDY_3)) {
               var2.remove(Protocol.SPDY_3);
            }

            this.protocols = Collections.unmodifiableList(var2);
            return this;
         }
      }

      public OkHttpClient.Builder proxy(Proxy var1) {
         this.proxy = var1;
         return this;
      }

      public OkHttpClient.Builder proxyAuthenticator(Authenticator var1) {
         if (var1 == null) {
            throw new NullPointerException("proxyAuthenticator == null");
         } else {
            this.proxyAuthenticator = var1;
            return this;
         }
      }

      public OkHttpClient.Builder proxySelector(ProxySelector var1) {
         this.proxySelector = var1;
         return this;
      }

      public OkHttpClient.Builder readTimeout(long var1, TimeUnit var3) {
         this.readTimeout = checkDuration("timeout", var1, var3);
         return this;
      }

      public OkHttpClient.Builder retryOnConnectionFailure(boolean var1) {
         this.retryOnConnectionFailure = var1;
         return this;
      }

      void setInternalCache(InternalCache var1) {
         this.internalCache = var1;
         this.cache = null;
      }

      public OkHttpClient.Builder socketFactory(SocketFactory var1) {
         if (var1 == null) {
            throw new NullPointerException("socketFactory == null");
         } else {
            this.socketFactory = var1;
            return this;
         }
      }

      public OkHttpClient.Builder sslSocketFactory(SSLSocketFactory var1) {
         if (var1 == null) {
            throw new NullPointerException("sslSocketFactory == null");
         } else {
            X509TrustManager var2 = Platform.get().trustManager(var1);
            if (var2 == null) {
               throw new IllegalStateException("Unable to extract the trust manager on " + Platform.get() + ", sslSocketFactory is " + var1.getClass());
            } else {
               this.sslSocketFactory = var1;
               this.certificateChainCleaner = CertificateChainCleaner.get(var2);
               return this;
            }
         }
      }

      public OkHttpClient.Builder sslSocketFactory(SSLSocketFactory var1, X509TrustManager var2) {
         if (var1 == null) {
            throw new NullPointerException("sslSocketFactory == null");
         } else if (var2 == null) {
            throw new NullPointerException("trustManager == null");
         } else {
            this.sslSocketFactory = var1;
            this.certificateChainCleaner = CertificateChainCleaner.get(var2);
            return this;
         }
      }

      public OkHttpClient.Builder writeTimeout(long var1, TimeUnit var3) {
         this.writeTimeout = checkDuration("timeout", var1, var3);
         return this;
      }
   }
}

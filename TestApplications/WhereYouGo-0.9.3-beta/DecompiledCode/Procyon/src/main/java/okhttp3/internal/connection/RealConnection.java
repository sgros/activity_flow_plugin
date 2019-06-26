// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.connection;

import javax.net.ssl.SSLSocketFactory;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.Http2Stream;
import okhttp3.internal.ws.RealWebSocket;
import java.net.SocketException;
import okhttp3.internal.http2.Http2Codec;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.Version;
import okio.Source;
import okhttp3.Response;
import okhttp3.internal.http.HttpHeaders;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.internal.http1.Http1Codec;
import okhttp3.HttpUrl;
import okhttp3.Request;
import java.net.ProtocolException;
import okhttp3.ConnectionSpec;
import okhttp3.internal.Util;
import okhttp3.internal.tls.OkHostnameVerifier;
import java.security.cert.Certificate;
import okhttp3.CertificatePinner;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import okhttp3.Address;
import java.net.ConnectException;
import okio.Okio;
import okhttp3.internal.platform.Platform;
import java.net.Proxy;
import java.util.ArrayList;
import okio.BufferedSource;
import okio.BufferedSink;
import okhttp3.Route;
import java.net.Socket;
import okhttp3.Protocol;
import okhttp3.Handshake;
import okhttp3.ConnectionPool;
import java.lang.ref.Reference;
import java.util.List;
import okhttp3.Connection;
import okhttp3.internal.http2.Http2Connection;

public final class RealConnection extends Listener implements Connection
{
    public int allocationLimit;
    public final List<Reference<StreamAllocation>> allocations;
    private final ConnectionPool connectionPool;
    private Handshake handshake;
    private Http2Connection http2Connection;
    public long idleAtNanos;
    public boolean noNewStreams;
    private Protocol protocol;
    private Socket rawSocket;
    private final Route route;
    private BufferedSink sink;
    private Socket socket;
    private BufferedSource source;
    public int successCount;
    
    public RealConnection(final ConnectionPool connectionPool, final Route route) {
        this.allocationLimit = 1;
        this.allocations = new ArrayList<Reference<StreamAllocation>>();
        this.idleAtNanos = Long.MAX_VALUE;
        this.connectionPool = connectionPool;
        this.route = route;
    }
    
    private void connectSocket(final int n, final int soTimeout) throws IOException {
        final Proxy proxy = this.route.proxy();
        final Address address = this.route.address();
        Label_0108: {
            if (proxy.type() != Proxy.Type.DIRECT && proxy.type() != Proxy.Type.HTTP) {
                break Label_0108;
            }
            Socket socket = address.socketFactory().createSocket();
            while (true) {
                (this.rawSocket = socket).setSoTimeout(soTimeout);
                try {
                    Platform.get().connectSocket(this.rawSocket, this.route.socketAddress(), n);
                    this.source = Okio.buffer(Okio.source(this.rawSocket));
                    this.sink = Okio.buffer(Okio.sink(this.rawSocket));
                    return;
                    socket = new Socket(proxy);
                }
                catch (ConnectException cause) {
                    final ConnectException ex = new ConnectException("Failed to connect to " + this.route.socketAddress());
                    ex.initCause(cause);
                    throw ex;
                }
            }
        }
    }
    
    private void connectTls(final ConnectionSpecSelector connectionSpecSelector) throws IOException {
        final Address address = this.route.address();
        Object sslSocketFactory = address.sslSocketFactory();
        SSLSocket sslSocket = null;
        SSLSocket sslSocket2 = null;
        Object value;
        try {
            sslSocketFactory = (sslSocket = (sslSocket2 = (SSLSocket)((SSLSocketFactory)sslSocketFactory).createSocket(this.rawSocket, address.url().host(), address.url().port(), true)));
            final ConnectionSpec configureSecureSocket = connectionSpecSelector.configureSecureSocket((SSLSocket)sslSocketFactory);
            sslSocket2 = (SSLSocket)sslSocketFactory;
            sslSocket = (SSLSocket)sslSocketFactory;
            if (configureSecureSocket.supportsTlsExtensions()) {
                sslSocket2 = (SSLSocket)sslSocketFactory;
                sslSocket = (SSLSocket)sslSocketFactory;
                Platform.get().configureTlsExtensions((SSLSocket)sslSocketFactory, address.url().host(), address.protocols());
            }
            sslSocket2 = (SSLSocket)sslSocketFactory;
            sslSocket = (SSLSocket)sslSocketFactory;
            ((SSLSocket)sslSocketFactory).startHandshake();
            sslSocket2 = (SSLSocket)sslSocketFactory;
            sslSocket = (SSLSocket)sslSocketFactory;
            value = Handshake.get(((SSLSocket)sslSocketFactory).getSession());
            sslSocket2 = (SSLSocket)sslSocketFactory;
            sslSocket = (SSLSocket)sslSocketFactory;
            if (!address.hostnameVerifier().verify(address.url().host(), ((SSLSocket)sslSocketFactory).getSession())) {
                sslSocket2 = (SSLSocket)sslSocketFactory;
                sslSocket = (SSLSocket)sslSocketFactory;
                final X509Certificate x509Certificate = ((Handshake)value).peerCertificates().get(0);
                sslSocket2 = (SSLSocket)sslSocketFactory;
                sslSocket = (SSLSocket)sslSocketFactory;
                value = new(javax.net.ssl.SSLPeerUnverifiedException.class);
                sslSocket2 = (SSLSocket)sslSocketFactory;
                sslSocket = (SSLSocket)sslSocketFactory;
                sslSocket2 = (SSLSocket)sslSocketFactory;
                sslSocket = (SSLSocket)sslSocketFactory;
                final StringBuilder sb = new StringBuilder();
                sslSocket2 = (SSLSocket)sslSocketFactory;
                sslSocket = (SSLSocket)sslSocketFactory;
                new SSLPeerUnverifiedException(sb.append("Hostname ").append(address.url().host()).append(" not verified:\n    certificate: ").append(CertificatePinner.pin(x509Certificate)).append("\n    DN: ").append(x509Certificate.getSubjectDN().getName()).append("\n    subjectAltNames: ").append(OkHostnameVerifier.allSubjectAltNames(x509Certificate)).toString());
                sslSocket2 = (SSLSocket)sslSocketFactory;
                sslSocket = (SSLSocket)sslSocketFactory;
                throw value;
            }
        }
        catch (AssertionError cause) {
            sslSocket = sslSocket2;
            if (Util.isAndroidGetsocknameError(cause)) {
                sslSocket = sslSocket2;
                sslSocket = sslSocket2;
                sslSocketFactory = new IOException(cause);
                sslSocket = sslSocket2;
                throw sslSocketFactory;
            }
            return;
        }
        finally {
            if (sslSocket != null) {
                Platform.get().afterHandshake(sslSocket);
            }
            if (!false) {
                Util.closeQuietly(sslSocket);
            }
        }
        address.certificatePinner().check(address.url().host(), ((Handshake)value).peerCertificates());
        final ConnectionSpec connectionSpec;
        String selectedProtocol;
        if (connectionSpec.supportsTlsExtensions()) {
            selectedProtocol = Platform.get().getSelectedProtocol((SSLSocket)sslSocketFactory);
        }
        else {
            selectedProtocol = null;
        }
        this.socket = (Socket)sslSocketFactory;
        this.source = Okio.buffer(Okio.source(this.socket));
        this.sink = Okio.buffer(Okio.sink(this.socket));
        this.handshake = (Handshake)value;
        Protocol protocol;
        if (selectedProtocol != null) {
            protocol = Protocol.get(selectedProtocol);
        }
        else {
            protocol = Protocol.HTTP_1_1;
        }
        this.protocol = protocol;
        if (sslSocketFactory != null) {
            Platform.get().afterHandshake((SSLSocket)sslSocketFactory);
        }
        if (!true) {
            Util.closeQuietly((Socket)sslSocketFactory);
        }
    }
    
    private void connectTunnel(final int n, final int n2, final int n3) throws IOException {
        Request request = this.createTunnelRequest();
        final HttpUrl url = request.url();
        int n4 = 0;
        while (++n4 <= 21) {
            this.connectSocket(n, n2);
            request = this.createTunnel(n2, n3, request, url);
            if (request == null) {
                return;
            }
            Util.closeQuietly(this.rawSocket);
            this.rawSocket = null;
            this.sink = null;
            this.source = null;
        }
        throw new ProtocolException("Too many tunnel connections attempted: " + 21);
    }
    
    private Request createTunnel(final int n, final int n2, Request request, final HttpUrl httpUrl) throws IOException {
        final Request request2 = null;
        final String string = "CONNECT " + Util.hostHeader(httpUrl, true) + " HTTP/1.1";
        Response build;
        Request authenticate = null;
    Label_0336:
        do {
            final Http1Codec http1Codec = new Http1Codec(null, null, this.source, this.sink);
            this.source.timeout().timeout(n, TimeUnit.MILLISECONDS);
            this.sink.timeout().timeout(n2, TimeUnit.MILLISECONDS);
            http1Codec.writeRequest(request.headers(), string);
            http1Codec.finishRequest();
            build = http1Codec.readResponseHeaders(false).request(request).build();
            long contentLength;
            if ((contentLength = HttpHeaders.contentLength(build)) == -1L) {
                contentLength = 0L;
            }
            final Source fixedLengthSource = http1Codec.newFixedLengthSource(contentLength);
            Util.skipAll(fixedLengthSource, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
            fixedLengthSource.close();
            switch (build.code()) {
                default: {
                    throw new IOException("Unexpected response code for CONNECT: " + build.code());
                }
                case 200: {
                    if (this.source.buffer().exhausted()) {
                        authenticate = request2;
                        if (this.sink.buffer().exhausted()) {
                            break Label_0336;
                        }
                    }
                    throw new IOException("TLS tunnel buffered too many bytes!");
                }
                case 407: {
                    authenticate = this.route.address().proxyAuthenticator().authenticate(this.route, build);
                    if (authenticate == null) {
                        throw new IOException("Failed to authenticate with proxy");
                    }
                    request = authenticate;
                    continue;
                }
            }
        } while (!"close".equalsIgnoreCase(build.header("Connection")));
        return authenticate;
    }
    
    private Request createTunnelRequest() {
        return new Request.Builder().url(this.route.address().url()).header("Host", Util.hostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
    }
    
    private void establishProtocol(final ConnectionSpecSelector connectionSpecSelector) throws IOException {
        if (this.route.address().sslSocketFactory() == null) {
            this.protocol = Protocol.HTTP_1_1;
            this.socket = this.rawSocket;
        }
        else {
            this.connectTls(connectionSpecSelector);
            if (this.protocol == Protocol.HTTP_2) {
                this.socket.setSoTimeout(0);
                (this.http2Connection = new Http2Connection.Builder(true).socket(this.socket, this.route.address().url().host(), this.source, this.sink).listener(this).build()).start();
            }
        }
    }
    
    public static RealConnection testConnection(final ConnectionPool connectionPool, final Route route, final Socket socket, final long idleAtNanos) {
        final RealConnection realConnection = new RealConnection(connectionPool, route);
        realConnection.socket = socket;
        realConnection.idleAtNanos = idleAtNanos;
        return realConnection;
    }
    
    public void cancel() {
        Util.closeQuietly(this.rawSocket);
    }
    
    public void connect(final int p0, final int p1, final int p2, final boolean p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        okhttp3/internal/connection/RealConnection.protocol:Lokhttp3/Protocol;
        //     4: ifnull          18
        //     7: new             Ljava/lang/IllegalStateException;
        //    10: dup            
        //    11: ldc_w           "already connected"
        //    14: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //    17: athrow         
        //    18: aconst_null    
        //    19: astore          5
        //    21: aload_0        
        //    22: getfield        okhttp3/internal/connection/RealConnection.route:Lokhttp3/Route;
        //    25: invokevirtual   okhttp3/Route.address:()Lokhttp3/Address;
        //    28: invokevirtual   okhttp3/Address.connectionSpecs:()Ljava/util/List;
        //    31: astore          6
        //    33: new             Lokhttp3/internal/connection/ConnectionSpecSelector;
        //    36: dup            
        //    37: aload           6
        //    39: invokespecial   okhttp3/internal/connection/ConnectionSpecSelector.<init>:(Ljava/util/List;)V
        //    42: astore          7
        //    44: aload           5
        //    46: astore          8
        //    48: aload_0        
        //    49: getfield        okhttp3/internal/connection/RealConnection.route:Lokhttp3/Route;
        //    52: invokevirtual   okhttp3/Route.address:()Lokhttp3/Address;
        //    55: invokevirtual   okhttp3/Address.sslSocketFactory:()Ljavax/net/ssl/SSLSocketFactory;
        //    58: ifnonnull       164
        //    61: aload           6
        //    63: getstatic       okhttp3/ConnectionSpec.CLEARTEXT:Lokhttp3/ConnectionSpec;
        //    66: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //    71: ifne            92
        //    74: new             Lokhttp3/internal/connection/RouteException;
        //    77: dup            
        //    78: new             Ljava/net/UnknownServiceException;
        //    81: dup            
        //    82: ldc_w           "CLEARTEXT communication not enabled for client"
        //    85: invokespecial   java/net/UnknownServiceException.<init>:(Ljava/lang/String;)V
        //    88: invokespecial   okhttp3/internal/connection/RouteException.<init>:(Ljava/io/IOException;)V
        //    91: athrow         
        //    92: aload_0        
        //    93: getfield        okhttp3/internal/connection/RealConnection.route:Lokhttp3/Route;
        //    96: invokevirtual   okhttp3/Route.address:()Lokhttp3/Address;
        //    99: invokevirtual   okhttp3/Address.url:()Lokhttp3/HttpUrl;
        //   102: invokevirtual   okhttp3/HttpUrl.host:()Ljava/lang/String;
        //   105: astore          6
        //   107: aload           5
        //   109: astore          8
        //   111: invokestatic    okhttp3/internal/platform/Platform.get:()Lokhttp3/internal/platform/Platform;
        //   114: aload           6
        //   116: invokevirtual   okhttp3/internal/platform/Platform.isCleartextTrafficPermitted:(Ljava/lang/String;)Z
        //   119: ifne            164
        //   122: new             Lokhttp3/internal/connection/RouteException;
        //   125: dup            
        //   126: new             Ljava/net/UnknownServiceException;
        //   129: dup            
        //   130: new             Ljava/lang/StringBuilder;
        //   133: dup            
        //   134: invokespecial   java/lang/StringBuilder.<init>:()V
        //   137: ldc_w           "CLEARTEXT communication to "
        //   140: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   143: aload           6
        //   145: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   148: ldc_w           " not permitted by network security policy"
        //   151: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   154: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   157: invokespecial   java/net/UnknownServiceException.<init>:(Ljava/lang/String;)V
        //   160: invokespecial   okhttp3/internal/connection/RouteException.<init>:(Ljava/io/IOException;)V
        //   163: athrow         
        //   164: aload_0        
        //   165: getfield        okhttp3/internal/connection/RealConnection.route:Lokhttp3/Route;
        //   168: invokevirtual   okhttp3/Route.requiresTunnel:()Z
        //   171: ifeq            218
        //   174: aload_0        
        //   175: iload_1        
        //   176: iload_2        
        //   177: iload_3        
        //   178: invokespecial   okhttp3/internal/connection/RealConnection.connectTunnel:(III)V
        //   181: aload_0        
        //   182: aload           7
        //   184: invokespecial   okhttp3/internal/connection/RealConnection.establishProtocol:(Lokhttp3/internal/connection/ConnectionSpecSelector;)V
        //   187: aload_0        
        //   188: getfield        okhttp3/internal/connection/RealConnection.http2Connection:Lokhttp3/internal/http2/Http2Connection;
        //   191: ifnull          217
        //   194: aload_0        
        //   195: getfield        okhttp3/internal/connection/RealConnection.connectionPool:Lokhttp3/ConnectionPool;
        //   198: astore          8
        //   200: aload           8
        //   202: monitorenter   
        //   203: aload_0        
        //   204: aload_0        
        //   205: getfield        okhttp3/internal/connection/RealConnection.http2Connection:Lokhttp3/internal/http2/Http2Connection;
        //   208: invokevirtual   okhttp3/internal/http2/Http2Connection.maxConcurrentStreams:()I
        //   211: putfield        okhttp3/internal/connection/RealConnection.allocationLimit:I
        //   214: aload           8
        //   216: monitorexit    
        //   217: return         
        //   218: aload_0        
        //   219: iload_1        
        //   220: iload_2        
        //   221: invokespecial   okhttp3/internal/connection/RealConnection.connectSocket:(II)V
        //   224: goto            181
        //   227: astore          6
        //   229: aload_0        
        //   230: getfield        okhttp3/internal/connection/RealConnection.socket:Ljava/net/Socket;
        //   233: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/net/Socket;)V
        //   236: aload_0        
        //   237: getfield        okhttp3/internal/connection/RealConnection.rawSocket:Ljava/net/Socket;
        //   240: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/net/Socket;)V
        //   243: aload_0        
        //   244: aconst_null    
        //   245: putfield        okhttp3/internal/connection/RealConnection.socket:Ljava/net/Socket;
        //   248: aload_0        
        //   249: aconst_null    
        //   250: putfield        okhttp3/internal/connection/RealConnection.rawSocket:Ljava/net/Socket;
        //   253: aload_0        
        //   254: aconst_null    
        //   255: putfield        okhttp3/internal/connection/RealConnection.source:Lokio/BufferedSource;
        //   258: aload_0        
        //   259: aconst_null    
        //   260: putfield        okhttp3/internal/connection/RealConnection.sink:Lokio/BufferedSink;
        //   263: aload_0        
        //   264: aconst_null    
        //   265: putfield        okhttp3/internal/connection/RealConnection.handshake:Lokhttp3/Handshake;
        //   268: aload_0        
        //   269: aconst_null    
        //   270: putfield        okhttp3/internal/connection/RealConnection.protocol:Lokhttp3/Protocol;
        //   273: aload_0        
        //   274: aconst_null    
        //   275: putfield        okhttp3/internal/connection/RealConnection.http2Connection:Lokhttp3/internal/http2/Http2Connection;
        //   278: aload           8
        //   280: ifnonnull       316
        //   283: new             Lokhttp3/internal/connection/RouteException;
        //   286: dup            
        //   287: aload           6
        //   289: invokespecial   okhttp3/internal/connection/RouteException.<init>:(Ljava/io/IOException;)V
        //   292: astore          5
        //   294: iload           4
        //   296: ifeq            313
        //   299: aload           5
        //   301: astore          8
        //   303: aload           7
        //   305: aload           6
        //   307: invokevirtual   okhttp3/internal/connection/ConnectionSpecSelector.connectionFailed:(Ljava/io/IOException;)Z
        //   310: ifne            164
        //   313: aload           5
        //   315: athrow         
        //   316: aload           8
        //   318: aload           6
        //   320: invokevirtual   okhttp3/internal/connection/RouteException.addConnectException:(Ljava/io/IOException;)V
        //   323: aload           8
        //   325: astore          5
        //   327: goto            294
        //   330: astore          5
        //   332: aload           8
        //   334: monitorexit    
        //   335: aload           5
        //   337: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  164    181    227    330    Ljava/io/IOException;
        //  181    187    227    330    Ljava/io/IOException;
        //  203    217    330    338    Any
        //  218    224    227    330    Ljava/io/IOException;
        //  332    335    330    338    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0217:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public Handshake handshake() {
        return this.handshake;
    }
    
    public boolean isEligible(final Address address) {
        return this.allocations.size() < this.allocationLimit && address.equals(this.route().address()) && !this.noNewStreams;
    }
    
    public boolean isHealthy(final boolean p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_2       
        //     2: aload_0        
        //     3: getfield        okhttp3/internal/connection/RealConnection.socket:Ljava/net/Socket;
        //     6: invokevirtual   java/net/Socket.isClosed:()Z
        //     9: ifne            32
        //    12: aload_0        
        //    13: getfield        okhttp3/internal/connection/RealConnection.socket:Ljava/net/Socket;
        //    16: invokevirtual   java/net/Socket.isInputShutdown:()Z
        //    19: ifne            32
        //    22: aload_0        
        //    23: getfield        okhttp3/internal/connection/RealConnection.socket:Ljava/net/Socket;
        //    26: invokevirtual   java/net/Socket.isOutputShutdown:()Z
        //    29: ifeq            36
        //    32: iconst_0       
        //    33: istore_3       
        //    34: iload_3        
        //    35: ireturn        
        //    36: aload_0        
        //    37: getfield        okhttp3/internal/connection/RealConnection.http2Connection:Lokhttp3/internal/http2/Http2Connection;
        //    40: ifnull          60
        //    43: iload_2        
        //    44: istore_3       
        //    45: aload_0        
        //    46: getfield        okhttp3/internal/connection/RealConnection.http2Connection:Lokhttp3/internal/http2/Http2Connection;
        //    49: invokevirtual   okhttp3/internal/http2/Http2Connection.isShutdown:()Z
        //    52: ifeq            34
        //    55: iconst_0       
        //    56: istore_3       
        //    57: goto            34
        //    60: iload_2        
        //    61: istore_3       
        //    62: iload_1        
        //    63: ifeq            34
        //    66: aload_0        
        //    67: getfield        okhttp3/internal/connection/RealConnection.socket:Ljava/net/Socket;
        //    70: invokevirtual   java/net/Socket.getSoTimeout:()I
        //    73: istore          4
        //    75: aload_0        
        //    76: getfield        okhttp3/internal/connection/RealConnection.socket:Ljava/net/Socket;
        //    79: iconst_1       
        //    80: invokevirtual   java/net/Socket.setSoTimeout:(I)V
        //    83: aload_0        
        //    84: getfield        okhttp3/internal/connection/RealConnection.source:Lokio/BufferedSource;
        //    87: invokeinterface okio/BufferedSource.exhausted:()Z
        //    92: istore_1       
        //    93: iload_1        
        //    94: ifeq            111
        //    97: aload_0        
        //    98: getfield        okhttp3/internal/connection/RealConnection.socket:Ljava/net/Socket;
        //   101: iload           4
        //   103: invokevirtual   java/net/Socket.setSoTimeout:(I)V
        //   106: iconst_0       
        //   107: istore_3       
        //   108: goto            34
        //   111: aload_0        
        //   112: getfield        okhttp3/internal/connection/RealConnection.socket:Ljava/net/Socket;
        //   115: iload           4
        //   117: invokevirtual   java/net/Socket.setSoTimeout:(I)V
        //   120: iload_2        
        //   121: istore_3       
        //   122: goto            34
        //   125: astore          5
        //   127: iload_2        
        //   128: istore_3       
        //   129: goto            34
        //   132: astore          5
        //   134: aload_0        
        //   135: getfield        okhttp3/internal/connection/RealConnection.socket:Ljava/net/Socket;
        //   138: iload           4
        //   140: invokevirtual   java/net/Socket.setSoTimeout:(I)V
        //   143: aload           5
        //   145: athrow         
        //   146: astore          5
        //   148: iconst_0       
        //   149: istore_3       
        //   150: goto            34
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  66     75     125    132    Ljava/net/SocketTimeoutException;
        //  66     75     146    153    Ljava/io/IOException;
        //  75     93     132    146    Any
        //  97     106    125    132    Ljava/net/SocketTimeoutException;
        //  97     106    146    153    Ljava/io/IOException;
        //  111    120    125    132    Ljava/net/SocketTimeoutException;
        //  111    120    146    153    Ljava/io/IOException;
        //  134    146    125    132    Ljava/net/SocketTimeoutException;
        //  134    146    146    153    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0111:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public boolean isMultiplexed() {
        return this.http2Connection != null;
    }
    
    public HttpCodec newCodec(final OkHttpClient okHttpClient, final StreamAllocation streamAllocation) throws SocketException {
        HttpCodec httpCodec;
        if (this.http2Connection != null) {
            httpCodec = new Http2Codec(okHttpClient, streamAllocation, this.http2Connection);
        }
        else {
            this.socket.setSoTimeout(okHttpClient.readTimeoutMillis());
            this.source.timeout().timeout(okHttpClient.readTimeoutMillis(), TimeUnit.MILLISECONDS);
            this.sink.timeout().timeout(okHttpClient.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
            httpCodec = new Http1Codec(okHttpClient, streamAllocation, this.source, this.sink);
        }
        return httpCodec;
    }
    
    public RealWebSocket.Streams newWebSocketStreams(final StreamAllocation streamAllocation) {
        return new RealWebSocket.Streams(true, this.source, this.sink) {
            @Override
            public void close() throws IOException {
                streamAllocation.streamFinished(true, streamAllocation.codec());
            }
        };
    }
    
    @Override
    public void onSettings(final Http2Connection http2Connection) {
        synchronized (this.connectionPool) {
            this.allocationLimit = http2Connection.maxConcurrentStreams();
        }
    }
    
    @Override
    public void onStream(final Http2Stream http2Stream) throws IOException {
        http2Stream.close(ErrorCode.REFUSED_STREAM);
    }
    
    @Override
    public Protocol protocol() {
        return this.protocol;
    }
    
    @Override
    public Route route() {
        return this.route;
    }
    
    @Override
    public Socket socket() {
        return this.socket;
    }
    
    @Override
    public String toString() {
        final StringBuilder append = new StringBuilder().append("Connection{").append(this.route.address().url().host()).append(":").append(this.route.address().url().port()).append(", proxy=").append(this.route.proxy()).append(" hostAddress=").append(this.route.socketAddress()).append(" cipherSuite=");
        Object cipherSuite;
        if (this.handshake != null) {
            cipherSuite = this.handshake.cipherSuite();
        }
        else {
            cipherSuite = "none";
        }
        return append.append(cipherSuite).append(" protocol=").append(this.protocol).append('}').toString();
    }
}

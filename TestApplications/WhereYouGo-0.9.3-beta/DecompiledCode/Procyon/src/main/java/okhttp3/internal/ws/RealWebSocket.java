// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.ws;

import okio.BufferedSource;
import okio.BufferedSink;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.io.Closeable;
import okhttp3.internal.Util;
import okhttp3.Callback;
import okhttp3.internal.Internal;
import okhttp3.OkHttpClient;
import java.io.Serializable;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import okhttp3.Response;
import java.util.Collections;
import java.util.Random;
import okio.ByteString;
import okhttp3.Request;
import java.util.ArrayDeque;
import okhttp3.WebSocketListener;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import okhttp3.Call;
import okhttp3.Protocol;
import java.util.List;
import okhttp3.WebSocket;

public final class RealWebSocket implements WebSocket, FrameCallback
{
    private static final long CANCEL_AFTER_CLOSE_MILLIS = 60000L;
    private static final long MAX_QUEUE_SIZE = 16777216L;
    private static final List<Protocol> ONLY_HTTP1;
    private Call call;
    private ScheduledFuture<?> cancelFuture;
    private boolean enqueuedClose;
    private ScheduledExecutorService executor;
    private boolean failed;
    private final String key;
    final WebSocketListener listener;
    private final ArrayDeque<Object> messageAndCloseQueue;
    private final Request originalRequest;
    int pingCount;
    int pongCount;
    private final ArrayDeque<ByteString> pongQueue;
    private long queueSize;
    private final Random random;
    private WebSocketReader reader;
    private int receivedCloseCode;
    private String receivedCloseReason;
    private Streams streams;
    private WebSocketWriter writer;
    private final Runnable writerRunnable;
    
    static {
        ONLY_HTTP1 = Collections.singletonList(Protocol.HTTP_1_1);
    }
    
    public RealWebSocket(final Request originalRequest, final WebSocketListener listener, final Random random) {
        this.pongQueue = new ArrayDeque<ByteString>();
        this.messageAndCloseQueue = new ArrayDeque<Object>();
        this.receivedCloseCode = -1;
        if (!"GET".equals(originalRequest.method())) {
            throw new IllegalArgumentException("Request must be GET: " + originalRequest.method());
        }
        this.originalRequest = originalRequest;
        this.listener = listener;
        this.random = random;
        final byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        this.key = ByteString.of(bytes).base64();
        this.writerRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (RealWebSocket.this.writeOneFrame()) {}
                }
                catch (IOException ex) {
                    RealWebSocket.this.failWebSocket(ex, null);
                }
            }
        };
    }
    
    private void runWriter() {
        assert Thread.holdsLock(this);
        if (this.executor != null) {
            this.executor.execute(this.writerRunnable);
        }
    }
    
    private boolean send(final ByteString byteString, final int n) {
        while (true) {
            final boolean b = false;
            // monitorenter(this)
            boolean b2 = b;
            Label_0070: {
                try {
                    if (!this.failed) {
                        if (this.enqueuedClose) {
                            b2 = b;
                        }
                        else {
                            if (this.queueSize + byteString.size() <= 16777216L) {
                                break Label_0070;
                            }
                            this.close(1001, null);
                            b2 = b;
                        }
                    }
                    return b2;
                }
                finally {
                }
                // monitorexit(this)
            }
            final ByteString byteString2;
            this.queueSize += byteString2.size();
            this.messageAndCloseQueue.add(new Message(n, byteString2));
            this.runWriter();
            b2 = true;
            return b2;
        }
    }
    
    void awaitTermination(final int n, final TimeUnit timeUnit) throws InterruptedException {
        this.executor.awaitTermination(n, timeUnit);
    }
    
    @Override
    public void cancel() {
        this.call.cancel();
    }
    
    void checkResponse(final Response response) throws ProtocolException {
        if (response.code() != 101) {
            throw new ProtocolException("Expected HTTP 101 response but was '" + response.code() + " " + response.message() + "'");
        }
        final String header = response.header("Connection");
        if (!"Upgrade".equalsIgnoreCase(header)) {
            throw new ProtocolException("Expected 'Connection' header value 'Upgrade' but was '" + header + "'");
        }
        final String header2 = response.header("Upgrade");
        if (!"websocket".equalsIgnoreCase(header2)) {
            throw new ProtocolException("Expected 'Upgrade' header value 'websocket' but was '" + header2 + "'");
        }
        final String header3 = response.header("Sec-WebSocket-Accept");
        final String base64 = ByteString.encodeUtf8(this.key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").sha1().base64();
        if (!base64.equals(header3)) {
            throw new ProtocolException("Expected 'Sec-WebSocket-Accept' header value '" + base64 + "' but was '" + header3 + "'");
        }
    }
    
    @Override
    public boolean close(final int n, final String s) {
        return this.close(n, s, 60000L);
    }
    
    boolean close(final int n, final String str, final long n2) {
        boolean b = true;
        Serializable encodeUtf8;
        synchronized (this) {
            WebSocketProtocol.validateCloseCode(n);
            encodeUtf8 = null;
            if (str != null && ((ByteString)(encodeUtf8 = ByteString.encodeUtf8(str))).size() > 123L) {
                encodeUtf8 = new IllegalArgumentException("reason.size() > 123: " + str);
                throw encodeUtf8;
            }
        }
        if (this.failed || this.enqueuedClose) {
            b = false;
        }
        else {
            this.enqueuedClose = true;
            this.messageAndCloseQueue.add(new Close(n, (ByteString)encodeUtf8, n2));
            this.runWriter();
        }
        // monitorexit(this)
        return b;
    }
    
    public void connect(OkHttpClient build) {
        build = build.newBuilder().protocols(RealWebSocket.ONLY_HTTP1).build();
        final int pingIntervalMillis = build.pingIntervalMillis();
        final Request build2 = this.originalRequest.newBuilder().header("Upgrade", "websocket").header("Connection", "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build();
        (this.call = Internal.instance.newWebSocketCall(build, build2)).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException ex) {
                RealWebSocket.this.failWebSocket(ex, null);
            }
            
            @Override
            public void onResponse(final Call p0, final Response p1) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: getfield        okhttp3/internal/ws/RealWebSocket$2.this$0:Lokhttp3/internal/ws/RealWebSocket;
                //     4: aload_2        
                //     5: invokevirtual   okhttp3/internal/ws/RealWebSocket.checkResponse:(Lokhttp3/Response;)V
                //     8: getstatic       okhttp3/internal/Internal.instance:Lokhttp3/internal/Internal;
                //    11: aload_1        
                //    12: invokevirtual   okhttp3/internal/Internal.streamAllocation:(Lokhttp3/Call;)Lokhttp3/internal/connection/StreamAllocation;
                //    15: astore_1       
                //    16: aload_1        
                //    17: invokevirtual   okhttp3/internal/connection/StreamAllocation.noNewStreams:()V
                //    20: aload_1        
                //    21: invokevirtual   okhttp3/internal/connection/StreamAllocation.connection:()Lokhttp3/internal/connection/RealConnection;
                //    24: aload_1        
                //    25: invokevirtual   okhttp3/internal/connection/RealConnection.newWebSocketStreams:(Lokhttp3/internal/connection/StreamAllocation;)Lokhttp3/internal/ws/RealWebSocket$Streams;
                //    28: astore_3       
                //    29: aload_0        
                //    30: getfield        okhttp3/internal/ws/RealWebSocket$2.this$0:Lokhttp3/internal/ws/RealWebSocket;
                //    33: getfield        okhttp3/internal/ws/RealWebSocket.listener:Lokhttp3/WebSocketListener;
                //    36: aload_0        
                //    37: getfield        okhttp3/internal/ws/RealWebSocket$2.this$0:Lokhttp3/internal/ws/RealWebSocket;
                //    40: aload_2        
                //    41: invokevirtual   okhttp3/WebSocketListener.onOpen:(Lokhttp3/WebSocket;Lokhttp3/Response;)V
                //    44: new             Ljava/lang/StringBuilder;
                //    47: astore_2       
                //    48: aload_2        
                //    49: invokespecial   java/lang/StringBuilder.<init>:()V
                //    52: aload_2        
                //    53: ldc             "OkHttp WebSocket "
                //    55: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                //    58: aload_0        
                //    59: getfield        okhttp3/internal/ws/RealWebSocket$2.val$request:Lokhttp3/Request;
                //    62: invokevirtual   okhttp3/Request.url:()Lokhttp3/HttpUrl;
                //    65: invokevirtual   okhttp3/HttpUrl.redact:()Ljava/lang/String;
                //    68: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                //    71: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
                //    74: astore_2       
                //    75: aload_0        
                //    76: getfield        okhttp3/internal/ws/RealWebSocket$2.this$0:Lokhttp3/internal/ws/RealWebSocket;
                //    79: aload_2        
                //    80: aload_0        
                //    81: getfield        okhttp3/internal/ws/RealWebSocket$2.val$pingIntervalMillis:I
                //    84: i2l            
                //    85: aload_3        
                //    86: invokevirtual   okhttp3/internal/ws/RealWebSocket.initReaderAndWriter:(Ljava/lang/String;JLokhttp3/internal/ws/RealWebSocket$Streams;)V
                //    89: aload_1        
                //    90: invokevirtual   okhttp3/internal/connection/StreamAllocation.connection:()Lokhttp3/internal/connection/RealConnection;
                //    93: invokevirtual   okhttp3/internal/connection/RealConnection.socket:()Ljava/net/Socket;
                //    96: iconst_0       
                //    97: invokevirtual   java/net/Socket.setSoTimeout:(I)V
                //   100: aload_0        
                //   101: getfield        okhttp3/internal/ws/RealWebSocket$2.this$0:Lokhttp3/internal/ws/RealWebSocket;
                //   104: invokevirtual   okhttp3/internal/ws/RealWebSocket.loopReader:()V
                //   107: return         
                //   108: astore_1       
                //   109: aload_0        
                //   110: getfield        okhttp3/internal/ws/RealWebSocket$2.this$0:Lokhttp3/internal/ws/RealWebSocket;
                //   113: aload_1        
                //   114: aload_2        
                //   115: invokevirtual   okhttp3/internal/ws/RealWebSocket.failWebSocket:(Ljava/lang/Exception;Lokhttp3/Response;)V
                //   118: aload_2        
                //   119: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
                //   122: goto            107
                //   125: astore_1       
                //   126: aload_0        
                //   127: getfield        okhttp3/internal/ws/RealWebSocket$2.this$0:Lokhttp3/internal/ws/RealWebSocket;
                //   130: aload_1        
                //   131: aconst_null    
                //   132: invokevirtual   okhttp3/internal/ws/RealWebSocket.failWebSocket:(Ljava/lang/Exception;Lokhttp3/Response;)V
                //   135: goto            107
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                        
                //  -----  -----  -----  -----  ----------------------------
                //  0      8      108    125    Ljava/net/ProtocolException;
                //  29     107    125    138    Ljava/lang/Exception;
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0107:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public void failWebSocket(final Exception ex, final Response response) {
        Streams streams;
        synchronized (this) {
            if (!this.failed) {
                this.failed = true;
                streams = this.streams;
                this.streams = null;
                if (this.cancelFuture != null) {
                    this.cancelFuture.cancel(false);
                }
                if (this.executor != null) {
                    this.executor.shutdown();
                }
                // monitorexit(this)
                final RealWebSocket realWebSocket = this;
                final WebSocketListener webSocketListener = realWebSocket.listener;
                final RealWebSocket realWebSocket2 = this;
                final Exception ex2 = ex;
                final Response response2 = response;
                webSocketListener.onFailure(realWebSocket2, ex2, response2);
            }
            return;
        }
        try {
            final RealWebSocket realWebSocket = this;
            final WebSocketListener webSocketListener = realWebSocket.listener;
            final RealWebSocket realWebSocket2 = this;
            final Exception ex2 = ex;
            final Response response2 = response;
            webSocketListener.onFailure(realWebSocket2, ex2, response2);
        }
        finally {
            Util.closeQuietly(streams);
        }
    }
    
    public void initReaderAndWriter(final String s, final long n, final Streams streams) throws IOException {
        synchronized (this) {
            this.streams = streams;
            this.writer = new WebSocketWriter(streams.client, streams.sink, this.random);
            this.executor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(s, false));
            if (n != 0L) {
                this.executor.scheduleAtFixedRate(new PingRunnable(), n, n, TimeUnit.MILLISECONDS);
            }
            if (!this.messageAndCloseQueue.isEmpty()) {
                this.runWriter();
            }
            // monitorexit(this)
            this.reader = new WebSocketReader(streams.client, streams.source, (WebSocketReader.FrameCallback)this);
        }
    }
    
    public void loopReader() throws IOException {
        while (this.receivedCloseCode == -1) {
            this.reader.processNextFrame();
        }
    }
    
    @Override
    public void onReadClose(final int receivedCloseCode, final String s) {
        if (receivedCloseCode == -1) {
            throw new IllegalArgumentException();
        }
        final Closeable closeable = null;
        synchronized (this) {
            if (this.receivedCloseCode != -1) {
                throw new IllegalStateException("already closed");
            }
        }
        this.receivedCloseCode = receivedCloseCode;
        final String receivedCloseReason;
        this.receivedCloseReason = receivedCloseReason;
        Closeable streams = closeable;
        if (this.enqueuedClose) {
            streams = closeable;
            if (this.messageAndCloseQueue.isEmpty()) {
                streams = this.streams;
                this.streams = null;
                if (this.cancelFuture != null) {
                    this.cancelFuture.cancel(false);
                }
                this.executor.shutdown();
            }
        }
        // monitorexit(this)
        try {
            this.listener.onClosing(this, receivedCloseCode, receivedCloseReason);
            if (streams != null) {
                this.listener.onClosed(this, receivedCloseCode, receivedCloseReason);
            }
        }
        finally {
            Util.closeQuietly(streams);
        }
    }
    
    @Override
    public void onReadMessage(final String s) throws IOException {
        this.listener.onMessage(this, s);
    }
    
    @Override
    public void onReadMessage(final ByteString byteString) throws IOException {
        this.listener.onMessage(this, byteString);
    }
    
    @Override
    public void onReadPing(final ByteString e) {
        synchronized (this) {
            if (!this.failed && (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty())) {
                this.pongQueue.add(e);
                this.runWriter();
                ++this.pingCount;
            }
        }
    }
    
    @Override
    public void onReadPong(final ByteString byteString) {
        synchronized (this) {
            ++this.pongCount;
        }
    }
    
    int pingCount() {
        synchronized (this) {
            return this.pingCount;
        }
    }
    
    boolean pong(final ByteString e) {
        synchronized (this) {
            boolean b;
            if (this.failed || (this.enqueuedClose && this.messageAndCloseQueue.isEmpty())) {
                b = false;
            }
            else {
                this.pongQueue.add(e);
                this.runWriter();
                b = true;
            }
            return b;
        }
    }
    
    int pongCount() {
        synchronized (this) {
            return this.pongCount;
        }
    }
    
    boolean processNextFrame() throws IOException {
        boolean b = false;
        try {
            this.reader.processNextFrame();
            if (this.receivedCloseCode == -1) {
                b = true;
            }
            return b;
        }
        catch (Exception ex) {
            this.failWebSocket(ex, null);
            return b;
        }
    }
    
    @Override
    public long queueSize() {
        synchronized (this) {
            return this.queueSize;
        }
    }
    
    @Override
    public Request request() {
        return this.originalRequest;
    }
    
    @Override
    public boolean send(final String s) {
        if (s == null) {
            throw new NullPointerException("text == null");
        }
        return this.send(ByteString.encodeUtf8(s), 1);
    }
    
    @Override
    public boolean send(final ByteString byteString) {
        if (byteString == null) {
            throw new NullPointerException("bytes == null");
        }
        return this.send(byteString, 2);
    }
    
    void tearDown() throws InterruptedException {
        if (this.cancelFuture != null) {
            this.cancelFuture.cancel(false);
        }
        this.executor.shutdown();
        this.executor.awaitTermination(10L, TimeUnit.SECONDS);
    }
    
    boolean writeOneFrame() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: aconst_null    
        //     3: astore_2       
        //     4: iconst_m1      
        //     5: istore_3       
        //     6: aconst_null    
        //     7: astore          4
        //     9: aconst_null    
        //    10: astore          5
        //    12: aload_0        
        //    13: monitorenter   
        //    14: aload_0        
        //    15: getfield        okhttp3/internal/ws/RealWebSocket.failed:Z
        //    18: ifeq            25
        //    21: aload_0        
        //    22: monitorexit    
        //    23: iload_1        
        //    24: ireturn        
        //    25: aload_0        
        //    26: getfield        okhttp3/internal/ws/RealWebSocket.writer:Lokhttp3/internal/ws/WebSocketWriter;
        //    29: astore          6
        //    31: aload_0        
        //    32: getfield        okhttp3/internal/ws/RealWebSocket.pongQueue:Ljava/util/ArrayDeque;
        //    35: invokevirtual   java/util/ArrayDeque.poll:()Ljava/lang/Object;
        //    38: checkcast       Lokio/ByteString;
        //    41: astore          7
        //    43: iload_3        
        //    44: istore          8
        //    46: aload           4
        //    48: astore          9
        //    50: aload           5
        //    52: astore          10
        //    54: aload           7
        //    56: ifnonnull       117
        //    59: aload_0        
        //    60: getfield        okhttp3/internal/ws/RealWebSocket.messageAndCloseQueue:Ljava/util/ArrayDeque;
        //    63: invokevirtual   java/util/ArrayDeque.poll:()Ljava/lang/Object;
        //    66: astore          11
        //    68: aload           11
        //    70: instanceof      Lokhttp3/internal/ws/RealWebSocket$Close;
        //    73: ifeq            197
        //    76: aload_0        
        //    77: getfield        okhttp3/internal/ws/RealWebSocket.receivedCloseCode:I
        //    80: istore          8
        //    82: aload_0        
        //    83: getfield        okhttp3/internal/ws/RealWebSocket.receivedCloseReason:Ljava/lang/String;
        //    86: astore          9
        //    88: iload           8
        //    90: iconst_m1      
        //    91: if_icmpeq       141
        //    94: aload_0        
        //    95: getfield        okhttp3/internal/ws/RealWebSocket.streams:Lokhttp3/internal/ws/RealWebSocket$Streams;
        //    98: astore          10
        //   100: aload_0        
        //   101: aconst_null    
        //   102: putfield        okhttp3/internal/ws/RealWebSocket.streams:Lokhttp3/internal/ws/RealWebSocket$Streams;
        //   105: aload_0        
        //   106: getfield        okhttp3/internal/ws/RealWebSocket.executor:Ljava/util/concurrent/ScheduledExecutorService;
        //   109: invokeinterface java/util/concurrent/ScheduledExecutorService.shutdown:()V
        //   114: aload           11
        //   116: astore_2       
        //   117: aload_0        
        //   118: monitorexit    
        //   119: aload           7
        //   121: ifnull          221
        //   124: aload           6
        //   126: aload           7
        //   128: invokevirtual   okhttp3/internal/ws/WebSocketWriter.writePong:(Lokio/ByteString;)V
        //   131: iconst_1       
        //   132: istore_1       
        //   133: aload           10
        //   135: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
        //   138: goto            23
        //   141: aload_0        
        //   142: getfield        okhttp3/internal/ws/RealWebSocket.executor:Ljava/util/concurrent/ScheduledExecutorService;
        //   145: astore_2       
        //   146: new             Lokhttp3/internal/ws/RealWebSocket$CancelRunnable;
        //   149: astore          10
        //   151: aload           10
        //   153: aload_0        
        //   154: invokespecial   okhttp3/internal/ws/RealWebSocket$CancelRunnable.<init>:(Lokhttp3/internal/ws/RealWebSocket;)V
        //   157: aload_0        
        //   158: aload_2        
        //   159: aload           10
        //   161: aload           11
        //   163: checkcast       Lokhttp3/internal/ws/RealWebSocket$Close;
        //   166: getfield        okhttp3/internal/ws/RealWebSocket$Close.cancelAfterCloseMillis:J
        //   169: getstatic       java/util/concurrent/TimeUnit.MILLISECONDS:Ljava/util/concurrent/TimeUnit;
        //   172: invokeinterface java/util/concurrent/ScheduledExecutorService.schedule:(Ljava/lang/Runnable;JLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
        //   177: putfield        okhttp3/internal/ws/RealWebSocket.cancelFuture:Ljava/util/concurrent/ScheduledFuture;
        //   180: aload           11
        //   182: astore_2       
        //   183: aload           5
        //   185: astore          10
        //   187: goto            117
        //   190: astore          10
        //   192: aload_0        
        //   193: monitorexit    
        //   194: aload           10
        //   196: athrow         
        //   197: aload           11
        //   199: astore_2       
        //   200: iload_3        
        //   201: istore          8
        //   203: aload           4
        //   205: astore          9
        //   207: aload           5
        //   209: astore          10
        //   211: aload           11
        //   213: ifnonnull       117
        //   216: aload_0        
        //   217: monitorexit    
        //   218: goto            23
        //   221: aload_2        
        //   222: instanceof      Lokhttp3/internal/ws/RealWebSocket$Message;
        //   225: ifeq            309
        //   228: aload_2        
        //   229: checkcast       Lokhttp3/internal/ws/RealWebSocket$Message;
        //   232: getfield        okhttp3/internal/ws/RealWebSocket$Message.data:Lokio/ByteString;
        //   235: astore          9
        //   237: aload           6
        //   239: aload_2        
        //   240: checkcast       Lokhttp3/internal/ws/RealWebSocket$Message;
        //   243: getfield        okhttp3/internal/ws/RealWebSocket$Message.formatOpcode:I
        //   246: aload           9
        //   248: invokevirtual   okio/ByteString.size:()I
        //   251: i2l            
        //   252: invokevirtual   okhttp3/internal/ws/WebSocketWriter.newMessageSink:(IJ)Lokio/Sink;
        //   255: invokestatic    okio/Okio.buffer:(Lokio/Sink;)Lokio/BufferedSink;
        //   258: astore_2       
        //   259: aload_2        
        //   260: aload           9
        //   262: invokeinterface okio/BufferedSink.write:(Lokio/ByteString;)Lokio/BufferedSink;
        //   267: pop            
        //   268: aload_2        
        //   269: invokeinterface okio/BufferedSink.close:()V
        //   274: aload_0        
        //   275: monitorenter   
        //   276: aload_0        
        //   277: aload_0        
        //   278: getfield        okhttp3/internal/ws/RealWebSocket.queueSize:J
        //   281: aload           9
        //   283: invokevirtual   okio/ByteString.size:()I
        //   286: i2l            
        //   287: lsub           
        //   288: putfield        okhttp3/internal/ws/RealWebSocket.queueSize:J
        //   291: aload_0        
        //   292: monitorexit    
        //   293: goto            131
        //   296: astore_2       
        //   297: aload_0        
        //   298: monitorexit    
        //   299: aload_2        
        //   300: athrow         
        //   301: astore_2       
        //   302: aload           10
        //   304: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
        //   307: aload_2        
        //   308: athrow         
        //   309: aload_2        
        //   310: instanceof      Lokhttp3/internal/ws/RealWebSocket$Close;
        //   313: ifeq            354
        //   316: aload_2        
        //   317: checkcast       Lokhttp3/internal/ws/RealWebSocket$Close;
        //   320: astore_2       
        //   321: aload           6
        //   323: aload_2        
        //   324: getfield        okhttp3/internal/ws/RealWebSocket$Close.code:I
        //   327: aload_2        
        //   328: getfield        okhttp3/internal/ws/RealWebSocket$Close.reason:Lokio/ByteString;
        //   331: invokevirtual   okhttp3/internal/ws/WebSocketWriter.writeClose:(ILokio/ByteString;)V
        //   334: aload           10
        //   336: ifnull          131
        //   339: aload_0        
        //   340: getfield        okhttp3/internal/ws/RealWebSocket.listener:Lokhttp3/WebSocketListener;
        //   343: aload_0        
        //   344: iload           8
        //   346: aload           9
        //   348: invokevirtual   okhttp3/WebSocketListener.onClosed:(Lokhttp3/WebSocket;ILjava/lang/String;)V
        //   351: goto            131
        //   354: new             Ljava/lang/AssertionError;
        //   357: astore_2       
        //   358: aload_2        
        //   359: invokespecial   java/lang/AssertionError.<init>:()V
        //   362: aload_2        
        //   363: athrow         
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  14     23     190    197    Any
        //  25     43     190    197    Any
        //  59     88     190    197    Any
        //  94     114    190    197    Any
        //  117    119    190    197    Any
        //  124    131    301    309    Any
        //  141    180    190    197    Any
        //  192    194    190    197    Any
        //  216    218    190    197    Any
        //  221    276    301    309    Any
        //  276    293    296    301    Any
        //  297    299    296    301    Any
        //  299    301    301    309    Any
        //  309    334    301    309    Any
        //  339    351    301    309    Any
        //  354    364    301    309    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0131:
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
    
    void writePingFrame() {
        synchronized (this) {
            if (!this.failed) {
                final WebSocketWriter writer = this.writer;
                // monitorexit(this)
                try {
                    writer.writePing(ByteString.EMPTY);
                }
                catch (IOException ex) {
                    this.failWebSocket(ex, null);
                }
            }
        }
    }
    
    final class CancelRunnable implements Runnable
    {
        @Override
        public void run() {
            RealWebSocket.this.cancel();
        }
    }
    
    static final class Close
    {
        final long cancelAfterCloseMillis;
        final int code;
        final ByteString reason;
        
        Close(final int code, final ByteString reason, final long cancelAfterCloseMillis) {
            this.code = code;
            this.reason = reason;
            this.cancelAfterCloseMillis = cancelAfterCloseMillis;
        }
    }
    
    static final class Message
    {
        final ByteString data;
        final int formatOpcode;
        
        Message(final int formatOpcode, final ByteString data) {
            this.formatOpcode = formatOpcode;
            this.data = data;
        }
    }
    
    private final class PingRunnable implements Runnable
    {
        PingRunnable() {
        }
        
        @Override
        public void run() {
            RealWebSocket.this.writePingFrame();
        }
    }
    
    public abstract static class Streams implements Closeable
    {
        public final boolean client;
        public final BufferedSink sink;
        public final BufferedSource source;
        
        public Streams(final boolean client, final BufferedSource source, final BufferedSink sink) {
            this.client = client;
            this.source = source;
            this.sink = sink;
        }
    }
}

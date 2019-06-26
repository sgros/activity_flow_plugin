// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.http2;

import okhttp3.internal.platform.Platform;
import okio.ByteString;
import okio.Okio;
import java.net.InetSocketAddress;
import okio.BufferedSink;
import java.io.InterruptedIOException;
import okhttp3.internal.NamedRunnable;
import okio.Buffer;
import okio.BufferedSource;
import okhttp3.Protocol;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import okhttp3.internal.Util;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.io.Closeable;

public final class Http2Connection implements Closeable
{
    private static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
    static final ExecutorService executor;
    long bytesLeftInWriteWindow;
    final boolean client;
    final Set<Integer> currentPushRequests;
    final String hostname;
    int lastGoodStreamId;
    final Listener listener;
    private int nextPingId;
    int nextStreamId;
    Settings okHttpSettings;
    final Settings peerSettings;
    private Map<Integer, Ping> pings;
    private final ExecutorService pushExecutor;
    final PushObserver pushObserver;
    final ReaderRunnable readerRunnable;
    boolean receivedInitialPeerSettings;
    boolean shutdown;
    final Socket socket;
    final Map<Integer, Http2Stream> streams;
    long unacknowledgedBytesRead;
    final Http2Writer writer;
    
    static {
        executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Http2Connection", true));
    }
    
    Http2Connection(final Builder builder) {
        final int n = 2;
        this.streams = new LinkedHashMap<Integer, Http2Stream>();
        this.unacknowledgedBytesRead = 0L;
        this.okHttpSettings = new Settings();
        this.peerSettings = new Settings();
        this.receivedInitialPeerSettings = false;
        this.currentPushRequests = new LinkedHashSet<Integer>();
        this.pushObserver = builder.pushObserver;
        this.client = builder.client;
        this.listener = builder.listener;
        int nextStreamId;
        if (builder.client) {
            nextStreamId = 1;
        }
        else {
            nextStreamId = 2;
        }
        this.nextStreamId = nextStreamId;
        if (builder.client) {
            this.nextStreamId += 2;
        }
        int nextPingId = n;
        if (builder.client) {
            nextPingId = 1;
        }
        this.nextPingId = nextPingId;
        if (builder.client) {
            this.okHttpSettings.set(7, 16777216);
        }
        this.hostname = builder.hostname;
        this.pushExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Util.threadFactory(Util.format("OkHttp %s Push Observer", this.hostname), true));
        this.peerSettings.set(7, 65535);
        this.peerSettings.set(5, 16384);
        this.bytesLeftInWriteWindow = this.peerSettings.getInitialWindowSize();
        this.socket = builder.socket;
        this.writer = new Http2Writer(builder.sink, this.client);
        this.readerRunnable = new ReaderRunnable(new Http2Reader(builder.source, this.client));
    }
    
    private Http2Stream newStream(final int n, final List<Header> list, final boolean b) throws IOException {
        // monitorexit(this)
        // monitorexit(http2Writer)
        while (true) {
            Label_0046: {
                if (b) {
                    break Label_0046;
                }
                final boolean b2 = true;
                Label_0052: {
                    synchronized (this.writer) {
                        synchronized (this) {
                            if (this.shutdown) {
                                throw new ConnectionShutdownException();
                            }
                            break Label_0052;
                        }
                    }
                    break Label_0046;
                }
                final int nextStreamId = this.nextStreamId;
                this.nextStreamId += 2;
                final List<Header> list2;
                final Http2Stream http2Stream = new Http2Stream(nextStreamId, this, b2, false, list2);
                int n2;
                if (!b || this.bytesLeftInWriteWindow == 0L || http2Stream.bytesLeftInWriteWindow == 0L) {
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
                if (http2Stream.isOpen()) {
                    this.streams.put(nextStreamId, http2Stream);
                }
                if (n == 0) {
                    this.writer.synStream(b2, nextStreamId, n, list2);
                }
                else {
                    if (this.client) {
                        throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
                    }
                    this.writer.pushPromise(n, nextStreamId, list2);
                }
                if (n2 != 0) {
                    this.writer.flush();
                }
                return http2Stream;
            }
            final boolean b2 = false;
            continue;
        }
    }
    
    void addBytesToWriteWindow(final long n) {
        this.bytesLeftInWriteWindow += n;
        if (n > 0L) {
            this.notifyAll();
        }
    }
    
    @Override
    public void close() throws IOException {
        this.close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }
    
    void close(final ErrorCode p0, final ErrorCode p1) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     6: aload_0        
        //     7: invokestatic    java/lang/Thread.holdsLock:(Ljava/lang/Object;)Z
        //    10: ifeq            21
        //    13: new             Ljava/lang/AssertionError;
        //    16: dup            
        //    17: invokespecial   java/lang/AssertionError.<init>:()V
        //    20: athrow         
        //    21: aconst_null    
        //    22: astore_3       
        //    23: aload_0        
        //    24: aload_1        
        //    25: invokevirtual   okhttp3/internal/http2/Http2Connection.shutdown:(Lokhttp3/internal/http2/ErrorCode;)V
        //    28: aload_3        
        //    29: astore_1       
        //    30: aconst_null    
        //    31: astore          4
        //    33: aconst_null    
        //    34: astore          5
        //    36: aload_0        
        //    37: monitorenter   
        //    38: aload_0        
        //    39: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
        //    42: invokeinterface java/util/Map.isEmpty:()Z
        //    47: ifne            90
        //    50: aload_0        
        //    51: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
        //    54: invokeinterface java/util/Map.values:()Ljava/util/Collection;
        //    59: aload_0        
        //    60: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
        //    63: invokeinterface java/util/Map.size:()I
        //    68: anewarray       Lokhttp3/internal/http2/Http2Stream;
        //    71: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    76: checkcast       [Lokhttp3/internal/http2/Http2Stream;
        //    79: astore          4
        //    81: aload_0        
        //    82: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
        //    85: invokeinterface java/util/Map.clear:()V
        //    90: aload_0        
        //    91: getfield        okhttp3/internal/http2/Http2Connection.pings:Ljava/util/Map;
        //    94: ifnull          133
        //    97: aload_0        
        //    98: getfield        okhttp3/internal/http2/Http2Connection.pings:Ljava/util/Map;
        //   101: invokeinterface java/util/Map.values:()Ljava/util/Collection;
        //   106: aload_0        
        //   107: getfield        okhttp3/internal/http2/Http2Connection.pings:Ljava/util/Map;
        //   110: invokeinterface java/util/Map.size:()I
        //   115: anewarray       Lokhttp3/internal/http2/Ping;
        //   118: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   123: checkcast       [Lokhttp3/internal/http2/Ping;
        //   126: astore          5
        //   128: aload_0        
        //   129: aconst_null    
        //   130: putfield        okhttp3/internal/http2/Http2Connection.pings:Ljava/util/Map;
        //   133: aload_0        
        //   134: monitorexit    
        //   135: aload_1        
        //   136: astore_3       
        //   137: aload           4
        //   139: ifnull          203
        //   142: aload           4
        //   144: arraylength    
        //   145: istore          6
        //   147: iconst_0       
        //   148: istore          7
        //   150: aload_1        
        //   151: astore_3       
        //   152: iload           7
        //   154: iload           6
        //   156: if_icmpge       203
        //   159: aload           4
        //   161: iload           7
        //   163: aaload         
        //   164: astore_3       
        //   165: aload_3        
        //   166: aload_2        
        //   167: invokevirtual   okhttp3/internal/http2/Http2Stream.close:(Lokhttp3/internal/http2/ErrorCode;)V
        //   170: aload_1        
        //   171: astore_3       
        //   172: iinc            7, 1
        //   175: aload_3        
        //   176: astore_1       
        //   177: goto            150
        //   180: astore_1       
        //   181: goto            30
        //   184: astore_1       
        //   185: aload_0        
        //   186: monitorexit    
        //   187: aload_1        
        //   188: athrow         
        //   189: astore          8
        //   191: aload_1        
        //   192: astore_3       
        //   193: aload_1        
        //   194: ifnull          172
        //   197: aload           8
        //   199: astore_3       
        //   200: goto            172
        //   203: aload           5
        //   205: ifnull          237
        //   208: aload           5
        //   210: arraylength    
        //   211: istore          6
        //   213: iconst_0       
        //   214: istore          7
        //   216: iload           7
        //   218: iload           6
        //   220: if_icmpge       237
        //   223: aload           5
        //   225: iload           7
        //   227: aaload         
        //   228: invokevirtual   okhttp3/internal/http2/Ping.cancel:()V
        //   231: iinc            7, 1
        //   234: goto            216
        //   237: aload_0        
        //   238: getfield        okhttp3/internal/http2/Http2Connection.writer:Lokhttp3/internal/http2/Http2Writer;
        //   241: invokevirtual   okhttp3/internal/http2/Http2Writer.close:()V
        //   244: aload_3        
        //   245: astore_1       
        //   246: aload_0        
        //   247: getfield        okhttp3/internal/http2/Http2Connection.socket:Ljava/net/Socket;
        //   250: invokevirtual   java/net/Socket.close:()V
        //   253: aload_1        
        //   254: ifnull          275
        //   257: aload_1        
        //   258: athrow         
        //   259: astore_2       
        //   260: aload_3        
        //   261: astore_1       
        //   262: aload_3        
        //   263: ifnonnull       246
        //   266: aload_2        
        //   267: astore_1       
        //   268: goto            246
        //   271: astore_1       
        //   272: goto            253
        //   275: return         
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  23     28     180    184    Ljava/io/IOException;
        //  38     90     184    189    Any
        //  90     133    184    189    Any
        //  133    135    184    189    Any
        //  165    170    189    203    Ljava/io/IOException;
        //  185    187    184    189    Any
        //  237    244    259    271    Ljava/io/IOException;
        //  246    253    271    275    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 138 out-of-bounds for length 138
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    public void flush() throws IOException {
        this.writer.flush();
    }
    
    public Protocol getProtocol() {
        return Protocol.HTTP_2;
    }
    
    Http2Stream getStream(final int i) {
        synchronized (this) {
            return this.streams.get(i);
        }
    }
    
    public boolean isShutdown() {
        synchronized (this) {
            return this.shutdown;
        }
    }
    
    public int maxConcurrentStreams() {
        synchronized (this) {
            return this.peerSettings.getMaxConcurrentStreams(Integer.MAX_VALUE);
        }
    }
    
    public Http2Stream newStream(final List<Header> list, final boolean b) throws IOException {
        return this.newStream(0, list, b);
    }
    
    public int openStreamCount() {
        synchronized (this) {
            return this.streams.size();
        }
    }
    
    public Ping ping() throws IOException {
        final Ping ping = new Ping();
        synchronized (this) {
            if (this.shutdown) {
                throw new ConnectionShutdownException();
            }
        }
        final int nextPingId = this.nextPingId;
        this.nextPingId += 2;
        if (this.pings == null) {
            this.pings = new LinkedHashMap<Integer, Ping>();
        }
        final Ping ping2;
        this.pings.put(nextPingId, ping2);
        // monitorexit(this)
        this.writePing(false, nextPingId, 1330343787, ping2);
        return ping2;
    }
    
    void pushDataLater(final int i, final BufferedSource bufferedSource, final int j, final boolean b) throws IOException {
        final Buffer buffer = new Buffer();
        bufferedSource.require(j);
        bufferedSource.read(buffer, j);
        if (buffer.size() != j) {
            throw new IOException(buffer.size() + " != " + j);
        }
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Data[%s]", new Object[] { this.hostname, i }) {
            public void execute() {
                try {
                    final boolean onData = Http2Connection.this.pushObserver.onData(i, buffer, j, b);
                    if (onData) {
                        Http2Connection.this.writer.rstStream(i, ErrorCode.CANCEL);
                    }
                    if (!onData && !b) {
                        return;
                    }
                    synchronized (Http2Connection.this) {
                        Http2Connection.this.currentPushRequests.remove(i);
                    }
                }
                catch (IOException o) {}
            }
        });
    }
    
    void pushHeadersLater(final int i, final List<Header> list, final boolean b) {
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Headers[%s]", new Object[] { this.hostname, i }) {
            public void execute() {
                final boolean onHeaders = Http2Connection.this.pushObserver.onHeaders(i, list, b);
                Label_0046: {
                    if (!onHeaders) {
                        break Label_0046;
                    }
                    try {
                        Http2Connection.this.writer.rstStream(i, ErrorCode.CANCEL);
                        if (!onHeaders && !b) {
                            return;
                        }
                        synchronized (Http2Connection.this) {
                            Http2Connection.this.currentPushRequests.remove(i);
                        }
                    }
                    catch (IOException o) {}
                }
            }
        });
    }
    
    void pushRequestLater(final int i, final List<Header> list) {
        synchronized (this) {
            if (this.currentPushRequests.contains(i)) {
                this.writeSynResetLater(i, ErrorCode.PROTOCOL_ERROR);
            }
            else {
                this.currentPushRequests.add(i);
                // monitorexit(this)
                this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Request[%s]", new Object[] { this.hostname, i }) {
                    public void execute() {
                        if (!Http2Connection.this.pushObserver.onRequest(i, list)) {
                            return;
                        }
                        try {
                            Http2Connection.this.writer.rstStream(i, ErrorCode.CANCEL);
                            synchronized (Http2Connection.this) {
                                Http2Connection.this.currentPushRequests.remove(i);
                            }
                        }
                        catch (IOException o) {}
                    }
                });
            }
        }
    }
    
    void pushResetLater(final int i, final ErrorCode errorCode) {
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Reset[%s]", new Object[] { this.hostname, i }) {
            public void execute() {
                Http2Connection.this.pushObserver.onReset(i, errorCode);
                synchronized (Http2Connection.this) {
                    Http2Connection.this.currentPushRequests.remove(i);
                }
            }
        });
    }
    
    public Http2Stream pushStream(final int n, final List<Header> list, final boolean b) throws IOException {
        if (this.client) {
            throw new IllegalStateException("Client cannot push requests.");
        }
        return this.newStream(n, list, b);
    }
    
    boolean pushedStream(final int n) {
        return n != 0 && (n & 0x1) == 0x0;
    }
    
    Ping removePing(final int i) {
        synchronized (this) {
            Ping ping;
            if (this.pings != null) {
                ping = this.pings.remove(i);
            }
            else {
                ping = null;
            }
            return ping;
        }
    }
    
    Http2Stream removeStream(final int i) {
        synchronized (this) {
            final Http2Stream http2Stream = this.streams.remove(i);
            this.notifyAll();
            return http2Stream;
        }
    }
    
    public void setSettings(final Settings settings) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
            }
        }
        final Settings settings2;
        this.okHttpSettings.merge(settings2);
        this.writer.settings(settings2);
    }
    // monitorexit(this)
    // monitorexit(http2Writer)
    
    public void shutdown(final ErrorCode p0) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        okhttp3/internal/http2/Http2Connection.writer:Lokhttp3/internal/http2/Http2Writer;
        //     4: astore_2       
        //     5: aload_2        
        //     6: monitorenter   
        //     7: aload_0        
        //     8: monitorenter   
        //     9: aload_0        
        //    10: getfield        okhttp3/internal/http2/Http2Connection.shutdown:Z
        //    13: ifeq            21
        //    16: aload_0        
        //    17: monitorexit    
        //    18: aload_2        
        //    19: monitorexit    
        //    20: return         
        //    21: aload_0        
        //    22: iconst_1       
        //    23: putfield        okhttp3/internal/http2/Http2Connection.shutdown:Z
        //    26: aload_0        
        //    27: getfield        okhttp3/internal/http2/Http2Connection.lastGoodStreamId:I
        //    30: istore_3       
        //    31: aload_0        
        //    32: monitorexit    
        //    33: aload_0        
        //    34: getfield        okhttp3/internal/http2/Http2Connection.writer:Lokhttp3/internal/http2/Http2Writer;
        //    37: iload_3        
        //    38: aload_1        
        //    39: getstatic       okhttp3/internal/Util.EMPTY_BYTE_ARRAY:[B
        //    42: invokevirtual   okhttp3/internal/http2/Http2Writer.goAway:(ILokhttp3/internal/http2/ErrorCode;[B)V
        //    45: aload_2        
        //    46: monitorexit    
        //    47: goto            20
        //    50: astore_1       
        //    51: aload_2        
        //    52: monitorexit    
        //    53: aload_1        
        //    54: athrow         
        //    55: astore_1       
        //    56: aload_0        
        //    57: monitorexit    
        //    58: aload_1        
        //    59: athrow         
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  7      9      50     55     Any
        //  9      18     55     60     Any
        //  18     20     50     55     Any
        //  21     33     55     60     Any
        //  33     47     50     55     Any
        //  51     53     50     55     Any
        //  56     58     55     60     Any
        //  58     60     50     55     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0020:
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
    
    public void start() throws IOException {
        this.start(true);
    }
    
    void start(final boolean b) throws IOException {
        if (b) {
            this.writer.connectionPreface();
            this.writer.settings(this.okHttpSettings);
            final int initialWindowSize = this.okHttpSettings.getInitialWindowSize();
            if (initialWindowSize != 65535) {
                this.writer.windowUpdate(0, initialWindowSize - 65535);
            }
        }
        new Thread(this.readerRunnable).start();
    }
    
    public void writeData(final int i, final boolean b, final Buffer buffer, final long n) throws IOException {
        long a = n;
        Label_0022: {
            if (n == 0L) {
                this.writer.data(b, i, buffer, 0);
            }
            else {
                Label_0098: {
                    break Label_0098;
                Label_0087_Outer:
                    while (true) {
                        while (true) {
                            Label_0168: {
                                while (true) {
                                    try {
                                        while (true) {
                                            final int min = Math.min((int)Math.min(a, this.bytesLeftInWriteWindow), this.writer.maxDataLength());
                                            this.bytesLeftInWriteWindow -= min;
                                            // monitorexit(this)
                                            a -= min;
                                            final Http2Writer writer = this.writer;
                                            if (!b || a != 0L) {
                                                break Label_0168;
                                            }
                                            final boolean b2 = true;
                                            writer.data(b2, i, buffer, min);
                                            if (a <= 0L) {
                                                break Label_0022;
                                            }
                                            // monitorenter(this)
                                            try {
                                                if (this.bytesLeftInWriteWindow > 0L) {
                                                    continue Label_0087_Outer;
                                                }
                                                if (!this.streams.containsKey(i)) {
                                                    throw new IOException("stream closed");
                                                }
                                                break;
                                            }
                                            catch (InterruptedException ex) {
                                                throw new InterruptedIOException();
                                            }
                                        }
                                    }
                                    finally {
                                    }
                                    // monitorexit(this)
                                    this.wait();
                                    continue;
                                }
                            }
                            final boolean b2 = false;
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    void writePing(final boolean b, final int n, final int n2, final Ping ping) throws IOException {
        final Http2Writer writer = this.writer;
        // monitorenter(writer)
        Label_0019: {
            if (ping == null) {
                break Label_0019;
            }
            try {
                ping.send();
                this.writer.ping(b, n, n2);
            }
            finally {
            }
            // monitorexit(writer)
        }
    }
    
    void writePingLater(final boolean b, final int i, final int j, final Ping ping) {
        Http2Connection.executor.execute(new NamedRunnable("OkHttp %s ping %08x%08x", new Object[] { this.hostname, i, j }) {
            public void execute() {
                try {
                    Http2Connection.this.writePing(b, i, j, ping);
                }
                catch (IOException ex) {}
            }
        });
    }
    
    void writeSynReply(final int n, final boolean b, final List<Header> list) throws IOException {
        this.writer.synReply(b, n, list);
    }
    
    void writeSynReset(final int n, final ErrorCode errorCode) throws IOException {
        this.writer.rstStream(n, errorCode);
    }
    
    void writeSynResetLater(final int i, final ErrorCode errorCode) {
        Http2Connection.executor.execute(new NamedRunnable("OkHttp %s stream %d", new Object[] { this.hostname, i }) {
            public void execute() {
                try {
                    Http2Connection.this.writeSynReset(i, errorCode);
                }
                catch (IOException ex) {}
            }
        });
    }
    
    void writeWindowUpdateLater(final int i, final long n) {
        Http2Connection.executor.execute(new NamedRunnable("OkHttp Window Update %s stream %d", new Object[] { this.hostname, i }) {
            public void execute() {
                try {
                    Http2Connection.this.writer.windowUpdate(i, n);
                }
                catch (IOException ex) {}
            }
        });
    }
    
    public static class Builder
    {
        boolean client;
        String hostname;
        Listener listener;
        PushObserver pushObserver;
        BufferedSink sink;
        Socket socket;
        BufferedSource source;
        
        public Builder(final boolean client) {
            this.listener = Listener.REFUSE_INCOMING_STREAMS;
            this.pushObserver = PushObserver.CANCEL;
            this.client = client;
        }
        
        public Http2Connection build() throws IOException {
            return new Http2Connection(this);
        }
        
        public Builder listener(final Listener listener) {
            this.listener = listener;
            return this;
        }
        
        public Builder pushObserver(final PushObserver pushObserver) {
            this.pushObserver = pushObserver;
            return this;
        }
        
        public Builder socket(final Socket socket) throws IOException {
            return this.socket(socket, ((InetSocketAddress)socket.getRemoteSocketAddress()).getHostName(), Okio.buffer(Okio.source(socket)), Okio.buffer(Okio.sink(socket)));
        }
        
        public Builder socket(final Socket socket, final String hostname, final BufferedSource source, final BufferedSink sink) {
            this.socket = socket;
            this.hostname = hostname;
            this.source = source;
            this.sink = sink;
            return this;
        }
    }
    
    public abstract static class Listener
    {
        public static final Listener REFUSE_INCOMING_STREAMS;
        
        static {
            REFUSE_INCOMING_STREAMS = new Listener() {
                @Override
                public void onStream(final Http2Stream http2Stream) throws IOException {
                    http2Stream.close(ErrorCode.REFUSED_STREAM);
                }
            };
        }
        
        public void onSettings(final Http2Connection http2Connection) {
        }
        
        public abstract void onStream(final Http2Stream p0) throws IOException;
    }
    
    class ReaderRunnable extends NamedRunnable implements Handler
    {
        final Http2Reader reader;
        
        ReaderRunnable(final Http2Reader reader) {
            super("OkHttp %s", new Object[] { Http2Connection.this.hostname });
            this.reader = reader;
        }
        
        private void applyAndAckSettings(final Settings settings) {
            Http2Connection.executor.execute(new NamedRunnable("OkHttp %s ACK Settings", new Object[] { Http2Connection.this.hostname }) {
                public void execute() {
                    try {
                        Http2Connection.this.writer.applyAndAckSettings(settings);
                    }
                    catch (IOException ex) {}
                }
            });
        }
        
        @Override
        public void ackSettings() {
        }
        
        @Override
        public void alternateService(final int n, final String s, final ByteString byteString, final String s2, final int n2, final long n3) {
        }
        
        @Override
        public void data(final boolean b, final int n, final BufferedSource bufferedSource, final int n2) throws IOException {
            if (Http2Connection.this.pushedStream(n)) {
                Http2Connection.this.pushDataLater(n, bufferedSource, n2, b);
            }
            else {
                final Http2Stream stream = Http2Connection.this.getStream(n);
                if (stream == null) {
                    Http2Connection.this.writeSynResetLater(n, ErrorCode.PROTOCOL_ERROR);
                    bufferedSource.skip(n2);
                }
                else {
                    stream.receiveData(bufferedSource, n2);
                    if (b) {
                        stream.receiveFin();
                    }
                }
            }
        }
        
        @Override
        protected void execute() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: astore_1       
            //     4: getstatic       okhttp3/internal/http2/ErrorCode.INTERNAL_ERROR:Lokhttp3/internal/http2/ErrorCode;
            //     7: astore_2       
            //     8: aload_1        
            //     9: astore_3       
            //    10: aload_1        
            //    11: astore          4
            //    13: aload_0        
            //    14: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //    17: aload_0        
            //    18: invokevirtual   okhttp3/internal/http2/Http2Reader.readConnectionPreface:(Lokhttp3/internal/http2/Http2Reader$Handler;)V
            //    21: aload_1        
            //    22: astore_3       
            //    23: aload_1        
            //    24: astore          4
            //    26: aload_0        
            //    27: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //    30: iconst_0       
            //    31: aload_0        
            //    32: invokevirtual   okhttp3/internal/http2/Http2Reader.nextFrame:(ZLokhttp3/internal/http2/Http2Reader$Handler;)Z
            //    35: ifne            21
            //    38: aload_1        
            //    39: astore_3       
            //    40: aload_1        
            //    41: astore          4
            //    43: getstatic       okhttp3/internal/http2/ErrorCode.NO_ERROR:Lokhttp3/internal/http2/ErrorCode;
            //    46: astore_1       
            //    47: aload_1        
            //    48: astore_3       
            //    49: aload_1        
            //    50: astore          4
            //    52: getstatic       okhttp3/internal/http2/ErrorCode.CANCEL:Lokhttp3/internal/http2/ErrorCode;
            //    55: astore          5
            //    57: aload_0        
            //    58: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    61: aload_1        
            //    62: aload           5
            //    64: invokevirtual   okhttp3/internal/http2/Http2Connection.close:(Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;)V
            //    67: aload_0        
            //    68: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //    71: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
            //    74: return         
            //    75: astore          4
            //    77: aload_3        
            //    78: astore          4
            //    80: getstatic       okhttp3/internal/http2/ErrorCode.PROTOCOL_ERROR:Lokhttp3/internal/http2/ErrorCode;
            //    83: astore_3       
            //    84: aload_3        
            //    85: astore          4
            //    87: getstatic       okhttp3/internal/http2/ErrorCode.PROTOCOL_ERROR:Lokhttp3/internal/http2/ErrorCode;
            //    90: astore_1       
            //    91: aload_0        
            //    92: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    95: aload_3        
            //    96: aload_1        
            //    97: invokevirtual   okhttp3/internal/http2/Http2Connection.close:(Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;)V
            //   100: aload_0        
            //   101: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //   104: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
            //   107: goto            74
            //   110: astore_3       
            //   111: aload_0        
            //   112: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   115: aload           4
            //   117: aload_2        
            //   118: invokevirtual   okhttp3/internal/http2/Http2Connection.close:(Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;)V
            //   121: aload_0        
            //   122: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //   125: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
            //   128: aload_3        
            //   129: athrow         
            //   130: astore          4
            //   132: goto            121
            //   135: astore          4
            //   137: goto            100
            //   140: astore          4
            //   142: goto            67
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  13     21     75     110    Ljava/io/IOException;
            //  13     21     110    135    Any
            //  26     38     75     110    Ljava/io/IOException;
            //  26     38     110    135    Any
            //  43     47     75     110    Ljava/io/IOException;
            //  43     47     110    135    Any
            //  52     57     75     110    Ljava/io/IOException;
            //  52     57     110    135    Any
            //  57     67     140    145    Ljava/io/IOException;
            //  80     84     110    135    Any
            //  87     91     110    135    Any
            //  91     100    135    140    Ljava/io/IOException;
            //  111    121    130    135    Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0067:
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        public void goAway(final int n, final ErrorCode errorCode, ByteString this$0) {
            if (this$0.size() > 0) {}
            this$0 = (ByteString)Http2Connection.this;
            synchronized (this$0) {
                final Http2Stream[] array = Http2Connection.this.streams.values().toArray(new Http2Stream[Http2Connection.this.streams.size()]);
                Http2Connection.this.shutdown = true;
                // monitorexit(this$0)
                for (int length = array.length, i = 0; i < length; ++i) {
                    this$0 = (ByteString)array[i];
                    if (((Http2Stream)this$0).getId() > n && ((Http2Stream)this$0).isLocallyInitiated()) {
                        ((Http2Stream)this$0).receiveRstStream(ErrorCode.REFUSED_STREAM);
                        Http2Connection.this.removeStream(((Http2Stream)this$0).getId());
                    }
                }
            }
        }
        
        @Override
        public void headers(final boolean b, final int i, final int n, final List<Header> list) {
            if (Http2Connection.this.pushedStream(i)) {
                Http2Connection.this.pushHeadersLater(i, list, b);
            }
            else {
                synchronized (Http2Connection.this) {
                    if (Http2Connection.this.shutdown) {
                        return;
                    }
                }
                final Http2Stream stream = Http2Connection.this.getStream(i);
                if (stream == null) {
                    if (i > Http2Connection.this.lastGoodStreamId) {
                        if (i % 2 != Http2Connection.this.nextStreamId % 2) {
                            final List<Header> list2;
                            final Http2Stream http2Stream = new Http2Stream(i, Http2Connection.this, false, b, list2);
                            Http2Connection.this.lastGoodStreamId = i;
                            Http2Connection.this.streams.put(i, http2Stream);
                            Http2Connection.executor.execute(new NamedRunnable("OkHttp %s stream %d", new Object[] { Http2Connection.this.hostname, i }) {
                                public void execute() {
                                    try {
                                        Http2Connection.this.listener.onStream(http2Stream);
                                    }
                                    catch (IOException ex) {
                                        Platform.get().log(4, "Http2Connection.Listener failure for " + Http2Connection.this.hostname, ex);
                                        try {
                                            http2Stream.close(ErrorCode.PROTOCOL_ERROR);
                                        }
                                        catch (IOException ex2) {}
                                    }
                                }
                            });
                        }
                        // monitorexit(http2Connection)
                    }
                }
                else {
                    // monitorexit(http2Connection)
                    final List<Header> list2;
                    stream.receiveHeaders(list2);
                    if (b) {
                        stream.receiveFin();
                    }
                }
            }
        }
        
        @Override
        public void ping(final boolean b, final int n, final int n2) {
            if (b) {
                final Ping removePing = Http2Connection.this.removePing(n);
                if (removePing != null) {
                    removePing.receive();
                }
            }
            else {
                Http2Connection.this.writePingLater(true, n, n2, null);
            }
        }
        
        @Override
        public void priority(final int n, final int n2, final int n3, final boolean b) {
        }
        
        @Override
        public void pushPromise(final int n, final int n2, final List<Header> list) {
            Http2Connection.this.pushRequestLater(n2, list);
        }
        
        @Override
        public void rstStream(final int n, final ErrorCode errorCode) {
            if (Http2Connection.this.pushedStream(n)) {
                Http2Connection.this.pushResetLater(n, errorCode);
            }
            else {
                final Http2Stream removeStream = Http2Connection.this.removeStream(n);
                if (removeStream != null) {
                    removeStream.receiveRstStream(errorCode);
                }
            }
        }
        
        @Override
        public void settings(final boolean p0, final Settings p1) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: lstore_3       
            //     2: aconst_null    
            //     3: astore          5
            //     5: aload_0        
            //     6: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //     9: astore          6
            //    11: aload           6
            //    13: monitorenter   
            //    14: aload_0        
            //    15: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    18: getfield        okhttp3/internal/http2/Http2Connection.peerSettings:Lokhttp3/internal/http2/Settings;
            //    21: invokevirtual   okhttp3/internal/http2/Settings.getInitialWindowSize:()I
            //    24: istore          7
            //    26: iload_1        
            //    27: ifeq            40
            //    30: aload_0        
            //    31: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    34: getfield        okhttp3/internal/http2/Http2Connection.peerSettings:Lokhttp3/internal/http2/Settings;
            //    37: invokevirtual   okhttp3/internal/http2/Settings.clear:()V
            //    40: aload_0        
            //    41: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    44: getfield        okhttp3/internal/http2/Http2Connection.peerSettings:Lokhttp3/internal/http2/Settings;
            //    47: aload_2        
            //    48: invokevirtual   okhttp3/internal/http2/Settings.merge:(Lokhttp3/internal/http2/Settings;)V
            //    51: aload_0        
            //    52: aload_2        
            //    53: invokespecial   okhttp3/internal/http2/Http2Connection$ReaderRunnable.applyAndAckSettings:(Lokhttp3/internal/http2/Settings;)V
            //    56: aload_0        
            //    57: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    60: getfield        okhttp3/internal/http2/Http2Connection.peerSettings:Lokhttp3/internal/http2/Settings;
            //    63: invokevirtual   okhttp3/internal/http2/Settings.getInitialWindowSize:()I
            //    66: istore          8
            //    68: lload_3        
            //    69: lstore          9
            //    71: aload           5
            //    73: astore_2       
            //    74: iload           8
            //    76: iconst_m1      
            //    77: if_icmpeq       186
            //    80: lload_3        
            //    81: lstore          9
            //    83: aload           5
            //    85: astore_2       
            //    86: iload           8
            //    88: iload           7
            //    90: if_icmpeq       186
            //    93: iload           8
            //    95: iload           7
            //    97: isub           
            //    98: i2l            
            //    99: lstore_3       
            //   100: aload_0        
            //   101: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   104: getfield        okhttp3/internal/http2/Http2Connection.receivedInitialPeerSettings:Z
            //   107: ifne            126
            //   110: aload_0        
            //   111: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   114: lload_3        
            //   115: invokevirtual   okhttp3/internal/http2/Http2Connection.addBytesToWriteWindow:(J)V
            //   118: aload_0        
            //   119: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   122: iconst_1       
            //   123: putfield        okhttp3/internal/http2/Http2Connection.receivedInitialPeerSettings:Z
            //   126: lload_3        
            //   127: lstore          9
            //   129: aload           5
            //   131: astore_2       
            //   132: aload_0        
            //   133: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   136: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
            //   139: invokeinterface java/util/Map.isEmpty:()Z
            //   144: ifne            186
            //   147: aload_0        
            //   148: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   151: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
            //   154: invokeinterface java/util/Map.values:()Ljava/util/Collection;
            //   159: aload_0        
            //   160: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   163: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
            //   166: invokeinterface java/util/Map.size:()I
            //   171: anewarray       Lokhttp3/internal/http2/Http2Stream;
            //   174: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
            //   179: checkcast       [Lokhttp3/internal/http2/Http2Stream;
            //   182: astore_2       
            //   183: lload_3        
            //   184: lstore          9
            //   186: getstatic       okhttp3/internal/http2/Http2Connection.executor:Ljava/util/concurrent/ExecutorService;
            //   189: astore          5
            //   191: new             Lokhttp3/internal/http2/Http2Connection$ReaderRunnable$2;
            //   194: astore          11
            //   196: aload           11
            //   198: aload_0        
            //   199: ldc_w           "OkHttp %s settings"
            //   202: iconst_1       
            //   203: anewarray       Ljava/lang/Object;
            //   206: dup            
            //   207: iconst_0       
            //   208: aload_0        
            //   209: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   212: getfield        okhttp3/internal/http2/Http2Connection.hostname:Ljava/lang/String;
            //   215: aastore        
            //   216: invokespecial   okhttp3/internal/http2/Http2Connection$ReaderRunnable$2.<init>:(Lokhttp3/internal/http2/Http2Connection$ReaderRunnable;Ljava/lang/String;[Ljava/lang/Object;)V
            //   219: aload           5
            //   221: aload           11
            //   223: invokeinterface java/util/concurrent/ExecutorService.execute:(Ljava/lang/Runnable;)V
            //   228: aload           6
            //   230: monitorexit    
            //   231: aload_2        
            //   232: ifnull          293
            //   235: lload           9
            //   237: lconst_0       
            //   238: lcmp           
            //   239: ifeq            293
            //   242: aload_2        
            //   243: arraylength    
            //   244: istore          7
            //   246: iconst_0       
            //   247: istore          8
            //   249: iload           8
            //   251: iload           7
            //   253: if_icmpge       293
            //   256: aload_2        
            //   257: iload           8
            //   259: aaload         
            //   260: astore          5
            //   262: aload           5
            //   264: monitorenter   
            //   265: aload           5
            //   267: lload           9
            //   269: invokevirtual   okhttp3/internal/http2/Http2Stream.addBytesToWriteWindow:(J)V
            //   272: aload           5
            //   274: monitorexit    
            //   275: iinc            8, 1
            //   278: goto            249
            //   281: astore_2       
            //   282: aload           6
            //   284: monitorexit    
            //   285: aload_2        
            //   286: athrow         
            //   287: astore_2       
            //   288: aload           5
            //   290: monitorexit    
            //   291: aload_2        
            //   292: athrow         
            //   293: return         
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  14     26     281    287    Any
            //  30     40     281    287    Any
            //  40     68     281    287    Any
            //  100    126    281    287    Any
            //  132    183    281    287    Any
            //  186    231    281    287    Any
            //  265    275    287    293    Any
            //  282    285    281    287    Any
            //  288    291    287    293    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        public void windowUpdate(final int n, final long n2) {
            Label_0050: {
                if (n != 0) {
                    break Label_0050;
                }
                synchronized (Http2Connection.this) {
                    final Http2Connection this$0 = Http2Connection.this;
                    this$0.bytesLeftInWriteWindow += n2;
                    Http2Connection.this.notifyAll();
                    return;
                }
            }
            final Http2Stream stream = Http2Connection.this.getStream(n);
            if (stream == null) {
                return;
            }
            synchronized (stream) {
                stream.addBytesToWriteWindow(n2);
            }
        }
    }
}

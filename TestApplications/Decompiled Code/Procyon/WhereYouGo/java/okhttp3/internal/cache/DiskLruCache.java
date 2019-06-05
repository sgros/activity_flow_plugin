// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.cache;

import java.util.Arrays;
import okio.Source;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.ArrayList;
import okhttp3.internal.platform.Platform;
import okio.BufferedSource;
import java.io.EOFException;
import java.util.Iterator;
import java.io.FileNotFoundException;
import okio.Okio;
import java.io.IOException;
import okio.Sink;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import okhttp3.internal.Util;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.LinkedHashMap;
import okio.BufferedSink;
import okhttp3.internal.io.FileSystem;
import java.util.concurrent.Executor;
import java.io.File;
import java.util.regex.Pattern;
import java.io.Flushable;
import java.io.Closeable;

public final class DiskLruCache implements Closeable, Flushable
{
    static final long ANY_SEQUENCE_NUMBER = -1L;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN;
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Runnable cleanupRunnable;
    boolean closed;
    final File directory;
    private final Executor executor;
    final FileSystem fileSystem;
    boolean hasJournalErrors;
    boolean initialized;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    BufferedSink journalWriter;
    final LinkedHashMap<String, Entry> lruEntries;
    private long maxSize;
    boolean mostRecentRebuildFailed;
    boolean mostRecentTrimFailed;
    private long nextSequenceNumber;
    int redundantOpCount;
    private long size;
    final int valueCount;
    
    static {
        LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
    }
    
    DiskLruCache(final FileSystem fileSystem, final File file, final int appVersion, final int valueCount, final long maxSize, final Executor executor) {
        this.size = 0L;
        this.lruEntries = new LinkedHashMap<String, Entry>(0, 0.75f, true);
        this.nextSequenceNumber = 0L;
        this.cleanupRunnable = new Runnable() {
            @Override
            public void run() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: istore_1       
                //     2: aload_0        
                //     3: getfield        okhttp3/internal/cache/DiskLruCache$1.this$0:Lokhttp3/internal/cache/DiskLruCache;
                //     6: astore_2       
                //     7: aload_2        
                //     8: monitorenter   
                //     9: aload_0        
                //    10: getfield        okhttp3/internal/cache/DiskLruCache$1.this$0:Lokhttp3/internal/cache/DiskLruCache;
                //    13: getfield        okhttp3/internal/cache/DiskLruCache.initialized:Z
                //    16: ifne            34
                //    19: iload_1        
                //    20: aload_0        
                //    21: getfield        okhttp3/internal/cache/DiskLruCache$1.this$0:Lokhttp3/internal/cache/DiskLruCache;
                //    24: getfield        okhttp3/internal/cache/DiskLruCache.closed:Z
                //    27: ior            
                //    28: ifeq            39
                //    31: aload_2        
                //    32: monitorexit    
                //    33: return         
                //    34: iconst_0       
                //    35: istore_1       
                //    36: goto            19
                //    39: aload_0        
                //    40: getfield        okhttp3/internal/cache/DiskLruCache$1.this$0:Lokhttp3/internal/cache/DiskLruCache;
                //    43: invokevirtual   okhttp3/internal/cache/DiskLruCache.trimToSize:()V
                //    46: aload_0        
                //    47: getfield        okhttp3/internal/cache/DiskLruCache$1.this$0:Lokhttp3/internal/cache/DiskLruCache;
                //    50: invokevirtual   okhttp3/internal/cache/DiskLruCache.journalRebuildRequired:()Z
                //    53: ifeq            71
                //    56: aload_0        
                //    57: getfield        okhttp3/internal/cache/DiskLruCache$1.this$0:Lokhttp3/internal/cache/DiskLruCache;
                //    60: invokevirtual   okhttp3/internal/cache/DiskLruCache.rebuildJournal:()V
                //    63: aload_0        
                //    64: getfield        okhttp3/internal/cache/DiskLruCache$1.this$0:Lokhttp3/internal/cache/DiskLruCache;
                //    67: iconst_0       
                //    68: putfield        okhttp3/internal/cache/DiskLruCache.redundantOpCount:I
                //    71: aload_2        
                //    72: monitorexit    
                //    73: goto            33
                //    76: astore_3       
                //    77: aload_2        
                //    78: monitorexit    
                //    79: aload_3        
                //    80: athrow         
                //    81: astore_3       
                //    82: aload_0        
                //    83: getfield        okhttp3/internal/cache/DiskLruCache$1.this$0:Lokhttp3/internal/cache/DiskLruCache;
                //    86: iconst_1       
                //    87: putfield        okhttp3/internal/cache/DiskLruCache.mostRecentTrimFailed:Z
                //    90: goto            46
                //    93: astore_3       
                //    94: aload_0        
                //    95: getfield        okhttp3/internal/cache/DiskLruCache$1.this$0:Lokhttp3/internal/cache/DiskLruCache;
                //    98: iconst_1       
                //    99: putfield        okhttp3/internal/cache/DiskLruCache.mostRecentRebuildFailed:Z
                //   102: aload_0        
                //   103: getfield        okhttp3/internal/cache/DiskLruCache$1.this$0:Lokhttp3/internal/cache/DiskLruCache;
                //   106: invokestatic    okio/Okio.blackhole:()Lokio/Sink;
                //   109: invokestatic    okio/Okio.buffer:(Lokio/Sink;)Lokio/BufferedSink;
                //   112: putfield        okhttp3/internal/cache/DiskLruCache.journalWriter:Lokio/BufferedSink;
                //   115: goto            71
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  9      19     76     81     Any
                //  19     33     76     81     Any
                //  39     46     81     93     Ljava/io/IOException;
                //  39     46     76     81     Any
                //  46     71     93     118    Ljava/io/IOException;
                //  46     71     76     81     Any
                //  71     73     76     81     Any
                //  77     79     76     81     Any
                //  82     90     76     81     Any
                //  94     115    76     81     Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IndexOutOfBoundsException: Index 62 out-of-bounds for length 62
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
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
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
        };
        this.fileSystem = fileSystem;
        this.directory = file;
        this.appVersion = appVersion;
        this.journalFile = new File(file, "journal");
        this.journalFileTmp = new File(file, "journal.tmp");
        this.journalFileBackup = new File(file, "journal.bkp");
        this.valueCount = valueCount;
        this.maxSize = maxSize;
        this.executor = executor;
    }
    
    private void checkNotClosed() {
        synchronized (this) {
            if (this.isClosed()) {
                throw new IllegalStateException("cache is closed");
            }
        }
    }
    // monitorexit(this)
    
    public static DiskLruCache create(final FileSystem fileSystem, final File file, final int n, final int n2, final long n3) {
        if (n3 <= 0L) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (n2 <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }
        return new DiskLruCache(fileSystem, file, n, n2, n3, new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Util.threadFactory("OkHttp DiskLruCache", true)));
    }
    
    private BufferedSink newJournalWriter() throws FileNotFoundException {
        return Okio.buffer(new FaultHidingSink(this.fileSystem.appendingSink(this.journalFile)) {
            @Override
            protected void onException(final IOException ex) {
                assert Thread.holdsLock(DiskLruCache.this);
                DiskLruCache.this.hasJournalErrors = true;
            }
        });
    }
    
    private void processJournal() throws IOException {
        this.fileSystem.delete(this.journalFileTmp);
        final Iterator<Entry> iterator = this.lruEntries.values().iterator();
        while (iterator.hasNext()) {
            final Entry entry = iterator.next();
            if (entry.currentEditor == null) {
                for (int i = 0; i < this.valueCount; ++i) {
                    this.size += entry.lengths[i];
                }
            }
            else {
                entry.currentEditor = null;
                for (int j = 0; j < this.valueCount; ++j) {
                    this.fileSystem.delete(entry.cleanFiles[j]);
                    this.fileSystem.delete(entry.dirtyFiles[j]);
                }
                iterator.remove();
            }
        }
    }
    
    private void readJournal() throws IOException {
        final BufferedSource buffer = Okio.buffer(this.fileSystem.source(this.journalFile));
        try {
            final String utf8LineStrict = buffer.readUtf8LineStrict();
            final String utf8LineStrict2 = buffer.readUtf8LineStrict();
            final String utf8LineStrict3 = buffer.readUtf8LineStrict();
            final String utf8LineStrict4 = buffer.readUtf8LineStrict();
            final String utf8LineStrict5 = buffer.readUtf8LineStrict();
            if (!"libcore.io.DiskLruCache".equals(utf8LineStrict) || !"1".equals(utf8LineStrict2) || !Integer.toString(this.appVersion).equals(utf8LineStrict3) || !Integer.toString(this.valueCount).equals(utf8LineStrict4) || !"".equals(utf8LineStrict5)) {
                throw new IOException("unexpected journal header: [" + utf8LineStrict + ", " + utf8LineStrict2 + ", " + utf8LineStrict4 + ", " + utf8LineStrict5 + "]");
            }
        }
        finally {
            Util.closeQuietly(buffer);
        }
        int n = 0;
        try {
            while (true) {
                this.readJournalLine(buffer.readUtf8LineStrict());
                ++n;
            }
        }
        catch (EOFException ex) {
            this.redundantOpCount = n - this.lruEntries.size();
            if (!buffer.exhausted()) {
                this.rebuildJournal();
            }
            else {
                this.journalWriter = this.newJournalWriter();
            }
            Util.closeQuietly(buffer);
        }
    }
    
    private void readJournalLine(final String s) throws IOException {
        final int index = s.indexOf(32);
        if (index == -1) {
            throw new IOException("unexpected journal line: " + s);
        }
        final int beginIndex = index + 1;
        final int index2 = s.indexOf(32, beginIndex);
        String s2 = null;
        Label_0112: {
            if (index2 != -1) {
                s2 = s.substring(beginIndex, index2);
                break Label_0112;
            }
            final String key = s2 = s.substring(beginIndex);
            if (index != "REMOVE".length()) {
                break Label_0112;
            }
            s2 = key;
            if (!s.startsWith("REMOVE")) {
                break Label_0112;
            }
            this.lruEntries.remove(key);
            return;
        }
        Entry value;
        if ((value = this.lruEntries.get(s2)) == null) {
            value = new Entry(s2);
            this.lruEntries.put(s2, value);
        }
        if (index2 != -1 && index == "CLEAN".length() && s.startsWith("CLEAN")) {
            final String[] split = s.substring(index2 + 1).split(" ");
            value.readable = true;
            value.currentEditor = null;
            value.setLengths(split);
            return;
        }
        if (index2 == -1 && index == "DIRTY".length() && s.startsWith("DIRTY")) {
            value.currentEditor = new Editor(value);
            return;
        }
        if (index2 != -1 || index != "READ".length() || !s.startsWith("READ")) {
            throw new IOException("unexpected journal line: " + s);
        }
    }
    
    private void validateKey(final String s) {
        if (!DiskLruCache.LEGAL_KEY_PATTERN.matcher(s).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + s + "\"");
        }
    }
    
    @Override
    public void close() throws IOException {
        synchronized (this) {
            if (!this.initialized || this.closed) {
                this.closed = true;
            }
            else {
                for (final Entry entry : this.lruEntries.values().toArray(new Entry[this.lruEntries.size()])) {
                    if (entry.currentEditor != null) {
                        entry.currentEditor.abort();
                    }
                }
                this.trimToSize();
                this.journalWriter.close();
                this.journalWriter = null;
                this.closed = true;
            }
        }
    }
    
    void completeEdit(final Editor editor, final boolean b) throws IOException {
        final Entry entry;
        synchronized (this) {
            entry = editor.entry;
            if (entry.currentEditor != editor) {
                throw new IllegalStateException();
            }
        }
    Label_0123:
        while (true) {
            if (b && !entry.readable) {
                for (int i = 0; i < this.valueCount; ++i) {
                    final Editor editor2;
                    if (!editor2.written[i]) {
                        editor2.abort();
                        throw new IllegalStateException("Newly created entry didn't create value for index " + i);
                    }
                    if (!this.fileSystem.exists(entry.dirtyFiles[i])) {
                        editor2.abort();
                        break Label_0123;
                    }
                }
            }
            Label_0132: {
                break Label_0132;
                return;
            }
            for (int j = 0; j < this.valueCount; ++j) {
                final File file = entry.dirtyFiles[j];
                if (b) {
                    if (this.fileSystem.exists(file)) {
                        final File file2 = entry.cleanFiles[j];
                        this.fileSystem.rename(file, file2);
                        final long n = entry.lengths[j];
                        final long size = this.fileSystem.size(file2);
                        entry.lengths[j] = size;
                        this.size = this.size - n + size;
                    }
                }
                else {
                    this.fileSystem.delete(file);
                }
            }
            ++this.redundantOpCount;
            entry.currentEditor = null;
            if (entry.readable | b) {
                entry.readable = true;
                this.journalWriter.writeUtf8("CLEAN").writeByte(32);
                this.journalWriter.writeUtf8(entry.key);
                entry.writeLengths(this.journalWriter);
                this.journalWriter.writeByte(10);
                if (b) {
                    final long nextSequenceNumber = this.nextSequenceNumber;
                    this.nextSequenceNumber = 1L + nextSequenceNumber;
                    entry.sequenceNumber = nextSequenceNumber;
                }
            }
            else {
                this.lruEntries.remove(entry.key);
                this.journalWriter.writeUtf8("REMOVE").writeByte(32);
                this.journalWriter.writeUtf8(entry.key);
                this.journalWriter.writeByte(10);
            }
            this.journalWriter.flush();
            if (this.size > this.maxSize || this.journalRebuildRequired()) {
                this.executor.execute(this.cleanupRunnable);
            }
            continue Label_0123;
        }
    }
    // monitorexit(this)
    
    public void delete() throws IOException {
        this.close();
        this.fileSystem.deleteContents(this.directory);
    }
    
    public Editor edit(final String s) throws IOException {
        return this.edit(s, -1L);
    }
    
    Editor edit(final String key, final long n) throws IOException {
        while (true) {
            final Editor editor = null;
            final Entry entry;
            synchronized (this) {
                this.initialize();
                this.checkNotClosed();
                this.validateKey(key);
                entry = this.lruEntries.get(key);
                Label_0071: {
                    if (n == -1L) {
                        break Label_0071;
                    }
                    Editor editor2 = editor;
                    if (entry != null) {
                        if (entry.sequenceNumber == n) {
                            break Label_0071;
                        }
                        editor2 = editor;
                    }
                    return editor2;
                }
                if (entry != null) {
                    final Editor editor2 = editor;
                    if (entry.currentEditor != null) {
                        return editor2;
                    }
                }
                if (this.mostRecentTrimFailed || this.mostRecentRebuildFailed) {
                    this.executor.execute(this.cleanupRunnable);
                    return editor;
                }
            }
            final String key2;
            this.journalWriter.writeUtf8("DIRTY").writeByte(32).writeUtf8(key2).writeByte(10);
            this.journalWriter.flush();
            Editor editor2 = editor;
            if (!this.hasJournalErrors) {
                Entry value;
                if ((value = entry) == null) {
                    value = new Entry(key2);
                    this.lruEntries.put(key2, value);
                }
                final Editor currentEditor = new Editor(value);
                value.currentEditor = currentEditor;
                editor2 = currentEditor;
                return editor2;
            }
            return editor2;
        }
    }
    
    public void evictAll() throws IOException {
        int i = 0;
        synchronized (this) {
            this.initialize();
            for (Entry[] array = this.lruEntries.values().toArray(new Entry[this.lruEntries.size()]); i < array.length; ++i) {
                this.removeEntry(array[i]);
            }
            this.mostRecentTrimFailed = false;
        }
    }
    
    @Override
    public void flush() throws IOException {
        synchronized (this) {
            if (this.initialized) {
                this.checkNotClosed();
                this.trimToSize();
                this.journalWriter.flush();
            }
        }
    }
    
    public Snapshot get(final String key) throws IOException {
        synchronized (this) {
            this.initialize();
            this.checkNotClosed();
            this.validateKey(key);
            final Entry entry = this.lruEntries.get(key);
            Snapshot snapshot;
            if (entry == null || !entry.readable) {
                snapshot = null;
            }
            else {
                final Snapshot snapshot2 = entry.snapshot();
                if (snapshot2 == null) {
                    snapshot = null;
                }
                else {
                    ++this.redundantOpCount;
                    this.journalWriter.writeUtf8("READ").writeByte(32).writeUtf8(key).writeByte(10);
                    snapshot = snapshot2;
                    if (this.journalRebuildRequired()) {
                        this.executor.execute(this.cleanupRunnable);
                        snapshot = snapshot2;
                    }
                }
            }
            return snapshot;
        }
    }
    
    public File getDirectory() {
        return this.directory;
    }
    
    public long getMaxSize() {
        synchronized (this) {
            return this.maxSize;
        }
    }
    
    public void initialize() throws IOException {
        synchronized (this) {
            assert Thread.holdsLock(this);
        }
        Label_0039: {
            if (!this.initialized) {
                Label_0200: {
                    if (this.fileSystem.exists(this.journalFileBackup)) {
                        if (!this.fileSystem.exists(this.journalFile)) {
                            break Label_0200;
                        }
                        this.fileSystem.delete(this.journalFileBackup);
                    }
                    Label_0188: {
                        if (!this.fileSystem.exists(this.journalFile)) {
                            break Label_0188;
                        }
                        try {
                            this.readJournal();
                            this.processJournal();
                            this.initialized = true;
                        }
                        catch (IOException ex) {
                            Platform.get().log(5, "DiskLruCache " + this.directory + " is corrupt: " + ex.getMessage() + ", removing", ex);
                            try {
                                this.delete();
                                this.closed = false;
                                this.rebuildJournal();
                                this.initialized = true;
                                break Label_0039;
                                this.fileSystem.rename(this.journalFileBackup, this.journalFile);
                            }
                            finally {
                                this.closed = false;
                            }
                        }
                    }
                }
            }
        }
    }
    // monitorexit(this)
    
    public boolean isClosed() {
        synchronized (this) {
            return this.closed;
        }
    }
    
    boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }
    
    void rebuildJournal() throws IOException {
        BufferedSink buffer;
        while (true) {
            while (true) {
                synchronized (this) {
                    if (this.journalWriter != null) {
                        this.journalWriter.close();
                    }
                    buffer = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
                    try {
                        buffer.writeUtf8("libcore.io.DiskLruCache").writeByte(10);
                        buffer.writeUtf8("1").writeByte(10);
                        buffer.writeDecimalLong(this.appVersion).writeByte(10);
                        buffer.writeDecimalLong(this.valueCount).writeByte(10);
                        buffer.writeByte(10);
                        for (final Entry entry : this.lruEntries.values()) {
                            if (entry.currentEditor == null) {
                                break;
                            }
                            buffer.writeUtf8("DIRTY").writeByte(32);
                            buffer.writeUtf8(entry.key);
                            buffer.writeByte(10);
                        }
                    }
                    finally {
                        buffer.close();
                    }
                }
                buffer.writeUtf8("CLEAN").writeByte(32);
                final Entry entry2;
                buffer.writeUtf8(entry2.key);
                entry2.writeLengths(buffer);
                buffer.writeByte(10);
                continue;
            }
        }
        buffer.close();
        if (this.fileSystem.exists(this.journalFile)) {
            this.fileSystem.rename(this.journalFile, this.journalFileBackup);
        }
        this.fileSystem.rename(this.journalFileTmp, this.journalFile);
        this.fileSystem.delete(this.journalFileBackup);
        this.journalWriter = this.newJournalWriter();
        this.hasJournalErrors = false;
        this.mostRecentRebuildFailed = false;
    }
    // monitorexit(this)
    
    public boolean remove(final String key) throws IOException {
        boolean b = false;
        synchronized (this) {
            this.initialize();
            this.checkNotClosed();
            this.validateKey(key);
            final Entry entry = this.lruEntries.get(key);
            if (entry != null) {
                final boolean removeEntry = this.removeEntry(entry);
                if (b = removeEntry) {
                    b = removeEntry;
                    if (this.size <= this.maxSize) {
                        this.mostRecentTrimFailed = false;
                        b = removeEntry;
                    }
                }
            }
            return b;
        }
    }
    
    boolean removeEntry(final Entry entry) throws IOException {
        if (entry.currentEditor != null) {
            entry.currentEditor.detach();
        }
        for (int i = 0; i < this.valueCount; ++i) {
            this.fileSystem.delete(entry.cleanFiles[i]);
            this.size -= entry.lengths[i];
            entry.lengths[i] = 0L;
        }
        ++this.redundantOpCount;
        this.journalWriter.writeUtf8("REMOVE").writeByte(32).writeUtf8(entry.key).writeByte(10);
        this.lruEntries.remove(entry.key);
        if (this.journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
        return true;
    }
    
    public void setMaxSize(final long maxSize) {
        synchronized (this) {
            this.maxSize = maxSize;
            if (this.initialized) {
                this.executor.execute(this.cleanupRunnable);
            }
        }
    }
    
    public long size() throws IOException {
        synchronized (this) {
            this.initialize();
            return this.size;
        }
    }
    
    public Iterator<Snapshot> snapshots() throws IOException {
        synchronized (this) {
            this.initialize();
            return new Iterator<Snapshot>() {
                final Iterator<Entry> delegate = new ArrayList<Entry>((Collection<? extends Entry>)DiskLruCache.this.lruEntries.values()).iterator();
                Snapshot nextSnapshot;
                Snapshot removeSnapshot;
                
                @Override
                public boolean hasNext() {
                    boolean b = true;
                    if (this.nextSnapshot == null) {
                        Label_0082: {
                            synchronized (DiskLruCache.this) {
                                if (DiskLruCache.this.closed) {
                                    // monitorexit(this.this$0)
                                    b = false;
                                    return b;
                                }
                                Snapshot snapshot = null;
                                Block_5: {
                                    while (this.delegate.hasNext()) {
                                        snapshot = this.delegate.next().snapshot();
                                        if (snapshot != null) {
                                            break Block_5;
                                        }
                                    }
                                    break Label_0082;
                                }
                                this.nextSnapshot = snapshot;
                                return b;
                            }
                        }
                        // monitorexit(diskLruCache)
                        b = false;
                    }
                    return b;
                }
                
                @Override
                public Snapshot next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.removeSnapshot = this.nextSnapshot;
                    this.nextSnapshot = null;
                    return this.removeSnapshot;
                }
                
                @Override
                public void remove() {
                    if (this.removeSnapshot == null) {
                        throw new IllegalStateException("remove() before next()");
                    }
                    try {
                        DiskLruCache.this.remove(this.removeSnapshot.key);
                    }
                    catch (IOException ex) {
                        this.removeSnapshot = null;
                    }
                    finally {
                        this.removeSnapshot = null;
                    }
                }
            };
        }
    }
    
    void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            this.removeEntry(this.lruEntries.values().iterator().next());
        }
        this.mostRecentTrimFailed = false;
    }
    
    public final class Editor
    {
        private boolean done;
        final Entry entry;
        final boolean[] written;
        
        Editor(final Entry entry) {
            this.entry = entry;
            boolean[] written;
            if (entry.readable) {
                written = null;
            }
            else {
                written = new boolean[DiskLruCache.this.valueCount];
            }
            this.written = written;
        }
        
        public void abort() throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.done) {
                    throw new IllegalStateException();
                }
            }
            if (this.entry.currentEditor == this) {
                DiskLruCache.this.completeEdit(this, false);
            }
            this.done = true;
        }
        // monitorexit(diskLruCache)
        
        public void abortUnlessCommitted() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        okhttp3/internal/cache/DiskLruCache$Editor.this$0:Lokhttp3/internal/cache/DiskLruCache;
            //     4: astore_1       
            //     5: aload_1        
            //     6: monitorenter   
            //     7: aload_0        
            //     8: getfield        okhttp3/internal/cache/DiskLruCache$Editor.done:Z
            //    11: ifne            36
            //    14: aload_0        
            //    15: getfield        okhttp3/internal/cache/DiskLruCache$Editor.entry:Lokhttp3/internal/cache/DiskLruCache$Entry;
            //    18: getfield        okhttp3/internal/cache/DiskLruCache$Entry.currentEditor:Lokhttp3/internal/cache/DiskLruCache$Editor;
            //    21: astore_2       
            //    22: aload_2        
            //    23: aload_0        
            //    24: if_acmpne       36
            //    27: aload_0        
            //    28: getfield        okhttp3/internal/cache/DiskLruCache$Editor.this$0:Lokhttp3/internal/cache/DiskLruCache;
            //    31: aload_0        
            //    32: iconst_0       
            //    33: invokevirtual   okhttp3/internal/cache/DiskLruCache.completeEdit:(Lokhttp3/internal/cache/DiskLruCache$Editor;Z)V
            //    36: aload_1        
            //    37: monitorexit    
            //    38: return         
            //    39: astore_2       
            //    40: aload_1        
            //    41: monitorexit    
            //    42: aload_2        
            //    43: athrow         
            //    44: astore_2       
            //    45: goto            36
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  7      22     39     44     Any
            //  27     36     44     48     Ljava/io/IOException;
            //  27     36     39     44     Any
            //  36     38     39     44     Any
            //  40     42     39     44     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0036:
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
        
        public void commit() throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.done) {
                    throw new IllegalStateException();
                }
            }
            if (this.entry.currentEditor == this) {
                DiskLruCache.this.completeEdit(this, true);
            }
            this.done = true;
        }
        // monitorexit(diskLruCache)
        
        void detach() {
            if (this.entry.currentEditor != this) {
                return;
            }
            int n = 0;
        Label_0045_Outer:
            while (true) {
                Label_0051: {
                    if (n >= DiskLruCache.this.valueCount) {
                        break Label_0051;
                    }
                    while (true) {
                        try {
                            DiskLruCache.this.fileSystem.delete(this.entry.dirtyFiles[n]);
                            ++n;
                            continue Label_0045_Outer;
                            this.entry.currentEditor = null;
                        }
                        catch (IOException ex) {
                            continue;
                        }
                        break;
                    }
                }
                break;
            }
        }
        
        public Sink newSink(final int n) {
            synchronized (DiskLruCache.this) {
                if (this.done) {
                    throw new IllegalStateException();
                }
            }
            Sink sink;
            if (this.entry.currentEditor != this) {
                sink = Okio.blackhole();
            }
            // monitorexit(diskLruCache)
            else {
                if (!this.entry.readable) {
                    this.written[n] = true;
                }
                final File file = this.entry.dirtyFiles[n];
                try {
                    sink = new FaultHidingSink(DiskLruCache.this.fileSystem.sink(file)) {
                        @Override
                        protected void onException(final IOException ex) {
                            synchronized (DiskLruCache.this) {
                                Editor.this.detach();
                            }
                        }
                    };
                }
                // monitorexit(diskLruCache)
                catch (FileNotFoundException ex) {
                    sink = Okio.blackhole();
                }
                // monitorexit(diskLruCache)
            }
            return sink;
        }
        
        public Source newSource(final int n) {
            synchronized (DiskLruCache.this) {
                if (this.done) {
                    throw new IllegalStateException();
                }
            }
            Source source = null;
            if (this.entry.readable && this.entry.currentEditor == this) {
                try {
                    source = DiskLruCache.this.fileSystem.source(this.entry.cleanFiles[n]);
                }
                // monitorexit(diskLruCache)
                catch (FileNotFoundException ex) {
                }
                // monitorexit(diskLruCache)
            }
            return source;
        }
    }
    
    private final class Entry
    {
        final File[] cleanFiles;
        Editor currentEditor;
        final File[] dirtyFiles;
        final String key;
        final long[] lengths;
        boolean readable;
        long sequenceNumber;
        
        Entry(final String s) {
            this.key = s;
            this.lengths = new long[DiskLruCache.this.valueCount];
            this.cleanFiles = new File[DiskLruCache.this.valueCount];
            this.dirtyFiles = new File[DiskLruCache.this.valueCount];
            final StringBuilder append = new StringBuilder(s).append('.');
            final int length = append.length();
            for (int i = 0; i < DiskLruCache.this.valueCount; ++i) {
                append.append(i);
                this.cleanFiles[i] = new File(DiskLruCache.this.directory, append.toString());
                append.append(".tmp");
                this.dirtyFiles[i] = new File(DiskLruCache.this.directory, append.toString());
                append.setLength(length);
            }
        }
        
        private IOException invalidLengths(final String[] a) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(a));
        }
        
        void setLengths(final String[] array) throws IOException {
            if (array.length != DiskLruCache.this.valueCount) {
                throw this.invalidLengths(array);
            }
            int i = 0;
            try {
                while (i < array.length) {
                    this.lengths[i] = Long.parseLong(array[i]);
                    ++i;
                }
            }
            catch (NumberFormatException ex) {
                throw this.invalidLengths(array);
            }
        }
        
        Snapshot snapshot() {
            if (!Thread.holdsLock(DiskLruCache.this)) {
                throw new AssertionError();
            }
            final Source[] array = new Source[DiskLruCache.this.valueCount];
            final long[] array2 = this.lengths.clone();
            int i = 0;
            try {
                while (i < DiskLruCache.this.valueCount) {
                    array[i] = DiskLruCache.this.fileSystem.source(this.cleanFiles[i]);
                    ++i;
                }
                final Closeable closeable = new Snapshot(this.key, this.sequenceNumber, array, array2);
                return (Snapshot)closeable;
            }
            catch (FileNotFoundException ex) {
                for (int n = 0; n < DiskLruCache.this.valueCount && array[n] != null; ++n) {
                    Util.closeQuietly(array[n]);
                }
                try {
                    DiskLruCache.this.removeEntry(this);
                    final Closeable closeable = null;
                }
                catch (IOException ex2) {}
            }
        }
        
        void writeLengths(final BufferedSink bufferedSink) throws IOException {
            final long[] lengths = this.lengths;
            for (int length = lengths.length, i = 0; i < length; ++i) {
                bufferedSink.writeByte(32).writeDecimalLong(lengths[i]);
            }
        }
    }
    
    public final class Snapshot implements Closeable
    {
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        private final Source[] sources;
        
        Snapshot(final String key, final long sequenceNumber, final Source[] sources, final long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.sources = sources;
            this.lengths = lengths;
        }
        
        @Override
        public void close() {
            final Source[] sources = this.sources;
            for (int length = sources.length, i = 0; i < length; ++i) {
                Util.closeQuietly(sources[i]);
            }
        }
        
        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }
        
        public long getLength(final int n) {
            return this.lengths[n];
        }
        
        public Source getSource(final int n) {
            return this.sources[n];
        }
        
        public String key() {
            return this.key;
        }
    }
}
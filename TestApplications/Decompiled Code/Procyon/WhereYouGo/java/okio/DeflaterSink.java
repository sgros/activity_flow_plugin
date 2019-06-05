// 
// Decompiled by Procyon v0.5.34
// 

package okio;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
import java.io.IOException;
import java.util.zip.Deflater;

public final class DeflaterSink implements Sink
{
    private boolean closed;
    private final Deflater deflater;
    private final BufferedSink sink;
    
    DeflaterSink(final BufferedSink sink, final Deflater deflater) {
        if (sink == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (deflater == null) {
            throw new IllegalArgumentException("inflater == null");
        }
        this.sink = sink;
        this.deflater = deflater;
    }
    
    public DeflaterSink(final Sink sink, final Deflater deflater) {
        this(Okio.buffer(sink), deflater);
    }
    
    @IgnoreJRERequirement
    private void deflate(final boolean b) throws IOException {
        final Buffer buffer = this.sink.buffer();
        Segment writableSegment;
        while (true) {
            writableSegment = buffer.writableSegment(1);
            int n;
            if (b) {
                n = this.deflater.deflate(writableSegment.data, writableSegment.limit, 8192 - writableSegment.limit, 2);
            }
            else {
                n = this.deflater.deflate(writableSegment.data, writableSegment.limit, 8192 - writableSegment.limit);
            }
            if (n > 0) {
                writableSegment.limit += n;
                buffer.size += n;
                this.sink.emitCompleteSegments();
            }
            else {
                if (this.deflater.needsInput()) {
                    break;
                }
                continue;
            }
        }
        if (writableSegment.pos == writableSegment.limit) {
            buffer.head = writableSegment.pop();
            SegmentPool.recycle(writableSegment);
        }
    }
    
    @Override
    public void close() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        okio/DeflaterSink.closed:Z
        //     4: ifeq            8
        //     7: return         
        //     8: aconst_null    
        //     9: astore_1       
        //    10: aload_0        
        //    11: invokevirtual   okio/DeflaterSink.finishDeflate:()V
        //    14: aload_0        
        //    15: getfield        okio/DeflaterSink.deflater:Ljava/util/zip/Deflater;
        //    18: invokevirtual   java/util/zip/Deflater.end:()V
        //    21: aload_1        
        //    22: astore_2       
        //    23: aload_0        
        //    24: getfield        okio/DeflaterSink.sink:Lokio/BufferedSink;
        //    27: invokeinterface okio/BufferedSink.close:()V
        //    32: aload_2        
        //    33: astore_1       
        //    34: aload_0        
        //    35: iconst_1       
        //    36: putfield        okio/DeflaterSink.closed:Z
        //    39: aload_1        
        //    40: ifnull          7
        //    43: aload_1        
        //    44: invokestatic    okio/Util.sneakyRethrow:(Ljava/lang/Throwable;)V
        //    47: goto            7
        //    50: astore_1       
        //    51: goto            14
        //    54: astore_3       
        //    55: aload_1        
        //    56: astore_2       
        //    57: aload_1        
        //    58: ifnonnull       23
        //    61: aload_3        
        //    62: astore_2       
        //    63: goto            23
        //    66: astore_3       
        //    67: aload_2        
        //    68: astore_1       
        //    69: aload_2        
        //    70: ifnonnull       34
        //    73: aload_3        
        //    74: astore_1       
        //    75: goto            34
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  10     14     50     54     Ljava/lang/Throwable;
        //  14     21     54     66     Ljava/lang/Throwable;
        //  23     32     66     78     Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 44 out-of-bounds for length 44
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
    
    void finishDeflate() throws IOException {
        this.deflater.finish();
        this.deflate(false);
    }
    
    @Override
    public void flush() throws IOException {
        this.deflate(true);
        this.sink.flush();
    }
    
    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }
    
    @Override
    public String toString() {
        return "DeflaterSink(" + this.sink + ")";
    }
    
    @Override
    public void write(final Buffer buffer, long a) throws IOException {
        Util.checkOffsetAndCount(buffer.size, 0L, a);
        while (a > 0L) {
            final Segment head = buffer.head;
            final int len = (int)Math.min(a, head.limit - head.pos);
            this.deflater.setInput(head.data, head.pos, len);
            this.deflate(false);
            buffer.size -= len;
            head.pos += len;
            if (head.pos == head.limit) {
                buffer.head = head.pop();
                SegmentPool.recycle(head);
            }
            a -= len;
        }
    }
}

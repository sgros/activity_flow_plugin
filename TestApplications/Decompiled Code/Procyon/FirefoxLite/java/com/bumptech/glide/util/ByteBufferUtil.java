// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.util;

import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public final class ByteBufferUtil
{
    private static final AtomicReference<byte[]> BUFFER_REF;
    
    static {
        BUFFER_REF = new AtomicReference<byte[]>();
    }
    
    public static ByteBuffer fromFile(final File p0) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: aload_0        
        //     3: invokevirtual   java/io/File.length:()J
        //     6: lstore_2       
        //     7: lload_2        
        //     8: ldc2_w          2147483647
        //    11: lcmp           
        //    12: ifgt            76
        //    15: new             Ljava/io/RandomAccessFile;
        //    18: astore          4
        //    20: aload           4
        //    22: aload_0        
        //    23: ldc             "r"
        //    25: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    28: aload           4
        //    30: invokevirtual   java/io/RandomAccessFile.getChannel:()Ljava/nio/channels/FileChannel;
        //    33: astore_0       
        //    34: aload_0        
        //    35: getstatic       java/nio/channels/FileChannel$MapMode.READ_ONLY:Ljava/nio/channels/FileChannel$MapMode;
        //    38: lconst_0       
        //    39: lload_2        
        //    40: invokevirtual   java/nio/channels/FileChannel.map:(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
        //    43: invokevirtual   java/nio/MappedByteBuffer.load:()Ljava/nio/MappedByteBuffer;
        //    46: astore_1       
        //    47: aload_0        
        //    48: ifnull          55
        //    51: aload_0        
        //    52: invokevirtual   java/nio/channels/FileChannel.close:()V
        //    55: aload           4
        //    57: invokevirtual   java/io/RandomAccessFile.close:()V
        //    60: aload_1        
        //    61: areturn        
        //    62: astore          5
        //    64: aload_0        
        //    65: astore_1       
        //    66: aload           5
        //    68: astore_0       
        //    69: goto            92
        //    72: astore_0       
        //    73: goto            92
        //    76: new             Ljava/io/IOException;
        //    79: astore_0       
        //    80: aload_0        
        //    81: ldc             "File too large to map into memory"
        //    83: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //    86: aload_0        
        //    87: athrow         
        //    88: astore_0       
        //    89: aconst_null    
        //    90: astore          4
        //    92: aload_1        
        //    93: ifnull          104
        //    96: aload_1        
        //    97: invokevirtual   java/nio/channels/FileChannel.close:()V
        //   100: goto            104
        //   103: astore_1       
        //   104: aload           4
        //   106: ifnull          114
        //   109: aload           4
        //   111: invokevirtual   java/io/RandomAccessFile.close:()V
        //   114: aload_0        
        //   115: athrow         
        //   116: astore_0       
        //   117: goto            55
        //   120: astore_0       
        //   121: goto            60
        //   124: astore          4
        //   126: goto            114
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  2      7      88     92     Any
        //  15     28     88     92     Any
        //  28     34     72     76     Any
        //  34     47     62     72     Any
        //  51     55     116    120    Ljava/io/IOException;
        //  55     60     120    124    Ljava/io/IOException;
        //  76     88     88     92     Any
        //  96     100    103    104    Ljava/io/IOException;
        //  109    114    124    129    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 69 out-of-bounds for length 69
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
    
    public static void toFile(final ByteBuffer p0, final File p1) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iconst_0       
        //     2: invokevirtual   java/nio/ByteBuffer.position:(I)Ljava/nio/Buffer;
        //     5: pop            
        //     6: new             Ljava/io/RandomAccessFile;
        //     9: astore_2       
        //    10: aload_2        
        //    11: aload_1        
        //    12: ldc             "rw"
        //    14: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    17: aload_2        
        //    18: invokevirtual   java/io/RandomAccessFile.getChannel:()Ljava/nio/channels/FileChannel;
        //    21: astore_1       
        //    22: aload_1        
        //    23: aload_0        
        //    24: invokevirtual   java/nio/channels/FileChannel.write:(Ljava/nio/ByteBuffer;)I
        //    27: pop            
        //    28: aload_1        
        //    29: iconst_0       
        //    30: invokevirtual   java/nio/channels/FileChannel.force:(Z)V
        //    33: aload_1        
        //    34: invokevirtual   java/nio/channels/FileChannel.close:()V
        //    37: aload_2        
        //    38: invokevirtual   java/io/RandomAccessFile.close:()V
        //    41: aload_1        
        //    42: ifnull          49
        //    45: aload_1        
        //    46: invokevirtual   java/nio/channels/FileChannel.close:()V
        //    49: aload_2        
        //    50: invokevirtual   java/io/RandomAccessFile.close:()V
        //    53: return         
        //    54: astore_0       
        //    55: goto            69
        //    58: astore_0       
        //    59: aconst_null    
        //    60: astore_1       
        //    61: goto            69
        //    64: astore_0       
        //    65: aconst_null    
        //    66: astore_1       
        //    67: aload_1        
        //    68: astore_2       
        //    69: aload_1        
        //    70: ifnull          81
        //    73: aload_1        
        //    74: invokevirtual   java/nio/channels/FileChannel.close:()V
        //    77: goto            81
        //    80: astore_1       
        //    81: aload_2        
        //    82: ifnull          89
        //    85: aload_2        
        //    86: invokevirtual   java/io/RandomAccessFile.close:()V
        //    89: aload_0        
        //    90: athrow         
        //    91: astore_0       
        //    92: goto            49
        //    95: astore_0       
        //    96: goto            53
        //    99: astore_1       
        //   100: goto            89
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  6      17     64     69     Any
        //  17     22     58     64     Any
        //  22     41     54     58     Any
        //  45     49     91     95     Ljava/io/IOException;
        //  49     53     95     99     Ljava/io/IOException;
        //  73     77     80     81     Ljava/io/IOException;
        //  85     89     99     103    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 60 out-of-bounds for length 60
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
    
    public static InputStream toStream(final ByteBuffer byteBuffer) {
        return new ByteBufferStream(byteBuffer);
    }
    
    private static class ByteBufferStream extends InputStream
    {
        private final ByteBuffer byteBuffer;
        private int markPos;
        
        public ByteBufferStream(final ByteBuffer byteBuffer) {
            this.markPos = -1;
            this.byteBuffer = byteBuffer;
        }
        
        @Override
        public int available() throws IOException {
            return this.byteBuffer.remaining();
        }
        
        @Override
        public void mark(final int n) {
            synchronized (this) {
                this.markPos = this.byteBuffer.position();
            }
        }
        
        @Override
        public boolean markSupported() {
            return true;
        }
        
        @Override
        public int read() throws IOException {
            if (!this.byteBuffer.hasRemaining()) {
                return -1;
            }
            return this.byteBuffer.get();
        }
        
        @Override
        public int read(final byte[] dst, final int offset, int min) throws IOException {
            if (!this.byteBuffer.hasRemaining()) {
                return -1;
            }
            min = Math.min(min, this.available());
            this.byteBuffer.get(dst, offset, min);
            return min;
        }
        
        @Override
        public void reset() throws IOException {
            synchronized (this) {
                if (this.markPos != -1) {
                    this.byteBuffer.position(this.markPos);
                    return;
                }
                throw new IOException("Cannot reset to unset mark position");
            }
        }
        
        @Override
        public long skip(long min) throws IOException {
            if (!this.byteBuffer.hasRemaining()) {
                return -1L;
            }
            min = Math.min(min, this.available());
            this.byteBuffer.position((int)(this.byteBuffer.position() + min));
            return min;
        }
    }
}

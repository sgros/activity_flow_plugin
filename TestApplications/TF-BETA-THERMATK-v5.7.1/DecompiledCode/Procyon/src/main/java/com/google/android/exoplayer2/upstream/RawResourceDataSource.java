// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.io.IOException;
import java.io.EOFException;
import java.io.FileInputStream;
import android.text.TextUtils;
import android.content.Context;
import android.net.Uri;
import android.content.res.Resources;
import java.io.InputStream;
import android.content.res.AssetFileDescriptor;

public final class RawResourceDataSource extends BaseDataSource
{
    private AssetFileDescriptor assetFileDescriptor;
    private long bytesRemaining;
    private InputStream inputStream;
    private boolean opened;
    private final Resources resources;
    private Uri uri;
    
    public RawResourceDataSource(final Context context) {
        super(false);
        this.resources = context.getResources();
    }
    
    @Override
    public void close() throws RawResourceDataSourceException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aconst_null    
        //     2: putfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.uri:Landroid/net/Uri;
        //     5: aload_0        
        //     6: getfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.inputStream:Ljava/io/InputStream;
        //     9: ifnull          19
        //    12: aload_0        
        //    13: getfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.inputStream:Ljava/io/InputStream;
        //    16: invokevirtual   java/io/InputStream.close:()V
        //    19: aload_0        
        //    20: aconst_null    
        //    21: putfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.inputStream:Ljava/io/InputStream;
        //    24: aload_0        
        //    25: getfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //    28: ifnull          38
        //    31: aload_0        
        //    32: getfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //    35: invokevirtual   android/content/res/AssetFileDescriptor.close:()V
        //    38: aload_0        
        //    39: aconst_null    
        //    40: putfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //    43: aload_0        
        //    44: getfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.opened:Z
        //    47: ifeq            59
        //    50: aload_0        
        //    51: iconst_0       
        //    52: putfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.opened:Z
        //    55: aload_0        
        //    56: invokevirtual   com/google/android/exoplayer2/upstream/BaseDataSource.transferEnded:()V
        //    59: return         
        //    60: astore_1       
        //    61: goto            76
        //    64: astore_2       
        //    65: new             Lcom/google/android/exoplayer2/upstream/RawResourceDataSource$RawResourceDataSourceException;
        //    68: astore_1       
        //    69: aload_1        
        //    70: aload_2        
        //    71: invokespecial   com/google/android/exoplayer2/upstream/RawResourceDataSource$RawResourceDataSourceException.<init>:(Ljava/io/IOException;)V
        //    74: aload_1        
        //    75: athrow         
        //    76: aload_0        
        //    77: aconst_null    
        //    78: putfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //    81: aload_0        
        //    82: getfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.opened:Z
        //    85: ifeq            97
        //    88: aload_0        
        //    89: iconst_0       
        //    90: putfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.opened:Z
        //    93: aload_0        
        //    94: invokevirtual   com/google/android/exoplayer2/upstream/BaseDataSource.transferEnded:()V
        //    97: aload_1        
        //    98: athrow         
        //    99: astore_1       
        //   100: goto            115
        //   103: astore_2       
        //   104: new             Lcom/google/android/exoplayer2/upstream/RawResourceDataSource$RawResourceDataSourceException;
        //   107: astore_1       
        //   108: aload_1        
        //   109: aload_2        
        //   110: invokespecial   com/google/android/exoplayer2/upstream/RawResourceDataSource$RawResourceDataSourceException.<init>:(Ljava/io/IOException;)V
        //   113: aload_1        
        //   114: athrow         
        //   115: aload_0        
        //   116: aconst_null    
        //   117: putfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.inputStream:Ljava/io/InputStream;
        //   120: aload_0        
        //   121: getfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //   124: ifnull          134
        //   127: aload_0        
        //   128: getfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //   131: invokevirtual   android/content/res/AssetFileDescriptor.close:()V
        //   134: aload_0        
        //   135: aconst_null    
        //   136: putfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //   139: aload_0        
        //   140: getfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.opened:Z
        //   143: ifeq            155
        //   146: aload_0        
        //   147: iconst_0       
        //   148: putfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.opened:Z
        //   151: aload_0        
        //   152: invokevirtual   com/google/android/exoplayer2/upstream/BaseDataSource.transferEnded:()V
        //   155: aload_1        
        //   156: athrow         
        //   157: astore_1       
        //   158: goto            173
        //   161: astore_1       
        //   162: new             Lcom/google/android/exoplayer2/upstream/RawResourceDataSource$RawResourceDataSourceException;
        //   165: astore_2       
        //   166: aload_2        
        //   167: aload_1        
        //   168: invokespecial   com/google/android/exoplayer2/upstream/RawResourceDataSource$RawResourceDataSourceException.<init>:(Ljava/io/IOException;)V
        //   171: aload_2        
        //   172: athrow         
        //   173: aload_0        
        //   174: aconst_null    
        //   175: putfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //   178: aload_0        
        //   179: getfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.opened:Z
        //   182: ifeq            194
        //   185: aload_0        
        //   186: iconst_0       
        //   187: putfield        com/google/android/exoplayer2/upstream/RawResourceDataSource.opened:Z
        //   190: aload_0        
        //   191: invokevirtual   com/google/android/exoplayer2/upstream/BaseDataSource.transferEnded:()V
        //   194: aload_1        
        //   195: athrow         
        //    Exceptions:
        //  throws com.google.android.exoplayer2.upstream.RawResourceDataSource.RawResourceDataSourceException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  5      19     103    115    Ljava/io/IOException;
        //  5      19     99     196    Any
        //  24     38     64     76     Ljava/io/IOException;
        //  24     38     60     99     Any
        //  65     76     60     99     Any
        //  104    115    99     196    Any
        //  120    134    161    173    Ljava/io/IOException;
        //  120    134    157    196    Any
        //  162    173    157    196    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0134:
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
    public Uri getUri() {
        return this.uri;
    }
    
    @Override
    public long open(final DataSpec dataSpec) throws RawResourceDataSourceException {
        try {
            this.uri = dataSpec.uri;
            Label_0215: {
                if (!TextUtils.equals((CharSequence)"rawresource", (CharSequence)this.uri.getScheme())) {
                    break Label_0215;
                }
                try {
                    final int int1 = Integer.parseInt(this.uri.getLastPathSegment());
                    this.transferInitializing(dataSpec);
                    this.assetFileDescriptor = this.resources.openRawResourceFd(int1);
                    (this.inputStream = new FileInputStream(this.assetFileDescriptor.getFileDescriptor())).skip(this.assetFileDescriptor.getStartOffset());
                    if (this.inputStream.skip(dataSpec.position) >= dataSpec.position) {
                        final long length = dataSpec.length;
                        long bytesRemaining = -1L;
                        if (length != -1L) {
                            this.bytesRemaining = dataSpec.length;
                        }
                        else {
                            final long length2 = this.assetFileDescriptor.getLength();
                            if (length2 != -1L) {
                                bytesRemaining = length2 - dataSpec.position;
                            }
                            this.bytesRemaining = bytesRemaining;
                        }
                        this.opened = true;
                        this.transferStarted(dataSpec);
                        return this.bytesRemaining;
                    }
                    throw new EOFException();
                }
                catch (NumberFormatException ex6) {
                    final RawResourceDataSourceException ex = new(com.google.android.exoplayer2.upstream.RawResourceDataSource.RawResourceDataSourceException.class);
                    final RawResourceDataSourceException ex3;
                    final RawResourceDataSourceException ex2 = ex3 = ex;
                    final String s = "Resource identifier must be an integer.";
                    new RawResourceDataSourceException(s);
                    throw ex2;
                }
                try {
                    final RawResourceDataSourceException ex = new(com.google.android.exoplayer2.upstream.RawResourceDataSource.RawResourceDataSourceException.class);
                    final RawResourceDataSourceException ex3;
                    final RawResourceDataSourceException ex2 = ex3 = ex;
                    final String s = "Resource identifier must be an integer.";
                    new RawResourceDataSourceException(s);
                    throw ex2;
                    throw new RawResourceDataSourceException("URI must use scheme rawresource");
                }
                catch (IOException ex5) {
                    throw new RawResourceDataSourceException(ex5);
                }
            }
        }
        catch (IOException ex7) {}
    }
    
    @Override
    public int read(final byte[] b, int read, int len) throws RawResourceDataSourceException {
        if (len == 0) {
            return 0;
        }
        final long bytesRemaining = this.bytesRemaining;
        if (bytesRemaining == 0L) {
            return -1;
        }
        Label_0046: {
            if (bytesRemaining == -1L) {
                break Label_0046;
            }
            final long b2 = len;
            try {
                len = (int)Math.min(bytesRemaining, b2);
                read = this.inputStream.read(b, read, len);
                if (read != -1) {
                    final long bytesRemaining2 = this.bytesRemaining;
                    if (bytesRemaining2 != -1L) {
                        this.bytesRemaining = bytesRemaining2 - read;
                    }
                    this.bytesTransferred(read);
                    return read;
                }
                if (this.bytesRemaining == -1L) {
                    return -1;
                }
                throw new RawResourceDataSourceException(new EOFException());
            }
            catch (IOException ex) {
                throw new RawResourceDataSourceException(ex);
            }
        }
    }
    
    public static class RawResourceDataSourceException extends IOException
    {
        public RawResourceDataSourceException(final IOException cause) {
            super(cause);
        }
        
        public RawResourceDataSourceException(final String message) {
            super(message);
        }
    }
}

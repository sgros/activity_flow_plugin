// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.nio.channels.FileChannel;
import java.io.IOException;
import java.io.EOFException;
import java.io.FileNotFoundException;
import android.content.Context;
import android.net.Uri;
import android.content.ContentResolver;
import java.io.FileInputStream;
import android.content.res.AssetFileDescriptor;

public final class ContentDataSource extends BaseDataSource
{
    private AssetFileDescriptor assetFileDescriptor;
    private long bytesRemaining;
    private FileInputStream inputStream;
    private boolean opened;
    private final ContentResolver resolver;
    private Uri uri;
    
    public ContentDataSource(final Context context) {
        super(false);
        this.resolver = context.getContentResolver();
    }
    
    @Override
    public void close() throws ContentDataSourceException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aconst_null    
        //     2: putfield        com/google/android/exoplayer2/upstream/ContentDataSource.uri:Landroid/net/Uri;
        //     5: aload_0        
        //     6: getfield        com/google/android/exoplayer2/upstream/ContentDataSource.inputStream:Ljava/io/FileInputStream;
        //     9: ifnull          19
        //    12: aload_0        
        //    13: getfield        com/google/android/exoplayer2/upstream/ContentDataSource.inputStream:Ljava/io/FileInputStream;
        //    16: invokevirtual   java/io/FileInputStream.close:()V
        //    19: aload_0        
        //    20: aconst_null    
        //    21: putfield        com/google/android/exoplayer2/upstream/ContentDataSource.inputStream:Ljava/io/FileInputStream;
        //    24: aload_0        
        //    25: getfield        com/google/android/exoplayer2/upstream/ContentDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //    28: ifnull          38
        //    31: aload_0        
        //    32: getfield        com/google/android/exoplayer2/upstream/ContentDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //    35: invokevirtual   android/content/res/AssetFileDescriptor.close:()V
        //    38: aload_0        
        //    39: aconst_null    
        //    40: putfield        com/google/android/exoplayer2/upstream/ContentDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //    43: aload_0        
        //    44: getfield        com/google/android/exoplayer2/upstream/ContentDataSource.opened:Z
        //    47: ifeq            59
        //    50: aload_0        
        //    51: iconst_0       
        //    52: putfield        com/google/android/exoplayer2/upstream/ContentDataSource.opened:Z
        //    55: aload_0        
        //    56: invokevirtual   com/google/android/exoplayer2/upstream/BaseDataSource.transferEnded:()V
        //    59: return         
        //    60: astore_1       
        //    61: goto            76
        //    64: astore_1       
        //    65: new             Lcom/google/android/exoplayer2/upstream/ContentDataSource$ContentDataSourceException;
        //    68: astore_2       
        //    69: aload_2        
        //    70: aload_1        
        //    71: invokespecial   com/google/android/exoplayer2/upstream/ContentDataSource$ContentDataSourceException.<init>:(Ljava/io/IOException;)V
        //    74: aload_2        
        //    75: athrow         
        //    76: aload_0        
        //    77: aconst_null    
        //    78: putfield        com/google/android/exoplayer2/upstream/ContentDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //    81: aload_0        
        //    82: getfield        com/google/android/exoplayer2/upstream/ContentDataSource.opened:Z
        //    85: ifeq            97
        //    88: aload_0        
        //    89: iconst_0       
        //    90: putfield        com/google/android/exoplayer2/upstream/ContentDataSource.opened:Z
        //    93: aload_0        
        //    94: invokevirtual   com/google/android/exoplayer2/upstream/BaseDataSource.transferEnded:()V
        //    97: aload_1        
        //    98: athrow         
        //    99: astore_1       
        //   100: goto            115
        //   103: astore_1       
        //   104: new             Lcom/google/android/exoplayer2/upstream/ContentDataSource$ContentDataSourceException;
        //   107: astore_2       
        //   108: aload_2        
        //   109: aload_1        
        //   110: invokespecial   com/google/android/exoplayer2/upstream/ContentDataSource$ContentDataSourceException.<init>:(Ljava/io/IOException;)V
        //   113: aload_2        
        //   114: athrow         
        //   115: aload_0        
        //   116: aconst_null    
        //   117: putfield        com/google/android/exoplayer2/upstream/ContentDataSource.inputStream:Ljava/io/FileInputStream;
        //   120: aload_0        
        //   121: getfield        com/google/android/exoplayer2/upstream/ContentDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //   124: ifnull          134
        //   127: aload_0        
        //   128: getfield        com/google/android/exoplayer2/upstream/ContentDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //   131: invokevirtual   android/content/res/AssetFileDescriptor.close:()V
        //   134: aload_0        
        //   135: aconst_null    
        //   136: putfield        com/google/android/exoplayer2/upstream/ContentDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //   139: aload_0        
        //   140: getfield        com/google/android/exoplayer2/upstream/ContentDataSource.opened:Z
        //   143: ifeq            155
        //   146: aload_0        
        //   147: iconst_0       
        //   148: putfield        com/google/android/exoplayer2/upstream/ContentDataSource.opened:Z
        //   151: aload_0        
        //   152: invokevirtual   com/google/android/exoplayer2/upstream/BaseDataSource.transferEnded:()V
        //   155: aload_1        
        //   156: athrow         
        //   157: astore_1       
        //   158: goto            173
        //   161: astore_2       
        //   162: new             Lcom/google/android/exoplayer2/upstream/ContentDataSource$ContentDataSourceException;
        //   165: astore_1       
        //   166: aload_1        
        //   167: aload_2        
        //   168: invokespecial   com/google/android/exoplayer2/upstream/ContentDataSource$ContentDataSourceException.<init>:(Ljava/io/IOException;)V
        //   171: aload_1        
        //   172: athrow         
        //   173: aload_0        
        //   174: aconst_null    
        //   175: putfield        com/google/android/exoplayer2/upstream/ContentDataSource.assetFileDescriptor:Landroid/content/res/AssetFileDescriptor;
        //   178: aload_0        
        //   179: getfield        com/google/android/exoplayer2/upstream/ContentDataSource.opened:Z
        //   182: ifeq            194
        //   185: aload_0        
        //   186: iconst_0       
        //   187: putfield        com/google/android/exoplayer2/upstream/ContentDataSource.opened:Z
        //   190: aload_0        
        //   191: invokevirtual   com/google/android/exoplayer2/upstream/BaseDataSource.transferEnded:()V
        //   194: aload_1        
        //   195: athrow         
        //    Exceptions:
        //  throws com.google.android.exoplayer2.upstream.ContentDataSource.ContentDataSourceException
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
    public long open(final DataSpec dataSpec) throws ContentDataSourceException {
        try {
            this.uri = dataSpec.uri;
            this.transferInitializing(dataSpec);
            this.assetFileDescriptor = this.resolver.openAssetFileDescriptor(this.uri, "r");
            if (this.assetFileDescriptor == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Could not open file descriptor for: ");
                sb.append(this.uri);
                throw new FileNotFoundException(sb.toString());
            }
            this.inputStream = new FileInputStream(this.assetFileDescriptor.getFileDescriptor());
            final long startOffset = this.assetFileDescriptor.getStartOffset();
            final long n = this.inputStream.skip(dataSpec.position + startOffset) - startOffset;
            if (n == dataSpec.position) {
                if (dataSpec.length != -1L) {
                    this.bytesRemaining = dataSpec.length;
                }
                else {
                    final long length = this.assetFileDescriptor.getLength();
                    if (length == -1L) {
                        final FileChannel channel = this.inputStream.getChannel();
                        final long size = channel.size();
                        long bytesRemaining;
                        if (size == 0L) {
                            bytesRemaining = -1L;
                        }
                        else {
                            bytesRemaining = size - channel.position();
                        }
                        this.bytesRemaining = bytesRemaining;
                    }
                    else {
                        this.bytesRemaining = length - n;
                    }
                }
                this.opened = true;
                this.transferStarted(dataSpec);
                return this.bytesRemaining;
            }
            throw new EOFException();
        }
        catch (IOException ex) {
            throw new ContentDataSourceException(ex);
        }
    }
    
    @Override
    public int read(final byte[] b, int read, int len) throws ContentDataSourceException {
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
                throw new ContentDataSourceException(new EOFException());
            }
            catch (IOException ex) {
                throw new ContentDataSourceException(ex);
            }
        }
    }
    
    public static class ContentDataSourceException extends IOException
    {
        public ContentDataSourceException(final IOException cause) {
            super(cause);
        }
    }
}

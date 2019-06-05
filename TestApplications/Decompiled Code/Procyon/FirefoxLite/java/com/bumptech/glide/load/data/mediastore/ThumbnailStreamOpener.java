// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data.mediastore;

import android.database.Cursor;
import java.io.File;
import java.io.FileNotFoundException;
import android.text.TextUtils;
import java.io.InputStream;
import android.net.Uri;
import com.bumptech.glide.load.ImageHeaderParser;
import java.util.List;
import android.content.ContentResolver;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;

class ThumbnailStreamOpener
{
    private static final FileService DEFAULT_SERVICE;
    private final ArrayPool byteArrayPool;
    private final ContentResolver contentResolver;
    private final List<ImageHeaderParser> parsers;
    private final ThumbnailQuery query;
    private final FileService service;
    
    static {
        DEFAULT_SERVICE = new FileService();
    }
    
    public ThumbnailStreamOpener(final List<ImageHeaderParser> parsers, final FileService service, final ThumbnailQuery query, final ArrayPool byteArrayPool, final ContentResolver contentResolver) {
        this.service = service;
        this.query = query;
        this.byteArrayPool = byteArrayPool;
        this.contentResolver = contentResolver;
        this.parsers = parsers;
    }
    
    public ThumbnailStreamOpener(final List<ImageHeaderParser> list, final ThumbnailQuery thumbnailQuery, final ArrayPool arrayPool, final ContentResolver contentResolver) {
        this(list, ThumbnailStreamOpener.DEFAULT_SERVICE, thumbnailQuery, arrayPool, contentResolver);
    }
    
    public int getOrientation(final Uri p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/bumptech/glide/load/data/mediastore/ThumbnailStreamOpener.contentResolver:Landroid/content/ContentResolver;
        //     4: aload_1        
        //     5: invokevirtual   android/content/ContentResolver.openInputStream:(Landroid/net/Uri;)Ljava/io/InputStream;
        //     8: astore_2       
        //     9: aload_2        
        //    10: astore_3       
        //    11: aload_0        
        //    12: getfield        com/bumptech/glide/load/data/mediastore/ThumbnailStreamOpener.parsers:Ljava/util/List;
        //    15: aload_2        
        //    16: aload_0        
        //    17: getfield        com/bumptech/glide/load/data/mediastore/ThumbnailStreamOpener.byteArrayPool:Lcom/bumptech/glide/load/engine/bitmap_recycle/ArrayPool;
        //    20: invokestatic    com/bumptech/glide/load/ImageHeaderParserUtils.getOrientation:(Ljava/util/List;Ljava/io/InputStream;Lcom/bumptech/glide/load/engine/bitmap_recycle/ArrayPool;)I
        //    23: istore          4
        //    25: aload_2        
        //    26: ifnull          33
        //    29: aload_2        
        //    30: invokevirtual   java/io/InputStream.close:()V
        //    33: iload           4
        //    35: ireturn        
        //    36: astore          5
        //    38: goto            51
        //    41: astore_1       
        //    42: aconst_null    
        //    43: astore_3       
        //    44: goto            121
        //    47: astore          5
        //    49: aconst_null    
        //    50: astore_2       
        //    51: aload_2        
        //    52: astore_3       
        //    53: ldc             "ThumbStreamOpener"
        //    55: iconst_3       
        //    56: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //    59: ifeq            110
        //    62: aload_2        
        //    63: astore_3       
        //    64: new             Ljava/lang/StringBuilder;
        //    67: astore          6
        //    69: aload_2        
        //    70: astore_3       
        //    71: aload           6
        //    73: invokespecial   java/lang/StringBuilder.<init>:()V
        //    76: aload_2        
        //    77: astore_3       
        //    78: aload           6
        //    80: ldc             "Failed to open uri: "
        //    82: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    85: pop            
        //    86: aload_2        
        //    87: astore_3       
        //    88: aload           6
        //    90: aload_1        
        //    91: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    94: pop            
        //    95: aload_2        
        //    96: astore_3       
        //    97: ldc             "ThumbStreamOpener"
        //    99: aload           6
        //   101: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   104: aload           5
        //   106: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   109: pop            
        //   110: aload_2        
        //   111: ifnull          118
        //   114: aload_2        
        //   115: invokevirtual   java/io/InputStream.close:()V
        //   118: iconst_m1      
        //   119: ireturn        
        //   120: astore_1       
        //   121: aload_3        
        //   122: ifnull          129
        //   125: aload_3        
        //   126: invokevirtual   java/io/InputStream.close:()V
        //   129: aload_1        
        //   130: athrow         
        //   131: astore_1       
        //   132: goto            33
        //   135: astore_1       
        //   136: goto            118
        //   139: astore_3       
        //   140: goto            129
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  0      9      47     51     Ljava/io/IOException;
        //  0      9      47     51     Ljava/lang/NullPointerException;
        //  0      9      41     47     Any
        //  11     25     36     41     Ljava/io/IOException;
        //  11     25     36     41     Ljava/lang/NullPointerException;
        //  11     25     120    121    Any
        //  29     33     131    135    Ljava/io/IOException;
        //  53     62     120    121    Any
        //  64     69     120    121    Any
        //  71     76     120    121    Any
        //  78     86     120    121    Any
        //  88     95     120    121    Any
        //  97     110    120    121    Any
        //  114    118    135    139    Ljava/io/IOException;
        //  125    129    139    143    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 82 out-of-bounds for length 82
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
    
    public InputStream open(Uri fromFile) throws FileNotFoundException {
        Object query = this.query.query(fromFile);
        InputStream openInputStream = null;
        if (query != null) {
            try {
                if (((Cursor)query).moveToFirst()) {
                    final String string = ((Cursor)query).getString(0);
                    if (TextUtils.isEmpty((CharSequence)string)) {
                        return null;
                    }
                    final File value = this.service.get(string);
                    if (this.service.exists(value) && this.service.length(value) > 0L) {
                        fromFile = Uri.fromFile(value);
                    }
                    else {
                        fromFile = null;
                    }
                    if (query != null) {
                        ((Cursor)query).close();
                    }
                    if (fromFile != null) {
                        try {
                            openInputStream = this.contentResolver.openInputStream(fromFile);
                        }
                        catch (NullPointerException cause) {
                            query = new StringBuilder();
                            ((StringBuilder)query).append("NPE opening uri: ");
                            ((StringBuilder)query).append(fromFile);
                            throw (FileNotFoundException)new FileNotFoundException(((StringBuilder)query).toString()).initCause(cause);
                        }
                    }
                    return openInputStream;
                }
            }
            finally {
                if (query != null) {
                    ((Cursor)query).close();
                }
            }
        }
        if (query != null) {
            ((Cursor)query).close();
        }
        return null;
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.EncodeStrategy;
import java.io.File;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.Options;
import android.graphics.Bitmap$CompressFormat;
import com.bumptech.glide.load.Option;
import android.graphics.Bitmap;
import com.bumptech.glide.load.ResourceEncoder;

public class BitmapEncoder implements ResourceEncoder<Bitmap>
{
    public static final Option<Bitmap$CompressFormat> COMPRESSION_FORMAT;
    public static final Option<Integer> COMPRESSION_QUALITY;
    
    static {
        COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", 90);
        COMPRESSION_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat");
    }
    
    private Bitmap$CompressFormat getFormat(final Bitmap bitmap, final Options options) {
        final Bitmap$CompressFormat bitmap$CompressFormat = options.get(BitmapEncoder.COMPRESSION_FORMAT);
        if (bitmap$CompressFormat != null) {
            return bitmap$CompressFormat;
        }
        if (bitmap.hasAlpha()) {
            return Bitmap$CompressFormat.PNG;
        }
        return Bitmap$CompressFormat.JPEG;
    }
    
    @Override
    public boolean encode(final Resource<Bitmap> p0, final File p1, final Options p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokeinterface com/bumptech/glide/load/engine/Resource.get:()Ljava/lang/Object;
        //     6: checkcast       Landroid/graphics/Bitmap;
        //     9: astore          4
        //    11: aload_0        
        //    12: aload           4
        //    14: aload_3        
        //    15: invokespecial   com/bumptech/glide/load/resource/bitmap/BitmapEncoder.getFormat:(Landroid/graphics/Bitmap;Lcom/bumptech/glide/load/Options;)Landroid/graphics/Bitmap$CompressFormat;
        //    18: astore          5
        //    20: new             Ljava/lang/StringBuilder;
        //    23: dup            
        //    24: invokespecial   java/lang/StringBuilder.<init>:()V
        //    27: astore_1       
        //    28: aload_1        
        //    29: ldc             "encode: ["
        //    31: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    34: pop            
        //    35: aload_1        
        //    36: aload           4
        //    38: invokevirtual   android/graphics/Bitmap.getWidth:()I
        //    41: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    44: pop            
        //    45: aload_1        
        //    46: ldc             "x"
        //    48: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    51: pop            
        //    52: aload_1        
        //    53: aload           4
        //    55: invokevirtual   android/graphics/Bitmap.getHeight:()I
        //    58: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    61: pop            
        //    62: aload_1        
        //    63: ldc             "] "
        //    65: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    68: pop            
        //    69: aload_1        
        //    70: aload           5
        //    72: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    75: pop            
        //    76: aload_1        
        //    77: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    80: invokestatic    android/support/v4/os/TraceCompat.beginSection:(Ljava/lang/String;)V
        //    83: invokestatic    com/bumptech/glide/util/LogTime.getLogTime:()J
        //    86: lstore          6
        //    88: aload_3        
        //    89: getstatic       com/bumptech/glide/load/resource/bitmap/BitmapEncoder.COMPRESSION_QUALITY:Lcom/bumptech/glide/load/Option;
        //    92: invokevirtual   com/bumptech/glide/load/Options.get:(Lcom/bumptech/glide/load/Option;)Ljava/lang/Object;
        //    95: checkcast       Ljava/lang/Integer;
        //    98: invokevirtual   java/lang/Integer.intValue:()I
        //   101: istore          8
        //   103: iconst_0       
        //   104: istore          9
        //   106: iconst_0       
        //   107: istore          10
        //   109: aconst_null    
        //   110: astore          11
        //   112: aconst_null    
        //   113: astore          12
        //   115: aload           12
        //   117: astore_1       
        //   118: new             Ljava/io/FileOutputStream;
        //   121: astore          13
        //   123: aload           12
        //   125: astore_1       
        //   126: aload           13
        //   128: aload_2        
        //   129: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   132: aload           4
        //   134: aload           5
        //   136: iload           8
        //   138: aload           13
        //   140: invokevirtual   android/graphics/Bitmap.compress:(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
        //   143: pop            
        //   144: aload           13
        //   146: invokevirtual   java/io/OutputStream.close:()V
        //   149: iconst_1       
        //   150: istore          14
        //   152: iconst_1       
        //   153: istore          10
        //   155: aload           13
        //   157: invokevirtual   java/io/OutputStream.close:()V
        //   160: iload           10
        //   162: istore          14
        //   164: goto            236
        //   167: astore_2       
        //   168: aload           13
        //   170: astore_1       
        //   171: goto            353
        //   174: astore_1       
        //   175: aload           13
        //   177: astore_2       
        //   178: aload_1        
        //   179: astore          13
        //   181: goto            193
        //   184: astore_2       
        //   185: goto            353
        //   188: astore          13
        //   190: aload           11
        //   192: astore_2       
        //   193: aload_2        
        //   194: astore_1       
        //   195: ldc             "BitmapEncoder"
        //   197: iconst_3       
        //   198: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //   201: ifeq            216
        //   204: aload_2        
        //   205: astore_1       
        //   206: ldc             "BitmapEncoder"
        //   208: ldc             "Failed to encode Bitmap"
        //   210: aload           13
        //   212: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   215: pop            
        //   216: iload           10
        //   218: istore          14
        //   220: aload_2        
        //   221: ifnull          236
        //   224: iload           9
        //   226: istore          14
        //   228: aload_2        
        //   229: invokevirtual   java/io/OutputStream.close:()V
        //   232: iload           10
        //   234: istore          14
        //   236: ldc             "BitmapEncoder"
        //   238: iconst_2       
        //   239: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //   242: ifeq            347
        //   245: new             Ljava/lang/StringBuilder;
        //   248: astore_1       
        //   249: aload_1        
        //   250: invokespecial   java/lang/StringBuilder.<init>:()V
        //   253: aload_1        
        //   254: ldc             "Compressed with type: "
        //   256: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   259: pop            
        //   260: aload_1        
        //   261: aload           5
        //   263: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   266: pop            
        //   267: aload_1        
        //   268: ldc             " of size "
        //   270: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   273: pop            
        //   274: aload_1        
        //   275: aload           4
        //   277: invokestatic    com/bumptech/glide/util/Util.getBitmapByteSize:(Landroid/graphics/Bitmap;)I
        //   280: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   283: pop            
        //   284: aload_1        
        //   285: ldc             " in "
        //   287: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   290: pop            
        //   291: aload_1        
        //   292: lload           6
        //   294: invokestatic    com/bumptech/glide/util/LogTime.getElapsedMillis:(J)D
        //   297: invokevirtual   java/lang/StringBuilder.append:(D)Ljava/lang/StringBuilder;
        //   300: pop            
        //   301: aload_1        
        //   302: ldc             ", options format: "
        //   304: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   307: pop            
        //   308: aload_1        
        //   309: aload_3        
        //   310: getstatic       com/bumptech/glide/load/resource/bitmap/BitmapEncoder.COMPRESSION_FORMAT:Lcom/bumptech/glide/load/Option;
        //   313: invokevirtual   com/bumptech/glide/load/Options.get:(Lcom/bumptech/glide/load/Option;)Ljava/lang/Object;
        //   316: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   319: pop            
        //   320: aload_1        
        //   321: ldc             ", hasAlpha: "
        //   323: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   326: pop            
        //   327: aload_1        
        //   328: aload           4
        //   330: invokevirtual   android/graphics/Bitmap.hasAlpha:()Z
        //   333: invokevirtual   java/lang/StringBuilder.append:(Z)Ljava/lang/StringBuilder;
        //   336: pop            
        //   337: ldc             "BitmapEncoder"
        //   339: aload_1        
        //   340: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   343: invokestatic    android/util/Log.v:(Ljava/lang/String;Ljava/lang/String;)I
        //   346: pop            
        //   347: invokestatic    android/support/v4/os/TraceCompat.endSection:()V
        //   350: iload           14
        //   352: ireturn        
        //   353: aload_1        
        //   354: ifnull          361
        //   357: aload_1        
        //   358: invokevirtual   java/io/OutputStream.close:()V
        //   361: aload_2        
        //   362: athrow         
        //   363: astore_1       
        //   364: invokestatic    android/support/v4/os/TraceCompat.endSection:()V
        //   367: aload_1        
        //   368: athrow         
        //   369: astore_1       
        //   370: goto            236
        //   373: astore_1       
        //   374: goto            361
        //    Signature:
        //  (Lcom/bumptech/glide/load/engine/Resource<Landroid/graphics/Bitmap;>;Ljava/io/File;Lcom/bumptech/glide/load/Options;)Z
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  83     103    363    369    Any
        //  118    123    188    193    Ljava/io/IOException;
        //  118    123    184    188    Any
        //  126    132    188    193    Ljava/io/IOException;
        //  126    132    184    188    Any
        //  132    149    174    184    Ljava/io/IOException;
        //  132    149    167    174    Any
        //  155    160    369    373    Ljava/io/IOException;
        //  155    160    363    369    Any
        //  195    204    184    188    Any
        //  206    216    184    188    Any
        //  228    232    369    373    Ljava/io/IOException;
        //  228    232    363    369    Any
        //  236    347    363    369    Any
        //  357    361    373    377    Ljava/io/IOException;
        //  357    361    363    369    Any
        //  361    363    363    369    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 197 out-of-bounds for length 197
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
    
    @Override
    public EncodeStrategy getEncodeStrategy(final Options options) {
        return EncodeStrategy.TRANSFORMED;
    }
}

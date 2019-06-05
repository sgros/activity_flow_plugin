// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.utils;

import java.io.IOException;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.DataReaderBigEndian;
import android.graphics.Bitmap$CompressFormat;
import locus.api.utils.Logger;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

public class UtilsBitmap
{
    private static final String TAG = "UtilsBitmap";
    
    public static Bitmap getBitmap(byte[] decodeByteArray) {
        try {
            decodeByteArray = BitmapFactory.decodeByteArray((byte[])decodeByteArray, 0, decodeByteArray.length);
            return (Bitmap)decodeByteArray;
        }
        catch (Exception ex) {
            Logger.logE("UtilsBitmap", "getBitmap(" + decodeByteArray + ")", ex);
            decodeByteArray = null;
            return (Bitmap)decodeByteArray;
        }
    }
    
    public static byte[] getBitmap(final Bitmap p0, final Bitmap$CompressFormat p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2       
        //     2: aconst_null    
        //     3: astore_3       
        //     4: aconst_null    
        //     5: astore          4
        //     7: aload_3        
        //     8: astore          5
        //    10: new             Ljava/io/ByteArrayOutputStream;
        //    13: astore          6
        //    15: aload_3        
        //    16: astore          5
        //    18: aload           6
        //    20: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //    23: aload_0        
        //    24: aload_1        
        //    25: bipush          80
        //    27: aload           6
        //    29: invokevirtual   android/graphics/Bitmap.compress:(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
        //    32: ifeq            50
        //    35: aload           6
        //    37: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
        //    40: astore_1       
        //    41: aload_1        
        //    42: astore_0       
        //    43: aload           6
        //    45: invokestatic    locus/api/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //    48: aload_0        
        //    49: areturn        
        //    50: ldc             "UtilsBitmap"
        //    52: ldc             "Problem with converting image to byte[]"
        //    54: invokestatic    locus/api/utils/Logger.logW:(Ljava/lang/String;Ljava/lang/String;)V
        //    57: aload           6
        //    59: invokestatic    locus/api/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //    62: aload_2        
        //    63: astore_0       
        //    64: goto            48
        //    67: astore          6
        //    69: aload           4
        //    71: astore_1       
        //    72: aload_1        
        //    73: astore          5
        //    75: new             Ljava/lang/StringBuilder;
        //    78: astore          4
        //    80: aload_1        
        //    81: astore          5
        //    83: aload           4
        //    85: invokespecial   java/lang/StringBuilder.<init>:()V
        //    88: aload_1        
        //    89: astore          5
        //    91: ldc             "UtilsBitmap"
        //    93: aload           4
        //    95: ldc             "getBitmap("
        //    97: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   100: aload_0        
        //   101: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   104: ldc             ")"
        //   106: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   109: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   112: aload           6
        //   114: invokestatic    locus/api/utils/Logger.logE:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Exception;)V
        //   117: aload_1        
        //   118: invokestatic    locus/api/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   121: aload_2        
        //   122: astore_0       
        //   123: goto            48
        //   126: astore_0       
        //   127: aload           5
        //   129: invokestatic    locus/api/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   132: aload_0        
        //   133: athrow         
        //   134: astore_0       
        //   135: aload           6
        //   137: astore          5
        //   139: goto            127
        //   142: astore          5
        //   144: aload           6
        //   146: astore_1       
        //   147: aload           5
        //   149: astore          6
        //   151: goto            72
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  10     15     67     72     Ljava/lang/Exception;
        //  10     15     126    127    Any
        //  18     23     67     72     Ljava/lang/Exception;
        //  18     23     126    127    Any
        //  23     41     142    154    Ljava/lang/Exception;
        //  23     41     134    142    Any
        //  50     57     142    154    Ljava/lang/Exception;
        //  50     57     134    142    Any
        //  75     80     126    127    Any
        //  83     88     126    127    Any
        //  91     117    126    127    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0048:
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
    
    public static Bitmap readBitmap(final DataReaderBigEndian dataReaderBigEndian) {
        final int int1 = dataReaderBigEndian.readInt();
        Bitmap bitmap;
        if (int1 > 0) {
            bitmap = getBitmap(dataReaderBigEndian.readBytes(int1));
        }
        else {
            bitmap = null;
        }
        return bitmap;
    }
    
    public static void writeBitmap(final DataWriterBigEndian dataWriterBigEndian, final Bitmap bitmap, final Bitmap$CompressFormat bitmap$CompressFormat) throws IOException {
        if (bitmap == null) {
            dataWriterBigEndian.writeInt(0);
        }
        else {
            final byte[] bitmap2 = getBitmap(bitmap, bitmap$CompressFormat);
            if (bitmap2 == null || bitmap2.length == 0) {
                Logger.logW("UtilsBitmap", "writeBitmap(), unknown problem");
                dataWriterBigEndian.writeInt(0);
            }
            else {
                dataWriterBigEndian.writeInt(bitmap2.length);
                dataWriterBigEndian.write(bitmap2);
            }
        }
    }
}

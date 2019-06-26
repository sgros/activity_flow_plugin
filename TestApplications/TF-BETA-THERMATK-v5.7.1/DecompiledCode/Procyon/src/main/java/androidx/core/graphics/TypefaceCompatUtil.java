// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.graphics;

import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Process;
import android.os.StrictMode$ThreadPolicy;
import android.util.Log;
import java.io.FileOutputStream;
import android.os.StrictMode;
import java.io.InputStream;
import java.io.File;
import java.nio.ByteBuffer;
import android.content.res.Resources;
import android.content.Context;
import java.io.IOException;
import java.io.Closeable;

public class TypefaceCompatUtil
{
    public static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (IOException ex) {}
    }
    
    public static ByteBuffer copyToDirectBuffer(Context tempFile, final Resources resources, final int n) {
        tempFile = (Context)getTempFile(tempFile);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!copyToFile((File)tempFile, resources, n)) {
                return null;
            }
            return mmap((File)tempFile);
        }
        finally {
            ((File)tempFile).delete();
        }
    }
    
    public static boolean copyToFile(final File file, final Resources resources, final int n) {
        Closeable closeable;
        try {
            final InputStream openRawResource = resources.openRawResource(n);
            try {
                final boolean copyToFile = copyToFile(file, openRawResource);
                closeQuietly(openRawResource);
                return copyToFile;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        closeQuietly(closeable);
    }
    
    public static boolean copyToFile(final File file, final InputStream ex) {
        final StrictMode$ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        final Closeable closeable = null;
        Closeable closeable2 = null;
        Closeable closeable3;
        try {
            try {
                closeable2 = closeable2;
                final FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                try {
                    final byte[] array = new byte[1024];
                    while (true) {
                        final int read = ((InputStream)ex).read(array);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream.write(array, 0, read);
                    }
                    closeQuietly(fileOutputStream);
                    StrictMode.setThreadPolicy(allowThreadDiskWrites);
                    return true;
                }
                catch (IOException ex) {}
                finally {
                    closeable2 = fileOutputStream;
                }
            }
            finally {}
        }
        catch (IOException ex) {
            closeable3 = closeable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Error copying resource contents to temp file: ");
        sb.append(ex.getMessage());
        Log.e("TypefaceCompatUtil", sb.toString());
        closeQuietly(closeable3);
        StrictMode.setThreadPolicy(allowThreadDiskWrites);
        return false;
        closeQuietly(closeable2);
        StrictMode.setThreadPolicy(allowThreadDiskWrites);
    }
    
    public static File getTempFile(Context cacheDir) {
        cacheDir = (Context)cacheDir.getCacheDir();
        if (cacheDir == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(".font");
        sb.append(Process.myPid());
        sb.append("-");
        sb.append(Process.myTid());
        sb.append("-");
        final String string = sb.toString();
        int i = 0;
    Label_0115_Outer:
        while (true) {
            Label_0121: {
                if (i >= 100) {
                    break Label_0121;
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append(i);
                final File file = new File((File)cacheDir, sb2.toString());
                while (true) {
                    try {
                        if (file.createNewFile()) {
                            return file;
                        }
                        ++i;
                        continue Label_0115_Outer;
                        return null;
                    }
                    catch (IOException ex) {
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    public static ByteBuffer mmap(final Context p0, final CancellationSignal p1, final Uri p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //     4: astore_0       
        //     5: aload_0        
        //     6: aload_2        
        //     7: ldc             "r"
        //     9: aload_1        
        //    10: invokevirtual   android/content/ContentResolver.openFileDescriptor:(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;
        //    13: astore_2       
        //    14: aload_2        
        //    15: ifnonnull       28
        //    18: aload_2        
        //    19: ifnull          26
        //    22: aload_2        
        //    23: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //    26: aconst_null    
        //    27: areturn        
        //    28: new             Ljava/io/FileInputStream;
        //    31: astore_3       
        //    32: aload_3        
        //    33: aload_2        
        //    34: invokevirtual   android/os/ParcelFileDescriptor.getFileDescriptor:()Ljava/io/FileDescriptor;
        //    37: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/FileDescriptor;)V
        //    40: aload_3        
        //    41: invokevirtual   java/io/FileInputStream.getChannel:()Ljava/nio/channels/FileChannel;
        //    44: astore_0       
        //    45: aload_0        
        //    46: invokevirtual   java/nio/channels/FileChannel.size:()J
        //    49: lstore          4
        //    51: aload_0        
        //    52: getstatic       java/nio/channels/FileChannel$MapMode.READ_ONLY:Ljava/nio/channels/FileChannel$MapMode;
        //    55: lconst_0       
        //    56: lload           4
        //    58: invokevirtual   java/nio/channels/FileChannel.map:(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
        //    61: astore_0       
        //    62: aload_3        
        //    63: invokevirtual   java/io/FileInputStream.close:()V
        //    66: aload_2        
        //    67: ifnull          74
        //    70: aload_2        
        //    71: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //    74: aload_0        
        //    75: areturn        
        //    76: astore_0       
        //    77: aconst_null    
        //    78: astore_1       
        //    79: goto            86
        //    82: astore_1       
        //    83: aload_1        
        //    84: athrow         
        //    85: astore_0       
        //    86: aload_1        
        //    87: ifnull          97
        //    90: aload_3        
        //    91: invokevirtual   java/io/FileInputStream.close:()V
        //    94: goto            101
        //    97: aload_3        
        //    98: invokevirtual   java/io/FileInputStream.close:()V
        //   101: aload_0        
        //   102: athrow         
        //   103: astore_0       
        //   104: aconst_null    
        //   105: astore_1       
        //   106: goto            113
        //   109: astore_1       
        //   110: aload_1        
        //   111: athrow         
        //   112: astore_0       
        //   113: aload_2        
        //   114: ifnull          132
        //   117: aload_1        
        //   118: ifnull          128
        //   121: aload_2        
        //   122: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //   125: goto            132
        //   128: aload_2        
        //   129: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //   132: aload_0        
        //   133: athrow         
        //   134: astore_0       
        //   135: aconst_null    
        //   136: areturn        
        //   137: astore_1       
        //   138: goto            101
        //   141: astore_1       
        //   142: goto            132
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  5      14     134    137    Ljava/io/IOException;
        //  22     26     134    137    Ljava/io/IOException;
        //  28     40     109    113    Ljava/lang/Throwable;
        //  28     40     103    109    Any
        //  40     62     82     86     Ljava/lang/Throwable;
        //  40     62     76     82     Any
        //  62     66     109    113    Ljava/lang/Throwable;
        //  62     66     103    109    Any
        //  70     74     134    137    Ljava/io/IOException;
        //  83     85     85     86     Any
        //  90     94     137    141    Ljava/lang/Throwable;
        //  90     94     103    109    Any
        //  97     101    109    113    Ljava/lang/Throwable;
        //  97     101    103    109    Any
        //  101    103    109    113    Ljava/lang/Throwable;
        //  101    103    103    109    Any
        //  110    112    112    113    Any
        //  121    125    141    145    Ljava/lang/Throwable;
        //  128    132    134    137    Ljava/io/IOException;
        //  132    134    134    137    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 86 out-of-bounds for length 86
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
    
    private static ByteBuffer mmap(final File p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore_1       
        //     4: aload_1        
        //     5: aload_0        
        //     6: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //     9: aload_1        
        //    10: invokevirtual   java/io/FileInputStream.getChannel:()Ljava/nio/channels/FileChannel;
        //    13: astore_0       
        //    14: aload_0        
        //    15: invokevirtual   java/nio/channels/FileChannel.size:()J
        //    18: lstore_2       
        //    19: aload_0        
        //    20: getstatic       java/nio/channels/FileChannel$MapMode.READ_ONLY:Ljava/nio/channels/FileChannel$MapMode;
        //    23: lconst_0       
        //    24: lload_2        
        //    25: invokevirtual   java/nio/channels/FileChannel.map:(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
        //    28: astore_0       
        //    29: aload_1        
        //    30: invokevirtual   java/io/FileInputStream.close:()V
        //    33: aload_0        
        //    34: areturn        
        //    35: astore_0       
        //    36: aconst_null    
        //    37: astore          4
        //    39: goto            48
        //    42: astore          4
        //    44: aload           4
        //    46: athrow         
        //    47: astore_0       
        //    48: aload           4
        //    50: ifnull          60
        //    53: aload_1        
        //    54: invokevirtual   java/io/FileInputStream.close:()V
        //    57: goto            64
        //    60: aload_1        
        //    61: invokevirtual   java/io/FileInputStream.close:()V
        //    64: aload_0        
        //    65: athrow         
        //    66: astore_0       
        //    67: aconst_null    
        //    68: areturn        
        //    69: astore          4
        //    71: goto            64
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      9      66     69     Ljava/io/IOException;
        //  9      29     42     48     Ljava/lang/Throwable;
        //  9      29     35     42     Any
        //  29     33     66     69     Ljava/io/IOException;
        //  44     47     47     48     Any
        //  53     57     69     74     Ljava/lang/Throwable;
        //  60     64     66     69     Ljava/io/IOException;
        //  64     66     66     69     Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0060:
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
}

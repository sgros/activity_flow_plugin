// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Build$VERSION;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Closeable;
import java.io.FilenameFilter;
import java.io.File;

class ReLinker
{
    private static final int COPY_BUFFER_SIZE = 8192;
    private static final String LIB_DIR = "lib";
    private static final String MAPPED_BASE_LIB_NAME;
    private static final int MAX_TRIES = 5;
    
    static {
        MAPPED_BASE_LIB_NAME = System.mapLibraryName("pl_droidsonroids_gif");
    }
    
    private ReLinker() {
    }
    
    private static void clearOldLibraryFiles(final File file, final FilenameFilter filter) {
        final File[] listFiles = file.getParentFile().listFiles(filter);
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                listFiles[i].delete();
            }
        }
    }
    
    private static void closeSilently(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (IOException ex) {}
    }
    
    private static void copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final byte[] array = new byte[8192];
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            outputStream.write(array, 0, read);
        }
    }
    
    private static ZipEntry findLibraryEntry(final ZipFile zipFile) {
        final String[] supportedABIs = getSupportedABIs();
        for (int length = supportedABIs.length, i = 0; i < length; ++i) {
            final ZipEntry entry = getEntry(zipFile, supportedABIs[i]);
            if (entry != null) {
                return entry;
            }
        }
        return null;
    }
    
    private static ZipEntry getEntry(final ZipFile zipFile, final String str) {
        return zipFile.getEntry("lib/" + str + "/" + ReLinker.MAPPED_BASE_LIB_NAME);
    }
    
    private static String[] getSupportedABIs() {
        String[] supported_ABIS;
        if (Build$VERSION.SDK_INT >= 21) {
            supported_ABIS = Build.SUPPORTED_ABIS;
        }
        else {
            supported_ABIS = new String[] { Build.CPU_ABI, Build.CPU_ABI2 };
        }
        return supported_ABIS;
    }
    
    @SuppressLint({ "UnsafeDynamicallyLoadedCode" })
    static void loadLibrary(final Context context) {
        synchronized (ReLinker.class) {
            System.load(unpackLibrary(context).getAbsolutePath());
        }
    }
    
    private static ZipFile openZipFile(final File file) {
        final ZipFile zipFile = null;
        int n = 0;
        ZipFile zipFile2 = null;
    Label_0021_Outer:
        while (true) {
            zipFile2 = zipFile;
            while (true) {
                if (n < 5) {
                    try {
                        zipFile2 = new ZipFile(file, 1);
                        if (zipFile2 == null) {
                            throw new IllegalStateException("Could not open APK file: " + file.getAbsolutePath());
                        }
                    }
                    catch (IOException ex) {
                        ++n;
                        continue Label_0021_Outer;
                    }
                    break;
                }
                continue;
            }
        }
        return zipFile2;
    }
    
    @SuppressLint({ "SetWorldReadable" })
    private static void setFilePermissions(final File file) {
        file.setReadable(true, false);
        file.setExecutable(true, false);
        file.setWritable(true);
    }
    
    private static File unpackLibrary(final Context p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/lang/StringBuilder.<init>:()V
        //     7: getstatic       pl/droidsonroids/gif/ReLinker.MAPPED_BASE_LIB_NAME:Ljava/lang/String;
        //    10: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    13: ldc             "1.2.6"
        //    15: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    18: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    21: astore_1       
        //    22: new             Ljava/io/File;
        //    25: dup            
        //    26: aload_0        
        //    27: ldc             "lib"
        //    29: iconst_0       
        //    30: invokevirtual   android/content/Context.getDir:(Ljava/lang/String;I)Ljava/io/File;
        //    33: aload_1        
        //    34: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    37: astore_2       
        //    38: aload_2        
        //    39: invokevirtual   java/io/File.isFile:()Z
        //    42: ifeq            49
        //    45: aload_2        
        //    46: astore_1       
        //    47: aload_1        
        //    48: areturn        
        //    49: new             Ljava/io/File;
        //    52: dup            
        //    53: aload_0        
        //    54: invokevirtual   android/content/Context.getCacheDir:()Ljava/io/File;
        //    57: aload_1        
        //    58: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    61: astore_3       
        //    62: aload_3        
        //    63: astore_1       
        //    64: aload_3        
        //    65: invokevirtual   java/io/File.isFile:()Z
        //    68: ifne            47
        //    71: new             Lpl/droidsonroids/gif/ReLinker$1;
        //    74: dup            
        //    75: ldc             "pl_droidsonroids_gif_surface"
        //    77: invokestatic    java/lang/System.mapLibraryName:(Ljava/lang/String;)Ljava/lang/String;
        //    80: invokespecial   pl/droidsonroids/gif/ReLinker$1.<init>:(Ljava/lang/String;)V
        //    83: astore_1       
        //    84: aload_2        
        //    85: aload_1        
        //    86: invokestatic    pl/droidsonroids/gif/ReLinker.clearOldLibraryFiles:(Ljava/io/File;Ljava/io/FilenameFilter;)V
        //    89: aload_3        
        //    90: aload_1        
        //    91: invokestatic    pl/droidsonroids/gif/ReLinker.clearOldLibraryFiles:(Ljava/io/File;Ljava/io/FilenameFilter;)V
        //    94: new             Ljava/io/File;
        //    97: dup            
        //    98: aload_0        
        //    99: invokevirtual   android/content/Context.getApplicationInfo:()Landroid/content/pm/ApplicationInfo;
        //   102: getfield        android/content/pm/ApplicationInfo.sourceDir:Ljava/lang/String;
        //   105: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   108: astore_0       
        //   109: aconst_null    
        //   110: astore          4
        //   112: aload_0        
        //   113: invokestatic    pl/droidsonroids/gif/ReLinker.openZipFile:(Ljava/io/File;)Ljava/util/zip/ZipFile;
        //   116: astore          5
        //   118: iconst_0       
        //   119: istore          6
        //   121: aload_2        
        //   122: astore_1       
        //   123: iload           6
        //   125: iconst_1       
        //   126: iadd           
        //   127: istore          7
        //   129: iload           6
        //   131: iconst_5       
        //   132: if_icmpge       290
        //   135: aload           5
        //   137: astore          4
        //   139: aload           5
        //   141: invokestatic    pl/droidsonroids/gif/ReLinker.findLibraryEntry:(Ljava/util/zip/ZipFile;)Ljava/util/zip/ZipEntry;
        //   144: astore_0       
        //   145: aload_0        
        //   146: ifnonnull       220
        //   149: aload           5
        //   151: astore          4
        //   153: new             Ljava/lang/IllegalStateException;
        //   156: astore_0       
        //   157: aload           5
        //   159: astore          4
        //   161: new             Ljava/lang/StringBuilder;
        //   164: astore_1       
        //   165: aload           5
        //   167: astore          4
        //   169: aload_1        
        //   170: invokespecial   java/lang/StringBuilder.<init>:()V
        //   173: aload           5
        //   175: astore          4
        //   177: aload_0        
        //   178: aload_1        
        //   179: ldc             "Library "
        //   181: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   184: getstatic       pl/droidsonroids/gif/ReLinker.MAPPED_BASE_LIB_NAME:Ljava/lang/String;
        //   187: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   190: ldc             " for supported ABIs not found in APK file"
        //   192: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   195: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   198: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   201: aload           5
        //   203: astore          4
        //   205: aload_0        
        //   206: athrow         
        //   207: astore_0       
        //   208: aload           4
        //   210: ifnull          218
        //   213: aload           4
        //   215: invokevirtual   java/util/zip/ZipFile.close:()V
        //   218: aload_0        
        //   219: athrow         
        //   220: aconst_null    
        //   221: astore          4
        //   223: aconst_null    
        //   224: astore_2       
        //   225: aconst_null    
        //   226: astore          8
        //   228: aconst_null    
        //   229: astore          9
        //   231: aload           5
        //   233: aload_0        
        //   234: invokevirtual   java/util/zip/ZipFile.getInputStream:(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;
        //   237: astore_0       
        //   238: aload_0        
        //   239: astore_2       
        //   240: aload_0        
        //   241: astore          4
        //   243: new             Ljava/io/FileOutputStream;
        //   246: astore          10
        //   248: aload_0        
        //   249: astore_2       
        //   250: aload_0        
        //   251: astore          4
        //   253: aload           10
        //   255: aload_1        
        //   256: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   259: aload_0        
        //   260: aload           10
        //   262: invokestatic    pl/droidsonroids/gif/ReLinker.copy:(Ljava/io/InputStream;Ljava/io/OutputStream;)V
        //   265: aload           5
        //   267: astore          4
        //   269: aload_0        
        //   270: invokestatic    pl/droidsonroids/gif/ReLinker.closeSilently:(Ljava/io/Closeable;)V
        //   273: aload           5
        //   275: astore          4
        //   277: aload           10
        //   279: invokestatic    pl/droidsonroids/gif/ReLinker.closeSilently:(Ljava/io/Closeable;)V
        //   282: aload           5
        //   284: astore          4
        //   286: aload_1        
        //   287: invokestatic    pl/droidsonroids/gif/ReLinker.setFilePermissions:(Ljava/io/File;)V
        //   290: aload           5
        //   292: ifnull          300
        //   295: aload           5
        //   297: invokevirtual   java/util/zip/ZipFile.close:()V
        //   300: goto            47
        //   303: astore_0       
        //   304: aload_2        
        //   305: astore_0       
        //   306: aload           9
        //   308: astore_2       
        //   309: iload           7
        //   311: iconst_2       
        //   312: if_icmple       317
        //   315: aload_3        
        //   316: astore_1       
        //   317: aload           5
        //   319: astore          4
        //   321: aload_0        
        //   322: invokestatic    pl/droidsonroids/gif/ReLinker.closeSilently:(Ljava/io/Closeable;)V
        //   325: aload           5
        //   327: astore          4
        //   329: aload_2        
        //   330: invokestatic    pl/droidsonroids/gif/ReLinker.closeSilently:(Ljava/io/Closeable;)V
        //   333: iload           7
        //   335: istore          6
        //   337: goto            123
        //   340: astore_1       
        //   341: aload           4
        //   343: astore_0       
        //   344: aload           8
        //   346: astore_2       
        //   347: aload           5
        //   349: astore          4
        //   351: aload_0        
        //   352: invokestatic    pl/droidsonroids/gif/ReLinker.closeSilently:(Ljava/io/Closeable;)V
        //   355: aload           5
        //   357: astore          4
        //   359: aload_2        
        //   360: invokestatic    pl/droidsonroids/gif/ReLinker.closeSilently:(Ljava/io/Closeable;)V
        //   363: aload           5
        //   365: astore          4
        //   367: aload_1        
        //   368: athrow         
        //   369: astore_0       
        //   370: goto            300
        //   373: astore_1       
        //   374: goto            218
        //   377: astore_1       
        //   378: aload           10
        //   380: astore_2       
        //   381: goto            347
        //   384: astore          4
        //   386: aload           10
        //   388: astore_2       
        //   389: goto            309
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  112    118    207    220    Any
        //  139    145    207    220    Any
        //  153    157    207    220    Any
        //  161    165    207    220    Any
        //  169    173    207    220    Any
        //  177    201    207    220    Any
        //  205    207    207    220    Any
        //  213    218    373    377    Ljava/io/IOException;
        //  231    238    303    309    Ljava/io/IOException;
        //  231    238    340    347    Any
        //  243    248    303    309    Ljava/io/IOException;
        //  243    248    340    347    Any
        //  253    259    303    309    Ljava/io/IOException;
        //  253    259    340    347    Any
        //  259    265    384    392    Ljava/io/IOException;
        //  259    265    377    384    Any
        //  269    273    207    220    Any
        //  277    282    207    220    Any
        //  286    290    207    220    Any
        //  295    300    369    373    Ljava/io/IOException;
        //  321    325    207    220    Any
        //  329    333    207    220    Any
        //  351    355    207    220    Any
        //  359    363    207    220    Any
        //  367    369    207    220    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0218:
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

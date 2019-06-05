// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.utils;

import java.util.Locale;
import java.io.FileFilter;
import android.os.Environment;
import java.io.IOException;
import java.io.File;

public class FileSystem
{
    public static String CACHE;
    public static String CACHE_AUDIO;
    private static final String CARD_ROOT = "{CARD_ROOT}";
    private static final String[] EXTERNAL_DIRECTORIES;
    public static String ROOT;
    private static final String TAG = "FileSystem";
    
    static {
        EXTERNAL_DIRECTORIES = new String[] { "{CARD_ROOT}external_sd", "{CARD_ROOT}_externalsd", "{CARD_ROOT}sd", "{CARD_ROOT}emmc", "{CARD_ROOT}ext_sd", "/Removable/MicroSD", "/mnt/emms", "/mnt/external1" };
        FileSystem.ROOT = null;
        FileSystem.CACHE = "cache/";
        FileSystem.CACHE_AUDIO = FileSystem.CACHE + "audio/";
    }
    
    public static boolean backupFile(final File file) {
        try {
            if (file.length() > 0L) {
                copyFile(file, new File(file.getAbsolutePath() + ".bak"));
            }
            return true;
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    public static void checkFolders(final String s) {
        try {
            new File(s).getParentFile().mkdirs();
        }
        catch (Exception ex) {
            Logger.e("FileSystem", "checkFolders(" + s + "), ex: " + ex.toString());
        }
    }
    
    public static void copyFile(final File p0, final File p1) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1        
        //     2: invokevirtual   java/io/File.equals:(Ljava/lang/Object;)Z
        //     5: ifeq            9
        //     8: return         
        //     9: aload_1        
        //    10: invokevirtual   java/io/File.exists:()Z
        //    13: ifne            21
        //    16: aload_1        
        //    17: invokevirtual   java/io/File.createNewFile:()Z
        //    20: pop            
        //    21: aconst_null    
        //    22: astore_2       
        //    23: aconst_null    
        //    24: astore_3       
        //    25: aload_3        
        //    26: astore          4
        //    28: aload_2        
        //    29: astore          5
        //    31: new             Ljava/io/FileInputStream;
        //    34: astore          6
        //    36: aload_3        
        //    37: astore          4
        //    39: aload_2        
        //    40: astore          5
        //    42: aload           6
        //    44: aload_0        
        //    45: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    48: aload_3        
        //    49: astore          4
        //    51: aload_2        
        //    52: astore          5
        //    54: aload           6
        //    56: invokevirtual   java/io/FileInputStream.getChannel:()Ljava/nio/channels/FileChannel;
        //    59: astore_2       
        //    60: aload_3        
        //    61: astore          4
        //    63: aload_2        
        //    64: astore          5
        //    66: new             Ljava/io/FileOutputStream;
        //    69: astore          6
        //    71: aload_3        
        //    72: astore          4
        //    74: aload_2        
        //    75: astore          5
        //    77: aload           6
        //    79: aload_1        
        //    80: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //    83: aload_3        
        //    84: astore          4
        //    86: aload_2        
        //    87: astore          5
        //    89: aload           6
        //    91: invokevirtual   java/io/FileOutputStream.getChannel:()Ljava/nio/channels/FileChannel;
        //    94: astore_3       
        //    95: aload_3        
        //    96: astore          4
        //    98: aload_2        
        //    99: astore          5
        //   101: aload_3        
        //   102: aload_2        
        //   103: lconst_0       
        //   104: aload_2        
        //   105: invokevirtual   java/nio/channels/FileChannel.size:()J
        //   108: invokevirtual   java/nio/channels/FileChannel.transferFrom:(Ljava/nio/channels/ReadableByteChannel;JJ)J
        //   111: pop2           
        //   112: aload_2        
        //   113: ifnull          120
        //   116: aload_2        
        //   117: invokevirtual   java/nio/channels/FileChannel.close:()V
        //   120: aload_3        
        //   121: ifnull          128
        //   124: aload_3        
        //   125: invokevirtual   java/nio/channels/FileChannel.close:()V
        //   128: aload_1        
        //   129: aload_0        
        //   130: invokevirtual   java/io/File.lastModified:()J
        //   133: invokevirtual   java/io/File.setLastModified:(J)Z
        //   136: pop            
        //   137: goto            8
        //   140: astore_0       
        //   141: aload           5
        //   143: ifnull          151
        //   146: aload           5
        //   148: invokevirtual   java/nio/channels/FileChannel.close:()V
        //   151: aload           4
        //   153: ifnull          161
        //   156: aload           4
        //   158: invokevirtual   java/nio/channels/FileChannel.close:()V
        //   161: aload_0        
        //   162: athrow         
        //   163: astore          4
        //   165: goto            120
        //   168: astore          4
        //   170: goto            128
        //   173: astore_1       
        //   174: goto            151
        //   177: astore_1       
        //   178: goto            161
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  31     36     140    181    Any
        //  42     48     140    181    Any
        //  54     60     140    181    Any
        //  66     71     140    181    Any
        //  77     83     140    181    Any
        //  89     95     140    181    Any
        //  101    112    140    181    Any
        //  116    120    163    168    Ljava/io/IOException;
        //  124    128    168    173    Ljava/io/IOException;
        //  146    151    173    177    Ljava/io/IOException;
        //  156    161    177    181    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 99 out-of-bounds for length 99
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
    
    public static boolean createRoot(final String s) {
        final boolean b = false;
        boolean b2;
        if (FileSystem.ROOT != null && new File(FileSystem.ROOT).exists()) {
            b2 = true;
        }
        else {
            try {
                final String externalStorageDir = getExternalStorageDir();
                b2 = b;
                if (externalStorageDir != null) {
                    final String s2 = null;
                    final String[] external_DIRECTORIES = FileSystem.EXTERNAL_DIRECTORIES;
                    final int length = external_DIRECTORIES.length;
                    int n = 0;
                    String string;
                    while (true) {
                        string = s2;
                        if (n >= length) {
                            break;
                        }
                        String replace;
                        final String s3 = replace = external_DIRECTORIES[n];
                        if (s3.contains("{CARD_ROOT}")) {
                            replace = s3.replace("{CARD_ROOT}", externalStorageDir);
                        }
                        if (new File(replace).exists()) {
                            string = replace + "/";
                            break;
                        }
                        ++n;
                    }
                    final File file = new File(externalStorageDir + s);
                    if (string == null) {
                        b2 = setRootDirectory(externalStorageDir, file.getAbsolutePath());
                    }
                    else {
                        final File file2 = new File(string + s);
                        if (file2.exists()) {
                            b2 = setRootDirectory(string, file2.getAbsolutePath());
                        }
                        else if (file.exists()) {
                            b2 = setRootDirectory(externalStorageDir, file.getAbsolutePath());
                        }
                        else {
                            b2 = setRootDirectory(string, file2.getAbsolutePath());
                        }
                    }
                }
            }
            catch (Exception ex) {
                Logger.e("FileSystem", "createRoot(), ex: " + ex.toString());
                b2 = b;
            }
        }
        return b2;
    }
    
    public static File findFile(final String s) {
        return findFile(s, "gwc");
    }
    
    public static File findFile(final String prefix, final String s) {
        final File[] files = getFiles(FileSystem.ROOT, s);
        File file;
        if (files == null) {
            file = null;
        }
        else {
            for (int length = files.length, i = 0; i < length; ++i) {
                if ((file = files[i]).getName().startsWith(prefix)) {
                    return file;
                }
            }
            file = null;
        }
        return file;
    }
    
    public static String getExternalStorageDir() {
        final String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String string;
        if (absolutePath == null) {
            string = null;
        }
        else {
            string = absolutePath;
            if (!absolutePath.endsWith("/")) {
                string = absolutePath + "/";
            }
        }
        return string;
    }
    
    public static File[] getFiles(final String s, final String s2) {
        return getFiles2(s, new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return file.getName().toLowerCase(Locale.getDefault()).endsWith(s2);
            }
        });
    }
    
    public static File[] getFiles2(String listFiles, final FileFilter filter) {
        try {
            final File file = new File((String)listFiles);
            if (!file.exists()) {
                listFiles = new File[0];
            }
            else {
                listFiles = file.listFiles(filter);
            }
            return (File[])listFiles;
        }
        catch (Exception ex) {
            Logger.e("FileSystem", "getFiles2(), folder: " + (String)listFiles);
            listFiles = new File[0];
            return (File[])listFiles;
        }
    }
    
    public static String getRoot() {
        if (FileSystem.ROOT == null) {
            createRoot("WhereYouGo");
        }
        return FileSystem.ROOT;
    }
    
    public static void saveBytes(final String str, final byte[] array) {
        synchronized (FileSystem.class) {
            try {
                if (array.length != 0) {
                    new FileSystemDataWritter(str, array, -1L);
                }
            }
            catch (Exception ex) {
                Logger.e("FileSystem", "saveBytes(" + str + "), e: " + ex.toString());
            }
        }
    }
    
    public static boolean setRootDirectory(final String s) {
        return setRootDirectory(null, s);
    }
    
    private static boolean setRootDirectory(String string, final String str) {
        boolean b2;
        final boolean b = b2 = false;
        if (str != null) {
            if (str.equals("")) {
                b2 = b;
            }
            else {
                string = str;
                if (!str.endsWith("/")) {
                    string = str + "/";
                }
                final File file = new File(string);
                if (!file.exists()) {
                    b2 = b;
                    if (!file.mkdir()) {
                        return b2;
                    }
                }
                FileSystem.ROOT = string;
                b2 = true;
            }
        }
        return b2;
    }
}

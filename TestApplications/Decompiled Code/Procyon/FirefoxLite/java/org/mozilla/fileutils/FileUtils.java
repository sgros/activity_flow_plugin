// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.fileutils;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import java.util.concurrent.Callable;
import org.mozilla.threadutils.ThreadUtils;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.lang.ref.WeakReference;
import android.util.Log;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import android.os.Bundle;
import android.media.MediaScannerConnection$OnScanCompletedListener;
import android.media.MediaScannerConnection;
import java.util.Iterator;
import java.io.FileNotFoundException;
import org.json.JSONObject;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.os.Environment;
import java.util.HashMap;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import android.webkit.WebStorage;
import android.os.Build$VERSION;
import android.content.Context;
import java.io.File;

public class FileUtils
{
    public static boolean canReadExternalStorage(final Context context) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        boolean b = true;
        if (sdk_INT >= 23) {
            if (context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
                b = false;
            }
            return b;
        }
        return true;
    }
    
    public static long clearCache(final Context context) {
        WebStorage.getInstance().deleteAllData();
        return deleteWebViewCacheDirectory(context);
    }
    
    public static boolean copy(File file, File file2) {
        final IOException ex = null;
        final File file3 = null;
        Object o = null;
        Label_0149: {
            try {
                if (file2.exists()) {
                    return false;
                }
                final FileInputStream fileInputStream = new FileInputStream((File)file);
                try {
                    final FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    try {
                        final boolean copy = copy(fileInputStream, fileOutputStream);
                        fileInputStream.close();
                        fileOutputStream.close();
                        try {
                            fileInputStream.close();
                            fileOutputStream.close();
                        }
                        catch (Exception ex2) {
                            ex2.printStackTrace();
                        }
                        return copy;
                    }
                    catch (IOException ex) {}
                }
                catch (IOException ex) {}
            }
            catch (IOException ex) {
                file = null;
                file2 = file3;
            }
            finally {
                o = null;
                file = ex;
                break Label_0149;
            }
            try {
                ex.printStackTrace();
                Label_0126: {
                    if (file2 != null) {
                        Label_0137: {
                            try {
                                ((FileInputStream)file2).close();
                            }
                            catch (Exception file) {
                                break Label_0137;
                            }
                            break Label_0126;
                        }
                        file.printStackTrace();
                        return false;
                    }
                }
                if (file != null) {
                    ((FileOutputStream)file).close();
                }
                return false;
            }
            finally {
                o = file2;
                final File file4;
                file2 = file4;
            }
        }
        Label_0166: {
            if (o != null) {
                Label_0177: {
                    try {
                        ((FileInputStream)o).close();
                    }
                    catch (Exception ex3) {
                        break Label_0177;
                    }
                    break Label_0166;
                }
                final Exception ex3;
                ex3.printStackTrace();
                throw file2;
            }
        }
        if (file != null) {
            ((FileOutputStream)file).close();
        }
        throw file2;
    }
    
    public static boolean copy(final InputStream inputStream, final OutputStream outputStream) {
        final byte[] array = new byte[1024];
        try {
            while (true) {
                final int read = inputStream.read(array);
                if (read == -1) {
                    break;
                }
                outputStream.write(array, 0, read);
            }
            return true;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    private static boolean deleteContent(final File parent) {
        final String[] list = parent.list();
        int i = 0;
        if (list == null) {
            return false;
        }
        final int length = list.length;
        boolean b = true;
        while (i < length) {
            final File file = new File(parent, list[i]);
            if (file.isDirectory()) {
                b &= deleteDirectory(file);
            }
            else {
                b &= file.delete();
            }
            ++i;
        }
        return b;
    }
    
    private static long deleteContentOnly(final File parent) {
        final String[] list = parent.list();
        long n = 0L;
        if (list == null) {
            return 0L;
        }
        long n2;
        for (int length = list.length, i = 0; i < length; ++i, n = n2) {
            final File file = new File(parent, list[i]);
            if (file.isDirectory()) {
                n2 = n + deleteContentOnly(file);
            }
            else {
                final long length2 = file.length();
                n2 = n;
                if (file.delete()) {
                    n2 = n + length2;
                }
            }
        }
        return n;
    }
    
    public static boolean deleteDirectory(final File file) {
        return deleteContent(file) && file.delete();
    }
    
    private static long deleteWebViewCacheDirectory(final Context context) {
        final File file = new File(context.getApplicationInfo().dataDir, "cache");
        if (!file.exists()) {
            return -1L;
        }
        return deleteContentOnly(file);
    }
    
    public static boolean ensureDir(final File file) {
        final boolean mkdirs = file.mkdirs();
        boolean b = true;
        if (mkdirs) {
            return true;
        }
        if (!file.exists() || !file.isDirectory() || !file.canWrite()) {
            b = false;
        }
        return b;
    }
    
    public static HashMap<String, Object> fromJsonOnDisk(String line) throws Exception {
        final File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory != null) {
            final File file = new File(externalStorageDirectory, line);
            if (file.exists()) {
                final StringBuilder sb = new StringBuilder();
                final FileInputStream in = new FileInputStream(file);
                Object o = line = null;
                try {
                    line = (String)o;
                    line = (String)o;
                    final InputStreamReader in2 = new InputStreamReader(in, "UTF-8");
                    line = (String)o;
                    final BufferedReader bufferedReader = new BufferedReader(in2);
                    Throwable t = null;
                    try {
                        while (true) {
                            line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            sb.append(line);
                            sb.append('\n');
                        }
                        line = (String)o;
                        bufferedReader.close();
                        in.close();
                        o = new JSONObject(sb.toString());
                        line = (String)new HashMap();
                        final Iterator keys = ((JSONObject)o).keys();
                        while (keys.hasNext()) {
                            final String key = keys.next();
                            ((HashMap<String, Object>)line).put(key, ((JSONObject)o).get(key));
                        }
                        return (HashMap<String, Object>)line;
                    }
                    catch (Throwable t) {
                        try {
                            throw t;
                        }
                        finally {}
                    }
                    finally {
                        t = null;
                    }
                    if (t != null) {
                        line = (String)o;
                        try {
                            bufferedReader.close();
                        }
                        catch (Throwable exception) {
                            line = (String)o;
                            t.addSuppressed(exception);
                        }
                    }
                    else {
                        line = (String)o;
                        bufferedReader.close();
                    }
                    line = (String)o;
                    throw;
                }
                catch (Throwable t3) {}
                finally {
                    if (line != null) {
                        try {
                            in.close();
                        }
                        catch (Throwable exception2) {
                            ((Throwable)line).addSuppressed(exception2);
                        }
                    }
                    else {
                        in.close();
                    }
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Can't find ");
            sb2.append(line);
            throw new FileNotFoundException(sb2.toString());
        }
        throw new Exception("No External Storage Available");
    }
    
    public static File getFaviconFolder(final Context context) {
        final File file = new File(context.getFilesDir(), "favicons");
        if (!ensureDir(file)) {
            return context.getCacheDir();
        }
        return file;
    }
    
    public static File getFileSlot(final File file, final String str) {
        final File file2 = new File(file, str);
        if (!file2.exists()) {
            return file2;
        }
        for (int i = 1; i < 1000; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append("-");
            sb.append(str);
            final File file3 = new File(file, sb.toString());
            if (!file3.exists()) {
                return file3;
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Not-lucky-");
        sb2.append(str);
        return getFileSlot(file, sb2.toString());
    }
    
    public static void notifyMediaScanner(final Context context, final String s) {
        MediaScannerConnection.scanFile(context, new String[] { s }, new String[] { null }, (MediaScannerConnection$OnScanCompletedListener)null);
    }
    
    public static Bundle readBundleFromStorage(final File p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    org/mozilla/fileutils/FileUtils.ensureDir:(Ljava/io/File;)Z
        //     4: pop            
        //     5: new             Ljava/io/File;
        //     8: dup            
        //     9: aload_0        
        //    10: aload_1        
        //    11: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    14: astore_0       
        //    15: aload_0        
        //    16: invokevirtual   java/io/File.exists:()Z
        //    19: istore_2       
        //    20: aconst_null    
        //    21: astore_3       
        //    22: aconst_null    
        //    23: astore          4
        //    25: iload_2        
        //    26: ifne            31
        //    29: aconst_null    
        //    30: areturn        
        //    31: new             Ljava/io/FileInputStream;
        //    34: astore          5
        //    36: aload           5
        //    38: aload_0        
        //    39: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    42: new             Ljava/io/ObjectInputStream;
        //    45: astore          6
        //    47: aload           6
        //    49: aload           5
        //    51: invokespecial   java/io/ObjectInputStream.<init>:(Ljava/io/InputStream;)V
        //    54: new             Lorg/mozilla/fileutils/AndroidBundleSerializer;
        //    57: astore_0       
        //    58: aload_0        
        //    59: invokespecial   org/mozilla/fileutils/AndroidBundleSerializer.<init>:()V
        //    62: aload_0        
        //    63: aload           6
        //    65: invokevirtual   org/mozilla/fileutils/AndroidBundleSerializer.deserializeBundle:(Ljava/io/ObjectInputStream;)Landroid/os/Bundle;
        //    68: astore_0       
        //    69: aload_0        
        //    70: astore_1       
        //    71: aload           6
        //    73: invokevirtual   java/io/ObjectInputStream.close:()V
        //    76: aload_0        
        //    77: astore_1       
        //    78: aload           5
        //    80: invokevirtual   java/io/FileInputStream.close:()V
        //    83: goto            214
        //    86: astore          4
        //    88: aload_0        
        //    89: astore_1       
        //    90: aload           4
        //    92: astore_0       
        //    93: goto            146
        //    96: astore_1       
        //    97: aconst_null    
        //    98: astore_0       
        //    99: goto            106
        //   102: astore_0       
        //   103: aload_0        
        //   104: athrow         
        //   105: astore_1       
        //   106: aload_0        
        //   107: ifnull          129
        //   110: aload           6
        //   112: invokevirtual   java/io/ObjectInputStream.close:()V
        //   115: goto            134
        //   118: astore          4
        //   120: aload_0        
        //   121: aload           4
        //   123: invokevirtual   java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
        //   126: goto            134
        //   129: aload           6
        //   131: invokevirtual   java/io/ObjectInputStream.close:()V
        //   134: aload_1        
        //   135: athrow         
        //   136: astore          4
        //   138: aconst_null    
        //   139: astore_0       
        //   140: goto            162
        //   143: astore_0       
        //   144: aconst_null    
        //   145: astore_1       
        //   146: aload_0        
        //   147: astore          4
        //   149: aload_0        
        //   150: athrow         
        //   151: astore          6
        //   153: aload           4
        //   155: astore_3       
        //   156: aload_1        
        //   157: astore_0       
        //   158: aload           6
        //   160: astore          4
        //   162: aload_3        
        //   163: ifnull          189
        //   166: aload_0        
        //   167: astore_1       
        //   168: aload           5
        //   170: invokevirtual   java/io/FileInputStream.close:()V
        //   173: goto            196
        //   176: astore          6
        //   178: aload_0        
        //   179: astore_1       
        //   180: aload_3        
        //   181: aload           6
        //   183: invokevirtual   java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
        //   186: goto            196
        //   189: aload_0        
        //   190: astore_1       
        //   191: aload           5
        //   193: invokevirtual   java/io/FileInputStream.close:()V
        //   196: aload_0        
        //   197: astore_1       
        //   198: aload           4
        //   200: athrow         
        //   201: astore_0       
        //   202: goto            208
        //   205: astore_0       
        //   206: aconst_null    
        //   207: astore_1       
        //   208: aload_0        
        //   209: invokevirtual   java/io/IOException.printStackTrace:()V
        //   212: aload_1        
        //   213: astore_0       
        //   214: aload_0        
        //   215: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  31     42     205    208    Ljava/io/IOException;
        //  42     54     143    146    Ljava/lang/Throwable;
        //  42     54     136    143    Any
        //  54     69     102    106    Ljava/lang/Throwable;
        //  54     69     96     102    Any
        //  71     76     86     96     Ljava/lang/Throwable;
        //  71     76     151    162    Any
        //  78     83     201    205    Ljava/io/IOException;
        //  103    105    105    106    Any
        //  110    115    118    129    Ljava/lang/Throwable;
        //  110    115    136    143    Any
        //  120    126    143    146    Ljava/lang/Throwable;
        //  120    126    136    143    Any
        //  129    134    143    146    Ljava/lang/Throwable;
        //  129    134    136    143    Any
        //  134    136    143    146    Ljava/lang/Throwable;
        //  134    136    136    143    Any
        //  149    151    151    162    Any
        //  168    173    176    189    Ljava/lang/Throwable;
        //  168    173    201    205    Ljava/io/IOException;
        //  180    186    201    205    Ljava/io/IOException;
        //  191    196    201    205    Ljava/io/IOException;
        //  198    201    201    205    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 126 out-of-bounds for length 126
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3435)
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
    
    public static String readStringFromFile(final File parent, String child) {
        ensureDir(parent);
        final File file = new File(parent, (String)child);
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            try {
                final StringBuilder sb = new StringBuilder();
                while (true) {
                    final String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
                final String string = sb.toString();
                bufferedReader.close();
                return string;
            }
            catch (Throwable child) {
                try {
                    throw child;
                }
                finally {}
            }
            finally {
                child = null;
            }
            if (child != null) {
                try {
                    bufferedReader.close();
                }
                catch (Throwable exception) {
                    child.addSuppressed(exception);
                }
            }
            else {
                bufferedReader.close();
            }
            throw parent;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static void writeBundleToStorage(File parent, String child, final Bundle bundle) {
        ensureDir(parent);
        final File file = new File(parent, child);
        final String s = null;
        final File file2 = parent = null;
        try {
            try {
                parent = file2;
                final FileOutputStream out = new FileOutputStream(file);
                parent = file2;
                parent = file2;
                child = (String)new ObjectOutputStream(out);
                try {
                    new AndroidBundleSerializer().serializeBundle((ObjectOutputStream)child, bundle);
                    final String s2 = child;
                    ((ObjectOutputStream)s2).close();
                }
                catch (IOException ex3) {}
                finally {
                    parent = (File)child;
                }
            }
            finally {}
        }
        catch (IOException ex4) {
            child = s;
        }
        try {
            final String s2 = child;
            ((ObjectOutputStream)s2).close();
            return;
            ((ObjectOutputStream)child).close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return;
        if (parent != null) {
            try {
                ((ObjectOutputStream)parent).close();
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }
    }
    
    public static void writeStringToFile(File file, final String child, final String str) {
        ensureDir(file);
        file = new File(file, child);
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            file = null;
            try {
                try {
                    bufferedWriter.write(str);
                    bufferedWriter.close();
                }
                finally {
                    if (file != null) {
                        final BufferedWriter bufferedWriter2 = bufferedWriter;
                        bufferedWriter2.close();
                    }
                    else {
                        bufferedWriter.close();
                    }
                }
            }
            catch (Throwable t) {}
            try {
                final BufferedWriter bufferedWriter2 = bufferedWriter;
                bufferedWriter2.close();
            }
            catch (Throwable t2) {}
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static class DeleteFileRunnable extends FileIORunnable
    {
        public DeleteFileRunnable(final File file) {
            super(file);
        }
        
        @Override
        protected void doIO(final File file) {
            if (!file.exists()) {
                return;
            }
            if (!file.delete()) {
                Log.e("DeleteFileRunnable", "Failed to delete file");
            }
        }
    }
    
    public static class DeleteFolderRunnable extends FileIORunnable
    {
        public DeleteFolderRunnable(final File file) {
            super(file);
        }
        
        @Override
        protected void doIO(final File file) {
            deleteContent(file);
        }
    }
    
    private abstract static class FileIORunnable implements Runnable
    {
        private File file;
        
        private FileIORunnable(final File file) {
            this.file = file;
        }
        
        protected abstract void doIO(final File p0);
        
        @Override
        public void run() {
            this.doIO(this.file);
        }
    }
    
    public static class GetCache extends GetFile
    {
        public GetCache(final WeakReference<Context> weakReference) {
            super((WeakReference)weakReference);
        }
        
        @Override
        protected File getFile(final Context context) {
            return context.getCacheDir();
        }
    }
    
    public static class GetFaviconFolder extends GetFile
    {
        public GetFaviconFolder(final WeakReference<Context> weakReference) {
            super((WeakReference)weakReference);
        }
        
        @Override
        protected File getFile(final Context context) {
            return FileUtils.getFaviconFolder(context);
        }
    }
    
    private abstract static class GetFile
    {
        private Future<File> getFileFuture;
        
        private GetFile(final WeakReference<Context> weakReference) {
            this.getFileFuture = ThreadUtils.postToBackgroundThread((Callable<File>)new _$$Lambda$FileUtils$GetFile$3co3PTIlVY5rnxq4Dt4YLPByI_U(this, weakReference));
        }
        
        public File get() throws ExecutionException, InterruptedException {
            return this.getFileFuture.get();
        }
        
        protected abstract File getFile(final Context p0);
    }
    
    private static class LiveDataTask<T, S> extends AsyncTask<Void, Void, S>
    {
        private Function<T, S> function;
        private MutableLiveData<T> liveData;
        
        protected LiveDataTask(final MutableLiveData<T> liveData, final Function<T, S> function) {
            this.liveData = liveData;
            this.function = function;
        }
        
        protected S doInBackground(final Void... array) {
            throw new IllegalStateException("LiveDataTask should not be instantiated");
        }
        
        protected void onPostExecute(final S n) {
            this.liveData.setValue(this.function.apply(n));
        }
        
        public interface Function<T, S>
        {
            T apply(final S p0);
        }
    }
    
    public static class ReadStringFromFileTask<T> extends LiveDataTask<T, String>
    {
        private File dir;
        private String fileName;
        
        public ReadStringFromFileTask(final File dir, final String fileName, final MutableLiveData<T> mutableLiveData, final Function<T, String> function) {
            super(mutableLiveData, (Function<T, Object>)function);
            this.dir = dir;
            this.fileName = fileName;
        }
        
        protected String doInBackground(final Void... array) {
            return FileUtils.readStringFromFile(this.dir, this.fileName);
        }
    }
    
    public static class WriteStringToFileRunnable extends FileIORunnable
    {
        private String string;
        
        public WriteStringToFileRunnable(final File file, final String string) {
            super(file);
            this.string = string;
        }
        
        @Override
        protected void doIO(final File file) {
            FileUtils.writeStringToFile(file.getParentFile(), file.getName(), this.string);
        }
    }
}

package org.mozilla.fileutils;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.util.Log;
import android.webkit.WebStorage;
import com.adjust.sdk.Constants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.mozilla.threadutils.ThreadUtils;

public class FileUtils {

    private static abstract class FileIORunnable implements Runnable {
        private File file;

        public abstract void doIO(File file);

        private FileIORunnable(File file) {
            this.file = file;
        }

        public void run() {
            doIO(this.file);
        }
    }

    private static abstract class GetFile {
        private Future<File> getFileFuture;

        public abstract File getFile(Context context);

        private GetFile(WeakReference<Context> weakReference) {
            this.getFileFuture = ThreadUtils.postToBackgroundThread(new C0424-$$Lambda$FileUtils$GetFile$3co3PTIlVY5rnxq4Dt4YLPByI_U(this, weakReference));
        }

        public static /* synthetic */ File lambda$new$0(GetFile getFile, WeakReference weakReference) throws Exception {
            Context context = (Context) weakReference.get();
            if (context == null) {
                return null;
            }
            return getFile.getFile(context);
        }

        public File get() throws ExecutionException, InterruptedException {
            return (File) this.getFileFuture.get();
        }
    }

    private static class LiveDataTask<T, S> extends AsyncTask<Void, Void, S> {
        private Function<T, S> function;
        private MutableLiveData<T> liveData;

        public interface Function<T, S> {
            T apply(S s);
        }

        protected LiveDataTask(MutableLiveData<T> mutableLiveData, Function<T, S> function) {
            this.liveData = mutableLiveData;
            this.function = function;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(S s) {
            this.liveData.setValue(this.function.apply(s));
        }

        /* Access modifiers changed, original: protected|varargs */
        public S doInBackground(Void... voidArr) {
            throw new IllegalStateException("LiveDataTask should not be instantiated");
        }
    }

    public static class DeleteFileRunnable extends FileIORunnable {
        public /* bridge */ /* synthetic */ void run() {
            super.run();
        }

        public DeleteFileRunnable(File file) {
            super(file);
        }

        /* Access modifiers changed, original: protected */
        public void doIO(File file) {
            if (file.exists() && !file.delete()) {
                Log.e("DeleteFileRunnable", "Failed to delete file");
            }
        }
    }

    public static class DeleteFolderRunnable extends FileIORunnable {
        public /* bridge */ /* synthetic */ void run() {
            super.run();
        }

        public DeleteFolderRunnable(File file) {
            super(file);
        }

        /* Access modifiers changed, original: protected */
        public void doIO(File file) {
            FileUtils.deleteContent(file);
        }
    }

    public static class GetCache extends GetFile {
        public /* bridge */ /* synthetic */ File get() throws ExecutionException, InterruptedException {
            return super.get();
        }

        public GetCache(WeakReference<Context> weakReference) {
            super(weakReference);
        }

        /* Access modifiers changed, original: protected */
        public File getFile(Context context) {
            return context.getCacheDir();
        }
    }

    public static class GetFaviconFolder extends GetFile {
        public /* bridge */ /* synthetic */ File get() throws ExecutionException, InterruptedException {
            return super.get();
        }

        public GetFaviconFolder(WeakReference<Context> weakReference) {
            super(weakReference);
        }

        /* Access modifiers changed, original: protected */
        public File getFile(Context context) {
            return FileUtils.getFaviconFolder(context);
        }
    }

    public static class ReadStringFromFileTask<T> extends LiveDataTask<T, String> {
        private File dir;
        private String fileName;

        public ReadStringFromFileTask(File file, String str, MutableLiveData<T> mutableLiveData, Function<T, String> function) {
            super(mutableLiveData, function);
            this.dir = file;
            this.fileName = str;
        }

        /* Access modifiers changed, original: protected|varargs */
        public String doInBackground(Void... voidArr) {
            return FileUtils.readStringFromFile(this.dir, this.fileName);
        }
    }

    public static class WriteStringToFileRunnable extends FileIORunnable {
        private String string;

        public /* bridge */ /* synthetic */ void run() {
            super.run();
        }

        public WriteStringToFileRunnable(File file, String str) {
            super(file);
            this.string = str;
        }

        /* Access modifiers changed, original: protected */
        public void doIO(File file) {
            FileUtils.writeStringToFile(file.getParentFile(), file.getName(), this.string);
        }
    }

    private static long deleteWebViewCacheDirectory(Context context) {
        File file = new File(context.getApplicationInfo().dataDir, "cache");
        if (file.exists()) {
            return deleteContentOnly(file);
        }
        return -1;
    }

    public static boolean ensureDir(File file) {
        boolean z = true;
        if (file.mkdirs()) {
            return true;
        }
        if (!(file.exists() && file.isDirectory() && file.canWrite())) {
            z = false;
        }
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x003d A:{SYNTHETIC, Splitter:B:28:0x003d} */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0045 A:{Catch:{ Exception -> 0x0041 }} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0052 A:{SYNTHETIC, Splitter:B:40:0x0052} */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x005a A:{Catch:{ Exception -> 0x0056 }} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0052 A:{SYNTHETIC, Splitter:B:40:0x0052} */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x005a A:{Catch:{ Exception -> 0x0056 }} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x003d A:{SYNTHETIC, Splitter:B:28:0x003d} */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0045 A:{Catch:{ Exception -> 0x0041 }} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0052 A:{SYNTHETIC, Splitter:B:40:0x0052} */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x005a A:{Catch:{ Exception -> 0x0056 }} */
    public static boolean copy(java.io.File r3, java.io.File r4) {
        /*
        r0 = 0;
        r1 = 0;
        r2 = r4.exists();	 Catch:{ IOException -> 0x0036, all -> 0x0033 }
        if (r2 == 0) goto L_0x0009;
    L_0x0008:
        return r0;
    L_0x0009:
        r2 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0036, all -> 0x0033 }
        r2.<init>(r3);	 Catch:{ IOException -> 0x0036, all -> 0x0033 }
        r3 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x002f, all -> 0x002d }
        r3.<init>(r4);	 Catch:{ IOException -> 0x002f, all -> 0x002d }
        r4 = copy(r2, r3);	 Catch:{ IOException -> 0x002b, all -> 0x0029 }
        r2.close();	 Catch:{ IOException -> 0x002b, all -> 0x0029 }
        r3.close();	 Catch:{ IOException -> 0x002b, all -> 0x0029 }
        r2.close();	 Catch:{ Exception -> 0x0024 }
        r3.close();	 Catch:{ Exception -> 0x0024 }
        goto L_0x0028;
    L_0x0024:
        r3 = move-exception;
        r3.printStackTrace();
    L_0x0028:
        return r4;
    L_0x0029:
        r4 = move-exception;
        goto L_0x004f;
    L_0x002b:
        r4 = move-exception;
        goto L_0x0031;
    L_0x002d:
        r4 = move-exception;
        goto L_0x0050;
    L_0x002f:
        r4 = move-exception;
        r3 = r1;
    L_0x0031:
        r1 = r2;
        goto L_0x0038;
    L_0x0033:
        r4 = move-exception;
        r2 = r1;
        goto L_0x0050;
    L_0x0036:
        r4 = move-exception;
        r3 = r1;
    L_0x0038:
        r4.printStackTrace();	 Catch:{ all -> 0x004d }
        if (r1 == 0) goto L_0x0043;
    L_0x003d:
        r1.close();	 Catch:{ Exception -> 0x0041 }
        goto L_0x0043;
    L_0x0041:
        r3 = move-exception;
        goto L_0x0049;
    L_0x0043:
        if (r3 == 0) goto L_0x004c;
    L_0x0045:
        r3.close();	 Catch:{ Exception -> 0x0041 }
        goto L_0x004c;
    L_0x0049:
        r3.printStackTrace();
    L_0x004c:
        return r0;
    L_0x004d:
        r4 = move-exception;
        r2 = r1;
    L_0x004f:
        r1 = r3;
    L_0x0050:
        if (r2 == 0) goto L_0x0058;
    L_0x0052:
        r2.close();	 Catch:{ Exception -> 0x0056 }
        goto L_0x0058;
    L_0x0056:
        r3 = move-exception;
        goto L_0x005e;
    L_0x0058:
        if (r1 == 0) goto L_0x0061;
    L_0x005a:
        r1.close();	 Catch:{ Exception -> 0x0056 }
        goto L_0x0061;
    L_0x005e:
        r3.printStackTrace();
    L_0x0061:
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.fileutils.FileUtils.copy(java.io.File, java.io.File):boolean");
    }

    public static File getFileSlot(File file, String str) {
        File file2 = new File(file, str);
        if (!file2.exists()) {
            return file2;
        }
        for (int i = 1; i < Constants.ONE_SECOND; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(i);
            stringBuilder.append("-");
            stringBuilder.append(str);
            File file3 = new File(file, stringBuilder.toString());
            if (!file3.exists()) {
                return file3;
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Not-lucky-");
        stringBuilder2.append(str);
        return getFileSlot(file, stringBuilder2.toString());
    }

    public static boolean copy(InputStream inputStream, OutputStream outputStream) {
        byte[] bArr = new byte[1024];
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    return true;
                }
                outputStream.write(bArr, 0, read);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static boolean deleteDirectory(File file) {
        return deleteContent(file) && file.delete();
    }

    private static boolean deleteContent(File file) {
        String[] list = file.list();
        int i = 0;
        if (list == null) {
            return false;
        }
        int length = list.length;
        int i2 = 1;
        while (i < length) {
            int deleteDirectory;
            File file2 = new File(file, list[i]);
            if (file2.isDirectory()) {
                deleteDirectory = deleteDirectory(file2);
            } else {
                deleteDirectory = file2.delete();
            }
            i2 &= deleteDirectory;
            i++;
        }
        return i2;
    }

    private static long deleteContentOnly(File file) {
        String[] list = file.list();
        long j = 0;
        if (list == null) {
            return 0;
        }
        for (String file2 : list) {
            File file3 = new File(file, file2);
            if (file3.isDirectory()) {
                j += deleteContentOnly(file3);
            } else {
                long length = file3.length();
                if (file3.delete()) {
                    j += length;
                }
            }
        }
        return j;
    }

    public static void notifyMediaScanner(Context context, String str) {
        MediaScannerConnection.scanFile(context, new String[]{str}, new String[]{null}, null);
    }

    public static long clearCache(Context context) {
        WebStorage.getInstance().deleteAllData();
        return deleteWebViewCacheDirectory(context);
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x002f A:{SYNTHETIC, Splitter:B:16:0x002f} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x003a A:{SYNTHETIC, Splitter:B:21:0x003a} */
    public static void writeBundleToStorage(java.io.File r1, java.lang.String r2, android.os.Bundle r3) {
        /*
        ensureDir(r1);
        r0 = new java.io.File;
        r0.<init>(r1, r2);
        r1 = 0;
        r2 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0029 }
        r2.<init>(r0);	 Catch:{ IOException -> 0x0029 }
        r0 = new java.io.ObjectOutputStream;	 Catch:{ IOException -> 0x0029 }
        r0.<init>(r2);	 Catch:{ IOException -> 0x0029 }
        r1 = new org.mozilla.fileutils.AndroidBundleSerializer;	 Catch:{ IOException -> 0x0023, all -> 0x001f }
        r1.<init>();	 Catch:{ IOException -> 0x0023, all -> 0x001f }
        r1.serializeBundle(r0, r3);	 Catch:{ IOException -> 0x0023, all -> 0x001f }
        r0.close();	 Catch:{ IOException -> 0x0033 }
        goto L_0x0037;
    L_0x001f:
        r1 = move-exception;
        r2 = r1;
        r1 = r0;
        goto L_0x0038;
    L_0x0023:
        r1 = move-exception;
        r2 = r1;
        r1 = r0;
        goto L_0x002a;
    L_0x0027:
        r2 = move-exception;
        goto L_0x0038;
    L_0x0029:
        r2 = move-exception;
    L_0x002a:
        r2.printStackTrace();	 Catch:{ all -> 0x0027 }
        if (r1 == 0) goto L_0x0037;
    L_0x002f:
        r1.close();	 Catch:{ IOException -> 0x0033 }
        goto L_0x0037;
    L_0x0033:
        r1 = move-exception;
        r1.printStackTrace();
    L_0x0037:
        return;
    L_0x0038:
        if (r1 == 0) goto L_0x0042;
    L_0x003a:
        r1.close();	 Catch:{ IOException -> 0x003e }
        goto L_0x0042;
    L_0x003e:
        r1 = move-exception;
        r1.printStackTrace();
    L_0x0042:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.fileutils.FileUtils.writeBundleToStorage(java.io.File, java.lang.String, android.os.Bundle):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0044 A:{ExcHandler: all (th java.lang.Throwable), Splitter:B:5:0x0015} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0057 A:{Catch:{ IOException -> 0x005b }} */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x004e A:{SYNTHETIC, Splitter:B:37:0x004e} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:29:0x0044, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:30:0x0045, code skipped:
            r1 = null;
     */
    /* JADX WARNING: Missing block: B:31:0x0047, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:32:0x0048, code skipped:
            r1 = null;
            r5 = r0;
     */
    /* JADX WARNING: Missing block: B:38:?, code skipped:
            r4.close();
     */
    /* JADX WARNING: Missing block: B:39:0x0052, code skipped:
            r4 = move-exception;
     */
    /* JADX WARNING: Missing block: B:41:?, code skipped:
            r5.addSuppressed(r4);
     */
    /* JADX WARNING: Missing block: B:42:0x0057, code skipped:
            r4.close();
     */
    public static android.os.Bundle readBundleFromStorage(java.io.File r4, java.lang.String r5) {
        /*
        ensureDir(r4);
        r0 = new java.io.File;
        r0.<init>(r4, r5);
        r4 = r0.exists();
        r5 = 0;
        if (r4 != 0) goto L_0x0010;
    L_0x000f:
        return r5;
    L_0x0010:
        r4 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x005d }
        r4.<init>(r0);	 Catch:{ IOException -> 0x005d }
        r0 = new java.io.ObjectInputStream;	 Catch:{ Throwable -> 0x0047, all -> 0x0044 }
        r0.<init>(r4);	 Catch:{ Throwable -> 0x0047, all -> 0x0044 }
        r1 = new org.mozilla.fileutils.AndroidBundleSerializer;	 Catch:{ Throwable -> 0x002f, all -> 0x002c }
        r1.<init>();	 Catch:{ Throwable -> 0x002f, all -> 0x002c }
        r1 = r1.deserializeBundle(r0);	 Catch:{ Throwable -> 0x002f, all -> 0x002c }
        r0.close();	 Catch:{ Throwable -> 0x002a }
        r4.close();	 Catch:{ IOException -> 0x005b }
        goto L_0x0062;
    L_0x002a:
        r5 = move-exception;
        goto L_0x004a;
    L_0x002c:
        r1 = move-exception;
        r2 = r5;
        goto L_0x0035;
    L_0x002f:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0031 }
    L_0x0031:
        r2 = move-exception;
        r3 = r2;
        r2 = r1;
        r1 = r3;
    L_0x0035:
        if (r2 == 0) goto L_0x0040;
    L_0x0037:
        r0.close();	 Catch:{ Throwable -> 0x003b, all -> 0x0044 }
        goto L_0x0043;
    L_0x003b:
        r0 = move-exception;
        r2.addSuppressed(r0);	 Catch:{ Throwable -> 0x0047, all -> 0x0044 }
        goto L_0x0043;
    L_0x0040:
        r0.close();	 Catch:{ Throwable -> 0x0047, all -> 0x0044 }
    L_0x0043:
        throw r1;	 Catch:{ Throwable -> 0x0047, all -> 0x0044 }
    L_0x0044:
        r0 = move-exception;
        r1 = r5;
        goto L_0x004c;
    L_0x0047:
        r0 = move-exception;
        r1 = r5;
        r5 = r0;
    L_0x004a:
        throw r5;	 Catch:{ all -> 0x004b }
    L_0x004b:
        r0 = move-exception;
    L_0x004c:
        if (r5 == 0) goto L_0x0057;
    L_0x004e:
        r4.close();	 Catch:{ Throwable -> 0x0052 }
        goto L_0x005a;
    L_0x0052:
        r4 = move-exception;
        r5.addSuppressed(r4);	 Catch:{ IOException -> 0x005b }
        goto L_0x005a;
    L_0x0057:
        r4.close();	 Catch:{ IOException -> 0x005b }
    L_0x005a:
        throw r0;	 Catch:{ IOException -> 0x005b }
    L_0x005b:
        r4 = move-exception;
        goto L_0x005f;
    L_0x005d:
        r4 = move-exception;
        r1 = r5;
    L_0x005f:
        r4.printStackTrace();
    L_0x0062:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.fileutils.FileUtils.readBundleFromStorage(java.io.File, java.lang.String):android.os.Bundle");
    }

    public static void writeStringToFile(File file, String str, String str2) {
        ensureDir(file);
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(file, str)), StandardCharsets.UTF_8));
            bufferedWriter.write(str2);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable th) {
            r3.addSuppressed(th);
        }
    }

    public static String readStringFromFile(File file, String str) {
        Throwable th;
        Throwable th2;
        ensureDir(file);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file, str)), StandardCharsets.UTF_8));
            try {
                StringBuilder stringBuilder = new StringBuilder();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        stringBuilder.append(readLine);
                    } else {
                        String stringBuilder2 = stringBuilder.toString();
                        bufferedReader.close();
                        return stringBuilder2;
                    }
                }
            } catch (Throwable th22) {
                Throwable th3 = th22;
                th22 = th;
                th = th3;
            }
            throw th;
            if (th22 != null) {
                try {
                    bufferedReader.close();
                } catch (Throwable th4) {
                    th22.addSuppressed(th4);
                }
            } else {
                bufferedReader.close();
            }
            throw th;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Missing block: B:13:?, code skipped:
            r2.close();
     */
    /* JADX WARNING: Missing block: B:14:0x003a, code skipped:
            r0.close();
            r0 = new org.json.JSONObject(r6.toString());
            r6 = new java.util.HashMap();
            r1 = r0.keys();
     */
    /* JADX WARNING: Missing block: B:16:0x0053, code skipped:
            if (r1.hasNext() == false) goto L_0x0063;
     */
    /* JADX WARNING: Missing block: B:17:0x0055, code skipped:
            r2 = (java.lang.String) r1.next();
            r6.put(r2, r0.get(r2));
     */
    /* JADX WARNING: Missing block: B:18:0x0063, code skipped:
            return r6;
     */
    public static java.util.HashMap<java.lang.String, java.lang.Object> fromJsonOnDisk(java.lang.String r6) throws java.lang.Exception {
        /*
        r0 = android.os.Environment.getExternalStorageDirectory();
        if (r0 == 0) goto L_0x00a7;
    L_0x0006:
        r1 = new java.io.File;
        r1.<init>(r0, r6);
        r0 = r1.exists();
        if (r0 == 0) goto L_0x0090;
    L_0x0011:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r0 = new java.io.FileInputStream;
        r0.<init>(r1);
        r1 = 0;
        r2 = new java.io.BufferedReader;	 Catch:{ Throwable -> 0x007e }
        r3 = new java.io.InputStreamReader;	 Catch:{ Throwable -> 0x007e }
        r4 = "UTF-8";
        r3.<init>(r0, r4);	 Catch:{ Throwable -> 0x007e }
        r2.<init>(r3);	 Catch:{ Throwable -> 0x007e }
    L_0x0028:
        r3 = r2.readLine();	 Catch:{ Throwable -> 0x0067, all -> 0x0064 }
        if (r3 == 0) goto L_0x0037;
    L_0x002e:
        r6.append(r3);	 Catch:{ Throwable -> 0x0067, all -> 0x0064 }
        r3 = 10;
        r6.append(r3);	 Catch:{ Throwable -> 0x0067, all -> 0x0064 }
        goto L_0x0028;
    L_0x0037:
        r2.close();	 Catch:{ Throwable -> 0x007e }
        r0.close();
        r0 = new org.json.JSONObject;
        r6 = r6.toString();
        r0.<init>(r6);
        r6 = new java.util.HashMap;
        r6.<init>();
        r1 = r0.keys();
    L_0x004f:
        r2 = r1.hasNext();
        if (r2 == 0) goto L_0x0063;
    L_0x0055:
        r2 = r1.next();
        r2 = (java.lang.String) r2;
        r3 = r0.get(r2);
        r6.put(r2, r3);
        goto L_0x004f;
    L_0x0063:
        return r6;
    L_0x0064:
        r6 = move-exception;
        r3 = r1;
        goto L_0x006d;
    L_0x0067:
        r6 = move-exception;
        throw r6;	 Catch:{ all -> 0x0069 }
    L_0x0069:
        r3 = move-exception;
        r5 = r3;
        r3 = r6;
        r6 = r5;
    L_0x006d:
        if (r3 == 0) goto L_0x0078;
    L_0x006f:
        r2.close();	 Catch:{ Throwable -> 0x0073 }
        goto L_0x007b;
    L_0x0073:
        r2 = move-exception;
        r3.addSuppressed(r2);	 Catch:{ Throwable -> 0x007e }
        goto L_0x007b;
    L_0x0078:
        r2.close();	 Catch:{ Throwable -> 0x007e }
    L_0x007b:
        throw r6;	 Catch:{ Throwable -> 0x007e }
    L_0x007c:
        r6 = move-exception;
        goto L_0x0081;
    L_0x007e:
        r6 = move-exception;
        r1 = r6;
        throw r1;	 Catch:{ all -> 0x007c }
    L_0x0081:
        if (r1 == 0) goto L_0x008c;
    L_0x0083:
        r0.close();	 Catch:{ Throwable -> 0x0087 }
        goto L_0x008f;
    L_0x0087:
        r0 = move-exception;
        r1.addSuppressed(r0);
        goto L_0x008f;
    L_0x008c:
        r0.close();
    L_0x008f:
        throw r6;
    L_0x0090:
        r0 = new java.io.FileNotFoundException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Can't find ";
        r1.append(r2);
        r1.append(r6);
        r6 = r1.toString();
        r0.<init>(r6);
        throw r0;
    L_0x00a7:
        r6 = new java.lang.Exception;
        r0 = "No External Storage Available";
        r6.<init>(r0);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.fileutils.FileUtils.fromJsonOnDisk(java.lang.String):java.util.HashMap");
    }

    public static boolean canReadExternalStorage(Context context) {
        boolean z = true;
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
            z = false;
        }
        return z;
    }

    public static File getFaviconFolder(Context context) {
        File file = new File(context.getFilesDir(), "favicons");
        return !ensureDir(file) ? context.getCacheDir() : file;
    }
}

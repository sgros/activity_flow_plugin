package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class TypefaceCompatUtil {
    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:25:0x0053 in {9, 11, 13, 15, 17, 18, 22, 24} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public static boolean copyToFile(java.io.File r5, java.io.InputStream r6) {
        /*
        r0 = android.os.StrictMode.allowThreadDiskWrites();
        r1 = 0;
        r2 = 0;
        r3 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x002a }
        r3.<init>(r5, r1);	 Catch:{ IOException -> 0x002a }
        r5 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r5 = new byte[r5];	 Catch:{ IOException -> 0x0025, all -> 0x0022 }
        r2 = r6.read(r5);	 Catch:{ IOException -> 0x0025, all -> 0x0022 }
        r4 = -1;	 Catch:{ IOException -> 0x0025, all -> 0x0022 }
        if (r2 == r4) goto L_0x001a;	 Catch:{ IOException -> 0x0025, all -> 0x0022 }
        r3.write(r5, r1, r2);	 Catch:{ IOException -> 0x0025, all -> 0x0022 }
        goto L_0x000f;
        r5 = 1;
        closeQuietly(r3);
        android.os.StrictMode.setThreadPolicy(r0);
        return r5;
        r5 = move-exception;
        r2 = r3;
        goto L_0x004c;
        r5 = move-exception;
        r2 = r3;
        goto L_0x002b;
        r5 = move-exception;
        goto L_0x004c;
        r5 = move-exception;
        r6 = "TypefaceCompatUtil";	 Catch:{ all -> 0x0028 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0028 }
        r3.<init>();	 Catch:{ all -> 0x0028 }
        r4 = "Error copying resource contents to temp file: ";	 Catch:{ all -> 0x0028 }
        r3.append(r4);	 Catch:{ all -> 0x0028 }
        r5 = r5.getMessage();	 Catch:{ all -> 0x0028 }
        r3.append(r5);	 Catch:{ all -> 0x0028 }
        r5 = r3.toString();	 Catch:{ all -> 0x0028 }
        android.util.Log.e(r6, r5);	 Catch:{ all -> 0x0028 }
        closeQuietly(r2);
        android.os.StrictMode.setThreadPolicy(r0);
        return r1;
        closeQuietly(r2);
        android.os.StrictMode.setThreadPolicy(r0);
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.TypefaceCompatUtil.copyToFile(java.io.File, java.io.InputStream):boolean");
    }

    public static File getTempFile(Context context) {
        File cacheDir = context.getCacheDir();
        if (cacheDir == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".font");
        stringBuilder.append(Process.myPid());
        String str = "-";
        stringBuilder.append(str);
        stringBuilder.append(Process.myTid());
        stringBuilder.append(str);
        String stringBuilder2 = stringBuilder.toString();
        int i = 0;
        while (i < 100) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(stringBuilder2);
            stringBuilder3.append(i);
            File file = new File(cacheDir, stringBuilder3.toString());
            try {
                if (file.createNewFile()) {
                    return file;
                }
                i++;
            } catch (IOException unused) {
            }
        }
        return null;
    }

    private static ByteBuffer mmap(File file) {
        Throwable th;
        try {
            ByteBuffer byteBuffer;
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                FileChannel channel = fileInputStream.getChannel();
                MappedByteBuffer map = channel.map(MapMode.READ_ONLY, 0, channel.size());
                fileInputStream.close();
                return map;
            } catch (Throwable th2) {
                Throwable th3 = th2;
                byteBuffer = th;
                th = th3;
            }
            if (byteBuffer != null) {
                try {
                    fileInputStream.close();
                } catch (Throwable unused) {
                }
            } else {
                fileInputStream.close();
            }
            throw th;
            throw th;
        } catch (IOException unused2) {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0048 A:{ExcHandler: all (th java.lang.Throwable), Splitter:B:7:0x0013} */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0053  */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:31:0x0048, code skipped:
            r9 = th;
     */
    /* JADX WARNING: Missing block: B:32:0x0049, code skipped:
            r10 = null;
     */
    /* JADX WARNING: Missing block: B:39:0x0053, code skipped:
            if (r10 != null) goto L_0x0055;
     */
    /* JADX WARNING: Missing block: B:41:?, code skipped:
            r8.close();
     */
    /* JADX WARNING: Missing block: B:43:?, code skipped:
            r8.close();
     */
    public static java.nio.ByteBuffer mmap(android.content.Context r8, android.os.CancellationSignal r9, android.net.Uri r10) {
        /*
        r8 = r8.getContentResolver();
        r0 = 0;
        r1 = "r";
        r8 = r8.openFileDescriptor(r10, r1, r9);	 Catch:{ IOException -> 0x005d }
        if (r8 != 0) goto L_0x0013;
    L_0x000d:
        if (r8 == 0) goto L_0x0012;
    L_0x000f:
        r8.close();	 Catch:{ IOException -> 0x005d }
    L_0x0012:
        return r0;
    L_0x0013:
        r9 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x004b, all -> 0x0048 }
        r10 = r8.getFileDescriptor();	 Catch:{ Throwable -> 0x004b, all -> 0x0048 }
        r9.<init>(r10);	 Catch:{ Throwable -> 0x004b, all -> 0x0048 }
        r1 = r9.getChannel();	 Catch:{ Throwable -> 0x0038, all -> 0x0035 }
        r5 = r1.size();	 Catch:{ Throwable -> 0x0038, all -> 0x0035 }
        r2 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ Throwable -> 0x0038, all -> 0x0035 }
        r3 = 0;
        r10 = r1.map(r2, r3, r5);	 Catch:{ Throwable -> 0x0038, all -> 0x0035 }
        r9.close();	 Catch:{ Throwable -> 0x004b, all -> 0x0048 }
        if (r8 == 0) goto L_0x0034;
    L_0x0031:
        r8.close();	 Catch:{ IOException -> 0x005d }
    L_0x0034:
        return r10;
    L_0x0035:
        r10 = move-exception;
        r1 = r0;
        goto L_0x003e;
    L_0x0038:
        r10 = move-exception;
        throw r10;	 Catch:{ all -> 0x003a }
    L_0x003a:
        r1 = move-exception;
        r7 = r1;
        r1 = r10;
        r10 = r7;
    L_0x003e:
        if (r1 == 0) goto L_0x0044;
    L_0x0040:
        r9.close();	 Catch:{ Throwable -> 0x0047, all -> 0x0048 }
        goto L_0x0047;
    L_0x0044:
        r9.close();	 Catch:{ Throwable -> 0x004b, all -> 0x0048 }
    L_0x0047:
        throw r10;	 Catch:{ Throwable -> 0x004b, all -> 0x0048 }
    L_0x0048:
        r9 = move-exception;
        r10 = r0;
        goto L_0x0051;
    L_0x004b:
        r9 = move-exception;
        throw r9;	 Catch:{ all -> 0x004d }
    L_0x004d:
        r10 = move-exception;
        r7 = r10;
        r10 = r9;
        r9 = r7;
    L_0x0051:
        if (r8 == 0) goto L_0x005c;
    L_0x0053:
        if (r10 == 0) goto L_0x0059;
    L_0x0055:
        r8.close();	 Catch:{ Throwable -> 0x005c }
        goto L_0x005c;
    L_0x0059:
        r8.close();	 Catch:{ IOException -> 0x005d }
    L_0x005c:
        throw r9;	 Catch:{ IOException -> 0x005d }
    L_0x005d:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.TypefaceCompatUtil.mmap(android.content.Context, android.os.CancellationSignal, android.net.Uri):java.nio.ByteBuffer");
    }

    public static ByteBuffer copyToDirectBuffer(Context context, Resources resources, int i) {
        File tempFile = getTempFile(context);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!copyToFile(tempFile, resources, i)) {
                return null;
            }
            ByteBuffer mmap = mmap(tempFile);
            tempFile.delete();
            return mmap;
        } finally {
            tempFile.delete();
        }
    }

    public static boolean copyToFile(File file, Resources resources, int i) {
        Throwable th;
        Closeable openRawResource;
        try {
            openRawResource = resources.openRawResource(i);
            try {
                boolean copyToFile = copyToFile(file, openRawResource);
                closeQuietly(openRawResource);
                return copyToFile;
            } catch (Throwable th2) {
                th = th2;
                closeQuietly(openRawResource);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            openRawResource = null;
            closeQuietly(openRawResource);
            throw th;
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}

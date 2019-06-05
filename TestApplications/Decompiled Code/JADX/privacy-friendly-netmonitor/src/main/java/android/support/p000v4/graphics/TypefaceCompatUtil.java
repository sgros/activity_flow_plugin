package android.support.p000v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.graphics.TypefaceCompatUtil */
public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    private TypefaceCompatUtil() {
    }

    public static File getTempFile(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CACHE_FILE_PREFIX);
        stringBuilder.append(Process.myPid());
        stringBuilder.append("-");
        stringBuilder.append(Process.myTid());
        stringBuilder.append("-");
        String stringBuilder2 = stringBuilder.toString();
        int i = 0;
        while (i < 100) {
            File cacheDir = context.getCacheDir();
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

    @RequiresApi(19)
    private static ByteBuffer mmap(File file) {
        Throwable th;
        Throwable th2;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                FileChannel channel = fileInputStream.getChannel();
                MappedByteBuffer map = channel.map(MapMode.READ_ONLY, 0, channel.size());
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return map;
            } catch (Throwable th22) {
                Throwable th3 = th22;
                th22 = th;
                th = th3;
            }
            throw th;
            if (fileInputStream != null) {
                if (th22 != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th4) {
                        th22.addSuppressed(th4);
                    }
                } else {
                    fileInputStream.close();
                }
            }
            throw th;
        } catch (IOException unused) {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0049 A:{ExcHandler: all (th java.lang.Throwable), Splitter:B:3:0x000b} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:30:0x0049, code skipped:
            r9 = th;
     */
    /* JADX WARNING: Missing block: B:31:0x004a, code skipped:
            r10 = null;
     */
    /* JADX WARNING: Missing block: B:35:0x004e, code skipped:
            r10 = move-exception;
     */
    /* JADX WARNING: Missing block: B:36:0x004f, code skipped:
            r7 = r10;
            r10 = r9;
            r9 = r7;
     */
    @android.support.annotation.RequiresApi(19)
    public static java.nio.ByteBuffer mmap(android.content.Context r8, android.os.CancellationSignal r9, android.net.Uri r10) {
        /*
        r8 = r8.getContentResolver();
        r0 = 0;
        r1 = "r";
        r8 = r8.openFileDescriptor(r10, r1, r9);	 Catch:{ IOException -> 0x0063 }
        r9 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        r10 = r8.getFileDescriptor();	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        r9.<init>(r10);	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        r1 = r9.getChannel();	 Catch:{ Throwable -> 0x0032, all -> 0x002f }
        r5 = r1.size();	 Catch:{ Throwable -> 0x0032, all -> 0x002f }
        r2 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ Throwable -> 0x0032, all -> 0x002f }
        r3 = 0;
        r10 = r1.map(r2, r3, r5);	 Catch:{ Throwable -> 0x0032, all -> 0x002f }
        if (r9 == 0) goto L_0x0029;
    L_0x0026:
        r9.close();	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
    L_0x0029:
        if (r8 == 0) goto L_0x002e;
    L_0x002b:
        r8.close();	 Catch:{ IOException -> 0x0063 }
    L_0x002e:
        return r10;
    L_0x002f:
        r10 = move-exception;
        r1 = r0;
        goto L_0x0038;
    L_0x0032:
        r10 = move-exception;
        throw r10;	 Catch:{ all -> 0x0034 }
    L_0x0034:
        r1 = move-exception;
        r7 = r1;
        r1 = r10;
        r10 = r7;
    L_0x0038:
        if (r9 == 0) goto L_0x0048;
    L_0x003a:
        if (r1 == 0) goto L_0x0045;
    L_0x003c:
        r9.close();	 Catch:{ Throwable -> 0x0040, all -> 0x0049 }
        goto L_0x0048;
    L_0x0040:
        r9 = move-exception;
        r1.addSuppressed(r9);	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        goto L_0x0048;
    L_0x0045:
        r9.close();	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
    L_0x0048:
        throw r10;	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
    L_0x0049:
        r9 = move-exception;
        r10 = r0;
        goto L_0x0052;
    L_0x004c:
        r9 = move-exception;
        throw r9;	 Catch:{ all -> 0x004e }
    L_0x004e:
        r10 = move-exception;
        r7 = r10;
        r10 = r9;
        r9 = r7;
    L_0x0052:
        if (r8 == 0) goto L_0x0062;
    L_0x0054:
        if (r10 == 0) goto L_0x005f;
    L_0x0056:
        r8.close();	 Catch:{ Throwable -> 0x005a }
        goto L_0x0062;
    L_0x005a:
        r8 = move-exception;
        r10.addSuppressed(r8);	 Catch:{ IOException -> 0x0063 }
        goto L_0x0062;
    L_0x005f:
        r8.close();	 Catch:{ IOException -> 0x0063 }
    L_0x0062:
        throw r9;	 Catch:{ IOException -> 0x0063 }
    L_0x0063:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p000v4.graphics.TypefaceCompatUtil.mmap(android.content.Context, android.os.CancellationSignal, android.net.Uri):java.nio.ByteBuffer");
    }

    @RequiresApi(19)
    public static ByteBuffer copyToDirectBuffer(Context context, Resources resources, int i) {
        File tempFile = TypefaceCompatUtil.getTempFile(context);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile(tempFile, resources, i)) {
                return null;
            }
            ByteBuffer mmap = TypefaceCompatUtil.mmap(tempFile);
            tempFile.delete();
            return mmap;
        } finally {
            tempFile.delete();
        }
    }

    public static boolean copyToFile(File file, InputStream inputStream) {
        IOException e;
        String str;
        StringBuilder stringBuilder;
        Throwable th;
        Closeable closeable = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read != -1) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        TypefaceCompatUtil.closeQuietly(fileOutputStream);
                        return true;
                    }
                }
            } catch (IOException e2) {
                e = e2;
                closeable = fileOutputStream;
                try {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Error copying resource contents to temp file: ");
                    stringBuilder.append(e.getMessage());
                    Log.e(str, stringBuilder.toString());
                    TypefaceCompatUtil.closeQuietly(closeable);
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    TypefaceCompatUtil.closeQuietly(closeable);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                closeable = fileOutputStream;
                TypefaceCompatUtil.closeQuietly(closeable);
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Error copying resource contents to temp file: ");
            stringBuilder.append(e.getMessage());
            Log.e(str, stringBuilder.toString());
            TypefaceCompatUtil.closeQuietly(closeable);
            return false;
        }
    }

    public static boolean copyToFile(File file, Resources resources, int i) {
        Throwable th;
        Closeable openRawResource;
        try {
            openRawResource = resources.openRawResource(i);
            try {
                boolean copyToFile = TypefaceCompatUtil.copyToFile(file, openRawResource);
                TypefaceCompatUtil.closeQuietly(openRawResource);
                return copyToFile;
            } catch (Throwable th2) {
                th = th2;
                TypefaceCompatUtil.closeQuietly(openRawResource);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            openRawResource = null;
            TypefaceCompatUtil.closeQuietly(openRawResource);
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

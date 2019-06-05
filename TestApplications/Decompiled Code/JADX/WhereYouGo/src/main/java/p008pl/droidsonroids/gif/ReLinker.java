package p008pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import java.io.Closeable;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* renamed from: pl.droidsonroids.gif.ReLinker */
class ReLinker {
    private static final int COPY_BUFFER_SIZE = 8192;
    private static final String LIB_DIR = "lib";
    private static final String MAPPED_BASE_LIB_NAME = System.mapLibraryName("pl_droidsonroids_gif");
    private static final int MAX_TRIES = 5;

    private ReLinker() {
    }

    @SuppressLint({"UnsafeDynamicallyLoadedCode"})
    static void loadLibrary(Context context) {
        synchronized (ReLinker.class) {
            System.load(ReLinker.unpackLibrary(context).getAbsolutePath());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00cc  */
    /* JADX WARNING: Missing block: B:25:?, code skipped:
            p008pl.droidsonroids.gif.ReLinker.closeSilently(r10);
            p008pl.droidsonroids.gif.ReLinker.closeSilently(r8);
            p008pl.droidsonroids.gif.ReLinker.setFilePermissions(r13);
     */
    private static java.io.File unpackLibrary(android.content.Context r21) {
        /*
        r18 = new java.lang.StringBuilder;
        r18.<init>();
        r19 = MAPPED_BASE_LIB_NAME;
        r18 = r18.append(r19);
        r19 = "1.2.6";
        r18 = r18.append(r19);
        r14 = r18.toString();
        r13 = new java.io.File;
        r18 = "lib";
        r19 = 0;
        r0 = r21;
        r1 = r18;
        r2 = r19;
        r18 = r0.getDir(r1, r2);
        r0 = r18;
        r13.<init>(r0, r14);
        r18 = r13.isFile();
        if (r18 == 0) goto L_0x0032;
    L_0x0030:
        r5 = r13;
    L_0x0031:
        return r5;
    L_0x0032:
        r5 = new java.io.File;
        r18 = r21.getCacheDir();
        r0 = r18;
        r5.<init>(r0, r14);
        r18 = r5.isFile();
        if (r18 != 0) goto L_0x0031;
    L_0x0043:
        r18 = "pl_droidsonroids_gif_surface";
        r12 = java.lang.System.mapLibraryName(r18);
        r9 = new pl.droidsonroids.gif.ReLinker$1;
        r9.<init>(r12);
        p008pl.droidsonroids.gif.ReLinker.clearOldLibraryFiles(r13, r9);
        p008pl.droidsonroids.gif.ReLinker.clearOldLibraryFiles(r5, r9);
        r4 = r21.getApplicationInfo();
        r3 = new java.io.File;
        r0 = r4.sourceDir;
        r18 = r0;
        r0 = r18;
        r3.<init>(r0);
        r17 = 0;
        r17 = p008pl.droidsonroids.gif.ReLinker.openZipFile(r3);	 Catch:{ all -> 0x009d }
        r15 = 0;
        r16 = r15;
    L_0x006c:
        r15 = r16 + 1;
        r18 = 5;
        r0 = r16;
        r1 = r18;
        if (r0 >= r1) goto L_0x00bd;
    L_0x0076:
        r11 = p008pl.droidsonroids.gif.ReLinker.findLibraryEntry(r17);	 Catch:{ all -> 0x009d }
        if (r11 != 0) goto L_0x00a4;
    L_0x007c:
        r18 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x009d }
        r19 = new java.lang.StringBuilder;	 Catch:{ all -> 0x009d }
        r19.<init>();	 Catch:{ all -> 0x009d }
        r20 = "Library ";
        r19 = r19.append(r20);	 Catch:{ all -> 0x009d }
        r20 = MAPPED_BASE_LIB_NAME;	 Catch:{ all -> 0x009d }
        r19 = r19.append(r20);	 Catch:{ all -> 0x009d }
        r20 = " for supported ABIs not found in APK file";
        r19 = r19.append(r20);	 Catch:{ all -> 0x009d }
        r19 = r19.toString();	 Catch:{ all -> 0x009d }
        r18.<init>(r19);	 Catch:{ all -> 0x009d }
        throw r18;	 Catch:{ all -> 0x009d }
    L_0x009d:
        r18 = move-exception;
        if (r17 == 0) goto L_0x00a3;
    L_0x00a0:
        r17.close();	 Catch:{ IOException -> 0x00e0 }
    L_0x00a3:
        throw r18;
    L_0x00a4:
        r10 = 0;
        r7 = 0;
        r0 = r17;
        r10 = r0.getInputStream(r11);	 Catch:{ IOException -> 0x00c5, all -> 0x00d6 }
        r8 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x00c5, all -> 0x00d6 }
        r8.<init>(r13);	 Catch:{ IOException -> 0x00c5, all -> 0x00d6 }
        p008pl.droidsonroids.gif.ReLinker.copy(r10, r8);	 Catch:{ IOException -> 0x00e5, all -> 0x00e2 }
        p008pl.droidsonroids.gif.ReLinker.closeSilently(r10);	 Catch:{ all -> 0x009d }
        p008pl.droidsonroids.gif.ReLinker.closeSilently(r8);	 Catch:{ all -> 0x009d }
        p008pl.droidsonroids.gif.ReLinker.setFilePermissions(r13);	 Catch:{ all -> 0x009d }
    L_0x00bd:
        if (r17 == 0) goto L_0x00c2;
    L_0x00bf:
        r17.close();	 Catch:{ IOException -> 0x00de }
    L_0x00c2:
        r5 = r13;
        goto L_0x0031;
    L_0x00c5:
        r6 = move-exception;
    L_0x00c6:
        r18 = 2;
        r0 = r18;
        if (r15 <= r0) goto L_0x00cd;
    L_0x00cc:
        r13 = r5;
    L_0x00cd:
        p008pl.droidsonroids.gif.ReLinker.closeSilently(r10);	 Catch:{ all -> 0x009d }
        p008pl.droidsonroids.gif.ReLinker.closeSilently(r7);	 Catch:{ all -> 0x009d }
        r16 = r15;
        goto L_0x006c;
    L_0x00d6:
        r18 = move-exception;
    L_0x00d7:
        p008pl.droidsonroids.gif.ReLinker.closeSilently(r10);	 Catch:{ all -> 0x009d }
        p008pl.droidsonroids.gif.ReLinker.closeSilently(r7);	 Catch:{ all -> 0x009d }
        throw r18;	 Catch:{ all -> 0x009d }
    L_0x00de:
        r18 = move-exception;
        goto L_0x00c2;
    L_0x00e0:
        r19 = move-exception;
        goto L_0x00a3;
    L_0x00e2:
        r18 = move-exception;
        r7 = r8;
        goto L_0x00d7;
    L_0x00e5:
        r6 = move-exception;
        r7 = r8;
        goto L_0x00c6;
        */
        throw new UnsupportedOperationException("Method not decompiled: p008pl.droidsonroids.gif.ReLinker.unpackLibrary(android.content.Context):java.io.File");
    }

    private static ZipEntry findLibraryEntry(ZipFile zipFile) {
        for (String abi : ReLinker.getSupportedABIs()) {
            ZipEntry libraryEntry = ReLinker.getEntry(zipFile, abi);
            if (libraryEntry != null) {
                return libraryEntry;
            }
        }
        return null;
    }

    private static String[] getSupportedABIs() {
        if (VERSION.SDK_INT >= 21) {
            return Build.SUPPORTED_ABIS;
        }
        return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
    }

    private static ZipEntry getEntry(ZipFile zipFile, String abi) {
        return zipFile.getEntry("lib/" + abi + "/" + MAPPED_BASE_LIB_NAME);
    }

    private static ZipFile openZipFile(File apkFile) {
        int tries = 0;
        ZipFile zipFile = null;
        while (true) {
            int i = tries;
            tries = i + 1;
            if (i >= 5) {
                break;
            }
            try {
                zipFile = new ZipFile(apkFile, 1);
                break;
            } catch (IOException e) {
            }
        }
        if (zipFile != null) {
            return zipFile;
        }
        throw new IllegalStateException("Could not open APK file: " + apkFile.getAbsolutePath());
    }

    private static void clearOldLibraryFiles(File outputFile, FilenameFilter filter) {
        File[] fileList = outputFile.getParentFile().listFiles(filter);
        if (fileList != null) {
            for (File file : fileList) {
                file.delete();
            }
        }
    }

    @SuppressLint({"SetWorldReadable"})
    private static void setFilePermissions(File outputFile) {
        outputFile.setReadable(true, false);
        outputFile.setExecutable(true, false);
        outputFile.setWritable(true);
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[8192];
        while (true) {
            int bytesRead = in.read(buf);
            if (bytesRead != -1) {
                out.write(buf, 0, bytesRead);
            } else {
                return;
            }
        }
    }

    private static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}

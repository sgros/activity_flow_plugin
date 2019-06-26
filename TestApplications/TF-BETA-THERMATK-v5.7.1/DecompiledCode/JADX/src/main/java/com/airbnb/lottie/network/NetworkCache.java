package com.airbnb.lottie.network;

import android.content.Context;
import com.airbnb.lottie.utils.Logger;
import java.io.File;
import java.io.FileNotFoundException;

class NetworkCache {
    private final Context appContext;
    private final String url;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:21:0x003b in {8, 13, 17, 20} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    java.io.File writeTempCacheFile(java.io.InputStream r5, com.airbnb.lottie.network.FileExtension r6) throws java.io.IOException {
        /*
        r4 = this;
        r0 = r4.url;
        r1 = 1;
        r6 = filenameForUrl(r0, r6, r1);
        r0 = new java.io.File;
        r1 = r4.appContext;
        r1 = r1.getCacheDir();
        r0.<init>(r1, r6);
        r6 = new java.io.FileOutputStream;	 Catch:{ all -> 0x0036 }
        r6.<init>(r0);	 Catch:{ all -> 0x0036 }
        r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r1 = new byte[r1];	 Catch:{ all -> 0x0031 }
        r2 = r5.read(r1);	 Catch:{ all -> 0x0031 }
        r3 = -1;	 Catch:{ all -> 0x0031 }
        if (r2 == r3) goto L_0x0027;	 Catch:{ all -> 0x0031 }
        r3 = 0;	 Catch:{ all -> 0x0031 }
        r6.write(r1, r3, r2);	 Catch:{ all -> 0x0031 }
        goto L_0x001b;	 Catch:{ all -> 0x0031 }
        r6.flush();	 Catch:{ all -> 0x0031 }
        r6.close();	 Catch:{ all -> 0x0036 }
        r5.close();
        return r0;
        r0 = move-exception;
        r6.close();	 Catch:{ all -> 0x0036 }
        throw r0;	 Catch:{ all -> 0x0036 }
        r6 = move-exception;
        r5.close();
        throw r6;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.network.NetworkCache.writeTempCacheFile(java.io.InputStream, com.airbnb.lottie.network.FileExtension):java.io.File");
    }

    NetworkCache(Context context, String str) {
        this.appContext = context.getApplicationContext();
        this.url = str;
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public androidx.core.util.Pair<com.airbnb.lottie.network.FileExtension, java.io.InputStream> fetch() {
        /*
        r5 = this;
        r0 = 0;
        r1 = r5.url;	 Catch:{ FileNotFoundException -> 0x0048 }
        r1 = r5.getCachedFile(r1);	 Catch:{ FileNotFoundException -> 0x0048 }
        if (r1 != 0) goto L_0x000a;
    L_0x0009:
        return r0;
    L_0x000a:
        r2 = new java.io.FileInputStream;	 Catch:{  }
        r2.<init>(r1);	 Catch:{  }
        r0 = r1.getAbsolutePath();
        r3 = ".zip";
        r0 = r0.endsWith(r3);
        if (r0 == 0) goto L_0x001e;
    L_0x001b:
        r0 = com.airbnb.lottie.network.FileExtension.ZIP;
        goto L_0x0020;
    L_0x001e:
        r0 = com.airbnb.lottie.network.FileExtension.JSON;
    L_0x0020:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Cache hit for ";
        r3.append(r4);
        r4 = r5.url;
        r3.append(r4);
        r4 = " at ";
        r3.append(r4);
        r1 = r1.getAbsolutePath();
        r3.append(r1);
        r1 = r3.toString();
        com.airbnb.lottie.utils.Logger.debug(r1);
        r1 = new androidx.core.util.Pair;
        r1.<init>(r0, r2);
        return r1;
    L_0x0048:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.network.NetworkCache.fetch():androidx.core.util.Pair");
    }

    /* Access modifiers changed, original: 0000 */
    public void renameTempFile(FileExtension fileExtension) {
        File file = new File(this.appContext.getCacheDir(), filenameForUrl(this.url, fileExtension, true));
        File file2 = new File(file.getAbsolutePath().replace(".temp", ""));
        boolean renameTo = file.renameTo(file2);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Copying temp file to real file (");
        stringBuilder.append(file2);
        stringBuilder.append(")");
        Logger.debug(stringBuilder.toString());
        if (!renameTo) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unable to rename cache file ");
            stringBuilder2.append(file.getAbsolutePath());
            stringBuilder2.append(" to ");
            stringBuilder2.append(file2.getAbsolutePath());
            stringBuilder2.append(".");
            Logger.warning(stringBuilder2.toString());
        }
    }

    private File getCachedFile(String str) throws FileNotFoundException {
        File file = new File(this.appContext.getCacheDir(), filenameForUrl(str, FileExtension.JSON, false));
        if (file.exists()) {
            return file;
        }
        file = new File(this.appContext.getCacheDir(), filenameForUrl(str, FileExtension.ZIP, false));
        return file.exists() ? file : null;
    }

    private static String filenameForUrl(String str, FileExtension fileExtension, boolean z) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("lottie_cache_");
        stringBuilder.append(str.replaceAll("\\W+", ""));
        stringBuilder.append(z ? fileExtension.tempExtension() : fileExtension.extension);
        return stringBuilder.toString();
    }
}

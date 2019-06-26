package com.airbnb.lottie.network;

import android.content.Context;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieResult;
import com.airbnb.lottie.utils.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipInputStream;

public class NetworkFetcher {
    private final Context appContext;
    private final NetworkCache networkCache;
    private final String url;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:20:0x0035 in {5, 7, 9, 10, 15, 17, 18, 19} preds:[]
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
    private java.lang.String getErrorFromConnection(java.net.HttpURLConnection r3) throws java.io.IOException {
        /*
        r2 = this;
        r3.getResponseCode();
        r0 = new java.io.BufferedReader;
        r1 = new java.io.InputStreamReader;
        r3 = r3.getErrorStream();
        r1.<init>(r3);
        r0.<init>(r1);
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r1 = r0.readLine();	 Catch:{ Exception -> 0x002f }
        if (r1 == 0) goto L_0x0025;	 Catch:{ Exception -> 0x002f }
        r3.append(r1);	 Catch:{ Exception -> 0x002f }
        r1 = 10;	 Catch:{ Exception -> 0x002f }
        r3.append(r1);	 Catch:{ Exception -> 0x002f }
        goto L_0x0016;
        r0.close();	 Catch:{ Exception -> 0x0028 }
        r3 = r3.toString();
        return r3;
        r3 = move-exception;
        goto L_0x0031;
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x002d }
        r0.close();	 Catch:{ Exception -> 0x0034 }
        throw r3;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.network.NetworkFetcher.getErrorFromConnection(java.net.HttpURLConnection):java.lang.String");
    }

    public static LottieResult<LottieComposition> fetchSync(Context context, String str) {
        return new NetworkFetcher(context, str).fetchSync();
    }

    private NetworkFetcher(Context context, String str) {
        this.appContext = context.getApplicationContext();
        this.url = str;
        this.networkCache = new NetworkCache(this.appContext, str);
    }

    public LottieResult<LottieComposition> fetchSync() {
        Object fetchFromCache = fetchFromCache();
        if (fetchFromCache != null) {
            return new LottieResult(fetchFromCache);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Animation for ");
        stringBuilder.append(this.url);
        stringBuilder.append(" not found in cache. Fetching from network.");
        Logger.debug(stringBuilder.toString());
        return fetchFromNetwork();
    }

    private LottieComposition fetchFromCache() {
        Pair fetch = this.networkCache.fetch();
        if (fetch == null) {
            return null;
        }
        LottieResult fromZipStreamSync;
        FileExtension fileExtension = (FileExtension) fetch.first;
        InputStream inputStream = (InputStream) fetch.second;
        if (fileExtension == FileExtension.ZIP) {
            fromZipStreamSync = LottieCompositionFactory.fromZipStreamSync(new ZipInputStream(inputStream), this.url);
        } else {
            fromZipStreamSync = LottieCompositionFactory.fromJsonInputStreamSync(inputStream, this.url);
        }
        if (fromZipStreamSync.getValue() != null) {
            return (LottieComposition) fromZipStreamSync.getValue();
        }
        return null;
    }

    private LottieResult<LottieComposition> fetchFromNetwork() {
        try {
            return fetchFromNetworkInternal();
        } catch (IOException e) {
            return new LottieResult(e);
        }
    }

    private LottieResult fetchFromNetworkInternal() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fetching ");
        stringBuilder.append(this.url);
        Logger.debug(stringBuilder.toString());
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.url).openConnection();
        httpURLConnection.setRequestMethod("GET");
        LottieResult lottieResult;
        try {
            httpURLConnection.connect();
            if (httpURLConnection.getErrorStream() == null) {
                int responseCode = httpURLConnection.getResponseCode();
                lottieResult = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                if (responseCode == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                    LottieResult resultFromConnection = getResultFromConnection(httpURLConnection);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Completed fetch from network. Success: ");
                    stringBuilder2.append(resultFromConnection.getValue() != null);
                    Logger.debug(stringBuilder2.toString());
                    httpURLConnection.disconnect();
                    return resultFromConnection;
                }
            }
            String errorFromConnection = getErrorFromConnection(httpURLConnection);
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Unable to fetch ");
            stringBuilder3.append(this.url);
            stringBuilder3.append(". Failed with ");
            stringBuilder3.append(httpURLConnection.getResponseCode());
            stringBuilder3.append("\n");
            stringBuilder3.append(errorFromConnection);
            lottieResult = new LottieResult(new IllegalArgumentException(stringBuilder3.toString()));
            return lottieResult;
        } catch (Exception e) {
            lottieResult = new LottieResult(e);
            return lottieResult;
        } finally {
            httpURLConnection.disconnect();
        }
    }

    private LottieResult<LottieComposition> getResultFromConnection(HttpURLConnection httpURLConnection) throws IOException {
        FileExtension fileExtension;
        LottieResult fromJsonInputStreamSync;
        String contentType = httpURLConnection.getContentType();
        String str = "application/json";
        if (contentType == null) {
            contentType = str;
        }
        Object obj = -1;
        int hashCode = contentType.hashCode();
        if (hashCode != -1248325150) {
            if (hashCode == -43840953 && contentType.equals(str)) {
                obj = 1;
            }
        } else if (contentType.equals("application/zip")) {
            obj = null;
        }
        if (obj != null) {
            Logger.debug("Received json response.");
            fileExtension = FileExtension.JSON;
            fromJsonInputStreamSync = LottieCompositionFactory.fromJsonInputStreamSync(new FileInputStream(new File(this.networkCache.writeTempCacheFile(httpURLConnection.getInputStream(), fileExtension).getAbsolutePath())), this.url);
        } else {
            Logger.debug("Handling zip response.");
            fileExtension = FileExtension.ZIP;
            fromJsonInputStreamSync = LottieCompositionFactory.fromZipStreamSync(new ZipInputStream(new FileInputStream(this.networkCache.writeTempCacheFile(httpURLConnection.getInputStream(), fileExtension))), this.url);
        }
        if (fromJsonInputStreamSync.getValue() != null) {
            this.networkCache.renameTempFile(fileExtension);
        }
        return fromJsonInputStreamSync;
    }
}

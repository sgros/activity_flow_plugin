package com.airbnb.lottie.network;

import android.content.Context;
import android.support.p001v4.util.Pair;
import com.airbnb.lottie.C0352L;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieResult;
import com.airbnb.lottie.LottieTask;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.zip.ZipInputStream;

public class NetworkFetcher {
    private final Context appContext;
    private final NetworkCache networkCache;
    private final String url;

    /* renamed from: com.airbnb.lottie.network.NetworkFetcher$1 */
    class C03821 implements Callable<LottieResult<LottieComposition>> {
        C03821() {
        }

        public LottieResult<LottieComposition> call() throws Exception {
            return NetworkFetcher.this.fetchSync();
        }
    }

    public static LottieTask<LottieComposition> fetch(Context context, String str) {
        return new NetworkFetcher(context, str).fetch();
    }

    private NetworkFetcher(Context context, String str) {
        this.appContext = context.getApplicationContext();
        this.url = str;
        this.networkCache = new NetworkCache(this.appContext, str);
    }

    private LottieTask<LottieComposition> fetch() {
        return new LottieTask(new C03821());
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
        C0352L.debug(stringBuilder.toString());
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
        if (fileExtension == FileExtension.Zip) {
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
        C0352L.debug(stringBuilder.toString());
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.url).openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        if (httpURLConnection.getErrorStream() == null && httpURLConnection.getResponseCode() == 200) {
            FileExtension fileExtension;
            LottieResult fromJsonInputStreamSync;
            String contentType = httpURLConnection.getContentType();
            Object obj = -1;
            int hashCode = contentType.hashCode();
            boolean z = true;
            if (hashCode != -1248325150) {
                if (hashCode == -43840953 && contentType.equals("application/json")) {
                    obj = 1;
                }
            } else if (contentType.equals("application/zip")) {
                obj = null;
            }
            if (obj != null) {
                C0352L.debug("Received json response.");
                fileExtension = FileExtension.Json;
                fromJsonInputStreamSync = LottieCompositionFactory.fromJsonInputStreamSync(new FileInputStream(new File(this.networkCache.writeTempCacheFile(httpURLConnection.getInputStream(), fileExtension).getAbsolutePath())), this.url);
            } else {
                C0352L.debug("Handling zip response.");
                fileExtension = FileExtension.Zip;
                fromJsonInputStreamSync = LottieCompositionFactory.fromZipStreamSync(new ZipInputStream(new FileInputStream(this.networkCache.writeTempCacheFile(httpURLConnection.getInputStream(), fileExtension))), this.url);
            }
            if (fromJsonInputStreamSync.getValue() != null) {
                this.networkCache.renameTempFile(fileExtension);
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Completed fetch from network. Success: ");
            if (fromJsonInputStreamSync.getValue() == null) {
                z = false;
            }
            stringBuilder2.append(z);
            C0352L.debug(stringBuilder2.toString());
            return fromJsonInputStreamSync;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
        StringBuilder stringBuilder3 = new StringBuilder();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                stringBuilder3.append(readLine);
                stringBuilder3.append(10);
            } else {
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("Unable to fetch ");
                stringBuilder4.append(this.url);
                stringBuilder4.append(". Failed with ");
                stringBuilder4.append(httpURLConnection.getResponseCode());
                stringBuilder4.append("\n");
                stringBuilder4.append(stringBuilder3);
                return new LottieResult(new IllegalArgumentException(stringBuilder4.toString()));
            }
        }
    }
}

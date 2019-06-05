// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.network;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.net.URL;
import java.net.HttpURLConnection;
import com.airbnb.lottie.L;
import java.io.IOException;
import android.support.v4.util.Pair;
import com.airbnb.lottie.LottieCompositionFactory;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import com.airbnb.lottie.LottieResult;
import java.util.concurrent.Callable;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieTask;
import android.content.Context;

public class NetworkFetcher
{
    private final Context appContext;
    private final NetworkCache networkCache;
    private final String url;
    
    private NetworkFetcher(final Context context, final String url) {
        this.appContext = context.getApplicationContext();
        this.url = url;
        this.networkCache = new NetworkCache(this.appContext, url);
    }
    
    private LottieTask<LottieComposition> fetch() {
        return new LottieTask<LottieComposition>(new Callable<LottieResult<LottieComposition>>() {
            @Override
            public LottieResult<LottieComposition> call() throws Exception {
                return NetworkFetcher.this.fetchSync();
            }
        });
    }
    
    public static LottieTask<LottieComposition> fetch(final Context context, final String s) {
        return new NetworkFetcher(context, s).fetch();
    }
    
    private LottieComposition fetchFromCache() {
        final Pair<FileExtension, InputStream> fetch = this.networkCache.fetch();
        if (fetch == null) {
            return null;
        }
        final FileExtension fileExtension = fetch.first;
        final InputStream in = fetch.second;
        LottieResult<LottieComposition> lottieResult;
        if (fileExtension == FileExtension.Zip) {
            lottieResult = LottieCompositionFactory.fromZipStreamSync(new ZipInputStream(in), this.url);
        }
        else {
            lottieResult = LottieCompositionFactory.fromJsonInputStreamSync(in, this.url);
        }
        if (lottieResult.getValue() != null) {
            return lottieResult.getValue();
        }
        return null;
    }
    
    private LottieResult<LottieComposition> fetchFromNetwork() {
        try {
            return (LottieResult<LottieComposition>)this.fetchFromNetworkInternal();
        }
        catch (IOException ex) {
            return new LottieResult<LottieComposition>(ex);
        }
    }
    
    private LottieResult fetchFromNetworkInternal() throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("Fetching ");
        sb.append(this.url);
        L.debug(sb.toString());
        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(this.url).openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        if (httpURLConnection.getErrorStream() == null && httpURLConnection.getResponseCode() == 200) {
            final String contentType = httpURLConnection.getContentType();
            int n = -1;
            final int hashCode = contentType.hashCode();
            boolean b = true;
            if (hashCode != -1248325150) {
                if (hashCode == -43840953) {
                    if (contentType.equals("application/json")) {
                        n = 1;
                    }
                }
            }
            else if (contentType.equals("application/zip")) {
                n = 0;
            }
            FileExtension fileExtension;
            LottieResult<LottieComposition> lottieResult;
            if (n != 0) {
                L.debug("Received json response.");
                fileExtension = FileExtension.Json;
                lottieResult = LottieCompositionFactory.fromJsonInputStreamSync(new FileInputStream(new File(this.networkCache.writeTempCacheFile(httpURLConnection.getInputStream(), fileExtension).getAbsolutePath())), this.url);
            }
            else {
                L.debug("Handling zip response.");
                fileExtension = FileExtension.Zip;
                lottieResult = LottieCompositionFactory.fromZipStreamSync(new ZipInputStream(new FileInputStream(this.networkCache.writeTempCacheFile(httpURLConnection.getInputStream(), fileExtension))), this.url);
            }
            if (lottieResult.getValue() != null) {
                this.networkCache.renameTempFile(fileExtension);
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Completed fetch from network. Success: ");
            if (lottieResult.getValue() == null) {
                b = false;
            }
            sb2.append(b);
            L.debug(sb2.toString());
            return lottieResult;
        }
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
        final StringBuilder obj = new StringBuilder();
        while (true) {
            final String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            obj.append(line);
            obj.append('\n');
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Unable to fetch ");
        sb3.append(this.url);
        sb3.append(". Failed with ");
        sb3.append(httpURLConnection.getResponseCode());
        sb3.append("\n");
        sb3.append((Object)obj);
        return new LottieResult(new IllegalArgumentException(sb3.toString()));
    }
    
    public LottieResult<LottieComposition> fetchSync() {
        final LottieComposition fetchFromCache = this.fetchFromCache();
        if (fetchFromCache != null) {
            return new LottieResult<LottieComposition>(fetchFromCache);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Animation for ");
        sb.append(this.url);
        sb.append(" not found in cache. Fetching from network.");
        L.debug(sb.toString());
        return this.fetchFromNetwork();
    }
}

package org.mozilla.cachedrequestloader;

import android.content.Context;
import android.net.TrafficStats;
import android.support.p001v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.fileutils.FileUtils.DeleteFileRunnable;
import org.mozilla.fileutils.FileUtils.GetCache;
import org.mozilla.fileutils.FileUtils.WriteStringToFileRunnable;
import org.mozilla.httprequest.HttpRequest;
import org.mozilla.threadutils.ThreadUtils;

public class BackgroundCachedRequestLoader implements RequestLoader {
    private static final ExecutorService backgroundExecutorService = Executors.newFixedThreadPool(2);
    private boolean delayCacheLoad;
    private boolean delayNetworkLoad;
    private RequestLoaderDelegation requestLoaderDelegation;

    public BackgroundCachedRequestLoader(Context context, String str, String str2, String str3, int i, boolean z) {
        this.delayCacheLoad = false;
        this.delayNetworkLoad = false;
        this.requestLoaderDelegation = new RequestLoaderDelegation(context, str, str2, str3, i, z, this);
    }

    public BackgroundCachedRequestLoader(Context context, String str, String str2, String str3, int i) {
        this(context, str, str2, str3, i, false);
    }

    public ResponseData getStringLiveData() {
        return this.requestLoaderDelegation.getStringLiveData();
    }

    public void loadFromCache(Context context, String str, ResponseData responseData) {
        backgroundExecutorService.submit(new lambda(this, context, str, responseData));
    }

    public static /* synthetic */ void lambda$loadFromCache$0(BackgroundCachedRequestLoader backgroundCachedRequestLoader, Context context, String str, ResponseData responseData) {
        try {
            backgroundCachedRequestLoader.sleepIfTesting(context, backgroundCachedRequestLoader.delayCacheLoad);
            responseData.postValue(new Pair(Integer.valueOf(1), FileUtils.readStringFromFile(new GetCache(new WeakReference(context)).get(), str)));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.e("CachedRequestLoader", "Failed to open Cache directory when reading cached banner config");
        }
    }

    public void loadFromRemote(Context context, ResponseData responseData, String str, String str2, int i) {
        backgroundExecutorService.submit(new C0422x2468b964(this, i, context, str, str2, responseData));
    }

    public static /* synthetic */ void lambda$loadFromRemote$1(BackgroundCachedRequestLoader backgroundCachedRequestLoader, int i, Context context, String str, String str2, ResponseData responseData) {
        TrafficStats.setThreadStatsTag(i);
        try {
            backgroundCachedRequestLoader.sleepIfTesting(context, backgroundCachedRequestLoader.delayNetworkLoad);
            String replace = HttpRequest.get(new URL(str), str2).replace("\n", "");
            responseData.postValue(new Pair(Integer.valueOf(0), replace));
            if (TextUtils.isEmpty(replace)) {
                backgroundCachedRequestLoader.requestLoaderDelegation.deleteCache();
            } else {
                backgroundCachedRequestLoader.requestLoaderDelegation.writeToCache(replace);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void writeToCache(String str, Context context, String str2) {
        try {
            ThreadUtils.postToBackgroundThread(new WriteStringToFileRunnable(new File(new GetCache(new WeakReference(context)).get(), str2), str));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.e("CachedRequestLoader", "Failed to open cache directory when writing to cache.");
        }
    }

    public void deleteCache(Context context, String str) {
        try {
            ThreadUtils.postToBackgroundThread(new DeleteFileRunnable(new File(new GetCache(new WeakReference(context)).get(), str)));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.e("CachedRequestLoader", "Failed to open cache directory when deleting cache.");
        }
    }

    private void sleepIfTesting(Context context, boolean z) {
        if (!context.getResources().getBoolean(C0423R.bool.isAndroidTest) && z) {
            throw new IllegalStateException("Delays are only available in testing.");
        } else if (z) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

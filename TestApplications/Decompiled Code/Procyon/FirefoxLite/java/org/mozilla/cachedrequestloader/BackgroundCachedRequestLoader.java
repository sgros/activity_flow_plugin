// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.cachedrequestloader;

import org.mozilla.threadutils.ThreadUtils;
import java.io.File;
import java.net.MalformedURLException;
import android.text.TextUtils;
import org.mozilla.httprequest.HttpRequest;
import java.net.URL;
import android.net.TrafficStats;
import java.util.concurrent.ExecutionException;
import android.util.Log;
import android.support.v4.util.Pair;
import org.mozilla.fileutils.FileUtils;
import java.lang.ref.WeakReference;
import android.content.Context;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class BackgroundCachedRequestLoader implements RequestLoader
{
    private static final ExecutorService backgroundExecutorService;
    private boolean delayCacheLoad;
    private boolean delayNetworkLoad;
    private RequestLoaderDelegation requestLoaderDelegation;
    
    static {
        backgroundExecutorService = Executors.newFixedThreadPool(2);
    }
    
    public BackgroundCachedRequestLoader(final Context context, final String s, final String s2, final String s3, final int n) {
        this(context, s, s2, s3, n, false);
    }
    
    public BackgroundCachedRequestLoader(final Context context, final String s, final String s2, final String s3, final int n, final boolean b) {
        this.delayCacheLoad = false;
        this.delayNetworkLoad = false;
        this.requestLoaderDelegation = new RequestLoaderDelegation(context, s, s2, s3, n, b, (RequestLoaderDelegation.RequestLoader)this);
    }
    
    private void sleepIfTesting(final Context context, final boolean b) {
        if (!context.getResources().getBoolean(R.bool.isAndroidTest) && b) {
            throw new IllegalStateException("Delays are only available in testing.");
        }
        if (!b) {
            return;
        }
        try {
            Thread.sleep(300L);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void deleteCache(final Context referent, final String child) {
        try {
            ThreadUtils.postToBackgroundThread(new FileUtils.DeleteFileRunnable(new File(new FileUtils.GetCache(new WeakReference<Context>(referent)).get(), child)));
        }
        catch (ExecutionException | InterruptedException ex) {
            final Throwable t;
            t.printStackTrace();
            Log.e("CachedRequestLoader", "Failed to open cache directory when deleting cache.");
        }
    }
    
    public ResponseData getStringLiveData() {
        return this.requestLoaderDelegation.getStringLiveData();
    }
    
    @Override
    public void loadFromCache(final Context context, final String s, final ResponseData responseData) {
        BackgroundCachedRequestLoader.backgroundExecutorService.submit(new _$$Lambda$BackgroundCachedRequestLoader$EHwI_s8H_FtNt9t2MTbwyEF0UBA(this, context, s, responseData));
    }
    
    @Override
    public void loadFromRemote(final Context context, final ResponseData responseData, final String s, final String s2, final int n) {
        BackgroundCachedRequestLoader.backgroundExecutorService.submit(new _$$Lambda$BackgroundCachedRequestLoader$b0Blxy5LETn8C0N3GuPfyvuqAPM(this, n, context, s, s2, responseData));
    }
    
    @Override
    public void writeToCache(final String s, final Context referent, final String child) {
        try {
            ThreadUtils.postToBackgroundThread(new FileUtils.WriteStringToFileRunnable(new File(new FileUtils.GetCache(new WeakReference<Context>(referent)).get(), child), s));
        }
        catch (ExecutionException | InterruptedException ex) {
            final Throwable t;
            t.printStackTrace();
            Log.e("CachedRequestLoader", "Failed to open cache directory when writing to cache.");
        }
    }
}

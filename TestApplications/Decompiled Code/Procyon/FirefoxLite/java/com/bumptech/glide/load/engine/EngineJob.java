// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import android.os.Message;
import java.util.Iterator;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import android.os.Handler$Callback;
import android.os.Looper;
import com.bumptech.glide.util.pool.StateVerifier;
import android.support.v4.util.Pools;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.ResourceCallback;
import java.util.List;
import android.os.Handler;
import com.bumptech.glide.util.pool.FactoryPools;

class EngineJob<R> implements Callback<R>, Poolable
{
    private static final EngineResourceFactory DEFAULT_FACTORY;
    private static final Handler MAIN_THREAD_HANDLER;
    private final List<ResourceCallback> cbs;
    private DataSource dataSource;
    private DecodeJob<R> decodeJob;
    private final GlideExecutor diskCacheExecutor;
    private EngineResource<?> engineResource;
    private final EngineResourceFactory engineResourceFactory;
    private GlideException exception;
    private boolean hasLoadFailed;
    private boolean hasResource;
    private List<ResourceCallback> ignoredCallbacks;
    private boolean isCacheable;
    private volatile boolean isCancelled;
    private Key key;
    private final EngineJobListener listener;
    private final Pools.Pool<EngineJob<?>> pool;
    private Resource<?> resource;
    private final GlideExecutor sourceExecutor;
    private final GlideExecutor sourceUnlimitedExecutor;
    private final StateVerifier stateVerifier;
    private boolean useUnlimitedSourceGeneratorPool;
    
    static {
        DEFAULT_FACTORY = new EngineResourceFactory();
        MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper(), (Handler$Callback)new MainThreadCallback());
    }
    
    EngineJob(final GlideExecutor glideExecutor, final GlideExecutor glideExecutor2, final GlideExecutor glideExecutor3, final EngineJobListener engineJobListener, final Pools.Pool<EngineJob<?>> pool) {
        this(glideExecutor, glideExecutor2, glideExecutor3, engineJobListener, pool, EngineJob.DEFAULT_FACTORY);
    }
    
    EngineJob(final GlideExecutor diskCacheExecutor, final GlideExecutor sourceExecutor, final GlideExecutor sourceUnlimitedExecutor, final EngineJobListener listener, final Pools.Pool<EngineJob<?>> pool, final EngineResourceFactory engineResourceFactory) {
        this.cbs = new ArrayList<ResourceCallback>(2);
        this.stateVerifier = StateVerifier.newInstance();
        this.diskCacheExecutor = diskCacheExecutor;
        this.sourceExecutor = sourceExecutor;
        this.sourceUnlimitedExecutor = sourceUnlimitedExecutor;
        this.listener = listener;
        this.pool = pool;
        this.engineResourceFactory = engineResourceFactory;
    }
    
    private void addIgnoredCallback(final ResourceCallback resourceCallback) {
        if (this.ignoredCallbacks == null) {
            this.ignoredCallbacks = new ArrayList<ResourceCallback>(2);
        }
        if (!this.ignoredCallbacks.contains(resourceCallback)) {
            this.ignoredCallbacks.add(resourceCallback);
        }
    }
    
    private GlideExecutor getActiveSourceExecutor() {
        GlideExecutor glideExecutor;
        if (this.useUnlimitedSourceGeneratorPool) {
            glideExecutor = this.sourceUnlimitedExecutor;
        }
        else {
            glideExecutor = this.sourceExecutor;
        }
        return glideExecutor;
    }
    
    private boolean isInIgnoredCallbacks(final ResourceCallback resourceCallback) {
        return this.ignoredCallbacks != null && this.ignoredCallbacks.contains(resourceCallback);
    }
    
    private void release(final boolean b) {
        Util.assertMainThread();
        this.cbs.clear();
        this.key = null;
        this.engineResource = null;
        this.resource = null;
        if (this.ignoredCallbacks != null) {
            this.ignoredCallbacks.clear();
        }
        this.hasLoadFailed = false;
        this.isCancelled = false;
        this.hasResource = false;
        this.decodeJob.release(b);
        this.decodeJob = null;
        this.exception = null;
        this.dataSource = null;
        this.pool.release(this);
    }
    
    public void addCallback(final ResourceCallback resourceCallback) {
        Util.assertMainThread();
        this.stateVerifier.throwIfRecycled();
        if (this.hasResource) {
            resourceCallback.onResourceReady(this.engineResource, this.dataSource);
        }
        else if (this.hasLoadFailed) {
            resourceCallback.onLoadFailed(this.exception);
        }
        else {
            this.cbs.add(resourceCallback);
        }
    }
    
    void cancel() {
        if (!this.hasLoadFailed && !this.hasResource && !this.isCancelled) {
            this.isCancelled = true;
            this.decodeJob.cancel();
            this.listener.onEngineJobCancelled(this, this.key);
        }
    }
    
    @Override
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }
    
    void handleCancelledOnMainThread() {
        this.stateVerifier.throwIfRecycled();
        if (this.isCancelled) {
            this.listener.onEngineJobCancelled(this, this.key);
            this.release(false);
            return;
        }
        throw new IllegalStateException("Not cancelled");
    }
    
    void handleExceptionOnMainThread() {
        this.stateVerifier.throwIfRecycled();
        if (this.isCancelled) {
            this.release(false);
            return;
        }
        if (this.cbs.isEmpty()) {
            throw new IllegalStateException("Received an exception without any callbacks to notify");
        }
        if (!this.hasLoadFailed) {
            this.hasLoadFailed = true;
            this.listener.onEngineJobComplete(this.key, null);
            for (final ResourceCallback resourceCallback : this.cbs) {
                if (!this.isInIgnoredCallbacks(resourceCallback)) {
                    resourceCallback.onLoadFailed(this.exception);
                }
            }
            this.release(false);
            return;
        }
        throw new IllegalStateException("Already failed once");
    }
    
    void handleResultOnMainThread() {
        this.stateVerifier.throwIfRecycled();
        if (this.isCancelled) {
            this.resource.recycle();
            this.release(false);
            return;
        }
        if (this.cbs.isEmpty()) {
            throw new IllegalStateException("Received a resource without any callbacks to notify");
        }
        if (!this.hasResource) {
            this.engineResource = this.engineResourceFactory.build(this.resource, this.isCacheable);
            this.hasResource = true;
            this.engineResource.acquire();
            this.listener.onEngineJobComplete(this.key, this.engineResource);
            for (final ResourceCallback resourceCallback : this.cbs) {
                if (!this.isInIgnoredCallbacks(resourceCallback)) {
                    this.engineResource.acquire();
                    resourceCallback.onResourceReady(this.engineResource, this.dataSource);
                }
            }
            this.engineResource.release();
            this.release(false);
            return;
        }
        throw new IllegalStateException("Already have resource");
    }
    
    EngineJob<R> init(final Key key, final boolean isCacheable, final boolean useUnlimitedSourceGeneratorPool) {
        this.key = key;
        this.isCacheable = isCacheable;
        this.useUnlimitedSourceGeneratorPool = useUnlimitedSourceGeneratorPool;
        return this;
    }
    
    @Override
    public void onLoadFailed(final GlideException exception) {
        this.exception = exception;
        EngineJob.MAIN_THREAD_HANDLER.obtainMessage(2, (Object)this).sendToTarget();
    }
    
    @Override
    public void onResourceReady(final Resource<R> resource, final DataSource dataSource) {
        this.resource = resource;
        this.dataSource = dataSource;
        EngineJob.MAIN_THREAD_HANDLER.obtainMessage(1, (Object)this).sendToTarget();
    }
    
    public void removeCallback(final ResourceCallback resourceCallback) {
        Util.assertMainThread();
        this.stateVerifier.throwIfRecycled();
        if (!this.hasResource && !this.hasLoadFailed) {
            this.cbs.remove(resourceCallback);
            if (this.cbs.isEmpty()) {
                this.cancel();
            }
        }
        else {
            this.addIgnoredCallback(resourceCallback);
        }
    }
    
    @Override
    public void reschedule(final DecodeJob<?> decodeJob) {
        this.getActiveSourceExecutor().execute(decodeJob);
    }
    
    public void start(final DecodeJob<R> decodeJob) {
        this.decodeJob = decodeJob;
        GlideExecutor glideExecutor;
        if (decodeJob.willDecodeFromCache()) {
            glideExecutor = this.diskCacheExecutor;
        }
        else {
            glideExecutor = this.getActiveSourceExecutor();
        }
        glideExecutor.execute(decodeJob);
    }
    
    static class EngineResourceFactory
    {
        public <R> EngineResource<R> build(final Resource<R> resource, final boolean b) {
            return new EngineResource<R>(resource, b);
        }
    }
    
    private static class MainThreadCallback implements Handler$Callback
    {
        MainThreadCallback() {
        }
        
        public boolean handleMessage(final Message message) {
            final EngineJob engineJob = (EngineJob)message.obj;
            switch (message.what) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized message: ");
                    sb.append(message.what);
                    throw new IllegalStateException(sb.toString());
                }
                case 3: {
                    engineJob.handleCancelledOnMainThread();
                    break;
                }
                case 2: {
                    engineJob.handleExceptionOnMainThread();
                    break;
                }
                case 1: {
                    engineJob.handleResultOnMainThread();
                    break;
                }
            }
            return true;
        }
    }
}

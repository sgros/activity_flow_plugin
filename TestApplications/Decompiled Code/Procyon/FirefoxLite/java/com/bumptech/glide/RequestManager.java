// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide;

import com.bumptech.glide.request.Request;
import java.util.Iterator;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import android.os.Looper;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import android.graphics.Bitmap;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.TargetTracker;
import com.bumptech.glide.manager.RequestTracker;
import android.os.Handler;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.manager.LifecycleListener;

public class RequestManager implements LifecycleListener
{
    private static final RequestOptions DECODE_TYPE_BITMAP;
    private static final RequestOptions DOWNLOAD_ONLY_OPTIONS;
    private final Runnable addSelfToLifecycle;
    private final ConnectivityMonitor connectivityMonitor;
    protected final Glide glide;
    final Lifecycle lifecycle;
    private final Handler mainHandler;
    private RequestOptions requestOptions;
    private final RequestTracker requestTracker;
    private final TargetTracker targetTracker;
    private final RequestManagerTreeNode treeNode;
    
    static {
        DECODE_TYPE_BITMAP = RequestOptions.decodeTypeOf(Bitmap.class).lock();
        DOWNLOAD_ONLY_OPTIONS = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA).priority(Priority.LOW).skipMemoryCache(true);
    }
    
    public RequestManager(final Glide glide, final Lifecycle lifecycle, final RequestManagerTreeNode requestManagerTreeNode) {
        this(glide, lifecycle, requestManagerTreeNode, new RequestTracker(), glide.getConnectivityMonitorFactory());
    }
    
    RequestManager(final Glide glide, final Lifecycle lifecycle, final RequestManagerTreeNode treeNode, final RequestTracker requestTracker, final ConnectivityMonitorFactory connectivityMonitorFactory) {
        this.targetTracker = new TargetTracker();
        this.addSelfToLifecycle = new Runnable() {
            @Override
            public void run() {
                RequestManager.this.lifecycle.addListener(RequestManager.this);
            }
        };
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.glide = glide;
        this.lifecycle = lifecycle;
        this.treeNode = treeNode;
        this.requestTracker = requestTracker;
        this.connectivityMonitor = connectivityMonitorFactory.build(glide.getGlideContext().getBaseContext(), new RequestManagerConnectivityListener(requestTracker));
        if (Util.isOnBackgroundThread()) {
            this.mainHandler.post(this.addSelfToLifecycle);
        }
        else {
            lifecycle.addListener(this);
        }
        lifecycle.addListener(this.connectivityMonitor);
        this.setRequestOptions(glide.getGlideContext().getDefaultRequestOptions());
        glide.registerRequestManager(this);
    }
    
    private void untrackOrDelegate(final Target<?> target) {
        if (!this.untrack(target)) {
            this.glide.removeFromManagers(target);
        }
    }
    
    public <ResourceType> RequestBuilder<ResourceType> as(final Class<ResourceType> clazz) {
        return new RequestBuilder<ResourceType>(this.glide, this, clazz);
    }
    
    public RequestBuilder<Bitmap> asBitmap() {
        return this.as(Bitmap.class).apply(RequestManager.DECODE_TYPE_BITMAP);
    }
    
    public RequestBuilder<Drawable> asDrawable() {
        return this.as(Drawable.class);
    }
    
    public void clear(final Target<?> target) {
        if (target == null) {
            return;
        }
        if (Util.isOnMainThread()) {
            this.untrackOrDelegate(target);
        }
        else {
            this.mainHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    RequestManager.this.clear(target);
                }
            });
        }
    }
    
    RequestOptions getDefaultRequestOptions() {
        return this.requestOptions;
    }
    
     <T> TransitionOptions<?, T> getDefaultTransitionOptions(final Class<T> clazz) {
        return this.glide.getGlideContext().getDefaultTransitionOptions(clazz);
    }
    
    public RequestBuilder<Drawable> load(final Object o) {
        return this.asDrawable().load(o);
    }
    
    @Override
    public void onDestroy() {
        this.targetTracker.onDestroy();
        final Iterator<Target<?>> iterator = this.targetTracker.getAll().iterator();
        while (iterator.hasNext()) {
            this.clear(iterator.next());
        }
        this.targetTracker.clear();
        this.requestTracker.clearRequests();
        this.lifecycle.removeListener(this);
        this.lifecycle.removeListener(this.connectivityMonitor);
        this.mainHandler.removeCallbacks(this.addSelfToLifecycle);
        this.glide.unregisterRequestManager(this);
    }
    
    @Override
    public void onStart() {
        this.resumeRequests();
        this.targetTracker.onStart();
    }
    
    @Override
    public void onStop() {
        this.pauseRequests();
        this.targetTracker.onStop();
    }
    
    public void pauseRequests() {
        Util.assertMainThread();
        this.requestTracker.pauseRequests();
    }
    
    public void resumeRequests() {
        Util.assertMainThread();
        this.requestTracker.resumeRequests();
    }
    
    protected void setRequestOptions(final RequestOptions requestOptions) {
        this.requestOptions = requestOptions.clone().autoClone();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("{tracker=");
        sb.append(this.requestTracker);
        sb.append(", treeNode=");
        sb.append(this.treeNode);
        sb.append("}");
        return sb.toString();
    }
    
    void track(final Target<?> target, final Request request) {
        this.targetTracker.track(target);
        this.requestTracker.runRequest(request);
    }
    
    boolean untrack(final Target<?> target) {
        final Request request = target.getRequest();
        if (request == null) {
            return true;
        }
        if (this.requestTracker.clearRemoveAndRecycle(request)) {
            this.targetTracker.untrack(target);
            target.setRequest(null);
            return true;
        }
        return false;
    }
    
    private static class RequestManagerConnectivityListener implements ConnectivityListener
    {
        private final RequestTracker requestTracker;
        
        public RequestManagerConnectivityListener(final RequestTracker requestTracker) {
            this.requestTracker = requestTracker;
        }
        
        @Override
        public void onConnectivityChanged(final boolean b) {
            if (b) {
                this.requestTracker.restartRequests();
            }
        }
    }
}

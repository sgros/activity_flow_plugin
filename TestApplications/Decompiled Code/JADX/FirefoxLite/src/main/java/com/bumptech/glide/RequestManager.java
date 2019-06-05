package com.bumptech.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitor.ConnectivityListener;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.manager.TargetTracker;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;

public class RequestManager implements LifecycleListener {
    private static final RequestOptions DECODE_TYPE_BITMAP = RequestOptions.decodeTypeOf(Bitmap.class).lock();
    private static final RequestOptions DOWNLOAD_ONLY_OPTIONS = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA).priority(Priority.LOW).skipMemoryCache(true);
    private final Runnable addSelfToLifecycle;
    private final ConnectivityMonitor connectivityMonitor;
    protected final Glide glide;
    final Lifecycle lifecycle;
    private final Handler mainHandler;
    private RequestOptions requestOptions;
    private final RequestTracker requestTracker;
    private final TargetTracker targetTracker;
    private final RequestManagerTreeNode treeNode;

    /* renamed from: com.bumptech.glide.RequestManager$1 */
    class C03851 implements Runnable {
        C03851() {
        }

        public void run() {
            RequestManager.this.lifecycle.addListener(RequestManager.this);
        }
    }

    private static class RequestManagerConnectivityListener implements ConnectivityListener {
        private final RequestTracker requestTracker;

        public RequestManagerConnectivityListener(RequestTracker requestTracker) {
            this.requestTracker = requestTracker;
        }

        public void onConnectivityChanged(boolean z) {
            if (z) {
                this.requestTracker.restartRequests();
            }
        }
    }

    public RequestManager(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode requestManagerTreeNode) {
        this(glide, lifecycle, requestManagerTreeNode, new RequestTracker(), glide.getConnectivityMonitorFactory());
    }

    RequestManager(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode requestManagerTreeNode, RequestTracker requestTracker, ConnectivityMonitorFactory connectivityMonitorFactory) {
        this.targetTracker = new TargetTracker();
        this.addSelfToLifecycle = new C03851();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.glide = glide;
        this.lifecycle = lifecycle;
        this.treeNode = requestManagerTreeNode;
        this.requestTracker = requestTracker;
        this.connectivityMonitor = connectivityMonitorFactory.build(glide.getGlideContext().getBaseContext(), new RequestManagerConnectivityListener(requestTracker));
        if (Util.isOnBackgroundThread()) {
            this.mainHandler.post(this.addSelfToLifecycle);
        } else {
            lifecycle.addListener(this);
        }
        lifecycle.addListener(this.connectivityMonitor);
        setRequestOptions(glide.getGlideContext().getDefaultRequestOptions());
        glide.registerRequestManager(this);
    }

    /* Access modifiers changed, original: protected */
    public void setRequestOptions(RequestOptions requestOptions) {
        this.requestOptions = requestOptions.clone().autoClone();
    }

    public void pauseRequests() {
        Util.assertMainThread();
        this.requestTracker.pauseRequests();
    }

    public void resumeRequests() {
        Util.assertMainThread();
        this.requestTracker.resumeRequests();
    }

    public void onStart() {
        resumeRequests();
        this.targetTracker.onStart();
    }

    public void onStop() {
        pauseRequests();
        this.targetTracker.onStop();
    }

    public void onDestroy() {
        this.targetTracker.onDestroy();
        for (Target clear : this.targetTracker.getAll()) {
            clear(clear);
        }
        this.targetTracker.clear();
        this.requestTracker.clearRequests();
        this.lifecycle.removeListener(this);
        this.lifecycle.removeListener(this.connectivityMonitor);
        this.mainHandler.removeCallbacks(this.addSelfToLifecycle);
        this.glide.unregisterRequestManager(this);
    }

    public RequestBuilder<Bitmap> asBitmap() {
        return mo8383as(Bitmap.class).apply(DECODE_TYPE_BITMAP);
    }

    public RequestBuilder<Drawable> asDrawable() {
        return mo8383as(Drawable.class);
    }

    public RequestBuilder<Drawable> load(Object obj) {
        return asDrawable().load(obj);
    }

    /* renamed from: as */
    public <ResourceType> RequestBuilder<ResourceType> mo8383as(Class<ResourceType> cls) {
        return new RequestBuilder(this.glide, this, cls);
    }

    public void clear(final Target<?> target) {
        if (target != null) {
            if (Util.isOnMainThread()) {
                untrackOrDelegate(target);
            } else {
                this.mainHandler.post(new Runnable() {
                    public void run() {
                        RequestManager.this.clear(target);
                    }
                });
            }
        }
    }

    private void untrackOrDelegate(Target<?> target) {
        if (!untrack(target)) {
            this.glide.removeFromManagers(target);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean untrack(Target<?> target) {
        Request request = target.getRequest();
        if (request == null) {
            return true;
        }
        if (!this.requestTracker.clearRemoveAndRecycle(request)) {
            return false;
        }
        this.targetTracker.untrack(target);
        target.setRequest(null);
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public void track(Target<?> target, Request request) {
        this.targetTracker.track(target);
        this.requestTracker.runRequest(request);
    }

    /* Access modifiers changed, original: 0000 */
    public RequestOptions getDefaultRequestOptions() {
        return this.requestOptions;
    }

    /* Access modifiers changed, original: 0000 */
    public <T> TransitionOptions<?, T> getDefaultTransitionOptions(Class<T> cls) {
        return this.glide.getGlideContext().getDefaultTransitionOptions(cls);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("{tracker=");
        stringBuilder.append(this.requestTracker);
        stringBuilder.append(", treeNode=");
        stringBuilder.append(this.treeNode);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

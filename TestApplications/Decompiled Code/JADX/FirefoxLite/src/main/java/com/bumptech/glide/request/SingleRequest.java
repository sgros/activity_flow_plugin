package com.bumptech.glide.request;

import android.graphics.drawable.Drawable;
import android.support.p001v4.content.res.ResourcesCompat;
import android.support.p001v4.util.Pools.Pool;
import android.support.p004v7.content.res.AppCompatResources;
import android.util.Log;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.Engine.LoadStatus;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.FactoryPools.Factory;
import com.bumptech.glide.util.pool.FactoryPools.Poolable;
import com.bumptech.glide.util.pool.StateVerifier;

public final class SingleRequest<R> implements Request, ResourceCallback, SizeReadyCallback, Poolable {
    private static final Pool<SingleRequest<?>> POOL = FactoryPools.simple(150, new C06801());
    private static boolean shouldCallAppCompatResources = true;
    private TransitionFactory<? super R> animationFactory;
    private Engine engine;
    private Drawable errorDrawable;
    private Drawable fallbackDrawable;
    private GlideContext glideContext;
    private int height;
    private boolean isCallingCallbacks;
    private LoadStatus loadStatus;
    private Object model;
    private int overrideHeight;
    private int overrideWidth;
    private Drawable placeholderDrawable;
    private Priority priority;
    private RequestCoordinator requestCoordinator;
    private RequestListener<R> requestListener;
    private RequestOptions requestOptions;
    private Resource<R> resource;
    private long startTime;
    private final StateVerifier stateVerifier = StateVerifier.newInstance();
    private Status status;
    private final String tag = String.valueOf(super.hashCode());
    private Target<R> target;
    private Class<R> transcodeClass;
    private int width;

    private enum Status {
        PENDING,
        RUNNING,
        WAITING_FOR_SIZE,
        COMPLETE,
        FAILED,
        CANCELLED,
        CLEARED,
        PAUSED
    }

    /* renamed from: com.bumptech.glide.request.SingleRequest$1 */
    static class C06801 implements Factory<SingleRequest<?>> {
        C06801() {
        }

        public SingleRequest<?> create() {
            return new SingleRequest();
        }
    }

    public static <R> SingleRequest<R> obtain(GlideContext glideContext, Object obj, Class<R> cls, RequestOptions requestOptions, int i, int i2, Priority priority, Target<R> target, RequestListener<R> requestListener, RequestCoordinator requestCoordinator, Engine engine, TransitionFactory<? super R> transitionFactory) {
        SingleRequest<R> singleRequest = (SingleRequest) POOL.acquire();
        if (singleRequest == null) {
            singleRequest = new SingleRequest();
        }
        singleRequest.init(glideContext, obj, cls, requestOptions, i, i2, priority, target, requestListener, requestCoordinator, engine, transitionFactory);
        return singleRequest;
    }

    SingleRequest() {
    }

    private void init(GlideContext glideContext, Object obj, Class<R> cls, RequestOptions requestOptions, int i, int i2, Priority priority, Target<R> target, RequestListener<R> requestListener, RequestCoordinator requestCoordinator, Engine engine, TransitionFactory<? super R> transitionFactory) {
        this.glideContext = glideContext;
        this.model = obj;
        this.transcodeClass = cls;
        this.requestOptions = requestOptions;
        this.overrideWidth = i;
        this.overrideHeight = i2;
        this.priority = priority;
        this.target = target;
        this.requestListener = requestListener;
        this.requestCoordinator = requestCoordinator;
        this.engine = engine;
        this.animationFactory = transitionFactory;
        this.status = Status.PENDING;
    }

    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    public void recycle() {
        assertNotCallingCallbacks();
        this.glideContext = null;
        this.model = null;
        this.transcodeClass = null;
        this.requestOptions = null;
        this.overrideWidth = -1;
        this.overrideHeight = -1;
        this.target = null;
        this.requestListener = null;
        this.requestCoordinator = null;
        this.animationFactory = null;
        this.loadStatus = null;
        this.errorDrawable = null;
        this.placeholderDrawable = null;
        this.fallbackDrawable = null;
        this.width = -1;
        this.height = -1;
        POOL.release(this);
    }

    public void begin() {
        assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        this.startTime = LogTime.getLogTime();
        if (this.model == null) {
            if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                this.width = this.overrideWidth;
                this.height = this.overrideHeight;
            }
            onLoadFailed(new GlideException("Received null model"), getFallbackDrawable() == null ? 5 : 3);
        } else if (this.status == Status.RUNNING) {
            throw new IllegalArgumentException("Cannot restart a running request");
        } else if (this.status == Status.COMPLETE) {
            onResourceReady(this.resource, DataSource.MEMORY_CACHE);
        } else {
            this.status = Status.WAITING_FOR_SIZE;
            if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                onSizeReady(this.overrideWidth, this.overrideHeight);
            } else {
                this.target.getSize(this);
            }
            if ((this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE) && canNotifyStatusChanged()) {
                this.target.onLoadStarted(getPlaceholderDrawable());
            }
            if (Log.isLoggable("Request", 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("finished run method in ");
                stringBuilder.append(LogTime.getElapsedMillis(this.startTime));
                logV(stringBuilder.toString());
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void cancel() {
        assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        this.target.removeCallback(this);
        this.status = Status.CANCELLED;
        if (this.loadStatus != null) {
            this.loadStatus.cancel();
            this.loadStatus = null;
        }
    }

    private void assertNotCallingCallbacks() {
        if (this.isCallingCallbacks) {
            throw new IllegalStateException("You can't start or clear loads in RequestListener or Target callbacks. If you must do so, consider posting your into() or clear() calls to the main thread using a Handler instead.");
        }
    }

    public void clear() {
        Util.assertMainThread();
        assertNotCallingCallbacks();
        if (this.status != Status.CLEARED) {
            cancel();
            if (this.resource != null) {
                releaseResource(this.resource);
            }
            if (canNotifyStatusChanged()) {
                this.target.onLoadCleared(getPlaceholderDrawable());
            }
            this.status = Status.CLEARED;
        }
    }

    public void pause() {
        clear();
        this.status = Status.PAUSED;
    }

    private void releaseResource(Resource<?> resource) {
        this.engine.release(resource);
        this.resource = null;
    }

    public boolean isRunning() {
        return this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE;
    }

    public boolean isComplete() {
        return this.status == Status.COMPLETE;
    }

    public boolean isResourceSet() {
        return isComplete();
    }

    public boolean isCancelled() {
        return this.status == Status.CANCELLED || this.status == Status.CLEARED;
    }

    private Drawable getErrorDrawable() {
        if (this.errorDrawable == null) {
            this.errorDrawable = this.requestOptions.getErrorPlaceholder();
            if (this.errorDrawable == null && this.requestOptions.getErrorId() > 0) {
                this.errorDrawable = loadDrawable(this.requestOptions.getErrorId());
            }
        }
        return this.errorDrawable;
    }

    private Drawable getPlaceholderDrawable() {
        if (this.placeholderDrawable == null) {
            this.placeholderDrawable = this.requestOptions.getPlaceholderDrawable();
            if (this.placeholderDrawable == null && this.requestOptions.getPlaceholderId() > 0) {
                this.placeholderDrawable = loadDrawable(this.requestOptions.getPlaceholderId());
            }
        }
        return this.placeholderDrawable;
    }

    private Drawable getFallbackDrawable() {
        if (this.fallbackDrawable == null) {
            this.fallbackDrawable = this.requestOptions.getFallbackDrawable();
            if (this.fallbackDrawable == null && this.requestOptions.getFallbackId() > 0) {
                this.fallbackDrawable = loadDrawable(this.requestOptions.getFallbackId());
            }
        }
        return this.fallbackDrawable;
    }

    private Drawable loadDrawable(int i) {
        if (shouldCallAppCompatResources) {
            return loadDrawableV7(i);
        }
        return loadDrawableBase(i);
    }

    private Drawable loadDrawableV7(int i) {
        try {
            return AppCompatResources.getDrawable(this.glideContext, i);
        } catch (NoClassDefFoundError unused) {
            shouldCallAppCompatResources = false;
            return loadDrawableBase(i);
        }
    }

    private Drawable loadDrawableBase(int i) {
        return ResourcesCompat.getDrawable(this.glideContext.getResources(), i, this.requestOptions.getTheme());
    }

    private void setErrorPlaceholder() {
        if (canNotifyStatusChanged()) {
            Drawable drawable = null;
            if (this.model == null) {
                drawable = getFallbackDrawable();
            }
            if (drawable == null) {
                drawable = getErrorDrawable();
            }
            if (drawable == null) {
                drawable = getPlaceholderDrawable();
            }
            this.target.onLoadFailed(drawable);
        }
    }

    public void onSizeReady(int i, int i2) {
        StringBuilder stringBuilder;
        this.stateVerifier.throwIfRecycled();
        if (Log.isLoggable("Request", 2)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Got onSizeReady in ");
            stringBuilder.append(LogTime.getElapsedMillis(this.startTime));
            logV(stringBuilder.toString());
        }
        if (this.status == Status.WAITING_FOR_SIZE) {
            this.status = Status.RUNNING;
            float sizeMultiplier = this.requestOptions.getSizeMultiplier();
            this.width = maybeApplySizeMultiplier(i, sizeMultiplier);
            this.height = maybeApplySizeMultiplier(i2, sizeMultiplier);
            if (Log.isLoggable("Request", 2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("finished setup for calling load in ");
                stringBuilder.append(LogTime.getElapsedMillis(this.startTime));
                logV(stringBuilder.toString());
            }
            Engine engine = this.engine;
            GlideContext glideContext = this.glideContext;
            engine = engine;
            GlideContext glideContext2 = glideContext;
            LoadStatus load = engine.load(glideContext2, this.model, this.requestOptions.getSignature(), this.width, this.height, this.requestOptions.getResourceClass(), this.transcodeClass, this.priority, this.requestOptions.getDiskCacheStrategy(), this.requestOptions.getTransformations(), this.requestOptions.isTransformationRequired(), this.requestOptions.isScaleOnlyOrNoTransform(), this.requestOptions.getOptions(), this.requestOptions.isMemoryCacheable(), this.requestOptions.getUseUnlimitedSourceGeneratorsPool(), this.requestOptions.getOnlyRetrieveFromCache(), this);
            this.loadStatus = load;
            if (Log.isLoggable("Request", 2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("finished onSizeReady in ");
                stringBuilder.append(LogTime.getElapsedMillis(this.startTime));
                logV(stringBuilder.toString());
            }
        }
    }

    private static int maybeApplySizeMultiplier(int i, float f) {
        return i == Integer.MIN_VALUE ? i : Math.round(f * ((float) i));
    }

    private boolean canSetResource() {
        return this.requestCoordinator == null || this.requestCoordinator.canSetImage(this);
    }

    private boolean canNotifyStatusChanged() {
        return this.requestCoordinator == null || this.requestCoordinator.canNotifyStatusChanged(this);
    }

    private boolean isFirstReadyResource() {
        return this.requestCoordinator == null || !this.requestCoordinator.isAnyResourceSet();
    }

    private void notifyLoadSuccess() {
        if (this.requestCoordinator != null) {
            this.requestCoordinator.onRequestSuccess(this);
        }
    }

    public void onResourceReady(Resource<?> resource, DataSource dataSource) {
        this.stateVerifier.throwIfRecycled();
        this.loadStatus = null;
        if (resource == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected to receive a Resource<R> with an object of ");
            stringBuilder.append(this.transcodeClass);
            stringBuilder.append(" inside, but instead got null.");
            onLoadFailed(new GlideException(stringBuilder.toString()));
            return;
        }
        Object obj = resource.get();
        if (obj == null || !this.transcodeClass.isAssignableFrom(obj.getClass())) {
            releaseResource(resource);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Expected to receive an object of ");
            stringBuilder2.append(this.transcodeClass);
            stringBuilder2.append(" but instead got ");
            stringBuilder2.append(obj != null ? obj.getClass() : "");
            stringBuilder2.append("{");
            stringBuilder2.append(obj);
            stringBuilder2.append("} inside Resource{");
            stringBuilder2.append(resource);
            stringBuilder2.append("}.");
            stringBuilder2.append(obj != null ? "" : " To indicate failure return a null Resource object, rather than a Resource object containing null data.");
            onLoadFailed(new GlideException(stringBuilder2.toString()));
        } else if (canSetResource()) {
            onResourceReady(resource, obj, dataSource);
        } else {
            releaseResource(resource);
            this.status = Status.COMPLETE;
        }
    }

    /* JADX WARNING: Missing block: B:8:0x007f, code skipped:
            if (r7.requestListener.onResourceReady(r9, r7.model, r7.target, r10, r6) == false) goto L_0x0081;
     */
    private void onResourceReady(com.bumptech.glide.load.engine.Resource<R> r8, R r9, com.bumptech.glide.load.DataSource r10) {
        /*
        r7 = this;
        r6 = r7.isFirstReadyResource();
        r0 = com.bumptech.glide.request.SingleRequest.Status.COMPLETE;
        r7.status = r0;
        r7.resource = r8;
        r8 = r7.glideContext;
        r8 = r8.getLogLevel();
        r0 = 3;
        if (r8 > r0) goto L_0x006a;
    L_0x0013:
        r8 = "Glide";
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Finished loading ";
        r0.append(r1);
        r1 = r9.getClass();
        r1 = r1.getSimpleName();
        r0.append(r1);
        r1 = " from ";
        r0.append(r1);
        r0.append(r10);
        r1 = " for ";
        r0.append(r1);
        r1 = r7.model;
        r0.append(r1);
        r1 = " with size [";
        r0.append(r1);
        r1 = r7.width;
        r0.append(r1);
        r1 = "x";
        r0.append(r1);
        r1 = r7.height;
        r0.append(r1);
        r1 = "] in ";
        r0.append(r1);
        r1 = r7.startTime;
        r1 = com.bumptech.glide.util.LogTime.getElapsedMillis(r1);
        r0.append(r1);
        r1 = " ms";
        r0.append(r1);
        r0 = r0.toString();
        android.util.Log.d(r8, r0);
    L_0x006a:
        r8 = 1;
        r7.isCallingCallbacks = r8;
        r8 = 0;
        r0 = r7.requestListener;	 Catch:{ all -> 0x0092 }
        if (r0 == 0) goto L_0x0081;
    L_0x0072:
        r0 = r7.requestListener;	 Catch:{ all -> 0x0092 }
        r2 = r7.model;	 Catch:{ all -> 0x0092 }
        r3 = r7.target;	 Catch:{ all -> 0x0092 }
        r1 = r9;
        r4 = r10;
        r5 = r6;
        r0 = r0.onResourceReady(r1, r2, r3, r4, r5);	 Catch:{ all -> 0x0092 }
        if (r0 != 0) goto L_0x008c;
    L_0x0081:
        r0 = r7.animationFactory;	 Catch:{ all -> 0x0092 }
        r10 = r0.build(r10, r6);	 Catch:{ all -> 0x0092 }
        r0 = r7.target;	 Catch:{ all -> 0x0092 }
        r0.onResourceReady(r9, r10);	 Catch:{ all -> 0x0092 }
    L_0x008c:
        r7.isCallingCallbacks = r8;
        r7.notifyLoadSuccess();
        return;
    L_0x0092:
        r9 = move-exception;
        r7.isCallingCallbacks = r8;
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.request.SingleRequest.onResourceReady(com.bumptech.glide.load.engine.Resource, java.lang.Object, com.bumptech.glide.load.DataSource):void");
    }

    public void onLoadFailed(GlideException glideException) {
        onLoadFailed(glideException, 5);
    }

    private void onLoadFailed(GlideException glideException, int i) {
        this.stateVerifier.throwIfRecycled();
        int logLevel = this.glideContext.getLogLevel();
        if (logLevel <= i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Load failed for ");
            stringBuilder.append(this.model);
            stringBuilder.append(" with size [");
            stringBuilder.append(this.width);
            stringBuilder.append("x");
            stringBuilder.append(this.height);
            stringBuilder.append("]");
            Log.w("Glide", stringBuilder.toString(), glideException);
            if (logLevel <= 4) {
                glideException.logRootCauses("Glide");
            }
        }
        this.loadStatus = null;
        this.status = Status.FAILED;
        this.isCallingCallbacks = true;
        try {
            if (this.requestListener == null || !this.requestListener.onLoadFailed(glideException, this.model, this.target, isFirstReadyResource())) {
                setErrorPlaceholder();
            }
            this.isCallingCallbacks = false;
        } catch (Throwable th) {
            this.isCallingCallbacks = false;
        }
    }

    public boolean isEquivalentTo(Request request) {
        boolean z = false;
        if (!(request instanceof SingleRequest)) {
            return false;
        }
        SingleRequest singleRequest = (SingleRequest) request;
        if (this.overrideWidth == singleRequest.overrideWidth && this.overrideHeight == singleRequest.overrideHeight && Util.bothModelsNullEquivalentOrEquals(this.model, singleRequest.model) && this.transcodeClass.equals(singleRequest.transcodeClass) && this.requestOptions.equals(singleRequest.requestOptions) && this.priority == singleRequest.priority) {
            z = true;
        }
        return z;
    }

    private void logV(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(" this: ");
        stringBuilder.append(this.tag);
        Log.v("Request", stringBuilder.toString());
    }
}

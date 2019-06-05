// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request;

import java.io.Serializable;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import android.util.Log;
import android.content.Context;
import android.support.v7.content.res.AppCompatResources;
import android.support.v4.content.res.ResourcesCompat;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.pool.StateVerifier;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.Priority;
import com.bumptech.glide.GlideContext;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.request.transition.TransitionFactory;
import android.support.v4.util.Pools;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.request.target.SizeReadyCallback;

public final class SingleRequest<R> implements Request, ResourceCallback, SizeReadyCallback, Poolable
{
    private static final Pools.Pool<SingleRequest<?>> POOL;
    private static boolean shouldCallAppCompatResources;
    private TransitionFactory<? super R> animationFactory;
    private Engine engine;
    private Drawable errorDrawable;
    private Drawable fallbackDrawable;
    private GlideContext glideContext;
    private int height;
    private boolean isCallingCallbacks;
    private Engine.LoadStatus loadStatus;
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
    private final StateVerifier stateVerifier;
    private Status status;
    private final String tag;
    private Target<R> target;
    private Class<R> transcodeClass;
    private int width;
    
    static {
        POOL = FactoryPools.simple(150, (FactoryPools.Factory<SingleRequest<?>>)new Factory<SingleRequest<?>>() {
            public SingleRequest<?> create() {
                return new SingleRequest<Object>();
            }
        });
        SingleRequest.shouldCallAppCompatResources = true;
    }
    
    SingleRequest() {
        this.tag = String.valueOf(super.hashCode());
        this.stateVerifier = StateVerifier.newInstance();
    }
    
    private void assertNotCallingCallbacks() {
        if (!this.isCallingCallbacks) {
            return;
        }
        throw new IllegalStateException("You can't start or clear loads in RequestListener or Target callbacks. If you must do so, consider posting your into() or clear() calls to the main thread using a Handler instead.");
    }
    
    private boolean canNotifyStatusChanged() {
        return this.requestCoordinator == null || this.requestCoordinator.canNotifyStatusChanged(this);
    }
    
    private boolean canSetResource() {
        return this.requestCoordinator == null || this.requestCoordinator.canSetImage(this);
    }
    
    private Drawable getErrorDrawable() {
        if (this.errorDrawable == null) {
            this.errorDrawable = this.requestOptions.getErrorPlaceholder();
            if (this.errorDrawable == null && this.requestOptions.getErrorId() > 0) {
                this.errorDrawable = this.loadDrawable(this.requestOptions.getErrorId());
            }
        }
        return this.errorDrawable;
    }
    
    private Drawable getFallbackDrawable() {
        if (this.fallbackDrawable == null) {
            this.fallbackDrawable = this.requestOptions.getFallbackDrawable();
            if (this.fallbackDrawable == null && this.requestOptions.getFallbackId() > 0) {
                this.fallbackDrawable = this.loadDrawable(this.requestOptions.getFallbackId());
            }
        }
        return this.fallbackDrawable;
    }
    
    private Drawable getPlaceholderDrawable() {
        if (this.placeholderDrawable == null) {
            this.placeholderDrawable = this.requestOptions.getPlaceholderDrawable();
            if (this.placeholderDrawable == null && this.requestOptions.getPlaceholderId() > 0) {
                this.placeholderDrawable = this.loadDrawable(this.requestOptions.getPlaceholderId());
            }
        }
        return this.placeholderDrawable;
    }
    
    private void init(final GlideContext glideContext, final Object model, final Class<R> transcodeClass, final RequestOptions requestOptions, final int overrideWidth, final int overrideHeight, final Priority priority, final Target<R> target, final RequestListener<R> requestListener, final RequestCoordinator requestCoordinator, final Engine engine, final TransitionFactory<? super R> animationFactory) {
        this.glideContext = glideContext;
        this.model = model;
        this.transcodeClass = transcodeClass;
        this.requestOptions = requestOptions;
        this.overrideWidth = overrideWidth;
        this.overrideHeight = overrideHeight;
        this.priority = priority;
        this.target = target;
        this.requestListener = requestListener;
        this.requestCoordinator = requestCoordinator;
        this.engine = engine;
        this.animationFactory = animationFactory;
        this.status = Status.PENDING;
    }
    
    private boolean isFirstReadyResource() {
        return this.requestCoordinator == null || !this.requestCoordinator.isAnyResourceSet();
    }
    
    private Drawable loadDrawable(final int n) {
        if (SingleRequest.shouldCallAppCompatResources) {
            return this.loadDrawableV7(n);
        }
        return this.loadDrawableBase(n);
    }
    
    private Drawable loadDrawableBase(final int n) {
        return ResourcesCompat.getDrawable(this.glideContext.getResources(), n, this.requestOptions.getTheme());
    }
    
    private Drawable loadDrawableV7(final int n) {
        try {
            return AppCompatResources.getDrawable((Context)this.glideContext, n);
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            SingleRequest.shouldCallAppCompatResources = false;
            return this.loadDrawableBase(n);
        }
    }
    
    private void logV(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" this: ");
        sb.append(this.tag);
        Log.v("Request", sb.toString());
    }
    
    private static int maybeApplySizeMultiplier(int round, final float n) {
        if (round != Integer.MIN_VALUE) {
            round = Math.round(n * round);
        }
        return round;
    }
    
    private void notifyLoadSuccess() {
        if (this.requestCoordinator != null) {
            this.requestCoordinator.onRequestSuccess(this);
        }
    }
    
    public static <R> SingleRequest<R> obtain(final GlideContext glideContext, final Object o, final Class<R> clazz, final RequestOptions requestOptions, final int n, final int n2, final Priority priority, final Target<R> target, final RequestListener<R> requestListener, final RequestCoordinator requestCoordinator, final Engine engine, final TransitionFactory<? super R> transitionFactory) {
        SingleRequest<?> singleRequest;
        if ((singleRequest = SingleRequest.POOL.acquire()) == null) {
            singleRequest = new SingleRequest<R>();
        }
        singleRequest.init(glideContext, o, clazz, requestOptions, n, n2, priority, target, requestListener, requestCoordinator, engine, transitionFactory);
        return (SingleRequest<R>)singleRequest;
    }
    
    private void onLoadFailed(final GlideException ex, final int n) {
        this.stateVerifier.throwIfRecycled();
        final int logLevel = this.glideContext.getLogLevel();
        if (logLevel <= n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Load failed for ");
            sb.append(this.model);
            sb.append(" with size [");
            sb.append(this.width);
            sb.append("x");
            sb.append(this.height);
            sb.append("]");
            Log.w("Glide", sb.toString(), (Throwable)ex);
            if (logLevel <= 4) {
                ex.logRootCauses("Glide");
            }
        }
        this.loadStatus = null;
        this.status = Status.FAILED;
        this.isCallingCallbacks = true;
        try {
            if (this.requestListener == null || !this.requestListener.onLoadFailed(ex, this.model, this.target, this.isFirstReadyResource())) {
                this.setErrorPlaceholder();
            }
        }
        finally {
            this.isCallingCallbacks = false;
        }
    }
    
    private void onResourceReady(final Resource<R> resource, final R r, final DataSource obj) {
        final boolean firstReadyResource = this.isFirstReadyResource();
        this.status = Status.COMPLETE;
        this.resource = resource;
        if (this.glideContext.getLogLevel() <= 3) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Finished loading ");
            sb.append(r.getClass().getSimpleName());
            sb.append(" from ");
            sb.append(obj);
            sb.append(" for ");
            sb.append(this.model);
            sb.append(" with size [");
            sb.append(this.width);
            sb.append("x");
            sb.append(this.height);
            sb.append("] in ");
            sb.append(LogTime.getElapsedMillis(this.startTime));
            sb.append(" ms");
            Log.d("Glide", sb.toString());
        }
        this.isCallingCallbacks = true;
        try {
            if (this.requestListener == null || !this.requestListener.onResourceReady(r, this.model, this.target, obj, firstReadyResource)) {
                this.target.onResourceReady(r, this.animationFactory.build(obj, firstReadyResource));
            }
            this.isCallingCallbacks = false;
            this.notifyLoadSuccess();
        }
        finally {
            this.isCallingCallbacks = false;
        }
    }
    
    private void releaseResource(final Resource<?> resource) {
        this.engine.release(resource);
        this.resource = null;
    }
    
    private void setErrorPlaceholder() {
        if (!this.canNotifyStatusChanged()) {
            return;
        }
        Drawable fallbackDrawable = null;
        if (this.model == null) {
            fallbackDrawable = this.getFallbackDrawable();
        }
        Drawable errorDrawable;
        if ((errorDrawable = fallbackDrawable) == null) {
            errorDrawable = this.getErrorDrawable();
        }
        Drawable placeholderDrawable;
        if ((placeholderDrawable = errorDrawable) == null) {
            placeholderDrawable = this.getPlaceholderDrawable();
        }
        this.target.onLoadFailed(placeholderDrawable);
    }
    
    @Override
    public void begin() {
        this.assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        this.startTime = LogTime.getLogTime();
        if (this.model == null) {
            if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                this.width = this.overrideWidth;
                this.height = this.overrideHeight;
            }
            int n;
            if (this.getFallbackDrawable() == null) {
                n = 5;
            }
            else {
                n = 3;
            }
            this.onLoadFailed(new GlideException("Received null model"), n);
            return;
        }
        if (this.status == Status.RUNNING) {
            throw new IllegalArgumentException("Cannot restart a running request");
        }
        if (this.status == Status.COMPLETE) {
            this.onResourceReady(this.resource, DataSource.MEMORY_CACHE);
            return;
        }
        this.status = Status.WAITING_FOR_SIZE;
        if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
            this.onSizeReady(this.overrideWidth, this.overrideHeight);
        }
        else {
            this.target.getSize(this);
        }
        if ((this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE) && this.canNotifyStatusChanged()) {
            this.target.onLoadStarted(this.getPlaceholderDrawable());
        }
        if (Log.isLoggable("Request", 2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("finished run method in ");
            sb.append(LogTime.getElapsedMillis(this.startTime));
            this.logV(sb.toString());
        }
    }
    
    void cancel() {
        this.assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        this.target.removeCallback(this);
        this.status = Status.CANCELLED;
        if (this.loadStatus != null) {
            this.loadStatus.cancel();
            this.loadStatus = null;
        }
    }
    
    @Override
    public void clear() {
        Util.assertMainThread();
        this.assertNotCallingCallbacks();
        if (this.status == Status.CLEARED) {
            return;
        }
        this.cancel();
        if (this.resource != null) {
            this.releaseResource(this.resource);
        }
        if (this.canNotifyStatusChanged()) {
            this.target.onLoadCleared(this.getPlaceholderDrawable());
        }
        this.status = Status.CLEARED;
    }
    
    @Override
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }
    
    @Override
    public boolean isCancelled() {
        return this.status == Status.CANCELLED || this.status == Status.CLEARED;
    }
    
    @Override
    public boolean isComplete() {
        return this.status == Status.COMPLETE;
    }
    
    @Override
    public boolean isEquivalentTo(final Request request) {
        final boolean b = request instanceof SingleRequest;
        final boolean b2 = false;
        if (b) {
            final SingleRequest singleRequest = (SingleRequest)request;
            boolean b3 = b2;
            if (this.overrideWidth == singleRequest.overrideWidth) {
                b3 = b2;
                if (this.overrideHeight == singleRequest.overrideHeight) {
                    b3 = b2;
                    if (Util.bothModelsNullEquivalentOrEquals(this.model, singleRequest.model)) {
                        b3 = b2;
                        if (this.transcodeClass.equals(singleRequest.transcodeClass)) {
                            b3 = b2;
                            if (this.requestOptions.equals(singleRequest.requestOptions)) {
                                b3 = b2;
                                if (this.priority == singleRequest.priority) {
                                    b3 = true;
                                }
                            }
                        }
                    }
                }
            }
            return b3;
        }
        return false;
    }
    
    @Override
    public boolean isResourceSet() {
        return this.isComplete();
    }
    
    @Override
    public boolean isRunning() {
        return this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE;
    }
    
    @Override
    public void onLoadFailed(final GlideException ex) {
        this.onLoadFailed(ex, 5);
    }
    
    @Override
    public void onResourceReady(final Resource<?> obj, final DataSource dataSource) {
        this.stateVerifier.throwIfRecycled();
        this.loadStatus = null;
        if (obj == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected to receive a Resource<R> with an object of ");
            sb.append(this.transcodeClass);
            sb.append(" inside, but instead got null.");
            this.onLoadFailed(new GlideException(sb.toString()));
            return;
        }
        final R value = obj.get();
        if (value == null || !this.transcodeClass.isAssignableFrom(value.getClass())) {
            this.releaseResource(obj);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Expected to receive an object of ");
            sb2.append(this.transcodeClass);
            sb2.append(" but instead got ");
            Serializable class1;
            if (value != null) {
                class1 = value.getClass();
            }
            else {
                class1 = "";
            }
            sb2.append(class1);
            sb2.append("{");
            sb2.append(value);
            sb2.append("} inside Resource{");
            sb2.append(obj);
            sb2.append("}.");
            String str;
            if (value != null) {
                str = "";
            }
            else {
                str = " To indicate failure return a null Resource object, rather than a Resource object containing null data.";
            }
            sb2.append(str);
            this.onLoadFailed(new GlideException(sb2.toString()));
            return;
        }
        if (!this.canSetResource()) {
            this.releaseResource(obj);
            this.status = Status.COMPLETE;
            return;
        }
        this.onResourceReady((Resource<R>)obj, value, dataSource);
    }
    
    @Override
    public void onSizeReady(final int n, final int n2) {
        this.stateVerifier.throwIfRecycled();
        if (Log.isLoggable("Request", 2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Got onSizeReady in ");
            sb.append(LogTime.getElapsedMillis(this.startTime));
            this.logV(sb.toString());
        }
        if (this.status != Status.WAITING_FOR_SIZE) {
            return;
        }
        this.status = Status.RUNNING;
        final float sizeMultiplier = this.requestOptions.getSizeMultiplier();
        this.width = maybeApplySizeMultiplier(n, sizeMultiplier);
        this.height = maybeApplySizeMultiplier(n2, sizeMultiplier);
        if (Log.isLoggable("Request", 2)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("finished setup for calling load in ");
            sb2.append(LogTime.getElapsedMillis(this.startTime));
            this.logV(sb2.toString());
        }
        this.loadStatus = this.engine.load(this.glideContext, this.model, this.requestOptions.getSignature(), this.width, this.height, this.requestOptions.getResourceClass(), this.transcodeClass, this.priority, this.requestOptions.getDiskCacheStrategy(), this.requestOptions.getTransformations(), this.requestOptions.isTransformationRequired(), this.requestOptions.isScaleOnlyOrNoTransform(), this.requestOptions.getOptions(), this.requestOptions.isMemoryCacheable(), this.requestOptions.getUseUnlimitedSourceGeneratorsPool(), this.requestOptions.getOnlyRetrieveFromCache(), this);
        if (Log.isLoggable("Request", 2)) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("finished onSizeReady in ");
            sb3.append(LogTime.getElapsedMillis(this.startTime));
            this.logV(sb3.toString());
        }
    }
    
    @Override
    public void pause() {
        this.clear();
        this.status = Status.PAUSED;
    }
    
    @Override
    public void recycle() {
        this.assertNotCallingCallbacks();
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
        SingleRequest.POOL.release(this);
    }
    
    private enum Status
    {
        CANCELLED, 
        CLEARED, 
        COMPLETE, 
        FAILED, 
        PAUSED, 
        PENDING, 
        RUNNING, 
        WAITING_FOR_SIZE;
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide;

import android.widget.ImageView;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

public class RequestBuilder<TranscodeType> implements Cloneable
{
    protected static final RequestOptions DOWNLOAD_ONLY_OPTIONS;
    private final GlideContext context;
    private final RequestOptions defaultRequestOptions;
    private final Glide glide;
    private boolean isDefaultTransitionOptionsSet;
    private boolean isModelSet;
    private boolean isThumbnailBuilt;
    private Object model;
    private RequestListener<TranscodeType> requestListener;
    private final RequestManager requestManager;
    protected RequestOptions requestOptions;
    private Float thumbSizeMultiplier;
    private RequestBuilder<TranscodeType> thumbnailBuilder;
    private final Class<TranscodeType> transcodeClass;
    private TransitionOptions<?, ? super TranscodeType> transitionOptions;
    
    static {
        DOWNLOAD_ONLY_OPTIONS = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).priority(Priority.LOW).skipMemoryCache(true);
    }
    
    protected RequestBuilder(final Glide glide, final RequestManager requestManager, final Class<TranscodeType> transcodeClass) {
        this.isDefaultTransitionOptionsSet = true;
        this.glide = glide;
        this.requestManager = requestManager;
        this.context = glide.getGlideContext();
        this.transcodeClass = transcodeClass;
        this.defaultRequestOptions = requestManager.getDefaultRequestOptions();
        this.transitionOptions = requestManager.getDefaultTransitionOptions(transcodeClass);
        this.requestOptions = this.defaultRequestOptions;
    }
    
    private Request buildRequest(final Target<TranscodeType> target, final RequestOptions requestOptions) {
        return this.buildRequestRecursive(target, null, this.transitionOptions, requestOptions.getPriority(), requestOptions.getOverrideWidth(), requestOptions.getOverrideHeight(), requestOptions);
    }
    
    private Request buildRequestRecursive(final Target<TranscodeType> target, ThumbnailRequestCoordinator thumbnailRequestCoordinator, final TransitionOptions<?, ? super TranscodeType> transitionOptions, final Priority priority, final int n, final int n2, final RequestOptions requestOptions) {
        if (this.thumbnailBuilder != null) {
            if (!this.isThumbnailBuilt) {
                TransitionOptions<?, ? super TranscodeType> transitionOptions2 = this.thumbnailBuilder.transitionOptions;
                if (this.thumbnailBuilder.isDefaultTransitionOptionsSet) {
                    transitionOptions2 = transitionOptions;
                }
                Priority priority2;
                if (this.thumbnailBuilder.requestOptions.isPrioritySet()) {
                    priority2 = this.thumbnailBuilder.requestOptions.getPriority();
                }
                else {
                    priority2 = this.getThumbnailPriority(priority);
                }
                final int overrideWidth = this.thumbnailBuilder.requestOptions.getOverrideWidth();
                final int overrideHeight = this.thumbnailBuilder.requestOptions.getOverrideHeight();
                int overrideWidth2 = overrideWidth;
                int overrideHeight2 = overrideHeight;
                if (Util.isValidDimensions(n, n2)) {
                    overrideWidth2 = overrideWidth;
                    overrideHeight2 = overrideHeight;
                    if (!this.thumbnailBuilder.requestOptions.isValidOverride()) {
                        overrideWidth2 = requestOptions.getOverrideWidth();
                        overrideHeight2 = requestOptions.getOverrideHeight();
                    }
                }
                final ThumbnailRequestCoordinator thumbnailRequestCoordinator2 = new ThumbnailRequestCoordinator(thumbnailRequestCoordinator);
                final Request obtainRequest = this.obtainRequest(target, requestOptions, thumbnailRequestCoordinator2, transitionOptions, priority, n, n2);
                this.isThumbnailBuilt = true;
                final Request buildRequestRecursive = this.thumbnailBuilder.buildRequestRecursive(target, thumbnailRequestCoordinator2, transitionOptions2, priority2, overrideWidth2, overrideHeight2, this.thumbnailBuilder.requestOptions);
                this.isThumbnailBuilt = false;
                thumbnailRequestCoordinator2.setRequests(obtainRequest, buildRequestRecursive);
                return thumbnailRequestCoordinator2;
            }
            throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
        }
        else {
            if (this.thumbSizeMultiplier != null) {
                thumbnailRequestCoordinator = new ThumbnailRequestCoordinator(thumbnailRequestCoordinator);
                thumbnailRequestCoordinator.setRequests(this.obtainRequest(target, requestOptions, thumbnailRequestCoordinator, transitionOptions, priority, n, n2), this.obtainRequest(target, requestOptions.clone().sizeMultiplier(this.thumbSizeMultiplier), thumbnailRequestCoordinator, transitionOptions, this.getThumbnailPriority(priority), n, n2));
                return thumbnailRequestCoordinator;
            }
            return this.obtainRequest(target, requestOptions, thumbnailRequestCoordinator, transitionOptions, priority, n, n2);
        }
    }
    
    private Priority getThumbnailPriority(final Priority priority) {
        switch (RequestBuilder$2.$SwitchMap$com$bumptech$glide$Priority[priority.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown priority: ");
                sb.append(this.requestOptions.getPriority());
                throw new IllegalArgumentException(sb.toString());
            }
            case 3:
            case 4: {
                return Priority.IMMEDIATE;
            }
            case 2: {
                return Priority.HIGH;
            }
            case 1: {
                return Priority.NORMAL;
            }
        }
    }
    
    private <Y extends Target<TranscodeType>> Y into(final Y y, final RequestOptions requestOptions) {
        Util.assertMainThread();
        Preconditions.checkNotNull(y);
        if (!this.isModelSet) {
            throw new IllegalArgumentException("You must call #load() before calling #into()");
        }
        final Request buildRequest = this.buildRequest(y, requestOptions.autoClone());
        final Request request = y.getRequest();
        if (buildRequest.isEquivalentTo(request)) {
            buildRequest.recycle();
            if (!Preconditions.checkNotNull(request).isRunning()) {
                request.begin();
            }
            return y;
        }
        this.requestManager.clear(y);
        y.setRequest(buildRequest);
        this.requestManager.track(y, buildRequest);
        return y;
    }
    
    private RequestBuilder<TranscodeType> loadGeneric(final Object model) {
        this.model = model;
        this.isModelSet = true;
        return this;
    }
    
    private Request obtainRequest(final Target<TranscodeType> target, final RequestOptions requestOptions, final RequestCoordinator requestCoordinator, final TransitionOptions<?, ? super TranscodeType> transitionOptions, final Priority priority, final int n, final int n2) {
        return SingleRequest.obtain(this.context, this.model, this.transcodeClass, requestOptions, n, n2, priority, target, this.requestListener, requestCoordinator, this.context.getEngine(), transitionOptions.getTransitionFactory());
    }
    
    public RequestBuilder<TranscodeType> apply(final RequestOptions requestOptions) {
        Preconditions.checkNotNull(requestOptions);
        this.requestOptions = this.getMutableOptions().apply(requestOptions);
        return this;
    }
    
    public RequestBuilder<TranscodeType> clone() {
        try {
            final RequestBuilder requestBuilder = (RequestBuilder)super.clone();
            requestBuilder.requestOptions = requestBuilder.requestOptions.clone();
            requestBuilder.transitionOptions = (TransitionOptions<?, ? super TranscodeType>)requestBuilder.transitionOptions.clone();
            return requestBuilder;
        }
        catch (CloneNotSupportedException cause) {
            throw new RuntimeException(cause);
        }
    }
    
    protected RequestOptions getMutableOptions() {
        RequestOptions requestOptions;
        if (this.defaultRequestOptions == this.requestOptions) {
            requestOptions = this.requestOptions.clone();
        }
        else {
            requestOptions = this.requestOptions;
        }
        return requestOptions;
    }
    
    public Target<TranscodeType> into(final ImageView imageView) {
        Util.assertMainThread();
        Preconditions.checkNotNull(imageView);
        RequestOptions requestOptions2;
        final RequestOptions requestOptions = requestOptions2 = this.requestOptions;
        if (!requestOptions.isTransformationSet()) {
            requestOptions2 = requestOptions;
            if (requestOptions.isTransformationAllowed()) {
                requestOptions2 = requestOptions;
                if (imageView.getScaleType() != null) {
                    switch (RequestBuilder$2.$SwitchMap$android$widget$ImageView$ScaleType[imageView.getScaleType().ordinal()]) {
                        default: {
                            requestOptions2 = requestOptions;
                            break;
                        }
                        case 6: {
                            requestOptions2 = requestOptions.clone().optionalCenterInside();
                            break;
                        }
                        case 3:
                        case 4:
                        case 5: {
                            requestOptions2 = requestOptions.clone().optionalFitCenter();
                            break;
                        }
                        case 2: {
                            requestOptions2 = requestOptions.clone().optionalCenterInside();
                            break;
                        }
                        case 1: {
                            requestOptions2 = requestOptions.clone().optionalCenterCrop();
                            break;
                        }
                    }
                }
            }
        }
        return this.into(this.context.buildImageViewTarget(imageView, this.transcodeClass), requestOptions2);
    }
    
    public <Y extends Target<TranscodeType>> Y into(final Y y) {
        return this.into(y, this.getMutableOptions());
    }
    
    public RequestBuilder<TranscodeType> listener(final RequestListener<TranscodeType> requestListener) {
        this.requestListener = requestListener;
        return this;
    }
    
    public RequestBuilder<TranscodeType> load(final Object o) {
        return this.loadGeneric(o);
    }
    
    public RequestBuilder<TranscodeType> load(final String s) {
        return this.loadGeneric(s);
    }
}

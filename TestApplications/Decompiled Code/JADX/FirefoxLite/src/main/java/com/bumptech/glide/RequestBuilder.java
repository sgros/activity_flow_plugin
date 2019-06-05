package com.bumptech.glide;

import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;

public class RequestBuilder<TranscodeType> implements Cloneable {
    protected static final RequestOptions DOWNLOAD_ONLY_OPTIONS = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).priority(Priority.LOW).skipMemoryCache(true);
    private final GlideContext context;
    private final RequestOptions defaultRequestOptions;
    private final Glide glide;
    private boolean isDefaultTransitionOptionsSet = true;
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

    /* renamed from: com.bumptech.glide.RequestBuilder$2 */
    static /* synthetic */ class C03842 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];

        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0071 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x005c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x007c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0066 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0087 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0048 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0052 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Can't wrap try/catch for region: R(29:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Can't wrap try/catch for region: R(29:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Can't wrap try/catch for region: R(28:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|(3:31|32|34)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(24:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|(3:31|32|34)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(24:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|(3:31|32|34)) */
        static {
            /*
            r0 = com.bumptech.glide.Priority.values();
            r0 = r0.length;
            r0 = new int[r0];
            $SwitchMap$com$bumptech$glide$Priority = r0;
            r0 = 1;
            r1 = $SwitchMap$com$bumptech$glide$Priority;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = com.bumptech.glide.Priority.LOW;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = r2.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1[r2] = r0;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            r1 = 2;
            r2 = $SwitchMap$com$bumptech$glide$Priority;	 Catch:{ NoSuchFieldError -> 0x001f }
            r3 = com.bumptech.glide.Priority.NORMAL;	 Catch:{ NoSuchFieldError -> 0x001f }
            r3 = r3.ordinal();	 Catch:{ NoSuchFieldError -> 0x001f }
            r2[r3] = r1;	 Catch:{ NoSuchFieldError -> 0x001f }
        L_0x001f:
            r2 = 3;
            r3 = $SwitchMap$com$bumptech$glide$Priority;	 Catch:{ NoSuchFieldError -> 0x002a }
            r4 = com.bumptech.glide.Priority.HIGH;	 Catch:{ NoSuchFieldError -> 0x002a }
            r4 = r4.ordinal();	 Catch:{ NoSuchFieldError -> 0x002a }
            r3[r4] = r2;	 Catch:{ NoSuchFieldError -> 0x002a }
        L_0x002a:
            r3 = 4;
            r4 = $SwitchMap$com$bumptech$glide$Priority;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r5 = com.bumptech.glide.Priority.IMMEDIATE;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r5 = r5.ordinal();	 Catch:{ NoSuchFieldError -> 0x0035 }
            r4[r5] = r3;	 Catch:{ NoSuchFieldError -> 0x0035 }
        L_0x0035:
            r4 = android.widget.ImageView.ScaleType.values();
            r4 = r4.length;
            r4 = new int[r4];
            $SwitchMap$android$widget$ImageView$ScaleType = r4;
            r4 = $SwitchMap$android$widget$ImageView$ScaleType;	 Catch:{ NoSuchFieldError -> 0x0048 }
            r5 = android.widget.ImageView.ScaleType.CENTER_CROP;	 Catch:{ NoSuchFieldError -> 0x0048 }
            r5 = r5.ordinal();	 Catch:{ NoSuchFieldError -> 0x0048 }
            r4[r5] = r0;	 Catch:{ NoSuchFieldError -> 0x0048 }
        L_0x0048:
            r0 = $SwitchMap$android$widget$ImageView$ScaleType;	 Catch:{ NoSuchFieldError -> 0x0052 }
            r4 = android.widget.ImageView.ScaleType.CENTER_INSIDE;	 Catch:{ NoSuchFieldError -> 0x0052 }
            r4 = r4.ordinal();	 Catch:{ NoSuchFieldError -> 0x0052 }
            r0[r4] = r1;	 Catch:{ NoSuchFieldError -> 0x0052 }
        L_0x0052:
            r0 = $SwitchMap$android$widget$ImageView$ScaleType;	 Catch:{ NoSuchFieldError -> 0x005c }
            r1 = android.widget.ImageView.ScaleType.FIT_CENTER;	 Catch:{ NoSuchFieldError -> 0x005c }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x005c }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x005c }
        L_0x005c:
            r0 = $SwitchMap$android$widget$ImageView$ScaleType;	 Catch:{ NoSuchFieldError -> 0x0066 }
            r1 = android.widget.ImageView.ScaleType.FIT_START;	 Catch:{ NoSuchFieldError -> 0x0066 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0066 }
            r0[r1] = r3;	 Catch:{ NoSuchFieldError -> 0x0066 }
        L_0x0066:
            r0 = $SwitchMap$android$widget$ImageView$ScaleType;	 Catch:{ NoSuchFieldError -> 0x0071 }
            r1 = android.widget.ImageView.ScaleType.FIT_END;	 Catch:{ NoSuchFieldError -> 0x0071 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0071 }
            r2 = 5;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0071 }
        L_0x0071:
            r0 = $SwitchMap$android$widget$ImageView$ScaleType;	 Catch:{ NoSuchFieldError -> 0x007c }
            r1 = android.widget.ImageView.ScaleType.FIT_XY;	 Catch:{ NoSuchFieldError -> 0x007c }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x007c }
            r2 = 6;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x007c }
        L_0x007c:
            r0 = $SwitchMap$android$widget$ImageView$ScaleType;	 Catch:{ NoSuchFieldError -> 0x0087 }
            r1 = android.widget.ImageView.ScaleType.CENTER;	 Catch:{ NoSuchFieldError -> 0x0087 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0087 }
            r2 = 7;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0087 }
        L_0x0087:
            r0 = $SwitchMap$android$widget$ImageView$ScaleType;	 Catch:{ NoSuchFieldError -> 0x0093 }
            r1 = android.widget.ImageView.ScaleType.MATRIX;	 Catch:{ NoSuchFieldError -> 0x0093 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0093 }
            r2 = 8;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0093 }
        L_0x0093:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.RequestBuilder$C03842.<clinit>():void");
        }
    }

    protected RequestBuilder(Glide glide, RequestManager requestManager, Class<TranscodeType> cls) {
        this.glide = glide;
        this.requestManager = requestManager;
        this.context = glide.getGlideContext();
        this.transcodeClass = cls;
        this.defaultRequestOptions = requestManager.getDefaultRequestOptions();
        this.transitionOptions = requestManager.getDefaultTransitionOptions(cls);
        this.requestOptions = this.defaultRequestOptions;
    }

    public RequestBuilder<TranscodeType> apply(RequestOptions requestOptions) {
        Preconditions.checkNotNull(requestOptions);
        this.requestOptions = getMutableOptions().apply(requestOptions);
        return this;
    }

    /* Access modifiers changed, original: protected */
    public RequestOptions getMutableOptions() {
        return this.defaultRequestOptions == this.requestOptions ? this.requestOptions.clone() : this.requestOptions;
    }

    public RequestBuilder<TranscodeType> listener(RequestListener<TranscodeType> requestListener) {
        this.requestListener = requestListener;
        return this;
    }

    public RequestBuilder<TranscodeType> load(Object obj) {
        return loadGeneric(obj);
    }

    private RequestBuilder<TranscodeType> loadGeneric(Object obj) {
        this.model = obj;
        this.isModelSet = true;
        return this;
    }

    public RequestBuilder<TranscodeType> load(String str) {
        return loadGeneric(str);
    }

    public RequestBuilder<TranscodeType> clone() {
        try {
            RequestBuilder requestBuilder = (RequestBuilder) super.clone();
            requestBuilder.requestOptions = requestBuilder.requestOptions.clone();
            requestBuilder.transitionOptions = requestBuilder.transitionOptions.clone();
            return requestBuilder;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public <Y extends Target<TranscodeType>> Y into(Y y) {
        return into(y, getMutableOptions());
    }

    private <Y extends Target<TranscodeType>> Y into(Y y, RequestOptions requestOptions) {
        Util.assertMainThread();
        Preconditions.checkNotNull(y);
        if (this.isModelSet) {
            Request buildRequest = buildRequest(y, requestOptions.autoClone());
            Request request = y.getRequest();
            if (buildRequest.isEquivalentTo(request)) {
                buildRequest.recycle();
                if (!((Request) Preconditions.checkNotNull(request)).isRunning()) {
                    request.begin();
                }
                return y;
            }
            this.requestManager.clear(y);
            y.setRequest(buildRequest);
            this.requestManager.track(y, buildRequest);
            return y;
        }
        throw new IllegalArgumentException("You must call #load() before calling #into()");
    }

    public Target<TranscodeType> into(ImageView imageView) {
        Util.assertMainThread();
        Preconditions.checkNotNull(imageView);
        RequestOptions requestOptions = this.requestOptions;
        if (!(requestOptions.isTransformationSet() || !requestOptions.isTransformationAllowed() || imageView.getScaleType() == null)) {
            switch (C03842.$SwitchMap$android$widget$ImageView$ScaleType[imageView.getScaleType().ordinal()]) {
                case 1:
                    requestOptions = requestOptions.clone().optionalCenterCrop();
                    break;
                case 2:
                    requestOptions = requestOptions.clone().optionalCenterInside();
                    break;
                case 3:
                case 4:
                case 5:
                    requestOptions = requestOptions.clone().optionalFitCenter();
                    break;
                case 6:
                    requestOptions = requestOptions.clone().optionalCenterInside();
                    break;
            }
        }
        return into(this.context.buildImageViewTarget(imageView, this.transcodeClass), requestOptions);
    }

    private Priority getThumbnailPriority(Priority priority) {
        switch (priority) {
            case LOW:
                return Priority.NORMAL;
            case NORMAL:
                return Priority.HIGH;
            case HIGH:
            case IMMEDIATE:
                return Priority.IMMEDIATE;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unknown priority: ");
                stringBuilder.append(this.requestOptions.getPriority());
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private Request buildRequest(Target<TranscodeType> target, RequestOptions requestOptions) {
        return buildRequestRecursive(target, null, this.transitionOptions, requestOptions.getPriority(), requestOptions.getOverrideWidth(), requestOptions.getOverrideHeight(), requestOptions);
    }

    private Request buildRequestRecursive(Target<TranscodeType> target, ThumbnailRequestCoordinator thumbnailRequestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions, Priority priority, int i, int i2, RequestOptions requestOptions) {
        ThumbnailRequestCoordinator thumbnailRequestCoordinator2 = thumbnailRequestCoordinator;
        Priority priority2 = priority;
        if (this.thumbnailBuilder != null) {
            if (this.isThumbnailBuilt) {
                throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
            }
            TransitionOptions transitionOptions2 = this.thumbnailBuilder.isDefaultTransitionOptionsSet ? transitionOptions : this.thumbnailBuilder.transitionOptions;
            Priority priority3 = this.thumbnailBuilder.requestOptions.isPrioritySet() ? this.thumbnailBuilder.requestOptions.getPriority() : getThumbnailPriority(priority2);
            int overrideWidth = this.thumbnailBuilder.requestOptions.getOverrideWidth();
            int overrideHeight = this.thumbnailBuilder.requestOptions.getOverrideHeight();
            if (Util.isValidDimensions(i, i2) && !this.thumbnailBuilder.requestOptions.isValidOverride()) {
                overrideWidth = requestOptions.getOverrideWidth();
                overrideHeight = requestOptions.getOverrideHeight();
            }
            int i3 = overrideWidth;
            int i4 = overrideHeight;
            ThumbnailRequestCoordinator thumbnailRequestCoordinator3 = new ThumbnailRequestCoordinator(thumbnailRequestCoordinator2);
            Request obtainRequest = obtainRequest(target, requestOptions, thumbnailRequestCoordinator3, transitionOptions, priority, i, i2);
            this.isThumbnailBuilt = true;
            ThumbnailRequestCoordinator thumbnailRequestCoordinator4 = thumbnailRequestCoordinator3;
            Request buildRequestRecursive = this.thumbnailBuilder.buildRequestRecursive(target, thumbnailRequestCoordinator3, transitionOptions2, priority3, i3, i4, this.thumbnailBuilder.requestOptions);
            this.isThumbnailBuilt = false;
            thumbnailRequestCoordinator4.setRequests(obtainRequest, buildRequestRecursive);
            return thumbnailRequestCoordinator4;
        } else if (this.thumbSizeMultiplier == null) {
            return obtainRequest(target, requestOptions, thumbnailRequestCoordinator, transitionOptions, priority, i, i2);
        } else {
            ThumbnailRequestCoordinator thumbnailRequestCoordinator5 = new ThumbnailRequestCoordinator(thumbnailRequestCoordinator2);
            thumbnailRequestCoordinator2 = thumbnailRequestCoordinator5;
            TransitionOptions<?, ? super TranscodeType> transitionOptions3 = transitionOptions;
            int i5 = i;
            int i6 = i2;
            thumbnailRequestCoordinator5.setRequests(obtainRequest(target, requestOptions, thumbnailRequestCoordinator2, transitionOptions3, priority, i5, i6), obtainRequest(target, requestOptions.clone().sizeMultiplier(this.thumbSizeMultiplier.floatValue()), thumbnailRequestCoordinator2, transitionOptions3, getThumbnailPriority(priority2), i5, i6));
            return thumbnailRequestCoordinator5;
        }
    }

    private Request obtainRequest(Target<TranscodeType> target, RequestOptions requestOptions, RequestCoordinator requestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions, Priority priority, int i, int i2) {
        return SingleRequest.obtain(this.context, this.model, this.transcodeClass, requestOptions, i, i2, priority, target, this.requestListener, requestCoordinator, this.context.getEngine(), transitionOptions.getTransitionFactory());
    }
}

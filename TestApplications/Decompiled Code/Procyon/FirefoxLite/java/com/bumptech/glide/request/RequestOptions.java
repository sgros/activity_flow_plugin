// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableTransformation;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.util.Preconditions;
import android.graphics.Bitmap;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import java.util.HashMap;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.load.Transformation;
import java.util.Map;
import android.content.res.Resources$Theme;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Options;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class RequestOptions implements Cloneable
{
    private DiskCacheStrategy diskCacheStrategy;
    private int errorId;
    private Drawable errorPlaceholder;
    private Drawable fallbackDrawable;
    private int fallbackId;
    private int fields;
    private boolean isAutoCloneEnabled;
    private boolean isCacheable;
    private boolean isLocked;
    private boolean isScaleOnlyOrNoTransform;
    private boolean isTransformationAllowed;
    private boolean isTransformationRequired;
    private boolean onlyRetrieveFromCache;
    private Options options;
    private int overrideHeight;
    private int overrideWidth;
    private Drawable placeholderDrawable;
    private int placeholderId;
    private Priority priority;
    private Class<?> resourceClass;
    private Key signature;
    private float sizeMultiplier;
    private Resources$Theme theme;
    private Map<Class<?>, Transformation<?>> transformations;
    private boolean useUnlimitedSourceGeneratorsPool;
    
    public RequestOptions() {
        this.sizeMultiplier = 1.0f;
        this.diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;
        this.priority = Priority.NORMAL;
        this.isCacheable = true;
        this.overrideHeight = -1;
        this.overrideWidth = -1;
        this.signature = EmptySignature.obtain();
        this.isTransformationAllowed = true;
        this.options = new Options();
        this.transformations = new HashMap<Class<?>, Transformation<?>>();
        this.resourceClass = Object.class;
        this.isScaleOnlyOrNoTransform = true;
    }
    
    public static RequestOptions decodeTypeOf(final Class<?> clazz) {
        return new RequestOptions().decode(clazz);
    }
    
    public static RequestOptions diskCacheStrategyOf(final DiskCacheStrategy diskCacheStrategy) {
        return new RequestOptions().diskCacheStrategy(diskCacheStrategy);
    }
    
    private boolean isSet(final int n) {
        return isSet(this.fields, n);
    }
    
    private static boolean isSet(final int n, final int n2) {
        return (n & n2) != 0x0;
    }
    
    private RequestOptions optionalScaleOnlyTransform(final DownsampleStrategy downsampleStrategy, final Transformation<Bitmap> transformation) {
        return this.scaleOnlyTransform(downsampleStrategy, transformation, false);
    }
    
    private RequestOptions scaleOnlyTransform(final DownsampleStrategy downsampleStrategy, final Transformation<Bitmap> transformation) {
        return this.scaleOnlyTransform(downsampleStrategy, transformation, true);
    }
    
    private RequestOptions scaleOnlyTransform(final DownsampleStrategy downsampleStrategy, final Transformation<Bitmap> transformation, final boolean b) {
        RequestOptions requestOptions;
        if (b) {
            requestOptions = this.transform(downsampleStrategy, transformation);
        }
        else {
            requestOptions = this.optionalTransform(downsampleStrategy, transformation);
        }
        requestOptions.isScaleOnlyOrNoTransform = true;
        return requestOptions;
    }
    
    private RequestOptions selfOrThrowIfLocked() {
        if (!this.isLocked) {
            return this;
        }
        throw new IllegalStateException("You cannot modify locked RequestOptions, consider clone()");
    }
    
    public RequestOptions apply(final RequestOptions requestOptions) {
        if (this.isAutoCloneEnabled) {
            return this.clone().apply(requestOptions);
        }
        if (isSet(requestOptions.fields, 2)) {
            this.sizeMultiplier = requestOptions.sizeMultiplier;
        }
        if (isSet(requestOptions.fields, 262144)) {
            this.useUnlimitedSourceGeneratorsPool = requestOptions.useUnlimitedSourceGeneratorsPool;
        }
        if (isSet(requestOptions.fields, 4)) {
            this.diskCacheStrategy = requestOptions.diskCacheStrategy;
        }
        if (isSet(requestOptions.fields, 8)) {
            this.priority = requestOptions.priority;
        }
        if (isSet(requestOptions.fields, 16)) {
            this.errorPlaceholder = requestOptions.errorPlaceholder;
        }
        if (isSet(requestOptions.fields, 32)) {
            this.errorId = requestOptions.errorId;
        }
        if (isSet(requestOptions.fields, 64)) {
            this.placeholderDrawable = requestOptions.placeholderDrawable;
        }
        if (isSet(requestOptions.fields, 128)) {
            this.placeholderId = requestOptions.placeholderId;
        }
        if (isSet(requestOptions.fields, 256)) {
            this.isCacheable = requestOptions.isCacheable;
        }
        if (isSet(requestOptions.fields, 512)) {
            this.overrideWidth = requestOptions.overrideWidth;
            this.overrideHeight = requestOptions.overrideHeight;
        }
        if (isSet(requestOptions.fields, 1024)) {
            this.signature = requestOptions.signature;
        }
        if (isSet(requestOptions.fields, 4096)) {
            this.resourceClass = requestOptions.resourceClass;
        }
        if (isSet(requestOptions.fields, 8192)) {
            this.fallbackDrawable = requestOptions.fallbackDrawable;
        }
        if (isSet(requestOptions.fields, 16384)) {
            this.fallbackId = requestOptions.fallbackId;
        }
        if (isSet(requestOptions.fields, 32768)) {
            this.theme = requestOptions.theme;
        }
        if (isSet(requestOptions.fields, 65536)) {
            this.isTransformationAllowed = requestOptions.isTransformationAllowed;
        }
        if (isSet(requestOptions.fields, 131072)) {
            this.isTransformationRequired = requestOptions.isTransformationRequired;
        }
        if (isSet(requestOptions.fields, 2048)) {
            this.transformations.putAll(requestOptions.transformations);
            this.isScaleOnlyOrNoTransform = requestOptions.isScaleOnlyOrNoTransform;
        }
        if (isSet(requestOptions.fields, 524288)) {
            this.onlyRetrieveFromCache = requestOptions.onlyRetrieveFromCache;
        }
        if (!this.isTransformationAllowed) {
            this.transformations.clear();
            this.fields &= 0xFFFFF7FF;
            this.isTransformationRequired = false;
            this.fields &= 0xFFFDFFFF;
            this.isScaleOnlyOrNoTransform = true;
        }
        this.fields |= requestOptions.fields;
        this.options.putAll(requestOptions.options);
        return this.selfOrThrowIfLocked();
    }
    
    public RequestOptions autoClone() {
        if (this.isLocked && !this.isAutoCloneEnabled) {
            throw new IllegalStateException("You cannot auto lock an already locked options object, try clone() first");
        }
        this.isAutoCloneEnabled = true;
        return this.lock();
    }
    
    public RequestOptions clone() {
        try {
            final RequestOptions requestOptions = (RequestOptions)super.clone();
            (requestOptions.options = new Options()).putAll(this.options);
            (requestOptions.transformations = new HashMap<Class<?>, Transformation<?>>()).putAll(this.transformations);
            requestOptions.isLocked = false;
            requestOptions.isAutoCloneEnabled = false;
            return requestOptions;
        }
        catch (CloneNotSupportedException cause) {
            throw new RuntimeException(cause);
        }
    }
    
    public RequestOptions decode(final Class<?> clazz) {
        if (this.isAutoCloneEnabled) {
            return this.clone().decode(clazz);
        }
        this.resourceClass = Preconditions.checkNotNull(clazz);
        this.fields |= 0x1000;
        return this.selfOrThrowIfLocked();
    }
    
    public RequestOptions diskCacheStrategy(final DiskCacheStrategy diskCacheStrategy) {
        if (this.isAutoCloneEnabled) {
            return this.clone().diskCacheStrategy(diskCacheStrategy);
        }
        this.diskCacheStrategy = Preconditions.checkNotNull(diskCacheStrategy);
        this.fields |= 0x4;
        return this.selfOrThrowIfLocked();
    }
    
    public RequestOptions dontAnimate() {
        if (this.isAutoCloneEnabled) {
            return this.clone().dontAnimate();
        }
        return this.selfOrThrowIfLocked();
    }
    
    public RequestOptions downsample(final DownsampleStrategy downsampleStrategy) {
        return this.set(Downsampler.DOWNSAMPLE_STRATEGY, (DownsampleStrategy)Preconditions.checkNotNull((T)downsampleStrategy));
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof RequestOptions;
        final boolean b2 = false;
        if (b) {
            final RequestOptions requestOptions = (RequestOptions)o;
            boolean b3 = b2;
            if (Float.compare(requestOptions.sizeMultiplier, this.sizeMultiplier) == 0) {
                b3 = b2;
                if (this.errorId == requestOptions.errorId) {
                    b3 = b2;
                    if (Util.bothNullOrEqual(this.errorPlaceholder, requestOptions.errorPlaceholder)) {
                        b3 = b2;
                        if (this.placeholderId == requestOptions.placeholderId) {
                            b3 = b2;
                            if (Util.bothNullOrEqual(this.placeholderDrawable, requestOptions.placeholderDrawable)) {
                                b3 = b2;
                                if (this.fallbackId == requestOptions.fallbackId) {
                                    b3 = b2;
                                    if (Util.bothNullOrEqual(this.fallbackDrawable, requestOptions.fallbackDrawable)) {
                                        b3 = b2;
                                        if (this.isCacheable == requestOptions.isCacheable) {
                                            b3 = b2;
                                            if (this.overrideHeight == requestOptions.overrideHeight) {
                                                b3 = b2;
                                                if (this.overrideWidth == requestOptions.overrideWidth) {
                                                    b3 = b2;
                                                    if (this.isTransformationRequired == requestOptions.isTransformationRequired) {
                                                        b3 = b2;
                                                        if (this.isTransformationAllowed == requestOptions.isTransformationAllowed) {
                                                            b3 = b2;
                                                            if (this.useUnlimitedSourceGeneratorsPool == requestOptions.useUnlimitedSourceGeneratorsPool) {
                                                                b3 = b2;
                                                                if (this.onlyRetrieveFromCache == requestOptions.onlyRetrieveFromCache) {
                                                                    b3 = b2;
                                                                    if (this.diskCacheStrategy.equals(requestOptions.diskCacheStrategy)) {
                                                                        b3 = b2;
                                                                        if (this.priority == requestOptions.priority) {
                                                                            b3 = b2;
                                                                            if (this.options.equals(requestOptions.options)) {
                                                                                b3 = b2;
                                                                                if (this.transformations.equals(requestOptions.transformations)) {
                                                                                    b3 = b2;
                                                                                    if (this.resourceClass.equals(requestOptions.resourceClass)) {
                                                                                        b3 = b2;
                                                                                        if (Util.bothNullOrEqual(this.signature, requestOptions.signature)) {
                                                                                            b3 = b2;
                                                                                            if (Util.bothNullOrEqual(this.theme, requestOptions.theme)) {
                                                                                                b3 = true;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
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
    
    public RequestOptions fitCenter() {
        return this.scaleOnlyTransform(DownsampleStrategy.FIT_CENTER, new FitCenter());
    }
    
    public final DiskCacheStrategy getDiskCacheStrategy() {
        return this.diskCacheStrategy;
    }
    
    public final int getErrorId() {
        return this.errorId;
    }
    
    public final Drawable getErrorPlaceholder() {
        return this.errorPlaceholder;
    }
    
    public final Drawable getFallbackDrawable() {
        return this.fallbackDrawable;
    }
    
    public final int getFallbackId() {
        return this.fallbackId;
    }
    
    public final boolean getOnlyRetrieveFromCache() {
        return this.onlyRetrieveFromCache;
    }
    
    public final Options getOptions() {
        return this.options;
    }
    
    public final int getOverrideHeight() {
        return this.overrideHeight;
    }
    
    public final int getOverrideWidth() {
        return this.overrideWidth;
    }
    
    public final Drawable getPlaceholderDrawable() {
        return this.placeholderDrawable;
    }
    
    public final int getPlaceholderId() {
        return this.placeholderId;
    }
    
    public final Priority getPriority() {
        return this.priority;
    }
    
    public final Class<?> getResourceClass() {
        return this.resourceClass;
    }
    
    public final Key getSignature() {
        return this.signature;
    }
    
    public final float getSizeMultiplier() {
        return this.sizeMultiplier;
    }
    
    public final Resources$Theme getTheme() {
        return this.theme;
    }
    
    public final Map<Class<?>, Transformation<?>> getTransformations() {
        return this.transformations;
    }
    
    public final boolean getUseUnlimitedSourceGeneratorsPool() {
        return this.useUnlimitedSourceGeneratorsPool;
    }
    
    @Override
    public int hashCode() {
        return Util.hashCode(this.theme, Util.hashCode(this.signature, Util.hashCode(this.resourceClass, Util.hashCode(this.transformations, Util.hashCode(this.options, Util.hashCode(this.priority, Util.hashCode(this.diskCacheStrategy, Util.hashCode(this.onlyRetrieveFromCache, Util.hashCode(this.useUnlimitedSourceGeneratorsPool, Util.hashCode(this.isTransformationAllowed, Util.hashCode(this.isTransformationRequired, Util.hashCode(this.overrideWidth, Util.hashCode(this.overrideHeight, Util.hashCode(this.isCacheable, Util.hashCode(this.fallbackDrawable, Util.hashCode(this.fallbackId, Util.hashCode(this.placeholderDrawable, Util.hashCode(this.placeholderId, Util.hashCode(this.errorPlaceholder, Util.hashCode(this.errorId, Util.hashCode(this.sizeMultiplier)))))))))))))))))))));
    }
    
    public final boolean isMemoryCacheable() {
        return this.isCacheable;
    }
    
    public final boolean isPrioritySet() {
        return this.isSet(8);
    }
    
    public boolean isScaleOnlyOrNoTransform() {
        return this.isScaleOnlyOrNoTransform;
    }
    
    public final boolean isTransformationAllowed() {
        return this.isTransformationAllowed;
    }
    
    public final boolean isTransformationRequired() {
        return this.isTransformationRequired;
    }
    
    public final boolean isTransformationSet() {
        return this.isSet(2048);
    }
    
    public final boolean isValidOverride() {
        return Util.isValidDimensions(this.overrideWidth, this.overrideHeight);
    }
    
    public RequestOptions lock() {
        this.isLocked = true;
        return this;
    }
    
    public RequestOptions optionalCenterCrop() {
        return this.optionalTransform(DownsampleStrategy.CENTER_OUTSIDE, new CenterCrop());
    }
    
    public RequestOptions optionalCenterInside() {
        return this.optionalScaleOnlyTransform(DownsampleStrategy.CENTER_INSIDE, new CenterInside());
    }
    
    public RequestOptions optionalFitCenter() {
        return this.optionalScaleOnlyTransform(DownsampleStrategy.FIT_CENTER, new FitCenter());
    }
    
    public RequestOptions optionalTransform(final Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return this.clone().optionalTransform(transformation);
        }
        this.optionalTransform(Bitmap.class, transformation);
        this.optionalTransform(BitmapDrawable.class, new BitmapDrawableTransformation(transformation));
        return this.selfOrThrowIfLocked();
    }
    
    final RequestOptions optionalTransform(final DownsampleStrategy downsampleStrategy, final Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return this.clone().optionalTransform(downsampleStrategy, transformation);
        }
        this.downsample(downsampleStrategy);
        return this.optionalTransform(transformation);
    }
    
    public <T> RequestOptions optionalTransform(final Class<T> clazz, final Transformation<T> transformation) {
        if (this.isAutoCloneEnabled) {
            return this.clone().optionalTransform((Class<Object>)clazz, (Transformation<Object>)transformation);
        }
        Preconditions.checkNotNull((Object)clazz);
        Preconditions.checkNotNull((Object)transformation);
        this.transformations.put(clazz, transformation);
        this.fields |= 0x800;
        this.isTransformationAllowed = true;
        this.fields |= 0x10000;
        this.isScaleOnlyOrNoTransform = false;
        return this.selfOrThrowIfLocked();
    }
    
    public RequestOptions placeholder(final int placeholderId) {
        if (this.isAutoCloneEnabled) {
            return this.clone().placeholder(placeholderId);
        }
        this.placeholderId = placeholderId;
        this.fields |= 0x80;
        return this.selfOrThrowIfLocked();
    }
    
    public RequestOptions priority(final Priority priority) {
        if (this.isAutoCloneEnabled) {
            return this.clone().priority(priority);
        }
        this.priority = Preconditions.checkNotNull(priority);
        this.fields |= 0x8;
        return this.selfOrThrowIfLocked();
    }
    
    public <T> RequestOptions set(final Option<T> option, final T t) {
        if (this.isAutoCloneEnabled) {
            return this.clone().set((Option<Object>)option, t);
        }
        Preconditions.checkNotNull((Object)option);
        Preconditions.checkNotNull(t);
        this.options.set(option, t);
        return this.selfOrThrowIfLocked();
    }
    
    public RequestOptions sizeMultiplier(final float sizeMultiplier) {
        if (this.isAutoCloneEnabled) {
            return this.clone().sizeMultiplier(sizeMultiplier);
        }
        if (sizeMultiplier >= 0.0f && sizeMultiplier <= 1.0f) {
            this.sizeMultiplier = sizeMultiplier;
            this.fields |= 0x2;
            return this.selfOrThrowIfLocked();
        }
        throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
    }
    
    public RequestOptions skipMemoryCache(final boolean b) {
        if (this.isAutoCloneEnabled) {
            return this.clone().skipMemoryCache(true);
        }
        this.isCacheable = (b ^ true);
        this.fields |= 0x100;
        return this.selfOrThrowIfLocked();
    }
    
    public RequestOptions transform(final Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return this.clone().transform(transformation);
        }
        this.optionalTransform(transformation);
        this.isTransformationRequired = true;
        this.fields |= 0x20000;
        return this.selfOrThrowIfLocked();
    }
    
    final RequestOptions transform(final DownsampleStrategy downsampleStrategy, final Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return this.clone().transform(downsampleStrategy, transformation);
        }
        this.downsample(downsampleStrategy);
        return this.transform(transformation);
    }
    
    public RequestOptions transforms(final Transformation<Bitmap>... array) {
        if (this.isAutoCloneEnabled) {
            return this.clone().transforms(array);
        }
        this.optionalTransform(new MultiTransformation<Bitmap>(array));
        this.isTransformationRequired = true;
        this.fields |= 0x20000;
        return this.selfOrThrowIfLocked();
    }
}

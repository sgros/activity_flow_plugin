// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.glide;

import com.bumptech.glide.load.Option;
import com.bumptech.glide.Priority;
import android.graphics.Bitmap;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public final class GlideOptions extends RequestOptions
{
    @Override
    public GlideOptions apply(final RequestOptions requestOptions) {
        return (GlideOptions)super.apply(requestOptions);
    }
    
    @Override
    public GlideOptions autoClone() {
        return (GlideOptions)super.autoClone();
    }
    
    @Override
    public GlideOptions clone() {
        return (GlideOptions)super.clone();
    }
    
    @Override
    public GlideOptions decode(final Class<?> clazz) {
        return (GlideOptions)super.decode(clazz);
    }
    
    @Override
    public GlideOptions diskCacheStrategy(final DiskCacheStrategy diskCacheStrategy) {
        return (GlideOptions)super.diskCacheStrategy(diskCacheStrategy);
    }
    
    @Override
    public GlideOptions dontAnimate() {
        return (GlideOptions)super.dontAnimate();
    }
    
    @Override
    public GlideOptions downsample(final DownsampleStrategy downsampleStrategy) {
        return (GlideOptions)super.downsample(downsampleStrategy);
    }
    
    @Override
    public GlideOptions fitCenter() {
        return (GlideOptions)super.fitCenter();
    }
    
    @Override
    public GlideOptions lock() {
        return (GlideOptions)super.lock();
    }
    
    @Override
    public GlideOptions optionalCenterCrop() {
        return (GlideOptions)super.optionalCenterCrop();
    }
    
    @Override
    public GlideOptions optionalCenterInside() {
        return (GlideOptions)super.optionalCenterInside();
    }
    
    @Override
    public GlideOptions optionalFitCenter() {
        return (GlideOptions)super.optionalFitCenter();
    }
    
    @Override
    public GlideOptions optionalTransform(final Transformation<Bitmap> transformation) {
        return (GlideOptions)super.optionalTransform(transformation);
    }
    
    @Override
    public <T> GlideOptions optionalTransform(final Class<T> clazz, final Transformation<T> transformation) {
        return (GlideOptions)super.optionalTransform(clazz, transformation);
    }
    
    @Override
    public GlideOptions placeholder(final int n) {
        return (GlideOptions)super.placeholder(n);
    }
    
    @Override
    public GlideOptions priority(final Priority priority) {
        return (GlideOptions)super.priority(priority);
    }
    
    @Override
    public <T> GlideOptions set(final Option<T> option, final T t) {
        return (GlideOptions)super.set(option, t);
    }
    
    @Override
    public GlideOptions sizeMultiplier(final float n) {
        return (GlideOptions)super.sizeMultiplier(n);
    }
    
    @Override
    public GlideOptions skipMemoryCache(final boolean b) {
        return (GlideOptions)super.skipMemoryCache(b);
    }
    
    @Override
    public GlideOptions transform(final Transformation<Bitmap> transformation) {
        return (GlideOptions)super.transform(transformation);
    }
    
    @Override
    public GlideOptions transforms(final Transformation<Bitmap>... array) {
        return (GlideOptions)super.transforms(array);
    }
}

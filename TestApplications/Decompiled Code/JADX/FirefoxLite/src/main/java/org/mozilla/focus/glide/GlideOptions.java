package org.mozilla.focus.glide;

import android.graphics.Bitmap;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.RequestOptions;

public final class GlideOptions extends RequestOptions {
    public GlideOptions sizeMultiplier(float f) {
        return (GlideOptions) super.sizeMultiplier(f);
    }

    public GlideOptions diskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
        return (GlideOptions) super.diskCacheStrategy(diskCacheStrategy);
    }

    public GlideOptions priority(Priority priority) {
        return (GlideOptions) super.priority(priority);
    }

    public GlideOptions placeholder(int i) {
        return (GlideOptions) super.placeholder(i);
    }

    public GlideOptions skipMemoryCache(boolean z) {
        return (GlideOptions) super.skipMemoryCache(z);
    }

    public GlideOptions clone() {
        return (GlideOptions) super.clone();
    }

    public <T> GlideOptions set(Option<T> option, T t) {
        return (GlideOptions) super.set(option, t);
    }

    public GlideOptions decode(Class<?> cls) {
        return (GlideOptions) super.decode(cls);
    }

    public GlideOptions downsample(DownsampleStrategy downsampleStrategy) {
        return (GlideOptions) super.downsample(downsampleStrategy);
    }

    public GlideOptions optionalCenterCrop() {
        return (GlideOptions) super.optionalCenterCrop();
    }

    public GlideOptions optionalFitCenter() {
        return (GlideOptions) super.optionalFitCenter();
    }

    public GlideOptions fitCenter() {
        return (GlideOptions) super.fitCenter();
    }

    public GlideOptions optionalCenterInside() {
        return (GlideOptions) super.optionalCenterInside();
    }

    public GlideOptions transform(Transformation<Bitmap> transformation) {
        return (GlideOptions) super.transform(transformation);
    }

    public GlideOptions transforms(Transformation<Bitmap>... transformationArr) {
        return (GlideOptions) super.transforms(transformationArr);
    }

    public GlideOptions optionalTransform(Transformation<Bitmap> transformation) {
        return (GlideOptions) super.optionalTransform(transformation);
    }

    public <T> GlideOptions optionalTransform(Class<T> cls, Transformation<T> transformation) {
        return (GlideOptions) super.optionalTransform((Class) cls, (Transformation) transformation);
    }

    public GlideOptions dontAnimate() {
        return (GlideOptions) super.dontAnimate();
    }

    public GlideOptions apply(RequestOptions requestOptions) {
        return (GlideOptions) super.apply(requestOptions);
    }

    public GlideOptions lock() {
        return (GlideOptions) super.lock();
    }

    public GlideOptions autoClone() {
        return (GlideOptions) super.autoClone();
    }
}

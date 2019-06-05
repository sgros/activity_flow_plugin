package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public class BitmapDrawableTransformation implements Transformation<BitmapDrawable> {
    private final Transformation<Bitmap> wrapped;

    public BitmapDrawableTransformation(Transformation<Bitmap> transformation) {
        this.wrapped = (Transformation) Preconditions.checkNotNull(transformation);
    }

    public Resource<BitmapDrawable> transform(Context context, Resource<BitmapDrawable> resource, int i, int i2) {
        BitmapResource obtain = BitmapResource.obtain(((BitmapDrawable) resource.get()).getBitmap(), Glide.get(context).getBitmapPool());
        Resource transform = this.wrapped.transform(context, obtain, i, i2);
        if (transform.equals(obtain)) {
            return resource;
        }
        return LazyBitmapDrawableResource.obtain(context, (Bitmap) transform.get());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BitmapDrawableTransformation)) {
            return false;
        }
        return this.wrapped.equals(((BitmapDrawableTransformation) obj).wrapped);
    }

    public int hashCode() {
        return this.wrapped.hashCode();
    }

    public void updateDiskCacheKey(MessageDigest messageDigest) {
        this.wrapped.updateDiskCacheKey(messageDigest);
    }
}

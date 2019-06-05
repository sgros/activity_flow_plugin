// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.load.engine.Resource;
import android.content.Context;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import android.graphics.Bitmap;
import com.bumptech.glide.load.Transformation;

public abstract class BitmapTransformation implements Transformation<Bitmap>
{
    protected abstract Bitmap transform(final BitmapPool p0, final Bitmap p1, final int p2, final int p3);
    
    @Override
    public final Resource<Bitmap> transform(final Context context, Resource<Bitmap> obtain, int height, final int i) {
        if (Util.isValidDimensions(height, i)) {
            final BitmapPool bitmapPool = Glide.get(context).getBitmapPool();
            final Bitmap bitmap = obtain.get();
            int width;
            if ((width = height) == Integer.MIN_VALUE) {
                width = bitmap.getWidth();
            }
            if ((height = i) == Integer.MIN_VALUE) {
                height = bitmap.getHeight();
            }
            final Bitmap transform = this.transform(bitmapPool, bitmap, width, height);
            if (!bitmap.equals(transform)) {
                obtain = BitmapResource.obtain(transform, bitmapPool);
            }
            return obtain;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot apply transformation on width: ");
        sb.append(height);
        sb.append(" or height: ");
        sb.append(i);
        sb.append(" less than or equal to zero and not Target.SIZE_ORIGINAL");
        throw new IllegalArgumentException(sb.toString());
    }
}

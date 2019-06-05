// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.glide.transformation;

import java.security.MessageDigest;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.nio.charset.StandardCharsets;
import android.graphics.Paint;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class ShrinkSizeTransformation extends BitmapTransformation
{
    private static final byte[] ID_BYTES;
    private static Paint paint;
    private double scale;
    
    static {
        ShrinkSizeTransformation.paint = new Paint();
        ID_BYTES = "org.mozilla.PorterDuffTransformation".getBytes(StandardCharsets.UTF_8);
    }
    
    public ShrinkSizeTransformation(final double scale) {
        if (scale < 1.0) {
            this.scale = scale;
            return;
        }
        throw new IllegalArgumentException("Scale should be <= 1");
    }
    
    private int margin(final int n, final double n2) {
        return (int)(n * (1.0 / n2 - 1.0) / 2.0);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof ShrinkSizeTransformation;
    }
    
    @Override
    public int hashCode() {
        return "org.mozilla.PorterDuffTransformation".hashCode();
    }
    
    @Override
    protected Bitmap transform(final BitmapPool bitmapPool, final Bitmap bitmap, final int n, final int n2) {
        final Bitmap value = bitmapPool.get((int)(bitmap.getWidth() / this.scale), (int)(bitmap.getHeight() / this.scale), bitmap.getConfig());
        new Canvas(value).drawBitmap(bitmap, (float)this.margin(bitmap.getWidth(), this.scale), (float)this.margin(bitmap.getHeight(), this.scale), ShrinkSizeTransformation.paint);
        return value;
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
        messageDigest.update(ShrinkSizeTransformation.ID_BYTES);
    }
}

package org.mozilla.rocket.glide.transformation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class ShrinkSizeTransformation extends BitmapTransformation {
    private static final byte[] ID_BYTES = "org.mozilla.PorterDuffTransformation".getBytes(StandardCharsets.UTF_8);
    private static Paint paint = new Paint();
    private double scale;

    private int margin(int i, double d) {
        return (int) ((((double) i) * ((1.0d / d) - 1.0d)) / 2.0d);
    }

    public ShrinkSizeTransformation(double d) {
        if (d < 1.0d) {
            this.scale = d;
            return;
        }
        throw new IllegalArgumentException("Scale should be <= 1");
    }

    /* Access modifiers changed, original: protected */
    public Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        Bitmap bitmap2 = bitmapPool.get((int) (((double) bitmap.getWidth()) / this.scale), (int) (((double) bitmap.getHeight()) / this.scale), bitmap.getConfig());
        new Canvas(bitmap2).drawBitmap(bitmap, (float) margin(bitmap.getWidth(), this.scale), (float) margin(bitmap.getHeight(), this.scale), paint);
        return bitmap2;
    }

    public boolean equals(Object obj) {
        return obj instanceof ShrinkSizeTransformation;
    }

    public int hashCode() {
        return "org.mozilla.PorterDuffTransformation".hashCode();
    }

    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}

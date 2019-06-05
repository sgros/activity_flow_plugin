package org.mozilla.rocket.glide.transformation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PorterDuffTransformation extends BitmapTransformation {
    private static final byte[] ID_BYTES = "org.mozilla.PorterDuffTransformation".getBytes(StandardCharsets.UTF_8);
    private static Paint paint = new Paint();
    private PorterDuffColorFilter porterDuffColorFilter;

    public PorterDuffTransformation(PorterDuffColorFilter porterDuffColorFilter) {
        this.porterDuffColorFilter = porterDuffColorFilter;
    }

    /* Access modifiers changed, original: protected */
    public Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        paint.setColorFilter(this.porterDuffColorFilter);
        Bitmap bitmap2 = bitmapPool.get(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        new Canvas(bitmap2).drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return bitmap2;
    }

    public boolean equals(Object obj) {
        return obj instanceof PorterDuffTransformation;
    }

    public int hashCode() {
        return "org.mozilla.PorterDuffTransformation".hashCode();
    }

    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}

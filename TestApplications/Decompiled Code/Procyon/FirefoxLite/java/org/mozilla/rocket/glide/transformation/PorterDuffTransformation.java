// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.glide.transformation;

import java.security.MessageDigest;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.nio.charset.StandardCharsets;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Paint;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class PorterDuffTransformation extends BitmapTransformation
{
    private static final byte[] ID_BYTES;
    private static Paint paint;
    private PorterDuffColorFilter porterDuffColorFilter;
    
    static {
        PorterDuffTransformation.paint = new Paint();
        ID_BYTES = "org.mozilla.PorterDuffTransformation".getBytes(StandardCharsets.UTF_8);
    }
    
    public PorterDuffTransformation(final PorterDuffColorFilter porterDuffColorFilter) {
        this.porterDuffColorFilter = porterDuffColorFilter;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof PorterDuffTransformation;
    }
    
    @Override
    public int hashCode() {
        return "org.mozilla.PorterDuffTransformation".hashCode();
    }
    
    @Override
    protected Bitmap transform(final BitmapPool bitmapPool, final Bitmap bitmap, final int n, final int n2) {
        PorterDuffTransformation.paint.setColorFilter((ColorFilter)this.porterDuffColorFilter);
        final Bitmap value = bitmapPool.get(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        new Canvas(value).drawBitmap(bitmap, 0.0f, 0.0f, PorterDuffTransformation.paint);
        return value;
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
        messageDigest.update(PorterDuffTransformation.ID_BYTES);
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;

public final class RoundedCorners extends BitmapTransformation
{
    private static final byte[] ID_BYTES;
    private final int roundingRadius;
    
    static {
        ID_BYTES = "com.bumptech.glide.load.resource.bitmap.RoundedCorners".getBytes(RoundedCorners.CHARSET);
    }
    
    public RoundedCorners(final int roundingRadius) {
        Preconditions.checkArgument(roundingRadius > 0, "roundingRadius must be greater than 0.");
        this.roundingRadius = roundingRadius;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof RoundedCorners && ((RoundedCorners)o).roundingRadius == this.roundingRadius;
    }
    
    @Override
    public int hashCode() {
        return "com.bumptech.glide.load.resource.bitmap.RoundedCorners".hashCode() + this.roundingRadius;
    }
    
    @Override
    protected Bitmap transform(final BitmapPool bitmapPool, final Bitmap bitmap, final int n, final int n2) {
        return TransformationUtils.roundedCorners(bitmapPool, bitmap, n, n2, this.roundingRadius);
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
        messageDigest.update(RoundedCorners.ID_BYTES);
        messageDigest.update(ByteBuffer.allocate(4).putInt(this.roundingRadius).array());
    }
}

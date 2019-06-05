// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.utils;

import java.util.List;

public final class FSize extends Poolable
{
    private static ObjectPool<FSize> pool;
    public float height;
    public float width;
    
    static {
        (FSize.pool = (ObjectPool<FSize>)ObjectPool.create(256, (ObjectPool.Poolable)new FSize(0.0f, 0.0f))).setReplenishPercentage(0.5f);
    }
    
    public FSize() {
    }
    
    public FSize(final float width, final float height) {
        this.width = width;
        this.height = height;
    }
    
    public static FSize getInstance(final float width, final float height) {
        final FSize fSize = FSize.pool.get();
        fSize.width = width;
        fSize.height = height;
        return fSize;
    }
    
    public static void recycleInstance(final FSize fSize) {
        FSize.pool.recycle(fSize);
    }
    
    public static void recycleInstances(final List<FSize> list) {
        FSize.pool.recycle(list);
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof FSize) {
            final FSize fSize = (FSize)o;
            boolean b2 = b;
            if (this.width == fSize.width) {
                b2 = b;
                if (this.height == fSize.height) {
                    b2 = true;
                }
            }
            return b2;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Float.floatToIntBits(this.width) ^ Float.floatToIntBits(this.height);
    }
    
    @Override
    protected Poolable instantiate() {
        return new FSize(0.0f, 0.0f);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.width);
        sb.append("x");
        sb.append(this.height);
        return sb.toString();
    }
}

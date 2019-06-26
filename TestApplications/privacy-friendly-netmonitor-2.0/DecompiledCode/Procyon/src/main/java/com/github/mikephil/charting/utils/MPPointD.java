// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.utils;

import java.util.List;

public class MPPointD extends Poolable
{
    private static ObjectPool<MPPointD> pool;
    public double x;
    public double y;
    
    static {
        (MPPointD.pool = (ObjectPool<MPPointD>)ObjectPool.create(64, (ObjectPool.Poolable)new MPPointD(0.0, 0.0))).setReplenishPercentage(0.5f);
    }
    
    private MPPointD(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public static MPPointD getInstance(final double x, final double y) {
        final MPPointD mpPointD = MPPointD.pool.get();
        mpPointD.x = x;
        mpPointD.y = y;
        return mpPointD;
    }
    
    public static void recycleInstance(final MPPointD mpPointD) {
        MPPointD.pool.recycle(mpPointD);
    }
    
    public static void recycleInstances(final List<MPPointD> list) {
        MPPointD.pool.recycle(list);
    }
    
    @Override
    protected Poolable instantiate() {
        return new MPPointD(0.0, 0.0);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MPPointD, x: ");
        sb.append(this.x);
        sb.append(", y: ");
        sb.append(this.y);
        return sb.toString();
    }
}

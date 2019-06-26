// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.utils;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class MPPointF extends Poolable
{
    public static final Parcelable$Creator<MPPointF> CREATOR;
    private static ObjectPool<MPPointF> pool;
    public float x;
    public float y;
    
    static {
        (MPPointF.pool = (ObjectPool<MPPointF>)ObjectPool.create(32, (ObjectPool.Poolable)new MPPointF(0.0f, 0.0f))).setReplenishPercentage(0.5f);
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<MPPointF>() {
            public MPPointF createFromParcel(final Parcel parcel) {
                final MPPointF mpPointF = new MPPointF(0.0f, 0.0f);
                mpPointF.my_readFromParcel(parcel);
                return mpPointF;
            }
            
            public MPPointF[] newArray(final int n) {
                return new MPPointF[n];
            }
        };
    }
    
    public MPPointF() {
    }
    
    public MPPointF(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public static MPPointF getInstance() {
        return MPPointF.pool.get();
    }
    
    public static MPPointF getInstance(final float x, final float y) {
        final MPPointF mpPointF = MPPointF.pool.get();
        mpPointF.x = x;
        mpPointF.y = y;
        return mpPointF;
    }
    
    public static MPPointF getInstance(final MPPointF mpPointF) {
        final MPPointF mpPointF2 = MPPointF.pool.get();
        mpPointF2.x = mpPointF.x;
        mpPointF2.y = mpPointF.y;
        return mpPointF2;
    }
    
    public static void recycleInstance(final MPPointF mpPointF) {
        MPPointF.pool.recycle(mpPointF);
    }
    
    public static void recycleInstances(final List<MPPointF> list) {
        MPPointF.pool.recycle(list);
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    @Override
    protected Poolable instantiate() {
        return new MPPointF(0.0f, 0.0f);
    }
    
    public void my_readFromParcel(final Parcel parcel) {
        this.x = parcel.readFloat();
        this.y = parcel.readFloat();
    }
}

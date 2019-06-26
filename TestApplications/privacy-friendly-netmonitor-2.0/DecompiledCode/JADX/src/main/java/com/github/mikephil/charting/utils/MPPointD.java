package com.github.mikephil.charting.utils;

import com.github.mikephil.charting.utils.ObjectPool.Poolable;
import java.util.List;

public class MPPointD extends Poolable {
    private static ObjectPool<MPPointD> pool = ObjectPool.create(64, new MPPointD(Utils.DOUBLE_EPSILON, Utils.DOUBLE_EPSILON));
    /* renamed from: x */
    public double f486x;
    /* renamed from: y */
    public double f487y;

    static {
        pool.setReplenishPercentage(0.5f);
    }

    public static MPPointD getInstance(double d, double d2) {
        MPPointD mPPointD = (MPPointD) pool.get();
        mPPointD.f486x = d;
        mPPointD.f487y = d2;
        return mPPointD;
    }

    public static void recycleInstance(MPPointD mPPointD) {
        pool.recycle((Poolable) mPPointD);
    }

    public static void recycleInstances(List<MPPointD> list) {
        pool.recycle((List) list);
    }

    /* Access modifiers changed, original: protected */
    public Poolable instantiate() {
        return new MPPointD(Utils.DOUBLE_EPSILON, Utils.DOUBLE_EPSILON);
    }

    private MPPointD(double d, double d2) {
        this.f486x = d;
        this.f487y = d2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MPPointD, x: ");
        stringBuilder.append(this.f486x);
        stringBuilder.append(", y: ");
        stringBuilder.append(this.f487y);
        return stringBuilder.toString();
    }
}

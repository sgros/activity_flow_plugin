package com.github.mikephil.charting.utils;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.github.mikephil.charting.utils.ObjectPool.Poolable;
import java.util.List;

public class MPPointF extends Poolable {
    public static final Creator<MPPointF> CREATOR = new C04351();
    private static ObjectPool<MPPointF> pool = ObjectPool.create(32, new MPPointF(0.0f, 0.0f));
    /* renamed from: x */
    public float f488x;
    /* renamed from: y */
    public float f489y;

    /* renamed from: com.github.mikephil.charting.utils.MPPointF$1 */
    static class C04351 implements Creator<MPPointF> {
        C04351() {
        }

        public MPPointF createFromParcel(Parcel parcel) {
            MPPointF mPPointF = new MPPointF(0.0f, 0.0f);
            mPPointF.my_readFromParcel(parcel);
            return mPPointF;
        }

        public MPPointF[] newArray(int i) {
            return new MPPointF[i];
        }
    }

    static {
        pool.setReplenishPercentage(0.5f);
    }

    public MPPointF(float f, float f2) {
        this.f488x = f;
        this.f489y = f2;
    }

    public static MPPointF getInstance(float f, float f2) {
        MPPointF mPPointF = (MPPointF) pool.get();
        mPPointF.f488x = f;
        mPPointF.f489y = f2;
        return mPPointF;
    }

    public static MPPointF getInstance() {
        return (MPPointF) pool.get();
    }

    public static MPPointF getInstance(MPPointF mPPointF) {
        MPPointF mPPointF2 = (MPPointF) pool.get();
        mPPointF2.f488x = mPPointF.f488x;
        mPPointF2.f489y = mPPointF.f489y;
        return mPPointF2;
    }

    public static void recycleInstance(MPPointF mPPointF) {
        pool.recycle((Poolable) mPPointF);
    }

    public static void recycleInstances(List<MPPointF> list) {
        pool.recycle((List) list);
    }

    public void my_readFromParcel(Parcel parcel) {
        this.f488x = parcel.readFloat();
        this.f489y = parcel.readFloat();
    }

    public float getX() {
        return this.f488x;
    }

    public float getY() {
        return this.f489y;
    }

    /* Access modifiers changed, original: protected */
    public Poolable instantiate() {
        return new MPPointF(0.0f, 0.0f);
    }
}

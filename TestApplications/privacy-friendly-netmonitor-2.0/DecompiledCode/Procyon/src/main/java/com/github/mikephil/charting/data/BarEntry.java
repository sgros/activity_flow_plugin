// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.highlight.Range;
import android.annotation.SuppressLint;

@SuppressLint({ "ParcelCreator" })
public class BarEntry extends Entry
{
    private float mNegativeSum;
    private float mPositiveSum;
    private Range[] mRanges;
    private float[] mYVals;
    
    public BarEntry(final float n, final float n2) {
        super(n, n2);
    }
    
    public BarEntry(final float n, final float n2, final Drawable drawable) {
        super(n, n2, drawable);
    }
    
    public BarEntry(final float n, final float n2, final Drawable drawable, final Object o) {
        super(n, n2, drawable, o);
    }
    
    public BarEntry(final float n, final float n2, final Object o) {
        super(n, n2, o);
    }
    
    public BarEntry(final float n, final float[] myVals) {
        super(n, calcSum(myVals));
        this.mYVals = myVals;
        this.calcPosNegSum();
        this.calcRanges();
    }
    
    public BarEntry(final float n, final float[] myVals, final Drawable drawable) {
        super(n, calcSum(myVals), drawable);
        this.mYVals = myVals;
        this.calcPosNegSum();
        this.calcRanges();
    }
    
    public BarEntry(final float n, final float[] myVals, final Drawable drawable, final Object o) {
        super(n, calcSum(myVals), drawable, o);
        this.mYVals = myVals;
        this.calcPosNegSum();
        this.calcRanges();
    }
    
    public BarEntry(final float n, final float[] myVals, final Object o) {
        super(n, calcSum(myVals), o);
        this.mYVals = myVals;
        this.calcPosNegSum();
        this.calcRanges();
    }
    
    private void calcPosNegSum() {
        if (this.mYVals == null) {
            this.mNegativeSum = 0.0f;
            this.mPositiveSum = 0.0f;
            return;
        }
        final float[] myVals = this.mYVals;
        final int length = myVals.length;
        int i = 0;
        float mPositiveSum;
        float mNegativeSum = mPositiveSum = 0.0f;
        while (i < length) {
            final float a = myVals[i];
            if (a <= 0.0f) {
                mNegativeSum += Math.abs(a);
            }
            else {
                mPositiveSum += a;
            }
            ++i;
        }
        this.mNegativeSum = mNegativeSum;
        this.mPositiveSum = mPositiveSum;
    }
    
    private static float calcSum(final float[] array) {
        float n = 0.0f;
        if (array == null) {
            return 0.0f;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            n += array[i];
        }
        return n;
    }
    
    protected void calcRanges() {
        final float[] yVals = this.getYVals();
        if (yVals != null && yVals.length != 0) {
            this.mRanges = new Range[yVals.length];
            float n = -this.getNegativeSum();
            int i = 0;
            float n2 = 0.0f;
            while (i < this.mRanges.length) {
                final float n3 = yVals[i];
                if (n3 < 0.0f) {
                    final Range[] mRanges = this.mRanges;
                    final float n4 = n - n3;
                    mRanges[i] = new Range(n, n4);
                    n = n4;
                }
                else {
                    final Range[] mRanges2 = this.mRanges;
                    final float n5 = n3 + n2;
                    mRanges2[i] = new Range(n2, n5);
                    n2 = n5;
                }
                ++i;
            }
        }
    }
    
    @Override
    public BarEntry copy() {
        final BarEntry barEntry = new BarEntry(this.getX(), this.getY(), this.getData());
        barEntry.setVals(this.mYVals);
        return barEntry;
    }
    
    @Deprecated
    public float getBelowSum(final int n) {
        return this.getSumBelow(n);
    }
    
    public float getNegativeSum() {
        return this.mNegativeSum;
    }
    
    public float getPositiveSum() {
        return this.mPositiveSum;
    }
    
    public Range[] getRanges() {
        return this.mRanges;
    }
    
    public float getSumBelow(final int n) {
        final float[] myVals = this.mYVals;
        float n2 = 0.0f;
        if (myVals == null) {
            return 0.0f;
        }
        for (int n3 = this.mYVals.length - 1; n3 > n && n3 >= 0; --n3) {
            n2 += this.mYVals[n3];
        }
        return n2;
    }
    
    @Override
    public float getY() {
        return super.getY();
    }
    
    public float[] getYVals() {
        return this.mYVals;
    }
    
    public boolean isStacked() {
        return this.mYVals != null;
    }
    
    public void setVals(final float[] myVals) {
        this.setY(calcSum(myVals));
        this.mYVals = myVals;
        this.calcPosNegSum();
        this.calcRanges();
    }
}

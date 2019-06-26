package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class BarBuffer extends AbstractBuffer<IBarDataSet> {
    protected float mBarWidth = 1.0f;
    protected boolean mContainsStacks = false;
    protected int mDataSetCount = 1;
    protected int mDataSetIndex = 0;
    protected boolean mInverted = false;

    public BarBuffer(int i, int i2, boolean z) {
        super(i);
        this.mDataSetCount = i2;
        this.mContainsStacks = z;
    }

    public void setBarWidth(float f) {
        this.mBarWidth = f;
    }

    public void setDataSet(int i) {
        this.mDataSetIndex = i;
    }

    public void setInverted(boolean z) {
        this.mInverted = z;
    }

    /* Access modifiers changed, original: protected */
    public void addBar(float f, float f2, float f3, float f4) {
        float[] fArr = this.buffer;
        int i = this.index;
        this.index = i + 1;
        fArr[i] = f;
        float[] fArr2 = this.buffer;
        int i2 = this.index;
        this.index = i2 + 1;
        fArr2[i2] = f2;
        fArr2 = this.buffer;
        int i3 = this.index;
        this.index = i3 + 1;
        fArr2[i3] = f3;
        fArr2 = this.buffer;
        i3 = this.index;
        this.index = i3 + 1;
        fArr2[i3] = f4;
    }

    public void feed(IBarDataSet iBarDataSet) {
        float entryCount = ((float) iBarDataSet.getEntryCount()) * this.phaseX;
        float f = this.mBarWidth / 2.0f;
        for (int i = 0; ((float) i) < entryCount; i++) {
            BarEntry barEntry = (BarEntry) iBarDataSet.getEntryForIndex(i);
            if (barEntry != null) {
                float x = barEntry.getX();
                float y = barEntry.getY();
                float[] yVals = barEntry.getYVals();
                float f2;
                float f3;
                if (!this.mContainsStacks || yVals == null) {
                    float f4;
                    f2 = x - f;
                    x += f;
                    if (this.mInverted) {
                        f4 = y >= 0.0f ? y : 0.0f;
                        if (y > 0.0f) {
                            y = 0.0f;
                        }
                        f3 = y;
                        y = f4;
                        f4 = f3;
                    } else {
                        f4 = y >= 0.0f ? y : 0.0f;
                        if (y > 0.0f) {
                            y = 0.0f;
                        }
                    }
                    if (f4 > 0.0f) {
                        f4 *= this.phaseY;
                    } else {
                        y *= this.phaseY;
                    }
                    addBar(f2, f4, x, y);
                } else {
                    float f5 = -barEntry.getNegativeSum();
                    y = 0.0f;
                    for (float f6 : yVals) {
                        float f62;
                        float f7;
                        if (f62 == 0.0f && (y == 0.0f || f5 == 0.0f)) {
                            f7 = f62;
                        } else if (f62 >= 0.0f) {
                            f7 = f62 + y;
                            f62 = y;
                            y = f7;
                        } else {
                            f7 = Math.abs(f62) + f5;
                            f3 = Math.abs(f62) + f5;
                            f62 = f5;
                            f5 = f3;
                        }
                        float f8 = x - f;
                        float f9 = x + f;
                        if (this.mInverted) {
                            f2 = f62 >= f7 ? f62 : f7;
                            if (f62 > f7) {
                                f62 = f7;
                            }
                        } else {
                            f2 = f62 >= f7 ? f62 : f7;
                            if (f62 > f7) {
                                f62 = f7;
                            }
                            f3 = f62;
                            f62 = f2;
                            f2 = f3;
                        }
                        addBar(f8, f62 * this.phaseY, f9, f2 * this.phaseY);
                    }
                }
            }
        }
        reset();
    }
}

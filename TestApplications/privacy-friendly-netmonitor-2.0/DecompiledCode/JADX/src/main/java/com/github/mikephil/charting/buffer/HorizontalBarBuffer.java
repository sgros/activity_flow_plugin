package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class HorizontalBarBuffer extends BarBuffer {
    public HorizontalBarBuffer(int i, int i2, boolean z) {
        super(i, i2, z);
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
                    addBar(y, x, f4, f2);
                } else {
                    float f5 = -barEntry.getNegativeSum();
                    y = 0.0f;
                    int i2 = 0;
                    while (i2 < yVals.length) {
                        float f6;
                        float f7 = yVals[i2];
                        if (f7 >= 0.0f) {
                            f7 += y;
                            f6 = f5;
                            f5 = f7;
                        } else {
                            f6 = Math.abs(f7) + f5;
                            f7 = Math.abs(f7) + f5;
                            f3 = f5;
                            f5 = y;
                            y = f3;
                            float f8 = f6;
                            f6 = f7;
                            f7 = f8;
                        }
                        float f9 = x - f;
                        float f10 = x + f;
                        if (this.mInverted) {
                            f2 = y >= f7 ? y : f7;
                            if (y <= f7) {
                                f7 = y;
                            }
                        } else {
                            f2 = y >= f7 ? y : f7;
                            if (y <= f7) {
                                f7 = y;
                            }
                            f3 = f7;
                            f7 = f2;
                            f2 = f3;
                        }
                        addBar(f2 * this.phaseY, f10, f7 * this.phaseY, f9);
                        i2++;
                        y = f5;
                        f5 = f6;
                    }
                }
            }
        }
        reset();
    }
}

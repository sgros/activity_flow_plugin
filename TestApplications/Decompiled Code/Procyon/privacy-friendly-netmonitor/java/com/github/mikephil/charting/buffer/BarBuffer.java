// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class BarBuffer extends AbstractBuffer<IBarDataSet>
{
    protected float mBarWidth;
    protected boolean mContainsStacks;
    protected int mDataSetCount;
    protected int mDataSetIndex;
    protected boolean mInverted;
    
    public BarBuffer(final int n, final int mDataSetCount, final boolean mContainsStacks) {
        super(n);
        this.mDataSetIndex = 0;
        this.mDataSetCount = 1;
        this.mContainsStacks = false;
        this.mInverted = false;
        this.mBarWidth = 1.0f;
        this.mDataSetCount = mDataSetCount;
        this.mContainsStacks = mContainsStacks;
    }
    
    protected void addBar(final float n, final float n2, final float n3, final float n4) {
        this.buffer[this.index++] = n;
        this.buffer[this.index++] = n2;
        this.buffer[this.index++] = n3;
        this.buffer[this.index++] = n4;
    }
    
    @Override
    public void feed(final IBarDataSet set) {
        final float n = (float)set.getEntryCount();
        final float phaseX = this.phaseX;
        final float n2 = this.mBarWidth / 2.0f;
        for (int n3 = 0; n3 < n * phaseX; ++n3) {
            final BarEntry barEntry = set.getEntryForIndex(n3);
            if (barEntry != null) {
                final float x = barEntry.getX();
                float y = barEntry.getY();
                final float[] yVals = barEntry.getYVals();
                if (this.mContainsStacks && yVals != null) {
                    float n4 = -barEntry.getNegativeSum();
                    float n5 = 0.0f;
                    float n8;
                    float n9;
                    for (int i = 0; i < yVals.length; ++i, n5 = n8, n4 = n9) {
                        final float n6 = yVals[i];
                        float n10;
                        float n11;
                        if (n6 == 0.0f && (n5 == 0.0f || n4 == 0.0f)) {
                            final float n7 = n6;
                            n8 = n5;
                            n9 = n4;
                            n10 = n6;
                            n11 = n7;
                        }
                        else if (n6 >= 0.0f) {
                            n11 = (n8 = n6 + n5);
                            n9 = n4;
                            n10 = n5;
                        }
                        else {
                            final float n12 = Math.abs(n6) + n4;
                            final float abs = Math.abs(n6);
                            final float n13 = n4;
                            final float n14 = abs + n4;
                            n11 = n12;
                            n10 = n13;
                            n9 = n14;
                            n8 = n5;
                        }
                        float n15;
                        if (this.mInverted) {
                            if (n10 >= n11) {
                                n15 = n10;
                            }
                            else {
                                n15 = n11;
                            }
                            if (n10 > n11) {
                                n10 = n11;
                            }
                        }
                        else {
                            float n16;
                            if (n10 >= n11) {
                                n16 = n10;
                            }
                            else {
                                n16 = n11;
                            }
                            if (n10 > n11) {
                                n10 = n11;
                            }
                            final float n17 = n16;
                            n15 = n10;
                            n10 = n17;
                        }
                        this.addBar(x - n2, n10 * this.phaseY, x + n2, n15 * this.phaseY);
                    }
                }
                else {
                    float n20;
                    if (this.mInverted) {
                        float n18;
                        if (y >= 0.0f) {
                            n18 = y;
                        }
                        else {
                            n18 = 0.0f;
                        }
                        if (y > 0.0f) {
                            y = 0.0f;
                        }
                        final float n19 = n18;
                        n20 = y;
                        y = n19;
                    }
                    else {
                        if (y >= 0.0f) {
                            n20 = y;
                        }
                        else {
                            n20 = 0.0f;
                        }
                        if (y > 0.0f) {
                            y = 0.0f;
                        }
                    }
                    if (n20 > 0.0f) {
                        n20 *= this.phaseY;
                    }
                    else {
                        y *= this.phaseY;
                    }
                    this.addBar(x - n2, n20, x + n2, y);
                }
            }
        }
        this.reset();
    }
    
    public void setBarWidth(final float mBarWidth) {
        this.mBarWidth = mBarWidth;
    }
    
    public void setDataSet(final int mDataSetIndex) {
        this.mDataSetIndex = mDataSetIndex;
    }
    
    public void setInverted(final boolean mInverted) {
        this.mInverted = mInverted;
    }
}

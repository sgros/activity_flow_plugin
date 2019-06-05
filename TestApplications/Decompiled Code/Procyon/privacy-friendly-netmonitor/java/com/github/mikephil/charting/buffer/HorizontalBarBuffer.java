// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class HorizontalBarBuffer extends BarBuffer
{
    public HorizontalBarBuffer(final int n, final int n2, final boolean b) {
        super(n, n2, b);
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
                    float n9;
                    float n11;
                    for (int i = 0; i < yVals.length; ++i, n5 = n9, n4 = n11) {
                        final float n6 = yVals[i];
                        float n10;
                        if (n6 >= 0.0f) {
                            final float n7 = n6 + n5;
                            final float n8 = n4;
                            n9 = n7;
                            n10 = n7;
                            n11 = n8;
                        }
                        else {
                            final float abs = Math.abs(n6);
                            final float abs2 = Math.abs(n6);
                            n9 = n5;
                            n5 = n4;
                            n11 = abs2 + n4;
                            n10 = abs + n4;
                        }
                        float n13;
                        float n14;
                        if (this.mInverted) {
                            float n12;
                            if (n5 >= n10) {
                                n12 = n5;
                            }
                            else {
                                n12 = n10;
                            }
                            n13 = n12;
                            n14 = n10;
                            if (n5 <= n10) {
                                n13 = n12;
                                n14 = n5;
                            }
                        }
                        else {
                            float n15;
                            if (n5 >= n10) {
                                n15 = n5;
                            }
                            else {
                                n15 = n10;
                            }
                            float n16 = n10;
                            if (n5 <= n10) {
                                n16 = n5;
                            }
                            n13 = n16;
                            n14 = n15;
                        }
                        this.addBar(n13 * this.phaseY, x + n2, n14 * this.phaseY, x - n2);
                    }
                }
                else {
                    float n19;
                    if (this.mInverted) {
                        float n17;
                        if (y >= 0.0f) {
                            n17 = y;
                        }
                        else {
                            n17 = 0.0f;
                        }
                        if (y > 0.0f) {
                            y = 0.0f;
                        }
                        final float n18 = n17;
                        n19 = y;
                        y = n18;
                    }
                    else {
                        if (y >= 0.0f) {
                            n19 = y;
                        }
                        else {
                            n19 = 0.0f;
                        }
                        if (y > 0.0f) {
                            y = 0.0f;
                        }
                    }
                    if (n19 > 0.0f) {
                        n19 *= this.phaseY;
                    }
                    else {
                        y *= this.phaseY;
                    }
                    this.addBar(y, x + n2, n19, x - n2);
                }
            }
        }
        this.reset();
    }
}

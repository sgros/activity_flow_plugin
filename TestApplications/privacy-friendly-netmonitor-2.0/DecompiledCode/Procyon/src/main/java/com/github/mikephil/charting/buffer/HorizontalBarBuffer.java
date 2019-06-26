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
                    float n8;
                    float n9;
                    for (int i = 0; i < yVals.length; ++i, n5 = n9, n4 = n8) {
                        final float n6 = yVals[i];
                        float n10;
                        float n11;
                        if (n6 >= 0.0f) {
                            final float n7 = n6 + n5;
                            n8 = n4;
                            n9 = n7;
                            n10 = n5;
                            n11 = n7;
                        }
                        else {
                            final float abs = Math.abs(n6);
                            final float abs2 = Math.abs(n6);
                            n9 = n5;
                            final float n12 = n4;
                            n8 = abs2 + n4;
                            n11 = abs + n4;
                            n10 = n12;
                        }
                        float n14;
                        float n15;
                        if (this.mInverted) {
                            float n13;
                            if (n10 >= n11) {
                                n13 = n10;
                            }
                            else {
                                n13 = n11;
                            }
                            n14 = n13;
                            n15 = n11;
                            if (n10 <= n11) {
                                n14 = n13;
                                n15 = n10;
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
                            float n17 = n11;
                            if (n10 <= n11) {
                                n17 = n10;
                            }
                            n14 = n17;
                            n15 = n16;
                        }
                        this.addBar(n14 * this.phaseY, x + n2, n15 * this.phaseY, x - n2);
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
                    this.addBar(y, x + n2, n20, x - n2);
                }
            }
        }
        this.reset();
    }
}

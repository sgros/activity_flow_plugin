// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import java.util.Iterator;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class BarData extends BarLineScatterCandleBubbleData<IBarDataSet>
{
    private float mBarWidth;
    
    public BarData() {
        this.mBarWidth = 0.85f;
    }
    
    public BarData(final List<IBarDataSet> list) {
        super(list);
        this.mBarWidth = 0.85f;
    }
    
    public BarData(final IBarDataSet... array) {
        super(array);
        this.mBarWidth = 0.85f;
    }
    
    public float getBarWidth() {
        return this.mBarWidth;
    }
    
    public float getGroupWidth(final float n, final float n2) {
        return this.mDataSets.size() * (this.mBarWidth + n2) + n;
    }
    
    public void groupBars(float n, float x, float groupWidth) {
        if (this.mDataSets.size() <= 1) {
            throw new RuntimeException("BarData needs to hold at least 2 BarDataSets to allow grouping.");
        }
        final int entryCount = this.getMaxEntryCountSet().getEntryCount();
        final float n2 = x / 2.0f;
        final float n3 = groupWidth / 2.0f;
        final float n4 = this.mBarWidth / 2.0f;
        groupWidth = this.getGroupWidth(x, groupWidth);
        int i = 0;
    Label_0197_Outer:
        while (i < entryCount) {
            x = n + n2;
            for (final IBarDataSet set : this.mDataSets) {
                x = x + n3 + n4;
                if (i < set.getEntryCount()) {
                    final BarEntry barEntry = set.getEntryForIndex(i);
                    if (barEntry != null) {
                        barEntry.setX(x);
                    }
                }
                x = x + n4 + n3;
            }
            x += n2;
            final float n5 = groupWidth - (x - n);
            while (true) {
                Label_0200: {
                    if (n5 > 0.0f) {
                        break Label_0200;
                    }
                    n = x;
                    if (n5 < 0.0f) {
                        break Label_0200;
                    }
                    ++i;
                    continue Label_0197_Outer;
                }
                n = x + n5;
                continue;
            }
        }
        this.notifyDataChanged();
    }
    
    public void setBarWidth(final float mBarWidth) {
        this.mBarWidth = mBarWidth;
    }
}

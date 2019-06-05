// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data;

import android.graphics.Color;
import java.util.List;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

public abstract class BarLineScatterCandleBubbleDataSet<T extends Entry> extends DataSet<T> implements IBarLineScatterCandleBubbleDataSet<T>
{
    protected int mHighLightColor;
    
    public BarLineScatterCandleBubbleDataSet(final List<T> list, final String s) {
        super(list, s);
        this.mHighLightColor = Color.rgb(255, 187, 115);
    }
    
    @Override
    public int getHighLightColor() {
        return this.mHighLightColor;
    }
    
    public void setHighLightColor(final int mHighLightColor) {
        this.mHighLightColor = mHighLightColor;
    }
}

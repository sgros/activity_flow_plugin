// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.BubbleEntry;

public interface IBubbleDataSet extends IBarLineScatterCandleBubbleDataSet<BubbleEntry>
{
    float getHighlightCircleWidth();
    
    float getMaxSize();
    
    boolean isNormalizeSizeEnabled();
    
    void setHighlightCircleWidth(final float p0);
}

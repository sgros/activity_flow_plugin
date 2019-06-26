// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.BarEntry;

public interface IBarDataSet extends IBarLineScatterCandleBubbleDataSet<BarEntry>
{
    int getBarBorderColor();
    
    float getBarBorderWidth();
    
    int getBarShadowColor();
    
    int getHighLightAlpha();
    
    String[] getStackLabels();
    
    int getStackSize();
    
    boolean isStacked();
}

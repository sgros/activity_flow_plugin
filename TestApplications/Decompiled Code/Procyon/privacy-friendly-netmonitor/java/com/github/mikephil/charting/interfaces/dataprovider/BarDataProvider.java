// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider
{
    BarData getBarData();
    
    boolean isDrawBarShadowEnabled();
    
    boolean isDrawValueAboveBarEnabled();
    
    boolean isHighlightFullBarEnabled();
}

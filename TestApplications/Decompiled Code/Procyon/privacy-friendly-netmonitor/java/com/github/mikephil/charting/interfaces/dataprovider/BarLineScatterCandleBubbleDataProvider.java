// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface
{
    BarLineScatterCandleBubbleData getData();
    
    float getHighestVisibleX();
    
    float getLowestVisibleX();
    
    Transformer getTransformer(final YAxis.AxisDependency p0);
    
    boolean isInverted(final YAxis.AxisDependency p0);
}

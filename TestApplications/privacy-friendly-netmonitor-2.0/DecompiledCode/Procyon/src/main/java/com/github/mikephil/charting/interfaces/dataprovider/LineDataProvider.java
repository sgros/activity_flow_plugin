// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.components.YAxis;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider
{
    YAxis getAxis(final YAxis.AxisDependency p0);
    
    LineData getLineData();
}

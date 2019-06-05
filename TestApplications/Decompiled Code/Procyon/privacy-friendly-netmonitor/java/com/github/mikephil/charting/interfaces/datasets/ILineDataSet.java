// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import android.graphics.DashPathEffect;
import com.github.mikephil.charting.data.Entry;

public interface ILineDataSet extends ILineRadarDataSet<Entry>
{
    int getCircleColor(final int p0);
    
    int getCircleColorCount();
    
    int getCircleHoleColor();
    
    float getCircleHoleRadius();
    
    float getCircleRadius();
    
    float getCubicIntensity();
    
    DashPathEffect getDashPathEffect();
    
    IFillFormatter getFillFormatter();
    
    LineDataSet.Mode getMode();
    
    boolean isDashedLineEnabled();
    
    boolean isDrawCircleHoleEnabled();
    
    boolean isDrawCirclesEnabled();
    
    @Deprecated
    boolean isDrawCubicEnabled();
    
    @Deprecated
    boolean isDrawSteppedEnabled();
}

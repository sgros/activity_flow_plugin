// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.data.Entry;

public interface ILineRadarDataSet<T extends Entry> extends ILineScatterCandleRadarDataSet<T>
{
    int getFillAlpha();
    
    int getFillColor();
    
    Drawable getFillDrawable();
    
    float getLineWidth();
    
    boolean isDrawFilledEnabled();
    
    void setDrawFilled(final boolean p0);
}

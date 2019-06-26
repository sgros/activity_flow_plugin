// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.Paint$Style;
import com.github.mikephil.charting.data.CandleEntry;

public interface ICandleDataSet extends ILineScatterCandleRadarDataSet<CandleEntry>
{
    float getBarSpace();
    
    int getDecreasingColor();
    
    Paint$Style getDecreasingPaintStyle();
    
    int getIncreasingColor();
    
    Paint$Style getIncreasingPaintStyle();
    
    int getNeutralColor();
    
    int getShadowColor();
    
    boolean getShadowColorSameAsCandle();
    
    float getShadowWidth();
    
    boolean getShowCandleBar();
}

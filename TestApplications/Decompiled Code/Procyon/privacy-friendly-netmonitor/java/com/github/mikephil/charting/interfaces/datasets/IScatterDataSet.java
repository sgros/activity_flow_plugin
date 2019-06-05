// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;
import com.github.mikephil.charting.data.Entry;

public interface IScatterDataSet extends ILineScatterCandleRadarDataSet<Entry>
{
    int getScatterShapeHoleColor();
    
    float getScatterShapeHoleRadius();
    
    float getScatterShapeSize();
    
    IShapeRenderer getShapeRenderer();
}

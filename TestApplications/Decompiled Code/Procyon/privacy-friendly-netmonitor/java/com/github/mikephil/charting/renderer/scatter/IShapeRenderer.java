// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Paint;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import android.graphics.Canvas;

public interface IShapeRenderer
{
    void renderShape(final Canvas p0, final IScatterDataSet p1, final ViewPortHandler p2, final float p3, final float p4, final Paint p5);
}

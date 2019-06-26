// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.components;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.MPPointF;
import android.graphics.Canvas;

public interface IMarker
{
    void draw(final Canvas p0, final float p1, final float p2);
    
    MPPointF getOffset();
    
    MPPointF getOffsetForDrawingAtPoint(final float p0, final float p1);
    
    void refreshContent(final Entry p0, final Highlight p1);
}

package com.github.mikephil.charting.components;

import android.graphics.Canvas;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public interface IMarker {
   void draw(Canvas var1, float var2, float var3);

   MPPointF getOffset();

   MPPointF getOffsetForDrawingAtPoint(float var1, float var2);

   void refreshContent(Entry var1, Highlight var2);
}

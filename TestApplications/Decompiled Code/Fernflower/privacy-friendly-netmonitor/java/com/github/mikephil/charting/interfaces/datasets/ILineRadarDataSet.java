package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.drawable.Drawable;

public interface ILineRadarDataSet extends ILineScatterCandleRadarDataSet {
   int getFillAlpha();

   int getFillColor();

   Drawable getFillDrawable();

   float getLineWidth();

   boolean isDrawFilledEnabled();

   void setDrawFilled(boolean var1);
}

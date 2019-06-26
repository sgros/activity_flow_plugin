package com.github.mikephil.charting.interfaces.datasets;

public interface IBubbleDataSet extends IBarLineScatterCandleBubbleDataSet {
   float getHighlightCircleWidth();

   float getMaxSize();

   boolean isNormalizeSizeEnabled();

   void setHighlightCircleWidth(float var1);
}

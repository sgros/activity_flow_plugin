package com.github.mikephil.charting.interfaces.datasets;

public interface IRadarDataSet extends ILineRadarDataSet {
   int getHighlightCircleFillColor();

   float getHighlightCircleInnerRadius();

   float getHighlightCircleOuterRadius();

   int getHighlightCircleStrokeAlpha();

   int getHighlightCircleStrokeColor();

   float getHighlightCircleStrokeWidth();

   boolean isDrawHighlightCircleEnabled();

   void setDrawHighlightCircleEnabled(boolean var1);
}

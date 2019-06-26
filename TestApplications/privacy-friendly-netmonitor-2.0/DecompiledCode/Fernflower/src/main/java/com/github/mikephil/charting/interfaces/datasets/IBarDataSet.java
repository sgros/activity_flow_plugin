package com.github.mikephil.charting.interfaces.datasets;

public interface IBarDataSet extends IBarLineScatterCandleBubbleDataSet {
   int getBarBorderColor();

   float getBarBorderWidth();

   int getBarShadowColor();

   int getHighLightAlpha();

   String[] getStackLabels();

   int getStackSize();

   boolean isStacked();
}

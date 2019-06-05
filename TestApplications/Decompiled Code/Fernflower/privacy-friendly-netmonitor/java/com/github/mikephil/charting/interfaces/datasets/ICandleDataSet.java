package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.Paint.Style;

public interface ICandleDataSet extends ILineScatterCandleRadarDataSet {
   float getBarSpace();

   int getDecreasingColor();

   Style getDecreasingPaintStyle();

   int getIncreasingColor();

   Style getIncreasingPaintStyle();

   int getNeutralColor();

   int getShadowColor();

   boolean getShadowColorSameAsCandle();

   float getShadowWidth();

   boolean getShowCandleBar();
}

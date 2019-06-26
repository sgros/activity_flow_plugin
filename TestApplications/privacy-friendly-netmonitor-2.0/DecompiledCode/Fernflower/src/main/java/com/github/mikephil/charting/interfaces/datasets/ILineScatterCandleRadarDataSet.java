package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.DashPathEffect;

public interface ILineScatterCandleRadarDataSet extends IBarLineScatterCandleBubbleDataSet {
   DashPathEffect getDashPathEffectHighlight();

   float getHighlightLineWidth();

   boolean isHorizontalHighlightIndicatorEnabled();

   boolean isVerticalHighlightIndicatorEnabled();
}

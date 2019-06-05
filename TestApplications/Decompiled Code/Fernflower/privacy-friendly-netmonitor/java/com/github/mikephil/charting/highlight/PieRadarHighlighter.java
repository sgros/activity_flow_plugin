package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.PieRadarChartBase;
import java.util.ArrayList;
import java.util.List;

public abstract class PieRadarHighlighter implements IHighlighter {
   protected PieRadarChartBase mChart;
   protected List mHighlightBuffer = new ArrayList();

   public PieRadarHighlighter(PieRadarChartBase var1) {
      this.mChart = var1;
   }

   protected abstract Highlight getClosestHighlight(int var1, float var2, float var3);

   public Highlight getHighlight(float var1, float var2) {
      if (this.mChart.distanceToCenter(var1, var2) > this.mChart.getRadius()) {
         return null;
      } else {
         float var3 = this.mChart.getAngleForPoint(var1, var2);
         float var4 = var3;
         if (this.mChart instanceof PieChart) {
            var4 = var3 / this.mChart.getAnimator().getPhaseY();
         }

         int var5 = this.mChart.getIndexForAngle(var4);
         return var5 >= 0 && var5 < this.mChart.getData().getMaxEntryCountSet().getEntryCount() ? this.getClosestHighlight(var5, var1, var2) : null;
      }
   }
}

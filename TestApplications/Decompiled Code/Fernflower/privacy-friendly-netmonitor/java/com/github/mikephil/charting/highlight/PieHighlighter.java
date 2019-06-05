package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

public class PieHighlighter extends PieRadarHighlighter {
   public PieHighlighter(PieChart var1) {
      super(var1);
   }

   protected Highlight getClosestHighlight(int var1, float var2, float var3) {
      IPieDataSet var4 = ((PieData)((PieChart)this.mChart).getData()).getDataSet();
      Entry var5 = var4.getEntryForIndex(var1);
      return new Highlight((float)var1, var5.getY(), var2, var3, 0, var4.getAxisDependency());
   }
}

package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import java.util.List;

public class RadarHighlighter extends PieRadarHighlighter {
   public RadarHighlighter(RadarChart var1) {
      super(var1);
   }

   protected Highlight getClosestHighlight(int var1, float var2, float var3) {
      List var4 = this.getHighlightsAtIndex(var1);
      float var5 = ((RadarChart)this.mChart).distanceToCenter(var2, var3) / ((RadarChart)this.mChart).getFactor();
      Highlight var6 = null;
      var3 = Float.MAX_VALUE;

      for(var1 = 0; var1 < var4.size(); var3 = var2) {
         Highlight var7 = (Highlight)var4.get(var1);
         float var8 = Math.abs(var7.getY() - var5);
         var2 = var3;
         if (var8 < var3) {
            var6 = var7;
            var2 = var8;
         }

         ++var1;
      }

      return var6;
   }

   protected List getHighlightsAtIndex(int var1) {
      this.mHighlightBuffer.clear();
      float var2 = ((RadarChart)this.mChart).getAnimator().getPhaseX();
      float var3 = ((RadarChart)this.mChart).getAnimator().getPhaseY();
      float var4 = ((RadarChart)this.mChart).getSliceAngle();
      float var5 = ((RadarChart)this.mChart).getFactor();
      MPPointF var6 = MPPointF.getInstance(0.0F, 0.0F);

      for(int var7 = 0; var7 < ((RadarData)((RadarChart)this.mChart).getData()).getDataSetCount(); ++var7) {
         IDataSet var9 = ((RadarData)((RadarChart)this.mChart).getData()).getDataSetByIndex(var7);
         Entry var10 = var9.getEntryForIndex(var1);
         float var11 = var10.getY();
         float var12 = ((RadarChart)this.mChart).getYChartMin();
         MPPointF var13 = ((RadarChart)this.mChart).getCenterOffsets();
         float var14 = (float)var1;
         Utils.getPosition(var13, (var11 - var12) * var5 * var3, var4 * var14 * var2 + ((RadarChart)this.mChart).getRotationAngle(), var6);
         this.mHighlightBuffer.add(new Highlight(var14, var10.getY(), var6.x, var6.y, var7, var9.getAxisDependency()));
      }

      return this.mHighlightBuffer;
   }
}

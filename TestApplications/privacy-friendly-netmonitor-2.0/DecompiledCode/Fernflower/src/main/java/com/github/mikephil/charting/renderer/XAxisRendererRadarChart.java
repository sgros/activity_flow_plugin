package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class XAxisRendererRadarChart extends XAxisRenderer {
   private RadarChart mChart;

   public XAxisRendererRadarChart(ViewPortHandler var1, XAxis var2, RadarChart var3) {
      super(var1, var2, (Transformer)null);
      this.mChart = var3;
   }

   public void renderAxisLabels(Canvas var1) {
      if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
         float var2 = this.mXAxis.getLabelRotationAngle();
         MPPointF var3 = MPPointF.getInstance(0.5F, 0.25F);
         this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
         this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
         this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());
         float var4 = this.mChart.getSliceAngle();
         float var5 = this.mChart.getFactor();
         MPPointF var6 = this.mChart.getCenterOffsets();
         MPPointF var7 = MPPointF.getInstance(0.0F, 0.0F);

         for(int var8 = 0; var8 < ((IRadarDataSet)((RadarData)this.mChart.getData()).getMaxEntryCountSet()).getEntryCount(); ++var8) {
            IAxisValueFormatter var9 = this.mXAxis.getValueFormatter();
            float var10 = (float)var8;
            String var12 = var9.getFormattedValue(var10, this.mXAxis);
            float var11 = this.mChart.getRotationAngle();
            Utils.getPosition(var6, this.mChart.getYRange() * var5 + (float)this.mXAxis.mLabelRotatedWidth / 2.0F, (var10 * var4 + var11) % 360.0F, var7);
            this.drawLabel(var1, var12, var7.x, var7.y - (float)this.mXAxis.mLabelRotatedHeight / 2.0F, var3, var2);
         }

         MPPointF.recycleInstance(var6);
         MPPointF.recycleInstance(var7);
         MPPointF.recycleInstance(var3);
      }
   }

   public void renderLimitLines(Canvas var1) {
   }
}

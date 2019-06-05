package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Path;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class YAxisRendererRadarChart extends YAxisRenderer {
   private RadarChart mChart;
   private Path mRenderLimitLinesPathBuffer = new Path();

   public YAxisRendererRadarChart(ViewPortHandler var1, YAxis var2, RadarChart var3) {
      super(var1, var2, (Transformer)null);
      this.mChart = var3;
   }

   protected void computeAxisValues(float var1, float var2) {
      int var3 = this.mAxis.getLabelCount();
      double var4 = (double)Math.abs(var2 - var1);
      if (var3 != 0 && var4 > 0.0D && !Double.isInfinite(var4)) {
         double var6 = (double)Utils.roundToNextSignificant(var4 / (double)var3);
         double var8 = var6;
         if (this.mAxis.isGranularityEnabled()) {
            var8 = var6;
            if (var6 < (double)this.mAxis.getGranularity()) {
               var8 = (double)this.mAxis.getGranularity();
            }
         }

         var6 = (double)Utils.roundToNextSignificant(Math.pow(10.0D, (double)((int)Math.log10(var8))));
         double var10 = var8;
         if ((int)(var8 / var6) > 5) {
            var10 = Math.floor(10.0D * var6);
         }

         byte var12 = this.mAxis.isCenterAxisLabelsEnabled();
         int var13;
         if (this.mAxis.isForceLabelsEnabled()) {
            var2 = (float)var4 / (float)(var3 - 1);
            this.mAxis.mEntryCount = var3;
            if (this.mAxis.mEntries.length < var3) {
               this.mAxis.mEntries = new float[var3];
            }

            for(var13 = 0; var13 < var3; ++var13) {
               this.mAxis.mEntries[var13] = var1;
               var1 += var2;
            }

            var13 = var3;
         } else {
            if (var10 == 0.0D) {
               var6 = 0.0D;
            } else {
               var6 = Math.ceil((double)var1 / var10) * var10;
            }

            var8 = var6;
            if (var12 != 0) {
               var8 = var6 - var10;
            }

            if (var10 == 0.0D) {
               var6 = 0.0D;
            } else {
               var6 = Utils.nextUp(Math.floor((double)var2 / var10) * var10);
            }

            if (var10 != 0.0D) {
               var4 = var8;
               var13 = var12;

               while(true) {
                  var3 = var13;
                  if (var4 > var6) {
                     break;
                  }

                  ++var13;
                  var4 += var10;
               }
            } else {
               var3 = var12;
            }

            int var14 = var3 + 1;
            this.mAxis.mEntryCount = var14;
            if (this.mAxis.mEntries.length < var14) {
               this.mAxis.mEntries = new float[var14];
            }

            var3 = 0;

            while(true) {
               var13 = var14;
               if (var3 >= var14) {
                  break;
               }

               var6 = var8;
               if (var8 == 0.0D) {
                  var6 = 0.0D;
               }

               this.mAxis.mEntries[var3] = (float)var6;
               var8 = var6 + var10;
               ++var3;
            }
         }

         if (var10 < 1.0D) {
            this.mAxis.mDecimals = (int)Math.ceil(-Math.log10(var10));
         } else {
            this.mAxis.mDecimals = 0;
         }

         if (var12 != 0) {
            if (this.mAxis.mCenteredEntries.length < var13) {
               this.mAxis.mCenteredEntries = new float[var13];
            }

            var1 = (this.mAxis.mEntries[1] - this.mAxis.mEntries[0]) / 2.0F;

            for(int var15 = 0; var15 < var13; ++var15) {
               this.mAxis.mCenteredEntries[var15] = this.mAxis.mEntries[var15] + var1;
            }
         }

         this.mAxis.mAxisMinimum = this.mAxis.mEntries[0];
         this.mAxis.mAxisMaximum = this.mAxis.mEntries[var13 - 1];
         this.mAxis.mAxisRange = Math.abs(this.mAxis.mAxisMaximum - this.mAxis.mAxisMinimum);
      } else {
         this.mAxis.mEntries = new float[0];
         this.mAxis.mCenteredEntries = new float[0];
         this.mAxis.mEntryCount = 0;
      }
   }

   public void renderAxisLabels(Canvas var1) {
      if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
         this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
         this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
         this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
         MPPointF var2 = this.mChart.getCenterOffsets();
         MPPointF var3 = MPPointF.getInstance(0.0F, 0.0F);
         float var4 = this.mChart.getFactor();
         int var5 = this.mYAxis.isDrawBottomYLabelEntryEnabled() ^ 1;
         int var6;
         if (this.mYAxis.isDrawTopYLabelEntryEnabled()) {
            var6 = this.mYAxis.mEntryCount;
         } else {
            var6 = this.mYAxis.mEntryCount - 1;
         }

         while(var5 < var6) {
            Utils.getPosition(var2, (this.mYAxis.mEntries[var5] - this.mYAxis.mAxisMinimum) * var4, this.mChart.getRotationAngle(), var3);
            var1.drawText(this.mYAxis.getFormattedLabel(var5), var3.x + 10.0F, var3.y, this.mAxisLabelPaint);
            ++var5;
         }

         MPPointF.recycleInstance(var2);
         MPPointF.recycleInstance(var3);
      }
   }

   public void renderLimitLines(Canvas var1) {
      List var2 = this.mYAxis.getLimitLines();
      if (var2 != null) {
         float var3 = this.mChart.getSliceAngle();
         float var4 = this.mChart.getFactor();
         MPPointF var5 = this.mChart.getCenterOffsets();
         MPPointF var6 = MPPointF.getInstance(0.0F, 0.0F);

         for(int var7 = 0; var7 < var2.size(); ++var7) {
            LimitLine var8 = (LimitLine)var2.get(var7);
            if (var8.isEnabled()) {
               this.mLimitLinePaint.setColor(var8.getLineColor());
               this.mLimitLinePaint.setPathEffect(var8.getDashPathEffect());
               this.mLimitLinePaint.setStrokeWidth(var8.getLineWidth());
               float var9 = var8.getLimit();
               float var10 = this.mChart.getYChartMin();
               Path var12 = this.mRenderLimitLinesPathBuffer;
               var12.reset();

               for(int var11 = 0; var11 < ((IRadarDataSet)((RadarData)this.mChart.getData()).getMaxEntryCountSet()).getEntryCount(); ++var11) {
                  Utils.getPosition(var5, (var9 - var10) * var4, (float)var11 * var3 + this.mChart.getRotationAngle(), var6);
                  if (var11 == 0) {
                     var12.moveTo(var6.x, var6.y);
                  } else {
                     var12.lineTo(var6.x, var6.y);
                  }
               }

               var12.close();
               var1.drawPath(var12, this.mLimitLinePaint);
            }
         }

         MPPointF.recycleInstance(var5);
         MPPointF.recycleInstance(var6);
      }
   }
}

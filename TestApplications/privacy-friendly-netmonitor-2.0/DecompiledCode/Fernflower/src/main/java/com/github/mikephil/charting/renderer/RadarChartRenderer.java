package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.Iterator;

public class RadarChartRenderer extends LineRadarRenderer {
   protected RadarChart mChart;
   protected Path mDrawDataSetSurfacePathBuffer = new Path();
   protected Path mDrawHighlightCirclePathBuffer = new Path();
   protected Paint mHighlightCirclePaint;
   protected Paint mWebPaint;

   public RadarChartRenderer(RadarChart var1, ChartAnimator var2, ViewPortHandler var3) {
      super(var2, var3);
      this.mChart = var1;
      this.mHighlightPaint = new Paint(1);
      this.mHighlightPaint.setStyle(Style.STROKE);
      this.mHighlightPaint.setStrokeWidth(2.0F);
      this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
      this.mWebPaint = new Paint(1);
      this.mWebPaint.setStyle(Style.STROKE);
      this.mHighlightCirclePaint = new Paint(1);
   }

   public void drawData(Canvas var1) {
      RadarData var2 = (RadarData)this.mChart.getData();
      int var3 = ((IRadarDataSet)var2.getMaxEntryCountSet()).getEntryCount();
      Iterator var5 = var2.getDataSets().iterator();

      while(var5.hasNext()) {
         IRadarDataSet var4 = (IRadarDataSet)var5.next();
         if (var4.isVisible()) {
            this.drawDataSet(var1, var4, var3);
         }
      }

   }

   protected void drawDataSet(Canvas var1, IRadarDataSet var2, int var3) {
      float var4 = this.mAnimator.getPhaseX();
      float var5 = this.mAnimator.getPhaseY();
      float var6 = this.mChart.getSliceAngle();
      float var7 = this.mChart.getFactor();
      MPPointF var8 = this.mChart.getCenterOffsets();
      MPPointF var9 = MPPointF.getInstance(0.0F, 0.0F);
      Path var10 = this.mDrawDataSetSurfacePathBuffer;
      var10.reset();
      int var11 = 0;

      for(boolean var12 = false; var11 < var2.getEntryCount(); ++var11) {
         this.mRenderPaint.setColor(var2.getColor(var11));
         Utils.getPosition(var8, (((RadarEntry)var2.getEntryForIndex(var11)).getY() - this.mChart.getYChartMin()) * var7 * var5, (float)var11 * var6 * var4 + this.mChart.getRotationAngle(), var9);
         if (!Float.isNaN(var9.x)) {
            if (!var12) {
               var10.moveTo(var9.x, var9.y);
               var12 = true;
            } else {
               var10.lineTo(var9.x, var9.y);
            }
         }
      }

      if (var2.getEntryCount() > var3) {
         var10.lineTo(var8.x, var8.y);
      }

      var10.close();
      if (var2.isDrawFilledEnabled()) {
         Drawable var13 = var2.getFillDrawable();
         if (var13 != null) {
            this.drawFilledPath(var1, var10, var13);
         } else {
            this.drawFilledPath(var1, var10, var2.getFillColor(), var2.getFillAlpha());
         }
      }

      this.mRenderPaint.setStrokeWidth(var2.getLineWidth());
      this.mRenderPaint.setStyle(Style.STROKE);
      if (!var2.isDrawFilledEnabled() || var2.getFillAlpha() < 255) {
         var1.drawPath(var10, this.mRenderPaint);
      }

      MPPointF.recycleInstance(var8);
      MPPointF.recycleInstance(var9);
   }

   public void drawExtras(Canvas var1) {
      this.drawWeb(var1);
   }

   public void drawHighlightCircle(Canvas var1, MPPointF var2, float var3, float var4, int var5, int var6, float var7) {
      var1.save();
      var4 = Utils.convertDpToPixel(var4);
      var3 = Utils.convertDpToPixel(var3);
      if (var5 != 1122867) {
         Path var8 = this.mDrawHighlightCirclePathBuffer;
         var8.reset();
         var8.addCircle(var2.x, var2.y, var4, Direction.CW);
         if (var3 > 0.0F) {
            var8.addCircle(var2.x, var2.y, var3, Direction.CCW);
         }

         this.mHighlightCirclePaint.setColor(var5);
         this.mHighlightCirclePaint.setStyle(Style.FILL);
         var1.drawPath(var8, this.mHighlightCirclePaint);
      }

      if (var6 != 1122867) {
         this.mHighlightCirclePaint.setColor(var6);
         this.mHighlightCirclePaint.setStyle(Style.STROKE);
         this.mHighlightCirclePaint.setStrokeWidth(Utils.convertDpToPixel(var7));
         var1.drawCircle(var2.x, var2.y, var4, this.mHighlightCirclePaint);
      }

      var1.restore();
   }

   public void drawHighlighted(Canvas var1, Highlight[] var2) {
      float var3 = this.mChart.getSliceAngle();
      float var4 = this.mChart.getFactor();
      MPPointF var5 = this.mChart.getCenterOffsets();
      MPPointF var6 = MPPointF.getInstance(0.0F, 0.0F);
      RadarData var7 = (RadarData)this.mChart.getData();
      int var8 = var2.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Highlight var10 = var2[var9];
         IRadarDataSet var11 = (IRadarDataSet)var7.getDataSetByIndex(var10.getDataSetIndex());
         if (var11 != null && var11.isHighlightEnabled()) {
            RadarEntry var12 = (RadarEntry)var11.getEntryForIndex((int)var10.getX());
            if (this.isInBoundsX(var12, var11)) {
               Utils.getPosition(var5, (var12.getY() - this.mChart.getYChartMin()) * var4 * this.mAnimator.getPhaseY(), var10.getX() * var3 * this.mAnimator.getPhaseX() + this.mChart.getRotationAngle(), var6);
               var10.setDraw(var6.x, var6.y);
               this.drawHighlightLines(var1, var6.x, var6.y, var11);
               if (var11.isDrawHighlightCircleEnabled() && !Float.isNaN(var6.x) && !Float.isNaN(var6.y)) {
                  int var13 = var11.getHighlightCircleStrokeColor();
                  int var14 = var13;
                  if (var13 == 1122867) {
                     var14 = var11.getColor(0);
                  }

                  var13 = var14;
                  if (var11.getHighlightCircleStrokeAlpha() < 255) {
                     var13 = ColorTemplate.colorWithAlpha(var14, var11.getHighlightCircleStrokeAlpha());
                  }

                  this.drawHighlightCircle(var1, var6, var11.getHighlightCircleInnerRadius(), var11.getHighlightCircleOuterRadius(), var11.getHighlightCircleFillColor(), var13, var11.getHighlightCircleStrokeWidth());
               }
            }
         }
      }

      MPPointF.recycleInstance(var5);
      MPPointF.recycleInstance(var6);
   }

   public void drawValues(Canvas var1) {
      float var2 = this.mAnimator.getPhaseX();
      float var3 = this.mAnimator.getPhaseY();
      float var4 = this.mChart.getSliceAngle();
      float var5 = this.mChart.getFactor();
      MPPointF var6 = this.mChart.getCenterOffsets();
      MPPointF var7 = MPPointF.getInstance(0.0F, 0.0F);
      MPPointF var8 = MPPointF.getInstance(0.0F, 0.0F);
      float var9 = Utils.convertDpToPixel(5.0F);

      float var12;
      for(int var10 = 0; var10 < ((RadarData)this.mChart.getData()).getDataSetCount(); var4 = var12) {
         IRadarDataSet var11 = (IRadarDataSet)((RadarData)this.mChart.getData()).getDataSetByIndex(var10);
         MPPointF var13;
         if (!this.shouldDrawValues(var11)) {
            var12 = var4;
            var13 = var7;
            var7 = var8;
            var4 = var2;
            var2 = var12;
            var8 = var13;
         } else {
            this.applyValueTextStyle(var11);
            var13 = MPPointF.getInstance(var11.getIconsOffset());
            var13.x = Utils.convertDpToPixel(var13.x);
            var13.y = Utils.convertDpToPixel(var13.y);

            for(int var14 = 0; var14 < var11.getEntryCount(); ++var14) {
               RadarEntry var15 = (RadarEntry)var11.getEntryForIndex(var14);
               float var16 = var15.getY();
               var12 = this.mChart.getYChartMin();
               float var17 = (float)var14 * var4 * var2;
               Utils.getPosition(var6, (var16 - var12) * var5 * var3, var17 + this.mChart.getRotationAngle(), var7);
               if (var11.isDrawValuesEnabled()) {
                  this.drawValue(var1, var11.getValueFormatter(), var15.getY(), var15, var10, var7.x, var7.y - var9, var11.getValueTextColor(var14));
               }

               if (var15.getIcon() != null && var11.isDrawIconsEnabled()) {
                  Drawable var18 = var15.getIcon();
                  Utils.getPosition(var6, var15.getY() * var5 * var3 + var13.y, var17 + this.mChart.getRotationAngle(), var8);
                  var8.y += var13.x;
                  Utils.drawImage(var1, var18, (int)var8.x, (int)var8.y, var18.getIntrinsicWidth(), var18.getIntrinsicHeight());
               }
            }

            var12 = var2;
            var2 = var4;
            MPPointF var19 = var7;
            var7 = var8;
            MPPointF.recycleInstance(var13);
            var8 = var19;
            var4 = var12;
         }

         ++var10;
         var13 = var7;
         var12 = var2;
         var7 = var8;
         var8 = var13;
         var2 = var4;
      }

      MPPointF.recycleInstance(var6);
      MPPointF.recycleInstance(var7);
      MPPointF.recycleInstance(var8);
   }

   protected void drawWeb(Canvas var1) {
      float var2 = this.mChart.getSliceAngle();
      float var3 = this.mChart.getFactor();
      float var4 = this.mChart.getRotationAngle();
      MPPointF var5 = this.mChart.getCenterOffsets();
      this.mWebPaint.setStrokeWidth(this.mChart.getWebLineWidth());
      this.mWebPaint.setColor(this.mChart.getWebColor());
      this.mWebPaint.setAlpha(this.mChart.getWebAlpha());
      int var6 = this.mChart.getSkipWebLineCount();
      int var7 = ((IRadarDataSet)((RadarData)this.mChart.getData()).getMaxEntryCountSet()).getEntryCount();
      MPPointF var8 = MPPointF.getInstance(0.0F, 0.0F);

      int var9;
      for(var9 = 0; var9 < var7; var9 += 1 + var6) {
         Utils.getPosition(var5, this.mChart.getYRange() * var3, (float)var9 * var2 + var4, var8);
         var1.drawLine(var5.x, var5.y, var8.x, var8.y, this.mWebPaint);
      }

      MPPointF.recycleInstance(var8);
      this.mWebPaint.setStrokeWidth(this.mChart.getWebLineWidthInner());
      this.mWebPaint.setColor(this.mChart.getWebColorInner());
      this.mWebPaint.setAlpha(this.mChart.getWebAlpha());
      var7 = this.mChart.getYAxis().mEntryCount;
      MPPointF var10 = MPPointF.getInstance(0.0F, 0.0F);
      var8 = MPPointF.getInstance(0.0F, 0.0F);

      for(var9 = 0; var9 < var7; ++var9) {
         var6 = 0;

         while(var6 < ((RadarData)this.mChart.getData()).getEntryCount()) {
            float var11 = (this.mChart.getYAxis().mEntries[var9] - this.mChart.getYChartMin()) * var3;
            Utils.getPosition(var5, var11, (float)var6 * var2 + var4, var10);
            ++var6;
            Utils.getPosition(var5, var11, (float)var6 * var2 + var4, var8);
            var1.drawLine(var10.x, var10.y, var8.x, var8.y, this.mWebPaint);
         }
      }

      MPPointF.recycleInstance(var10);
      MPPointF.recycleInstance(var8);
   }

   public Paint getWebPaint() {
      return this.mWebPaint;
   }

   public void initBuffers() {
   }
}

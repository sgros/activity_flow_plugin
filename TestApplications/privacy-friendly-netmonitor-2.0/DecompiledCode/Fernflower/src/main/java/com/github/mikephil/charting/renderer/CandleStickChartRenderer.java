package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.Iterator;
import java.util.List;

public class CandleStickChartRenderer extends LineScatterCandleRadarRenderer {
   private float[] mBodyBuffers = new float[4];
   protected CandleDataProvider mChart;
   private float[] mCloseBuffers = new float[4];
   private float[] mOpenBuffers = new float[4];
   private float[] mRangeBuffers = new float[4];
   private float[] mShadowBuffers = new float[8];

   public CandleStickChartRenderer(CandleDataProvider var1, ChartAnimator var2, ViewPortHandler var3) {
      super(var2, var3);
      this.mChart = var1;
   }

   public void drawData(Canvas var1) {
      Iterator var2 = this.mChart.getCandleData().getDataSets().iterator();

      while(var2.hasNext()) {
         ICandleDataSet var3 = (ICandleDataSet)var2.next();
         if (var3.isVisible()) {
            this.drawDataSet(var1, var3);
         }
      }

   }

   protected void drawDataSet(Canvas var1, ICandleDataSet var2) {
      Transformer var3 = this.mChart.getTransformer(var2.getAxisDependency());
      float var4 = this.mAnimator.getPhaseY();
      float var5 = var2.getBarSpace();
      boolean var6 = var2.getShowCandleBar();
      this.mXBounds.set(this.mChart, var2);
      this.mRenderPaint.setStrokeWidth(var2.getShadowWidth());

      for(int var7 = this.mXBounds.min; var7 <= this.mXBounds.range + this.mXBounds.min; ++var7) {
         CandleEntry var8 = (CandleEntry)var2.getEntryForIndex(var7);
         if (var8 != null) {
            float var9 = var8.getX();
            float var10 = var8.getOpen();
            float var11 = var8.getClose();
            float var12 = var8.getHigh();
            float var13 = var8.getLow();
            int var14;
            if (var6) {
               this.mShadowBuffers[0] = var9;
               this.mShadowBuffers[2] = var9;
               this.mShadowBuffers[4] = var9;
               this.mShadowBuffers[6] = var9;
               if (var10 > var11) {
                  this.mShadowBuffers[1] = var12 * var4;
                  this.mShadowBuffers[3] = var10 * var4;
                  this.mShadowBuffers[5] = var13 * var4;
                  this.mShadowBuffers[7] = var11 * var4;
               } else if (var10 < var11) {
                  this.mShadowBuffers[1] = var12 * var4;
                  this.mShadowBuffers[3] = var11 * var4;
                  this.mShadowBuffers[5] = var13 * var4;
                  this.mShadowBuffers[7] = var10 * var4;
               } else {
                  this.mShadowBuffers[1] = var12 * var4;
                  this.mShadowBuffers[3] = var10 * var4;
                  this.mShadowBuffers[5] = var13 * var4;
                  this.mShadowBuffers[7] = this.mShadowBuffers[3];
               }

               var3.pointValuesToPixel(this.mShadowBuffers);
               Paint var15;
               if (var2.getShadowColorSameAsCandle()) {
                  if (var10 > var11) {
                     var15 = this.mRenderPaint;
                     if (var2.getDecreasingColor() == 1122867) {
                        var14 = var2.getColor(var7);
                     } else {
                        var14 = var2.getDecreasingColor();
                     }

                     var15.setColor(var14);
                  } else if (var10 < var11) {
                     var15 = this.mRenderPaint;
                     if (var2.getIncreasingColor() == 1122867) {
                        var14 = var2.getColor(var7);
                     } else {
                        var14 = var2.getIncreasingColor();
                     }

                     var15.setColor(var14);
                  } else {
                     var15 = this.mRenderPaint;
                     if (var2.getNeutralColor() == 1122867) {
                        var14 = var2.getColor(var7);
                     } else {
                        var14 = var2.getNeutralColor();
                     }

                     var15.setColor(var14);
                  }
               } else {
                  var15 = this.mRenderPaint;
                  if (var2.getShadowColor() == 1122867) {
                     var14 = var2.getColor(var7);
                  } else {
                     var14 = var2.getShadowColor();
                  }

                  var15.setColor(var14);
               }

               this.mRenderPaint.setStyle(Style.STROKE);
               var1.drawLines(this.mShadowBuffers, this.mRenderPaint);
               this.mBodyBuffers[0] = var9 - 0.5F + var5;
               this.mBodyBuffers[1] = var11 * var4;
               this.mBodyBuffers[2] = var9 + 0.5F - var5;
               this.mBodyBuffers[3] = var10 * var4;
               var3.pointValuesToPixel(this.mBodyBuffers);
               if (var10 > var11) {
                  if (var2.getDecreasingColor() == 1122867) {
                     this.mRenderPaint.setColor(var2.getColor(var7));
                  } else {
                     this.mRenderPaint.setColor(var2.getDecreasingColor());
                  }

                  this.mRenderPaint.setStyle(var2.getDecreasingPaintStyle());
                  var1.drawRect(this.mBodyBuffers[0], this.mBodyBuffers[3], this.mBodyBuffers[2], this.mBodyBuffers[1], this.mRenderPaint);
               } else if (var10 < var11) {
                  if (var2.getIncreasingColor() == 1122867) {
                     this.mRenderPaint.setColor(var2.getColor(var7));
                  } else {
                     this.mRenderPaint.setColor(var2.getIncreasingColor());
                  }

                  this.mRenderPaint.setStyle(var2.getIncreasingPaintStyle());
                  var1.drawRect(this.mBodyBuffers[0], this.mBodyBuffers[1], this.mBodyBuffers[2], this.mBodyBuffers[3], this.mRenderPaint);
               } else {
                  if (var2.getNeutralColor() == 1122867) {
                     this.mRenderPaint.setColor(var2.getColor(var7));
                  } else {
                     this.mRenderPaint.setColor(var2.getNeutralColor());
                  }

                  var1.drawLine(this.mBodyBuffers[0], this.mBodyBuffers[1], this.mBodyBuffers[2], this.mBodyBuffers[3], this.mRenderPaint);
               }
            } else {
               this.mRangeBuffers[0] = var9;
               this.mRangeBuffers[1] = var12 * var4;
               this.mRangeBuffers[2] = var9;
               this.mRangeBuffers[3] = var13 * var4;
               this.mOpenBuffers[0] = var9 - 0.5F + var5;
               float[] var16 = this.mOpenBuffers;
               var12 = var10 * var4;
               var16[1] = var12;
               this.mOpenBuffers[2] = var9;
               this.mOpenBuffers[3] = var12;
               this.mCloseBuffers[0] = 0.5F + var9 - var5;
               var16 = this.mCloseBuffers;
               var12 = var11 * var4;
               var16[1] = var12;
               this.mCloseBuffers[2] = var9;
               this.mCloseBuffers[3] = var12;
               var3.pointValuesToPixel(this.mRangeBuffers);
               var3.pointValuesToPixel(this.mOpenBuffers);
               var3.pointValuesToPixel(this.mCloseBuffers);
               if (var10 > var11) {
                  if (var2.getDecreasingColor() == 1122867) {
                     var14 = var2.getColor(var7);
                  } else {
                     var14 = var2.getDecreasingColor();
                  }
               } else if (var10 < var11) {
                  if (var2.getIncreasingColor() == 1122867) {
                     var14 = var2.getColor(var7);
                  } else {
                     var14 = var2.getIncreasingColor();
                  }
               } else if (var2.getNeutralColor() == 1122867) {
                  var14 = var2.getColor(var7);
               } else {
                  var14 = var2.getNeutralColor();
               }

               this.mRenderPaint.setColor(var14);
               var1.drawLine(this.mRangeBuffers[0], this.mRangeBuffers[1], this.mRangeBuffers[2], this.mRangeBuffers[3], this.mRenderPaint);
               var1.drawLine(this.mOpenBuffers[0], this.mOpenBuffers[1], this.mOpenBuffers[2], this.mOpenBuffers[3], this.mRenderPaint);
               var1.drawLine(this.mCloseBuffers[0], this.mCloseBuffers[1], this.mCloseBuffers[2], this.mCloseBuffers[3], this.mRenderPaint);
            }
         }
      }

   }

   public void drawExtras(Canvas var1) {
   }

   public void drawHighlighted(Canvas var1, Highlight[] var2) {
      CandleData var3 = this.mChart.getCandleData();
      int var4 = 0;

      for(int var5 = var2.length; var4 < var5; ++var4) {
         Highlight var6 = var2[var4];
         ICandleDataSet var7 = (ICandleDataSet)var3.getDataSetByIndex(var6.getDataSetIndex());
         if (var7 != null && var7.isHighlightEnabled()) {
            CandleEntry var8 = (CandleEntry)var7.getEntryForXValue(var6.getX(), var6.getY());
            if (this.isInBoundsX(var8, var7)) {
               float var9 = (var8.getLow() * this.mAnimator.getPhaseY() + var8.getHigh() * this.mAnimator.getPhaseY()) / 2.0F;
               MPPointD var10 = this.mChart.getTransformer(var7.getAxisDependency()).getPixelForValues(var8.getX(), var9);
               var6.setDraw((float)var10.x, (float)var10.y);
               this.drawHighlightLines(var1, (float)var10.x, (float)var10.y, var7);
            }
         }
      }

   }

   public void drawValues(Canvas var1) {
      if (this.isDrawingValuesAllowed(this.mChart)) {
         List var2 = this.mChart.getCandleData().getDataSets();

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            ICandleDataSet var4 = (ICandleDataSet)var2.get(var3);
            if (this.shouldDrawValues(var4)) {
               this.applyValueTextStyle(var4);
               Transformer var5 = this.mChart.getTransformer(var4.getAxisDependency());
               this.mXBounds.set(this.mChart, var4);
               float[] var6 = var5.generateTransformedValuesCandle(var4, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), this.mXBounds.min, this.mXBounds.max);
               float var7 = Utils.convertDpToPixel(5.0F);
               MPPointF var14 = MPPointF.getInstance(var4.getIconsOffset());
               var14.x = Utils.convertDpToPixel(var14.x);
               var14.y = Utils.convertDpToPixel(var14.y);

               for(int var8 = 0; var8 < var6.length; var8 += 2) {
                  float var9 = var6[var8];
                  float var10 = var6[var8 + 1];
                  if (!this.mViewPortHandler.isInBoundsRight(var9)) {
                     break;
                  }

                  if (this.mViewPortHandler.isInBoundsLeft(var9) && this.mViewPortHandler.isInBoundsY(var10)) {
                     int var11 = var8 / 2;
                     CandleEntry var12 = (CandleEntry)var4.getEntryForIndex(this.mXBounds.min + var11);
                     if (var4.isDrawValuesEnabled()) {
                        this.drawValue(var1, var4.getValueFormatter(), var12.getHigh(), var12, var3, var9, var10 - var7, var4.getValueTextColor(var11));
                     }

                     if (var12.getIcon() != null && var4.isDrawIconsEnabled()) {
                        Drawable var15 = var12.getIcon();
                        Utils.drawImage(var1, var15, (int)(var9 + var14.x), (int)(var10 + var14.y), var15.getIntrinsicWidth(), var15.getIntrinsicHeight());
                     }
                  }
               }

               MPPointF.recycleInstance(var14);
            }
         }
      }

   }

   public void initBuffers() {
   }
}

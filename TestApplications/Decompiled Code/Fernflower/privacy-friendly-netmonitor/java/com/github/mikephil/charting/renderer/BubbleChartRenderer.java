package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.Iterator;
import java.util.List;

public class BubbleChartRenderer extends BarLineScatterCandleBubbleRenderer {
   private float[] _hsvBuffer = new float[3];
   protected BubbleDataProvider mChart;
   private float[] pointBuffer = new float[2];
   private float[] sizeBuffer = new float[4];

   public BubbleChartRenderer(BubbleDataProvider var1, ChartAnimator var2, ViewPortHandler var3) {
      super(var2, var3);
      this.mChart = var1;
      this.mRenderPaint.setStyle(Style.FILL);
      this.mHighlightPaint.setStyle(Style.STROKE);
      this.mHighlightPaint.setStrokeWidth(Utils.convertDpToPixel(1.5F));
   }

   public void drawData(Canvas var1) {
      Iterator var2 = this.mChart.getBubbleData().getDataSets().iterator();

      while(var2.hasNext()) {
         IBubbleDataSet var3 = (IBubbleDataSet)var2.next();
         if (var3.isVisible()) {
            this.drawDataSet(var1, var3);
         }
      }

   }

   protected void drawDataSet(Canvas var1, IBubbleDataSet var2) {
      Transformer var3 = this.mChart.getTransformer(var2.getAxisDependency());
      float var4 = this.mAnimator.getPhaseY();
      this.mXBounds.set(this.mChart, var2);
      this.sizeBuffer[0] = 0.0F;
      this.sizeBuffer[2] = 1.0F;
      var3.pointValuesToPixel(this.sizeBuffer);
      boolean var5 = var2.isNormalizeSizeEnabled();
      float var6 = Math.abs(this.sizeBuffer[2] - this.sizeBuffer[0]);
      float var7 = Math.min(Math.abs(this.mViewPortHandler.contentBottom() - this.mViewPortHandler.contentTop()), var6);

      for(int var8 = this.mXBounds.min; var8 <= this.mXBounds.range + this.mXBounds.min; ++var8) {
         BubbleEntry var9 = (BubbleEntry)var2.getEntryForIndex(var8);
         this.pointBuffer[0] = var9.getX();
         this.pointBuffer[1] = var9.getY() * var4;
         var3.pointValuesToPixel(this.pointBuffer);
         var6 = this.getShapeSize(var9.getSize(), var2.getMaxSize(), var7, var5) / 2.0F;
         if (this.mViewPortHandler.isInBoundsTop(this.pointBuffer[1] + var6) && this.mViewPortHandler.isInBoundsBottom(this.pointBuffer[1] - var6) && this.mViewPortHandler.isInBoundsLeft(this.pointBuffer[0] + var6)) {
            if (!this.mViewPortHandler.isInBoundsRight(this.pointBuffer[0] - var6)) {
               break;
            }

            int var10 = var2.getColor((int)var9.getX());
            this.mRenderPaint.setColor(var10);
            var1.drawCircle(this.pointBuffer[0], this.pointBuffer[1], var6, this.mRenderPaint);
         }
      }

   }

   public void drawExtras(Canvas var1) {
   }

   public void drawHighlighted(Canvas var1, Highlight[] var2) {
      BubbleData var3 = this.mChart.getBubbleData();
      float var4 = this.mAnimator.getPhaseY();
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Highlight var7 = var2[var6];
         IBubbleDataSet var8 = (IBubbleDataSet)var3.getDataSetByIndex(var7.getDataSetIndex());
         if (var8 != null && var8.isHighlightEnabled()) {
            BubbleEntry var9 = (BubbleEntry)var8.getEntryForXValue(var7.getX(), var7.getY());
            if (var9.getY() == var7.getY() && this.isInBoundsX(var9, var8)) {
               Transformer var10 = this.mChart.getTransformer(var8.getAxisDependency());
               this.sizeBuffer[0] = 0.0F;
               this.sizeBuffer[2] = 1.0F;
               var10.pointValuesToPixel(this.sizeBuffer);
               boolean var11 = var8.isNormalizeSizeEnabled();
               float var12 = Math.abs(this.sizeBuffer[2] - this.sizeBuffer[0]);
               var12 = Math.min(Math.abs(this.mViewPortHandler.contentBottom() - this.mViewPortHandler.contentTop()), var12);
               this.pointBuffer[0] = var9.getX();
               this.pointBuffer[1] = var9.getY() * var4;
               var10.pointValuesToPixel(this.pointBuffer);
               var7.setDraw(this.pointBuffer[0], this.pointBuffer[1]);
               var12 = this.getShapeSize(var9.getSize(), var8.getMaxSize(), var12, var11) / 2.0F;
               if (this.mViewPortHandler.isInBoundsTop(this.pointBuffer[1] + var12) && this.mViewPortHandler.isInBoundsBottom(this.pointBuffer[1] - var12) && this.mViewPortHandler.isInBoundsLeft(this.pointBuffer[0] + var12)) {
                  if (!this.mViewPortHandler.isInBoundsRight(this.pointBuffer[0] - var12)) {
                     break;
                  }

                  int var13 = var8.getColor((int)var9.getX());
                  Color.RGBToHSV(Color.red(var13), Color.green(var13), Color.blue(var13), this._hsvBuffer);
                  float[] var14 = this._hsvBuffer;
                  var14[2] *= 0.5F;
                  var13 = Color.HSVToColor(Color.alpha(var13), this._hsvBuffer);
                  this.mHighlightPaint.setColor(var13);
                  this.mHighlightPaint.setStrokeWidth(var8.getHighlightCircleWidth());
                  var1.drawCircle(this.pointBuffer[0], this.pointBuffer[1], var12, this.mHighlightPaint);
               }
            }
         }
      }

   }

   public void drawValues(Canvas var1) {
      BubbleData var2 = this.mChart.getBubbleData();
      if (var2 != null) {
         if (this.isDrawingValuesAllowed(this.mChart)) {
            List var3 = var2.getDataSets();
            float var4 = (float)Utils.calcTextHeight(this.mValuePaint, "1");

            for(int var5 = 0; var5 < var3.size(); ++var5) {
               IBubbleDataSet var6 = (IBubbleDataSet)var3.get(var5);
               if (this.shouldDrawValues(var6)) {
                  this.applyValueTextStyle(var6);
                  float var7 = Math.max(0.0F, Math.min(1.0F, this.mAnimator.getPhaseX()));
                  float var8 = this.mAnimator.getPhaseY();
                  this.mXBounds.set(this.mChart, var6);
                  float[] var9 = this.mChart.getTransformer(var6.getAxisDependency()).generateTransformedValuesBubble(var6, var8, this.mXBounds.min, this.mXBounds.max);
                  if (var7 != 1.0F) {
                     var8 = var7;
                  }

                  MPPointF var16 = MPPointF.getInstance(var6.getIconsOffset());
                  var16.x = Utils.convertDpToPixel(var16.x);
                  var16.y = Utils.convertDpToPixel(var16.y);

                  for(int var10 = 0; var10 < var9.length; var10 += 2) {
                     int var11 = var10 / 2;
                     int var12 = var6.getValueTextColor(this.mXBounds.min + var11);
                     var12 = Color.argb(Math.round(255.0F * var8), Color.red(var12), Color.green(var12), Color.blue(var12));
                     var7 = var9[var10];
                     float var13 = var9[var10 + 1];
                     if (!this.mViewPortHandler.isInBoundsRight(var7)) {
                        break;
                     }

                     if (this.mViewPortHandler.isInBoundsLeft(var7) && this.mViewPortHandler.isInBoundsY(var13)) {
                        BubbleEntry var14 = (BubbleEntry)var6.getEntryForIndex(var11 + this.mXBounds.min);
                        if (var6.isDrawValuesEnabled()) {
                           this.drawValue(var1, var6.getValueFormatter(), var14.getSize(), var14, var5, var7, var13 + 0.5F * var4, var12);
                        }

                        if (var14.getIcon() != null && var6.isDrawIconsEnabled()) {
                           Drawable var17 = var14.getIcon();
                           Utils.drawImage(var1, var17, (int)(var7 + var16.x), (int)(var13 + var16.y), var17.getIntrinsicWidth(), var17.getIntrinsicHeight());
                        }
                     }
                  }

                  MPPointF.recycleInstance(var16);
               }
            }
         }

      }
   }

   protected float getShapeSize(float var1, float var2, float var3, boolean var4) {
      float var5 = var1;
      if (var4) {
         if (var2 == 0.0F) {
            var5 = 1.0F;
         } else {
            var5 = (float)Math.sqrt((double)(var1 / var2));
         }
      }

      return var3 * var5;
   }

   public void initBuffers() {
   }
}

package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class YAxisRendererHorizontalBarChart extends YAxisRenderer {
   protected Path mDrawZeroLinePathBuffer = new Path();
   protected float[] mRenderLimitLinesBuffer = new float[4];
   protected Path mRenderLimitLinesPathBuffer = new Path();

   public YAxisRendererHorizontalBarChart(ViewPortHandler var1, YAxis var2, Transformer var3) {
      super(var1, var2, var3);
      this.mLimitLinePaint.setTextAlign(Align.LEFT);
   }

   public void computeAxis(float var1, float var2, boolean var3) {
      float var4 = var1;
      float var5 = var2;
      if (this.mViewPortHandler.contentHeight() > 10.0F) {
         var4 = var1;
         var5 = var2;
         if (!this.mViewPortHandler.isFullyZoomedOutX()) {
            MPPointD var6 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
            MPPointD var7 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop());
            if (!var3) {
               var2 = (float)var6.x;
               var1 = (float)var7.x;
            } else {
               var2 = (float)var7.x;
               var1 = (float)var6.x;
            }

            MPPointD.recycleInstance(var6);
            MPPointD.recycleInstance(var7);
            var5 = var1;
            var4 = var2;
         }
      }

      this.computeAxisValues(var4, var5);
   }

   protected void drawYLabels(Canvas var1, float var2, float[] var3, float var4) {
      this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
      this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
      this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
      int var5 = this.mYAxis.isDrawBottomYLabelEntryEnabled() ^ 1;
      int var6;
      if (this.mYAxis.isDrawTopYLabelEntryEnabled()) {
         var6 = this.mYAxis.mEntryCount;
      } else {
         var6 = this.mYAxis.mEntryCount - 1;
      }

      while(var5 < var6) {
         var1.drawText(this.mYAxis.getFormattedLabel(var5), var3[var5 * 2], var2 - var4, this.mAxisLabelPaint);
         ++var5;
      }

   }

   protected void drawZeroLine(Canvas var1) {
      int var2 = var1.save();
      this.mZeroLineClippingRect.set(this.mViewPortHandler.getContentRect());
      this.mZeroLineClippingRect.inset(-this.mYAxis.getZeroLineWidth(), 0.0F);
      var1.clipRect(this.mLimitLineClippingRect);
      MPPointD var3 = this.mTrans.getPixelForValues(0.0F, 0.0F);
      this.mZeroLinePaint.setColor(this.mYAxis.getZeroLineColor());
      this.mZeroLinePaint.setStrokeWidth(this.mYAxis.getZeroLineWidth());
      Path var4 = this.mDrawZeroLinePathBuffer;
      var4.reset();
      var4.moveTo((float)var3.x - 1.0F, this.mViewPortHandler.contentTop());
      var4.lineTo((float)var3.x - 1.0F, this.mViewPortHandler.contentBottom());
      var1.drawPath(var4, this.mZeroLinePaint);
      var1.restoreToCount(var2);
   }

   public RectF getGridClippingRect() {
      this.mGridClippingRect.set(this.mViewPortHandler.getContentRect());
      this.mGridClippingRect.inset(-this.mAxis.getGridLineWidth(), 0.0F);
      return this.mGridClippingRect;
   }

   protected float[] getTransformedPositions() {
      if (this.mGetTransformedPositionsBuffer.length != this.mYAxis.mEntryCount * 2) {
         this.mGetTransformedPositionsBuffer = new float[this.mYAxis.mEntryCount * 2];
      }

      float[] var1 = this.mGetTransformedPositionsBuffer;

      for(int var2 = 0; var2 < var1.length; var2 += 2) {
         var1[var2] = this.mYAxis.mEntries[var2 / 2];
      }

      this.mTrans.pointValuesToPixel(var1);
      return var1;
   }

   protected Path linePath(Path var1, int var2, float[] var3) {
      var1.moveTo(var3[var2], this.mViewPortHandler.contentTop());
      var1.lineTo(var3[var2], this.mViewPortHandler.contentBottom());
      return var1;
   }

   public void renderAxisLabels(Canvas var1) {
      if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
         float[] var2 = this.getTransformedPositions();
         this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
         this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
         this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
         this.mAxisLabelPaint.setTextAlign(Align.CENTER);
         float var3 = Utils.convertDpToPixel(2.5F);
         float var4 = (float)Utils.calcTextHeight(this.mAxisLabelPaint, "Q");
         YAxis.AxisDependency var5 = this.mYAxis.getAxisDependency();
         YAxis.YAxisLabelPosition var6 = this.mYAxis.getLabelPosition();
         if (var5 == YAxis.AxisDependency.LEFT) {
            if (var6 == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
               var4 = this.mViewPortHandler.contentTop() - var3;
            } else {
               var4 = this.mViewPortHandler.contentTop() - var3;
            }
         } else if (var6 == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
            var4 = this.mViewPortHandler.contentBottom() + var4 + var3;
         } else {
            var4 = this.mViewPortHandler.contentBottom() + var4 + var3;
         }

         this.drawYLabels(var1, var4, var2, this.mYAxis.getYOffset());
      }
   }

   public void renderAxisLine(Canvas var1) {
      if (this.mYAxis.isEnabled() && this.mYAxis.isDrawAxisLineEnabled()) {
         this.mAxisLinePaint.setColor(this.mYAxis.getAxisLineColor());
         this.mAxisLinePaint.setStrokeWidth(this.mYAxis.getAxisLineWidth());
         if (this.mYAxis.getAxisDependency() == YAxis.AxisDependency.LEFT) {
            var1.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mAxisLinePaint);
         } else {
            var1.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
         }

      }
   }

   public void renderLimitLines(Canvas var1) {
      List var2 = this.mYAxis.getLimitLines();
      if (var2 != null && var2.size() > 0) {
         float[] var3 = this.mRenderLimitLinesBuffer;
         var3[0] = 0.0F;
         var3[1] = 0.0F;
         var3[2] = 0.0F;
         var3[3] = 0.0F;
         Path var4 = this.mRenderLimitLinesPathBuffer;
         var4.reset();

         for(int var5 = 0; var5 < var2.size(); ++var5) {
            LimitLine var6 = (LimitLine)var2.get(var5);
            if (var6.isEnabled()) {
               int var7 = var1.save();
               this.mLimitLineClippingRect.set(this.mViewPortHandler.getContentRect());
               this.mLimitLineClippingRect.inset(-var6.getLineWidth(), 0.0F);
               var1.clipRect(this.mLimitLineClippingRect);
               var3[0] = var6.getLimit();
               var3[2] = var6.getLimit();
               this.mTrans.pointValuesToPixel(var3);
               var3[1] = this.mViewPortHandler.contentTop();
               var3[3] = this.mViewPortHandler.contentBottom();
               var4.moveTo(var3[0], var3[1]);
               var4.lineTo(var3[2], var3[3]);
               this.mLimitLinePaint.setStyle(Style.STROKE);
               this.mLimitLinePaint.setColor(var6.getLineColor());
               this.mLimitLinePaint.setPathEffect(var6.getDashPathEffect());
               this.mLimitLinePaint.setStrokeWidth(var6.getLineWidth());
               var1.drawPath(var4, this.mLimitLinePaint);
               var4.reset();
               String var8 = var6.getLabel();
               if (var8 != null && !var8.equals("")) {
                  this.mLimitLinePaint.setStyle(var6.getTextStyle());
                  this.mLimitLinePaint.setPathEffect((PathEffect)null);
                  this.mLimitLinePaint.setColor(var6.getTextColor());
                  this.mLimitLinePaint.setTypeface(var6.getTypeface());
                  this.mLimitLinePaint.setStrokeWidth(0.5F);
                  this.mLimitLinePaint.setTextSize(var6.getTextSize());
                  float var9 = var6.getLineWidth() + var6.getXOffset();
                  float var10 = Utils.convertDpToPixel(2.0F) + var6.getYOffset();
                  LimitLine.LimitLabelPosition var12 = var6.getLabelPosition();
                  float var11;
                  if (var12 == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                     var11 = (float)Utils.calcTextHeight(this.mLimitLinePaint, var8);
                     this.mLimitLinePaint.setTextAlign(Align.LEFT);
                     var1.drawText(var8, var3[0] + var9, this.mViewPortHandler.contentTop() + var10 + var11, this.mLimitLinePaint);
                  } else if (var12 == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                     this.mLimitLinePaint.setTextAlign(Align.LEFT);
                     var1.drawText(var8, var3[0] + var9, this.mViewPortHandler.contentBottom() - var10, this.mLimitLinePaint);
                  } else if (var12 == LimitLine.LimitLabelPosition.LEFT_TOP) {
                     this.mLimitLinePaint.setTextAlign(Align.RIGHT);
                     var11 = (float)Utils.calcTextHeight(this.mLimitLinePaint, var8);
                     var1.drawText(var8, var3[0] - var9, this.mViewPortHandler.contentTop() + var10 + var11, this.mLimitLinePaint);
                  } else {
                     this.mLimitLinePaint.setTextAlign(Align.RIGHT);
                     var1.drawText(var8, var3[0] - var9, this.mViewPortHandler.contentBottom() - var10, this.mLimitLinePaint);
                  }
               }

               var1.restoreToCount(var7);
            }
         }

      }
   }
}

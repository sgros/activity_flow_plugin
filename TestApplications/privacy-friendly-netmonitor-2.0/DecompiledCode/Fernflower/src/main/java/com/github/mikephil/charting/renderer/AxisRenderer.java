package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class AxisRenderer extends Renderer {
   protected AxisBase mAxis;
   protected Paint mAxisLabelPaint;
   protected Paint mAxisLinePaint;
   protected Paint mGridPaint;
   protected Paint mLimitLinePaint;
   protected Transformer mTrans;

   public AxisRenderer(ViewPortHandler var1, Transformer var2, AxisBase var3) {
      super(var1);
      this.mTrans = var2;
      this.mAxis = var3;
      if (this.mViewPortHandler != null) {
         this.mAxisLabelPaint = new Paint(1);
         this.mGridPaint = new Paint();
         this.mGridPaint.setColor(-7829368);
         this.mGridPaint.setStrokeWidth(1.0F);
         this.mGridPaint.setStyle(Style.STROKE);
         this.mGridPaint.setAlpha(90);
         this.mAxisLinePaint = new Paint();
         this.mAxisLinePaint.setColor(-16777216);
         this.mAxisLinePaint.setStrokeWidth(1.0F);
         this.mAxisLinePaint.setStyle(Style.STROKE);
         this.mLimitLinePaint = new Paint(1);
         this.mLimitLinePaint.setStyle(Style.STROKE);
      }

   }

   public void computeAxis(float var1, float var2, boolean var3) {
      float var4 = var1;
      float var5 = var2;
      if (this.mViewPortHandler != null) {
         var4 = var1;
         var5 = var2;
         if (this.mViewPortHandler.contentWidth() > 10.0F) {
            var4 = var1;
            var5 = var2;
            if (!this.mViewPortHandler.isFullyZoomedOutY()) {
               MPPointD var6 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
               MPPointD var7 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom());
               if (!var3) {
                  var2 = (float)var7.y;
                  var1 = (float)var6.y;
               } else {
                  var2 = (float)var6.y;
                  var1 = (float)var7.y;
               }

               MPPointD.recycleInstance(var6);
               MPPointD.recycleInstance(var7);
               var5 = var1;
               var4 = var2;
            }
         }
      }

      this.computeAxisValues(var4, var5);
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

         double var10 = (double)Utils.roundToNextSignificant(Math.pow(10.0D, (double)((int)Math.log10(var8))));
         var6 = var8;
         if ((int)(var8 / var10) > 5) {
            var6 = Math.floor(10.0D * var10);
         }

         int var12 = this.mAxis.isCenterAxisLabelsEnabled();
         int var13;
         if (this.mAxis.isForceLabelsEnabled()) {
            var8 = (double)((float)var4 / (float)(var3 - 1));
            this.mAxis.mEntryCount = var3;
            if (this.mAxis.mEntries.length < var3) {
               this.mAxis.mEntries = new float[var3];
            }

            var12 = 0;

            while(true) {
               var13 = var3;
               var4 = var8;
               if (var12 >= var3) {
                  break;
               }

               this.mAxis.mEntries[var12] = var1;
               var1 = (float)((double)var1 + var8);
               ++var12;
            }
         } else {
            if (var6 == 0.0D) {
               var4 = 0.0D;
            } else {
               var4 = Math.ceil((double)var1 / var6) * var6;
            }

            var8 = var4;
            if (this.mAxis.isCenterAxisLabelsEnabled()) {
               var8 = var4 - var6;
            }

            if (var6 == 0.0D) {
               var4 = 0.0D;
            } else {
               var4 = Utils.nextUp(Math.floor((double)var2 / var6) * var6);
            }

            var13 = var12;
            if (var6 != 0.0D) {
               var10 = var8;

               while(true) {
                  var13 = var12;
                  if (var10 > var4) {
                     break;
                  }

                  ++var12;
                  var10 += var6;
               }
            }

            var12 = var13;
            this.mAxis.mEntryCount = var13;
            if (this.mAxis.mEntries.length < var13) {
               this.mAxis.mEntries = new float[var13];
            }

            var3 = 0;

            while(true) {
               var13 = var12;
               var4 = var6;
               if (var3 >= var12) {
                  break;
               }

               var4 = var8;
               if (var8 == 0.0D) {
                  var4 = 0.0D;
               }

               this.mAxis.mEntries[var3] = (float)var4;
               var8 = var4 + var6;
               ++var3;
            }
         }

         if (var4 < 1.0D) {
            this.mAxis.mDecimals = (int)Math.ceil(-Math.log10(var4));
         } else {
            this.mAxis.mDecimals = 0;
         }

         if (this.mAxis.isCenterAxisLabelsEnabled()) {
            if (this.mAxis.mCenteredEntries.length < var13) {
               this.mAxis.mCenteredEntries = new float[var13];
            }

            var1 = (float)var4 / 2.0F;

            for(var12 = 0; var12 < var13; ++var12) {
               this.mAxis.mCenteredEntries[var12] = this.mAxis.mEntries[var12] + var1;
            }
         }

      } else {
         this.mAxis.mEntries = new float[0];
         this.mAxis.mCenteredEntries = new float[0];
         this.mAxis.mEntryCount = 0;
      }
   }

   public Paint getPaintAxisLabels() {
      return this.mAxisLabelPaint;
   }

   public Paint getPaintAxisLine() {
      return this.mAxisLinePaint;
   }

   public Paint getPaintGrid() {
      return this.mGridPaint;
   }

   public Transformer getTransformer() {
      return this.mTrans;
   }

   public abstract void renderAxisLabels(Canvas var1);

   public abstract void renderAxisLine(Canvas var1);

   public abstract void renderGridLines(Canvas var1);

   public abstract void renderLimitLines(Canvas var1);
}

package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.Iterator;
import java.util.List;

public class ScatterChartRenderer extends LineScatterCandleRadarRenderer {
   protected ScatterDataProvider mChart;
   float[] mPixelBuffer = new float[2];

   public ScatterChartRenderer(ScatterDataProvider var1, ChartAnimator var2, ViewPortHandler var3) {
      super(var2, var3);
      this.mChart = var1;
   }

   public void drawData(Canvas var1) {
      Iterator var2 = this.mChart.getScatterData().getDataSets().iterator();

      while(var2.hasNext()) {
         IScatterDataSet var3 = (IScatterDataSet)var2.next();
         if (var3.isVisible()) {
            this.drawDataSet(var1, var3);
         }
      }

   }

   protected void drawDataSet(Canvas var1, IScatterDataSet var2) {
      ViewPortHandler var3 = this.mViewPortHandler;
      Transformer var4 = this.mChart.getTransformer(var2.getAxisDependency());
      float var5 = this.mAnimator.getPhaseY();
      IShapeRenderer var6 = var2.getShapeRenderer();
      if (var6 == null) {
         Log.i("MISSING", "There's no IShapeRenderer specified for ScatterDataSet");
      } else {
         int var7 = (int)Math.min(Math.ceil((double)((float)var2.getEntryCount() * this.mAnimator.getPhaseX())), (double)((float)var2.getEntryCount()));

         for(int var8 = 0; var8 < var7; ++var8) {
            Entry var9 = var2.getEntryForIndex(var8);
            this.mPixelBuffer[0] = var9.getX();
            this.mPixelBuffer[1] = var9.getY() * var5;
            var4.pointValuesToPixel(this.mPixelBuffer);
            if (!var3.isInBoundsRight(this.mPixelBuffer[0])) {
               break;
            }

            if (var3.isInBoundsLeft(this.mPixelBuffer[0]) && var3.isInBoundsY(this.mPixelBuffer[1])) {
               this.mRenderPaint.setColor(var2.getColor(var8 / 2));
               var6.renderShape(var1, var2, this.mViewPortHandler, this.mPixelBuffer[0], this.mPixelBuffer[1], this.mRenderPaint);
            }
         }

      }
   }

   public void drawExtras(Canvas var1) {
   }

   public void drawHighlighted(Canvas var1, Highlight[] var2) {
      ScatterData var3 = this.mChart.getScatterData();
      int var4 = 0;

      for(int var5 = var2.length; var4 < var5; ++var4) {
         Highlight var6 = var2[var4];
         IScatterDataSet var7 = (IScatterDataSet)var3.getDataSetByIndex(var6.getDataSetIndex());
         if (var7 != null && var7.isHighlightEnabled()) {
            Entry var8 = var7.getEntryForXValue(var6.getX(), var6.getY());
            if (this.isInBoundsX(var8, var7)) {
               MPPointD var9 = this.mChart.getTransformer(var7.getAxisDependency()).getPixelForValues(var8.getX(), var8.getY() * this.mAnimator.getPhaseY());
               var6.setDraw((float)var9.x, (float)var9.y);
               this.drawHighlightLines(var1, (float)var9.x, (float)var9.y, var7);
            }
         }
      }

   }

   public void drawValues(Canvas var1) {
      if (this.isDrawingValuesAllowed(this.mChart)) {
         List var3 = this.mChart.getScatterData().getDataSets();

         for(int var4 = 0; var4 < this.mChart.getScatterData().getDataSetCount(); ++var4) {
            IScatterDataSet var5 = (IScatterDataSet)var3.get(var4);
            if (this.shouldDrawValues(var5)) {
               this.applyValueTextStyle(var5);
               this.mXBounds.set(this.mChart, var5);
               float[] var6 = this.mChart.getTransformer(var5.getAxisDependency()).generateTransformedValuesScatter(var5, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), this.mXBounds.min, this.mXBounds.max);
               float var7 = Utils.convertDpToPixel(var5.getScatterShapeSize());
               MPPointF var2 = MPPointF.getInstance(var5.getIconsOffset());
               var2.x = Utils.convertDpToPixel(var2.x);
               var2.y = Utils.convertDpToPixel(var2.y);

               for(int var8 = 0; var8 < var6.length && this.mViewPortHandler.isInBoundsRight(var6[var8]); var8 += 2) {
                  if (this.mViewPortHandler.isInBoundsLeft(var6[var8])) {
                     ViewPortHandler var10 = this.mViewPortHandler;
                     int var11 = var8 + 1;
                     if (var10.isInBoundsY(var6[var11])) {
                        int var12 = var8 / 2;
                        Entry var13 = var5.getEntryForIndex(this.mXBounds.min + var12);
                        if (var5.isDrawValuesEnabled()) {
                           this.drawValue(var1, var5.getValueFormatter(), var13.getY(), var13, var4, var6[var8], var6[var11] - var7, var5.getValueTextColor(var12 + this.mXBounds.min));
                        }

                        if (var13.getIcon() != null && var5.isDrawIconsEnabled()) {
                           Drawable var14 = var13.getIcon();
                           Utils.drawImage(var1, var14, (int)(var6[var8] + var2.x), (int)(var6[var11] + var2.y), var14.getIntrinsicWidth(), var14.getIntrinsicHeight());
                        }
                     }
                  }
               }

               MPPointF.recycleInstance(var2);
            }
         }
      }

   }

   public void initBuffers() {
   }
}

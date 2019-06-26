package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Path;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class LineScatterCandleRadarRenderer extends BarLineScatterCandleBubbleRenderer {
   private Path mHighlightLinePath = new Path();

   public LineScatterCandleRadarRenderer(ChartAnimator var1, ViewPortHandler var2) {
      super(var1, var2);
   }

   protected void drawHighlightLines(Canvas var1, float var2, float var3, ILineScatterCandleRadarDataSet var4) {
      this.mHighlightPaint.setColor(var4.getHighLightColor());
      this.mHighlightPaint.setStrokeWidth(var4.getHighlightLineWidth());
      this.mHighlightPaint.setPathEffect(var4.getDashPathEffectHighlight());
      if (var4.isVerticalHighlightIndicatorEnabled()) {
         this.mHighlightLinePath.reset();
         this.mHighlightLinePath.moveTo(var2, this.mViewPortHandler.contentTop());
         this.mHighlightLinePath.lineTo(var2, this.mViewPortHandler.contentBottom());
         var1.drawPath(this.mHighlightLinePath, this.mHighlightPaint);
      }

      if (var4.isHorizontalHighlightIndicatorEnabled()) {
         this.mHighlightLinePath.reset();
         this.mHighlightLinePath.moveTo(this.mViewPortHandler.contentLeft(), var3);
         this.mHighlightLinePath.lineTo(this.mViewPortHandler.contentRight(), var3);
         var1.drawPath(this.mHighlightLinePath, this.mHighlightPaint);
      }

   }
}

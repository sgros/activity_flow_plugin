package com.github.mikephil.charting.utils;

public class TransformerHorizontalBarChart extends Transformer {
   public TransformerHorizontalBarChart(ViewPortHandler var1) {
      super(var1);
   }

   public void prepareMatrixOffset(boolean var1) {
      this.mMatrixOffset.reset();
      if (!var1) {
         this.mMatrixOffset.postTranslate(this.mViewPortHandler.offsetLeft(), this.mViewPortHandler.getChartHeight() - this.mViewPortHandler.offsetBottom());
      } else {
         this.mMatrixOffset.setTranslate(-(this.mViewPortHandler.getChartWidth() - this.mViewPortHandler.offsetRight()), this.mViewPortHandler.getChartHeight() - this.mViewPortHandler.offsetBottom());
         this.mMatrixOffset.postScale(-1.0F, 1.0F);
      }

   }
}

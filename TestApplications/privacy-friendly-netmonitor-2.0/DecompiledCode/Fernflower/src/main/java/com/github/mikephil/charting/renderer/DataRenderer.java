package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class DataRenderer extends Renderer {
   protected ChartAnimator mAnimator;
   protected Paint mDrawPaint;
   protected Paint mHighlightPaint;
   protected Paint mRenderPaint;
   protected Paint mValuePaint;

   public DataRenderer(ChartAnimator var1, ViewPortHandler var2) {
      super(var2);
      this.mAnimator = var1;
      this.mRenderPaint = new Paint(1);
      this.mRenderPaint.setStyle(Style.FILL);
      this.mDrawPaint = new Paint(4);
      this.mValuePaint = new Paint(1);
      this.mValuePaint.setColor(Color.rgb(63, 63, 63));
      this.mValuePaint.setTextAlign(Align.CENTER);
      this.mValuePaint.setTextSize(Utils.convertDpToPixel(9.0F));
      this.mHighlightPaint = new Paint(1);
      this.mHighlightPaint.setStyle(Style.STROKE);
      this.mHighlightPaint.setStrokeWidth(2.0F);
      this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
   }

   protected void applyValueTextStyle(IDataSet var1) {
      this.mValuePaint.setTypeface(var1.getValueTypeface());
      this.mValuePaint.setTextSize(var1.getValueTextSize());
   }

   public abstract void drawData(Canvas var1);

   public abstract void drawExtras(Canvas var1);

   public abstract void drawHighlighted(Canvas var1, Highlight[] var2);

   public void drawValue(Canvas var1, IValueFormatter var2, float var3, Entry var4, int var5, float var6, float var7, int var8) {
      this.mValuePaint.setColor(var8);
      var1.drawText(var2.getFormattedValue(var3, var4, var5, this.mViewPortHandler), var6, var7, this.mValuePaint);
   }

   public abstract void drawValues(Canvas var1);

   public Paint getPaintHighlight() {
      return this.mHighlightPaint;
   }

   public Paint getPaintRender() {
      return this.mRenderPaint;
   }

   public Paint getPaintValues() {
      return this.mValuePaint;
   }

   public abstract void initBuffers();

   protected boolean isDrawingValuesAllowed(ChartInterface var1) {
      boolean var2;
      if ((float)var1.getData().getEntryCount() < (float)var1.getMaxVisibleCount() * this.mViewPortHandler.getScaleX()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}

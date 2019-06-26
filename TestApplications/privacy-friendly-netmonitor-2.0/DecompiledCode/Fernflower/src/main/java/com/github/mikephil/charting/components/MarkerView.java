package com.github.mikephil.charting.components;

import android.content.Context;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import java.lang.ref.WeakReference;

public class MarkerView extends RelativeLayout implements IMarker {
   private MPPointF mOffset = new MPPointF();
   private MPPointF mOffset2 = new MPPointF();
   private WeakReference mWeakChart;

   public MarkerView(Context var1, int var2) {
      super(var1);
      this.setupLayoutResource(var2);
   }

   private void setupLayoutResource(int var1) {
      View var2 = LayoutInflater.from(this.getContext()).inflate(var1, this);
      var2.setLayoutParams(new LayoutParams(-2, -2));
      var2.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
      var2.layout(0, 0, var2.getMeasuredWidth(), var2.getMeasuredHeight());
   }

   public void draw(Canvas var1, float var2, float var3) {
      MPPointF var4 = this.getOffsetForDrawingAtPoint(var2, var3);
      int var5 = var1.save();
      var1.translate(var2 + var4.x, var3 + var4.y);
      this.draw(var1);
      var1.restoreToCount(var5);
   }

   public Chart getChartView() {
      Chart var1;
      if (this.mWeakChart == null) {
         var1 = null;
      } else {
         var1 = (Chart)this.mWeakChart.get();
      }

      return var1;
   }

   public MPPointF getOffset() {
      return this.mOffset;
   }

   public MPPointF getOffsetForDrawingAtPoint(float var1, float var2) {
      MPPointF var3 = this.getOffset();
      this.mOffset2.x = var3.x;
      this.mOffset2.y = var3.y;
      Chart var6 = this.getChartView();
      float var4 = (float)this.getWidth();
      float var5 = (float)this.getHeight();
      if (this.mOffset2.x + var1 < 0.0F) {
         this.mOffset2.x = -var1;
      } else if (var6 != null && var1 + var4 + this.mOffset2.x > (float)var6.getWidth()) {
         this.mOffset2.x = (float)var6.getWidth() - var1 - var4;
      }

      if (this.mOffset2.y + var2 < 0.0F) {
         this.mOffset2.y = -var2;
      } else if (var6 != null && var2 + var5 + this.mOffset2.y > (float)var6.getHeight()) {
         this.mOffset2.y = (float)var6.getHeight() - var2 - var5;
      }

      return this.mOffset2;
   }

   public void refreshContent(Entry var1, Highlight var2) {
      this.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
      this.layout(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
   }

   public void setChartView(Chart var1) {
      this.mWeakChart = new WeakReference(var1);
   }

   public void setOffset(float var1, float var2) {
      this.mOffset.x = var1;
      this.mOffset.y = var2;
   }

   public void setOffset(MPPointF var1) {
      this.mOffset = var1;
      if (this.mOffset == null) {
         this.mOffset = new MPPointF();
      }

   }
}

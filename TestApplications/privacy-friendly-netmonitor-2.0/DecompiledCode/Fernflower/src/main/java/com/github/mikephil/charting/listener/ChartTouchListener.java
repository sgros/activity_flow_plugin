package com.github.mikephil.charting.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.highlight.Highlight;

public abstract class ChartTouchListener extends SimpleOnGestureListener implements OnTouchListener {
   protected static final int DRAG = 1;
   protected static final int NONE = 0;
   protected static final int PINCH_ZOOM = 4;
   protected static final int POST_ZOOM = 5;
   protected static final int ROTATE = 6;
   protected static final int X_ZOOM = 2;
   protected static final int Y_ZOOM = 3;
   protected Chart mChart;
   protected GestureDetector mGestureDetector;
   protected ChartTouchListener.ChartGesture mLastGesture;
   protected Highlight mLastHighlighted;
   protected int mTouchMode;

   public ChartTouchListener(Chart var1) {
      this.mLastGesture = ChartTouchListener.ChartGesture.NONE;
      this.mTouchMode = 0;
      this.mChart = var1;
      this.mGestureDetector = new GestureDetector(var1.getContext(), this);
   }

   protected static float distance(float var0, float var1, float var2, float var3) {
      var0 -= var1;
      var1 = var2 - var3;
      return (float)Math.sqrt((double)(var0 * var0 + var1 * var1));
   }

   public void endAction(MotionEvent var1) {
      OnChartGestureListener var2 = this.mChart.getOnChartGestureListener();
      if (var2 != null) {
         var2.onChartGestureEnd(var1, this.mLastGesture);
      }

   }

   public ChartTouchListener.ChartGesture getLastGesture() {
      return this.mLastGesture;
   }

   public int getTouchMode() {
      return this.mTouchMode;
   }

   protected void performHighlight(Highlight var1, MotionEvent var2) {
      if (var1 != null && !var1.equalTo(this.mLastHighlighted)) {
         this.mChart.highlightValue(var1, true);
         this.mLastHighlighted = var1;
      } else {
         this.mChart.highlightValue((Highlight)null, true);
         this.mLastHighlighted = null;
      }

   }

   public void setLastHighlighted(Highlight var1) {
      this.mLastHighlighted = var1;
   }

   public void startAction(MotionEvent var1) {
      OnChartGestureListener var2 = this.mChart.getOnChartGestureListener();
      if (var2 != null) {
         var2.onChartGestureStart(var1, this.mLastGesture);
      }

   }

   public static enum ChartGesture {
      DOUBLE_TAP,
      DRAG,
      FLING,
      LONG_PRESS,
      NONE,
      PINCH_ZOOM,
      ROTATE,
      SINGLE_TAP,
      X_ZOOM,
      Y_ZOOM;
   }
}

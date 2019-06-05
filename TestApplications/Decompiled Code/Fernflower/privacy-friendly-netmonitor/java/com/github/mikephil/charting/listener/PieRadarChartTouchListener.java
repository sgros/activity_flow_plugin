package com.github.mikephil.charting.listener;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.github.mikephil.charting.charts.PieRadarChartBase;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;

public class PieRadarChartTouchListener extends ChartTouchListener {
   private ArrayList _velocitySamples = new ArrayList();
   private float mDecelerationAngularVelocity = 0.0F;
   private long mDecelerationLastTime = 0L;
   private float mStartAngle = 0.0F;
   private MPPointF mTouchStartPoint = MPPointF.getInstance(0.0F, 0.0F);

   public PieRadarChartTouchListener(PieRadarChartBase var1) {
      super(var1);
   }

   private float calculateVelocity() {
      if (this._velocitySamples.isEmpty()) {
         return 0.0F;
      } else {
         ArrayList var1 = this._velocitySamples;
         boolean var2 = false;
         PieRadarChartTouchListener.AngularVelocitySample var3 = (PieRadarChartTouchListener.AngularVelocitySample)var1.get(0);
         PieRadarChartTouchListener.AngularVelocitySample var4 = (PieRadarChartTouchListener.AngularVelocitySample)this._velocitySamples.get(this._velocitySamples.size() - 1);
         int var5 = this._velocitySamples.size() - 1;

         PieRadarChartTouchListener.AngularVelocitySample var8;
         for(var8 = var3; var5 >= 0; --var5) {
            var8 = (PieRadarChartTouchListener.AngularVelocitySample)this._velocitySamples.get(var5);
            if (var8.angle != var4.angle) {
               break;
            }
         }

         float var6 = (float)(var4.time - var3.time) / 1000.0F;
         float var7 = var6;
         if (var6 == 0.0F) {
            var7 = 0.1F;
         }

         boolean var9 = var2;
         if (var4.angle >= var8.angle) {
            var9 = true;
         }

         var2 = var9;
         if ((double)Math.abs(var4.angle - var8.angle) > 270.0D) {
            var2 = var9 ^ true;
         }

         if ((double)(var4.angle - var3.angle) > 180.0D) {
            var3.angle = (float)((double)var3.angle + 360.0D);
         } else if ((double)(var3.angle - var4.angle) > 180.0D) {
            var4.angle = (float)((double)var4.angle + 360.0D);
         }

         var6 = Math.abs((var4.angle - var3.angle) / var7);
         var7 = var6;
         if (!var2) {
            var7 = -var6;
         }

         return var7;
      }
   }

   private void resetVelocity() {
      this._velocitySamples.clear();
   }

   private void sampleVelocity(float var1, float var2) {
      long var3 = AnimationUtils.currentAnimationTimeMillis();
      this._velocitySamples.add(new PieRadarChartTouchListener.AngularVelocitySample(var3, ((PieRadarChartBase)this.mChart).getAngleForPoint(var1, var2)));

      for(int var5 = this._velocitySamples.size(); var5 - 2 > 0 && var3 - ((PieRadarChartTouchListener.AngularVelocitySample)this._velocitySamples.get(0)).time > 1000L; --var5) {
         this._velocitySamples.remove(0);
      }

   }

   public void computeScroll() {
      if (this.mDecelerationAngularVelocity != 0.0F) {
         long var1 = AnimationUtils.currentAnimationTimeMillis();
         this.mDecelerationAngularVelocity *= ((PieRadarChartBase)this.mChart).getDragDecelerationFrictionCoef();
         float var3 = (float)(var1 - this.mDecelerationLastTime) / 1000.0F;
         ((PieRadarChartBase)this.mChart).setRotationAngle(((PieRadarChartBase)this.mChart).getRotationAngle() + this.mDecelerationAngularVelocity * var3);
         this.mDecelerationLastTime = var1;
         if ((double)Math.abs(this.mDecelerationAngularVelocity) >= 0.001D) {
            Utils.postInvalidateOnAnimation(this.mChart);
         } else {
            this.stopDeceleration();
         }

      }
   }

   public void onLongPress(MotionEvent var1) {
      this.mLastGesture = ChartTouchListener.ChartGesture.LONG_PRESS;
      OnChartGestureListener var2 = ((PieRadarChartBase)this.mChart).getOnChartGestureListener();
      if (var2 != null) {
         var2.onChartLongPressed(var1);
      }

   }

   public boolean onSingleTapConfirmed(MotionEvent var1) {
      return true;
   }

   public boolean onSingleTapUp(MotionEvent var1) {
      this.mLastGesture = ChartTouchListener.ChartGesture.SINGLE_TAP;
      OnChartGestureListener var2 = ((PieRadarChartBase)this.mChart).getOnChartGestureListener();
      if (var2 != null) {
         var2.onChartSingleTapped(var1);
      }

      if (!((PieRadarChartBase)this.mChart).isHighlightPerTapEnabled()) {
         return false;
      } else {
         this.performHighlight(((PieRadarChartBase)this.mChart).getHighlightByTouchPoint(var1.getX(), var1.getY()), var1);
         return true;
      }
   }

   @SuppressLint({"ClickableViewAccessibility"})
   public boolean onTouch(View var1, MotionEvent var2) {
      if (this.mGestureDetector.onTouchEvent(var2)) {
         return true;
      } else {
         if (((PieRadarChartBase)this.mChart).isRotationEnabled()) {
            float var3 = var2.getX();
            float var4 = var2.getY();
            switch(var2.getAction()) {
            case 0:
               this.startAction(var2);
               this.stopDeceleration();
               this.resetVelocity();
               if (((PieRadarChartBase)this.mChart).isDragDecelerationEnabled()) {
                  this.sampleVelocity(var3, var4);
               }

               this.setGestureStartAngle(var3, var4);
               this.mTouchStartPoint.x = var3;
               this.mTouchStartPoint.y = var4;
               break;
            case 1:
               if (((PieRadarChartBase)this.mChart).isDragDecelerationEnabled()) {
                  this.stopDeceleration();
                  this.sampleVelocity(var3, var4);
                  this.mDecelerationAngularVelocity = this.calculateVelocity();
                  if (this.mDecelerationAngularVelocity != 0.0F) {
                     this.mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();
                     Utils.postInvalidateOnAnimation(this.mChart);
                  }
               }

               ((PieRadarChartBase)this.mChart).enableScroll();
               this.mTouchMode = 0;
               this.endAction(var2);
               break;
            case 2:
               if (((PieRadarChartBase)this.mChart).isDragDecelerationEnabled()) {
                  this.sampleVelocity(var3, var4);
               }

               if (this.mTouchMode == 0 && distance(var3, this.mTouchStartPoint.x, var4, this.mTouchStartPoint.y) > Utils.convertDpToPixel(8.0F)) {
                  this.mLastGesture = ChartTouchListener.ChartGesture.ROTATE;
                  this.mTouchMode = 6;
                  ((PieRadarChartBase)this.mChart).disableScroll();
               } else if (this.mTouchMode == 6) {
                  this.updateGestureRotation(var3, var4);
                  ((PieRadarChartBase)this.mChart).invalidate();
               }

               this.endAction(var2);
            }
         }

         return true;
      }
   }

   public void setGestureStartAngle(float var1, float var2) {
      this.mStartAngle = ((PieRadarChartBase)this.mChart).getAngleForPoint(var1, var2) - ((PieRadarChartBase)this.mChart).getRawRotationAngle();
   }

   public void stopDeceleration() {
      this.mDecelerationAngularVelocity = 0.0F;
   }

   public void updateGestureRotation(float var1, float var2) {
      ((PieRadarChartBase)this.mChart).setRotationAngle(((PieRadarChartBase)this.mChart).getAngleForPoint(var1, var2) - this.mStartAngle);
   }

   private class AngularVelocitySample {
      public float angle;
      public long time;

      public AngularVelocitySample(long var2, float var4) {
         this.time = var2;
         this.angle = var4;
      }
   }
}

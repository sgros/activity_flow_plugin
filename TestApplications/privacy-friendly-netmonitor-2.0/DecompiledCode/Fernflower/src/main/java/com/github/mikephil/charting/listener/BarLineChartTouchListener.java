package com.github.mikephil.charting.listener;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class BarLineChartTouchListener extends ChartTouchListener {
   private IDataSet mClosestDataSetToTouch;
   private MPPointF mDecelerationCurrentPoint = MPPointF.getInstance(0.0F, 0.0F);
   private long mDecelerationLastTime = 0L;
   private MPPointF mDecelerationVelocity = MPPointF.getInstance(0.0F, 0.0F);
   private float mDragTriggerDist;
   private Matrix mMatrix = new Matrix();
   private float mMinScalePointerDistance;
   private float mSavedDist = 1.0F;
   private Matrix mSavedMatrix = new Matrix();
   private float mSavedXDist = 1.0F;
   private float mSavedYDist = 1.0F;
   private MPPointF mTouchPointCenter = MPPointF.getInstance(0.0F, 0.0F);
   private MPPointF mTouchStartPoint = MPPointF.getInstance(0.0F, 0.0F);
   private VelocityTracker mVelocityTracker;

   public BarLineChartTouchListener(BarLineChartBase var1, Matrix var2, float var3) {
      super(var1);
      this.mMatrix = var2;
      this.mDragTriggerDist = Utils.convertDpToPixel(var3);
      this.mMinScalePointerDistance = Utils.convertDpToPixel(3.5F);
   }

   private static float getXDist(MotionEvent var0) {
      return Math.abs(var0.getX(0) - var0.getX(1));
   }

   private static float getYDist(MotionEvent var0) {
      return Math.abs(var0.getY(0) - var0.getY(1));
   }

   private boolean inverted() {
      boolean var1;
      if ((this.mClosestDataSetToTouch != null || !((BarLineChartBase)this.mChart).isAnyAxisInverted()) && (this.mClosestDataSetToTouch == null || !((BarLineChartBase)this.mChart).isInverted(this.mClosestDataSetToTouch.getAxisDependency()))) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static void midPoint(MPPointF var0, MotionEvent var1) {
      float var2 = var1.getX(0);
      float var3 = var1.getX(1);
      float var4 = var1.getY(0);
      float var5 = var1.getY(1);
      var0.x = (var2 + var3) / 2.0F;
      var0.y = (var4 + var5) / 2.0F;
   }

   private void performDrag(MotionEvent var1, float var2, float var3) {
      this.mLastGesture = ChartTouchListener.ChartGesture.DRAG;
      this.mMatrix.set(this.mSavedMatrix);
      OnChartGestureListener var4 = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
      float var5 = var2;
      float var6 = var3;
      if (this.inverted()) {
         if (this.mChart instanceof HorizontalBarChart) {
            var5 = -var2;
            var6 = var3;
         } else {
            var6 = -var3;
            var5 = var2;
         }
      }

      this.mMatrix.postTranslate(var5, var6);
      if (var4 != null) {
         var4.onChartTranslate(var1, var5, var6);
      }

   }

   private void performHighlightDrag(MotionEvent var1) {
      Highlight var2 = ((BarLineChartBase)this.mChart).getHighlightByTouchPoint(var1.getX(), var1.getY());
      if (var2 != null && !var2.equalTo(this.mLastHighlighted)) {
         this.mLastHighlighted = var2;
         ((BarLineChartBase)this.mChart).highlightValue(var2, true);
      }

   }

   private void performZoom(MotionEvent var1) {
      if (var1.getPointerCount() >= 2) {
         OnChartGestureListener var2 = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
         float var3 = spacing(var1);
         if (var3 > this.mMinScalePointerDistance) {
            MPPointF var4 = this.getTrans(this.mTouchPointCenter.x, this.mTouchPointCenter.y);
            ViewPortHandler var5 = ((BarLineChartBase)this.mChart).getViewPortHandler();
            int var6 = this.mTouchMode;
            boolean var7 = false;
            boolean var8 = false;
            boolean var9 = false;
            boolean var11;
            if (var6 == 4) {
               this.mLastGesture = ChartTouchListener.ChartGesture.PINCH_ZOOM;
               float var10 = var3 / this.mSavedDist;
               if (var10 < 1.0F) {
                  var9 = true;
               }

               if (var9) {
                  var11 = var5.canZoomOutMoreX();
               } else {
                  var11 = var5.canZoomInMoreX();
               }

               boolean var12;
               if (var9) {
                  var12 = var5.canZoomOutMoreY();
               } else {
                  var12 = var5.canZoomInMoreY();
               }

               if (((BarLineChartBase)this.mChart).isScaleXEnabled()) {
                  var3 = var10;
               } else {
                  var3 = 1.0F;
               }

               if (!((BarLineChartBase)this.mChart).isScaleYEnabled()) {
                  var10 = 1.0F;
               }

               if (var12 || var11) {
                  this.mMatrix.set(this.mSavedMatrix);
                  this.mMatrix.postScale(var3, var10, var4.x, var4.y);
                  if (var2 != null) {
                     var2.onChartScale(var1, var3, var10);
                  }
               }
            } else if (this.mTouchMode == 2 && ((BarLineChartBase)this.mChart).isScaleXEnabled()) {
               this.mLastGesture = ChartTouchListener.ChartGesture.X_ZOOM;
               var3 = getXDist(var1) / this.mSavedXDist;
               var9 = var7;
               if (var3 < 1.0F) {
                  var9 = true;
               }

               if (var9) {
                  var11 = var5.canZoomOutMoreX();
               } else {
                  var11 = var5.canZoomInMoreX();
               }

               if (var11) {
                  this.mMatrix.set(this.mSavedMatrix);
                  this.mMatrix.postScale(var3, 1.0F, var4.x, var4.y);
                  if (var2 != null) {
                     var2.onChartScale(var1, var3, 1.0F);
                  }
               }
            } else if (this.mTouchMode == 3 && ((BarLineChartBase)this.mChart).isScaleYEnabled()) {
               this.mLastGesture = ChartTouchListener.ChartGesture.Y_ZOOM;
               var3 = getYDist(var1) / this.mSavedYDist;
               var9 = var8;
               if (var3 < 1.0F) {
                  var9 = true;
               }

               if (var9) {
                  var11 = var5.canZoomOutMoreY();
               } else {
                  var11 = var5.canZoomInMoreY();
               }

               if (var11) {
                  this.mMatrix.set(this.mSavedMatrix);
                  this.mMatrix.postScale(1.0F, var3, var4.x, var4.y);
                  if (var2 != null) {
                     var2.onChartScale(var1, 1.0F, var3);
                  }
               }
            }

            MPPointF.recycleInstance(var4);
         }
      }

   }

   private void saveTouchStart(MotionEvent var1) {
      this.mSavedMatrix.set(this.mMatrix);
      this.mTouchStartPoint.x = var1.getX();
      this.mTouchStartPoint.y = var1.getY();
      this.mClosestDataSetToTouch = ((BarLineChartBase)this.mChart).getDataSetByTouchPoint(var1.getX(), var1.getY());
   }

   private static float spacing(MotionEvent var0) {
      float var1 = var0.getX(0) - var0.getX(1);
      float var2 = var0.getY(0) - var0.getY(1);
      return (float)Math.sqrt((double)(var1 * var1 + var2 * var2));
   }

   public void computeScroll() {
      float var1 = this.mDecelerationVelocity.x;
      float var2 = 0.0F;
      if (var1 != 0.0F || this.mDecelerationVelocity.y != 0.0F) {
         long var3 = AnimationUtils.currentAnimationTimeMillis();
         MPPointF var5 = this.mDecelerationVelocity;
         var5.x *= ((BarLineChartBase)this.mChart).getDragDecelerationFrictionCoef();
         var5 = this.mDecelerationVelocity;
         var5.y *= ((BarLineChartBase)this.mChart).getDragDecelerationFrictionCoef();
         var1 = (float)(var3 - this.mDecelerationLastTime) / 1000.0F;
         float var6 = this.mDecelerationVelocity.x;
         float var7 = this.mDecelerationVelocity.y;
         var5 = this.mDecelerationCurrentPoint;
         var5.x += var6 * var1;
         var5 = this.mDecelerationCurrentPoint;
         var5.y += var7 * var1;
         MotionEvent var8 = MotionEvent.obtain(var3, var3, 2, this.mDecelerationCurrentPoint.x, this.mDecelerationCurrentPoint.y, 0);
         if (((BarLineChartBase)this.mChart).isDragXEnabled()) {
            var1 = this.mDecelerationCurrentPoint.x - this.mTouchStartPoint.x;
         } else {
            var1 = 0.0F;
         }

         if (((BarLineChartBase)this.mChart).isDragYEnabled()) {
            var2 = this.mDecelerationCurrentPoint.y - this.mTouchStartPoint.y;
         }

         this.performDrag(var8, var1, var2);
         var8.recycle();
         this.mMatrix = ((BarLineChartBase)this.mChart).getViewPortHandler().refresh(this.mMatrix, this.mChart, false);
         this.mDecelerationLastTime = var3;
         if ((double)Math.abs(this.mDecelerationVelocity.x) < 0.01D && (double)Math.abs(this.mDecelerationVelocity.y) < 0.01D) {
            ((BarLineChartBase)this.mChart).calculateOffsets();
            ((BarLineChartBase)this.mChart).postInvalidate();
            this.stopDeceleration();
         } else {
            Utils.postInvalidateOnAnimation(this.mChart);
         }

      }
   }

   public Matrix getMatrix() {
      return this.mMatrix;
   }

   public MPPointF getTrans(float var1, float var2) {
      ViewPortHandler var3 = ((BarLineChartBase)this.mChart).getViewPortHandler();
      float var4 = var3.offsetLeft();
      if (this.inverted()) {
         var2 = -(var2 - var3.offsetTop());
      } else {
         var2 = -((float)((BarLineChartBase)this.mChart).getMeasuredHeight() - var2 - var3.offsetBottom());
      }

      return MPPointF.getInstance(var1 - var4, var2);
   }

   public boolean onDoubleTap(MotionEvent var1) {
      this.mLastGesture = ChartTouchListener.ChartGesture.DOUBLE_TAP;
      OnChartGestureListener var2 = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
      if (var2 != null) {
         var2.onChartDoubleTapped(var1);
      }

      if (((BarLineChartBase)this.mChart).isDoubleTapToZoomEnabled() && ((BarLineScatterCandleBubbleData)((BarLineChartBase)this.mChart).getData()).getEntryCount() > 0) {
         MPPointF var7 = this.getTrans(var1.getX(), var1.getY());
         BarLineChartBase var3 = (BarLineChartBase)this.mChart;
         boolean var4 = ((BarLineChartBase)this.mChart).isScaleXEnabled();
         float var5 = 1.0F;
         float var6;
         if (var4) {
            var6 = 1.4F;
         } else {
            var6 = 1.0F;
         }

         if (((BarLineChartBase)this.mChart).isScaleYEnabled()) {
            var5 = 1.4F;
         }

         var3.zoom(var6, var5, var7.x, var7.y);
         if (((BarLineChartBase)this.mChart).isLogEnabled()) {
            StringBuilder var8 = new StringBuilder();
            var8.append("Double-Tap, Zooming In, x: ");
            var8.append(var7.x);
            var8.append(", y: ");
            var8.append(var7.y);
            Log.i("BarlineChartTouch", var8.toString());
         }

         MPPointF.recycleInstance(var7);
      }

      return super.onDoubleTap(var1);
   }

   public boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4) {
      this.mLastGesture = ChartTouchListener.ChartGesture.FLING;
      OnChartGestureListener var5 = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
      if (var5 != null) {
         var5.onChartFling(var1, var2, var3, var4);
      }

      return super.onFling(var1, var2, var3, var4);
   }

   public void onLongPress(MotionEvent var1) {
      this.mLastGesture = ChartTouchListener.ChartGesture.LONG_PRESS;
      OnChartGestureListener var2 = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
      if (var2 != null) {
         var2.onChartLongPressed(var1);
      }

   }

   public boolean onSingleTapUp(MotionEvent var1) {
      this.mLastGesture = ChartTouchListener.ChartGesture.SINGLE_TAP;
      OnChartGestureListener var2 = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
      if (var2 != null) {
         var2.onChartSingleTapped(var1);
      }

      if (!((BarLineChartBase)this.mChart).isHighlightPerTapEnabled()) {
         return false;
      } else {
         this.performHighlight(((BarLineChartBase)this.mChart).getHighlightByTouchPoint(var1.getX(), var1.getY()), var1);
         return super.onSingleTapUp(var1);
      }
   }

   @SuppressLint({"ClickableViewAccessibility"})
   public boolean onTouch(View var1, MotionEvent var2) {
      if (this.mVelocityTracker == null) {
         this.mVelocityTracker = VelocityTracker.obtain();
      }

      this.mVelocityTracker.addMovement(var2);
      int var3 = var2.getActionMasked();
      byte var4 = 3;
      if (var3 == 3 && this.mVelocityTracker != null) {
         this.mVelocityTracker.recycle();
         this.mVelocityTracker = null;
      }

      if (this.mTouchMode == 0) {
         this.mGestureDetector.onTouchEvent(var2);
      }

      if (!((BarLineChartBase)this.mChart).isDragEnabled() && !((BarLineChartBase)this.mChart).isScaleXEnabled() && !((BarLineChartBase)this.mChart).isScaleYEnabled()) {
         return true;
      } else {
         int var5 = var2.getAction();
         boolean var10 = false;
         float var7;
         float var8;
         switch(var5 & 255) {
         case 0:
            this.startAction(var2);
            this.stopDeceleration();
            this.saveTouchStart(var2);
            break;
         case 1:
            VelocityTracker var9 = this.mVelocityTracker;
            int var12 = var2.getPointerId(0);
            var9.computeCurrentVelocity(1000, (float)Utils.getMaximumFlingVelocity());
            var7 = var9.getYVelocity(var12);
            var8 = var9.getXVelocity(var12);
            if ((Math.abs(var8) > (float)Utils.getMinimumFlingVelocity() || Math.abs(var7) > (float)Utils.getMinimumFlingVelocity()) && this.mTouchMode == 1 && ((BarLineChartBase)this.mChart).isDragDecelerationEnabled()) {
               this.stopDeceleration();
               this.mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();
               this.mDecelerationCurrentPoint.x = var2.getX();
               this.mDecelerationCurrentPoint.y = var2.getY();
               this.mDecelerationVelocity.x = var8;
               this.mDecelerationVelocity.y = var7;
               Utils.postInvalidateOnAnimation(this.mChart);
            }

            if (this.mTouchMode == 2 || this.mTouchMode == 3 || this.mTouchMode == 4 || this.mTouchMode == 5) {
               ((BarLineChartBase)this.mChart).calculateOffsets();
               ((BarLineChartBase)this.mChart).postInvalidate();
            }

            this.mTouchMode = 0;
            ((BarLineChartBase)this.mChart).enableScroll();
            if (this.mVelocityTracker != null) {
               this.mVelocityTracker.recycle();
               this.mVelocityTracker = null;
            }

            this.endAction(var2);
            break;
         case 2:
            if (this.mTouchMode == 1) {
               ((BarLineChartBase)this.mChart).disableScroll();
               boolean var6 = ((BarLineChartBase)this.mChart).isDragXEnabled();
               var7 = 0.0F;
               if (var6) {
                  var8 = var2.getX() - this.mTouchStartPoint.x;
               } else {
                  var8 = 0.0F;
               }

               if (((BarLineChartBase)this.mChart).isDragYEnabled()) {
                  var7 = var2.getY() - this.mTouchStartPoint.y;
               }

               this.performDrag(var2, var8, var7);
            } else if (this.mTouchMode != 2 && this.mTouchMode != 3 && this.mTouchMode != 4) {
               if (this.mTouchMode == 0 && Math.abs(distance(var2.getX(), this.mTouchStartPoint.x, var2.getY(), this.mTouchStartPoint.y)) > this.mDragTriggerDist && ((BarLineChartBase)this.mChart).isDragEnabled()) {
                  boolean var11;
                  label127: {
                     if (((BarLineChartBase)this.mChart).isFullyZoomedOut()) {
                        var11 = var10;
                        if (((BarLineChartBase)this.mChart).hasNoDragOffset()) {
                           break label127;
                        }
                     }

                     var11 = true;
                  }

                  if (var11) {
                     var7 = Math.abs(var2.getX() - this.mTouchStartPoint.x);
                     var8 = Math.abs(var2.getY() - this.mTouchStartPoint.y);
                     if ((((BarLineChartBase)this.mChart).isDragXEnabled() || var8 >= var7) && (((BarLineChartBase)this.mChart).isDragYEnabled() || var8 <= var7)) {
                        this.mLastGesture = ChartTouchListener.ChartGesture.DRAG;
                        this.mTouchMode = 1;
                     }
                  } else if (((BarLineChartBase)this.mChart).isHighlightPerDragEnabled()) {
                     this.mLastGesture = ChartTouchListener.ChartGesture.DRAG;
                     if (((BarLineChartBase)this.mChart).isHighlightPerDragEnabled()) {
                        this.performHighlightDrag(var2);
                     }
                  }
               }
            } else {
               ((BarLineChartBase)this.mChart).disableScroll();
               if (((BarLineChartBase)this.mChart).isScaleXEnabled() || ((BarLineChartBase)this.mChart).isScaleYEnabled()) {
                  this.performZoom(var2);
               }
            }
            break;
         case 3:
            this.mTouchMode = 0;
            this.endAction(var2);
         case 4:
         default:
            break;
         case 5:
            if (var2.getPointerCount() >= 2) {
               ((BarLineChartBase)this.mChart).disableScroll();
               this.saveTouchStart(var2);
               this.mSavedXDist = getXDist(var2);
               this.mSavedYDist = getYDist(var2);
               this.mSavedDist = spacing(var2);
               if (this.mSavedDist > 10.0F) {
                  if (((BarLineChartBase)this.mChart).isPinchZoomEnabled()) {
                     this.mTouchMode = 4;
                  } else if (((BarLineChartBase)this.mChart).isScaleXEnabled() != ((BarLineChartBase)this.mChart).isScaleYEnabled()) {
                     if (((BarLineChartBase)this.mChart).isScaleXEnabled()) {
                        var4 = 2;
                     }

                     this.mTouchMode = var4;
                  } else {
                     if (this.mSavedXDist > this.mSavedYDist) {
                        var4 = 2;
                     }

                     this.mTouchMode = var4;
                  }
               }

               midPoint(this.mTouchPointCenter, var2);
            }
            break;
         case 6:
            Utils.velocityTrackerPointerUpCleanUpIfNecessary(var2, this.mVelocityTracker);
            this.mTouchMode = 5;
         }

         this.mMatrix = ((BarLineChartBase)this.mChart).getViewPortHandler().refresh(this.mMatrix, this.mChart, true);
         return true;
      }
   }

   public void setDragTriggerDist(float var1) {
      this.mDragTriggerDist = Utils.convertDpToPixel(var1);
   }

   public void stopDeceleration() {
      this.mDecelerationVelocity.x = 0.0F;
      this.mDecelerationVelocity.y = 0.0F;
   }
}

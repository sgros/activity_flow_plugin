package com.github.mikephil.charting.charts;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.listener.PieRadarChartTouchListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

public abstract class PieRadarChartBase extends Chart {
   protected float mMinOffset = 0.0F;
   private float mRawRotationAngle = 270.0F;
   protected boolean mRotateEnabled = true;
   private float mRotationAngle = 270.0F;

   public PieRadarChartBase(Context var1) {
      super(var1);
   }

   public PieRadarChartBase(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public PieRadarChartBase(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void calcMinMax() {
   }

   public void calculateOffsets() {
      Legend var1 = this.mLegend;
      float var2 = 0.0F;
      float var3 = 0.0F;
      float var4;
      float var5;
      float var6;
      float var8;
      if (var1 != null && this.mLegend.isEnabled() && !this.mLegend.isDrawInsideEnabled()) {
         label103: {
            label102: {
               label101: {
                  label100: {
                     var4 = Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent());
                     label97:
                     switch(this.mLegend.getOrientation()) {
                     case VERTICAL:
                        if (this.mLegend.getHorizontalAlignment() != Legend.LegendHorizontalAlignment.LEFT && this.mLegend.getHorizontalAlignment() != Legend.LegendHorizontalAlignment.RIGHT) {
                           var4 = 0.0F;
                        } else if (this.mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.CENTER) {
                           var4 += Utils.convertDpToPixel(13.0F);
                        } else {
                           var5 = var4 + Utils.convertDpToPixel(8.0F);
                           var2 = this.mLegend.mNeededHeight;
                           var6 = this.mLegend.mTextHeightMax;
                           MPPointF var7 = this.getCenter();
                           if (this.mLegend.getHorizontalAlignment() == Legend.LegendHorizontalAlignment.RIGHT) {
                              var4 = (float)this.getWidth() - var5 + 15.0F;
                           } else {
                              var4 = var5 - 15.0F;
                           }

                           var2 = var2 + var6 + 15.0F;
                           var6 = this.distanceToCenter(var4, var2);
                           MPPointF var11 = this.getPosition(var7, this.getRadius(), this.getAngleForPoint(var4, var2));
                           var4 = this.distanceToCenter(var11.x, var11.y);
                           var8 = Utils.convertDpToPixel(5.0F);
                           if (var2 >= var7.y && (float)this.getHeight() - var5 > (float)this.getWidth()) {
                              var4 = var5;
                           } else if (var6 < var4) {
                              var4 = var8 + (var4 - var6);
                           } else {
                              var4 = 0.0F;
                           }

                           MPPointF.recycleInstance(var7);
                           MPPointF.recycleInstance(var11);
                        }

                        switch(this.mLegend.getHorizontalAlignment()) {
                        case LEFT:
                           var5 = 0.0F;
                           var6 = var5;
                           var3 = var4;
                           var4 = var5;
                           break label103;
                        case RIGHT:
                           break label102;
                        case CENTER:
                           switch(this.mLegend.getVerticalAlignment()) {
                           case TOP:
                              var5 = Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                              break label100;
                           case BOTTOM:
                              var4 = Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                              break label101;
                           }
                        default:
                           break label97;
                        }
                     case HORIZONTAL:
                        if (this.mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.TOP || this.mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.BOTTOM) {
                           var4 = this.getRequiredLegendOffset();
                           var5 = Math.min(this.mLegend.mNeededHeight + var4, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                           var4 = var5;
                           switch(this.mLegend.getVerticalAlignment()) {
                           case TOP:
                              break label100;
                           case BOTTOM:
                              break label101;
                           }
                        }
                     }

                     var4 = 0.0F;
                     break label102;
                  }

                  var6 = var5;
                  var4 = 0.0F;
                  var5 = var4;
                  break label103;
               }

               var5 = var4;
               var4 = 0.0F;
               var6 = var4;
               break label103;
            }

            var5 = 0.0F;
            var6 = 0.0F;
         }

         var3 += this.getRequiredBaseOffset();
         var2 = var4 + this.getRequiredBaseOffset();
         var6 += this.getRequiredBaseOffset();
         var4 = var5 + this.getRequiredBaseOffset();
         var5 = var2;
      } else {
         var5 = 0.0F;
         var4 = var5;
         var6 = var5;
         var3 = var2;
      }

      var8 = Utils.convertDpToPixel(this.mMinOffset);
      var2 = var8;
      if (this instanceof RadarChart) {
         XAxis var12 = this.getXAxis();
         var2 = var8;
         if (var12.isEnabled()) {
            var2 = var8;
            if (var12.isDrawLabelsEnabled()) {
               var2 = Math.max(var8, (float)var12.mLabelRotatedWidth);
            }
         }
      }

      float var9 = this.getExtraTopOffset();
      float var10 = this.getExtraRightOffset();
      var8 = this.getExtraBottomOffset();
      var3 = Math.max(var2, var3 + this.getExtraLeftOffset());
      var6 = Math.max(var2, var6 + var9);
      var5 = Math.max(var2, var5 + var10);
      var4 = Math.max(var2, Math.max(this.getRequiredBaseOffset(), var4 + var8));
      this.mViewPortHandler.restrainViewPort(var3, var6, var5, var4);
      if (this.mLogEnabled) {
         StringBuilder var13 = new StringBuilder();
         var13.append("offsetLeft: ");
         var13.append(var3);
         var13.append(", offsetTop: ");
         var13.append(var6);
         var13.append(", offsetRight: ");
         var13.append(var5);
         var13.append(", offsetBottom: ");
         var13.append(var4);
         Log.i("MPAndroidChart", var13.toString());
      }

   }

   public void computeScroll() {
      if (this.mChartTouchListener instanceof PieRadarChartTouchListener) {
         ((PieRadarChartTouchListener)this.mChartTouchListener).computeScroll();
      }

   }

   public float distanceToCenter(float var1, float var2) {
      MPPointF var3 = this.getCenterOffsets();
      if (var1 > var3.x) {
         var1 -= var3.x;
      } else {
         var1 = var3.x - var1;
      }

      if (var2 > var3.y) {
         var2 -= var3.y;
      } else {
         var2 = var3.y - var2;
      }

      var1 = (float)Math.sqrt(Math.pow((double)var1, 2.0D) + Math.pow((double)var2, 2.0D));
      MPPointF.recycleInstance(var3);
      return var1;
   }

   public float getAngleForPoint(float var1, float var2) {
      MPPointF var3 = this.getCenterOffsets();
      double var4 = (double)(var1 - var3.x);
      double var6 = (double)(var2 - var3.y);
      float var8 = (float)Math.toDegrees(Math.acos(var6 / Math.sqrt(var4 * var4 + var6 * var6)));
      var2 = var8;
      if (var1 > var3.x) {
         var2 = 360.0F - var8;
      }

      var2 += 90.0F;
      var1 = var2;
      if (var2 > 360.0F) {
         var1 = var2 - 360.0F;
      }

      MPPointF.recycleInstance(var3);
      return var1;
   }

   public float getDiameter() {
      RectF var1 = this.mViewPortHandler.getContentRect();
      var1.left += this.getExtraLeftOffset();
      var1.top += this.getExtraTopOffset();
      var1.right -= this.getExtraRightOffset();
      var1.bottom -= this.getExtraBottomOffset();
      return Math.min(var1.width(), var1.height());
   }

   public abstract int getIndexForAngle(float var1);

   public int getMaxVisibleCount() {
      return this.mData.getEntryCount();
   }

   public float getMinOffset() {
      return this.mMinOffset;
   }

   public MPPointF getPosition(MPPointF var1, float var2, float var3) {
      MPPointF var4 = MPPointF.getInstance(0.0F, 0.0F);
      this.getPosition(var1, var2, var3, var4);
      return var4;
   }

   public void getPosition(MPPointF var1, float var2, float var3, MPPointF var4) {
      double var5 = (double)var1.x;
      double var7 = (double)var2;
      double var9 = (double)var3;
      var4.x = (float)(var5 + Math.cos(Math.toRadians(var9)) * var7);
      var4.y = (float)((double)var1.y + var7 * Math.sin(Math.toRadians(var9)));
   }

   public abstract float getRadius();

   public float getRawRotationAngle() {
      return this.mRawRotationAngle;
   }

   protected abstract float getRequiredBaseOffset();

   protected abstract float getRequiredLegendOffset();

   public float getRotationAngle() {
      return this.mRotationAngle;
   }

   public float getYChartMax() {
      return 0.0F;
   }

   public float getYChartMin() {
      return 0.0F;
   }

   protected void init() {
      super.init();
      this.mChartTouchListener = new PieRadarChartTouchListener(this);
   }

   public boolean isRotationEnabled() {
      return this.mRotateEnabled;
   }

   public void notifyDataSetChanged() {
      if (this.mData != null) {
         this.calcMinMax();
         if (this.mLegend != null) {
            this.mLegendRenderer.computeLegend(this.mData);
         }

         this.calculateOffsets();
      }
   }

   public boolean onTouchEvent(MotionEvent var1) {
      return this.mTouchEnabled && this.mChartTouchListener != null ? this.mChartTouchListener.onTouch(this, var1) : super.onTouchEvent(var1);
   }

   public void setMinOffset(float var1) {
      this.mMinOffset = var1;
   }

   public void setRotationAngle(float var1) {
      this.mRawRotationAngle = var1;
      this.mRotationAngle = Utils.getNormalizedAngle(this.mRawRotationAngle);
   }

   public void setRotationEnabled(boolean var1) {
      this.mRotateEnabled = var1;
   }

   @SuppressLint({"NewApi"})
   public void spin(int var1, float var2, float var3, Easing.EasingOption var4) {
      if (VERSION.SDK_INT >= 11) {
         this.setRotationAngle(var2);
         ObjectAnimator var5 = ObjectAnimator.ofFloat(this, "rotationAngle", new float[]{var2, var3});
         var5.setDuration((long)var1);
         var5.setInterpolator(Easing.getEasingFunctionFromOption(var4));
         var5.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator var1) {
               PieRadarChartBase.this.postInvalidate();
            }
         });
         var5.start();
      }
   }
}

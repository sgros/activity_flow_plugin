package com.github.mikephil.charting.utils;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.View;

public class ViewPortHandler {
   protected Matrix mCenterViewPortMatrixBuffer = new Matrix();
   protected float mChartHeight = 0.0F;
   protected float mChartWidth = 0.0F;
   protected RectF mContentRect = new RectF();
   protected final Matrix mMatrixTouch = new Matrix();
   private float mMaxScaleX = Float.MAX_VALUE;
   private float mMaxScaleY = Float.MAX_VALUE;
   private float mMinScaleX = 1.0F;
   private float mMinScaleY = 1.0F;
   private float mScaleX = 1.0F;
   private float mScaleY = 1.0F;
   private float mTransOffsetX = 0.0F;
   private float mTransOffsetY = 0.0F;
   private float mTransX = 0.0F;
   private float mTransY = 0.0F;
   protected final float[] matrixBuffer = new float[9];
   protected float[] valsBufferForFitScreen = new float[9];

   public boolean canZoomInMoreX() {
      boolean var1;
      if (this.mScaleX < this.mMaxScaleX) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean canZoomInMoreY() {
      boolean var1;
      if (this.mScaleY < this.mMaxScaleY) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean canZoomOutMoreX() {
      boolean var1;
      if (this.mScaleX > this.mMinScaleX) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean canZoomOutMoreY() {
      boolean var1;
      if (this.mScaleY > this.mMinScaleY) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void centerViewPort(float[] var1, View var2) {
      Matrix var3 = this.mCenterViewPortMatrixBuffer;
      var3.reset();
      var3.set(this.mMatrixTouch);
      float var4 = var1[0];
      float var5 = this.offsetLeft();
      float var6 = var1[1];
      float var7 = this.offsetTop();
      var3.postTranslate(-(var4 - var5), -(var6 - var7));
      this.refresh(var3, var2, true);
   }

   public float contentBottom() {
      return this.mContentRect.bottom;
   }

   public float contentHeight() {
      return this.mContentRect.height();
   }

   public float contentLeft() {
      return this.mContentRect.left;
   }

   public float contentRight() {
      return this.mContentRect.right;
   }

   public float contentTop() {
      return this.mContentRect.top;
   }

   public float contentWidth() {
      return this.mContentRect.width();
   }

   public Matrix fitScreen() {
      Matrix var1 = new Matrix();
      this.fitScreen(var1);
      return var1;
   }

   public void fitScreen(Matrix var1) {
      this.mMinScaleX = 1.0F;
      this.mMinScaleY = 1.0F;
      var1.set(this.mMatrixTouch);
      float[] var2 = this.valsBufferForFitScreen;

      for(int var3 = 0; var3 < 9; ++var3) {
         var2[var3] = 0.0F;
      }

      var1.getValues(var2);
      var2[2] = 0.0F;
      var2[5] = 0.0F;
      var2[0] = 1.0F;
      var2[4] = 1.0F;
      var1.setValues(var2);
   }

   public float getChartHeight() {
      return this.mChartHeight;
   }

   public float getChartWidth() {
      return this.mChartWidth;
   }

   public MPPointF getContentCenter() {
      return MPPointF.getInstance(this.mContentRect.centerX(), this.mContentRect.centerY());
   }

   public RectF getContentRect() {
      return this.mContentRect;
   }

   public Matrix getMatrixTouch() {
      return this.mMatrixTouch;
   }

   public float getMaxScaleX() {
      return this.mMaxScaleX;
   }

   public float getMaxScaleY() {
      return this.mMaxScaleY;
   }

   public float getMinScaleX() {
      return this.mMinScaleX;
   }

   public float getMinScaleY() {
      return this.mMinScaleY;
   }

   public float getScaleX() {
      return this.mScaleX;
   }

   public float getScaleY() {
      return this.mScaleY;
   }

   public float getSmallestContentExtension() {
      return Math.min(this.mContentRect.width(), this.mContentRect.height());
   }

   public float getTransX() {
      return this.mTransX;
   }

   public float getTransY() {
      return this.mTransY;
   }

   public boolean hasChartDimens() {
      return this.mChartHeight > 0.0F && this.mChartWidth > 0.0F;
   }

   public boolean hasNoDragOffset() {
      boolean var1;
      if (this.mTransOffsetX <= 0.0F && this.mTransOffsetY <= 0.0F) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isFullyZoomedOut() {
      boolean var1;
      if (this.isFullyZoomedOutX() && this.isFullyZoomedOutY()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isFullyZoomedOutX() {
      boolean var1;
      if (this.mScaleX <= this.mMinScaleX && this.mMinScaleX <= 1.0F) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isFullyZoomedOutY() {
      boolean var1;
      if (this.mScaleY <= this.mMinScaleY && this.mMinScaleY <= 1.0F) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isInBounds(float var1, float var2) {
      boolean var3;
      if (this.isInBoundsX(var1) && this.isInBoundsY(var2)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isInBoundsBottom(float var1) {
      var1 = (float)((int)(var1 * 100.0F)) / 100.0F;
      boolean var2;
      if (this.mContentRect.bottom >= var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isInBoundsLeft(float var1) {
      boolean var2;
      if (this.mContentRect.left <= var1 + 1.0F) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isInBoundsRight(float var1) {
      var1 = (float)((int)(var1 * 100.0F)) / 100.0F;
      boolean var2;
      if (this.mContentRect.right >= var1 - 1.0F) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isInBoundsTop(float var1) {
      boolean var2;
      if (this.mContentRect.top <= var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isInBoundsX(float var1) {
      boolean var2;
      if (this.isInBoundsLeft(var1) && this.isInBoundsRight(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isInBoundsY(float var1) {
      boolean var2;
      if (this.isInBoundsTop(var1) && this.isInBoundsBottom(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void limitTransAndScale(Matrix var1, RectF var2) {
      var1.getValues(this.matrixBuffer);
      float var3 = this.matrixBuffer[2];
      float var4 = this.matrixBuffer[0];
      float var5 = this.matrixBuffer[5];
      float var6 = this.matrixBuffer[4];
      this.mScaleX = Math.min(Math.max(this.mMinScaleX, var4), this.mMaxScaleX);
      this.mScaleY = Math.min(Math.max(this.mMinScaleY, var6), this.mMaxScaleY);
      var6 = 0.0F;
      if (var2 != null) {
         var6 = var2.width();
         var4 = var2.height();
      } else {
         var4 = 0.0F;
      }

      this.mTransX = Math.min(Math.max(var3, -var6 * (this.mScaleX - 1.0F) - this.mTransOffsetX), this.mTransOffsetX);
      this.mTransY = Math.max(Math.min(var5, var4 * (this.mScaleY - 1.0F) + this.mTransOffsetY), -this.mTransOffsetY);
      this.matrixBuffer[2] = this.mTransX;
      this.matrixBuffer[0] = this.mScaleX;
      this.matrixBuffer[5] = this.mTransY;
      this.matrixBuffer[4] = this.mScaleY;
      var1.setValues(this.matrixBuffer);
   }

   public float offsetBottom() {
      return this.mChartHeight - this.mContentRect.bottom;
   }

   public float offsetLeft() {
      return this.mContentRect.left;
   }

   public float offsetRight() {
      return this.mChartWidth - this.mContentRect.right;
   }

   public float offsetTop() {
      return this.mContentRect.top;
   }

   public Matrix refresh(Matrix var1, View var2, boolean var3) {
      this.mMatrixTouch.set(var1);
      this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
      if (var3) {
         var2.invalidate();
      }

      var1.set(this.mMatrixTouch);
      return var1;
   }

   public void resetZoom(Matrix var1) {
      var1.reset();
      var1.set(this.mMatrixTouch);
      var1.postScale(1.0F, 1.0F, 0.0F, 0.0F);
   }

   public void restrainViewPort(float var1, float var2, float var3, float var4) {
      this.mContentRect.set(var1, var2, this.mChartWidth - var3, this.mChartHeight - var4);
   }

   public void setChartDimens(float var1, float var2) {
      float var3 = this.offsetLeft();
      float var4 = this.offsetTop();
      float var5 = this.offsetRight();
      float var6 = this.offsetBottom();
      this.mChartHeight = var2;
      this.mChartWidth = var1;
      this.restrainViewPort(var3, var4, var5, var6);
   }

   public void setDragOffsetX(float var1) {
      this.mTransOffsetX = Utils.convertDpToPixel(var1);
   }

   public void setDragOffsetY(float var1) {
      this.mTransOffsetY = Utils.convertDpToPixel(var1);
   }

   public void setMaximumScaleX(float var1) {
      float var2 = var1;
      if (var1 == 0.0F) {
         var2 = Float.MAX_VALUE;
      }

      this.mMaxScaleX = var2;
      this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
   }

   public void setMaximumScaleY(float var1) {
      float var2 = var1;
      if (var1 == 0.0F) {
         var2 = Float.MAX_VALUE;
      }

      this.mMaxScaleY = var2;
      this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
   }

   public void setMinMaxScaleX(float var1, float var2) {
      float var3 = var1;
      if (var1 < 1.0F) {
         var3 = 1.0F;
      }

      var1 = var2;
      if (var2 == 0.0F) {
         var1 = Float.MAX_VALUE;
      }

      this.mMinScaleX = var3;
      this.mMaxScaleX = var1;
      this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
   }

   public void setMinMaxScaleY(float var1, float var2) {
      float var3 = var1;
      if (var1 < 1.0F) {
         var3 = 1.0F;
      }

      var1 = var2;
      if (var2 == 0.0F) {
         var1 = Float.MAX_VALUE;
      }

      this.mMinScaleY = var3;
      this.mMaxScaleY = var1;
      this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
   }

   public void setMinimumScaleX(float var1) {
      float var2 = var1;
      if (var1 < 1.0F) {
         var2 = 1.0F;
      }

      this.mMinScaleX = var2;
      this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
   }

   public void setMinimumScaleY(float var1) {
      float var2 = var1;
      if (var1 < 1.0F) {
         var2 = 1.0F;
      }

      this.mMinScaleY = var2;
      this.limitTransAndScale(this.mMatrixTouch, this.mContentRect);
   }

   public Matrix setZoom(float var1, float var2) {
      Matrix var3 = new Matrix();
      this.setZoom(var1, var2, var3);
      return var3;
   }

   public Matrix setZoom(float var1, float var2, float var3, float var4) {
      Matrix var5 = new Matrix();
      var5.set(this.mMatrixTouch);
      var5.setScale(var1, var2, var3, var4);
      return var5;
   }

   public void setZoom(float var1, float var2, Matrix var3) {
      var3.reset();
      var3.set(this.mMatrixTouch);
      var3.setScale(var1, var2);
   }

   public Matrix translate(float[] var1) {
      Matrix var2 = new Matrix();
      this.translate(var1, var2);
      return var2;
   }

   public void translate(float[] var1, Matrix var2) {
      var2.reset();
      var2.set(this.mMatrixTouch);
      float var3 = var1[0];
      float var4 = this.offsetLeft();
      float var5 = var1[1];
      float var6 = this.offsetTop();
      var2.postTranslate(-(var3 - var4), -(var5 - var6));
   }

   public Matrix zoom(float var1, float var2) {
      Matrix var3 = new Matrix();
      this.zoom(var1, var2, var3);
      return var3;
   }

   public Matrix zoom(float var1, float var2, float var3, float var4) {
      Matrix var5 = new Matrix();
      this.zoom(var1, var2, var3, var4, var5);
      return var5;
   }

   public void zoom(float var1, float var2, float var3, float var4, Matrix var5) {
      var5.reset();
      var5.set(this.mMatrixTouch);
      var5.postScale(var1, var2, var3, var4);
   }

   public void zoom(float var1, float var2, Matrix var3) {
      var3.reset();
      var3.set(this.mMatrixTouch);
      var3.postScale(var1, var2);
   }

   public Matrix zoomIn(float var1, float var2) {
      Matrix var3 = new Matrix();
      this.zoomIn(var1, var2, var3);
      return var3;
   }

   public void zoomIn(float var1, float var2, Matrix var3) {
      var3.reset();
      var3.set(this.mMatrixTouch);
      var3.postScale(1.4F, 1.4F, var1, var2);
   }

   public Matrix zoomOut(float var1, float var2) {
      Matrix var3 = new Matrix();
      this.zoomOut(var1, var2, var3);
      return var3;
   }

   public void zoomOut(float var1, float var2, Matrix var3) {
      var3.reset();
      var3.set(this.mMatrixTouch);
      var3.postScale(0.7F, 0.7F, var1, var2);
   }
}

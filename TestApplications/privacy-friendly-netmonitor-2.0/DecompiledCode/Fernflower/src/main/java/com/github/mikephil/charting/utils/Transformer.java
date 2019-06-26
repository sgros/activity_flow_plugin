package com.github.mikephil.charting.utils;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import java.util.List;

public class Transformer {
   private Matrix mMBuffer1 = new Matrix();
   private Matrix mMBuffer2 = new Matrix();
   protected Matrix mMatrixOffset = new Matrix();
   protected Matrix mMatrixValueToPx = new Matrix();
   protected Matrix mPixelToValueMatrixBuffer = new Matrix();
   protected ViewPortHandler mViewPortHandler;
   float[] ptsBuffer = new float[2];
   protected float[] valuePointsForGenerateTransformedValuesBubble = new float[1];
   protected float[] valuePointsForGenerateTransformedValuesCandle = new float[1];
   protected float[] valuePointsForGenerateTransformedValuesLine = new float[1];
   protected float[] valuePointsForGenerateTransformedValuesScatter = new float[1];

   public Transformer(ViewPortHandler var1) {
      this.mViewPortHandler = var1;
   }

   public float[] generateTransformedValuesBubble(IBubbleDataSet var1, float var2, int var3, int var4) {
      int var5 = (var4 - var3 + 1) * 2;
      if (this.valuePointsForGenerateTransformedValuesBubble.length != var5) {
         this.valuePointsForGenerateTransformedValuesBubble = new float[var5];
      }

      float[] var6 = this.valuePointsForGenerateTransformedValuesBubble;

      for(var4 = 0; var4 < var5; var4 += 2) {
         Entry var7 = var1.getEntryForIndex(var4 / 2 + var3);
         if (var7 != null) {
            var6[var4] = var7.getX();
            var6[var4 + 1] = var7.getY() * var2;
         } else {
            var6[var4] = 0.0F;
            var6[var4 + 1] = 0.0F;
         }
      }

      this.getValueToPixelMatrix().mapPoints(var6);
      return var6;
   }

   public float[] generateTransformedValuesCandle(ICandleDataSet var1, float var2, float var3, int var4, int var5) {
      int var6 = (int)((float)(var5 - var4) * var2 + 1.0F) * 2;
      if (this.valuePointsForGenerateTransformedValuesCandle.length != var6) {
         this.valuePointsForGenerateTransformedValuesCandle = new float[var6];
      }

      float[] var7 = this.valuePointsForGenerateTransformedValuesCandle;

      for(var5 = 0; var5 < var6; var5 += 2) {
         CandleEntry var8 = (CandleEntry)var1.getEntryForIndex(var5 / 2 + var4);
         if (var8 != null) {
            var7[var5] = var8.getX();
            var7[var5 + 1] = var8.getHigh() * var3;
         } else {
            var7[var5] = 0.0F;
            var7[var5 + 1] = 0.0F;
         }
      }

      this.getValueToPixelMatrix().mapPoints(var7);
      return var7;
   }

   public float[] generateTransformedValuesLine(ILineDataSet var1, float var2, float var3, int var4, int var5) {
      int var6 = ((int)((float)(var5 - var4) * var2) + 1) * 2;
      if (this.valuePointsForGenerateTransformedValuesLine.length != var6) {
         this.valuePointsForGenerateTransformedValuesLine = new float[var6];
      }

      float[] var7 = this.valuePointsForGenerateTransformedValuesLine;

      for(var5 = 0; var5 < var6; var5 += 2) {
         Entry var8 = var1.getEntryForIndex(var5 / 2 + var4);
         if (var8 != null) {
            var7[var5] = var8.getX();
            var7[var5 + 1] = var8.getY() * var3;
         } else {
            var7[var5] = 0.0F;
            var7[var5 + 1] = 0.0F;
         }
      }

      this.getValueToPixelMatrix().mapPoints(var7);
      return var7;
   }

   public float[] generateTransformedValuesScatter(IScatterDataSet var1, float var2, float var3, int var4, int var5) {
      int var6 = (int)((float)(var5 - var4) * var2 + 1.0F) * 2;
      if (this.valuePointsForGenerateTransformedValuesScatter.length != var6) {
         this.valuePointsForGenerateTransformedValuesScatter = new float[var6];
      }

      float[] var7 = this.valuePointsForGenerateTransformedValuesScatter;

      for(var5 = 0; var5 < var6; var5 += 2) {
         Entry var8 = var1.getEntryForIndex(var5 / 2 + var4);
         if (var8 != null) {
            var7[var5] = var8.getX();
            var7[var5 + 1] = var8.getY() * var3;
         } else {
            var7[var5] = 0.0F;
            var7[var5 + 1] = 0.0F;
         }
      }

      this.getValueToPixelMatrix().mapPoints(var7);
      return var7;
   }

   public Matrix getOffsetMatrix() {
      return this.mMatrixOffset;
   }

   public MPPointD getPixelForValues(float var1, float var2) {
      this.ptsBuffer[0] = var1;
      this.ptsBuffer[1] = var2;
      this.pointValuesToPixel(this.ptsBuffer);
      return MPPointD.getInstance((double)this.ptsBuffer[0], (double)this.ptsBuffer[1]);
   }

   public Matrix getPixelToValueMatrix() {
      this.getValueToPixelMatrix().invert(this.mMBuffer2);
      return this.mMBuffer2;
   }

   public Matrix getValueMatrix() {
      return this.mMatrixValueToPx;
   }

   public Matrix getValueToPixelMatrix() {
      this.mMBuffer1.set(this.mMatrixValueToPx);
      this.mMBuffer1.postConcat(this.mViewPortHandler.mMatrixTouch);
      this.mMBuffer1.postConcat(this.mMatrixOffset);
      return this.mMBuffer1;
   }

   public MPPointD getValuesByTouchPoint(float var1, float var2) {
      MPPointD var3 = MPPointD.getInstance(0.0D, 0.0D);
      this.getValuesByTouchPoint(var1, var2, var3);
      return var3;
   }

   public void getValuesByTouchPoint(float var1, float var2, MPPointD var3) {
      this.ptsBuffer[0] = var1;
      this.ptsBuffer[1] = var2;
      this.pixelsToValue(this.ptsBuffer);
      var3.x = (double)this.ptsBuffer[0];
      var3.y = (double)this.ptsBuffer[1];
   }

   public void pathValueToPixel(Path var1) {
      var1.transform(this.mMatrixValueToPx);
      var1.transform(this.mViewPortHandler.getMatrixTouch());
      var1.transform(this.mMatrixOffset);
   }

   public void pathValuesToPixel(List var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.pathValueToPixel((Path)var1.get(var2));
      }

   }

   public void pixelsToValue(float[] var1) {
      Matrix var2 = this.mPixelToValueMatrixBuffer;
      var2.reset();
      this.mMatrixOffset.invert(var2);
      var2.mapPoints(var1);
      this.mViewPortHandler.getMatrixTouch().invert(var2);
      var2.mapPoints(var1);
      this.mMatrixValueToPx.invert(var2);
      var2.mapPoints(var1);
   }

   public void pointValuesToPixel(float[] var1) {
      this.mMatrixValueToPx.mapPoints(var1);
      this.mViewPortHandler.getMatrixTouch().mapPoints(var1);
      this.mMatrixOffset.mapPoints(var1);
   }

   public void prepareMatrixOffset(boolean var1) {
      this.mMatrixOffset.reset();
      if (!var1) {
         this.mMatrixOffset.postTranslate(this.mViewPortHandler.offsetLeft(), this.mViewPortHandler.getChartHeight() - this.mViewPortHandler.offsetBottom());
      } else {
         this.mMatrixOffset.setTranslate(this.mViewPortHandler.offsetLeft(), -this.mViewPortHandler.offsetTop());
         this.mMatrixOffset.postScale(1.0F, -1.0F);
      }

   }

   public void prepareMatrixValuePx(float var1, float var2, float var3, float var4) {
      float var5 = this.mViewPortHandler.contentWidth() / var2;
      float var6 = this.mViewPortHandler.contentHeight() / var3;
      var2 = var5;
      if (Float.isInfinite(var5)) {
         var2 = 0.0F;
      }

      var3 = var6;
      if (Float.isInfinite(var6)) {
         var3 = 0.0F;
      }

      this.mMatrixValueToPx.reset();
      this.mMatrixValueToPx.postTranslate(-var1, -var4);
      this.mMatrixValueToPx.postScale(var2, -var3);
   }

   public void rectToPixelPhase(RectF var1, float var2) {
      var1.top *= var2;
      var1.bottom *= var2;
      this.mMatrixValueToPx.mapRect(var1);
      this.mViewPortHandler.getMatrixTouch().mapRect(var1);
      this.mMatrixOffset.mapRect(var1);
   }

   public void rectToPixelPhaseHorizontal(RectF var1, float var2) {
      var1.left *= var2;
      var1.right *= var2;
      this.mMatrixValueToPx.mapRect(var1);
      this.mViewPortHandler.getMatrixTouch().mapRect(var1);
      this.mMatrixOffset.mapRect(var1);
   }

   public void rectValueToPixel(RectF var1) {
      this.mMatrixValueToPx.mapRect(var1);
      this.mViewPortHandler.getMatrixTouch().mapRect(var1);
      this.mMatrixOffset.mapRect(var1);
   }

   public void rectValueToPixelHorizontal(RectF var1) {
      this.mMatrixValueToPx.mapRect(var1);
      this.mViewPortHandler.getMatrixTouch().mapRect(var1);
      this.mMatrixOffset.mapRect(var1);
   }

   public void rectValueToPixelHorizontal(RectF var1, float var2) {
      var1.left *= var2;
      var1.right *= var2;
      this.mMatrixValueToPx.mapRect(var1);
      this.mViewPortHandler.getMatrixTouch().mapRect(var1);
      this.mMatrixOffset.mapRect(var1);
   }

   public void rectValuesToPixel(List var1) {
      Matrix var2 = this.getValueToPixelMatrix();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         var2.mapRect((RectF)var1.get(var3));
      }

   }
}

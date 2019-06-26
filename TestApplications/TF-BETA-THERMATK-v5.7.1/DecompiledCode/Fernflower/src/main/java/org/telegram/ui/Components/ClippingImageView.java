package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Shader.TileMode;
import android.view.View;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;

public class ClippingImageView extends View {
   private float animationProgress;
   private float[][] animationValues;
   private RectF bitmapRect;
   private BitmapShader bitmapShader;
   private ImageReceiver.BitmapHolder bmp;
   private int clipBottom;
   private int clipLeft;
   private int clipRight;
   private int clipTop;
   private RectF drawRect;
   private int imageX;
   private int imageY;
   private Matrix matrix;
   private boolean needRadius;
   private int orientation;
   private Paint paint = new Paint(2);
   private int radius;
   private Paint roundPaint;
   private RectF roundRect;
   private Matrix shaderMatrix;

   public ClippingImageView(Context var1) {
      super(var1);
      this.paint.setFilterBitmap(true);
      this.matrix = new Matrix();
      this.drawRect = new RectF();
      this.bitmapRect = new RectF();
      this.roundPaint = new Paint(3);
      this.roundRect = new RectF();
      this.shaderMatrix = new Matrix();
   }

   @Keep
   public float getAnimationProgress() {
      return this.animationProgress;
   }

   public Bitmap getBitmap() {
      ImageReceiver.BitmapHolder var1 = this.bmp;
      Bitmap var2;
      if (var1 != null) {
         var2 = var1.bitmap;
      } else {
         var2 = null;
      }

      return var2;
   }

   public int getClipBottom() {
      return this.clipBottom;
   }

   public int getClipHorizontal() {
      return this.clipRight;
   }

   public int getClipLeft() {
      return this.clipLeft;
   }

   public int getClipRight() {
      return this.clipRight;
   }

   public int getClipTop() {
      return this.clipTop;
   }

   public int getOrientation() {
      return this.orientation;
   }

   public int getRadius() {
      return this.radius;
   }

   public void onDraw(Canvas var1) {
      if (this.getVisibility() == 0) {
         ImageReceiver.BitmapHolder var2 = this.bmp;
         if (var2 != null && !var2.isRecycled()) {
            float var3 = this.getScaleY();
            var1.save();
            int var4;
            if (this.needRadius) {
               this.shaderMatrix.reset();
               this.roundRect.set((float)this.imageX / var3, (float)this.imageY / var3, (float)this.getWidth() - (float)this.imageX / var3, (float)this.getHeight() - (float)this.imageY / var3);
               this.bitmapRect.set(0.0F, 0.0F, (float)this.bmp.getWidth(), (float)this.bmp.getHeight());
               AndroidUtilities.setRectToRect(this.shaderMatrix, this.bitmapRect, this.roundRect, this.orientation, false);
               this.bitmapShader.setLocalMatrix(this.shaderMatrix);
               var1.clipRect((float)this.clipLeft / var3, (float)this.clipTop / var3, (float)this.getWidth() - (float)this.clipRight / var3, (float)this.getHeight() - (float)this.clipBottom / var3);
               RectF var6 = this.roundRect;
               var4 = this.radius;
               var1.drawRoundRect(var6, (float)var4, (float)var4, this.roundPaint);
            } else {
               var4 = this.orientation;
               if (var4 != 90 && var4 != 270) {
                  if (var4 == 180) {
                     this.drawRect.set((float)(-this.getWidth() / 2), (float)(-this.getHeight() / 2), (float)(this.getWidth() / 2), (float)(this.getHeight() / 2));
                     this.matrix.setRectToRect(this.bitmapRect, this.drawRect, ScaleToFit.FILL);
                     this.matrix.postRotate((float)this.orientation, 0.0F, 0.0F);
                     this.matrix.postTranslate((float)(this.getWidth() / 2), (float)(this.getHeight() / 2));
                  } else {
                     this.drawRect.set(0.0F, 0.0F, (float)this.getWidth(), (float)this.getHeight());
                     this.matrix.setRectToRect(this.bitmapRect, this.drawRect, ScaleToFit.FILL);
                  }
               } else {
                  this.drawRect.set((float)(-this.getHeight() / 2), (float)(-this.getWidth() / 2), (float)(this.getHeight() / 2), (float)(this.getWidth() / 2));
                  this.matrix.setRectToRect(this.bitmapRect, this.drawRect, ScaleToFit.FILL);
                  this.matrix.postRotate((float)this.orientation, 0.0F, 0.0F);
                  this.matrix.postTranslate((float)(this.getWidth() / 2), (float)(this.getHeight() / 2));
               }

               var1.clipRect((float)this.clipLeft / var3, (float)this.clipTop / var3, (float)this.getWidth() - (float)this.clipRight / var3, (float)this.getHeight() - (float)this.clipBottom / var3);

               try {
                  var1.drawBitmap(this.bmp.bitmap, this.matrix, this.paint);
               } catch (Exception var5) {
                  FileLog.e((Throwable)var5);
               }
            }

            var1.restore();
         }

      }
   }

   @Keep
   public void setAnimationProgress(float var1) {
      this.animationProgress = var1;
      float[][] var2 = this.animationValues;
      this.setScaleX(var2[0][0] + (var2[1][0] - var2[0][0]) * this.animationProgress);
      var2 = this.animationValues;
      this.setScaleY(var2[0][1] + (var2[1][1] - var2[0][1]) * this.animationProgress);
      var2 = this.animationValues;
      this.setTranslationX(var2[0][2] + (var2[1][2] - var2[0][2]) * this.animationProgress);
      var2 = this.animationValues;
      this.setTranslationY(var2[0][3] + (var2[1][3] - var2[0][3]) * this.animationProgress);
      var2 = this.animationValues;
      this.setClipHorizontal((int)(var2[0][4] + (var2[1][4] - var2[0][4]) * this.animationProgress));
      var2 = this.animationValues;
      this.setClipTop((int)(var2[0][5] + (var2[1][5] - var2[0][5]) * this.animationProgress));
      var2 = this.animationValues;
      this.setClipBottom((int)(var2[0][6] + (var2[1][6] - var2[0][6]) * this.animationProgress));
      var2 = this.animationValues;
      this.setRadius((int)(var2[0][7] + (var2[1][7] - var2[0][7]) * this.animationProgress));
      var2 = this.animationValues;
      if (var2[0].length > 8) {
         this.setImageY((int)(var2[0][8] + (var2[1][8] - var2[0][8]) * this.animationProgress));
         var2 = this.animationValues;
         this.setImageX((int)(var2[0][9] + (var2[1][9] - var2[0][9]) * this.animationProgress));
      }

      this.invalidate();
   }

   public void setAnimationValues(float[][] var1) {
      this.animationValues = var1;
   }

   public void setClipBottom(int var1) {
      this.clipBottom = var1;
      this.invalidate();
   }

   public void setClipHorizontal(int var1) {
      this.clipRight = var1;
      this.clipLeft = var1;
      this.invalidate();
   }

   public void setClipLeft(int var1) {
      this.clipLeft = var1;
      this.invalidate();
   }

   public void setClipRight(int var1) {
      this.clipRight = var1;
      this.invalidate();
   }

   public void setClipTop(int var1) {
      this.clipTop = var1;
      this.invalidate();
   }

   public void setClipVertical(int var1) {
      this.clipBottom = var1;
      this.clipTop = var1;
      this.invalidate();
   }

   public void setImageBitmap(ImageReceiver.BitmapHolder var1) {
      ImageReceiver.BitmapHolder var2 = this.bmp;
      if (var2 != null) {
         var2.release();
         this.bitmapShader = null;
      }

      this.bmp = var1;
      if (var1 != null && var1.bitmap != null) {
         this.bitmapRect.set(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight());
         if (this.needRadius) {
            Bitmap var4 = this.bmp.bitmap;
            TileMode var3 = TileMode.CLAMP;
            this.bitmapShader = new BitmapShader(var4, var3, var3);
            this.roundPaint.setShader(this.bitmapShader);
         }
      }

      this.invalidate();
   }

   public void setImageX(int var1) {
      this.imageX = var1;
   }

   public void setImageY(int var1) {
      this.imageY = var1;
   }

   public void setNeedRadius(boolean var1) {
      this.needRadius = var1;
   }

   public void setOrientation(int var1) {
      this.orientation = var1;
   }

   public void setRadius(int var1) {
      this.radius = var1;
   }
}

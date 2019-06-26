package org.telegram.ui.Components.Crop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;

public class CropAreaView extends View {
   private CropAreaView.Control activeControl;
   private RectF actualRect = new RectF();
   private Animator animator;
   private RectF bottomEdge = new RectF();
   private RectF bottomLeftCorner = new RectF();
   private float bottomPadding;
   private RectF bottomRightCorner = new RectF();
   private Bitmap circleBitmap;
   Paint dimPaint;
   private boolean dimVisibile = true;
   private Paint eraserPaint;
   Paint framePaint;
   private boolean frameVisible = true;
   private boolean freeform = true;
   private Animator gridAnimator;
   private float gridProgress;
   private CropAreaView.GridType gridType;
   Paint handlePaint;
   AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
   private boolean isDragging;
   private RectF leftEdge = new RectF();
   Paint linePaint;
   private CropAreaView.AreaViewListener listener;
   private float lockAspectRatio;
   private float minWidth = (float)AndroidUtilities.dp(32.0F);
   private CropAreaView.GridType previousGridType;
   private int previousX;
   private int previousY;
   private RectF rightEdge = new RectF();
   Paint shadowPaint;
   private float sidePadding = (float)AndroidUtilities.dp(16.0F);
   private RectF tempRect = new RectF();
   private RectF topEdge = new RectF();
   private RectF topLeftCorner = new RectF();
   private RectF topRightCorner = new RectF();

   public CropAreaView(Context var1) {
      super(var1);
      this.gridType = CropAreaView.GridType.NONE;
      this.dimPaint = new Paint();
      this.dimPaint.setColor(-872415232);
      this.shadowPaint = new Paint();
      this.shadowPaint.setStyle(Style.FILL);
      this.shadowPaint.setColor(436207616);
      this.shadowPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.linePaint = new Paint();
      this.linePaint.setStyle(Style.FILL);
      this.linePaint.setColor(-1);
      this.linePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0F));
      this.handlePaint = new Paint();
      this.handlePaint.setStyle(Style.FILL);
      this.handlePaint.setColor(-1);
      this.framePaint = new Paint();
      this.framePaint.setStyle(Style.FILL);
      this.framePaint.setColor(-1291845633);
      this.eraserPaint = new Paint(1);
      this.eraserPaint.setColor(0);
      this.eraserPaint.setStyle(Style.FILL);
      this.eraserPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
   }

   private void constrainRectByHeight(RectF var1, float var2) {
      float var3 = var1.height();
      var1.right = var1.left + var2 * var3;
      var1.bottom = var1.top + var3;
   }

   private void constrainRectByWidth(RectF var1, float var2) {
      float var3 = var1.width();
      var2 = var3 / var2;
      var1.right = var1.left + var3;
      var1.bottom = var1.top + var2;
   }

   private float getGridProgress() {
      return this.gridProgress;
   }

   @Keep
   private void setCropBottom(float var1) {
      this.actualRect.bottom = var1;
      this.invalidate();
   }

   @Keep
   private void setCropLeft(float var1) {
      this.actualRect.left = var1;
      this.invalidate();
   }

   @Keep
   private void setCropRight(float var1) {
      this.actualRect.right = var1;
      this.invalidate();
   }

   @Keep
   private void setCropTop(float var1) {
      this.actualRect.top = var1;
      this.invalidate();
   }

   @Keep
   private void setGridProgress(float var1) {
      this.gridProgress = var1;
      this.invalidate();
   }

   private void updateTouchAreas() {
      int var1 = AndroidUtilities.dp(16.0F);
      RectF var2 = this.topLeftCorner;
      RectF var3 = this.actualRect;
      float var4 = var3.left;
      float var5 = (float)var1;
      float var6 = var3.top;
      var2.set(var4 - var5, var6 - var5, var4 + var5, var6 + var5);
      var3 = this.topRightCorner;
      var2 = this.actualRect;
      var6 = var2.right;
      var4 = var2.top;
      var3.set(var6 - var5, var4 - var5, var6 + var5, var4 + var5);
      var2 = this.bottomLeftCorner;
      var3 = this.actualRect;
      var6 = var3.left;
      var4 = var3.bottom;
      var2.set(var6 - var5, var4 - var5, var6 + var5, var4 + var5);
      var2 = this.bottomRightCorner;
      var3 = this.actualRect;
      var6 = var3.right;
      var4 = var3.bottom;
      var2.set(var6 - var5, var4 - var5, var6 + var5, var4 + var5);
      var2 = this.topEdge;
      var3 = this.actualRect;
      var6 = var3.left;
      var4 = var3.top;
      var2.set(var6 + var5, var4 - var5, var3.right - var5, var4 + var5);
      var3 = this.leftEdge;
      var2 = this.actualRect;
      var6 = var2.left;
      var3.set(var6 - var5, var2.top + var5, var6 + var5, var2.bottom - var5);
      var2 = this.rightEdge;
      var3 = this.actualRect;
      var6 = var3.right;
      var2.set(var6 - var5, var3.top + var5, var6 + var5, var3.bottom - var5);
      var3 = this.bottomEdge;
      var2 = this.actualRect;
      var4 = var2.left;
      var6 = var2.bottom;
      var3.set(var4 + var5, var6 - var5, var2.right - var5, var6 + var5);
   }

   public void calculateRect(RectF var1, float var2) {
      int var3;
      if (VERSION.SDK_INT >= 21) {
         var3 = AndroidUtilities.statusBarHeight;
      } else {
         var3 = 0;
      }

      float var4 = (float)var3;
      float var5 = (float)this.getMeasuredHeight() - this.bottomPadding - var4;
      float var6 = (float)this.getMeasuredWidth() / var5;
      float var7 = Math.min((float)this.getMeasuredWidth(), var5);
      float var8 = this.sidePadding;
      float var9 = (float)this.getMeasuredWidth();
      float var10 = this.sidePadding;
      var9 -= var10 * 2.0F;
      float var11 = var5 - var10 * 2.0F;
      var10 = (float)this.getMeasuredWidth() / 2.0F;
      var4 += var5 / 2.0F;
      if ((double)Math.abs(1.0F - var2) < 1.0E-4D) {
         var2 = (var7 - var8 * 2.0F) / 2.0F;
         var9 = var10 - var2;
         var6 = var4 - var2;
         var8 = var10 + var2;
         var2 += var4;
      } else if (var2 > var6) {
         var7 = var9 / 2.0F;
         var6 = var9 / var2 / 2.0F;
         var8 = var10 + var7;
         var2 = var4 + var6;
         var6 = var4 - var6;
         var9 = var10 - var7;
      } else {
         var9 = var2 * var11 / 2.0F;
         var6 = var11 / 2.0F;
         var8 = var10 + var9;
         var2 = var4 + var6;
         var9 = var10 - var9;
         var6 = var4 - var6;
      }

      var1.set(var9, var6, var8, var2);
   }

   public void fill(final RectF var1, Animator var2, boolean var3) {
      if (var3) {
         Animator var4 = this.animator;
         if (var4 != null) {
            var4.cancel();
            this.animator = null;
         }

         AnimatorSet var5 = new AnimatorSet();
         this.animator = var5;
         var5.setDuration(300L);
         Animator[] var6 = new Animator[5];
         var6[0] = ObjectAnimator.ofFloat(this, "cropLeft", new float[]{var1.left});
         var6[0].setInterpolator(this.interpolator);
         var6[1] = ObjectAnimator.ofFloat(this, "cropTop", new float[]{var1.top});
         var6[1].setInterpolator(this.interpolator);
         var6[2] = ObjectAnimator.ofFloat(this, "cropRight", new float[]{var1.right});
         var6[2].setInterpolator(this.interpolator);
         var6[3] = ObjectAnimator.ofFloat(this, "cropBottom", new float[]{var1.bottom});
         var6[3].setInterpolator(this.interpolator);
         var6[4] = var2;
         var6[4].setInterpolator(this.interpolator);
         var5.playTogether(var6);
         var5.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1x) {
               CropAreaView.this.setActualRect(var1);
               CropAreaView.this.animator = null;
            }
         });
         var5.start();
      } else {
         this.setActualRect(var1);
      }

   }

   public float getAspectRatio() {
      RectF var1 = this.actualRect;
      return (var1.right - var1.left) / (var1.bottom - var1.top);
   }

   public float getCropBottom() {
      return this.actualRect.bottom;
   }

   public float getCropCenterX() {
      RectF var1 = this.actualRect;
      float var2 = var1.left;
      return var2 + (var1.right - var2) / 2.0F;
   }

   public float getCropCenterY() {
      RectF var1 = this.actualRect;
      float var2 = var1.top;
      return var2 + (var1.bottom - var2) / 2.0F;
   }

   public float getCropHeight() {
      RectF var1 = this.actualRect;
      return var1.bottom - var1.top;
   }

   public float getCropLeft() {
      return this.actualRect.left;
   }

   public void getCropRect(RectF var1) {
      var1.set(this.actualRect);
   }

   public float getCropRight() {
      return this.actualRect.right;
   }

   public float getCropTop() {
      return this.actualRect.top;
   }

   public float getCropWidth() {
      RectF var1 = this.actualRect;
      return var1.right - var1.left;
   }

   public Interpolator getInterpolator() {
      return this.interpolator;
   }

   public float getLockAspectRatio() {
      return this.lockAspectRatio;
   }

   public RectF getTargetRectToFill() {
      RectF var1 = new RectF();
      this.calculateRect(var1, this.getAspectRatio());
      return var1;
   }

   public boolean isDragging() {
      return this.isDragging;
   }

   protected void onDraw(Canvas var1) {
      RectF var5;
      if (this.freeform) {
         int var2 = AndroidUtilities.dp(2.0F);
         int var3 = AndroidUtilities.dp(16.0F);
         int var4 = AndroidUtilities.dp(3.0F);
         var5 = this.actualRect;
         float var6 = var5.left;
         int var7 = (int)var6 - var2;
         float var8 = var5.top;
         int var9 = (int)var8 - var2;
         int var10 = (int)(var5.right - var6);
         int var11 = var2 * 2;
         var10 += var11;
         var11 += (int)(var5.bottom - var8);
         float var12;
         if (this.dimVisibile) {
            var6 = (float)this.getWidth();
            var8 = (float)(var9 + var2);
            var1.drawRect(0.0F, 0.0F, var6, var8, this.dimPaint);
            var12 = (float)(var7 + var2);
            var6 = (float)(var9 + var11 - var2);
            var1.drawRect(0.0F, var8, var12, var6, this.dimPaint);
            var1.drawRect((float)(var7 + var10 - var2), var8, (float)this.getWidth(), var6, this.dimPaint);
            var1.drawRect(0.0F, var6, (float)this.getWidth(), (float)this.getHeight(), this.dimPaint);
         }

         if (!this.frameVisible) {
            return;
         }

         int var13 = var4 - var2;
         int var14 = var4 * 2;
         int var15 = var10 - var14;
         int var16 = var11 - var14;
         CropAreaView.GridType var17 = this.gridType;
         CropAreaView.GridType var28 = var17;
         if (var17 == CropAreaView.GridType.NONE) {
            var28 = var17;
            if (this.gridProgress > 0.0F) {
               var28 = this.previousGridType;
            }
         }

         this.shadowPaint.setAlpha((int)(this.gridProgress * 26.0F));
         this.linePaint.setAlpha((int)(this.gridProgress * 178.0F));

         int var18;
         int var19;
         for(var14 = 0; var14 < 3; var14 = var18) {
            int var20;
            int var21;
            if (var28 != CropAreaView.GridType.MINOR) {
               var18 = var2;
               var19 = var3;
               var20 = var11;
               var21 = var10;
               var11 = var3;
               var3 = var20;
               var10 = var2;
               var2 = var21;
               if (var28 == CropAreaView.GridType.MAJOR) {
                  var11 = var19;
                  var3 = var20;
                  var10 = var18;
                  var2 = var21;
                  if (var14 > 0) {
                     var2 = var7 + var4;
                     var8 = (float)(var15 / 3 * var14 + var2);
                     var3 = var9 + var4;
                     var6 = (float)var3;
                     var12 = (float)(var3 + var16);
                     var1.drawLine(var8, var6, var8, var12, this.shadowPaint);
                     var1.drawLine(var8, var6, var8, var12, this.linePaint);
                     var6 = (float)var2;
                     var8 = (float)(var3 + var16 / 3 * var14);
                     var12 = (float)(var2 + var15);
                     var1.drawLine(var6, var8, var12, var8, this.shadowPaint);
                     var1.drawLine(var6, var8, var12, var8, this.linePaint);
                     var2 = var21;
                     var10 = var18;
                     var3 = var20;
                     var11 = var19;
                  }
               }
            } else {
               var18 = 1;

               while(true) {
                  if (var18 >= 4) {
                     var19 = var11;
                     var18 = var10;
                     var11 = var3;
                     var3 = var19;
                     var10 = var2;
                     var2 = var18;
                     break;
                  }

                  if (var14 != 2 || var18 != 3) {
                     var19 = var7 + var4;
                     var20 = var15 / 3;
                     var8 = (float)(var19 + var20 / 3 * var18 + var20 * var14);
                     var20 = var9 + var4;
                     var12 = (float)var20;
                     var6 = (float)(var20 + var16);
                     var1.drawLine(var8, var12, var8, var6, this.shadowPaint);
                     var1.drawLine(var8, var12, var8, var6, this.linePaint);
                     var6 = (float)var19;
                     var21 = var16 / 3;
                     var12 = (float)(var20 + var21 / 3 * var18 + var21 * var14);
                     var8 = (float)(var19 + var15);
                     var1.drawLine(var6, var12, var8, var12, this.shadowPaint);
                     var1.drawLine(var6, var12, var8, var12, this.linePaint);
                  }

                  ++var18;
               }
            }

            var18 = var14 + 1;
            var14 = var3;
            var3 = var10;
            var10 = var2;
            var2 = var3;
            var3 = var11;
            var11 = var14;
         }

         var19 = var7 + var13;
         float var22 = (float)var19;
         var18 = var9 + var13;
         var8 = (float)var18;
         var10 += var7;
         var14 = var10 - var13;
         var12 = (float)var14;
         var1.drawRect(var22, var8, var12, (float)(var18 + var2), this.framePaint);
         var6 = (float)(var19 + var2);
         var11 += var9;
         var18 = var11 - var13;
         float var23 = (float)var18;
         var1.drawRect(var22, var8, var6, var23, this.framePaint);
         var1.drawRect(var22, (float)(var18 - var2), var12, var23, this.framePaint);
         var1.drawRect((float)(var14 - var2), var8, var12, var23, this.framePaint);
         var8 = (float)var7;
         float var24 = (float)var9;
         var22 = (float)(var7 + var3);
         float var25 = (float)(var9 + var4);
         var1.drawRect(var8, var24, var22, var25, this.handlePaint);
         var12 = (float)(var7 + var4);
         float var26 = (float)(var9 + var3);
         var1.drawRect(var8, var24, var12, var26, this.handlePaint);
         var6 = (float)(var10 - var3);
         var23 = (float)var10;
         var1.drawRect(var6, var24, var23, var25, this.handlePaint);
         var25 = (float)(var10 - var4);
         var1.drawRect(var25, var24, var23, var26, this.handlePaint);
         var24 = (float)(var11 - var4);
         var26 = (float)var11;
         var1.drawRect(var8, var24, var22, var26, this.handlePaint);
         var22 = (float)(var11 - var3);
         var1.drawRect(var8, var22, var12, var26, this.handlePaint);
         var1.drawRect(var6, var24, var23, var26, this.handlePaint);
         var1.drawRect(var25, var22, var23, var26, this.handlePaint);
      } else {
         Bitmap var29 = this.circleBitmap;
         if (var29 == null || (float)var29.getWidth() != this.actualRect.width()) {
            var29 = this.circleBitmap;
            if (var29 != null) {
               var29.recycle();
               this.circleBitmap = null;
            }

            try {
               this.circleBitmap = Bitmap.createBitmap((int)this.actualRect.width(), (int)this.actualRect.height(), Config.ARGB_8888);
               Canvas var30 = new Canvas(this.circleBitmap);
               var30.drawRect(0.0F, 0.0F, this.actualRect.width(), this.actualRect.height(), this.dimPaint);
               var30.drawCircle(this.actualRect.width() / 2.0F, this.actualRect.height() / 2.0F, this.actualRect.width() / 2.0F, this.eraserPaint);
               var30.setBitmap((Bitmap)null);
            } catch (Throwable var27) {
            }
         }

         var1.drawRect(0.0F, 0.0F, (float)this.getWidth(), (float)((int)this.actualRect.top), this.dimPaint);
         var5 = this.actualRect;
         var1.drawRect(0.0F, (float)((int)var5.top), (float)((int)var5.left), (float)((int)var5.bottom), this.dimPaint);
         var5 = this.actualRect;
         var1.drawRect((float)((int)var5.right), (float)((int)var5.top), (float)this.getWidth(), (float)((int)this.actualRect.bottom), this.dimPaint);
         var1.drawRect(0.0F, (float)((int)this.actualRect.bottom), (float)this.getWidth(), (float)this.getHeight(), this.dimPaint);
         Bitmap var31 = this.circleBitmap;
         var5 = this.actualRect;
         var1.drawBitmap(var31, (float)((int)var5.left), (float)((int)var5.top), (Paint)null);
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = (int)(var1.getX() - ((ViewGroup)this.getParent()).getX());
      int var3 = (int)(var1.getY() - ((ViewGroup)this.getParent()).getY());
      int var4;
      if (VERSION.SDK_INT >= 21) {
         var4 = AndroidUtilities.statusBarHeight;
      } else {
         var4 = 0;
      }

      float var5 = (float)var4;
      var4 = var1.getActionMasked();
      float var6;
      CropAreaView.AreaViewListener var12;
      RectF var13;
      if (var4 == 0) {
         if (this.freeform) {
            var13 = this.topLeftCorner;
            var5 = (float)var2;
            var6 = (float)var3;
            if (var13.contains(var5, var6)) {
               this.activeControl = CropAreaView.Control.TOP_LEFT;
            } else if (this.topRightCorner.contains(var5, var6)) {
               this.activeControl = CropAreaView.Control.TOP_RIGHT;
            } else if (this.bottomLeftCorner.contains(var5, var6)) {
               this.activeControl = CropAreaView.Control.BOTTOM_LEFT;
            } else if (this.bottomRightCorner.contains(var5, var6)) {
               this.activeControl = CropAreaView.Control.BOTTOM_RIGHT;
            } else if (this.leftEdge.contains(var5, var6)) {
               this.activeControl = CropAreaView.Control.LEFT;
            } else if (this.topEdge.contains(var5, var6)) {
               this.activeControl = CropAreaView.Control.TOP;
            } else if (this.rightEdge.contains(var5, var6)) {
               this.activeControl = CropAreaView.Control.RIGHT;
            } else {
               if (!this.bottomEdge.contains(var5, var6)) {
                  this.activeControl = CropAreaView.Control.NONE;
                  return false;
               }

               this.activeControl = CropAreaView.Control.BOTTOM;
            }

            this.previousX = var2;
            this.previousY = var3;
            this.setGridType(CropAreaView.GridType.MAJOR, false);
            this.isDragging = true;
            var12 = this.listener;
            if (var12 != null) {
               var12.onAreaChangeBegan();
            }

            return true;
         } else {
            this.activeControl = CropAreaView.Control.NONE;
            return false;
         }
      } else if (var4 != 1 && var4 != 3) {
         if (var4 == 2) {
            if (this.activeControl == CropAreaView.Control.NONE) {
               return false;
            } else {
               this.tempRect.set(this.actualRect);
               var6 = (float)(var2 - this.previousX);
               float var7 = (float)(var3 - this.previousY);
               this.previousX = var2;
               this.previousY = var3;
               float var8;
               switch(this.activeControl) {
               case TOP_LEFT:
                  var13 = this.tempRect;
                  var13.left += var6;
                  var13.top += var7;
                  if (this.lockAspectRatio > 0.0F) {
                     var8 = var13.width();
                     float var9 = this.tempRect.height();
                     if (Math.abs(var6) > Math.abs(var7)) {
                        this.constrainRectByWidth(this.tempRect, this.lockAspectRatio);
                     } else {
                        this.constrainRectByHeight(this.tempRect, this.lockAspectRatio);
                     }

                     var13 = this.tempRect;
                     var13.left -= var13.width() - var8;
                     var13 = this.tempRect;
                     var13.top -= var13.width() - var9;
                  }
                  break;
               case TOP_RIGHT:
                  var13 = this.tempRect;
                  var13.right += var6;
                  var13.top += var7;
                  if (this.lockAspectRatio > 0.0F) {
                     var8 = var13.height();
                     if (Math.abs(var6) > Math.abs(var7)) {
                        this.constrainRectByWidth(this.tempRect, this.lockAspectRatio);
                     } else {
                        this.constrainRectByHeight(this.tempRect, this.lockAspectRatio);
                     }

                     var13 = this.tempRect;
                     var13.top -= var13.width() - var8;
                  }
                  break;
               case BOTTOM_LEFT:
                  var13 = this.tempRect;
                  var13.left += var6;
                  var13.bottom += var7;
                  if (this.lockAspectRatio > 0.0F) {
                     var8 = var13.width();
                     if (Math.abs(var6) > Math.abs(var7)) {
                        this.constrainRectByWidth(this.tempRect, this.lockAspectRatio);
                     } else {
                        this.constrainRectByHeight(this.tempRect, this.lockAspectRatio);
                     }

                     var13 = this.tempRect;
                     var13.left -= var13.width() - var8;
                  }
                  break;
               case BOTTOM_RIGHT:
                  var13 = this.tempRect;
                  var13.right += var6;
                  var13.bottom += var7;
                  if (this.lockAspectRatio > 0.0F) {
                     if (Math.abs(var6) > Math.abs(var7)) {
                        this.constrainRectByWidth(this.tempRect, this.lockAspectRatio);
                     } else {
                        this.constrainRectByHeight(this.tempRect, this.lockAspectRatio);
                     }
                  }
                  break;
               case TOP:
                  var13 = this.tempRect;
                  var13.top += var7;
                  var6 = this.lockAspectRatio;
                  if (var6 > 0.0F) {
                     this.constrainRectByHeight(var13, var6);
                  }
                  break;
               case LEFT:
                  var13 = this.tempRect;
                  var13.left += var6;
                  var6 = this.lockAspectRatio;
                  if (var6 > 0.0F) {
                     this.constrainRectByWidth(var13, var6);
                  }
                  break;
               case RIGHT:
                  var13 = this.tempRect;
                  var13.right += var6;
                  var6 = this.lockAspectRatio;
                  if (var6 > 0.0F) {
                     this.constrainRectByWidth(var13, var6);
                  }
                  break;
               case BOTTOM:
                  var13 = this.tempRect;
                  var13.bottom += var7;
                  var6 = this.lockAspectRatio;
                  if (var6 > 0.0F) {
                     this.constrainRectByHeight(var13, var6);
                  }
               }

               var13 = this.tempRect;
               var7 = var13.left;
               var6 = this.sidePadding;
               if (var7 < var6) {
                  var7 = this.lockAspectRatio;
                  if (var7 > 0.0F) {
                     var13.bottom = var13.top + (var13.right - var6) / var7;
                  }

                  this.tempRect.left = this.sidePadding;
               } else if (var13.right > (float)this.getWidth() - this.sidePadding) {
                  this.tempRect.right = (float)this.getWidth() - this.sidePadding;
                  if (this.lockAspectRatio > 0.0F) {
                     var13 = this.tempRect;
                     var13.bottom = var13.top + var13.width() / this.lockAspectRatio;
                  }
               }

               var6 = this.sidePadding;
               var5 += var6;
               var6 += this.bottomPadding;
               var13 = this.tempRect;
               if (var13.top < var5) {
                  var6 = this.lockAspectRatio;
                  if (var6 > 0.0F) {
                     var13.right = var13.left + (var13.bottom - var5) * var6;
                  }

                  this.tempRect.top = var5;
               } else if (var13.bottom > (float)this.getHeight() - var6) {
                  this.tempRect.bottom = (float)this.getHeight() - var6;
                  if (this.lockAspectRatio > 0.0F) {
                     var13 = this.tempRect;
                     var13.right = var13.left + var13.height() * this.lockAspectRatio;
                  }
               }

               var6 = this.tempRect.width();
               var5 = this.minWidth;
               if (var6 < var5) {
                  var13 = this.tempRect;
                  var13.right = var13.left + var5;
               }

               var6 = this.tempRect.height();
               var5 = this.minWidth;
               if (var6 < var5) {
                  var13 = this.tempRect;
                  var13.bottom = var13.top + var5;
               }

               var5 = this.lockAspectRatio;
               if (var5 > 0.0F) {
                  if (var5 < 1.0F) {
                     var6 = this.tempRect.width();
                     var5 = this.minWidth;
                     if (var6 <= var5) {
                        var13 = this.tempRect;
                        var13.right = var13.left + var5;
                        var13.bottom = var13.top + var13.width() / this.lockAspectRatio;
                     }
                  } else {
                     var5 = this.tempRect.height();
                     var6 = this.minWidth;
                     if (var5 <= var6) {
                        var13 = this.tempRect;
                        var13.bottom = var13.top + var6;
                        var13.right = var13.left + var13.height() * this.lockAspectRatio;
                     }
                  }
               }

               this.setActualRect(this.tempRect);
               var12 = this.listener;
               if (var12 != null) {
                  var12.onAreaChange();
               }

               return true;
            }
         } else {
            return false;
         }
      } else {
         this.isDragging = false;
         CropAreaView.Control var10 = this.activeControl;
         CropAreaView.Control var11 = CropAreaView.Control.NONE;
         if (var10 == var11) {
            return false;
         } else {
            this.activeControl = var11;
            var12 = this.listener;
            if (var12 != null) {
               var12.onAreaChangeEnded();
            }

            return true;
         }
      }
   }

   public void resetAnimator() {
      Animator var1 = this.animator;
      if (var1 != null) {
         var1.cancel();
         this.animator = null;
      }

   }

   public void setActualRect(float var1) {
      this.calculateRect(this.actualRect, var1);
      this.updateTouchAreas();
      this.invalidate();
   }

   public void setActualRect(RectF var1) {
      this.actualRect.set(var1);
      this.updateTouchAreas();
      this.invalidate();
   }

   public void setBitmap(Bitmap var1, boolean var2, boolean var3) {
      if (var1 != null && !var1.isRecycled()) {
         this.freeform = var3;
         float var4;
         int var5;
         if (var2) {
            var4 = (float)var1.getHeight();
            var5 = var1.getWidth();
         } else {
            var4 = (float)var1.getWidth();
            var5 = var1.getHeight();
         }

         var4 /= (float)var5;
         if (!this.freeform) {
            this.lockAspectRatio = 1.0F;
            var4 = 1.0F;
         }

         this.setActualRect(var4);
      }

   }

   public void setBottomPadding(float var1) {
      this.bottomPadding = var1;
   }

   public void setDimVisibility(boolean var1) {
      this.dimVisibile = var1;
   }

   public void setFrameVisibility(boolean var1) {
      this.frameVisible = var1;
   }

   public void setFreeform(boolean var1) {
      this.freeform = var1;
   }

   public void setGridType(CropAreaView.GridType var1, boolean var2) {
      if (this.gridAnimator != null && (!var2 || this.gridType != var1)) {
         this.gridAnimator.cancel();
         this.gridAnimator = null;
      }

      CropAreaView.GridType var3 = this.gridType;
      if (var3 != var1) {
         this.previousGridType = var3;
         this.gridType = var1;
         float var4;
         if (var1 == CropAreaView.GridType.NONE) {
            var4 = 0.0F;
         } else {
            var4 = 1.0F;
         }

         if (!var2) {
            this.gridProgress = var4;
            this.invalidate();
         } else {
            this.gridAnimator = ObjectAnimator.ofFloat(this, "gridProgress", new float[]{this.gridProgress, var4});
            this.gridAnimator.setDuration(200L);
            this.gridAnimator.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  CropAreaView.this.gridAnimator = null;
               }
            });
            if (var1 == CropAreaView.GridType.NONE) {
               this.gridAnimator.setStartDelay(200L);
            }

            this.gridAnimator.start();
         }

      }
   }

   public void setListener(CropAreaView.AreaViewListener var1) {
      this.listener = var1;
   }

   public void setLockedAspectRatio(float var1) {
      this.lockAspectRatio = var1;
   }

   interface AreaViewListener {
      void onAreaChange();

      void onAreaChangeBegan();

      void onAreaChangeEnded();
   }

   private static enum Control {
      BOTTOM,
      BOTTOM_LEFT,
      BOTTOM_RIGHT,
      LEFT,
      NONE,
      RIGHT,
      TOP,
      TOP_LEFT,
      TOP_RIGHT;
   }

   static enum GridType {
      MAJOR,
      MINOR,
      NONE;
   }
}

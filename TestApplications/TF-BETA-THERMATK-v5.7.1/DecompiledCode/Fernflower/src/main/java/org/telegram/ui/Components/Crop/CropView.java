package org.telegram.ui.Components.Crop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.AlertDialog;

public class CropView extends FrameLayout implements CropAreaView.AreaViewListener, CropGestureDetector.CropGestureListener {
   private static final float EPSILON = 1.0E-5F;
   private static final float MAX_SCALE = 30.0F;
   private static final int RESULT_SIDE = 1280;
   private boolean animating = false;
   private CropAreaView areaView;
   private View backView;
   private Bitmap bitmap;
   private float bottomPadding;
   private CropGestureDetector detector;
   private boolean freeform;
   private boolean hasAspectRatioDialog;
   private ImageView imageView;
   private RectF initialAreaRect = new RectF();
   private CropView.CropViewListener listener;
   private Matrix presentationMatrix = new Matrix();
   private RectF previousAreaRect = new RectF();
   private float rotationStartScale;
   private CropView.CropState state;
   private Matrix tempMatrix = new Matrix();
   private CropView.CropRectangle tempRect = new CropView.CropRectangle();

   public CropView(Context var1) {
      super(var1);
      this.backView = new View(var1);
      this.backView.setBackgroundColor(-16777216);
      this.backView.setVisibility(4);
      this.addView(this.backView);
      this.imageView = new ImageView(var1);
      this.imageView.setDrawingCacheEnabled(true);
      this.imageView.setScaleType(ScaleType.MATRIX);
      this.addView(this.imageView);
      this.detector = new CropGestureDetector(var1);
      this.detector.setOnGestureListener(this);
      this.areaView = new CropAreaView(var1);
      this.areaView.setListener(this);
      this.addView(this.areaView);
   }

   private void fillAreaView(RectF var1, final boolean var2) {
      int var3 = 0;
      float var4 = Math.max(var1.width() / this.areaView.getCropWidth(), var1.height() / this.areaView.getCropHeight());
      if (this.state.getScale() * var4 > 30.0F) {
         var4 = 30.0F / this.state.getScale();
         var2 = true;
      } else {
         var2 = false;
      }

      if (VERSION.SDK_INT >= 21) {
         var3 = AndroidUtilities.statusBarHeight;
      }

      float var5 = (float)var3;
      float var6 = (var1.centerX() - (float)(this.imageView.getWidth() / 2)) / this.areaView.getCropWidth();
      float var7 = this.state.getOrientedWidth();
      float var8 = (var1.centerY() - ((float)this.imageView.getHeight() - this.bottomPadding + var5) / 2.0F) / this.areaView.getCropHeight();
      var5 = this.state.getOrientedHeight();
      ValueAnimator var9 = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
      var9.addUpdateListener(new _$$Lambda$CropView$u7JJSQis3TQtsCeT54hdvCdMU_Y(this, var4, new float[]{1.0F}, var7 * var6, var8 * var5));
      var9.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            if (var2) {
               CropView.this.fitContentInBounds(false, false, true);
            }

         }
      });
      this.areaView.fill(var1, var9, true);
      this.initialAreaRect.set(var1);
   }

   private void fitContentInBounds(boolean var1, boolean var2, boolean var3) {
      this.fitContentInBounds(var1, var2, var3, false);
   }

   private void fitContentInBounds(final boolean var1, final boolean var2, final boolean var3, final boolean var4) {
      if (this.state != null) {
         float var5 = this.areaView.getCropWidth();
         float var6 = this.areaView.getCropHeight();
         float var7 = this.state.getOrientedWidth();
         float var8 = this.state.getOrientedHeight();
         float var9 = this.state.getRotation();
         float var10 = (float)Math.toRadians((double)var9);
         RectF var11 = this.calculateBoundingBox(var5, var6, var9);
         RectF var12 = new RectF(0.0F, 0.0F, var7, var8);
         var5 = (var5 - var7) / 2.0F;
         float var13 = (var6 - var8) / 2.0F;
         var6 = this.state.getScale();
         this.tempRect.setRect(var12);
         Matrix var14 = this.state.getMatrix();
         var14.preTranslate(var5 / var6, var13 / var6);
         this.tempMatrix.reset();
         this.tempMatrix.setTranslate(var12.centerX(), var12.centerY());
         Matrix var15 = this.tempMatrix;
         var15.setConcat(var15, var14);
         this.tempMatrix.preTranslate(-var12.centerX(), -var12.centerY());
         this.tempRect.applyMatrix(this.tempMatrix);
         this.tempMatrix.reset();
         this.tempMatrix.preRotate(-var9, var7 / 2.0F, var8 / 2.0F);
         this.tempRect.applyMatrix(this.tempMatrix);
         this.tempRect.getRect(var12);
         PointF var19 = new PointF(this.state.getX(), this.state.getY());
         if (!var12.contains(var11)) {
            if (!var1 || var11.width() <= var12.width() && var11.height() <= var12.height()) {
               var7 = var6;
            } else {
               var7 = this.fitScale(var12, var6, var11.width() / this.scaleWidthToMaxSize(var11, var12));
            }

            this.fitTranslation(var12, var11, var19, var10);
         } else if (var2 && this.rotationStartScale > 0.0F) {
            var8 = var11.width() / this.scaleWidthToMaxSize(var11, var12);
            var7 = var8;
            if (this.state.getScale() * var8 < this.rotationStartScale) {
               var7 = 1.0F;
            }

            var7 = this.fitScale(var12, var6, var7);
            this.fitTranslation(var12, var11, var19, var10);
         } else {
            var7 = var6;
         }

         var10 = var19.x - this.state.getX();
         var8 = var19.y - this.state.getY();
         if (var3) {
            var7 /= var6;
            if (Math.abs(var7 - 1.0F) < 1.0E-5F && Math.abs(var10) < 1.0E-5F && Math.abs(var8) < 1.0E-5F) {
               return;
            }

            this.animating = true;
            ValueAnimator var18 = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
            var18.addUpdateListener(new _$$Lambda$CropView$wiih4K5GW50uIgzkO67FnI_u_xc(this, var10, new float[]{1.0F, 0.0F, 0.0F}, var8, var7));
            var18.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1x) {
                  CropView.this.animating = false;
                  if (!var4) {
                     CropView.this.fitContentInBounds(var1, var2, var3, true);
                  }

               }
            });
            var18.setInterpolator(this.areaView.getInterpolator());
            long var16;
            if (var4) {
               var16 = 100L;
            } else {
               var16 = 200L;
            }

            var18.setDuration(var16);
            var18.start();
         } else {
            this.state.translate(var10, var8);
            this.state.scale(var7 / var6, 0.0F, 0.0F);
            this.updateMatrix();
         }

      }
   }

   private float fitScale(RectF var1, float var2, float var3) {
      float var4 = var1.width() * var3;
      float var5 = var1.height() * var3;
      float var6 = (var1.width() - var4) / 2.0F;
      float var7 = (var1.height() - var5) / 2.0F;
      float var8 = var1.left;
      float var9 = var1.top;
      var1.set(var8 + var6, var9 + var7, var8 + var6 + var4, var9 + var7 + var5);
      return var2 * var3;
   }

   private void fitTranslation(RectF var1, RectF var2, PointF var3, float var4) {
      float var5 = var2.left;
      float var6 = var2.top;
      float var7 = var2.right;
      float var8 = var2.bottom;
      float var9 = var1.left;
      float var10 = var5;
      float var11 = var7;
      if (var9 > var5) {
         var11 = var7 + (var9 - var5);
         var10 = var9;
      }

      var5 = var1.top;
      var9 = var6;
      var7 = var8;
      if (var5 > var6) {
         var7 = var8 + (var5 - var6);
         var9 = var5;
      }

      var6 = var1.right;
      var8 = var10;
      if (var6 < var11) {
         var8 = var10 + (var6 - var11);
      }

      var11 = var1.bottom;
      var10 = var9;
      if (var11 < var7) {
         var10 = var9 + (var11 - var7);
      }

      var6 = var2.centerX();
      var7 = var2.width() / 2.0F;
      var11 = var2.centerY();
      var9 = var2.height() / 2.0F;
      double var12 = (double)var4;
      Double.isNaN(var12);
      double var14 = 1.5707963267948966D - var12;
      double var16 = Math.sin(var14);
      double var18 = (double)(var6 - (var8 + var7));
      Double.isNaN(var18);
      var4 = (float)(var16 * var18);
      var16 = Math.cos(var14);
      Double.isNaN(var18);
      var7 = (float)(var16 * var18);
      Double.isNaN(var12);
      var18 = var12 + 1.5707963267948966D;
      var16 = Math.cos(var18);
      var12 = (double)(var11 - (var10 + var9));
      Double.isNaN(var12);
      var10 = (float)(var16 * var12);
      var18 = Math.sin(var18);
      Double.isNaN(var12);
      var9 = (float)(var18 * var12);
      var3.set(var3.x + var4 + var10, var3.y + var7 + var9);
   }

   private void resetRotationStartScale() {
      this.rotationStartScale = 0.0F;
   }

   private void setLockedAspectRatio(float var1) {
      this.areaView.setLockedAspectRatio(var1);
      RectF var2 = new RectF();
      this.areaView.calculateRect(var2, var1);
      this.fillAreaView(var2, true);
      CropView.CropViewListener var3 = this.listener;
      if (var3 != null) {
         var3.onChange(false);
         this.listener.onAspectLock(true);
      }

   }

   public RectF calculateBoundingBox(float var1, float var2, float var3) {
      RectF var4 = new RectF(0.0F, 0.0F, var1, var2);
      Matrix var5 = new Matrix();
      var5.postRotate(var3, var1 / 2.0F, var2 / 2.0F);
      var5.mapRect(var4);
      return var4;
   }

   public float getCropHeight() {
      return this.areaView.getCropHeight();
   }

   public float getCropLeft() {
      return this.areaView.getCropLeft();
   }

   public float getCropTop() {
      return this.areaView.getCropTop();
   }

   public float getCropWidth() {
      return this.areaView.getCropWidth();
   }

   public Bitmap getResult() {
      CropView.CropState var1 = this.state;
      if (var1 == null || !var1.hasChanges() && this.state.getBaseRotation() < 1.0E-5F && this.freeform) {
         return this.bitmap;
      } else {
         RectF var6 = new RectF();
         this.areaView.getCropRect(var6);
         int var2 = (int)Math.ceil((double)this.scaleWidthToMaxSize(var6, new RectF(0.0F, 0.0F, 1280.0F, 1280.0F)));
         float var3 = (float)var2;
         int var4 = (int)Math.ceil((double)(var3 / this.areaView.getAspectRatio()));
         Bitmap var5 = Bitmap.createBitmap(var2, var4, Config.ARGB_8888);
         Matrix var7 = new Matrix();
         var7.postTranslate(-this.state.getWidth() / 2.0F, -this.state.getHeight() / 2.0F);
         var7.postRotate(this.state.getOrientation());
         this.state.getConcatMatrix(var7);
         var3 /= this.areaView.getCropWidth();
         var7.postScale(var3, var3);
         var7.postTranslate((float)(var2 / 2), (float)(var4 / 2));
         (new Canvas(var5)).drawBitmap(this.bitmap, var7, new Paint(2));
         return var5;
      }
   }

   public void hide() {
      this.backView.setVisibility(4);
      this.imageView.setVisibility(4);
      this.areaView.setDimVisibility(false);
      this.areaView.setFrameVisibility(false);
      this.areaView.invalidate();
   }

   public void hideBackView() {
      this.backView.setVisibility(4);
   }

   public boolean isReady() {
      boolean var1;
      if (!this.detector.isScaling() && !this.detector.isDragging() && !this.areaView.isDragging()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   // $FF: synthetic method
   public void lambda$fillAreaView$0$CropView(float var1, float[] var2, float var3, float var4, ValueAnimator var5) {
      var1 = ((var1 - 1.0F) * (Float)var5.getAnimatedValue() + 1.0F) / var2[0];
      var2[0] *= var1;
      this.state.scale(var1, var3, var4);
      this.updateMatrix();
   }

   // $FF: synthetic method
   public void lambda$fitContentInBounds$1$CropView(float var1, float[] var2, float var3, float var4, ValueAnimator var5) {
      float var6 = (Float)var5.getAnimatedValue();
      var1 = var1 * var6 - var2[1];
      var2[1] += var1;
      var3 = var3 * var6 - var2[2];
      var2[2] += var3;
      this.state.translate(var1 * var2[0], var3 * var2[0]);
      var1 = ((var4 - 1.0F) * var6 + 1.0F) / var2[0];
      var2[0] *= var1;
      this.state.scale(var1, 0.0F, 0.0F);
      this.updateMatrix();
   }

   // $FF: synthetic method
   public void lambda$showAspectRatioDialog$2$CropView(Integer[][] var1, DialogInterface var2, int var3) {
      this.hasAspectRatioDialog = false;
      if (var3 != 0) {
         if (var3 != 1) {
            Integer[] var6 = var1[var3 - 2];
            if (this.areaView.getAspectRatio() > 1.0F) {
               this.setLockedAspectRatio((float)var6[0] / (float)var6[1]);
            } else {
               this.setLockedAspectRatio((float)var6[1] / (float)var6[0]);
            }
         } else {
            this.setLockedAspectRatio(1.0F);
         }
      } else {
         float var4;
         if (this.state.getBaseRotation() % 180.0F != 0.0F) {
            var4 = this.state.getHeight();
         } else {
            var4 = this.state.getWidth();
         }

         float var5;
         if (this.state.getBaseRotation() % 180.0F != 0.0F) {
            var5 = this.state.getWidth();
         } else {
            var5 = this.state.getHeight();
         }

         this.setLockedAspectRatio(var4 / var5);
      }

   }

   // $FF: synthetic method
   public void lambda$showAspectRatioDialog$3$CropView(DialogInterface var1) {
      this.hasAspectRatioDialog = false;
   }

   public void onAreaChange() {
      this.areaView.setGridType(CropAreaView.GridType.MAJOR, false);
      float var1 = this.previousAreaRect.centerX();
      float var2 = this.areaView.getCropCenterX();
      float var3 = this.previousAreaRect.centerY();
      float var4 = this.areaView.getCropCenterY();
      this.state.translate(var1 - var2, var3 - var4);
      this.updateMatrix();
      this.areaView.getCropRect(this.previousAreaRect);
      this.fitContentInBounds(true, false, false);
   }

   public void onAreaChangeBegan() {
      this.areaView.getCropRect(this.previousAreaRect);
      this.resetRotationStartScale();
      CropView.CropViewListener var1 = this.listener;
      if (var1 != null) {
         var1.onChange(false);
      }

   }

   public void onAreaChangeEnded() {
      this.areaView.setGridType(CropAreaView.GridType.NONE, true);
      this.fillAreaView(this.areaView.getTargetRectToFill(), false);
   }

   public void onDrag(float var1, float var2) {
      if (!this.animating) {
         this.state.translate(var1, var2);
         this.updateMatrix();
      }
   }

   public void onFling(float var1, float var2, float var3, float var4) {
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      return true;
   }

   public void onRotationBegan() {
      this.areaView.setGridType(CropAreaView.GridType.MINOR, false);
      if (this.rotationStartScale < 1.0E-5F) {
         this.rotationStartScale = this.state.getScale();
      }

   }

   public void onRotationEnded() {
      this.areaView.setGridType(CropAreaView.GridType.NONE, true);
   }

   public void onScale(float var1, float var2, float var3) {
      if (!this.animating) {
         float var4 = var1;
         if (this.state.getScale() * var1 > 30.0F) {
            var4 = 30.0F / this.state.getScale();
         }

         int var5;
         if (VERSION.SDK_INT >= 21) {
            var5 = AndroidUtilities.statusBarHeight;
         } else {
            var5 = 0;
         }

         float var6 = (float)var5;
         var1 = (var2 - (float)(this.imageView.getWidth() / 2)) / this.areaView.getCropWidth();
         var2 = this.state.getOrientedWidth();
         var3 = (var3 - ((float)this.imageView.getHeight() - this.bottomPadding - var6) / 2.0F) / this.areaView.getCropHeight();
         var6 = this.state.getOrientedHeight();
         this.state.scale(var4, var1 * var2, var3 * var6);
         this.updateMatrix();
      }
   }

   public void onScrollChangeBegan() {
      if (!this.animating) {
         this.areaView.setGridType(CropAreaView.GridType.MAJOR, true);
         this.resetRotationStartScale();
         CropView.CropViewListener var1 = this.listener;
         if (var1 != null) {
            var1.onChange(false);
         }

      }
   }

   public void onScrollChangeEnded() {
      this.areaView.setGridType(CropAreaView.GridType.NONE, true);
      this.fitContentInBounds(true, false, true);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (this.animating) {
         return true;
      } else {
         boolean var2 = false;
         if (this.areaView.onTouchEvent(var1)) {
            return true;
         } else {
            int var3 = var1.getAction();
            if (var3 != 0) {
               if (var3 == 1 || var3 == 3) {
                  this.onScrollChangeEnded();
               }
            } else {
               this.onScrollChangeBegan();
            }

            boolean var4;
            try {
               var4 = this.detector.onTouchEvent(var1);
            } catch (Exception var5) {
               var4 = var2;
            }

            return var4;
         }
      }
   }

   public void reset() {
      this.areaView.resetAnimator();
      CropAreaView var1 = this.areaView;
      Bitmap var2 = this.bitmap;
      boolean var3;
      if (this.state.getBaseRotation() % 180.0F != 0.0F) {
         var3 = true;
      } else {
         var3 = false;
      }

      var1.setBitmap(var2, var3, this.freeform);
      CropAreaView var5 = this.areaView;
      float var4;
      if (this.freeform) {
         var4 = 0.0F;
      } else {
         var4 = 1.0F;
      }

      var5.setLockedAspectRatio(var4);
      this.state.reset(this.areaView, 0.0F, this.freeform);
      this.areaView.getCropRect(this.initialAreaRect);
      this.updateMatrix();
      this.resetRotationStartScale();
      CropView.CropViewListener var6 = this.listener;
      if (var6 != null) {
         var6.onChange(true);
         this.listener.onAspectLock(false);
      }

   }

   public void rotate90Degrees() {
      if (this.state != null) {
         this.areaView.resetAnimator();
         this.resetRotationStartScale();
         float var1 = (this.state.getOrientation() - this.state.getBaseRotation() - 90.0F) % 360.0F;
         boolean var2 = this.freeform;
         boolean var3 = true;
         CropAreaView var4;
         boolean var5;
         if (var2 && this.areaView.getLockAspectRatio() > 0.0F) {
            var4 = this.areaView;
            var4.setLockedAspectRatio(1.0F / var4.getLockAspectRatio());
            var4 = this.areaView;
            var4.setActualRect(var4.getLockAspectRatio());
            var5 = false;
         } else {
            var4 = this.areaView;
            Bitmap var6 = this.bitmap;
            if ((this.state.getBaseRotation() + var1) % 180.0F != 0.0F) {
               var5 = true;
            } else {
               var5 = false;
            }

            var4.setBitmap(var6, var5, this.freeform);
            var5 = var2;
         }

         this.state.reset(this.areaView, var1, var5);
         this.updateMatrix();
         CropView.CropViewListener var7 = this.listener;
         if (var7 != null) {
            if (var1 == 0.0F && this.areaView.getLockAspectRatio() == 0.0F) {
               var5 = var3;
            } else {
               var5 = false;
            }

            var7.onChange(var5);
         }

      }
   }

   public float scaleWidthToMaxSize(RectF var1, RectF var2) {
      float var3 = var2.width();
      float var4 = var3;
      if ((float)Math.floor((double)(var1.height() * var3 / var1.width())) > var2.height()) {
         var4 = (float)Math.floor((double)(var2.height() * var1.width() / var1.height()));
      }

      return var4;
   }

   public void setAspectRatio(float var1) {
      this.areaView.setActualRect(var1);
   }

   public void setBitmap(Bitmap var1, int var2, boolean var3, boolean var4) {
      this.freeform = var3;
      if (var1 == null) {
         this.bitmap = null;
         this.state = null;
         this.imageView.setImageDrawable((Drawable)null);
      } else {
         this.bitmap = var1;
         CropView.CropState var5 = this.state;
         if (var5 != null && var4) {
            var5.updateBitmap(this.bitmap, var2);
         } else {
            this.state = new CropView.CropState(this.bitmap, var2);
            this.imageView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
               public boolean onPreDraw() {
                  CropView.this.reset();
                  CropView.this.imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                  return false;
               }
            });
         }

         this.imageView.setImageBitmap(this.bitmap);
      }

   }

   public void setBottomPadding(float var1) {
      this.bottomPadding = var1;
      this.areaView.setBottomPadding(var1);
   }

   public void setFreeform(boolean var1) {
      this.areaView.setFreeform(var1);
      this.freeform = var1;
   }

   public void setListener(CropView.CropViewListener var1) {
      this.listener = var1;
   }

   public void setRotation(float var1) {
      float var2 = this.state.getRotation();
      this.state.rotate(var1 - var2, 0.0F, 0.0F);
      this.fitContentInBounds(true, true, false);
   }

   public void show() {
      this.backView.setVisibility(0);
      this.imageView.setVisibility(0);
      this.areaView.setDimVisibility(true);
      this.areaView.setFrameVisibility(true);
      this.areaView.invalidate();
   }

   public void showAspectRatioDialog() {
      if (this.areaView.getLockAspectRatio() > 0.0F) {
         this.areaView.setLockedAspectRatio(0.0F);
         CropView.CropViewListener var8 = this.listener;
         if (var8 != null) {
            var8.onAspectLock(false);
         }

      } else if (!this.hasAspectRatioDialog) {
         this.hasAspectRatioDialog = true;
         String[] var2 = new String[8];
         Integer[][] var1 = new Integer[][]{{3, 2}, {5, 3}, {4, 3}, {5, 4}, {7, 5}, {16, 9}};
         var2[0] = LocaleController.getString("CropOriginal", 2131559177);
         var2[1] = LocaleController.getString("CropSquare", 2131559179);
         int var3 = var1.length;
         int var4 = 0;

         for(int var5 = 2; var4 < var3; ++var4) {
            Integer[] var6 = var1[var4];
            if (this.areaView.getAspectRatio() > 1.0F) {
               var2[var5] = String.format("%d:%d", var6[0], var6[1]);
            } else {
               var2[var5] = String.format("%d:%d", var6[1], var6[0]);
            }

            ++var5;
         }

         AlertDialog var7 = (new AlertDialog.Builder(this.getContext())).setItems(var2, new _$$Lambda$CropView$05yvHZ5A2zqkNRsfdUscWVy3MHM(this, var1)).create();
         var7.setCanceledOnTouchOutside(true);
         var7.setOnCancelListener(new _$$Lambda$CropView$MBImByPOxBcWFdcV4k41dkfucIY(this));
         var7.show();
      }
   }

   public void showBackView() {
      this.backView.setVisibility(0);
   }

   public void updateLayout() {
      float var1 = this.areaView.getCropWidth();
      CropView.CropState var2 = this.state;
      if (var2 != null) {
         this.areaView.calculateRect(this.initialAreaRect, var2.getWidth() / this.state.getHeight());
         CropAreaView var3 = this.areaView;
         var3.setActualRect(var3.getAspectRatio());
         this.areaView.getCropRect(this.previousAreaRect);
         var1 = this.areaView.getCropWidth() / var1;
         this.state.scale(var1, 0.0F, 0.0F);
         this.updateMatrix();
      }

   }

   public void updateMatrix() {
      this.presentationMatrix.reset();
      this.presentationMatrix.postTranslate(-this.state.getWidth() / 2.0F, -this.state.getHeight() / 2.0F);
      this.presentationMatrix.postRotate(this.state.getOrientation());
      this.state.getConcatMatrix(this.presentationMatrix);
      this.presentationMatrix.postTranslate(this.areaView.getCropCenterX(), this.areaView.getCropCenterY());
      this.imageView.setImageMatrix(this.presentationMatrix);
   }

   public void willShow() {
      this.areaView.setFrameVisibility(true);
      this.areaView.setDimVisibility(true);
      this.areaView.invalidate();
   }

   private class CropRectangle {
      float[] coords = new float[8];

      CropRectangle() {
      }

      void applyMatrix(Matrix var1) {
         var1.mapPoints(this.coords);
      }

      void getRect(RectF var1) {
         float[] var2 = this.coords;
         var1.set(var2[0], var2[1], var2[2], var2[7]);
      }

      void setRect(RectF var1) {
         float[] var2 = this.coords;
         float var3 = var1.left;
         var2[0] = var3;
         float var4 = var1.top;
         var2[1] = var4;
         float var5 = var1.right;
         var2[2] = var5;
         var2[3] = var4;
         var2[4] = var5;
         var5 = var1.bottom;
         var2[5] = var5;
         var2[6] = var3;
         var2[7] = var5;
      }
   }

   private class CropState {
      private float baseRotation;
      private float height;
      private Matrix matrix;
      private float minimumScale;
      private float orientation;
      private float rotation;
      private float scale;
      private float width;
      private float x;
      private float y;

      private CropState(Bitmap var2, int var3) {
         this.width = (float)var2.getWidth();
         this.height = (float)var2.getHeight();
         this.x = 0.0F;
         this.y = 0.0F;
         this.scale = 1.0F;
         this.baseRotation = (float)var3;
         this.rotation = 0.0F;
         this.matrix = new Matrix();
      }

      // $FF: synthetic method
      CropState(Bitmap var2, int var3, Object var4) {
         this(var2, var3);
      }

      private float getBaseRotation() {
         return this.baseRotation;
      }

      private void getConcatMatrix(Matrix var1) {
         var1.postConcat(this.matrix);
      }

      private float getHeight() {
         return this.height;
      }

      private Matrix getMatrix() {
         Matrix var1 = new Matrix();
         var1.set(this.matrix);
         return var1;
      }

      private float getMinimumScale() {
         return this.minimumScale;
      }

      private float getOrientation() {
         return this.orientation + this.baseRotation;
      }

      private float getOrientedHeight() {
         float var1;
         if ((this.orientation + this.baseRotation) % 180.0F != 0.0F) {
            var1 = this.width;
         } else {
            var1 = this.height;
         }

         return var1;
      }

      private float getOrientedWidth() {
         float var1;
         if ((this.orientation + this.baseRotation) % 180.0F != 0.0F) {
            var1 = this.height;
         } else {
            var1 = this.width;
         }

         return var1;
      }

      private float getRotation() {
         return this.rotation;
      }

      private float getScale() {
         return this.scale;
      }

      private float getWidth() {
         return this.width;
      }

      private float getX() {
         return this.x;
      }

      private float getY() {
         return this.y;
      }

      private boolean hasChanges() {
         boolean var1;
         if (Math.abs(this.x) <= 1.0E-5F && Math.abs(this.y) <= 1.0E-5F && Math.abs(this.scale - this.minimumScale) <= 1.0E-5F && Math.abs(this.rotation) <= 1.0E-5F && Math.abs(this.orientation) <= 1.0E-5F) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      private void reset(CropAreaView var1, float var2, boolean var3) {
         this.matrix.reset();
         this.x = 0.0F;
         this.y = 0.0F;
         this.rotation = 0.0F;
         this.orientation = var2;
         this.updateMinimumScale();
         this.scale = this.minimumScale;
         Matrix var4 = this.matrix;
         var2 = this.scale;
         var4.postScale(var2, var2);
      }

      private void rotate(float var1, float var2, float var3) {
         this.rotation += var1;
         this.matrix.postRotate(var1, var2, var3);
      }

      private void scale(float var1, float var2, float var3) {
         this.scale *= var1;
         this.matrix.postScale(var1, var1, var2, var3);
      }

      private void translate(float var1, float var2) {
         this.x += var1;
         this.y += var2;
         this.matrix.postTranslate(var1, var2);
      }

      private void updateBitmap(Bitmap var1, int var2) {
         float var3 = this.width / (float)var1.getWidth();
         this.scale *= var3;
         this.width = (float)var1.getWidth();
         this.height = (float)var1.getHeight();
         this.updateMinimumScale();
         float[] var4 = new float[9];
         this.matrix.getValues(var4);
         this.matrix.reset();
         Matrix var5 = this.matrix;
         var3 = this.scale;
         var5.postScale(var3, var3);
         this.matrix.postTranslate(var4[2], var4[5]);
         CropView.this.updateMatrix();
      }

      private void updateMinimumScale() {
         float var1;
         if ((this.orientation + this.baseRotation) % 180.0F != 0.0F) {
            var1 = this.height;
         } else {
            var1 = this.width;
         }

         float var2;
         if ((this.orientation + this.baseRotation) % 180.0F != 0.0F) {
            var2 = this.width;
         } else {
            var2 = this.height;
         }

         if (CropView.this.freeform) {
            this.minimumScale = CropView.this.areaView.getCropWidth() / var1;
         } else {
            this.minimumScale = Math.max(CropView.this.areaView.getCropWidth() / var1, CropView.this.areaView.getCropHeight() / var2);
         }

      }
   }

   public interface CropViewListener {
      void onAspectLock(boolean var1);

      void onChange(boolean var1);
   }
}

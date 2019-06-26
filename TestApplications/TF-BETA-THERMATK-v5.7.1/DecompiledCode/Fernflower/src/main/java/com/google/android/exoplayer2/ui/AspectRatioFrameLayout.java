package com.google.android.exoplayer2.ui;

import android.content.Context;
import android.graphics.Matrix;
import android.view.TextureView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;

public class AspectRatioFrameLayout extends FrameLayout {
   private static final float MAX_ASPECT_RATIO_DEFORMATION_FRACTION = 0.01F;
   public static final int RESIZE_MODE_FILL = 3;
   public static final int RESIZE_MODE_FIT = 0;
   public static final int RESIZE_MODE_FIXED_HEIGHT = 2;
   public static final int RESIZE_MODE_FIXED_WIDTH = 1;
   public static final int RESIZE_MODE_ZOOM = 4;
   private AspectRatioFrameLayout.AspectRatioListener aspectRatioListener;
   private final AspectRatioFrameLayout.AspectRatioUpdateDispatcher aspectRatioUpdateDispatcher = new AspectRatioFrameLayout.AspectRatioUpdateDispatcher();
   private boolean drawingReady;
   private Matrix matrix = new Matrix();
   private int resizeMode = 0;
   private int rotation;
   private float videoAspectRatio;

   public AspectRatioFrameLayout(Context var1) {
      super(var1);
   }

   public float getAspectRatio() {
      return this.videoAspectRatio;
   }

   public int getResizeMode() {
      return this.resizeMode;
   }

   public int getVideoRotation() {
      return this.rotation;
   }

   public boolean isDrawingReady() {
      return this.drawingReady;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      if (this.videoAspectRatio > 0.0F) {
         var2 = this.getMeasuredWidth();
         var1 = this.getMeasuredHeight();
         float var3 = (float)var2;
         float var4 = (float)var1;
         float var5 = var3 / var4;
         float var6 = this.videoAspectRatio / var5 - 1.0F;
         float var7 = Math.abs(var6);
         byte var8 = 0;
         if (var7 <= 0.01F) {
            this.aspectRatioUpdateDispatcher.scheduleUpdate(this.videoAspectRatio, var5, false);
         } else {
            label66: {
               label65: {
                  int var9 = this.resizeMode;
                  if (var9 != 0) {
                     if (var9 != 1) {
                        if (var9 == 2) {
                           var6 = this.videoAspectRatio;
                           break label65;
                        }

                        if (var9 != 3) {
                           if (var9 != 4) {
                              break label66;
                           }

                           if (var6 > 0.0F) {
                              var6 = this.videoAspectRatio;
                              break label65;
                           }

                           var6 = this.videoAspectRatio;
                        } else {
                           if (var6 > 0.0F) {
                              var6 = this.videoAspectRatio;
                              break label65;
                           }

                           var6 = this.videoAspectRatio;
                        }
                     } else {
                        var6 = this.videoAspectRatio;
                     }
                  } else {
                     if (var6 <= 0.0F) {
                        var6 = this.videoAspectRatio;
                        break label65;
                     }

                     var6 = this.videoAspectRatio;
                  }

                  var1 = (int)(var3 / var6);
                  break label66;
               }

               var2 = (int)(var4 * var6);
            }

            this.aspectRatioUpdateDispatcher.scheduleUpdate(this.videoAspectRatio, var5, true);
            super.onMeasure(MeasureSpec.makeMeasureSpec(var2, 1073741824), MeasureSpec.makeMeasureSpec(var1, 1073741824));
            var2 = this.getChildCount();

            for(var1 = var8; var1 < var2; ++var1) {
               View var10 = this.getChildAt(var1);
               if (var10 instanceof TextureView) {
                  this.matrix.reset();
                  var2 = this.getWidth() / 2;
                  var1 = this.getHeight() / 2;
                  Matrix var11 = this.matrix;
                  var4 = (float)this.rotation;
                  var6 = (float)var2;
                  var5 = (float)var1;
                  var11.postRotate(var4, var6, var5);
                  var1 = this.rotation;
                  if (var1 == 90 || var1 == 270) {
                     var4 = (float)this.getHeight() / (float)this.getWidth();
                     this.matrix.postScale(1.0F / var4, var4, var6, var5);
                  }

                  ((TextureView)var10).setTransform(this.matrix);
                  break;
               }
            }

         }
      }
   }

   public void setAspectRatio(float var1, int var2) {
      if (this.videoAspectRatio != var1) {
         this.videoAspectRatio = var1;
         this.rotation = var2;
         this.requestLayout();
      }

   }

   public void setAspectRatioListener(AspectRatioFrameLayout.AspectRatioListener var1) {
      this.aspectRatioListener = var1;
   }

   public void setDrawingReady(boolean var1) {
      if (this.drawingReady != var1) {
         this.drawingReady = var1;
      }
   }

   public void setResizeMode(int var1) {
      if (this.resizeMode != var1) {
         this.resizeMode = var1;
         this.requestLayout();
      }

   }

   public interface AspectRatioListener {
      void onAspectRatioUpdated(float var1, float var2, boolean var3);
   }

   private final class AspectRatioUpdateDispatcher implements Runnable {
      private boolean aspectRatioMismatch;
      private boolean isScheduled;
      private float naturalAspectRatio;
      private float targetAspectRatio;

      private AspectRatioUpdateDispatcher() {
      }

      // $FF: synthetic method
      AspectRatioUpdateDispatcher(Object var2) {
         this();
      }

      public void run() {
         this.isScheduled = false;
         if (AspectRatioFrameLayout.this.aspectRatioListener != null) {
            AspectRatioFrameLayout.this.aspectRatioListener.onAspectRatioUpdated(this.targetAspectRatio, this.naturalAspectRatio, this.aspectRatioMismatch);
         }
      }

      public void scheduleUpdate(float var1, float var2, boolean var3) {
         this.targetAspectRatio = var1;
         this.naturalAspectRatio = var2;
         this.aspectRatioMismatch = var3;
         if (!this.isScheduled) {
            this.isScheduled = true;
            AspectRatioFrameLayout.this.post(this);
         }

      }
   }
}

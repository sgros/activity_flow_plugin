package org.telegram.messenger.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.Paint.Style;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.view.TextureView.SurfaceTextureListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;

@SuppressLint({"NewApi"})
public class CameraView extends FrameLayout implements SurfaceTextureListener {
   private CameraSession cameraSession;
   private int clipLeft;
   private int clipTop;
   private int cx;
   private int cy;
   private CameraView.CameraViewDelegate delegate;
   private int focusAreaSize;
   private float focusProgress = 1.0F;
   private boolean initialFrontface;
   private boolean initied;
   private float innerAlpha;
   private Paint innerPaint = new Paint(1);
   private DecelerateInterpolator interpolator = new DecelerateInterpolator();
   private boolean isFrontface;
   private long lastDrawTime;
   private Matrix matrix = new Matrix();
   private boolean mirror;
   private float outerAlpha;
   private Paint outerPaint = new Paint(1);
   private Size previewSize;
   private TextureView textureView;
   private Matrix txform = new Matrix();

   public CameraView(Context var1, boolean var2) {
      super(var1, (AttributeSet)null);
      this.isFrontface = var2;
      this.initialFrontface = var2;
      this.textureView = new TextureView(var1);
      this.textureView.setSurfaceTextureListener(this);
      this.addView(this.textureView);
      this.focusAreaSize = AndroidUtilities.dp(96.0F);
      this.outerPaint.setColor(-1);
      this.outerPaint.setStyle(Style.STROKE);
      this.outerPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.innerPaint.setColor(Integer.MAX_VALUE);
   }

   private void adjustAspectRatio(int var1, int var2, int var3) {
      this.txform.reset();
      int var4 = this.getWidth();
      int var5 = this.getHeight();
      float var6 = (float)(var4 / 2);
      float var7 = (float)(var5 / 2);
      float var8;
      if (var3 != 0 && var3 != 2) {
         var8 = Math.max((float)(this.clipTop + var5) / (float)var2, (float)(this.clipLeft + var4) / (float)var1);
      } else {
         var8 = Math.max((float)(this.clipTop + var5) / (float)var1, (float)(this.clipLeft + var4) / (float)var2);
      }

      float var9 = (float)var1;
      float var10 = (float)var2;
      float var11 = (float)var4;
      var10 = var10 * var8 / var11;
      float var12 = (float)var5;
      var8 = var9 * var8 / var12;
      this.txform.postScale(var10, var8, var6, var7);
      if (1 != var3 && 3 != var3) {
         if (2 == var3) {
            this.txform.postRotate(180.0F, var6, var7);
         }
      } else {
         this.txform.postRotate((float)((var3 - 2) * 90), var6, var7);
      }

      if (this.mirror) {
         this.txform.postScale(-1.0F, 1.0F, var6, var7);
      }

      if (this.clipTop != 0 || this.clipLeft != 0) {
         this.txform.postTranslate((float)(-this.clipLeft / 2), (float)(-this.clipTop / 2));
      }

      this.textureView.setTransform(this.txform);
      Matrix var13 = new Matrix();
      var13.postRotate((float)this.cameraSession.getDisplayOrientation());
      var13.postScale(var11 / 2000.0F, var12 / 2000.0F);
      var13.postTranslate(var11 / 2.0F, var12 / 2.0F);
      var13.invert(this.matrix);
   }

   private Rect calculateTapArea(float var1, float var2, float var3) {
      int var4 = Float.valueOf((float)this.focusAreaSize * var3).intValue();
      int var5 = (int)var1;
      int var6 = var4 / 2;
      var5 = this.clamp(var5 - var6, 0, this.getWidth() - var4);
      var6 = this.clamp((int)var2 - var6, 0, this.getHeight() - var4);
      RectF var7 = new RectF((float)var5, (float)var6, (float)(var5 + var4), (float)(var6 + var4));
      this.matrix.mapRect(var7);
      return new Rect(Math.round(var7.left), Math.round(var7.top), Math.round(var7.right), Math.round(var7.bottom));
   }

   private void checkPreviewMatrix() {
      Size var1 = this.previewSize;
      if (var1 != null) {
         this.adjustAspectRatio(var1.getWidth(), this.previewSize.getHeight(), ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getRotation());
      }
   }

   private int clamp(int var1, int var2, int var3) {
      if (var1 > var3) {
         return var3;
      } else {
         return var1 < var2 ? var2 : var1;
      }
   }

   private void initCamera() {
      ArrayList var1 = CameraController.getInstance().getCameras();
      if (var1 != null) {
         int var2 = 0;

         CameraInfo var4;
         while(true) {
            if (var2 >= var1.size()) {
               var4 = null;
               break;
            }

            CameraInfo var3 = (CameraInfo)var1.get(var2);
            if (this.isFrontface) {
               var4 = var3;
               if (var3.frontCamera != 0) {
                  break;
               }
            }

            if (!this.isFrontface && var3.frontCamera == 0) {
               var4 = var3;
               break;
            }

            ++var2;
         }

         if (var4 != null) {
            Point var13 = AndroidUtilities.displaySize;
            float var5 = (float)Math.max(var13.x, var13.y);
            var13 = AndroidUtilities.displaySize;
            var5 /= (float)Math.min(var13.x, var13.y);
            short var6;
            short var12;
            Size var14;
            if (this.initialFrontface) {
               var14 = new Size(16, 9);
               var12 = 480;
               var6 = 270;
            } else {
               if (Math.abs(var5 - 1.3333334F) < 0.1F) {
                  var14 = new Size(4, 3);
                  var12 = 960;
               } else {
                  var14 = new Size(16, 9);
                  var12 = 720;
               }

               short var7 = 1280;
               var6 = var12;
               var12 = var7;
            }

            if (this.textureView.getWidth() > 0 && this.textureView.getHeight() > 0) {
               Point var10 = AndroidUtilities.displaySize;
               int var8 = Math.min(var10.x, var10.y);
               int var15 = var14.getHeight() * var8 / var14.getWidth();
               this.previewSize = CameraController.chooseOptimalSize(var4.getPreviewSizes(), var8, var15, var14);
            }

            Size var11;
            label58: {
               var11 = CameraController.chooseOptimalSize(var4.getPictureSizes(), var12, var6, var14);
               if (var11.getWidth() >= 1280 && var11.getHeight() >= 1280) {
                  if (Math.abs(var5 - 1.3333334F) < 0.1F) {
                     var14 = new Size(3, 4);
                  } else {
                     var14 = new Size(9, 16);
                  }

                  Size var9 = CameraController.chooseOptimalSize(var4.getPictureSizes(), var6, var12, var14);
                  var14 = var9;
                  if (var9.getWidth() < 1280) {
                     break label58;
                  }

                  if (var9.getHeight() < 1280) {
                     var14 = var9;
                     break label58;
                  }
               }

               var14 = var11;
            }

            SurfaceTexture var16 = this.textureView.getSurfaceTexture();
            var11 = this.previewSize;
            if (var11 != null && var16 != null) {
               var16.setDefaultBufferSize(var11.getWidth(), this.previewSize.getHeight());
               this.cameraSession = new CameraSession(var4, this.previewSize, var14, 256);
               CameraController.getInstance().open(this.cameraSession, var16, new Runnable() {
                  public void run() {
                     if (CameraView.this.cameraSession != null) {
                        CameraView.this.cameraSession.setInitied();
                     }

                     CameraView.this.checkPreviewMatrix();
                  }
               }, new Runnable() {
                  public void run() {
                     if (CameraView.this.delegate != null) {
                        CameraView.this.delegate.onCameraCreated(CameraView.this.cameraSession.cameraInfo.camera);
                     }

                  }
               });
            }

         }
      }
   }

   public void destroy(boolean var1, Runnable var2) {
      CameraSession var3 = this.cameraSession;
      if (var3 != null) {
         var3.destroy();
         CameraController var4 = CameraController.getInstance();
         CameraSession var5 = this.cameraSession;
         CountDownLatch var6;
         if (!var1) {
            var6 = new CountDownLatch(1);
         } else {
            var6 = null;
         }

         var4.close(var5, var6, var2);
      }

   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      boolean var5 = super.drawChild(var1, var2, var3);
      if (this.focusProgress != 1.0F || this.innerAlpha != 0.0F || this.outerAlpha != 0.0F) {
         int var6;
         long var7;
         label35: {
            var6 = AndroidUtilities.dp(30.0F);
            var7 = System.currentTimeMillis();
            long var9 = var7 - this.lastDrawTime;
            if (var9 >= 0L) {
               var3 = var9;
               if (var9 <= 17L) {
                  break label35;
               }
            }

            var3 = 17L;
         }

         this.lastDrawTime = var7;
         this.outerPaint.setAlpha((int)(this.interpolator.getInterpolation(this.outerAlpha) * 255.0F));
         this.innerPaint.setAlpha((int)(this.interpolator.getInterpolation(this.innerAlpha) * 127.0F));
         float var11 = this.interpolator.getInterpolation(this.focusProgress);
         float var12 = (float)this.cx;
         float var13 = (float)this.cy;
         float var14 = (float)var6;
         var1.drawCircle(var12, var13, (1.0F - var11) * var14 + var14, this.outerPaint);
         var1.drawCircle((float)this.cx, (float)this.cy, var14 * var11, this.innerPaint);
         var11 = this.focusProgress;
         if (var11 < 1.0F) {
            this.focusProgress = var11 + (float)var3 / 200.0F;
            if (this.focusProgress > 1.0F) {
               this.focusProgress = 1.0F;
            }

            this.invalidate();
         } else {
            var11 = this.innerAlpha;
            if (var11 != 0.0F) {
               this.innerAlpha = var11 - (float)var3 / 150.0F;
               if (this.innerAlpha < 0.0F) {
                  this.innerAlpha = 0.0F;
               }

               this.invalidate();
            } else {
               var11 = this.outerAlpha;
               if (var11 != 0.0F) {
                  this.outerAlpha = var11 - (float)var3 / 150.0F;
                  if (this.outerAlpha < 0.0F) {
                     this.outerAlpha = 0.0F;
                  }

                  this.invalidate();
               }
            }
         }
      }

      return var5;
   }

   public void focusToPoint(int var1, int var2) {
      float var3 = (float)var1;
      float var4 = (float)var2;
      Rect var5 = this.calculateTapArea(var3, var4, 1.0F);
      Rect var6 = this.calculateTapArea(var3, var4, 1.5F);
      CameraSession var7 = this.cameraSession;
      if (var7 != null) {
         var7.focusToRect(var5, var6);
      }

      this.focusProgress = 0.0F;
      this.innerAlpha = 1.0F;
      this.outerAlpha = 1.0F;
      this.cx = var1;
      this.cy = var2;
      this.lastDrawTime = System.currentTimeMillis();
      this.invalidate();
   }

   public CameraSession getCameraSession() {
      return this.cameraSession;
   }

   public Size getPreviewSize() {
      return this.previewSize;
   }

   public boolean hasFrontFaceCamera() {
      ArrayList var1 = CameraController.getInstance().getCameras();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         if (((CameraInfo)var1.get(var2)).frontCamera != 0) {
            return true;
         }
      }

      return false;
   }

   public boolean isFrontface() {
      return this.isFrontface;
   }

   public boolean isInitied() {
      return this.initied;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.checkPreviewMatrix();
   }

   public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
      this.initCamera();
   }

   public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
      if (this.cameraSession != null) {
         CameraController.getInstance().close(this.cameraSession, (CountDownLatch)null, (Runnable)null);
      }

      return false;
   }

   public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
      this.checkPreviewMatrix();
   }

   public void onSurfaceTextureUpdated(SurfaceTexture var1) {
      if (!this.initied) {
         CameraSession var2 = this.cameraSession;
         if (var2 != null && var2.isInitied()) {
            CameraView.CameraViewDelegate var3 = this.delegate;
            if (var3 != null) {
               var3.onCameraInit();
            }

            this.initied = true;
         }
      }

   }

   public void setClipLeft(int var1) {
      this.clipLeft = var1;
   }

   public void setClipTop(int var1) {
      this.clipTop = var1;
   }

   public void setDelegate(CameraView.CameraViewDelegate var1) {
      this.delegate = var1;
   }

   public void setMirror(boolean var1) {
      this.mirror = var1;
   }

   public void switchCamera() {
      if (this.cameraSession != null) {
         CameraController.getInstance().close(this.cameraSession, (CountDownLatch)null, (Runnable)null);
         this.cameraSession = null;
      }

      this.initied = false;
      this.isFrontface ^= true;
      this.initCamera();
   }

   public interface CameraViewDelegate {
      void onCameraCreated(Camera var1);

      void onCameraInit();
   }
}

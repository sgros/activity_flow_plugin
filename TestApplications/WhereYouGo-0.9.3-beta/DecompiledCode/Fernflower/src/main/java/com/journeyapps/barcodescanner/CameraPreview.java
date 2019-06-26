package com.journeyapps.barcodescanner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.TextureView.SurfaceTextureListener;
import com.google.zxing.client.android.R;
import com.journeyapps.barcodescanner.camera.CameraInstance;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.journeyapps.barcodescanner.camera.CameraSurface;
import com.journeyapps.barcodescanner.camera.CenterCropStrategy;
import com.journeyapps.barcodescanner.camera.DisplayConfiguration;
import com.journeyapps.barcodescanner.camera.FitCenterStrategy;
import com.journeyapps.barcodescanner.camera.FitXYStrategy;
import com.journeyapps.barcodescanner.camera.PreviewScalingStrategy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CameraPreview extends ViewGroup {
   private static final int ROTATION_LISTENER_DELAY_MS = 250;
   private static final String TAG = CameraPreview.class.getSimpleName();
   private CameraInstance cameraInstance;
   private CameraSettings cameraSettings = new CameraSettings();
   private Size containerSize;
   private Size currentSurfaceSize;
   private DisplayConfiguration displayConfiguration;
   private final CameraPreview.StateListener fireState = new CameraPreview.StateListener() {
      public void cameraClosed() {
         Iterator var1 = CameraPreview.this.stateListeners.iterator();

         while(var1.hasNext()) {
            ((CameraPreview.StateListener)var1.next()).cameraClosed();
         }

      }

      public void cameraError(Exception var1) {
         Iterator var2 = CameraPreview.this.stateListeners.iterator();

         while(var2.hasNext()) {
            ((CameraPreview.StateListener)var2.next()).cameraError(var1);
         }

      }

      public void previewSized() {
         Iterator var1 = CameraPreview.this.stateListeners.iterator();

         while(var1.hasNext()) {
            ((CameraPreview.StateListener)var1.next()).previewSized();
         }

      }

      public void previewStarted() {
         Iterator var1 = CameraPreview.this.stateListeners.iterator();

         while(var1.hasNext()) {
            ((CameraPreview.StateListener)var1.next()).previewStarted();
         }

      }

      public void previewStopped() {
         Iterator var1 = CameraPreview.this.stateListeners.iterator();

         while(var1.hasNext()) {
            ((CameraPreview.StateListener)var1.next()).previewStopped();
         }

      }
   };
   private Rect framingRect = null;
   private Size framingRectSize = null;
   private double marginFraction = 0.1D;
   private int openedOrientation = -1;
   private boolean previewActive = false;
   private Rect previewFramingRect = null;
   private PreviewScalingStrategy previewScalingStrategy = null;
   private Size previewSize;
   private RotationCallback rotationCallback = new RotationCallback() {
      public void onRotationChanged(int var1) {
         CameraPreview.this.stateHandler.postDelayed(new Runnable() {
            public void run() {
               CameraPreview.this.rotationChanged();
            }
         }, 250L);
      }
   };
   private RotationListener rotationListener;
   private final Callback stateCallback = new Callback() {
      public boolean handleMessage(Message var1) {
         boolean var2;
         if (var1.what == R.id.zxing_prewiew_size_ready) {
            CameraPreview.this.previewSized((Size)var1.obj);
            var2 = true;
         } else {
            if (var1.what == R.id.zxing_camera_error) {
               Exception var3 = (Exception)var1.obj;
               if (CameraPreview.this.isActive()) {
                  CameraPreview.this.pause();
                  CameraPreview.this.fireState.cameraError(var3);
               }
            } else if (var1.what == R.id.zxing_camera_closed) {
               CameraPreview.this.fireState.cameraClosed();
            }

            var2 = false;
         }

         return var2;
      }
   };
   private Handler stateHandler;
   private List stateListeners = new ArrayList();
   private final android.view.SurfaceHolder.Callback surfaceCallback = new android.view.SurfaceHolder.Callback() {
      public void surfaceChanged(SurfaceHolder var1, int var2, int var3, int var4) {
         if (var1 == null) {
            Log.e(CameraPreview.TAG, "*** WARNING *** surfaceChanged() gave us a null surface!");
         } else {
            CameraPreview.this.currentSurfaceSize = new Size(var3, var4);
            CameraPreview.this.startPreviewIfReady();
         }

      }

      public void surfaceCreated(SurfaceHolder var1) {
      }

      public void surfaceDestroyed(SurfaceHolder var1) {
         CameraPreview.this.currentSurfaceSize = null;
      }
   };
   private Rect surfaceRect;
   private SurfaceView surfaceView;
   private TextureView textureView;
   private boolean torchOn = false;
   private boolean useTextureView = false;
   private WindowManager windowManager;

   public CameraPreview(Context var1) {
      super(var1);
      this.initialize(var1, (AttributeSet)null, 0, 0);
   }

   public CameraPreview(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.initialize(var1, var2, 0, 0);
   }

   public CameraPreview(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.initialize(var1, var2, var3, 0);
   }

   private void calculateFrames() {
      if (this.containerSize != null && this.previewSize != null && this.displayConfiguration != null) {
         int var1 = this.previewSize.width;
         int var2 = this.previewSize.height;
         int var3 = this.containerSize.width;
         int var4 = this.containerSize.height;
         this.surfaceRect = this.displayConfiguration.scalePreview(this.previewSize);
         this.framingRect = this.calculateFramingRect(new Rect(0, 0, var3, var4), this.surfaceRect);
         Rect var5 = new Rect(this.framingRect);
         var5.offset(-this.surfaceRect.left, -this.surfaceRect.top);
         this.previewFramingRect = new Rect(var5.left * var1 / this.surfaceRect.width(), var5.top * var2 / this.surfaceRect.height(), var5.right * var1 / this.surfaceRect.width(), var5.bottom * var2 / this.surfaceRect.height());
         if (this.previewFramingRect.width() > 0 && this.previewFramingRect.height() > 0) {
            this.fireState.previewSized();
         } else {
            this.previewFramingRect = null;
            this.framingRect = null;
            Log.w(TAG, "Preview frame is too small");
         }

      } else {
         this.previewFramingRect = null;
         this.framingRect = null;
         this.surfaceRect = null;
         throw new IllegalStateException("containerSize or previewSize is not set yet");
      }
   }

   private void containerSized(Size var1) {
      this.containerSize = var1;
      if (this.cameraInstance != null && this.cameraInstance.getDisplayConfiguration() == null) {
         this.displayConfiguration = new DisplayConfiguration(this.getDisplayRotation(), var1);
         this.displayConfiguration.setPreviewScalingStrategy(this.getPreviewScalingStrategy());
         this.cameraInstance.setDisplayConfiguration(this.displayConfiguration);
         this.cameraInstance.configureCamera();
         if (this.torchOn) {
            this.cameraInstance.setTorch(this.torchOn);
         }
      }

   }

   private int getDisplayRotation() {
      return this.windowManager.getDefaultDisplay().getRotation();
   }

   private void initCamera() {
      if (this.cameraInstance != null) {
         Log.w(TAG, "initCamera called twice");
      } else {
         this.cameraInstance = this.createCameraInstance();
         this.cameraInstance.setReadyHandler(this.stateHandler);
         this.cameraInstance.open();
         this.openedOrientation = this.getDisplayRotation();
      }

   }

   private void initialize(Context var1, AttributeSet var2, int var3, int var4) {
      if (this.getBackground() == null) {
         this.setBackgroundColor(-16777216);
      }

      this.initializeAttributes(var2);
      this.windowManager = (WindowManager)var1.getSystemService("window");
      this.stateHandler = new Handler(this.stateCallback);
      this.rotationListener = new RotationListener();
   }

   private void previewSized(Size var1) {
      this.previewSize = var1;
      if (this.containerSize != null) {
         this.calculateFrames();
         this.requestLayout();
         this.startPreviewIfReady();
      }

   }

   private void rotationChanged() {
      if (this.isActive() && this.getDisplayRotation() != this.openedOrientation) {
         this.pause();
         this.resume();
      }

   }

   @SuppressLint({"NewAPI"})
   private void setupSurfaceView() {
      if (this.useTextureView && VERSION.SDK_INT >= 14) {
         this.textureView = new TextureView(this.getContext());
         this.textureView.setSurfaceTextureListener(this.surfaceTextureListener());
         this.addView(this.textureView);
      } else {
         this.surfaceView = new SurfaceView(this.getContext());
         if (VERSION.SDK_INT < 11) {
            this.surfaceView.getHolder().setType(3);
         }

         this.surfaceView.getHolder().addCallback(this.surfaceCallback);
         this.addView(this.surfaceView);
      }

   }

   private void startCameraPreview(CameraSurface var1) {
      if (!this.previewActive && this.cameraInstance != null) {
         Log.i(TAG, "Starting preview");
         this.cameraInstance.setSurface(var1);
         this.cameraInstance.startPreview();
         this.previewActive = true;
         this.previewStarted();
         this.fireState.previewStarted();
      }

   }

   private void startPreviewIfReady() {
      if (this.currentSurfaceSize != null && this.previewSize != null && this.surfaceRect != null) {
         if (this.surfaceView != null && this.currentSurfaceSize.equals(new Size(this.surfaceRect.width(), this.surfaceRect.height()))) {
            this.startCameraPreview(new CameraSurface(this.surfaceView.getHolder()));
         } else if (this.textureView != null && VERSION.SDK_INT >= 14 && this.textureView.getSurfaceTexture() != null) {
            if (this.previewSize != null) {
               Matrix var1 = this.calculateTextureTransform(new Size(this.textureView.getWidth(), this.textureView.getHeight()), this.previewSize);
               this.textureView.setTransform(var1);
            }

            this.startCameraPreview(new CameraSurface(this.textureView.getSurfaceTexture()));
         }
      }

   }

   @TargetApi(14)
   private SurfaceTextureListener surfaceTextureListener() {
      return new SurfaceTextureListener() {
         public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
            this.onSurfaceTextureSizeChanged(var1, var2, var3);
         }

         public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
            return false;
         }

         public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
            CameraPreview.this.currentSurfaceSize = new Size(var2, var3);
            CameraPreview.this.startPreviewIfReady();
         }

         public void onSurfaceTextureUpdated(SurfaceTexture var1) {
         }
      };
   }

   public void addStateListener(CameraPreview.StateListener var1) {
      this.stateListeners.add(var1);
   }

   protected Rect calculateFramingRect(Rect var1, Rect var2) {
      var1 = new Rect(var1);
      var1.intersect(var2);
      if (this.framingRectSize != null) {
         var1.inset(Math.max(0, (var1.width() - this.framingRectSize.width) / 2), Math.max(0, (var1.height() - this.framingRectSize.height) / 2));
      } else {
         int var3 = (int)Math.min((double)var1.width() * this.marginFraction, (double)var1.height() * this.marginFraction);
         var1.inset(var3, var3);
         if (var1.height() > var1.width()) {
            var1.inset(0, (var1.height() - var1.width()) / 2);
         }
      }

      return var1;
   }

   protected Matrix calculateTextureTransform(Size var1, Size var2) {
      float var3 = (float)var1.width / (float)var1.height;
      float var4 = (float)var2.width / (float)var2.height;
      float var5;
      if (var3 < var4) {
         var5 = var4 / var3;
         var4 = 1.0F;
      } else {
         var5 = 1.0F;
         var4 = var3 / var4;
      }

      Matrix var7 = new Matrix();
      var7.setScale(var5, var4);
      float var6 = (float)var1.width;
      var3 = (float)var1.height;
      var7.postTranslate(((float)var1.width - var6 * var5) / 2.0F, ((float)var1.height - var3 * var4) / 2.0F);
      return var7;
   }

   protected CameraInstance createCameraInstance() {
      CameraInstance var1 = new CameraInstance(this.getContext());
      var1.setCameraSettings(this.cameraSettings);
      return var1;
   }

   public CameraInstance getCameraInstance() {
      return this.cameraInstance;
   }

   public CameraSettings getCameraSettings() {
      return this.cameraSettings;
   }

   public Rect getFramingRect() {
      return this.framingRect;
   }

   public Size getFramingRectSize() {
      return this.framingRectSize;
   }

   public double getMarginFraction() {
      return this.marginFraction;
   }

   public Rect getPreviewFramingRect() {
      return this.previewFramingRect;
   }

   public PreviewScalingStrategy getPreviewScalingStrategy() {
      Object var1;
      if (this.previewScalingStrategy != null) {
         var1 = this.previewScalingStrategy;
      } else if (this.textureView != null) {
         var1 = new CenterCropStrategy();
      } else {
         var1 = new FitCenterStrategy();
      }

      return (PreviewScalingStrategy)var1;
   }

   protected void initializeAttributes(AttributeSet var1) {
      TypedArray var4 = this.getContext().obtainStyledAttributes(var1, R.styleable.zxing_camera_preview);
      int var2 = (int)var4.getDimension(R.styleable.zxing_camera_preview_zxing_framing_rect_width, -1.0F);
      int var3 = (int)var4.getDimension(R.styleable.zxing_camera_preview_zxing_framing_rect_height, -1.0F);
      if (var2 > 0 && var3 > 0) {
         this.framingRectSize = new Size(var2, var3);
      }

      this.useTextureView = var4.getBoolean(R.styleable.zxing_camera_preview_zxing_use_texture_view, true);
      var3 = var4.getInteger(R.styleable.zxing_camera_preview_zxing_preview_scaling_strategy, -1);
      if (var3 == 1) {
         this.previewScalingStrategy = new CenterCropStrategy();
      } else if (var3 == 2) {
         this.previewScalingStrategy = new FitCenterStrategy();
      } else if (var3 == 3) {
         this.previewScalingStrategy = new FitXYStrategy();
      }

      var4.recycle();
   }

   protected boolean isActive() {
      boolean var1;
      if (this.cameraInstance != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isCameraClosed() {
      boolean var1;
      if (this.cameraInstance != null && !this.cameraInstance.isCameraClosed()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isPreviewActive() {
      return this.previewActive;
   }

   public boolean isUseTextureView() {
      return this.useTextureView;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.setupSurfaceView();
   }

   @SuppressLint({"DrawAllocation"})
   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.containerSized(new Size(var4 - var2, var5 - var3));
      if (this.surfaceView != null) {
         if (this.surfaceRect == null) {
            this.surfaceView.layout(0, 0, this.getWidth(), this.getHeight());
         } else {
            this.surfaceView.layout(this.surfaceRect.left, this.surfaceRect.top, this.surfaceRect.right, this.surfaceRect.bottom);
         }
      } else if (this.textureView != null && VERSION.SDK_INT >= 14) {
         this.textureView.layout(0, 0, this.getWidth(), this.getHeight());
      }

   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof Bundle)) {
         super.onRestoreInstanceState(var1);
      } else {
         Bundle var2 = (Bundle)var1;
         super.onRestoreInstanceState(var2.getParcelable("super"));
         this.setTorch(var2.getBoolean("torch"));
      }

   }

   protected Parcelable onSaveInstanceState() {
      Parcelable var1 = super.onSaveInstanceState();
      Bundle var2 = new Bundle();
      var2.putParcelable("super", var1);
      var2.putBoolean("torch", this.torchOn);
      return var2;
   }

   public void pause() {
      Util.validateMainThread();
      Log.d(TAG, "pause()");
      this.openedOrientation = -1;
      if (this.cameraInstance != null) {
         this.cameraInstance.close();
         this.cameraInstance = null;
         this.previewActive = false;
      } else {
         this.stateHandler.sendEmptyMessage(R.id.zxing_camera_closed);
      }

      if (this.currentSurfaceSize == null && this.surfaceView != null) {
         this.surfaceView.getHolder().removeCallback(this.surfaceCallback);
      }

      if (this.currentSurfaceSize == null && this.textureView != null && VERSION.SDK_INT >= 14) {
         this.textureView.setSurfaceTextureListener((SurfaceTextureListener)null);
      }

      this.containerSize = null;
      this.previewSize = null;
      this.previewFramingRect = null;
      this.rotationListener.stop();
      this.fireState.previewStopped();
   }

   public void pauseAndWait() {
      CameraInstance var1 = this.getCameraInstance();
      this.pause();
      long var2 = System.nanoTime();

      while(var1 != null && !var1.isCameraClosed() && System.nanoTime() - var2 <= 2000000000L) {
         try {
            Thread.sleep(1L);
         } catch (InterruptedException var4) {
            break;
         }
      }

   }

   protected void previewStarted() {
   }

   public void resume() {
      Util.validateMainThread();
      Log.d(TAG, "resume()");
      this.initCamera();
      if (this.currentSurfaceSize != null) {
         this.startPreviewIfReady();
      } else if (this.surfaceView != null) {
         this.surfaceView.getHolder().addCallback(this.surfaceCallback);
      } else if (this.textureView != null && VERSION.SDK_INT >= 14) {
         if (this.textureView.isAvailable()) {
            this.surfaceTextureListener().onSurfaceTextureAvailable(this.textureView.getSurfaceTexture(), this.textureView.getWidth(), this.textureView.getHeight());
         } else {
            this.textureView.setSurfaceTextureListener(this.surfaceTextureListener());
         }
      }

      this.requestLayout();
      this.rotationListener.listen(this.getContext(), this.rotationCallback);
   }

   public void setCameraSettings(CameraSettings var1) {
      this.cameraSettings = var1;
   }

   public void setFramingRectSize(Size var1) {
      this.framingRectSize = var1;
   }

   public void setMarginFraction(double var1) {
      if (var1 >= 0.5D) {
         throw new IllegalArgumentException("The margin fraction must be less than 0.5");
      } else {
         this.marginFraction = var1;
      }
   }

   public void setPreviewScalingStrategy(PreviewScalingStrategy var1) {
      this.previewScalingStrategy = var1;
   }

   public void setTorch(boolean var1) {
      this.torchOn = var1;
      if (this.cameraInstance != null) {
         this.cameraInstance.setTorch(var1);
      }

   }

   public void setUseTextureView(boolean var1) {
      this.useTextureView = var1;
   }

   public interface StateListener {
      void cameraClosed();

      void cameraError(Exception var1);

      void previewSized();

      void previewStarted();

      void previewStopped();
   }
}

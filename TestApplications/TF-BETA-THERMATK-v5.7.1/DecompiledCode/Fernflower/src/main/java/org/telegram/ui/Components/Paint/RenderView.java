package org.telegram.ui.Components.Paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import java.util.concurrent.CountDownLatch;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.ui.Components.Size;

public class RenderView extends TextureView {
   private Bitmap bitmap;
   private Brush brush;
   private int color;
   private RenderView.RenderViewDelegate delegate;
   private Input input;
   private RenderView.CanvasInternal internal;
   private int orientation;
   private Painting painting;
   private DispatchQueue queue;
   private boolean shuttingDown;
   private boolean transformedBitmap;
   private UndoStore undoStore;
   private float weight;

   public RenderView(Context var1, Painting var2, Bitmap var3, int var4) {
      super(var1);
      this.bitmap = var3;
      this.orientation = var4;
      this.painting = var2;
      this.painting.setRenderView(this);
      this.setSurfaceTextureListener(new SurfaceTextureListener() {
         public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
            if (var1 != null && RenderView.this.internal == null) {
               RenderView var4 = RenderView.this;
               var4.internal = var4.new CanvasInternal(var1);
               RenderView.this.internal.setBufferSize(var2, var3);
               RenderView.this.updateTransform();
               RenderView.this.internal.requestRender();
               if (RenderView.this.painting.isPaused()) {
                  RenderView.this.painting.onResume();
               }
            }

         }

         public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
            if (RenderView.this.internal == null) {
               return true;
            } else {
               if (!RenderView.this.shuttingDown) {
                  RenderView.this.painting.onPause(new Runnable() {
                     public void run() {
                        RenderView.this.internal.shutdown();
                        RenderView.this.internal = null;
                     }
                  });
               }

               return true;
            }
         }

         public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
            if (RenderView.this.internal != null) {
               RenderView.this.internal.setBufferSize(var2, var3);
               RenderView.this.updateTransform();
               RenderView.this.internal.requestRender();
               RenderView.this.internal.postRunnable(new Runnable() {
                  public void run() {
                     if (RenderView.this.internal != null) {
                        RenderView.this.internal.requestRender();
                     }

                  }
               });
            }
         }

         public void onSurfaceTextureUpdated(SurfaceTexture var1) {
         }
      });
      this.input = new Input(this);
      this.painting.setDelegate(new Painting.PaintingDelegate() {
         public void contentChanged(RectF var1) {
            if (RenderView.this.internal != null) {
               RenderView.this.internal.scheduleRedraw();
            }

         }

         public DispatchQueue requestDispatchQueue() {
            return RenderView.this.queue;
         }

         public UndoStore requestUndoStore() {
            return RenderView.this.undoStore;
         }

         public void strokeCommited() {
         }
      });
   }

   private float brushWeightForSize(float var1) {
      float var2 = this.painting.getSize().width;
      return 0.00390625F * var2 + var2 * 0.043945312F * var1;
   }

   private void updateTransform() {
      Matrix var1 = new Matrix();
      float var2;
      if (this.painting != null) {
         var2 = (float)this.getWidth() / this.painting.getSize().width;
      } else {
         var2 = 1.0F;
      }

      float var3 = var2;
      if (var2 <= 0.0F) {
         var3 = 1.0F;
      }

      Size var4 = this.getPainting().getSize();
      var1.preTranslate((float)this.getWidth() / 2.0F, (float)this.getHeight() / 2.0F);
      var1.preScale(var3, -var3);
      var1.preTranslate(-var4.width / 2.0F, -var4.height / 2.0F);
      this.input.setMatrix(var1);
      float[] var5 = GLMatrix.MultiplyMat4f(GLMatrix.LoadOrtho(0.0F, (float)this.internal.bufferWidth, 0.0F, (float)this.internal.bufferHeight, -1.0F, 1.0F), GLMatrix.LoadGraphicsMatrix(var1));
      this.painting.setRenderProjection(var5);
   }

   public Brush getCurrentBrush() {
      return this.brush;
   }

   public int getCurrentColor() {
      return this.color;
   }

   public float getCurrentWeight() {
      return this.weight;
   }

   public Painting getPainting() {
      return this.painting;
   }

   public Bitmap getResultBitmap() {
      RenderView.CanvasInternal var1 = this.internal;
      Bitmap var2;
      if (var1 != null) {
         var2 = var1.getTexture();
      } else {
         var2 = null;
      }

      return var2;
   }

   public void onBeganDrawing() {
      RenderView.RenderViewDelegate var1 = this.delegate;
      if (var1 != null) {
         var1.onBeganDrawing();
      }

   }

   public void onFinishedDrawing(boolean var1) {
      RenderView.RenderViewDelegate var2 = this.delegate;
      if (var2 != null) {
         var2.onFinishedDrawing(var1);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (var1.getPointerCount() > 1) {
         return false;
      } else {
         RenderView.CanvasInternal var2 = this.internal;
         if (var2 != null && var2.initialized && this.internal.ready) {
            this.input.process(var1);
         }

         return true;
      }
   }

   public void performInContext(final Runnable var1) {
      RenderView.CanvasInternal var2 = this.internal;
      if (var2 != null) {
         var2.postRunnable(new Runnable() {
            public void run() {
               if (RenderView.this.internal != null && RenderView.this.internal.initialized) {
                  RenderView.this.internal.setCurrentContext();
                  var1.run();
               }

            }
         });
      }
   }

   public void setBrush(Brush var1) {
      Painting var2 = this.painting;
      this.brush = var1;
      var2.setBrush(var1);
   }

   public void setBrushSize(float var1) {
      this.weight = this.brushWeightForSize(var1);
   }

   public void setColor(int var1) {
      this.color = var1;
   }

   public void setDelegate(RenderView.RenderViewDelegate var1) {
      this.delegate = var1;
   }

   public void setQueue(DispatchQueue var1) {
      this.queue = var1;
   }

   public void setUndoStore(UndoStore var1) {
      this.undoStore = var1;
   }

   public boolean shouldDraw() {
      RenderView.RenderViewDelegate var1 = this.delegate;
      boolean var2;
      if (var1 != null && !var1.shouldDraw()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void shutdown() {
      this.shuttingDown = true;
      if (this.internal != null) {
         this.performInContext(new Runnable() {
            public void run() {
               RenderView.this.painting.cleanResources(RenderView.this.transformedBitmap);
               RenderView.this.internal.shutdown();
               RenderView.this.internal = null;
            }
         });
      }

      this.setVisibility(8);
   }

   private class CanvasInternal extends DispatchQueue {
      private final int EGL_CONTEXT_CLIENT_VERSION = 12440;
      private final int EGL_OPENGL_ES2_BIT = 4;
      private int bufferHeight;
      private int bufferWidth;
      private Runnable drawRunnable = new Runnable() {
         public void run() {
            if (CanvasInternal.this.initialized && !RenderView.this.shuttingDown) {
               CanvasInternal.this.setCurrentContext();
               GLES20.glBindFramebuffer(36160, 0);
               GLES20.glViewport(0, 0, CanvasInternal.this.bufferWidth, CanvasInternal.this.bufferHeight);
               GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
               GLES20.glClear(16384);
               RenderView.this.painting.render();
               GLES20.glBlendFunc(1, 771);
               CanvasInternal.this.egl10.eglSwapBuffers(CanvasInternal.this.eglDisplay, CanvasInternal.this.eglSurface);
               if (!CanvasInternal.this.ready) {
                  RenderView.this.queue.postRunnable(new Runnable() {
                     public void run() {
                        CanvasInternal.this.ready = true;
                     }
                  }, 200L);
               }
            }

         }
      };
      private EGL10 egl10;
      private EGLConfig eglConfig;
      private EGLContext eglContext;
      private EGLDisplay eglDisplay;
      private EGLSurface eglSurface;
      private boolean initialized;
      private long lastRenderCallTime;
      private boolean ready;
      private Runnable scheduledRunnable;
      private SurfaceTexture surfaceTexture;

      public CanvasInternal(SurfaceTexture var2) {
         super("CanvasInternal");
         this.surfaceTexture = var2;
      }

      private void checkBitmap() {
         Size var1 = RenderView.this.painting.getSize();
         if ((float)RenderView.this.bitmap.getWidth() != var1.width || (float)RenderView.this.bitmap.getHeight() != var1.height || RenderView.this.orientation != 0) {
            float var2 = (float)RenderView.this.bitmap.getWidth();
            if (RenderView.this.orientation % 360 == 90 || RenderView.this.orientation % 360 == 270) {
               var2 = (float)RenderView.this.bitmap.getHeight();
            }

            var2 = var1.width / var2;
            RenderView var3 = RenderView.this;
            var3.bitmap = this.createBitmap(var3.bitmap, var2);
            RenderView.this.orientation = 0;
            RenderView.this.transformedBitmap = true;
         }

      }

      private Bitmap createBitmap(Bitmap var1, float var2) {
         Matrix var3 = new Matrix();
         var3.setScale(var2, var2);
         var3.postRotate((float)RenderView.this.orientation);
         return Bitmap.createBitmap(var1, 0, 0, var1.getWidth(), var1.getHeight(), var3, true);
      }

      private boolean initGL() {
         this.egl10 = (EGL10)EGLContext.getEGL();
         this.eglDisplay = this.egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
         EGLDisplay var1 = this.eglDisplay;
         StringBuilder var7;
         if (var1 == EGL10.EGL_NO_DISPLAY) {
            if (BuildVars.LOGS_ENABLED) {
               var7 = new StringBuilder();
               var7.append("eglGetDisplay failed ");
               var7.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
               FileLog.e(var7.toString());
            }

            this.finish();
            return false;
         } else {
            int[] var2 = new int[2];
            if (!this.egl10.eglInitialize(var1, var2)) {
               if (BuildVars.LOGS_ENABLED) {
                  var7 = new StringBuilder();
                  var7.append("eglInitialize failed ");
                  var7.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                  FileLog.e(var7.toString());
               }

               this.finish();
               return false;
            } else {
               int[] var3 = new int[1];
               EGLConfig[] var6 = new EGLConfig[1];
               if (!this.egl10.eglChooseConfig(this.eglDisplay, new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 0, 12326, 0, 12344}, var6, 1, var3)) {
                  if (BuildVars.LOGS_ENABLED) {
                     var7 = new StringBuilder();
                     var7.append("eglChooseConfig failed ");
                     var7.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                     FileLog.e(var7.toString());
                  }

                  this.finish();
                  return false;
               } else if (var3[0] > 0) {
                  this.eglConfig = var6[0];
                  this.eglContext = this.egl10.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
                  if (this.eglContext == null) {
                     if (BuildVars.LOGS_ENABLED) {
                        var7 = new StringBuilder();
                        var7.append("eglCreateContext failed ");
                        var7.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                        FileLog.e(var7.toString());
                     }

                     this.finish();
                     return false;
                  } else {
                     SurfaceTexture var4 = this.surfaceTexture;
                     if (var4 instanceof SurfaceTexture) {
                        this.eglSurface = this.egl10.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, var4, (int[])null);
                        EGLSurface var5 = this.eglSurface;
                        if (var5 != null && var5 != EGL10.EGL_NO_SURFACE) {
                           if (!this.egl10.eglMakeCurrent(this.eglDisplay, var5, var5, this.eglContext)) {
                              if (BuildVars.LOGS_ENABLED) {
                                 var7 = new StringBuilder();
                                 var7.append("eglMakeCurrent failed ");
                                 var7.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                                 FileLog.e(var7.toString());
                              }

                              this.finish();
                              return false;
                           } else {
                              GLES20.glEnable(3042);
                              GLES20.glDisable(3024);
                              GLES20.glDisable(2960);
                              GLES20.glDisable(2929);
                              RenderView.this.painting.setupShaders();
                              this.checkBitmap();
                              RenderView.this.painting.setBitmap(RenderView.this.bitmap);
                              Utils.HasGLError();
                              return true;
                           }
                        } else {
                           if (BuildVars.LOGS_ENABLED) {
                              var7 = new StringBuilder();
                              var7.append("createWindowSurface failed ");
                              var7.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                              FileLog.e(var7.toString());
                           }

                           this.finish();
                           return false;
                        }
                     } else {
                        this.finish();
                        return false;
                     }
                  }
               } else {
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.e("eglConfig not initialized");
                  }

                  this.finish();
                  return false;
               }
            }
         }
      }

      private boolean setCurrentContext() {
         if (!this.initialized) {
            return false;
         } else {
            if (!this.eglContext.equals(this.egl10.eglGetCurrentContext()) || !this.eglSurface.equals(this.egl10.eglGetCurrentSurface(12377))) {
               EGL10 var1 = this.egl10;
               EGLDisplay var2 = this.eglDisplay;
               EGLSurface var3 = this.eglSurface;
               if (!var1.eglMakeCurrent(var2, var3, var3, this.eglContext)) {
                  return false;
               }
            }

            return true;
         }
      }

      public void finish() {
         if (this.eglSurface != null) {
            EGL10 var1 = this.egl10;
            EGLDisplay var2 = this.eglDisplay;
            EGLSurface var3 = EGL10.EGL_NO_SURFACE;
            var1.eglMakeCurrent(var2, var3, var3, EGL10.EGL_NO_CONTEXT);
            this.egl10.eglDestroySurface(this.eglDisplay, this.eglSurface);
            this.eglSurface = null;
         }

         EGLContext var4 = this.eglContext;
         if (var4 != null) {
            this.egl10.eglDestroyContext(this.eglDisplay, var4);
            this.eglContext = null;
         }

         EGLDisplay var5 = this.eglDisplay;
         if (var5 != null) {
            this.egl10.eglTerminate(var5);
            this.eglDisplay = null;
         }

      }

      public Bitmap getTexture() {
         if (!this.initialized) {
            return null;
         } else {
            final CountDownLatch var1 = new CountDownLatch(1);
            final Bitmap[] var2 = new Bitmap[1];

            try {
               Runnable var3 = new Runnable() {
                  public void run() {
                     Painting.PaintingData var1x = RenderView.this.painting.getPaintingData(new RectF(0.0F, 0.0F, RenderView.this.painting.getSize().width, RenderView.this.painting.getSize().height), false);
                     var2[0] = var1x.bitmap;
                     var1.countDown();
                  }
               };
               this.postRunnable(var3);
               var1.await();
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }

            return var2[0];
         }
      }

      public void requestRender() {
         this.postRunnable(new Runnable() {
            public void run() {
               CanvasInternal.this.drawRunnable.run();
            }
         });
      }

      public void run() {
         if (RenderView.this.bitmap != null && !RenderView.this.bitmap.isRecycled()) {
            this.initialized = this.initGL();
            super.run();
         }

      }

      public void scheduleRedraw() {
         Runnable var1 = this.scheduledRunnable;
         if (var1 != null) {
            this.cancelRunnable(var1);
            this.scheduledRunnable = null;
         }

         this.scheduledRunnable = new Runnable() {
            public void run() {
               CanvasInternal.this.scheduledRunnable = null;
               CanvasInternal.this.drawRunnable.run();
            }
         };
         this.postRunnable(this.scheduledRunnable, 1L);
      }

      public void setBufferSize(int var1, int var2) {
         this.bufferWidth = var1;
         this.bufferHeight = var2;
      }

      public void shutdown() {
         this.postRunnable(new Runnable() {
            public void run() {
               CanvasInternal.this.finish();
               Looper var1 = Looper.myLooper();
               if (var1 != null) {
                  var1.quit();
               }

            }
         });
      }
   }

   public interface RenderViewDelegate {
      void onBeganDrawing();

      void onFinishedDrawing(boolean var1);

      boolean shouldDraw();
   }
}

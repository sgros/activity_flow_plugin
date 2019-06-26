package com.google.android.exoplayer2.util;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Handler;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@TargetApi(17)
public final class EGLSurfaceTexture implements OnFrameAvailableListener, Runnable {
   private static final int[] EGL_CONFIG_ATTRIBUTES = new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 0, 12327, 12344, 12339, 4, 12344};
   private static final int EGL_PROTECTED_CONTENT_EXT = 12992;
   private static final int EGL_SURFACE_HEIGHT = 1;
   private static final int EGL_SURFACE_WIDTH = 1;
   public static final int SECURE_MODE_NONE = 0;
   public static final int SECURE_MODE_PROTECTED_PBUFFER = 2;
   public static final int SECURE_MODE_SURFACELESS_CONTEXT = 1;
   private final EGLSurfaceTexture.TextureImageListener callback;
   private EGLContext context;
   private EGLDisplay display;
   private final Handler handler;
   private EGLSurface surface;
   private SurfaceTexture texture;
   private final int[] textureIdHolder;

   public EGLSurfaceTexture(Handler var1) {
      this(var1, (EGLSurfaceTexture.TextureImageListener)null);
   }

   public EGLSurfaceTexture(Handler var1, EGLSurfaceTexture.TextureImageListener var2) {
      this.handler = var1;
      this.callback = var2;
      this.textureIdHolder = new int[1];
   }

   private static EGLConfig chooseEGLConfig(EGLDisplay var0) {
      EGLConfig[] var1 = new EGLConfig[1];
      int[] var2 = new int[1];
      boolean var3 = EGL14.eglChooseConfig(var0, EGL_CONFIG_ATTRIBUTES, 0, var1, 0, 1, var2, 0);
      if (var3 && var2[0] > 0 && var1[0] != null) {
         return var1[0];
      } else {
         throw new EGLSurfaceTexture.GlException(Util.formatInvariant("eglChooseConfig failed: success=%b, numConfigs[0]=%d, configs[0]=%s", var3, var2[0], var1[0]));
      }
   }

   private static EGLContext createEGLContext(EGLDisplay var0, EGLConfig var1, int var2) {
      int[] var3;
      if (var2 == 0) {
         var3 = new int[]{12440, 2, 12344};
      } else {
         var3 = new int[]{12440, 2, 12992, 1, 12344};
      }

      EGLContext var4 = EGL14.eglCreateContext(var0, var1, EGL14.EGL_NO_CONTEXT, var3, 0);
      if (var4 != null) {
         return var4;
      } else {
         throw new EGLSurfaceTexture.GlException("eglCreateContext failed");
      }
   }

   private static EGLSurface createEGLSurface(EGLDisplay var0, EGLConfig var1, EGLContext var2, int var3) {
      EGLSurface var5;
      if (var3 == 1) {
         var5 = EGL14.EGL_NO_SURFACE;
      } else {
         int[] var4;
         if (var3 == 2) {
            var4 = new int[]{12375, 1, 12374, 1, 12992, 1, 12344};
         } else {
            var4 = new int[]{12375, 1, 12374, 1, 12344};
         }

         var5 = EGL14.eglCreatePbufferSurface(var0, var1, var4, 0);
         if (var5 == null) {
            throw new EGLSurfaceTexture.GlException("eglCreatePbufferSurface failed");
         }
      }

      if (EGL14.eglMakeCurrent(var0, var5, var5, var2)) {
         return var5;
      } else {
         throw new EGLSurfaceTexture.GlException("eglMakeCurrent failed");
      }
   }

   private void dispatchOnFrameAvailable() {
      EGLSurfaceTexture.TextureImageListener var1 = this.callback;
      if (var1 != null) {
         var1.onFrameAvailable();
      }

   }

   private static void generateTextureIds(int[] var0) {
      GLES20.glGenTextures(1, var0, 0);
      GlUtil.checkGlError();
   }

   private static EGLDisplay getDefaultDisplay() {
      EGLDisplay var0 = EGL14.eglGetDisplay(0);
      if (var0 != null) {
         int[] var1 = new int[2];
         if (EGL14.eglInitialize(var0, var1, 0, var1, 1)) {
            return var0;
         } else {
            throw new EGLSurfaceTexture.GlException("eglInitialize failed");
         }
      } else {
         throw new EGLSurfaceTexture.GlException("eglGetDisplay failed");
      }
   }

   public SurfaceTexture getSurfaceTexture() {
      SurfaceTexture var1 = this.texture;
      Assertions.checkNotNull(var1);
      return (SurfaceTexture)var1;
   }

   public void init(int var1) {
      this.display = getDefaultDisplay();
      EGLConfig var2 = chooseEGLConfig(this.display);
      this.context = createEGLContext(this.display, var2, var1);
      this.surface = createEGLSurface(this.display, var2, this.context, var1);
      generateTextureIds(this.textureIdHolder);
      this.texture = new SurfaceTexture(this.textureIdHolder[0]);
      this.texture.setOnFrameAvailableListener(this);
   }

   public void onFrameAvailable(SurfaceTexture var1) {
      this.handler.post(this);
   }

   public void release() {
      this.handler.removeCallbacks(this);
      boolean var5 = false;

      EGLDisplay var2;
      try {
         var5 = true;
         if (this.texture != null) {
            this.texture.release();
            GLES20.glDeleteTextures(1, this.textureIdHolder, 0);
            var5 = false;
         } else {
            var5 = false;
         }
      } finally {
         if (var5) {
            var2 = this.display;
            EGLSurface var9;
            if (var2 != null && !var2.equals(EGL14.EGL_NO_DISPLAY)) {
               EGLDisplay var3 = this.display;
               var9 = EGL14.EGL_NO_SURFACE;
               EGL14.eglMakeCurrent(var3, var9, var9, EGL14.EGL_NO_CONTEXT);
            }

            var9 = this.surface;
            if (var9 != null && !var9.equals(EGL14.EGL_NO_SURFACE)) {
               EGL14.eglDestroySurface(this.display, this.surface);
            }

            EGLContext var10 = this.context;
            if (var10 != null) {
               EGL14.eglDestroyContext(this.display, var10);
            }

            if (Util.SDK_INT >= 19) {
               EGL14.eglReleaseThread();
            }

            var2 = this.display;
            if (var2 != null && !var2.equals(EGL14.EGL_NO_DISPLAY)) {
               EGL14.eglTerminate(this.display);
            }

            this.display = null;
            this.context = null;
            this.surface = null;
            this.texture = null;
         }
      }

      EGLDisplay var1 = this.display;
      EGLSurface var7;
      if (var1 != null && !var1.equals(EGL14.EGL_NO_DISPLAY)) {
         var2 = this.display;
         var7 = EGL14.EGL_NO_SURFACE;
         EGL14.eglMakeCurrent(var2, var7, var7, EGL14.EGL_NO_CONTEXT);
      }

      var7 = this.surface;
      if (var7 != null && !var7.equals(EGL14.EGL_NO_SURFACE)) {
         EGL14.eglDestroySurface(this.display, this.surface);
      }

      EGLContext var8 = this.context;
      if (var8 != null) {
         EGL14.eglDestroyContext(this.display, var8);
      }

      if (Util.SDK_INT >= 19) {
         EGL14.eglReleaseThread();
      }

      var1 = this.display;
      if (var1 != null && !var1.equals(EGL14.EGL_NO_DISPLAY)) {
         EGL14.eglTerminate(this.display);
      }

      this.display = null;
      this.context = null;
      this.surface = null;
      this.texture = null;
   }

   public void run() {
      this.dispatchOnFrameAvailable();
      SurfaceTexture var1 = this.texture;
      if (var1 != null) {
         try {
            var1.updateTexImage();
         } catch (RuntimeException var2) {
         }
      }

   }

   public static final class GlException extends RuntimeException {
      private GlException(String var1) {
         super(var1);
      }

      // $FF: synthetic method
      GlException(String var1, Object var2) {
         this(var1);
      }
   }

   @Documented
   @Retention(RetentionPolicy.SOURCE)
   public @interface SecureMode {
   }

   public interface TextureImageListener {
      void onFrameAvailable();
   }
}

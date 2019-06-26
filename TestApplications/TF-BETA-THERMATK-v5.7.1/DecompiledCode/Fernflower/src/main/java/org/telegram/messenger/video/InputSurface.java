package org.telegram.messenger.video;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.view.Surface;

@TargetApi(17)
public class InputSurface {
   private static final int EGL_OPENGL_ES2_BIT = 4;
   private static final int EGL_RECORDABLE_ANDROID = 12610;
   private EGLContext mEGLContext;
   private EGLDisplay mEGLDisplay;
   private EGLSurface mEGLSurface;
   private Surface mSurface;

   public InputSurface(Surface var1) {
      if (var1 != null) {
         this.mSurface = var1;
         this.eglSetup();
      } else {
         throw new NullPointerException();
      }
   }

   private void checkEglError(String var1) {
      boolean var2;
      for(var2 = false; EGL14.eglGetError() != 12288; var2 = true) {
      }

      if (var2) {
         throw new RuntimeException("EGL error encountered (see log)");
      }
   }

   private void eglSetup() {
      this.mEGLDisplay = EGL14.eglGetDisplay(0);
      EGLDisplay var1 = this.mEGLDisplay;
      if (var1 != EGL14.EGL_NO_DISPLAY) {
         int[] var2 = new int[2];
         if (EGL14.eglInitialize(var1, var2, 0, var2, 1)) {
            EGLConfig[] var3 = new EGLConfig[1];
            var2 = new int[1];
            var1 = this.mEGLDisplay;
            int var4 = var3.length;
            if (EGL14.eglChooseConfig(var1, new int[]{12324, 8, 12323, 8, 12322, 8, 12352, 4, 12610, 1, 12344}, 0, var3, 0, var4, var2, 0)) {
               this.mEGLContext = EGL14.eglCreateContext(this.mEGLDisplay, var3[0], EGL14.EGL_NO_CONTEXT, new int[]{12440, 2, 12344}, 0);
               this.checkEglError("eglCreateContext");
               if (this.mEGLContext != null) {
                  this.mEGLSurface = EGL14.eglCreateWindowSurface(this.mEGLDisplay, var3[0], this.mSurface, new int[]{12344}, 0);
                  this.checkEglError("eglCreateWindowSurface");
                  if (this.mEGLSurface == null) {
                     throw new RuntimeException("surface was null");
                  }
               } else {
                  throw new RuntimeException("null context");
               }
            } else {
               throw new RuntimeException("unable to find RGB888+recordable ES2 EGL config");
            }
         } else {
            this.mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
         }
      } else {
         throw new RuntimeException("unable to get EGL14 display");
      }
   }

   public Surface getSurface() {
      return this.mSurface;
   }

   public void makeCurrent() {
      EGLDisplay var1 = this.mEGLDisplay;
      EGLSurface var2 = this.mEGLSurface;
      if (!EGL14.eglMakeCurrent(var1, var2, var2, this.mEGLContext)) {
         throw new RuntimeException("eglMakeCurrent failed");
      }
   }

   public void release() {
      if (EGL14.eglGetCurrentContext().equals(this.mEGLContext)) {
         EGLDisplay var1 = this.mEGLDisplay;
         EGLSurface var2 = EGL14.EGL_NO_SURFACE;
         EGL14.eglMakeCurrent(var1, var2, var2, EGL14.EGL_NO_CONTEXT);
      }

      EGL14.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
      EGL14.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
      this.mSurface.release();
      this.mEGLDisplay = null;
      this.mEGLContext = null;
      this.mEGLSurface = null;
      this.mSurface = null;
   }

   @TargetApi(18)
   public void setPresentationTime(long var1) {
      EGLExt.eglPresentationTimeANDROID(this.mEGLDisplay, this.mEGLSurface, var1);
   }

   public boolean swapBuffers() {
      return EGL14.eglSwapBuffers(this.mEGLDisplay, this.mEGLSurface);
   }
}

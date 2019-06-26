package org.telegram.messenger.video;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES20;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class OutputSurface implements OnFrameAvailableListener {
   private static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
   private static final int EGL_OPENGL_ES2_BIT = 4;
   private EGL10 mEGL;
   private EGLContext mEGLContext = null;
   private EGLDisplay mEGLDisplay = null;
   private EGLSurface mEGLSurface = null;
   private boolean mFrameAvailable;
   private final Object mFrameSyncObject = new Object();
   private int mHeight;
   private ByteBuffer mPixelBuf;
   private Surface mSurface;
   private SurfaceTexture mSurfaceTexture;
   private TextureRenderer mTextureRender;
   private int mWidth;
   private int rotateRender = 0;

   public OutputSurface() {
      this.setup();
   }

   public OutputSurface(int var1, int var2, int var3) {
      if (var1 > 0 && var2 > 0) {
         this.mWidth = var1;
         this.mHeight = var2;
         this.rotateRender = var3;
         this.mPixelBuf = ByteBuffer.allocateDirect(this.mWidth * this.mHeight * 4);
         this.mPixelBuf.order(ByteOrder.LITTLE_ENDIAN);
         this.eglSetup(var1, var2);
         this.makeCurrent();
         this.setup();
      } else {
         throw new IllegalArgumentException();
      }
   }

   private void checkEglError(String var1) {
      if (this.mEGL.eglGetError() != 12288) {
         throw new RuntimeException("EGL error encountered (see log)");
      }
   }

   private void eglSetup(int var1, int var2) {
      this.mEGL = (EGL10)EGLContext.getEGL();
      this.mEGLDisplay = this.mEGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
      EGLDisplay var3 = this.mEGLDisplay;
      if (var3 != EGL10.EGL_NO_DISPLAY) {
         if (this.mEGL.eglInitialize(var3, (int[])null)) {
            EGLConfig[] var4 = new EGLConfig[1];
            int[] var8 = new int[1];
            EGL10 var5 = this.mEGL;
            EGLDisplay var6 = this.mEGLDisplay;
            int var7 = var4.length;
            if (var5.eglChooseConfig(var6, new int[]{12324, 8, 12323, 8, 12322, 8, 12321, 8, 12339, 1, 12352, 4, 12344}, var4, var7, var8)) {
               this.mEGLContext = this.mEGL.eglCreateContext(this.mEGLDisplay, var4[0], EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
               this.checkEglError("eglCreateContext");
               if (this.mEGLContext != null) {
                  this.mEGLSurface = this.mEGL.eglCreatePbufferSurface(this.mEGLDisplay, var4[0], new int[]{12375, var1, 12374, var2, 12344});
                  this.checkEglError("eglCreatePbufferSurface");
                  if (this.mEGLSurface == null) {
                     throw new RuntimeException("surface was null");
                  }
               } else {
                  throw new RuntimeException("null context");
               }
            } else {
               throw new RuntimeException("unable to find RGB888+pbuffer EGL config");
            }
         } else {
            this.mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL10");
         }
      } else {
         throw new RuntimeException("unable to get EGL10 display");
      }
   }

   private void setup() {
      this.mTextureRender = new TextureRenderer(this.rotateRender);
      this.mTextureRender.surfaceCreated();
      this.mSurfaceTexture = new SurfaceTexture(this.mTextureRender.getTextureId());
      this.mSurfaceTexture.setOnFrameAvailableListener(this);
      this.mSurface = new Surface(this.mSurfaceTexture);
   }

   public void awaitNewImage() {
      // $FF: Couldn't be decompiled
   }

   public void drawImage(boolean var1) {
      this.mTextureRender.drawFrame(this.mSurfaceTexture, var1);
   }

   public ByteBuffer getFrame() {
      this.mPixelBuf.rewind();
      GLES20.glReadPixels(0, 0, this.mWidth, this.mHeight, 6408, 5121, this.mPixelBuf);
      return this.mPixelBuf;
   }

   public Surface getSurface() {
      return this.mSurface;
   }

   public void makeCurrent() {
      if (this.mEGL != null) {
         this.checkEglError("before makeCurrent");
         EGL10 var1 = this.mEGL;
         EGLDisplay var2 = this.mEGLDisplay;
         EGLSurface var3 = this.mEGLSurface;
         if (!var1.eglMakeCurrent(var2, var3, var3, this.mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
         }
      } else {
         throw new RuntimeException("not configured for makeCurrent");
      }
   }

   public void onFrameAvailable(SurfaceTexture var1) {
      Object var15 = this.mFrameSyncObject;
      synchronized(var15){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (!this.mFrameAvailable) {
               this.mFrameAvailable = true;
               this.mFrameSyncObject.notifyAll();
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label116:
         try {
            RuntimeException var16 = new RuntimeException("mFrameAvailable already set, frame could be dropped");
            throw var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label116;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void release() {
      EGL10 var1 = this.mEGL;
      if (var1 != null) {
         if (var1.eglGetCurrentContext().equals(this.mEGLContext)) {
            EGL10 var2 = this.mEGL;
            EGLDisplay var4 = this.mEGLDisplay;
            EGLSurface var3 = EGL10.EGL_NO_SURFACE;
            var2.eglMakeCurrent(var4, var3, var3, EGL10.EGL_NO_CONTEXT);
         }

         this.mEGL.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
         this.mEGL.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
      }

      this.mSurface.release();
      this.mEGLDisplay = null;
      this.mEGLContext = null;
      this.mEGLSurface = null;
      this.mEGL = null;
      this.mTextureRender = null;
      this.mSurface = null;
      this.mSurfaceTexture = null;
   }
}

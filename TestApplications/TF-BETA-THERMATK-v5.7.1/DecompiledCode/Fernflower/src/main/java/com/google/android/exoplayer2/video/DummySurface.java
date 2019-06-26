package com.google.android.exoplayer2.video;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.Surface;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.EGLSurfaceTexture;
import com.google.android.exoplayer2.util.Util;

@TargetApi(17)
public final class DummySurface extends Surface {
   private static int secureMode;
   private static boolean secureModeInitialized;
   public final boolean secure;
   private final DummySurface.DummySurfaceThread thread;
   private boolean threadReleased;

   private DummySurface(DummySurface.DummySurfaceThread var1, SurfaceTexture var2, boolean var3) {
      super(var2);
      this.thread = var1;
      this.secure = var3;
   }

   // $FF: synthetic method
   DummySurface(DummySurface.DummySurfaceThread var1, SurfaceTexture var2, boolean var3, Object var4) {
      this(var1, var2, var3);
   }

   private static void assertApiLevel17OrHigher() {
      if (Util.SDK_INT < 17) {
         throw new UnsupportedOperationException("Unsupported prior to API level 17");
      }
   }

   @TargetApi(24)
   private static int getSecureModeV24(Context var0) {
      if (Util.SDK_INT >= 26 || !"samsung".equals(Util.MANUFACTURER) && !"XT1650".equals(Util.MODEL)) {
         if (Util.SDK_INT < 26 && !var0.getPackageManager().hasSystemFeature("android.hardware.vr.high_performance")) {
            return 0;
         } else {
            String var2 = EGL14.eglQueryString(EGL14.eglGetDisplay(0), 12373);
            if (var2 == null) {
               return 0;
            } else if (!var2.contains("EGL_EXT_protected_content")) {
               return 0;
            } else {
               byte var1;
               if (var2.contains("EGL_KHR_surfaceless_context")) {
                  var1 = 1;
               } else {
                  var1 = 2;
               }

               return var1;
            }
         }
      } else {
         return 0;
      }
   }

   public static boolean isSecureSupported(Context var0) {
      synchronized(DummySurface.class){}

      boolean var2;
      int var3;
      label273: {
         Throwable var10000;
         label277: {
            boolean var1;
            boolean var10001;
            try {
               var1 = secureModeInitialized;
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label277;
            }

            var2 = true;
            if (!var1) {
               label266: {
                  label265: {
                     try {
                        if (Util.SDK_INT >= 24) {
                           break label265;
                        }
                     } catch (Throwable var32) {
                        var10000 = var32;
                        var10001 = false;
                        break label277;
                     }

                     var3 = 0;
                     break label266;
                  }

                  try {
                     var3 = getSecureModeV24(var0);
                  } catch (Throwable var31) {
                     var10000 = var31;
                     var10001 = false;
                     break label277;
                  }
               }

               try {
                  secureMode = var3;
                  secureModeInitialized = true;
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label277;
               }
            }

            label257:
            try {
               var3 = secureMode;
               break label273;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label257;
            }
         }

         Throwable var34 = var10000;
         throw var34;
      }

      if (var3 == 0) {
         var2 = false;
      }

      return var2;
   }

   public static DummySurface newInstanceV17(Context var0, boolean var1) {
      assertApiLevel17OrHigher();
      int var2 = 0;
      boolean var3;
      if (var1 && !isSecureSupported(var0)) {
         var3 = false;
      } else {
         var3 = true;
      }

      Assertions.checkState(var3);
      DummySurface.DummySurfaceThread var4 = new DummySurface.DummySurfaceThread();
      if (var1) {
         var2 = secureMode;
      }

      return var4.init(var2);
   }

   public void release() {
      super.release();
      DummySurface.DummySurfaceThread var1 = this.thread;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (!this.threadReleased) {
               this.thread.release();
               this.threadReleased = true;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
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

   private static class DummySurfaceThread extends HandlerThread implements Callback {
      private EGLSurfaceTexture eglSurfaceTexture;
      private Handler handler;
      private Error initError;
      private RuntimeException initException;
      private DummySurface surface;

      public DummySurfaceThread() {
         super("dummySurface");
      }

      private void initInternal(int var1) {
         Assertions.checkNotNull(this.eglSurfaceTexture);
         this.eglSurfaceTexture.init(var1);
         SurfaceTexture var2 = this.eglSurfaceTexture.getSurfaceTexture();
         boolean var3;
         if (var1 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.surface = new DummySurface(this, var2, var3);
      }

      private void releaseInternal() {
         Assertions.checkNotNull(this.eglSurfaceTexture);
         this.eglSurfaceTexture.release();
      }

      public boolean handleMessage(Message param1) {
         // $FF: Couldn't be decompiled
      }

      public DummySurface init(int param1) {
         // $FF: Couldn't be decompiled
      }

      public void release() {
         Assertions.checkNotNull(this.handler);
         this.handler.sendEmptyMessage(2);
      }
   }
}

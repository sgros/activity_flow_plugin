package com.google.android.exoplayer2.video;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.Choreographer;
import android.view.Display;
import android.view.WindowManager;
import android.view.Choreographer.FrameCallback;
import com.google.android.exoplayer2.util.Util;

@TargetApi(16)
public final class VideoFrameReleaseTimeHelper {
   private long adjustedLastFrameTimeNs;
   private final VideoFrameReleaseTimeHelper.DefaultDisplayListener displayListener;
   private long frameCount;
   private boolean haveSync;
   private long lastFramePresentationTimeUs;
   private long pendingAdjustedFrameTimeNs;
   private long syncFramePresentationTimeNs;
   private long syncUnadjustedReleaseTimeNs;
   private long vsyncDurationNs;
   private long vsyncOffsetNs;
   private final VideoFrameReleaseTimeHelper.VSyncSampler vsyncSampler;
   private final WindowManager windowManager;

   public VideoFrameReleaseTimeHelper(Context var1) {
      VideoFrameReleaseTimeHelper.DefaultDisplayListener var2 = null;
      if (var1 != null) {
         var1 = var1.getApplicationContext();
         this.windowManager = (WindowManager)var1.getSystemService("window");
      } else {
         this.windowManager = null;
      }

      if (this.windowManager != null) {
         if (Util.SDK_INT >= 17) {
            var2 = this.maybeBuildDefaultDisplayListenerV17(var1);
         }

         this.displayListener = var2;
         this.vsyncSampler = VideoFrameReleaseTimeHelper.VSyncSampler.getInstance();
      } else {
         this.displayListener = null;
         this.vsyncSampler = null;
      }

      this.vsyncDurationNs = -9223372036854775807L;
      this.vsyncOffsetNs = -9223372036854775807L;
   }

   private static long closestVsync(long var0, long var2, long var4) {
      var2 += (var0 - var2) / var4 * var4;
      if (var0 <= var2) {
         var4 = var2 - var4;
      } else {
         long var6 = var4 + var2;
         var4 = var2;
         var2 = var6;
      }

      if (var2 - var0 >= var0 - var4) {
         var2 = var4;
      }

      return var2;
   }

   private boolean isDriftTooLarge(long var1, long var3) {
      long var5 = this.syncFramePresentationTimeNs;
      boolean var7;
      if (Math.abs(var3 - this.syncUnadjustedReleaseTimeNs - (var1 - var5)) > 20000000L) {
         var7 = true;
      } else {
         var7 = false;
      }

      return var7;
   }

   @TargetApi(17)
   private VideoFrameReleaseTimeHelper.DefaultDisplayListener maybeBuildDefaultDisplayListenerV17(Context var1) {
      DisplayManager var2 = (DisplayManager)var1.getSystemService("display");
      VideoFrameReleaseTimeHelper.DefaultDisplayListener var3;
      if (var2 == null) {
         var3 = null;
      } else {
         var3 = new VideoFrameReleaseTimeHelper.DefaultDisplayListener(var2);
      }

      return var3;
   }

   private void updateDefaultDisplayRefreshRateParams() {
      Display var1 = this.windowManager.getDefaultDisplay();
      if (var1 != null) {
         double var2 = (double)var1.getRefreshRate();
         Double.isNaN(var2);
         this.vsyncDurationNs = (long)(1.0E9D / var2);
         this.vsyncOffsetNs = this.vsyncDurationNs * 80L / 100L;
      }

   }

   public long adjustReleaseTime(long var1, long var3) {
      long var5;
      long var7;
      long var9;
      label38: {
         var5 = 1000L * var1;
         if (this.haveSync) {
            if (var1 != this.lastFramePresentationTimeUs) {
               ++this.frameCount;
               this.adjustedLastFrameTimeNs = this.pendingAdjustedFrameTimeNs;
            }

            var7 = this.frameCount;
            if (var7 >= 6L) {
               var7 = (var5 - this.syncFramePresentationTimeNs) / var7;
               var9 = this.adjustedLastFrameTimeNs + var7;
               if (!this.isDriftTooLarge(var9, var3)) {
                  var7 = this.syncUnadjustedReleaseTimeNs + var9 - this.syncFramePresentationTimeNs;
                  break label38;
               }

               this.haveSync = false;
            } else if (this.isDriftTooLarge(var5, var3)) {
               this.haveSync = false;
            }
         }

         var7 = var3;
         var9 = var5;
      }

      if (!this.haveSync) {
         this.syncFramePresentationTimeNs = var5;
         this.syncUnadjustedReleaseTimeNs = var3;
         this.frameCount = 0L;
         this.haveSync = true;
      }

      this.lastFramePresentationTimeUs = var1;
      this.pendingAdjustedFrameTimeNs = var9;
      VideoFrameReleaseTimeHelper.VSyncSampler var11 = this.vsyncSampler;
      if (var11 != null && this.vsyncDurationNs != -9223372036854775807L) {
         var1 = var11.sampledVsyncTimeNs;
         return var1 == -9223372036854775807L ? var7 : closestVsync(var7, var1, this.vsyncDurationNs) - this.vsyncOffsetNs;
      } else {
         return var7;
      }
   }

   public void disable() {
      if (this.windowManager != null) {
         VideoFrameReleaseTimeHelper.DefaultDisplayListener var1 = this.displayListener;
         if (var1 != null) {
            var1.unregister();
         }

         this.vsyncSampler.removeObserver();
      }

   }

   public void enable() {
      this.haveSync = false;
      if (this.windowManager != null) {
         this.vsyncSampler.addObserver();
         VideoFrameReleaseTimeHelper.DefaultDisplayListener var1 = this.displayListener;
         if (var1 != null) {
            var1.register();
         }

         this.updateDefaultDisplayRefreshRateParams();
      }

   }

   @TargetApi(17)
   private final class DefaultDisplayListener implements DisplayListener {
      private final DisplayManager displayManager;

      public DefaultDisplayListener(DisplayManager var2) {
         this.displayManager = var2;
      }

      public void onDisplayAdded(int var1) {
      }

      public void onDisplayChanged(int var1) {
         if (var1 == 0) {
            VideoFrameReleaseTimeHelper.this.updateDefaultDisplayRefreshRateParams();
         }

      }

      public void onDisplayRemoved(int var1) {
      }

      public void register() {
         this.displayManager.registerDisplayListener(this, (Handler)null);
      }

      public void unregister() {
         this.displayManager.unregisterDisplayListener(this);
      }
   }

   private static final class VSyncSampler implements FrameCallback, Callback {
      private static final VideoFrameReleaseTimeHelper.VSyncSampler INSTANCE = new VideoFrameReleaseTimeHelper.VSyncSampler();
      private Choreographer choreographer;
      private final HandlerThread choreographerOwnerThread = new HandlerThread("ChoreographerOwner:Handler");
      private final Handler handler;
      private int observerCount;
      public volatile long sampledVsyncTimeNs = -9223372036854775807L;

      private VSyncSampler() {
         this.choreographerOwnerThread.start();
         this.handler = Util.createHandler(this.choreographerOwnerThread.getLooper(), this);
         this.handler.sendEmptyMessage(0);
      }

      private void addObserverInternal() {
         ++this.observerCount;
         if (this.observerCount == 1) {
            this.choreographer.postFrameCallback(this);
         }

      }

      private void createChoreographerInstanceInternal() {
         this.choreographer = Choreographer.getInstance();
      }

      public static VideoFrameReleaseTimeHelper.VSyncSampler getInstance() {
         return INSTANCE;
      }

      private void removeObserverInternal() {
         --this.observerCount;
         if (this.observerCount == 0) {
            this.choreographer.removeFrameCallback(this);
            this.sampledVsyncTimeNs = -9223372036854775807L;
         }

      }

      public void addObserver() {
         this.handler.sendEmptyMessage(1);
      }

      public void doFrame(long var1) {
         this.sampledVsyncTimeNs = var1;
         this.choreographer.postFrameCallbackDelayed(this, 500L);
      }

      public boolean handleMessage(Message var1) {
         int var2 = var1.what;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  return false;
               } else {
                  this.removeObserverInternal();
                  return true;
               }
            } else {
               this.addObserverInternal();
               return true;
            }
         } else {
            this.createChoreographerInstanceInternal();
            return true;
         }
      }

      public void removeObserver() {
         this.handler.sendEmptyMessage(2);
      }
   }
}

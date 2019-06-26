package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.google.android.exoplayer2.util.Util;

public class DefaultLoadControl implements LoadControl {
   private final DefaultAllocator allocator;
   private final long backBufferDurationUs;
   private final long bufferForPlaybackAfterRebufferUs;
   private final long bufferForPlaybackUs;
   private boolean isBuffering;
   private final long maxBufferUs;
   private final long minBufferUs;
   private final boolean prioritizeTimeOverSizeThresholds;
   private final PriorityTaskManager priorityTaskManager;
   private final boolean retainBackBufferFromKeyframe;
   private final int targetBufferBytesOverwrite;
   private int targetBufferSize;

   @Deprecated
   public DefaultLoadControl(DefaultAllocator var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      this(var1, var2, var3, var4, var5, var6, var7, (PriorityTaskManager)null);
   }

   @Deprecated
   public DefaultLoadControl(DefaultAllocator var1, int var2, int var3, int var4, int var5, int var6, boolean var7, PriorityTaskManager var8) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, 0, false);
   }

   protected DefaultLoadControl(DefaultAllocator var1, int var2, int var3, int var4, int var5, int var6, boolean var7, PriorityTaskManager var8, int var9, boolean var10) {
      assertGreaterOrEqual(var4, 0, "bufferForPlaybackMs", "0");
      assertGreaterOrEqual(var5, 0, "bufferForPlaybackAfterRebufferMs", "0");
      assertGreaterOrEqual(var2, var4, "minBufferMs", "bufferForPlaybackMs");
      assertGreaterOrEqual(var2, var5, "minBufferMs", "bufferForPlaybackAfterRebufferMs");
      assertGreaterOrEqual(var3, var2, "maxBufferMs", "minBufferMs");
      assertGreaterOrEqual(var9, 0, "backBufferDurationMs", "0");
      this.allocator = var1;
      this.minBufferUs = C.msToUs((long)var2);
      this.maxBufferUs = C.msToUs((long)var3);
      this.bufferForPlaybackUs = C.msToUs((long)var4);
      this.bufferForPlaybackAfterRebufferUs = C.msToUs((long)var5);
      this.targetBufferBytesOverwrite = var6;
      this.prioritizeTimeOverSizeThresholds = var7;
      this.priorityTaskManager = var8;
      this.backBufferDurationUs = C.msToUs((long)var9);
      this.retainBackBufferFromKeyframe = var10;
   }

   private static void assertGreaterOrEqual(int var0, int var1, String var2, String var3) {
      boolean var4;
      if (var0 >= var1) {
         var4 = true;
      } else {
         var4 = false;
      }

      StringBuilder var5 = new StringBuilder();
      var5.append(var2);
      var5.append(" cannot be less than ");
      var5.append(var3);
      Assertions.checkArgument(var4, var5.toString());
   }

   private void reset(boolean var1) {
      this.targetBufferSize = 0;
      PriorityTaskManager var2 = this.priorityTaskManager;
      if (var2 != null && this.isBuffering) {
         var2.remove(0);
      }

      this.isBuffering = false;
      if (var1) {
         this.allocator.reset();
      }

   }

   protected int calculateTargetBufferSize(Renderer[] var1, TrackSelectionArray var2) {
      int var3 = 0;

      int var4;
      int var5;
      for(var4 = 0; var3 < var1.length; var4 = var5) {
         var5 = var4;
         if (var2.get(var3) != null) {
            var5 = var4 + Util.getDefaultBufferSize(var1[var3].getTrackType());
         }

         ++var3;
      }

      return var4;
   }

   public Allocator getAllocator() {
      return this.allocator;
   }

   public long getBackBufferDurationUs() {
      return this.backBufferDurationUs;
   }

   public void onPrepared() {
      this.reset(false);
   }

   public void onReleased() {
      this.reset(true);
   }

   public void onStopped() {
      this.reset(true);
   }

   public void onTracksSelected(Renderer[] var1, TrackGroupArray var2, TrackSelectionArray var3) {
      int var4 = this.targetBufferBytesOverwrite;
      int var5 = var4;
      if (var4 == -1) {
         var5 = this.calculateTargetBufferSize(var1, var3);
      }

      this.targetBufferSize = var5;
      this.allocator.setTargetBufferSize(this.targetBufferSize);
   }

   public boolean retainBackBufferFromKeyframe() {
      return this.retainBackBufferFromKeyframe;
   }

   public boolean shouldContinueLoading(long var1, float var3) {
      int var4 = this.allocator.getTotalBytesAllocated();
      int var5 = this.targetBufferSize;
      boolean var6 = true;
      boolean var14;
      if (var4 >= var5) {
         var14 = true;
      } else {
         var14 = false;
      }

      boolean var7 = this.isBuffering;
      long var8 = this.minBufferUs;
      long var10 = var8;
      if (var3 > 1.0F) {
         var10 = Math.min(Util.getMediaDurationForPlayoutDuration(var8, var3), this.maxBufferUs);
      }

      boolean var12;
      if (var1 < var10) {
         var12 = var6;
         if (!this.prioritizeTimeOverSizeThresholds) {
            if (!var14) {
               var12 = var6;
            } else {
               var12 = false;
            }
         }

         this.isBuffering = var12;
      } else if (var1 >= this.maxBufferUs || var14) {
         this.isBuffering = false;
      }

      PriorityTaskManager var13 = this.priorityTaskManager;
      if (var13 != null) {
         var12 = this.isBuffering;
         if (var12 != var7) {
            if (var12) {
               var13.add(0);
            } else {
               var13.remove(0);
            }
         }
      }

      return this.isBuffering;
   }

   public boolean shouldStartPlayback(long var1, float var3, boolean var4) {
      long var5 = Util.getPlayoutDurationForMediaDuration(var1, var3);
      if (var4) {
         var1 = this.bufferForPlaybackAfterRebufferUs;
      } else {
         var1 = this.bufferForPlaybackUs;
      }

      if (var1 <= 0L || var5 >= var1 || !this.prioritizeTimeOverSizeThresholds && this.allocator.getTotalBytesAllocated() >= this.targetBufferSize) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }
}

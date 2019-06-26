package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.PlaybackParameters;

public final class StandaloneMediaClock implements MediaClock {
   private long baseElapsedMs;
   private long baseUs;
   private final Clock clock;
   private PlaybackParameters playbackParameters;
   private boolean started;

   public StandaloneMediaClock(Clock var1) {
      this.clock = var1;
      this.playbackParameters = PlaybackParameters.DEFAULT;
   }

   public PlaybackParameters getPlaybackParameters() {
      return this.playbackParameters;
   }

   public long getPositionUs() {
      long var1 = this.baseUs;
      long var3 = var1;
      if (this.started) {
         var3 = this.clock.elapsedRealtime() - this.baseElapsedMs;
         PlaybackParameters var5 = this.playbackParameters;
         if (var5.speed == 1.0F) {
            var3 = C.msToUs(var3);
         } else {
            var3 = var5.getMediaTimeUsForPlayoutTimeMs(var3);
         }

         var3 += var1;
      }

      return var3;
   }

   public void resetPosition(long var1) {
      this.baseUs = var1;
      if (this.started) {
         this.baseElapsedMs = this.clock.elapsedRealtime();
      }

   }

   public PlaybackParameters setPlaybackParameters(PlaybackParameters var1) {
      if (this.started) {
         this.resetPosition(this.getPositionUs());
      }

      this.playbackParameters = var1;
      return var1;
   }

   public void start() {
      if (!this.started) {
         this.baseElapsedMs = this.clock.elapsedRealtime();
         this.started = true;
      }

   }

   public void stop() {
      if (this.started) {
         this.resetPosition(this.getPositionUs());
         this.started = false;
      }

   }
}

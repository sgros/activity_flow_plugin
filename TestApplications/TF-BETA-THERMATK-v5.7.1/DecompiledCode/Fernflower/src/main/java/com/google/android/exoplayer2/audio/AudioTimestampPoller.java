package com.google.android.exoplayer2.audio;

import android.annotation.TargetApi;
import android.media.AudioTimestamp;
import android.media.AudioTrack;
import com.google.android.exoplayer2.util.Util;

final class AudioTimestampPoller {
   private final AudioTimestampPoller.AudioTimestampV19 audioTimestamp;
   private long initialTimestampPositionFrames;
   private long initializeSystemTimeUs;
   private long lastTimestampSampleTimeUs;
   private long sampleIntervalUs;
   private int state;

   public AudioTimestampPoller(AudioTrack var1) {
      if (Util.SDK_INT >= 19) {
         this.audioTimestamp = new AudioTimestampPoller.AudioTimestampV19(var1);
         this.reset();
      } else {
         this.audioTimestamp = null;
         this.updateState(3);
      }

   }

   private void updateState(int var1) {
      this.state = var1;
      if (var1 != 0) {
         if (var1 != 1) {
            if (var1 != 2 && var1 != 3) {
               if (var1 != 4) {
                  throw new IllegalStateException();
               }

               this.sampleIntervalUs = 500000L;
            } else {
               this.sampleIntervalUs = 10000000L;
            }
         } else {
            this.sampleIntervalUs = 5000L;
         }
      } else {
         this.lastTimestampSampleTimeUs = 0L;
         this.initialTimestampPositionFrames = -1L;
         this.initializeSystemTimeUs = System.nanoTime() / 1000L;
         this.sampleIntervalUs = 5000L;
      }

   }

   public void acceptTimestamp() {
      if (this.state == 4) {
         this.reset();
      }

   }

   public long getTimestampPositionFrames() {
      AudioTimestampPoller.AudioTimestampV19 var1 = this.audioTimestamp;
      long var2;
      if (var1 != null) {
         var2 = var1.getTimestampPositionFrames();
      } else {
         var2 = -1L;
      }

      return var2;
   }

   public long getTimestampSystemTimeUs() {
      AudioTimestampPoller.AudioTimestampV19 var1 = this.audioTimestamp;
      long var2;
      if (var1 != null) {
         var2 = var1.getTimestampSystemTimeUs();
      } else {
         var2 = -9223372036854775807L;
      }

      return var2;
   }

   public boolean hasTimestamp() {
      int var1 = this.state;
      boolean var2 = true;
      boolean var3 = var2;
      if (var1 != 1) {
         if (var1 == 2) {
            var3 = var2;
         } else {
            var3 = false;
         }
      }

      return var3;
   }

   public boolean isTimestampAdvancing() {
      boolean var1;
      if (this.state == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean maybePollTimestamp(long var1) {
      AudioTimestampPoller.AudioTimestampV19 var3 = this.audioTimestamp;
      if (var3 != null && var1 - this.lastTimestampSampleTimeUs >= this.sampleIntervalUs) {
         this.lastTimestampSampleTimeUs = var1;
         boolean var4 = var3.maybeUpdateTimestamp();
         int var5 = this.state;
         boolean var6;
         if (var5 != 0) {
            if (var5 != 1) {
               if (var5 != 2) {
                  if (var5 != 3) {
                     if (var5 != 4) {
                        throw new IllegalStateException();
                     }

                     var6 = var4;
                  } else {
                     var6 = var4;
                     if (var4) {
                        this.reset();
                        var6 = var4;
                     }
                  }
               } else {
                  var6 = var4;
                  if (!var4) {
                     this.reset();
                     var6 = var4;
                  }
               }
            } else if (var4) {
               var6 = var4;
               if (this.audioTimestamp.getTimestampPositionFrames() > this.initialTimestampPositionFrames) {
                  this.updateState(2);
                  var6 = var4;
               }
            } else {
               this.reset();
               var6 = var4;
            }
         } else if (var4) {
            if (this.audioTimestamp.getTimestampSystemTimeUs() >= this.initializeSystemTimeUs) {
               this.initialTimestampPositionFrames = this.audioTimestamp.getTimestampPositionFrames();
               this.updateState(1);
               var6 = var4;
            } else {
               var6 = false;
            }
         } else {
            var6 = var4;
            if (var1 - this.initializeSystemTimeUs > 500000L) {
               this.updateState(3);
               var6 = var4;
            }
         }

         return var6;
      } else {
         return false;
      }
   }

   public void rejectTimestamp() {
      this.updateState(4);
   }

   public void reset() {
      if (this.audioTimestamp != null) {
         this.updateState(0);
      }

   }

   @TargetApi(19)
   private static final class AudioTimestampV19 {
      private final AudioTimestamp audioTimestamp;
      private final AudioTrack audioTrack;
      private long lastTimestampPositionFrames;
      private long lastTimestampRawPositionFrames;
      private long rawTimestampFramePositionWrapCount;

      public AudioTimestampV19(AudioTrack var1) {
         this.audioTrack = var1;
         this.audioTimestamp = new AudioTimestamp();
      }

      public long getTimestampPositionFrames() {
         return this.lastTimestampPositionFrames;
      }

      public long getTimestampSystemTimeUs() {
         return this.audioTimestamp.nanoTime / 1000L;
      }

      public boolean maybeUpdateTimestamp() {
         boolean var1 = this.audioTrack.getTimestamp(this.audioTimestamp);
         if (var1) {
            long var2 = this.audioTimestamp.framePosition;
            if (this.lastTimestampRawPositionFrames > var2) {
               ++this.rawTimestampFramePositionWrapCount;
            }

            this.lastTimestampRawPositionFrames = var2;
            this.lastTimestampPositionFrames = var2 + (this.rawTimestampFramePositionWrapCount << 32);
         }

         return var1;
      }
   }
}

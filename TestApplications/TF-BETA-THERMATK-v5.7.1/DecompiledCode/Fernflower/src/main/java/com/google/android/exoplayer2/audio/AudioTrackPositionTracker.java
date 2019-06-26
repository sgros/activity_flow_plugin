package com.google.android.exoplayer2.audio;

import android.media.AudioTrack;
import android.os.SystemClock;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.lang.reflect.Method;

final class AudioTrackPositionTracker {
   private AudioTimestampPoller audioTimestampPoller;
   private AudioTrack audioTrack;
   private int bufferSize;
   private long bufferSizeUs;
   private long endPlaybackHeadPosition;
   private long forceResetWorkaroundTimeMs;
   private Method getLatencyMethod;
   private boolean hasData;
   private boolean isOutputPcm;
   private long lastLatencySampleTimeUs;
   private long lastPlayheadSampleTimeUs;
   private long lastRawPlaybackHeadPosition;
   private long latencyUs;
   private final AudioTrackPositionTracker.Listener listener;
   private boolean needsPassthroughWorkarounds;
   private int nextPlayheadOffsetIndex;
   private int outputPcmFrameSize;
   private int outputSampleRate;
   private long passthroughWorkaroundPauseOffset;
   private int playheadOffsetCount;
   private final long[] playheadOffsets;
   private long rawPlaybackHeadWrapCount;
   private long smoothedPlayheadOffsetUs;
   private long stopPlaybackHeadPosition;
   private long stopTimestampUs;

   public AudioTrackPositionTracker(AudioTrackPositionTracker.Listener var1) {
      Assertions.checkNotNull(var1);
      this.listener = (AudioTrackPositionTracker.Listener)var1;
      if (Util.SDK_INT >= 18) {
         try {
            this.getLatencyMethod = AudioTrack.class.getMethod("getLatency", (Class[])null);
         } catch (NoSuchMethodException var2) {
         }
      }

      this.playheadOffsets = new long[10];
   }

   private boolean forceHasPendingData() {
      boolean var2;
      if (this.needsPassthroughWorkarounds) {
         AudioTrack var1 = this.audioTrack;
         Assertions.checkNotNull(var1);
         if (((AudioTrack)var1).getPlayState() == 2 && this.getPlaybackHeadPosition() == 0L) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   private long framesToDurationUs(long var1) {
      return var1 * 1000000L / (long)this.outputSampleRate;
   }

   private long getPlaybackHeadPosition() {
      AudioTrack var1 = this.audioTrack;
      Assertions.checkNotNull(var1);
      var1 = (AudioTrack)var1;
      long var2;
      if (this.stopTimestampUs != -9223372036854775807L) {
         var2 = (SystemClock.elapsedRealtime() * 1000L - this.stopTimestampUs) * (long)this.outputSampleRate / 1000000L;
         return Math.min(this.endPlaybackHeadPosition, this.stopPlaybackHeadPosition + var2);
      } else {
         int var4 = var1.getPlayState();
         if (var4 == 1) {
            return 0L;
         } else {
            long var5 = 4294967295L & (long)var1.getPlaybackHeadPosition();
            var2 = var5;
            if (this.needsPassthroughWorkarounds) {
               if (var4 == 2 && var5 == 0L) {
                  this.passthroughWorkaroundPauseOffset = this.lastRawPlaybackHeadPosition;
               }

               var2 = var5 + this.passthroughWorkaroundPauseOffset;
            }

            if (Util.SDK_INT <= 28) {
               if (var2 == 0L && this.lastRawPlaybackHeadPosition > 0L && var4 == 3) {
                  if (this.forceResetWorkaroundTimeMs == -9223372036854775807L) {
                     this.forceResetWorkaroundTimeMs = SystemClock.elapsedRealtime();
                  }

                  return this.lastRawPlaybackHeadPosition;
               }

               this.forceResetWorkaroundTimeMs = -9223372036854775807L;
            }

            if (this.lastRawPlaybackHeadPosition > var2) {
               ++this.rawPlaybackHeadWrapCount;
            }

            this.lastRawPlaybackHeadPosition = var2;
            return var2 + (this.rawPlaybackHeadWrapCount << 32);
         }
      }
   }

   private long getPlaybackHeadPositionUs() {
      return this.framesToDurationUs(this.getPlaybackHeadPosition());
   }

   private void maybePollAndCheckTimestamp(long var1, long var3) {
      AudioTimestampPoller var5 = this.audioTimestampPoller;
      Assertions.checkNotNull(var5);
      var5 = (AudioTimestampPoller)var5;
      if (var5.maybePollTimestamp(var1)) {
         long var6 = var5.getTimestampSystemTimeUs();
         long var8 = var5.getTimestampPositionFrames();
         if (Math.abs(var6 - var1) > 5000000L) {
            this.listener.onSystemTimeUsMismatch(var8, var6, var1, var3);
            var5.rejectTimestamp();
         } else if (Math.abs(this.framesToDurationUs(var8) - var3) > 5000000L) {
            this.listener.onPositionFramesMismatch(var8, var6, var1, var3);
            var5.rejectTimestamp();
         } else {
            var5.acceptTimestamp();
         }

      }
   }

   private void maybeSampleSyncParams() {
      long var1 = this.getPlaybackHeadPositionUs();
      if (var1 != 0L) {
         long var3 = System.nanoTime() / 1000L;
         if (var3 - this.lastPlayheadSampleTimeUs >= 30000L) {
            long[] var5 = this.playheadOffsets;
            int var6 = this.nextPlayheadOffsetIndex;
            var5[var6] = var1 - var3;
            this.nextPlayheadOffsetIndex = (var6 + 1) % 10;
            var6 = this.playheadOffsetCount;
            if (var6 < 10) {
               this.playheadOffsetCount = var6 + 1;
            }

            this.lastPlayheadSampleTimeUs = var3;
            this.smoothedPlayheadOffsetUs = 0L;
            var6 = 0;

            while(true) {
               int var7 = this.playheadOffsetCount;
               if (var6 >= var7) {
                  break;
               }

               this.smoothedPlayheadOffsetUs += this.playheadOffsets[var6] / (long)var7;
               ++var6;
            }
         }

         if (!this.needsPassthroughWorkarounds) {
            this.maybePollAndCheckTimestamp(var3, var1);
            this.maybeUpdateLatency(var3);
         }
      }
   }

   private void maybeUpdateLatency(long var1) {
      if (this.isOutputPcm) {
         Method var3 = this.getLatencyMethod;
         if (var3 != null && var1 - this.lastLatencySampleTimeUs >= 500000L) {
            try {
               AudioTrack var4 = this.audioTrack;
               Assertions.checkNotNull(var4);
               Integer var6 = (Integer)var3.invoke(var4);
               Util.castNonNull(var6);
               this.latencyUs = (long)(Integer)var6 * 1000L - this.bufferSizeUs;
               this.latencyUs = Math.max(this.latencyUs, 0L);
               if (this.latencyUs > 5000000L) {
                  this.listener.onInvalidLatency(this.latencyUs);
                  this.latencyUs = 0L;
               }
            } catch (Exception var5) {
               this.getLatencyMethod = null;
            }

            this.lastLatencySampleTimeUs = var1;
         }
      }

   }

   private static boolean needsPassthroughWorkarounds(int var0) {
      boolean var1;
      if (Util.SDK_INT >= 23 || var0 != 5 && var0 != 6) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private void resetSyncParams() {
      this.smoothedPlayheadOffsetUs = 0L;
      this.playheadOffsetCount = 0;
      this.nextPlayheadOffsetIndex = 0;
      this.lastPlayheadSampleTimeUs = 0L;
   }

   public int getAvailableBufferSize(long var1) {
      int var3 = (int)(var1 - this.getPlaybackHeadPosition() * (long)this.outputPcmFrameSize);
      return this.bufferSize - var3;
   }

   public long getCurrentPositionUs(boolean var1) {
      AudioTrack var2 = this.audioTrack;
      Assertions.checkNotNull(var2);
      if (((AudioTrack)var2).getPlayState() == 3) {
         this.maybeSampleSyncParams();
      }

      long var3 = System.nanoTime() / 1000L;
      AudioTimestampPoller var7 = this.audioTimestampPoller;
      Assertions.checkNotNull(var7);
      var7 = (AudioTimestampPoller)var7;
      long var5;
      if (var7.hasTimestamp()) {
         var5 = this.framesToDurationUs(var7.getTimestampPositionFrames());
         return !var7.isTimestampAdvancing() ? var5 : var5 + (var3 - var7.getTimestampSystemTimeUs());
      } else {
         if (this.playheadOffsetCount == 0) {
            var3 = this.getPlaybackHeadPositionUs();
         } else {
            var3 += this.smoothedPlayheadOffsetUs;
         }

         var5 = var3;
         if (!var1) {
            var5 = var3 - this.latencyUs;
         }

         return var5;
      }
   }

   public void handleEndOfStream(long var1) {
      this.stopPlaybackHeadPosition = this.getPlaybackHeadPosition();
      this.stopTimestampUs = SystemClock.elapsedRealtime() * 1000L;
      this.endPlaybackHeadPosition = var1;
   }

   public boolean hasPendingData(long var1) {
      boolean var3;
      if (var1 <= this.getPlaybackHeadPosition() && !this.forceHasPendingData()) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   public boolean isPlaying() {
      AudioTrack var1 = this.audioTrack;
      Assertions.checkNotNull(var1);
      boolean var2;
      if (((AudioTrack)var1).getPlayState() == 3) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isStalled(long var1) {
      boolean var3;
      if (this.forceResetWorkaroundTimeMs != -9223372036854775807L && var1 > 0L && SystemClock.elapsedRealtime() - this.forceResetWorkaroundTimeMs >= 200L) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean mayHandleBuffer(long var1) {
      AudioTrack var3 = this.audioTrack;
      Assertions.checkNotNull(var3);
      int var4 = ((AudioTrack)var3).getPlayState();
      if (this.needsPassthroughWorkarounds) {
         if (var4 == 2) {
            this.hasData = false;
            return false;
         }

         if (var4 == 1 && this.getPlaybackHeadPosition() == 0L) {
            return false;
         }
      }

      boolean var5 = this.hasData;
      this.hasData = this.hasPendingData(var1);
      if (var5 && !this.hasData && var4 != 1) {
         AudioTrackPositionTracker.Listener var6 = this.listener;
         if (var6 != null) {
            var6.onUnderrun(this.bufferSize, C.usToMs(this.bufferSizeUs));
         }
      }

      return true;
   }

   public boolean pause() {
      this.resetSyncParams();
      if (this.stopTimestampUs == -9223372036854775807L) {
         AudioTimestampPoller var1 = this.audioTimestampPoller;
         Assertions.checkNotNull(var1);
         ((AudioTimestampPoller)var1).reset();
         return true;
      } else {
         return false;
      }
   }

   public void reset() {
      this.resetSyncParams();
      this.audioTrack = null;
      this.audioTimestampPoller = null;
   }

   public void setAudioTrack(AudioTrack var1, int var2, int var3, int var4) {
      this.audioTrack = var1;
      this.outputPcmFrameSize = var3;
      this.bufferSize = var4;
      this.audioTimestampPoller = new AudioTimestampPoller(var1);
      this.outputSampleRate = var1.getSampleRate();
      this.needsPassthroughWorkarounds = needsPassthroughWorkarounds(var2);
      this.isOutputPcm = Util.isEncodingLinearPcm(var2);
      long var5;
      if (this.isOutputPcm) {
         var5 = this.framesToDurationUs((long)(var4 / var3));
      } else {
         var5 = -9223372036854775807L;
      }

      this.bufferSizeUs = var5;
      this.lastRawPlaybackHeadPosition = 0L;
      this.rawPlaybackHeadWrapCount = 0L;
      this.passthroughWorkaroundPauseOffset = 0L;
      this.hasData = false;
      this.stopTimestampUs = -9223372036854775807L;
      this.forceResetWorkaroundTimeMs = -9223372036854775807L;
      this.latencyUs = 0L;
   }

   public void start() {
      AudioTimestampPoller var1 = this.audioTimestampPoller;
      Assertions.checkNotNull(var1);
      ((AudioTimestampPoller)var1).reset();
   }

   public interface Listener {
      void onInvalidLatency(long var1);

      void onPositionFramesMismatch(long var1, long var3, long var5, long var7);

      void onSystemTimeUsMismatch(long var1, long var3, long var5, long var7);

      void onUnderrun(int var1, long var2);
   }
}

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public final class ClippingMediaPeriod implements MediaPeriod, MediaPeriod.Callback {
   private MediaPeriod.Callback callback;
   long endUs;
   public final MediaPeriod mediaPeriod;
   private long pendingInitialDiscontinuityPositionUs;
   private ClippingMediaPeriod.ClippingSampleStream[] sampleStreams;
   long startUs;

   public ClippingMediaPeriod(MediaPeriod var1, boolean var2, long var3, long var5) {
      this.mediaPeriod = var1;
      this.sampleStreams = new ClippingMediaPeriod.ClippingSampleStream[0];
      long var7;
      if (var2) {
         var7 = var3;
      } else {
         var7 = -9223372036854775807L;
      }

      this.pendingInitialDiscontinuityPositionUs = var7;
      this.startUs = var3;
      this.endUs = var5;
   }

   private SeekParameters clipSeekParameters(long var1, SeekParameters var3) {
      long var4 = Util.constrainValue(var3.toleranceBeforeUs, 0L, var1 - this.startUs);
      long var6 = var3.toleranceAfterUs;
      long var8 = this.endUs;
      if (var8 == Long.MIN_VALUE) {
         var1 = Long.MAX_VALUE;
      } else {
         var1 = var8 - var1;
      }

      var1 = Util.constrainValue(var6, 0L, var1);
      return var4 == var3.toleranceBeforeUs && var1 == var3.toleranceAfterUs ? var3 : new SeekParameters(var4, var1);
   }

   private static boolean shouldKeepInitialDiscontinuity(long var0, TrackSelection[] var2) {
      if (var0 != 0L) {
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            TrackSelection var5 = var2[var4];
            if (var5 != null && !MimeTypes.isAudio(var5.getSelectedFormat().sampleMimeType)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean continueLoading(long var1) {
      return this.mediaPeriod.continueLoading(var1);
   }

   public void discardBuffer(long var1, boolean var3) {
      this.mediaPeriod.discardBuffer(var1, var3);
   }

   public long getAdjustedSeekPositionUs(long var1, SeekParameters var3) {
      long var4 = this.startUs;
      if (var1 == var4) {
         return var4;
      } else {
         var3 = this.clipSeekParameters(var1, var3);
         return this.mediaPeriod.getAdjustedSeekPositionUs(var1, var3);
      }
   }

   public long getBufferedPositionUs() {
      long var1 = this.mediaPeriod.getBufferedPositionUs();
      if (var1 != Long.MIN_VALUE) {
         long var3 = this.endUs;
         if (var3 == Long.MIN_VALUE || var1 < var3) {
            return var1;
         }
      }

      return Long.MIN_VALUE;
   }

   public long getNextLoadPositionUs() {
      long var1 = this.mediaPeriod.getNextLoadPositionUs();
      if (var1 != Long.MIN_VALUE) {
         long var3 = this.endUs;
         if (var3 == Long.MIN_VALUE || var1 < var3) {
            return var1;
         }
      }

      return Long.MIN_VALUE;
   }

   public TrackGroupArray getTrackGroups() {
      return this.mediaPeriod.getTrackGroups();
   }

   boolean isPendingInitialDiscontinuity() {
      boolean var1;
      if (this.pendingInitialDiscontinuityPositionUs != -9223372036854775807L) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void maybeThrowPrepareError() throws IOException {
      this.mediaPeriod.maybeThrowPrepareError();
   }

   public void onContinueLoadingRequested(MediaPeriod var1) {
      this.callback.onContinueLoadingRequested(this);
   }

   public void onPrepared(MediaPeriod var1) {
      this.callback.onPrepared(this);
   }

   public void prepare(MediaPeriod.Callback var1, long var2) {
      this.callback = var1;
      this.mediaPeriod.prepare(this, var2);
   }

   public long readDiscontinuity() {
      long var1;
      long var3;
      if (this.isPendingInitialDiscontinuity()) {
         var1 = this.pendingInitialDiscontinuityPositionUs;
         this.pendingInitialDiscontinuityPositionUs = -9223372036854775807L;
         var3 = this.readDiscontinuity();
         if (var3 != -9223372036854775807L) {
            var1 = var3;
         }

         return var1;
      } else {
         var1 = this.mediaPeriod.readDiscontinuity();
         if (var1 == -9223372036854775807L) {
            return -9223372036854775807L;
         } else {
            var3 = this.startUs;
            boolean var5 = true;
            boolean var6;
            if (var1 >= var3) {
               var6 = true;
            } else {
               var6 = false;
            }

            Assertions.checkState(var6);
            var3 = this.endUs;
            var6 = var5;
            if (var3 != Long.MIN_VALUE) {
               if (var1 <= var3) {
                  var6 = var5;
               } else {
                  var6 = false;
               }
            }

            Assertions.checkState(var6);
            return var1;
         }
      }
   }

   public void reevaluateBuffer(long var1) {
      this.mediaPeriod.reevaluateBuffer(var1);
   }

   public long seekToUs(long var1) {
      this.pendingInitialDiscontinuityPositionUs = -9223372036854775807L;
      ClippingMediaPeriod.ClippingSampleStream[] var3 = this.sampleStreams;
      int var4 = var3.length;
      boolean var5 = false;

      for(int var6 = 0; var6 < var4; ++var6) {
         ClippingMediaPeriod.ClippingSampleStream var7 = var3[var6];
         if (var7 != null) {
            var7.clearSentEos();
         }
      }

      long var8;
      boolean var10;
      label23: {
         var8 = this.mediaPeriod.seekToUs(var1);
         if (var8 != var1) {
            var10 = var5;
            if (var8 < this.startUs) {
               break label23;
            }

            var1 = this.endUs;
            if (var1 != Long.MIN_VALUE) {
               var10 = var5;
               if (var8 > var1) {
                  break label23;
               }
            }
         }

         var10 = true;
      }

      Assertions.checkState(var10);
      return var8;
   }

   public long selectTracks(TrackSelection[] var1, boolean[] var2, SampleStream[] var3, boolean[] var4, long var5) {
      this.sampleStreams = new ClippingMediaPeriod.ClippingSampleStream[var3.length];
      SampleStream[] var7 = new SampleStream[var3.length];
      byte var8 = 0;
      int var9 = 0;

      while(true) {
         int var10 = var3.length;
         SampleStream var11 = null;
         if (var9 >= var10) {
            long var13;
            long var15;
            label57: {
               var13 = this.mediaPeriod.selectTracks(var1, var2, var7, var4, var5);
               if (this.isPendingInitialDiscontinuity()) {
                  var15 = this.startUs;
                  if (var5 == var15 && shouldKeepInitialDiscontinuity(var15, var1)) {
                     var15 = var13;
                     break label57;
                  }
               }

               var15 = -9223372036854775807L;
            }

            boolean var17;
            label51: {
               label50: {
                  this.pendingInitialDiscontinuityPositionUs = var15;
                  if (var13 != var5) {
                     if (var13 < this.startUs) {
                        break label50;
                     }

                     var5 = this.endUs;
                     if (var5 != Long.MIN_VALUE && var13 > var5) {
                        break label50;
                     }
                  }

                  var17 = true;
                  break label51;
               }

               var17 = false;
            }

            Assertions.checkState(var17);

            for(var9 = var8; var9 < var3.length; ++var9) {
               if (var7[var9] == null) {
                  this.sampleStreams[var9] = null;
               } else if (var3[var9] == null || this.sampleStreams[var9].childStream != var7[var9]) {
                  this.sampleStreams[var9] = new ClippingMediaPeriod.ClippingSampleStream(var7[var9]);
               }

               var3[var9] = this.sampleStreams[var9];
            }

            return var13;
         }

         ClippingMediaPeriod.ClippingSampleStream[] var12 = this.sampleStreams;
         var12[var9] = (ClippingMediaPeriod.ClippingSampleStream)var3[var9];
         if (var12[var9] != null) {
            var11 = var12[var9].childStream;
         }

         var7[var9] = var11;
         ++var9;
      }
   }

   private final class ClippingSampleStream implements SampleStream {
      public final SampleStream childStream;
      private boolean sentEos;

      public ClippingSampleStream(SampleStream var2) {
         this.childStream = var2;
      }

      public void clearSentEos() {
         this.sentEos = false;
      }

      public boolean isReady() {
         boolean var1;
         if (!ClippingMediaPeriod.this.isPendingInitialDiscontinuity() && this.childStream.isReady()) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public void maybeThrowError() throws IOException {
         this.childStream.maybeThrowError();
      }

      public int readData(FormatHolder var1, DecoderInputBuffer var2, boolean var3) {
         if (ClippingMediaPeriod.this.isPendingInitialDiscontinuity()) {
            return -3;
         } else if (this.sentEos) {
            var2.setFlags(4);
            return -4;
         } else {
            int var4 = this.childStream.readData(var1, var2, var3);
            long var5;
            if (var4 == -5) {
               Format var8 = var1.format;
               if (var8.encoderDelay != 0 || var8.encoderPadding != 0) {
                  var5 = ClippingMediaPeriod.this.startUs;
                  int var7 = 0;
                  if (var5 != 0L) {
                     var4 = 0;
                  } else {
                     var4 = var8.encoderDelay;
                  }

                  if (ClippingMediaPeriod.this.endUs == Long.MIN_VALUE) {
                     var7 = var8.encoderPadding;
                  }

                  var1.format = var8.copyWithGaplessInfo(var4, var7);
               }

               return -5;
            } else {
               var5 = ClippingMediaPeriod.this.endUs;
               if (var5 == Long.MIN_VALUE || (var4 != -4 || var2.timeUs < var5) && (var4 != -3 || ClippingMediaPeriod.this.getBufferedPositionUs() != Long.MIN_VALUE)) {
                  return var4;
               } else {
                  var2.clear();
                  var2.setFlags(4);
                  this.sentEos = true;
                  return -4;
               }
            }
         }
      }

      public int skipData(long var1) {
         return ClippingMediaPeriod.this.isPendingInitialDiscontinuity() ? -3 : this.childStream.skipData(var1);
      }
   }
}

package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.Format;

public final class Track {
   public final long durationUs;
   public final long[] editListDurations;
   public final long[] editListMediaTimes;
   public final Format format;
   public final int id;
   public final long movieTimescale;
   public final int nalUnitLengthFieldLength;
   private final TrackEncryptionBox[] sampleDescriptionEncryptionBoxes;
   public final int sampleTransformation;
   public final long timescale;
   public final int type;

   public Track(int var1, int var2, long var3, long var5, long var7, Format var9, int var10, TrackEncryptionBox[] var11, int var12, long[] var13, long[] var14) {
      this.id = var1;
      this.type = var2;
      this.timescale = var3;
      this.movieTimescale = var5;
      this.durationUs = var7;
      this.format = var9;
      this.sampleTransformation = var10;
      this.sampleDescriptionEncryptionBoxes = var11;
      this.nalUnitLengthFieldLength = var12;
      this.editListDurations = var13;
      this.editListMediaTimes = var14;
   }

   public TrackEncryptionBox getSampleDescriptionEncryptionBox(int var1) {
      TrackEncryptionBox[] var2 = this.sampleDescriptionEncryptionBoxes;
      TrackEncryptionBox var3;
      if (var2 == null) {
         var3 = null;
      } else {
         var3 = var2[var1];
      }

      return var3;
   }
}

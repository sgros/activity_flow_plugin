package com.google.android.exoplayer2.extractor.mp4;

final class DefaultSampleValues {
   public final int duration;
   public final int flags;
   public final int sampleDescriptionIndex;
   public final int size;

   public DefaultSampleValues(int var1, int var2, int var3, int var4) {
      this.sampleDescriptionIndex = var1;
      this.duration = var2;
      this.size = var3;
      this.flags = var4;
   }
}

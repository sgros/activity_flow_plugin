package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.extractor.ConstantBitrateSeekMap;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;

final class ConstantBitrateSeeker extends ConstantBitrateSeekMap implements Mp3Extractor.Seeker {
   public ConstantBitrateSeeker(long var1, long var3, MpegAudioHeader var5) {
      super(var1, var3, var5.bitrate, var5.frameSize);
   }

   public long getDataEndPosition() {
      return -1L;
   }

   public long getTimeUs(long var1) {
      return this.getTimeUsAtPosition(var1);
   }
}

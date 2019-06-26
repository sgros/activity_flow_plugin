package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

final class TrackFragment {
   public long atomPosition;
   public long auxiliaryDataPosition;
   public long dataPosition;
   public boolean definesEncryptionData;
   public DefaultSampleValues header;
   public long nextFragmentDecodeTime;
   public int[] sampleCompositionTimeOffsetTable;
   public int sampleCount;
   public long[] sampleDecodingTimeTable;
   public ParsableByteArray sampleEncryptionData;
   public int sampleEncryptionDataLength;
   public boolean sampleEncryptionDataNeedsFill;
   public boolean[] sampleHasSubsampleEncryptionTable;
   public boolean[] sampleIsSyncFrameTable;
   public int[] sampleSizeTable;
   public TrackEncryptionBox trackEncryptionBox;
   public int trunCount;
   public long[] trunDataPosition;
   public int[] trunLength;

   public void fillEncryptionData(ExtractorInput var1) throws IOException, InterruptedException {
      var1.readFully(this.sampleEncryptionData.data, 0, this.sampleEncryptionDataLength);
      this.sampleEncryptionData.setPosition(0);
      this.sampleEncryptionDataNeedsFill = false;
   }

   public void fillEncryptionData(ParsableByteArray var1) {
      var1.readBytes(this.sampleEncryptionData.data, 0, this.sampleEncryptionDataLength);
      this.sampleEncryptionData.setPosition(0);
      this.sampleEncryptionDataNeedsFill = false;
   }

   public long getSamplePresentationTime(int var1) {
      return this.sampleDecodingTimeTable[var1] + (long)this.sampleCompositionTimeOffsetTable[var1];
   }

   public void initEncryptionData(int var1) {
      ParsableByteArray var2 = this.sampleEncryptionData;
      if (var2 == null || var2.limit() < var1) {
         this.sampleEncryptionData = new ParsableByteArray(var1);
      }

      this.sampleEncryptionDataLength = var1;
      this.definesEncryptionData = true;
      this.sampleEncryptionDataNeedsFill = true;
   }

   public void initTables(int var1, int var2) {
      this.trunCount = var1;
      this.sampleCount = var2;
      int[] var3 = this.trunLength;
      if (var3 == null || var3.length < var1) {
         this.trunDataPosition = new long[var1];
         this.trunLength = new int[var1];
      }

      var3 = this.sampleSizeTable;
      if (var3 == null || var3.length < var2) {
         var1 = var2 * 125 / 100;
         this.sampleSizeTable = new int[var1];
         this.sampleCompositionTimeOffsetTable = new int[var1];
         this.sampleDecodingTimeTable = new long[var1];
         this.sampleIsSyncFrameTable = new boolean[var1];
         this.sampleHasSubsampleEncryptionTable = new boolean[var1];
      }

   }

   public void reset() {
      this.trunCount = 0;
      this.nextFragmentDecodeTime = 0L;
      this.definesEncryptionData = false;
      this.sampleEncryptionDataNeedsFill = false;
      this.trackEncryptionBox = null;
   }

   public boolean sampleHasSubsampleEncryptionTable(int var1) {
      boolean var2;
      if (this.definesEncryptionData && this.sampleHasSubsampleEncryptionTable[var1]) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}

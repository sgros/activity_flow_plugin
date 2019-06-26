package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.ParsableNalUnitBitArray;
import java.util.Collections;

public final class H265Reader implements ElementaryStreamReader {
   private String formatId;
   private boolean hasOutputFormat;
   private TrackOutput output;
   private long pesTimeUs;
   private final NalUnitTargetBuffer pps;
   private final boolean[] prefixFlags;
   private final NalUnitTargetBuffer prefixSei;
   private H265Reader.SampleReader sampleReader;
   private final SeiReader seiReader;
   private final ParsableByteArray seiWrapper;
   private final NalUnitTargetBuffer sps;
   private final NalUnitTargetBuffer suffixSei;
   private long totalBytesWritten;
   private final NalUnitTargetBuffer vps;

   public H265Reader(SeiReader var1) {
      this.seiReader = var1;
      this.prefixFlags = new boolean[3];
      this.vps = new NalUnitTargetBuffer(32, 128);
      this.sps = new NalUnitTargetBuffer(33, 128);
      this.pps = new NalUnitTargetBuffer(34, 128);
      this.prefixSei = new NalUnitTargetBuffer(39, 128);
      this.suffixSei = new NalUnitTargetBuffer(40, 128);
      this.seiWrapper = new ParsableByteArray();
   }

   private void endNalUnit(long var1, int var3, int var4, long var5) {
      if (this.hasOutputFormat) {
         this.sampleReader.endNalUnit(var1, var3);
      } else {
         this.vps.endNalUnit(var4);
         this.sps.endNalUnit(var4);
         this.pps.endNalUnit(var4);
         if (this.vps.isCompleted() && this.sps.isCompleted() && this.pps.isCompleted()) {
            this.output.format(parseMediaFormat(this.formatId, this.vps, this.sps, this.pps));
            this.hasOutputFormat = true;
         }
      }

      NalUnitTargetBuffer var7;
      if (this.prefixSei.endNalUnit(var4)) {
         var7 = this.prefixSei;
         var3 = NalUnitUtil.unescapeStream(var7.nalData, var7.nalLength);
         this.seiWrapper.reset(this.prefixSei.nalData, var3);
         this.seiWrapper.skipBytes(5);
         this.seiReader.consume(var5, this.seiWrapper);
      }

      if (this.suffixSei.endNalUnit(var4)) {
         var7 = this.suffixSei;
         var3 = NalUnitUtil.unescapeStream(var7.nalData, var7.nalLength);
         this.seiWrapper.reset(this.suffixSei.nalData, var3);
         this.seiWrapper.skipBytes(5);
         this.seiReader.consume(var5, this.seiWrapper);
      }

   }

   private void nalUnitData(byte[] var1, int var2, int var3) {
      if (this.hasOutputFormat) {
         this.sampleReader.readNalUnitData(var1, var2, var3);
      } else {
         this.vps.appendToNalUnit(var1, var2, var3);
         this.sps.appendToNalUnit(var1, var2, var3);
         this.pps.appendToNalUnit(var1, var2, var3);
      }

      this.prefixSei.appendToNalUnit(var1, var2, var3);
      this.suffixSei.appendToNalUnit(var1, var2, var3);
   }

   private static Format parseMediaFormat(String var0, NalUnitTargetBuffer var1, NalUnitTargetBuffer var2, NalUnitTargetBuffer var3) {
      int var4 = var1.nalLength;
      byte[] var5 = new byte[var2.nalLength + var4 + var3.nalLength];
      byte[] var6 = var1.nalData;
      byte var7 = 0;
      System.arraycopy(var6, 0, var5, 0, var4);
      System.arraycopy(var2.nalData, 0, var5, var1.nalLength, var2.nalLength);
      System.arraycopy(var3.nalData, 0, var5, var1.nalLength + var2.nalLength, var3.nalLength);
      ParsableNalUnitBitArray var19 = new ParsableNalUnitBitArray(var2.nalData, 0, var2.nalLength);
      var19.skipBits(44);
      int var8 = var19.readBits(3);
      var19.skipBit();
      var19.skipBits(88);
      var19.skipBits(8);
      int var9 = 0;

      int var10;
      for(var4 = 0; var9 < var8; ++var9) {
         var10 = var4;
         if (var19.readBit()) {
            var10 = var4 + 89;
         }

         var4 = var10;
         if (var19.readBit()) {
            var4 = var10 + 8;
         }
      }

      var19.skipBits(var4);
      if (var8 > 0) {
         var19.skipBits((8 - var8) * 2);
      }

      var19.readUnsignedExpGolombCodedInt();
      int var11 = var19.readUnsignedExpGolombCodedInt();
      if (var11 == 3) {
         var19.skipBit();
      }

      int var12 = var19.readUnsignedExpGolombCodedInt();
      int var13 = var19.readUnsignedExpGolombCodedInt();
      var9 = var12;
      var10 = var13;
      if (var19.readBit()) {
         int var14 = var19.readUnsignedExpGolombCodedInt();
         var9 = var19.readUnsignedExpGolombCodedInt();
         int var15 = var19.readUnsignedExpGolombCodedInt();
         int var16 = var19.readUnsignedExpGolombCodedInt();
         byte var22;
         if (var11 != 1 && var11 != 2) {
            var22 = 1;
         } else {
            var22 = 2;
         }

         byte var24;
         if (var11 == 1) {
            var24 = 2;
         } else {
            var24 = 1;
         }

         var9 = var12 - var22 * (var14 + var9);
         var10 = var13 - var24 * (var15 + var16);
      }

      var19.readUnsignedExpGolombCodedInt();
      var19.readUnsignedExpGolombCodedInt();
      var13 = var19.readUnsignedExpGolombCodedInt();
      if (var19.readBit()) {
         var4 = 0;
      } else {
         var4 = var8;
      }

      while(var4 <= var8) {
         var19.readUnsignedExpGolombCodedInt();
         var19.readUnsignedExpGolombCodedInt();
         var19.readUnsignedExpGolombCodedInt();
         ++var4;
      }

      var19.readUnsignedExpGolombCodedInt();
      var19.readUnsignedExpGolombCodedInt();
      var19.readUnsignedExpGolombCodedInt();
      var19.readUnsignedExpGolombCodedInt();
      var19.readUnsignedExpGolombCodedInt();
      var19.readUnsignedExpGolombCodedInt();
      if (var19.readBit() && var19.readBit()) {
         skipScalingList(var19);
      }

      var19.skipBits(2);
      if (var19.readBit()) {
         var19.skipBits(8);
         var19.readUnsignedExpGolombCodedInt();
         var19.readUnsignedExpGolombCodedInt();
         var19.skipBit();
      }

      skipShortTermRefPicSets(var19);
      if (var19.readBit()) {
         for(var4 = var7; var4 < var19.readUnsignedExpGolombCodedInt(); ++var4) {
            var19.skipBits(var13 + 4 + 1);
         }
      }

      var19.skipBits(2);
      float var17 = 1.0F;
      float var18;
      if (var19.readBit() && var19.readBit()) {
         var4 = var19.readBits(8);
         if (var4 == 255) {
            int var23 = var19.readBits(16);
            var4 = var19.readBits(16);
            var18 = var17;
            if (var23 != 0) {
               var18 = var17;
               if (var4 != 0) {
                  var18 = (float)var23 / (float)var4;
                  return Format.createVideoSampleFormat(var0, "video/hevc", (String)null, -1, -1, var9, var10, -1.0F, Collections.singletonList(var5), -1, var18, (DrmInitData)null);
               }
            }

            return Format.createVideoSampleFormat(var0, "video/hevc", (String)null, -1, -1, var9, var10, -1.0F, Collections.singletonList(var5), -1, var18, (DrmInitData)null);
         }

         float[] var20 = NalUnitUtil.ASPECT_RATIO_IDC_VALUES;
         if (var4 < var20.length) {
            var18 = var20[var4];
            return Format.createVideoSampleFormat(var0, "video/hevc", (String)null, -1, -1, var9, var10, -1.0F, Collections.singletonList(var5), -1, var18, (DrmInitData)null);
         }

         StringBuilder var21 = new StringBuilder();
         var21.append("Unexpected aspect_ratio_idc value: ");
         var21.append(var4);
         Log.w("H265Reader", var21.toString());
      }

      var18 = 1.0F;
      return Format.createVideoSampleFormat(var0, "video/hevc", (String)null, -1, -1, var9, var10, -1.0F, Collections.singletonList(var5), -1, var18, (DrmInitData)null);
   }

   private static void skipScalingList(ParsableNalUnitBitArray var0) {
      byte var5;
      for(int var1 = 0; var1 < 4; ++var1) {
         for(int var2 = 0; var2 < 6; var2 += var5) {
            if (!var0.readBit()) {
               var0.readUnsignedExpGolombCodedInt();
            } else {
               int var3 = Math.min(64, 1 << (var1 << 1) + 4);
               if (var1 > 1) {
                  var0.readSignedExpGolombCodedInt();
               }

               for(int var4 = 0; var4 < var3; ++var4) {
                  var0.readSignedExpGolombCodedInt();
               }
            }

            var5 = 3;
            if (var1 != 3) {
               var5 = 1;
            }
         }
      }

   }

   private static void skipShortTermRefPicSets(ParsableNalUnitBitArray var0) {
      int var1 = var0.readUnsignedExpGolombCodedInt();
      int var2 = 0;
      boolean var3 = false;

      int var6;
      for(int var4 = 0; var2 < var1; var4 = var6) {
         if (var2 != 0) {
            var3 = var0.readBit();
         }

         int var5;
         if (var3) {
            var0.skipBit();
            var0.readUnsignedExpGolombCodedInt();
            var5 = 0;

            while(true) {
               var6 = var4;
               if (var5 > var4) {
                  break;
               }

               if (var0.readBit()) {
                  var0.skipBit();
               }

               ++var5;
            }
         } else {
            var5 = var0.readUnsignedExpGolombCodedInt();
            var6 = var0.readUnsignedExpGolombCodedInt();

            for(var4 = 0; var4 < var5; ++var4) {
               var0.readUnsignedExpGolombCodedInt();
               var0.skipBit();
            }

            for(var4 = 0; var4 < var6; ++var4) {
               var0.readUnsignedExpGolombCodedInt();
               var0.skipBit();
            }

            var6 += var5;
         }

         ++var2;
      }

   }

   private void startNalUnit(long var1, int var3, int var4, long var5) {
      if (this.hasOutputFormat) {
         this.sampleReader.startNalUnit(var1, var3, var4, var5);
      } else {
         this.vps.startNalUnit(var4);
         this.sps.startNalUnit(var4);
         this.pps.startNalUnit(var4);
      }

      this.prefixSei.startNalUnit(var4);
      this.suffixSei.startNalUnit(var4);
   }

   public void consume(ParsableByteArray var1) {
      label28:
      while(true) {
         if (var1.bytesLeft() > 0) {
            int var2 = var1.getPosition();
            int var3 = var1.limit();
            byte[] var4 = var1.data;
            this.totalBytesWritten += (long)var1.bytesLeft();
            this.output.sampleData(var1, var1.bytesLeft());

            while(true) {
               if (var2 >= var3) {
                  continue label28;
               }

               int var5 = NalUnitUtil.findNalUnit(var4, var2, var3, this.prefixFlags);
               if (var5 == var3) {
                  this.nalUnitData(var4, var2, var3);
                  return;
               }

               int var6 = NalUnitUtil.getH265NalUnitType(var4, var5);
               int var7 = var5 - var2;
               if (var7 > 0) {
                  this.nalUnitData(var4, var2, var5);
               }

               int var8 = var3 - var5;
               long var9 = this.totalBytesWritten - (long)var8;
               if (var7 < 0) {
                  var2 = -var7;
               } else {
                  var2 = 0;
               }

               this.endNalUnit(var9, var8, var2, this.pesTimeUs);
               this.startNalUnit(var9, var8, var6, this.pesTimeUs);
               var2 = var5 + 3;
            }
         }

         return;
      }
   }

   public void createTracks(ExtractorOutput var1, TsPayloadReader.TrackIdGenerator var2) {
      var2.generateNewId();
      this.formatId = var2.getFormatId();
      this.output = var1.track(var2.getTrackId(), 2);
      this.sampleReader = new H265Reader.SampleReader(this.output);
      this.seiReader.createTracks(var1, var2);
   }

   public void packetFinished() {
   }

   public void packetStarted(long var1, int var3) {
      this.pesTimeUs = var1;
   }

   public void seek() {
      NalUnitUtil.clearPrefixFlags(this.prefixFlags);
      this.vps.reset();
      this.sps.reset();
      this.pps.reset();
      this.prefixSei.reset();
      this.suffixSei.reset();
      this.sampleReader.reset();
      this.totalBytesWritten = 0L;
   }

   private static final class SampleReader {
      private boolean isFirstParameterSet;
      private boolean isFirstSlice;
      private boolean lookingForFirstSliceFlag;
      private int nalUnitBytesRead;
      private boolean nalUnitHasKeyframeData;
      private long nalUnitStartPosition;
      private long nalUnitTimeUs;
      private final TrackOutput output;
      private boolean readingSample;
      private boolean sampleIsKeyframe;
      private long samplePosition;
      private long sampleTimeUs;
      private boolean writingParameterSets;

      public SampleReader(TrackOutput var1) {
         this.output = var1;
      }

      private void outputSample(int var1) {
         byte var2 = this.sampleIsKeyframe;
         int var3 = (int)(this.nalUnitStartPosition - this.samplePosition);
         this.output.sampleMetadata(this.sampleTimeUs, var2, var3, var1, (TrackOutput.CryptoData)null);
      }

      public void endNalUnit(long var1, int var3) {
         if (this.writingParameterSets && this.isFirstSlice) {
            this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
            this.writingParameterSets = false;
         } else if (this.isFirstParameterSet || this.isFirstSlice) {
            if (this.readingSample) {
               this.outputSample(var3 + (int)(var1 - this.nalUnitStartPosition));
            }

            this.samplePosition = this.nalUnitStartPosition;
            this.sampleTimeUs = this.nalUnitTimeUs;
            this.readingSample = true;
            this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
         }

      }

      public void readNalUnitData(byte[] var1, int var2, int var3) {
         if (this.lookingForFirstSliceFlag) {
            int var4 = this.nalUnitBytesRead;
            int var5 = var2 + 2 - var4;
            if (var5 < var3) {
               boolean var6;
               if ((var1[var5] & 128) != 0) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               this.isFirstSlice = var6;
               this.lookingForFirstSliceFlag = false;
            } else {
               this.nalUnitBytesRead = var4 + (var3 - var2);
            }
         }

      }

      public void reset() {
         this.lookingForFirstSliceFlag = false;
         this.isFirstSlice = false;
         this.isFirstParameterSet = false;
         this.readingSample = false;
         this.writingParameterSets = false;
      }

      public void startNalUnit(long var1, int var3, int var4, long var5) {
         this.isFirstSlice = false;
         this.isFirstParameterSet = false;
         this.nalUnitTimeUs = var5;
         this.nalUnitBytesRead = 0;
         this.nalUnitStartPosition = var1;
         boolean var7 = true;
         if (var4 >= 32) {
            if (!this.writingParameterSets && this.readingSample) {
               this.outputSample(var3);
               this.readingSample = false;
            }

            if (var4 <= 34) {
               this.isFirstParameterSet = this.writingParameterSets ^ true;
               this.writingParameterSets = true;
            }
         }

         boolean var8;
         if (var4 >= 16 && var4 <= 21) {
            var8 = true;
         } else {
            var8 = false;
         }

         this.nalUnitHasKeyframeData = var8;
         var8 = var7;
         if (!this.nalUnitHasKeyframeData) {
            if (var4 <= 9) {
               var8 = var7;
            } else {
               var8 = false;
            }
         }

         this.lookingForFirstSliceFlag = var8;
      }
   }
}

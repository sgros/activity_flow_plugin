package com.google.android.exoplayer2.extractor.ts;

import android.util.SparseArray;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.ParsableNalUnitBitArray;
import java.util.ArrayList;
import java.util.Arrays;

public final class H264Reader implements ElementaryStreamReader {
   private final boolean allowNonIdrKeyframes;
   private final boolean detectAccessUnits;
   private String formatId;
   private boolean hasOutputFormat;
   private TrackOutput output;
   private long pesTimeUs;
   private final NalUnitTargetBuffer pps;
   private final boolean[] prefixFlags;
   private boolean randomAccessIndicator;
   private H264Reader.SampleReader sampleReader;
   private final NalUnitTargetBuffer sei;
   private final SeiReader seiReader;
   private final ParsableByteArray seiWrapper;
   private final NalUnitTargetBuffer sps;
   private long totalBytesWritten;

   public H264Reader(SeiReader var1, boolean var2, boolean var3) {
      this.seiReader = var1;
      this.allowNonIdrKeyframes = var2;
      this.detectAccessUnits = var3;
      this.prefixFlags = new boolean[3];
      this.sps = new NalUnitTargetBuffer(7, 128);
      this.pps = new NalUnitTargetBuffer(8, 128);
      this.sei = new NalUnitTargetBuffer(6, 128);
      this.seiWrapper = new ParsableByteArray();
   }

   private void endNalUnit(long var1, int var3, int var4, long var5) {
      NalUnitTargetBuffer var10;
      if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
         this.sps.endNalUnit(var4);
         this.pps.endNalUnit(var4);
         if (!this.hasOutputFormat) {
            if (this.sps.isCompleted() && this.pps.isCompleted()) {
               ArrayList var7 = new ArrayList();
               NalUnitTargetBuffer var8 = this.sps;
               var7.add(Arrays.copyOf(var8.nalData, var8.nalLength));
               var8 = this.pps;
               var7.add(Arrays.copyOf(var8.nalData, var8.nalLength));
               var8 = this.sps;
               NalUnitUtil.SpsData var13 = NalUnitUtil.parseSpsNalUnit(var8.nalData, 3, var8.nalLength);
               NalUnitTargetBuffer var9 = this.pps;
               NalUnitUtil.PpsData var14 = NalUnitUtil.parsePpsNalUnit(var9.nalData, 3, var9.nalLength);
               this.output.format(Format.createVideoSampleFormat(this.formatId, "video/avc", CodecSpecificDataUtil.buildAvcCodecString(var13.profileIdc, var13.constraintsFlagsAndReservedZero2Bits, var13.levelIdc), -1, -1, var13.width, var13.height, -1.0F, var7, -1, var13.pixelWidthAspectRatio, (DrmInitData)null));
               this.hasOutputFormat = true;
               this.sampleReader.putSps(var13);
               this.sampleReader.putPps(var14);
               this.sps.reset();
               this.pps.reset();
            }
         } else if (this.sps.isCompleted()) {
            var10 = this.sps;
            NalUnitUtil.SpsData var11 = NalUnitUtil.parseSpsNalUnit(var10.nalData, 3, var10.nalLength);
            this.sampleReader.putSps(var11);
            this.sps.reset();
         } else if (this.pps.isCompleted()) {
            var10 = this.pps;
            NalUnitUtil.PpsData var12 = NalUnitUtil.parsePpsNalUnit(var10.nalData, 3, var10.nalLength);
            this.sampleReader.putPps(var12);
            this.pps.reset();
         }
      }

      if (this.sei.endNalUnit(var4)) {
         var10 = this.sei;
         var4 = NalUnitUtil.unescapeStream(var10.nalData, var10.nalLength);
         this.seiWrapper.reset(this.sei.nalData, var4);
         this.seiWrapper.setPosition(4);
         this.seiReader.consume(var5, this.seiWrapper);
      }

      if (this.sampleReader.endNalUnit(var1, var3, this.hasOutputFormat, this.randomAccessIndicator)) {
         this.randomAccessIndicator = false;
      }

   }

   private void nalUnitData(byte[] var1, int var2, int var3) {
      if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
         this.sps.appendToNalUnit(var1, var2, var3);
         this.pps.appendToNalUnit(var1, var2, var3);
      }

      this.sei.appendToNalUnit(var1, var2, var3);
      this.sampleReader.appendToNalUnit(var1, var2, var3);
   }

   private void startNalUnit(long var1, int var3, long var4) {
      if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
         this.sps.startNalUnit(var3);
         this.pps.startNalUnit(var3);
      }

      this.sei.startNalUnit(var3);
      this.sampleReader.startNalUnit(var1, var3, var4);
   }

   public void consume(ParsableByteArray var1) {
      int var2 = var1.getPosition();
      int var3 = var1.limit();
      byte[] var4 = var1.data;
      this.totalBytesWritten += (long)var1.bytesLeft();
      this.output.sampleData(var1, var1.bytesLeft());

      while(true) {
         int var5 = NalUnitUtil.findNalUnit(var4, var2, var3, this.prefixFlags);
         if (var5 == var3) {
            this.nalUnitData(var4, var2, var3);
            return;
         }

         int var6 = NalUnitUtil.getNalUnitType(var4, var5);
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
         this.startNalUnit(var9, var6, this.pesTimeUs);
         var2 = var5 + 3;
      }
   }

   public void createTracks(ExtractorOutput var1, TsPayloadReader.TrackIdGenerator var2) {
      var2.generateNewId();
      this.formatId = var2.getFormatId();
      this.output = var1.track(var2.getTrackId(), 2);
      this.sampleReader = new H264Reader.SampleReader(this.output, this.allowNonIdrKeyframes, this.detectAccessUnits);
      this.seiReader.createTracks(var1, var2);
   }

   public void packetFinished() {
   }

   public void packetStarted(long var1, int var3) {
      this.pesTimeUs = var1;
      boolean var4 = this.randomAccessIndicator;
      boolean var5;
      if ((var3 & 2) != 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.randomAccessIndicator = var4 | var5;
   }

   public void seek() {
      NalUnitUtil.clearPrefixFlags(this.prefixFlags);
      this.sps.reset();
      this.pps.reset();
      this.sei.reset();
      this.sampleReader.reset();
      this.totalBytesWritten = 0L;
      this.randomAccessIndicator = false;
   }

   private static final class SampleReader {
      private final boolean allowNonIdrKeyframes;
      private final ParsableNalUnitBitArray bitArray;
      private byte[] buffer;
      private int bufferLength;
      private final boolean detectAccessUnits;
      private boolean isFilling;
      private long nalUnitStartPosition;
      private long nalUnitTimeUs;
      private int nalUnitType;
      private final TrackOutput output;
      private final SparseArray pps;
      private H264Reader.SampleReader.SliceHeaderData previousSliceHeader;
      private boolean readingSample;
      private boolean sampleIsKeyframe;
      private long samplePosition;
      private long sampleTimeUs;
      private H264Reader.SampleReader.SliceHeaderData sliceHeader;
      private final SparseArray sps;

      public SampleReader(TrackOutput var1, boolean var2, boolean var3) {
         this.output = var1;
         this.allowNonIdrKeyframes = var2;
         this.detectAccessUnits = var3;
         this.sps = new SparseArray();
         this.pps = new SparseArray();
         this.previousSliceHeader = new H264Reader.SampleReader.SliceHeaderData();
         this.sliceHeader = new H264Reader.SampleReader.SliceHeaderData();
         this.buffer = new byte[128];
         this.bitArray = new ParsableNalUnitBitArray(this.buffer, 0, 0);
         this.reset();
      }

      private void outputSample(int var1) {
         byte var2 = this.sampleIsKeyframe;
         int var3 = (int)(this.nalUnitStartPosition - this.samplePosition);
         this.output.sampleMetadata(this.sampleTimeUs, var2, var3, var1, (TrackOutput.CryptoData)null);
      }

      public void appendToNalUnit(byte[] var1, int var2, int var3) {
         if (this.isFilling) {
            int var4 = var3 - var2;
            byte[] var5 = this.buffer;
            int var6 = var5.length;
            var3 = this.bufferLength;
            if (var6 < var3 + var4) {
               this.buffer = Arrays.copyOf(var5, (var3 + var4) * 2);
            }

            System.arraycopy(var1, var2, this.buffer, this.bufferLength, var4);
            this.bufferLength += var4;
            this.bitArray.reset(this.buffer, 0, this.bufferLength);
            if (this.bitArray.canReadBits(8)) {
               this.bitArray.skipBit();
               int var7 = this.bitArray.readBits(2);
               this.bitArray.skipBits(5);
               if (this.bitArray.canReadExpGolombCodedNum()) {
                  this.bitArray.readUnsignedExpGolombCodedInt();
                  if (this.bitArray.canReadExpGolombCodedNum()) {
                     int var8 = this.bitArray.readUnsignedExpGolombCodedInt();
                     if (!this.detectAccessUnits) {
                        this.isFilling = false;
                        this.sliceHeader.setSliceType(var8);
                     } else if (this.bitArray.canReadExpGolombCodedNum()) {
                        int var9 = this.bitArray.readUnsignedExpGolombCodedInt();
                        if (this.pps.indexOfKey(var9) < 0) {
                           this.isFilling = false;
                        } else {
                           NalUnitUtil.PpsData var17 = (NalUnitUtil.PpsData)this.pps.get(var9);
                           NalUnitUtil.SpsData var16 = (NalUnitUtil.SpsData)this.sps.get(var17.seqParameterSetId);
                           if (var16.separateColorPlaneFlag) {
                              if (!this.bitArray.canReadBits(2)) {
                                 return;
                              }

                              this.bitArray.skipBits(2);
                           }

                           if (this.bitArray.canReadBits(var16.frameNumLength)) {
                              int var10;
                              boolean var11;
                              boolean var12;
                              boolean var13;
                              label112: {
                                 var10 = this.bitArray.readBits(var16.frameNumLength);
                                 if (!var16.frameMbsOnlyFlag) {
                                    if (!this.bitArray.canReadBits(1)) {
                                       return;
                                    }

                                    var11 = this.bitArray.readBit();
                                    if (var11) {
                                       if (!this.bitArray.canReadBits(1)) {
                                          return;
                                       }

                                       var12 = this.bitArray.readBit();
                                       var13 = true;
                                       break label112;
                                    }
                                 } else {
                                    var11 = false;
                                 }

                                 var13 = false;
                                 var12 = false;
                              }

                              boolean var14;
                              if (this.nalUnitType == 5) {
                                 var14 = true;
                              } else {
                                 var14 = false;
                              }

                              if (var14) {
                                 if (!this.bitArray.canReadExpGolombCodedNum()) {
                                    return;
                                 }

                                 var6 = this.bitArray.readUnsignedExpGolombCodedInt();
                              } else {
                                 var6 = 0;
                              }

                              int var15;
                              label105: {
                                 label104: {
                                    label103: {
                                       var2 = var16.picOrderCountType;
                                       if (var2 == 0) {
                                          if (!this.bitArray.canReadBits(var16.picOrderCntLsbLength)) {
                                             return;
                                          }

                                          var2 = this.bitArray.readBits(var16.picOrderCntLsbLength);
                                          if (var17.bottomFieldPicOrderInFramePresentFlag && !var11) {
                                             if (!this.bitArray.canReadExpGolombCodedNum()) {
                                                return;
                                             }

                                             var3 = this.bitArray.readSignedExpGolombCodedInt();
                                             break label103;
                                          }
                                       } else {
                                          if (var2 == 1 && !var16.deltaPicOrderAlwaysZeroFlag) {
                                             if (!this.bitArray.canReadExpGolombCodedNum()) {
                                                return;
                                             }

                                             var4 = this.bitArray.readSignedExpGolombCodedInt();
                                             if (var17.bottomFieldPicOrderInFramePresentFlag && !var11) {
                                                if (!this.bitArray.canReadExpGolombCodedNum()) {
                                                   return;
                                                }

                                                var15 = this.bitArray.readSignedExpGolombCodedInt();
                                                var2 = 0;
                                                var3 = 0;
                                                break label105;
                                             }

                                             var2 = 0;
                                             var3 = 0;
                                             break label104;
                                          }

                                          var2 = 0;
                                       }

                                       var3 = 0;
                                    }

                                    var4 = 0;
                                 }

                                 var15 = 0;
                              }

                              this.sliceHeader.setAll(var16, var7, var8, var10, var9, var11, var13, var12, var14, var6, var2, var3, var4, var15);
                              this.isFilling = false;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      public boolean endNalUnit(long var1, int var3, boolean var4, boolean var5) {
         int var6 = this.nalUnitType;
         boolean var7 = false;
         if (var6 == 9 || this.detectAccessUnits && this.sliceHeader.isFirstVclNalUnitOfPicture(this.previousSliceHeader)) {
            if (var4 && this.readingSample) {
               this.outputSample(var3 + (int)(var1 - this.nalUnitStartPosition));
            }

            this.samplePosition = this.nalUnitStartPosition;
            this.sampleTimeUs = this.nalUnitTimeUs;
            this.sampleIsKeyframe = false;
            this.readingSample = true;
         }

         if (this.allowNonIdrKeyframes) {
            var5 = this.sliceHeader.isISlice();
         }

         boolean var8;
         label27: {
            var4 = this.sampleIsKeyframe;
            var6 = this.nalUnitType;
            if (var6 != 5) {
               var8 = var7;
               if (!var5) {
                  break label27;
               }

               var8 = var7;
               if (var6 != 1) {
                  break label27;
               }
            }

            var8 = true;
         }

         this.sampleIsKeyframe = var4 | var8;
         return this.sampleIsKeyframe;
      }

      public boolean needsSpsPps() {
         return this.detectAccessUnits;
      }

      public void putPps(NalUnitUtil.PpsData var1) {
         this.pps.append(var1.picParameterSetId, var1);
      }

      public void putSps(NalUnitUtil.SpsData var1) {
         this.sps.append(var1.seqParameterSetId, var1);
      }

      public void reset() {
         this.isFilling = false;
         this.readingSample = false;
         this.sliceHeader.clear();
      }

      public void startNalUnit(long var1, int var3, long var4) {
         this.nalUnitType = var3;
         this.nalUnitTimeUs = var4;
         this.nalUnitStartPosition = var1;
         if (!this.allowNonIdrKeyframes || this.nalUnitType != 1) {
            if (!this.detectAccessUnits) {
               return;
            }

            var3 = this.nalUnitType;
            if (var3 != 5 && var3 != 1 && var3 != 2) {
               return;
            }
         }

         H264Reader.SampleReader.SliceHeaderData var6 = this.previousSliceHeader;
         this.previousSliceHeader = this.sliceHeader;
         this.sliceHeader = var6;
         this.sliceHeader.clear();
         this.bufferLength = 0;
         this.isFilling = true;
      }

      private static final class SliceHeaderData {
         private boolean bottomFieldFlag;
         private boolean bottomFieldFlagPresent;
         private int deltaPicOrderCnt0;
         private int deltaPicOrderCnt1;
         private int deltaPicOrderCntBottom;
         private boolean fieldPicFlag;
         private int frameNum;
         private boolean hasSliceType;
         private boolean idrPicFlag;
         private int idrPicId;
         private boolean isComplete;
         private int nalRefIdc;
         private int picOrderCntLsb;
         private int picParameterSetId;
         private int sliceType;
         private NalUnitUtil.SpsData spsData;

         private SliceHeaderData() {
         }

         // $FF: synthetic method
         SliceHeaderData(Object var1) {
            this();
         }

         private boolean isFirstVclNalUnitOfPicture(H264Reader.SampleReader.SliceHeaderData var1) {
            boolean var2 = this.isComplete;
            boolean var3 = true;
            if (var2) {
               var2 = var3;
               if (!var1.isComplete) {
                  return var2;
               }

               var2 = var3;
               if (this.frameNum != var1.frameNum) {
                  return var2;
               }

               var2 = var3;
               if (this.picParameterSetId != var1.picParameterSetId) {
                  return var2;
               }

               var2 = var3;
               if (this.fieldPicFlag != var1.fieldPicFlag) {
                  return var2;
               }

               if (this.bottomFieldFlagPresent && var1.bottomFieldFlagPresent) {
                  var2 = var3;
                  if (this.bottomFieldFlag != var1.bottomFieldFlag) {
                     return var2;
                  }
               }

               int var4 = this.nalRefIdc;
               int var5 = var1.nalRefIdc;
               if (var4 != var5) {
                  var2 = var3;
                  if (var4 == 0) {
                     return var2;
                  }

                  var2 = var3;
                  if (var5 == 0) {
                     return var2;
                  }
               }

               if (this.spsData.picOrderCountType == 0 && var1.spsData.picOrderCountType == 0) {
                  var2 = var3;
                  if (this.picOrderCntLsb != var1.picOrderCntLsb) {
                     return var2;
                  }

                  var2 = var3;
                  if (this.deltaPicOrderCntBottom != var1.deltaPicOrderCntBottom) {
                     return var2;
                  }
               }

               if (this.spsData.picOrderCountType == 1 && var1.spsData.picOrderCountType == 1) {
                  var2 = var3;
                  if (this.deltaPicOrderCnt0 != var1.deltaPicOrderCnt0) {
                     return var2;
                  }

                  var2 = var3;
                  if (this.deltaPicOrderCnt1 != var1.deltaPicOrderCnt1) {
                     return var2;
                  }
               }

               boolean var6 = this.idrPicFlag;
               boolean var7 = var1.idrPicFlag;
               var2 = var3;
               if (var6 != var7) {
                  return var2;
               }

               if (var6 && var7 && this.idrPicId != var1.idrPicId) {
                  var2 = var3;
                  return var2;
               }
            }

            var2 = false;
            return var2;
         }

         public void clear() {
            this.hasSliceType = false;
            this.isComplete = false;
         }

         public boolean isISlice() {
            boolean var2;
            if (this.hasSliceType) {
               int var1 = this.sliceType;
               if (var1 == 7 || var1 == 2) {
                  var2 = true;
                  return var2;
               }
            }

            var2 = false;
            return var2;
         }

         public void setAll(NalUnitUtil.SpsData var1, int var2, int var3, int var4, int var5, boolean var6, boolean var7, boolean var8, boolean var9, int var10, int var11, int var12, int var13, int var14) {
            this.spsData = var1;
            this.nalRefIdc = var2;
            this.sliceType = var3;
            this.frameNum = var4;
            this.picParameterSetId = var5;
            this.fieldPicFlag = var6;
            this.bottomFieldFlagPresent = var7;
            this.bottomFieldFlag = var8;
            this.idrPicFlag = var9;
            this.idrPicId = var10;
            this.picOrderCntLsb = var11;
            this.deltaPicOrderCntBottom = var12;
            this.deltaPicOrderCnt0 = var13;
            this.deltaPicOrderCnt1 = var14;
            this.isComplete = true;
            this.hasSliceType = true;
         }

         public void setSliceType(int var1) {
            this.sliceType = var1;
            this.hasSliceType = true;
         }
      }
   }
}

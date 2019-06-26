package com.google.android.exoplayer2.extractor.mkv;

import android.util.Pair;
import android.util.SparseArray;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.LongArray;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.AvcConfig;
import com.google.android.exoplayer2.video.ColorInfo;
import com.google.android.exoplayer2.video.HevcConfig;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class MatroskaExtractor implements Extractor {
   public static final ExtractorsFactory FACTORY;
   private static final byte[] SSA_DIALOGUE_FORMAT;
   private static final byte[] SSA_PREFIX;
   private static final byte[] SSA_TIMECODE_EMPTY;
   private static final byte[] SUBRIP_PREFIX;
   private static final byte[] SUBRIP_TIMECODE_EMPTY;
   private static final UUID WAVE_SUBFORMAT_PCM;
   private long blockDurationUs;
   private int blockFlags;
   private int blockLacingSampleCount;
   private int blockLacingSampleIndex;
   private int[] blockLacingSampleSizes;
   private int blockState;
   private long blockTimeUs;
   private int blockTrackNumber;
   private int blockTrackNumberLength;
   private long clusterTimecodeUs;
   private LongArray cueClusterPositions;
   private LongArray cueTimesUs;
   private long cuesContentPosition;
   private MatroskaExtractor.Track currentTrack;
   private long durationTimecode;
   private long durationUs;
   private final ParsableByteArray encryptionInitializationVector;
   private final ParsableByteArray encryptionSubsampleData;
   private ByteBuffer encryptionSubsampleDataBuffer;
   private ExtractorOutput extractorOutput;
   private final ParsableByteArray nalLength;
   private final ParsableByteArray nalStartCode;
   private final EbmlReader reader;
   private int sampleBytesRead;
   private int sampleBytesWritten;
   private int sampleCurrentNalBytesRemaining;
   private boolean sampleEncodingHandled;
   private boolean sampleInitializationVectorRead;
   private int samplePartitionCount;
   private boolean samplePartitionCountRead;
   private boolean sampleRead;
   private boolean sampleSeenReferenceBlock;
   private byte sampleSignalByte;
   private boolean sampleSignalByteRead;
   private final ParsableByteArray sampleStrippedBytes;
   private final ParsableByteArray scratch;
   private int seekEntryId;
   private final ParsableByteArray seekEntryIdBytes;
   private long seekEntryPosition;
   private boolean seekForCues;
   private final boolean seekForCuesEnabled;
   private long seekPositionAfterBuildingCues;
   private boolean seenClusterPositionForCurrentCuePoint;
   private long segmentContentPosition;
   private long segmentContentSize;
   private boolean sentSeekMap;
   private final ParsableByteArray subtitleSample;
   private long timecodeScale;
   private final SparseArray tracks;
   private final VarintReader varintReader;
   private final ParsableByteArray vorbisNumPageSamples;

   static {
      FACTORY = _$$Lambda$MatroskaExtractor$jNXW0tyYIOPE6N2jicocV6rRvBs.INSTANCE;
      SUBRIP_PREFIX = new byte[]{49, 10, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 32, 45, 45, 62, 32, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 10};
      SUBRIP_TIMECODE_EMPTY = new byte[]{32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
      SSA_DIALOGUE_FORMAT = Util.getUtf8Bytes("Format: Start, End, ReadOrder, Layer, Style, Name, MarginL, MarginR, MarginV, Effect, Text");
      SSA_PREFIX = new byte[]{68, 105, 97, 108, 111, 103, 117, 101, 58, 32, 48, 58, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 58, 48, 48, 58, 48, 48, 58, 48, 48, 44};
      SSA_TIMECODE_EMPTY = new byte[]{32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
      WAVE_SUBFORMAT_PCM = new UUID(72057594037932032L, -9223371306706625679L);
   }

   public MatroskaExtractor() {
      this(0);
   }

   public MatroskaExtractor(int var1) {
      this(new DefaultEbmlReader(), var1);
   }

   MatroskaExtractor(EbmlReader var1, int var2) {
      this.segmentContentPosition = -1L;
      this.timecodeScale = -9223372036854775807L;
      this.durationTimecode = -9223372036854775807L;
      this.durationUs = -9223372036854775807L;
      this.cuesContentPosition = -1L;
      this.seekPositionAfterBuildingCues = -1L;
      this.clusterTimecodeUs = -9223372036854775807L;
      this.reader = var1;
      this.reader.init(new MatroskaExtractor.InnerEbmlReaderOutput());
      boolean var3 = true;
      if ((var2 & 1) != 0) {
         var3 = false;
      }

      this.seekForCuesEnabled = var3;
      this.varintReader = new VarintReader();
      this.tracks = new SparseArray();
      this.scratch = new ParsableByteArray(4);
      this.vorbisNumPageSamples = new ParsableByteArray(ByteBuffer.allocate(4).putInt(-1).array());
      this.seekEntryIdBytes = new ParsableByteArray(4);
      this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
      this.nalLength = new ParsableByteArray(4);
      this.sampleStrippedBytes = new ParsableByteArray();
      this.subtitleSample = new ParsableByteArray();
      this.encryptionInitializationVector = new ParsableByteArray(8);
      this.encryptionSubsampleData = new ParsableByteArray();
   }

   // $FF: synthetic method
   static UUID access$400() {
      return WAVE_SUBFORMAT_PCM;
   }

   private SeekMap buildSeekMap() {
      if (this.segmentContentPosition != -1L && this.durationUs != -9223372036854775807L) {
         LongArray var1 = this.cueTimesUs;
         if (var1 != null && var1.size() != 0) {
            var1 = this.cueClusterPositions;
            if (var1 != null && var1.size() == this.cueTimesUs.size()) {
               int var2 = this.cueTimesUs.size();
               int[] var3 = new int[var2];
               long[] var9 = new long[var2];
               long[] var4 = new long[var2];
               long[] var5 = new long[var2];
               byte var6 = 0;
               int var7 = 0;

               while(true) {
                  int var8 = var6;
                  if (var7 >= var2) {
                     while(true) {
                        var7 = var2 - 1;
                        if (var8 >= var7) {
                           var3[var7] = (int)(this.segmentContentPosition + this.segmentContentSize - var9[var7]);
                           var4[var7] = this.durationUs - var5[var7];
                           this.cueTimesUs = null;
                           this.cueClusterPositions = null;
                           return new ChunkIndex(var3, var9, var4, var5);
                        }

                        var7 = var8 + 1;
                        var3[var8] = (int)(var9[var7] - var9[var8]);
                        var4[var8] = var5[var7] - var5[var8];
                        var8 = var7;
                     }
                  }

                  var5[var7] = this.cueTimesUs.get(var7);
                  var9[var7] = this.segmentContentPosition + this.cueClusterPositions.get(var7);
                  ++var7;
               }
            }
         }
      }

      this.cueTimesUs = null;
      this.cueClusterPositions = null;
      return new SeekMap.Unseekable(this.durationUs);
   }

   private void commitSampleToOutput(MatroskaExtractor.Track var1, long var2) {
      MatroskaExtractor.TrueHdSampleRechunker var4 = var1.trueHdSampleRechunker;
      if (var4 != null) {
         var4.sampleMetadata(var1, var2);
      } else {
         if ("S_TEXT/UTF8".equals(var1.codecId)) {
            this.commitSubtitleSample(var1, "%02d:%02d:%02d,%03d", 19, 1000L, SUBRIP_TIMECODE_EMPTY);
         } else if ("S_TEXT/ASS".equals(var1.codecId)) {
            this.commitSubtitleSample(var1, "%01d:%02d:%02d:%02d", 21, 10000L, SSA_TIMECODE_EMPTY);
         }

         var1.output.sampleMetadata(var2, this.blockFlags, this.sampleBytesWritten, 0, var1.cryptoData);
      }

      this.sampleRead = true;
      this.resetSample();
   }

   private void commitSubtitleSample(MatroskaExtractor.Track var1, String var2, int var3, long var4, byte[] var6) {
      setSampleDuration(this.subtitleSample.data, this.blockDurationUs, var2, var3, var4, var6);
      TrackOutput var8 = var1.output;
      ParsableByteArray var7 = this.subtitleSample;
      var8.sampleData(var7, var7.limit());
      this.sampleBytesWritten += this.subtitleSample.limit();
   }

   private static int[] ensureArrayCapacity(int[] var0, int var1) {
      if (var0 == null) {
         return new int[var1];
      } else {
         return var0.length >= var1 ? var0 : new int[Math.max(var0.length * 2, var1)];
      }
   }

   private static boolean isCodecSupported(String var0) {
      boolean var1;
      if (!"V_VP8".equals(var0) && !"V_VP9".equals(var0) && !"V_MPEG2".equals(var0) && !"V_MPEG4/ISO/SP".equals(var0) && !"V_MPEG4/ISO/ASP".equals(var0) && !"V_MPEG4/ISO/AP".equals(var0) && !"V_MPEG4/ISO/AVC".equals(var0) && !"V_MPEGH/ISO/HEVC".equals(var0) && !"V_MS/VFW/FOURCC".equals(var0) && !"V_THEORA".equals(var0) && !"A_OPUS".equals(var0) && !"A_VORBIS".equals(var0) && !"A_AAC".equals(var0) && !"A_MPEG/L2".equals(var0) && !"A_MPEG/L3".equals(var0) && !"A_AC3".equals(var0) && !"A_EAC3".equals(var0) && !"A_TRUEHD".equals(var0) && !"A_DTS".equals(var0) && !"A_DTS/EXPRESS".equals(var0) && !"A_DTS/LOSSLESS".equals(var0) && !"A_FLAC".equals(var0) && !"A_MS/ACM".equals(var0) && !"A_PCM/INT/LIT".equals(var0) && !"S_TEXT/UTF8".equals(var0) && !"S_TEXT/ASS".equals(var0) && !"S_VOBSUB".equals(var0) && !"S_HDMV/PGS".equals(var0) && !"S_DVBSUB".equals(var0)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new MatroskaExtractor()};
   }

   private boolean maybeSeekForCues(PositionHolder var1, long var2) {
      if (this.seekForCues) {
         this.seekPositionAfterBuildingCues = var2;
         var1.position = this.cuesContentPosition;
         this.seekForCues = false;
         return true;
      } else {
         if (this.sentSeekMap) {
            var2 = this.seekPositionAfterBuildingCues;
            if (var2 != -1L) {
               var1.position = var2;
               this.seekPositionAfterBuildingCues = -1L;
               return true;
            }
         }

         return false;
      }
   }

   private void readScratch(ExtractorInput var1, int var2) throws IOException, InterruptedException {
      if (this.scratch.limit() < var2) {
         ParsableByteArray var3;
         if (this.scratch.capacity() < var2) {
            var3 = this.scratch;
            byte[] var4 = var3.data;
            var3.reset(Arrays.copyOf(var4, Math.max(var4.length * 2, var2)), this.scratch.limit());
         }

         var3 = this.scratch;
         var1.readFully(var3.data, var3.limit(), var2 - this.scratch.limit());
         this.scratch.setLimit(var2);
      }
   }

   private int readToOutput(ExtractorInput var1, TrackOutput var2, int var3) throws IOException, InterruptedException {
      int var4 = this.sampleStrippedBytes.bytesLeft();
      if (var4 > 0) {
         var3 = Math.min(var3, var4);
         var2.sampleData(this.sampleStrippedBytes, var3);
      } else {
         var3 = var2.sampleData(var1, var3, false);
      }

      this.sampleBytesRead += var3;
      this.sampleBytesWritten += var3;
      return var3;
   }

   private void readToTarget(ExtractorInput var1, byte[] var2, int var3, int var4) throws IOException, InterruptedException {
      int var5 = Math.min(var4, this.sampleStrippedBytes.bytesLeft());
      var1.readFully(var2, var3 + var5, var4 - var5);
      if (var5 > 0) {
         this.sampleStrippedBytes.readBytes(var2, var3, var5);
      }

      this.sampleBytesRead += var4;
   }

   private void resetSample() {
      this.sampleBytesRead = 0;
      this.sampleBytesWritten = 0;
      this.sampleCurrentNalBytesRemaining = 0;
      this.sampleEncodingHandled = false;
      this.sampleSignalByteRead = false;
      this.samplePartitionCountRead = false;
      this.samplePartitionCount = 0;
      this.sampleSignalByte = (byte)0;
      this.sampleInitializationVectorRead = false;
      this.sampleStrippedBytes.reset();
   }

   private long scaleTimecodeToUs(long var1) throws ParserException {
      long var3 = this.timecodeScale;
      if (var3 != -9223372036854775807L) {
         return Util.scaleLargeTimestamp(var1, var3, 1000L);
      } else {
         throw new ParserException("Can't scale timecode prior to timecodeScale being set.");
      }
   }

   private static void setSampleDuration(byte[] var0, long var1, String var3, int var4, long var5, byte[] var7) {
      byte[] var12;
      if (var1 == -9223372036854775807L) {
         var12 = var7;
      } else {
         int var8 = (int)(var1 / 3600000000L);
         var1 -= (long)(var8 * 3600) * 1000000L;
         int var9 = (int)(var1 / 60000000L);
         var1 -= (long)(var9 * 60) * 1000000L;
         int var10 = (int)(var1 / 1000000L);
         int var11 = (int)((var1 - (long)var10 * 1000000L) / var5);
         var12 = Util.getUtf8Bytes(String.format(Locale.US, var3, var8, var9, var10, var11));
      }

      System.arraycopy(var12, 0, var0, var4, var7.length);
   }

   private void writeSampleData(ExtractorInput var1, MatroskaExtractor.Track var2, int var3) throws IOException, InterruptedException {
      if ("S_TEXT/UTF8".equals(var2.codecId)) {
         this.writeSubtitleSampleData(var1, SUBRIP_PREFIX, var3);
      } else if ("S_TEXT/ASS".equals(var2.codecId)) {
         this.writeSubtitleSampleData(var1, SSA_PREFIX, var3);
      } else {
         TrackOutput var4 = var2.output;
         boolean var5 = this.sampleEncodingHandled;
         boolean var6 = true;
         byte[] var8;
         int var13;
         int var15;
         if (!var5) {
            if (!var2.hasContentEncryption) {
               var8 = var2.sampleStrippedBytes;
               if (var8 != null) {
                  this.sampleStrippedBytes.reset(var8, var8.length);
               }
            } else {
               this.blockFlags &= -1073741825;
               var5 = this.sampleSignalByteRead;
               short var7 = 128;
               if (!var5) {
                  var1.readFully(this.scratch.data, 0, 1);
                  ++this.sampleBytesRead;
                  var8 = this.scratch.data;
                  if ((var8[0] & 128) == 128) {
                     throw new ParserException("Extension bit is set in signal byte");
                  }

                  this.sampleSignalByte = (byte)var8[0];
                  this.sampleSignalByteRead = true;
               }

               boolean var9;
               if ((this.sampleSignalByte & 1) == 1) {
                  var9 = true;
               } else {
                  var9 = false;
               }

               if (var9) {
                  if ((this.sampleSignalByte & 2) == 2) {
                     var9 = true;
                  } else {
                     var9 = false;
                  }

                  this.blockFlags |= 1073741824;
                  if (!this.sampleInitializationVectorRead) {
                     var1.readFully(this.encryptionInitializationVector.data, 0, 8);
                     this.sampleBytesRead += 8;
                     this.sampleInitializationVectorRead = true;
                     var8 = this.scratch.data;
                     if (!var9) {
                        var7 = 0;
                     }

                     var8[0] = (byte)((byte)(var7 | 8));
                     this.scratch.setPosition(0);
                     var4.sampleData(this.scratch, 1);
                     ++this.sampleBytesWritten;
                     this.encryptionInitializationVector.setPosition(0);
                     var4.sampleData(this.encryptionInitializationVector, 8);
                     this.sampleBytesWritten += 8;
                  }

                  if (var9) {
                     if (!this.samplePartitionCountRead) {
                        var1.readFully(this.scratch.data, 0, 1);
                        ++this.sampleBytesRead;
                        this.scratch.setPosition(0);
                        this.samplePartitionCount = this.scratch.readUnsignedByte();
                        this.samplePartitionCountRead = true;
                     }

                     var15 = this.samplePartitionCount * 4;
                     this.scratch.reset(var15);
                     var1.readFully(this.scratch.data, 0, var15);
                     this.sampleBytesRead += var15;
                     short var10 = (short)(this.samplePartitionCount / 2 + 1);
                     int var11 = var10 * 6 + 2;
                     ByteBuffer var14 = this.encryptionSubsampleDataBuffer;
                     if (var14 == null || var14.capacity() < var11) {
                        this.encryptionSubsampleDataBuffer = ByteBuffer.allocate(var11);
                     }

                     this.encryptionSubsampleDataBuffer.position(0);
                     this.encryptionSubsampleDataBuffer.putShort(var10);
                     var15 = 0;
                     var13 = 0;

                     while(true) {
                        int var12 = this.samplePartitionCount;
                        if (var15 >= var12) {
                           var15 = var3 - this.sampleBytesRead - var13;
                           if (var12 % 2 == 1) {
                              this.encryptionSubsampleDataBuffer.putInt(var15);
                           } else {
                              this.encryptionSubsampleDataBuffer.putShort((short)var15);
                              this.encryptionSubsampleDataBuffer.putInt(0);
                           }

                           this.encryptionSubsampleData.reset(this.encryptionSubsampleDataBuffer.array(), var11);
                           var4.sampleData(this.encryptionSubsampleData, var11);
                           this.sampleBytesWritten += var11;
                           break;
                        }

                        var12 = this.scratch.readUnsignedIntToInt();
                        if (var15 % 2 == 0) {
                           this.encryptionSubsampleDataBuffer.putShort((short)(var12 - var13));
                        } else {
                           this.encryptionSubsampleDataBuffer.putInt(var12 - var13);
                        }

                        ++var15;
                        var13 = var12;
                     }
                  }
               }
            }

            this.sampleEncodingHandled = true;
         }

         var3 += this.sampleStrippedBytes.limit();
         if (!"V_MPEG4/ISO/AVC".equals(var2.codecId) && !"V_MPEGH/ISO/HEVC".equals(var2.codecId)) {
            if (var2.trueHdSampleRechunker != null) {
               if (this.sampleStrippedBytes.limit() != 0) {
                  var6 = false;
               }

               Assertions.checkState(var6);
               var2.trueHdSampleRechunker.startSample(var1, this.blockFlags, var3);
            }

            while(true) {
               var15 = this.sampleBytesRead;
               if (var15 >= var3) {
                  break;
               }

               this.readToOutput(var1, var4, var3 - var15);
            }
         } else {
            var8 = this.nalLength.data;
            var8[0] = (byte)0;
            var8[1] = (byte)0;
            var8[2] = (byte)0;
            var13 = var2.nalUnitLengthFieldLength;

            while(this.sampleBytesRead < var3) {
               var15 = this.sampleCurrentNalBytesRemaining;
               if (var15 == 0) {
                  this.readToTarget(var1, var8, 4 - var13, var13);
                  this.nalLength.setPosition(0);
                  this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
                  this.nalStartCode.setPosition(0);
                  var4.sampleData(this.nalStartCode, 4);
                  this.sampleBytesWritten += 4;
               } else {
                  this.sampleCurrentNalBytesRemaining = var15 - this.readToOutput(var1, var4, var15);
               }
            }
         }

         if ("A_VORBIS".equals(var2.codecId)) {
            this.vorbisNumPageSamples.setPosition(0);
            var4.sampleData(this.vorbisNumPageSamples, 4);
            this.sampleBytesWritten += 4;
         }

      }
   }

   private void writeSubtitleSampleData(ExtractorInput var1, byte[] var2, int var3) throws IOException, InterruptedException {
      int var4 = var2.length + var3;
      if (this.subtitleSample.capacity() < var4) {
         this.subtitleSample.data = Arrays.copyOf(var2, var4 + var3);
      } else {
         System.arraycopy(var2, 0, this.subtitleSample.data, 0, var2.length);
      }

      var1.readFully(this.subtitleSample.data, var2.length, var3);
      this.subtitleSample.reset(var4);
   }

   void binaryElement(int var1, int var2, ExtractorInput var3) throws IOException, InterruptedException {
      MatroskaExtractor.Track var4;
      StringBuilder var18;
      if (var1 != 161 && var1 != 163) {
         if (var1 != 16981) {
            if (var1 != 18402) {
               if (var1 != 21419) {
                  if (var1 != 25506) {
                     if (var1 != 30322) {
                        var18 = new StringBuilder();
                        var18.append("Unexpected id: ");
                        var18.append(var1);
                        throw new ParserException(var18.toString());
                     }

                     var4 = this.currentTrack;
                     var4.projectionData = new byte[var2];
                     var3.readFully(var4.projectionData, 0, var2);
                  } else {
                     var4 = this.currentTrack;
                     var4.codecPrivate = new byte[var2];
                     var3.readFully(var4.codecPrivate, 0, var2);
                  }
               } else {
                  Arrays.fill(this.seekEntryIdBytes.data, (byte)0);
                  var3.readFully(this.seekEntryIdBytes.data, 4 - var2, var2);
                  this.seekEntryIdBytes.setPosition(0);
                  this.seekEntryId = (int)this.seekEntryIdBytes.readUnsignedInt();
               }
            } else {
               byte[] var19 = new byte[var2];
               var3.readFully(var19, 0, var2);
               this.currentTrack.cryptoData = new TrackOutput.CryptoData(1, var19, 0, 0);
            }
         } else {
            var4 = this.currentTrack;
            var4.sampleStrippedBytes = new byte[var2];
            var3.readFully(var4.sampleStrippedBytes, 0, var2);
         }
      } else {
         if (this.blockState == 0) {
            this.blockTrackNumber = (int)this.varintReader.readUnsignedVarint(var3, false, true, 8);
            this.blockTrackNumberLength = this.varintReader.getLastLength();
            this.blockDurationUs = -9223372036854775807L;
            this.blockState = 1;
            this.scratch.reset();
         }

         var4 = (MatroskaExtractor.Track)this.tracks.get(this.blockTrackNumber);
         if (var4 == null) {
            var3.skipFully(var2 - this.blockTrackNumberLength);
            this.blockState = 0;
            return;
         }

         if (this.blockState == 1) {
            this.readScratch(var3, 3);
            int var5 = (this.scratch.data[2] & 6) >> 1;
            byte[] var22;
            if (var5 == 0) {
               this.blockLacingSampleCount = 1;
               this.blockLacingSampleSizes = ensureArrayCapacity(this.blockLacingSampleSizes, 1);
               this.blockLacingSampleSizes[0] = var2 - this.blockTrackNumberLength - 3;
            } else {
               if (var1 != 163) {
                  throw new ParserException("Lacing only supported in SimpleBlocks.");
               }

               this.readScratch(var3, 4);
               this.blockLacingSampleCount = (this.scratch.data[3] & 255) + 1;
               this.blockLacingSampleSizes = ensureArrayCapacity(this.blockLacingSampleSizes, this.blockLacingSampleCount);
               int var6;
               if (var5 == 2) {
                  var6 = this.blockTrackNumberLength;
                  var5 = this.blockLacingSampleCount;
                  var2 = (var2 - var6 - 4) / var5;
                  Arrays.fill(this.blockLacingSampleSizes, 0, var5, var2);
               } else {
                  int var7;
                  int var8;
                  int var9;
                  int[] var10;
                  if (var5 == 1) {
                     var7 = 0;
                     var5 = 4;
                     var6 = 0;

                     while(true) {
                        var8 = this.blockLacingSampleCount;
                        if (var7 >= var8 - 1) {
                           this.blockLacingSampleSizes[var8 - 1] = var2 - this.blockTrackNumberLength - var5 - var6;
                           break;
                        }

                        this.blockLacingSampleSizes[var7] = 0;
                        var8 = var5;

                        do {
                           var5 = var8 + 1;
                           this.readScratch(var3, var5);
                           var9 = this.scratch.data[var5 - 1] & 255;
                           var10 = this.blockLacingSampleSizes;
                           var10[var7] += var9;
                           var8 = var5;
                        } while(var9 == 255);

                        var6 += var10[var7];
                        ++var7;
                     }
                  } else {
                     if (var5 != 3) {
                        var18 = new StringBuilder();
                        var18.append("Unexpected lacing value: ");
                        var18.append(var5);
                        throw new ParserException(var18.toString());
                     }

                     var7 = 0;
                     var5 = 4;
                     var6 = 0;

                     while(true) {
                        var8 = this.blockLacingSampleCount;
                        if (var7 >= var8 - 1) {
                           this.blockLacingSampleSizes[var8 - 1] = var2 - this.blockTrackNumberLength - var5 - var6;
                           break;
                        }

                        this.blockLacingSampleSizes[var7] = 0;
                        var9 = var5 + 1;
                        this.readScratch(var3, var9);
                        var22 = this.scratch.data;
                        int var11 = var9 - 1;
                        if (var22[var11] == 0) {
                           throw new ParserException("No valid varint length mask found");
                        }

                        long var12 = 0L;
                        var8 = 0;

                        long var14;
                        label134:
                        while(true) {
                           var5 = var9;
                           var14 = var12;
                           if (var8 >= 8) {
                              break;
                           }

                           var5 = 1 << 7 - var8;
                           if ((this.scratch.data[var11] & var5) != 0) {
                              var9 += var8;
                              this.readScratch(var3, var9);
                              var14 = (long)(this.scratch.data[var11] & 255 & ~var5);
                              var5 = var11 + 1;

                              while(true) {
                                 var12 = var14;
                                 if (var5 >= var9) {
                                    var5 = var9;
                                    var14 = var14;
                                    if (var7 > 0) {
                                       var14 = var12 - ((1L << var8 * 7 + 6) - 1L);
                                       var5 = var9;
                                    }
                                    break label134;
                                 }

                                 var14 = var14 << 8 | (long)(this.scratch.data[var5] & 255);
                                 ++var5;
                              }
                           }

                           ++var8;
                        }

                        if (var14 < -2147483648L || var14 > 2147483647L) {
                           throw new ParserException("EBML lacing sample size out of range.");
                        }

                        var8 = (int)var14;
                        var10 = this.blockLacingSampleSizes;
                        if (var7 != 0) {
                           var8 += var10[var7 - 1];
                        }

                        var10[var7] = var8;
                        var6 += this.blockLacingSampleSizes[var7];
                        ++var7;
                     }
                  }
               }
            }

            var22 = this.scratch.data;
            byte var16 = var22[0];
            byte var20 = var22[1];
            this.blockTimeUs = this.clusterTimecodeUs + this.scaleTimecodeToUs((long)(var20 & 255 | var16 << 8));
            boolean var21;
            if ((this.scratch.data[2] & 8) == 8) {
               var21 = true;
            } else {
               var21 = false;
            }

            byte var17;
            if (var4.type == 2 || var1 == 163 && (this.scratch.data[2] & 128) == 128) {
               var17 = 1;
            } else {
               var17 = 0;
            }

            if (var21) {
               var5 = Integer.MIN_VALUE;
            } else {
               var5 = 0;
            }

            this.blockFlags = var17 | var5;
            this.blockState = 2;
            this.blockLacingSampleIndex = 0;
         }

         if (var1 != 163) {
            this.writeSampleData(var3, var4, this.blockLacingSampleSizes[0]);
         } else {
            while(true) {
               var1 = this.blockLacingSampleIndex;
               if (var1 >= this.blockLacingSampleCount) {
                  this.blockState = 0;
                  break;
               }

               this.writeSampleData(var3, var4, this.blockLacingSampleSizes[var1]);
               this.commitSampleToOutput(var4, this.blockTimeUs + (long)(this.blockLacingSampleIndex * var4.defaultSampleDurationNs / 1000));
               ++this.blockLacingSampleIndex;
            }
         }
      }

   }

   void endMasterElement(int var1) throws ParserException {
      if (var1 != 160) {
         MatroskaExtractor.Track var4;
         if (var1 != 174) {
            long var2;
            if (var1 != 19899) {
               if (var1 != 25152) {
                  if (var1 != 28032) {
                     if (var1 != 357149030) {
                        if (var1 != 374648427) {
                           if (var1 == 475249515 && !this.sentSeekMap) {
                              this.extractorOutput.seekMap(this.buildSeekMap());
                              this.sentSeekMap = true;
                           }
                        } else {
                           if (this.tracks.size() == 0) {
                              throw new ParserException("No valid tracks were found");
                           }

                           this.extractorOutput.endTracks();
                        }
                     } else {
                        if (this.timecodeScale == -9223372036854775807L) {
                           this.timecodeScale = 1000000L;
                        }

                        var2 = this.durationTimecode;
                        if (var2 != -9223372036854775807L) {
                           this.durationUs = this.scaleTimecodeToUs(var2);
                        }
                     }
                  } else {
                     var4 = this.currentTrack;
                     if (var4.hasContentEncryption && var4.sampleStrippedBytes != null) {
                        throw new ParserException("Combining encryption and compression is not supported");
                     }
                  }
               } else {
                  MatroskaExtractor.Track var7 = this.currentTrack;
                  if (var7.hasContentEncryption) {
                     TrackOutput.CryptoData var6 = var7.cryptoData;
                     if (var6 == null) {
                        throw new ParserException("Encrypted Track found but ContentEncKeyID was not found");
                     }

                     var7.drmInitData = new DrmInitData(new DrmInitData.SchemeData[]{new DrmInitData.SchemeData(C.UUID_NIL, "video/webm", var6.encryptionKey)});
                  }
               }
            } else {
               var1 = this.seekEntryId;
               if (var1 != -1) {
                  var2 = this.seekEntryPosition;
                  if (var2 != -1L) {
                     if (var1 == 475249515) {
                        this.cuesContentPosition = var2;
                     }

                     return;
                  }
               }

               throw new ParserException("Mandatory element SeekID or SeekPosition not found");
            }
         } else {
            if (isCodecSupported(this.currentTrack.codecId)) {
               var4 = this.currentTrack;
               var4.initializeOutput(this.extractorOutput, var4.number);
               SparseArray var5 = this.tracks;
               var4 = this.currentTrack;
               var5.put(var4.number, var4);
            }

            this.currentTrack = null;
         }
      } else {
         if (this.blockState != 2) {
            return;
         }

         if (!this.sampleSeenReferenceBlock) {
            this.blockFlags |= 1;
         }

         this.commitSampleToOutput((MatroskaExtractor.Track)this.tracks.get(this.blockTrackNumber), this.blockTimeUs);
         this.blockState = 0;
      }

   }

   void floatElement(int var1, double var2) {
      if (var1 != 181) {
         if (var1 != 17545) {
            switch(var1) {
            case 21969:
               this.currentTrack.primaryRChromaticityX = (float)var2;
               break;
            case 21970:
               this.currentTrack.primaryRChromaticityY = (float)var2;
               break;
            case 21971:
               this.currentTrack.primaryGChromaticityX = (float)var2;
               break;
            case 21972:
               this.currentTrack.primaryGChromaticityY = (float)var2;
               break;
            case 21973:
               this.currentTrack.primaryBChromaticityX = (float)var2;
               break;
            case 21974:
               this.currentTrack.primaryBChromaticityY = (float)var2;
               break;
            case 21975:
               this.currentTrack.whitePointChromaticityX = (float)var2;
               break;
            case 21976:
               this.currentTrack.whitePointChromaticityY = (float)var2;
               break;
            case 21977:
               this.currentTrack.maxMasteringLuminance = (float)var2;
               break;
            case 21978:
               this.currentTrack.minMasteringLuminance = (float)var2;
               break;
            default:
               switch(var1) {
               case 30323:
                  this.currentTrack.projectionPoseYaw = (float)var2;
                  break;
               case 30324:
                  this.currentTrack.projectionPosePitch = (float)var2;
                  break;
               case 30325:
                  this.currentTrack.projectionPoseRoll = (float)var2;
               }
            }
         } else {
            this.durationTimecode = (long)var2;
         }
      } else {
         this.currentTrack.sampleRate = (int)var2;
      }

   }

   public void init(ExtractorOutput var1) {
      this.extractorOutput = var1;
   }

   void integerElement(int var1, long var2) throws ParserException {
      StringBuilder var6;
      if (var1 != 20529) {
         if (var1 != 20530) {
            boolean var4 = false;
            boolean var5 = false;
            MatroskaExtractor.Track var7;
            switch(var1) {
            case 131:
               this.currentTrack.type = (int)var2;
               break;
            case 136:
               var7 = this.currentTrack;
               var5 = var4;
               if (var2 == 1L) {
                  var5 = true;
               }

               var7.flagDefault = var5;
               break;
            case 155:
               this.blockDurationUs = this.scaleTimecodeToUs(var2);
               break;
            case 159:
               this.currentTrack.channelCount = (int)var2;
               break;
            case 176:
               this.currentTrack.width = (int)var2;
               break;
            case 179:
               this.cueTimesUs.add(this.scaleTimecodeToUs(var2));
               break;
            case 186:
               this.currentTrack.height = (int)var2;
               break;
            case 215:
               this.currentTrack.number = (int)var2;
               break;
            case 231:
               this.clusterTimecodeUs = this.scaleTimecodeToUs(var2);
               break;
            case 241:
               if (!this.seenClusterPositionForCurrentCuePoint) {
                  this.cueClusterPositions.add(var2);
                  this.seenClusterPositionForCurrentCuePoint = true;
               }
               break;
            case 251:
               this.sampleSeenReferenceBlock = true;
               break;
            case 16980:
               if (var2 != 3L) {
                  var6 = new StringBuilder();
                  var6.append("ContentCompAlgo ");
                  var6.append(var2);
                  var6.append(" not supported");
                  throw new ParserException(var6.toString());
               }
               break;
            case 17029:
               if (var2 >= 1L && var2 <= 2L) {
                  break;
               }

               var6 = new StringBuilder();
               var6.append("DocTypeReadVersion ");
               var6.append(var2);
               var6.append(" not supported");
               throw new ParserException(var6.toString());
            case 17143:
               if (var2 != 1L) {
                  var6 = new StringBuilder();
                  var6.append("EBMLReadVersion ");
                  var6.append(var2);
                  var6.append(" not supported");
                  throw new ParserException(var6.toString());
               }
               break;
            case 18401:
               if (var2 != 5L) {
                  var6 = new StringBuilder();
                  var6.append("ContentEncAlgo ");
                  var6.append(var2);
                  var6.append(" not supported");
                  throw new ParserException(var6.toString());
               }
               break;
            case 18408:
               if (var2 != 1L) {
                  var6 = new StringBuilder();
                  var6.append("AESSettingsCipherMode ");
                  var6.append(var2);
                  var6.append(" not supported");
                  throw new ParserException(var6.toString());
               }
               break;
            case 21420:
               this.seekEntryPosition = var2 + this.segmentContentPosition;
               break;
            case 21432:
               var1 = (int)var2;
               if (var1 != 0) {
                  if (var1 != 1) {
                     if (var1 != 3) {
                        if (var1 == 15) {
                           this.currentTrack.stereoMode = 3;
                        }
                     } else {
                        this.currentTrack.stereoMode = 1;
                     }
                  } else {
                     this.currentTrack.stereoMode = 2;
                  }
               } else {
                  this.currentTrack.stereoMode = 0;
               }
               break;
            case 21680:
               this.currentTrack.displayWidth = (int)var2;
               break;
            case 21682:
               this.currentTrack.displayUnit = (int)var2;
               break;
            case 21690:
               this.currentTrack.displayHeight = (int)var2;
               break;
            case 21930:
               var7 = this.currentTrack;
               if (var2 == 1L) {
                  var5 = true;
               }

               var7.flagForced = var5;
               break;
            case 22186:
               this.currentTrack.codecDelayNs = var2;
               break;
            case 22203:
               this.currentTrack.seekPreRollNs = var2;
               break;
            case 25188:
               this.currentTrack.audioBitDepth = (int)var2;
               break;
            case 30321:
               var1 = (int)var2;
               if (var1 != 0) {
                  if (var1 != 1) {
                     if (var1 != 2) {
                        if (var1 == 3) {
                           this.currentTrack.projectionType = 3;
                        }
                     } else {
                        this.currentTrack.projectionType = 2;
                     }
                  } else {
                     this.currentTrack.projectionType = 1;
                  }
               } else {
                  this.currentTrack.projectionType = 0;
               }
               break;
            case 2352003:
               this.currentTrack.defaultSampleDurationNs = (int)var2;
               break;
            case 2807729:
               this.timecodeScale = var2;
               break;
            default:
               switch(var1) {
               case 21945:
                  var1 = (int)var2;
                  if (var1 != 1) {
                     if (var1 == 2) {
                        this.currentTrack.colorRange = 1;
                     }
                  } else {
                     this.currentTrack.colorRange = 2;
                  }
                  break;
               case 21946:
                  var1 = (int)var2;
                  if (var1 != 1) {
                     if (var1 == 16) {
                        this.currentTrack.colorTransfer = 6;
                        break;
                     }

                     if (var1 == 18) {
                        this.currentTrack.colorTransfer = 7;
                        break;
                     }

                     if (var1 != 6 && var1 != 7) {
                        break;
                     }
                  }

                  this.currentTrack.colorTransfer = 3;
                  break;
               case 21947:
                  var7 = this.currentTrack;
                  var7.hasColorInfo = true;
                  var1 = (int)var2;
                  if (var1 != 1) {
                     if (var1 != 9) {
                        if (var1 == 4 || var1 == 5 || var1 == 6 || var1 == 7) {
                           this.currentTrack.colorSpace = 2;
                        }
                     } else {
                        var7.colorSpace = 6;
                     }
                  } else {
                     var7.colorSpace = 1;
                  }
                  break;
               case 21948:
                  this.currentTrack.maxContentLuminance = (int)var2;
                  break;
               case 21949:
                  this.currentTrack.maxFrameAverageLuminance = (int)var2;
               }
            }
         } else if (var2 != 1L) {
            var6 = new StringBuilder();
            var6.append("ContentEncodingScope ");
            var6.append(var2);
            var6.append(" not supported");
            throw new ParserException(var6.toString());
         }
      } else if (var2 != 0L) {
         var6 = new StringBuilder();
         var6.append("ContentEncodingOrder ");
         var6.append(var2);
         var6.append(" not supported");
         throw new ParserException(var6.toString());
      }

   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      int var3 = 0;
      this.sampleRead = false;
      boolean var4 = true;

      while(var4 && !this.sampleRead) {
         boolean var5 = this.reader.read(var1);
         var4 = var5;
         if (var5) {
            var4 = var5;
            if (this.maybeSeekForCues(var2, var1.getPosition())) {
               return 1;
            }
         }
      }

      if (var4) {
         return 0;
      } else {
         while(var3 < this.tracks.size()) {
            ((MatroskaExtractor.Track)this.tracks.valueAt(var3)).outputPendingSampleMetadata();
            ++var3;
         }

         return -1;
      }
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      this.clusterTimecodeUs = -9223372036854775807L;
      int var5 = 0;
      this.blockState = 0;
      this.reader.reset();
      this.varintReader.reset();
      this.resetSample();

      while(var5 < this.tracks.size()) {
         ((MatroskaExtractor.Track)this.tracks.valueAt(var5)).reset();
         ++var5;
      }

   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      return (new Sniffer()).sniff(var1);
   }

   void startMasterElement(int var1, long var2, long var4) throws ParserException {
      if (var1 != 160) {
         if (var1 != 174) {
            if (var1 != 187) {
               if (var1 != 19899) {
                  if (var1 != 20533) {
                     if (var1 != 21968) {
                        if (var1 != 25152) {
                           if (var1 != 408125543) {
                              if (var1 != 475249515) {
                                 if (var1 == 524531317 && !this.sentSeekMap) {
                                    if (this.seekForCuesEnabled && this.cuesContentPosition != -1L) {
                                       this.seekForCues = true;
                                    } else {
                                       this.extractorOutput.seekMap(new SeekMap.Unseekable(this.durationUs));
                                       this.sentSeekMap = true;
                                    }
                                 }
                              } else {
                                 this.cueTimesUs = new LongArray();
                                 this.cueClusterPositions = new LongArray();
                              }
                           } else {
                              long var6 = this.segmentContentPosition;
                              if (var6 != -1L && var6 != var2) {
                                 throw new ParserException("Multiple Segment elements not supported");
                              }

                              this.segmentContentPosition = var2;
                              this.segmentContentSize = var4;
                           }
                        }
                     } else {
                        this.currentTrack.hasColorInfo = true;
                     }
                  } else {
                     this.currentTrack.hasContentEncryption = true;
                  }
               } else {
                  this.seekEntryId = -1;
                  this.seekEntryPosition = -1L;
               }
            } else {
               this.seenClusterPositionForCurrentCuePoint = false;
            }
         } else {
            this.currentTrack = new MatroskaExtractor.Track();
         }
      } else {
         this.sampleSeenReferenceBlock = false;
      }

   }

   void stringElement(int var1, String var2) throws ParserException {
      if (var1 != 134) {
         if (var1 != 17026) {
            if (var1 != 21358) {
               if (var1 == 2274716) {
                  this.currentTrack.language = var2;
               }
            } else {
               this.currentTrack.name = var2;
            }
         } else if (!"webm".equals(var2) && !"matroska".equals(var2)) {
            StringBuilder var3 = new StringBuilder();
            var3.append("DocType ");
            var3.append(var2);
            var3.append(" not supported");
            throw new ParserException(var3.toString());
         }
      } else {
         this.currentTrack.codecId = var2;
      }

   }

   private final class InnerEbmlReaderOutput implements EbmlReaderOutput {
      private InnerEbmlReaderOutput() {
      }

      // $FF: synthetic method
      InnerEbmlReaderOutput(Object var2) {
         this();
      }

      public void binaryElement(int var1, int var2, ExtractorInput var3) throws IOException, InterruptedException {
         MatroskaExtractor.this.binaryElement(var1, var2, var3);
      }

      public void endMasterElement(int var1) throws ParserException {
         MatroskaExtractor.this.endMasterElement(var1);
      }

      public void floatElement(int var1, double var2) throws ParserException {
         MatroskaExtractor.this.floatElement(var1, var2);
      }

      public int getElementType(int var1) {
         switch(var1) {
         case 131:
         case 136:
         case 155:
         case 159:
         case 176:
         case 179:
         case 186:
         case 215:
         case 231:
         case 241:
         case 251:
         case 16980:
         case 17029:
         case 17143:
         case 18401:
         case 18408:
         case 20529:
         case 20530:
         case 21420:
         case 21432:
         case 21680:
         case 21682:
         case 21690:
         case 21930:
         case 21945:
         case 21946:
         case 21947:
         case 21948:
         case 21949:
         case 22186:
         case 22203:
         case 25188:
         case 30321:
         case 2352003:
         case 2807729:
            return 2;
         case 134:
         case 17026:
         case 21358:
         case 2274716:
            return 3;
         case 160:
         case 174:
         case 183:
         case 187:
         case 224:
         case 225:
         case 18407:
         case 19899:
         case 20532:
         case 20533:
         case 21936:
         case 21968:
         case 25152:
         case 28032:
         case 30320:
         case 290298740:
         case 357149030:
         case 374648427:
         case 408125543:
         case 440786851:
         case 475249515:
         case 524531317:
            return 1;
         case 161:
         case 163:
         case 16981:
         case 18402:
         case 21419:
         case 25506:
         case 30322:
            return 4;
         case 181:
         case 17545:
         case 21969:
         case 21970:
         case 21971:
         case 21972:
         case 21973:
         case 21974:
         case 21975:
         case 21976:
         case 21977:
         case 21978:
         case 30323:
         case 30324:
         case 30325:
            return 5;
         default:
            return 0;
         }
      }

      public void integerElement(int var1, long var2) throws ParserException {
         MatroskaExtractor.this.integerElement(var1, var2);
      }

      public boolean isLevel1Element(int var1) {
         boolean var2;
         if (var1 != 357149030 && var1 != 524531317 && var1 != 475249515 && var1 != 374648427) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void startMasterElement(int var1, long var2, long var4) throws ParserException {
         MatroskaExtractor.this.startMasterElement(var1, var2, var4);
      }

      public void stringElement(int var1, String var2) throws ParserException {
         MatroskaExtractor.this.stringElement(var1, var2);
      }
   }

   private static final class Track {
      public int audioBitDepth;
      public int channelCount;
      public long codecDelayNs;
      public String codecId;
      public byte[] codecPrivate;
      public int colorRange;
      public int colorSpace;
      public int colorTransfer;
      public TrackOutput.CryptoData cryptoData;
      public int defaultSampleDurationNs;
      public int displayHeight;
      public int displayUnit;
      public int displayWidth;
      public DrmInitData drmInitData;
      public boolean flagDefault;
      public boolean flagForced;
      public boolean hasColorInfo;
      public boolean hasContentEncryption;
      public int height;
      private String language;
      public int maxContentLuminance;
      public int maxFrameAverageLuminance;
      public float maxMasteringLuminance;
      public float minMasteringLuminance;
      public int nalUnitLengthFieldLength;
      public String name;
      public int number;
      public TrackOutput output;
      public float primaryBChromaticityX;
      public float primaryBChromaticityY;
      public float primaryGChromaticityX;
      public float primaryGChromaticityY;
      public float primaryRChromaticityX;
      public float primaryRChromaticityY;
      public byte[] projectionData;
      public float projectionPosePitch;
      public float projectionPoseRoll;
      public float projectionPoseYaw;
      public int projectionType;
      public int sampleRate;
      public byte[] sampleStrippedBytes;
      public long seekPreRollNs;
      public int stereoMode;
      public MatroskaExtractor.TrueHdSampleRechunker trueHdSampleRechunker;
      public int type;
      public float whitePointChromaticityX;
      public float whitePointChromaticityY;
      public int width;

      private Track() {
         this.width = -1;
         this.height = -1;
         this.displayWidth = -1;
         this.displayHeight = -1;
         this.displayUnit = 0;
         this.projectionType = -1;
         this.projectionPoseYaw = 0.0F;
         this.projectionPosePitch = 0.0F;
         this.projectionPoseRoll = 0.0F;
         this.projectionData = null;
         this.stereoMode = -1;
         this.hasColorInfo = false;
         this.colorSpace = -1;
         this.colorTransfer = -1;
         this.colorRange = -1;
         this.maxContentLuminance = 1000;
         this.maxFrameAverageLuminance = 200;
         this.primaryRChromaticityX = -1.0F;
         this.primaryRChromaticityY = -1.0F;
         this.primaryGChromaticityX = -1.0F;
         this.primaryGChromaticityY = -1.0F;
         this.primaryBChromaticityX = -1.0F;
         this.primaryBChromaticityY = -1.0F;
         this.whitePointChromaticityX = -1.0F;
         this.whitePointChromaticityY = -1.0F;
         this.maxMasteringLuminance = -1.0F;
         this.minMasteringLuminance = -1.0F;
         this.channelCount = 1;
         this.audioBitDepth = -1;
         this.sampleRate = 8000;
         this.codecDelayNs = 0L;
         this.seekPreRollNs = 0L;
         this.flagDefault = true;
         this.language = "eng";
      }

      // $FF: synthetic method
      Track(Object var1) {
         this();
      }

      private byte[] getHdrStaticInfo() {
         if (this.primaryRChromaticityX != -1.0F && this.primaryRChromaticityY != -1.0F && this.primaryGChromaticityX != -1.0F && this.primaryGChromaticityY != -1.0F && this.primaryBChromaticityX != -1.0F && this.primaryBChromaticityY != -1.0F && this.whitePointChromaticityX != -1.0F && this.whitePointChromaticityY != -1.0F && this.maxMasteringLuminance != -1.0F && this.minMasteringLuminance != -1.0F) {
            byte[] var1 = new byte[25];
            ByteBuffer var2 = ByteBuffer.wrap(var1);
            var2.put((byte)0);
            var2.putShort((short)((int)(this.primaryRChromaticityX * 50000.0F + 0.5F)));
            var2.putShort((short)((int)(this.primaryRChromaticityY * 50000.0F + 0.5F)));
            var2.putShort((short)((int)(this.primaryGChromaticityX * 50000.0F + 0.5F)));
            var2.putShort((short)((int)(this.primaryGChromaticityY * 50000.0F + 0.5F)));
            var2.putShort((short)((int)(this.primaryBChromaticityX * 50000.0F + 0.5F)));
            var2.putShort((short)((int)(this.primaryBChromaticityY * 50000.0F + 0.5F)));
            var2.putShort((short)((int)(this.whitePointChromaticityX * 50000.0F + 0.5F)));
            var2.putShort((short)((int)(this.whitePointChromaticityY * 50000.0F + 0.5F)));
            var2.putShort((short)((int)(this.maxMasteringLuminance + 0.5F)));
            var2.putShort((short)((int)(this.minMasteringLuminance + 0.5F)));
            var2.putShort((short)this.maxContentLuminance);
            var2.putShort((short)this.maxFrameAverageLuminance);
            return var1;
         } else {
            return null;
         }
      }

      private static Pair parseFourCcPrivate(ParsableByteArray var0) throws ParserException {
         long var1;
         boolean var10001;
         try {
            var0.skipBytes(16);
            var1 = var0.readLittleEndianUnsignedInt();
         } catch (ArrayIndexOutOfBoundsException var9) {
            var10001 = false;
            throw new ParserException("Error parsing FourCC private data");
         }

         if (var1 == 1482049860L) {
            try {
               return new Pair("video/3gpp", (Object)null);
            } catch (ArrayIndexOutOfBoundsException var4) {
               var10001 = false;
            }
         } else {
            if (var1 != 826496599L) {
               Log.w("MatroskaExtractor", "Unknown FourCC. Setting mimeType to video/x-unknown");
               return new Pair("video/x-unknown", (Object)null);
            }

            int var3;
            byte[] var10;
            try {
               var3 = var0.getPosition() + 20;
               var10 = var0.data;
            } catch (ArrayIndexOutOfBoundsException var7) {
               var10001 = false;
               throw new ParserException("Error parsing FourCC private data");
            }

            while(true) {
               try {
                  if (var3 >= var10.length - 4) {
                     break;
                  }
               } catch (ArrayIndexOutOfBoundsException var8) {
                  var10001 = false;
                  throw new ParserException("Error parsing FourCC private data");
               }

               if (var10[var3] == 0 && var10[var3 + 1] == 0 && var10[var3 + 2] == 1 && var10[var3 + 3] == 15) {
                  try {
                     return new Pair("video/wvc1", Collections.singletonList(Arrays.copyOfRange(var10, var3, var10.length)));
                  } catch (ArrayIndexOutOfBoundsException var5) {
                     var10001 = false;
                     throw new ParserException("Error parsing FourCC private data");
                  }
               }

               ++var3;
            }

            try {
               ParserException var11 = new ParserException("Failed to find FourCC VC1 initialization data");
               throw var11;
            } catch (ArrayIndexOutOfBoundsException var6) {
               var10001 = false;
            }
         }

         throw new ParserException("Error parsing FourCC private data");
      }

      private static boolean parseMsAcmCodecPrivate(ParsableByteArray param0) throws ParserException {
         // $FF: Couldn't be decompiled
      }

      private static List parseVorbisCodecPrivate(byte[] var0) throws ParserException {
         boolean var10001;
         ParserException var14;
         if (var0[0] == 2) {
            int var1 = 1;

            int var2;
            for(var2 = 0; var0[var1] == -1; ++var1) {
               var2 += 255;
            }

            int var3 = var1 + 1;
            int var4 = var2 + var0[var1];
            var2 = 0;

            for(var1 = var3; var0[var1] == -1; ++var1) {
               var2 += 255;
            }

            var3 = var1 + 1;
            byte var15 = var0[var1];
            if (var0[var3] == 1) {
               byte[] var5;
               try {
                  var5 = new byte[var4];
                  System.arraycopy(var0, var3, var5, 0, var4);
               } catch (ArrayIndexOutOfBoundsException var10) {
                  var10001 = false;
                  throw new ParserException("Error parsing vorbis codec private");
               }

               var3 += var4;
               if (var0[var3] == 3) {
                  var2 = var3 + var2 + var15;
                  if (var0[var2] == 5) {
                     try {
                        byte[] var6 = new byte[var0.length - var2];
                        System.arraycopy(var0, var2, var6, 0, var0.length - var2);
                        ArrayList var13 = new ArrayList(2);
                        var13.add(var5);
                        var13.add(var6);
                        return var13;
                     } catch (ArrayIndexOutOfBoundsException var7) {
                        var10001 = false;
                     }
                  } else {
                     try {
                        var14 = new ParserException("Error parsing vorbis codec private");
                        throw var14;
                     } catch (ArrayIndexOutOfBoundsException var8) {
                        var10001 = false;
                     }
                  }
               } else {
                  try {
                     var14 = new ParserException("Error parsing vorbis codec private");
                     throw var14;
                  } catch (ArrayIndexOutOfBoundsException var9) {
                     var10001 = false;
                  }
               }
            } else {
               try {
                  var14 = new ParserException("Error parsing vorbis codec private");
                  throw var14;
               } catch (ArrayIndexOutOfBoundsException var11) {
                  var10001 = false;
               }
            }
         } else {
            try {
               var14 = new ParserException("Error parsing vorbis codec private");
               throw var14;
            } catch (ArrayIndexOutOfBoundsException var12) {
               var10001 = false;
            }
         }

         throw new ParserException("Error parsing vorbis codec private");
      }

      public void initializeOutput(ExtractorOutput var1, int var2) throws ParserException {
         String var3;
         int var4;
         byte var5;
         byte var16;
         label305: {
            var3 = this.codecId;
            var4 = var3.hashCode();
            var5 = 3;
            switch(var4) {
            case -2095576542:
               if (var3.equals("V_MPEG4/ISO/AP")) {
                  var16 = 5;
                  break label305;
               }
               break;
            case -2095575984:
               if (var3.equals("V_MPEG4/ISO/SP")) {
                  var16 = 3;
                  break label305;
               }
               break;
            case -1985379776:
               if (var3.equals("A_MS/ACM")) {
                  var16 = 22;
                  break label305;
               }
               break;
            case -1784763192:
               if (var3.equals("A_TRUEHD")) {
                  var16 = 17;
                  break label305;
               }
               break;
            case -1730367663:
               if (var3.equals("A_VORBIS")) {
                  var16 = 10;
                  break label305;
               }
               break;
            case -1482641358:
               if (var3.equals("A_MPEG/L2")) {
                  var16 = 13;
                  break label305;
               }
               break;
            case -1482641357:
               if (var3.equals("A_MPEG/L3")) {
                  var16 = 14;
                  break label305;
               }
               break;
            case -1373388978:
               if (var3.equals("V_MS/VFW/FOURCC")) {
                  var16 = 8;
                  break label305;
               }
               break;
            case -933872740:
               if (var3.equals("S_DVBSUB")) {
                  var16 = 28;
                  break label305;
               }
               break;
            case -538363189:
               if (var3.equals("V_MPEG4/ISO/ASP")) {
                  var16 = 4;
                  break label305;
               }
               break;
            case -538363109:
               if (var3.equals("V_MPEG4/ISO/AVC")) {
                  var16 = 6;
                  break label305;
               }
               break;
            case -425012669:
               if (var3.equals("S_VOBSUB")) {
                  var16 = 26;
                  break label305;
               }
               break;
            case -356037306:
               if (var3.equals("A_DTS/LOSSLESS")) {
                  var16 = 20;
                  break label305;
               }
               break;
            case 62923557:
               if (var3.equals("A_AAC")) {
                  var16 = 12;
                  break label305;
               }
               break;
            case 62923603:
               if (var3.equals("A_AC3")) {
                  var16 = 15;
                  break label305;
               }
               break;
            case 62927045:
               if (var3.equals("A_DTS")) {
                  var16 = 18;
                  break label305;
               }
               break;
            case 82338133:
               if (var3.equals("V_VP8")) {
                  var16 = 0;
                  break label305;
               }
               break;
            case 82338134:
               if (var3.equals("V_VP9")) {
                  var16 = 1;
                  break label305;
               }
               break;
            case 99146302:
               if (var3.equals("S_HDMV/PGS")) {
                  var16 = 27;
                  break label305;
               }
               break;
            case 444813526:
               if (var3.equals("V_THEORA")) {
                  var16 = 9;
                  break label305;
               }
               break;
            case 542569478:
               if (var3.equals("A_DTS/EXPRESS")) {
                  var16 = 19;
                  break label305;
               }
               break;
            case 725957860:
               if (var3.equals("A_PCM/INT/LIT")) {
                  var16 = 23;
                  break label305;
               }
               break;
            case 738597099:
               if (var3.equals("S_TEXT/ASS")) {
                  var16 = 25;
                  break label305;
               }
               break;
            case 855502857:
               if (var3.equals("V_MPEGH/ISO/HEVC")) {
                  var16 = 7;
                  break label305;
               }
               break;
            case 1422270023:
               if (var3.equals("S_TEXT/UTF8")) {
                  var16 = 24;
                  break label305;
               }
               break;
            case 1809237540:
               if (var3.equals("V_MPEG2")) {
                  var16 = 2;
                  break label305;
               }
               break;
            case 1950749482:
               if (var3.equals("A_EAC3")) {
                  var16 = 16;
                  break label305;
               }
               break;
            case 1950789798:
               if (var3.equals("A_FLAC")) {
                  var16 = 21;
                  break label305;
               }
               break;
            case 1951062397:
               if (var3.equals("A_OPUS")) {
                  var16 = 11;
                  break label305;
               }
            }

            var16 = -1;
         }

         Object var6;
         int var9;
         Object var18;
         short var19;
         short var26;
         label269: {
            label268: {
               label330: {
                  String var7;
                  List var15;
                  label331: {
                     label332: {
                        label254: {
                           label253: {
                              var6 = null;
                              int var8;
                              byte[] var14;
                              StringBuilder var17;
                              switch(var16) {
                              case 0:
                                 var3 = "video/x-vnd.on2.vp8";
                                 break label253;
                              case 1:
                                 var3 = "video/x-vnd.on2.vp9";
                                 break label253;
                              case 2:
                                 var3 = "video/mpeg2";
                                 break label253;
                              case 3:
                              case 4:
                              case 5:
                                 var14 = this.codecPrivate;
                                 if (var14 == null) {
                                    var15 = null;
                                 } else {
                                    var15 = Collections.singletonList(var14);
                                 }

                                 var7 = "video/mp4v-es";
                                 break label331;
                              case 6:
                                 AvcConfig var24 = AvcConfig.parse(new ParsableByteArray(this.codecPrivate));
                                 var18 = var24.initializationData;
                                 this.nalUnitLengthFieldLength = var24.nalUnitLengthFieldLength;
                                 var3 = "video/avc";
                                 break label254;
                              case 7:
                                 HevcConfig var23 = HevcConfig.parse(new ParsableByteArray(this.codecPrivate));
                                 var18 = var23.initializationData;
                                 this.nalUnitLengthFieldLength = var23.nalUnitLengthFieldLength;
                                 var3 = "video/hevc";
                                 break label254;
                              case 8:
                                 Pair var22 = parseFourCcPrivate(new ParsableByteArray(this.codecPrivate));
                                 var7 = (String)var22.first;
                                 var15 = (List)var22.second;
                                 break label331;
                              case 9:
                                 var3 = "video/x-unknown";
                                 break label253;
                              case 10:
                                 var18 = parseVorbisCodecPrivate(this.codecPrivate);
                                 var3 = "audio/vorbis";
                                 var26 = 8192;
                                 break label330;
                              case 11:
                                 var18 = new ArrayList(3);
                                 ((List)var18).add(this.codecPrivate);
                                 ((List)var18).add(ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(this.codecDelayNs).array());
                                 ((List)var18).add(ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(this.seekPreRollNs).array());
                                 var3 = "audio/opus";
                                 var26 = 5760;
                                 break label330;
                              case 12:
                                 var15 = Collections.singletonList(this.codecPrivate);
                                 var7 = "audio/mp4a-latm";
                                 break label331;
                              case 13:
                                 var3 = "audio/mpeg-L2";
                                 break label332;
                              case 14:
                                 var3 = "audio/mpeg";
                                 break label332;
                              case 15:
                                 var3 = "audio/ac3";
                                 break label253;
                              case 16:
                                 var3 = "audio/eac3";
                                 break label253;
                              case 17:
                                 this.trueHdSampleRechunker = new MatroskaExtractor.TrueHdSampleRechunker();
                                 var3 = "audio/true-hd";
                                 break label253;
                              case 18:
                              case 19:
                                 var3 = "audio/vnd.dts";
                                 break label253;
                              case 20:
                                 var3 = "audio/vnd.dts.hd";
                                 break label253;
                              case 21:
                                 var15 = Collections.singletonList(this.codecPrivate);
                                 var7 = "audio/flac";
                                 break label331;
                              case 22:
                                 if (parseMsAcmCodecPrivate(new ParsableByteArray(this.codecPrivate))) {
                                    var8 = Util.getPcmEncoding(this.audioBitDepth);
                                    var4 = var8;
                                    if (var8 != 0) {
                                       break label268;
                                    }

                                    var17 = new StringBuilder();
                                    var17.append("Unsupported PCM bit depth: ");
                                    var17.append(this.audioBitDepth);
                                    var17.append(". Setting mimeType to ");
                                    var17.append("audio/x-unknown");
                                    Log.w("MatroskaExtractor", var17.toString());
                                 } else {
                                    var17 = new StringBuilder();
                                    var17.append("Non-PCM MS/ACM is unsupported. Setting mimeType to ");
                                    var17.append("audio/x-unknown");
                                    Log.w("MatroskaExtractor", var17.toString());
                                 }
                                 break;
                              case 23:
                                 var8 = Util.getPcmEncoding(this.audioBitDepth);
                                 var4 = var8;
                                 if (var8 != 0) {
                                    break label268;
                                 }

                                 var17 = new StringBuilder();
                                 var17.append("Unsupported PCM bit depth: ");
                                 var17.append(this.audioBitDepth);
                                 var17.append(". Setting mimeType to ");
                                 var17.append("audio/x-unknown");
                                 Log.w("MatroskaExtractor", var17.toString());
                                 break;
                              case 24:
                                 var3 = "application/x-subrip";
                                 break label253;
                              case 25:
                                 var3 = "text/x-ssa";
                                 break label253;
                              case 26:
                                 var18 = Collections.singletonList(this.codecPrivate);
                                 var3 = "application/vobsub";
                                 break label254;
                              case 27:
                                 var3 = "application/pgs";
                                 break label253;
                              case 28:
                                 var14 = this.codecPrivate;
                                 var15 = Collections.singletonList(new byte[]{var14[0], var14[1], var14[2], var14[3]});
                                 var7 = "application/dvbsubs";
                                 break label331;
                              default:
                                 throw new ParserException("Unrecognized codec identifier.");
                              }

                              var3 = "audio/x-unknown";
                           }

                           var18 = null;
                        }

                        var26 = -1;
                        break label330;
                     }

                     var18 = null;
                     var26 = 4096;
                     break label330;
                  }

                  var19 = -1;
                  var9 = -1;
                  List var10 = var15;
                  var3 = var7;
                  var18 = var10;
                  break label269;
               }

               var9 = -1;
               var19 = var26;
               break label269;
            }

            var3 = "audio/raw";
            var18 = null;
            var19 = -1;
            var9 = var4;
         }

         byte var11 = this.flagDefault;
         byte var28;
         if (this.flagForced) {
            var28 = 2;
         } else {
            var28 = 0;
         }

         var4 = var11 | 0 | var28;
         byte var13;
         Format var27;
         if (MimeTypes.isAudio(var3)) {
            var27 = Format.createAudioSampleFormat(Integer.toString(var2), var3, (String)null, -1, var19, this.channelCount, this.sampleRate, var9, (List)var18, this.drmInitData, var4, this.language);
            var13 = 1;
         } else if (MimeTypes.isVideo(var3)) {
            if (this.displayUnit == 0) {
               var9 = this.displayWidth;
               var4 = var9;
               if (var9 == -1) {
                  var4 = this.width;
               }

               this.displayWidth = var4;
               var9 = this.displayHeight;
               var4 = var9;
               if (var9 == -1) {
                  var4 = this.height;
               }

               this.displayHeight = var4;
            }

            float var12;
            label199: {
               var9 = this.displayWidth;
               if (var9 != -1) {
                  var4 = this.displayHeight;
                  if (var4 != -1) {
                     var12 = (float)(this.height * var9) / (float)(this.width * var4);
                     break label199;
                  }
               }

               var12 = -1.0F;
            }

            ColorInfo var20 = (ColorInfo)var6;
            if (this.hasColorInfo) {
               byte[] var21 = this.getHdrStaticInfo();
               var20 = new ColorInfo(this.colorSpace, this.colorRange, this.colorTransfer, var21);
            }

            if ("htc_video_rotA-000".equals(this.name)) {
               var26 = 0;
            } else if ("htc_video_rotA-090".equals(this.name)) {
               var26 = 90;
            } else if ("htc_video_rotA-180".equals(this.name)) {
               var26 = 180;
            } else if ("htc_video_rotA-270".equals(this.name)) {
               var26 = 270;
            } else {
               var26 = -1;
            }

            if (this.projectionType == 0 && Float.compare(this.projectionPoseYaw, 0.0F) == 0 && Float.compare(this.projectionPosePitch, 0.0F) == 0) {
               if (Float.compare(this.projectionPoseRoll, 0.0F) == 0) {
                  var26 = 0;
               } else if (Float.compare(this.projectionPosePitch, 90.0F) == 0) {
                  var26 = 90;
               } else if (Float.compare(this.projectionPosePitch, -180.0F) != 0 && Float.compare(this.projectionPosePitch, 180.0F) != 0) {
                  if (Float.compare(this.projectionPosePitch, -90.0F) == 0) {
                     var26 = 270;
                  }
               } else {
                  var26 = 180;
               }
            }

            var27 = Format.createVideoSampleFormat(Integer.toString(var2), var3, (String)null, -1, var19, this.width, this.height, -1.0F, (List)var18, var26, var12, this.projectionData, this.stereoMode, var20, this.drmInitData);
            var13 = 2;
         } else if ("application/x-subrip".equals(var3)) {
            var27 = Format.createTextSampleFormat(Integer.toString(var2), var3, var4, this.language, this.drmInitData);
            var13 = var5;
         } else if ("text/x-ssa".equals(var3)) {
            ArrayList var25 = new ArrayList(2);
            var25.add(MatroskaExtractor.SSA_DIALOGUE_FORMAT);
            var25.add(this.codecPrivate);
            var27 = Format.createTextSampleFormat(Integer.toString(var2), var3, (String)null, -1, var4, this.language, -1, this.drmInitData, Long.MAX_VALUE, var25);
            var13 = var5;
         } else {
            if (!"application/vobsub".equals(var3) && !"application/pgs".equals(var3) && !"application/dvbsubs".equals(var3)) {
               throw new ParserException("Unexpected MIME type.");
            }

            var27 = Format.createImageSampleFormat(Integer.toString(var2), var3, (String)null, -1, var4, (List)var18, this.language, this.drmInitData);
            var13 = var5;
         }

         this.output = var1.track(this.number, var13);
         this.output.format(var27);
      }

      public void outputPendingSampleMetadata() {
         MatroskaExtractor.TrueHdSampleRechunker var1 = this.trueHdSampleRechunker;
         if (var1 != null) {
            var1.outputPendingSampleMetadata(this);
         }

      }

      public void reset() {
         MatroskaExtractor.TrueHdSampleRechunker var1 = this.trueHdSampleRechunker;
         if (var1 != null) {
            var1.reset();
         }

      }
   }

   private static final class TrueHdSampleRechunker {
      private int blockFlags;
      private int chunkSize;
      private boolean foundSyncframe;
      private int sampleCount;
      private final byte[] syncframePrefix = new byte[10];
      private long timeUs;

      public TrueHdSampleRechunker() {
      }

      public void outputPendingSampleMetadata(MatroskaExtractor.Track var1) {
         if (this.foundSyncframe && this.sampleCount > 0) {
            var1.output.sampleMetadata(this.timeUs, this.blockFlags, this.chunkSize, 0, var1.cryptoData);
            this.sampleCount = 0;
         }

      }

      public void reset() {
         this.foundSyncframe = false;
      }

      public void sampleMetadata(MatroskaExtractor.Track var1, long var2) {
         if (this.foundSyncframe) {
            int var4 = this.sampleCount++;
            if (var4 == 0) {
               this.timeUs = var2;
            }

            if (this.sampleCount >= 16) {
               var1.output.sampleMetadata(this.timeUs, this.blockFlags, this.chunkSize, 0, var1.cryptoData);
               this.sampleCount = 0;
            }
         }
      }

      public void startSample(ExtractorInput var1, int var2, int var3) throws IOException, InterruptedException {
         if (!this.foundSyncframe) {
            var1.peekFully(this.syncframePrefix, 0, 10);
            var1.resetPeekPosition();
            if (Ac3Util.parseTrueHdSyncframeAudioSampleCount(this.syncframePrefix) == 0) {
               return;
            }

            this.foundSyncframe = true;
            this.sampleCount = 0;
         }

         if (this.sampleCount == 0) {
            this.blockFlags = var2;
            this.chunkSize = 0;
         }

         this.chunkSize += var3;
      }
   }
}

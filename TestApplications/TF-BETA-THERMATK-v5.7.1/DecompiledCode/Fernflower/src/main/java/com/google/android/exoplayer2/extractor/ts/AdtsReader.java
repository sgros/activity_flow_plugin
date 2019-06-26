package com.google.android.exoplayer2.extractor.ts;

import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.DummyTrackOutput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Arrays;
import java.util.Collections;

public final class AdtsReader implements ElementaryStreamReader {
   private static final byte[] ID3_IDENTIFIER = new byte[]{73, 68, 51};
   private final ParsableBitArray adtsScratch;
   private int bytesRead;
   private int currentFrameVersion;
   private TrackOutput currentOutput;
   private long currentSampleDuration;
   private final boolean exposeId3;
   private int firstFrameSampleRateIndex;
   private int firstFrameVersion;
   private String formatId;
   private boolean foundFirstFrame;
   private boolean hasCrc;
   private boolean hasOutputFormat;
   private final ParsableByteArray id3HeaderBuffer;
   private TrackOutput id3Output;
   private final String language;
   private int matchState;
   private TrackOutput output;
   private long sampleDurationUs;
   private int sampleSize;
   private int state;
   private long timeUs;

   public AdtsReader(boolean var1) {
      this(var1, (String)null);
   }

   public AdtsReader(boolean var1, String var2) {
      this.adtsScratch = new ParsableBitArray(new byte[7]);
      this.id3HeaderBuffer = new ParsableByteArray(Arrays.copyOf(ID3_IDENTIFIER, 10));
      this.setFindingSampleState();
      this.firstFrameVersion = -1;
      this.firstFrameSampleRateIndex = -1;
      this.sampleDurationUs = -9223372036854775807L;
      this.exposeId3 = var1;
      this.language = var2;
   }

   private void checkAdtsHeader(ParsableByteArray var1) {
      if (var1.bytesLeft() != 0) {
         this.adtsScratch.data[0] = (byte)var1.data[var1.getPosition()];
         this.adtsScratch.setPosition(2);
         int var2 = this.adtsScratch.readBits(4);
         int var3 = this.firstFrameSampleRateIndex;
         if (var3 != -1 && var2 != var3) {
            this.resetSync();
         } else {
            if (!this.foundFirstFrame) {
               this.foundFirstFrame = true;
               this.firstFrameVersion = this.currentFrameVersion;
               this.firstFrameSampleRateIndex = var2;
            }

            this.setReadingAdtsHeaderState();
         }
      }
   }

   private boolean checkSyncPositionValid(ParsableByteArray var1, int var2) {
      var1.setPosition(var2 + 1);
      byte[] var3 = this.adtsScratch.data;
      boolean var4 = true;
      if (!this.tryRead(var1, var3, 1)) {
         return false;
      } else {
         this.adtsScratch.setPosition(4);
         int var5 = this.adtsScratch.readBits(1);
         int var6 = this.firstFrameVersion;
         if (var6 != -1 && var5 != var6) {
            return false;
         } else {
            if (this.firstFrameSampleRateIndex != -1) {
               if (!this.tryRead(var1, this.adtsScratch.data, 1)) {
                  return true;
               }

               this.adtsScratch.setPosition(2);
               if (this.adtsScratch.readBits(4) != this.firstFrameSampleRateIndex) {
                  return false;
               }

               var1.setPosition(var2 + 2);
            }

            if (!this.tryRead(var1, this.adtsScratch.data, 4)) {
               return true;
            } else {
               this.adtsScratch.setPosition(14);
               var6 = this.adtsScratch.readBits(13);
               if (var6 <= 6) {
                  return false;
               } else {
                  var2 += var6;
                  var6 = var2 + 1;
                  if (var6 >= var1.limit()) {
                     return true;
                  } else {
                     var3 = var1.data;
                     boolean var7;
                     if (this.isAdtsSyncBytes(var3[var2], var3[var6])) {
                        var7 = var4;
                        if (this.firstFrameVersion == -1) {
                           return var7;
                        }

                        if ((var1.data[var6] & 8) >> 3 == var5) {
                           var7 = var4;
                           return var7;
                        }
                     }

                     var7 = false;
                     return var7;
                  }
               }
            }
         }
      }
   }

   private boolean continueRead(ParsableByteArray var1, byte[] var2, int var3) {
      int var4 = Math.min(var1.bytesLeft(), var3 - this.bytesRead);
      var1.readBytes(var2, this.bytesRead, var4);
      this.bytesRead += var4;
      boolean var5;
      if (this.bytesRead == var3) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   private void findNextSample(ParsableByteArray var1) {
      byte[] var2 = var1.data;
      int var3 = var1.getPosition();
      int var4 = var1.limit();

      while(var3 < var4) {
         int var5 = var3 + 1;
         var3 = var2[var3] & 255;
         if (this.matchState == 512 && this.isAdtsSyncBytes((byte)-1, (byte)var3) && (this.foundFirstFrame || this.checkSyncPositionValid(var1, var5 - 2))) {
            this.currentFrameVersion = (var3 & 8) >> 3;
            boolean var6 = true;
            if ((var3 & 1) != 0) {
               var6 = false;
            }

            this.hasCrc = var6;
            if (!this.foundFirstFrame) {
               this.setCheckingAdtsHeaderState();
            } else {
               this.setReadingAdtsHeaderState();
            }

            var1.setPosition(var5);
            return;
         }

         int var7 = this.matchState;
         var3 |= var7;
         if (var3 != 329) {
            if (var3 != 511) {
               if (var3 != 836) {
                  if (var3 == 1075) {
                     this.setReadingId3HeaderState();
                     var1.setPosition(var5);
                     return;
                  }

                  var3 = var5;
                  if (var7 != 256) {
                     this.matchState = 256;
                     var3 = var5 - 1;
                  }
               } else {
                  this.matchState = 1024;
                  var3 = var5;
               }
            } else {
               this.matchState = 512;
               var3 = var5;
            }
         } else {
            this.matchState = 768;
            var3 = var5;
         }
      }

      var1.setPosition(var3);
   }

   private boolean isAdtsSyncBytes(byte var1, byte var2) {
      return isAdtsSyncWord((var1 & 255) << 8 | var2 & 255);
   }

   public static boolean isAdtsSyncWord(int var0) {
      boolean var1;
      if ((var0 & '\ufff6') == 65520) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void parseAdtsHeader() throws ParserException {
      this.adtsScratch.setPosition(0);
      int var1;
      int var2;
      if (!this.hasOutputFormat) {
         var1 = this.adtsScratch.readBits(2) + 1;
         var2 = var1;
         if (var1 != 2) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Detected audio object type: ");
            var3.append(var1);
            var3.append(", but assuming AAC LC.");
            Log.w("AdtsReader", var3.toString());
            var2 = 2;
         }

         this.adtsScratch.skipBits(5);
         var1 = this.adtsScratch.readBits(3);
         byte[] var5 = CodecSpecificDataUtil.buildAacAudioSpecificConfig(var2, this.firstFrameSampleRateIndex, var1);
         Pair var4 = CodecSpecificDataUtil.parseAacAudioSpecificConfig(var5);
         Format var6 = Format.createAudioSampleFormat(this.formatId, "audio/mp4a-latm", (String)null, -1, -1, (Integer)var4.second, (Integer)var4.first, Collections.singletonList(var5), (DrmInitData)null, 0, this.language);
         this.sampleDurationUs = 1024000000L / (long)var6.sampleRate;
         this.output.format(var6);
         this.hasOutputFormat = true;
      } else {
         this.adtsScratch.skipBits(10);
      }

      this.adtsScratch.skipBits(4);
      var1 = this.adtsScratch.readBits(13) - 2 - 5;
      var2 = var1;
      if (this.hasCrc) {
         var2 = var1 - 2;
      }

      this.setReadingSampleState(this.output, this.sampleDurationUs, 0, var2);
   }

   private void parseId3Header() {
      this.id3Output.sampleData(this.id3HeaderBuffer, 10);
      this.id3HeaderBuffer.setPosition(6);
      this.setReadingSampleState(this.id3Output, 0L, 10, this.id3HeaderBuffer.readSynchSafeInt() + 10);
   }

   private void readSample(ParsableByteArray var1) {
      int var2 = Math.min(var1.bytesLeft(), this.sampleSize - this.bytesRead);
      this.currentOutput.sampleData(var1, var2);
      this.bytesRead += var2;
      var2 = this.bytesRead;
      int var3 = this.sampleSize;
      if (var2 == var3) {
         this.currentOutput.sampleMetadata(this.timeUs, 1, var3, 0, (TrackOutput.CryptoData)null);
         this.timeUs += this.currentSampleDuration;
         this.setFindingSampleState();
      }

   }

   private void resetSync() {
      this.foundFirstFrame = false;
      this.setFindingSampleState();
   }

   private void setCheckingAdtsHeaderState() {
      this.state = 1;
      this.bytesRead = 0;
   }

   private void setFindingSampleState() {
      this.state = 0;
      this.bytesRead = 0;
      this.matchState = 256;
   }

   private void setReadingAdtsHeaderState() {
      this.state = 3;
      this.bytesRead = 0;
   }

   private void setReadingId3HeaderState() {
      this.state = 2;
      this.bytesRead = ID3_IDENTIFIER.length;
      this.sampleSize = 0;
      this.id3HeaderBuffer.setPosition(0);
   }

   private void setReadingSampleState(TrackOutput var1, long var2, int var4, int var5) {
      this.state = 4;
      this.bytesRead = var4;
      this.currentOutput = var1;
      this.currentSampleDuration = var2;
      this.sampleSize = var5;
   }

   private boolean tryRead(ParsableByteArray var1, byte[] var2, int var3) {
      if (var1.bytesLeft() < var3) {
         return false;
      } else {
         var1.readBytes(var2, 0, var3);
         return true;
      }
   }

   public void consume(ParsableByteArray var1) throws ParserException {
      while(var1.bytesLeft() > 0) {
         int var2 = this.state;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     if (var2 != 4) {
                        throw new IllegalStateException();
                     }

                     this.readSample(var1);
                  } else {
                     byte var3;
                     if (this.hasCrc) {
                        var3 = 7;
                     } else {
                        var3 = 5;
                     }

                     if (this.continueRead(var1, this.adtsScratch.data, var3)) {
                        this.parseAdtsHeader();
                     }
                  }
               } else if (this.continueRead(var1, this.id3HeaderBuffer.data, 10)) {
                  this.parseId3Header();
               }
            } else {
               this.checkAdtsHeader(var1);
            }
         } else {
            this.findNextSample(var1);
         }
      }

   }

   public void createTracks(ExtractorOutput var1, TsPayloadReader.TrackIdGenerator var2) {
      var2.generateNewId();
      this.formatId = var2.getFormatId();
      this.output = var1.track(var2.getTrackId(), 1);
      if (this.exposeId3) {
         var2.generateNewId();
         this.id3Output = var1.track(var2.getTrackId(), 4);
         this.id3Output.format(Format.createSampleFormat(var2.getFormatId(), "application/id3", (String)null, -1, (DrmInitData)null));
      } else {
         this.id3Output = new DummyTrackOutput();
      }

   }

   public long getSampleDurationUs() {
      return this.sampleDurationUs;
   }

   public void packetFinished() {
   }

   public void packetStarted(long var1, int var3) {
      this.timeUs = var1;
   }

   public void seek() {
      this.resetSync();
   }
}

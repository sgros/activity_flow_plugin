package com.google.android.exoplayer2.extractor.ts;

import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Collections;

public final class LatmReader implements ElementaryStreamReader {
   private int audioMuxVersionA;
   private int bytesRead;
   private int channelCount;
   private Format format;
   private String formatId;
   private int frameLengthType;
   private final String language;
   private int numSubframes;
   private long otherDataLenBits;
   private boolean otherDataPresent;
   private TrackOutput output;
   private final ParsableBitArray sampleBitArray;
   private final ParsableByteArray sampleDataBuffer;
   private long sampleDurationUs;
   private int sampleRateHz;
   private int sampleSize;
   private int secondHeaderByte;
   private int state;
   private boolean streamMuxRead;
   private long timeUs;

   public LatmReader(String var1) {
      this.language = var1;
      this.sampleDataBuffer = new ParsableByteArray(1024);
      this.sampleBitArray = new ParsableBitArray(this.sampleDataBuffer.data);
   }

   private static long latmGetValue(ParsableBitArray var0) {
      return (long)var0.readBits((var0.readBits(2) + 1) * 8);
   }

   private void parseAudioMuxElement(ParsableBitArray var1) throws ParserException {
      if (!var1.readBit()) {
         this.streamMuxRead = true;
         this.parseStreamMuxConfig(var1);
      } else if (!this.streamMuxRead) {
         return;
      }

      if (this.audioMuxVersionA == 0) {
         if (this.numSubframes == 0) {
            this.parsePayloadMux(var1, this.parsePayloadLengthInfo(var1));
            if (this.otherDataPresent) {
               var1.skipBits((int)this.otherDataLenBits);
            }

         } else {
            throw new ParserException();
         }
      } else {
         throw new ParserException();
      }
   }

   private int parseAudioSpecificConfig(ParsableBitArray var1) throws ParserException {
      int var2 = var1.bitsLeft();
      Pair var3 = CodecSpecificDataUtil.parseAacAudioSpecificConfig(var1, true);
      this.sampleRateHz = (Integer)var3.first;
      this.channelCount = (Integer)var3.second;
      return var2 - var1.bitsLeft();
   }

   private void parseFrameLength(ParsableBitArray var1) {
      this.frameLengthType = var1.readBits(3);
      int var2 = this.frameLengthType;
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 != 3 && var2 != 4 && var2 != 5) {
               if (var2 != 6 && var2 != 7) {
                  throw new IllegalStateException();
               }

               var1.skipBits(1);
            } else {
               var1.skipBits(6);
            }
         } else {
            var1.skipBits(9);
         }
      } else {
         var1.skipBits(8);
      }

   }

   private int parsePayloadLengthInfo(ParsableBitArray var1) throws ParserException {
      if (this.frameLengthType != 0) {
         throw new ParserException();
      } else {
         int var2 = 0;

         int var3;
         int var4;
         do {
            var3 = var1.readBits(8);
            var4 = var2 + var3;
            var2 = var4;
         } while(var3 == 255);

         return var4;
      }
   }

   private void parsePayloadMux(ParsableBitArray var1, int var2) {
      int var3 = var1.getPosition();
      if ((var3 & 7) == 0) {
         this.sampleDataBuffer.setPosition(var3 >> 3);
      } else {
         var1.readBits(this.sampleDataBuffer.data, 0, var2 * 8);
         this.sampleDataBuffer.setPosition(0);
      }

      this.output.sampleData(this.sampleDataBuffer, var2);
      this.output.sampleMetadata(this.timeUs, 1, var2, 0, (TrackOutput.CryptoData)null);
      this.timeUs += this.sampleDurationUs;
   }

   private void parseStreamMuxConfig(ParsableBitArray var1) throws ParserException {
      int var2 = var1.readBits(1);
      int var3;
      if (var2 == 1) {
         var3 = var1.readBits(1);
      } else {
         var3 = 0;
      }

      this.audioMuxVersionA = var3;
      if (this.audioMuxVersionA != 0) {
         throw new ParserException();
      } else {
         if (var2 == 1) {
            latmGetValue(var1);
         }

         if (!var1.readBit()) {
            throw new ParserException();
         } else {
            this.numSubframes = var1.readBits(6);
            int var4 = var1.readBits(4);
            var3 = var1.readBits(3);
            if (var4 == 0 && var3 == 0) {
               if (var2 == 0) {
                  var4 = var1.getPosition();
                  var3 = this.parseAudioSpecificConfig(var1);
                  var1.setPosition(var4);
                  byte[] var5 = new byte[(var3 + 7) / 8];
                  var1.readBits(var5, 0, var3);
                  Format var7 = Format.createAudioSampleFormat(this.formatId, "audio/mp4a-latm", (String)null, -1, -1, this.channelCount, this.sampleRateHz, Collections.singletonList(var5), (DrmInitData)null, 0, this.language);
                  if (!var7.equals(this.format)) {
                     this.format = var7;
                     this.sampleDurationUs = 1024000000L / (long)var7.sampleRate;
                     this.output.format(var7);
                  }
               } else {
                  var1.skipBits((int)latmGetValue(var1) - this.parseAudioSpecificConfig(var1));
               }

               this.parseFrameLength(var1);
               this.otherDataPresent = var1.readBit();
               this.otherDataLenBits = 0L;
               if (this.otherDataPresent) {
                  boolean var6;
                  if (var2 == 1) {
                     this.otherDataLenBits = latmGetValue(var1);
                  } else {
                     do {
                        var6 = var1.readBit();
                        this.otherDataLenBits = (this.otherDataLenBits << 8) + (long)var1.readBits(8);
                     } while(var6);
                  }
               }

               if (var1.readBit()) {
                  var1.skipBits(8);
               }

            } else {
               throw new ParserException();
            }
         }
      }
   }

   private void resetBufferForSize(int var1) {
      this.sampleDataBuffer.reset(var1);
      this.sampleBitArray.reset(this.sampleDataBuffer.data);
   }

   public void consume(ParsableByteArray var1) throws ParserException {
      while(var1.bytesLeft() > 0) {
         int var2 = this.state;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     throw new IllegalStateException();
                  }

                  var2 = Math.min(var1.bytesLeft(), this.sampleSize - this.bytesRead);
                  var1.readBytes(this.sampleBitArray.data, this.bytesRead, var2);
                  this.bytesRead += var2;
                  if (this.bytesRead == this.sampleSize) {
                     this.sampleBitArray.setPosition(0);
                     this.parseAudioMuxElement(this.sampleBitArray);
                     this.state = 0;
                  }
               } else {
                  this.sampleSize = (this.secondHeaderByte & -225) << 8 | var1.readUnsignedByte();
                  var2 = this.sampleSize;
                  if (var2 > this.sampleDataBuffer.data.length) {
                     this.resetBufferForSize(var2);
                  }

                  this.bytesRead = 0;
                  this.state = 3;
               }
            } else {
               var2 = var1.readUnsignedByte();
               if ((var2 & 224) == 224) {
                  this.secondHeaderByte = var2;
                  this.state = 2;
               } else if (var2 != 86) {
                  this.state = 0;
               }
            }
         } else if (var1.readUnsignedByte() == 86) {
            this.state = 1;
         }
      }

   }

   public void createTracks(ExtractorOutput var1, TsPayloadReader.TrackIdGenerator var2) {
      var2.generateNewId();
      this.output = var1.track(var2.getTrackId(), 1);
      this.formatId = var2.getFormatId();
   }

   public void packetFinished() {
   }

   public void packetStarted(long var1, int var3) {
      this.timeUs = var1;
   }

   public void seek() {
      this.state = 0;
      this.streamMuxRead = false;
   }
}

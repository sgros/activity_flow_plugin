package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;

public final class PesReader implements TsPayloadReader {
   private int bytesRead;
   private boolean dataAlignmentIndicator;
   private boolean dtsFlag;
   private int extendedHeaderLength;
   private int payloadSize;
   private final ParsableBitArray pesScratch;
   private boolean ptsFlag;
   private final ElementaryStreamReader reader;
   private boolean seenFirstDts;
   private int state;
   private long timeUs;
   private TimestampAdjuster timestampAdjuster;

   public PesReader(ElementaryStreamReader var1) {
      this.reader = var1;
      this.pesScratch = new ParsableBitArray(new byte[10]);
      this.state = 0;
   }

   private boolean continueRead(ParsableByteArray var1, byte[] var2, int var3) {
      int var4 = Math.min(var1.bytesLeft(), var3 - this.bytesRead);
      boolean var5 = true;
      if (var4 <= 0) {
         return true;
      } else {
         if (var2 == null) {
            var1.skipBytes(var4);
         } else {
            var1.readBytes(var2, this.bytesRead, var4);
         }

         this.bytesRead += var4;
         if (this.bytesRead != var3) {
            var5 = false;
         }

         return var5;
      }
   }

   private boolean parseHeader() {
      this.pesScratch.setPosition(0);
      int var1 = this.pesScratch.readBits(24);
      if (var1 != 1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unexpected start code prefix: ");
         var2.append(var1);
         Log.w("PesReader", var2.toString());
         this.payloadSize = -1;
         return false;
      } else {
         this.pesScratch.skipBits(8);
         var1 = this.pesScratch.readBits(16);
         this.pesScratch.skipBits(5);
         this.dataAlignmentIndicator = this.pesScratch.readBit();
         this.pesScratch.skipBits(2);
         this.ptsFlag = this.pesScratch.readBit();
         this.dtsFlag = this.pesScratch.readBit();
         this.pesScratch.skipBits(6);
         this.extendedHeaderLength = this.pesScratch.readBits(8);
         if (var1 == 0) {
            this.payloadSize = -1;
         } else {
            this.payloadSize = var1 + 6 - 9 - this.extendedHeaderLength;
         }

         return true;
      }
   }

   private void parseHeaderExtension() {
      this.pesScratch.setPosition(0);
      this.timeUs = -9223372036854775807L;
      if (this.ptsFlag) {
         this.pesScratch.skipBits(4);
         long var1 = (long)this.pesScratch.readBits(3);
         this.pesScratch.skipBits(1);
         long var3 = (long)(this.pesScratch.readBits(15) << 15);
         this.pesScratch.skipBits(1);
         long var5 = (long)this.pesScratch.readBits(15);
         this.pesScratch.skipBits(1);
         if (!this.seenFirstDts && this.dtsFlag) {
            this.pesScratch.skipBits(4);
            long var7 = (long)this.pesScratch.readBits(3);
            this.pesScratch.skipBits(1);
            long var9 = (long)(this.pesScratch.readBits(15) << 15);
            this.pesScratch.skipBits(1);
            long var11 = (long)this.pesScratch.readBits(15);
            this.pesScratch.skipBits(1);
            this.timestampAdjuster.adjustTsTimestamp(var7 << 30 | var9 | var11);
            this.seenFirstDts = true;
         }

         this.timeUs = this.timestampAdjuster.adjustTsTimestamp(var1 << 30 | var3 | var5);
      }

   }

   private void setState(int var1) {
      this.state = var1;
      this.bytesRead = 0;
   }

   public final void consume(ParsableByteArray var1, int var2) throws ParserException {
      int var3 = var2;
      if ((var2 & 1) != 0) {
         var3 = this.state;
         if (var3 != 0 && var3 != 1) {
            if (var3 != 2) {
               if (var3 != 3) {
                  throw new IllegalStateException();
               }

               if (this.payloadSize != -1) {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("Unexpected start indicator: expected ");
                  var4.append(this.payloadSize);
                  var4.append(" more bytes");
                  Log.w("PesReader", var4.toString());
               }

               this.reader.packetFinished();
            } else {
               Log.w("PesReader", "Unexpected start indicator reading extended header");
            }
         }

         this.setState(1);
         var3 = var2;
      }

      while(var1.bytesLeft() > 0) {
         int var5 = this.state;
         if (var5 != 0) {
            byte var8 = 0;
            byte var6 = 0;
            int var7 = 0;
            if (var5 != 1) {
               if (var5 != 2) {
                  if (var5 != 3) {
                     throw new IllegalStateException();
                  }

                  int var9 = var1.bytesLeft();
                  var2 = this.payloadSize;
                  if (var2 != -1) {
                     var7 = var9 - var2;
                  }

                  var2 = var9;
                  if (var7 > 0) {
                     var2 = var9 - var7;
                     var1.setLimit(var1.getPosition() + var2);
                  }

                  this.reader.consume(var1);
                  var7 = this.payloadSize;
                  if (var7 != -1) {
                     this.payloadSize = var7 - var2;
                     if (this.payloadSize == 0) {
                        this.reader.packetFinished();
                        this.setState(1);
                     }
                  }
               } else {
                  var7 = Math.min(10, this.extendedHeaderLength);
                  if (this.continueRead(var1, this.pesScratch.data, var7) && this.continueRead(var1, (byte[])null, this.extendedHeaderLength)) {
                     this.parseHeaderExtension();
                     if (this.dataAlignmentIndicator) {
                        var8 = 4;
                     }

                     var3 |= var8;
                     this.reader.packetStarted(this.timeUs, var3);
                     this.setState(3);
                  }
               }
            } else if (this.continueRead(var1, this.pesScratch.data, 9)) {
               var8 = var6;
               if (this.parseHeader()) {
                  var8 = 2;
               }

               this.setState(var8);
            }
         } else {
            var1.skipBytes(var1.bytesLeft());
         }
      }

   }

   public void init(TimestampAdjuster var1, ExtractorOutput var2, TsPayloadReader.TrackIdGenerator var3) {
      this.timestampAdjuster = var1;
      this.reader.createTracks(var2, var3);
   }

   public final void seek() {
      this.state = 0;
      this.bytesRead = 0;
      this.seenFirstDts = false;
      this.reader.seek();
   }
}

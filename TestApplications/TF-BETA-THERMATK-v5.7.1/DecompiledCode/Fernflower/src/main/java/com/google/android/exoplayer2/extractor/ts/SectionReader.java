package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;

public final class SectionReader implements TsPayloadReader {
   private int bytesRead;
   private final SectionPayloadReader reader;
   private final ParsableByteArray sectionData;
   private boolean sectionSyntaxIndicator;
   private int totalSectionLength;
   private boolean waitingForPayloadStart;

   public SectionReader(SectionPayloadReader var1) {
      this.reader = var1;
      this.sectionData = new ParsableByteArray(32);
   }

   public void consume(ParsableByteArray var1, int var2) {
      boolean var7;
      if ((var2 & 1) != 0) {
         var7 = true;
      } else {
         var7 = false;
      }

      int var3;
      if (var7) {
         var3 = var1.readUnsignedByte() + var1.getPosition();
      } else {
         var3 = -1;
      }

      if (this.waitingForPayloadStart) {
         if (!var7) {
            return;
         }

         this.waitingForPayloadStart = false;
         var1.setPosition(var3);
         this.bytesRead = 0;
      }

      while(var1.bytesLeft() > 0) {
         var2 = this.bytesRead;
         if (var2 < 3) {
            if (var2 == 0) {
               var2 = var1.readUnsignedByte();
               var1.setPosition(var1.getPosition() - 1);
               if (var2 == 255) {
                  this.waitingForPayloadStart = true;
                  return;
               }
            }

            var2 = Math.min(var1.bytesLeft(), 3 - this.bytesRead);
            var1.readBytes(this.sectionData.data, this.bytesRead, var2);
            this.bytesRead += var2;
            if (this.bytesRead == 3) {
               this.sectionData.reset(3);
               this.sectionData.skipBytes(1);
               var2 = this.sectionData.readUnsignedByte();
               var3 = this.sectionData.readUnsignedByte();
               boolean var4;
               if ((var2 & 128) != 0) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               this.sectionSyntaxIndicator = var4;
               this.totalSectionLength = ((var2 & 15) << 8 | var3) + 3;
               var3 = this.sectionData.capacity();
               var2 = this.totalSectionLength;
               if (var3 < var2) {
                  ParsableByteArray var5 = this.sectionData;
                  byte[] var6 = var5.data;
                  var5.reset(Math.min(4098, Math.max(var2, var6.length * 2)));
                  System.arraycopy(var6, 0, this.sectionData.data, 0, 3);
               }
            }
         } else {
            var2 = Math.min(var1.bytesLeft(), this.totalSectionLength - this.bytesRead);
            var1.readBytes(this.sectionData.data, this.bytesRead, var2);
            this.bytesRead += var2;
            var2 = this.bytesRead;
            var3 = this.totalSectionLength;
            if (var2 == var3) {
               if (this.sectionSyntaxIndicator) {
                  if (Util.crc(this.sectionData.data, 0, var3, -1) != 0) {
                     this.waitingForPayloadStart = true;
                     return;
                  }

                  this.sectionData.reset(this.totalSectionLength - 4);
               } else {
                  this.sectionData.reset(var3);
               }

               this.reader.consume(this.sectionData);
               this.bytesRead = 0;
            }
         }
      }

   }

   public void init(TimestampAdjuster var1, ExtractorOutput var2, TsPayloadReader.TrackIdGenerator var3) {
      this.reader.init(var1, var2, var3);
      this.waitingForPayloadStart = true;
   }

   public void seek() {
      this.waitingForPayloadStart = true;
   }
}

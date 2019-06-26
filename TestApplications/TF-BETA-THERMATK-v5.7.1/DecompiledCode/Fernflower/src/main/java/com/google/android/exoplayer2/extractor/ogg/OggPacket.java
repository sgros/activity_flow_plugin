package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
import java.util.Arrays;

final class OggPacket {
   private int currentSegmentIndex = -1;
   private final ParsableByteArray packetArray = new ParsableByteArray(new byte['︁'], 0);
   private final OggPageHeader pageHeader = new OggPageHeader();
   private boolean populated;
   private int segmentCount;

   private int calculatePacketSize(int var1) {
      int var2 = 0;
      this.segmentCount = 0;

      int var3;
      int var5;
      do {
         var3 = this.segmentCount;
         OggPageHeader var4 = this.pageHeader;
         var5 = var2;
         if (var1 + var3 >= var4.pageSegmentCount) {
            break;
         }

         int[] var6 = var4.laces;
         this.segmentCount = var3 + 1;
         var3 = var6[var3 + var1];
         var5 = var2 + var3;
         var2 = var5;
      } while(var3 == 255);

      return var5;
   }

   public OggPageHeader getPageHeader() {
      return this.pageHeader;
   }

   public ParsableByteArray getPayload() {
      return this.packetArray;
   }

   public boolean populate(ExtractorInput var1) throws IOException, InterruptedException {
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      if (this.populated) {
         this.populated = false;
         this.packetArray.reset();
      }

      int var5;
      for(; !this.populated; this.currentSegmentIndex = var5) {
         int var4;
         if (this.currentSegmentIndex < 0) {
            if (!this.pageHeader.populate(var1, true)) {
               return false;
            }

            OggPageHeader var3 = this.pageHeader;
            var4 = var3.headerSize;
            if ((var3.type & 1) == 1 && this.packetArray.limit() == 0) {
               var4 += this.calculatePacketSize(0);
               var5 = this.segmentCount + 0;
            } else {
               var5 = 0;
            }

            var1.skipFully(var4);
            this.currentSegmentIndex = var5;
         }

         var5 = this.calculatePacketSize(this.currentSegmentIndex);
         var4 = this.currentSegmentIndex + this.segmentCount;
         if (var5 > 0) {
            ParsableByteArray var6;
            if (this.packetArray.capacity() < this.packetArray.limit() + var5) {
               var6 = this.packetArray;
               var6.data = Arrays.copyOf(var6.data, var6.limit() + var5);
            }

            var6 = this.packetArray;
            var1.readFully(var6.data, var6.limit(), var5);
            var6 = this.packetArray;
            var6.setLimit(var6.limit() + var5);
            if (this.pageHeader.laces[var4 - 1] != 255) {
               var2 = true;
            } else {
               var2 = false;
            }

            this.populated = var2;
         }

         var5 = var4;
         if (var4 == this.pageHeader.pageSegmentCount) {
            var5 = -1;
         }
      }

      return true;
   }

   public void reset() {
      this.pageHeader.reset();
      this.packetArray.reset();
      this.currentSegmentIndex = -1;
      this.populated = false;
   }

   public void trimPayload() {
      ParsableByteArray var1 = this.packetArray;
      byte[] var2 = var1.data;
      if (var2.length != 65025) {
         var1.data = Arrays.copyOf(var2, Math.max(65025, var1.limit()));
      }
   }
}

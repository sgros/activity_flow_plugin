package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

final class Sniffer {
   private int peekLength;
   private final ParsableByteArray scratch = new ParsableByteArray(8);

   public Sniffer() {
   }

   private long readUint(ExtractorInput var1) throws IOException, InterruptedException {
      byte[] var2 = this.scratch.data;
      int var3 = 0;
      var1.peekFully(var2, 0, 1);
      int var4 = this.scratch.data[0] & 255;
      if (var4 == 0) {
         return Long.MIN_VALUE;
      } else {
         int var5 = 128;

         int var6;
         for(var6 = 0; (var4 & var5) == 0; ++var6) {
            var5 >>= 1;
         }

         var5 = var4 & ~var5;
         var1.peekFully(this.scratch.data, 1, var6);

         while(var3 < var6) {
            byte[] var7 = this.scratch.data;
            ++var3;
            var5 = (var7[var3] & 255) + (var5 << 8);
         }

         this.peekLength += var6 + 1;
         return (long)var5;
      }
   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      long var2 = var1.getLength();
      long var4 = 1024L;
      long var6 = var4;
      if (var2 != -1L) {
         if (var2 > 1024L) {
            var6 = var4;
         } else {
            var6 = var2;
         }
      }

      int var8 = (int)var6;
      var1.peekFully(this.scratch.data, 0, 4);
      var6 = this.scratch.readUnsignedInt();
      this.peekLength = 4;

      while(true) {
         boolean var9 = true;
         if (var6 == 440786851L) {
            var4 = this.readUint(var1);
            var6 = (long)this.peekLength;
            if (var4 == Long.MIN_VALUE || var2 != -1L && var6 + var4 >= var2) {
               return false;
            } else {
               while(true) {
                  var8 = this.peekLength;
                  var2 = (long)var8;
                  long var11 = var6 + var4;
                  if (var2 >= var11) {
                     if ((long)var8 != var11) {
                        var9 = false;
                     }

                     return var9;
                  }

                  if (this.readUint(var1) == Long.MIN_VALUE) {
                     return false;
                  }

                  var2 = this.readUint(var1);
                  if (var2 < 0L || var2 > 2147483647L) {
                     return false;
                  }

                  if (var2 != 0L) {
                     var8 = (int)var2;
                     var1.advancePeekPosition(var8);
                     this.peekLength += var8;
                  }
               }
            }
         }

         int var10 = this.peekLength + 1;
         this.peekLength = var10;
         if (var10 == var8) {
            return false;
         }

         var1.peekFully(this.scratch.data, 0, 1);
         var6 = var6 << 8 & -256L | (long)(this.scratch.data[0] & 255);
      }
   }
}

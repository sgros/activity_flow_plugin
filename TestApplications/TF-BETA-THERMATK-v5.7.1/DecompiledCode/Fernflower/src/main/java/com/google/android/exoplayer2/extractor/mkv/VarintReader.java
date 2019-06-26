package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import java.io.IOException;

final class VarintReader {
   private static final long[] VARINT_LENGTH_MASKS = new long[]{128L, 64L, 32L, 16L, 8L, 4L, 2L, 1L};
   private int length;
   private final byte[] scratch = new byte[8];
   private int state;

   public VarintReader() {
   }

   public static long assembleVarint(byte[] var0, int var1, boolean var2) {
      long var3 = (long)var0[0] & 255L;
      long var5 = var3;
      if (var2) {
         var5 = var3 & ~VARINT_LENGTH_MASKS[var1 - 1];
      }

      for(int var7 = 1; var7 < var1; ++var7) {
         var5 = var5 << 8 | (long)var0[var7] & 255L;
      }

      return var5;
   }

   public static int parseUnsignedVarintLength(int var0) {
      int var1 = 0;

      while(true) {
         long[] var2 = VARINT_LENGTH_MASKS;
         if (var1 >= var2.length) {
            var0 = -1;
            break;
         }

         if ((var2[var1] & (long)var0) != 0L) {
            var0 = var1 + 1;
            break;
         }

         ++var1;
      }

      return var0;
   }

   public int getLastLength() {
      return this.length;
   }

   public long readUnsignedVarint(ExtractorInput var1, boolean var2, boolean var3, int var4) throws IOException, InterruptedException {
      if (this.state == 0) {
         if (!var1.readFully(this.scratch, 0, 1, var2)) {
            return -1L;
         }

         this.length = parseUnsignedVarintLength(this.scratch[0] & 255);
         if (this.length == -1) {
            throw new IllegalStateException("No valid varint length mask found");
         }

         this.state = 1;
      }

      int var5 = this.length;
      if (var5 > var4) {
         this.state = 0;
         return -2L;
      } else {
         if (var5 != 1) {
            var1.readFully(this.scratch, 1, var5 - 1);
         }

         this.state = 0;
         return assembleVarint(this.scratch, this.length, var3);
      }
   }

   public void reset() {
      this.state = 0;
      this.length = 0;
   }
}

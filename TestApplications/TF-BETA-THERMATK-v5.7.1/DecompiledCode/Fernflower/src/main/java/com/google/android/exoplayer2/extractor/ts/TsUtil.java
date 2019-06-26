package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.util.ParsableByteArray;

public final class TsUtil {
   public static int findSyncBytePosition(byte[] var0, int var1, int var2) {
      while(var1 < var2 && var0[var1] != 71) {
         ++var1;
      }

      return var1;
   }

   public static long readPcrFromPacket(ParsableByteArray var0, int var1, int var2) {
      var0.setPosition(var1);
      if (var0.bytesLeft() < 5) {
         return -9223372036854775807L;
      } else {
         var1 = var0.readInt();
         if ((8388608 & var1) != 0) {
            return -9223372036854775807L;
         } else if ((2096896 & var1) >> 8 != var2) {
            return -9223372036854775807L;
         } else {
            boolean var5 = true;
            boolean var4;
            if ((var1 & 32) != 0) {
               var4 = true;
            } else {
               var4 = false;
            }

            if (!var4) {
               return -9223372036854775807L;
            } else {
               if (var0.readUnsignedByte() >= 7 && var0.bytesLeft() >= 7) {
                  if ((var0.readUnsignedByte() & 16) == 16) {
                     var4 = var5;
                  } else {
                     var4 = false;
                  }

                  if (var4) {
                     byte[] var3 = new byte[6];
                     var0.readBytes(var3, 0, var3.length);
                     return readPcrValueFromPcrBytes(var3);
                  }
               }

               return -9223372036854775807L;
            }
         }
      }
   }

   private static long readPcrValueFromPcrBytes(byte[] var0) {
      return ((long)var0[0] & 255L) << 25 | ((long)var0[1] & 255L) << 17 | ((long)var0[2] & 255L) << 9 | ((long)var0[3] & 255L) << 1 | (255L & (long)var0[4]) >> 7;
   }
}

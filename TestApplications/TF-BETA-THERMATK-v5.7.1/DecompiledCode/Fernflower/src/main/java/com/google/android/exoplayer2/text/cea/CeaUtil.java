package com.google.android.exoplayer2.text.cea;

import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;

public final class CeaUtil {
   public static final int USER_DATA_IDENTIFIER_GA94 = Util.getIntegerCodeForString("GA94");

   public static void consume(long var0, ParsableByteArray var2, TrackOutput[] var3) {
      while(true) {
         int var4 = var2.bytesLeft();
         boolean var5 = true;
         if (var4 <= 1) {
            return;
         }

         int var6 = readNon255TerminatedValue(var2);
         int var7 = readNon255TerminatedValue(var2);
         int var8 = var2.getPosition() + var7;
         if (var7 != -1 && var7 <= var2.bytesLeft()) {
            var4 = var8;
            if (var6 == 4) {
               var4 = var8;
               if (var7 >= 8) {
                  var4 = var2.readUnsignedByte();
                  int var9 = var2.readUnsignedShort();
                  if (var9 == 49) {
                     var7 = var2.readInt();
                  } else {
                     var7 = 0;
                  }

                  var6 = var2.readUnsignedByte();
                  if (var9 == 47) {
                     var2.skipBytes(1);
                  }

                  boolean var10;
                  if (var4 == 181 && (var9 == 49 || var9 == 47) && var6 == 3) {
                     var10 = true;
                  } else {
                     var10 = false;
                  }

                  boolean var11 = var10;
                  if (var9 == 49) {
                     boolean var12;
                     if (var7 == USER_DATA_IDENTIFIER_GA94) {
                        var12 = var5;
                     } else {
                        var12 = false;
                     }

                     var11 = var10 & var12;
                  }

                  var4 = var8;
                  if (var11) {
                     consumeCcData(var0, var2, var3);
                     var4 = var8;
                  }
               }
            }
         } else {
            Log.w("CeaUtil", "Skipping remainder of malformed SEI NAL unit.");
            var4 = var2.limit();
         }

         var2.setPosition(var4);
      }
   }

   public static void consumeCcData(long var0, ParsableByteArray var2, TrackOutput[] var3) {
      int var4 = var2.readUnsignedByte();
      byte var5 = 0;
      boolean var6;
      if ((var4 & 64) != 0) {
         var6 = true;
      } else {
         var6 = false;
      }

      if (var6) {
         var2.skipBytes(1);
         int var7 = (var4 & 31) * 3;
         var4 = var2.getPosition();
         int var8 = var3.length;

         for(int var10 = var5; var10 < var8; ++var10) {
            TrackOutput var9 = var3[var10];
            var2.setPosition(var4);
            var9.sampleData(var2, var7);
            var9.sampleMetadata(var0, 1, var7, 0, (TrackOutput.CryptoData)null);
         }

      }
   }

   private static int readNon255TerminatedValue(ParsableByteArray var0) {
      int var1 = 0;

      while(var0.bytesLeft() != 0) {
         int var2 = var0.readUnsignedByte();
         int var3 = var1 + var2;
         var1 = var3;
         if (var2 != 255) {
            return var3;
         }
      }

      return -1;
   }
}

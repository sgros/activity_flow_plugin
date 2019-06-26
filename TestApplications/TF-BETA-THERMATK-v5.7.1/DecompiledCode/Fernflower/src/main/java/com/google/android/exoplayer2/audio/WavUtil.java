package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.Util;

public final class WavUtil {
   public static final int DATA_FOURCC = Util.getIntegerCodeForString("data");
   public static final int FMT_FOURCC = Util.getIntegerCodeForString("fmt ");
   public static final int RIFF_FOURCC = Util.getIntegerCodeForString("RIFF");
   public static final int WAVE_FOURCC = Util.getIntegerCodeForString("WAVE");

   public static int getEncodingForType(int var0, int var1) {
      if (var0 != 1) {
         byte var2 = 0;
         if (var0 == 3) {
            byte var3 = var2;
            if (var1 == 32) {
               var3 = 4;
            }

            return var3;
         }

         if (var0 != 65534) {
            if (var0 != 6) {
               if (var0 != 7) {
                  return 0;
               }

               return 268435456;
            }

            return 536870912;
         }
      }

      return Util.getPcmEncoding(var1);
   }
}

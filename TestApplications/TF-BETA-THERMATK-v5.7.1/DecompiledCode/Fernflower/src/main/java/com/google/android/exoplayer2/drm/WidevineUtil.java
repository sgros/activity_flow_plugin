package com.google.android.exoplayer2.drm;

import android.util.Pair;
import java.util.Map;

public final class WidevineUtil {
   private static long getDurationRemainingSec(Map var0, String var1) {
      if (var0 != null) {
         boolean var10001;
         String var6;
         try {
            var6 = (String)var0.get(var1);
         } catch (NumberFormatException var5) {
            var10001 = false;
            return -9223372036854775807L;
         }

         if (var6 != null) {
            try {
               long var2 = Long.parseLong(var6);
               return var2;
            } catch (NumberFormatException var4) {
               var10001 = false;
            }
         }
      }

      return -9223372036854775807L;
   }

   public static Pair getLicenseDurationRemainingSec(DrmSession var0) {
      Map var1 = var0.queryKeyStatus();
      return var1 == null ? null : new Pair(getDurationRemainingSec(var1, "LicenseDurationRemaining"), getDurationRemainingSec(var1, "PlaybackDurationRemaining"));
   }
}

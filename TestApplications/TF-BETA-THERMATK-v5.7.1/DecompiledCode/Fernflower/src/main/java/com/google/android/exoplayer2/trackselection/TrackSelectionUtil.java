package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.Format;

public final class TrackSelectionUtil {
   public static int[] getFormatBitrates(Format[] var0, int[] var1) {
      int var2 = var0.length;
      int[] var3 = var1;
      if (var1 == null) {
         var3 = new int[var2];
      }

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = var0[var4].bitrate;
      }

      return var3;
   }
}

package com.google.android.exoplayer2.upstream;

import android.text.TextUtils;
import com.google.android.exoplayer2.util.Util;

// $FF: synthetic class
public final class HttpDataSource$_CC {
   // $FF: synthetic method
   public static boolean lambda$static$0(String var0) {
      var0 = Util.toLowerInvariant(var0);
      boolean var1;
      if (!TextUtils.isEmpty(var0) && (!var0.contains("text") || var0.contains("text/vtt")) && !var0.contains("html") && !var0.contains("xml")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}

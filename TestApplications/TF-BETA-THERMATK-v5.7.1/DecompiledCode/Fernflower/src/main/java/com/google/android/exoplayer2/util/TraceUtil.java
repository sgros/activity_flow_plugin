package com.google.android.exoplayer2.util;

import android.annotation.TargetApi;
import android.os.Trace;

public final class TraceUtil {
   private TraceUtil() {
   }

   public static void beginSection(String var0) {
      if (Util.SDK_INT >= 18) {
         beginSectionV18(var0);
      }

   }

   @TargetApi(18)
   private static void beginSectionV18(String var0) {
      Trace.beginSection(var0);
   }

   public static void endSection() {
      if (Util.SDK_INT >= 18) {
         endSectionV18();
      }

   }

   @TargetApi(18)
   private static void endSectionV18() {
      Trace.endSection();
   }
}

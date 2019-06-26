package com.airbnb.lottie;

import androidx.core.os.TraceCompat;

public class L {
   public static boolean DBG;
   private static int depthPastMaxDepth;
   private static String[] sections;
   private static long[] startTimeNs;
   private static int traceDepth;
   private static boolean traceEnabled;

   public static void beginSection(String var0) {
      if (traceEnabled) {
         int var1 = traceDepth;
         if (var1 == 20) {
            ++depthPastMaxDepth;
         } else {
            sections[var1] = var0;
            startTimeNs[var1] = System.nanoTime();
            TraceCompat.beginSection(var0);
            ++traceDepth;
         }
      }
   }

   public static float endSection(String var0) {
      int var1 = depthPastMaxDepth;
      if (var1 > 0) {
         depthPastMaxDepth = var1 - 1;
         return 0.0F;
      } else if (!traceEnabled) {
         return 0.0F;
      } else {
         --traceDepth;
         var1 = traceDepth;
         if (var1 != -1) {
            if (var0.equals(sections[var1])) {
               TraceCompat.endSection();
               return (float)(System.nanoTime() - startTimeNs[traceDepth]) / 1000000.0F;
            } else {
               StringBuilder var2 = new StringBuilder();
               var2.append("Unbalanced trace call ");
               var2.append(var0);
               var2.append(". Expected ");
               var2.append(sections[traceDepth]);
               var2.append(".");
               throw new IllegalStateException(var2.toString());
            }
         } else {
            throw new IllegalStateException("Can't end trace section. There are none.");
         }
      }
   }
}

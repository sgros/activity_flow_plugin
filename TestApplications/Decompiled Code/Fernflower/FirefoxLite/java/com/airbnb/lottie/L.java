package com.airbnb.lottie;

import android.support.v4.os.TraceCompat;
import android.util.Log;
import java.util.HashSet;
import java.util.Set;

public class L {
   public static boolean DBG;
   private static int depthPastMaxDepth = 0;
   private static final Set loggedMessages = new HashSet();
   private static String[] sections;
   private static long[] startTimeNs;
   private static int traceDepth = 0;
   private static boolean traceEnabled = false;

   public static void beginSection(String var0) {
      if (traceEnabled) {
         if (traceDepth == 20) {
            ++depthPastMaxDepth;
         } else {
            sections[traceDepth] = var0;
            startTimeNs[traceDepth] = System.nanoTime();
            TraceCompat.beginSection(var0);
            ++traceDepth;
         }
      }
   }

   public static void debug(String var0) {
      if (DBG) {
         Log.d("LOTTIE", var0);
      }

   }

   public static float endSection(String var0) {
      if (depthPastMaxDepth > 0) {
         --depthPastMaxDepth;
         return 0.0F;
      } else if (!traceEnabled) {
         return 0.0F;
      } else {
         --traceDepth;
         if (traceDepth != -1) {
            if (var0.equals(sections[traceDepth])) {
               TraceCompat.endSection();
               return (float)(System.nanoTime() - startTimeNs[traceDepth]) / 1000000.0F;
            } else {
               StringBuilder var1 = new StringBuilder();
               var1.append("Unbalanced trace call ");
               var1.append(var0);
               var1.append(". Expected ");
               var1.append(sections[traceDepth]);
               var1.append(".");
               throw new IllegalStateException(var1.toString());
            }
         } else {
            throw new IllegalStateException("Can't end trace section. There are none.");
         }
      }
   }

   public static void warn(String var0) {
      if (!loggedMessages.contains(var0)) {
         Log.w("LOTTIE", var0);
         loggedMessages.add(var0);
      }
   }
}

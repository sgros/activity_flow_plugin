package com.google.android.exoplayer2;

import java.util.HashSet;

public final class ExoPlayerLibraryInfo {
   private static final HashSet registeredModules = new HashSet();
   private static String registeredModulesString = "goog.exo.core";

   public static void registerModule(String var0) {
      synchronized(ExoPlayerLibraryInfo.class){}

      try {
         if (registeredModules.add(var0)) {
            StringBuilder var1 = new StringBuilder();
            var1.append(registeredModulesString);
            var1.append(", ");
            var1.append(var0);
            registeredModulesString = var1.toString();
         }
      } finally {
         ;
      }

   }

   public static String registeredModules() {
      synchronized(ExoPlayerLibraryInfo.class){}

      String var0;
      try {
         var0 = registeredModulesString;
      } finally {
         ;
      }

      return var0;
   }
}

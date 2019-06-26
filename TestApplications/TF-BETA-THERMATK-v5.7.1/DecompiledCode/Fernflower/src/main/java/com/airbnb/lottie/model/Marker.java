package com.airbnb.lottie.model;

public class Marker {
   private static String CARRIAGE_RETURN;
   public final float durationFrames;
   private final String name;
   public final float startFrame;

   public Marker(String var1, float var2, float var3) {
      this.name = var1;
      this.durationFrames = var3;
      this.startFrame = var2;
   }

   public boolean matchesName(String var1) {
      if (this.name.equalsIgnoreCase(var1)) {
         return true;
      } else {
         if (this.name.endsWith(CARRIAGE_RETURN)) {
            String var2 = this.name;
            if (var2.substring(0, var2.length() - 1).equalsIgnoreCase(var1)) {
               return true;
            }
         }

         return false;
      }
   }
}

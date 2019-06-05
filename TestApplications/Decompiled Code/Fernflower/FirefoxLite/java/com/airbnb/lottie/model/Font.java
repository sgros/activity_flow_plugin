package com.airbnb.lottie.model;

public class Font {
   private final float ascent;
   private final String family;
   private final String name;
   private final String style;

   public Font(String var1, String var2, String var3, float var4) {
      this.family = var1;
      this.name = var2;
      this.style = var3;
      this.ascent = var4;
   }

   public String getFamily() {
      return this.family;
   }

   public String getName() {
      return this.name;
   }

   public String getStyle() {
      return this.style;
   }
}

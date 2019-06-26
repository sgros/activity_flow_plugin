package com.airbnb.lottie.model;

import java.util.List;

public class FontCharacter {
   private final char character;
   private final String fontFamily;
   private final List shapes;
   private final double size;
   private final String style;
   private final double width;

   public FontCharacter(List var1, char var2, double var3, double var5, String var7, String var8) {
      this.shapes = var1;
      this.character = (char)var2;
      this.size = var3;
      this.width = var5;
      this.style = var7;
      this.fontFamily = var8;
   }

   public static int hashFor(char var0, String var1, String var2) {
      return ((0 + var0) * 31 + var1.hashCode()) * 31 + var2.hashCode();
   }

   public List getShapes() {
      return this.shapes;
   }

   public double getWidth() {
      return this.width;
   }

   public int hashCode() {
      return hashFor(this.character, this.fontFamily, this.style);
   }
}

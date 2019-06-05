package com.google.zxing;

public final class Dimension {
   private final int height;
   private final int width;

   public Dimension(int var1, int var2) {
      if (var1 >= 0 && var2 >= 0) {
         this.width = var1;
         this.height = var2;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 instanceof Dimension) {
         Dimension var4 = (Dimension)var1;
         var3 = var2;
         if (this.width == var4.width) {
            var3 = var2;
            if (this.height == var4.height) {
               var3 = true;
            }
         }
      }

      return var3;
   }

   public int getHeight() {
      return this.height;
   }

   public int getWidth() {
      return this.width;
   }

   public int hashCode() {
      return this.width * 32713 + this.height;
   }

   public String toString() {
      return this.width + "x" + this.height;
   }
}

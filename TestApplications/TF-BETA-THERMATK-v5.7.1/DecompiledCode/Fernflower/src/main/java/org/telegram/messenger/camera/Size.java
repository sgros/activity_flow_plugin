package org.telegram.messenger.camera;

public final class Size {
   public final int mHeight;
   public final int mWidth;

   public Size(int var1, int var2) {
      this.mWidth = var1;
      this.mHeight = var2;
   }

   private static NumberFormatException invalidSize(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("Invalid Size: \"");
      var1.append(var0);
      var1.append("\"");
      throw new NumberFormatException(var1.toString());
   }

   public static Size parseSize(String var0) throws NumberFormatException {
      int var1 = var0.indexOf(42);
      int var2 = var1;
      if (var1 < 0) {
         var2 = var0.indexOf(120);
      }

      if (var2 >= 0) {
         try {
            Size var3 = new Size(Integer.parseInt(var0.substring(0, var2)), Integer.parseInt(var0.substring(var2 + 1)));
            return var3;
         } catch (NumberFormatException var4) {
            invalidSize(var0);
            throw null;
         }
      } else {
         invalidSize(var0);
         throw null;
      }
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else {
         boolean var3 = var2;
         if (var1 instanceof Size) {
            Size var4 = (Size)var1;
            var3 = var2;
            if (this.mWidth == var4.mWidth) {
               var3 = var2;
               if (this.mHeight == var4.mHeight) {
                  var3 = true;
               }
            }
         }

         return var3;
      }
   }

   public int getHeight() {
      return this.mHeight;
   }

   public int getWidth() {
      return this.mWidth;
   }

   public int hashCode() {
      int var1 = this.mHeight;
      int var2 = this.mWidth;
      return var1 ^ (var2 >>> 16 | var2 << 16);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.mWidth);
      var1.append("x");
      var1.append(this.mHeight);
      return var1.toString();
   }
}

package com.google.zxing;

public abstract class LuminanceSource {
   private final int height;
   private final int width;

   protected LuminanceSource(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   public LuminanceSource crop(int var1, int var2, int var3, int var4) {
      throw new UnsupportedOperationException("This luminance source does not support cropping.");
   }

   public final int getHeight() {
      return this.height;
   }

   public abstract byte[] getMatrix();

   public abstract byte[] getRow(int var1, byte[] var2);

   public final int getWidth() {
      return this.width;
   }

   public LuminanceSource invert() {
      return new InvertedLuminanceSource(this);
   }

   public boolean isCropSupported() {
      return false;
   }

   public boolean isRotateSupported() {
      return false;
   }

   public LuminanceSource rotateCounterClockwise() {
      throw new UnsupportedOperationException("This luminance source does not support rotation by 90 degrees.");
   }

   public LuminanceSource rotateCounterClockwise45() {
      throw new UnsupportedOperationException("This luminance source does not support rotation by 45 degrees.");
   }

   public final String toString() {
      byte[] var1 = new byte[this.width];
      StringBuilder var2 = new StringBuilder(this.height * (this.width + 1));

      for(int var3 = 0; var3 < this.height; ++var3) {
         var1 = this.getRow(var3, var1);

         for(int var4 = 0; var4 < this.width; ++var4) {
            int var5 = var1[var4] & 255;
            char var6;
            byte var7;
            if (var5 < 64) {
               var7 = 35;
               var6 = (char)var7;
            } else if (var5 < 128) {
               var7 = 43;
               var6 = (char)var7;
            } else if (var5 < 192) {
               var7 = 46;
               var6 = (char)var7;
            } else {
               var7 = 32;
               var6 = (char)var7;
            }

            var2.append(var6);
         }

         var2.append('\n');
      }

      return var2.toString();
   }
}

package com.google.zxing;

public final class RGBLuminanceSource extends LuminanceSource {
   private final int dataHeight;
   private final int dataWidth;
   private final int left;
   private final byte[] luminances;
   private final int top;

   public RGBLuminanceSource(int var1, int var2, int[] var3) {
      super(var1, var2);
      this.dataWidth = var1;
      this.dataHeight = var2;
      this.left = 0;
      this.top = 0;
      var2 = var1 * var2;
      this.luminances = new byte[var2];

      for(var1 = 0; var1 < var2; ++var1) {
         int var4 = var3[var1];
         this.luminances[var1] = (byte)((byte)(((var4 >> 16 & 255) + (var4 >> 7 & 510) + (var4 & 255)) / 4));
      }

   }

   private RGBLuminanceSource(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      super(var6, var7);
      if (var4 + var6 <= var2 && var5 + var7 <= var3) {
         this.luminances = var1;
         this.dataWidth = var2;
         this.dataHeight = var3;
         this.left = var4;
         this.top = var5;
      } else {
         throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
      }
   }

   public LuminanceSource crop(int var1, int var2, int var3, int var4) {
      return new RGBLuminanceSource(this.luminances, this.dataWidth, this.dataHeight, this.left + var1, this.top + var2, var3, var4);
   }

   public byte[] getMatrix() {
      int var1 = this.getWidth();
      int var2 = this.getHeight();
      byte[] var3;
      if (var1 == this.dataWidth && var2 == this.dataHeight) {
         var3 = this.luminances;
      } else {
         int var4 = var1 * var2;
         byte[] var5 = new byte[var4];
         int var6 = this.top * this.dataWidth + this.left;
         if (var1 == this.dataWidth) {
            System.arraycopy(this.luminances, var6, var5, 0, var4);
            var3 = var5;
         } else {
            var4 = 0;

            while(true) {
               var3 = var5;
               if (var4 >= var2) {
                  break;
               }

               System.arraycopy(this.luminances, var6, var5, var4 * var1, var1);
               var6 += this.dataWidth;
               ++var4;
            }
         }
      }

      return var3;
   }

   public byte[] getRow(int var1, byte[] var2) {
      if (var1 >= 0 && var1 < this.getHeight()) {
         int var3;
         byte[] var4;
         label14: {
            var3 = this.getWidth();
            if (var2 != null) {
               var4 = var2;
               if (var2.length >= var3) {
                  break label14;
               }
            }

            var4 = new byte[var3];
         }

         int var5 = this.top;
         int var6 = this.dataWidth;
         int var7 = this.left;
         System.arraycopy(this.luminances, (var5 + var1) * var6 + var7, var4, 0, var3);
         return var4;
      } else {
         throw new IllegalArgumentException("Requested row is outside the image: " + var1);
      }
   }

   public boolean isCropSupported() {
      return true;
   }
}

package com.google.zxing;

public final class PlanarYUVLuminanceSource extends LuminanceSource {
   private static final int THUMBNAIL_SCALE_FACTOR = 2;
   private final int dataHeight;
   private final int dataWidth;
   private final int left;
   private final int top;
   private final byte[] yuvData;

   public PlanarYUVLuminanceSource(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
      super(var6, var7);
      if (var4 + var6 <= var2 && var5 + var7 <= var3) {
         this.yuvData = var1;
         this.dataWidth = var2;
         this.dataHeight = var3;
         this.left = var4;
         this.top = var5;
         if (var8) {
            this.reverseHorizontal(var6, var7);
         }

      } else {
         throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
      }
   }

   private void reverseHorizontal(int var1, int var2) {
      byte[] var3 = this.yuvData;
      int var4 = 0;

      for(int var5 = this.top * this.dataWidth + this.left; var4 < var2; var5 += this.dataWidth) {
         int var6 = var1 / 2;
         int var7 = var5;

         for(int var8 = var5 + var1 - 1; var7 < var5 + var6; --var8) {
            byte var9 = var3[var7];
            var3[var7] = (byte)var3[var8];
            var3[var8] = (byte)var9;
            ++var7;
         }

         ++var4;
      }

   }

   public LuminanceSource crop(int var1, int var2, int var3, int var4) {
      return new PlanarYUVLuminanceSource(this.yuvData, this.dataWidth, this.dataHeight, this.left + var1, this.top + var2, var3, var4, false);
   }

   public byte[] getMatrix() {
      int var1 = this.getWidth();
      int var2 = this.getHeight();
      byte[] var3;
      if (var1 == this.dataWidth && var2 == this.dataHeight) {
         var3 = this.yuvData;
      } else {
         int var4 = var1 * var2;
         byte[] var5 = new byte[var4];
         int var6 = this.top * this.dataWidth + this.left;
         if (var1 == this.dataWidth) {
            System.arraycopy(this.yuvData, var6, var5, 0, var4);
            var3 = var5;
         } else {
            var4 = 0;

            while(true) {
               var3 = var5;
               if (var4 >= var2) {
                  break;
               }

               System.arraycopy(this.yuvData, var6, var5, var4 * var1, var1);
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
         System.arraycopy(this.yuvData, (var5 + var1) * var6 + var7, var4, 0, var3);
         return var4;
      } else {
         throw new IllegalArgumentException("Requested row is outside the image: " + var1);
      }
   }

   public int getThumbnailHeight() {
      return this.getHeight() / 2;
   }

   public int getThumbnailWidth() {
      return this.getWidth() / 2;
   }

   public boolean isCropSupported() {
      return true;
   }

   public int[] renderThumbnail() {
      int var1 = this.getWidth() / 2;
      int var2 = this.getHeight() / 2;
      int[] var3 = new int[var1 * var2];
      byte[] var4 = this.yuvData;
      int var5 = this.top * this.dataWidth + this.left;

      for(int var6 = 0; var6 < var2; ++var6) {
         for(int var7 = 0; var7 < var1; ++var7) {
            var3[var6 * var1 + var7] = -16777216 | 65793 * (var4[(var7 << 1) + var5] & 255);
         }

         var5 += this.dataWidth << 1;
      }

      return var3;
   }
}

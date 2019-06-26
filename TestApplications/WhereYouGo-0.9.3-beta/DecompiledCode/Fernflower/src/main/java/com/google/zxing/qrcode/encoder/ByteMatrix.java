package com.google.zxing.qrcode.encoder;

public final class ByteMatrix {
   private final byte[][] bytes;
   private final int height;
   private final int width;

   public ByteMatrix(int var1, int var2) {
      this.bytes = new byte[var2][var1];
      this.width = var1;
      this.height = var2;
   }

   public void clear(byte var1) {
      for(int var2 = 0; var2 < this.height; ++var2) {
         for(int var3 = 0; var3 < this.width; ++var3) {
            this.bytes[var2][var3] = (byte)var1;
         }
      }

   }

   public byte get(int var1, int var2) {
      return this.bytes[var2][var1];
   }

   public byte[][] getArray() {
      return this.bytes;
   }

   public int getHeight() {
      return this.height;
   }

   public int getWidth() {
      return this.width;
   }

   public void set(int var1, int var2, byte var3) {
      this.bytes[var2][var1] = (byte)var3;
   }

   public void set(int var1, int var2, int var3) {
      this.bytes[var2][var1] = (byte)((byte)var3);
   }

   public void set(int var1, int var2, boolean var3) {
      byte[] var4 = this.bytes[var2];
      byte var5;
      if (var3) {
         var5 = 1;
      } else {
         var5 = 0;
      }

      var4[var1] = (byte)((byte)var5);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(this.width * 2 * this.height + 2);

      for(int var2 = 0; var2 < this.height; ++var2) {
         for(int var3 = 0; var3 < this.width; ++var3) {
            switch(this.bytes[var2][var3]) {
            case 0:
               var1.append(" 0");
               break;
            case 1:
               var1.append(" 1");
               break;
            default:
               var1.append("  ");
            }
         }

         var1.append('\n');
      }

      return var1.toString();
   }
}

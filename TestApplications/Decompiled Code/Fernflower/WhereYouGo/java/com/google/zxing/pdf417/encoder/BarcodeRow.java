package com.google.zxing.pdf417.encoder;

final class BarcodeRow {
   private int currentLocation;
   private final byte[] row;

   BarcodeRow(int var1) {
      this.row = new byte[var1];
      this.currentLocation = 0;
   }

   private void set(int var1, boolean var2) {
      byte[] var3 = this.row;
      byte var4;
      if (var2) {
         var4 = 1;
      } else {
         var4 = 0;
      }

      var3[var1] = (byte)((byte)var4);
   }

   void addBar(boolean var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = this.currentLocation++;
         this.set(var4, var1);
      }

   }

   byte[] getScaledRow(int var1) {
      byte[] var2 = new byte[this.row.length * var1];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = (byte)this.row[var3 / var1];
      }

      return var2;
   }

   void set(int var1, byte var2) {
      this.row[var1] = (byte)var2;
   }
}

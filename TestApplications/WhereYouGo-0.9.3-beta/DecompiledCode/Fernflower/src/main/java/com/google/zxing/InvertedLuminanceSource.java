package com.google.zxing;

public final class InvertedLuminanceSource extends LuminanceSource {
   private final LuminanceSource delegate;

   public InvertedLuminanceSource(LuminanceSource var1) {
      super(var1.getWidth(), var1.getHeight());
      this.delegate = var1;
   }

   public LuminanceSource crop(int var1, int var2, int var3, int var4) {
      return new InvertedLuminanceSource(this.delegate.crop(var1, var2, var3, var4));
   }

   public byte[] getMatrix() {
      byte[] var1 = this.delegate.getMatrix();
      int var2 = this.getWidth() * this.getHeight();
      byte[] var3 = new byte[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = (byte)((byte)(255 - (var1[var4] & 255)));
      }

      return var3;
   }

   public byte[] getRow(int var1, byte[] var2) {
      var2 = this.delegate.getRow(var1, var2);
      int var3 = this.getWidth();

      for(var1 = 0; var1 < var3; ++var1) {
         var2[var1] = (byte)((byte)(255 - (var2[var1] & 255)));
      }

      return var2;
   }

   public LuminanceSource invert() {
      return this.delegate;
   }

   public boolean isCropSupported() {
      return this.delegate.isCropSupported();
   }

   public boolean isRotateSupported() {
      return this.delegate.isRotateSupported();
   }

   public LuminanceSource rotateCounterClockwise() {
      return new InvertedLuminanceSource(this.delegate.rotateCounterClockwise());
   }

   public LuminanceSource rotateCounterClockwise45() {
      return new InvertedLuminanceSource(this.delegate.rotateCounterClockwise45());
   }
}

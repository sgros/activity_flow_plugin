package com.google.zxing.qrcode.detector;

public final class FinderPatternInfo {
   private final FinderPattern bottomLeft;
   private final FinderPattern topLeft;
   private final FinderPattern topRight;

   public FinderPatternInfo(FinderPattern[] var1) {
      this.bottomLeft = var1[0];
      this.topLeft = var1[1];
      this.topRight = var1[2];
   }

   public FinderPattern getBottomLeft() {
      return this.bottomLeft;
   }

   public FinderPattern getTopLeft() {
      return this.topLeft;
   }

   public FinderPattern getTopRight() {
      return this.topRight;
   }
}

package com.google.zxing.common;

import com.google.zxing.ResultPoint;

public class DetectorResult {
   private final BitMatrix bits;
   private final ResultPoint[] points;

   public DetectorResult(BitMatrix var1, ResultPoint[] var2) {
      this.bits = var1;
      this.points = var2;
   }

   public final BitMatrix getBits() {
      return this.bits;
   }

   public final ResultPoint[] getPoints() {
      return this.points;
   }
}

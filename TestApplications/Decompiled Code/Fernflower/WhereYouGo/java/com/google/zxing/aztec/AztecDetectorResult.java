package com.google.zxing.aztec;

import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;

public final class AztecDetectorResult extends DetectorResult {
   private final boolean compact;
   private final int nbDatablocks;
   private final int nbLayers;

   public AztecDetectorResult(BitMatrix var1, ResultPoint[] var2, boolean var3, int var4, int var5) {
      super(var1, var2);
      this.compact = var3;
      this.nbDatablocks = var4;
      this.nbLayers = var5;
   }

   public int getNbDatablocks() {
      return this.nbDatablocks;
   }

   public int getNbLayers() {
      return this.nbLayers;
   }

   public boolean isCompact() {
      return this.compact;
   }
}

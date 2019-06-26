package com.google.zxing;

import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

public abstract class Binarizer {
   private final LuminanceSource source;

   protected Binarizer(LuminanceSource var1) {
      this.source = var1;
   }

   public abstract Binarizer createBinarizer(LuminanceSource var1);

   public abstract BitMatrix getBlackMatrix() throws NotFoundException;

   public abstract BitArray getBlackRow(int var1, BitArray var2) throws NotFoundException;

   public final int getHeight() {
      return this.source.getHeight();
   }

   public final LuminanceSource getLuminanceSource() {
      return this.source;
   }

   public final int getWidth() {
      return this.source.getWidth();
   }
}

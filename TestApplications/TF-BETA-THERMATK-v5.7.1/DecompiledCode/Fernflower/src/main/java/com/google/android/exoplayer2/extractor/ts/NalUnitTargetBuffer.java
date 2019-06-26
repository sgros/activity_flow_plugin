package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.util.Assertions;
import java.util.Arrays;

final class NalUnitTargetBuffer {
   private boolean isCompleted;
   private boolean isFilling;
   public byte[] nalData;
   public int nalLength;
   private final int targetType;

   public NalUnitTargetBuffer(int var1, int var2) {
      this.targetType = var1;
      this.nalData = new byte[var2 + 3];
      this.nalData[2] = (byte)1;
   }

   public void appendToNalUnit(byte[] var1, int var2, int var3) {
      if (this.isFilling) {
         var3 -= var2;
         byte[] var4 = this.nalData;
         int var5 = var4.length;
         int var6 = this.nalLength;
         if (var5 < var6 + var3) {
            this.nalData = Arrays.copyOf(var4, (var6 + var3) * 2);
         }

         System.arraycopy(var1, var2, this.nalData, this.nalLength, var3);
         this.nalLength += var3;
      }
   }

   public boolean endNalUnit(int var1) {
      if (!this.isFilling) {
         return false;
      } else {
         this.nalLength -= var1;
         this.isFilling = false;
         this.isCompleted = true;
         return true;
      }
   }

   public boolean isCompleted() {
      return this.isCompleted;
   }

   public void reset() {
      this.isFilling = false;
      this.isCompleted = false;
   }

   public void startNalUnit(int var1) {
      boolean var2 = this.isFilling;
      boolean var3 = true;
      Assertions.checkState(var2 ^ true);
      if (var1 != this.targetType) {
         var3 = false;
      }

      this.isFilling = var3;
      if (this.isFilling) {
         this.nalLength = 3;
         this.isCompleted = false;
      }

   }
}

package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitArray;
import java.util.Iterator;
import java.util.LinkedList;

final class State {
   static final State INITIAL_STATE;
   private final int binaryShiftByteCount;
   private final int bitCount;
   private final int mode;
   private final Token token;

   static {
      INITIAL_STATE = new State(Token.EMPTY, 0, 0, 0);
   }

   private State(Token var1, int var2, int var3, int var4) {
      this.token = var1;
      this.mode = var2;
      this.binaryShiftByteCount = var3;
      this.bitCount = var4;
   }

   State addBinaryShiftChar(int var1) {
      int var5;
      int var6;
      Token var7;
      label27: {
         Token var2 = this.token;
         int var3 = this.mode;
         int var4 = this.bitCount;
         if (this.mode != 4) {
            var5 = var4;
            var6 = var3;
            var7 = var2;
            if (this.mode != 2) {
               break label27;
            }
         }

         var6 = HighLevelEncoder.LATCH_TABLE[var3][0];
         var7 = var2.add('\uffff' & var6, var6 >> 16);
         var5 = var4 + (var6 >> 16);
         var6 = 0;
      }

      byte var9;
      if (this.binaryShiftByteCount != 0 && this.binaryShiftByteCount != 31) {
         if (this.binaryShiftByteCount == 62) {
            var9 = 9;
         } else {
            var9 = 8;
         }
      } else {
         var9 = 18;
      }

      State var8 = new State(var7, var6, this.binaryShiftByteCount + 1, var5 + var9);
      State var10 = var8;
      if (var8.binaryShiftByteCount == 2078) {
         var10 = var8.endBinaryShift(var1 + 1);
      }

      return var10;
   }

   State endBinaryShift(int var1) {
      State var2;
      if (this.binaryShiftByteCount == 0) {
         var2 = this;
      } else {
         var2 = new State(this.token.addBinaryShift(var1 - this.binaryShiftByteCount, this.binaryShiftByteCount), this.mode, 0, this.bitCount);
      }

      return var2;
   }

   int getBinaryShiftByteCount() {
      return this.binaryShiftByteCount;
   }

   int getBitCount() {
      return this.bitCount;
   }

   int getMode() {
      return this.mode;
   }

   Token getToken() {
      return this.token;
   }

   boolean isBetterThanOrEqualTo(State var1) {
      int var2 = this.bitCount + (HighLevelEncoder.LATCH_TABLE[this.mode][var1.mode] >> 16);
      int var3 = var2;
      if (var1.binaryShiftByteCount > 0) {
         label17: {
            if (this.binaryShiftByteCount != 0) {
               var3 = var2;
               if (this.binaryShiftByteCount <= var1.binaryShiftByteCount) {
                  break label17;
               }
            }

            var3 = var2 + 10;
         }
      }

      boolean var4;
      if (var3 <= var1.bitCount) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   State latchAndAppend(int var1, int var2) {
      int var3 = this.bitCount;
      Token var4 = this.token;
      int var5 = var3;
      Token var6 = var4;
      if (var1 != this.mode) {
         var5 = HighLevelEncoder.LATCH_TABLE[this.mode][var1];
         var6 = var4.add('\uffff' & var5, var5 >> 16);
         var5 = var3 + (var5 >> 16);
      }

      byte var7;
      if (var1 == 2) {
         var7 = 4;
      } else {
         var7 = 5;
      }

      return new State(var6.add(var2, var7), var1, 0, var5 + var7);
   }

   State shiftAndAppend(int var1, int var2) {
      Token var3 = this.token;
      byte var4;
      if (this.mode == 2) {
         var4 = 4;
      } else {
         var4 = 5;
      }

      return new State(var3.add(HighLevelEncoder.SHIFT_TABLE[this.mode][var1], var4).add(var2, 5), this.mode, 0, this.bitCount + var4 + 5);
   }

   BitArray toBitArray(byte[] var1) {
      LinkedList var2 = new LinkedList();

      for(Token var3 = this.endBinaryShift(var1.length).token; var3 != null; var3 = var3.getPrevious()) {
         var2.addFirst(var3);
      }

      BitArray var5 = new BitArray();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         ((Token)var4.next()).appendTo(var5, var1);
      }

      return var5;
   }

   public String toString() {
      return String.format("%s bits=%d bytes=%d", HighLevelEncoder.MODE_NAMES[this.mode], this.bitCount, this.binaryShiftByteCount);
   }
}

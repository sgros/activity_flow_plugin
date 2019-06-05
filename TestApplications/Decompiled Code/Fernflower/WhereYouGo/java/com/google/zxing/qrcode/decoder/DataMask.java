package com.google.zxing.qrcode.decoder;

import com.google.zxing.common.BitMatrix;

enum DataMask {
   DATA_MASK_000 {
      boolean isMasked(int var1, int var2) {
         boolean var3;
         if ((var1 + var2 & 1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   },
   DATA_MASK_001 {
      boolean isMasked(int var1, int var2) {
         boolean var3;
         if ((var1 & 1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   },
   DATA_MASK_010 {
      boolean isMasked(int var1, int var2) {
         boolean var3;
         if (var2 % 3 == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   },
   DATA_MASK_011 {
      boolean isMasked(int var1, int var2) {
         boolean var3;
         if ((var1 + var2) % 3 == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   },
   DATA_MASK_100 {
      boolean isMasked(int var1, int var2) {
         boolean var3;
         if ((var1 / 2 + var2 / 3 & 1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   },
   DATA_MASK_101 {
      boolean isMasked(int var1, int var2) {
         boolean var3;
         if (var1 * var2 % 6 == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   },
   DATA_MASK_110 {
      boolean isMasked(int var1, int var2) {
         boolean var3;
         if (var1 * var2 % 6 < 3) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   },
   DATA_MASK_111 {
      boolean isMasked(int var1, int var2) {
         boolean var3;
         if ((var1 + var2 + var1 * var2 % 3 & 1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   };

   private DataMask() {
   }

   // $FF: synthetic method
   DataMask(Object var3) {
      this();
   }

   abstract boolean isMasked(int var1, int var2);

   final void unmaskBitMatrix(BitMatrix var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         for(int var4 = 0; var4 < var2; ++var4) {
            if (this.isMasked(var3, var4)) {
               var1.flip(var4, var3);
            }
         }
      }

   }
}

package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.FormatException;

final class DecodedNumeric extends DecodedObject {
   static final int FNC1 = 10;
   private final int firstDigit;
   private final int secondDigit;

   DecodedNumeric(int var1, int var2, int var3) throws FormatException {
      super(var1);
      if (var2 >= 0 && var2 <= 10 && var3 >= 0 && var3 <= 10) {
         this.firstDigit = var2;
         this.secondDigit = var3;
      } else {
         throw FormatException.getFormatInstance();
      }
   }

   int getFirstDigit() {
      return this.firstDigit;
   }

   int getSecondDigit() {
      return this.secondDigit;
   }

   int getValue() {
      return this.firstDigit * 10 + this.secondDigit;
   }

   boolean isAnyFNC1() {
      boolean var1;
      if (this.firstDigit != 10 && this.secondDigit != 10) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   boolean isFirstDigitFNC1() {
      boolean var1;
      if (this.firstDigit == 10) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   boolean isSecondDigitFNC1() {
      boolean var1;
      if (this.secondDigit == 10) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}

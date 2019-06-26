package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

final class GeneralAppIdDecoder {
   private final StringBuilder buffer = new StringBuilder();
   private final CurrentParsingState current = new CurrentParsingState();
   private final BitArray information;

   GeneralAppIdDecoder(BitArray var1) {
      this.information = var1;
   }

   private DecodedChar decodeAlphanumeric(int var1) {
      int var2 = this.extractNumericValueFromBitArray(var1, 5);
      DecodedChar var3;
      if (var2 == 15) {
         var3 = new DecodedChar(var1 + 5, '$');
      } else if (var2 >= 5 && var2 < 15) {
         var3 = new DecodedChar(var1 + 5, (char)(var2 + 48 - 5));
      } else {
         var2 = this.extractNumericValueFromBitArray(var1, 6);
         if (var2 >= 32 && var2 < 58) {
            var3 = new DecodedChar(var1 + 6, (char)(var2 + 33));
         } else {
            char var4;
            byte var5;
            switch(var2) {
            case 58:
               var5 = 42;
               var4 = (char)var5;
               break;
            case 59:
               var5 = 44;
               var4 = (char)var5;
               break;
            case 60:
               var5 = 45;
               var4 = (char)var5;
               break;
            case 61:
               var5 = 46;
               var4 = (char)var5;
               break;
            case 62:
               var5 = 47;
               var4 = (char)var5;
               break;
            default:
               throw new IllegalStateException("Decoding invalid alphanumeric value: " + var2);
            }

            var3 = new DecodedChar(var1 + 6, var4);
         }
      }

      return var3;
   }

   private DecodedChar decodeIsoIec646(int var1) throws FormatException {
      int var2 = this.extractNumericValueFromBitArray(var1, 5);
      DecodedChar var3;
      if (var2 == 15) {
         var3 = new DecodedChar(var1 + 5, '$');
      } else if (var2 >= 5 && var2 < 15) {
         var3 = new DecodedChar(var1 + 5, (char)(var2 + 48 - 5));
      } else {
         var2 = this.extractNumericValueFromBitArray(var1, 7);
         if (var2 >= 64 && var2 < 90) {
            var3 = new DecodedChar(var1 + 7, (char)(var2 + 1));
         } else if (var2 >= 90 && var2 < 116) {
            var3 = new DecodedChar(var1 + 7, (char)(var2 + 7));
         } else {
            char var4;
            byte var5;
            switch(this.extractNumericValueFromBitArray(var1, 8)) {
            case 232:
               var5 = 33;
               var4 = (char)var5;
               break;
            case 233:
               var5 = 34;
               var4 = (char)var5;
               break;
            case 234:
               var5 = 37;
               var4 = (char)var5;
               break;
            case 235:
               var5 = 38;
               var4 = (char)var5;
               break;
            case 236:
               var5 = 39;
               var4 = (char)var5;
               break;
            case 237:
               var5 = 40;
               var4 = (char)var5;
               break;
            case 238:
               var5 = 41;
               var4 = (char)var5;
               break;
            case 239:
               var5 = 42;
               var4 = (char)var5;
               break;
            case 240:
               var5 = 43;
               var4 = (char)var5;
               break;
            case 241:
               var5 = 44;
               var4 = (char)var5;
               break;
            case 242:
               var5 = 45;
               var4 = (char)var5;
               break;
            case 243:
               var5 = 46;
               var4 = (char)var5;
               break;
            case 244:
               var5 = 47;
               var4 = (char)var5;
               break;
            case 245:
               var5 = 58;
               var4 = (char)var5;
               break;
            case 246:
               var5 = 59;
               var4 = (char)var5;
               break;
            case 247:
               var5 = 60;
               var4 = (char)var5;
               break;
            case 248:
               var5 = 61;
               var4 = (char)var5;
               break;
            case 249:
               var5 = 62;
               var4 = (char)var5;
               break;
            case 250:
               var5 = 63;
               var4 = (char)var5;
               break;
            case 251:
               var5 = 95;
               var4 = (char)var5;
               break;
            case 252:
               var5 = 32;
               var4 = (char)var5;
               break;
            default:
               throw FormatException.getFormatInstance();
            }

            var3 = new DecodedChar(var1 + 8, var4);
         }
      }

      return var3;
   }

   private DecodedNumeric decodeNumeric(int var1) throws FormatException {
      DecodedNumeric var2;
      if (var1 + 7 > this.information.getSize()) {
         var1 = this.extractNumericValueFromBitArray(var1, 4);
         if (var1 == 0) {
            var2 = new DecodedNumeric(this.information.getSize(), 10, 10);
         } else {
            var2 = new DecodedNumeric(this.information.getSize(), var1 - 1, 10);
         }
      } else {
         int var3 = this.extractNumericValueFromBitArray(var1, 7);
         var2 = new DecodedNumeric(var1 + 7, (var3 - 8) / 11, (var3 - 8) % 11);
      }

      return var2;
   }

   static int extractNumericValueFromBitArray(BitArray var0, int var1, int var2) {
      int var3 = 0;

      int var5;
      for(int var4 = 0; var4 < var2; var3 = var5) {
         var5 = var3;
         if (var0.get(var1 + var4)) {
            var5 = var3 | 1 << var2 - var4 - 1;
         }

         ++var4;
      }

      return var3;
   }

   private boolean isAlphaOr646ToNumericLatch(int var1) {
      boolean var2 = false;
      boolean var3;
      if (var1 + 3 > this.information.getSize()) {
         var3 = var2;
      } else {
         int var4 = var1;

         while(true) {
            if (var4 >= var1 + 3) {
               var3 = true;
               break;
            }

            var3 = var2;
            if (this.information.get(var4)) {
               break;
            }

            ++var4;
         }
      }

      return var3;
   }

   private boolean isAlphaTo646ToAlphaLatch(int var1) {
      boolean var2 = false;
      boolean var3;
      if (var1 + 1 > this.information.getSize()) {
         var3 = var2;
      } else {
         for(int var4 = 0; var4 < 5 && var4 + var1 < this.information.getSize(); ++var4) {
            if (var4 == 2) {
               var3 = var2;
               if (!this.information.get(var1 + 2)) {
                  return var3;
               }
            } else if (this.information.get(var1 + var4)) {
               var3 = var2;
               return var3;
            }
         }

         var3 = true;
      }

      return var3;
   }

   private boolean isNumericToAlphaNumericLatch(int var1) {
      boolean var2 = false;
      boolean var3;
      if (var1 + 1 > this.information.getSize()) {
         var3 = var2;
      } else {
         for(int var4 = 0; var4 < 4 && var4 + var1 < this.information.getSize(); ++var4) {
            var3 = var2;
            if (this.information.get(var1 + var4)) {
               return var3;
            }
         }

         var3 = true;
      }

      return var3;
   }

   private boolean isStillAlpha(int var1) {
      boolean var2 = false;
      boolean var3;
      if (var1 + 5 > this.information.getSize()) {
         var3 = var2;
      } else {
         int var4 = this.extractNumericValueFromBitArray(var1, 5);
         if (var4 >= 5 && var4 < 16) {
            var3 = true;
         } else {
            var3 = var2;
            if (var1 + 6 <= this.information.getSize()) {
               var1 = this.extractNumericValueFromBitArray(var1, 6);
               var3 = var2;
               if (var1 >= 16) {
                  var3 = var2;
                  if (var1 < 63) {
                     var3 = true;
                  }
               }
            }
         }
      }

      return var3;
   }

   private boolean isStillIsoIec646(int var1) {
      boolean var2 = false;
      boolean var3;
      if (var1 + 5 > this.information.getSize()) {
         var3 = var2;
      } else {
         int var4 = this.extractNumericValueFromBitArray(var1, 5);
         if (var4 >= 5 && var4 < 16) {
            var3 = true;
         } else {
            var3 = var2;
            if (var1 + 7 <= this.information.getSize()) {
               var4 = this.extractNumericValueFromBitArray(var1, 7);
               if (var4 >= 64 && var4 < 116) {
                  var3 = true;
               } else {
                  var3 = var2;
                  if (var1 + 8 <= this.information.getSize()) {
                     var1 = this.extractNumericValueFromBitArray(var1, 8);
                     var3 = var2;
                     if (var1 >= 232) {
                        var3 = var2;
                        if (var1 < 253) {
                           var3 = true;
                        }
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   private boolean isStillNumeric(int var1) {
      boolean var2 = true;
      boolean var3;
      if (var1 + 7 > this.information.getSize()) {
         if (var1 + 4 <= this.information.getSize()) {
            var3 = var2;
         } else {
            var3 = false;
         }
      } else {
         int var4 = var1;

         while(true) {
            if (var4 >= var1 + 3) {
               var3 = this.information.get(var1 + 3);
               break;
            }

            var3 = var2;
            if (this.information.get(var4)) {
               break;
            }

            ++var4;
         }
      }

      return var3;
   }

   private BlockParsedResult parseAlphaBlock() {
      while(true) {
         BlockParsedResult var2;
         if (this.isStillAlpha(this.current.getPosition())) {
            DecodedChar var1 = this.decodeAlphanumeric(this.current.getPosition());
            this.current.setPosition(var1.getNewPosition());
            if (!var1.isFNC1()) {
               this.buffer.append(var1.getValue());
               continue;
            }

            var2 = new BlockParsedResult(new DecodedInformation(this.current.getPosition(), this.buffer.toString()), true);
         } else {
            if (this.isAlphaOr646ToNumericLatch(this.current.getPosition())) {
               this.current.incrementPosition(3);
               this.current.setNumeric();
            } else if (this.isAlphaTo646ToAlphaLatch(this.current.getPosition())) {
               if (this.current.getPosition() + 5 < this.information.getSize()) {
                  this.current.incrementPosition(5);
               } else {
                  this.current.setPosition(this.information.getSize());
               }

               this.current.setIsoIec646();
            }

            var2 = new BlockParsedResult(false);
         }

         return var2;
      }
   }

   private DecodedInformation parseBlocks() throws FormatException {
      BlockParsedResult var2;
      boolean var3;
      boolean var4;
      do {
         int var1 = this.current.getPosition();
         if (this.current.isAlpha()) {
            var2 = this.parseAlphaBlock();
            var3 = var2.isFinished();
         } else if (this.current.isIsoIec646()) {
            var2 = this.parseIsoIec646Block();
            var3 = var2.isFinished();
         } else {
            var2 = this.parseNumericBlock();
            var3 = var2.isFinished();
         }

         if (var1 != this.current.getPosition()) {
            var4 = true;
         } else {
            var4 = false;
         }
      } while((var4 || var3) && !var3);

      return var2.getDecodedInformation();
   }

   private BlockParsedResult parseIsoIec646Block() throws FormatException {
      while(true) {
         BlockParsedResult var2;
         if (this.isStillIsoIec646(this.current.getPosition())) {
            DecodedChar var1 = this.decodeIsoIec646(this.current.getPosition());
            this.current.setPosition(var1.getNewPosition());
            if (!var1.isFNC1()) {
               this.buffer.append(var1.getValue());
               continue;
            }

            var2 = new BlockParsedResult(new DecodedInformation(this.current.getPosition(), this.buffer.toString()), true);
         } else {
            if (this.isAlphaOr646ToNumericLatch(this.current.getPosition())) {
               this.current.incrementPosition(3);
               this.current.setNumeric();
            } else if (this.isAlphaTo646ToAlphaLatch(this.current.getPosition())) {
               if (this.current.getPosition() + 5 < this.information.getSize()) {
                  this.current.incrementPosition(5);
               } else {
                  this.current.setPosition(this.information.getSize());
               }

               this.current.setAlpha();
            }

            var2 = new BlockParsedResult(false);
         }

         return var2;
      }
   }

   private BlockParsedResult parseNumericBlock() throws FormatException {
      while(true) {
         BlockParsedResult var3;
         if (this.isStillNumeric(this.current.getPosition())) {
            DecodedNumeric var1 = this.decodeNumeric(this.current.getPosition());
            this.current.setPosition(var1.getNewPosition());
            if (var1.isFirstDigitFNC1()) {
               DecodedInformation var2;
               if (var1.isSecondDigitFNC1()) {
                  var2 = new DecodedInformation(this.current.getPosition(), this.buffer.toString());
               } else {
                  var2 = new DecodedInformation(this.current.getPosition(), this.buffer.toString(), var1.getSecondDigit());
               }

               var3 = new BlockParsedResult(var2, true);
            } else {
               this.buffer.append(var1.getFirstDigit());
               if (!var1.isSecondDigitFNC1()) {
                  this.buffer.append(var1.getSecondDigit());
                  continue;
               }

               var3 = new BlockParsedResult(new DecodedInformation(this.current.getPosition(), this.buffer.toString()), true);
            }
         } else {
            if (this.isNumericToAlphaNumericLatch(this.current.getPosition())) {
               this.current.setAlpha();
               this.current.incrementPosition(4);
            }

            var3 = new BlockParsedResult(false);
         }

         return var3;
      }
   }

   String decodeAllCodes(StringBuilder var1, int var2) throws NotFoundException, FormatException {
      String var3 = null;

      while(true) {
         DecodedInformation var4 = this.decodeGeneralPurposeField(var2, var3);
         var3 = FieldParser.parseFieldsInGeneralPurpose(var4.getNewString());
         if (var3 != null) {
            var1.append(var3);
         }

         if (var4.isRemaining()) {
            var3 = String.valueOf(var4.getRemainingValue());
         } else {
            var3 = null;
         }

         if (var2 == var4.getNewPosition()) {
            return var1.toString();
         }

         var2 = var4.getNewPosition();
      }
   }

   DecodedInformation decodeGeneralPurposeField(int var1, String var2) throws FormatException {
      this.buffer.setLength(0);
      if (var2 != null) {
         this.buffer.append(var2);
      }

      this.current.setPosition(var1);
      DecodedInformation var3 = this.parseBlocks();
      if (var3 != null && var3.isRemaining()) {
         var3 = new DecodedInformation(this.current.getPosition(), this.buffer.toString(), var3.getRemainingValue());
      } else {
         var3 = new DecodedInformation(this.current.getPosition(), this.buffer.toString());
      }

      return var3;
   }

   int extractNumericValueFromBitArray(int var1, int var2) {
      return extractNumericValueFromBitArray(this.information, var1, var2);
   }
}

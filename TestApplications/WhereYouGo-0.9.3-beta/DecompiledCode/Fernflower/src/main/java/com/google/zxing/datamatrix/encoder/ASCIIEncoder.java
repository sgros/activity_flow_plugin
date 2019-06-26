package com.google.zxing.datamatrix.encoder;

final class ASCIIEncoder implements Encoder {
   private static char encodeASCIIDigits(char var0, char var1) {
      if (HighLevelEncoder.isDigit(var0) && HighLevelEncoder.isDigit(var1)) {
         return (char)((var0 - 48) * 10 + (var1 - 48) + 130);
      } else {
         throw new IllegalArgumentException("not digits: " + var0 + var1);
      }
   }

   public void encode(EncoderContext var1) {
      if (HighLevelEncoder.determineConsecutiveDigitCount(var1.getMessage(), var1.pos) >= 2) {
         var1.writeCodeword(encodeASCIIDigits(var1.getMessage().charAt(var1.pos), var1.getMessage().charAt(var1.pos + 1)));
         var1.pos += 2;
      } else {
         char var2 = var1.getCurrentChar();
         int var3 = HighLevelEncoder.lookAheadTest(var1.getMessage(), var1.pos, this.getEncodingMode());
         if (var3 != this.getEncodingMode()) {
            switch(var3) {
            case 1:
               var1.writeCodeword('æ');
               var1.signalEncoderChange(1);
               break;
            case 2:
               var1.writeCodeword('ï');
               var1.signalEncoderChange(2);
               break;
            case 3:
               var1.writeCodeword('î');
               var1.signalEncoderChange(3);
               break;
            case 4:
               var1.writeCodeword('ð');
               var1.signalEncoderChange(4);
               break;
            case 5:
               var1.writeCodeword('ç');
               var1.signalEncoderChange(5);
               break;
            default:
               throw new IllegalStateException("Illegal mode: " + var3);
            }
         } else if (HighLevelEncoder.isExtendedASCII(var2)) {
            var1.writeCodeword('ë');
            var1.writeCodeword((char)(var2 - 128 + 1));
            ++var1.pos;
         } else {
            var1.writeCodeword((char)(var2 + 1));
            ++var1.pos;
         }
      }

   }

   public int getEncodingMode() {
      return 0;
   }
}

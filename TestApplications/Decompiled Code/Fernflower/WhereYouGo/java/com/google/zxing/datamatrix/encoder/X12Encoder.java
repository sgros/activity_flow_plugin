package com.google.zxing.datamatrix.encoder;

final class X12Encoder extends C40Encoder {
   public void encode(EncoderContext var1) {
      StringBuilder var2 = new StringBuilder();

      while(var1.hasMoreCharacters()) {
         char var3 = var1.getCurrentChar();
         ++var1.pos;
         this.encodeChar(var3, var2);
         if (var2.length() % 3 == 0) {
            writeNextTriplet(var1, var2);
            int var4 = HighLevelEncoder.lookAheadTest(var1.getMessage(), var1.pos, this.getEncodingMode());
            if (var4 != this.getEncodingMode()) {
               var1.signalEncoderChange(var4);
               break;
            }
         }
      }

      this.handleEOD(var1, var2);
   }

   int encodeChar(char var1, StringBuilder var2) {
      if (var1 == '\r') {
         var2.append('\u0000');
      } else if (var1 == '*') {
         var2.append('\u0001');
      } else if (var1 == '>') {
         var2.append('\u0002');
      } else if (var1 == ' ') {
         var2.append('\u0003');
      } else if (var1 >= '0' && var1 <= '9') {
         var2.append((char)(var1 - 48 + 4));
      } else if (var1 >= 'A' && var1 <= 'Z') {
         var2.append((char)(var1 - 65 + 14));
      } else {
         HighLevelEncoder.illegalCharacter(var1);
      }

      return 1;
   }

   public int getEncodingMode() {
      return 3;
   }

   void handleEOD(EncoderContext var1, StringBuilder var2) {
      var1.updateSymbolInfo();
      int var3 = var1.getSymbolInfo().getDataCapacity() - var1.getCodewordCount();
      int var4 = var2.length();
      var1.pos -= var4;
      if (var1.getRemainingCharacters() > 1 || var3 > 1 || var1.getRemainingCharacters() != var3) {
         var1.writeCodeword('þ');
      }

      if (var1.getNewEncoding() < 0) {
         var1.signalEncoderChange(0);
      }

   }
}

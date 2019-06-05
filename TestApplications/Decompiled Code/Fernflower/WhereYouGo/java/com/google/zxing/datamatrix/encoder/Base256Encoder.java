package com.google.zxing.datamatrix.encoder;

final class Base256Encoder implements Encoder {
   private static char randomize255State(char var0, int var1) {
      int var3 = var0 + var1 * 149 % 255 + 1;
      char var2;
      if (var3 <= 255) {
         var0 = (char)var3;
         var2 = var0;
      } else {
         var0 = (char)(var3 - 256);
         var2 = var0;
      }

      return var2;
   }

   public void encode(EncoderContext var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append('\u0000');

      int var3;
      while(var1.hasMoreCharacters()) {
         var2.append(var1.getCurrentChar());
         ++var1.pos;
         var3 = HighLevelEncoder.lookAheadTest(var1.getMessage(), var1.pos, this.getEncodingMode());
         if (var3 != this.getEncodingMode()) {
            var1.signalEncoderChange(var3);
            break;
         }
      }

      int var4 = var2.length() - 1;
      var3 = var1.getCodewordCount() + var4 + 1;
      var1.updateSymbolInfo(var3);
      boolean var5;
      if (var1.getSymbolInfo().getDataCapacity() - var3 > 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var1.hasMoreCharacters() || var5) {
         if (var4 <= 249) {
            var2.setCharAt(0, (char)var4);
         } else {
            if (var4 > 1555) {
               throw new IllegalStateException("Message length not in valid ranges: " + var4);
            }

            var2.setCharAt(0, (char)(var4 / 250 + 249));
            var2.insert(1, (char)(var4 % 250));
         }
      }

      var3 = 0;

      for(var4 = var2.length(); var3 < var4; ++var3) {
         var1.writeCodeword(randomize255State(var2.charAt(var3), var1.getCodewordCount() + 1));
      }

   }

   public int getEncodingMode() {
      return 5;
   }
}

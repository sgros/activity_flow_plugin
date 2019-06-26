package com.google.zxing.datamatrix.encoder;

class C40Encoder implements Encoder {
   private int backtrackOneCharacter(EncoderContext var1, StringBuilder var2, StringBuilder var3, int var4) {
      int var5 = var2.length();
      var2.delete(var5 - var4, var5);
      --var1.pos;
      var4 = this.encodeChar(var1.getCurrentChar(), var3);
      var1.resetSymbolInfo();
      return var4;
   }

   private static String encodeToCodewords(CharSequence var0, int var1) {
      var1 = var0.charAt(var1) * 1600 + var0.charAt(var1 + 1) * 40 + var0.charAt(var1 + 2) + 1;
      return new String(new char[]{(char)(var1 / 256), (char)(var1 % 256)});
   }

   static void writeNextTriplet(EncoderContext var0, StringBuilder var1) {
      var0.writeCodewords(encodeToCodewords(var1, 0));
      var1.delete(0, 3);
   }

   public void encode(EncoderContext var1) {
      StringBuilder var2 = new StringBuilder();

      label46:
      while(var1.hasMoreCharacters()) {
         char var3 = var1.getCurrentChar();
         ++var1.pos;
         int var4 = this.encodeChar(var3, var2);
         int var5 = var2.length() / 3;
         var5 = var1.getCodewordCount() + (var5 << 1);
         var1.updateSymbolInfo(var5);
         int var6 = var1.getSymbolInfo().getDataCapacity() - var5;
         if (!var1.hasMoreCharacters()) {
            StringBuilder var7 = new StringBuilder();
            var5 = var4;
            if (var2.length() % 3 == 2) {
               label53: {
                  if (var6 >= 2) {
                     var5 = var4;
                     if (var6 <= 2) {
                        break label53;
                     }
                  }

                  var5 = this.backtrackOneCharacter(var1, var2, var7, var4);
               }
            }

            while(true) {
               if (var2.length() % 3 != 1 || (var5 > 3 || var6 == 1) && var5 <= 3) {
                  break label46;
               }

               var5 = this.backtrackOneCharacter(var1, var2, var7, var5);
            }
         }

         if (var2.length() % 3 == 0) {
            var5 = HighLevelEncoder.lookAheadTest(var1.getMessage(), var1.pos, this.getEncodingMode());
            if (var5 != this.getEncodingMode()) {
               var1.signalEncoderChange(var5);
               break;
            }
         }
      }

      this.handleEOD(var1, var2);
   }

   int encodeChar(char var1, StringBuilder var2) {
      int var3 = 1;
      if (var1 == ' ') {
         var2.append('\u0003');
      } else if (var1 >= '0' && var1 <= '9') {
         var2.append((char)(var1 - 48 + 4));
      } else if (var1 >= 'A' && var1 <= 'Z') {
         var2.append((char)(var1 - 65 + 14));
      } else if (var1 >= 0 && var1 <= 31) {
         var2.append('\u0000');
         var2.append(var1);
         var3 = 2;
      } else if (var1 >= '!' && var1 <= '/') {
         var2.append('\u0001');
         var2.append((char)(var1 - 33));
         var3 = 2;
      } else if (var1 >= ':' && var1 <= '@') {
         var2.append('\u0001');
         var2.append((char)(var1 - 58 + 15));
         var3 = 2;
      } else if (var1 >= '[' && var1 <= '_') {
         var2.append('\u0001');
         var2.append((char)(var1 - 91 + 22));
         var3 = 2;
      } else if (var1 >= '`' && var1 <= 127) {
         var2.append('\u0002');
         var2.append((char)(var1 - 96));
         var3 = 2;
      } else {
         if (var1 < 128) {
            throw new IllegalArgumentException("Illegal character: " + var1);
         }

         var2.append("\u0001\u001e");
         var3 = this.encodeChar((char)(var1 - 128), var2) + 2;
      }

      return var3;
   }

   public int getEncodingMode() {
      return 1;
   }

   void handleEOD(EncoderContext var1, StringBuilder var2) {
      int var3 = var2.length() / 3;
      int var4 = var2.length() % 3;
      var3 = var1.getCodewordCount() + (var3 << 1);
      var1.updateSymbolInfo(var3);
      var3 = var1.getSymbolInfo().getDataCapacity() - var3;
      if (var4 == 2) {
         var2.append('\u0000');

         while(var2.length() >= 3) {
            writeNextTriplet(var1, var2);
         }

         if (var1.hasMoreCharacters()) {
            var1.writeCodeword('þ');
         }
      } else if (var3 == 1 && var4 == 1) {
         while(var2.length() >= 3) {
            writeNextTriplet(var1, var2);
         }

         if (var1.hasMoreCharacters()) {
            var1.writeCodeword('þ');
         }

         --var1.pos;
      } else {
         if (var4 != 0) {
            throw new IllegalStateException("Unexpected case. Please report!");
         }

         while(var2.length() >= 3) {
            writeNextTriplet(var1, var2);
         }

         if (var3 > 0 || var1.hasMoreCharacters()) {
            var1.writeCodeword('þ');
         }
      }

      var1.signalEncoderChange(0);
   }
}

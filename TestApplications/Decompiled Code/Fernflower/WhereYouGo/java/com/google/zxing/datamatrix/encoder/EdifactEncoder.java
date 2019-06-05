package com.google.zxing.datamatrix.encoder;

final class EdifactEncoder implements Encoder {
   private static void encodeChar(char var0, StringBuilder var1) {
      if (var0 >= ' ' && var0 <= '?') {
         var1.append(var0);
      } else if (var0 >= '@' && var0 <= '^') {
         var1.append((char)(var0 - 64));
      } else {
         HighLevelEncoder.illegalCharacter(var0);
      }

   }

   private static String encodeToCodewords(CharSequence var0, int var1) {
      char var2 = 0;
      int var3 = var0.length() - var1;
      if (var3 == 0) {
         throw new IllegalStateException("StringBuilder must not be empty");
      } else {
         char var4 = var0.charAt(var1);
         char var5;
         if (var3 >= 2) {
            var5 = var0.charAt(var1 + 1);
         } else {
            var5 = 0;
         }

         char var6;
         if (var3 >= 3) {
            var6 = var0.charAt(var1 + 2);
         } else {
            var6 = 0;
         }

         if (var3 >= 4) {
            var2 = var0.charAt(var1 + 3);
         }

         var1 = (var4 << 18) + (var5 << 12) + (var6 << 6) + var2;
         char var7 = (char)(var1 >> 16 & 255);
         char var8 = (char)(var1 >> 8 & 255);
         char var9 = (char)(var1 & 255);
         StringBuilder var10 = new StringBuilder(3);
         var10.append(var7);
         if (var3 >= 2) {
            var10.append(var8);
         }

         if (var3 >= 3) {
            var10.append(var9);
         }

         return var10.toString();
      }
   }

   private static void handleEOD(EncoderContext var0, CharSequence var1) {
      boolean var2 = true;

      Throwable var10000;
      label992: {
         int var3;
         boolean var10001;
         try {
            var3 = var1.length();
         } catch (Throwable var96) {
            var10000 = var96;
            var10001 = false;
            break label992;
         }

         if (var3 == 0) {
            var0.signalEncoderChange(0);
            return;
         }

         if (var3 == 1) {
            int var4;
            int var5;
            int var6;
            try {
               var0.updateSymbolInfo();
               var4 = var0.getSymbolInfo().getDataCapacity();
               var5 = var0.getCodewordCount();
               var6 = var0.getRemainingCharacters();
            } catch (Throwable var95) {
               var10000 = var95;
               var10001 = false;
               break label992;
            }

            if (var6 == 0 && var4 - var5 <= 2) {
               var0.signalEncoderChange(0);
               return;
            }
         }

         if (var3 > 4) {
            label942:
            try {
               IllegalStateException var97 = new IllegalStateException("Count must not exceed 4");
               throw var97;
            } catch (Throwable var88) {
               var10000 = var88;
               var10001 = false;
               break label942;
            }
         } else {
            label1005: {
               --var3;

               String var98;
               boolean var100;
               label975: {
                  label974: {
                     try {
                        var98 = encodeToCodewords(var1, 0);
                        if (!var0.hasMoreCharacters()) {
                           break label974;
                        }
                     } catch (Throwable var94) {
                        var10000 = var94;
                        var10001 = false;
                        break label1005;
                     }

                     var100 = false;
                     break label975;
                  }

                  var100 = true;
               }

               if (var100 && var3 <= 2) {
                  var100 = var2;
               } else {
                  var100 = false;
               }

               var2 = var100;
               if (var3 <= 2) {
                  label994: {
                     try {
                        var0.updateSymbolInfo(var0.getCodewordCount() + var3);
                     } catch (Throwable var92) {
                        var10000 = var92;
                        var10001 = false;
                        break label1005;
                     }

                     var2 = var100;

                     try {
                        if (var0.getSymbolInfo().getDataCapacity() - var0.getCodewordCount() < 3) {
                           break label994;
                        }
                     } catch (Throwable var93) {
                        var10000 = var93;
                        var10001 = false;
                        break label1005;
                     }

                     var2 = false;

                     try {
                        var0.updateSymbolInfo(var0.getCodewordCount() + var98.length());
                     } catch (Throwable var91) {
                        var10000 = var91;
                        var10001 = false;
                        break label1005;
                     }
                  }
               }

               if (var2) {
                  try {
                     var0.resetSymbolInfo();
                     var0.pos -= var3;
                  } catch (Throwable var90) {
                     var10000 = var90;
                     var10001 = false;
                     break label1005;
                  }
               } else {
                  try {
                     var0.writeCodewords(var98);
                  } catch (Throwable var89) {
                     var10000 = var89;
                     var10001 = false;
                     break label1005;
                  }
               }

               var0.signalEncoderChange(0);
               return;
            }
         }
      }

      Throwable var99 = var10000;
      var0.signalEncoderChange(0);
      throw var99;
   }

   public void encode(EncoderContext var1) {
      StringBuilder var2 = new StringBuilder();

      while(var1.hasMoreCharacters()) {
         encodeChar(var1.getCurrentChar(), var2);
         ++var1.pos;
         if (var2.length() >= 4) {
            var1.writeCodewords(encodeToCodewords(var2, 0));
            var2.delete(0, 4);
            if (HighLevelEncoder.lookAheadTest(var1.getMessage(), var1.pos, this.getEncodingMode()) != this.getEncodingMode()) {
               var1.signalEncoderChange(0);
               break;
            }
         }
      }

      var2.append('\u001f');
      handleEOD(var1, var2);
   }

   public int getEncodingMode() {
      return 4;
   }
}

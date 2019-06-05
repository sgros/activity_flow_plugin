package com.google.zxing.qrcode.encoder;

final class MaskUtil {
   private static final int N1 = 3;
   private static final int N2 = 3;
   private static final int N3 = 40;
   private static final int N4 = 10;

   private MaskUtil() {
   }

   static int applyMaskPenaltyRule1(ByteMatrix var0) {
      return applyMaskPenaltyRule1Internal(var0, true) + applyMaskPenaltyRule1Internal(var0, false);
   }

   private static int applyMaskPenaltyRule1Internal(ByteMatrix var0, boolean var1) {
      int var2 = 0;
      int var3;
      if (var1) {
         var3 = var0.getHeight();
      } else {
         var3 = var0.getWidth();
      }

      int var4;
      if (var1) {
         var4 = var0.getWidth();
      } else {
         var4 = var0.getHeight();
      }

      byte[][] var10 = var0.getArray();

      int var12;
      for(int var5 = 0; var5 < var3; var2 = var12) {
         int var6 = 0;
         byte var7 = -1;

         int var13;
         for(int var8 = 0; var8 < var4; var2 = var13) {
            byte var9;
            if (var1) {
               var9 = var10[var5][var8];
            } else {
               var9 = var10[var8][var5];
            }

            if (var9 == var7) {
               ++var6;
               var13 = var2;
               var2 = var6;
            } else {
               var12 = var2;
               if (var6 >= 5) {
                  var12 = var2 + var6 - 5 + 3;
               }

               var2 = 1;
               var13 = var12;
               var7 = var9;
            }

            ++var8;
            var6 = var2;
         }

         var12 = var2;
         if (var6 >= 5) {
            var12 = var2 + var6 - 5 + 3;
         }

         ++var5;
      }

      return var2;
   }

   static int applyMaskPenaltyRule2(ByteMatrix var0) {
      int var1 = 0;
      byte[][] var2 = var0.getArray();
      int var3 = var0.getWidth();
      int var4 = var0.getHeight();

      int var8;
      for(int var5 = 0; var5 < var4 - 1; ++var5) {
         for(int var6 = 0; var6 < var3 - 1; var1 = var8) {
            byte var7 = var2[var5][var6];
            var8 = var1;
            if (var7 == var2[var5][var6 + 1]) {
               var8 = var1;
               if (var7 == var2[var5 + 1][var6]) {
                  var8 = var1;
                  if (var7 == var2[var5 + 1][var6 + 1]) {
                     var8 = var1 + 1;
                  }
               }
            }

            ++var6;
         }
      }

      return var1 * 3;
   }

   static int applyMaskPenaltyRule3(ByteMatrix var0) {
      int var1 = 0;
      byte[][] var2 = var0.getArray();
      int var3 = var0.getWidth();
      int var4 = var0.getHeight();

      for(int var5 = 0; var5 < var4; ++var5) {
         for(int var6 = 0; var6 < var3; ++var6) {
            byte[] var8 = var2[var5];
            int var7 = var1;
            if (var6 + 6 < var3) {
               var7 = var1;
               if (var8[var6] == 1) {
                  var7 = var1;
                  if (var8[var6 + 1] == 0) {
                     var7 = var1;
                     if (var8[var6 + 2] == 1) {
                        var7 = var1;
                        if (var8[var6 + 3] == 1) {
                           var7 = var1;
                           if (var8[var6 + 4] == 1) {
                              var7 = var1;
                              if (var8[var6 + 5] == 0) {
                                 var7 = var1;
                                 if (var8[var6 + 6] == 1) {
                                    label96: {
                                       if (!isWhiteHorizontal(var8, var6 - 4, var6)) {
                                          var7 = var1;
                                          if (!isWhiteHorizontal(var8, var6 + 7, var6 + 11)) {
                                             break label96;
                                          }
                                       }

                                       var7 = var1 + 1;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            var1 = var7;
            if (var5 + 6 < var4) {
               var1 = var7;
               if (var2[var5][var6] == 1) {
                  var1 = var7;
                  if (var2[var5 + 1][var6] == 0) {
                     var1 = var7;
                     if (var2[var5 + 2][var6] == 1) {
                        var1 = var7;
                        if (var2[var5 + 3][var6] == 1) {
                           var1 = var7;
                           if (var2[var5 + 4][var6] == 1) {
                              var1 = var7;
                              if (var2[var5 + 5][var6] == 0) {
                                 var1 = var7;
                                 if (var2[var5 + 6][var6] == 1) {
                                    if (!isWhiteVertical(var2, var6, var5 - 4, var5)) {
                                       var1 = var7;
                                       if (!isWhiteVertical(var2, var6, var5 + 7, var5 + 11)) {
                                          continue;
                                       }
                                    }

                                    var1 = var7 + 1;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var1 * 40;
   }

   static int applyMaskPenaltyRule4(ByteMatrix var0) {
      int var1 = 0;
      byte[][] var2 = var0.getArray();
      int var3 = var0.getWidth();
      int var4 = var0.getHeight();

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         byte[] var6 = var2[var5];

         int var8;
         for(int var7 = 0; var7 < var3; var1 = var8) {
            var8 = var1;
            if (var6[var7] == 1) {
               var8 = var1 + 1;
            }

            ++var7;
         }
      }

      var5 = var0.getHeight() * var0.getWidth();
      return Math.abs((var1 << 1) - var5) * 10 / var5 * 10;
   }

   static boolean getDataMaskBit(int var0, int var1, int var2) {
      switch(var0) {
      case 0:
         var0 = var2 + var1 & 1;
         break;
      case 1:
         var0 = var2 & 1;
         break;
      case 2:
         var0 = var1 % 3;
         break;
      case 3:
         var0 = (var2 + var1) % 3;
         break;
      case 4:
         var0 = var2 / 2 + var1 / 3 & 1;
         break;
      case 5:
         var0 = var2 * var1;
         var0 = (var0 & 1) + var0 % 3;
         break;
      case 6:
         var0 = var2 * var1;
         var0 = (var0 & 1) + var0 % 3 & 1;
         break;
      case 7:
         var0 = var2 * var1 % 3 + (var2 + var1 & 1) & 1;
         break;
      default:
         throw new IllegalArgumentException("Invalid mask pattern: " + var0);
      }

      boolean var3;
      if (var0 == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   private static boolean isWhiteHorizontal(byte[] var0, int var1, int var2) {
      boolean var3 = false;
      var1 = Math.max(var1, 0);
      var2 = Math.min(var2, var0.length);

      while(true) {
         if (var1 >= var2) {
            var3 = true;
            break;
         }

         if (var0[var1] == 1) {
            break;
         }

         ++var1;
      }

      return var3;
   }

   private static boolean isWhiteVertical(byte[][] var0, int var1, int var2, int var3) {
      boolean var4 = false;
      var2 = Math.max(var2, 0);
      var3 = Math.min(var3, var0.length);

      while(true) {
         if (var2 >= var3) {
            var4 = true;
            break;
         }

         if (var0[var2][var1] == 1) {
            break;
         }

         ++var2;
      }

      return var4;
   }
}

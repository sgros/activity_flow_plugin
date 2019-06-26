package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.ArrayList;
import java.util.Map;

public final class Code128Reader extends OneDReader {
   private static final int CODE_CODE_A = 101;
   private static final int CODE_CODE_B = 100;
   private static final int CODE_CODE_C = 99;
   private static final int CODE_FNC_1 = 102;
   private static final int CODE_FNC_2 = 97;
   private static final int CODE_FNC_3 = 96;
   private static final int CODE_FNC_4_A = 101;
   private static final int CODE_FNC_4_B = 100;
   static final int[][] CODE_PATTERNS;
   private static final int CODE_SHIFT = 98;
   private static final int CODE_START_A = 103;
   private static final int CODE_START_B = 104;
   private static final int CODE_START_C = 105;
   private static final int CODE_STOP = 106;
   private static final float MAX_AVG_VARIANCE = 0.25F;
   private static final float MAX_INDIVIDUAL_VARIANCE = 0.7F;

   static {
      int[] var0 = new int[]{2, 2, 2, 2, 2, 1};
      int[] var1 = new int[]{1, 1, 2, 2, 3, 2};
      int[] var2 = new int[]{3, 1, 2, 1, 3, 1};
      int[] var3 = new int[]{3, 1, 1, 2, 2, 2};
      int[] var4 = new int[]{2, 1, 1, 3, 1, 3};
      int[] var5 = new int[]{2, 3, 1, 1, 1, 3};
      int[] var6 = new int[]{3, 1, 2, 3, 1, 1};
      int[] var7 = new int[]{2, 2, 1, 4, 1, 1};
      int[] var8 = new int[]{1, 4, 1, 2, 2, 1};
      int[] var9 = new int[]{1, 1, 2, 2, 1, 4};
      int[] var10 = new int[]{1, 1, 2, 4, 1, 2};
      int[] var11 = new int[]{1, 2, 2, 1, 1, 4};
      int[] var12 = new int[]{1, 2, 2, 4, 1, 1};
      int[] var13 = new int[]{4, 1, 3, 1, 1, 1};
      int[] var14 = new int[]{1, 3, 4, 1, 1, 1};
      int[] var15 = new int[]{1, 1, 1, 1, 4, 3};
      int[] var16 = new int[]{3, 1, 1, 1, 4, 1};
      int[] var17 = new int[]{4, 1, 1, 1, 3, 1};
      int[] var18 = new int[]{2, 1, 1, 4, 1, 2};
      CODE_PATTERNS = new int[][]{{2, 1, 2, 2, 2, 2}, {2, 2, 2, 1, 2, 2}, var0, {1, 2, 1, 2, 2, 3}, {1, 2, 1, 3, 2, 2}, {1, 3, 1, 2, 2, 2}, {1, 2, 2, 2, 1, 3}, {1, 2, 2, 3, 1, 2}, {1, 3, 2, 2, 1, 2}, {2, 2, 1, 2, 1, 3}, {2, 2, 1, 3, 1, 2}, {2, 3, 1, 2, 1, 2}, var1, {1, 2, 2, 1, 3, 2}, {1, 2, 2, 2, 3, 1}, {1, 1, 3, 2, 2, 2}, {1, 2, 3, 1, 2, 2}, {1, 2, 3, 2, 2, 1}, {2, 2, 3, 2, 1, 1}, {2, 2, 1, 1, 3, 2}, {2, 2, 1, 2, 3, 1}, {2, 1, 3, 2, 1, 2}, {2, 2, 3, 1, 1, 2}, var2, var3, {3, 2, 1, 1, 2, 2}, {3, 2, 1, 2, 2, 1}, {3, 1, 2, 2, 1, 2}, {3, 2, 2, 1, 1, 2}, {3, 2, 2, 2, 1, 1}, {2, 1, 2, 1, 2, 3}, {2, 1, 2, 3, 2, 1}, {2, 3, 2, 1, 2, 1}, {1, 1, 1, 3, 2, 3}, {1, 3, 1, 1, 2, 3}, {1, 3, 1, 3, 2, 1}, {1, 1, 2, 3, 1, 3}, {1, 3, 2, 1, 1, 3}, {1, 3, 2, 3, 1, 1}, var4, var5, {2, 3, 1, 3, 1, 1}, {1, 1, 2, 1, 3, 3}, {1, 1, 2, 3, 3, 1}, {1, 3, 2, 1, 3, 1}, {1, 1, 3, 1, 2, 3}, {1, 1, 3, 3, 2, 1}, {1, 3, 3, 1, 2, 1}, {3, 1, 3, 1, 2, 1}, {2, 1, 1, 3, 3, 1}, {2, 3, 1, 1, 3, 1}, {2, 1, 3, 1, 1, 3}, {2, 1, 3, 3, 1, 1}, {2, 1, 3, 1, 3, 1}, {3, 1, 1, 1, 2, 3}, {3, 1, 1, 3, 2, 1}, {3, 3, 1, 1, 2, 1}, {3, 1, 2, 1, 1, 3}, var6, {3, 3, 2, 1, 1, 1}, {3, 1, 4, 1, 1, 1}, var7, {4, 3, 1, 1, 1, 1}, {1, 1, 1, 2, 2, 4}, {1, 1, 1, 4, 2, 2}, {1, 2, 1, 1, 2, 4}, {1, 2, 1, 4, 2, 1}, {1, 4, 1, 1, 2, 2}, var8, var9, var10, var11, var12, {1, 4, 2, 1, 1, 2}, {1, 4, 2, 2, 1, 1}, {2, 4, 1, 2, 1, 1}, {2, 2, 1, 1, 1, 4}, var13, {2, 4, 1, 1, 1, 2}, var14, {1, 1, 1, 2, 4, 2}, {1, 2, 1, 1, 4, 2}, {1, 2, 1, 2, 4, 1}, {1, 1, 4, 2, 1, 2}, {1, 2, 4, 1, 1, 2}, {1, 2, 4, 2, 1, 1}, {4, 1, 1, 2, 1, 2}, {4, 2, 1, 1, 1, 2}, {4, 2, 1, 2, 1, 1}, {2, 1, 2, 1, 4, 1}, {2, 1, 4, 1, 2, 1}, {4, 1, 2, 1, 2, 1}, var15, {1, 1, 1, 3, 4, 1}, {1, 3, 1, 1, 4, 1}, {1, 1, 4, 1, 1, 3}, {1, 1, 4, 3, 1, 1}, {4, 1, 1, 1, 1, 3}, {4, 1, 1, 3, 1, 1}, {1, 1, 3, 1, 4, 1}, {1, 1, 4, 1, 3, 1}, var16, var17, var18, {2, 1, 1, 2, 1, 4}, {2, 1, 1, 2, 3, 2}, {2, 3, 3, 1, 1, 1, 2}};
   }

   private static int decodeCode(BitArray var0, int[] var1, int var2) throws NotFoundException {
      recordPattern(var0, var2, var1);
      float var3 = 0.25F;
      int var4 = -1;

      float var6;
      for(var2 = 0; var2 < CODE_PATTERNS.length; var3 = var6) {
         float var5 = patternMatchVariance(var1, CODE_PATTERNS[var2], 0.7F);
         var6 = var3;
         if (var5 < var3) {
            var6 = var5;
            var4 = var2;
         }

         ++var2;
      }

      if (var4 >= 0) {
         return var4;
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private static int[] findStartPattern(BitArray var0) throws NotFoundException {
      int var1 = var0.getSize();
      int var2 = var0.getNextSet(0);
      int var3 = 0;
      int[] var4 = new int[6];
      int var5 = var2;

      int var7;
      for(boolean var6 = false; var2 < var1; var3 = var7) {
         if (var0.get(var2) ^ var6) {
            int var10002 = var4[var3]++;
            var7 = var3;
         } else {
            if (var3 == 5) {
               float var8 = 0.25F;
               int var9 = -1;

               float var11;
               for(var7 = 103; var7 <= 105; var8 = var11) {
                  float var10 = patternMatchVariance(var4, CODE_PATTERNS[var7], 0.7F);
                  var11 = var8;
                  if (var10 < var8) {
                     var11 = var10;
                     var9 = var7;
                  }

                  ++var7;
               }

               if (var9 >= 0 && var0.isRange(Math.max(0, var5 - (var2 - var5) / 2), var5, false)) {
                  return new int[]{var5, var2, var9};
               }

               var5 += var4[0] + var4[1];
               System.arraycopy(var4, 2, var4, 0, 4);
               var4[4] = 0;
               var4[5] = 0;
               var7 = var3 - 1;
            } else {
               var7 = var3 + 1;
            }

            var4[var7] = 1;
            if (!var6) {
               var6 = true;
            } else {
               var6 = false;
            }
         }

         ++var2;
      }

      throw NotFoundException.getNotFoundInstance();
   }

   public Result decodeRow(int var1, BitArray var2, Map var3) throws NotFoundException, FormatException, ChecksumException {
      boolean var4;
      if (var3 != null && var3.containsKey(DecodeHintType.ASSUME_GS1)) {
         var4 = true;
      } else {
         var4 = false;
      }

      int[] var5 = findStartPattern(var2);
      int var6 = var5[2];
      ArrayList var7 = new ArrayList(20);
      var7.add((byte)var6);
      byte var8;
      switch(var6) {
      case 103:
         var8 = 101;
         break;
      case 104:
         var8 = 100;
         break;
      case 105:
         var8 = 99;
         break;
      default:
         throw FormatException.getFormatInstance();
      }

      boolean var9 = false;
      boolean var10 = false;
      StringBuilder var37 = new StringBuilder(20);
      int var11 = var5[0];
      int var12 = var5[1];
      int[] var13 = new int[6];
      int var14 = 0;
      int var15 = 0;
      int var16 = 0;
      int var17 = 1;
      boolean var18 = false;
      boolean var19 = false;
      byte var20 = var8;

      while(true) {
         boolean var21 = var10;
         int var41;
         if (var9) {
            var41 = var2.getNextUnset(var12);
            if (!var2.isRange(var41, Math.min(var2.getSize(), (var41 - var11) / 2 + var41), false)) {
               throw NotFoundException.getNotFoundInstance();
            }

            if ((var6 - var16 * var14) % 103 != var14) {
               throw ChecksumException.getChecksumInstance();
            }

            var41 = var37.length();
            if (var41 == 0) {
               throw NotFoundException.getNotFoundInstance();
            }

            if (var41 > 0 && var17 != 0) {
               if (var20 == 99) {
                  var37.delete(var41 - 2, var41);
               } else {
                  var37.delete(var41 - 1, var41);
               }
            }

            float var33 = (float)(var5[1] + var5[0]) / 2.0F;
            float var34 = (float)var11;
            float var35 = (float)(var12 - var11) / 2.0F;
            int var44 = var7.size();
            byte[] var36 = new byte[var44];

            for(var41 = 0; var41 < var44; ++var41) {
               var36[var41] = (Byte)var7.get(var41);
            }

            String var40 = var37.toString();
            ResultPoint var39 = new ResultPoint(var33, (float)var1);
            ResultPoint var43 = new ResultPoint(var34 + var35, (float)var1);
            BarcodeFormat var38 = BarcodeFormat.CODE_128;
            return new Result(var40, var36, new ResultPoint[]{var39, var43}, var38);
         }

         var10 = false;
         int var22 = var15;
         int var23 = decodeCode(var2, var13, var12);
         var7.add((byte)var23);
         int var24 = var17;
         if (var23 != 106) {
            var24 = 1;
         }

         int var25 = var6;
         int var26 = var16;
         if (var23 != 106) {
            var26 = var16 + 1;
            var25 = var6 + var26 * var23;
         }

         int var27 = var12;
         var41 = 0;

         int var28;
         for(var28 = var12; var41 < 6; ++var41) {
            var28 += var13[var41];
         }

         switch(var23) {
         case 103:
         case 104:
         case 105:
            throw FormatException.getFormatInstance();
         }

         boolean var29;
         boolean var30;
         boolean var31;
         byte var32;
         boolean var42;
         label197:
         switch(var20) {
         case 99:
            if (var23 < 100) {
               if (var23 < 10) {
                  var37.append('0');
               }

               var37.append(var23);
               var32 = var20;
               var31 = var9;
               var30 = var10;
               var42 = var19;
               var29 = var18;
            } else {
               var12 = var24;
               if (var23 != 106) {
                  var12 = 0;
               }

               switch(var23) {
               case 100:
                  var32 = 100;
                  var31 = var9;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  break label197;
               case 101:
                  var32 = 101;
                  var31 = var9;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  break label197;
               case 102:
                  var32 = var20;
                  var31 = var9;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  if (var4) {
                     if (var37.length() == 0) {
                        var37.append("]C1");
                        var32 = var20;
                        var31 = var9;
                        var30 = var10;
                        var24 = var12;
                        var42 = var19;
                        var29 = var18;
                     } else {
                        var37.append('\u001d');
                        var32 = var20;
                        var31 = var9;
                        var30 = var10;
                        var24 = var12;
                        var42 = var19;
                        var29 = var18;
                     }
                  }
                  break label197;
               case 103:
               case 104:
               case 105:
               default:
                  var32 = var20;
                  var31 = var9;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  break label197;
               case 106:
                  var31 = true;
                  var32 = var20;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
               }
            }
            break;
         case 100:
            if (var23 < 96) {
               if (var19 == var18) {
                  var37.append((char)(var23 + 32));
               } else {
                  var37.append((char)(var23 + 32 + 128));
               }

               var42 = false;
               var32 = var20;
               var31 = var9;
               var30 = var10;
               var29 = var18;
            } else {
               var12 = var24;
               if (var23 != 106) {
                  var12 = 0;
               }

               var32 = var20;
               var31 = var9;
               var30 = var10;
               var24 = var12;
               var42 = var19;
               var29 = var18;
               switch(var23) {
               case 96:
               case 97:
                  break label197;
               case 98:
                  var30 = true;
                  var32 = 101;
                  var31 = var9;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  break label197;
               case 99:
                  var32 = 99;
                  var31 = var9;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  break label197;
               case 100:
                  if (!var18 && var19) {
                     var29 = true;
                     var42 = false;
                     var32 = var20;
                     var31 = var9;
                     var30 = var10;
                     var24 = var12;
                  } else {
                     if (var18 && var19) {
                        var29 = false;
                        var42 = false;
                        var32 = var20;
                        var31 = var9;
                        var30 = var10;
                        var24 = var12;
                        break label197;
                     }

                     var42 = true;
                     var32 = var20;
                     var31 = var9;
                     var30 = var10;
                     var24 = var12;
                     var29 = var18;
                  }
                  break label197;
               case 101:
                  var32 = 101;
                  var31 = var9;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  break label197;
               case 102:
                  var32 = var20;
                  var31 = var9;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  if (var4) {
                     if (var37.length() == 0) {
                        var37.append("]C1");
                        var32 = var20;
                        var31 = var9;
                        var30 = var10;
                        var24 = var12;
                        var42 = var19;
                        var29 = var18;
                     } else {
                        var37.append('\u001d');
                        var32 = var20;
                        var31 = var9;
                        var30 = var10;
                        var24 = var12;
                        var42 = var19;
                        var29 = var18;
                     }
                  }
                  break label197;
               case 103:
               case 104:
               case 105:
               default:
                  var32 = var20;
                  var31 = var9;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  break label197;
               case 106:
                  var31 = true;
                  var32 = var20;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
               }
            }
            break;
         case 101:
            if (var23 < 64) {
               if (var19 == var18) {
                  var37.append((char)(var23 + 32));
               } else {
                  var37.append((char)(var23 + 32 + 128));
               }

               var42 = false;
               var32 = var20;
               var31 = var9;
               var30 = var10;
               var29 = var18;
            } else if (var23 < 96) {
               if (var19 == var18) {
                  var37.append((char)(var23 - 64));
               } else {
                  var37.append((char)(var23 + 64));
               }

               var42 = false;
               var32 = var20;
               var31 = var9;
               var30 = var10;
               var29 = var18;
            } else {
               var12 = var24;
               if (var23 != 106) {
                  var12 = 0;
               }

               var32 = var20;
               var31 = var9;
               var30 = var10;
               var24 = var12;
               var42 = var19;
               var29 = var18;
               switch(var23) {
               case 96:
               case 97:
                  break label197;
               case 98:
                  var30 = true;
                  var32 = 100;
                  var31 = var9;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  break label197;
               case 99:
                  var32 = 99;
                  var31 = var9;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  break label197;
               case 100:
                  var32 = 100;
                  var31 = var9;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  break label197;
               case 101:
                  if (!var18 && var19) {
                     var29 = true;
                     var42 = false;
                     var32 = var20;
                     var31 = var9;
                     var30 = var10;
                     var24 = var12;
                  } else {
                     if (var18 && var19) {
                        var29 = false;
                        var42 = false;
                        var32 = var20;
                        var31 = var9;
                        var30 = var10;
                        var24 = var12;
                        break label197;
                     }

                     var42 = true;
                     var32 = var20;
                     var31 = var9;
                     var30 = var10;
                     var24 = var12;
                     var29 = var18;
                  }
                  break label197;
               case 102:
                  var32 = var20;
                  var31 = var9;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  if (var4) {
                     if (var37.length() == 0) {
                        var37.append("]C1");
                        var32 = var20;
                        var31 = var9;
                        var30 = var10;
                        var24 = var12;
                        var42 = var19;
                        var29 = var18;
                     } else {
                        var37.append('\u001d');
                        var32 = var20;
                        var31 = var9;
                        var30 = var10;
                        var24 = var12;
                        var42 = var19;
                        var29 = var18;
                     }
                  }
                  break label197;
               case 103:
               case 104:
               case 105:
               default:
                  var32 = var20;
                  var31 = var9;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
                  break label197;
               case 106:
                  var31 = true;
                  var32 = var20;
                  var30 = var10;
                  var24 = var12;
                  var42 = var19;
                  var29 = var18;
               }
            }
            break;
         default:
            var29 = var18;
            var42 = var19;
            var30 = var10;
            var31 = var9;
            var32 = var20;
         }

         var6 = var25;
         var15 = var23;
         var20 = var32;
         var9 = var31;
         var10 = var30;
         var17 = var24;
         var14 = var22;
         var11 = var12;
         var16 = var26;
         var12 = var28;
         var19 = var42;
         var18 = var29;
         if (var21) {
            if (var32 == 101) {
               var20 = 100;
            } else {
               var20 = 101;
            }

            var6 = var25;
            var15 = var23;
            var9 = var31;
            var10 = var30;
            var17 = var24;
            var14 = var22;
            var11 = var27;
            var16 = var26;
            var12 = var28;
            var19 = var42;
            var18 = var29;
         }
      }
   }
}

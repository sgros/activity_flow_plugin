package com.google.zxing.oned.rss;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.detector.MathUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class RSS14Reader extends AbstractRSSReader {
   private static final int[][] FINDER_PATTERNS;
   private static final int[] INSIDE_GSUM = new int[]{0, 336, 1036, 1516};
   private static final int[] INSIDE_ODD_TOTAL_SUBSET = new int[]{4, 20, 48, 81};
   private static final int[] INSIDE_ODD_WIDEST = new int[]{2, 4, 6, 8};
   private static final int[] OUTSIDE_EVEN_TOTAL_SUBSET = new int[]{1, 10, 34, 70, 126};
   private static final int[] OUTSIDE_GSUM = new int[]{0, 161, 961, 2015, 2715};
   private static final int[] OUTSIDE_ODD_WIDEST = new int[]{8, 6, 4, 3, 1};
   private final List possibleLeftPairs = new ArrayList();
   private final List possibleRightPairs = new ArrayList();

   static {
      int[] var0 = new int[]{3, 8, 2, 1};
      int[] var1 = new int[]{3, 5, 5, 1};
      int[] var2 = new int[]{3, 3, 7, 1};
      int[] var3 = new int[]{2, 5, 6, 1};
      int[] var4 = new int[]{2, 3, 8, 1};
      int[] var5 = new int[]{1, 3, 9, 1};
      FINDER_PATTERNS = new int[][]{var0, var1, var2, {3, 1, 9, 1}, {2, 7, 4, 1}, var3, var4, {1, 5, 7, 1}, var5};
   }

   private static void addOrTally(Collection var0, Pair var1) {
      if (var1 != null) {
         boolean var2 = false;
         Iterator var3 = var0.iterator();

         boolean var4;
         while(true) {
            var4 = var2;
            if (!var3.hasNext()) {
               break;
            }

            Pair var5 = (Pair)var3.next();
            if (var5.getValue() == var1.getValue()) {
               var5.incrementCount();
               var4 = true;
               break;
            }
         }

         if (!var4) {
            var0.add(var1);
         }
      }

   }

   private void adjustOddEvenCounts(boolean var1, int var2) throws NotFoundException {
      boolean var3 = false;
      int var4 = MathUtils.sum(this.getOddCounts());
      int var5 = MathUtils.sum(this.getEvenCounts());
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      boolean var10 = false;
      boolean var11 = false;
      boolean var12;
      if (var1) {
         if (var4 > 12) {
            var12 = true;
         } else {
            var12 = var9;
            if (var4 < 4) {
               var7 = true;
               var12 = var9;
            }
         }

         if (var5 > 12) {
            var9 = true;
            var8 = var7;
            var6 = var12;
         } else {
            var9 = var11;
            var6 = var12;
            var8 = var7;
            if (var5 < 4) {
               var10 = true;
               var9 = var11;
               var6 = var12;
               var8 = var7;
            }
         }
      } else {
         if (var4 > 11) {
            var12 = true;
            var7 = var6;
         } else {
            var12 = var8;
            var7 = var6;
            if (var4 < 5) {
               var7 = true;
               var12 = var8;
            }
         }

         if (var5 > 10) {
            var9 = true;
            var6 = var12;
            var8 = var7;
         } else {
            var9 = var11;
            var6 = var12;
            var8 = var7;
            if (var5 < 4) {
               var10 = true;
               var9 = var11;
               var6 = var12;
               var8 = var7;
            }
         }
      }

      int var15 = var4 + var5 - var2;
      byte var13;
      if (var1) {
         var13 = 1;
      } else {
         var13 = 0;
      }

      if ((var4 & 1) == var13) {
         var12 = true;
      } else {
         var12 = false;
      }

      boolean var14 = var3;
      if ((var5 & 1) == 1) {
         var14 = true;
      }

      if (var15 == 1) {
         if (var12) {
            if (var14) {
               throw NotFoundException.getNotFoundInstance();
            }

            var6 = true;
         } else {
            if (!var14) {
               throw NotFoundException.getNotFoundInstance();
            }

            var9 = true;
         }
      } else if (var15 == -1) {
         if (var12) {
            if (var14) {
               throw NotFoundException.getNotFoundInstance();
            }

            var8 = true;
         } else {
            if (!var14) {
               throw NotFoundException.getNotFoundInstance();
            }

            var10 = true;
         }
      } else {
         if (var15 != 0) {
            throw NotFoundException.getNotFoundInstance();
         }

         if (var12) {
            if (!var14) {
               throw NotFoundException.getNotFoundInstance();
            }

            if (var4 < var5) {
               var8 = true;
               var9 = true;
            } else {
               var6 = true;
               var10 = true;
            }
         } else if (var14) {
            throw NotFoundException.getNotFoundInstance();
         }
      }

      if (var8) {
         if (var6) {
            throw NotFoundException.getNotFoundInstance();
         }

         increment(this.getOddCounts(), this.getOddRoundingErrors());
      }

      if (var6) {
         decrement(this.getOddCounts(), this.getOddRoundingErrors());
      }

      if (var10) {
         if (var9) {
            throw NotFoundException.getNotFoundInstance();
         }

         increment(this.getEvenCounts(), this.getOddRoundingErrors());
      }

      if (var9) {
         decrement(this.getEvenCounts(), this.getEvenRoundingErrors());
      }

   }

   private static boolean checkChecksum(Pair var0, Pair var1) {
      int var2 = var0.getChecksumPortion();
      int var3 = var1.getChecksumPortion();
      int var4 = var0.getFinderPattern().getValue() * 9 + var1.getFinderPattern().getValue();
      int var5 = var4;
      if (var4 > 72) {
         var5 = var4 - 1;
      }

      var4 = var5;
      if (var5 > 8) {
         var4 = var5 - 1;
      }

      boolean var6;
      if ((var2 + var3 * 16) % 79 == var4) {
         var6 = true;
      } else {
         var6 = false;
      }

      return var6;
   }

   private static Result constructResult(Pair var0, Pair var1) {
      String var2 = String.valueOf(4537077L * (long)var0.getValue() + (long)var1.getValue());
      StringBuilder var3 = new StringBuilder(14);

      int var4;
      for(var4 = 13 - var2.length(); var4 > 0; --var4) {
         var3.append('0');
      }

      var3.append(var2);
      int var5 = 0;

      for(var4 = 0; var4 < 13; ++var4) {
         int var6 = var3.charAt(var4) - 48;
         int var7 = var6;
         if ((var4 & 1) == 0) {
            var7 = var6 * 3;
         }

         var5 += var7;
      }

      var5 = 10 - var5 % 10;
      var4 = var5;
      if (var5 == 10) {
         var4 = 0;
      }

      var3.append(var4);
      ResultPoint[] var12 = var0.getFinderPattern().getResultPoints();
      ResultPoint[] var8 = var1.getFinderPattern().getResultPoints();
      String var11 = String.valueOf(var3.toString());
      ResultPoint var10 = var12[0];
      ResultPoint var13 = var12[1];
      ResultPoint var14 = var8[0];
      ResultPoint var15 = var8[1];
      BarcodeFormat var9 = BarcodeFormat.RSS_14;
      return new Result(var11, (byte[])null, new ResultPoint[]{var10, var13, var14, var15}, var9);
   }

   private DataCharacter decodeDataCharacter(BitArray var1, FinderPattern var2, boolean var3) throws NotFoundException {
      int[] var4 = this.getDataCharacterCounters();
      var4[0] = 0;
      var4[1] = 0;
      var4[2] = 0;
      var4[3] = 0;
      var4[4] = 0;
      var4[5] = 0;
      var4[6] = 0;
      var4[7] = 0;
      int var5;
      int var9;
      int var12;
      if (var3) {
         recordPatternInReverse(var1, var2.getStartEnd()[0], var4);
      } else {
         recordPattern(var1, var2.getStartEnd()[1] + 1, var4);
         var5 = 0;

         for(var12 = var4.length - 1; var5 < var12; --var12) {
            var9 = var4[var5];
            var4[var5] = var4[var12];
            var4[var12] = var9;
            ++var5;
         }
      }

      byte var17;
      if (var3) {
         var17 = 16;
      } else {
         var17 = 15;
      }

      float var6 = (float)MathUtils.sum(var4) / (float)var17;
      int[] var7 = this.getOddCounts();
      int[] var16 = this.getEvenCounts();
      float[] var14 = this.getOddRoundingErrors();
      float[] var8 = this.getEvenRoundingErrors();

      int var11;
      for(var9 = 0; var9 < var4.length; ++var9) {
         float var10 = (float)var4[var9] / var6;
         var11 = (int)(0.5F + var10);
         if (var11 <= 0) {
            var12 = 1;
         } else {
            var12 = var11;
            if (var11 > 8) {
               var12 = 8;
            }
         }

         var11 = var9 / 2;
         if ((var9 & 1) == 0) {
            var7[var11] = var12;
            var14[var11] = var10 - (float)var12;
         } else {
            var16[var11] = var12;
            var8[var11] = var10 - (float)var12;
         }
      }

      this.adjustOddEvenCounts(var3, var17);
      var12 = 0;
      var5 = 0;

      for(var9 = var7.length - 1; var9 >= 0; --var9) {
         var5 = var5 * 9 + var7[var9];
         var12 += var7[var9];
      }

      int var13 = 0;
      var9 = 0;

      for(var11 = var16.length - 1; var11 >= 0; --var11) {
         var13 = var13 * 9 + var16[var11];
         var9 += var16[var11];
      }

      var5 += var13 * 3;
      DataCharacter var15;
      if (var3) {
         if ((var12 & 1) != 0 || var12 > 12 || var12 < 4) {
            throw NotFoundException.getNotFoundInstance();
         }

         var9 = (12 - var12) / 2;
         var11 = OUTSIDE_ODD_WIDEST[var9];
         var12 = RSSUtils.getRSSvalue(var7, var11, false);
         var11 = RSSUtils.getRSSvalue(var16, 9 - var11, true);
         var15 = new DataCharacter(var12 * OUTSIDE_EVEN_TOTAL_SUBSET[var9] + var11 + OUTSIDE_GSUM[var9], var5);
      } else {
         if ((var9 & 1) != 0 || var9 > 10 || var9 < 4) {
            throw NotFoundException.getNotFoundInstance();
         }

         var11 = (10 - var9) / 2;
         var9 = INSIDE_ODD_WIDEST[var11];
         var12 = RSSUtils.getRSSvalue(var7, var9, true);
         var15 = new DataCharacter(RSSUtils.getRSSvalue(var16, 9 - var9, false) * INSIDE_ODD_TOTAL_SUBSET[var11] + var12 + INSIDE_GSUM[var11], var5);
      }

      return var15;
   }

   private Pair decodePair(BitArray var1, boolean var2, int var3, Map var4) {
      Pair var14;
      label46: {
         int[] var5;
         FinderPattern var6;
         boolean var10001;
         try {
            var5 = this.findFinderPattern(var1, 0, var2);
            var6 = this.parseFoundFinderPattern(var1, var3, var2, var5);
         } catch (NotFoundException var13) {
            var10001 = false;
            break label46;
         }

         ResultPointCallback var15;
         if (var4 == null) {
            var15 = null;
         } else {
            try {
               var15 = (ResultPointCallback)var4.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
            } catch (NotFoundException var12) {
               var10001 = false;
               break label46;
            }
         }

         if (var15 != null) {
            float var7 = (float)(var5[0] + var5[1]) / 2.0F;
            float var8 = var7;
            if (var2) {
               try {
                  var8 = (float)(var1.getSize() - 1) - var7;
               } catch (NotFoundException var11) {
                  var10001 = false;
                  break label46;
               }
            }

            try {
               ResultPoint var17 = new ResultPoint(var8, (float)var3);
               var15.foundPossibleResultPoint(var17);
            } catch (NotFoundException var10) {
               var10001 = false;
               break label46;
            }
         }

         try {
            DataCharacter var16 = this.decodeDataCharacter(var1, var6, true);
            DataCharacter var18 = this.decodeDataCharacter(var1, var6, false);
            var14 = new Pair(var16.getValue() * 1597 + var18.getValue(), var16.getChecksumPortion() + var18.getChecksumPortion() * 4, var6);
            return var14;
         } catch (NotFoundException var9) {
            var10001 = false;
         }
      }

      var14 = null;
      return var14;
   }

   private int[] findFinderPattern(BitArray var1, int var2, boolean var3) throws NotFoundException {
      int[] var4 = this.getDecodeFinderCounters();
      var4[0] = 0;
      var4[1] = 0;
      var4[2] = 0;
      var4[3] = 0;
      int var5 = var1.getSize();
      boolean var6 = false;

      boolean var7;
      while(true) {
         var7 = var6;
         if (var2 >= var5) {
            break;
         }

         if (!var1.get(var2)) {
            var6 = true;
         } else {
            var6 = false;
         }

         var7 = var6;
         if (var3 == var6) {
            break;
         }

         ++var2;
      }

      byte var8 = 0;
      int var10 = var2;
      var2 = var2;
      var3 = var7;

      for(int var9 = var8; var10 < var5; ++var10) {
         if (var1.get(var10) ^ var3) {
            int var10002 = var4[var9]++;
         } else {
            if (var9 == 3) {
               if (isFinderPattern(var4)) {
                  return new int[]{var2, var10};
               }

               var2 += var4[0] + var4[1];
               var4[0] = var4[2];
               var4[1] = var4[3];
               var4[2] = 0;
               var4[3] = 0;
               --var9;
            } else {
               ++var9;
            }

            var4[var9] = 1;
            if (!var3) {
               var3 = true;
            } else {
               var3 = false;
            }
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private FinderPattern parseFoundFinderPattern(BitArray var1, int var2, boolean var3, int[] var4) throws NotFoundException {
      boolean var5 = var1.get(var4[0]);

      int var6;
      for(var6 = var4[0] - 1; var6 >= 0 && var1.get(var6) ^ var5; --var6) {
      }

      int var7 = var6 + 1;
      var6 = var4[0];
      int[] var8 = this.getDecodeFinderCounters();
      System.arraycopy(var8, 0, var8, 1, var8.length - 1);
      var8[0] = var6 - var7;
      int var9 = parseFinderValue(var8, FINDER_PATTERNS);
      int var11 = var4[1];
      int var12 = var7;
      var6 = var11;
      if (var3) {
         var12 = var1.getSize() - 1 - var7;
         var6 = var1.getSize() - 1 - var11;
      }

      return new FinderPattern(var9, new int[]{var7, var4[1]}, var12, var6, var2);
   }

   public Result decodeRow(int var1, BitArray var2, Map var3) throws NotFoundException {
      Pair var4 = this.decodePair(var2, false, var1, var3);
      addOrTally(this.possibleLeftPairs, var4);
      var2.reverse();
      Pair var7 = this.decodePair(var2, true, var1, var3);
      addOrTally(this.possibleRightPairs, var7);
      var2.reverse();
      Iterator var8 = this.possibleLeftPairs.iterator();

      while(true) {
         Pair var6;
         do {
            if (!var8.hasNext()) {
               throw NotFoundException.getNotFoundInstance();
            }

            var6 = (Pair)var8.next();
         } while(var6.getCount() <= 1);

         Iterator var5 = this.possibleRightPairs.iterator();

         while(var5.hasNext()) {
            var4 = (Pair)var5.next();
            if (var4.getCount() > 1 && checkChecksum(var6, var4)) {
               return constructResult(var6, var4);
            }
         }
      }
   }

   public void reset() {
      this.possibleLeftPairs.clear();
      this.possibleRightPairs.clear();
   }
}

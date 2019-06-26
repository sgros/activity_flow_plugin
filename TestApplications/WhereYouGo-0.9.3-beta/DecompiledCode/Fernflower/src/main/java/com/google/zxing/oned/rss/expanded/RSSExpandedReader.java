package com.google.zxing.oned.rss.expanded;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.oned.rss.AbstractRSSReader;
import com.google.zxing.oned.rss.DataCharacter;
import com.google.zxing.oned.rss.FinderPattern;
import com.google.zxing.oned.rss.RSSUtils;
import com.google.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class RSSExpandedReader extends AbstractRSSReader {
   private static final int[] EVEN_TOTAL_SUBSET = new int[]{4, 20, 52, 104, 204};
   private static final int[][] FINDER_PATTERNS = new int[][]{{1, 8, 4, 1}, {3, 6, 4, 1}, {3, 4, 6, 1}, {3, 2, 8, 1}, {2, 6, 5, 1}, {2, 2, 9, 1}};
   private static final int[][] FINDER_PATTERN_SEQUENCES;
   private static final int FINDER_PAT_A = 0;
   private static final int FINDER_PAT_B = 1;
   private static final int FINDER_PAT_C = 2;
   private static final int FINDER_PAT_D = 3;
   private static final int FINDER_PAT_E = 4;
   private static final int FINDER_PAT_F = 5;
   private static final int[] GSUM = new int[]{0, 348, 1388, 2948, 3988};
   private static final int MAX_PAIRS = 11;
   private static final int[] SYMBOL_WIDEST = new int[]{7, 5, 4, 3, 1};
   private static final int[][] WEIGHTS;
   private final List pairs = new ArrayList(11);
   private final List rows = new ArrayList();
   private final int[] startEnd = new int[2];
   private boolean startFromEven;

   static {
      int[] var0 = new int[]{1, 3, 9, 27, 81, 32, 96, 77};
      int[] var1 = new int[]{20, 60, 180, 118, 143, 7, 21, 63};
      int[] var2 = new int[]{189, 145, 13, 39, 117, 140, 209, 205};
      int[] var3 = new int[]{193, 157, 49, 147, 19, 57, 171, 91};
      int[] var4 = new int[]{62, 186, 136, 197, 169, 85, 44, 132};
      int[] var5 = new int[]{185, 133, 188, 142, 4, 12, 36, 108};
      int[] var6 = new int[]{46, 138, 203, 187, 139, 206, 196, 166};
      int[] var7 = new int[]{76, 17, 51, 153, 37, 111, 122, 155};
      int[] var8 = new int[]{43, 129, 176, 106, 107, 110, 119, 146};
      int[] var9 = new int[]{16, 48, 144, 10, 30, 90, 59, 177};
      int[] var10 = new int[]{109, 116, 137, 200, 178, 112, 125, 164};
      int[] var11 = new int[]{148, 22, 66, 198, 172, 94, 71, 2};
      int[] var12 = new int[]{120, 149, 25, 75, 14, 42, 126, 167};
      int[] var13 = new int[]{79, 26, 78, 23, 69, 207, 199, 175};
      int[] var14 = new int[]{103, 98, 83, 38, 114, 131, 182, 124};
      int[] var15 = new int[]{55, 165, 73, 8, 24, 72, 5, 15};
      WEIGHTS = new int[][]{var0, var1, var2, var3, var4, var5, {113, 128, 173, 97, 80, 29, 87, 50}, {150, 28, 84, 41, 123, 158, 52, 156}, var6, var7, var8, var9, var10, {70, 210, 208, 202, 184, 130, 179, 115}, {134, 191, 151, 31, 93, 68, 204, 190}, var11, {6, 18, 54, 162, 64, 192, 154, 40}, var12, var13, var14, {161, 61, 183, 127, 170, 88, 53, 159}, var15, {45, 135, 194, 160, 58, 174, 100, 89}};
      FINDER_PATTERN_SEQUENCES = new int[][]{{0, 0}, {0, 1, 1}, {0, 2, 1, 3}, {0, 4, 1, 3, 2}, {0, 4, 1, 3, 3, 5}, {0, 4, 1, 3, 4, 5, 5}, {0, 0, 1, 1, 2, 2, 3, 3}, {0, 0, 1, 1, 2, 2, 3, 4, 4}, {0, 0, 1, 1, 2, 2, 3, 4, 5, 5}, {0, 0, 1, 1, 2, 3, 3, 4, 4, 5, 5}};
   }

   private void adjustOddEvenCounts(int var1) throws NotFoundException {
      boolean var2 = false;
      int var3 = MathUtils.sum(this.getOddCounts());
      int var4 = MathUtils.sum(this.getEvenCounts());
      boolean var5 = false;
      boolean var6 = false;
      boolean var7;
      if (var3 > 13) {
         var7 = true;
      } else {
         var7 = var6;
         if (var3 < 4) {
            var5 = true;
            var7 = var6;
         }
      }

      boolean var8 = false;
      boolean var9 = false;
      if (var4 > 13) {
         var6 = true;
      } else {
         var6 = var9;
         if (var4 < 4) {
            var8 = true;
            var6 = var9;
         }
      }

      int var10 = var3 + var4 - var1;
      if ((var3 & 1) == 1) {
         var9 = true;
      } else {
         var9 = false;
      }

      boolean var11 = var2;
      if ((var4 & 1) == 0) {
         var11 = true;
      }

      if (var10 == 1) {
         if (var9) {
            if (var11) {
               throw NotFoundException.getNotFoundInstance();
            }

            var7 = true;
         } else {
            if (!var11) {
               throw NotFoundException.getNotFoundInstance();
            }

            var6 = true;
         }
      } else if (var10 == -1) {
         if (var9) {
            if (var11) {
               throw NotFoundException.getNotFoundInstance();
            }

            var5 = true;
         } else {
            if (!var11) {
               throw NotFoundException.getNotFoundInstance();
            }

            var8 = true;
         }
      } else {
         if (var10 != 0) {
            throw NotFoundException.getNotFoundInstance();
         }

         if (var9) {
            if (!var11) {
               throw NotFoundException.getNotFoundInstance();
            }

            if (var3 < var4) {
               var5 = true;
               var6 = true;
            } else {
               var7 = true;
               var8 = true;
            }
         } else if (var11) {
            throw NotFoundException.getNotFoundInstance();
         }
      }

      if (var5) {
         if (var7) {
            throw NotFoundException.getNotFoundInstance();
         }

         increment(this.getOddCounts(), this.getOddRoundingErrors());
      }

      if (var7) {
         decrement(this.getOddCounts(), this.getOddRoundingErrors());
      }

      if (var8) {
         if (var6) {
            throw NotFoundException.getNotFoundInstance();
         }

         increment(this.getEvenCounts(), this.getOddRoundingErrors());
      }

      if (var6) {
         decrement(this.getEvenCounts(), this.getEvenRoundingErrors());
      }

   }

   private boolean checkChecksum() {
      boolean var1 = false;
      ExpandedPair var2 = (ExpandedPair)this.pairs.get(0);
      DataCharacter var3 = var2.getLeftChar();
      DataCharacter var9 = var2.getRightChar();
      if (var9 != null) {
         int var4 = var9.getChecksumPortion();
         int var5 = 2;

         for(int var6 = 1; var6 < this.pairs.size(); ++var6) {
            var2 = (ExpandedPair)this.pairs.get(var6);
            int var7 = var4 + var2.getLeftChar().getChecksumPortion();
            int var8 = var5 + 1;
            var9 = var2.getRightChar();
            var4 = var7;
            var5 = var8;
            if (var9 != null) {
               var4 = var7 + var9.getChecksumPortion();
               var5 = var8 + 1;
            }
         }

         if ((var5 - 4) * 211 + var4 % 211 == var3.getValue()) {
            var1 = true;
         }
      }

      return var1;
   }

   private List checkRows(List var1, int var2) throws NotFoundException {
      for(; var2 < this.rows.size(); ++var2) {
         ExpandedRow var3 = (ExpandedRow)this.rows.get(var2);
         this.pairs.clear();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            ExpandedRow var5 = (ExpandedRow)var4.next();
            this.pairs.addAll(var5.getPairs());
         }

         this.pairs.addAll(var3.getPairs());
         if (isValidSequence(this.pairs)) {
            if (this.checkChecksum()) {
               var1 = this.pairs;
            } else {
               ArrayList var8 = new ArrayList();
               var8.addAll(var1);
               var8.add(var3);

               List var7;
               try {
                  var7 = this.checkRows(var8, var2 + 1);
               } catch (NotFoundException var6) {
                  continue;
               }

               var1 = var7;
            }

            return var1;
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private List checkRows(boolean var1) {
      List var2;
      if (this.rows.size() > 25) {
         this.rows.clear();
         var2 = null;
      } else {
         this.pairs.clear();
         if (var1) {
            Collections.reverse(this.rows);
         }

         List var3 = null;

         label20: {
            try {
               ArrayList var5 = new ArrayList();
               var2 = this.checkRows(var5, 0);
            } catch (NotFoundException var4) {
               break label20;
            }

            var3 = var2;
         }

         var2 = var3;
         if (var1) {
            Collections.reverse(this.rows);
            var2 = var3;
         }
      }

      return var2;
   }

   static Result constructResult(List var0) throws NotFoundException, FormatException {
      String var1 = AbstractExpandedDecoder.createDecoder(BitArrayBuilder.buildBitArray(var0)).parseInformation();
      ResultPoint[] var2 = ((ExpandedPair)var0.get(0)).getFinderPattern().getResultPoints();
      ResultPoint[] var3 = ((ExpandedPair)var0.get(var0.size() - 1)).getFinderPattern().getResultPoints();
      ResultPoint var6 = var2[0];
      ResultPoint var4 = var2[1];
      ResultPoint var7 = var3[0];
      ResultPoint var5 = var3[1];
      BarcodeFormat var8 = BarcodeFormat.RSS_EXPANDED;
      return new Result(var1, (byte[])null, new ResultPoint[]{var6, var4, var7, var5}, var8);
   }

   private void findNextPair(BitArray var1, List var2, int var3) throws NotFoundException {
      int[] var4 = this.getDecodeFinderCounters();
      var4[0] = 0;
      var4[1] = 0;
      var4[2] = 0;
      var4[3] = 0;
      int var5 = var1.getSize();
      if (var3 < 0) {
         if (var2.isEmpty()) {
            var3 = 0;
         } else {
            var3 = ((ExpandedPair)var2.get(var2.size() - 1)).getFinderPattern().getStartEnd()[1];
         }
      }

      boolean var6;
      if (var2.size() % 2 != 0) {
         var6 = true;
      } else {
         var6 = false;
      }

      boolean var7 = var6;
      if (this.startFromEven) {
         if (!var6) {
            var7 = true;
         } else {
            var7 = false;
         }
      }

      var6 = false;

      boolean var8;
      while(true) {
         var8 = var6;
         if (var3 >= var5) {
            break;
         }

         if (!var1.get(var3)) {
            var6 = true;
         } else {
            var6 = false;
         }

         var8 = var6;
         if (!var6) {
            break;
         }

         ++var3;
      }

      byte var9 = 0;
      int var10 = var3;
      var3 = var3;

      for(int var11 = var9; var10 < var5; ++var10) {
         if (var1.get(var10) ^ var8) {
            int var10002 = var4[var11]++;
         } else {
            if (var11 == 3) {
               if (var7) {
                  reverseCounters(var4);
               }

               if (isFinderPattern(var4)) {
                  this.startEnd[0] = var3;
                  this.startEnd[1] = var10;
                  return;
               }

               if (var7) {
                  reverseCounters(var4);
               }

               var3 += var4[0] + var4[1];
               var4[0] = var4[2];
               var4[1] = var4[3];
               var4[2] = 0;
               var4[3] = 0;
               --var11;
            } else {
               ++var11;
            }

            var4[var11] = 1;
            if (!var8) {
               var8 = true;
            } else {
               var8 = false;
            }
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static int getNextSecondBar(BitArray var0, int var1) {
      if (var0.get(var1)) {
         var1 = var0.getNextSet(var0.getNextUnset(var1));
      } else {
         var1 = var0.getNextUnset(var0.getNextSet(var1));
      }

      return var1;
   }

   private static boolean isNotA1left(FinderPattern var0, boolean var1, boolean var2) {
      if (var0.getValue() == 0 && var1 && var2) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static boolean isPartialRow(Iterable var0, Iterable var1) {
      Iterator var10 = var1.iterator();

      boolean var9;
      while(true) {
         if (!var10.hasNext()) {
            var9 = false;
            break;
         }

         ExpandedRow var2 = (ExpandedRow)var10.next();
         boolean var3 = true;
         Iterator var4 = var0.iterator();

         boolean var5;
         while(true) {
            var5 = var3;
            if (!var4.hasNext()) {
               break;
            }

            ExpandedPair var6 = (ExpandedPair)var4.next();
            boolean var7 = false;
            Iterator var8 = var2.getPairs().iterator();

            while(true) {
               var5 = var7;
               if (!var8.hasNext()) {
                  break;
               }

               if (var6.equals((ExpandedPair)var8.next())) {
                  var5 = true;
                  break;
               }
            }

            if (!var5) {
               var5 = false;
               break;
            }
         }

         if (var5) {
            var9 = true;
            break;
         }
      }

      return var9;
   }

   private static boolean isValidSequence(List var0) {
      int[][] var1 = FINDER_PATTERN_SEQUENCES;
      int var2 = var1.length;
      int var3 = 0;

      boolean var8;
      while(true) {
         if (var3 >= var2) {
            var8 = false;
            break;
         }

         int[] var4 = var1[var3];
         if (var0.size() <= var4.length) {
            boolean var5 = true;
            int var6 = 0;

            boolean var7;
            while(true) {
               var7 = var5;
               if (var6 >= var0.size()) {
                  break;
               }

               if (((ExpandedPair)var0.get(var6)).getFinderPattern().getValue() != var4[var6]) {
                  var7 = false;
                  break;
               }

               ++var6;
            }

            if (var7) {
               var8 = true;
               break;
            }
         }

         ++var3;
      }

      return var8;
   }

   private FinderPattern parseFoundFinderPattern(BitArray var1, int var2, boolean var3) {
      int var4;
      int var5;
      int var6;
      if (var3) {
         for(var4 = this.startEnd[0] - 1; var4 >= 0 && !var1.get(var4); --var4) {
         }

         var5 = var4 + 1;
         var6 = this.startEnd[0] - var5;
         var4 = this.startEnd[1];
      } else {
         var5 = this.startEnd[0];
         var4 = var1.getNextUnset(this.startEnd[1] + 1);
         var6 = var4 - this.startEnd[1];
      }

      int[] var8 = this.getDecodeFinderCounters();
      System.arraycopy(var8, 0, var8, 1, var8.length - 1);
      var8[0] = var6;

      FinderPattern var9;
      try {
         var6 = parseFinderValue(var8, FINDER_PATTERNS);
      } catch (NotFoundException var7) {
         var9 = null;
         return var9;
      }

      var9 = new FinderPattern(var6, new int[]{var5, var4}, var5, var4, var2);
      return var9;
   }

   private static void removePartialRows(List var0, List var1) {
      Iterator var8 = var1.iterator();

      while(true) {
         ExpandedRow var2;
         do {
            if (!var8.hasNext()) {
               return;
            }

            var2 = (ExpandedRow)var8.next();
         } while(var2.getPairs().size() == var0.size());

         boolean var3 = true;
         Iterator var9 = var2.getPairs().iterator();

         boolean var4;
         while(true) {
            var4 = var3;
            if (!var9.hasNext()) {
               break;
            }

            ExpandedPair var5 = (ExpandedPair)var9.next();
            boolean var6 = false;
            Iterator var7 = var0.iterator();

            while(true) {
               var4 = var6;
               if (!var7.hasNext()) {
                  break;
               }

               if (var5.equals((ExpandedPair)var7.next())) {
                  var4 = true;
                  break;
               }
            }

            if (!var4) {
               var4 = false;
               break;
            }
         }

         if (var4) {
            var8.remove();
         }
      }
   }

   private static void reverseCounters(int[] var0) {
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1 / 2; ++var2) {
         int var3 = var0[var2];
         var0[var2] = var0[var1 - var2 - 1];
         var0[var1 - var2 - 1] = var3;
      }

   }

   private void storeRow(int var1, boolean var2) {
      int var3 = 0;
      boolean var4 = false;
      boolean var5 = false;

      boolean var6;
      while(true) {
         var6 = var5;
         if (var3 >= this.rows.size()) {
            break;
         }

         ExpandedRow var7 = (ExpandedRow)this.rows.get(var3);
         if (var7.getRowNumber() > var1) {
            var6 = var7.isEquivalent(this.pairs);
            break;
         }

         var4 = var7.isEquivalent(this.pairs);
         ++var3;
      }

      if (!var6 && !var4 && !isPartialRow(this.pairs, this.rows)) {
         this.rows.add(var3, new ExpandedRow(this.pairs, var1, var2));
         removePartialRows(this.pairs, this.rows);
      }

   }

   DataCharacter decodeDataCharacter(BitArray var1, FinderPattern var2, boolean var3, boolean var4) throws NotFoundException {
      int[] var5 = this.getDataCharacterCounters();
      var5[0] = 0;
      var5[1] = 0;
      var5[2] = 0;
      var5[3] = 0;
      var5[4] = 0;
      var5[5] = 0;
      var5[6] = 0;
      var5[7] = 0;
      int var8;
      int var9;
      int var10;
      if (var4) {
         recordPatternInReverse(var1, var2.getStartEnd()[0], var5);
      } else {
         recordPattern(var1, var2.getStartEnd()[1], var5);
         var8 = 0;

         for(var9 = var5.length - 1; var8 < var9; --var9) {
            var10 = var5[var8];
            var5[var8] = var5[var9];
            var5[var9] = var10;
            ++var8;
         }
      }

      float var6 = (float)MathUtils.sum(var5) / 17.0F;
      float var7 = (float)(var2.getStartEnd()[1] - var2.getStartEnd()[0]) / 15.0F;
      if (Math.abs(var6 - var7) / var7 > 0.3F) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         int[] var11 = this.getOddCounts();
         int[] var17 = this.getEvenCounts();
         float[] var12 = this.getOddRoundingErrors();
         float[] var13 = this.getEvenRoundingErrors();

         for(var8 = 0; var8 < var5.length; ++var8) {
            var7 = 1.0F * (float)var5[var8] / var6;
            var10 = (int)(0.5F + var7);
            if (var10 <= 0) {
               if (var7 < 0.3F) {
                  throw NotFoundException.getNotFoundInstance();
               }

               var9 = 1;
            } else {
               var9 = var10;
               if (var10 > 8) {
                  if (var7 > 8.7F) {
                     throw NotFoundException.getNotFoundInstance();
                  }

                  var9 = 8;
               }
            }

            var10 = var8 / 2;
            if ((var8 & 1) == 0) {
               var11[var10] = var9;
               var12[var10] = var7 - (float)var9;
            } else {
               var17[var10] = var9;
               var13[var10] = var7 - (float)var9;
            }
         }

         this.adjustOddEvenCounts(17);
         var10 = var2.getValue();
         byte var19;
         if (var3) {
            var19 = 0;
         } else {
            var19 = 2;
         }

         byte var18;
         if (var4) {
            var18 = 0;
         } else {
            var18 = 1;
         }

         int var14 = var18 + var10 * 4 + var19 - 1;
         var8 = 0;
         var9 = 0;

         int var15;
         for(var10 = var11.length - 1; var10 >= 0; var9 = var15) {
            var15 = var9;
            if (isNotA1left(var2, var3, var4)) {
               var15 = WEIGHTS[var14][var10 * 2];
               var15 = var9 + var11[var10] * var15;
            }

            var8 += var11[var10];
            --var10;
         }

         var10 = 0;

         int var16;
         for(var15 = var17.length - 1; var15 >= 0; var10 = var16) {
            var16 = var10;
            if (isNotA1left(var2, var3, var4)) {
               var16 = WEIGHTS[var14][var15 * 2 + 1];
               var16 = var10 + var17[var15] * var16;
            }

            --var15;
         }

         if ((var8 & 1) == 0 && var8 <= 13 && var8 >= 4) {
            var15 = (13 - var8) / 2;
            var16 = SYMBOL_WIDEST[var15];
            var8 = RSSUtils.getRSSvalue(var11, var16, true);
            var16 = RSSUtils.getRSSvalue(var17, 9 - var16, false);
            return new DataCharacter(var8 * EVEN_TOTAL_SUBSET[var15] + var16 + GSUM[var15], var9 + var10);
         } else {
            throw NotFoundException.getNotFoundInstance();
         }
      }
   }

   public Result decodeRow(int var1, BitArray var2, Map var3) throws NotFoundException, FormatException {
      this.pairs.clear();
      this.startFromEven = false;

      Result var5;
      Result var6;
      try {
         var6 = constructResult(this.decodeRow2pairs(var1, var2));
      } catch (NotFoundException var4) {
         this.pairs.clear();
         this.startFromEven = true;
         var5 = constructResult(this.decodeRow2pairs(var1, var2));
         return var5;
      }

      var5 = var6;
      return var5;
   }

   List decodeRow2pairs(int var1, BitArray var2) throws NotFoundException {
      while(true) {
         try {
            ExpandedPair var7 = this.retrieveNextPair(var2, this.pairs, var1);
            this.pairs.add(var7);
         } catch (NotFoundException var5) {
            if (this.pairs.isEmpty()) {
               throw var5;
            }

            List var6;
            if (this.checkChecksum()) {
               var6 = this.pairs;
               return var6;
            } else {
               boolean var4;
               if (!this.rows.isEmpty()) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               this.storeRow(var1, false);
               if (var4) {
                  List var3 = this.checkRows(false);
                  var6 = var3;
                  if (var3 != null) {
                     return var6;
                  }

                  var3 = this.checkRows(true);
                  var6 = var3;
                  if (var3 != null) {
                     return var6;
                  }
               }

               throw NotFoundException.getNotFoundInstance();
            }
         }
      }
   }

   List getRows() {
      return this.rows;
   }

   public void reset() {
      this.pairs.clear();
      this.rows.clear();
   }

   ExpandedPair retrieveNextPair(BitArray var1, List var2, int var3) throws NotFoundException {
      boolean var4;
      if (var2.size() % 2 == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      boolean var5 = var4;
      if (this.startFromEven) {
         if (!var4) {
            var5 = true;
         } else {
            var5 = false;
         }
      }

      boolean var6 = true;
      int var7 = -1;

      FinderPattern var8;
      boolean var9;
      do {
         this.findNextPair(var1, var2, var7);
         var8 = this.parseFoundFinderPattern(var1, var3, var5);
         if (var8 == null) {
            var7 = getNextSecondBar(var1, this.startEnd[0]);
            var9 = var6;
         } else {
            var9 = false;
         }

         var6 = var9;
      } while(var9);

      DataCharacter var10 = this.decodeDataCharacter(var1, var8, var5, true);
      if (!var2.isEmpty() && ((ExpandedPair)var2.get(var2.size() - 1)).mustBeLast()) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         DataCharacter var12;
         try {
            var12 = this.decodeDataCharacter(var1, var8, var5, false);
         } catch (NotFoundException var11) {
            var12 = null;
         }

         return new ExpandedPair(var10, var12, var8, true);
      }
   }
}

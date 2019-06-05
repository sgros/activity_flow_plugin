package com.google.zxing.multi.qrcode.detector;

import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.detector.FinderPattern;
import com.google.zxing.qrcode.detector.FinderPatternFinder;
import com.google.zxing.qrcode.detector.FinderPatternInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

final class MultiFinderPatternFinder extends FinderPatternFinder {
   private static final float DIFF_MODSIZE_CUTOFF = 0.5F;
   private static final float DIFF_MODSIZE_CUTOFF_PERCENT = 0.05F;
   private static final FinderPatternInfo[] EMPTY_RESULT_ARRAY = new FinderPatternInfo[0];
   private static final float MAX_MODULE_COUNT_PER_EDGE = 180.0F;
   private static final float MIN_MODULE_COUNT_PER_EDGE = 9.0F;

   MultiFinderPatternFinder(BitMatrix var1) {
      super(var1);
   }

   MultiFinderPatternFinder(BitMatrix var1, ResultPointCallback var2) {
      super(var1, var2);
   }

   private FinderPattern[][] selectMutipleBestPatterns() throws NotFoundException {
      List var1 = this.getPossibleCenters();
      int var2 = var1.size();
      if (var2 < 3) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         FinderPattern[][] var3;
         if (var2 == 3) {
            var3 = new FinderPattern[][]{{(FinderPattern)var1.get(0), (FinderPattern)var1.get(1), (FinderPattern)var1.get(2)}};
         } else {
            Collections.sort(var1, new MultiFinderPatternFinder.ModuleSizeComparator());
            ArrayList var15 = new ArrayList();

            for(int var4 = 0; var4 < var2 - 2; ++var4) {
               FinderPattern var5 = (FinderPattern)var1.get(var4);
               if (var5 != null) {
                  for(int var6 = var4 + 1; var6 < var2 - 1; ++var6) {
                     FinderPattern var7 = (FinderPattern)var1.get(var6);
                     if (var7 != null) {
                        float var8 = (var5.getEstimatedModuleSize() - var7.getEstimatedModuleSize()) / Math.min(var5.getEstimatedModuleSize(), var7.getEstimatedModuleSize());
                        if (Math.abs(var5.getEstimatedModuleSize() - var7.getEstimatedModuleSize()) > 0.5F && var8 >= 0.05F) {
                           break;
                        }

                        for(int var9 = var6 + 1; var9 < var2; ++var9) {
                           FinderPattern var10 = (FinderPattern)var1.get(var9);
                           if (var10 != null) {
                              var8 = (var7.getEstimatedModuleSize() - var10.getEstimatedModuleSize()) / Math.min(var7.getEstimatedModuleSize(), var10.getEstimatedModuleSize());
                              if (Math.abs(var7.getEstimatedModuleSize() - var10.getEstimatedModuleSize()) > 0.5F && var8 >= 0.05F) {
                                 break;
                              }

                              FinderPattern[] var11 = new FinderPattern[]{var5, var7, var10};
                              ResultPoint.orderBestPatterns(var11);
                              FinderPatternInfo var16 = new FinderPatternInfo(var11);
                              float var12 = ResultPoint.distance(var16.getTopLeft(), var16.getBottomLeft());
                              var8 = ResultPoint.distance(var16.getTopRight(), var16.getBottomLeft());
                              float var13 = ResultPoint.distance(var16.getTopLeft(), var16.getTopRight());
                              float var14 = (var12 + var13) / (var5.getEstimatedModuleSize() * 2.0F);
                              if (var14 <= 180.0F && var14 >= 9.0F && Math.abs((var12 - var13) / Math.min(var12, var13)) < 0.1F) {
                                 var14 = (float)Math.sqrt((double)(var12 * var12 + var13 * var13));
                                 if (Math.abs((var8 - var14) / Math.min(var8, var14)) < 0.1F) {
                                    var15.add(var11);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (var15.isEmpty()) {
               throw NotFoundException.getNotFoundInstance();
            }

            var3 = (FinderPattern[][])var15.toArray(new FinderPattern[var15.size()][]);
         }

         return var3;
      }
   }

   public FinderPatternInfo[] findMulti(Map var1) throws NotFoundException {
      boolean var2;
      if (var1 != null && var1.containsKey(DecodeHintType.TRY_HARDER)) {
         var2 = true;
      } else {
         var2 = false;
      }

      boolean var3;
      if (var1 != null && var1.containsKey(DecodeHintType.PURE_BARCODE)) {
         var3 = true;
      } else {
         var3 = false;
      }

      BitMatrix var12 = this.getImage();
      int var4 = var12.getHeight();
      int var5 = var12.getWidth();
      int var6 = (int)((float)var4 / 228.0F * 3.0F);
      if (var6 < 3 || var2) {
         var6 = 3;
      }

      int[] var7 = new int[5];

      int var10;
      int var15;
      for(int var8 = var6 - 1; var8 < var4; var8 += var6) {
         var7[0] = 0;
         var7[1] = 0;
         var7[2] = 0;
         var7[3] = 0;
         var7[4] = 0;
         var15 = 0;

         for(int var9 = 0; var9 < var5; ++var9) {
            int var10002;
            if (var12.get(var9, var8)) {
               var10 = var15;
               if ((var15 & 1) == 1) {
                  var10 = var15 + 1;
               }

               var10002 = var7[var10]++;
               var15 = var10;
            } else if ((var15 & 1) == 0) {
               if (var15 == 4) {
                  if (foundPatternCross(var7) && this.handlePossibleCenter(var7, var8, var9, var3)) {
                     var15 = 0;
                     var7[0] = 0;
                     var7[1] = 0;
                     var7[2] = 0;
                     var7[3] = 0;
                     var7[4] = 0;
                  } else {
                     var7[0] = var7[2];
                     var7[1] = var7[3];
                     var7[2] = var7[4];
                     var7[3] = 1;
                     var7[4] = 0;
                     var15 = 3;
                  }
               } else {
                  ++var15;
                  var10002 = var7[var15]++;
               }
            } else {
               var10002 = var7[var15]++;
            }
         }

         if (foundPatternCross(var7)) {
            this.handlePossibleCenter(var7, var8, var5, var3);
         }
      }

      FinderPattern[][] var11 = this.selectMutipleBestPatterns();
      ArrayList var13 = new ArrayList();
      var10 = var11.length;

      for(var15 = 0; var15 < var10; ++var15) {
         FinderPattern[] var16 = var11[var15];
         ResultPoint.orderBestPatterns(var16);
         var13.add(new FinderPatternInfo(var16));
      }

      FinderPatternInfo[] var14;
      if (var13.isEmpty()) {
         var14 = EMPTY_RESULT_ARRAY;
      } else {
         var14 = (FinderPatternInfo[])var13.toArray(new FinderPatternInfo[var13.size()]);
      }

      return var14;
   }

   private static final class ModuleSizeComparator implements Serializable, Comparator {
      private ModuleSizeComparator() {
      }

      // $FF: synthetic method
      ModuleSizeComparator(Object var1) {
         this();
      }

      public int compare(FinderPattern var1, FinderPattern var2) {
         float var3 = var2.getEstimatedModuleSize() - var1.getEstimatedModuleSize();
         byte var4;
         if ((double)var3 < 0.0D) {
            var4 = -1;
         } else if ((double)var3 > 0.0D) {
            var4 = 1;
         } else {
            var4 = 0;
         }

         return var4;
      }
   }
}

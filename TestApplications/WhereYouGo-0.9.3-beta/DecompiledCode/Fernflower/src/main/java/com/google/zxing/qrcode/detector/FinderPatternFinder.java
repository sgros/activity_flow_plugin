package com.google.zxing.qrcode.detector;

import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FinderPatternFinder {
   private static final int CENTER_QUORUM = 2;
   protected static final int MAX_MODULES = 57;
   protected static final int MIN_SKIP = 3;
   private final int[] crossCheckStateCount;
   private boolean hasSkipped;
   private final BitMatrix image;
   private final List possibleCenters;
   private final ResultPointCallback resultPointCallback;

   public FinderPatternFinder(BitMatrix var1) {
      this(var1, (ResultPointCallback)null);
   }

   public FinderPatternFinder(BitMatrix var1, ResultPointCallback var2) {
      this.image = var1;
      this.possibleCenters = new ArrayList();
      this.crossCheckStateCount = new int[5];
      this.resultPointCallback = var2;
   }

   private static float centerFromEnd(int[] var0, int var1) {
      return (float)(var1 - var0[4] - var0[3]) - (float)var0[2] / 2.0F;
   }

   private boolean crossCheckDiagonal(int var1, int var2, int var3, int var4) {
      int[] var5 = this.getCrossCheckStateCount();

      int var10002;
      int var6;
      for(var6 = 0; var1 >= var6 && var2 >= var6 && this.image.get(var2 - var6, var1 - var6); ++var6) {
         var10002 = var5[2]++;
      }

      boolean var8;
      if (var1 >= var6) {
         int var7 = var6;
         if (var2 >= var6) {
            while(var1 >= var7 && var2 >= var7 && !this.image.get(var2 - var7, var1 - var7) && var5[1] <= var3) {
               var10002 = var5[1]++;
               ++var7;
            }

            if (var1 >= var7 && var2 >= var7 && var5[1] <= var3) {
               while(var1 >= var7 && var2 >= var7 && this.image.get(var2 - var7, var1 - var7) && var5[0] <= var3) {
                  var10002 = var5[0]++;
                  ++var7;
               }

               if (var5[0] > var3) {
                  var8 = false;
               } else {
                  int var9 = this.image.getHeight();
                  int var10 = this.image.getWidth();

                  for(var7 = 1; var1 + var7 < var9 && var2 + var7 < var10 && this.image.get(var2 + var7, var1 + var7); ++var7) {
                     var10002 = var5[2]++;
                  }

                  if (var1 + var7 < var9) {
                     var6 = var7;
                     if (var2 + var7 < var10) {
                        while(var1 + var6 < var9 && var2 + var6 < var10 && !this.image.get(var2 + var6, var1 + var6) && var5[3] < var3) {
                           var10002 = var5[3]++;
                           ++var6;
                        }

                        if (var1 + var6 < var9 && var2 + var6 < var10 && var5[3] < var3) {
                           while(var1 + var6 < var9 && var2 + var6 < var10 && this.image.get(var2 + var6, var1 + var6) && var5[4] < var3) {
                              var10002 = var5[4]++;
                              ++var6;
                           }

                           if (var5[4] >= var3) {
                              var8 = false;
                              return var8;
                           } else {
                              if (Math.abs(var5[0] + var5[1] + var5[2] + var5[3] + var5[4] - var4) < var4 * 2 && foundPatternCross(var5)) {
                                 var8 = true;
                              } else {
                                 var8 = false;
                              }

                              return var8;
                           }
                        }

                        var8 = false;
                        return var8;
                     }
                  }

                  var8 = false;
               }

               return var8;
            }

            var8 = false;
            return var8;
         }
      }

      var8 = false;
      return var8;
   }

   private float crossCheckHorizontal(int var1, int var2, int var3, int var4) {
      BitMatrix var5 = this.image;
      int var6 = var5.getWidth();
      int[] var7 = this.getCrossCheckStateCount();

      int var10002;
      int var8;
      for(var8 = var1; var8 >= 0 && var5.get(var8, var2); --var8) {
         var10002 = var7[2]++;
      }

      int var9 = var8;
      float var10;
      if (var8 < 0) {
         var10 = Float.NaN;
      } else {
         while(var9 >= 0 && !var5.get(var9, var2) && var7[1] <= var3) {
            var10002 = var7[1]++;
            --var9;
         }

         if (var9 >= 0 && var7[1] <= var3) {
            while(var9 >= 0 && var5.get(var9, var2) && var7[0] <= var3) {
               var10002 = var7[0]++;
               --var9;
            }

            if (var7[0] > var3) {
               var10 = Float.NaN;
            } else {
               ++var1;

               while(var1 < var6 && var5.get(var1, var2)) {
                  var10002 = var7[2]++;
                  ++var1;
               }

               var8 = var1;
               if (var1 == var6) {
                  var10 = Float.NaN;
               } else {
                  while(var8 < var6 && !var5.get(var8, var2) && var7[3] < var3) {
                     var10002 = var7[3]++;
                     ++var8;
                  }

                  if (var8 != var6 && var7[3] < var3) {
                     while(var8 < var6 && var5.get(var8, var2) && var7[4] < var3) {
                        var10002 = var7[4]++;
                        ++var8;
                     }

                     if (var7[4] >= var3) {
                        var10 = Float.NaN;
                     } else if (Math.abs(var7[0] + var7[1] + var7[2] + var7[3] + var7[4] - var4) * 5 >= var4) {
                        var10 = Float.NaN;
                     } else if (foundPatternCross(var7)) {
                        var10 = centerFromEnd(var7, var8);
                     } else {
                        var10 = Float.NaN;
                     }
                  } else {
                     var10 = Float.NaN;
                  }
               }
            }
         } else {
            var10 = Float.NaN;
         }
      }

      return var10;
   }

   private float crossCheckVertical(int var1, int var2, int var3, int var4) {
      BitMatrix var5 = this.image;
      int var6 = var5.getHeight();
      int[] var7 = this.getCrossCheckStateCount();

      int var10002;
      int var8;
      for(var8 = var1; var8 >= 0 && var5.get(var2, var8); --var8) {
         var10002 = var7[2]++;
      }

      int var9 = var8;
      float var10;
      if (var8 < 0) {
         var10 = Float.NaN;
      } else {
         while(var9 >= 0 && !var5.get(var2, var9) && var7[1] <= var3) {
            var10002 = var7[1]++;
            --var9;
         }

         if (var9 >= 0 && var7[1] <= var3) {
            while(var9 >= 0 && var5.get(var2, var9) && var7[0] <= var3) {
               var10002 = var7[0]++;
               --var9;
            }

            if (var7[0] > var3) {
               var10 = Float.NaN;
            } else {
               ++var1;

               while(var1 < var6 && var5.get(var2, var1)) {
                  var10002 = var7[2]++;
                  ++var1;
               }

               var8 = var1;
               if (var1 == var6) {
                  var10 = Float.NaN;
               } else {
                  while(var8 < var6 && !var5.get(var2, var8) && var7[3] < var3) {
                     var10002 = var7[3]++;
                     ++var8;
                  }

                  if (var8 != var6 && var7[3] < var3) {
                     while(var8 < var6 && var5.get(var2, var8) && var7[4] < var3) {
                        var10002 = var7[4]++;
                        ++var8;
                     }

                     if (var7[4] >= var3) {
                        var10 = Float.NaN;
                     } else if (Math.abs(var7[0] + var7[1] + var7[2] + var7[3] + var7[4] - var4) * 5 >= var4 * 2) {
                        var10 = Float.NaN;
                     } else if (foundPatternCross(var7)) {
                        var10 = centerFromEnd(var7, var8);
                     } else {
                        var10 = Float.NaN;
                     }
                  } else {
                     var10 = Float.NaN;
                  }
               }
            }
         } else {
            var10 = Float.NaN;
         }
      }

      return var10;
   }

   private int findRowSkip() {
      byte var1 = 0;
      int var2;
      if (this.possibleCenters.size() <= 1) {
         var2 = var1;
      } else {
         FinderPattern var3 = null;
         Iterator var4 = this.possibleCenters.iterator();

         while(true) {
            var2 = var1;
            if (!var4.hasNext()) {
               break;
            }

            FinderPattern var5 = (FinderPattern)var4.next();
            if (var5.getCount() >= 2) {
               if (var3 != null) {
                  this.hasSkipped = true;
                  var2 = (int)(Math.abs(var3.getX() - var5.getX()) - Math.abs(var3.getY() - var5.getY())) / 2;
                  break;
               }

               var3 = var5;
            }
         }
      }

      return var2;
   }

   protected static boolean foundPatternCross(int[] var0) {
      boolean var1 = false;
      int var2 = 0;
      int var3 = 0;

      boolean var5;
      while(true) {
         if (var3 >= 5) {
            var5 = var1;
            if (var2 >= 7) {
               float var6 = (float)var2 / 7.0F;
               float var7 = var6 / 2.0F;
               var5 = var1;
               if (Math.abs(var6 - (float)var0[0]) < var7) {
                  var5 = var1;
                  if (Math.abs(var6 - (float)var0[1]) < var7) {
                     var5 = var1;
                     if (Math.abs(3.0F * var6 - (float)var0[2]) < 3.0F * var7) {
                        var5 = var1;
                        if (Math.abs(var6 - (float)var0[3]) < var7) {
                           var5 = var1;
                           if (Math.abs(var6 - (float)var0[4]) < var7) {
                              var5 = true;
                           }
                        }
                     }
                  }
               }
            }
            break;
         }

         int var4 = var0[var3];
         if (var4 == 0) {
            var5 = var1;
            break;
         }

         var2 += var4;
         ++var3;
      }

      return var5;
   }

   private int[] getCrossCheckStateCount() {
      this.crossCheckStateCount[0] = 0;
      this.crossCheckStateCount[1] = 0;
      this.crossCheckStateCount[2] = 0;
      this.crossCheckStateCount[3] = 0;
      this.crossCheckStateCount[4] = 0;
      return this.crossCheckStateCount;
   }

   private boolean haveMultiplyConfirmedCenters() {
      boolean var1 = false;
      int var2 = 0;
      float var3 = 0.0F;
      int var4 = this.possibleCenters.size();
      Iterator var5 = this.possibleCenters.iterator();

      while(var5.hasNext()) {
         FinderPattern var6 = (FinderPattern)var5.next();
         if (var6.getCount() >= 2) {
            ++var2;
            var3 += var6.getEstimatedModuleSize();
         }
      }

      if (var2 >= 3) {
         float var7 = var3 / (float)var4;
         float var8 = 0.0F;

         for(var5 = this.possibleCenters.iterator(); var5.hasNext(); var8 += Math.abs(((FinderPattern)var5.next()).getEstimatedModuleSize() - var7)) {
         }

         if (var8 <= 0.05F * var3) {
            var1 = true;
         }
      }

      return var1;
   }

   private FinderPattern[] selectBestPatterns() throws NotFoundException {
      int var1 = this.possibleCenters.size();
      if (var1 < 3) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         float var3;
         Iterator var4;
         if (var1 > 3) {
            float var2 = 0.0F;
            var3 = 0.0F;

            float var5;
            for(var4 = this.possibleCenters.iterator(); var4.hasNext(); var3 += var5 * var5) {
               var5 = ((FinderPattern)var4.next()).getEstimatedModuleSize();
               var2 += var5;
            }

            var2 /= (float)var1;
            var3 = (float)Math.sqrt((double)(var3 / (float)var1 - var2 * var2));
            Collections.sort(this.possibleCenters, new FinderPatternFinder.FurthestFromAverageComparator(var2));
            var3 = Math.max(0.2F * var2, var3);

            int var6;
            for(var1 = 0; var1 < this.possibleCenters.size() && this.possibleCenters.size() > 3; var1 = var6 + 1) {
               var6 = var1;
               if (Math.abs(((FinderPattern)this.possibleCenters.get(var1)).getEstimatedModuleSize() - var2) > var3) {
                  this.possibleCenters.remove(var1);
                  var6 = var1 - 1;
               }
            }
         }

         if (this.possibleCenters.size() > 3) {
            var3 = 0.0F;

            for(var4 = this.possibleCenters.iterator(); var4.hasNext(); var3 += ((FinderPattern)var4.next()).getEstimatedModuleSize()) {
            }

            var3 /= (float)this.possibleCenters.size();
            Collections.sort(this.possibleCenters, new FinderPatternFinder.CenterComparator(var3));
            this.possibleCenters.subList(3, this.possibleCenters.size()).clear();
         }

         return new FinderPattern[]{(FinderPattern)this.possibleCenters.get(0), (FinderPattern)this.possibleCenters.get(1), (FinderPattern)this.possibleCenters.get(2)};
      }
   }

   final FinderPatternInfo find(Map var1) throws NotFoundException {
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

      int var4 = this.image.getHeight();
      int var5 = this.image.getWidth();
      int var6 = var4 * 3 / 228;
      if (var6 < 3 || var2) {
         var6 = 3;
      }

      boolean var7 = false;
      int[] var12 = new int[5];

      int var14;
      for(int var8 = var6 - 1; var8 < var4 && !var7; var6 = var14) {
         var12[0] = 0;
         var12[1] = 0;
         var12[2] = 0;
         var12[3] = 0;
         var12[4] = 0;
         var14 = 0;

         int var9;
         boolean var11;
         for(var9 = 0; var9 < var5; ++var9) {
            int var10002;
            if (this.image.get(var9, var8)) {
               int var10 = var14;
               if ((var14 & 1) == 1) {
                  var10 = var14 + 1;
               }

               var10002 = var12[var10]++;
               var14 = var10;
            } else if ((var14 & 1) == 0) {
               if (var14 == 4) {
                  if (foundPatternCross(var12)) {
                     if (this.handlePossibleCenter(var12, var8, var9, var3)) {
                        byte var15 = 2;
                        if (this.hasSkipped) {
                           var11 = this.haveMultiplyConfirmedCenters();
                           var6 = var8;
                        } else {
                           var14 = this.findRowSkip();
                           var11 = var7;
                           var6 = var8;
                           if (var14 > var12[2]) {
                              var6 = var8 + (var14 - var12[2] - 2);
                              var9 = var5 - 1;
                              var11 = var7;
                           }
                        }

                        var14 = 0;
                        var12[0] = 0;
                        var12[1] = 0;
                        var12[2] = 0;
                        var12[3] = 0;
                        var12[4] = 0;
                        var7 = var11;
                        var8 = var6;
                        var6 = var15;
                     } else {
                        var12[0] = var12[2];
                        var12[1] = var12[3];
                        var12[2] = var12[4];
                        var12[3] = 1;
                        var12[4] = 0;
                        var14 = 3;
                     }
                  } else {
                     var12[0] = var12[2];
                     var12[1] = var12[3];
                     var12[2] = var12[4];
                     var12[3] = 1;
                     var12[4] = 0;
                     var14 = 3;
                  }
               } else {
                  ++var14;
                  var10002 = var12[var14]++;
               }
            } else {
               var10002 = var12[var14]++;
            }
         }

         var11 = var7;
         var14 = var6;
         if (foundPatternCross(var12)) {
            var11 = var7;
            var14 = var6;
            if (this.handlePossibleCenter(var12, var8, var5, var3)) {
               var9 = var12[0];
               var11 = var7;
               var14 = var9;
               if (this.hasSkipped) {
                  var11 = this.haveMultiplyConfirmedCenters();
                  var14 = var9;
               }
            }
         }

         var8 += var14;
         var7 = var11;
      }

      FinderPattern[] var13 = this.selectBestPatterns();
      ResultPoint.orderBestPatterns(var13);
      return new FinderPatternInfo(var13);
   }

   protected final BitMatrix getImage() {
      return this.image;
   }

   protected final List getPossibleCenters() {
      return this.possibleCenters;
   }

   protected final boolean handlePossibleCenter(int[] var1, int var2, int var3, boolean var4) {
      int var5 = var1[0] + var1[1] + var1[2] + var1[3] + var1[4];
      float var6 = centerFromEnd(var1, var3);
      float var7 = this.crossCheckVertical(var2, (int)var6, var1[2], var5);
      if (!Float.isNaN(var7)) {
         var6 = this.crossCheckHorizontal((int)var6, (int)var7, var1[2], var5);
         if (!Float.isNaN(var6) && (!var4 || this.crossCheckDiagonal((int)var7, (int)var6, var1[2], var5))) {
            float var8 = (float)var5 / 7.0F;
            boolean var11 = false;
            var3 = 0;

            FinderPattern var9;
            boolean var10;
            while(true) {
               var10 = var11;
               if (var3 >= this.possibleCenters.size()) {
                  break;
               }

               var9 = (FinderPattern)this.possibleCenters.get(var3);
               if (var9.aboutEquals(var8, var7, var6)) {
                  this.possibleCenters.set(var3, var9.combineEstimate(var7, var6, var8));
                  var10 = true;
                  break;
               }

               ++var3;
            }

            if (!var10) {
               var9 = new FinderPattern(var6, var7, var8);
               this.possibleCenters.add(var9);
               if (this.resultPointCallback != null) {
                  this.resultPointCallback.foundPossibleResultPoint(var9);
               }
            }

            var4 = true;
            return var4;
         }
      }

      var4 = false;
      return var4;
   }

   private static final class CenterComparator implements Serializable, Comparator {
      private final float average;

      private CenterComparator(float var1) {
         this.average = var1;
      }

      // $FF: synthetic method
      CenterComparator(float var1, Object var2) {
         this(var1);
      }

      public int compare(FinderPattern var1, FinderPattern var2) {
         int var5;
         if (var2.getCount() == var1.getCount()) {
            float var3 = Math.abs(var2.getEstimatedModuleSize() - this.average);
            float var4 = Math.abs(var1.getEstimatedModuleSize() - this.average);
            if (var3 < var4) {
               var5 = 1;
            } else if (var3 == var4) {
               var5 = 0;
            } else {
               var5 = -1;
            }
         } else {
            var5 = var2.getCount() - var1.getCount();
         }

         return var5;
      }
   }

   private static final class FurthestFromAverageComparator implements Serializable, Comparator {
      private final float average;

      private FurthestFromAverageComparator(float var1) {
         this.average = var1;
      }

      // $FF: synthetic method
      FurthestFromAverageComparator(float var1, Object var2) {
         this(var1);
      }

      public int compare(FinderPattern var1, FinderPattern var2) {
         float var3 = Math.abs(var2.getEstimatedModuleSize() - this.average);
         float var4 = Math.abs(var1.getEstimatedModuleSize() - this.average);
         byte var5;
         if (var3 < var4) {
            var5 = -1;
         } else if (var3 == var4) {
            var5 = 0;
         } else {
            var5 = 1;
         }

         return var5;
      }
   }
}

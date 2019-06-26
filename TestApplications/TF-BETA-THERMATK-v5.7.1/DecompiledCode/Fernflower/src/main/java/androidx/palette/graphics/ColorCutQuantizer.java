package androidx.palette.graphics;

import android.graphics.Color;
import android.util.TimingLogger;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

final class ColorCutQuantizer {
   private static final Comparator VBOX_COMPARATOR_VOLUME = new Comparator() {
      public int compare(ColorCutQuantizer.Vbox var1, ColorCutQuantizer.Vbox var2) {
         return var2.getVolume() - var1.getVolume();
      }
   };
   final int[] mColors;
   final Palette.Filter[] mFilters;
   final int[] mHistogram;
   final List mQuantizedColors;
   private final float[] mTempHsl = new float[3];
   final TimingLogger mTimingLogger = null;

   ColorCutQuantizer(int[] var1, int var2, Palette.Filter[] var3) {
      this.mFilters = var3;
      int[] var9 = new int['耀'];
      this.mHistogram = var9;
      byte var4 = 0;

      int var5;
      int var6;
      for(var5 = 0; var5 < var1.length; ++var5) {
         var6 = quantizeFromRgb888(var1[var5]);
         var1[var5] = var6;
         int var10002 = var9[var6]++;
      }

      var6 = 0;

      int var7;
      for(var5 = 0; var6 < var9.length; var5 = var7) {
         if (var9[var6] > 0 && this.shouldIgnoreColor(var6)) {
            var9[var6] = 0;
         }

         var7 = var5;
         if (var9[var6] > 0) {
            var7 = var5 + 1;
         }

         ++var6;
      }

      var1 = new int[var5];
      this.mColors = var1;
      var6 = 0;

      for(int var8 = 0; var6 < var9.length; var8 = var7) {
         var7 = var8;
         if (var9[var6] > 0) {
            var1[var8] = var6;
            var7 = var8 + 1;
         }

         ++var6;
      }

      if (var5 <= var2) {
         this.mQuantizedColors = new ArrayList();
         var5 = var1.length;

         for(var2 = var4; var2 < var5; ++var2) {
            var6 = var1[var2];
            this.mQuantizedColors.add(new Palette.Swatch(approximateToRgb888(var6), var9[var6]));
         }
      } else {
         this.mQuantizedColors = this.quantizePixels(var2);
      }

   }

   private static int approximateToRgb888(int var0) {
      return approximateToRgb888(quantizedRed(var0), quantizedGreen(var0), quantizedBlue(var0));
   }

   static int approximateToRgb888(int var0, int var1, int var2) {
      return Color.rgb(modifyWordWidth(var0, 5, 8), modifyWordWidth(var1, 5, 8), modifyWordWidth(var2, 5, 8));
   }

   private List generateAverageColors(Collection var1) {
      ArrayList var2 = new ArrayList(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Palette.Swatch var4 = ((ColorCutQuantizer.Vbox)var3.next()).getAverageColor();
         if (!this.shouldIgnoreColor(var4)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   static void modifySignificantOctet(int[] var0, int var1, int var2, int var3) {
      if (var1 != -3) {
         int var4 = var2;
         int var5;
         if (var1 != -2) {
            if (var1 == -1) {
               while(var2 <= var3) {
                  var4 = var0[var2];
                  var5 = quantizedBlue(var4);
                  var1 = quantizedGreen(var4);
                  var0[var2] = quantizedRed(var4) | var5 << 10 | var1 << 5;
                  ++var2;
               }
            }
         } else {
            while(var4 <= var3) {
               var1 = var0[var4];
               var2 = quantizedGreen(var1);
               var5 = quantizedRed(var1);
               var0[var4] = quantizedBlue(var1) | var2 << 10 | var5 << 5;
               ++var4;
            }
         }
      }

   }

   private static int modifyWordWidth(int var0, int var1, int var2) {
      if (var2 > var1) {
         var0 <<= var2 - var1;
      } else {
         var0 >>= var1 - var2;
      }

      return var0 & (1 << var2) - 1;
   }

   private static int quantizeFromRgb888(int var0) {
      int var1 = modifyWordWidth(Color.red(var0), 8, 5);
      int var2 = modifyWordWidth(Color.green(var0), 8, 5);
      return modifyWordWidth(Color.blue(var0), 8, 5) | var1 << 10 | var2 << 5;
   }

   private List quantizePixels(int var1) {
      PriorityQueue var2 = new PriorityQueue(var1, VBOX_COMPARATOR_VOLUME);
      var2.offer(new ColorCutQuantizer.Vbox(0, this.mColors.length - 1));
      this.splitBoxes(var2, var1);
      return this.generateAverageColors(var2);
   }

   static int quantizedBlue(int var0) {
      return var0 & 31;
   }

   static int quantizedGreen(int var0) {
      return var0 >> 5 & 31;
   }

   static int quantizedRed(int var0) {
      return var0 >> 10 & 31;
   }

   private boolean shouldIgnoreColor(int var1) {
      var1 = approximateToRgb888(var1);
      ColorUtils.colorToHSL(var1, this.mTempHsl);
      return this.shouldIgnoreColor(var1, this.mTempHsl);
   }

   private boolean shouldIgnoreColor(int var1, float[] var2) {
      Palette.Filter[] var3 = this.mFilters;
      if (var3 != null && var3.length > 0) {
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            if (!this.mFilters[var5].isAllowed(var1, var2)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean shouldIgnoreColor(Palette.Swatch var1) {
      return this.shouldIgnoreColor(var1.getRgb(), var1.getHsl());
   }

   private void splitBoxes(PriorityQueue var1, int var2) {
      while(true) {
         if (var1.size() < var2) {
            ColorCutQuantizer.Vbox var3 = (ColorCutQuantizer.Vbox)var1.poll();
            if (var3 != null && var3.canSplit()) {
               var1.offer(var3.splitBox());
               var1.offer(var3);
               continue;
            }
         }

         return;
      }
   }

   List getQuantizedColors() {
      return this.mQuantizedColors;
   }

   private class Vbox {
      private int mLowerIndex;
      private int mMaxBlue;
      private int mMaxGreen;
      private int mMaxRed;
      private int mMinBlue;
      private int mMinGreen;
      private int mMinRed;
      private int mPopulation;
      private int mUpperIndex;

      Vbox(int var2, int var3) {
         this.mLowerIndex = var2;
         this.mUpperIndex = var3;
         this.fitBox();
      }

      final boolean canSplit() {
         int var1 = this.getColorCount();
         boolean var2 = true;
         if (var1 <= 1) {
            var2 = false;
         }

         return var2;
      }

      final int findSplitPoint() {
         int var1 = this.getLongestColorDimension();
         ColorCutQuantizer var2 = ColorCutQuantizer.this;
         int[] var3 = var2.mColors;
         int[] var7 = var2.mHistogram;
         ColorCutQuantizer.modifySignificantOctet(var3, var1, this.mLowerIndex, this.mUpperIndex);
         Arrays.sort(var3, this.mLowerIndex, this.mUpperIndex + 1);
         ColorCutQuantizer.modifySignificantOctet(var3, var1, this.mLowerIndex, this.mUpperIndex);
         int var4 = this.mPopulation / 2;
         int var5 = this.mLowerIndex;
         var1 = 0;

         while(true) {
            int var6 = this.mUpperIndex;
            if (var5 > var6) {
               return this.mLowerIndex;
            }

            var1 += var7[var3[var5]];
            if (var1 >= var4) {
               return Math.min(var6 - 1, var5);
            }

            ++var5;
         }
      }

      final void fitBox() {
         ColorCutQuantizer var1 = ColorCutQuantizer.this;
         int[] var2 = var1.mColors;
         int[] var15 = var1.mHistogram;
         int var3 = this.mLowerIndex;
         int var4 = Integer.MAX_VALUE;
         int var5 = Integer.MIN_VALUE;
         int var6 = Integer.MAX_VALUE;
         int var7 = Integer.MIN_VALUE;
         int var8 = Integer.MAX_VALUE;
         int var9 = Integer.MIN_VALUE;

         int var10;
         int var12;
         for(var10 = 0; var3 <= this.mUpperIndex; var10 = var12) {
            int var11 = var2[var3];
            var12 = var10 + var15[var11];
            int var13 = ColorCutQuantizer.quantizedRed(var11);
            int var14 = ColorCutQuantizer.quantizedGreen(var11);
            var11 = ColorCutQuantizer.quantizedBlue(var11);
            var10 = var5;
            if (var13 > var5) {
               var10 = var13;
            }

            var5 = var4;
            if (var13 < var4) {
               var5 = var13;
            }

            var13 = var7;
            if (var14 > var7) {
               var13 = var14;
            }

            var7 = var6;
            if (var14 < var6) {
               var7 = var14;
            }

            var14 = var9;
            if (var11 > var9) {
               var14 = var11;
            }

            var9 = var8;
            if (var11 < var8) {
               var9 = var11;
            }

            ++var3;
            var4 = var5;
            var5 = var10;
            var6 = var7;
            var7 = var13;
            var8 = var9;
            var9 = var14;
         }

         this.mMinRed = var4;
         this.mMaxRed = var5;
         this.mMinGreen = var6;
         this.mMaxGreen = var7;
         this.mMinBlue = var8;
         this.mMaxBlue = var9;
         this.mPopulation = var10;
      }

      final Palette.Swatch getAverageColor() {
         ColorCutQuantizer var1 = ColorCutQuantizer.this;
         int[] var2 = var1.mColors;
         int[] var12 = var1.mHistogram;
         int var3 = this.mLowerIndex;
         int var4 = 0;
         int var5 = 0;
         int var6 = 0;

         int var7;
         for(var7 = 0; var3 <= this.mUpperIndex; ++var3) {
            int var8 = var2[var3];
            int var9 = var12[var8];
            var5 += var9;
            var4 += ColorCutQuantizer.quantizedRed(var8) * var9;
            var6 += ColorCutQuantizer.quantizedGreen(var8) * var9;
            var7 += var9 * ColorCutQuantizer.quantizedBlue(var8);
         }

         float var10 = (float)var4;
         float var11 = (float)var5;
         return new Palette.Swatch(ColorCutQuantizer.approximateToRgb888(Math.round(var10 / var11), Math.round((float)var6 / var11), Math.round((float)var7 / var11)), var5);
      }

      final int getColorCount() {
         return this.mUpperIndex + 1 - this.mLowerIndex;
      }

      final int getLongestColorDimension() {
         int var1 = this.mMaxRed - this.mMinRed;
         int var2 = this.mMaxGreen - this.mMinGreen;
         int var3 = this.mMaxBlue - this.mMinBlue;
         if (var1 >= var2 && var1 >= var3) {
            return -3;
         } else {
            return var2 >= var1 && var2 >= var3 ? -2 : -1;
         }
      }

      final int getVolume() {
         return (this.mMaxRed - this.mMinRed + 1) * (this.mMaxGreen - this.mMinGreen + 1) * (this.mMaxBlue - this.mMinBlue + 1);
      }

      final ColorCutQuantizer.Vbox splitBox() {
         if (this.canSplit()) {
            int var1 = this.findSplitPoint();
            ColorCutQuantizer.Vbox var2 = ColorCutQuantizer.this.new Vbox(var1 + 1, this.mUpperIndex);
            this.mUpperIndex = var1;
            this.fitBox();
            return var2;
         } else {
            throw new IllegalStateException("Can not split a box with only 1 color");
         }
      }
   }
}

package androidx.palette.graphics;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.SparseBooleanArray;
import androidx.collection.ArrayMap;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class Palette {
   static final Palette.Filter DEFAULT_FILTER = new Palette.Filter() {
      private boolean isBlack(float[] var1) {
         boolean var2;
         if (var1[2] <= 0.05F) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      private boolean isNearRedILine(float[] var1) {
         boolean var2 = false;
         boolean var3 = var2;
         if (var1[0] >= 10.0F) {
            var3 = var2;
            if (var1[0] <= 37.0F) {
               var3 = var2;
               if (var1[1] <= 0.82F) {
                  var3 = true;
               }
            }
         }

         return var3;
      }

      private boolean isWhite(float[] var1) {
         boolean var2;
         if (var1[2] >= 0.95F) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public boolean isAllowed(int var1, float[] var2) {
         boolean var3;
         if (!this.isWhite(var2) && !this.isBlack(var2) && !this.isNearRedILine(var2)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   };
   private final Palette.Swatch mDominantSwatch;
   private final Map mSelectedSwatches;
   private final List mSwatches;
   private final List mTargets;
   private final SparseBooleanArray mUsedColors;

   Palette(List var1, List var2) {
      this.mSwatches = var1;
      this.mTargets = var2;
      this.mUsedColors = new SparseBooleanArray();
      this.mSelectedSwatches = new ArrayMap();
      this.mDominantSwatch = this.findDominantSwatch();
   }

   private Palette.Swatch findDominantSwatch() {
      int var1 = this.mSwatches.size();
      int var2 = Integer.MIN_VALUE;
      Palette.Swatch var3 = null;

      int var6;
      for(int var4 = 0; var4 < var1; var2 = var6) {
         Palette.Swatch var5 = (Palette.Swatch)this.mSwatches.get(var4);
         var6 = var2;
         if (var5.getPopulation() > var2) {
            var6 = var5.getPopulation();
            var3 = var5;
         }

         ++var4;
      }

      return var3;
   }

   public static Palette.Builder from(Bitmap var0) {
      return new Palette.Builder(var0);
   }

   private float generateScore(Palette.Swatch var1, Target var2) {
      float[] var3 = var1.getHsl();
      Palette.Swatch var4 = this.mDominantSwatch;
      int var5;
      if (var4 != null) {
         var5 = var4.getPopulation();
      } else {
         var5 = 1;
      }

      float var6 = var2.getSaturationWeight();
      float var7 = 0.0F;
      if (var6 > 0.0F) {
         var6 = var2.getSaturationWeight();
         var6 = (1.0F - Math.abs(var3[1] - var2.getTargetSaturation())) * var6;
      } else {
         var6 = 0.0F;
      }

      float var8;
      if (var2.getLightnessWeight() > 0.0F) {
         var8 = var2.getLightnessWeight() * (1.0F - Math.abs(var3[2] - var2.getTargetLightness()));
      } else {
         var8 = 0.0F;
      }

      if (var2.getPopulationWeight() > 0.0F) {
         var7 = var2.getPopulationWeight() * ((float)var1.getPopulation() / (float)var5);
      }

      return var6 + var8 + var7;
   }

   private Palette.Swatch generateScoredTarget(Target var1) {
      Palette.Swatch var2 = this.getMaxScoredSwatchForTarget(var1);
      if (var2 != null && var1.isExclusive()) {
         this.mUsedColors.append(var2.getRgb(), true);
      }

      return var2;
   }

   private Palette.Swatch getMaxScoredSwatchForTarget(Target var1) {
      int var2 = this.mSwatches.size();
      float var3 = 0.0F;
      Palette.Swatch var4 = null;

      Palette.Swatch var8;
      for(int var5 = 0; var5 < var2; var4 = var8) {
         Palette.Swatch var6 = (Palette.Swatch)this.mSwatches.get(var5);
         float var7 = var3;
         var8 = var4;
         if (this.shouldBeScoredForTarget(var6, var1)) {
            label25: {
               float var9 = this.generateScore(var6, var1);
               if (var4 != null) {
                  var7 = var3;
                  var8 = var4;
                  if (var9 <= var3) {
                     break label25;
                  }
               }

               var8 = var6;
               var7 = var9;
            }
         }

         ++var5;
         var3 = var7;
      }

      return var4;
   }

   private boolean shouldBeScoredForTarget(Palette.Swatch var1, Target var2) {
      float[] var3 = var1.getHsl();
      boolean var4 = true;
      if (var3[1] < var2.getMinimumSaturation() || var3[1] > var2.getMaximumSaturation() || var3[2] < var2.getMinimumLightness() || var3[2] > var2.getMaximumLightness() || this.mUsedColors.get(var1.getRgb())) {
         var4 = false;
      }

      return var4;
   }

   void generate() {
      int var1 = this.mTargets.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         Target var3 = (Target)this.mTargets.get(var2);
         var3.normalizeWeights();
         this.mSelectedSwatches.put(var3, this.generateScoredTarget(var3));
      }

      this.mUsedColors.clear();
   }

   public int getColorForTarget(Target var1, int var2) {
      Palette.Swatch var3 = this.getSwatchForTarget(var1);
      if (var3 != null) {
         var2 = var3.getRgb();
      }

      return var2;
   }

   public int getDarkMutedColor(int var1) {
      return this.getColorForTarget(Target.DARK_MUTED, var1);
   }

   public Palette.Swatch getSwatchForTarget(Target var1) {
      return (Palette.Swatch)this.mSelectedSwatches.get(var1);
   }

   public static final class Builder {
      private final Bitmap mBitmap;
      private final List mFilters = new ArrayList();
      private int mMaxColors = 16;
      private Rect mRegion;
      private int mResizeArea = 12544;
      private int mResizeMaxDimension = -1;
      private final List mSwatches;
      private final List mTargets = new ArrayList();

      public Builder(Bitmap var1) {
         if (var1 != null && !var1.isRecycled()) {
            this.mFilters.add(Palette.DEFAULT_FILTER);
            this.mBitmap = var1;
            this.mSwatches = null;
            this.mTargets.add(Target.LIGHT_VIBRANT);
            this.mTargets.add(Target.VIBRANT);
            this.mTargets.add(Target.DARK_VIBRANT);
            this.mTargets.add(Target.LIGHT_MUTED);
            this.mTargets.add(Target.MUTED);
            this.mTargets.add(Target.DARK_MUTED);
         } else {
            throw new IllegalArgumentException("Bitmap is not valid");
         }
      }

      private int[] getPixelsFromBitmap(Bitmap var1) {
         int var2 = var1.getWidth();
         int var3 = var1.getHeight();
         int[] var4 = new int[var2 * var3];
         var1.getPixels(var4, 0, var2, 0, 0, var2, var3);
         Rect var8 = this.mRegion;
         if (var8 == null) {
            return var4;
         } else {
            int var5 = var8.width();
            int var6 = this.mRegion.height();
            int[] var9 = new int[var5 * var6];

            for(var3 = 0; var3 < var6; ++var3) {
               Rect var7 = this.mRegion;
               System.arraycopy(var4, (var7.top + var3) * var2 + var7.left, var9, var3 * var5, var5);
            }

            return var9;
         }
      }

      private Bitmap scaleBitmapDown(Bitmap var1) {
         int var2 = this.mResizeArea;
         double var3 = -1.0D;
         int var5;
         double var6;
         if (var2 > 0) {
            var5 = var1.getWidth() * var1.getHeight();
            var2 = this.mResizeArea;
            var6 = var3;
            if (var5 > var2) {
               var3 = (double)var2;
               var6 = (double)var5;
               Double.isNaN(var3);
               Double.isNaN(var6);
               var6 = Math.sqrt(var3 / var6);
            }
         } else {
            var6 = var3;
            if (this.mResizeMaxDimension > 0) {
               var5 = Math.max(var1.getWidth(), var1.getHeight());
               var2 = this.mResizeMaxDimension;
               var6 = var3;
               if (var5 > var2) {
                  var6 = (double)var2;
                  var3 = (double)var5;
                  Double.isNaN(var6);
                  Double.isNaN(var3);
                  var6 /= var3;
               }
            }
         }

         if (var6 <= 0.0D) {
            return var1;
         } else {
            var3 = (double)var1.getWidth();
            Double.isNaN(var3);
            var2 = (int)Math.ceil(var3 * var6);
            var3 = (double)var1.getHeight();
            Double.isNaN(var3);
            return Bitmap.createScaledBitmap(var1, var2, (int)Math.ceil(var3 * var6), false);
         }
      }

      public Palette generate() {
         Bitmap var1 = this.mBitmap;
         List var11;
         if (var1 != null) {
            Bitmap var2 = this.scaleBitmapDown(var1);
            Rect var9 = this.mRegion;
            if (var2 != this.mBitmap && var9 != null) {
               double var3 = (double)var2.getWidth();
               double var5 = (double)this.mBitmap.getWidth();
               Double.isNaN(var3);
               Double.isNaN(var5);
               var3 /= var5;
               var5 = (double)var9.left;
               Double.isNaN(var5);
               var9.left = (int)Math.floor(var5 * var3);
               var5 = (double)var9.top;
               Double.isNaN(var5);
               var9.top = (int)Math.floor(var5 * var3);
               var5 = (double)var9.right;
               Double.isNaN(var5);
               var9.right = Math.min((int)Math.ceil(var5 * var3), var2.getWidth());
               var5 = (double)var9.bottom;
               Double.isNaN(var5);
               var9.bottom = Math.min((int)Math.ceil(var5 * var3), var2.getHeight());
            }

            int[] var7 = this.getPixelsFromBitmap(var2);
            int var8 = this.mMaxColors;
            Palette.Filter[] var10;
            if (this.mFilters.isEmpty()) {
               var10 = null;
            } else {
               var11 = this.mFilters;
               var10 = (Palette.Filter[])var11.toArray(new Palette.Filter[var11.size()]);
            }

            ColorCutQuantizer var12 = new ColorCutQuantizer(var7, var8, var10);
            if (var2 != this.mBitmap) {
               var2.recycle();
            }

            var11 = var12.getQuantizedColors();
         } else {
            var11 = this.mSwatches;
            if (var11 == null) {
               throw new AssertionError();
            }
         }

         Palette var13 = new Palette(var11, this.mTargets);
         var13.generate();
         return var13;
      }
   }

   public interface Filter {
      boolean isAllowed(int var1, float[] var2);
   }

   public static final class Swatch {
      private final int mBlue;
      private int mBodyTextColor;
      private boolean mGeneratedTextColors;
      private final int mGreen;
      private float[] mHsl;
      private final int mPopulation;
      private final int mRed;
      private final int mRgb;
      private int mTitleTextColor;

      public Swatch(int var1, int var2) {
         this.mRed = Color.red(var1);
         this.mGreen = Color.green(var1);
         this.mBlue = Color.blue(var1);
         this.mRgb = var1;
         this.mPopulation = var2;
      }

      private void ensureTextColorsGenerated() {
         if (!this.mGeneratedTextColors) {
            int var1 = ColorUtils.calculateMinimumAlpha(-1, this.mRgb, 4.5F);
            int var2 = ColorUtils.calculateMinimumAlpha(-1, this.mRgb, 3.0F);
            if (var1 != -1 && var2 != -1) {
               this.mBodyTextColor = ColorUtils.setAlphaComponent(-1, var1);
               this.mTitleTextColor = ColorUtils.setAlphaComponent(-1, var2);
               this.mGeneratedTextColors = true;
               return;
            }

            int var3 = ColorUtils.calculateMinimumAlpha(-16777216, this.mRgb, 4.5F);
            int var4 = ColorUtils.calculateMinimumAlpha(-16777216, this.mRgb, 3.0F);
            if (var3 != -1 && var4 != -1) {
               this.mBodyTextColor = ColorUtils.setAlphaComponent(-16777216, var3);
               this.mTitleTextColor = ColorUtils.setAlphaComponent(-16777216, var4);
               this.mGeneratedTextColors = true;
               return;
            }

            if (var1 != -1) {
               var3 = ColorUtils.setAlphaComponent(-1, var1);
            } else {
               var3 = ColorUtils.setAlphaComponent(-16777216, var3);
            }

            this.mBodyTextColor = var3;
            if (var2 != -1) {
               var3 = ColorUtils.setAlphaComponent(-1, var2);
            } else {
               var3 = ColorUtils.setAlphaComponent(-16777216, var4);
            }

            this.mTitleTextColor = var3;
            this.mGeneratedTextColors = true;
         }

      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && Palette.Swatch.class == var1.getClass()) {
            Palette.Swatch var3 = (Palette.Swatch)var1;
            if (this.mPopulation != var3.mPopulation || this.mRgb != var3.mRgb) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public int getBodyTextColor() {
         this.ensureTextColorsGenerated();
         return this.mBodyTextColor;
      }

      public float[] getHsl() {
         if (this.mHsl == null) {
            this.mHsl = new float[3];
         }

         ColorUtils.RGBToHSL(this.mRed, this.mGreen, this.mBlue, this.mHsl);
         return this.mHsl;
      }

      public int getPopulation() {
         return this.mPopulation;
      }

      public int getRgb() {
         return this.mRgb;
      }

      public int getTitleTextColor() {
         this.ensureTextColorsGenerated();
         return this.mTitleTextColor;
      }

      public int hashCode() {
         return this.mRgb * 31 + this.mPopulation;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(Palette.Swatch.class.getSimpleName());
         var1.append(" [RGB: #");
         var1.append(Integer.toHexString(this.getRgb()));
         var1.append(']');
         var1.append(" [HSL: ");
         var1.append(Arrays.toString(this.getHsl()));
         var1.append(']');
         var1.append(" [Population: ");
         var1.append(this.mPopulation);
         var1.append(']');
         var1.append(" [Title Text: #");
         var1.append(Integer.toHexString(this.getTitleTextColor()));
         var1.append(']');
         var1.append(" [Body Text: #");
         var1.append(Integer.toHexString(this.getBodyTextColor()));
         var1.append(']');
         return var1.toString();
      }
   }
}

package com.bumptech.glide.load.resource.bitmap;

public abstract class DownsampleStrategy {
   public static final DownsampleStrategy AT_LEAST = new DownsampleStrategy.AtLeast();
   public static final DownsampleStrategy AT_MOST = new DownsampleStrategy.AtMost();
   public static final DownsampleStrategy CENTER_INSIDE = new DownsampleStrategy.CenterInside();
   public static final DownsampleStrategy CENTER_OUTSIDE = new DownsampleStrategy.CenterOutside();
   public static final DownsampleStrategy DEFAULT;
   public static final DownsampleStrategy FIT_CENTER = new DownsampleStrategy.FitCenter();
   public static final DownsampleStrategy NONE = new DownsampleStrategy.None();

   static {
      DEFAULT = CENTER_OUTSIDE;
   }

   public abstract DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int var1, int var2, int var3, int var4);

   public abstract float getScaleFactor(int var1, int var2, int var3, int var4);

   private static class AtLeast extends DownsampleStrategy {
      AtLeast() {
      }

      public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int var1, int var2, int var3, int var4) {
         return DownsampleStrategy.SampleSizeRounding.QUALITY;
      }

      public float getScaleFactor(int var1, int var2, int var3, int var4) {
         var1 = Math.min(var2 / var4, var1 / var3);
         float var5 = 1.0F;
         if (var1 != 0) {
            var5 = 1.0F / (float)Integer.highestOneBit(var1);
         }

         return var5;
      }
   }

   private static class AtMost extends DownsampleStrategy {
      AtMost() {
      }

      public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int var1, int var2, int var3, int var4) {
         return DownsampleStrategy.SampleSizeRounding.MEMORY;
      }

      public float getScaleFactor(int var1, int var2, int var3, int var4) {
         var3 = (int)Math.ceil((double)Math.max((float)var2 / (float)var4, (float)var1 / (float)var3));
         var2 = Integer.highestOneBit(var3);
         byte var5 = 1;
         var2 = Math.max(1, var2);
         if (var2 >= var3) {
            var5 = 0;
         }

         return 1.0F / (float)(var2 << var5);
      }
   }

   private static class CenterInside extends DownsampleStrategy {
      CenterInside() {
      }

      public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int var1, int var2, int var3, int var4) {
         return DownsampleStrategy.SampleSizeRounding.QUALITY;
      }

      public float getScaleFactor(int var1, int var2, int var3, int var4) {
         return Math.min(1.0F, FIT_CENTER.getScaleFactor(var1, var2, var3, var4));
      }
   }

   private static class CenterOutside extends DownsampleStrategy {
      CenterOutside() {
      }

      public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int var1, int var2, int var3, int var4) {
         return DownsampleStrategy.SampleSizeRounding.QUALITY;
      }

      public float getScaleFactor(int var1, int var2, int var3, int var4) {
         return Math.max((float)var3 / (float)var1, (float)var4 / (float)var2);
      }
   }

   private static class FitCenter extends DownsampleStrategy {
      FitCenter() {
      }

      public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int var1, int var2, int var3, int var4) {
         return DownsampleStrategy.SampleSizeRounding.QUALITY;
      }

      public float getScaleFactor(int var1, int var2, int var3, int var4) {
         return Math.min((float)var3 / (float)var1, (float)var4 / (float)var2);
      }
   }

   private static class None extends DownsampleStrategy {
      None() {
      }

      public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int var1, int var2, int var3, int var4) {
         return DownsampleStrategy.SampleSizeRounding.QUALITY;
      }

      public float getScaleFactor(int var1, int var2, int var3, int var4) {
         return 1.0F;
      }
   }

   public static enum SampleSizeRounding {
      MEMORY,
      QUALITY;
   }
}

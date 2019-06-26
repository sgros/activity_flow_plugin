package com.google.android.exoplayer2.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SlidingPercentile {
   private static final Comparator INDEX_COMPARATOR;
   private static final int MAX_RECYCLED_SAMPLES = 5;
   private static final int SORT_ORDER_BY_INDEX = 1;
   private static final int SORT_ORDER_BY_VALUE = 0;
   private static final int SORT_ORDER_NONE = -1;
   private static final Comparator VALUE_COMPARATOR;
   private int currentSortOrder;
   private final int maxWeight;
   private int nextSampleIndex;
   private int recycledSampleCount;
   private final SlidingPercentile.Sample[] recycledSamples;
   private final ArrayList samples;
   private int totalWeight;

   static {
      INDEX_COMPARATOR = _$$Lambda$SlidingPercentile$IHMSNRVWSvKImU2XQD2j4ISb4_U.INSTANCE;
      VALUE_COMPARATOR = _$$Lambda$SlidingPercentile$UufTq1Ma5g1qQu0Vqc6f2CE68bE.INSTANCE;
   }

   public SlidingPercentile(int var1) {
      this.maxWeight = var1;
      this.recycledSamples = new SlidingPercentile.Sample[5];
      this.samples = new ArrayList();
      this.currentSortOrder = -1;
   }

   private void ensureSortedByIndex() {
      if (this.currentSortOrder != 1) {
         Collections.sort(this.samples, INDEX_COMPARATOR);
         this.currentSortOrder = 1;
      }

   }

   private void ensureSortedByValue() {
      if (this.currentSortOrder != 0) {
         Collections.sort(this.samples, VALUE_COMPARATOR);
         this.currentSortOrder = 0;
      }

   }

   // $FF: synthetic method
   static int lambda$static$0(SlidingPercentile.Sample var0, SlidingPercentile.Sample var1) {
      return var0.index - var1.index;
   }

   // $FF: synthetic method
   static int lambda$static$1(SlidingPercentile.Sample var0, SlidingPercentile.Sample var1) {
      return Float.compare(var0.value, var1.value);
   }

   public void addSample(int var1, float var2) {
      this.ensureSortedByIndex();
      int var3 = this.recycledSampleCount;
      SlidingPercentile.Sample[] var4;
      SlidingPercentile.Sample var6;
      if (var3 > 0) {
         var4 = this.recycledSamples;
         --var3;
         this.recycledSampleCount = var3;
         var6 = var4[var3];
      } else {
         var6 = new SlidingPercentile.Sample();
      }

      var3 = this.nextSampleIndex++;
      var6.index = var3;
      var6.weight = var1;
      var6.value = var2;
      this.samples.add(var6);
      this.totalWeight += var1;

      while(true) {
         var1 = this.totalWeight;
         var3 = this.maxWeight;
         if (var1 <= var3) {
            return;
         }

         var3 = var1 - var3;
         SlidingPercentile.Sample var5 = (SlidingPercentile.Sample)this.samples.get(0);
         var1 = var5.weight;
         if (var1 <= var3) {
            this.totalWeight -= var1;
            this.samples.remove(0);
            var1 = this.recycledSampleCount;
            if (var1 < 5) {
               var4 = this.recycledSamples;
               this.recycledSampleCount = var1 + 1;
               var4[var1] = var5;
            }
         } else {
            var5.weight = var1 - var3;
            this.totalWeight -= var3;
         }
      }
   }

   public float getPercentile(float var1) {
      this.ensureSortedByValue();
      float var2 = (float)this.totalWeight;
      int var3 = 0;

      for(int var4 = 0; var3 < this.samples.size(); ++var3) {
         SlidingPercentile.Sample var5 = (SlidingPercentile.Sample)this.samples.get(var3);
         var4 += var5.weight;
         if ((float)var4 >= var1 * var2) {
            return var5.value;
         }
      }

      if (this.samples.isEmpty()) {
         var1 = Float.NaN;
      } else {
         ArrayList var6 = this.samples;
         var1 = ((SlidingPercentile.Sample)var6.get(var6.size() - 1)).value;
      }

      return var1;
   }

   public void reset() {
      this.samples.clear();
      this.currentSortOrder = -1;
      this.nextSampleIndex = 0;
      this.totalWeight = 0;
   }

   private static class Sample {
      public int index;
      public float value;
      public int weight;

      private Sample() {
      }

      // $FF: synthetic method
      Sample(Object var1) {
         this();
      }
   }
}

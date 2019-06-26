package com.airbnb.lottie.utils;

public class MeanCalculator {
   private int n;
   private float sum;

   public void add(float var1) {
      this.sum += var1;
      ++this.n;
      int var2 = this.n;
      if (var2 == Integer.MAX_VALUE) {
         this.sum /= 2.0F;
         this.n = var2 / 2;
      }

   }
}

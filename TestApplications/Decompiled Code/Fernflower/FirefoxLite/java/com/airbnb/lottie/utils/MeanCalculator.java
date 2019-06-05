package com.airbnb.lottie.utils;

public class MeanCalculator {
   private int n;
   private float sum;

   public void add(float var1) {
      this.sum += var1;
      ++this.n;
      if (this.n == Integer.MAX_VALUE) {
         this.sum /= 2.0F;
         this.n /= 2;
      }

   }
}

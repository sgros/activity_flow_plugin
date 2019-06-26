package com.github.mikephil.charting.utils;

import java.util.List;

public class MPPointD extends ObjectPool.Poolable {
   private static ObjectPool pool = ObjectPool.create(64, new MPPointD(0.0D, 0.0D));
   public double x;
   public double y;

   static {
      pool.setReplenishPercentage(0.5F);
   }

   private MPPointD(double var1, double var3) {
      this.x = var1;
      this.y = var3;
   }

   public static MPPointD getInstance(double var0, double var2) {
      MPPointD var4 = (MPPointD)pool.get();
      var4.x = var0;
      var4.y = var2;
      return var4;
   }

   public static void recycleInstance(MPPointD var0) {
      pool.recycle((ObjectPool.Poolable)var0);
   }

   public static void recycleInstances(List var0) {
      pool.recycle(var0);
   }

   protected ObjectPool.Poolable instantiate() {
      return new MPPointD(0.0D, 0.0D);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("MPPointD, x: ");
      var1.append(this.x);
      var1.append(", y: ");
      var1.append(this.y);
      return var1.toString();
   }
}

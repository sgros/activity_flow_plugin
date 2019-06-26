package com.github.mikephil.charting.utils;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.List;

public class MPPointF extends ObjectPool.Poolable {
   public static final Creator CREATOR;
   private static ObjectPool pool = ObjectPool.create(32, new MPPointF(0.0F, 0.0F));
   public float x;
   public float y;

   static {
      pool.setReplenishPercentage(0.5F);
      CREATOR = new Creator() {
         public MPPointF createFromParcel(Parcel var1) {
            MPPointF var2 = new MPPointF(0.0F, 0.0F);
            var2.my_readFromParcel(var1);
            return var2;
         }

         public MPPointF[] newArray(int var1) {
            return new MPPointF[var1];
         }
      };
   }

   public MPPointF() {
   }

   public MPPointF(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public static MPPointF getInstance() {
      return (MPPointF)pool.get();
   }

   public static MPPointF getInstance(float var0, float var1) {
      MPPointF var2 = (MPPointF)pool.get();
      var2.x = var0;
      var2.y = var1;
      return var2;
   }

   public static MPPointF getInstance(MPPointF var0) {
      MPPointF var1 = (MPPointF)pool.get();
      var1.x = var0.x;
      var1.y = var0.y;
      return var1;
   }

   public static void recycleInstance(MPPointF var0) {
      pool.recycle((ObjectPool.Poolable)var0);
   }

   public static void recycleInstances(List var0) {
      pool.recycle(var0);
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   protected ObjectPool.Poolable instantiate() {
      return new MPPointF(0.0F, 0.0F);
   }

   public void my_readFromParcel(Parcel var1) {
      this.x = var1.readFloat();
      this.y = var1.readFloat();
   }
}

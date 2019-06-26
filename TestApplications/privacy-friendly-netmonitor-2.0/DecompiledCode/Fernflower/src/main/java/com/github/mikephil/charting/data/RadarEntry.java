package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;

@SuppressLint({"ParcelCreator"})
public class RadarEntry extends Entry {
   public RadarEntry(float var1) {
      super(0.0F, var1);
   }

   public RadarEntry(float var1, Object var2) {
      super(0.0F, var1, var2);
   }

   public RadarEntry copy() {
      return new RadarEntry(this.getY(), this.getData());
   }

   public float getValue() {
      return this.getY();
   }

   @Deprecated
   public float getX() {
      return super.getX();
   }

   @Deprecated
   public void setX(float var1) {
      super.setX(var1);
   }
}

package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;

@SuppressLint({"ParcelCreator"})
public class PieEntry extends Entry {
   private String label;

   public PieEntry(float var1) {
      super(0.0F, var1);
   }

   public PieEntry(float var1, Drawable var2) {
      super(0.0F, var1, var2);
   }

   public PieEntry(float var1, Drawable var2, Object var3) {
      super(0.0F, var1, var2, var3);
   }

   public PieEntry(float var1, Object var2) {
      super(0.0F, var1, var2);
   }

   public PieEntry(float var1, String var2) {
      super(0.0F, var1);
      this.label = var2;
   }

   public PieEntry(float var1, String var2, Drawable var3) {
      super(0.0F, var1, var3);
      this.label = var2;
   }

   public PieEntry(float var1, String var2, Drawable var3, Object var4) {
      super(0.0F, var1, var3, var4);
      this.label = var2;
   }

   public PieEntry(float var1, String var2, Object var3) {
      super(0.0F, var1, var3);
      this.label = var2;
   }

   public PieEntry copy() {
      return new PieEntry(this.getY(), this.label, this.getData());
   }

   public String getLabel() {
      return this.label;
   }

   public float getValue() {
      return this.getY();
   }

   @Deprecated
   public float getX() {
      Log.i("DEPRECATED", "Pie entries do not have x values");
      return super.getX();
   }

   public void setLabel(String var1) {
      this.label = var1;
   }

   @Deprecated
   public void setX(float var1) {
      super.setX(var1);
      Log.i("DEPRECATED", "Pie entries do not have x values");
   }
}

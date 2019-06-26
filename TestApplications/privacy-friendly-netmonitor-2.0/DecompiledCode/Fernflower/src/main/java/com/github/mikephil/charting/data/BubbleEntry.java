package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

@SuppressLint({"ParcelCreator"})
public class BubbleEntry extends Entry {
   private float mSize = 0.0F;

   public BubbleEntry(float var1, float var2, float var3) {
      super(var1, var2);
      this.mSize = var3;
   }

   public BubbleEntry(float var1, float var2, float var3, Drawable var4) {
      super(var1, var2, var4);
      this.mSize = var3;
   }

   public BubbleEntry(float var1, float var2, float var3, Drawable var4, Object var5) {
      super(var1, var2, var4, var5);
      this.mSize = var3;
   }

   public BubbleEntry(float var1, float var2, float var3, Object var4) {
      super(var1, var2, var4);
      this.mSize = var3;
   }

   public BubbleEntry copy() {
      return new BubbleEntry(this.getX(), this.getY(), this.mSize, this.getData());
   }

   public float getSize() {
      return this.mSize;
   }

   public void setSize(float var1) {
      this.mSize = var1;
   }
}

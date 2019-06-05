package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;

public abstract class BaseEntry {
   private Object mData;
   private Drawable mIcon;
   private float y;

   public BaseEntry() {
      this.y = 0.0F;
      this.mData = null;
      this.mIcon = null;
   }

   public BaseEntry(float var1) {
      this.y = 0.0F;
      this.mData = null;
      this.mIcon = null;
      this.y = var1;
   }

   public BaseEntry(float var1, Drawable var2) {
      this(var1);
      this.mIcon = var2;
   }

   public BaseEntry(float var1, Drawable var2, Object var3) {
      this(var1);
      this.mIcon = var2;
      this.mData = var3;
   }

   public BaseEntry(float var1, Object var2) {
      this(var1);
      this.mData = var2;
   }

   public Object getData() {
      return this.mData;
   }

   public Drawable getIcon() {
      return this.mIcon;
   }

   public float getY() {
      return this.y;
   }

   public void setData(Object var1) {
      this.mData = var1;
   }

   public void setIcon(Drawable var1) {
      this.mIcon = var1;
   }

   public void setY(float var1) {
      this.y = var1;
   }
}

package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

@SuppressLint({"ParcelCreator"})
public class CandleEntry extends Entry {
   private float mClose = 0.0F;
   private float mOpen = 0.0F;
   private float mShadowHigh = 0.0F;
   private float mShadowLow = 0.0F;

   public CandleEntry(float var1, float var2, float var3, float var4, float var5) {
      super(var1, (var2 + var3) / 2.0F);
      this.mShadowHigh = var2;
      this.mShadowLow = var3;
      this.mOpen = var4;
      this.mClose = var5;
   }

   public CandleEntry(float var1, float var2, float var3, float var4, float var5, Drawable var6) {
      super(var1, (var2 + var3) / 2.0F, var6);
      this.mShadowHigh = var2;
      this.mShadowLow = var3;
      this.mOpen = var4;
      this.mClose = var5;
   }

   public CandleEntry(float var1, float var2, float var3, float var4, float var5, Drawable var6, Object var7) {
      super(var1, (var2 + var3) / 2.0F, var6, var7);
      this.mShadowHigh = var2;
      this.mShadowLow = var3;
      this.mOpen = var4;
      this.mClose = var5;
   }

   public CandleEntry(float var1, float var2, float var3, float var4, float var5, Object var6) {
      super(var1, (var2 + var3) / 2.0F, var6);
      this.mShadowHigh = var2;
      this.mShadowLow = var3;
      this.mOpen = var4;
      this.mClose = var5;
   }

   public CandleEntry copy() {
      return new CandleEntry(this.getX(), this.mShadowHigh, this.mShadowLow, this.mOpen, this.mClose, this.getData());
   }

   public float getBodyRange() {
      return Math.abs(this.mOpen - this.mClose);
   }

   public float getClose() {
      return this.mClose;
   }

   public float getHigh() {
      return this.mShadowHigh;
   }

   public float getLow() {
      return this.mShadowLow;
   }

   public float getOpen() {
      return this.mOpen;
   }

   public float getShadowRange() {
      return Math.abs(this.mShadowHigh - this.mShadowLow);
   }

   public float getY() {
      return super.getY();
   }

   public void setClose(float var1) {
      this.mClose = var1;
   }

   public void setHigh(float var1) {
      this.mShadowHigh = var1;
   }

   public void setLow(float var1) {
      this.mShadowLow = var1;
   }

   public void setOpen(float var1) {
      this.mOpen = var1;
   }
}

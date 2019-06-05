package com.airbnb.lottie.model;

public class DocumentData {
   public final double baselineShift;
   public final int color;
   public final String fontName;
   final int justification;
   final double lineHeight;
   public final double size;
   public final int strokeColor;
   public final boolean strokeOverFill;
   public final double strokeWidth;
   public final String text;
   public final int tracking;

   public DocumentData(String var1, String var2, double var3, int var5, int var6, double var7, double var9, int var11, int var12, double var13, boolean var15) {
      this.text = var1;
      this.fontName = var2;
      this.size = var3;
      this.justification = var5;
      this.tracking = var6;
      this.lineHeight = var7;
      this.baselineShift = var9;
      this.color = var11;
      this.strokeColor = var12;
      this.strokeWidth = var13;
      this.strokeOverFill = var15;
   }

   public int hashCode() {
      int var1 = (int)((double)((this.text.hashCode() * 31 + this.fontName.hashCode()) * 31) + this.size);
      int var2 = this.justification;
      int var3 = this.tracking;
      long var4 = Double.doubleToLongBits(this.lineHeight);
      return (((var1 * 31 + var2) * 31 + var3) * 31 + (int)(var4 ^ var4 >>> 32)) * 31 + this.color;
   }
}

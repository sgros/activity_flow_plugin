package com.airbnb.lottie.model;

public class DocumentData {
   public final double baselineShift;
   public final int color;
   public final String fontName;
   public final DocumentData.Justification justification;
   public final double lineHeight;
   public final double size;
   public final int strokeColor;
   public final boolean strokeOverFill;
   public final double strokeWidth;
   public final String text;
   public final int tracking;

   public DocumentData(String var1, String var2, double var3, DocumentData.Justification var5, int var6, double var7, double var9, int var11, int var12, double var13, boolean var15) {
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
      double var1 = (double)((this.text.hashCode() * 31 + this.fontName.hashCode()) * 31);
      double var3 = this.size;
      Double.isNaN(var1);
      int var5 = (int)(var1 + var3);
      int var6 = this.justification.ordinal();
      int var7 = this.tracking;
      long var8 = Double.doubleToLongBits(this.lineHeight);
      return (((var5 * 31 + var6) * 31 + var7) * 31 + (int)(var8 ^ var8 >>> 32)) * 31 + this.color;
   }

   public static enum Justification {
      CENTER,
      LEFT_ALIGN,
      RIGHT_ALIGN;
   }
}

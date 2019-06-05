package com.airbnb.lottie.value;

public class ScaleXY {
   private final float scaleX;
   private final float scaleY;

   public ScaleXY() {
      this(1.0F, 1.0F);
   }

   public ScaleXY(float var1, float var2) {
      this.scaleX = var1;
      this.scaleY = var2;
   }

   public float getScaleX() {
      return this.scaleX;
   }

   public float getScaleY() {
      return this.scaleY;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getScaleX());
      var1.append("x");
      var1.append(this.getScaleY());
      return var1.toString();
   }
}

package com.airbnb.lottie.model;

import android.graphics.PointF;

public class CubicCurveData {
   private final PointF controlPoint1;
   private final PointF controlPoint2;
   private final PointF vertex;

   public CubicCurveData() {
      this.controlPoint1 = new PointF();
      this.controlPoint2 = new PointF();
      this.vertex = new PointF();
   }

   public CubicCurveData(PointF var1, PointF var2, PointF var3) {
      this.controlPoint1 = var1;
      this.controlPoint2 = var2;
      this.vertex = var3;
   }

   public PointF getControlPoint1() {
      return this.controlPoint1;
   }

   public PointF getControlPoint2() {
      return this.controlPoint2;
   }

   public PointF getVertex() {
      return this.vertex;
   }

   public void setControlPoint1(float var1, float var2) {
      this.controlPoint1.set(var1, var2);
   }

   public void setControlPoint2(float var1, float var2) {
      this.controlPoint2.set(var1, var2);
   }

   public void setVertex(float var1, float var2) {
      this.vertex.set(var1, var2);
   }
}

package com.airbnb.lottie.model.content;

import android.graphics.PointF;
import com.airbnb.lottie.L;
import com.airbnb.lottie.model.CubicCurveData;
import com.airbnb.lottie.utils.MiscUtils;
import java.util.ArrayList;
import java.util.List;

public class ShapeData {
   private boolean closed;
   private final List curves = new ArrayList();
   private PointF initialPoint;

   public ShapeData() {
   }

   public ShapeData(PointF var1, boolean var2, List var3) {
      this.initialPoint = var1;
      this.closed = var2;
      this.curves.addAll(var3);
   }

   private void setInitialPoint(float var1, float var2) {
      if (this.initialPoint == null) {
         this.initialPoint = new PointF();
      }

      this.initialPoint.set(var1, var2);
   }

   public List getCurves() {
      return this.curves;
   }

   public PointF getInitialPoint() {
      return this.initialPoint;
   }

   public void interpolateBetween(ShapeData var1, ShapeData var2, float var3) {
      if (this.initialPoint == null) {
         this.initialPoint = new PointF();
      }

      boolean var4 = var1.isClosed();
      int var5 = 0;
      if (!var4 && !var2.isClosed()) {
         var4 = false;
      } else {
         var4 = true;
      }

      this.closed = var4;
      if (var1.getCurves().size() != var2.getCurves().size()) {
         StringBuilder var6 = new StringBuilder();
         var6.append("Curves must have the same number of control points. Shape 1: ");
         var6.append(var1.getCurves().size());
         var6.append("\tShape 2: ");
         var6.append(var2.getCurves().size());
         L.warn(var6.toString());
      }

      if (this.curves.isEmpty()) {
         for(int var7 = Math.min(var1.getCurves().size(), var2.getCurves().size()); var5 < var7; ++var5) {
            this.curves.add(new CubicCurveData());
         }
      }

      PointF var8 = var1.getInitialPoint();
      PointF var13 = var2.getInitialPoint();
      this.setInitialPoint(MiscUtils.lerp(var8.x, var13.x, var3), MiscUtils.lerp(var8.y, var13.y, var3));

      for(var5 = this.curves.size() - 1; var5 >= 0; --var5) {
         CubicCurveData var9 = (CubicCurveData)var1.getCurves().get(var5);
         CubicCurveData var10 = (CubicCurveData)var2.getCurves().get(var5);
         var8 = var9.getControlPoint1();
         var13 = var9.getControlPoint2();
         PointF var14 = var9.getVertex();
         PointF var11 = var10.getControlPoint1();
         PointF var12 = var10.getControlPoint2();
         PointF var15 = var10.getVertex();
         ((CubicCurveData)this.curves.get(var5)).setControlPoint1(MiscUtils.lerp(var8.x, var11.x, var3), MiscUtils.lerp(var8.y, var11.y, var3));
         ((CubicCurveData)this.curves.get(var5)).setControlPoint2(MiscUtils.lerp(var13.x, var12.x, var3), MiscUtils.lerp(var13.y, var12.y, var3));
         ((CubicCurveData)this.curves.get(var5)).setVertex(MiscUtils.lerp(var14.x, var15.x, var3), MiscUtils.lerp(var14.y, var15.y, var3));
      }

   }

   public boolean isClosed() {
      return this.closed;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ShapeData{numCurves=");
      var1.append(this.curves.size());
      var1.append("closed=");
      var1.append(this.closed);
      var1.append('}');
      return var1.toString();
   }
}
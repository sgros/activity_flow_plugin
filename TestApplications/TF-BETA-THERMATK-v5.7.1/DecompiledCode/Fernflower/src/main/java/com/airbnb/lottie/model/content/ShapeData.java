package com.airbnb.lottie.model.content;

import android.graphics.PointF;
import com.airbnb.lottie.model.CubicCurveData;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.utils.MiscUtils;
import java.util.ArrayList;
import java.util.List;

public class ShapeData {
   private boolean closed;
   private final List curves;
   private PointF initialPoint;

   public ShapeData() {
      this.curves = new ArrayList();
   }

   public ShapeData(PointF var1, boolean var2, List var3) {
      this.initialPoint = var1;
      this.closed = var2;
      this.curves = new ArrayList(var3);
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

      boolean var4;
      if (!var1.isClosed() && !var2.isClosed()) {
         var4 = false;
      } else {
         var4 = true;
      }

      this.closed = var4;
      if (var1.getCurves().size() != var2.getCurves().size()) {
         StringBuilder var5 = new StringBuilder();
         var5.append("Curves must have the same number of control points. Shape 1: ");
         var5.append(var1.getCurves().size());
         var5.append("\tShape 2: ");
         var5.append(var2.getCurves().size());
         Logger.warning(var5.toString());
      }

      int var6 = Math.min(var1.getCurves().size(), var2.getCurves().size());
      int var7;
      if (this.curves.size() < var6) {
         for(var7 = this.curves.size(); var7 < var6; ++var7) {
            this.curves.add(new CubicCurveData());
         }
      } else if (this.curves.size() > var6) {
         for(var7 = this.curves.size() - 1; var7 >= var6; --var7) {
            List var13 = this.curves;
            var13.remove(var13.size() - 1);
         }
      }

      PointF var8 = var1.getInitialPoint();
      PointF var14 = var2.getInitialPoint();
      this.setInitialPoint(MiscUtils.lerp(var8.x, var14.x, var3), MiscUtils.lerp(var8.y, var14.y, var3));

      for(var7 = this.curves.size() - 1; var7 >= 0; --var7) {
         CubicCurveData var9 = (CubicCurveData)var1.getCurves().get(var7);
         CubicCurveData var10 = (CubicCurveData)var2.getCurves().get(var7);
         var14 = var9.getControlPoint1();
         var8 = var9.getControlPoint2();
         PointF var15 = var9.getVertex();
         PointF var11 = var10.getControlPoint1();
         PointF var12 = var10.getControlPoint2();
         PointF var16 = var10.getVertex();
         ((CubicCurveData)this.curves.get(var7)).setControlPoint1(MiscUtils.lerp(var14.x, var11.x, var3), MiscUtils.lerp(var14.y, var11.y, var3));
         ((CubicCurveData)this.curves.get(var7)).setControlPoint2(MiscUtils.lerp(var8.x, var12.x, var3), MiscUtils.lerp(var8.y, var12.y, var3));
         ((CubicCurveData)this.curves.get(var7)).setVertex(MiscUtils.lerp(var15.x, var16.x, var3), MiscUtils.lerp(var15.y, var16.y, var3));
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

package org.telegram.ui.Components;

import android.graphics.PointF;
import android.view.animation.Interpolator;

public class CubicBezierInterpolator implements Interpolator {
   public static final CubicBezierInterpolator DEFAULT = new CubicBezierInterpolator(0.25D, 0.1D, 0.25D, 1.0D);
   public static final CubicBezierInterpolator EASE_BOTH = new CubicBezierInterpolator(0.42D, 0.0D, 0.58D, 1.0D);
   public static final CubicBezierInterpolator EASE_IN = new CubicBezierInterpolator(0.42D, 0.0D, 1.0D, 1.0D);
   public static final CubicBezierInterpolator EASE_OUT = new CubicBezierInterpolator(0.0D, 0.0D, 0.58D, 1.0D);
   public static final CubicBezierInterpolator EASE_OUT_QUINT = new CubicBezierInterpolator(0.23D, 1.0D, 0.32D, 1.0D);
   protected PointF a;
   protected PointF b;
   protected PointF c;
   protected PointF end;
   protected PointF start;

   public CubicBezierInterpolator(double var1, double var3, double var5, double var7) {
      this((float)var1, (float)var3, (float)var5, (float)var7);
   }

   public CubicBezierInterpolator(float var1, float var2, float var3, float var4) {
      this(new PointF(var1, var2), new PointF(var3, var4));
   }

   public CubicBezierInterpolator(PointF var1, PointF var2) throws IllegalArgumentException {
      this.a = new PointF();
      this.b = new PointF();
      this.c = new PointF();
      float var3 = var1.x;
      if (var3 >= 0.0F && var3 <= 1.0F) {
         var3 = var2.x;
         if (var3 >= 0.0F && var3 <= 1.0F) {
            this.start = var1;
            this.end = var2;
         } else {
            throw new IllegalArgumentException("endX value must be in the range [0, 1]");
         }
      } else {
         throw new IllegalArgumentException("startX value must be in the range [0, 1]");
      }
   }

   private float getBezierCoordinateX(float var1) {
      PointF var2 = this.c;
      PointF var3 = this.start;
      var2.x = var3.x * 3.0F;
      PointF var4 = this.b;
      var4.x = (this.end.x - var3.x) * 3.0F - var2.x;
      var3 = this.a;
      var3.x = 1.0F - var2.x - var4.x;
      return var1 * (var2.x + (var4.x + var3.x * var1) * var1);
   }

   private float getXDerivate(float var1) {
      return this.c.x + var1 * (this.b.x * 2.0F + this.a.x * 3.0F * var1);
   }

   protected float getBezierCoordinateY(float var1) {
      PointF var2 = this.c;
      PointF var3 = this.start;
      var2.y = var3.y * 3.0F;
      PointF var4 = this.b;
      var4.y = (this.end.y - var3.y) * 3.0F - var2.y;
      var3 = this.a;
      var3.y = 1.0F - var2.y - var4.y;
      return var1 * (var2.y + (var4.y + var3.y * var1) * var1);
   }

   public float getInterpolation(float var1) {
      return this.getBezierCoordinateY(this.getXForTime(var1));
   }

   protected float getXForTime(float var1) {
      int var2 = 1;

      float var3;
      for(var3 = var1; var2 < 14; ++var2) {
         float var4 = this.getBezierCoordinateX(var3) - var1;
         if ((double)Math.abs(var4) < 0.001D) {
            break;
         }

         var3 -= var4 / this.getXDerivate(var3);
      }

      return var3;
   }
}

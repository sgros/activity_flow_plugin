package android.support.design.shape;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import java.util.List;

public class ShapePath {
   public float endX;
   public float endY;
   private final List operations;
   public float startX;
   public float startY;

   public void addArc(float var1, float var2, float var3, float var4, float var5, float var6) {
      ShapePath.PathArcOperation var7 = new ShapePath.PathArcOperation(var1, var2, var3, var4);
      var7.startAngle = var5;
      var7.sweepAngle = var6;
      this.operations.add(var7);
      float var8 = (var3 - var1) / 2.0F;
      double var9 = (double)(var5 + var6);
      this.endX = (var1 + var3) * 0.5F + var8 * (float)Math.cos(Math.toRadians(var9));
      this.endY = (var2 + var4) * 0.5F + (var4 - var2) / 2.0F * (float)Math.sin(Math.toRadians(var9));
   }

   public void applyToPath(Matrix var1, Path var2) {
      int var3 = this.operations.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         ((ShapePath.PathOperation)this.operations.get(var4)).applyToPath(var1, var2);
      }

   }

   public void lineTo(float var1, float var2) {
      ShapePath.PathLineOperation var3 = new ShapePath.PathLineOperation();
      var3.x = var1;
      var3.y = var2;
      this.operations.add(var3);
      this.endX = var1;
      this.endY = var2;
   }

   public void reset(float var1, float var2) {
      this.startX = var1;
      this.startY = var2;
      this.endX = var1;
      this.endY = var2;
      this.operations.clear();
   }

   public static class PathArcOperation extends ShapePath.PathOperation {
      private static final RectF rectF = new RectF();
      public float bottom;
      public float left;
      public float right;
      public float startAngle;
      public float sweepAngle;
      public float top;

      public PathArcOperation(float var1, float var2, float var3, float var4) {
         this.left = var1;
         this.top = var2;
         this.right = var3;
         this.bottom = var4;
      }

      public void applyToPath(Matrix var1, Path var2) {
         Matrix var3 = this.matrix;
         var1.invert(var3);
         var2.transform(var3);
         rectF.set(this.left, this.top, this.right, this.bottom);
         var2.arcTo(rectF, this.startAngle, this.sweepAngle, false);
         var2.transform(var1);
      }
   }

   public static class PathLineOperation extends ShapePath.PathOperation {
      private float x;
      private float y;

      public void applyToPath(Matrix var1, Path var2) {
         Matrix var3 = this.matrix;
         var1.invert(var3);
         var2.transform(var3);
         var2.lineTo(this.x, this.y);
         var2.transform(var1);
      }
   }

   public abstract static class PathOperation {
      protected final Matrix matrix = new Matrix();

      public abstract void applyToPath(Matrix var1, Path var2);
   }
}

package org.telegram.ui.Components.Paint;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Render {
   private static RectF Draw(RenderState var0) {
      RectF var1 = new RectF(0.0F, 0.0F, 0.0F, 0.0F);
      int var2 = var0.getCount();
      if (var2 == 0) {
         return var1;
      } else {
         int var3 = var2 - 1;
         ByteBuffer var4 = ByteBuffer.allocateDirect((var2 * 4 + var3 * 2) * 20);
         var4.order(ByteOrder.nativeOrder());
         FloatBuffer var5 = var4.asFloatBuffer();
         var5.position(0);
         var0.setPosition(0);
         int var6 = 0;

         int var7;
         for(var7 = 0; var6 < var2; ++var6) {
            float var8 = var0.read();
            float var9 = var0.read();
            float var10 = var0.read();
            float var11 = var0.read();
            float var12 = var0.read();
            RectF var13 = new RectF(var8 - var10, var9 - var10, var8 + var10, var9 + var10);
            float[] var14 = new float[8];
            var9 = var13.left;
            var14[0] = var9;
            var10 = var13.top;
            var14[1] = var10;
            var8 = var13.right;
            var14[2] = var8;
            var14[3] = var10;
            var14[4] = var9;
            var10 = var13.bottom;
            var14[5] = var10;
            var14[6] = var8;
            var14[7] = var10;
            var8 = var13.centerX();
            var10 = var13.centerY();
            Matrix var16 = new Matrix();
            var16.setRotate((float)Math.toDegrees((double)var11), var8, var10);
            var16.mapPoints(var14);
            var16.mapRect(var13);
            Utils.RectFIntegral(var13);
            var1.union(var13);
            if (var7 != 0) {
               var5.put(var14[0]);
               var5.put(var14[1]);
               var5.put(0.0F);
               var5.put(0.0F);
               var5.put(var12);
               ++var7;
            }

            var5.put(var14[0]);
            var5.put(var14[1]);
            var5.put(0.0F);
            var5.put(0.0F);
            var5.put(var12);
            var5.put(var14[2]);
            var5.put(var14[3]);
            var5.put(1.0F);
            var5.put(0.0F);
            var5.put(var12);
            var5.put(var14[4]);
            var5.put(var14[5]);
            var5.put(0.0F);
            var5.put(1.0F);
            var5.put(var12);
            var5.put(var14[6]);
            var5.put(var14[7]);
            var5.put(1.0F);
            var5.put(1.0F);
            var5.put(var12);
            int var15 = var7 + 1 + 1 + 1 + 1;
            var7 = var15;
            if (var6 != var3) {
               var5.put(var14[6]);
               var5.put(var14[7]);
               var5.put(1.0F);
               var5.put(1.0F);
               var5.put(var12);
               var7 = var15 + 1;
            }
         }

         var5.position(0);
         GLES20.glVertexAttribPointer(0, 2, 5126, false, 20, var5.slice());
         GLES20.glEnableVertexAttribArray(0);
         var5.position(2);
         GLES20.glVertexAttribPointer(1, 2, 5126, true, 20, var5.slice());
         GLES20.glEnableVertexAttribArray(1);
         var5.position(4);
         GLES20.glVertexAttribPointer(2, 1, 5126, true, 20, var5.slice());
         GLES20.glEnableVertexAttribArray(2);
         GLES20.glDrawArrays(5, 0, var7);
         return var1;
      }
   }

   private static void PaintSegment(Point var0, Point var1, RenderState var2) {
      double var3 = (double)var0.getDistanceTo(var1);
      Point var5 = var1.substract(var0);
      Point var6 = new Point(1.0D, 1.0D, 0.0D);
      float var7;
      if (Math.abs(var2.angle) > 0.0F) {
         var7 = var2.angle;
      } else {
         var7 = (float)Math.atan2(var5.y, var5.x);
      }

      float var8 = var2.baseWeight * var2.scale;
      double var9 = (double)Math.max(1.0F, var2.spacing * var8);
      if (var3 > 0.0D) {
         Double.isNaN(var3);
         var6 = var5.multiplyByScalar(1.0D / var3);
      }

      float var11 = Math.min(1.0F, var2.alpha * 1.15F);
      boolean var12 = var0.edge;
      boolean var13 = var1.edge;
      double var14 = var2.remainder;
      Double.isNaN(var3);
      Double.isNaN(var9);
      int var16 = (int)Math.ceil((var3 - var14) / var9);
      int var17 = var2.getCount();
      var2.appendValuesCount(var16);
      var2.setPosition(var17);
      var0 = var0.add(var6.multiplyByScalar(var2.remainder));
      var14 = var2.remainder;

      boolean var18;
      for(var18 = true; var14 <= var3; var14 += var9) {
         float var19;
         if (var12) {
            var19 = var11;
         } else {
            var19 = var2.alpha;
         }

         var18 = var2.addPoint(var0.toPointF(), var8, var7, var19, -1);
         if (!var18) {
            break;
         }

         var0 = var0.add(var6.multiplyByScalar(var9));
         var12 = false;
         Double.isNaN(var9);
      }

      if (var18 && var13) {
         var2.appendValuesCount(1);
         var2.addPoint(var1.toPointF(), var8, var7, var11, -1);
      }

      Double.isNaN(var3);
      var2.remainder = var14 - var3;
   }

   private static void PaintStamp(Point var0, RenderState var1) {
      float var2 = var1.baseWeight;
      float var3 = var1.scale;
      PointF var6 = var0.toPointF();
      float var4;
      if (Math.abs(var1.angle) > 0.0F) {
         var4 = var1.angle;
      } else {
         var4 = 0.0F;
      }

      float var5 = var1.alpha;
      var1.prepare();
      var1.appendValuesCount(1);
      var1.addPoint(var6, var2 * var3, var4, var5, 0);
   }

   public static RectF RenderPath(Path var0, RenderState var1) {
      var1.baseWeight = var0.getBaseWeight();
      var1.spacing = var0.getBrush().getSpacing();
      var1.alpha = var0.getBrush().getAlpha();
      var1.angle = var0.getBrush().getAngle();
      var1.scale = var0.getBrush().getScale();
      int var2 = var0.getLength();
      if (var2 == 0) {
         return null;
      } else {
         int var3 = 0;
         if (var2 == 1) {
            PaintStamp(var0.getPoints()[0], var1);
         } else {
            Point[] var4 = var0.getPoints();
            var1.prepare();

            while(var3 < var4.length - 1) {
               Point var5 = var4[var3];
               ++var3;
               PaintSegment(var5, var4[var3], var1);
            }
         }

         var0.remainder = var1.remainder;
         return Draw(var1);
      }
   }
}

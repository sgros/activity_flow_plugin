package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class TriangleShapeRenderer implements IShapeRenderer {
   protected Path mTrianglePathBuffer = new Path();

   public void renderShape(Canvas var1, IScatterDataSet var2, ViewPortHandler var3, float var4, float var5, Paint var6) {
      float var7 = var2.getScatterShapeSize();
      float var8 = var7 / 2.0F;
      float var9 = (var7 - Utils.convertDpToPixel(var2.getScatterShapeHoleRadius()) * 2.0F) / 2.0F;
      int var10 = var2.getScatterShapeHoleColor();
      var6.setStyle(Style.FILL);
      Path var16 = this.mTrianglePathBuffer;
      var16.reset();
      float var11 = var5 - var8;
      var16.moveTo(var4, var11);
      float var12 = var4 + var8;
      var5 += var8;
      var16.lineTo(var12, var5);
      var8 = var4 - var8;
      var16.lineTo(var8, var5);
      double var13 = (double)var7;
      if (var13 > 0.0D) {
         var16.lineTo(var4, var11);
         float var15 = var8 + var9;
         var7 = var5 - var9;
         var16.moveTo(var15, var7);
         var16.lineTo(var12 - var9, var7);
         var16.lineTo(var4, var11 + var9);
         var16.lineTo(var15, var7);
      }

      var16.close();
      var1.drawPath(var16, var6);
      var16.reset();
      if (var13 > 0.0D && var10 != 1122867) {
         var6.setColor(var10);
         var16.moveTo(var4, var11 + var9);
         var4 = var5 - var9;
         var16.lineTo(var12 - var9, var4);
         var16.lineTo(var8 + var9, var4);
         var16.close();
         var1.drawPath(var16, var6);
         var16.reset();
      }

   }
}

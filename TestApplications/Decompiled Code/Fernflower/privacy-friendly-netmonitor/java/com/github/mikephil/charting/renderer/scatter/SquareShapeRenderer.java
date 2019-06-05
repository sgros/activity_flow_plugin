package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class SquareShapeRenderer implements IShapeRenderer {
   public void renderShape(Canvas var1, IScatterDataSet var2, ViewPortHandler var3, float var4, float var5, Paint var6) {
      float var7 = var2.getScatterShapeSize();
      float var8 = var7 / 2.0F;
      float var9 = Utils.convertDpToPixel(var2.getScatterShapeHoleRadius());
      float var10 = (var7 - var9 * 2.0F) / 2.0F;
      float var11 = var10 / 2.0F;
      int var12 = var2.getScatterShapeHoleColor();
      if ((double)var7 > 0.0D) {
         var6.setStyle(Style.STROKE);
         var6.setStrokeWidth(var10);
         var8 = var4 - var9;
         var7 = var5 - var9;
         var4 += var9;
         var5 += var9;
         var1.drawRect(var8 - var11, var7 - var11, var4 + var11, var5 + var11, var6);
         if (var12 != 1122867) {
            var6.setStyle(Style.FILL);
            var6.setColor(var12);
            var1.drawRect(var8, var7, var4, var5, var6);
         }
      } else {
         var6.setStyle(Style.FILL);
         var1.drawRect(var4 - var8, var5 - var8, var4 + var8, var5 + var8, var6);
      }

   }
}

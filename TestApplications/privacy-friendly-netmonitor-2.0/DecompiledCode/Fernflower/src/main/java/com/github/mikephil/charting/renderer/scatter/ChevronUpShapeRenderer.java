package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class ChevronUpShapeRenderer implements IShapeRenderer {
   public void renderShape(Canvas var1, IScatterDataSet var2, ViewPortHandler var3, float var4, float var5, Paint var6) {
      float var7 = var2.getScatterShapeSize() / 2.0F;
      var6.setStyle(Style.STROKE);
      var6.setStrokeWidth(Utils.convertDpToPixel(1.0F));
      float var8 = 2.0F * var7;
      var7 = var5 - var8;
      var1.drawLine(var4, var7, var4 + var8, var5, var6);
      var1.drawLine(var4, var7, var4 - var8, var5, var6);
   }
}

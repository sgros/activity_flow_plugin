package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class LineRadarRenderer extends LineScatterCandleRadarRenderer {
   public LineRadarRenderer(ChartAnimator var1, ViewPortHandler var2) {
      super(var1, var2);
   }

   private boolean clipPathSupported() {
      boolean var1;
      if (Utils.getSDKInt() >= 18) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected void drawFilledPath(Canvas var1, Path var2, int var3, int var4) {
      var3 = var3 & 16777215 | var4 << 24;
      if (this.clipPathSupported()) {
         var4 = var1.save();
         var1.clipPath(var2);
         var1.drawColor(var3);
         var1.restoreToCount(var4);
      } else {
         Style var5 = this.mRenderPaint.getStyle();
         var4 = this.mRenderPaint.getColor();
         this.mRenderPaint.setStyle(Style.FILL);
         this.mRenderPaint.setColor(var3);
         var1.drawPath(var2, this.mRenderPaint);
         this.mRenderPaint.setColor(var4);
         this.mRenderPaint.setStyle(var5);
      }

   }

   protected void drawFilledPath(Canvas var1, Path var2, Drawable var3) {
      if (this.clipPathSupported()) {
         int var4 = var1.save();
         var1.clipPath(var2);
         var3.setBounds((int)this.mViewPortHandler.contentLeft(), (int)this.mViewPortHandler.contentTop(), (int)this.mViewPortHandler.contentRight(), (int)this.mViewPortHandler.contentBottom());
         var3.draw(var1);
         var1.restoreToCount(var4);
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Fill-drawables not (yet) supported below API level 18, this code was run on API level ");
         var5.append(Utils.getSDKInt());
         var5.append(".");
         throw new RuntimeException(var5.toString());
      }
   }
}

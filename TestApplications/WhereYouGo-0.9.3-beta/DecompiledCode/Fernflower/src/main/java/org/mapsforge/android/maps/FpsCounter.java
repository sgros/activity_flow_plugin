package org.mapsforge.android.maps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.os.SystemClock;

public class FpsCounter {
   private static final Paint FPS_PAINT = new Paint(1);
   private static final Paint FPS_PAINT_STROKE = new Paint(1);
   private static final int ONE_SECOND = 1000;
   private int fps;
   private int frameCounter;
   private long previousTime = SystemClock.uptimeMillis();
   private boolean showFpsCounter;

   FpsCounter() {
      configureFpsPaint();
   }

   private static void configureFpsPaint() {
      FPS_PAINT.setTypeface(Typeface.defaultFromStyle(1));
      FPS_PAINT.setTextSize(20.0F);
      FPS_PAINT_STROKE.setColor(-16777216);
      FPS_PAINT_STROKE.setTypeface(Typeface.defaultFromStyle(1));
      FPS_PAINT_STROKE.setTextSize(20.0F);
      FPS_PAINT_STROKE.setColor(-1);
      FPS_PAINT_STROKE.setStyle(Style.STROKE);
      FPS_PAINT_STROKE.setStrokeWidth(3.0F);
   }

   void draw(Canvas var1) {
      long var2 = SystemClock.uptimeMillis();
      long var4 = var2 - this.previousTime;
      if (var4 > 1000L) {
         this.fps = Math.round((float)this.frameCounter * 1000.0F / (float)var4);
         this.previousTime = var2;
         this.frameCounter = 0;
      }

      var1.drawText(String.valueOf(this.fps), 20.0F, 30.0F, FPS_PAINT_STROKE);
      var1.drawText(String.valueOf(this.fps), 20.0F, 30.0F, FPS_PAINT);
      ++this.frameCounter;
   }

   public boolean isShowFpsCounter() {
      return this.showFpsCounter;
   }

   public void setFpsCounter(boolean var1) {
      this.showFpsCounter = var1;
   }
}

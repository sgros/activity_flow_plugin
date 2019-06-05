package org.mapsforge.graphics.android;

import android.graphics.BitmapShader;
import android.graphics.DashPathEffect;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;
import org.mapsforge.map.graphics.Align;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.graphics.Cap;
import org.mapsforge.map.graphics.FontFamily;
import org.mapsforge.map.graphics.FontStyle;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.graphics.Style;

class AndroidPaint implements Paint {
   private Bitmap bitmap;
   final android.graphics.Paint paint = new android.graphics.Paint(1);

   private static int getStyle(FontStyle var0) {
      byte var1;
      switch(var0) {
      case BOLD:
         var1 = 1;
         break;
      case BOLD_ITALIC:
         var1 = 3;
         break;
      case ITALIC:
         var1 = 2;
         break;
      case NORMAL:
         var1 = 0;
         break;
      default:
         throw new IllegalArgumentException("unknown font style: " + var0);
      }

      return var1;
   }

   private static Typeface getTypeface(FontFamily var0) {
      Typeface var1;
      switch(var0) {
      case DEFAULT:
         var1 = Typeface.DEFAULT;
         break;
      case DEFAULT_BOLD:
         var1 = Typeface.DEFAULT_BOLD;
         break;
      case MONOSPACE:
         var1 = Typeface.MONOSPACE;
         break;
      case SANS_SERIF:
         var1 = Typeface.SANS_SERIF;
         break;
      case SERIF:
         var1 = Typeface.SERIF;
         break;
      default:
         throw new IllegalArgumentException("unknown font family: " + var0);
      }

      return var1;
   }

   public void destroy() {
      if (this.bitmap != null) {
         this.paint.setShader((Shader)null);
         this.bitmap.destroy();
      }

   }

   public int getColor() {
      return this.paint.getColor();
   }

   public int getTextHeight(String var1) {
      Rect var2 = new Rect();
      this.paint.getTextBounds(var1, 0, var1.length(), var2);
      return var2.height();
   }

   public int getTextWidth(String var1) {
      Rect var2 = new Rect();
      this.paint.getTextBounds(var1, 0, var1.length(), var2);
      return var2.width();
   }

   public void setBitmapShader(Bitmap var1) {
      if (var1 != null) {
         this.bitmap = var1;
         BitmapShader var2 = new BitmapShader(android.graphics.Bitmap.createBitmap(var1.getPixels(), var1.getWidth(), var1.getHeight(), Config.ARGB_8888), TileMode.REPEAT, TileMode.REPEAT);
         this.paint.setShader(var2);
      }

   }

   public void setColor(int var1) {
      this.paint.setColor(var1);
   }

   public void setDashPathEffect(float[] var1) {
      DashPathEffect var2 = new DashPathEffect(var1, 0.0F);
      this.paint.setPathEffect(var2);
   }

   public void setStrokeCap(Cap var1) {
      android.graphics.Paint.Cap var2 = android.graphics.Paint.Cap.valueOf(var1.name());
      this.paint.setStrokeCap(var2);
   }

   public void setStrokeWidth(float var1) {
      this.paint.setStrokeWidth(var1);
   }

   public void setStyle(Style var1) {
      android.graphics.Paint.Style var2 = android.graphics.Paint.Style.valueOf(var1.name());
      this.paint.setStyle(var2);
   }

   public void setTextAlign(Align var1) {
      android.graphics.Paint.Align var2 = android.graphics.Paint.Align.valueOf(var1.name());
      this.paint.setTextAlign(var2);
   }

   public void setTextSize(float var1) {
      this.paint.setTextSize(var1);
   }

   public void setTypeface(FontFamily var1, FontStyle var2) {
      Typeface var3 = Typeface.create(getTypeface(var1), getStyle(var2));
      this.paint.setTypeface(var3);
   }
}

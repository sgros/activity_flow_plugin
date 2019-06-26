package org.mapsforge.map.graphics;

public interface Paint {
   void destroy();

   int getColor();

   int getTextHeight(String var1);

   int getTextWidth(String var1);

   void setBitmapShader(Bitmap var1);

   void setColor(int var1);

   void setDashPathEffect(float[] var1);

   void setStrokeCap(Cap var1);

   void setStrokeWidth(float var1);

   void setStyle(Style var1);

   void setTextAlign(Align var1);

   void setTextSize(float var1);

   void setTypeface(FontFamily var1, FontStyle var2);
}

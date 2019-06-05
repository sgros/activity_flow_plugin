package org.mapsforge.graphics.android;

import android.graphics.Bitmap;
import android.graphics.Paint;
import java.io.InputStream;
import org.mapsforge.map.rendertheme.GraphicAdapter;

public final class AndroidGraphics implements GraphicAdapter {
   public static final AndroidGraphics INSTANCE = new AndroidGraphics();

   private AndroidGraphics() {
   }

   public static Bitmap getAndroidBitmap(org.mapsforge.map.graphics.Bitmap var0) {
      return ((AndroidBitmap)var0).bitmap;
   }

   public static Paint getAndroidPaint(org.mapsforge.map.graphics.Paint var0) {
      return ((AndroidPaint)var0).paint;
   }

   public org.mapsforge.map.graphics.Bitmap decodeStream(InputStream var1) {
      return new AndroidBitmap(var1);
   }

   public int getColor(GraphicAdapter.Color var1) {
      int var2;
      switch(var1) {
      case BLACK:
         var2 = -16777216;
         break;
      case CYAN:
         var2 = -16711681;
         break;
      case TRANSPARENT:
         var2 = 0;
         break;
      case WHITE:
         var2 = -1;
         break;
      default:
         throw new IllegalArgumentException("unknown color value: " + var1);
      }

      return var2;
   }

   public org.mapsforge.map.graphics.Paint getPaint() {
      return new AndroidPaint();
   }

   public int parseColor(String var1) {
      return android.graphics.Color.parseColor(var1);
   }
}

package org.mapsforge.map.rendertheme;

import java.io.InputStream;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.graphics.Paint;

public interface GraphicAdapter {
   Bitmap decodeStream(InputStream var1);

   int getColor(GraphicAdapter.Color var1);

   Paint getPaint();

   int parseColor(String var1);

   public static enum Color {
      BLACK,
      CYAN,
      TRANSPARENT,
      WHITE;
   }
}

package org.mapsforge.map.graphics;

public interface Bitmap {
   void destroy();

   int getHeight();

   int[] getPixels();

   int getWidth();
}

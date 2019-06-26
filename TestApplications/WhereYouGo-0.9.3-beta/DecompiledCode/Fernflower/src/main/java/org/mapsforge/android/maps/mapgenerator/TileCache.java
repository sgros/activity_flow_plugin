package org.mapsforge.android.maps.mapgenerator;

import android.graphics.Bitmap;

public interface TileCache {
   boolean containsKey(MapGeneratorJob var1);

   void destroy();

   Bitmap get(MapGeneratorJob var1);

   int getCapacity();

   boolean isPersistent();

   void put(MapGeneratorJob var1, Bitmap var2);

   void setCapacity(int var1);

   void setPersistent(boolean var1);
}

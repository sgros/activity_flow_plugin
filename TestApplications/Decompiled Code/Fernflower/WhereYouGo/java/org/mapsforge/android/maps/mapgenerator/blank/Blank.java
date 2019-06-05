package org.mapsforge.android.maps.mapgenerator.blank;

import android.graphics.Bitmap;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.android.maps.mapgenerator.MapGeneratorJob;
import org.mapsforge.core.model.GeoPoint;

public class Blank implements MapGenerator {
   public boolean executeJob(MapGeneratorJob var1, Bitmap var2) {
      return false;
   }

   public GeoPoint getStartPoint() {
      return null;
   }

   public Byte getStartZoomLevel() {
      return null;
   }

   public byte getZoomLevelMax() {
      return 127;
   }

   public boolean requiresInternetConnection() {
      return false;
   }
}

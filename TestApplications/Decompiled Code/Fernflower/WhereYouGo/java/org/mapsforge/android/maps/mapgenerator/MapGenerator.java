package org.mapsforge.android.maps.mapgenerator;

import android.graphics.Bitmap;
import org.mapsforge.core.model.GeoPoint;

public interface MapGenerator {
   boolean executeJob(MapGeneratorJob var1, Bitmap var2);

   GeoPoint getStartPoint();

   Byte getStartZoomLevel();

   byte getZoomLevelMax();

   boolean requiresInternetConnection();
}

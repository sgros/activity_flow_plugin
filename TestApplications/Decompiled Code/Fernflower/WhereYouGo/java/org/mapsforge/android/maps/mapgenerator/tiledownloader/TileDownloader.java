package org.mapsforge.android.maps.mapgenerator.tiledownloader;

import android.graphics.Bitmap;
import java.util.logging.Logger;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.android.maps.mapgenerator.MapGeneratorJob;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Tile;

public abstract class TileDownloader implements MapGenerator {
   private static final Logger LOG = Logger.getLogger(TileDownloader.class.getName());
   private static final GeoPoint START_POINT = new GeoPoint(51.33D, 10.45D);
   private static final Byte START_ZOOM_LEVEL = 5;
   private final int[] pixels = new int[65536];

   protected TileDownloader() {
   }

   public final boolean executeJob(MapGeneratorJob param1, Bitmap param2) {
      // $FF: Couldn't be decompiled
   }

   public abstract String getAttribution();

   public abstract String getHostName();

   public abstract String getProtocol();

   public final GeoPoint getStartPoint() {
      return START_POINT;
   }

   public final Byte getStartZoomLevel() {
      return START_ZOOM_LEVEL;
   }

   public abstract String getTilePath(Tile var1);

   public final boolean requiresInternetConnection() {
      return true;
   }
}

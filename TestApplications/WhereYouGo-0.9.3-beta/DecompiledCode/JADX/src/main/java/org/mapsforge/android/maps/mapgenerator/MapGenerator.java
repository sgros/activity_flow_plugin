package org.mapsforge.android.maps.mapgenerator;

import android.graphics.Bitmap;
import org.mapsforge.core.model.GeoPoint;

public interface MapGenerator {
    boolean executeJob(MapGeneratorJob mapGeneratorJob, Bitmap bitmap);

    GeoPoint getStartPoint();

    Byte getStartZoomLevel();

    byte getZoomLevelMax();

    boolean requiresInternetConnection();
}

package org.mapsforge.android.maps;

import android.graphics.Point;
import org.mapsforge.core.model.GeoPoint;

public interface Projection {
    GeoPoint fromPixels(int i, int i2);

    double getLatitudeSpan();

    double getLongitudeSpan();

    float metersToPixels(float f, byte b);

    Point toPixels(GeoPoint geoPoint, Point point);

    Point toPoint(GeoPoint geoPoint, Point point, byte b);
}

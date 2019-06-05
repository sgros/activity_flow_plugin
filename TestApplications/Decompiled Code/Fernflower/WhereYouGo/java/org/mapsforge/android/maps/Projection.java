package org.mapsforge.android.maps;

import android.graphics.Point;
import org.mapsforge.core.model.GeoPoint;

public interface Projection {
   GeoPoint fromPixels(int var1, int var2);

   double getLatitudeSpan();

   double getLongitudeSpan();

   float metersToPixels(float var1, byte var2);

   Point toPixels(GeoPoint var1, Point var2);

   Point toPoint(GeoPoint var1, Point var2, byte var3);
}

package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.map.graphics.Paint;

class WayTextContainer {
   final double[] coordinates;
   final Paint paint;
   final String text;

   WayTextContainer(double[] var1, String var2, Paint var3) {
      this.coordinates = var1;
      this.text = var2;
      this.paint = var3;
   }
}

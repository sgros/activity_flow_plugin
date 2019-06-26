package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.core.model.Point;

class WayContainer implements ShapeContainer {
   final Point[][] coordinates;

   WayContainer(Point[][] var1) {
      this.coordinates = var1;
   }

   public ShapeType getShapeType() {
      return ShapeType.WAY;
   }
}

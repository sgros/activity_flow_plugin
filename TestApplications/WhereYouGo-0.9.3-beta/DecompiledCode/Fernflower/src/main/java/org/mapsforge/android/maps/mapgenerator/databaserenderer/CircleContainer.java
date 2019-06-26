package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.core.model.Point;

class CircleContainer implements ShapeContainer {
   final Point point;
   final float radius;

   CircleContainer(Point var1, float var2) {
      this.point = var1;
      this.radius = var2;
   }

   public ShapeType getShapeType() {
      return ShapeType.CIRCLE;
   }
}

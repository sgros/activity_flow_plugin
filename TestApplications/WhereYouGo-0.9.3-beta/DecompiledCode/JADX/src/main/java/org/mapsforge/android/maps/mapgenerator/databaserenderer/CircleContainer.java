package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.core.model.Point;

class CircleContainer implements ShapeContainer {
    final Point point;
    final float radius;

    CircleContainer(Point point, float radius) {
        this.point = point;
        this.radius = radius;
    }

    public ShapeType getShapeType() {
        return ShapeType.CIRCLE;
    }
}

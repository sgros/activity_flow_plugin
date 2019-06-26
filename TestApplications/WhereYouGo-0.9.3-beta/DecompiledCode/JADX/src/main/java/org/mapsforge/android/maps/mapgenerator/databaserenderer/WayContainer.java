package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.core.model.Point;

class WayContainer implements ShapeContainer {
    final Point[][] coordinates;

    WayContainer(Point[][] coordinates) {
        this.coordinates = coordinates;
    }

    public ShapeType getShapeType() {
        return ShapeType.WAY;
    }
}

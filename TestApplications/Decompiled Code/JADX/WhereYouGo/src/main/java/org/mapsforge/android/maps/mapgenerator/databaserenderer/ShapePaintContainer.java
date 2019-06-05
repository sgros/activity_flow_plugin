package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.map.graphics.Paint;

class ShapePaintContainer {
    final Paint paint;
    final ShapeContainer shapeContainer;

    ShapePaintContainer(ShapeContainer shapeContainer, Paint paint) {
        this.shapeContainer = shapeContainer;
        this.paint = paint;
    }
}

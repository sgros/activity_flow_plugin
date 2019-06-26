package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.map.graphics.Paint;

class WayTextContainer {
    final double[] coordinates;
    final Paint paint;
    final String text;

    WayTextContainer(double[] coordinates, String text, Paint paint) {
        this.coordinates = coordinates;
        this.text = text;
        this.paint = paint;
    }
}

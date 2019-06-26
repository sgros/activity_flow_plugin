package org.osmdroid.events;

import org.osmdroid.views.MapView;

public class ZoomEvent implements MapEvent {
    protected MapView source;
    protected double zoomLevel;

    public ZoomEvent(MapView mapView, double d) {
        this.source = mapView;
        this.zoomLevel = d;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ZoomEvent [source=");
        stringBuilder.append(this.source);
        stringBuilder.append(", zoomLevel=");
        stringBuilder.append(this.zoomLevel);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}

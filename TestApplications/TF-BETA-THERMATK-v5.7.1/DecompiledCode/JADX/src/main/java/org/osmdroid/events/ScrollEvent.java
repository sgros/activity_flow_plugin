package org.osmdroid.events;

import org.osmdroid.views.MapView;

public class ScrollEvent implements MapEvent {
    protected MapView source;
    /* renamed from: x */
    protected int f626x;
    /* renamed from: y */
    protected int f627y;

    public ScrollEvent(MapView mapView, int i, int i2) {
        this.source = mapView;
        this.f626x = i;
        this.f627y = i2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ScrollEvent [source=");
        stringBuilder.append(this.source);
        stringBuilder.append(", x=");
        stringBuilder.append(this.f626x);
        stringBuilder.append(", y=");
        stringBuilder.append(this.f627y);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}

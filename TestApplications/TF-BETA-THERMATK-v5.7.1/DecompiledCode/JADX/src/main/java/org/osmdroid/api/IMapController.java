package org.osmdroid.api;

public interface IMapController {
    void animateTo(IGeoPoint iGeoPoint);

    void animateTo(IGeoPoint iGeoPoint, Double d, Long l);

    void setCenter(IGeoPoint iGeoPoint);

    double setZoom(double d);

    void stopAnimation(boolean z);

    boolean zoomIn();

    boolean zoomInFixing(int i, int i2);

    boolean zoomOut();

    boolean zoomOutFixing(int i, int i2);
}

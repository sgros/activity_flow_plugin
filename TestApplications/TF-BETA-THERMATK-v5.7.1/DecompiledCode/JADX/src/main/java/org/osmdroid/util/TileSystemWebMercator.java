package org.osmdroid.util;

public class TileSystemWebMercator extends TileSystem {
    public double getMaxLatitude() {
        return 85.05112877980658d;
    }

    public double getMaxLongitude() {
        return 180.0d;
    }

    public double getMinLatitude() {
        return -85.05112877980658d;
    }

    public double getMinLongitude() {
        return -180.0d;
    }

    public double getX01FromLongitude(double d) {
        return (d - getMinLongitude()) / (getMaxLongitude() - getMinLongitude());
    }

    public double getY01FromLatitude(double d) {
        d = Math.sin((d * 3.141592653589793d) / 180.0d);
        return 0.5d - (Math.log((d + 1.0d) / (1.0d - d)) / 12.566370614359172d);
    }

    public double getLongitudeFromX01(double d) {
        return getMinLongitude() + ((getMaxLongitude() - getMinLongitude()) * d);
    }

    public double getLatitudeFromY01(double d) {
        return 90.0d - ((Math.atan(Math.exp(((d - 0.5d) * 2.0d) * 3.141592653589793d)) * 360.0d) / 3.141592653589793d);
    }
}

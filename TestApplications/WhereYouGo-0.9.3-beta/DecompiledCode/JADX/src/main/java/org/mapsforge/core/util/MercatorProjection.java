package org.mapsforge.core.util;

public final class MercatorProjection {
    public static final double EARTH_CIRCUMFERENCE = 4.0075016686E7d;
    public static final double LATITUDE_MAX = 85.05112877980659d;
    public static final double LATITUDE_MIN = -85.05112877980659d;

    public static double calculateGroundResolution(double latitude, byte zoomLevel) {
        return (Math.cos(0.017453292519943295d * latitude) * 4.0075016686E7d) / ((double) getMapSize(zoomLevel));
    }

    public static double deltaLat(double deltaPixel, double lat, byte zoom) {
        return Math.abs(pixelYToLatitude(latitudeToPixelY(lat, zoom) + deltaPixel, zoom) - lat);
    }

    public static long getMapSize(byte zoomLevel) {
        if (zoomLevel >= (byte) 0) {
            return 256 << zoomLevel;
        }
        throw new IllegalArgumentException("zoom level must not be negative: " + zoomLevel);
    }

    public static double latitudeToPixelY(double latitude, byte zoomLevel) {
        double sinLatitude = Math.sin(0.017453292519943295d * latitude);
        long mapSize = getMapSize(zoomLevel);
        return Math.min(Math.max(0.0d, (0.5d - (Math.log((1.0d + sinLatitude) / (1.0d - sinLatitude)) / 12.566370614359172d)) * ((double) mapSize)), (double) mapSize);
    }

    public static long latitudeToTileY(double latitude, byte zoomLevel) {
        return pixelYToTileY(latitudeToPixelY(latitude, zoomLevel), zoomLevel);
    }

    public static double longitudeToPixelX(double longitude, byte zoomLevel) {
        return ((180.0d + longitude) / 360.0d) * ((double) getMapSize(zoomLevel));
    }

    public static long longitudeToTileX(double longitude, byte zoomLevel) {
        return pixelXToTileX(longitudeToPixelX(longitude, zoomLevel), zoomLevel);
    }

    public static double pixelXToLongitude(double pixelX, byte zoomLevel) {
        long mapSize = getMapSize(zoomLevel);
        if (pixelX >= 0.0d && pixelX <= ((double) mapSize)) {
            return 360.0d * ((pixelX / ((double) mapSize)) - 0.5d);
        }
        throw new IllegalArgumentException("invalid pixelX coordinate at zoom level " + zoomLevel + ": " + pixelX);
    }

    public static long pixelXToTileX(double pixelX, byte zoomLevel) {
        return (long) Math.min(Math.max(pixelX / 256.0d, 0.0d), Math.pow(2.0d, (double) zoomLevel) - 1.0d);
    }

    public static double pixelYToLatitude(double pixelY, byte zoomLevel) {
        long mapSize = getMapSize(zoomLevel);
        if (pixelY >= 0.0d && pixelY <= ((double) mapSize)) {
            return 90.0d - ((360.0d * Math.atan(Math.exp((-(0.5d - (pixelY / ((double) mapSize)))) * 6.283185307179586d))) / 3.141592653589793d);
        }
        throw new IllegalArgumentException("invalid pixelY coordinate at zoom level " + zoomLevel + ": " + pixelY);
    }

    public static long pixelYToTileY(double pixelY, byte zoomLevel) {
        return (long) Math.min(Math.max(pixelY / 256.0d, 0.0d), Math.pow(2.0d, (double) zoomLevel) - 1.0d);
    }

    public static double tileXToLongitude(long tileX, byte zoomLevel) {
        return pixelXToLongitude((double) (256 * tileX), zoomLevel);
    }

    public static double tileYToLatitude(long tileY, byte zoomLevel) {
        return pixelYToLatitude((double) (256 * tileY), zoomLevel);
    }

    private MercatorProjection() {
        throw new IllegalStateException();
    }
}

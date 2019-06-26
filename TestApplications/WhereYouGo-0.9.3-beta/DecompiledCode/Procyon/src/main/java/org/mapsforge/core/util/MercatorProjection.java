// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.core.util;

public final class MercatorProjection
{
    public static final double EARTH_CIRCUMFERENCE = 4.0075016686E7;
    public static final double LATITUDE_MAX = 85.05112877980659;
    public static final double LATITUDE_MIN = -85.05112877980659;
    
    private MercatorProjection() {
        throw new IllegalStateException();
    }
    
    public static double calculateGroundResolution(final double n, final byte b) {
        return Math.cos(0.017453292519943295 * n) * 4.0075016686E7 / getMapSize(b);
    }
    
    public static double deltaLat(final double n, final double n2, final byte b) {
        return Math.abs(pixelYToLatitude(latitudeToPixelY(n2, b) + n, b) - n2);
    }
    
    public static long getMapSize(final byte i) {
        if (i < 0) {
            throw new IllegalArgumentException("zoom level must not be negative: " + i);
        }
        return 256L << i;
    }
    
    public static double latitudeToPixelY(double sin, final byte b) {
        sin = Math.sin(0.017453292519943295 * sin);
        final long mapSize = getMapSize(b);
        return Math.min(Math.max(0.0, (0.5 - Math.log((1.0 + sin) / (1.0 - sin)) / 12.566370614359172) * mapSize), (double)mapSize);
    }
    
    public static long latitudeToTileY(final double n, final byte b) {
        return pixelYToTileY(latitudeToPixelY(n, b), b);
    }
    
    public static double longitudeToPixelX(final double n, final byte b) {
        return (180.0 + n) / 360.0 * getMapSize(b);
    }
    
    public static long longitudeToTileX(final double n, final byte b) {
        return pixelXToTileX(longitudeToPixelX(n, b), b);
    }
    
    public static double pixelXToLongitude(final double d, final byte i) {
        final long mapSize = getMapSize(i);
        if (d < 0.0 || d > mapSize) {
            throw new IllegalArgumentException("invalid pixelX coordinate at zoom level " + i + ": " + d);
        }
        return 360.0 * (d / mapSize - 0.5);
    }
    
    public static long pixelXToTileX(final double n, final byte b) {
        return (long)Math.min(Math.max(n / 256.0, 0.0), Math.pow(2.0, b) - 1.0);
    }
    
    public static double pixelYToLatitude(final double d, final byte i) {
        final long mapSize = getMapSize(i);
        if (d < 0.0 || d > mapSize) {
            throw new IllegalArgumentException("invalid pixelY coordinate at zoom level " + i + ": " + d);
        }
        return 90.0 - 360.0 * Math.atan(Math.exp(-(0.5 - d / mapSize) * 6.283185307179586)) / 3.141592653589793;
    }
    
    public static long pixelYToTileY(final double n, final byte b) {
        return (long)Math.min(Math.max(n / 256.0, 0.0), Math.pow(2.0, b) - 1.0);
    }
    
    public static double tileXToLongitude(final long n, final byte b) {
        return pixelXToLongitude((double)(256L * n), b);
    }
    
    public static double tileYToLatitude(final long n, final byte b) {
        return pixelYToLatitude((double)(256L * n), b);
    }
}

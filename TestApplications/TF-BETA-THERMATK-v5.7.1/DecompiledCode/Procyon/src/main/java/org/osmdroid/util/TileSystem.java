// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import android.graphics.Rect;

public abstract class TileSystem
{
    private static int mMaxZoomLevel = 29;
    private static int mTileSize = 256;
    
    public static double Clip(final double a, final double b, final double b2) {
        return Math.min(Math.max(a, b), b2);
    }
    
    public static long ClipToLong(final double n, final double n2, final boolean b) {
        final long floorToLong = MyMath.floorToLong(n);
        if (!b) {
            return floorToLong;
        }
        if (floorToLong <= 0L) {
            return 0L;
        }
        final long floorToLong2 = MyMath.floorToLong(n2 - 1.0);
        long n3 = floorToLong;
        if (floorToLong >= n2) {
            n3 = floorToLong2;
        }
        return n3;
    }
    
    public static double GroundResolution(final double n, final double n2) {
        return GroundResolutionMapSize(wrap(n, -90.0, 90.0, 180.0), MapSize(n2));
    }
    
    public static double GroundResolutionMapSize(final double n, final double n2) {
        return Math.cos(Clip(n, -90.0, 90.0) * 3.141592653589793 / 180.0) * 2.0 * 3.141592653589793 * 6378137.0 / n2;
    }
    
    public static double MapSize(double factor) {
        final double v = getTileSize();
        factor = getFactor(factor);
        Double.isNaN(v);
        return v * factor;
    }
    
    public static double getFactor(final double b) {
        return Math.pow(2.0, b);
    }
    
    public static int getInputTileZoomLevel(final double n) {
        return MyMath.floorToInt(n);
    }
    
    public static int getMaximumZoomLevel() {
        return TileSystem.mMaxZoomLevel;
    }
    
    public static long getMercatorFromTile(final int n, final double n2) {
        final double v = n;
        Double.isNaN(v);
        return Math.round(v * n2);
    }
    
    public static int getTileFromMercator(final long n, final double n2) {
        final double v = (double)n;
        Double.isNaN(v);
        return MyMath.floorToInt(v / n2);
    }
    
    public static Rect getTileFromMercator(final RectL rectL, final double n, final Rect rect) {
        Rect rect2 = rect;
        if (rect == null) {
            rect2 = new Rect();
        }
        rect2.left = getTileFromMercator(rectL.left, n);
        rect2.top = getTileFromMercator(rectL.top, n);
        rect2.right = getTileFromMercator(rectL.right, n);
        rect2.bottom = getTileFromMercator(rectL.bottom, n);
        return rect2;
    }
    
    public static double getTileSize(final double n) {
        final double v = getInputTileZoomLevel(n);
        Double.isNaN(v);
        return MapSize(n - v);
    }
    
    public static int getTileSize() {
        return TileSystem.mTileSize;
    }
    
    public static void setTileSize(final int mTileSize) {
        TileSystem.mMaxZoomLevel = Math.min(29, 63 - (int)(Math.log(mTileSize) / Math.log(2.0) + 0.5) - 1);
        TileSystem.mTileSize = mTileSize;
    }
    
    public static int truncateToInt(final long a) {
        return (int)Math.max(Math.min(a, 2147483647L), -2147483648L);
    }
    
    private static double wrap(double n, final double n2, final double n3, final double d) {
        if (n2 > n3) {
            final StringBuilder sb = new StringBuilder();
            sb.append("minValue must be smaller than maxValue: ");
            sb.append(n2);
            sb.append(">");
            sb.append(n3);
            throw new IllegalArgumentException(sb.toString());
        }
        if (d <= n3 - n2 + 1.0) {
            double n4;
            while (true) {
                n4 = n;
                if (n >= n2) {
                    break;
                }
                n += d;
            }
            while (n4 > n3) {
                n4 -= d;
            }
            return n4;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("interval must be equal or smaller than maxValue-minValue: min: ");
        sb2.append(n2);
        sb2.append(" max:");
        sb2.append(n3);
        sb2.append(" int:");
        sb2.append(d);
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public double cleanLatitude(final double n) {
        return Clip(n, this.getMinLatitude(), this.getMaxLatitude());
    }
    
    public double cleanLongitude(double n) {
        while (n < -180.0) {
            n += 360.0;
        }
        while (n > 180.0) {
            n -= 360.0;
        }
        return Clip(n, this.getMinLongitude(), this.getMaxLongitude());
    }
    
    public double getBoundingBoxZoom(final BoundingBox boundingBox, final int n, final int n2) {
        final double longitudeZoom = this.getLongitudeZoom(boundingBox.getLonEast(), boundingBox.getLonWest(), n);
        final double latitudeZoom = this.getLatitudeZoom(boundingBox.getLatNorth(), boundingBox.getLatSouth(), n2);
        if (longitudeZoom == Double.MIN_VALUE) {
            return latitudeZoom;
        }
        if (latitudeZoom == Double.MIN_VALUE) {
            return longitudeZoom;
        }
        return Math.min(latitudeZoom, longitudeZoom);
    }
    
    public long getCleanMercator(final long n, final double n2, final boolean b) {
        double wrap;
        if (b) {
            wrap = wrap((double)n, 0.0, n2, n2);
        }
        else {
            wrap = (double)n;
        }
        return ClipToLong(wrap, n2, b);
    }
    
    public GeoPoint getGeoFromMercator(final long n, final long n2, final double n3, final GeoPoint geoPoint, final boolean b, final boolean b2) {
        GeoPoint geoPoint2 = geoPoint;
        if (geoPoint == null) {
            geoPoint2 = new GeoPoint(0.0, 0.0);
        }
        geoPoint2.setLatitude(this.getLatitudeFromY01(this.getXY01FromMercator(n2, n3, b2), b2));
        geoPoint2.setLongitude(this.getLongitudeFromX01(this.getXY01FromMercator(n, n3, b), b));
        return geoPoint2;
    }
    
    public abstract double getLatitudeFromY01(final double p0);
    
    public double getLatitudeFromY01(double n, final boolean b) {
        double clip = n;
        if (b) {
            clip = Clip(n, 0.0, 1.0);
        }
        final double n2 = n = this.getLatitudeFromY01(clip);
        if (b) {
            n = Clip(n2, this.getMinLatitude(), this.getMaxLatitude());
        }
        return n;
    }
    
    public double getLatitudeZoom(double y01FromLatitude, double v, final int n) {
        y01FromLatitude = this.getY01FromLatitude(y01FromLatitude, true);
        y01FromLatitude = this.getY01FromLatitude(v, true) - y01FromLatitude;
        if (y01FromLatitude <= 0.0) {
            return Double.MIN_VALUE;
        }
        v = n;
        Double.isNaN(v);
        v /= y01FromLatitude;
        y01FromLatitude = getTileSize();
        Double.isNaN(y01FromLatitude);
        return Math.log(v / y01FromLatitude) / Math.log(2.0);
    }
    
    public abstract double getLongitudeFromX01(final double p0);
    
    public double getLongitudeFromX01(double n, final boolean b) {
        double clip = n;
        if (b) {
            clip = Clip(n, 0.0, 1.0);
        }
        final double n2 = n = this.getLongitudeFromX01(clip);
        if (b) {
            n = Clip(n2, this.getMinLongitude(), this.getMaxLongitude());
        }
        return n;
    }
    
    public double getLongitudeZoom(double v, double x01FromLongitude, final int n) {
        x01FromLongitude = this.getX01FromLongitude(x01FromLongitude, true);
        x01FromLongitude = (v = this.getX01FromLongitude(v, true) - x01FromLongitude);
        if (x01FromLongitude < 0.0) {
            v = x01FromLongitude + 1.0;
        }
        if (v == 0.0) {
            return Double.MIN_VALUE;
        }
        x01FromLongitude = n;
        Double.isNaN(x01FromLongitude);
        x01FromLongitude /= v;
        v = getTileSize();
        Double.isNaN(v);
        return Math.log(x01FromLongitude / v) / Math.log(2.0);
    }
    
    public abstract double getMaxLatitude();
    
    public abstract double getMaxLongitude();
    
    public long getMercatorFromXY01(final double n, final double n2, final boolean b) {
        return ClipToLong(n * n2, n2, b);
    }
    
    public long getMercatorXFromLongitude(final double n, final double n2, final boolean b) {
        return this.getMercatorFromXY01(this.getX01FromLongitude(n, b), n2, b);
    }
    
    public long getMercatorYFromLatitude(final double n, final double n2, final boolean b) {
        return this.getMercatorFromXY01(this.getY01FromLatitude(n, b), n2, b);
    }
    
    public abstract double getMinLatitude();
    
    public abstract double getMinLongitude();
    
    public abstract double getX01FromLongitude(final double p0);
    
    public double getX01FromLongitude(double n, final boolean b) {
        double clip = n;
        if (b) {
            clip = Clip(n, this.getMinLongitude(), this.getMaxLongitude());
        }
        final double n2 = n = this.getX01FromLongitude(clip);
        if (b) {
            n = Clip(n2, 0.0, 1.0);
        }
        return n;
    }
    
    public double getXY01FromMercator(final long n, double clip, final boolean b) {
        if (b) {
            final double v = (double)n;
            Double.isNaN(v);
            clip = Clip(v / clip, 0.0, 1.0);
        }
        else {
            final double v2 = (double)n;
            Double.isNaN(v2);
            clip = v2 / clip;
        }
        return clip;
    }
    
    public abstract double getY01FromLatitude(final double p0);
    
    public double getY01FromLatitude(double n, final boolean b) {
        double clip = n;
        if (b) {
            clip = Clip(n, this.getMinLatitude(), this.getMaxLatitude());
        }
        final double n2 = n = this.getY01FromLatitude(clip);
        if (b) {
            n = Clip(n2, 0.0, 1.0);
        }
        return n;
    }
    
    public boolean isValidLatitude(final double n) {
        return n >= this.getMinLatitude() && n <= this.getMaxLatitude();
    }
    
    public boolean isValidLongitude(final double n) {
        return n >= this.getMinLongitude() && n <= this.getMaxLongitude();
    }
    
    public String toStringLatitudeSpan() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.getMinLatitude());
        sb.append(",");
        sb.append(this.getMaxLatitude());
        sb.append("]");
        return sb.toString();
    }
    
    public String toStringLongitudeSpan() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.getMinLongitude());
        sb.append(",");
        sb.append(this.getMaxLongitude());
        sb.append("]");
        return sb.toString();
    }
}

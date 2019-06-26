package org.osmdroid.util;

import android.graphics.Rect;

public abstract class TileSystem {
    private static int mMaxZoomLevel = 29;
    private static int mTileSize = 256;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:15:0x0060 in {6, 9, 10, 12, 14} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private static double wrap(double r4, double r6, double r8, double r10) {
        /*
        r0 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r0 > 0) goto L_0x0041;
        r0 = r8 - r6;
        r2 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r0 = r0 + r2;
        r2 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1));
        if (r2 > 0) goto L_0x001a;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 >= 0) goto L_0x0013;
        r4 = r4 + r10;
        goto L_0x000d;
        r6 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r6 <= 0) goto L_0x0019;
        r4 = r4 - r10;
        goto L_0x0013;
        return r4;
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r0 = "interval must be equal or smaller than maxValue-minValue: min: ";
        r5.append(r0);
        r5.append(r6);
        r6 = " max:";
        r5.append(r6);
        r5.append(r8);
        r6 = " int:";
        r5.append(r6);
        r5.append(r10);
        r5 = r5.toString();
        r4.<init>(r5);
        throw r4;
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r10 = "minValue must be smaller than maxValue: ";
        r5.append(r10);
        r5.append(r6);
        r6 = ">";
        r5.append(r6);
        r5.append(r8);
        r5 = r5.toString();
        r4.<init>(r5);
        throw r4;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.util.TileSystem.wrap(double, double, double, double):double");
    }

    public abstract double getLatitudeFromY01(double d);

    public abstract double getLongitudeFromX01(double d);

    public abstract double getMaxLatitude();

    public abstract double getMaxLongitude();

    public abstract double getMinLatitude();

    public abstract double getMinLongitude();

    public abstract double getX01FromLongitude(double d);

    public abstract double getY01FromLatitude(double d);

    public static void setTileSize(int i) {
        mMaxZoomLevel = Math.min(29, (63 - ((int) ((Math.log((double) i) / Math.log(2.0d)) + 0.5d))) - 1);
        mTileSize = i;
    }

    public static int getTileSize() {
        return mTileSize;
    }

    public static int getMaximumZoomLevel() {
        return mMaxZoomLevel;
    }

    public static double getTileSize(double d) {
        double inputTileZoomLevel = (double) getInputTileZoomLevel(d);
        Double.isNaN(inputTileZoomLevel);
        return MapSize(d - inputTileZoomLevel);
    }

    public static int getInputTileZoomLevel(double d) {
        return MyMath.floorToInt(d);
    }

    public static double MapSize(double d) {
        double tileSize = (double) getTileSize();
        d = getFactor(d);
        Double.isNaN(tileSize);
        return tileSize * d;
    }

    public static double getFactor(double d) {
        return Math.pow(2.0d, d);
    }

    public static double GroundResolution(double d, double d2) {
        return GroundResolutionMapSize(wrap(d, -90.0d, 90.0d, 180.0d), MapSize(d2));
    }

    public static double GroundResolutionMapSize(double d, double d2) {
        return (((Math.cos((Clip(d, -90.0d, 90.0d) * 3.141592653589793d) / 180.0d) * 2.0d) * 3.141592653589793d) * 6378137.0d) / d2;
    }

    public double getX01FromLongitude(double d, boolean z) {
        if (z) {
            d = Clip(d, getMinLongitude(), getMaxLongitude());
        }
        double x01FromLongitude = getX01FromLongitude(d);
        return z ? Clip(x01FromLongitude, 0.0d, 1.0d) : x01FromLongitude;
    }

    public double getY01FromLatitude(double d, boolean z) {
        if (z) {
            d = Clip(d, getMinLatitude(), getMaxLatitude());
        }
        double y01FromLatitude = getY01FromLatitude(d);
        return z ? Clip(y01FromLatitude, 0.0d, 1.0d) : y01FromLatitude;
    }

    public static double Clip(double d, double d2, double d3) {
        return Math.min(Math.max(d, d2), d3);
    }

    public double getBoundingBoxZoom(BoundingBox boundingBox, int i, int i2) {
        double longitudeZoom = getLongitudeZoom(boundingBox.getLonEast(), boundingBox.getLonWest(), i);
        double latitudeZoom = getLatitudeZoom(boundingBox.getLatNorth(), boundingBox.getLatSouth(), i2);
        if (longitudeZoom == Double.MIN_VALUE) {
            return latitudeZoom;
        }
        if (latitudeZoom == Double.MIN_VALUE) {
            return longitudeZoom;
        }
        return Math.min(latitudeZoom, longitudeZoom);
    }

    public double getLongitudeZoom(double d, double d2, int i) {
        d = getX01FromLongitude(d, true) - getX01FromLongitude(d2, true);
        if (d < 0.0d) {
            d += 1.0d;
        }
        if (d == 0.0d) {
            return Double.MIN_VALUE;
        }
        d2 = (double) i;
        Double.isNaN(d2);
        d2 /= d;
        d = (double) getTileSize();
        Double.isNaN(d);
        return Math.log(d2 / d) / Math.log(2.0d);
    }

    public double getLatitudeZoom(double d, double d2, int i) {
        d2 = getY01FromLatitude(d2, true) - getY01FromLatitude(d, true);
        if (d2 <= 0.0d) {
            return Double.MIN_VALUE;
        }
        d = (double) i;
        Double.isNaN(d);
        d /= d2;
        d2 = (double) getTileSize();
        Double.isNaN(d2);
        return Math.log(d / d2) / Math.log(2.0d);
    }

    public long getMercatorYFromLatitude(double d, double d2, boolean z) {
        return getMercatorFromXY01(getY01FromLatitude(d, z), d2, z);
    }

    public long getMercatorXFromLongitude(double d, double d2, boolean z) {
        return getMercatorFromXY01(getX01FromLongitude(d, z), d2, z);
    }

    public long getMercatorFromXY01(double d, double d2, boolean z) {
        return ClipToLong(d * d2, d2, z);
    }

    public double getLatitudeFromY01(double d, boolean z) {
        if (z) {
            d = Clip(d, 0.0d, 1.0d);
        }
        double latitudeFromY01 = getLatitudeFromY01(d);
        return z ? Clip(latitudeFromY01, getMinLatitude(), getMaxLatitude()) : latitudeFromY01;
    }

    public double getLongitudeFromX01(double d, boolean z) {
        if (z) {
            d = Clip(d, 0.0d, 1.0d);
        }
        double longitudeFromX01 = getLongitudeFromX01(d);
        return z ? Clip(longitudeFromX01, getMinLongitude(), getMaxLongitude()) : longitudeFromX01;
    }

    public long getCleanMercator(long j, double d, boolean z) {
        return ClipToLong(z ? wrap((double) j, 0.0d, d, d) : (double) j, d, z);
    }

    public static long ClipToLong(double d, double d2, boolean z) {
        long floorToLong = MyMath.floorToLong(d);
        if (!z) {
            return floorToLong;
        }
        if (floorToLong <= 0) {
            return 0;
        }
        long floorToLong2 = MyMath.floorToLong(d2 - 1.0d);
        if (((double) floorToLong) >= d2) {
            floorToLong = floorToLong2;
        }
        return floorToLong;
    }

    public static int truncateToInt(long j) {
        return (int) Math.max(Math.min(j, 2147483647L), -2147483648L);
    }

    public GeoPoint getGeoFromMercator(long j, long j2, double d, GeoPoint geoPoint, boolean z, boolean z2) {
        if (geoPoint == null) {
            geoPoint = new GeoPoint(0.0d, 0.0d);
        }
        double d2 = d;
        geoPoint.setLatitude(getLatitudeFromY01(getXY01FromMercator(j2, d2, z2), z2));
        geoPoint.setLongitude(getLongitudeFromX01(getXY01FromMercator(j, d2, z), z));
        return geoPoint;
    }

    public double getXY01FromMercator(long j, double d, boolean z) {
        double d2;
        if (z) {
            d2 = (double) j;
            Double.isNaN(d2);
            return Clip(d2 / d, 0.0d, 1.0d);
        }
        d2 = (double) j;
        Double.isNaN(d2);
        return d2 / d;
    }

    public static int getTileFromMercator(long j, double d) {
        double d2 = (double) j;
        Double.isNaN(d2);
        return MyMath.floorToInt(d2 / d);
    }

    public static Rect getTileFromMercator(RectL rectL, double d, Rect rect) {
        if (rect == null) {
            rect = new Rect();
        }
        rect.left = getTileFromMercator(rectL.left, d);
        rect.top = getTileFromMercator(rectL.top, d);
        rect.right = getTileFromMercator(rectL.right, d);
        rect.bottom = getTileFromMercator(rectL.bottom, d);
        return rect;
    }

    public static long getMercatorFromTile(int i, double d) {
        double d2 = (double) i;
        Double.isNaN(d2);
        return Math.round(d2 * d);
    }

    public double cleanLongitude(double d) {
        while (d < -180.0d) {
            d += 360.0d;
        }
        double d2 = d;
        while (d2 > 180.0d) {
            d2 -= 360.0d;
        }
        return Clip(d2, getMinLongitude(), getMaxLongitude());
    }

    public double cleanLatitude(double d) {
        return Clip(d, getMinLatitude(), getMaxLatitude());
    }

    public boolean isValidLongitude(double d) {
        return d >= getMinLongitude() && d <= getMaxLongitude();
    }

    public boolean isValidLatitude(double d) {
        return d >= getMinLatitude() && d <= getMaxLatitude();
    }

    public String toStringLongitudeSpan() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(getMinLongitude());
        stringBuilder.append(",");
        stringBuilder.append(getMaxLongitude());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public String toStringLatitudeSpan() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(getMinLatitude());
        stringBuilder.append(",");
        stringBuilder.append(getMaxLatitude());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}

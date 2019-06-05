// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.extra;

public class LocationCompute
{
    public static final double AVERAGE_RADIUS_OF_EARTH = 6371000.0;
    private static double[] mDistResult;
    private static final double parWgs84AxisA = 6378137.0;
    private static final double parWgs84AxisB = 6356752.3142;
    private static final double parWgs84Flat = 0.0033528106718309896;
    private Location loc;
    private double mLat1;
    private double mLat2;
    private double mLon1;
    private double mLon2;
    private final float[] mResults;
    
    static {
        LocationCompute.mDistResult = new double[1];
    }
    
    public LocationCompute(final Location loc) {
        this.mLat1 = 0.0;
        this.mLon1 = 0.0;
        this.mLat2 = 0.0;
        this.mLon2 = 0.0;
        this.mResults = new float[2];
        this.loc = loc;
    }
    
    public static void computeDistanceAndBearing(double n, double n2, double n3, double cos, double n4, final double n5, final double n6, final float[] array) {
        final double n7 = (n4 * n4 - n5 * n5) / (n5 * n5);
        final double n8 = cos * 0.017453292519943295 - n2 * 0.017453292519943295;
        n4 = 0.0;
        n2 = Math.atan((1.0 - n6) * Math.tan(n * 0.017453292519943295));
        n = Math.atan((1.0 - n6) * Math.tan(n3 * 0.017453292519943295));
        final double cos2 = Math.cos(n2);
        final double cos3 = Math.cos(n);
        final double sin = Math.sin(n2);
        final double sin2 = Math.sin(n);
        final double n9 = cos2 * cos3;
        final double n10 = sin * sin2;
        n2 = 0.0;
        n3 = 0.0;
        cos = 0.0;
        n = 0.0;
        double n11 = n8;
        int n12 = 0;
        while (true) {
            final double n13 = n11;
            if (n12 >= 20) {
                break;
            }
            cos = Math.cos(n13);
            n = Math.sin(n13);
            n3 = cos3 * n;
            n2 = cos2 * sin2 - sin * cos3 * cos;
            final double sqrt = Math.sqrt(n3 * n3 + n2 * n2);
            final double x = n10 + n9 * cos;
            n2 = Math.atan2(sqrt, x);
            if (sqrt == 0.0) {
                n3 = 0.0;
            }
            else {
                n3 = n9 * n / sqrt;
            }
            final double n14 = 1.0 - n3 * n3;
            double n15;
            if (n14 == 0.0) {
                n15 = 0.0;
            }
            else {
                n15 = x - 2.0 * n10 / n14;
            }
            final double n16 = n14 * n7;
            n4 = 1.0 + n16 / 16384.0 * (4096.0 + (-768.0 + (320.0 - 175.0 * n16) * n16) * n16);
            final double n17 = n16 / 1024.0 * (256.0 + (-128.0 + (74.0 - 47.0 * n16) * n16) * n16);
            final double n18 = n6 / 16.0 * n14 * (4.0 + (4.0 - 3.0 * n14) * n6);
            final double n19 = n15 * n15;
            final double n20 = n17 * sqrt * (n17 / 4.0 * ((-1.0 + 2.0 * n19) * x - n17 / 6.0 * n15 * (-3.0 + 4.0 * sqrt * sqrt) * (-3.0 + 4.0 * n19)) + n15);
            final double n21 = n8 + (1.0 - n18) * n6 * n3 * (n18 * sqrt * (n18 * x * (-1.0 + 2.0 * n15 * n15) + n15) + n2);
            if (Math.abs((n21 - n13) / n21) < 1.0E-12) {
                n3 = n20;
                break;
            }
            ++n12;
            n3 = n20;
            n11 = n21;
        }
        array[0] = (float)(n5 * n4 * (n2 - n3));
        if (array.length > 1) {
            array[1] = (float)((float)Math.atan2(cos3 * n, cos2 * sin2 - sin * cos3 * cos) * 57.29577951308232);
            if (array.length > 2) {
                array[2] = (float)((float)Math.atan2(cos2 * n, -sin * cos3 + cos2 * sin2 * cos) * 57.29577951308232);
            }
        }
    }
    
    public static void computeDistanceAndBearing(final double n, final double n2, final double n3, final double n4, final float[] array) {
        computeDistanceAndBearing(n, n2, n3, n4, 6378137.0, 6356752.3142, 0.0033528106718309896, array);
    }
    
    public static void computeDistanceAndBearingFast(double n, double n2, double n3, double n4, final double[] array) {
        n *= 0.017453292519943295;
        n3 *= 0.017453292519943295;
        n2 *= 0.017453292519943295;
        n4 *= 0.017453292519943295;
        final double cos = Math.cos(n);
        final double cos2 = Math.cos(n3);
        final double sin = Math.sin((n3 - n) / 2.0);
        final double sin2 = Math.sin((n4 - n2) / 2.0);
        final double a = sin * sin + cos * cos2 * sin2 * sin2;
        array[0] = 6371000.0 * (2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a)));
        if (array.length > 1) {
            array[1] = Math.toDegrees(Math.atan2(Math.sin(n4 - n2) * cos2, Math.sin(n3) * cos - Math.sin(n) * cos2 * Math.cos(n4 - n2)));
        }
    }
    
    public static double computeDistanceFast(double n, final double n2, final double n3, final double n4) {
        synchronized (LocationCompute.class) {
            computeDistanceAndBearingFast(n, n2, n3, n4, LocationCompute.mDistResult);
            n = LocationCompute.mDistResult[0];
            return n;
        }
    }
    
    public static double computeDistanceFast(final Location location, final Location location2) {
        synchronized (LocationCompute.class) {
            computeDistanceAndBearingFast(location.getLatitude(), location.getLongitude(), location2.getLatitude(), location2.getLongitude(), LocationCompute.mDistResult);
            return LocationCompute.mDistResult[0];
        }
    }
    
    public static void distanceBetween(final double n, final double n2, final double n3, final double n4, final float[] array) {
        if (array == null || array.length < 1) {
            throw new IllegalArgumentException("results is null or has length < 1");
        }
        computeDistanceAndBearing(n, n2, n3, n4, array);
    }
    
    public float bearingTo(final Location location) {
        synchronized (this.mResults) {
            if (this.loc.latitude != this.mLat1 || this.loc.longitude != this.mLon1 || location.latitude != this.mLat2 || location.longitude != this.mLon2) {
                computeDistanceAndBearing(this.loc.latitude, this.loc.longitude, location.latitude, location.longitude, this.mResults);
                this.mLat1 = this.loc.latitude;
                this.mLon1 = this.loc.longitude;
                this.mLat2 = location.latitude;
                this.mLon2 = location.longitude;
            }
            return this.mResults[1];
        }
    }
    
    public float distanceTo(final Location location) {
        synchronized (this.mResults) {
            if (this.loc.latitude != this.mLat1 || this.loc.longitude != this.mLon1 || location.latitude != this.mLat2 || location.longitude != this.mLon2) {
                computeDistanceAndBearing(this.loc.latitude, this.loc.longitude, location.latitude, location.longitude, this.mResults);
                this.mLat1 = this.loc.latitude;
                this.mLon1 = this.loc.longitude;
                this.mLat2 = location.latitude;
                this.mLon2 = location.longitude;
            }
            return this.mResults[0];
        }
    }
}

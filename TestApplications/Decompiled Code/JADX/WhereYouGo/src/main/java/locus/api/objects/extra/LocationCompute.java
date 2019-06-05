package locus.api.objects.extra;

public class LocationCompute {
    public static final double AVERAGE_RADIUS_OF_EARTH = 6371000.0d;
    private static double[] mDistResult = new double[1];
    private static final double parWgs84AxisA = 6378137.0d;
    private static final double parWgs84AxisB = 6356752.3142d;
    private static final double parWgs84Flat = 0.0033528106718309896d;
    private Location loc;
    private double mLat1 = 0.0d;
    private double mLat2 = 0.0d;
    private double mLon1 = 0.0d;
    private double mLon2 = 0.0d;
    private final float[] mResults = new float[2];

    public LocationCompute(Location loc) {
        this.loc = loc;
    }

    public static void computeDistanceAndBearing(double lat1, double lon1, double lat2, double lon2, float[] results) {
        computeDistanceAndBearing(lat1, lon1, lat2, lon2, parWgs84AxisA, parWgs84AxisB, parWgs84Flat, results);
    }

    public static void computeDistanceAndBearing(double lat1, double lon1, double lat2, double lon2, double a, double b, double f, float[] results) {
        lat2 *= 0.017453292519943295d;
        double aSqMinusBSqOverBSq = ((a * a) - (b * b)) / (b * b);
        double L = (lon2 * 0.017453292519943295d) - (lon1 * 0.017453292519943295d);
        double A = 0.0d;
        double U1 = Math.atan((1.0d - f) * Math.tan(lat1 * 0.017453292519943295d));
        double U2 = Math.atan((1.0d - f) * Math.tan(lat2));
        double cosU1 = Math.cos(U1);
        double cosU2 = Math.cos(U2);
        double sinU1 = Math.sin(U1);
        double sinU2 = Math.sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = sinU1 * sinU2;
        double sigma = 0.0d;
        double deltaSigma = 0.0d;
        double cosLambda = 0.0d;
        double sinLambda = 0.0d;
        double lambda = L;
        for (int iter = 0; iter < 20; iter++) {
            double cos2SM;
            double lambdaOrig = lambda;
            cosLambda = Math.cos(lambda);
            sinLambda = Math.sin(lambda);
            double t1 = cosU2 * sinLambda;
            double t2 = (cosU1 * sinU2) - ((sinU1 * cosU2) * cosLambda);
            double sinSigma = Math.sqrt((t1 * t1) + (t2 * t2));
            double cosSigma = sinU1sinU2 + (cosU1cosU2 * cosLambda);
            sigma = Math.atan2(sinSigma, cosSigma);
            double sinAlpha = sinSigma == 0.0d ? 0.0d : (cosU1cosU2 * sinLambda) / sinSigma;
            double cosSqAlpha = 1.0d - (sinAlpha * sinAlpha);
            if (cosSqAlpha == 0.0d) {
                cos2SM = 0.0d;
            } else {
                cos2SM = cosSigma - ((2.0d * sinU1sinU2) / cosSqAlpha);
            }
            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq;
            A = 1.0d + ((uSquared / 16384.0d) * (4096.0d + ((-768.0d + ((320.0d - (175.0d * uSquared)) * uSquared)) * uSquared)));
            double B = (uSquared / 1024.0d) * (256.0d + ((-128.0d + ((74.0d - (47.0d * uSquared)) * uSquared)) * uSquared));
            double C = ((f / 16.0d) * cosSqAlpha) * (4.0d + ((4.0d - (3.0d * cosSqAlpha)) * f));
            double cos2SMSq = cos2SM * cos2SM;
            deltaSigma = (B * sinSigma) * (((B / 4.0d) * (((-1.0d + (2.0d * cos2SMSq)) * cosSigma) - ((((B / 6.0d) * cos2SM) * (-3.0d + ((4.0d * sinSigma) * sinSigma))) * (-3.0d + (4.0d * cos2SMSq))))) + cos2SM);
            lambda = L + ((((1.0d - C) * f) * sinAlpha) * (((C * sinSigma) * (((C * cosSigma) * (-1.0d + ((2.0d * cos2SM) * cos2SM))) + cos2SM)) + sigma));
            if (Math.abs((lambda - lambdaOrig) / lambda) < 1.0E-12d) {
                break;
            }
        }
        results[0] = (float) ((b * A) * (sigma - deltaSigma));
        if (results.length > 1) {
            results[1] = (float) (((double) ((float) Math.atan2(cosU2 * sinLambda, (cosU1 * sinU2) - ((sinU1 * cosU2) * cosLambda)))) * 57.29577951308232d);
            if (results.length > 2) {
                results[2] = (float) (((double) ((float) Math.atan2(cosU1 * sinLambda, ((-sinU1) * cosU2) + ((cosU1 * sinU2) * cosLambda)))) * 57.29577951308232d);
            }
        }
    }

    public static void distanceBetween(double startLatitude, double startLongitude, double endLatitude, double endLongitude, float[] results) {
        if (results == null || results.length < 1) {
            throw new IllegalArgumentException("results is null or has length < 1");
        }
        computeDistanceAndBearing(startLatitude, startLongitude, endLatitude, endLongitude, results);
    }

    public float distanceTo(Location dest) {
        float f;
        synchronized (this.mResults) {
            if (!(this.loc.latitude == this.mLat1 && this.loc.longitude == this.mLon1 && dest.latitude == this.mLat2 && dest.longitude == this.mLon2)) {
                computeDistanceAndBearing(this.loc.latitude, this.loc.longitude, dest.latitude, dest.longitude, this.mResults);
                this.mLat1 = this.loc.latitude;
                this.mLon1 = this.loc.longitude;
                this.mLat2 = dest.latitude;
                this.mLon2 = dest.longitude;
            }
            f = this.mResults[0];
        }
        return f;
    }

    public float bearingTo(Location dest) {
        float f;
        synchronized (this.mResults) {
            if (!(this.loc.latitude == this.mLat1 && this.loc.longitude == this.mLon1 && dest.latitude == this.mLat2 && dest.longitude == this.mLon2)) {
                computeDistanceAndBearing(this.loc.latitude, this.loc.longitude, dest.latitude, dest.longitude, this.mResults);
                this.mLat1 = this.loc.latitude;
                this.mLon1 = this.loc.longitude;
                this.mLat2 = dest.latitude;
                this.mLon2 = dest.longitude;
            }
            f = this.mResults[1];
        }
        return f;
    }

    public static synchronized double computeDistanceFast(Location loc1, Location loc2) {
        double d;
        synchronized (LocationCompute.class) {
            computeDistanceAndBearingFast(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude(), mDistResult);
            d = mDistResult[0];
        }
        return d;
    }

    public static synchronized double computeDistanceFast(double lat1, double lon1, double lat2, double lon2) {
        double d;
        synchronized (LocationCompute.class) {
            computeDistanceAndBearingFast(lat1, lon1, lat2, lon2, mDistResult);
            d = mDistResult[0];
        }
        return d;
    }

    public static void computeDistanceAndBearingFast(double lat1, double lon1, double lat2, double lon2, double[] results) {
        lat1 *= 0.017453292519943295d;
        lat2 *= 0.017453292519943295d;
        lon1 *= 0.017453292519943295d;
        lon2 *= 0.017453292519943295d;
        double cosLat1 = Math.cos(lat1);
        double cosLat2 = Math.cos(lat2);
        double sinDLat2 = Math.sin((lat2 - lat1) / 2.0d);
        double sinDLon2 = Math.sin((lon2 - lon1) / 2.0d);
        double a = (sinDLat2 * sinDLat2) + (((cosLat1 * cosLat2) * sinDLon2) * sinDLon2);
        results[0] = 6371000.0d * (2.0d * Math.atan2(Math.sqrt(a), Math.sqrt(1.0d - a)));
        if (results.length > 1) {
            results[1] = Math.toDegrees(Math.atan2(Math.sin(lon2 - lon1) * cosLat2, (Math.sin(lat2) * cosLat1) - ((Math.sin(lat1) * cosLat2) * Math.cos(lon2 - lon1))));
        }
    }
}

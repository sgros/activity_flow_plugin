// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.core.model;

import java.util.ArrayList;
import java.util.StringTokenizer;

public final class CoordinatesUtil
{
    private static final double CONVERSION_FACTOR = 1000000.0;
    private static final String DELIMITER = ",";
    public static final double LATITUDE_MAX = 90.0;
    public static final double LATITUDE_MIN = -90.0;
    public static final double LONGITUDE_MAX = 180.0;
    public static final double LONGITUDE_MIN = -180.0;
    
    private CoordinatesUtil() {
        throw new IllegalStateException();
    }
    
    public static int degreesToMicrodegrees(final double n) {
        return (int)(1000000.0 * n);
    }
    
    public static double microdegreesToDegrees(final int n) {
        return n / 1000000.0;
    }
    
    public static double[] parseCoordinateString(final String str, final int initialCapacity) {
        final StringTokenizer stringTokenizer = new StringTokenizer(str, ",", true);
        int n = 1;
        final ArrayList<String> list = new ArrayList<String>(initialCapacity);
        while (stringTokenizer.hasMoreTokens()) {
            final String nextToken = stringTokenizer.nextToken();
            int n2;
            if (n == 0) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            n = n2;
            if (n2 == 0) {
                list.add(nextToken);
                n = n2;
            }
        }
        if (n != 0) {
            throw new IllegalArgumentException("invalid coordinate delimiter: " + str);
        }
        if (list.size() != initialCapacity) {
            throw new IllegalArgumentException("invalid number of coordinate values: " + str);
        }
        final double[] array = new double[initialCapacity];
        for (int i = 0; i < initialCapacity; ++i) {
            array[i] = Double.parseDouble((String)list.get(i));
        }
        return array;
    }
    
    public static void validateLatitude(final double n) {
        if (Double.isNaN(n) || n < -90.0 || n > 90.0) {
            throw new IllegalArgumentException("invalid latitude: " + n);
        }
    }
    
    public static void validateLongitude(final double n) {
        if (Double.isNaN(n) || n < -180.0 || n > 180.0) {
            throw new IllegalArgumentException("invalid longitude: " + n);
        }
    }
}

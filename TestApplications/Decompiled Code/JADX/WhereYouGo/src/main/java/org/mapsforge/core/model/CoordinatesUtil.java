package org.mapsforge.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class CoordinatesUtil {
    private static final double CONVERSION_FACTOR = 1000000.0d;
    private static final String DELIMITER = ",";
    public static final double LATITUDE_MAX = 90.0d;
    public static final double LATITUDE_MIN = -90.0d;
    public static final double LONGITUDE_MAX = 180.0d;
    public static final double LONGITUDE_MIN = -180.0d;

    public static int degreesToMicrodegrees(double coordinate) {
        return (int) (CONVERSION_FACTOR * coordinate);
    }

    public static double microdegreesToDegrees(int coordinate) {
        return ((double) coordinate) / CONVERSION_FACTOR;
    }

    public static double[] parseCoordinateString(String coordinatesString, int numberOfCoordinates) {
        StringTokenizer stringTokenizer = new StringTokenizer(coordinatesString, DELIMITER, true);
        boolean isDelimiter = true;
        List<String> tokens = new ArrayList(numberOfCoordinates);
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            isDelimiter = !isDelimiter;
            if (!isDelimiter) {
                tokens.add(token);
            }
        }
        if (isDelimiter) {
            throw new IllegalArgumentException("invalid coordinate delimiter: " + coordinatesString);
        } else if (tokens.size() != numberOfCoordinates) {
            throw new IllegalArgumentException("invalid number of coordinate values: " + coordinatesString);
        } else {
            double[] coordinates = new double[numberOfCoordinates];
            for (int i = 0; i < numberOfCoordinates; i++) {
                coordinates[i] = Double.parseDouble((String) tokens.get(i));
            }
            return coordinates;
        }
    }

    public static void validateLatitude(double latitude) {
        if (Double.isNaN(latitude) || latitude < -90.0d || latitude > 90.0d) {
            throw new IllegalArgumentException("invalid latitude: " + latitude);
        }
    }

    public static void validateLongitude(double longitude) {
        if (Double.isNaN(longitude) || longitude < -180.0d || longitude > 180.0d) {
            throw new IllegalArgumentException("invalid longitude: " + longitude);
        }
    }

    private CoordinatesUtil() {
        throw new IllegalStateException();
    }
}

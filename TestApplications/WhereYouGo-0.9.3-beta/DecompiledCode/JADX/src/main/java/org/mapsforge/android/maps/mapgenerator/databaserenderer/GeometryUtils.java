package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.core.model.Point;

final class GeometryUtils {
    static Point calculateCenterOfBoundingBox(Point[] coordinates) {
        double pointXMin = coordinates[0].f68x;
        double pointXMax = coordinates[0].f68x;
        double pointYMin = coordinates[0].f69y;
        double pointYMax = coordinates[0].f69y;
        for (int i = 1; i < coordinates.length; i++) {
            Point immutablePoint = coordinates[i];
            if (immutablePoint.f68x < pointXMin) {
                pointXMin = immutablePoint.f68x;
            } else if (immutablePoint.f68x > pointXMax) {
                pointXMax = immutablePoint.f68x;
            }
            if (immutablePoint.f69y < pointYMin) {
                pointYMin = immutablePoint.f69y;
            } else if (immutablePoint.f69y > pointYMax) {
                pointYMax = immutablePoint.f69y;
            }
        }
        return new Point((pointXMin + pointXMax) / 2.0d, (pointYMax + pointYMin) / 2.0d);
    }

    static boolean isClosedWay(Point[] way) {
        return way[0].equals(way[way.length - 1]);
    }

    private GeometryUtils() {
        throw new IllegalStateException();
    }
}

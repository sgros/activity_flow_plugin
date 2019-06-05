// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.core.model.Point;

final class GeometryUtils
{
    private GeometryUtils() {
        throw new IllegalStateException();
    }
    
    static Point calculateCenterOfBoundingBox(final Point[] array) {
        double x = array[0].x;
        double x2 = array[0].x;
        double y = array[0].y;
        double y2 = array[0].y;
        double x3;
        double x4;
        double y3;
        double y4;
        for (int i = 1; i < array.length; ++i, x2 = x4, x = x3, y2 = y4, y = y3) {
            final Point point = array[i];
            if (point.x < x) {
                x3 = point.x;
                x4 = x2;
            }
            else {
                x4 = x2;
                x3 = x;
                if (point.x > x2) {
                    x4 = point.x;
                    x3 = x;
                }
            }
            if (point.y < y) {
                y3 = point.y;
                y4 = y2;
            }
            else {
                y4 = y2;
                y3 = y;
                if (point.y > y2) {
                    y4 = point.y;
                    y3 = y;
                }
            }
        }
        return new Point((x + x2) / 2.0, (y2 + y) / 2.0);
    }
    
    static boolean isClosedWay(final Point[] array) {
        return array[0].equals(array[array.length - 1]);
    }
}

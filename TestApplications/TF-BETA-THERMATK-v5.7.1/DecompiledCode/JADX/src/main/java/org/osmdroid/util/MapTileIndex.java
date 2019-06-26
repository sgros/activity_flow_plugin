package org.osmdroid.util;

public class MapTileIndex {
    public static int mMaxZoomLevel = 29;
    private static int mModulo = (1 << mMaxZoomLevel);

    public static long getTileIndex(int i, int i2, int i3) {
        checkValues(i, i2, i3);
        long j = (long) i;
        i = mMaxZoomLevel;
        return ((j << (i * 2)) + (((long) i2) << i)) + ((long) i3);
    }

    public static int getZoom(long j) {
        return (int) (j >> (mMaxZoomLevel * 2));
    }

    public static int getX(long j) {
        return (int) ((j >> mMaxZoomLevel) % ((long) mModulo));
    }

    public static int getY(long j) {
        return (int) (j % ((long) mModulo));
    }

    public static String toString(int i, int i2, int i3) {
        StringBuilder stringBuilder = new StringBuilder();
        String str = "/";
        stringBuilder.append(str);
        stringBuilder.append(i);
        stringBuilder.append(str);
        stringBuilder.append(i2);
        stringBuilder.append(str);
        stringBuilder.append(i3);
        return stringBuilder.toString();
    }

    public static String toString(long j) {
        return toString(getZoom(j), getX(j), getY(j));
    }

    private static void checkValues(int i, int i2, int i3) {
        if (i < 0 || i > mMaxZoomLevel) {
            throwIllegalValue(i, i, "Zoom");
            throw null;
        }
        long j = (long) (1 << i);
        if (i2 < 0 || ((long) i2) >= j) {
            throwIllegalValue(i, i2, "X");
            throw null;
        } else if (i3 < 0 || ((long) i3) >= j) {
            throwIllegalValue(i, i3, "Y");
            throw null;
        }
    }

    private static void throwIllegalValue(int i, int i2, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MapTileIndex: ");
        stringBuilder.append(str);
        stringBuilder.append(" (");
        stringBuilder.append(i2);
        stringBuilder.append(") is too big (zoom=");
        stringBuilder.append(i);
        stringBuilder.append(")");
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}

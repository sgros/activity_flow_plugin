// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

public class MapTileIndex
{
    public static int mMaxZoomLevel = 29;
    private static int mModulo;
    
    static {
        MapTileIndex.mModulo = 1 << MapTileIndex.mMaxZoomLevel;
    }
    
    private static void checkValues(final int n, final int n2, final int n3) {
        if (n < 0 || n > MapTileIndex.mMaxZoomLevel) {
            throwIllegalValue(n, n, "Zoom");
            throw null;
        }
        final long n4 = 1 << n;
        if (n2 < 0 || n2 >= n4) {
            throwIllegalValue(n, n2, "X");
            throw null;
        }
        if (n3 >= 0 && n3 < n4) {
            return;
        }
        throwIllegalValue(n, n3, "Y");
        throw null;
    }
    
    public static long getTileIndex(int mMaxZoomLevel, final int n, final int n2) {
        checkValues(mMaxZoomLevel, n, n2);
        final long n3 = mMaxZoomLevel;
        mMaxZoomLevel = MapTileIndex.mMaxZoomLevel;
        return (n3 << mMaxZoomLevel * 2) + ((long)n << mMaxZoomLevel) + n2;
    }
    
    public static int getX(final long n) {
        return (int)((n >> MapTileIndex.mMaxZoomLevel) % MapTileIndex.mModulo);
    }
    
    public static int getY(final long n) {
        return (int)(n % MapTileIndex.mModulo);
    }
    
    public static int getZoom(final long n) {
        return (int)(n >> MapTileIndex.mMaxZoomLevel * 2);
    }
    
    private static void throwIllegalValue(final int i, final int j, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("MapTileIndex: ");
        sb.append(str);
        sb.append(" (");
        sb.append(j);
        sb.append(") is too big (zoom=");
        sb.append(i);
        sb.append(")");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static String toString(final int i, final int j, final int k) {
        final StringBuilder sb = new StringBuilder();
        sb.append("/");
        sb.append(i);
        sb.append("/");
        sb.append(j);
        sb.append("/");
        sb.append(k);
        return sb.toString();
    }
    
    public static String toString(final long n) {
        return toString(getZoom(n), getX(n), getY(n));
    }
}

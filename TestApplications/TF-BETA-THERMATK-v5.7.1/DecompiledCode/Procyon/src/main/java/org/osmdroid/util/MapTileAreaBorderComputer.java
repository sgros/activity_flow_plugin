// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

public class MapTileAreaBorderComputer implements MapTileAreaComputer
{
    private final int mBorder;
    
    public MapTileAreaBorderComputer(final int mBorder) {
        this.mBorder = mBorder;
    }
    
    @Override
    public MapTileArea computeFromSource(final MapTileArea mapTileArea, MapTileArea mapTileArea2) {
        if (mapTileArea2 == null) {
            mapTileArea2 = new MapTileArea();
        }
        if (mapTileArea.size() == 0) {
            mapTileArea2.reset();
            return mapTileArea2;
        }
        final int n = mapTileArea.getLeft() - this.mBorder;
        final int top = mapTileArea.getTop();
        final int mBorder = this.mBorder;
        final int n2 = top - mBorder;
        final int n3 = mBorder * 2 - 1;
        mapTileArea2.set(mapTileArea.getZoom(), n, n2, mapTileArea.getWidth() + n + n3, mapTileArea.getHeight() + n2 + n3);
        return mapTileArea2;
    }
}

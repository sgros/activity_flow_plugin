// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

public class MapTileAreaZoomComputer implements MapTileAreaComputer
{
    private final int mZoomDelta;
    
    public MapTileAreaZoomComputer(final int mZoomDelta) {
        this.mZoomDelta = mZoomDelta;
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
        final int zoom = mapTileArea.getZoom();
        final int mZoomDelta = this.mZoomDelta;
        final int n = zoom + mZoomDelta;
        if (n < 0 || n > MapTileIndex.mMaxZoomLevel) {
            mapTileArea2.reset();
            return mapTileArea2;
        }
        if (mZoomDelta <= 0) {
            mapTileArea2.set(n, mapTileArea.getLeft() >> -this.mZoomDelta, mapTileArea.getTop() >> -this.mZoomDelta, mapTileArea.getRight() >> -this.mZoomDelta, mapTileArea.getBottom() >> -this.mZoomDelta);
            return mapTileArea2;
        }
        mapTileArea2.set(n, mapTileArea.getLeft() << this.mZoomDelta, mapTileArea.getTop() << this.mZoomDelta, (mapTileArea.getRight() + 1 << this.mZoomDelta) - 1, (mapTileArea.getBottom() + 1 << this.mZoomDelta) - 1);
        return mapTileArea2;
    }
}

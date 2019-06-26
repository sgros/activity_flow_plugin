package org.osmdroid.util;

public class MapTileAreaBorderComputer implements MapTileAreaComputer {
    private final int mBorder;

    public MapTileAreaBorderComputer(int i) {
        this.mBorder = i;
    }

    public MapTileArea computeFromSource(MapTileArea mapTileArea, MapTileArea mapTileArea2) {
        if (mapTileArea2 == null) {
            mapTileArea2 = new MapTileArea();
        }
        if (mapTileArea.size() == 0) {
            mapTileArea2.reset();
            return mapTileArea2;
        }
        int left = mapTileArea.getLeft() - this.mBorder;
        int top = mapTileArea.getTop();
        int i = this.mBorder;
        int i2 = top - i;
        i = (i * 2) - 1;
        mapTileArea2.set(mapTileArea.getZoom(), left, i2, (mapTileArea.getWidth() + left) + i, (mapTileArea.getHeight() + i2) + i);
        return mapTileArea2;
    }
}

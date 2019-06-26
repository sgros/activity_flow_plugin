package org.osmdroid.tileprovider.tilesource;

import org.osmdroid.util.MapTileIndex;

public class XYTileSource extends OnlineTileSourceBase {
    public XYTileSource(String str, int i, int i2, int i3, String str2, String[] strArr) {
        super(str, i, i2, i3, str2, strArr);
    }

    public XYTileSource(String str, int i, int i2, int i3, String str2, String[] strArr, String str3) {
        super(str, i, i2, i3, str2, strArr, str3);
    }

    public XYTileSource(String str, int i, int i2, int i3, String str2, String[] strArr, String str3, TileSourcePolicy tileSourcePolicy) {
        super(str, i, i2, i3, str2, strArr, str3, tileSourcePolicy);
    }

    public String toString() {
        return name();
    }

    public String getTileURLString(long j) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getBaseUrl());
        stringBuilder.append(MapTileIndex.getZoom(j));
        String str = "/";
        stringBuilder.append(str);
        stringBuilder.append(MapTileIndex.getX(j));
        stringBuilder.append(str);
        stringBuilder.append(MapTileIndex.getY(j));
        stringBuilder.append(this.mImageFilenameEnding);
        return stringBuilder.toString();
    }
}

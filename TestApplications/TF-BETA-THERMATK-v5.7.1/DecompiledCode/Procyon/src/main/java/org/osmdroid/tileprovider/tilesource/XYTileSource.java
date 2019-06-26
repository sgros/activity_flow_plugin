// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.tilesource;

import org.osmdroid.util.MapTileIndex;

public class XYTileSource extends OnlineTileSourceBase
{
    public XYTileSource(final String s, final int n, final int n2, final int n3, final String s2, final String[] array) {
        super(s, n, n2, n3, s2, array);
    }
    
    public XYTileSource(final String s, final int n, final int n2, final int n3, final String s2, final String[] array, final String s3) {
        super(s, n, n2, n3, s2, array, s3);
    }
    
    public XYTileSource(final String s, final int n, final int n2, final int n3, final String s2, final String[] array, final String s3, final TileSourcePolicy tileSourcePolicy) {
        super(s, n, n2, n3, s2, array, s3, tileSourcePolicy);
    }
    
    @Override
    public String getTileURLString(final long n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getBaseUrl());
        sb.append(MapTileIndex.getZoom(n));
        sb.append("/");
        sb.append(MapTileIndex.getX(n));
        sb.append("/");
        sb.append(MapTileIndex.getY(n));
        sb.append(super.mImageFilenameEnding);
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.name();
    }
}

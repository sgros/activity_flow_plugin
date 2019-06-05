// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.tiledownloader;

import org.mapsforge.core.model.Tile;

public class FormatURLTileDownloader extends TileDownloader
{
    private final String ATTRIBUTION;
    private final String FORMAT;
    private final String HOST_NAME;
    private final String PROTOCOL;
    private final byte ZOOM_MAX;
    private boolean quad;
    
    public FormatURLTileDownloader(final int n, final String s, final String attribution) {
        this.ZOOM_MAX = (byte)n;
        final String[] split = s.split("://");
        this.PROTOCOL = split[0];
        final String[] split2 = split[1].split("/", 2);
        this.HOST_NAME = split2[0];
        this.FORMAT = split2[1];
        this.quad = this.FORMAT.contains("{q}");
        this.ATTRIBUTION = attribution;
    }
    
    public FormatURLTileDownloader(final String s, final String s2) {
        this(18, s, s2);
    }
    
    public static String tileXYToQuadKey(final int n, int i, int n2) {
        final StringBuilder sb = new StringBuilder();
        int n3;
        char c;
        for (i = n2; i > 0; --i) {
            n2 = 48;
            n3 = 1 << i - 1;
            if ((n & n3) != 0x0) {
                n2 = 49;
            }
            c = (char)n2;
            if ((n & n3) != 0x0) {
                n2 = (c = (char)((char)(n2 + 1) + '\u0001'));
            }
            sb.append(c);
        }
        return sb.toString();
    }
    
    @Override
    public String getAttribution() {
        return this.ATTRIBUTION;
    }
    
    @Override
    public String getHostName() {
        return this.HOST_NAME;
    }
    
    @Override
    public String getProtocol() {
        return this.PROTOCOL;
    }
    
    @Override
    public String getTilePath(final Tile tile) {
        String s;
        if (this.quad) {
            s = this.FORMAT.replace("{q}", tileXYToQuadKey((int)tile.tileX, (int)tile.tileY, tile.zoomLevel));
        }
        else {
            s = this.FORMAT.replace("{x}", Long.toString(tile.tileX)).replace("{y}", Long.toString(tile.tileY)).replace("{z}", Byte.toString(tile.zoomLevel));
        }
        return s;
    }
    
    @Override
    public byte getZoomLevelMax() {
        return this.ZOOM_MAX;
    }
}

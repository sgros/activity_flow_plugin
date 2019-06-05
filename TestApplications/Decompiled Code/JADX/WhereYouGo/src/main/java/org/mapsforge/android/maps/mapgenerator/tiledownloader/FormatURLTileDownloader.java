package org.mapsforge.android.maps.mapgenerator.tiledownloader;

import org.mapsforge.core.model.Tile;

public class FormatURLTileDownloader extends TileDownloader {
    private final String ATTRIBUTION;
    private final String FORMAT;
    private final String HOST_NAME;
    private final String PROTOCOL;
    private final byte ZOOM_MAX;
    private boolean quad;

    public FormatURLTileDownloader(String format, String attribution) {
        this(18, format, attribution);
    }

    public FormatURLTileDownloader(int maxZoom, String format, String attribution) {
        this.ZOOM_MAX = (byte) maxZoom;
        String[] tokens = format.split("://");
        this.PROTOCOL = tokens[0];
        tokens = tokens[1].split("/", 2);
        this.HOST_NAME = tokens[0];
        this.FORMAT = tokens[1];
        this.quad = this.FORMAT.contains("{q}");
        this.ATTRIBUTION = attribution;
    }

    public String getHostName() {
        return this.HOST_NAME;
    }

    public String getProtocol() {
        return this.PROTOCOL;
    }

    public String getTilePath(Tile tile) {
        if (this.quad) {
            return this.FORMAT.replace("{q}", tileXYToQuadKey((int) tile.tileX, (int) tile.tileY, tile.zoomLevel));
        }
        return this.FORMAT.replace("{x}", Long.toString(tile.tileX)).replace("{y}", Long.toString(tile.tileY)).replace("{z}", Byte.toString(tile.zoomLevel));
    }

    public byte getZoomLevelMax() {
        return this.ZOOM_MAX;
    }

    public static String tileXYToQuadKey(int x, int y, int z) {
        StringBuilder quadKey = new StringBuilder();
        for (int i = z; i > 0; i--) {
            char digit = '0';
            int mask = 1 << (i - 1);
            if ((x & mask) != 0) {
                digit = (char) 49;
            }
            if ((x & mask) != 0) {
                digit = (char) (((char) (digit + 1)) + 1);
            }
            quadKey.append(digit);
        }
        return quadKey.toString();
    }

    public String getAttribution() {
        return this.ATTRIBUTION;
    }
}

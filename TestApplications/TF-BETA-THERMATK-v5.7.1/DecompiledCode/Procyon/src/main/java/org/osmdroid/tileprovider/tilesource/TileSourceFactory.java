// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.tilesource;

import java.util.Iterator;
import java.util.ArrayList;
import org.osmdroid.util.MapTileIndex;
import java.util.List;

public class TileSourceFactory
{
    public static final OnlineTileSourceBase BASE_OVERLAY_NL;
    public static final OnlineTileSourceBase CLOUDMADESMALLTILES;
    public static final OnlineTileSourceBase CLOUDMADESTANDARDTILES;
    public static final OnlineTileSourceBase ChartbundleENRH;
    public static final OnlineTileSourceBase ChartbundleENRL;
    public static final OnlineTileSourceBase ChartbundleWAC;
    public static final OnlineTileSourceBase DEFAULT_TILE_SOURCE;
    public static final OnlineTileSourceBase FIETS_OVERLAY_NL;
    public static final OnlineTileSourceBase HIKEBIKEMAP;
    public static final OnlineTileSourceBase MAPNIK;
    public static final OnlineTileSourceBase OPEN_SEAMAP;
    public static final OnlineTileSourceBase OpenTopo;
    public static final OnlineTileSourceBase PUBLIC_TRANSPORT;
    public static final OnlineTileSourceBase ROADS_OVERLAY_NL;
    public static final OnlineTileSourceBase USGS_SAT;
    public static final OnlineTileSourceBase USGS_TOPO;
    private static List<ITileSource> mTileSources;
    
    static {
        MAPNIK = new XYTileSource("Mapnik", 0, 19, 256, ".png", new String[] { "https://a.tile.openstreetmap.org/", "https://b.tile.openstreetmap.org/", "https://c.tile.openstreetmap.org/" }, "© OpenStreetMap contributors", new TileSourcePolicy(2, 15));
        PUBLIC_TRANSPORT = new XYTileSource("OSMPublicTransport", 0, 17, 256, ".png", new String[] { "http://openptmap.org/tiles/" }, "© OpenStreetMap contributors");
        DEFAULT_TILE_SOURCE = TileSourceFactory.MAPNIK;
        CLOUDMADESTANDARDTILES = new CloudmadeTileSource("CloudMadeStandardTiles", 0, 18, 256, ".png", new String[] { "http://a.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s", "http://b.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s", "http://c.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s" });
        CLOUDMADESMALLTILES = new CloudmadeTileSource("CloudMadeSmallTiles", 0, 21, 64, ".png", new String[] { "http://a.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s", "http://b.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s", "http://c.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s" });
        FIETS_OVERLAY_NL = new XYTileSource("Fiets", 3, 18, 256, ".png", new String[] { "https://overlay.openstreetmap.nl/openfietskaart-overlay/" }, "© OpenStreetMap contributors");
        BASE_OVERLAY_NL = new XYTileSource("BaseNL", 0, 18, 256, ".png", new String[] { "https://overlay.openstreetmap.nl/basemap/" });
        ROADS_OVERLAY_NL = new XYTileSource("RoadsNL", 0, 18, 256, ".png", new String[] { "https://overlay.openstreetmap.nl/roads/" }, "© OpenStreetMap contributors");
        HIKEBIKEMAP = new XYTileSource("HikeBikeMap", 0, 18, 256, ".png", new String[] { "http://a.tiles.wmflabs.org/hikebike/", "http://b.tiles.wmflabs.org/hikebike/", "http://c.tiles.wmflabs.org/hikebike/" });
        OPEN_SEAMAP = new XYTileSource("OpenSeaMap", 3, 18, 256, ".png", new String[] { "https://tiles.openseamap.org/seamark/" }, "OpenSeaMap");
        USGS_TOPO = new OnlineTileSourceBase("USGS National Map Topo", 0, 15, 256, "", new String[] { "https://basemap.nationalmap.gov/arcgis/rest/services/USGSTopo/MapServer/tile/" }, "USGS") {
            @Override
            public String getTileURLString(final long n) {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.getBaseUrl());
                sb.append(MapTileIndex.getZoom(n));
                sb.append("/");
                sb.append(MapTileIndex.getY(n));
                sb.append("/");
                sb.append(MapTileIndex.getX(n));
                return sb.toString();
            }
        };
        USGS_SAT = new OnlineTileSourceBase("USGS National Map Sat", 0, 15, 256, "", new String[] { "https://basemap.nationalmap.gov/arcgis/rest/services/USGSImageryTopo/MapServer/tile/" }, "USGS") {
            @Override
            public String getTileURLString(final long n) {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.getBaseUrl());
                sb.append(MapTileIndex.getZoom(n));
                sb.append("/");
                sb.append(MapTileIndex.getY(n));
                sb.append("/");
                sb.append(MapTileIndex.getX(n));
                return sb.toString();
            }
        };
        ChartbundleWAC = new XYTileSource("ChartbundleWAC", 4, 12, 256, ".png?type=google", new String[] { "https://wms.chartbundle.com/tms/v1.0/wac/" }, "chartbundle.com");
        ChartbundleENRH = new XYTileSource("ChartbundleENRH", 4, 12, 256, ".png?type=google", new String[] { "https://wms.chartbundle.com/tms/v1.0/enrh/", "chartbundle.com" });
        ChartbundleENRL = new XYTileSource("ChartbundleENRL", 4, 12, 256, ".png?type=google", new String[] { "https://wms.chartbundle.com/tms/v1.0/enrl/", "chartbundle.com" });
        OpenTopo = new XYTileSource("OpenTopoMap", 0, 17, 256, ".png", new String[] { "https://a.tile.opentopomap.org/", "https://b.tile.opentopomap.org/", "https://c.tile.opentopomap.org/" }, "Kartendaten: © OpenStreetMap-Mitwirkende, SRTM | Kartendarstellung: © OpenTopoMap (CC-BY-SA)");
        (TileSourceFactory.mTileSources = new ArrayList<ITileSource>()).add(TileSourceFactory.MAPNIK);
        TileSourceFactory.mTileSources.add(TileSourceFactory.PUBLIC_TRANSPORT);
        TileSourceFactory.mTileSources.add(TileSourceFactory.HIKEBIKEMAP);
        TileSourceFactory.mTileSources.add(TileSourceFactory.USGS_TOPO);
        TileSourceFactory.mTileSources.add(TileSourceFactory.USGS_SAT);
        TileSourceFactory.mTileSources.add(TileSourceFactory.ChartbundleWAC);
        TileSourceFactory.mTileSources.add(TileSourceFactory.ChartbundleENRH);
        TileSourceFactory.mTileSources.add(TileSourceFactory.ChartbundleENRL);
        TileSourceFactory.mTileSources.add(TileSourceFactory.OpenTopo);
    }
    
    public static ITileSource getTileSource(final String s) throws IllegalArgumentException {
        for (final ITileSource tileSource : TileSourceFactory.mTileSources) {
            if (tileSource.name().equals(s)) {
                return tileSource;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("No such tile source: ");
        sb.append(s);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static List<ITileSource> getTileSources() {
        return TileSourceFactory.mTileSources;
    }
}

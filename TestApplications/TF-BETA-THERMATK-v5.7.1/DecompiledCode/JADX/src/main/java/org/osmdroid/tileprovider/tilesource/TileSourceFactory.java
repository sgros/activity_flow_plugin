package org.osmdroid.tileprovider.tilesource;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.MapTileIndex;

public class TileSourceFactory {
    public static final OnlineTileSourceBase BASE_OVERLAY_NL = new XYTileSource("BaseNL", 0, 18, 256, ".png", new String[]{"https://overlay.openstreetmap.nl/basemap/"});
    public static final OnlineTileSourceBase CLOUDMADESMALLTILES = new CloudmadeTileSource("CloudMadeSmallTiles", 0, 21, 64, ".png", new String[]{"http://a.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s", "http://b.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s", "http://c.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s"});
    public static final OnlineTileSourceBase CLOUDMADESTANDARDTILES;
    public static final OnlineTileSourceBase ChartbundleENRH;
    public static final OnlineTileSourceBase ChartbundleENRL = new XYTileSource("ChartbundleENRL", 4, 12, 256, ".png?type=google", new String[]{"https://wms.chartbundle.com/tms/v1.0/enrl/", "chartbundle.com"});
    public static final OnlineTileSourceBase ChartbundleWAC = new XYTileSource("ChartbundleWAC", 4, 12, 256, ".png?type=google", new String[]{"https://wms.chartbundle.com/tms/v1.0/wac/"}, "chartbundle.com");
    public static final OnlineTileSourceBase DEFAULT_TILE_SOURCE = MAPNIK;
    public static final OnlineTileSourceBase FIETS_OVERLAY_NL = new XYTileSource("Fiets", 3, 18, 256, ".png", new String[]{"https://overlay.openstreetmap.nl/openfietskaart-overlay/"}, "© OpenStreetMap contributors");
    public static final OnlineTileSourceBase HIKEBIKEMAP = new XYTileSource("HikeBikeMap", 0, 18, 256, ".png", new String[]{"http://a.tiles.wmflabs.org/hikebike/", "http://b.tiles.wmflabs.org/hikebike/", "http://c.tiles.wmflabs.org/hikebike/"});
    public static final OnlineTileSourceBase MAPNIK;
    public static final OnlineTileSourceBase OPEN_SEAMAP = new XYTileSource("OpenSeaMap", 3, 18, 256, ".png", new String[]{"https://tiles.openseamap.org/seamark/"}, "OpenSeaMap");
    public static final OnlineTileSourceBase OpenTopo = new XYTileSource("OpenTopoMap", 0, 17, 256, ".png", new String[]{"https://a.tile.opentopomap.org/", "https://b.tile.opentopomap.org/", "https://c.tile.opentopomap.org/"}, "Kartendaten: © OpenStreetMap-Mitwirkende, SRTM | Kartendarstellung: © OpenTopoMap (CC-BY-SA)");
    public static final OnlineTileSourceBase PUBLIC_TRANSPORT = new XYTileSource("OSMPublicTransport", 0, 17, 256, ".png", new String[]{"http://openptmap.org/tiles/"}, "© OpenStreetMap contributors");
    public static final OnlineTileSourceBase ROADS_OVERLAY_NL = new XYTileSource("RoadsNL", 0, 18, 256, ".png", new String[]{"https://overlay.openstreetmap.nl/roads/"}, "© OpenStreetMap contributors");
    public static final OnlineTileSourceBase USGS_SAT = new OnlineTileSourceBase("USGS National Map Sat", 0, 15, 256, "", new String[]{"https://basemap.nationalmap.gov/arcgis/rest/services/USGSImageryTopo/MapServer/tile/"}, "USGS") {
        public String getTileURLString(long j) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getBaseUrl());
            stringBuilder.append(MapTileIndex.getZoom(j));
            String str = "/";
            stringBuilder.append(str);
            stringBuilder.append(MapTileIndex.getY(j));
            stringBuilder.append(str);
            stringBuilder.append(MapTileIndex.getX(j));
            return stringBuilder.toString();
        }
    };
    public static final OnlineTileSourceBase USGS_TOPO = new OnlineTileSourceBase("USGS National Map Topo", 0, 15, 256, "", new String[]{"https://basemap.nationalmap.gov/arcgis/rest/services/USGSTopo/MapServer/tile/"}, "USGS") {
        public String getTileURLString(long j) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getBaseUrl());
            stringBuilder.append(MapTileIndex.getZoom(j));
            String str = "/";
            stringBuilder.append(str);
            stringBuilder.append(MapTileIndex.getY(j));
            stringBuilder.append(str);
            stringBuilder.append(MapTileIndex.getX(j));
            return stringBuilder.toString();
        }
    };
    private static List<ITileSource> mTileSources = new ArrayList();

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:8:0x0034 in {5, 7} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public static org.osmdroid.tileprovider.tilesource.ITileSource getTileSource(java.lang.String r3) throws java.lang.IllegalArgumentException {
        /*
        r0 = mTileSources;
        r0 = r0.iterator();
        r1 = r0.hasNext();
        if (r1 == 0) goto L_0x001d;
        r1 = r0.next();
        r1 = (org.osmdroid.tileprovider.tilesource.ITileSource) r1;
        r2 = r1.name();
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0006;
        return r1;
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "No such tile source: ";
        r1.append(r2);
        r1.append(r3);
        r3 = r1.toString();
        r0.<init>(r3);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.tilesource.TileSourceFactory.getTileSource(java.lang.String):org.osmdroid.tileprovider.tilesource.ITileSource");
    }

    public static List<ITileSource> getTileSources() {
        return mTileSources;
    }

    static {
        String str = "Mapnik";
        String str2 = ".png";
        MAPNIK = new XYTileSource(str, 0, 19, 256, str2, new String[]{"https://a.tile.openstreetmap.org/", "https://b.tile.openstreetmap.org/", "https://c.tile.openstreetmap.org/"}, "© OpenStreetMap contributors", new TileSourcePolicy(2, 15));
        String[] strArr = new String[3];
        strArr[0] = "http://a.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s";
        strArr[1] = "http://b.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s";
        strArr[2] = "http://c.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s";
        CLOUDMADESTANDARDTILES = new CloudmadeTileSource("CloudMadeStandardTiles", 0, 18, 256, ".png", strArr);
        String[] strArr2 = new String[2];
        strArr2[0] = "https://wms.chartbundle.com/tms/v1.0/enrh/";
        strArr2[1] = "chartbundle.com";
        ChartbundleENRH = new XYTileSource("ChartbundleENRH", 4, 12, 256, ".png?type=google", strArr2);
        mTileSources.add(MAPNIK);
        mTileSources.add(PUBLIC_TRANSPORT);
        mTileSources.add(HIKEBIKEMAP);
        mTileSources.add(USGS_TOPO);
        mTileSources.add(USGS_SAT);
        mTileSources.add(ChartbundleWAC);
        mTileSources.add(ChartbundleENRH);
        mTileSources.add(ChartbundleENRL);
        mTileSources.add(OpenTopo);
    }
}

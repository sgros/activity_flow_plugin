package org.mapsforge.map.reader;

class QueryParameters {
    long fromBaseTileX;
    long fromBaseTileY;
    long fromBlockX;
    long fromBlockY;
    int queryTileBitmask;
    int queryZoomLevel;
    long toBaseTileX;
    long toBaseTileY;
    long toBlockX;
    long toBlockY;
    boolean useTileBitmask;

    QueryParameters() {
    }
}

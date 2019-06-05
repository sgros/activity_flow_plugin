package org.mapsforge.map.reader;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.reader.header.SubFileParameter;

final class QueryCalculations {
    private static int getFirstLevelTileBitmask(Tile tile) {
        if (tile.tileX % 2 == 0 && tile.tileY % 2 == 0) {
            return 52224;
        }
        if (tile.tileX % 2 == 1 && tile.tileY % 2 == 0) {
            return 13056;
        }
        if (tile.tileX % 2 == 0 && tile.tileY % 2 == 1) {
            return 204;
        }
        return 51;
    }

    private static int getSecondLevelTileBitmaskLowerLeft(long subtileX, long subtileY) {
        if (subtileX % 2 == 0 && subtileY % 2 == 0) {
            return 128;
        }
        if (subtileX % 2 == 1 && subtileY % 2 == 0) {
            return 64;
        }
        if (subtileX % 2 == 0 && subtileY % 2 == 1) {
            return 8;
        }
        return 4;
    }

    private static int getSecondLevelTileBitmaskLowerRight(long subtileX, long subtileY) {
        if (subtileX % 2 == 0 && subtileY % 2 == 0) {
            return 32;
        }
        if (subtileX % 2 == 1 && subtileY % 2 == 0) {
            return 16;
        }
        if (subtileX % 2 == 0 && subtileY % 2 == 1) {
            return 2;
        }
        return 1;
    }

    private static int getSecondLevelTileBitmaskUpperLeft(long subtileX, long subtileY) {
        if (subtileX % 2 == 0 && subtileY % 2 == 0) {
            return 32768;
        }
        if (subtileX % 2 == 1 && subtileY % 2 == 0) {
            return 16384;
        }
        if (subtileX % 2 == 0 && subtileY % 2 == 1) {
            return 2048;
        }
        return 1024;
    }

    private static int getSecondLevelTileBitmaskUpperRight(long subtileX, long subtileY) {
        if (subtileX % 2 == 0 && subtileY % 2 == 0) {
            return 8192;
        }
        if (subtileX % 2 == 1 && subtileY % 2 == 0) {
            return 4096;
        }
        if (subtileX % 2 == 0 && subtileY % 2 == 1) {
            return 512;
        }
        return 256;
    }

    static void calculateBaseTiles(QueryParameters queryParameters, Tile tile, SubFileParameter subFileParameter) {
        int zoomLevelDifference;
        if (tile.zoomLevel < subFileParameter.baseZoomLevel) {
            zoomLevelDifference = subFileParameter.baseZoomLevel - tile.zoomLevel;
            queryParameters.fromBaseTileX = tile.tileX << zoomLevelDifference;
            queryParameters.fromBaseTileY = tile.tileY << zoomLevelDifference;
            queryParameters.toBaseTileX = (queryParameters.fromBaseTileX + ((long) (1 << zoomLevelDifference))) - 1;
            queryParameters.toBaseTileY = (queryParameters.fromBaseTileY + ((long) (1 << zoomLevelDifference))) - 1;
            queryParameters.useTileBitmask = false;
        } else if (tile.zoomLevel > subFileParameter.baseZoomLevel) {
            zoomLevelDifference = tile.zoomLevel - subFileParameter.baseZoomLevel;
            queryParameters.fromBaseTileX = tile.tileX >>> zoomLevelDifference;
            queryParameters.fromBaseTileY = tile.tileY >>> zoomLevelDifference;
            queryParameters.toBaseTileX = queryParameters.fromBaseTileX;
            queryParameters.toBaseTileY = queryParameters.fromBaseTileY;
            queryParameters.useTileBitmask = true;
            queryParameters.queryTileBitmask = calculateTileBitmask(tile, zoomLevelDifference);
        } else {
            queryParameters.fromBaseTileX = tile.tileX;
            queryParameters.fromBaseTileY = tile.tileY;
            queryParameters.toBaseTileX = queryParameters.fromBaseTileX;
            queryParameters.toBaseTileY = queryParameters.fromBaseTileY;
            queryParameters.useTileBitmask = false;
        }
    }

    static void calculateBlocks(QueryParameters queryParameters, SubFileParameter subFileParameter) {
        queryParameters.fromBlockX = Math.max(queryParameters.fromBaseTileX - subFileParameter.boundaryTileLeft, 0);
        queryParameters.fromBlockY = Math.max(queryParameters.fromBaseTileY - subFileParameter.boundaryTileTop, 0);
        queryParameters.toBlockX = Math.min(queryParameters.toBaseTileX - subFileParameter.boundaryTileLeft, subFileParameter.blocksWidth - 1);
        queryParameters.toBlockY = Math.min(queryParameters.toBaseTileY - subFileParameter.boundaryTileTop, subFileParameter.blocksHeight - 1);
    }

    static int calculateTileBitmask(Tile tile, int zoomLevelDifference) {
        if (zoomLevelDifference == 1) {
            return getFirstLevelTileBitmask(tile);
        }
        long subtileX = tile.tileX >>> (zoomLevelDifference - 2);
        long subtileY = tile.tileY >>> (zoomLevelDifference - 2);
        long parentTileX = subtileX >>> 1;
        long parentTileY = subtileY >>> 1;
        if (parentTileX % 2 == 0 && parentTileY % 2 == 0) {
            return getSecondLevelTileBitmaskUpperLeft(subtileX, subtileY);
        }
        if (parentTileX % 2 == 1 && parentTileY % 2 == 0) {
            return getSecondLevelTileBitmaskUpperRight(subtileX, subtileY);
        }
        if (parentTileX % 2 == 0 && parentTileY % 2 == 1) {
            return getSecondLevelTileBitmaskLowerLeft(subtileX, subtileY);
        }
        return getSecondLevelTileBitmaskLowerRight(subtileX, subtileY);
    }

    private QueryCalculations() {
        throw new IllegalStateException();
    }
}

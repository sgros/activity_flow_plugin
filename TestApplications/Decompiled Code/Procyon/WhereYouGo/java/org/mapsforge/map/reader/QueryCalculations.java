// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader;

import org.mapsforge.map.reader.header.SubFileParameter;
import org.mapsforge.core.model.Tile;

final class QueryCalculations
{
    private QueryCalculations() {
        throw new IllegalStateException();
    }
    
    static void calculateBaseTiles(final QueryParameters queryParameters, final Tile tile, final SubFileParameter subFileParameter) {
        if (tile.zoomLevel < subFileParameter.baseZoomLevel) {
            final int n = subFileParameter.baseZoomLevel - tile.zoomLevel;
            queryParameters.fromBaseTileX = tile.tileX << n;
            queryParameters.fromBaseTileY = tile.tileY << n;
            queryParameters.toBaseTileX = queryParameters.fromBaseTileX + (1 << n) - 1L;
            queryParameters.toBaseTileY = queryParameters.fromBaseTileY + (1 << n) - 1L;
            queryParameters.useTileBitmask = false;
        }
        else if (tile.zoomLevel > subFileParameter.baseZoomLevel) {
            final int n2 = tile.zoomLevel - subFileParameter.baseZoomLevel;
            queryParameters.fromBaseTileX = tile.tileX >>> n2;
            queryParameters.fromBaseTileY = tile.tileY >>> n2;
            queryParameters.toBaseTileX = queryParameters.fromBaseTileX;
            queryParameters.toBaseTileY = queryParameters.fromBaseTileY;
            queryParameters.useTileBitmask = true;
            queryParameters.queryTileBitmask = calculateTileBitmask(tile, n2);
        }
        else {
            queryParameters.fromBaseTileX = tile.tileX;
            queryParameters.fromBaseTileY = tile.tileY;
            queryParameters.toBaseTileX = queryParameters.fromBaseTileX;
            queryParameters.toBaseTileY = queryParameters.fromBaseTileY;
            queryParameters.useTileBitmask = false;
        }
    }
    
    static void calculateBlocks(final QueryParameters queryParameters, final SubFileParameter subFileParameter) {
        queryParameters.fromBlockX = Math.max(queryParameters.fromBaseTileX - subFileParameter.boundaryTileLeft, 0L);
        queryParameters.fromBlockY = Math.max(queryParameters.fromBaseTileY - subFileParameter.boundaryTileTop, 0L);
        queryParameters.toBlockX = Math.min(queryParameters.toBaseTileX - subFileParameter.boundaryTileLeft, subFileParameter.blocksWidth - 1L);
        queryParameters.toBlockY = Math.min(queryParameters.toBaseTileY - subFileParameter.boundaryTileTop, subFileParameter.blocksHeight - 1L);
    }
    
    static int calculateTileBitmask(final Tile tile, int n) {
        if (n == 1) {
            n = getFirstLevelTileBitmask(tile);
        }
        else {
            final long n2 = tile.tileX >>> n - 2;
            final long n3 = tile.tileY >>> n - 2;
            final long n4 = n2 >>> 1;
            final long n5 = n3 >>> 1;
            if (n4 % 2L == 0L && n5 % 2L == 0L) {
                n = getSecondLevelTileBitmaskUpperLeft(n2, n3);
            }
            else if (n4 % 2L == 1L && n5 % 2L == 0L) {
                n = getSecondLevelTileBitmaskUpperRight(n2, n3);
            }
            else if (n4 % 2L == 0L && n5 % 2L == 1L) {
                n = getSecondLevelTileBitmaskLowerLeft(n2, n3);
            }
            else {
                n = getSecondLevelTileBitmaskLowerRight(n2, n3);
            }
        }
        return n;
    }
    
    private static int getFirstLevelTileBitmask(final Tile tile) {
        int n;
        if (tile.tileX % 2L == 0L && tile.tileY % 2L == 0L) {
            n = 52224;
        }
        else if (tile.tileX % 2L == 1L && tile.tileY % 2L == 0L) {
            n = 13056;
        }
        else if (tile.tileX % 2L == 0L && tile.tileY % 2L == 1L) {
            n = 204;
        }
        else {
            n = 51;
        }
        return n;
    }
    
    private static int getSecondLevelTileBitmaskLowerLeft(final long n, final long n2) {
        int n3;
        if (n % 2L == 0L && n2 % 2L == 0L) {
            n3 = 128;
        }
        else if (n % 2L == 1L && n2 % 2L == 0L) {
            n3 = 64;
        }
        else if (n % 2L == 0L && n2 % 2L == 1L) {
            n3 = 8;
        }
        else {
            n3 = 4;
        }
        return n3;
    }
    
    private static int getSecondLevelTileBitmaskLowerRight(final long n, final long n2) {
        int n3;
        if (n % 2L == 0L && n2 % 2L == 0L) {
            n3 = 32;
        }
        else if (n % 2L == 1L && n2 % 2L == 0L) {
            n3 = 16;
        }
        else if (n % 2L == 0L && n2 % 2L == 1L) {
            n3 = 2;
        }
        else {
            n3 = 1;
        }
        return n3;
    }
    
    private static int getSecondLevelTileBitmaskUpperLeft(final long n, final long n2) {
        int n3;
        if (n % 2L == 0L && n2 % 2L == 0L) {
            n3 = 32768;
        }
        else if (n % 2L == 1L && n2 % 2L == 0L) {
            n3 = 16384;
        }
        else if (n % 2L == 0L && n2 % 2L == 1L) {
            n3 = 2048;
        }
        else {
            n3 = 1024;
        }
        return n3;
    }
    
    private static int getSecondLevelTileBitmaskUpperRight(final long n, final long n2) {
        int n3;
        if (n % 2L == 0L && n2 % 2L == 0L) {
            n3 = 8192;
        }
        else if (n % 2L == 1L && n2 % 2L == 0L) {
            n3 = 4096;
        }
        else if (n % 2L == 0L && n2 % 2L == 1L) {
            n3 = 512;
        }
        else {
            n3 = 256;
        }
        return n3;
    }
}

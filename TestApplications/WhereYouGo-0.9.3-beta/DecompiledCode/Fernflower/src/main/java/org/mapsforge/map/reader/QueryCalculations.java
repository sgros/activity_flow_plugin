package org.mapsforge.map.reader;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.reader.header.SubFileParameter;

final class QueryCalculations {
   private QueryCalculations() {
      throw new IllegalStateException();
   }

   static void calculateBaseTiles(QueryParameters var0, Tile var1, SubFileParameter var2) {
      int var3;
      if (var1.zoomLevel < var2.baseZoomLevel) {
         var3 = var2.baseZoomLevel - var1.zoomLevel;
         var0.fromBaseTileX = var1.tileX << var3;
         var0.fromBaseTileY = var1.tileY << var3;
         var0.toBaseTileX = var0.fromBaseTileX + (long)(1 << var3) - 1L;
         var0.toBaseTileY = var0.fromBaseTileY + (long)(1 << var3) - 1L;
         var0.useTileBitmask = false;
      } else if (var1.zoomLevel > var2.baseZoomLevel) {
         var3 = var1.zoomLevel - var2.baseZoomLevel;
         var0.fromBaseTileX = var1.tileX >>> var3;
         var0.fromBaseTileY = var1.tileY >>> var3;
         var0.toBaseTileX = var0.fromBaseTileX;
         var0.toBaseTileY = var0.fromBaseTileY;
         var0.useTileBitmask = true;
         var0.queryTileBitmask = calculateTileBitmask(var1, var3);
      } else {
         var0.fromBaseTileX = var1.tileX;
         var0.fromBaseTileY = var1.tileY;
         var0.toBaseTileX = var0.fromBaseTileX;
         var0.toBaseTileY = var0.fromBaseTileY;
         var0.useTileBitmask = false;
      }

   }

   static void calculateBlocks(QueryParameters var0, SubFileParameter var1) {
      var0.fromBlockX = Math.max(var0.fromBaseTileX - var1.boundaryTileLeft, 0L);
      var0.fromBlockY = Math.max(var0.fromBaseTileY - var1.boundaryTileTop, 0L);
      var0.toBlockX = Math.min(var0.toBaseTileX - var1.boundaryTileLeft, var1.blocksWidth - 1L);
      var0.toBlockY = Math.min(var0.toBaseTileY - var1.boundaryTileTop, var1.blocksHeight - 1L);
   }

   static int calculateTileBitmask(Tile var0, int var1) {
      if (var1 == 1) {
         var1 = getFirstLevelTileBitmask(var0);
      } else {
         long var2 = var0.tileX >>> var1 - 2;
         long var4 = var0.tileY >>> var1 - 2;
         long var6 = var2 >>> 1;
         long var8 = var4 >>> 1;
         if (var6 % 2L == 0L && var8 % 2L == 0L) {
            var1 = getSecondLevelTileBitmaskUpperLeft(var2, var4);
         } else if (var6 % 2L == 1L && var8 % 2L == 0L) {
            var1 = getSecondLevelTileBitmaskUpperRight(var2, var4);
         } else if (var6 % 2L == 0L && var8 % 2L == 1L) {
            var1 = getSecondLevelTileBitmaskLowerLeft(var2, var4);
         } else {
            var1 = getSecondLevelTileBitmaskLowerRight(var2, var4);
         }
      }

      return var1;
   }

   private static int getFirstLevelTileBitmask(Tile var0) {
      char var1;
      if (var0.tileX % 2L == 0L && var0.tileY % 2L == 0L) {
         var1 = '찀';
      } else if (var0.tileX % 2L == 1L && var0.tileY % 2L == 0L) {
         var1 = 13056;
      } else if (var0.tileX % 2L == 0L && var0.tileY % 2L == 1L) {
         var1 = 204;
      } else {
         var1 = '3';
      }

      return var1;
   }

   private static int getSecondLevelTileBitmaskLowerLeft(long var0, long var2) {
      short var4;
      if (var0 % 2L == 0L && var2 % 2L == 0L) {
         var4 = 128;
      } else if (var0 % 2L == 1L && var2 % 2L == 0L) {
         var4 = 64;
      } else if (var0 % 2L == 0L && var2 % 2L == 1L) {
         var4 = 8;
      } else {
         var4 = 4;
      }

      return var4;
   }

   private static int getSecondLevelTileBitmaskLowerRight(long var0, long var2) {
      byte var4;
      if (var0 % 2L == 0L && var2 % 2L == 0L) {
         var4 = 32;
      } else if (var0 % 2L == 1L && var2 % 2L == 0L) {
         var4 = 16;
      } else if (var0 % 2L == 0L && var2 % 2L == 1L) {
         var4 = 2;
      } else {
         var4 = 1;
      }

      return var4;
   }

   private static int getSecondLevelTileBitmaskUpperLeft(long var0, long var2) {
      char var4;
      if (var0 % 2L == 0L && var2 % 2L == 0L) {
         var4 = '耀';
      } else if (var0 % 2L == 1L && var2 % 2L == 0L) {
         var4 = 16384;
      } else if (var0 % 2L == 0L && var2 % 2L == 1L) {
         var4 = 2048;
      } else {
         var4 = 1024;
      }

      return var4;
   }

   private static int getSecondLevelTileBitmaskUpperRight(long var0, long var2) {
      short var4;
      if (var0 % 2L == 0L && var2 % 2L == 0L) {
         var4 = 8192;
      } else if (var0 % 2L == 1L && var2 % 2L == 0L) {
         var4 = 4096;
      } else if (var0 % 2L == 0L && var2 % 2L == 1L) {
         var4 = 512;
      } else {
         var4 = 256;
      }

      return var4;
   }
}

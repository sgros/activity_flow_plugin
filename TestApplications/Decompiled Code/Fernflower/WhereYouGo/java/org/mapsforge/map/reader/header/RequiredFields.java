package org.mapsforge.map.reader.header;

import java.io.IOException;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.CoordinatesUtil;
import org.mapsforge.core.model.Tag;
import org.mapsforge.map.reader.ReadBuffer;

final class RequiredFields {
   private static final String BINARY_OSM_MAGIC_BYTE = "mapsforge binary OSM";
   private static final int HEADER_SIZE_MAX = 1000000;
   private static final int HEADER_SIZE_MIN = 70;
   private static final String MERCATOR = "Mercator";
   private static final int SUPPORTED_FILE_VERSION = 3;

   private RequiredFields() {
      throw new IllegalStateException();
   }

   static FileOpenResult readBoundingBox(ReadBuffer var0, MapFileInfoBuilder var1) {
      double var2 = CoordinatesUtil.microdegreesToDegrees(var0.readInt());
      double var4 = CoordinatesUtil.microdegreesToDegrees(var0.readInt());
      double var6 = CoordinatesUtil.microdegreesToDegrees(var0.readInt());
      double var8 = CoordinatesUtil.microdegreesToDegrees(var0.readInt());

      FileOpenResult var11;
      try {
         BoundingBox var12 = new BoundingBox(var2, var4, var6, var8);
         var1.boundingBox = var12;
      } catch (IllegalArgumentException var10) {
         var11 = new FileOpenResult(var10.getMessage());
         return var11;
      }

      var11 = FileOpenResult.SUCCESS;
      return var11;
   }

   static FileOpenResult readFileSize(ReadBuffer var0, long var1, MapFileInfoBuilder var3) {
      long var4 = var0.readLong();
      FileOpenResult var6;
      if (var4 != var1) {
         var6 = new FileOpenResult("invalid file size: " + var4);
      } else {
         var3.fileSize = var1;
         var6 = FileOpenResult.SUCCESS;
      }

      return var6;
   }

   static FileOpenResult readFileVersion(ReadBuffer var0, MapFileInfoBuilder var1) {
      int var2 = var0.readInt();
      FileOpenResult var3;
      if (var2 != 3) {
         var3 = new FileOpenResult("unsupported file version: " + var2);
      } else {
         var1.fileVersion = var2;
         var3 = FileOpenResult.SUCCESS;
      }

      return var3;
   }

   static FileOpenResult readMagicByte(ReadBuffer var0) throws IOException {
      int var1 = "mapsforge binary OSM".length();
      FileOpenResult var2;
      if (!var0.readFromFile(var1 + 4)) {
         var2 = new FileOpenResult("reading magic byte has failed");
      } else {
         String var3 = var0.readUTF8EncodedString(var1);
         if (!"mapsforge binary OSM".equals(var3)) {
            var2 = new FileOpenResult("invalid magic byte: " + var3);
         } else {
            var2 = FileOpenResult.SUCCESS;
         }
      }

      return var2;
   }

   static FileOpenResult readMapDate(ReadBuffer var0, MapFileInfoBuilder var1) {
      long var2 = var0.readLong();
      FileOpenResult var4;
      if (var2 < 1200000000000L) {
         var4 = new FileOpenResult("invalid map date: " + var2);
      } else {
         var1.mapDate = var2;
         var4 = FileOpenResult.SUCCESS;
      }

      return var4;
   }

   static FileOpenResult readPoiTags(ReadBuffer var0, MapFileInfoBuilder var1) {
      int var2 = var0.readShort();
      FileOpenResult var6;
      if (var2 < 0) {
         var6 = new FileOpenResult("invalid number of POI tags: " + var2);
      } else {
         Tag[] var3 = new Tag[var2];
         int var4 = 0;

         while(true) {
            if (var4 >= var2) {
               var1.poiTags = var3;
               var6 = FileOpenResult.SUCCESS;
               break;
            }

            String var5 = var0.readUTF8EncodedString();
            if (var5 == null) {
               var6 = new FileOpenResult("POI tag must not be null: " + var4);
               break;
            }

            var3[var4] = new Tag(var5);
            ++var4;
         }
      }

      return var6;
   }

   static FileOpenResult readProjectionName(ReadBuffer var0, MapFileInfoBuilder var1) {
      String var2 = var0.readUTF8EncodedString();
      FileOpenResult var3;
      if (!"Mercator".equals(var2)) {
         var3 = new FileOpenResult("unsupported projection: " + var2);
      } else {
         var1.projectionName = var2;
         var3 = FileOpenResult.SUCCESS;
      }

      return var3;
   }

   static FileOpenResult readRemainingHeader(ReadBuffer var0) throws IOException {
      int var1 = var0.readInt();
      FileOpenResult var2;
      if (var1 >= 70 && var1 <= 1000000) {
         if (!var0.readFromFile(var1)) {
            var2 = new FileOpenResult("reading header data has failed: " + var1);
         } else {
            var2 = FileOpenResult.SUCCESS;
         }
      } else {
         var2 = new FileOpenResult("invalid remaining header size: " + var1);
      }

      return var2;
   }

   static FileOpenResult readTilePixelSize(ReadBuffer var0, MapFileInfoBuilder var1) {
      int var2 = var0.readShort();
      FileOpenResult var3;
      if (var2 != 256) {
         var3 = new FileOpenResult("unsupported tile pixel size: " + var2);
      } else {
         var1.tilePixelSize = var2;
         var3 = FileOpenResult.SUCCESS;
      }

      return var3;
   }

   static FileOpenResult readWayTags(ReadBuffer var0, MapFileInfoBuilder var1) {
      int var2 = var0.readShort();
      FileOpenResult var6;
      if (var2 < 0) {
         var6 = new FileOpenResult("invalid number of way tags: " + var2);
      } else {
         Tag[] var3 = new Tag[var2];
         int var4 = 0;

         while(true) {
            if (var4 >= var2) {
               var1.wayTags = var3;
               var6 = FileOpenResult.SUCCESS;
               break;
            }

            String var5 = var0.readUTF8EncodedString();
            if (var5 == null) {
               var6 = new FileOpenResult("way tag must not be null: " + var4);
               break;
            }

            var3[var4] = new Tag(var5);
            ++var4;
         }
      }

      return var6;
   }
}

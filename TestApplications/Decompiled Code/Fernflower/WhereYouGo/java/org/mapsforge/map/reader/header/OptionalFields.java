package org.mapsforge.map.reader.header;

import org.mapsforge.core.model.CoordinatesUtil;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.map.reader.ReadBuffer;

final class OptionalFields {
   private static final int HEADER_BITMASK_COMMENT = 8;
   private static final int HEADER_BITMASK_CREATED_BY = 4;
   private static final int HEADER_BITMASK_DEBUG = 128;
   private static final int HEADER_BITMASK_LANGUAGE_PREFERENCE = 16;
   private static final int HEADER_BITMASK_START_POSITION = 64;
   private static final int HEADER_BITMASK_START_ZOOM_LEVEL = 32;
   private static final int LANGUAGE_PREFERENCE_LENGTH = 2;
   private static final int START_ZOOM_LEVEL_MAX = 22;
   String comment;
   String createdBy;
   final boolean hasComment;
   final boolean hasCreatedBy;
   final boolean hasLanguagePreference;
   final boolean hasStartPosition;
   final boolean hasStartZoomLevel;
   final boolean isDebugFile;
   String languagePreference;
   GeoPoint startPosition;
   Byte startZoomLevel;

   private OptionalFields(byte var1) {
      boolean var2 = true;
      super();
      boolean var3;
      if ((var1 & 128) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.isDebugFile = var3;
      if ((var1 & 64) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.hasStartPosition = var3;
      if ((var1 & 32) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.hasStartZoomLevel = var3;
      if ((var1 & 16) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.hasLanguagePreference = var3;
      if ((var1 & 8) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.hasComment = var3;
      if ((var1 & 4) != 0) {
         var3 = var2;
      } else {
         var3 = false;
      }

      this.hasCreatedBy = var3;
   }

   private FileOpenResult readLanguagePreference(ReadBuffer var1) {
      FileOpenResult var3;
      if (this.hasLanguagePreference) {
         String var2 = var1.readUTF8EncodedString();
         if (var2.length() != 2) {
            var3 = new FileOpenResult("invalid language preference: " + var2);
            return var3;
         }

         this.languagePreference = var2;
      }

      var3 = FileOpenResult.SUCCESS;
      return var3;
   }

   private FileOpenResult readMapStartPosition(ReadBuffer var1) {
      FileOpenResult var7;
      if (this.hasStartPosition) {
         double var2 = CoordinatesUtil.microdegreesToDegrees(var1.readInt());
         double var4 = CoordinatesUtil.microdegreesToDegrees(var1.readInt());

         try {
            GeoPoint var8 = new GeoPoint(var2, var4);
            this.startPosition = var8;
         } catch (IllegalArgumentException var6) {
            var7 = new FileOpenResult(var6.getMessage());
            return var7;
         }
      }

      var7 = FileOpenResult.SUCCESS;
      return var7;
   }

   private FileOpenResult readMapStartZoomLevel(ReadBuffer var1) {
      FileOpenResult var3;
      if (this.hasStartZoomLevel) {
         byte var2 = var1.readByte();
         if (var2 < 0 || var2 > 22) {
            var3 = new FileOpenResult("invalid map start zoom level: " + var2);
            return var3;
         }

         this.startZoomLevel = var2;
      }

      var3 = FileOpenResult.SUCCESS;
      return var3;
   }

   private FileOpenResult readOptionalFields(ReadBuffer var1) {
      FileOpenResult var2 = this.readMapStartPosition(var1);
      FileOpenResult var3;
      if (!var2.isSuccess()) {
         var3 = var2;
      } else {
         var2 = this.readMapStartZoomLevel(var1);
         if (!var2.isSuccess()) {
            var3 = var2;
         } else {
            var2 = this.readLanguagePreference(var1);
            if (!var2.isSuccess()) {
               var3 = var2;
            } else {
               if (this.hasComment) {
                  this.comment = var1.readUTF8EncodedString();
               }

               if (this.hasCreatedBy) {
                  this.createdBy = var1.readUTF8EncodedString();
               }

               var3 = FileOpenResult.SUCCESS;
            }
         }
      }

      return var3;
   }

   static FileOpenResult readOptionalFields(ReadBuffer var0, MapFileInfoBuilder var1) {
      OptionalFields var2 = new OptionalFields(var0.readByte());
      var1.optionalFields = var2;
      FileOpenResult var3 = var2.readOptionalFields(var0);
      if (var3.isSuccess()) {
         var3 = FileOpenResult.SUCCESS;
      }

      return var3;
   }
}

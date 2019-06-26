package org.mapsforge.map.reader.header;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Tag;

public class MapFileInfo {
   public final BoundingBox boundingBox;
   public final String comment;
   public final String createdBy;
   public final boolean debugFile;
   public final long fileSize;
   public final int fileVersion;
   public final String languagePreference;
   public final long mapDate;
   public final byte numberOfSubFiles;
   public final Tag[] poiTags;
   public final String projectionName;
   public final GeoPoint startPosition;
   public final Byte startZoomLevel;
   public final int tilePixelSize;
   public final Tag[] wayTags;

   MapFileInfo(MapFileInfoBuilder var1) {
      this.comment = var1.optionalFields.comment;
      this.createdBy = var1.optionalFields.createdBy;
      this.debugFile = var1.optionalFields.isDebugFile;
      this.fileSize = var1.fileSize;
      this.fileVersion = var1.fileVersion;
      this.languagePreference = var1.optionalFields.languagePreference;
      this.boundingBox = var1.boundingBox;
      this.mapDate = var1.mapDate;
      this.numberOfSubFiles = (byte)var1.numberOfSubFiles;
      this.poiTags = var1.poiTags;
      this.projectionName = var1.projectionName;
      this.startPosition = var1.optionalFields.startPosition;
      this.startZoomLevel = var1.optionalFields.startZoomLevel;
      this.tilePixelSize = var1.tilePixelSize;
      this.wayTags = var1.wayTags;
   }
}

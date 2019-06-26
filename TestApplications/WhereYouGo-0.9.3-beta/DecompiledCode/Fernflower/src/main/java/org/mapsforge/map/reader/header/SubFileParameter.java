package org.mapsforge.map.reader.header;

import org.mapsforge.core.util.MercatorProjection;

public class SubFileParameter {
   public static final byte BYTES_PER_INDEX_ENTRY = 5;
   public final byte baseZoomLevel;
   public final long blocksHeight;
   public final long blocksWidth;
   public final long boundaryTileBottom;
   public final long boundaryTileLeft;
   public final long boundaryTileRight;
   public final long boundaryTileTop;
   private final int hashCodeValue;
   public final long indexEndAddress;
   public final long indexStartAddress;
   public final long numberOfBlocks;
   public final long startAddress;
   public final long subFileSize;
   public final byte zoomLevelMax;
   public final byte zoomLevelMin;

   SubFileParameter(SubFileParameterBuilder var1) {
      this.startAddress = var1.startAddress;
      this.indexStartAddress = var1.indexStartAddress;
      this.subFileSize = var1.subFileSize;
      this.baseZoomLevel = (byte)var1.baseZoomLevel;
      this.zoomLevelMin = (byte)var1.zoomLevelMin;
      this.zoomLevelMax = (byte)var1.zoomLevelMax;
      this.hashCodeValue = this.calculateHashCode();
      this.boundaryTileBottom = MercatorProjection.latitudeToTileY(var1.boundingBox.minLatitude, this.baseZoomLevel);
      this.boundaryTileLeft = MercatorProjection.longitudeToTileX(var1.boundingBox.minLongitude, this.baseZoomLevel);
      this.boundaryTileTop = MercatorProjection.latitudeToTileY(var1.boundingBox.maxLatitude, this.baseZoomLevel);
      this.boundaryTileRight = MercatorProjection.longitudeToTileX(var1.boundingBox.maxLongitude, this.baseZoomLevel);
      this.blocksWidth = this.boundaryTileRight - this.boundaryTileLeft + 1L;
      this.blocksHeight = this.boundaryTileBottom - this.boundaryTileTop + 1L;
      this.numberOfBlocks = this.blocksWidth * this.blocksHeight;
      this.indexEndAddress = this.indexStartAddress + this.numberOfBlocks * 5L;
   }

   private int calculateHashCode() {
      return (((int)(this.startAddress ^ this.startAddress >>> 32) + 217) * 31 + (int)(this.subFileSize ^ this.subFileSize >>> 32)) * 31 + this.baseZoomLevel;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (!(var1 instanceof SubFileParameter)) {
            var2 = false;
         } else {
            SubFileParameter var3 = (SubFileParameter)var1;
            if (this.startAddress != var3.startAddress) {
               var2 = false;
            } else if (this.subFileSize != var3.subFileSize) {
               var2 = false;
            } else if (this.baseZoomLevel != var3.baseZoomLevel) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public int hashCode() {
      return this.hashCodeValue;
   }
}

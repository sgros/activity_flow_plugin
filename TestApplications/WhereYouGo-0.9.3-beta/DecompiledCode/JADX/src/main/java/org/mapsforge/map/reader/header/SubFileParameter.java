package org.mapsforge.map.reader.header;

import org.mapsforge.core.util.MercatorProjection;

public class SubFileParameter {
    public static final byte BYTES_PER_INDEX_ENTRY = (byte) 5;
    public final byte baseZoomLevel;
    public final long blocksHeight;
    public final long blocksWidth;
    public final long boundaryTileBottom;
    public final long boundaryTileLeft;
    public final long boundaryTileRight;
    public final long boundaryTileTop;
    private final int hashCodeValue = calculateHashCode();
    public final long indexEndAddress;
    public final long indexStartAddress;
    public final long numberOfBlocks;
    public final long startAddress;
    public final long subFileSize;
    public final byte zoomLevelMax;
    public final byte zoomLevelMin;

    SubFileParameter(SubFileParameterBuilder subFileParameterBuilder) {
        this.startAddress = subFileParameterBuilder.startAddress;
        this.indexStartAddress = subFileParameterBuilder.indexStartAddress;
        this.subFileSize = subFileParameterBuilder.subFileSize;
        this.baseZoomLevel = subFileParameterBuilder.baseZoomLevel;
        this.zoomLevelMin = subFileParameterBuilder.zoomLevelMin;
        this.zoomLevelMax = subFileParameterBuilder.zoomLevelMax;
        this.boundaryTileBottom = MercatorProjection.latitudeToTileY(subFileParameterBuilder.boundingBox.minLatitude, this.baseZoomLevel);
        this.boundaryTileLeft = MercatorProjection.longitudeToTileX(subFileParameterBuilder.boundingBox.minLongitude, this.baseZoomLevel);
        this.boundaryTileTop = MercatorProjection.latitudeToTileY(subFileParameterBuilder.boundingBox.maxLatitude, this.baseZoomLevel);
        this.boundaryTileRight = MercatorProjection.longitudeToTileX(subFileParameterBuilder.boundingBox.maxLongitude, this.baseZoomLevel);
        this.blocksWidth = (this.boundaryTileRight - this.boundaryTileLeft) + 1;
        this.blocksHeight = (this.boundaryTileBottom - this.boundaryTileTop) + 1;
        this.numberOfBlocks = this.blocksWidth * this.blocksHeight;
        this.indexEndAddress = this.indexStartAddress + (this.numberOfBlocks * 5);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SubFileParameter)) {
            return false;
        }
        SubFileParameter other = (SubFileParameter) obj;
        if (this.startAddress != other.startAddress) {
            return false;
        }
        if (this.subFileSize != other.subFileSize) {
            return false;
        }
        if (this.baseZoomLevel != other.baseZoomLevel) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.hashCodeValue;
    }

    private int calculateHashCode() {
        return ((((((int) (this.startAddress ^ (this.startAddress >>> 32))) + 217) * 31) + ((int) (this.subFileSize ^ (this.subFileSize >>> 32)))) * 31) + this.baseZoomLevel;
    }
}

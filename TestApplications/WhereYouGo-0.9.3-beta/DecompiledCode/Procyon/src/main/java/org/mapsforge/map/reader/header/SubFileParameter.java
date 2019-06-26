// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader.header;

import org.mapsforge.core.util.MercatorProjection;

public class SubFileParameter
{
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
    
    SubFileParameter(final SubFileParameterBuilder subFileParameterBuilder) {
        this.startAddress = subFileParameterBuilder.startAddress;
        this.indexStartAddress = subFileParameterBuilder.indexStartAddress;
        this.subFileSize = subFileParameterBuilder.subFileSize;
        this.baseZoomLevel = subFileParameterBuilder.baseZoomLevel;
        this.zoomLevelMin = subFileParameterBuilder.zoomLevelMin;
        this.zoomLevelMax = subFileParameterBuilder.zoomLevelMax;
        this.hashCodeValue = this.calculateHashCode();
        this.boundaryTileBottom = MercatorProjection.latitudeToTileY(subFileParameterBuilder.boundingBox.minLatitude, this.baseZoomLevel);
        this.boundaryTileLeft = MercatorProjection.longitudeToTileX(subFileParameterBuilder.boundingBox.minLongitude, this.baseZoomLevel);
        this.boundaryTileTop = MercatorProjection.latitudeToTileY(subFileParameterBuilder.boundingBox.maxLatitude, this.baseZoomLevel);
        this.boundaryTileRight = MercatorProjection.longitudeToTileX(subFileParameterBuilder.boundingBox.maxLongitude, this.baseZoomLevel);
        this.blocksWidth = this.boundaryTileRight - this.boundaryTileLeft + 1L;
        this.blocksHeight = this.boundaryTileBottom - this.boundaryTileTop + 1L;
        this.numberOfBlocks = this.blocksWidth * this.blocksHeight;
        this.indexEndAddress = this.indexStartAddress + this.numberOfBlocks * 5L;
    }
    
    private int calculateHashCode() {
        return (((int)(this.startAddress ^ this.startAddress >>> 32) + 217) * 31 + (int)(this.subFileSize ^ this.subFileSize >>> 32)) * 31 + this.baseZoomLevel;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (!(o instanceof SubFileParameter)) {
                b = false;
            }
            else {
                final SubFileParameter subFileParameter = (SubFileParameter)o;
                if (this.startAddress != subFileParameter.startAddress) {
                    b = false;
                }
                else if (this.subFileSize != subFileParameter.subFileSize) {
                    b = false;
                }
                else if (this.baseZoomLevel != subFileParameter.baseZoomLevel) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    @Override
    public int hashCode() {
        return this.hashCodeValue;
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader.header;

import java.io.IOException;
import org.mapsforge.map.reader.ReadBuffer;

public class MapFileHeader
{
    private static final int BASE_ZOOM_LEVEL_MAX = 20;
    private static final int HEADER_SIZE_MIN = 70;
    private static final byte SIGNATURE_LENGTH_INDEX = 16;
    private static final char SPACE = ' ';
    private MapFileInfo mapFileInfo;
    private SubFileParameter[] subFileParameters;
    private byte zoomLevelMaximum;
    private byte zoomLevelMinimum;
    
    private FileOpenResult readSubFileParameters(final ReadBuffer readBuffer, final long n, final MapFileInfoBuilder mapFileInfoBuilder) {
        final byte byte1 = readBuffer.readByte();
        FileOpenResult success;
        if (byte1 < 1) {
            success = new FileOpenResult("invalid number of sub-files: " + byte1);
        }
        else {
            mapFileInfoBuilder.numberOfSubFiles = byte1;
            final SubFileParameter[] array = new SubFileParameter[byte1];
            this.zoomLevelMinimum = 127;
            this.zoomLevelMaximum = -128;
            for (byte b = 0; b < byte1; ++b) {
                final SubFileParameterBuilder subFileParameterBuilder = new SubFileParameterBuilder();
                final byte byte2 = readBuffer.readByte();
                if (byte2 < 0 || byte2 > 20) {
                    success = new FileOpenResult("invalid base zooom level: " + byte2);
                    return success;
                }
                subFileParameterBuilder.baseZoomLevel = byte2;
                final byte byte3 = readBuffer.readByte();
                if (byte3 < 0 || byte3 > 22) {
                    success = new FileOpenResult("invalid minimum zoom level: " + byte3);
                    return success;
                }
                subFileParameterBuilder.zoomLevelMin = byte3;
                final byte byte4 = readBuffer.readByte();
                if (byte4 < 0 || byte4 > 22) {
                    success = new FileOpenResult("invalid maximum zoom level: " + byte4);
                    return success;
                }
                subFileParameterBuilder.zoomLevelMax = byte4;
                if (byte3 > byte4) {
                    success = new FileOpenResult("invalid zoom level range: " + byte3 + ' ' + byte4);
                    return success;
                }
                final long long1 = readBuffer.readLong();
                if (long1 < 70L || long1 >= n) {
                    success = new FileOpenResult("invalid start address: " + long1);
                    return success;
                }
                subFileParameterBuilder.startAddress = long1;
                long indexStartAddress = long1;
                if (mapFileInfoBuilder.optionalFields.isDebugFile) {
                    indexStartAddress = long1 + 16L;
                }
                subFileParameterBuilder.indexStartAddress = indexStartAddress;
                final long long2 = readBuffer.readLong();
                if (long2 < 1L) {
                    success = new FileOpenResult("invalid sub-file size: " + long2);
                    return success;
                }
                subFileParameterBuilder.subFileSize = long2;
                subFileParameterBuilder.boundingBox = mapFileInfoBuilder.boundingBox;
                this.updateZoomLevelInformation(array[b] = subFileParameterBuilder.build());
            }
            this.subFileParameters = new SubFileParameter[this.zoomLevelMaximum + 1];
            for (byte b2 = 0; b2 < byte1; ++b2) {
                final SubFileParameter subFileParameter = array[b2];
                for (byte zoomLevelMin = subFileParameter.zoomLevelMin; zoomLevelMin <= subFileParameter.zoomLevelMax; ++zoomLevelMin) {
                    this.subFileParameters[zoomLevelMin] = subFileParameter;
                }
            }
            success = FileOpenResult.SUCCESS;
        }
        return success;
    }
    
    private void updateZoomLevelInformation(final SubFileParameter subFileParameter) {
        if (this.zoomLevelMinimum > subFileParameter.zoomLevelMin) {
            this.zoomLevelMinimum = subFileParameter.zoomLevelMin;
        }
        if (this.zoomLevelMaximum < subFileParameter.zoomLevelMax) {
            this.zoomLevelMaximum = subFileParameter.zoomLevelMax;
        }
    }
    
    public MapFileInfo getMapFileInfo() {
        return this.mapFileInfo;
    }
    
    public byte getQueryZoomLevel(final byte b) {
        byte b2;
        if (b > this.zoomLevelMaximum) {
            b2 = this.zoomLevelMaximum;
        }
        else if ((b2 = b) < this.zoomLevelMinimum) {
            b2 = this.zoomLevelMinimum;
        }
        return b2;
    }
    
    public SubFileParameter getSubFileParameter(final int n) {
        return this.subFileParameters[n];
    }
    
    public FileOpenResult readHeader(final ReadBuffer readBuffer, final long n) throws IOException {
        final FileOpenResult magicByte = RequiredFields.readMagicByte(readBuffer);
        FileOpenResult fileOpenResult;
        if (!magicByte.isSuccess()) {
            fileOpenResult = magicByte;
        }
        else {
            final FileOpenResult remainingHeader = RequiredFields.readRemainingHeader(readBuffer);
            if (!remainingHeader.isSuccess()) {
                fileOpenResult = remainingHeader;
            }
            else {
                final MapFileInfoBuilder mapFileInfoBuilder = new MapFileInfoBuilder();
                final FileOpenResult fileVersion = RequiredFields.readFileVersion(readBuffer, mapFileInfoBuilder);
                if (!fileVersion.isSuccess()) {
                    fileOpenResult = fileVersion;
                }
                else {
                    final FileOpenResult fileSize = RequiredFields.readFileSize(readBuffer, n, mapFileInfoBuilder);
                    if (!fileSize.isSuccess()) {
                        fileOpenResult = fileSize;
                    }
                    else {
                        final FileOpenResult mapDate = RequiredFields.readMapDate(readBuffer, mapFileInfoBuilder);
                        if (!mapDate.isSuccess()) {
                            fileOpenResult = mapDate;
                        }
                        else {
                            final FileOpenResult boundingBox = RequiredFields.readBoundingBox(readBuffer, mapFileInfoBuilder);
                            if (!boundingBox.isSuccess()) {
                                fileOpenResult = boundingBox;
                            }
                            else {
                                final FileOpenResult tilePixelSize = RequiredFields.readTilePixelSize(readBuffer, mapFileInfoBuilder);
                                if (!tilePixelSize.isSuccess()) {
                                    fileOpenResult = tilePixelSize;
                                }
                                else {
                                    final FileOpenResult projectionName = RequiredFields.readProjectionName(readBuffer, mapFileInfoBuilder);
                                    if (!projectionName.isSuccess()) {
                                        fileOpenResult = projectionName;
                                    }
                                    else {
                                        final FileOpenResult optionalFields = OptionalFields.readOptionalFields(readBuffer, mapFileInfoBuilder);
                                        if (!optionalFields.isSuccess()) {
                                            fileOpenResult = optionalFields;
                                        }
                                        else {
                                            final FileOpenResult poiTags = RequiredFields.readPoiTags(readBuffer, mapFileInfoBuilder);
                                            if (!poiTags.isSuccess()) {
                                                fileOpenResult = poiTags;
                                            }
                                            else {
                                                final FileOpenResult wayTags = RequiredFields.readWayTags(readBuffer, mapFileInfoBuilder);
                                                if (!wayTags.isSuccess()) {
                                                    fileOpenResult = wayTags;
                                                }
                                                else {
                                                    fileOpenResult = this.readSubFileParameters(readBuffer, n, mapFileInfoBuilder);
                                                    if (fileOpenResult.isSuccess()) {
                                                        this.mapFileInfo = mapFileInfoBuilder.build();
                                                        fileOpenResult = FileOpenResult.SUCCESS;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return fileOpenResult;
    }
}

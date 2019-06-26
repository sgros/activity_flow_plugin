package org.mapsforge.map.reader.header;

import java.io.IOException;
import org.mapsforge.map.reader.ReadBuffer;

public class MapFileHeader {
    private static final int BASE_ZOOM_LEVEL_MAX = 20;
    private static final int HEADER_SIZE_MIN = 70;
    private static final byte SIGNATURE_LENGTH_INDEX = (byte) 16;
    private static final char SPACE = ' ';
    private MapFileInfo mapFileInfo;
    private SubFileParameter[] subFileParameters;
    private byte zoomLevelMaximum;
    private byte zoomLevelMinimum;

    public MapFileInfo getMapFileInfo() {
        return this.mapFileInfo;
    }

    public byte getQueryZoomLevel(byte zoomLevel) {
        if (zoomLevel > this.zoomLevelMaximum) {
            return this.zoomLevelMaximum;
        }
        if (zoomLevel < this.zoomLevelMinimum) {
            return this.zoomLevelMinimum;
        }
        return zoomLevel;
    }

    public SubFileParameter getSubFileParameter(int queryZoomLevel) {
        return this.subFileParameters[queryZoomLevel];
    }

    public FileOpenResult readHeader(ReadBuffer readBuffer, long fileSize) throws IOException {
        FileOpenResult fileOpenResult = RequiredFields.readMagicByte(readBuffer);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        fileOpenResult = RequiredFields.readRemainingHeader(readBuffer);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        MapFileInfoBuilder mapFileInfoBuilder = new MapFileInfoBuilder();
        fileOpenResult = RequiredFields.readFileVersion(readBuffer, mapFileInfoBuilder);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        fileOpenResult = RequiredFields.readFileSize(readBuffer, fileSize, mapFileInfoBuilder);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        fileOpenResult = RequiredFields.readMapDate(readBuffer, mapFileInfoBuilder);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        fileOpenResult = RequiredFields.readBoundingBox(readBuffer, mapFileInfoBuilder);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        fileOpenResult = RequiredFields.readTilePixelSize(readBuffer, mapFileInfoBuilder);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        fileOpenResult = RequiredFields.readProjectionName(readBuffer, mapFileInfoBuilder);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        fileOpenResult = OptionalFields.readOptionalFields(readBuffer, mapFileInfoBuilder);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        fileOpenResult = RequiredFields.readPoiTags(readBuffer, mapFileInfoBuilder);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        fileOpenResult = RequiredFields.readWayTags(readBuffer, mapFileInfoBuilder);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        fileOpenResult = readSubFileParameters(readBuffer, fileSize, mapFileInfoBuilder);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        this.mapFileInfo = mapFileInfoBuilder.build();
        return FileOpenResult.SUCCESS;
    }

    private FileOpenResult readSubFileParameters(ReadBuffer readBuffer, long fileSize, MapFileInfoBuilder mapFileInfoBuilder) {
        byte numberOfSubFiles = readBuffer.readByte();
        if (numberOfSubFiles < (byte) 1) {
            return new FileOpenResult("invalid number of sub-files: " + numberOfSubFiles);
        }
        mapFileInfoBuilder.numberOfSubFiles = numberOfSubFiles;
        SubFileParameter[] tempSubFileParameters = new SubFileParameter[numberOfSubFiles];
        this.zoomLevelMinimum = Byte.MAX_VALUE;
        this.zoomLevelMaximum = Byte.MIN_VALUE;
        for (byte currentSubFile = (byte) 0; currentSubFile < numberOfSubFiles; currentSubFile = (byte) (currentSubFile + 1)) {
            SubFileParameterBuilder subFileParameterBuilder = new SubFileParameterBuilder();
            byte baseZoomLevel = readBuffer.readByte();
            if (baseZoomLevel < (byte) 0 || baseZoomLevel > (byte) 20) {
                return new FileOpenResult("invalid base zooom level: " + baseZoomLevel);
            }
            subFileParameterBuilder.baseZoomLevel = baseZoomLevel;
            byte zoomLevelMin = readBuffer.readByte();
            if (zoomLevelMin < (byte) 0 || zoomLevelMin > (byte) 22) {
                return new FileOpenResult("invalid minimum zoom level: " + zoomLevelMin);
            }
            subFileParameterBuilder.zoomLevelMin = zoomLevelMin;
            byte zoomLevelMax = readBuffer.readByte();
            if (zoomLevelMax < (byte) 0 || zoomLevelMax > (byte) 22) {
                return new FileOpenResult("invalid maximum zoom level: " + zoomLevelMax);
            }
            subFileParameterBuilder.zoomLevelMax = zoomLevelMax;
            if (zoomLevelMin > zoomLevelMax) {
                return new FileOpenResult("invalid zoom level range: " + zoomLevelMin + SPACE + zoomLevelMax);
            }
            long startAddress = readBuffer.readLong();
            if (startAddress < 70 || startAddress >= fileSize) {
                return new FileOpenResult("invalid start address: " + startAddress);
            }
            subFileParameterBuilder.startAddress = startAddress;
            long indexStartAddress = startAddress;
            if (mapFileInfoBuilder.optionalFields.isDebugFile) {
                indexStartAddress += 16;
            }
            subFileParameterBuilder.indexStartAddress = indexStartAddress;
            long subFileSize = readBuffer.readLong();
            if (subFileSize < 1) {
                return new FileOpenResult("invalid sub-file size: " + subFileSize);
            }
            subFileParameterBuilder.subFileSize = subFileSize;
            subFileParameterBuilder.boundingBox = mapFileInfoBuilder.boundingBox;
            tempSubFileParameters[currentSubFile] = subFileParameterBuilder.build();
            updateZoomLevelInformation(tempSubFileParameters[currentSubFile]);
        }
        this.subFileParameters = new SubFileParameter[(this.zoomLevelMaximum + 1)];
        for (byte currentMapFile = (byte) 0; currentMapFile < numberOfSubFiles; currentMapFile++) {
            SubFileParameter subFileParameter = tempSubFileParameters[currentMapFile];
            for (byte zoomLevel = subFileParameter.zoomLevelMin; zoomLevel <= subFileParameter.zoomLevelMax; zoomLevel = (byte) (zoomLevel + 1)) {
                this.subFileParameters[zoomLevel] = subFileParameter;
            }
        }
        return FileOpenResult.SUCCESS;
    }

    private void updateZoomLevelInformation(SubFileParameter subFileParameter) {
        if (this.zoomLevelMinimum > subFileParameter.zoomLevelMin) {
            this.zoomLevelMinimum = subFileParameter.zoomLevelMin;
        }
        if (this.zoomLevelMaximum < subFileParameter.zoomLevelMax) {
            this.zoomLevelMaximum = subFileParameter.zoomLevelMax;
        }
    }
}

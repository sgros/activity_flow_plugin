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

    static FileOpenResult readBoundingBox(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        try {
            mapFileInfoBuilder.boundingBox = new BoundingBox(CoordinatesUtil.microdegreesToDegrees(readBuffer.readInt()), CoordinatesUtil.microdegreesToDegrees(readBuffer.readInt()), CoordinatesUtil.microdegreesToDegrees(readBuffer.readInt()), CoordinatesUtil.microdegreesToDegrees(readBuffer.readInt()));
            return FileOpenResult.SUCCESS;
        } catch (IllegalArgumentException e) {
            return new FileOpenResult(e.getMessage());
        }
    }

    static FileOpenResult readFileSize(ReadBuffer readBuffer, long fileSize, MapFileInfoBuilder mapFileInfoBuilder) {
        long headerFileSize = readBuffer.readLong();
        if (headerFileSize != fileSize) {
            return new FileOpenResult("invalid file size: " + headerFileSize);
        }
        mapFileInfoBuilder.fileSize = fileSize;
        return FileOpenResult.SUCCESS;
    }

    static FileOpenResult readFileVersion(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        int fileVersion = readBuffer.readInt();
        if (fileVersion != 3) {
            return new FileOpenResult("unsupported file version: " + fileVersion);
        }
        mapFileInfoBuilder.fileVersion = fileVersion;
        return FileOpenResult.SUCCESS;
    }

    static FileOpenResult readMagicByte(ReadBuffer readBuffer) throws IOException {
        int magicByteLength = BINARY_OSM_MAGIC_BYTE.length();
        if (!readBuffer.readFromFile(magicByteLength + 4)) {
            return new FileOpenResult("reading magic byte has failed");
        }
        String magicByte = readBuffer.readUTF8EncodedString(magicByteLength);
        if (BINARY_OSM_MAGIC_BYTE.equals(magicByte)) {
            return FileOpenResult.SUCCESS;
        }
        return new FileOpenResult("invalid magic byte: " + magicByte);
    }

    static FileOpenResult readMapDate(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        long mapDate = readBuffer.readLong();
        if (mapDate < 1200000000000L) {
            return new FileOpenResult("invalid map date: " + mapDate);
        }
        mapFileInfoBuilder.mapDate = mapDate;
        return FileOpenResult.SUCCESS;
    }

    static FileOpenResult readPoiTags(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        int numberOfPoiTags = readBuffer.readShort();
        if (numberOfPoiTags < 0) {
            return new FileOpenResult("invalid number of POI tags: " + numberOfPoiTags);
        }
        Tag[] poiTags = new Tag[numberOfPoiTags];
        for (int currentTagId = 0; currentTagId < numberOfPoiTags; currentTagId++) {
            String tag = readBuffer.readUTF8EncodedString();
            if (tag == null) {
                return new FileOpenResult("POI tag must not be null: " + currentTagId);
            }
            poiTags[currentTagId] = new Tag(tag);
        }
        mapFileInfoBuilder.poiTags = poiTags;
        return FileOpenResult.SUCCESS;
    }

    static FileOpenResult readProjectionName(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        String projectionName = readBuffer.readUTF8EncodedString();
        if (!MERCATOR.equals(projectionName)) {
            return new FileOpenResult("unsupported projection: " + projectionName);
        }
        mapFileInfoBuilder.projectionName = projectionName;
        return FileOpenResult.SUCCESS;
    }

    static FileOpenResult readRemainingHeader(ReadBuffer readBuffer) throws IOException {
        int remainingHeaderSize = readBuffer.readInt();
        if (remainingHeaderSize < HEADER_SIZE_MIN || remainingHeaderSize > HEADER_SIZE_MAX) {
            return new FileOpenResult("invalid remaining header size: " + remainingHeaderSize);
        }
        if (readBuffer.readFromFile(remainingHeaderSize)) {
            return FileOpenResult.SUCCESS;
        }
        return new FileOpenResult("reading header data has failed: " + remainingHeaderSize);
    }

    static FileOpenResult readTilePixelSize(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        int tilePixelSize = readBuffer.readShort();
        if (tilePixelSize != 256) {
            return new FileOpenResult("unsupported tile pixel size: " + tilePixelSize);
        }
        mapFileInfoBuilder.tilePixelSize = tilePixelSize;
        return FileOpenResult.SUCCESS;
    }

    static FileOpenResult readWayTags(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        int numberOfWayTags = readBuffer.readShort();
        if (numberOfWayTags < 0) {
            return new FileOpenResult("invalid number of way tags: " + numberOfWayTags);
        }
        Tag[] wayTags = new Tag[numberOfWayTags];
        for (int currentTagId = 0; currentTagId < numberOfWayTags; currentTagId++) {
            String tag = readBuffer.readUTF8EncodedString();
            if (tag == null) {
                return new FileOpenResult("way tag must not be null: " + currentTagId);
            }
            wayTags[currentTagId] = new Tag(tag);
        }
        mapFileInfoBuilder.wayTags = wayTags;
        return FileOpenResult.SUCCESS;
    }

    private RequiredFields() {
        throw new IllegalStateException();
    }
}

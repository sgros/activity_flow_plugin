// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader.header;

import org.mapsforge.core.model.Tag;
import java.io.IOException;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.CoordinatesUtil;
import org.mapsforge.map.reader.ReadBuffer;

final class RequiredFields
{
    private static final String BINARY_OSM_MAGIC_BYTE = "mapsforge binary OSM";
    private static final int HEADER_SIZE_MAX = 1000000;
    private static final int HEADER_SIZE_MIN = 70;
    private static final String MERCATOR = "Mercator";
    private static final int SUPPORTED_FILE_VERSION = 3;
    
    private RequiredFields() {
        throw new IllegalStateException();
    }
    
    static FileOpenResult readBoundingBox(final ReadBuffer readBuffer, final MapFileInfoBuilder mapFileInfoBuilder) {
        final double microdegreesToDegrees = CoordinatesUtil.microdegreesToDegrees(readBuffer.readInt());
        final double microdegreesToDegrees2 = CoordinatesUtil.microdegreesToDegrees(readBuffer.readInt());
        final double microdegreesToDegrees3 = CoordinatesUtil.microdegreesToDegrees(readBuffer.readInt());
        final double microdegreesToDegrees4 = CoordinatesUtil.microdegreesToDegrees(readBuffer.readInt());
        try {
            mapFileInfoBuilder.boundingBox = new BoundingBox(microdegreesToDegrees, microdegreesToDegrees2, microdegreesToDegrees3, microdegreesToDegrees4);
            return FileOpenResult.SUCCESS;
        }
        catch (IllegalArgumentException ex) {
            return new FileOpenResult(ex.getMessage());
        }
    }
    
    static FileOpenResult readFileSize(final ReadBuffer readBuffer, final long fileSize, final MapFileInfoBuilder mapFileInfoBuilder) {
        final long long1 = readBuffer.readLong();
        FileOpenResult success;
        if (long1 != fileSize) {
            success = new FileOpenResult("invalid file size: " + long1);
        }
        else {
            mapFileInfoBuilder.fileSize = fileSize;
            success = FileOpenResult.SUCCESS;
        }
        return success;
    }
    
    static FileOpenResult readFileVersion(final ReadBuffer readBuffer, final MapFileInfoBuilder mapFileInfoBuilder) {
        final int int1 = readBuffer.readInt();
        FileOpenResult success;
        if (int1 != 3) {
            success = new FileOpenResult("unsupported file version: " + int1);
        }
        else {
            mapFileInfoBuilder.fileVersion = int1;
            success = FileOpenResult.SUCCESS;
        }
        return success;
    }
    
    static FileOpenResult readMagicByte(final ReadBuffer readBuffer) throws IOException {
        final int length = "mapsforge binary OSM".length();
        FileOpenResult success;
        if (!readBuffer.readFromFile(length + 4)) {
            success = new FileOpenResult("reading magic byte has failed");
        }
        else {
            final String utf8EncodedString = readBuffer.readUTF8EncodedString(length);
            if (!"mapsforge binary OSM".equals(utf8EncodedString)) {
                success = new FileOpenResult("invalid magic byte: " + utf8EncodedString);
            }
            else {
                success = FileOpenResult.SUCCESS;
            }
        }
        return success;
    }
    
    static FileOpenResult readMapDate(final ReadBuffer readBuffer, final MapFileInfoBuilder mapFileInfoBuilder) {
        final long long1 = readBuffer.readLong();
        FileOpenResult success;
        if (long1 < 1200000000000L) {
            success = new FileOpenResult("invalid map date: " + long1);
        }
        else {
            mapFileInfoBuilder.mapDate = long1;
            success = FileOpenResult.SUCCESS;
        }
        return success;
    }
    
    static FileOpenResult readPoiTags(final ReadBuffer readBuffer, final MapFileInfoBuilder mapFileInfoBuilder) {
        final int short1 = readBuffer.readShort();
        FileOpenResult success;
        if (short1 < 0) {
            success = new FileOpenResult("invalid number of POI tags: " + short1);
        }
        else {
            final Tag[] poiTags = new Tag[short1];
            for (int i = 0; i < short1; ++i) {
                final String utf8EncodedString = readBuffer.readUTF8EncodedString();
                if (utf8EncodedString == null) {
                    success = new FileOpenResult("POI tag must not be null: " + i);
                    return success;
                }
                poiTags[i] = new Tag(utf8EncodedString);
            }
            mapFileInfoBuilder.poiTags = poiTags;
            success = FileOpenResult.SUCCESS;
        }
        return success;
    }
    
    static FileOpenResult readProjectionName(final ReadBuffer readBuffer, final MapFileInfoBuilder mapFileInfoBuilder) {
        final String utf8EncodedString = readBuffer.readUTF8EncodedString();
        FileOpenResult success;
        if (!"Mercator".equals(utf8EncodedString)) {
            success = new FileOpenResult("unsupported projection: " + utf8EncodedString);
        }
        else {
            mapFileInfoBuilder.projectionName = utf8EncodedString;
            success = FileOpenResult.SUCCESS;
        }
        return success;
    }
    
    static FileOpenResult readRemainingHeader(final ReadBuffer readBuffer) throws IOException {
        final int int1 = readBuffer.readInt();
        FileOpenResult success;
        if (int1 < 70 || int1 > 1000000) {
            success = new FileOpenResult("invalid remaining header size: " + int1);
        }
        else if (!readBuffer.readFromFile(int1)) {
            success = new FileOpenResult("reading header data has failed: " + int1);
        }
        else {
            success = FileOpenResult.SUCCESS;
        }
        return success;
    }
    
    static FileOpenResult readTilePixelSize(final ReadBuffer readBuffer, final MapFileInfoBuilder mapFileInfoBuilder) {
        final int short1 = readBuffer.readShort();
        FileOpenResult success;
        if (short1 != 256) {
            success = new FileOpenResult("unsupported tile pixel size: " + short1);
        }
        else {
            mapFileInfoBuilder.tilePixelSize = short1;
            success = FileOpenResult.SUCCESS;
        }
        return success;
    }
    
    static FileOpenResult readWayTags(final ReadBuffer readBuffer, final MapFileInfoBuilder mapFileInfoBuilder) {
        final int short1 = readBuffer.readShort();
        FileOpenResult success;
        if (short1 < 0) {
            success = new FileOpenResult("invalid number of way tags: " + short1);
        }
        else {
            final Tag[] wayTags = new Tag[short1];
            for (int i = 0; i < short1; ++i) {
                final String utf8EncodedString = readBuffer.readUTF8EncodedString();
                if (utf8EncodedString == null) {
                    success = new FileOpenResult("way tag must not be null: " + i);
                    return success;
                }
                wayTags[i] = new Tag(utf8EncodedString);
            }
            mapFileInfoBuilder.wayTags = wayTags;
            success = FileOpenResult.SUCCESS;
        }
        return success;
    }
}

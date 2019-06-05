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

    static FileOpenResult readOptionalFields(ReadBuffer readBuffer, MapFileInfoBuilder mapFileInfoBuilder) {
        OptionalFields optionalFields = new OptionalFields(readBuffer.readByte());
        mapFileInfoBuilder.optionalFields = optionalFields;
        FileOpenResult fileOpenResult = optionalFields.readOptionalFields(readBuffer);
        return !fileOpenResult.isSuccess() ? fileOpenResult : FileOpenResult.SUCCESS;
    }

    private OptionalFields(byte flags) {
        boolean z;
        boolean z2 = true;
        this.isDebugFile = (flags & 128) != 0;
        if ((flags & 64) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.hasStartPosition = z;
        if ((flags & 32) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.hasStartZoomLevel = z;
        if ((flags & 16) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.hasLanguagePreference = z;
        if ((flags & 8) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.hasComment = z;
        if ((flags & 4) == 0) {
            z2 = false;
        }
        this.hasCreatedBy = z2;
    }

    private FileOpenResult readLanguagePreference(ReadBuffer readBuffer) {
        if (this.hasLanguagePreference) {
            String countryCode = readBuffer.readUTF8EncodedString();
            if (countryCode.length() != 2) {
                return new FileOpenResult("invalid language preference: " + countryCode);
            }
            this.languagePreference = countryCode;
        }
        return FileOpenResult.SUCCESS;
    }

    private FileOpenResult readMapStartPosition(ReadBuffer readBuffer) {
        if (this.hasStartPosition) {
            try {
                this.startPosition = new GeoPoint(CoordinatesUtil.microdegreesToDegrees(readBuffer.readInt()), CoordinatesUtil.microdegreesToDegrees(readBuffer.readInt()));
            } catch (IllegalArgumentException e) {
                return new FileOpenResult(e.getMessage());
            }
        }
        return FileOpenResult.SUCCESS;
    }

    private FileOpenResult readMapStartZoomLevel(ReadBuffer readBuffer) {
        if (this.hasStartZoomLevel) {
            byte mapStartZoomLevel = readBuffer.readByte();
            if (mapStartZoomLevel < (byte) 0 || mapStartZoomLevel > (byte) 22) {
                return new FileOpenResult("invalid map start zoom level: " + mapStartZoomLevel);
            }
            this.startZoomLevel = Byte.valueOf(mapStartZoomLevel);
        }
        return FileOpenResult.SUCCESS;
    }

    private FileOpenResult readOptionalFields(ReadBuffer readBuffer) {
        FileOpenResult fileOpenResult = readMapStartPosition(readBuffer);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        fileOpenResult = readMapStartZoomLevel(readBuffer);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        fileOpenResult = readLanguagePreference(readBuffer);
        if (!fileOpenResult.isSuccess()) {
            return fileOpenResult;
        }
        if (this.hasComment) {
            this.comment = readBuffer.readUTF8EncodedString();
        }
        if (this.hasCreatedBy) {
            this.createdBy = readBuffer.readUTF8EncodedString();
        }
        return FileOpenResult.SUCCESS;
    }
}

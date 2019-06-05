// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader.header;

import org.mapsforge.core.model.CoordinatesUtil;
import org.mapsforge.map.reader.ReadBuffer;
import org.mapsforge.core.model.GeoPoint;

final class OptionalFields
{
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
    
    private OptionalFields(final byte b) {
        final boolean b2 = true;
        this.isDebugFile = ((b & 0x80) != 0x0);
        this.hasStartPosition = ((b & 0x40) != 0x0);
        this.hasStartZoomLevel = ((b & 0x20) != 0x0);
        this.hasLanguagePreference = ((b & 0x10) != 0x0);
        this.hasComment = ((b & 0x8) != 0x0);
        this.hasCreatedBy = ((b & 0x4) != 0x0 && b2);
    }
    
    private FileOpenResult readLanguagePreference(final ReadBuffer readBuffer) {
        if (!this.hasLanguagePreference) {
            return FileOpenResult.SUCCESS;
        }
        final String utf8EncodedString = readBuffer.readUTF8EncodedString();
        if (utf8EncodedString.length() == 2) {
            this.languagePreference = utf8EncodedString;
            return FileOpenResult.SUCCESS;
        }
        return new FileOpenResult("invalid language preference: " + utf8EncodedString);
        success = FileOpenResult.SUCCESS;
        return success;
    }
    
    private FileOpenResult readMapStartPosition(final ReadBuffer readBuffer) {
        Label_0040: {
            if (!this.hasStartPosition) {
                break Label_0040;
            }
            final double microdegreesToDegrees = CoordinatesUtil.microdegreesToDegrees(readBuffer.readInt());
            final double microdegreesToDegrees2 = CoordinatesUtil.microdegreesToDegrees(readBuffer.readInt());
            try {
                this.startPosition = new GeoPoint(microdegreesToDegrees, microdegreesToDegrees2);
                return FileOpenResult.SUCCESS;
            }
            catch (IllegalArgumentException ex) {
                return new FileOpenResult(ex.getMessage());
            }
        }
    }
    
    private FileOpenResult readMapStartZoomLevel(final ReadBuffer readBuffer) {
        if (!this.hasStartZoomLevel) {
            return FileOpenResult.SUCCESS;
        }
        final byte byte1 = readBuffer.readByte();
        if (byte1 >= 0 && byte1 <= 22) {
            this.startZoomLevel = byte1;
            return FileOpenResult.SUCCESS;
        }
        return new FileOpenResult("invalid map start zoom level: " + byte1);
        success = FileOpenResult.SUCCESS;
        return success;
    }
    
    private FileOpenResult readOptionalFields(final ReadBuffer readBuffer) {
        final FileOpenResult mapStartPosition = this.readMapStartPosition(readBuffer);
        FileOpenResult success;
        if (!mapStartPosition.isSuccess()) {
            success = mapStartPosition;
        }
        else {
            final FileOpenResult mapStartZoomLevel = this.readMapStartZoomLevel(readBuffer);
            if (!mapStartZoomLevel.isSuccess()) {
                success = mapStartZoomLevel;
            }
            else {
                final FileOpenResult languagePreference = this.readLanguagePreference(readBuffer);
                if (!languagePreference.isSuccess()) {
                    success = languagePreference;
                }
                else {
                    if (this.hasComment) {
                        this.comment = readBuffer.readUTF8EncodedString();
                    }
                    if (this.hasCreatedBy) {
                        this.createdBy = readBuffer.readUTF8EncodedString();
                    }
                    success = FileOpenResult.SUCCESS;
                }
            }
        }
        return success;
    }
    
    static FileOpenResult readOptionalFields(final ReadBuffer readBuffer, final MapFileInfoBuilder mapFileInfoBuilder) {
        final OptionalFields optionalFields = new OptionalFields(readBuffer.readByte());
        mapFileInfoBuilder.optionalFields = optionalFields;
        FileOpenResult fileOpenResult = optionalFields.readOptionalFields(readBuffer);
        if (fileOpenResult.isSuccess()) {
            fileOpenResult = FileOpenResult.SUCCESS;
        }
        return fileOpenResult;
    }
}

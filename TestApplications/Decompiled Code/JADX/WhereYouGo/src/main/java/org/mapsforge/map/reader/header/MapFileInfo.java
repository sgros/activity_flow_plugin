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

    MapFileInfo(MapFileInfoBuilder mapFileInfoBuilder) {
        this.comment = mapFileInfoBuilder.optionalFields.comment;
        this.createdBy = mapFileInfoBuilder.optionalFields.createdBy;
        this.debugFile = mapFileInfoBuilder.optionalFields.isDebugFile;
        this.fileSize = mapFileInfoBuilder.fileSize;
        this.fileVersion = mapFileInfoBuilder.fileVersion;
        this.languagePreference = mapFileInfoBuilder.optionalFields.languagePreference;
        this.boundingBox = mapFileInfoBuilder.boundingBox;
        this.mapDate = mapFileInfoBuilder.mapDate;
        this.numberOfSubFiles = mapFileInfoBuilder.numberOfSubFiles;
        this.poiTags = mapFileInfoBuilder.poiTags;
        this.projectionName = mapFileInfoBuilder.projectionName;
        this.startPosition = mapFileInfoBuilder.optionalFields.startPosition;
        this.startZoomLevel = mapFileInfoBuilder.optionalFields.startZoomLevel;
        this.tilePixelSize = mapFileInfoBuilder.tilePixelSize;
        this.wayTags = mapFileInfoBuilder.wayTags;
    }
}

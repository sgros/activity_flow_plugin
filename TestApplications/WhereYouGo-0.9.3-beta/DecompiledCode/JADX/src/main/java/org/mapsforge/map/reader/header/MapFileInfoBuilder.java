package org.mapsforge.map.reader.header;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Tag;

class MapFileInfoBuilder {
    BoundingBox boundingBox;
    long fileSize;
    int fileVersion;
    long mapDate;
    byte numberOfSubFiles;
    OptionalFields optionalFields;
    Tag[] poiTags;
    String projectionName;
    int tilePixelSize;
    Tag[] wayTags;

    MapFileInfoBuilder() {
    }

    /* Access modifiers changed, original: 0000 */
    public MapFileInfo build() {
        return new MapFileInfo(this);
    }
}

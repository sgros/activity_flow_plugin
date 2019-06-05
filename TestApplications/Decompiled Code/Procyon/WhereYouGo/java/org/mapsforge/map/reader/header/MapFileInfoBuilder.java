// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader.header;

import org.mapsforge.core.model.Tag;
import org.mapsforge.core.model.BoundingBox;

class MapFileInfoBuilder
{
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
    
    MapFileInfo build() {
        return new MapFileInfo(this);
    }
}

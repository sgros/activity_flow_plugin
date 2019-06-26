// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader;

import org.mapsforge.core.model.Tag;
import java.util.List;
import org.mapsforge.core.model.GeoPoint;

public class PointOfInterest
{
    public final byte layer;
    public final GeoPoint position;
    public final List<Tag> tags;
    
    PointOfInterest(final byte b, final List<Tag> tags, final GeoPoint position) {
        this.layer = b;
        this.tags = tags;
        this.position = position;
    }
}

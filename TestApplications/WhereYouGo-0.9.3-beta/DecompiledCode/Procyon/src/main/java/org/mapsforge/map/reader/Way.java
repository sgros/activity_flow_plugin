// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader;

import org.mapsforge.core.model.Tag;
import java.util.List;
import org.mapsforge.core.model.GeoPoint;

public class Way
{
    public final GeoPoint[][] geoPoints;
    public final GeoPoint labelPosition;
    public final byte layer;
    public final List<Tag> tags;
    
    Way(final byte b, final List<Tag> tags, final GeoPoint[][] geoPoints, final GeoPoint labelPosition) {
        this.layer = b;
        this.tags = tags;
        this.geoPoints = geoPoints;
        this.labelPosition = labelPosition;
    }
}

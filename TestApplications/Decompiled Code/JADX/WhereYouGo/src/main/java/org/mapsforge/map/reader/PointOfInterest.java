package org.mapsforge.map.reader;

import java.util.List;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Tag;

public class PointOfInterest {
    public final byte layer;
    public final GeoPoint position;
    public final List<Tag> tags;

    PointOfInterest(byte layer, List<Tag> tags, GeoPoint position) {
        this.layer = layer;
        this.tags = tags;
        this.position = position;
    }
}

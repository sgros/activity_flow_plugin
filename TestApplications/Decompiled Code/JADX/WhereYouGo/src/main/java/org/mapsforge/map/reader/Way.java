package org.mapsforge.map.reader;

import java.util.List;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Tag;

public class Way {
    public final GeoPoint[][] geoPoints;
    public final GeoPoint labelPosition;
    public final byte layer;
    public final List<Tag> tags;

    Way(byte layer, List<Tag> tags, GeoPoint[][] geoPoints, GeoPoint labelPosition) {
        this.layer = layer;
        this.tags = tags;
        this.geoPoints = geoPoints;
        this.labelPosition = labelPosition;
    }
}

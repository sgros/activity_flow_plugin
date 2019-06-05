package org.mapsforge.core.model;

import java.io.Serializable;

public class MapPosition implements Serializable {
    private static final long serialVersionUID = 1;
    public final GeoPoint geoPoint;
    public final byte zoomLevel;

    public MapPosition(GeoPoint geoPoint, byte zoomLevel) {
        if (geoPoint == null) {
            throw new IllegalArgumentException("geoPoint must not be null");
        } else if (zoomLevel < (byte) 0) {
            throw new IllegalArgumentException("zoomLevel must not be negative: " + zoomLevel);
        } else {
            this.geoPoint = geoPoint;
            this.zoomLevel = zoomLevel;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MapPosition)) {
            return false;
        }
        MapPosition other = (MapPosition) obj;
        if (this.geoPoint == null) {
            if (other.geoPoint != null) {
                return false;
            }
            return true;
        } else if (!this.geoPoint.equals(other.geoPoint)) {
            return false;
        } else {
            if (this.zoomLevel != other.zoomLevel) {
                return false;
            }
            return true;
        }
    }

    public int hashCode() {
        return (((this.geoPoint == null ? 0 : this.geoPoint.hashCode()) + 31) * 31) + this.zoomLevel;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("geoPoint=");
        stringBuilder.append(this.geoPoint);
        stringBuilder.append(", zoomLevel=");
        stringBuilder.append(this.zoomLevel);
        return stringBuilder.toString();
    }
}

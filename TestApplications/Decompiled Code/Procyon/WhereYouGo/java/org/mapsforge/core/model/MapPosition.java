// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.core.model;

import java.io.Serializable;

public class MapPosition implements Serializable
{
    private static final long serialVersionUID = 1L;
    public final GeoPoint geoPoint;
    public final byte zoomLevel;
    
    public MapPosition(final GeoPoint geoPoint, final byte i) {
        if (geoPoint == null) {
            throw new IllegalArgumentException("geoPoint must not be null");
        }
        if (i < 0) {
            throw new IllegalArgumentException("zoomLevel must not be negative: " + i);
        }
        this.geoPoint = geoPoint;
        this.zoomLevel = i;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (!(o instanceof MapPosition)) {
                b = false;
            }
            else {
                final MapPosition mapPosition = (MapPosition)o;
                if (this.geoPoint == null) {
                    if (mapPosition.geoPoint != null) {
                        b = false;
                    }
                }
                else if (!this.geoPoint.equals(mapPosition.geoPoint)) {
                    b = false;
                }
                else if (this.zoomLevel != mapPosition.zoomLevel) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    @Override
    public int hashCode() {
        int hashCode;
        if (this.geoPoint == null) {
            hashCode = 0;
        }
        else {
            hashCode = this.geoPoint.hashCode();
        }
        return (hashCode + 31) * 31 + this.zoomLevel;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("geoPoint=");
        sb.append(this.geoPoint);
        sb.append(", zoomLevel=");
        sb.append(this.zoomLevel);
        return sb.toString();
    }
}

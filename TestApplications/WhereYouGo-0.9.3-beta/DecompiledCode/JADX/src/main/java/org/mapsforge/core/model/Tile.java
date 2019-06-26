package org.mapsforge.core.model;

import java.io.Serializable;

public class Tile implements Serializable {
    public static final int TILE_SIZE = 256;
    private static final long serialVersionUID = 1;
    public final long tileX;
    public final long tileY;
    public final byte zoomLevel;

    public Tile(long tileX, long tileY, byte zoomLevel) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.zoomLevel = zoomLevel;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tile)) {
            return false;
        }
        Tile other = (Tile) obj;
        if (this.tileX != other.tileX) {
            return false;
        }
        if (this.tileY != other.tileY) {
            return false;
        }
        if (this.zoomLevel != other.zoomLevel) {
            return false;
        }
        return true;
    }

    public long getPixelX() {
        return this.tileX * 256;
    }

    public long getPixelY() {
        return this.tileY * 256;
    }

    public int hashCode() {
        return ((((((int) (this.tileX ^ (this.tileX >>> 32))) + 217) * 31) + ((int) (this.tileY ^ (this.tileY >>> 32)))) * 31) + this.zoomLevel;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("tileX=");
        stringBuilder.append(this.tileX);
        stringBuilder.append(", tileY=");
        stringBuilder.append(this.tileY);
        stringBuilder.append(", zoomLevel=");
        stringBuilder.append(this.zoomLevel);
        return stringBuilder.toString();
    }
}

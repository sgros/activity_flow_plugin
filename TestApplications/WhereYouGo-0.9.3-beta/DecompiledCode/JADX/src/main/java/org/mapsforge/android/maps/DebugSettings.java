package org.mapsforge.android.maps;

import java.io.Serializable;

public class DebugSettings implements Serializable {
    private static final long serialVersionUID = 1;
    public final boolean drawTileCoordinates;
    public final boolean drawTileFrames;
    private final int hashCodeValue = calculateHashCode();
    public final boolean highlightWaterTiles;

    public DebugSettings(boolean drawTileCoordinates, boolean drawTileFrames, boolean highlightWaterTiles) {
        this.drawTileCoordinates = drawTileCoordinates;
        this.drawTileFrames = drawTileFrames;
        this.highlightWaterTiles = highlightWaterTiles;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DebugSettings)) {
            return false;
        }
        DebugSettings other = (DebugSettings) obj;
        if (this.drawTileCoordinates != other.drawTileCoordinates) {
            return false;
        }
        if (this.drawTileFrames != other.drawTileFrames) {
            return false;
        }
        if (this.highlightWaterTiles != other.highlightWaterTiles) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.hashCodeValue;
    }

    private int calculateHashCode() {
        int i;
        int i2 = 1231;
        if (this.drawTileCoordinates) {
            i = 1231;
        } else {
            i = 1237;
        }
        int i3 = (i + 31) * 31;
        if (this.drawTileFrames) {
            i = 1231;
        } else {
            i = 1237;
        }
        i = (i3 + i) * 31;
        if (!this.highlightWaterTiles) {
            i2 = 1237;
        }
        return i + i2;
    }
}

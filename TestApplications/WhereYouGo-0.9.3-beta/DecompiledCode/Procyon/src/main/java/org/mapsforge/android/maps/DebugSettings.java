// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps;

import java.io.Serializable;

public class DebugSettings implements Serializable
{
    private static final long serialVersionUID = 1L;
    public final boolean drawTileCoordinates;
    public final boolean drawTileFrames;
    private final int hashCodeValue;
    public final boolean highlightWaterTiles;
    
    public DebugSettings(final boolean drawTileCoordinates, final boolean drawTileFrames, final boolean highlightWaterTiles) {
        this.drawTileCoordinates = drawTileCoordinates;
        this.drawTileFrames = drawTileFrames;
        this.highlightWaterTiles = highlightWaterTiles;
        this.hashCodeValue = this.calculateHashCode();
    }
    
    private int calculateHashCode() {
        int n = 1231;
        int n2;
        if (this.drawTileCoordinates) {
            n2 = 1231;
        }
        else {
            n2 = 1237;
        }
        int n3;
        if (this.drawTileFrames) {
            n3 = 1231;
        }
        else {
            n3 = 1237;
        }
        if (!this.highlightWaterTiles) {
            n = 1237;
        }
        return ((n2 + 31) * 31 + n3) * 31 + n;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (!(o instanceof DebugSettings)) {
                b = false;
            }
            else {
                final DebugSettings debugSettings = (DebugSettings)o;
                if (this.drawTileCoordinates != debugSettings.drawTileCoordinates) {
                    b = false;
                }
                else if (this.drawTileFrames != debugSettings.drawTileFrames) {
                    b = false;
                }
                else if (this.highlightWaterTiles != debugSettings.highlightWaterTiles) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    @Override
    public int hashCode() {
        return this.hashCodeValue;
    }
}

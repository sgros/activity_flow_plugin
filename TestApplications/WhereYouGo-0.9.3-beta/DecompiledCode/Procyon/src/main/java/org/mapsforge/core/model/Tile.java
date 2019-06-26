// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.core.model;

import java.io.Serializable;

public class Tile implements Serializable
{
    public static final int TILE_SIZE = 256;
    private static final long serialVersionUID = 1L;
    public final long tileX;
    public final long tileY;
    public final byte zoomLevel;
    
    public Tile(final long tileX, final long tileY, final byte b) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.zoomLevel = b;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (!(o instanceof Tile)) {
                b = false;
            }
            else {
                final Tile tile = (Tile)o;
                if (this.tileX != tile.tileX) {
                    b = false;
                }
                else if (this.tileY != tile.tileY) {
                    b = false;
                }
                else if (this.zoomLevel != tile.zoomLevel) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    public long getPixelX() {
        return this.tileX * 256L;
    }
    
    public long getPixelY() {
        return this.tileY * 256L;
    }
    
    @Override
    public int hashCode() {
        return (((int)(this.tileX ^ this.tileX >>> 32) + 217) * 31 + (int)(this.tileY ^ this.tileY >>> 32)) * 31 + this.zoomLevel;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("tileX=");
        sb.append(this.tileX);
        sb.append(", tileY=");
        sb.append(this.tileY);
        sb.append(", zoomLevel=");
        sb.append(this.zoomLevel);
        return sb.toString();
    }
}

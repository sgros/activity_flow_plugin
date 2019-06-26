// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import android.graphics.Rect;

public abstract class TileLooper
{
    private boolean horizontalWrapEnabled;
    protected int mTileZoomLevel;
    protected final Rect mTiles;
    private boolean verticalWrapEnabled;
    
    public TileLooper() {
        this(false, false);
    }
    
    public TileLooper(final boolean horizontalWrapEnabled, final boolean verticalWrapEnabled) {
        this.mTiles = new Rect();
        this.horizontalWrapEnabled = true;
        this.verticalWrapEnabled = true;
        this.horizontalWrapEnabled = horizontalWrapEnabled;
        this.verticalWrapEnabled = verticalWrapEnabled;
    }
    
    public abstract void finaliseLoop();
    
    public abstract void handleTile(final long p0, final int p1, final int p2);
    
    public void initialiseLoop() {
    }
    
    protected void loop(final double n, final RectL rectL) {
        TileSystem.getTileFromMercator(rectL, TileSystem.getTileSize(n), this.mTiles);
        this.mTileZoomLevel = TileSystem.getInputTileZoomLevel(n);
        this.initialiseLoop();
        final int n2 = 1 << this.mTileZoomLevel;
        int left = this.mTiles.left;
        while (true) {
            final Rect mTiles = this.mTiles;
            if (left > mTiles.right) {
                break;
            }
            for (int i = mTiles.top; i <= this.mTiles.bottom; ++i) {
                if ((this.horizontalWrapEnabled || (left >= 0 && left < n2)) && (this.verticalWrapEnabled || (i >= 0 && i < n2))) {
                    this.handleTile(MapTileIndex.getTileIndex(this.mTileZoomLevel, MyMath.mod(left, n2), MyMath.mod(i, n2)), left, i);
                }
            }
            ++left;
        }
        this.finaliseLoop();
    }
    
    public void setHorizontalWrapEnabled(final boolean horizontalWrapEnabled) {
        this.horizontalWrapEnabled = horizontalWrapEnabled;
    }
    
    public void setVerticalWrapEnabled(final boolean verticalWrapEnabled) {
        this.verticalWrapEnabled = verticalWrapEnabled;
    }
}

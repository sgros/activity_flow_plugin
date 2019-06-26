package org.osmdroid.util;

import android.graphics.Rect;

public abstract class TileLooper {
    private boolean horizontalWrapEnabled;
    protected int mTileZoomLevel;
    protected final Rect mTiles;
    private boolean verticalWrapEnabled;

    public abstract void finaliseLoop();

    public abstract void handleTile(long j, int i, int i2);

    public void initialiseLoop() {
    }

    public TileLooper() {
        this(false, false);
    }

    public TileLooper(boolean z, boolean z2) {
        this.mTiles = new Rect();
        this.horizontalWrapEnabled = true;
        this.verticalWrapEnabled = true;
        this.horizontalWrapEnabled = z;
        this.verticalWrapEnabled = z2;
    }

    /* Access modifiers changed, original: protected */
    public void loop(double d, RectL rectL) {
        TileSystem.getTileFromMercator(rectL, TileSystem.getTileSize(d), this.mTiles);
        this.mTileZoomLevel = TileSystem.getInputTileZoomLevel(d);
        initialiseLoop();
        int i = 1 << this.mTileZoomLevel;
        int i2 = this.mTiles.left;
        while (true) {
            Rect rect = this.mTiles;
            if (i2 <= rect.right) {
                int i3 = rect.top;
                while (i3 <= this.mTiles.bottom) {
                    if ((this.horizontalWrapEnabled || (i2 >= 0 && i2 < i)) && (this.verticalWrapEnabled || (i3 >= 0 && i3 < i))) {
                        handleTile(MapTileIndex.getTileIndex(this.mTileZoomLevel, MyMath.mod(i2, i), MyMath.mod(i3, i)), i2, i3);
                    }
                    i3++;
                }
                i2++;
            } else {
                finaliseLoop();
                return;
            }
        }
    }

    public void setHorizontalWrapEnabled(boolean z) {
        this.horizontalWrapEnabled = z;
    }

    public void setVerticalWrapEnabled(boolean z) {
        this.verticalWrapEnabled = z;
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import android.graphics.Rect;
import java.util.Iterator;

public class MapTileArea implements MapTileContainer, IterableWithSize<Long>
{
    private int mHeight;
    private int mLeft;
    private int mMapTileUpperBound;
    private int mTop;
    private int mWidth;
    private int mZoom;
    
    private int cleanValue(int mMapTileUpperBound) {
        int n;
        while (true) {
            n = mMapTileUpperBound;
            if (mMapTileUpperBound >= 0) {
                break;
            }
            mMapTileUpperBound += this.mMapTileUpperBound;
        }
        while (true) {
            mMapTileUpperBound = this.mMapTileUpperBound;
            if (n < mMapTileUpperBound) {
                break;
            }
            n -= mMapTileUpperBound;
        }
        return n;
    }
    
    private int computeSize(final int i, int n) {
        while (i > n) {
            n += this.mMapTileUpperBound;
        }
        return Math.min(this.mMapTileUpperBound, n - i + 1);
    }
    
    private boolean contains(int i, final int n, final int n2) {
        while (i < n) {
            i += this.mMapTileUpperBound;
        }
        return i < n + n2;
    }
    
    @Override
    public boolean contains(final long n) {
        return MapTileIndex.getZoom(n) == this.mZoom && this.contains(MapTileIndex.getX(n), this.mLeft, this.mWidth) && this.contains(MapTileIndex.getY(n), this.mTop, this.mHeight);
    }
    
    public int getBottom() {
        return (this.mTop + this.mHeight) % this.mMapTileUpperBound;
    }
    
    public int getHeight() {
        return this.mHeight;
    }
    
    public int getLeft() {
        return this.mLeft;
    }
    
    public int getRight() {
        return (this.mLeft + this.mWidth) % this.mMapTileUpperBound;
    }
    
    public int getTop() {
        return this.mTop;
    }
    
    public int getWidth() {
        return this.mWidth;
    }
    
    public int getZoom() {
        return this.mZoom;
    }
    
    @Override
    public Iterator<Long> iterator() {
        return new Iterator<Long>() {
            private int mIndex;
            
            @Override
            public boolean hasNext() {
                return this.mIndex < MapTileArea.this.size();
            }
            
            @Override
            public Long next() {
                if (!this.hasNext()) {
                    return null;
                }
                int n = MapTileArea.this.mLeft + this.mIndex % MapTileArea.this.mWidth;
                final int n2 = MapTileArea.this.mTop + this.mIndex / MapTileArea.this.mWidth;
                ++this.mIndex;
                int i;
                while (true) {
                    i = n2;
                    if (n < MapTileArea.this.mMapTileUpperBound) {
                        break;
                    }
                    n -= MapTileArea.this.mMapTileUpperBound;
                }
                while (i >= MapTileArea.this.mMapTileUpperBound) {
                    i -= MapTileArea.this.mMapTileUpperBound;
                }
                return MapTileIndex.getTileIndex(MapTileArea.this.mZoom, n, i);
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public MapTileArea reset() {
        this.mWidth = 0;
        return this;
    }
    
    public MapTileArea set(final int mZoom, final int n, final int n2, final int n3, final int n4) {
        this.mZoom = mZoom;
        this.mMapTileUpperBound = 1 << this.mZoom;
        this.mWidth = this.computeSize(n, n3);
        this.mHeight = this.computeSize(n2, n4);
        this.mLeft = this.cleanValue(n);
        this.mTop = this.cleanValue(n2);
        return this;
    }
    
    public MapTileArea set(final int n, final Rect rect) {
        this.set(n, rect.left, rect.top, rect.right, rect.bottom);
        return this;
    }
    
    public MapTileArea set(final MapTileArea mapTileArea) {
        if (mapTileArea.size() == 0) {
            this.reset();
            return this;
        }
        this.set(mapTileArea.mZoom, mapTileArea.mLeft, mapTileArea.mTop, mapTileArea.getRight(), mapTileArea.getBottom());
        return this;
    }
    
    public int size() {
        return this.mWidth * this.mHeight;
    }
    
    @Override
    public String toString() {
        if (this.mWidth == 0) {
            return "MapTileArea:empty";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("MapTileArea:zoom=");
        sb.append(this.mZoom);
        sb.append(",left=");
        sb.append(this.mLeft);
        sb.append(",top=");
        sb.append(this.mTop);
        sb.append(",width=");
        sb.append(this.mWidth);
        sb.append(",height=");
        sb.append(this.mHeight);
        return sb.toString();
    }
}

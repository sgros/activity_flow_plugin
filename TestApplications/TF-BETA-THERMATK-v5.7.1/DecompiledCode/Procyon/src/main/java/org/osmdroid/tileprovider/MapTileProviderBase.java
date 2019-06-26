// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider;

import org.osmdroid.tileprovider.modules.MapTileApproximater;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import java.util.HashMap;
import android.graphics.Paint;
import org.osmdroid.util.TileLooper;
import org.osmdroid.util.RectL;
import org.osmdroid.util.PointL;
import org.osmdroid.util.TileSystem;
import android.graphics.Rect;
import org.osmdroid.views.Projection;
import java.util.Iterator;
import android.util.Log;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.config.Configuration;
import java.util.LinkedHashSet;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import android.os.Handler;
import java.util.Collection;
import android.graphics.drawable.Drawable;

public abstract class MapTileProviderBase implements IMapTileProviderCallback
{
    protected final MapTileCache mTileCache;
    protected Drawable mTileNotFoundImage;
    private final Collection<Handler> mTileRequestCompleteHandlers;
    private ITileSource mTileSource;
    protected boolean mUseDataConnection;
    
    public MapTileProviderBase(final ITileSource tileSource) {
        this(tileSource, null);
    }
    
    public MapTileProviderBase(final ITileSource mTileSource, final Handler handler) {
        this.mTileRequestCompleteHandlers = new LinkedHashSet<Handler>();
        this.mUseDataConnection = true;
        this.mTileNotFoundImage = null;
        this.mTileCache = this.createTileCache();
        this.mTileRequestCompleteHandlers.add(handler);
        this.mTileSource = mTileSource;
    }
    
    public void clearTileCache() {
        this.mTileCache.clear();
    }
    
    public MapTileCache createTileCache() {
        return new MapTileCache();
    }
    
    public void detach() {
        BitmapPool.getInstance().asyncRecycle(this.mTileNotFoundImage);
        this.mTileNotFoundImage = null;
        this.clearTileCache();
    }
    
    public void ensureCapacity(final int n) {
        this.mTileCache.ensureCapacity(n);
    }
    
    public abstract Drawable getMapTile(final long p0);
    
    public abstract int getMaximumZoomLevel();
    
    public abstract int getMinimumZoomLevel();
    
    public MapTileCache getTileCache() {
        return this.mTileCache;
    }
    
    public Collection<Handler> getTileRequestCompleteHandlers() {
        return this.mTileRequestCompleteHandlers;
    }
    
    public ITileSource getTileSource() {
        return this.mTileSource;
    }
    
    @Override
    public void mapTileRequestCompleted(final MapTileRequestState mapTileRequestState, final Drawable drawable) {
        this.putTileIntoCache(mapTileRequestState.getMapTile(), drawable, -1);
        for (final Handler handler : this.mTileRequestCompleteHandlers) {
            if (handler != null) {
                handler.sendEmptyMessage(0);
            }
        }
        if (Configuration.getInstance().isDebugTileProviders()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("MapTileProviderBase.mapTileRequestCompleted(): ");
            sb.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
            Log.d("OsmDroid", sb.toString());
        }
    }
    
    @Override
    public void mapTileRequestExpiredTile(final MapTileRequestState mapTileRequestState, final Drawable drawable) {
        this.putTileIntoCache(mapTileRequestState.getMapTile(), drawable, ExpirableBitmapDrawable.getState(drawable));
        for (final Handler handler : this.mTileRequestCompleteHandlers) {
            if (handler != null) {
                handler.sendEmptyMessage(0);
            }
        }
        if (Configuration.getInstance().isDebugTileProviders()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("MapTileProviderBase.mapTileRequestExpiredTile(): ");
            sb.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
            Log.d("OsmDroid", sb.toString());
        }
    }
    
    @Override
    public void mapTileRequestFailed(final MapTileRequestState mapTileRequestState) {
        if (this.mTileNotFoundImage != null) {
            this.putTileIntoCache(mapTileRequestState.getMapTile(), this.mTileNotFoundImage, -4);
            for (final Handler handler : this.mTileRequestCompleteHandlers) {
                if (handler != null) {
                    handler.sendEmptyMessage(0);
                }
            }
        }
        else {
            for (final Handler handler2 : this.mTileRequestCompleteHandlers) {
                if (handler2 != null) {
                    handler2.sendEmptyMessage(1);
                }
            }
        }
        if (Configuration.getInstance().isDebugTileProviders()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("MapTileProviderBase.mapTileRequestFailed(): ");
            sb.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
            Log.d("OsmDroid", sb.toString());
        }
    }
    
    protected void putTileIntoCache(final long n, final Drawable drawable, final int n2) {
        if (drawable == null) {
            return;
        }
        final Drawable mapTile = this.mTileCache.getMapTile(n);
        if (mapTile != null && ExpirableBitmapDrawable.getState(mapTile) > n2) {
            return;
        }
        ExpirableBitmapDrawable.setState(drawable, n2);
        this.mTileCache.putTile(n, drawable);
    }
    
    public void rescaleCache(final Projection projection, final double d, final double d2, final Rect rect) {
        if (TileSystem.getInputTileZoomLevel(d) == TileSystem.getInputTileZoomLevel(d2)) {
            return;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        if (Configuration.getInstance().isDebugTileProviders()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("rescale tile cache from ");
            sb.append(d2);
            sb.append(" to ");
            sb.append(d);
            Log.i("OsmDroid", sb.toString());
        }
        final PointL mercatorPixels = projection.toMercatorPixels(rect.left, rect.top, null);
        final PointL mercatorPixels2 = projection.toMercatorPixels(rect.right, rect.bottom, null);
        final RectL rectL = new RectL(mercatorPixels.x, mercatorPixels.y, mercatorPixels2.x, mercatorPixels2.y);
        ScaleTileLooper scaleTileLooper;
        if (d > d2) {
            scaleTileLooper = new ZoomInTileLooper();
        }
        else {
            scaleTileLooper = new ZoomOutTileLooper();
        }
        scaleTileLooper.loop(d, rectL, d2, this.getTileSource().getTileSizePixels());
        final long currentTimeMillis2 = System.currentTimeMillis();
        if (Configuration.getInstance().isDebugTileProviders()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Finished rescale in ");
            sb2.append(currentTimeMillis2 - currentTimeMillis);
            sb2.append("ms");
            Log.i("OsmDroid", sb2.toString());
        }
    }
    
    public void setTileSource(final ITileSource mTileSource) {
        this.mTileSource = mTileSource;
        this.clearTileCache();
    }
    
    public void setUseDataConnection(final boolean mUseDataConnection) {
        this.mUseDataConnection = mUseDataConnection;
    }
    
    public boolean useDataConnection() {
        return this.mUseDataConnection;
    }
    
    private abstract class ScaleTileLooper extends TileLooper
    {
        private boolean isWorth;
        protected Paint mDebugPaint;
        protected Rect mDestRect;
        protected int mDiff;
        protected final HashMap<Long, Bitmap> mNewTiles;
        protected int mOldTileZoomLevel;
        protected Rect mSrcRect;
        protected int mTileSize;
        protected int mTileSize_2;
        
        private ScaleTileLooper() {
            this.mNewTiles = new HashMap<Long, Bitmap>();
        }
        
        protected abstract void computeTile(final long p0, final int p1, final int p2);
        
        @Override
        public void finaliseLoop() {
            while (!this.mNewTiles.isEmpty()) {
                final long longValue = this.mNewTiles.keySet().iterator().next();
                this.putScaledTileIntoCache(longValue, this.mNewTiles.remove(longValue));
            }
        }
        
        @Override
        public void handleTile(final long n, final int n2, final int n3) {
            if (!this.isWorth) {
                return;
            }
            if (MapTileProviderBase.this.getMapTile(n) == null) {
                try {
                    this.computeTile(n, n2, n3);
                }
                catch (OutOfMemoryError outOfMemoryError) {
                    Log.e("OsmDroid", "OutOfMemoryError rescaling cache");
                }
            }
        }
        
        @Override
        public void initialiseLoop() {
            super.initialiseLoop();
            this.mDiff = Math.abs(super.mTileZoomLevel - this.mOldTileZoomLevel);
            final int mTileSize = this.mTileSize;
            final int mDiff = this.mDiff;
            this.mTileSize_2 = mTileSize >> mDiff;
            this.isWorth = (mDiff != 0);
        }
        
        public void loop(final double n, final RectL rectL, final double n2, final int mTileSize) {
            this.mSrcRect = new Rect();
            this.mDestRect = new Rect();
            this.mDebugPaint = new Paint();
            this.mOldTileZoomLevel = TileSystem.getInputTileZoomLevel(n2);
            this.mTileSize = mTileSize;
            this.loop(n, rectL);
        }
        
        protected void putScaledTileIntoCache(final long n, final Bitmap bitmap) {
            MapTileProviderBase.this.putTileIntoCache(n, (Drawable)new ReusableBitmapDrawable(bitmap), -3);
            if (Configuration.getInstance().isDebugMode()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Created scaled tile: ");
                sb.append(MapTileIndex.toString(n));
                Log.d("OsmDroid", sb.toString());
                this.mDebugPaint.setTextSize(40.0f);
                new Canvas(bitmap).drawText("scaled", 50.0f, 50.0f, this.mDebugPaint);
            }
        }
    }
    
    private class ZoomInTileLooper extends ScaleTileLooper
    {
        public void computeTile(final long l, final int n, final int n2) {
            final Drawable mapTile = MapTileProviderBase.this.mTileCache.getMapTile(MapTileIndex.getTileIndex(super.mOldTileZoomLevel, MapTileIndex.getX(l) >> super.mDiff, MapTileIndex.getY(l) >> super.mDiff));
            if (mapTile instanceof BitmapDrawable) {
                final Bitmap approximateTileFromLowerZoom = MapTileApproximater.approximateTileFromLowerZoom((BitmapDrawable)mapTile, l, super.mDiff);
                if (approximateTileFromLowerZoom != null) {
                    super.mNewTiles.put(l, approximateTileFromLowerZoom);
                }
            }
        }
    }
    
    private class ZoomOutTileLooper extends ScaleTileLooper
    {
        @Override
        protected void computeTile(final long l, int i, int j) {
            if (super.mDiff >= 4) {
                return;
            }
            final int x = MapTileIndex.getX(l);
            final int mDiff = super.mDiff;
            final int y = MapTileIndex.getY(l);
            final int mDiff2 = super.mDiff;
            final int n = 1 << mDiff2;
            Canvas canvas;
            Object value = canvas = null;
            Canvas canvas2;
            Object o;
            Canvas canvas3;
            Drawable mapTile;
            Object tileBitmap;
            Canvas canvas4;
            Bitmap bitmap;
            Rect mDestRect;
            int mTileSize_2;
            Canvas canvas5;
            for (i = 0; i < n; ++i, canvas5 = canvas3, value = o, canvas = canvas5) {
                canvas2 = canvas;
                o = value;
                j = 0;
                canvas3 = canvas2;
                while (j < n) {
                    mapTile = MapTileProviderBase.this.mTileCache.getMapTile(MapTileIndex.getTileIndex(super.mOldTileZoomLevel, (x << mDiff) + i, (y << mDiff2) + j));
                    tileBitmap = o;
                    canvas4 = canvas3;
                    if (mapTile instanceof BitmapDrawable) {
                        bitmap = ((BitmapDrawable)mapTile).getBitmap();
                        tileBitmap = o;
                        canvas4 = canvas3;
                        if (bitmap != null) {
                            if ((tileBitmap = o) == null) {
                                tileBitmap = MapTileApproximater.getTileBitmap(super.mTileSize);
                                canvas3 = new Canvas((Bitmap)tileBitmap);
                                canvas3.drawColor(-3355444);
                            }
                            mDestRect = super.mDestRect;
                            mTileSize_2 = super.mTileSize_2;
                            mDestRect.set(i * mTileSize_2, j * mTileSize_2, (i + 1) * mTileSize_2, mTileSize_2 * (j + 1));
                            canvas3.drawBitmap(bitmap, (Rect)null, super.mDestRect, (Paint)null);
                            canvas4 = canvas3;
                        }
                    }
                    ++j;
                    o = tileBitmap;
                    canvas3 = canvas4;
                }
            }
            if (value != null) {
                super.mNewTiles.put(l, value);
            }
        }
    }
}

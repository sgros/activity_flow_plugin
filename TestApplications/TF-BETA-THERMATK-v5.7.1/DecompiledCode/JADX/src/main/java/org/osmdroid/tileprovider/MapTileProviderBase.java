package org.osmdroid.tileprovider;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.modules.MapTileApproximater;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.PointL;
import org.osmdroid.util.RectL;
import org.osmdroid.util.TileLooper;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.Projection;

public abstract class MapTileProviderBase implements IMapTileProviderCallback {
    protected final MapTileCache mTileCache;
    protected Drawable mTileNotFoundImage;
    private final Collection<Handler> mTileRequestCompleteHandlers;
    private ITileSource mTileSource;
    protected boolean mUseDataConnection;

    private abstract class ScaleTileLooper extends TileLooper {
        private boolean isWorth;
        protected Paint mDebugPaint;
        protected Rect mDestRect;
        protected int mDiff;
        protected final HashMap<Long, Bitmap> mNewTiles;
        protected int mOldTileZoomLevel;
        protected Rect mSrcRect;
        protected int mTileSize;
        protected int mTileSize_2;

        public abstract void computeTile(long j, int i, int i2);

        private ScaleTileLooper() {
            this.mNewTiles = new HashMap();
        }

        public void loop(double d, RectL rectL, double d2, int i) {
            this.mSrcRect = new Rect();
            this.mDestRect = new Rect();
            this.mDebugPaint = new Paint();
            this.mOldTileZoomLevel = TileSystem.getInputTileZoomLevel(d2);
            this.mTileSize = i;
            loop(d, rectL);
        }

        public void initialiseLoop() {
            super.initialiseLoop();
            this.mDiff = Math.abs(this.mTileZoomLevel - this.mOldTileZoomLevel);
            int i = this.mTileSize;
            int i2 = this.mDiff;
            this.mTileSize_2 = i >> i2;
            this.isWorth = i2 != 0;
        }

        public void handleTile(long j, int i, int i2) {
            if (this.isWorth && MapTileProviderBase.this.getMapTile(j) == null) {
                try {
                    computeTile(j, i, i2);
                } catch (OutOfMemoryError unused) {
                    Log.e("OsmDroid", "OutOfMemoryError rescaling cache");
                }
            }
        }

        public void finaliseLoop() {
            while (!this.mNewTiles.isEmpty()) {
                long longValue = ((Long) this.mNewTiles.keySet().iterator().next()).longValue();
                putScaledTileIntoCache(longValue, (Bitmap) this.mNewTiles.remove(Long.valueOf(longValue)));
            }
        }

        /* Access modifiers changed, original: protected */
        public void putScaledTileIntoCache(long j, Bitmap bitmap) {
            MapTileProviderBase.this.putTileIntoCache(j, new ReusableBitmapDrawable(bitmap), -3);
            if (Configuration.getInstance().isDebugMode()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Created scaled tile: ");
                stringBuilder.append(MapTileIndex.toString(j));
                Log.d("OsmDroid", stringBuilder.toString());
                this.mDebugPaint.setTextSize(40.0f);
                new Canvas(bitmap).drawText("scaled", 50.0f, 50.0f, this.mDebugPaint);
            }
        }
    }

    private class ZoomInTileLooper extends ScaleTileLooper {
        private ZoomInTileLooper() {
            super();
        }

        public void computeTile(long j, int i, int i2) {
            Drawable mapTile = MapTileProviderBase.this.mTileCache.getMapTile(MapTileIndex.getTileIndex(this.mOldTileZoomLevel, MapTileIndex.getX(j) >> this.mDiff, MapTileIndex.getY(j) >> this.mDiff));
            if (mapTile instanceof BitmapDrawable) {
                Bitmap approximateTileFromLowerZoom = MapTileApproximater.approximateTileFromLowerZoom((BitmapDrawable) mapTile, j, this.mDiff);
                if (approximateTileFromLowerZoom != null) {
                    this.mNewTiles.put(Long.valueOf(j), approximateTileFromLowerZoom);
                }
            }
        }
    }

    private class ZoomOutTileLooper extends ScaleTileLooper {
        private ZoomOutTileLooper() {
            super();
        }

        /* Access modifiers changed, original: protected */
        public void computeTile(long j, int i, int i2) {
            if (this.mDiff < 4) {
                int x = MapTileIndex.getX(j) << this.mDiff;
                int y = MapTileIndex.getY(j);
                int i3 = this.mDiff;
                y <<= i3;
                i3 = 1 << i3;
                Bitmap bitmap = null;
                Canvas canvas = bitmap;
                int i4 = 0;
                while (i4 < i3) {
                    Canvas canvas2 = canvas;
                    Bitmap bitmap2 = bitmap;
                    for (int i5 = 0; i5 < i3; i5++) {
                        Drawable mapTile = MapTileProviderBase.this.mTileCache.getMapTile(MapTileIndex.getTileIndex(this.mOldTileZoomLevel, x + i4, y + i5));
                        if (mapTile instanceof BitmapDrawable) {
                            Bitmap bitmap3 = ((BitmapDrawable) mapTile).getBitmap();
                            if (bitmap3 != null) {
                                if (bitmap2 == null) {
                                    bitmap2 = MapTileApproximater.getTileBitmap(this.mTileSize);
                                    canvas2 = new Canvas(bitmap2);
                                    canvas2.drawColor(-3355444);
                                }
                                Rect rect = this.mDestRect;
                                int i6 = this.mTileSize_2;
                                rect.set(i4 * i6, i5 * i6, (i4 + 1) * i6, i6 * (i5 + 1));
                                canvas2.drawBitmap(bitmap3, null, this.mDestRect, null);
                            }
                        }
                    }
                    i4++;
                    bitmap = bitmap2;
                    canvas = canvas2;
                }
                if (bitmap != null) {
                    this.mNewTiles.put(Long.valueOf(j), bitmap);
                }
            }
        }
    }

    public abstract Drawable getMapTile(long j);

    public abstract int getMaximumZoomLevel();

    public abstract int getMinimumZoomLevel();

    public void detach() {
        BitmapPool.getInstance().asyncRecycle(this.mTileNotFoundImage);
        this.mTileNotFoundImage = null;
        clearTileCache();
    }

    public void setTileSource(ITileSource iTileSource) {
        this.mTileSource = iTileSource;
        clearTileCache();
    }

    public ITileSource getTileSource() {
        return this.mTileSource;
    }

    public MapTileCache createTileCache() {
        return new MapTileCache();
    }

    public MapTileProviderBase(ITileSource iTileSource) {
        this(iTileSource, null);
    }

    public MapTileProviderBase(ITileSource iTileSource, Handler handler) {
        this.mTileRequestCompleteHandlers = new LinkedHashSet();
        this.mUseDataConnection = true;
        this.mTileNotFoundImage = null;
        this.mTileCache = createTileCache();
        this.mTileRequestCompleteHandlers.add(handler);
        this.mTileSource = iTileSource;
    }

    public void mapTileRequestCompleted(MapTileRequestState mapTileRequestState, Drawable drawable) {
        putTileIntoCache(mapTileRequestState.getMapTile(), drawable, -1);
        for (Handler handler : this.mTileRequestCompleteHandlers) {
            if (handler != null) {
                handler.sendEmptyMessage(0);
            }
        }
        if (Configuration.getInstance().isDebugTileProviders()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MapTileProviderBase.mapTileRequestCompleted(): ");
            stringBuilder.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
            Log.d("OsmDroid", stringBuilder.toString());
        }
    }

    public void mapTileRequestFailed(MapTileRequestState mapTileRequestState) {
        if (this.mTileNotFoundImage != null) {
            putTileIntoCache(mapTileRequestState.getMapTile(), this.mTileNotFoundImage, -4);
            for (Handler handler : this.mTileRequestCompleteHandlers) {
                if (handler != null) {
                    handler.sendEmptyMessage(0);
                }
            }
        } else {
            for (Handler handler2 : this.mTileRequestCompleteHandlers) {
                if (handler2 != null) {
                    handler2.sendEmptyMessage(1);
                }
            }
        }
        if (Configuration.getInstance().isDebugTileProviders()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MapTileProviderBase.mapTileRequestFailed(): ");
            stringBuilder.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
            Log.d("OsmDroid", stringBuilder.toString());
        }
    }

    public void mapTileRequestExpiredTile(MapTileRequestState mapTileRequestState, Drawable drawable) {
        putTileIntoCache(mapTileRequestState.getMapTile(), drawable, ExpirableBitmapDrawable.getState(drawable));
        for (Handler handler : this.mTileRequestCompleteHandlers) {
            if (handler != null) {
                handler.sendEmptyMessage(0);
            }
        }
        if (Configuration.getInstance().isDebugTileProviders()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MapTileProviderBase.mapTileRequestExpiredTile(): ");
            stringBuilder.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
            Log.d("OsmDroid", stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public void putTileIntoCache(long j, Drawable drawable, int i) {
        if (drawable != null) {
            Drawable mapTile = this.mTileCache.getMapTile(j);
            if (mapTile == null || ExpirableBitmapDrawable.getState(mapTile) <= i) {
                ExpirableBitmapDrawable.setState(drawable, i);
                this.mTileCache.putTile(j, drawable);
            }
        }
    }

    public Collection<Handler> getTileRequestCompleteHandlers() {
        return this.mTileRequestCompleteHandlers;
    }

    public void ensureCapacity(int i) {
        this.mTileCache.ensureCapacity(i);
    }

    public MapTileCache getTileCache() {
        return this.mTileCache;
    }

    public void clearTileCache() {
        this.mTileCache.clear();
    }

    public boolean useDataConnection() {
        return this.mUseDataConnection;
    }

    public void setUseDataConnection(boolean z) {
        this.mUseDataConnection = z;
    }

    public void rescaleCache(Projection projection, double d, double d2, Rect rect) {
        Projection projection2 = projection;
        double d3 = d;
        double d4 = d2;
        Rect rect2 = rect;
        if (TileSystem.getInputTileZoomLevel(d) != TileSystem.getInputTileZoomLevel(d2)) {
            long currentTimeMillis = System.currentTimeMillis();
            String str = "OsmDroid";
            if (Configuration.getInstance().isDebugTileProviders()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("rescale tile cache from ");
                stringBuilder.append(d4);
                stringBuilder.append(" to ");
                stringBuilder.append(d3);
                Log.i(str, stringBuilder.toString());
            }
            PointL toMercatorPixels = projection2.toMercatorPixels(rect2.left, rect2.top, null);
            PointL toMercatorPixels2 = projection2.toMercatorPixels(rect2.right, rect2.bottom, null);
            long j = currentTimeMillis;
            RectL rectL = new RectL(toMercatorPixels.f44x, toMercatorPixels.f45y, toMercatorPixels2.f44x, toMercatorPixels2.f45y);
            (d3 > d4 ? new ZoomInTileLooper() : new ZoomOutTileLooper()).loop(d, rectL, d2, getTileSource().getTileSizePixels());
            long currentTimeMillis2 = System.currentTimeMillis();
            if (Configuration.getInstance().isDebugTileProviders()) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Finished rescale in ");
                stringBuilder2.append(currentTimeMillis2 - j);
                stringBuilder2.append("ms");
                Log.i(str, stringBuilder2.toString());
            }
        }
    }
}

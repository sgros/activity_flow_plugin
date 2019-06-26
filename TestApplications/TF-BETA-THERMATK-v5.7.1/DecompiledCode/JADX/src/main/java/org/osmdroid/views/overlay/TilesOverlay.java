package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.ReusableBitmapDrawable;
import org.osmdroid.tileprovider.TileStates;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.RectL;
import org.osmdroid.util.TileLooper;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

public class TilesOverlay extends Overlay implements IOverlayMenuProvider {
    public static final ColorFilter INVERT_COLORS = new ColorMatrixColorFilter(negate);
    public static final int MENU_MAP_MODE = Overlay.getSafeMenuId();
    public static final int MENU_OFFLINE = Overlay.getSafeMenuId();
    public static final int MENU_SNAPSHOT = Overlay.getSafeMenuId();
    public static final int MENU_STATES = Overlay.getSafeMenuId();
    public static final int MENU_TILE_SOURCE_STARTING_ID = Overlay.getSafeMenuIdSequence(TileSourceFactory.getTileSources().size());
    static final float[] negate = new float[]{-1.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, -1.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, -1.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    private Context ctx;
    private ColorFilter currentColorFilter = null;
    private boolean horizontalWrapEnabled = true;
    private Rect mCanvasRect;
    protected final Paint mDebugPaint = new Paint();
    private final Rect mIntersectionRect = new Rect();
    private int mLoadingBackgroundColor = Color.rgb(216, 208, 208);
    private int mLoadingLineColor = Color.rgb(Callback.DEFAULT_DRAG_ANIMATION_DURATION, 192, 192);
    private BitmapDrawable mLoadingTile = null;
    private boolean mOptionsMenuEnabled = true;
    protected Projection mProjection;
    private final Rect mProtectedTiles = new Rect();
    private final OverlayTileLooper mTileLooper = new OverlayTileLooper();
    protected final MapTileProviderBase mTileProvider;
    private final Rect mTileRect = new Rect();
    private final TileStates mTileStates = new TileStates();
    protected final RectL mViewPort = new RectL();
    protected Drawable userSelectedLoadingDrawable = null;
    private boolean verticalWrapEnabled = true;

    protected class OverlayTileLooper extends TileLooper {
        private Canvas mCanvas;

        public void loop(double d, RectL rectL, Canvas canvas) {
            this.mCanvas = canvas;
            loop(d, rectL);
        }

        public void initialiseLoop() {
            Rect rect = this.mTiles;
            TilesOverlay.this.mTileProvider.ensureCapacity((((rect.bottom - rect.top) + 1) * ((rect.right - rect.left) + 1)) + Configuration.getInstance().getCacheMapTileOvershoot());
            TilesOverlay.this.mTileStates.initialiseLoop();
            super.initialiseLoop();
        }

        public void handleTile(long j, int i, int i2) {
            int i3 = i;
            int i4 = i2;
            Drawable mapTile = TilesOverlay.this.mTileProvider.getMapTile(j);
            TilesOverlay.this.mTileStates.handleTile(mapTile);
            if (this.mCanvas != null) {
                boolean z = mapTile instanceof ReusableBitmapDrawable;
                ReusableBitmapDrawable reusableBitmapDrawable = z ? (ReusableBitmapDrawable) mapTile : null;
                if (mapTile == null) {
                    mapTile = TilesOverlay.this.getLoadingTile();
                }
                if (mapTile != null) {
                    TilesOverlay tilesOverlay = TilesOverlay.this;
                    tilesOverlay.mProjection.getPixelFromTile(i3, i4, tilesOverlay.mTileRect);
                    if (z) {
                        reusableBitmapDrawable.beginUsingDrawable();
                    }
                    if (z) {
                        try {
                            if (!reusableBitmapDrawable.isBitmapValid()) {
                                mapTile = TilesOverlay.this.getLoadingTile();
                                z = false;
                            }
                        } catch (Throwable th) {
                            if (z) {
                                reusableBitmapDrawable.finishUsingDrawable();
                            }
                        }
                    }
                    TilesOverlay.this.onTileReadyToDraw(this.mCanvas, mapTile, TilesOverlay.this.mTileRect);
                    if (z) {
                        reusableBitmapDrawable.finishUsingDrawable();
                    }
                }
                if (Configuration.getInstance().isDebugTileProviders()) {
                    TilesOverlay tilesOverlay2 = TilesOverlay.this;
                    tilesOverlay2.mProjection.getPixelFromTile(i3, i4, tilesOverlay2.mTileRect);
                    this.mCanvas.drawText(MapTileIndex.toString(j), (float) (TilesOverlay.this.mTileRect.left + 1), ((float) TilesOverlay.this.mTileRect.top) + TilesOverlay.this.mDebugPaint.getTextSize(), TilesOverlay.this.mDebugPaint);
                    this.mCanvas.drawLine((float) TilesOverlay.this.mTileRect.left, (float) TilesOverlay.this.mTileRect.top, (float) TilesOverlay.this.mTileRect.right, (float) TilesOverlay.this.mTileRect.top, TilesOverlay.this.mDebugPaint);
                    this.mCanvas.drawLine((float) TilesOverlay.this.mTileRect.left, (float) TilesOverlay.this.mTileRect.top, (float) TilesOverlay.this.mTileRect.left, (float) TilesOverlay.this.mTileRect.bottom, TilesOverlay.this.mDebugPaint);
                }
            }
        }

        public void finaliseLoop() {
            TilesOverlay.this.mTileStates.finaliseLoop();
        }
    }

    public TilesOverlay(MapTileProviderBase mapTileProviderBase, Context context, boolean z, boolean z2) {
        this.ctx = context;
        if (mapTileProviderBase != null) {
            this.mTileProvider = mapTileProviderBase;
            setHorizontalWrapEnabled(z);
            setVerticalWrapEnabled(z2);
            return;
        }
        throw new IllegalArgumentException("You must pass a valid tile provider to the tiles overlay.");
    }

    public void onDetach(MapView mapView) {
        this.mTileProvider.detach();
        this.ctx = null;
        BitmapPool.getInstance().asyncRecycle(this.mLoadingTile);
        this.mLoadingTile = null;
        BitmapPool.getInstance().asyncRecycle(this.userSelectedLoadingDrawable);
        this.userSelectedLoadingDrawable = null;
    }

    public int getMinimumZoomLevel() {
        return this.mTileProvider.getMinimumZoomLevel();
    }

    public int getMaximumZoomLevel() {
        return this.mTileProvider.getMaximumZoomLevel();
    }

    public boolean useDataConnection() {
        return this.mTileProvider.useDataConnection();
    }

    public void setUseDataConnection(boolean z) {
        this.mTileProvider.setUseDataConnection(z);
    }

    public void protectDisplayedTilesForCache(Canvas canvas, Projection projection) {
        if (setViewPort(canvas, projection)) {
            TileSystem.getTileFromMercator(this.mViewPort, TileSystem.getTileSize(this.mProjection.getZoomLevel()), this.mProtectedTiles);
            this.mTileProvider.getTileCache().getMapTileArea().set(TileSystem.getInputTileZoomLevel(this.mProjection.getZoomLevel()), this.mProtectedTiles);
            this.mTileProvider.getTileCache().maintenance();
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean setViewPort(Canvas canvas, Projection projection) {
        setProjection(projection);
        getProjection().getMercatorViewPort(this.mViewPort);
        return true;
    }

    public void draw(Canvas canvas, Projection projection) {
        if (Configuration.getInstance().isDebugTileProviders()) {
            Log.d("OsmDroid", "onDraw");
        }
        if (setViewPort(canvas, projection)) {
            drawTiles(canvas, getProjection(), getProjection().getZoomLevel(), this.mViewPort);
        }
    }

    public void drawTiles(Canvas canvas, Projection projection, double d, RectL rectL) {
        this.mProjection = projection;
        this.mTileLooper.loop(d, rectL, canvas);
    }

    /* Access modifiers changed, original: protected */
    public Rect getCanvasRect() {
        return this.mCanvasRect;
    }

    /* Access modifiers changed, original: protected */
    public void setProjection(Projection projection) {
        this.mProjection = projection;
    }

    /* Access modifiers changed, original: protected */
    public Projection getProjection() {
        return this.mProjection;
    }

    /* Access modifiers changed, original: protected */
    public void onTileReadyToDraw(Canvas canvas, Drawable drawable, Rect rect) {
        drawable.setColorFilter(this.currentColorFilter);
        drawable.setBounds(rect.left, rect.top, rect.right, rect.bottom);
        rect = getCanvasRect();
        if (rect == null) {
            drawable.draw(canvas);
        } else if (this.mIntersectionRect.setIntersect(canvas.getClipBounds(), rect)) {
            canvas.save();
            canvas.clipRect(this.mIntersectionRect);
            drawable.draw(canvas);
            canvas.restore();
        }
    }

    public void setLoadingBackgroundColor(int i) {
        if (this.mLoadingBackgroundColor != i) {
            this.mLoadingBackgroundColor = i;
            clearLoadingTile();
        }
    }

    private Drawable getLoadingTile() {
        String str = "OsmDroid";
        Drawable drawable = this.userSelectedLoadingDrawable;
        if (drawable != null) {
            return drawable;
        }
        if (this.mLoadingTile == null && this.mLoadingBackgroundColor != 0) {
            try {
                int tileSizePixels = this.mTileProvider.getTileSource() != null ? this.mTileProvider.getTileSource().getTileSizePixels() : 256;
                Bitmap createBitmap = Bitmap.createBitmap(tileSizePixels, tileSizePixels, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                Paint paint = new Paint();
                canvas.drawColor(this.mLoadingBackgroundColor);
                paint.setColor(this.mLoadingLineColor);
                paint.setStrokeWidth(0.0f);
                int i = tileSizePixels / 16;
                for (int i2 = 0; i2 < tileSizePixels; i2 += i) {
                    float f = (float) i2;
                    float f2 = (float) tileSizePixels;
                    canvas.drawLine(0.0f, f, f2, f, paint);
                    canvas.drawLine(f, 0.0f, f, f2, paint);
                }
                this.mLoadingTile = new BitmapDrawable(createBitmap);
            } catch (OutOfMemoryError unused) {
                Log.e(str, "OutOfMemoryError getting loading tile");
                System.gc();
            } catch (NullPointerException unused2) {
                Log.e(str, "NullPointerException getting loading tile");
                System.gc();
            }
        }
        return this.mLoadingTile;
    }

    private void clearLoadingTile() {
        BitmapDrawable bitmapDrawable = this.mLoadingTile;
        this.mLoadingTile = null;
        BitmapPool.getInstance().asyncRecycle(bitmapDrawable);
    }

    public void setHorizontalWrapEnabled(boolean z) {
        this.horizontalWrapEnabled = z;
        this.mTileLooper.setHorizontalWrapEnabled(z);
    }

    public void setVerticalWrapEnabled(boolean z) {
        this.verticalWrapEnabled = z;
        this.mTileLooper.setVerticalWrapEnabled(z);
    }
}

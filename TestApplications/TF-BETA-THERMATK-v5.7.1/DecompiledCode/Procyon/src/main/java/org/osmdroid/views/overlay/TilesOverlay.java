// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay;

import org.osmdroid.util.MapTileIndex;
import org.osmdroid.tileprovider.ReusableBitmapDrawable;
import org.osmdroid.util.TileLooper;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.config.Configuration;
import android.util.Log;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Bitmap$Config;
import org.osmdroid.tileprovider.BitmapPool;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import android.graphics.drawable.Drawable;
import org.osmdroid.util.RectL;
import org.osmdroid.tileprovider.TileStates;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.views.Projection;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Paint;
import android.graphics.Rect;
import android.content.Context;
import android.graphics.ColorFilter;

public class TilesOverlay extends Overlay implements IOverlayMenuProvider
{
    public static final ColorFilter INVERT_COLORS;
    public static final int MENU_MAP_MODE;
    public static final int MENU_OFFLINE;
    public static final int MENU_SNAPSHOT;
    public static final int MENU_STATES;
    public static final int MENU_TILE_SOURCE_STARTING_ID;
    static final float[] negate;
    private Context ctx;
    private ColorFilter currentColorFilter;
    private boolean horizontalWrapEnabled;
    private Rect mCanvasRect;
    protected final Paint mDebugPaint;
    private final Rect mIntersectionRect;
    private int mLoadingBackgroundColor;
    private int mLoadingLineColor;
    private BitmapDrawable mLoadingTile;
    private boolean mOptionsMenuEnabled;
    protected Projection mProjection;
    private final Rect mProtectedTiles;
    private final OverlayTileLooper mTileLooper;
    protected final MapTileProviderBase mTileProvider;
    private final Rect mTileRect;
    private final TileStates mTileStates;
    protected final RectL mViewPort;
    protected Drawable userSelectedLoadingDrawable;
    private boolean verticalWrapEnabled;
    
    static {
        MENU_MAP_MODE = Overlay.getSafeMenuId();
        MENU_TILE_SOURCE_STARTING_ID = Overlay.getSafeMenuIdSequence(TileSourceFactory.getTileSources().size());
        MENU_OFFLINE = Overlay.getSafeMenuId();
        MENU_SNAPSHOT = Overlay.getSafeMenuId();
        MENU_STATES = Overlay.getSafeMenuId();
        negate = new float[] { -1.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, -1.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, -1.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f };
        INVERT_COLORS = (ColorFilter)new ColorMatrixColorFilter(TilesOverlay.negate);
    }
    
    public TilesOverlay(final MapTileProviderBase mTileProvider, final Context ctx, final boolean horizontalWrapEnabled, final boolean verticalWrapEnabled) {
        this.userSelectedLoadingDrawable = null;
        this.mDebugPaint = new Paint();
        this.mTileRect = new Rect();
        this.mViewPort = new RectL();
        this.mOptionsMenuEnabled = true;
        this.mLoadingTile = null;
        this.mLoadingBackgroundColor = Color.rgb(216, 208, 208);
        this.mLoadingLineColor = Color.rgb(200, 192, 192);
        this.horizontalWrapEnabled = true;
        this.verticalWrapEnabled = true;
        this.currentColorFilter = null;
        this.mProtectedTiles = new Rect();
        this.mTileStates = new TileStates();
        this.mTileLooper = new OverlayTileLooper();
        this.mIntersectionRect = new Rect();
        this.ctx = ctx;
        if (mTileProvider != null) {
            this.mTileProvider = mTileProvider;
            this.setHorizontalWrapEnabled(horizontalWrapEnabled);
            this.setVerticalWrapEnabled(verticalWrapEnabled);
            return;
        }
        throw new IllegalArgumentException("You must pass a valid tile provider to the tiles overlay.");
    }
    
    private void clearLoadingTile() {
        final BitmapDrawable mLoadingTile = this.mLoadingTile;
        this.mLoadingTile = null;
        BitmapPool.getInstance().asyncRecycle((Drawable)mLoadingTile);
    }
    
    private Drawable getLoadingTile() {
        final Drawable userSelectedLoadingDrawable = this.userSelectedLoadingDrawable;
        if (userSelectedLoadingDrawable != null) {
            return userSelectedLoadingDrawable;
        }
        if (this.mLoadingTile == null && this.mLoadingBackgroundColor != 0) {
            try {
                int tileSizePixels;
                if (this.mTileProvider.getTileSource() != null) {
                    tileSizePixels = this.mTileProvider.getTileSource().getTileSizePixels();
                }
                else {
                    tileSizePixels = 256;
                }
                final Bitmap bitmap = Bitmap.createBitmap(tileSizePixels, tileSizePixels, Bitmap$Config.ARGB_8888);
                final Canvas canvas = new Canvas(bitmap);
                final Paint paint = new Paint();
                canvas.drawColor(this.mLoadingBackgroundColor);
                paint.setColor(this.mLoadingLineColor);
                paint.setStrokeWidth(0.0f);
                for (int n = tileSizePixels / 16, i = 0; i < tileSizePixels; i += n) {
                    final float n2 = (float)i;
                    final float n3 = (float)tileSizePixels;
                    canvas.drawLine(0.0f, n2, n3, n2, paint);
                    canvas.drawLine(n2, 0.0f, n2, n3, paint);
                }
                this.mLoadingTile = new BitmapDrawable(bitmap);
            }
            catch (NullPointerException ex) {
                Log.e("OsmDroid", "NullPointerException getting loading tile");
                System.gc();
            }
            catch (OutOfMemoryError outOfMemoryError) {
                Log.e("OsmDroid", "OutOfMemoryError getting loading tile");
                System.gc();
            }
        }
        return (Drawable)this.mLoadingTile;
    }
    
    @Override
    public void draw(final Canvas canvas, final Projection projection) {
        if (Configuration.getInstance().isDebugTileProviders()) {
            Log.d("OsmDroid", "onDraw");
        }
        if (!this.setViewPort(canvas, projection)) {
            return;
        }
        this.drawTiles(canvas, this.getProjection(), this.getProjection().getZoomLevel(), this.mViewPort);
    }
    
    public void drawTiles(final Canvas canvas, final Projection mProjection, final double n, final RectL rectL) {
        this.mProjection = mProjection;
        this.mTileLooper.loop(n, rectL, canvas);
    }
    
    protected Rect getCanvasRect() {
        return this.mCanvasRect;
    }
    
    public int getMaximumZoomLevel() {
        return this.mTileProvider.getMaximumZoomLevel();
    }
    
    public int getMinimumZoomLevel() {
        return this.mTileProvider.getMinimumZoomLevel();
    }
    
    protected Projection getProjection() {
        return this.mProjection;
    }
    
    @Override
    public void onDetach(final MapView mapView) {
        this.mTileProvider.detach();
        this.ctx = null;
        BitmapPool.getInstance().asyncRecycle((Drawable)this.mLoadingTile);
        this.mLoadingTile = null;
        BitmapPool.getInstance().asyncRecycle(this.userSelectedLoadingDrawable);
        this.userSelectedLoadingDrawable = null;
    }
    
    protected void onTileReadyToDraw(final Canvas canvas, final Drawable drawable, Rect canvasRect) {
        drawable.setColorFilter(this.currentColorFilter);
        drawable.setBounds(canvasRect.left, canvasRect.top, canvasRect.right, canvasRect.bottom);
        canvasRect = this.getCanvasRect();
        if (canvasRect == null) {
            drawable.draw(canvas);
            return;
        }
        if (!this.mIntersectionRect.setIntersect(canvas.getClipBounds(), canvasRect)) {
            return;
        }
        canvas.save();
        canvas.clipRect(this.mIntersectionRect);
        drawable.draw(canvas);
        canvas.restore();
    }
    
    public void protectDisplayedTilesForCache(final Canvas canvas, final Projection projection) {
        if (!this.setViewPort(canvas, projection)) {
            return;
        }
        TileSystem.getTileFromMercator(this.mViewPort, TileSystem.getTileSize(this.mProjection.getZoomLevel()), this.mProtectedTiles);
        this.mTileProvider.getTileCache().getMapTileArea().set(TileSystem.getInputTileZoomLevel(this.mProjection.getZoomLevel()), this.mProtectedTiles);
        this.mTileProvider.getTileCache().maintenance();
    }
    
    public void setHorizontalWrapEnabled(final boolean b) {
        this.horizontalWrapEnabled = b;
        this.mTileLooper.setHorizontalWrapEnabled(b);
    }
    
    public void setLoadingBackgroundColor(final int mLoadingBackgroundColor) {
        if (this.mLoadingBackgroundColor != mLoadingBackgroundColor) {
            this.mLoadingBackgroundColor = mLoadingBackgroundColor;
            this.clearLoadingTile();
        }
    }
    
    protected void setProjection(final Projection mProjection) {
        this.mProjection = mProjection;
    }
    
    public void setUseDataConnection(final boolean useDataConnection) {
        this.mTileProvider.setUseDataConnection(useDataConnection);
    }
    
    public void setVerticalWrapEnabled(final boolean b) {
        this.verticalWrapEnabled = b;
        this.mTileLooper.setVerticalWrapEnabled(b);
    }
    
    protected boolean setViewPort(final Canvas canvas, final Projection projection) {
        this.setProjection(projection);
        this.getProjection().getMercatorViewPort(this.mViewPort);
        return true;
    }
    
    public boolean useDataConnection() {
        return this.mTileProvider.useDataConnection();
    }
    
    protected class OverlayTileLooper extends TileLooper
    {
        private Canvas mCanvas;
        
        public OverlayTileLooper() {
        }
        
        @Override
        public void finaliseLoop() {
            TilesOverlay.this.mTileStates.finaliseLoop();
        }
        
        @Override
        public void handleTile(final long n, final int n2, final int n3) {
            final Drawable mapTile = TilesOverlay.this.mTileProvider.getMapTile(n);
            TilesOverlay.this.mTileStates.handleTile(mapTile);
            if (this.mCanvas == null) {
                return;
            }
            final boolean b = mapTile instanceof ReusableBitmapDrawable;
            ReusableBitmapDrawable reusableBitmapDrawable;
            if (b) {
                reusableBitmapDrawable = (ReusableBitmapDrawable)mapTile;
            }
            else {
                reusableBitmapDrawable = null;
            }
            Drawable access$100 = mapTile;
            if (mapTile == null) {
                access$100 = TilesOverlay.this.getLoadingTile();
            }
            if (access$100 != null) {
                final TilesOverlay this$0 = TilesOverlay.this;
                this$0.mProjection.getPixelFromTile(n2, n3, this$0.mTileRect);
                if (b) {
                    reusableBitmapDrawable.beginUsingDrawable();
                }
                Drawable access$101 = access$100;
                boolean b2 = b;
                Label_0163: {
                    if (!b) {
                        break Label_0163;
                    }
                    access$101 = access$100;
                    b2 = b;
                    boolean b3 = b;
                    try {
                        if (!reusableBitmapDrawable.isBitmapValid()) {
                            b3 = b;
                            access$101 = TilesOverlay.this.getLoadingTile();
                            b2 = false;
                        }
                        b3 = b2;
                        TilesOverlay.this.onTileReadyToDraw(this.mCanvas, access$101, TilesOverlay.this.mTileRect);
                    }
                    finally {
                        if (b3) {
                            reusableBitmapDrawable.finishUsingDrawable();
                        }
                    }
                }
            }
            if (Configuration.getInstance().isDebugTileProviders()) {
                final TilesOverlay this$2 = TilesOverlay.this;
                this$2.mProjection.getPixelFromTile(n2, n3, this$2.mTileRect);
                this.mCanvas.drawText(MapTileIndex.toString(n), (float)(TilesOverlay.this.mTileRect.left + 1), TilesOverlay.this.mTileRect.top + TilesOverlay.this.mDebugPaint.getTextSize(), TilesOverlay.this.mDebugPaint);
                this.mCanvas.drawLine((float)TilesOverlay.this.mTileRect.left, (float)TilesOverlay.this.mTileRect.top, (float)TilesOverlay.this.mTileRect.right, (float)TilesOverlay.this.mTileRect.top, TilesOverlay.this.mDebugPaint);
                this.mCanvas.drawLine((float)TilesOverlay.this.mTileRect.left, (float)TilesOverlay.this.mTileRect.top, (float)TilesOverlay.this.mTileRect.left, (float)TilesOverlay.this.mTileRect.bottom, TilesOverlay.this.mDebugPaint);
            }
        }
        
        @Override
        public void initialiseLoop() {
            final Rect mTiles = super.mTiles;
            TilesOverlay.this.mTileProvider.ensureCapacity((mTiles.bottom - mTiles.top + 1) * (mTiles.right - mTiles.left + 1) + Configuration.getInstance().getCacheMapTileOvershoot());
            TilesOverlay.this.mTileStates.initialiseLoop();
            super.initialiseLoop();
        }
        
        public void loop(final double n, final RectL rectL, final Canvas mCanvas) {
            this.mCanvas = mCanvas;
            this.loop(n, rectL);
        }
    }
}

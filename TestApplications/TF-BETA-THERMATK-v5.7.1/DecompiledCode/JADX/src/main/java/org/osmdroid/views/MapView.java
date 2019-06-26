package org.osmdroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.metalev.multitouch.controller.MultiTouchController;
import org.metalev.multitouch.controller.MultiTouchController.MultiTouchObjectCanvas;
import org.metalev.multitouch.controller.MultiTouchController.PointInfo;
import org.metalev.multitouch.controller.MultiTouchController.PositionAndScale;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.IStyledTileSource;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.GeometryMath;
import org.osmdroid.util.TileSystem;
import org.osmdroid.util.TileSystemWebMercator;
import org.osmdroid.views.CustomZoomButtonsController.OnZoomListener;
import org.osmdroid.views.CustomZoomButtonsController.Visibility;
import org.osmdroid.views.overlay.DefaultOverlayManager;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.TilesOverlay;

public class MapView extends ViewGroup implements IMapView, MultiTouchObjectCanvas<Object> {
    private static TileSystem mTileSystem = new TileSystemWebMercator();
    private boolean enableFling;
    private boolean horizontalMapRepetitionEnabled;
    private GeoPoint mCenter;
    private final MapController mController;
    private boolean mDestroyModeOnDetach;
    private final GestureDetector mGestureDetector;
    private boolean mImpossibleFlinging;
    private final Rect mInvalidateRect;
    protected final AtomicBoolean mIsAnimating;
    protected boolean mIsFlinging;
    private boolean mLayoutOccurred;
    private final Point mLayoutPoint;
    protected List<MapListener> mListners;
    private TilesOverlay mMapOverlay;
    private long mMapScrollX;
    private long mMapScrollY;
    protected Double mMaximumZoomLevel;
    protected Double mMinimumZoomLevel;
    private MultiTouchController<Object> mMultiTouchController;
    private PointF mMultiTouchScaleCurrentPoint;
    private final GeoPoint mMultiTouchScaleGeoPoint;
    private final PointF mMultiTouchScaleInitPoint;
    private final LinkedList<OnFirstLayoutListener> mOnFirstLayoutListeners;
    private OverlayManager mOverlayManager;
    protected Projection mProjection;
    private final MapViewRepository mRepository;
    private final Rect mRescaleScreenRect;
    final Point mRotateScalePoint;
    private double mScrollableAreaLimitEast;
    private int mScrollableAreaLimitExtraPixelHeight;
    private int mScrollableAreaLimitExtraPixelWidth;
    private boolean mScrollableAreaLimitLatitude;
    private boolean mScrollableAreaLimitLongitude;
    private double mScrollableAreaLimitNorth;
    private double mScrollableAreaLimitSouth;
    private double mScrollableAreaLimitWest;
    private final Scroller mScroller;
    private double mStartAnimationZoom;
    private MapTileProviderBase mTileProvider;
    private Handler mTileRequestCompleteHandler;
    private float mTilesScaleFactor;
    private boolean mTilesScaledToDpi;
    private final CustomZoomButtonsController mZoomController;
    private double mZoomLevel;
    private boolean mZoomRounding;
    private float mapOrientation;
    private boolean pauseFling;
    private boolean verticalMapRepetitionEnabled;

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public int alignment;
        public IGeoPoint geoPoint;
        public int offsetX;
        public int offsetY;

        public LayoutParams(int i, int i2, IGeoPoint iGeoPoint, int i3, int i4, int i5) {
            super(i, i2);
            if (iGeoPoint != null) {
                this.geoPoint = iGeoPoint;
            } else {
                this.geoPoint = new GeoPoint(0.0d, 0.0d);
            }
            this.alignment = i3;
            this.offsetX = i4;
            this.offsetY = i5;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.geoPoint = new GeoPoint(0.0d, 0.0d);
            this.alignment = 8;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    private class MapViewDoubleClickListener implements OnDoubleTapListener {
        private MapViewDoubleClickListener() {
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (MapView.this.getOverlayManager().onDoubleTap(motionEvent, MapView.this)) {
                return true;
            }
            if (MapView.this.mZoomController != null && MapView.this.mZoomController.onSingleTapConfirmed(motionEvent)) {
                return true;
            }
            MapView.this.getProjection().rotateAndScalePoint((int) motionEvent.getX(), (int) motionEvent.getY(), MapView.this.mRotateScalePoint);
            IMapController controller = MapView.this.getController();
            Point point = MapView.this.mRotateScalePoint;
            return controller.zoomInFixing(point.x, point.y);
        }

        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            return MapView.this.getOverlayManager().onDoubleTapEvent(motionEvent, MapView.this);
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if ((MapView.this.mZoomController == null || !MapView.this.mZoomController.onSingleTapConfirmed(motionEvent)) && !MapView.this.getOverlayManager().onSingleTapConfirmed(motionEvent, MapView.this)) {
                return false;
            }
            return true;
        }
    }

    private class MapViewGestureDetectorListener implements OnGestureListener {
        private MapViewGestureDetectorListener() {
        }

        public boolean onDown(MotionEvent motionEvent) {
            MapView mapView = MapView.this;
            if (mapView.mIsFlinging) {
                if (mapView.mScroller != null) {
                    MapView.this.mScroller.abortAnimation();
                }
                MapView.this.mIsFlinging = false;
            }
            if (!(MapView.this.getOverlayManager().onDown(motionEvent, MapView.this) || MapView.this.mZoomController == null)) {
                MapView.this.mZoomController.activate();
            }
            return true;
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!MapView.this.enableFling || MapView.this.pauseFling) {
                MapView.this.pauseFling = false;
                return false;
            }
            if (MapView.this.getOverlayManager().onFling(motionEvent, motionEvent2, f, f2, MapView.this)) {
                return true;
            }
            if (MapView.this.mImpossibleFlinging) {
                MapView.this.mImpossibleFlinging = false;
                return false;
            }
            MapView mapView = MapView.this;
            mapView.mIsFlinging = true;
            if (mapView.mScroller != null) {
                MapView.this.mScroller.fling((int) MapView.this.getMapScrollX(), (int) MapView.this.getMapScrollY(), (int) (-f), (int) (-f2), Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
            return true;
        }

        public void onLongPress(MotionEvent motionEvent) {
            if (MapView.this.mMultiTouchController != null && MapView.this.mMultiTouchController.isPinching()) {
                return;
            }
            if (MapView.this.mZoomController == null || !MapView.this.mZoomController.onLongPress(motionEvent)) {
                MapView.this.getOverlayManager().onLongPress(motionEvent, MapView.this);
            }
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (MapView.this.getOverlayManager().onScroll(motionEvent, motionEvent2, f, f2, MapView.this)) {
                return true;
            }
            MapView.this.scrollBy((int) f, (int) f2);
            return true;
        }

        public void onShowPress(MotionEvent motionEvent) {
            MapView.this.getOverlayManager().onShowPress(motionEvent, MapView.this);
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return MapView.this.getOverlayManager().onSingleTapUp(motionEvent, MapView.this);
        }
    }

    public interface OnFirstLayoutListener {
        void onFirstLayout(View view, int i, int i2, int i3, int i4);
    }

    private class MapViewZoomListener implements OnZoomListener {
        private MapViewZoomListener() {
        }

        public void onZoom(boolean z) {
            if (z) {
                MapView.this.getController().zoomIn();
            } else {
                MapView.this.getController().zoomOut();
            }
        }
    }

    @Deprecated
    public float getMapScale() {
        return 1.0f;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    public MapView(Context context, MapTileProviderBase mapTileProviderBase, Handler handler, AttributeSet attributeSet) {
        this(context, mapTileProviderBase, handler, attributeSet, Configuration.getInstance().isMapViewHardwareAccelerated());
    }

    public MapView(Context context, MapTileProviderBase mapTileProviderBase, Handler handler, AttributeSet attributeSet, boolean z) {
        super(context, attributeSet);
        this.mZoomLevel = 0.0d;
        this.mIsAnimating = new AtomicBoolean(false);
        this.mMultiTouchScaleInitPoint = new PointF();
        this.mMultiTouchScaleGeoPoint = new GeoPoint(0.0d, 0.0d);
        this.mapOrientation = 0.0f;
        this.mInvalidateRect = new Rect();
        this.mTilesScaledToDpi = false;
        this.mTilesScaleFactor = 1.0f;
        this.mRotateScalePoint = new Point();
        this.mLayoutPoint = new Point();
        this.mOnFirstLayoutListeners = new LinkedList();
        this.mLayoutOccurred = false;
        this.horizontalMapRepetitionEnabled = true;
        this.verticalMapRepetitionEnabled = true;
        this.mListners = new ArrayList();
        this.mRepository = new MapViewRepository(this);
        this.mRescaleScreenRect = new Rect();
        this.mDestroyModeOnDetach = true;
        this.enableFling = true;
        this.pauseFling = false;
        setWillNotDraw(false);
        if (isInEditMode()) {
            this.mTileRequestCompleteHandler = null;
            this.mController = null;
            this.mZoomController = null;
            this.mScroller = null;
            this.mGestureDetector = null;
            return;
        }
        if (!z && VERSION.SDK_INT >= 11) {
            setLayerType(1, null);
        }
        this.mController = new MapController(this);
        this.mScroller = new Scroller(context);
        if (mapTileProviderBase == null) {
            MapTileProviderBase mapTileProviderArray;
            ITileSource tileSourceFromAttributes = getTileSourceFromAttributes(attributeSet);
            if (isInEditMode()) {
                mapTileProviderArray = new MapTileProviderArray(tileSourceFromAttributes, null, new MapTileModuleProviderBase[0]);
            } else {
                mapTileProviderArray = new MapTileProviderBasic(context.getApplicationContext(), tileSourceFromAttributes);
            }
            mapTileProviderBase = mapTileProviderArray;
        }
        if (handler == null) {
            handler = new SimpleInvalidationHandler(this);
        }
        this.mTileRequestCompleteHandler = handler;
        this.mTileProvider = mapTileProviderBase;
        this.mTileProvider.getTileRequestCompleteHandlers().add(this.mTileRequestCompleteHandler);
        updateTileSizeForDensity(this.mTileProvider.getTileSource());
        this.mMapOverlay = new TilesOverlay(this.mTileProvider, context, this.horizontalMapRepetitionEnabled, this.verticalMapRepetitionEnabled);
        this.mOverlayManager = new DefaultOverlayManager(this.mMapOverlay);
        this.mZoomController = new CustomZoomButtonsController(this);
        this.mZoomController.setOnZoomListener(new MapViewZoomListener());
        checkZoomButtons();
        this.mGestureDetector = new GestureDetector(context, new MapViewGestureDetectorListener());
        this.mGestureDetector.setOnDoubleTapListener(new MapViewDoubleClickListener());
        if (Configuration.getInstance().isMapViewRecyclerFriendly() && VERSION.SDK_INT >= 16) {
            setHasTransientState(true);
        }
        this.mZoomController.setVisibility(Visibility.SHOW_AND_FADEOUT);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        this(context, null, null, attributeSet);
    }

    public MapView(Context context) {
        this(context, null, null, null);
    }

    public MapView(Context context, MapTileProviderBase mapTileProviderBase) {
        this(context, mapTileProviderBase, null);
    }

    public MapView(Context context, MapTileProviderBase mapTileProviderBase, Handler handler) {
        this(context, mapTileProviderBase, handler, null);
    }

    public IMapController getController() {
        return this.mController;
    }

    public List<Overlay> getOverlays() {
        return getOverlayManager().overlays();
    }

    public OverlayManager getOverlayManager() {
        return this.mOverlayManager;
    }

    public void setOverlayManager(OverlayManager overlayManager) {
        this.mOverlayManager = overlayManager;
    }

    public MapTileProviderBase getTileProvider() {
        return this.mTileProvider;
    }

    public Scroller getScroller() {
        return this.mScroller;
    }

    public Handler getTileRequestCompleteHandler() {
        return this.mTileRequestCompleteHandler;
    }

    public double getLatitudeSpanDouble() {
        return getBoundingBox().getLatitudeSpan();
    }

    public double getLongitudeSpanDouble() {
        return getBoundingBox().getLongitudeSpan();
    }

    public BoundingBox getBoundingBox() {
        return getProjection().getBoundingBox();
    }

    public Rect getScreenRect(Rect rect) {
        rect = getIntrinsicScreenRect(rect);
        if (!(getMapOrientation() == 0.0f || getMapOrientation() == 180.0f)) {
            GeometryMath.getBoundingBoxForRotatatedRectangle(rect, rect.centerX(), rect.centerY(), getMapOrientation(), rect);
        }
        return rect;
    }

    public Rect getIntrinsicScreenRect(Rect rect) {
        if (rect == null) {
            rect = new Rect();
        }
        rect.set(0, 0, getWidth(), getHeight());
        return rect;
    }

    public Projection getProjection() {
        if (this.mProjection == null) {
            this.mProjection = new Projection(this);
            this.mProjection.adjustOffsets(this.mMultiTouchScaleGeoPoint, this.mMultiTouchScaleCurrentPoint);
            if (this.mScrollableAreaLimitLatitude) {
                this.mProjection.adjustOffsets(this.mScrollableAreaLimitNorth, this.mScrollableAreaLimitSouth, true, this.mScrollableAreaLimitExtraPixelHeight);
            }
            if (this.mScrollableAreaLimitLongitude) {
                this.mProjection.adjustOffsets(this.mScrollableAreaLimitWest, this.mScrollableAreaLimitEast, false, this.mScrollableAreaLimitExtraPixelWidth);
            }
            this.mImpossibleFlinging = this.mProjection.setMapScroll(this);
        }
        return this.mProjection;
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public void setProjection(Projection projection) {
        this.mProjection = projection;
    }

    private void resetProjection() {
        this.mProjection = null;
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public void setMapCenter(IGeoPoint iGeoPoint) {
        getController().animateTo(iGeoPoint);
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public void setMapCenter(int i, int i2) {
        setMapCenter(new GeoPoint(i, i2));
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public void setMapCenter(double d, double d2) {
        setMapCenter(new GeoPoint(d, d2));
    }

    public boolean isTilesScaledToDpi() {
        return this.mTilesScaledToDpi;
    }

    public void setTilesScaledToDpi(boolean z) {
        this.mTilesScaledToDpi = z;
        updateTileSizeForDensity(getTileProvider().getTileSource());
    }

    public float getTilesScaleFactor() {
        return this.mTilesScaleFactor;
    }

    public void setTilesScaleFactor(float f) {
        this.mTilesScaleFactor = f;
        updateTileSizeForDensity(getTileProvider().getTileSource());
    }

    public void resetTilesScaleFactor() {
        this.mTilesScaleFactor = 1.0f;
        updateTileSizeForDensity(getTileProvider().getTileSource());
    }

    private void updateTileSizeForDensity(ITileSource iTileSource) {
        float tileSizePixels = (float) iTileSource.getTileSizePixels();
        int i = (int) (tileSizePixels * (isTilesScaledToDpi() ? ((getResources().getDisplayMetrics().density * 256.0f) / tileSizePixels) * this.mTilesScaleFactor : this.mTilesScaleFactor));
        if (Configuration.getInstance().isDebugMapView()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Scaling tiles to ");
            stringBuilder.append(i);
            Log.d("OsmDroid", stringBuilder.toString());
        }
        TileSystem.setTileSize(i);
    }

    public void setTileSource(ITileSource iTileSource) {
        this.mTileProvider.setTileSource(iTileSource);
        updateTileSizeForDensity(iTileSource);
        checkZoomButtons();
        setZoomLevel(this.mZoomLevel);
        postInvalidate();
    }

    /* Access modifiers changed, original: 0000 */
    public double setZoomLevel(double d) {
        d = Math.max(getMinZoomLevel(), Math.min(getMaxZoomLevel(), d));
        double d2 = this.mZoomLevel;
        if (d != d2) {
            Scroller scroller = this.mScroller;
            if (scroller != null) {
                scroller.forceFinished(true);
            }
            this.mIsFlinging = false;
        }
        GeoPoint currentCenter = getProjection().getCurrentCenter();
        this.mZoomLevel = d;
        setExpectedCenter(currentCenter);
        checkZoomButtons();
        GeoPoint geoPoint = null;
        if (isLayoutOccurred()) {
            getController().setCenter(currentCenter);
            Point point = new Point();
            Projection projection = getProjection();
            OverlayManager overlayManager = getOverlayManager();
            PointF pointF = this.mMultiTouchScaleInitPoint;
            if (overlayManager.onSnapToItem((int) pointF.x, (int) pointF.y, point, this)) {
                getController().animateTo(projection.fromPixels(point.x, point.y, null, false));
            }
            this.mTileProvider.rescaleCache(projection, d, d2, getScreenRect(this.mRescaleScreenRect));
            this.pauseFling = true;
        }
        if (d != d2) {
            for (MapListener mapListener : this.mListners) {
                if (geoPoint == null) {
                    geoPoint = new ZoomEvent(this, d);
                }
                mapListener.onZoom(geoPoint);
            }
        }
        requestLayout();
        invalidate();
        return this.mZoomLevel;
    }

    public void zoomToBoundingBox(BoundingBox boundingBox, boolean z) {
        zoomToBoundingBox(boundingBox, z, 0);
    }

    public double zoomToBoundingBox(BoundingBox boundingBox, boolean z, int i, double d, Long l) {
        int i2 = i * 2;
        int width = getWidth() - i2;
        int height = getHeight() - i2;
        BoundingBox boundingBox2 = boundingBox;
        double boundingBoxZoom = mTileSystem.getBoundingBoxZoom(boundingBox, width, height);
        if (boundingBoxZoom == Double.MIN_VALUE || boundingBoxZoom > d) {
            boundingBoxZoom = d;
        }
        boundingBoxZoom = Math.min(getMaxZoomLevel(), Math.max(boundingBoxZoom, getMinZoomLevel()));
        GeoPoint centerWithDateLine = boundingBox.getCenterWithDateLine();
        Projection projection = new Projection(boundingBoxZoom, getWidth(), getHeight(), centerWithDateLine, getMapOrientation(), isHorizontalMapRepetitionEnabled(), isVerticalMapRepetitionEnabled());
        Point point = new Point();
        double centerLongitude = boundingBox.getCenterLongitude();
        projection.toPixels(new GeoPoint(boundingBox.getActualNorth(), centerLongitude), point);
        int i3 = point.y;
        projection.toPixels(new GeoPoint(boundingBox.getActualSouth(), centerLongitude), point);
        int height2 = ((getHeight() - point.y) - i3) / 2;
        if (height2 != 0) {
            projection.adjustOffsets(0, (long) height2);
            projection.fromPixels(getWidth() / 2, getHeight() / 2, centerWithDateLine);
        }
        if (z) {
            getController().animateTo(centerWithDateLine, Double.valueOf(boundingBoxZoom), l);
        } else {
            getController().setZoom(boundingBoxZoom);
            getController().setCenter(centerWithDateLine);
        }
        return boundingBoxZoom;
    }

    public void zoomToBoundingBox(BoundingBox boundingBox, boolean z, int i) {
        zoomToBoundingBox(boundingBox, z, i, getMaxZoomLevel(), null);
    }

    @Deprecated
    public int getZoomLevel() {
        return (int) getZoomLevelDouble();
    }

    public double getZoomLevelDouble() {
        return this.mZoomLevel;
    }

    @Deprecated
    public double getZoomLevel(boolean z) {
        return getZoomLevelDouble();
    }

    public double getMinZoomLevel() {
        Double d = this.mMinimumZoomLevel;
        return d == null ? (double) this.mMapOverlay.getMinimumZoomLevel() : d.doubleValue();
    }

    public double getMaxZoomLevel() {
        Double d = this.mMaximumZoomLevel;
        return d == null ? (double) this.mMapOverlay.getMaximumZoomLevel() : d.doubleValue();
    }

    public void setMinZoomLevel(Double d) {
        this.mMinimumZoomLevel = d;
    }

    public void setMaxZoomLevel(Double d) {
        this.mMaximumZoomLevel = d;
    }

    public boolean canZoomIn() {
        return this.mZoomLevel < getMaxZoomLevel();
    }

    public boolean canZoomOut() {
        return this.mZoomLevel > getMinZoomLevel();
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public boolean zoomIn() {
        return getController().zoomIn();
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public boolean zoomInFixing(IGeoPoint iGeoPoint) {
        Point toPixels = getProjection().toPixels(iGeoPoint, null);
        return getController().zoomInFixing(toPixels.x, toPixels.y);
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public boolean zoomInFixing(int i, int i2) {
        return getController().zoomInFixing(i, i2);
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public boolean zoomOut() {
        return getController().zoomOut();
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public boolean zoomOutFixing(IGeoPoint iGeoPoint) {
        Point toPixels = getProjection().toPixels(iGeoPoint, null);
        return zoomOutFixing(toPixels.x, toPixels.y);
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public boolean zoomOutFixing(int i, int i2) {
        return getController().zoomOutFixing(i, i2);
    }

    public IGeoPoint getMapCenter() {
        return getMapCenter(null);
    }

    public IGeoPoint getMapCenter(GeoPoint geoPoint) {
        return getProjection().fromPixels(getWidth() / 2, getHeight() / 2, geoPoint, false);
    }

    public void setMapOrientation(float f) {
        setMapOrientation(f, true);
    }

    public void setMapOrientation(float f, boolean z) {
        this.mapOrientation = f % 360.0f;
        if (z) {
            requestLayout();
            invalidate();
        }
    }

    public float getMapOrientation() {
        return this.mapOrientation;
    }

    public boolean useDataConnection() {
        return this.mMapOverlay.useDataConnection();
    }

    public void setUseDataConnection(boolean z) {
        this.mMapOverlay.setUseDataConnection(z);
    }

    public void setScrollableAreaLimitDouble(BoundingBox boundingBox) {
        if (boundingBox == null) {
            resetScrollableAreaLimitLatitude();
            resetScrollableAreaLimitLongitude();
            return;
        }
        setScrollableAreaLimitLatitude(boundingBox.getActualNorth(), boundingBox.getActualSouth(), 0);
        setScrollableAreaLimitLongitude(boundingBox.getLonWest(), boundingBox.getLonEast(), 0);
    }

    public void resetScrollableAreaLimitLatitude() {
        this.mScrollableAreaLimitLatitude = false;
    }

    public void resetScrollableAreaLimitLongitude() {
        this.mScrollableAreaLimitLongitude = false;
    }

    public void setScrollableAreaLimitLatitude(double d, double d2, int i) {
        this.mScrollableAreaLimitLatitude = true;
        this.mScrollableAreaLimitNorth = d;
        this.mScrollableAreaLimitSouth = d2;
        this.mScrollableAreaLimitExtraPixelHeight = i;
    }

    public void setScrollableAreaLimitLongitude(double d, double d2, int i) {
        this.mScrollableAreaLimitLongitude = true;
        this.mScrollableAreaLimitWest = d;
        this.mScrollableAreaLimitEast = d2;
        this.mScrollableAreaLimitExtraPixelWidth = i;
    }

    public boolean isScrollableAreaLimitLatitude() {
        return this.mScrollableAreaLimitLatitude;
    }

    public boolean isScrollableAreaLimitLongitude() {
        return this.mScrollableAreaLimitLongitude;
    }

    public void invalidateMapCoordinates(Rect rect) {
        invalidateMapCoordinates(rect.left, rect.top, rect.right, rect.bottom, false);
    }

    public void invalidateMapCoordinates(int i, int i2, int i3, int i4) {
        invalidateMapCoordinates(i, i2, i3, i4, false);
    }

    public void postInvalidateMapCoordinates(int i, int i2, int i3, int i4) {
        invalidateMapCoordinates(i, i2, i3, i4, true);
    }

    private void invalidateMapCoordinates(int i, int i2, int i3, int i4, boolean z) {
        this.mInvalidateRect.set(i, i2, i3, i4);
        i = getWidth() / 2;
        i2 = getHeight() / 2;
        if (getMapOrientation() != 0.0f) {
            GeometryMath.getBoundingBoxForRotatatedRectangle(this.mInvalidateRect, i, i2, getMapOrientation() + 180.0f, this.mInvalidateRect);
        }
        if (z) {
            Rect rect = this.mInvalidateRect;
            super.postInvalidate(rect.left, rect.top, rect.right, rect.bottom);
            return;
        }
        super.invalidate(this.mInvalidateRect);
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2, null, 8, 0, 0);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        measureChildren(i, i2);
        super.onMeasure(i, i2);
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        myOnLayout(z, i, i2, i3, i4);
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:9:0x005f, code skipped:
            r7 = r8;
     */
    /* JADX WARNING: Missing block: B:12:0x007f, code skipped:
            r12 = ((long) r9) + r10;
            r9 = (long) r4;
     */
    /* JADX WARNING: Missing block: B:16:0x00b6, code skipped:
            r9 = (long) r9;
     */
    /* JADX WARNING: Missing block: B:17:0x00b7, code skipped:
            r10 = r12 - r9;
     */
    /* JADX WARNING: Missing block: B:19:0x00ca, code skipped:
            r10 = r12 - r10;
     */
    /* JADX WARNING: Missing block: B:22:0x00ea, code skipped:
            r10 = r10 + ((long) r9);
     */
    /* JADX WARNING: Missing block: B:24:0x00fb, code skipped:
            r7 = r7 + ((long) r3.offsetX);
            r10 = r10 + ((long) r3.offsetY);
            r2.layout(org.osmdroid.util.TileSystem.truncateToInt(r7), org.osmdroid.util.TileSystem.truncateToInt(r10), org.osmdroid.util.TileSystem.truncateToInt(r7 + ((long) r5)), org.osmdroid.util.TileSystem.truncateToInt(r10 + ((long) r4)));
     */
    public void myOnLayout(boolean r15, int r16, int r17, int r18, int r19) {
        /*
        r14 = this;
        r6 = r14;
        r14.resetProjection();
        r0 = r14.getChildCount();
        r1 = 0;
    L_0x0009:
        if (r1 >= r0) goto L_0x011e;
    L_0x000b:
        r2 = r14.getChildAt(r1);
        r3 = r2.getVisibility();
        r4 = 8;
        if (r3 == r4) goto L_0x011a;
    L_0x0017:
        r3 = r2.getLayoutParams();
        r3 = (org.osmdroid.views.MapView.LayoutParams) r3;
        r4 = r2.getMeasuredHeight();
        r5 = r2.getMeasuredWidth();
        r7 = r14.getProjection();
        r8 = r3.geoPoint;
        r9 = r6.mLayoutPoint;
        r7.toPixels(r8, r9);
        r7 = r14.getMapOrientation();
        r8 = 0;
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 == 0) goto L_0x0052;
    L_0x0039:
        r7 = r14.getProjection();
        r8 = r6.mLayoutPoint;
        r9 = r8.x;
        r8 = r8.y;
        r10 = 0;
        r7 = r7.rotateAndScalePoint(r9, r8, r10);
        r8 = r6.mLayoutPoint;
        r9 = r7.x;
        r8.x = r9;
        r7 = r7.y;
        r8.y = r7;
    L_0x0052:
        r7 = r6.mLayoutPoint;
        r8 = r7.x;
        r8 = (long) r8;
        r7 = r7.y;
        r10 = (long) r7;
        r7 = r3.alignment;
        switch(r7) {
            case 1: goto L_0x00ed;
            case 2: goto L_0x00db;
            case 3: goto L_0x00cd;
            case 4: goto L_0x00bb;
            case 5: goto L_0x00a3;
            case 6: goto L_0x0091;
            case 7: goto L_0x0083;
            case 8: goto L_0x0070;
            case 9: goto L_0x0062;
            default: goto L_0x005f;
        };
    L_0x005f:
        r7 = r8;
        goto L_0x00fb;
    L_0x0062:
        r7 = r14.getPaddingLeft();
        r12 = (long) r7;
        r12 = r12 + r8;
        r7 = (long) r5;
        r7 = r12 - r7;
        r9 = r14.getPaddingTop();
        goto L_0x007f;
    L_0x0070:
        r7 = r14.getPaddingLeft();
        r12 = (long) r7;
        r12 = r12 + r8;
        r7 = r5 / 2;
        r7 = (long) r7;
        r7 = r12 - r7;
        r9 = r14.getPaddingTop();
    L_0x007f:
        r12 = (long) r9;
        r12 = r12 + r10;
        r9 = (long) r4;
        goto L_0x00b7;
    L_0x0083:
        r7 = r14.getPaddingLeft();
        r12 = (long) r7;
        r8 = r8 + r12;
        r7 = r14.getPaddingTop();
        r12 = (long) r7;
        r12 = r12 + r10;
        r10 = (long) r4;
        goto L_0x00ca;
    L_0x0091:
        r7 = r14.getPaddingLeft();
        r12 = (long) r7;
        r12 = r12 + r8;
        r7 = (long) r5;
        r7 = r12 - r7;
        r9 = r14.getPaddingTop();
        r12 = (long) r9;
        r12 = r12 + r10;
        r9 = r4 / 2;
        goto L_0x00b6;
    L_0x00a3:
        r7 = r14.getPaddingLeft();
        r12 = (long) r7;
        r12 = r12 + r8;
        r7 = r5 / 2;
        r7 = (long) r7;
        r7 = r12 - r7;
        r9 = r14.getPaddingTop();
        r12 = (long) r9;
        r12 = r12 + r10;
        r9 = r4 / 2;
    L_0x00b6:
        r9 = (long) r9;
    L_0x00b7:
        r9 = r12 - r9;
        r10 = r9;
        goto L_0x00fb;
    L_0x00bb:
        r7 = r14.getPaddingLeft();
        r12 = (long) r7;
        r8 = r8 + r12;
        r7 = r14.getPaddingTop();
        r12 = (long) r7;
        r12 = r12 + r10;
        r7 = r4 / 2;
        r10 = (long) r7;
    L_0x00ca:
        r10 = r12 - r10;
        goto L_0x005f;
    L_0x00cd:
        r7 = r14.getPaddingLeft();
        r12 = (long) r7;
        r12 = r12 + r8;
        r7 = (long) r5;
        r7 = r12 - r7;
        r9 = r14.getPaddingTop();
        goto L_0x00ea;
    L_0x00db:
        r7 = r14.getPaddingLeft();
        r12 = (long) r7;
        r12 = r12 + r8;
        r7 = r5 / 2;
        r7 = (long) r7;
        r7 = r12 - r7;
        r9 = r14.getPaddingTop();
    L_0x00ea:
        r12 = (long) r9;
        r10 = r10 + r12;
        goto L_0x00fb;
    L_0x00ed:
        r7 = r14.getPaddingLeft();
        r12 = (long) r7;
        r8 = r8 + r12;
        r7 = r14.getPaddingTop();
        r12 = (long) r7;
        r10 = r10 + r12;
        goto L_0x005f;
    L_0x00fb:
        r9 = r3.offsetX;
        r12 = (long) r9;
        r7 = r7 + r12;
        r3 = r3.offsetY;
        r12 = (long) r3;
        r10 = r10 + r12;
        r3 = org.osmdroid.util.TileSystem.truncateToInt(r7);
        r9 = org.osmdroid.util.TileSystem.truncateToInt(r10);
        r12 = (long) r5;
        r7 = r7 + r12;
        r5 = org.osmdroid.util.TileSystem.truncateToInt(r7);
        r7 = (long) r4;
        r10 = r10 + r7;
        r4 = org.osmdroid.util.TileSystem.truncateToInt(r10);
        r2.layout(r3, r9, r5, r4);
    L_0x011a:
        r1 = r1 + 1;
        goto L_0x0009;
    L_0x011e:
        r0 = r14.isLayoutOccurred();
        if (r0 != 0) goto L_0x014b;
    L_0x0124:
        r0 = 1;
        r6.mLayoutOccurred = r0;
        r0 = r6.mOnFirstLayoutListeners;
        r7 = r0.iterator();
    L_0x012d:
        r0 = r7.hasNext();
        if (r0 == 0) goto L_0x0146;
    L_0x0133:
        r0 = r7.next();
        r0 = (org.osmdroid.views.MapView.OnFirstLayoutListener) r0;
        r1 = r14;
        r2 = r16;
        r3 = r17;
        r4 = r18;
        r5 = r19;
        r0.onFirstLayout(r1, r2, r3, r4, r5);
        goto L_0x012d;
    L_0x0146:
        r0 = r6.mOnFirstLayoutListeners;
        r0.clear();
    L_0x014b:
        r14.resetProjection();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.MapView.myOnLayout(boolean, int, int, int, int):void");
    }

    public void addOnFirstLayoutListener(OnFirstLayoutListener onFirstLayoutListener) {
        if (!isLayoutOccurred()) {
            this.mOnFirstLayoutListeners.add(onFirstLayoutListener);
        }
    }

    public void removeOnFirstLayoutListener(OnFirstLayoutListener onFirstLayoutListener) {
        this.mOnFirstLayoutListeners.remove(onFirstLayoutListener);
    }

    public boolean isLayoutOccurred() {
        return this.mLayoutOccurred;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void onPause() {
        getOverlayManager().onPause();
    }

    public void onResume() {
        getOverlayManager().onResume();
    }

    public void onDetach() {
        getOverlayManager().onDetach(this);
        this.mTileProvider.detach();
        CustomZoomButtonsController customZoomButtonsController = this.mZoomController;
        if (customZoomButtonsController != null) {
            customZoomButtonsController.onDetach();
        }
        Handler handler = this.mTileRequestCompleteHandler;
        if (handler instanceof SimpleInvalidationHandler) {
            ((SimpleInvalidationHandler) handler).destroy();
        }
        this.mTileRequestCompleteHandler = null;
        Projection projection = this.mProjection;
        if (projection != null) {
            projection.detach();
        }
        this.mProjection = null;
        this.mRepository.onDetach();
        this.mListners.clear();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return getOverlayManager().onKeyDown(i, keyEvent, this) || super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return getOverlayManager().onKeyUp(i, keyEvent, this) || super.onKeyUp(i, keyEvent);
    }

    public boolean onTrackballEvent(MotionEvent motionEvent) {
        if (getOverlayManager().onTrackballEvent(motionEvent, this)) {
            return true;
        }
        scrollBy((int) (motionEvent.getX() * 25.0f), (int) (motionEvent.getY() * 25.0f));
        return super.onTrackballEvent(motionEvent);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        String str = "OsmDroid";
        if (Configuration.getInstance().isDebugMapView()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("dispatchTouchEvent(");
            stringBuilder.append(motionEvent);
            stringBuilder.append(")");
            Log.d(str, stringBuilder.toString());
        }
        MotionEvent rotateTouchEvent = rotateTouchEvent(motionEvent);
        try {
            if (super.dispatchTouchEvent(motionEvent)) {
                if (Configuration.getInstance().isDebugMapView()) {
                    Log.d(str, "super handled onTouchEvent");
                }
                if (rotateTouchEvent != motionEvent) {
                    rotateTouchEvent.recycle();
                }
                return true;
            } else if (getOverlayManager().onTouchEvent(rotateTouchEvent, this)) {
                if (rotateTouchEvent != motionEvent) {
                    rotateTouchEvent.recycle();
                }
                return true;
            } else {
                Object obj;
                if (this.mMultiTouchController == null || !this.mMultiTouchController.onTouchEvent(motionEvent)) {
                    obj = null;
                } else {
                    if (Configuration.getInstance().isDebugMapView()) {
                        Log.d(str, "mMultiTouchController handled onTouchEvent");
                    }
                    obj = 1;
                }
                if (this.mGestureDetector.onTouchEvent(rotateTouchEvent)) {
                    if (Configuration.getInstance().isDebugMapView()) {
                        Log.d(str, "mGestureDetector handled onTouchEvent");
                    }
                    obj = 1;
                }
                if (obj != null) {
                    if (rotateTouchEvent != motionEvent) {
                        rotateTouchEvent.recycle();
                    }
                    return true;
                }
                if (rotateTouchEvent != motionEvent) {
                    rotateTouchEvent.recycle();
                }
                if (Configuration.getInstance().isDebugMapView()) {
                    Log.d(str, "no-one handled onTouchEvent");
                }
                return false;
            }
        } catch (Throwable th) {
            if (rotateTouchEvent != motionEvent) {
                rotateTouchEvent.recycle();
            }
        }
    }

    private MotionEvent rotateTouchEvent(MotionEvent motionEvent) {
        if (getMapOrientation() == 0.0f) {
            return motionEvent;
        }
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        if (VERSION.SDK_INT < 11) {
            getProjection().unrotateAndScalePoint((int) motionEvent.getX(), (int) motionEvent.getY(), this.mRotateScalePoint);
            Point point = this.mRotateScalePoint;
            obtain.setLocation((float) point.x, (float) point.y);
        } else {
            obtain.transform(getProjection().getInvertedScaleRotateCanvasMatrix());
        }
        return obtain;
    }

    public void computeScroll() {
        Scroller scroller = this.mScroller;
        if (scroller != null && this.mIsFlinging && scroller.computeScrollOffset()) {
            if (this.mScroller.isFinished()) {
                this.mIsFlinging = false;
            } else {
                scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
                postInvalidate();
            }
        }
    }

    public void scrollTo(int i, int i2) {
        setMapScroll((long) i, (long) i2);
        resetProjection();
        invalidate();
        if (getMapOrientation() != 0.0f) {
            myOnLayout(true, getLeft(), getTop(), getRight(), getBottom());
        }
        ScrollEvent scrollEvent = null;
        for (MapListener mapListener : this.mListners) {
            if (scrollEvent == null) {
                scrollEvent = new ScrollEvent(this, i, i2);
            }
            mapListener.onScroll(scrollEvent);
        }
    }

    public void scrollBy(int i, int i2) {
        scrollTo((int) (getMapScrollX() + ((long) i)), (int) (getMapScrollY() + ((long) i2)));
    }

    public void setBackgroundColor(int i) {
        this.mMapOverlay.setLoadingBackgroundColor(i);
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        String str = "OsmDroid";
        long currentTimeMillis = System.currentTimeMillis();
        resetProjection();
        getProjection().save(canvas, true, false);
        try {
            getOverlayManager().onDraw(canvas, this);
            getProjection().restore(canvas, false);
            if (this.mZoomController != null) {
                this.mZoomController.draw(canvas);
            }
        } catch (Exception e) {
            Log.e(str, "error dispatchDraw, probably in edit mode", e);
        }
        if (Configuration.getInstance().isDebugMapView()) {
            long currentTimeMillis2 = System.currentTimeMillis();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Rendering overall: ");
            stringBuilder.append(currentTimeMillis2 - currentTimeMillis);
            stringBuilder.append("ms");
            Log.d(str, stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        if (this.mDestroyModeOnDetach) {
            onDetach();
        }
        super.onDetachedFromWindow();
    }

    public boolean isAnimating() {
        return this.mIsAnimating.get();
    }

    public Object getDraggableObjectAtPoint(PointInfo pointInfo) {
        if (isAnimating()) {
            return null;
        }
        setMultiTouchScaleInitPoint(pointInfo.getX(), pointInfo.getY());
        return this;
    }

    public void getPositionAndScale(Object obj, PositionAndScale positionAndScale) {
        startAnimation();
        PointF pointF = this.mMultiTouchScaleInitPoint;
        positionAndScale.set(pointF.x, pointF.y, true, 1.0f, false, 0.0f, 0.0f, false, 0.0f);
    }

    public void selectObject(Object obj, PointInfo pointInfo) {
        if (this.mZoomRounding) {
            this.mZoomLevel = (double) Math.round(this.mZoomLevel);
            invalidate();
        }
        resetMultiTouchScale();
    }

    public boolean setPositionAndScale(Object obj, PositionAndScale positionAndScale, PointInfo pointInfo) {
        setMultiTouchScaleCurrentPoint(positionAndScale.getXOff(), positionAndScale.getYOff());
        setMultiTouchScale(positionAndScale.getScale());
        requestLayout();
        invalidate();
        return true;
    }

    public void resetMultiTouchScale() {
        this.mMultiTouchScaleCurrentPoint = null;
    }

    /* Access modifiers changed, original: protected */
    public void setMultiTouchScaleInitPoint(float f, float f2) {
        this.mMultiTouchScaleInitPoint.set(f, f2);
        Point unrotateAndScalePoint = getProjection().unrotateAndScalePoint((int) f, (int) f2, null);
        getProjection().fromPixels(unrotateAndScalePoint.x, unrotateAndScalePoint.y, this.mMultiTouchScaleGeoPoint);
        setMultiTouchScaleCurrentPoint(f, f2);
    }

    /* Access modifiers changed, original: protected */
    public void setMultiTouchScaleCurrentPoint(float f, float f2) {
        this.mMultiTouchScaleCurrentPoint = new PointF(f, f2);
    }

    /* Access modifiers changed, original: protected */
    public void setMultiTouchScale(float f) {
        setZoomLevel((Math.log((double) f) / Math.log(2.0d)) + this.mStartAnimationZoom);
    }

    /* Access modifiers changed, original: protected */
    public void startAnimation() {
        this.mStartAnimationZoom = getZoomLevelDouble();
    }

    @Deprecated
    public void setMapListener(MapListener mapListener) {
        this.mListners.add(mapListener);
    }

    public void addMapListener(MapListener mapListener) {
        this.mListners.add(mapListener);
    }

    public void removeMapListener(MapListener mapListener) {
        this.mListners.remove(mapListener);
    }

    private void checkZoomButtons() {
        this.mZoomController.setZoomInEnabled(canZoomIn());
        this.mZoomController.setZoomOutEnabled(canZoomOut());
    }

    @Deprecated
    public void setBuiltInZoomControls(boolean z) {
        getZoomController().setVisibility(z ? Visibility.SHOW_AND_FADEOUT : Visibility.NEVER);
    }

    public void setMultiTouchControls(boolean z) {
        this.mMultiTouchController = z ? new MultiTouchController(this, false) : null;
    }

    public boolean isHorizontalMapRepetitionEnabled() {
        return this.horizontalMapRepetitionEnabled;
    }

    public void setHorizontalMapRepetitionEnabled(boolean z) {
        this.horizontalMapRepetitionEnabled = z;
        this.mMapOverlay.setHorizontalWrapEnabled(z);
        resetProjection();
        invalidate();
    }

    public boolean isVerticalMapRepetitionEnabled() {
        return this.verticalMapRepetitionEnabled;
    }

    public void setVerticalMapRepetitionEnabled(boolean z) {
        this.verticalMapRepetitionEnabled = z;
        this.mMapOverlay.setVerticalWrapEnabled(z);
        resetProjection();
        invalidate();
    }

    private ITileSource getTileSourceFromAttributes(AttributeSet attributeSet) {
        ITileSource iTileSource = TileSourceFactory.DEFAULT_TILE_SOURCE;
        String str = "OsmDroid";
        if (attributeSet != null) {
            String attributeValue = attributeSet.getAttributeValue(null, "tilesource");
            if (attributeValue != null) {
                try {
                    ITileSource tileSource = TileSourceFactory.getTileSource(attributeValue);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Using tile source specified in layout attributes: ");
                    stringBuilder.append(tileSource);
                    Log.i(str, stringBuilder.toString());
                    iTileSource = tileSource;
                } catch (IllegalArgumentException unused) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Invalid tile source specified in layout attributes: ");
                    stringBuilder2.append(iTileSource);
                    Log.w(str, stringBuilder2.toString());
                }
            }
        }
        if (attributeSet != null && (iTileSource instanceof IStyledTileSource)) {
            String attributeValue2 = attributeSet.getAttributeValue(null, "style");
            if (attributeValue2 == null) {
                Log.i(str, "Using default style: 1");
            } else {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Using style specified in layout attributes: ");
                stringBuilder3.append(attributeValue2);
                Log.i(str, stringBuilder3.toString());
                ((IStyledTileSource) iTileSource).setStyle(attributeValue2);
            }
        }
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("Using tile source: ");
        stringBuilder4.append(iTileSource.name());
        Log.i(str, stringBuilder4.toString());
        return iTileSource;
    }

    public void setFlingEnabled(boolean z) {
        this.enableFling = z;
    }

    public boolean isFlingEnabled() {
        return this.enableFling;
    }

    public void setTileProvider(MapTileProviderBase mapTileProviderBase) {
        this.mTileProvider.detach();
        this.mTileProvider.clearTileCache();
        this.mTileProvider = mapTileProviderBase;
        this.mTileProvider.getTileRequestCompleteHandlers().add(this.mTileRequestCompleteHandler);
        updateTileSizeForDensity(this.mTileProvider.getTileSource());
        this.mMapOverlay = new TilesOverlay(this.mTileProvider, getContext(), this.horizontalMapRepetitionEnabled, this.verticalMapRepetitionEnabled);
        this.mOverlayManager.setTilesOverlay(this.mMapOverlay);
        invalidate();
    }

    @Deprecated
    public void setInitCenter(IGeoPoint iGeoPoint) {
        setExpectedCenter(iGeoPoint);
    }

    public long getMapScrollX() {
        return this.mMapScrollX;
    }

    public long getMapScrollY() {
        return this.mMapScrollY;
    }

    /* Access modifiers changed, original: 0000 */
    public void setMapScroll(long j, long j2) {
        this.mMapScrollX = j;
        this.mMapScrollY = j2;
        requestLayout();
    }

    /* Access modifiers changed, original: 0000 */
    public GeoPoint getExpectedCenter() {
        return this.mCenter;
    }

    public void setExpectedCenter(IGeoPoint iGeoPoint, long j, long j2) {
        GeoPoint currentCenter = getProjection().getCurrentCenter();
        this.mCenter = (GeoPoint) iGeoPoint;
        setMapScroll(-j, -j2);
        resetProjection();
        if (!getProjection().getCurrentCenter().equals(currentCenter)) {
            ScrollEvent scrollEvent = null;
            for (MapListener mapListener : this.mListners) {
                if (scrollEvent == null) {
                    scrollEvent = new ScrollEvent(this, 0, 0);
                }
                mapListener.onScroll(scrollEvent);
            }
        }
        invalidate();
    }

    public void setExpectedCenter(IGeoPoint iGeoPoint) {
        setExpectedCenter(iGeoPoint, 0, 0);
    }

    public void setZoomRounding(boolean z) {
        this.mZoomRounding = z;
    }

    public static TileSystem getTileSystem() {
        return mTileSystem;
    }

    public static void setTileSystem(TileSystem tileSystem) {
        mTileSystem = tileSystem;
    }

    public MapViewRepository getRepository() {
        return this.mRepository;
    }

    public CustomZoomButtonsController getZoomController() {
        return this.mZoomController;
    }

    public TilesOverlay getMapOverlay() {
        return this.mMapOverlay;
    }

    public void setDestroyMode(boolean z) {
        this.mDestroyModeOnDetach = z;
    }
}

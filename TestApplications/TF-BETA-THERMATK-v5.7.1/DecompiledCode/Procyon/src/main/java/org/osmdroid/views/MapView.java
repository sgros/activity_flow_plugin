// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views;

import org.osmdroid.events.ZoomEvent;
import org.osmdroid.events.ScrollEvent;
import android.view.KeyEvent;
import android.graphics.Canvas;
import java.util.Iterator;
import org.osmdroid.api.IProjection;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.api.IGeoPoint;
import android.view.ViewGroup$LayoutParams;
import android.view.MotionEvent;
import org.osmdroid.util.GeometryMath;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.IStyledTileSource;
import android.util.Log;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import android.view.GestureDetector$OnDoubleTapListener;
import android.view.GestureDetector$OnGestureListener;
import org.osmdroid.views.overlay.DefaultOverlayManager;
import android.view.View;
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import android.graphics.Paint;
import android.os.Build$VERSION;
import java.util.ArrayList;
import org.osmdroid.config.Configuration;
import android.util.AttributeSet;
import android.content.Context;
import org.osmdroid.util.TileSystemWebMercator;
import android.os.Handler;
import org.osmdroid.tileprovider.MapTileProviderBase;
import android.widget.Scroller;
import org.osmdroid.views.overlay.OverlayManager;
import java.util.LinkedList;
import android.graphics.PointF;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.events.MapListener;
import java.util.List;
import android.graphics.Point;
import java.util.concurrent.atomic.AtomicBoolean;
import android.graphics.Rect;
import android.view.GestureDetector;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.TileSystem;
import org.metalev.multitouch.controller.MultiTouchController;
import org.osmdroid.api.IMapView;
import android.view.ViewGroup;

public class MapView extends ViewGroup implements IMapView, MultiTouchObjectCanvas<Object>
{
    private static TileSystem mTileSystem;
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
    
    static {
        MapView.mTileSystem = new TileSystemWebMercator();
    }
    
    public MapView(final Context context) {
        this(context, null, null, null);
    }
    
    public MapView(final Context context, final AttributeSet set) {
        this(context, null, null, set);
    }
    
    public MapView(final Context context, final MapTileProviderBase mapTileProviderBase) {
        this(context, mapTileProviderBase, null);
    }
    
    public MapView(final Context context, final MapTileProviderBase mapTileProviderBase, final Handler handler) {
        this(context, mapTileProviderBase, handler, null);
    }
    
    public MapView(final Context context, final MapTileProviderBase mapTileProviderBase, final Handler handler, final AttributeSet set) {
        this(context, mapTileProviderBase, handler, set, Configuration.getInstance().isMapViewHardwareAccelerated());
    }
    
    public MapView(final Context context, final MapTileProviderBase mapTileProviderBase, final Handler handler, final AttributeSet set, final boolean b) {
        super(context, set);
        this.mZoomLevel = 0.0;
        this.mIsAnimating = new AtomicBoolean(false);
        this.mMultiTouchScaleInitPoint = new PointF();
        this.mMultiTouchScaleGeoPoint = new GeoPoint(0.0, 0.0);
        this.mapOrientation = 0.0f;
        this.mInvalidateRect = new Rect();
        this.mTilesScaledToDpi = false;
        this.mTilesScaleFactor = 1.0f;
        this.mRotateScalePoint = new Point();
        this.mLayoutPoint = new Point();
        this.mOnFirstLayoutListeners = new LinkedList<OnFirstLayoutListener>();
        this.mLayoutOccurred = false;
        this.horizontalMapRepetitionEnabled = true;
        this.verticalMapRepetitionEnabled = true;
        this.mListners = new ArrayList<MapListener>();
        this.mRepository = new MapViewRepository(this);
        this.mRescaleScreenRect = new Rect();
        this.mDestroyModeOnDetach = true;
        this.enableFling = true;
        this.setWillNotDraw(this.pauseFling = false);
        if (this.isInEditMode()) {
            this.mTileRequestCompleteHandler = null;
            this.mController = null;
            this.mZoomController = null;
            this.mScroller = null;
            this.mGestureDetector = null;
            return;
        }
        if (!b && Build$VERSION.SDK_INT >= 11) {
            this.setLayerType(1, (Paint)null);
        }
        this.mController = new MapController(this);
        this.mScroller = new Scroller(context);
        MapTileProviderBase mTileProvider;
        if ((mTileProvider = mapTileProviderBase) == null) {
            final ITileSource tileSourceFromAttributes = this.getTileSourceFromAttributes(set);
            MapTileProviderArray mapTileProviderArray;
            if (this.isInEditMode()) {
                mapTileProviderArray = new MapTileProviderArray(tileSourceFromAttributes, null, new MapTileModuleProviderBase[0]);
            }
            else {
                mapTileProviderArray = new MapTileProviderBasic(context.getApplicationContext(), tileSourceFromAttributes);
            }
            mTileProvider = mapTileProviderArray;
        }
        Handler mTileRequestCompleteHandler;
        if ((mTileRequestCompleteHandler = handler) == null) {
            mTileRequestCompleteHandler = new SimpleInvalidationHandler((View)this);
        }
        this.mTileRequestCompleteHandler = mTileRequestCompleteHandler;
        this.mTileProvider = mTileProvider;
        this.mTileProvider.getTileRequestCompleteHandlers().add(this.mTileRequestCompleteHandler);
        this.updateTileSizeForDensity(this.mTileProvider.getTileSource());
        this.mMapOverlay = new TilesOverlay(this.mTileProvider, context, this.horizontalMapRepetitionEnabled, this.verticalMapRepetitionEnabled);
        this.mOverlayManager = new DefaultOverlayManager(this.mMapOverlay);
        (this.mZoomController = new CustomZoomButtonsController(this)).setOnZoomListener((CustomZoomButtonsController.OnZoomListener)new MapViewZoomListener());
        this.checkZoomButtons();
        (this.mGestureDetector = new GestureDetector(context, (GestureDetector$OnGestureListener)new MapViewGestureDetectorListener())).setOnDoubleTapListener((GestureDetector$OnDoubleTapListener)new MapViewDoubleClickListener());
        if (Configuration.getInstance().isMapViewRecyclerFriendly() && Build$VERSION.SDK_INT >= 16) {
            this.setHasTransientState(true);
        }
        this.mZoomController.setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
    }
    
    private void checkZoomButtons() {
        this.mZoomController.setZoomInEnabled(this.canZoomIn());
        this.mZoomController.setZoomOutEnabled(this.canZoomOut());
    }
    
    private ITileSource getTileSourceFromAttributes(final AttributeSet set) {
        ITileSource obj2;
        final OnlineTileSourceBase obj = (OnlineTileSourceBase)(obj2 = TileSourceFactory.DEFAULT_TILE_SOURCE);
        if (set != null) {
            final String attributeValue = set.getAttributeValue((String)null, "tilesource");
            obj2 = obj;
            if (attributeValue != null) {
                try {
                    obj2 = TileSourceFactory.getTileSource(attributeValue);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Using tile source specified in layout attributes: ");
                    sb.append(obj2);
                    Log.i("OsmDroid", sb.toString());
                }
                catch (IllegalArgumentException ex) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Invalid tile source specified in layout attributes: ");
                    sb2.append(obj);
                    Log.w("OsmDroid", sb2.toString());
                    obj2 = obj;
                }
            }
        }
        if (set != null && obj2 instanceof IStyledTileSource) {
            final String attributeValue2 = set.getAttributeValue((String)null, "style");
            if (attributeValue2 == null) {
                Log.i("OsmDroid", "Using default style: 1");
            }
            else {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Using style specified in layout attributes: ");
                sb3.append(attributeValue2);
                Log.i("OsmDroid", sb3.toString());
                ((IStyledTileSource)obj2).setStyle(attributeValue2);
            }
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("Using tile source: ");
        sb4.append(obj2.name());
        Log.i("OsmDroid", sb4.toString());
        return obj2;
    }
    
    public static TileSystem getTileSystem() {
        return MapView.mTileSystem;
    }
    
    private void invalidateMapCoordinates(int n, int n2, final int n3, final int n4, final boolean b) {
        this.mInvalidateRect.set(n, n2, n3, n4);
        n = this.getWidth() / 2;
        n2 = this.getHeight() / 2;
        if (this.getMapOrientation() != 0.0f) {
            GeometryMath.getBoundingBoxForRotatatedRectangle(this.mInvalidateRect, n, n2, this.getMapOrientation() + 180.0f, this.mInvalidateRect);
        }
        if (b) {
            final Rect mInvalidateRect = this.mInvalidateRect;
            super.postInvalidate(mInvalidateRect.left, mInvalidateRect.top, mInvalidateRect.right, mInvalidateRect.bottom);
        }
        else {
            super.invalidate(this.mInvalidateRect);
        }
    }
    
    private void resetProjection() {
        this.mProjection = null;
    }
    
    private MotionEvent rotateTouchEvent(final MotionEvent motionEvent) {
        if (this.getMapOrientation() == 0.0f) {
            return motionEvent;
        }
        final MotionEvent obtain = MotionEvent.obtain(motionEvent);
        if (Build$VERSION.SDK_INT < 11) {
            this.getProjection().unrotateAndScalePoint((int)motionEvent.getX(), (int)motionEvent.getY(), this.mRotateScalePoint);
            final Point mRotateScalePoint = this.mRotateScalePoint;
            obtain.setLocation((float)mRotateScalePoint.x, (float)mRotateScalePoint.y);
        }
        else {
            obtain.transform(this.getProjection().getInvertedScaleRotateCanvasMatrix());
        }
        return obtain;
    }
    
    public static void setTileSystem(final TileSystem mTileSystem) {
        MapView.mTileSystem = mTileSystem;
    }
    
    private void updateTileSizeForDensity(final ITileSource tileSource) {
        final int tileSizePixels = tileSource.getTileSizePixels();
        final float density = this.getResources().getDisplayMetrics().density;
        final float n = (float)tileSizePixels;
        final float n2 = density * 256.0f / n;
        float mTilesScaleFactor;
        if (this.isTilesScaledToDpi()) {
            mTilesScaleFactor = n2 * this.mTilesScaleFactor;
        }
        else {
            mTilesScaleFactor = this.mTilesScaleFactor;
        }
        final int n3 = (int)(n * mTilesScaleFactor);
        if (Configuration.getInstance().isDebugMapView()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Scaling tiles to ");
            sb.append(n3);
            Log.d("OsmDroid", sb.toString());
        }
        TileSystem.setTileSize(n3);
    }
    
    public void addMapListener(final MapListener mapListener) {
        this.mListners.add(mapListener);
    }
    
    public void addOnFirstLayoutListener(final OnFirstLayoutListener e) {
        if (!this.isLayoutOccurred()) {
            this.mOnFirstLayoutListeners.add(e);
        }
    }
    
    public boolean canZoomIn() {
        return this.mZoomLevel < this.getMaxZoomLevel();
    }
    
    public boolean canZoomOut() {
        return this.mZoomLevel > this.getMinZoomLevel();
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void computeScroll() {
        final Scroller mScroller = this.mScroller;
        if (mScroller == null) {
            return;
        }
        if (!this.mIsFlinging) {
            return;
        }
        if (!mScroller.computeScrollOffset()) {
            return;
        }
        if (this.mScroller.isFinished()) {
            this.mIsFlinging = false;
        }
        else {
            this.scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            this.postInvalidate();
        }
    }
    
    public boolean dispatchTouchEvent(final MotionEvent obj) {
        if (Configuration.getInstance().isDebugMapView()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dispatchTouchEvent(");
            sb.append(obj);
            sb.append(")");
            Log.d("OsmDroid", sb.toString());
        }
        final MotionEvent rotateTouchEvent = this.rotateTouchEvent(obj);
        try {
            if (super.dispatchTouchEvent(obj)) {
                if (Configuration.getInstance().isDebugMapView()) {
                    Log.d("OsmDroid", "super handled onTouchEvent");
                }
                return true;
            }
            if (this.getOverlayManager().onTouchEvent(rotateTouchEvent, this)) {
                return true;
            }
            boolean b;
            if (this.mMultiTouchController != null && this.mMultiTouchController.onTouchEvent(obj)) {
                if (Configuration.getInstance().isDebugMapView()) {
                    Log.d("OsmDroid", "mMultiTouchController handled onTouchEvent");
                }
                b = true;
            }
            else {
                b = false;
            }
            if (this.mGestureDetector.onTouchEvent(rotateTouchEvent)) {
                if (Configuration.getInstance().isDebugMapView()) {
                    Log.d("OsmDroid", "mGestureDetector handled onTouchEvent");
                }
                b = true;
            }
            if (b) {
                return true;
            }
            if (rotateTouchEvent != obj) {
                rotateTouchEvent.recycle();
            }
            if (Configuration.getInstance().isDebugMapView()) {
                Log.d("OsmDroid", "no-one handled onTouchEvent");
            }
            return false;
        }
        finally {
            if (rotateTouchEvent != obj) {
                rotateTouchEvent.recycle();
            }
        }
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2, null, 8, 0, 0);
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    public BoundingBox getBoundingBox() {
        return this.getProjection().getBoundingBox();
    }
    
    public IMapController getController() {
        return this.mController;
    }
    
    public Object getDraggableObjectAtPoint(final PointInfo pointInfo) {
        if (this.isAnimating()) {
            return null;
        }
        this.setMultiTouchScaleInitPoint(pointInfo.getX(), pointInfo.getY());
        return this;
    }
    
    GeoPoint getExpectedCenter() {
        return this.mCenter;
    }
    
    public Rect getIntrinsicScreenRect(final Rect rect) {
        Rect rect2 = rect;
        if (rect == null) {
            rect2 = new Rect();
        }
        rect2.set(0, 0, this.getWidth(), this.getHeight());
        return rect2;
    }
    
    public double getLatitudeSpanDouble() {
        return this.getBoundingBox().getLatitudeSpan();
    }
    
    public double getLongitudeSpanDouble() {
        return this.getBoundingBox().getLongitudeSpan();
    }
    
    public IGeoPoint getMapCenter() {
        return this.getMapCenter(null);
    }
    
    public IGeoPoint getMapCenter(final GeoPoint geoPoint) {
        return this.getProjection().fromPixels(this.getWidth() / 2, this.getHeight() / 2, geoPoint, false);
    }
    
    public float getMapOrientation() {
        return this.mapOrientation;
    }
    
    public TilesOverlay getMapOverlay() {
        return this.mMapOverlay;
    }
    
    @Deprecated
    public float getMapScale() {
        return 1.0f;
    }
    
    public long getMapScrollX() {
        return this.mMapScrollX;
    }
    
    public long getMapScrollY() {
        return this.mMapScrollY;
    }
    
    public double getMaxZoomLevel() {
        final Double mMaximumZoomLevel = this.mMaximumZoomLevel;
        double doubleValue;
        if (mMaximumZoomLevel == null) {
            doubleValue = this.mMapOverlay.getMaximumZoomLevel();
        }
        else {
            doubleValue = mMaximumZoomLevel;
        }
        return doubleValue;
    }
    
    public double getMinZoomLevel() {
        final Double mMinimumZoomLevel = this.mMinimumZoomLevel;
        double doubleValue;
        if (mMinimumZoomLevel == null) {
            doubleValue = this.mMapOverlay.getMinimumZoomLevel();
        }
        else {
            doubleValue = mMinimumZoomLevel;
        }
        return doubleValue;
    }
    
    public OverlayManager getOverlayManager() {
        return this.mOverlayManager;
    }
    
    public List<Overlay> getOverlays() {
        return this.getOverlayManager().overlays();
    }
    
    public void getPositionAndScale(final Object o, final PositionAndScale positionAndScale) {
        this.startAnimation();
        final PointF mMultiTouchScaleInitPoint = this.mMultiTouchScaleInitPoint;
        positionAndScale.set(mMultiTouchScaleInitPoint.x, mMultiTouchScaleInitPoint.y, true, 1.0f, false, 0.0f, 0.0f, false, 0.0f);
    }
    
    public Projection getProjection() {
        if (this.mProjection == null) {
            (this.mProjection = new Projection(this)).adjustOffsets(this.mMultiTouchScaleGeoPoint, this.mMultiTouchScaleCurrentPoint);
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
    
    public MapViewRepository getRepository() {
        return this.mRepository;
    }
    
    public Rect getScreenRect(Rect intrinsicScreenRect) {
        intrinsicScreenRect = this.getIntrinsicScreenRect(intrinsicScreenRect);
        if (this.getMapOrientation() != 0.0f && this.getMapOrientation() != 180.0f) {
            GeometryMath.getBoundingBoxForRotatatedRectangle(intrinsicScreenRect, intrinsicScreenRect.centerX(), intrinsicScreenRect.centerY(), this.getMapOrientation(), intrinsicScreenRect);
        }
        return intrinsicScreenRect;
    }
    
    public Scroller getScroller() {
        return this.mScroller;
    }
    
    public MapTileProviderBase getTileProvider() {
        return this.mTileProvider;
    }
    
    public Handler getTileRequestCompleteHandler() {
        return this.mTileRequestCompleteHandler;
    }
    
    public float getTilesScaleFactor() {
        return this.mTilesScaleFactor;
    }
    
    public CustomZoomButtonsController getZoomController() {
        return this.mZoomController;
    }
    
    @Deprecated
    public double getZoomLevel(final boolean b) {
        return this.getZoomLevelDouble();
    }
    
    @Deprecated
    public int getZoomLevel() {
        return (int)this.getZoomLevelDouble();
    }
    
    public double getZoomLevelDouble() {
        return this.mZoomLevel;
    }
    
    public void invalidateMapCoordinates(final int n, final int n2, final int n3, final int n4) {
        this.invalidateMapCoordinates(n, n2, n3, n4, false);
    }
    
    public void invalidateMapCoordinates(final Rect rect) {
        this.invalidateMapCoordinates(rect.left, rect.top, rect.right, rect.bottom, false);
    }
    
    public boolean isAnimating() {
        return this.mIsAnimating.get();
    }
    
    public boolean isFlingEnabled() {
        return this.enableFling;
    }
    
    public boolean isHorizontalMapRepetitionEnabled() {
        return this.horizontalMapRepetitionEnabled;
    }
    
    public boolean isLayoutOccurred() {
        return this.mLayoutOccurred;
    }
    
    public boolean isScrollableAreaLimitLatitude() {
        return this.mScrollableAreaLimitLatitude;
    }
    
    public boolean isScrollableAreaLimitLongitude() {
        return this.mScrollableAreaLimitLongitude;
    }
    
    public boolean isTilesScaledToDpi() {
        return this.mTilesScaledToDpi;
    }
    
    public boolean isVerticalMapRepetitionEnabled() {
        return this.verticalMapRepetitionEnabled;
    }
    
    protected void myOnLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        this.resetProjection();
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                final int measuredHeight = child.getMeasuredHeight();
                final int measuredWidth = child.getMeasuredWidth();
                this.getProjection().toPixels(layoutParams.geoPoint, this.mLayoutPoint);
                if (this.getMapOrientation() != 0.0f) {
                    final Projection projection = this.getProjection();
                    final Point mLayoutPoint = this.mLayoutPoint;
                    final Point rotateAndScalePoint = projection.rotateAndScalePoint(mLayoutPoint.x, mLayoutPoint.y, null);
                    final Point mLayoutPoint2 = this.mLayoutPoint;
                    mLayoutPoint2.x = rotateAndScalePoint.x;
                    mLayoutPoint2.y = rotateAndScalePoint.y;
                }
                final Point mLayoutPoint3 = this.mLayoutPoint;
                long n5 = mLayoutPoint3.x;
                final long n6 = mLayoutPoint3.y;
                long n7 = 0L;
                Label_0520: {
                    int n14 = 0;
                    Label_0486: {
                        while (true) {
                            long n10 = 0L;
                            long n11 = 0L;
                            Label_0431: {
                                long n8 = 0L;
                                long n12 = 0L;
                                long n15 = 0L;
                                Label_0386: {
                                    int n13 = 0;
                                    Label_0381: {
                                        int n9 = 0;
                                        Label_0272: {
                                            switch (layoutParams.alignment) {
                                                default: {
                                                    n7 = n6;
                                                    break;
                                                }
                                                case 9: {
                                                    n8 = this.getPaddingLeft() + n5 - measuredWidth;
                                                    n9 = this.getPaddingTop();
                                                    break Label_0272;
                                                }
                                                case 8: {
                                                    n8 = this.getPaddingLeft() + n5 - measuredWidth / 2;
                                                    n9 = this.getPaddingTop();
                                                    break Label_0272;
                                                }
                                                case 7: {
                                                    n5 += this.getPaddingLeft();
                                                    n10 = this.getPaddingTop() + n6;
                                                    n11 = measuredHeight;
                                                    break Label_0431;
                                                }
                                                case 6: {
                                                    n8 = this.getPaddingLeft() + n5 - measuredWidth;
                                                    n12 = this.getPaddingTop() + n6;
                                                    n13 = measuredHeight / 2;
                                                    break Label_0381;
                                                }
                                                case 5: {
                                                    n8 = this.getPaddingLeft() + n5 - measuredWidth / 2;
                                                    n12 = this.getPaddingTop() + n6;
                                                    n13 = measuredHeight / 2;
                                                    break Label_0381;
                                                }
                                                case 4: {
                                                    n5 += this.getPaddingLeft();
                                                    n10 = this.getPaddingTop() + n6;
                                                    n11 = measuredHeight / 2;
                                                    break Label_0431;
                                                }
                                                case 3: {
                                                    n5 = this.getPaddingLeft() + n5 - measuredWidth;
                                                    n14 = this.getPaddingTop();
                                                    break Label_0486;
                                                }
                                                case 2: {
                                                    n5 = this.getPaddingLeft() + n5 - measuredWidth / 2;
                                                    n14 = this.getPaddingTop();
                                                    break Label_0486;
                                                }
                                                case 1: {
                                                    n5 += this.getPaddingLeft();
                                                    n7 = n6 + this.getPaddingTop();
                                                    break;
                                                }
                                            }
                                            break Label_0520;
                                        }
                                        n12 = n9 + n6;
                                        n15 = measuredHeight;
                                        break Label_0386;
                                    }
                                    n15 = n13;
                                }
                                final long n16 = n12 - n15;
                                n5 = n8;
                                n7 = n16;
                                break Label_0520;
                            }
                            n7 = n10 - n11;
                            continue;
                        }
                    }
                    n7 = n6 + n14;
                }
                final long n17 = n5 + layoutParams.offsetX;
                final long n18 = n7 + layoutParams.offsetY;
                child.layout(TileSystem.truncateToInt(n17), TileSystem.truncateToInt(n18), TileSystem.truncateToInt(n17 + measuredWidth), TileSystem.truncateToInt(n18 + measuredHeight));
            }
        }
        if (!this.isLayoutOccurred()) {
            this.mLayoutOccurred = true;
            final Iterator<OnFirstLayoutListener> iterator = (Iterator<OnFirstLayoutListener>)this.mOnFirstLayoutListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onFirstLayout((View)this, n, n2, n3, n4);
            }
            this.mOnFirstLayoutListeners.clear();
        }
        this.resetProjection();
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
    
    public void onDetach() {
        this.getOverlayManager().onDetach(this);
        this.mTileProvider.detach();
        final CustomZoomButtonsController mZoomController = this.mZoomController;
        if (mZoomController != null) {
            mZoomController.onDetach();
        }
        final Handler mTileRequestCompleteHandler = this.mTileRequestCompleteHandler;
        if (mTileRequestCompleteHandler instanceof SimpleInvalidationHandler) {
            ((SimpleInvalidationHandler)mTileRequestCompleteHandler).destroy();
        }
        this.mTileRequestCompleteHandler = null;
        final Projection mProjection = this.mProjection;
        if (mProjection != null) {
            mProjection.detach();
        }
        this.mProjection = null;
        this.mRepository.onDetach();
        this.mListners.clear();
    }
    
    protected void onDetachedFromWindow() {
        if (this.mDestroyModeOnDetach) {
            this.onDetach();
        }
        super.onDetachedFromWindow();
    }
    
    public void onDraw(final Canvas canvas) {
        final long currentTimeMillis = System.currentTimeMillis();
        this.resetProjection();
        this.getProjection().save(canvas, true, false);
        try {
            this.getOverlayManager().onDraw(canvas, this);
            this.getProjection().restore(canvas, false);
            if (this.mZoomController != null) {
                this.mZoomController.draw(canvas);
            }
        }
        catch (Exception ex) {
            Log.e("OsmDroid", "error dispatchDraw, probably in edit mode", (Throwable)ex);
        }
        if (Configuration.getInstance().isDebugMapView()) {
            final long currentTimeMillis2 = System.currentTimeMillis();
            final StringBuilder sb = new StringBuilder();
            sb.append("Rendering overall: ");
            sb.append(currentTimeMillis2 - currentTimeMillis);
            sb.append("ms");
            Log.d("OsmDroid", sb.toString());
        }
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return this.getOverlayManager().onKeyDown(n, keyEvent, this) || super.onKeyDown(n, keyEvent);
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        return this.getOverlayManager().onKeyUp(n, keyEvent, this) || super.onKeyUp(n, keyEvent);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        this.myOnLayout(b, n, n2, n3, n4);
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.measureChildren(n, n2);
        super.onMeasure(n, n2);
    }
    
    public void onPause() {
        this.getOverlayManager().onPause();
    }
    
    public void onResume() {
        this.getOverlayManager().onResume();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return false;
    }
    
    public boolean onTrackballEvent(final MotionEvent motionEvent) {
        if (this.getOverlayManager().onTrackballEvent(motionEvent, this)) {
            return true;
        }
        this.scrollBy((int)(motionEvent.getX() * 25.0f), (int)(motionEvent.getY() * 25.0f));
        return super.onTrackballEvent(motionEvent);
    }
    
    public void postInvalidateMapCoordinates(final int n, final int n2, final int n3, final int n4) {
        this.invalidateMapCoordinates(n, n2, n3, n4, true);
    }
    
    public void removeMapListener(final MapListener mapListener) {
        this.mListners.remove(mapListener);
    }
    
    public void removeOnFirstLayoutListener(final OnFirstLayoutListener o) {
        this.mOnFirstLayoutListeners.remove(o);
    }
    
    public void resetMultiTouchScale() {
        this.mMultiTouchScaleCurrentPoint = null;
    }
    
    public void resetScrollableAreaLimitLatitude() {
        this.mScrollableAreaLimitLatitude = false;
    }
    
    public void resetScrollableAreaLimitLongitude() {
        this.mScrollableAreaLimitLongitude = false;
    }
    
    public void resetTilesScaleFactor() {
        this.mTilesScaleFactor = 1.0f;
        this.updateTileSizeForDensity(this.getTileProvider().getTileSource());
    }
    
    public void scrollBy(final int n, final int n2) {
        this.scrollTo((int)(this.getMapScrollX() + n), (int)(this.getMapScrollY() + n2));
    }
    
    public void scrollTo(final int n, final int n2) {
        this.setMapScroll(n, n2);
        this.resetProjection();
        this.invalidate();
        if (this.getMapOrientation() != 0.0f) {
            this.myOnLayout(true, this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
        }
        ScrollEvent scrollEvent = null;
        for (final MapListener mapListener : this.mListners) {
            if (scrollEvent == null) {
                scrollEvent = new ScrollEvent(this, n, n2);
            }
            mapListener.onScroll(scrollEvent);
        }
    }
    
    public void selectObject(final Object o, final PointInfo pointInfo) {
        if (this.mZoomRounding) {
            this.mZoomLevel = (double)Math.round(this.mZoomLevel);
            this.invalidate();
        }
        this.resetMultiTouchScale();
    }
    
    public void setBackgroundColor(final int loadingBackgroundColor) {
        this.mMapOverlay.setLoadingBackgroundColor(loadingBackgroundColor);
        this.invalidate();
    }
    
    @Deprecated
    public void setBuiltInZoomControls(final boolean b) {
        final CustomZoomButtonsController zoomController = this.getZoomController();
        CustomZoomButtonsController.Visibility visibility;
        if (b) {
            visibility = CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT;
        }
        else {
            visibility = CustomZoomButtonsController.Visibility.NEVER;
        }
        zoomController.setVisibility(visibility);
    }
    
    public void setDestroyMode(final boolean mDestroyModeOnDetach) {
        this.mDestroyModeOnDetach = mDestroyModeOnDetach;
    }
    
    public void setExpectedCenter(final IGeoPoint geoPoint) {
        this.setExpectedCenter(geoPoint, 0L, 0L);
    }
    
    public void setExpectedCenter(final IGeoPoint geoPoint, final long n, final long n2) {
        final GeoPoint currentCenter = this.getProjection().getCurrentCenter();
        this.mCenter = (GeoPoint)geoPoint;
        this.setMapScroll(-n, -n2);
        this.resetProjection();
        if (!this.getProjection().getCurrentCenter().equals(currentCenter)) {
            ScrollEvent scrollEvent = null;
            for (final MapListener mapListener : this.mListners) {
                if (scrollEvent == null) {
                    scrollEvent = new ScrollEvent(this, 0, 0);
                }
                mapListener.onScroll(scrollEvent);
            }
        }
        this.invalidate();
    }
    
    public void setFlingEnabled(final boolean enableFling) {
        this.enableFling = enableFling;
    }
    
    public void setHorizontalMapRepetitionEnabled(final boolean b) {
        this.horizontalMapRepetitionEnabled = b;
        this.mMapOverlay.setHorizontalWrapEnabled(b);
        this.resetProjection();
        this.invalidate();
    }
    
    @Deprecated
    public void setInitCenter(final IGeoPoint expectedCenter) {
        this.setExpectedCenter(expectedCenter);
    }
    
    @Deprecated
    void setMapCenter(final double n, final double n2) {
        this.setMapCenter(new GeoPoint(n, n2));
    }
    
    @Deprecated
    void setMapCenter(final int n, final int n2) {
        this.setMapCenter(new GeoPoint(n, n2));
    }
    
    @Deprecated
    void setMapCenter(final IGeoPoint geoPoint) {
        this.getController().animateTo(geoPoint);
    }
    
    @Deprecated
    public void setMapListener(final MapListener mapListener) {
        this.mListners.add(mapListener);
    }
    
    public void setMapOrientation(final float n) {
        this.setMapOrientation(n, true);
    }
    
    public void setMapOrientation(final float n, final boolean b) {
        this.mapOrientation = n % 360.0f;
        if (b) {
            this.requestLayout();
            this.invalidate();
        }
    }
    
    void setMapScroll(final long mMapScrollX, final long mMapScrollY) {
        this.mMapScrollX = mMapScrollX;
        this.mMapScrollY = mMapScrollY;
        this.requestLayout();
    }
    
    public void setMaxZoomLevel(final Double mMaximumZoomLevel) {
        this.mMaximumZoomLevel = mMaximumZoomLevel;
    }
    
    public void setMinZoomLevel(final Double mMinimumZoomLevel) {
        this.mMinimumZoomLevel = mMinimumZoomLevel;
    }
    
    public void setMultiTouchControls(final boolean b) {
        MultiTouchController<Object> mMultiTouchController;
        if (b) {
            mMultiTouchController = new MultiTouchController<Object>((MultiTouchController.MultiTouchObjectCanvas<Object>)this, false);
        }
        else {
            mMultiTouchController = null;
        }
        this.mMultiTouchController = mMultiTouchController;
    }
    
    protected void setMultiTouchScale(final float n) {
        this.setZoomLevel(Math.log(n) / Math.log(2.0) + this.mStartAnimationZoom);
    }
    
    protected void setMultiTouchScaleCurrentPoint(final float n, final float n2) {
        this.mMultiTouchScaleCurrentPoint = new PointF(n, n2);
    }
    
    protected void setMultiTouchScaleInitPoint(final float n, final float n2) {
        this.mMultiTouchScaleInitPoint.set(n, n2);
        final Point unrotateAndScalePoint = this.getProjection().unrotateAndScalePoint((int)n, (int)n2, null);
        this.getProjection().fromPixels(unrotateAndScalePoint.x, unrotateAndScalePoint.y, this.mMultiTouchScaleGeoPoint);
        this.setMultiTouchScaleCurrentPoint(n, n2);
    }
    
    public void setOverlayManager(final OverlayManager mOverlayManager) {
        this.mOverlayManager = mOverlayManager;
    }
    
    public boolean setPositionAndScale(final Object o, final PositionAndScale positionAndScale, final PointInfo pointInfo) {
        this.setMultiTouchScaleCurrentPoint(positionAndScale.getXOff(), positionAndScale.getYOff());
        this.setMultiTouchScale(positionAndScale.getScale());
        this.requestLayout();
        this.invalidate();
        return true;
    }
    
    @Deprecated
    protected void setProjection(final Projection mProjection) {
        this.mProjection = mProjection;
    }
    
    public void setScrollableAreaLimitDouble(final BoundingBox boundingBox) {
        if (boundingBox == null) {
            this.resetScrollableAreaLimitLatitude();
            this.resetScrollableAreaLimitLongitude();
        }
        else {
            this.setScrollableAreaLimitLatitude(boundingBox.getActualNorth(), boundingBox.getActualSouth(), 0);
            this.setScrollableAreaLimitLongitude(boundingBox.getLonWest(), boundingBox.getLonEast(), 0);
        }
    }
    
    public void setScrollableAreaLimitLatitude(final double mScrollableAreaLimitNorth, final double mScrollableAreaLimitSouth, final int mScrollableAreaLimitExtraPixelHeight) {
        this.mScrollableAreaLimitLatitude = true;
        this.mScrollableAreaLimitNorth = mScrollableAreaLimitNorth;
        this.mScrollableAreaLimitSouth = mScrollableAreaLimitSouth;
        this.mScrollableAreaLimitExtraPixelHeight = mScrollableAreaLimitExtraPixelHeight;
    }
    
    public void setScrollableAreaLimitLongitude(final double mScrollableAreaLimitWest, final double mScrollableAreaLimitEast, final int mScrollableAreaLimitExtraPixelWidth) {
        this.mScrollableAreaLimitLongitude = true;
        this.mScrollableAreaLimitWest = mScrollableAreaLimitWest;
        this.mScrollableAreaLimitEast = mScrollableAreaLimitEast;
        this.mScrollableAreaLimitExtraPixelWidth = mScrollableAreaLimitExtraPixelWidth;
    }
    
    public void setTileProvider(final MapTileProviderBase mTileProvider) {
        this.mTileProvider.detach();
        this.mTileProvider.clearTileCache();
        this.mTileProvider = mTileProvider;
        this.mTileProvider.getTileRequestCompleteHandlers().add(this.mTileRequestCompleteHandler);
        this.updateTileSizeForDensity(this.mTileProvider.getTileSource());
        this.mMapOverlay = new TilesOverlay(this.mTileProvider, this.getContext(), this.horizontalMapRepetitionEnabled, this.verticalMapRepetitionEnabled);
        this.mOverlayManager.setTilesOverlay(this.mMapOverlay);
        this.invalidate();
    }
    
    public void setTileSource(final ITileSource tileSource) {
        this.mTileProvider.setTileSource(tileSource);
        this.updateTileSizeForDensity(tileSource);
        this.checkZoomButtons();
        this.setZoomLevel(this.mZoomLevel);
        this.postInvalidate();
    }
    
    public void setTilesScaleFactor(final float mTilesScaleFactor) {
        this.mTilesScaleFactor = mTilesScaleFactor;
        this.updateTileSizeForDensity(this.getTileProvider().getTileSource());
    }
    
    public void setTilesScaledToDpi(final boolean mTilesScaledToDpi) {
        this.mTilesScaledToDpi = mTilesScaledToDpi;
        this.updateTileSizeForDensity(this.getTileProvider().getTileSource());
    }
    
    public void setUseDataConnection(final boolean useDataConnection) {
        this.mMapOverlay.setUseDataConnection(useDataConnection);
    }
    
    public void setVerticalMapRepetitionEnabled(final boolean b) {
        this.verticalMapRepetitionEnabled = b;
        this.mMapOverlay.setVerticalWrapEnabled(b);
        this.resetProjection();
        this.invalidate();
    }
    
    double setZoomLevel(double max) {
        max = Math.max(this.getMinZoomLevel(), Math.min(this.getMaxZoomLevel(), max));
        final double mZoomLevel = this.mZoomLevel;
        if (max != mZoomLevel) {
            final Scroller mScroller = this.mScroller;
            if (mScroller != null) {
                mScroller.forceFinished(true);
            }
            this.mIsFlinging = false;
        }
        final GeoPoint currentCenter = this.getProjection().getCurrentCenter();
        this.mZoomLevel = max;
        this.setExpectedCenter(currentCenter);
        this.checkZoomButtons();
        final boolean layoutOccurred = this.isLayoutOccurred();
        ZoomEvent zoomEvent = null;
        if (layoutOccurred) {
            this.getController().setCenter(currentCenter);
            final Point point = new Point();
            final Projection projection = this.getProjection();
            final OverlayManager overlayManager = this.getOverlayManager();
            final PointF mMultiTouchScaleInitPoint = this.mMultiTouchScaleInitPoint;
            if (overlayManager.onSnapToItem((int)mMultiTouchScaleInitPoint.x, (int)mMultiTouchScaleInitPoint.y, point, this)) {
                this.getController().animateTo(projection.fromPixels(point.x, point.y, null, false));
            }
            this.mTileProvider.rescaleCache(projection, max, mZoomLevel, this.getScreenRect(this.mRescaleScreenRect));
            this.pauseFling = true;
        }
        if (max != mZoomLevel) {
            for (final MapListener mapListener : this.mListners) {
                if (zoomEvent == null) {
                    zoomEvent = new ZoomEvent(this, max);
                }
                mapListener.onZoom(zoomEvent);
            }
        }
        this.requestLayout();
        this.invalidate();
        return this.mZoomLevel;
    }
    
    public void setZoomRounding(final boolean mZoomRounding) {
        this.mZoomRounding = mZoomRounding;
    }
    
    protected void startAnimation() {
        this.mStartAnimationZoom = this.getZoomLevelDouble();
    }
    
    public boolean useDataConnection() {
        return this.mMapOverlay.useDataConnection();
    }
    
    @Deprecated
    boolean zoomIn() {
        return this.getController().zoomIn();
    }
    
    @Deprecated
    boolean zoomInFixing(final int n, final int n2) {
        return this.getController().zoomInFixing(n, n2);
    }
    
    @Deprecated
    boolean zoomInFixing(final IGeoPoint geoPoint) {
        final Point pixels = this.getProjection().toPixels(geoPoint, null);
        return this.getController().zoomInFixing(pixels.x, pixels.y);
    }
    
    @Deprecated
    boolean zoomOut() {
        return this.getController().zoomOut();
    }
    
    @Deprecated
    boolean zoomOutFixing(final int n, final int n2) {
        return this.getController().zoomOutFixing(n, n2);
    }
    
    @Deprecated
    boolean zoomOutFixing(final IGeoPoint geoPoint) {
        final Point pixels = this.getProjection().toPixels(geoPoint, null);
        return this.zoomOutFixing(pixels.x, pixels.y);
    }
    
    public double zoomToBoundingBox(final BoundingBox boundingBox, final boolean b, int y, double centerLongitude, final Long n) {
        final TileSystem mTileSystem = MapView.mTileSystem;
        final int width = this.getWidth();
        y *= 2;
        final double boundingBoxZoom = mTileSystem.getBoundingBoxZoom(boundingBox, width - y, this.getHeight() - y);
        double a = 0.0;
        Label_0058: {
            if (boundingBoxZoom != Double.MIN_VALUE) {
                a = boundingBoxZoom;
                if (boundingBoxZoom <= centerLongitude) {
                    break Label_0058;
                }
            }
            a = centerLongitude;
        }
        final double min = Math.min(this.getMaxZoomLevel(), Math.max(a, this.getMinZoomLevel()));
        final GeoPoint centerWithDateLine = boundingBox.getCenterWithDateLine();
        final Projection projection = new Projection(min, this.getWidth(), this.getHeight(), centerWithDateLine, this.getMapOrientation(), this.isHorizontalMapRepetitionEnabled(), this.isVerticalMapRepetitionEnabled());
        final Point point = new Point();
        centerLongitude = boundingBox.getCenterLongitude();
        projection.toPixels(new GeoPoint(boundingBox.getActualNorth(), centerLongitude), point);
        y = point.y;
        projection.toPixels(new GeoPoint(boundingBox.getActualSouth(), centerLongitude), point);
        y = (this.getHeight() - point.y - y) / 2;
        if (y != 0) {
            projection.adjustOffsets(0L, y);
            projection.fromPixels(this.getWidth() / 2, this.getHeight() / 2, centerWithDateLine);
        }
        if (b) {
            this.getController().animateTo(centerWithDateLine, min, n);
        }
        else {
            this.getController().setZoom(min);
            this.getController().setCenter(centerWithDateLine);
        }
        return min;
    }
    
    public void zoomToBoundingBox(final BoundingBox boundingBox, final boolean b) {
        this.zoomToBoundingBox(boundingBox, b, 0);
    }
    
    public void zoomToBoundingBox(final BoundingBox boundingBox, final boolean b, final int n) {
        this.zoomToBoundingBox(boundingBox, b, n, this.getMaxZoomLevel(), null);
    }
    
    public static class LayoutParams extends ViewGroup$LayoutParams
    {
        public int alignment;
        public IGeoPoint geoPoint;
        public int offsetX;
        public int offsetY;
        
        public LayoutParams(final int n, final int n2, final IGeoPoint geoPoint, final int alignment, final int offsetX, final int offsetY) {
            super(n, n2);
            if (geoPoint != null) {
                this.geoPoint = geoPoint;
            }
            else {
                this.geoPoint = new GeoPoint(0.0, 0.0);
            }
            this.alignment = alignment;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.geoPoint = new GeoPoint(0.0, 0.0);
            this.alignment = 8;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
        }
    }
    
    private class MapViewDoubleClickListener implements GestureDetector$OnDoubleTapListener
    {
        public boolean onDoubleTap(final MotionEvent motionEvent) {
            if (MapView.this.getOverlayManager().onDoubleTap(motionEvent, MapView.this)) {
                return true;
            }
            if (MapView.this.mZoomController != null && MapView.this.mZoomController.onSingleTapConfirmed(motionEvent)) {
                return true;
            }
            MapView.this.getProjection().rotateAndScalePoint((int)motionEvent.getX(), (int)motionEvent.getY(), MapView.this.mRotateScalePoint);
            final IMapController controller = MapView.this.getController();
            final Point mRotateScalePoint = MapView.this.mRotateScalePoint;
            return controller.zoomInFixing(mRotateScalePoint.x, mRotateScalePoint.y);
        }
        
        public boolean onDoubleTapEvent(final MotionEvent motionEvent) {
            return MapView.this.getOverlayManager().onDoubleTapEvent(motionEvent, MapView.this);
        }
        
        public boolean onSingleTapConfirmed(final MotionEvent motionEvent) {
            return (MapView.this.mZoomController != null && MapView.this.mZoomController.onSingleTapConfirmed(motionEvent)) || MapView.this.getOverlayManager().onSingleTapConfirmed(motionEvent, MapView.this);
        }
    }
    
    private class MapViewGestureDetectorListener implements GestureDetector$OnGestureListener
    {
        public boolean onDown(final MotionEvent motionEvent) {
            final MapView this$0 = MapView.this;
            if (this$0.mIsFlinging) {
                if (this$0.mScroller != null) {
                    MapView.this.mScroller.abortAnimation();
                }
                MapView.this.mIsFlinging = false;
            }
            if (MapView.this.getOverlayManager().onDown(motionEvent, MapView.this)) {
                return true;
            }
            if (MapView.this.mZoomController != null) {
                MapView.this.mZoomController.activate();
            }
            return true;
        }
        
        public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
            if (!MapView.this.enableFling || MapView.this.pauseFling) {
                MapView.this.pauseFling = false;
                return false;
            }
            if (MapView.this.getOverlayManager().onFling(motionEvent, motionEvent2, n, n2, MapView.this)) {
                return true;
            }
            if (MapView.this.mImpossibleFlinging) {
                MapView.this.mImpossibleFlinging = false;
                return false;
            }
            final MapView this$0 = MapView.this;
            this$0.mIsFlinging = true;
            if (this$0.mScroller != null) {
                MapView.this.mScroller.fling((int)MapView.this.getMapScrollX(), (int)MapView.this.getMapScrollY(), (int)(-n), (int)(-n2), Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
            return true;
        }
        
        public void onLongPress(final MotionEvent motionEvent) {
            if (MapView.this.mMultiTouchController != null && MapView.this.mMultiTouchController.isPinching()) {
                return;
            }
            if (MapView.this.mZoomController != null && MapView.this.mZoomController.onLongPress(motionEvent)) {
                return;
            }
            MapView.this.getOverlayManager().onLongPress(motionEvent, MapView.this);
        }
        
        public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
            if (MapView.this.getOverlayManager().onScroll(motionEvent, motionEvent2, n, n2, MapView.this)) {
                return true;
            }
            MapView.this.scrollBy((int)n, (int)n2);
            return true;
        }
        
        public void onShowPress(final MotionEvent motionEvent) {
            MapView.this.getOverlayManager().onShowPress(motionEvent, MapView.this);
        }
        
        public boolean onSingleTapUp(final MotionEvent motionEvent) {
            return MapView.this.getOverlayManager().onSingleTapUp(motionEvent, MapView.this);
        }
    }
    
    private class MapViewZoomListener implements OnZoomListener
    {
        @Override
        public void onZoom(final boolean b) {
            if (b) {
                MapView.this.getController().zoomIn();
            }
            else {
                MapView.this.getController().zoomOut();
            }
        }
    }
    
    public interface OnFirstLayoutListener
    {
        void onFirstLayout(final View p0, final int p1, final int p2, final int p3, final int p4);
    }
}

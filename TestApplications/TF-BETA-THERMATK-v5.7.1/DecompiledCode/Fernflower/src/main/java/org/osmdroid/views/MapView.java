package org.osmdroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Scroller;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.metalev.multitouch.controller.MultiTouchController;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.IStyledTileSource;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.GeometryMath;
import org.osmdroid.util.TileSystem;
import org.osmdroid.util.TileSystemWebMercator;
import org.osmdroid.views.overlay.DefaultOverlayManager;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.TilesOverlay;

public class MapView extends ViewGroup implements IMapView, MultiTouchController.MultiTouchObjectCanvas {
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
   protected List mListners;
   private TilesOverlay mMapOverlay;
   private long mMapScrollX;
   private long mMapScrollY;
   protected Double mMaximumZoomLevel;
   protected Double mMinimumZoomLevel;
   private MultiTouchController mMultiTouchController;
   private PointF mMultiTouchScaleCurrentPoint;
   private final GeoPoint mMultiTouchScaleGeoPoint;
   private final PointF mMultiTouchScaleInitPoint;
   private final LinkedList mOnFirstLayoutListeners;
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

   public MapView(Context var1) {
      this(var1, (MapTileProviderBase)null, (Handler)null, (AttributeSet)null);
   }

   public MapView(Context var1, AttributeSet var2) {
      this(var1, (MapTileProviderBase)null, (Handler)null, var2);
   }

   public MapView(Context var1, MapTileProviderBase var2) {
      this(var1, var2, (Handler)null);
   }

   public MapView(Context var1, MapTileProviderBase var2, Handler var3) {
      this(var1, var2, var3, (AttributeSet)null);
   }

   public MapView(Context var1, MapTileProviderBase var2, Handler var3, AttributeSet var4) {
      this(var1, var2, var3, var4, Configuration.getInstance().isMapViewHardwareAccelerated());
   }

   public MapView(Context var1, MapTileProviderBase var2, Handler var3, AttributeSet var4, boolean var5) {
      super(var1, var4);
      this.mZoomLevel = 0.0D;
      this.mIsAnimating = new AtomicBoolean(false);
      this.mMultiTouchScaleInitPoint = new PointF();
      this.mMultiTouchScaleGeoPoint = new GeoPoint(0.0D, 0.0D);
      this.mapOrientation = 0.0F;
      this.mInvalidateRect = new Rect();
      this.mTilesScaledToDpi = false;
      this.mTilesScaleFactor = 1.0F;
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
      this.setWillNotDraw(false);
      if (this.isInEditMode()) {
         this.mTileRequestCompleteHandler = null;
         this.mController = null;
         this.mZoomController = null;
         this.mScroller = null;
         this.mGestureDetector = null;
      } else {
         if (!var5 && VERSION.SDK_INT >= 11) {
            this.setLayerType(1, (Paint)null);
         }

         this.mController = new MapController(this);
         this.mScroller = new Scroller(var1);
         Object var6 = var2;
         Object var8;
         if (var2 == null) {
            ITileSource var7 = this.getTileSourceFromAttributes(var4);
            if (this.isInEditMode()) {
               var8 = new MapTileProviderArray(var7, (IRegisterReceiver)null, new MapTileModuleProviderBase[0]);
            } else {
               var8 = new MapTileProviderBasic(var1.getApplicationContext(), var7);
            }

            var6 = var8;
         }

         var8 = var3;
         if (var3 == null) {
            var8 = new SimpleInvalidationHandler(this);
         }

         this.mTileRequestCompleteHandler = (Handler)var8;
         this.mTileProvider = (MapTileProviderBase)var6;
         this.mTileProvider.getTileRequestCompleteHandlers().add(this.mTileRequestCompleteHandler);
         this.updateTileSizeForDensity(this.mTileProvider.getTileSource());
         this.mMapOverlay = new TilesOverlay(this.mTileProvider, var1, this.horizontalMapRepetitionEnabled, this.verticalMapRepetitionEnabled);
         this.mOverlayManager = new DefaultOverlayManager(this.mMapOverlay);
         this.mZoomController = new CustomZoomButtonsController(this);
         this.mZoomController.setOnZoomListener(new MapView.MapViewZoomListener());
         this.checkZoomButtons();
         this.mGestureDetector = new GestureDetector(var1, new MapView.MapViewGestureDetectorListener());
         this.mGestureDetector.setOnDoubleTapListener(new MapView.MapViewDoubleClickListener());
         if (Configuration.getInstance().isMapViewRecyclerFriendly() && VERSION.SDK_INT >= 16) {
            this.setHasTransientState(true);
         }

         this.mZoomController.setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
      }
   }

   private void checkZoomButtons() {
      this.mZoomController.setZoomInEnabled(this.canZoomIn());
      this.mZoomController.setZoomOutEnabled(this.canZoomOut());
   }

   private ITileSource getTileSourceFromAttributes(AttributeSet var1) {
      OnlineTileSourceBase var2 = TileSourceFactory.DEFAULT_TILE_SOURCE;
      Object var3 = var2;
      if (var1 != null) {
         String var4 = var1.getAttributeValue((String)null, "tilesource");
         var3 = var2;
         if (var4 != null) {
            try {
               var3 = TileSourceFactory.getTileSource(var4);
               StringBuilder var9 = new StringBuilder();
               var9.append("Using tile source specified in layout attributes: ");
               var9.append(var3);
               Log.i("OsmDroid", var9.toString());
            } catch (IllegalArgumentException var5) {
               StringBuilder var8 = new StringBuilder();
               var8.append("Invalid tile source specified in layout attributes: ");
               var8.append(var2);
               Log.w("OsmDroid", var8.toString());
               var3 = var2;
            }
         }
      }

      StringBuilder var6;
      if (var1 != null && var3 instanceof IStyledTileSource) {
         String var7 = var1.getAttributeValue((String)null, "style");
         if (var7 == null) {
            Log.i("OsmDroid", "Using default style: 1");
         } else {
            var6 = new StringBuilder();
            var6.append("Using style specified in layout attributes: ");
            var6.append(var7);
            Log.i("OsmDroid", var6.toString());
            ((IStyledTileSource)var3).setStyle(var7);
         }
      }

      var6 = new StringBuilder();
      var6.append("Using tile source: ");
      var6.append(((ITileSource)var3).name());
      Log.i("OsmDroid", var6.toString());
      return (ITileSource)var3;
   }

   public static TileSystem getTileSystem() {
      return mTileSystem;
   }

   private void invalidateMapCoordinates(int var1, int var2, int var3, int var4, boolean var5) {
      this.mInvalidateRect.set(var1, var2, var3, var4);
      var1 = this.getWidth() / 2;
      var2 = this.getHeight() / 2;
      if (this.getMapOrientation() != 0.0F) {
         GeometryMath.getBoundingBoxForRotatatedRectangle(this.mInvalidateRect, var1, var2, this.getMapOrientation() + 180.0F, this.mInvalidateRect);
      }

      if (var5) {
         Rect var6 = this.mInvalidateRect;
         super.postInvalidate(var6.left, var6.top, var6.right, var6.bottom);
      } else {
         super.invalidate(this.mInvalidateRect);
      }

   }

   private void resetProjection() {
      this.mProjection = null;
   }

   private MotionEvent rotateTouchEvent(MotionEvent var1) {
      if (this.getMapOrientation() == 0.0F) {
         return var1;
      } else {
         MotionEvent var2 = MotionEvent.obtain(var1);
         if (VERSION.SDK_INT < 11) {
            this.getProjection().unrotateAndScalePoint((int)var1.getX(), (int)var1.getY(), this.mRotateScalePoint);
            Point var3 = this.mRotateScalePoint;
            var2.setLocation((float)var3.x, (float)var3.y);
         } else {
            var2.transform(this.getProjection().getInvertedScaleRotateCanvasMatrix());
         }

         return var2;
      }
   }

   public static void setTileSystem(TileSystem var0) {
      mTileSystem = var0;
   }

   private void updateTileSizeForDensity(ITileSource var1) {
      int var2 = var1.getTileSizePixels();
      float var3 = this.getResources().getDisplayMetrics().density;
      float var4 = (float)var2;
      var3 = var3 * 256.0F / var4;
      if (this.isTilesScaledToDpi()) {
         var3 *= this.mTilesScaleFactor;
      } else {
         var3 = this.mTilesScaleFactor;
      }

      var2 = (int)(var4 * var3);
      if (Configuration.getInstance().isDebugMapView()) {
         StringBuilder var5 = new StringBuilder();
         var5.append("Scaling tiles to ");
         var5.append(var2);
         Log.d("OsmDroid", var5.toString());
      }

      TileSystem.setTileSize(var2);
   }

   public void addMapListener(MapListener var1) {
      this.mListners.add(var1);
   }

   public void addOnFirstLayoutListener(MapView.OnFirstLayoutListener var1) {
      if (!this.isLayoutOccurred()) {
         this.mOnFirstLayoutListeners.add(var1);
      }

   }

   public boolean canZoomIn() {
      boolean var1;
      if (this.mZoomLevel < this.getMaxZoomLevel()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean canZoomOut() {
      boolean var1;
      if (this.mZoomLevel > this.getMinZoomLevel()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof MapView.LayoutParams;
   }

   public void computeScroll() {
      Scroller var1 = this.mScroller;
      if (var1 != null) {
         if (this.mIsFlinging) {
            if (var1.computeScrollOffset()) {
               if (this.mScroller.isFinished()) {
                  this.mIsFlinging = false;
               } else {
                  this.scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
                  this.postInvalidate();
               }

            }
         }
      }
   }

   public boolean dispatchTouchEvent(MotionEvent var1) {
      if (Configuration.getInstance().isDebugMapView()) {
         StringBuilder var2 = new StringBuilder();
         var2.append("dispatchTouchEvent(");
         var2.append(var1);
         var2.append(")");
         Log.d("OsmDroid", var2.toString());
      }

      MotionEvent var26 = this.rotateTouchEvent(var1);

      Throwable var10000;
      label532: {
         boolean var10001;
         label533: {
            try {
               if (!super.dispatchTouchEvent(var1)) {
                  break label533;
               }

               if (Configuration.getInstance().isDebugMapView()) {
                  Log.d("OsmDroid", "super handled onTouchEvent");
               }
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label532;
            }

            if (var26 != var1) {
               var26.recycle();
            }

            return true;
         }

         boolean var3;
         try {
            var3 = this.getOverlayManager().onTouchEvent(var26, this);
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label532;
         }

         if (var3) {
            if (var26 != var1) {
               var26.recycle();
            }

            return true;
         }

         boolean var4;
         label514: {
            label513: {
               try {
                  if (this.mMultiTouchController != null && this.mMultiTouchController.onTouchEvent(var1)) {
                     if (Configuration.getInstance().isDebugMapView()) {
                        Log.d("OsmDroid", "mMultiTouchController handled onTouchEvent");
                     }
                     break label513;
                  }
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label532;
               }

               var4 = false;
               break label514;
            }

            var4 = true;
         }

         label503: {
            try {
               if (!this.mGestureDetector.onTouchEvent(var26)) {
                  break label503;
               }

               if (Configuration.getInstance().isDebugMapView()) {
                  Log.d("OsmDroid", "mGestureDetector handled onTouchEvent");
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label532;
            }

            var4 = true;
         }

         if (var4) {
            if (var26 != var1) {
               var26.recycle();
            }

            return true;
         }

         if (var26 != var1) {
            var26.recycle();
         }

         if (Configuration.getInstance().isDebugMapView()) {
            Log.d("OsmDroid", "no-one handled onTouchEvent");
         }

         return false;
      }

      Throwable var5 = var10000;
      if (var26 != var1) {
         var26.recycle();
      }

      throw var5;
   }

   protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
      return new MapView.LayoutParams(-2, -2, (IGeoPoint)null, 8, 0, 0);
   }

   public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new MapView.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return new MapView.LayoutParams(var1);
   }

   public BoundingBox getBoundingBox() {
      return this.getProjection().getBoundingBox();
   }

   public IMapController getController() {
      return this.mController;
   }

   public Object getDraggableObjectAtPoint(MultiTouchController.PointInfo var1) {
      if (this.isAnimating()) {
         return null;
      } else {
         this.setMultiTouchScaleInitPoint(var1.getX(), var1.getY());
         return this;
      }
   }

   GeoPoint getExpectedCenter() {
      return this.mCenter;
   }

   public Rect getIntrinsicScreenRect(Rect var1) {
      Rect var2 = var1;
      if (var1 == null) {
         var2 = new Rect();
      }

      var2.set(0, 0, this.getWidth(), this.getHeight());
      return var2;
   }

   public double getLatitudeSpanDouble() {
      return this.getBoundingBox().getLatitudeSpan();
   }

   public double getLongitudeSpanDouble() {
      return this.getBoundingBox().getLongitudeSpan();
   }

   public IGeoPoint getMapCenter() {
      return this.getMapCenter((GeoPoint)null);
   }

   public IGeoPoint getMapCenter(GeoPoint var1) {
      return this.getProjection().fromPixels(this.getWidth() / 2, this.getHeight() / 2, var1, false);
   }

   public float getMapOrientation() {
      return this.mapOrientation;
   }

   public TilesOverlay getMapOverlay() {
      return this.mMapOverlay;
   }

   @Deprecated
   public float getMapScale() {
      return 1.0F;
   }

   public long getMapScrollX() {
      return this.mMapScrollX;
   }

   public long getMapScrollY() {
      return this.mMapScrollY;
   }

   public double getMaxZoomLevel() {
      Double var1 = this.mMaximumZoomLevel;
      double var2;
      if (var1 == null) {
         var2 = (double)this.mMapOverlay.getMaximumZoomLevel();
      } else {
         var2 = var1;
      }

      return var2;
   }

   public double getMinZoomLevel() {
      Double var1 = this.mMinimumZoomLevel;
      double var2;
      if (var1 == null) {
         var2 = (double)this.mMapOverlay.getMinimumZoomLevel();
      } else {
         var2 = var1;
      }

      return var2;
   }

   public OverlayManager getOverlayManager() {
      return this.mOverlayManager;
   }

   public List getOverlays() {
      return this.getOverlayManager().overlays();
   }

   public void getPositionAndScale(Object var1, MultiTouchController.PositionAndScale var2) {
      this.startAnimation();
      PointF var3 = this.mMultiTouchScaleInitPoint;
      var2.set(var3.x, var3.y, true, 1.0F, false, 0.0F, 0.0F, false, 0.0F);
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

   public MapViewRepository getRepository() {
      return this.mRepository;
   }

   public Rect getScreenRect(Rect var1) {
      var1 = this.getIntrinsicScreenRect(var1);
      if (this.getMapOrientation() != 0.0F && this.getMapOrientation() != 180.0F) {
         GeometryMath.getBoundingBoxForRotatatedRectangle(var1, var1.centerX(), var1.centerY(), this.getMapOrientation(), var1);
      }

      return var1;
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
   public double getZoomLevel(boolean var1) {
      return this.getZoomLevelDouble();
   }

   @Deprecated
   public int getZoomLevel() {
      return (int)this.getZoomLevelDouble();
   }

   public double getZoomLevelDouble() {
      return this.mZoomLevel;
   }

   public void invalidateMapCoordinates(int var1, int var2, int var3, int var4) {
      this.invalidateMapCoordinates(var1, var2, var3, var4, false);
   }

   public void invalidateMapCoordinates(Rect var1) {
      this.invalidateMapCoordinates(var1.left, var1.top, var1.right, var1.bottom, false);
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

   protected void myOnLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.resetProjection();
      int var6 = this.getChildCount();

      for(int var7 = 0; var7 < var6; ++var7) {
         View var8 = this.getChildAt(var7);
         if (var8.getVisibility() != 8) {
            MapView.LayoutParams var9 = (MapView.LayoutParams)var8.getLayoutParams();
            int var10 = var8.getMeasuredHeight();
            int var11 = var8.getMeasuredWidth();
            this.getProjection().toPixels(var9.geoPoint, this.mLayoutPoint);
            Point var22;
            if (this.getMapOrientation() != 0.0F) {
               Projection var12 = this.getProjection();
               Point var13 = this.mLayoutPoint;
               var22 = var12.rotateAndScalePoint(var13.x, var13.y, (Point)null);
               var13 = this.mLayoutPoint;
               var13.x = var22.x;
               var13.y = var22.y;
            }

            long var14;
            long var18;
            label78: {
               long var16;
               label77: {
                  label95: {
                     int var20;
                     label70: {
                        label69: {
                           var22 = this.mLayoutPoint;
                           var14 = (long)var22.x;
                           var16 = (long)var22.y;
                           switch(var9.alignment) {
                           case 1:
                              var14 += (long)this.getPaddingLeft();
                              var18 = var16 + (long)this.getPaddingTop();
                              break label78;
                           case 2:
                              var14 = (long)this.getPaddingLeft() + var14 - (long)(var11 / 2);
                              var20 = this.getPaddingTop();
                              break label70;
                           case 3:
                              var14 = (long)this.getPaddingLeft() + var14 - (long)var11;
                              var20 = this.getPaddingTop();
                              break label70;
                           case 4:
                              var14 += (long)this.getPaddingLeft();
                              var18 = (long)this.getPaddingTop() + var16;
                              var16 = (long)(var10 / 2);
                              break label95;
                           case 5:
                              var18 = (long)this.getPaddingLeft() + var14 - (long)(var11 / 2);
                              var14 = (long)this.getPaddingTop() + var16;
                              var20 = var10 / 2;
                              break;
                           case 6:
                              var18 = (long)this.getPaddingLeft() + var14 - (long)var11;
                              var14 = (long)this.getPaddingTop() + var16;
                              var20 = var10 / 2;
                              break;
                           case 7:
                              var14 += (long)this.getPaddingLeft();
                              var18 = (long)this.getPaddingTop() + var16;
                              var16 = (long)var10;
                              break label95;
                           case 8:
                              var18 = (long)this.getPaddingLeft() + var14 - (long)(var11 / 2);
                              var20 = this.getPaddingTop();
                              break label69;
                           case 9:
                              var18 = (long)this.getPaddingLeft() + var14 - (long)var11;
                              var20 = this.getPaddingTop();
                              break label69;
                           default:
                              var18 = var16;
                              break label78;
                           }

                           var16 = (long)var20;
                           break label77;
                        }

                        var14 = (long)var20 + var16;
                        var16 = (long)var10;
                        break label77;
                     }

                     var18 = var16 + (long)var20;
                     break label78;
                  }

                  var18 -= var16;
                  break label78;
               }

               var16 = var14 - var16;
               var14 = var18;
               var18 = var16;
            }

            var14 += (long)var9.offsetX;
            var18 += (long)var9.offsetY;
            var8.layout(TileSystem.truncateToInt(var14), TileSystem.truncateToInt(var18), TileSystem.truncateToInt(var14 + (long)var11), TileSystem.truncateToInt(var18 + (long)var10));
         }
      }

      if (!this.isLayoutOccurred()) {
         this.mLayoutOccurred = true;
         Iterator var21 = this.mOnFirstLayoutListeners.iterator();

         while(var21.hasNext()) {
            ((MapView.OnFirstLayoutListener)var21.next()).onFirstLayout(this, var2, var3, var4, var5);
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
      CustomZoomButtonsController var1 = this.mZoomController;
      if (var1 != null) {
         var1.onDetach();
      }

      Handler var2 = this.mTileRequestCompleteHandler;
      if (var2 instanceof SimpleInvalidationHandler) {
         ((SimpleInvalidationHandler)var2).destroy();
      }

      this.mTileRequestCompleteHandler = null;
      Projection var3 = this.mProjection;
      if (var3 != null) {
         var3.detach();
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

   public void onDraw(Canvas var1) {
      long var2 = System.currentTimeMillis();
      this.resetProjection();
      this.getProjection().save(var1, true, false);

      try {
         this.getOverlayManager().onDraw(var1, this);
         this.getProjection().restore(var1, false);
         if (this.mZoomController != null) {
            this.mZoomController.draw(var1);
         }
      } catch (Exception var6) {
         Log.e("OsmDroid", "error dispatchDraw, probably in edit mode", var6);
      }

      if (Configuration.getInstance().isDebugMapView()) {
         long var4 = System.currentTimeMillis();
         StringBuilder var7 = new StringBuilder();
         var7.append("Rendering overall: ");
         var7.append(var4 - var2);
         var7.append("ms");
         Log.d("OsmDroid", var7.toString());
      }

   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3;
      if (!this.getOverlayManager().onKeyDown(var1, var2, this) && !super.onKeyDown(var1, var2)) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      boolean var3;
      if (!this.getOverlayManager().onKeyUp(var1, var2, this) && !super.onKeyUp(var1, var2)) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.myOnLayout(var1, var2, var3, var4, var5);
   }

   protected void onMeasure(int var1, int var2) {
      this.measureChildren(var1, var2);
      super.onMeasure(var1, var2);
   }

   public void onPause() {
      this.getOverlayManager().onPause();
   }

   public void onResume() {
      this.getOverlayManager().onResume();
   }

   public boolean onTouchEvent(MotionEvent var1) {
      return false;
   }

   public boolean onTrackballEvent(MotionEvent var1) {
      if (this.getOverlayManager().onTrackballEvent(var1, this)) {
         return true;
      } else {
         this.scrollBy((int)(var1.getX() * 25.0F), (int)(var1.getY() * 25.0F));
         return super.onTrackballEvent(var1);
      }
   }

   public void postInvalidateMapCoordinates(int var1, int var2, int var3, int var4) {
      this.invalidateMapCoordinates(var1, var2, var3, var4, true);
   }

   public void removeMapListener(MapListener var1) {
      this.mListners.remove(var1);
   }

   public void removeOnFirstLayoutListener(MapView.OnFirstLayoutListener var1) {
      this.mOnFirstLayoutListeners.remove(var1);
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
      this.mTilesScaleFactor = 1.0F;
      this.updateTileSizeForDensity(this.getTileProvider().getTileSource());
   }

   public void scrollBy(int var1, int var2) {
      this.scrollTo((int)(this.getMapScrollX() + (long)var1), (int)(this.getMapScrollY() + (long)var2));
   }

   public void scrollTo(int var1, int var2) {
      this.setMapScroll((long)var1, (long)var2);
      this.resetProjection();
      this.invalidate();
      if (this.getMapOrientation() != 0.0F) {
         this.myOnLayout(true, this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
      }

      ScrollEvent var3 = null;

      MapListener var5;
      for(Iterator var4 = this.mListners.iterator(); var4.hasNext(); var5.onScroll(var3)) {
         var5 = (MapListener)var4.next();
         if (var3 == null) {
            var3 = new ScrollEvent(this, var1, var2);
         }
      }

   }

   public void selectObject(Object var1, MultiTouchController.PointInfo var2) {
      if (this.mZoomRounding) {
         this.mZoomLevel = (double)Math.round(this.mZoomLevel);
         this.invalidate();
      }

      this.resetMultiTouchScale();
   }

   public void setBackgroundColor(int var1) {
      this.mMapOverlay.setLoadingBackgroundColor(var1);
      this.invalidate();
   }

   @Deprecated
   public void setBuiltInZoomControls(boolean var1) {
      CustomZoomButtonsController var2 = this.getZoomController();
      CustomZoomButtonsController.Visibility var3;
      if (var1) {
         var3 = CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT;
      } else {
         var3 = CustomZoomButtonsController.Visibility.NEVER;
      }

      var2.setVisibility(var3);
   }

   public void setDestroyMode(boolean var1) {
      this.mDestroyModeOnDetach = var1;
   }

   public void setExpectedCenter(IGeoPoint var1) {
      this.setExpectedCenter(var1, 0L, 0L);
   }

   public void setExpectedCenter(IGeoPoint var1, long var2, long var4) {
      GeoPoint var6 = this.getProjection().getCurrentCenter();
      this.mCenter = (GeoPoint)var1;
      this.setMapScroll(-var2, -var4);
      this.resetProjection();
      if (!this.getProjection().getCurrentCenter().equals(var6)) {
         ScrollEvent var9 = null;

         MapListener var7;
         for(Iterator var8 = this.mListners.iterator(); var8.hasNext(); var7.onScroll(var9)) {
            var7 = (MapListener)var8.next();
            if (var9 == null) {
               var9 = new ScrollEvent(this, 0, 0);
            }
         }
      }

      this.invalidate();
   }

   public void setFlingEnabled(boolean var1) {
      this.enableFling = var1;
   }

   public void setHorizontalMapRepetitionEnabled(boolean var1) {
      this.horizontalMapRepetitionEnabled = var1;
      this.mMapOverlay.setHorizontalWrapEnabled(var1);
      this.resetProjection();
      this.invalidate();
   }

   @Deprecated
   public void setInitCenter(IGeoPoint var1) {
      this.setExpectedCenter(var1);
   }

   @Deprecated
   void setMapCenter(double var1, double var3) {
      this.setMapCenter(new GeoPoint(var1, var3));
   }

   @Deprecated
   void setMapCenter(int var1, int var2) {
      this.setMapCenter(new GeoPoint(var1, var2));
   }

   @Deprecated
   void setMapCenter(IGeoPoint var1) {
      this.getController().animateTo(var1);
   }

   @Deprecated
   public void setMapListener(MapListener var1) {
      this.mListners.add(var1);
   }

   public void setMapOrientation(float var1) {
      this.setMapOrientation(var1, true);
   }

   public void setMapOrientation(float var1, boolean var2) {
      this.mapOrientation = var1 % 360.0F;
      if (var2) {
         this.requestLayout();
         this.invalidate();
      }

   }

   void setMapScroll(long var1, long var3) {
      this.mMapScrollX = var1;
      this.mMapScrollY = var3;
      this.requestLayout();
   }

   public void setMaxZoomLevel(Double var1) {
      this.mMaximumZoomLevel = var1;
   }

   public void setMinZoomLevel(Double var1) {
      this.mMinimumZoomLevel = var1;
   }

   public void setMultiTouchControls(boolean var1) {
      MultiTouchController var2;
      if (var1) {
         var2 = new MultiTouchController(this, false);
      } else {
         var2 = null;
      }

      this.mMultiTouchController = var2;
   }

   protected void setMultiTouchScale(float var1) {
      this.setZoomLevel(Math.log((double)var1) / Math.log(2.0D) + this.mStartAnimationZoom);
   }

   protected void setMultiTouchScaleCurrentPoint(float var1, float var2) {
      this.mMultiTouchScaleCurrentPoint = new PointF(var1, var2);
   }

   protected void setMultiTouchScaleInitPoint(float var1, float var2) {
      this.mMultiTouchScaleInitPoint.set(var1, var2);
      Point var3 = this.getProjection().unrotateAndScalePoint((int)var1, (int)var2, (Point)null);
      this.getProjection().fromPixels(var3.x, var3.y, this.mMultiTouchScaleGeoPoint);
      this.setMultiTouchScaleCurrentPoint(var1, var2);
   }

   public void setOverlayManager(OverlayManager var1) {
      this.mOverlayManager = var1;
   }

   public boolean setPositionAndScale(Object var1, MultiTouchController.PositionAndScale var2, MultiTouchController.PointInfo var3) {
      this.setMultiTouchScaleCurrentPoint(var2.getXOff(), var2.getYOff());
      this.setMultiTouchScale(var2.getScale());
      this.requestLayout();
      this.invalidate();
      return true;
   }

   @Deprecated
   protected void setProjection(Projection var1) {
      this.mProjection = var1;
   }

   public void setScrollableAreaLimitDouble(BoundingBox var1) {
      if (var1 == null) {
         this.resetScrollableAreaLimitLatitude();
         this.resetScrollableAreaLimitLongitude();
      } else {
         this.setScrollableAreaLimitLatitude(var1.getActualNorth(), var1.getActualSouth(), 0);
         this.setScrollableAreaLimitLongitude(var1.getLonWest(), var1.getLonEast(), 0);
      }

   }

   public void setScrollableAreaLimitLatitude(double var1, double var3, int var5) {
      this.mScrollableAreaLimitLatitude = true;
      this.mScrollableAreaLimitNorth = var1;
      this.mScrollableAreaLimitSouth = var3;
      this.mScrollableAreaLimitExtraPixelHeight = var5;
   }

   public void setScrollableAreaLimitLongitude(double var1, double var3, int var5) {
      this.mScrollableAreaLimitLongitude = true;
      this.mScrollableAreaLimitWest = var1;
      this.mScrollableAreaLimitEast = var3;
      this.mScrollableAreaLimitExtraPixelWidth = var5;
   }

   public void setTileProvider(MapTileProviderBase var1) {
      this.mTileProvider.detach();
      this.mTileProvider.clearTileCache();
      this.mTileProvider = var1;
      this.mTileProvider.getTileRequestCompleteHandlers().add(this.mTileRequestCompleteHandler);
      this.updateTileSizeForDensity(this.mTileProvider.getTileSource());
      this.mMapOverlay = new TilesOverlay(this.mTileProvider, this.getContext(), this.horizontalMapRepetitionEnabled, this.verticalMapRepetitionEnabled);
      this.mOverlayManager.setTilesOverlay(this.mMapOverlay);
      this.invalidate();
   }

   public void setTileSource(ITileSource var1) {
      this.mTileProvider.setTileSource(var1);
      this.updateTileSizeForDensity(var1);
      this.checkZoomButtons();
      this.setZoomLevel(this.mZoomLevel);
      this.postInvalidate();
   }

   public void setTilesScaleFactor(float var1) {
      this.mTilesScaleFactor = var1;
      this.updateTileSizeForDensity(this.getTileProvider().getTileSource());
   }

   public void setTilesScaledToDpi(boolean var1) {
      this.mTilesScaledToDpi = var1;
      this.updateTileSizeForDensity(this.getTileProvider().getTileSource());
   }

   public void setUseDataConnection(boolean var1) {
      this.mMapOverlay.setUseDataConnection(var1);
   }

   public void setVerticalMapRepetitionEnabled(boolean var1) {
      this.verticalMapRepetitionEnabled = var1;
      this.mMapOverlay.setVerticalWrapEnabled(var1);
      this.resetProjection();
      this.invalidate();
   }

   double setZoomLevel(double var1) {
      var1 = Math.max(this.getMinZoomLevel(), Math.min(this.getMaxZoomLevel(), var1));
      double var3 = this.mZoomLevel;
      if (var1 != var3) {
         Scroller var5 = this.mScroller;
         if (var5 != null) {
            var5.forceFinished(true);
         }

         this.mIsFlinging = false;
      }

      GeoPoint var6 = this.getProjection().getCurrentCenter();
      this.mZoomLevel = var1;
      this.setExpectedCenter(var6);
      this.checkZoomButtons();
      boolean var7 = this.isLayoutOccurred();
      ZoomEvent var13 = null;
      if (var7) {
         this.getController().setCenter(var6);
         Point var8 = new Point();
         Projection var14 = this.getProjection();
         OverlayManager var9 = this.getOverlayManager();
         PointF var10 = this.mMultiTouchScaleInitPoint;
         if (var9.onSnapToItem((int)var10.x, (int)var10.y, var8, this)) {
            IGeoPoint var11 = var14.fromPixels(var8.x, var8.y, (GeoPoint)null, false);
            this.getController().animateTo(var11);
         }

         this.mTileProvider.rescaleCache(var14, var1, var3, this.getScreenRect(this.mRescaleScreenRect));
         this.pauseFling = true;
      }

      MapListener var12;
      if (var1 != var3) {
         for(Iterator var15 = this.mListners.iterator(); var15.hasNext(); var12.onZoom(var13)) {
            var12 = (MapListener)var15.next();
            if (var13 == null) {
               var13 = new ZoomEvent(this, var1);
            }
         }
      }

      this.requestLayout();
      this.invalidate();
      return this.mZoomLevel;
   }

   public void setZoomRounding(boolean var1) {
      this.mZoomRounding = var1;
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
   boolean zoomInFixing(int var1, int var2) {
      return this.getController().zoomInFixing(var1, var2);
   }

   @Deprecated
   boolean zoomInFixing(IGeoPoint var1) {
      Point var2 = this.getProjection().toPixels(var1, (Point)null);
      return this.getController().zoomInFixing(var2.x, var2.y);
   }

   @Deprecated
   boolean zoomOut() {
      return this.getController().zoomOut();
   }

   @Deprecated
   boolean zoomOutFixing(int var1, int var2) {
      return this.getController().zoomOutFixing(var1, var2);
   }

   @Deprecated
   boolean zoomOutFixing(IGeoPoint var1) {
      Point var2 = this.getProjection().toPixels(var1, (Point)null);
      return this.zoomOutFixing(var2.x, var2.y);
   }

   public double zoomToBoundingBox(BoundingBox var1, boolean var2, int var3, double var4, Long var6) {
      int var8;
      double var11;
      label20: {
         TileSystem var7 = mTileSystem;
         var8 = this.getWidth();
         var3 *= 2;
         double var9 = var7.getBoundingBoxZoom(var1, var8 - var3, this.getHeight() - var3);
         if (var9 != Double.MIN_VALUE) {
            var11 = var9;
            if (var9 <= var4) {
               break label20;
            }
         }

         var11 = var4;
      }

      var11 = Math.min(this.getMaxZoomLevel(), Math.max(var11, this.getMinZoomLevel()));
      GeoPoint var13 = var1.getCenterWithDateLine();
      Projection var15 = new Projection(var11, this.getWidth(), this.getHeight(), var13, this.getMapOrientation(), this.isHorizontalMapRepetitionEnabled(), this.isVerticalMapRepetitionEnabled());
      Point var14 = new Point();
      var4 = var1.getCenterLongitude();
      var15.toPixels(new GeoPoint(var1.getActualNorth(), var4), var14);
      var3 = var14.y;
      var15.toPixels(new GeoPoint(var1.getActualSouth(), var4), var14);
      var8 = var14.y;
      var3 = (this.getHeight() - var8 - var3) / 2;
      if (var3 != 0) {
         var15.adjustOffsets(0L, (long)var3);
         var15.fromPixels(this.getWidth() / 2, this.getHeight() / 2, var13);
      }

      if (var2) {
         this.getController().animateTo(var13, var11, var6);
      } else {
         this.getController().setZoom(var11);
         this.getController().setCenter(var13);
      }

      return var11;
   }

   public void zoomToBoundingBox(BoundingBox var1, boolean var2) {
      this.zoomToBoundingBox(var1, var2, 0);
   }

   public void zoomToBoundingBox(BoundingBox var1, boolean var2, int var3) {
      this.zoomToBoundingBox(var1, var2, var3, this.getMaxZoomLevel(), (Long)null);
   }

   public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
      public int alignment;
      public IGeoPoint geoPoint;
      public int offsetX;
      public int offsetY;

      public LayoutParams(int var1, int var2, IGeoPoint var3, int var4, int var5, int var6) {
         super(var1, var2);
         if (var3 != null) {
            this.geoPoint = var3;
         } else {
            this.geoPoint = new GeoPoint(0.0D, 0.0D);
         }

         this.alignment = var4;
         this.offsetX = var5;
         this.offsetY = var6;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         this.geoPoint = new GeoPoint(0.0D, 0.0D);
         this.alignment = 8;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }
   }

   private class MapViewDoubleClickListener implements OnDoubleTapListener {
      private MapViewDoubleClickListener() {
      }

      // $FF: synthetic method
      MapViewDoubleClickListener(Object var2) {
         this();
      }

      public boolean onDoubleTap(MotionEvent var1) {
         if (MapView.this.getOverlayManager().onDoubleTap(var1, MapView.this)) {
            return true;
         } else if (MapView.this.mZoomController != null && MapView.this.mZoomController.onSingleTapConfirmed(var1)) {
            return true;
         } else {
            MapView.this.getProjection().rotateAndScalePoint((int)var1.getX(), (int)var1.getY(), MapView.this.mRotateScalePoint);
            IMapController var2 = MapView.this.getController();
            Point var3 = MapView.this.mRotateScalePoint;
            return var2.zoomInFixing(var3.x, var3.y);
         }
      }

      public boolean onDoubleTapEvent(MotionEvent var1) {
         return MapView.this.getOverlayManager().onDoubleTapEvent(var1, MapView.this);
      }

      public boolean onSingleTapConfirmed(MotionEvent var1) {
         if (MapView.this.mZoomController != null && MapView.this.mZoomController.onSingleTapConfirmed(var1)) {
            return true;
         } else {
            return MapView.this.getOverlayManager().onSingleTapConfirmed(var1, MapView.this);
         }
      }
   }

   private class MapViewGestureDetectorListener implements OnGestureListener {
      private MapViewGestureDetectorListener() {
      }

      // $FF: synthetic method
      MapViewGestureDetectorListener(Object var2) {
         this();
      }

      public boolean onDown(MotionEvent var1) {
         MapView var2 = MapView.this;
         if (var2.mIsFlinging) {
            if (var2.mScroller != null) {
               MapView.this.mScroller.abortAnimation();
            }

            MapView.this.mIsFlinging = false;
         }

         if (MapView.this.getOverlayManager().onDown(var1, MapView.this)) {
            return true;
         } else {
            if (MapView.this.mZoomController != null) {
               MapView.this.mZoomController.activate();
            }

            return true;
         }
      }

      public boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4) {
         if (MapView.this.enableFling && !MapView.this.pauseFling) {
            if (MapView.this.getOverlayManager().onFling(var1, var2, var3, var4, MapView.this)) {
               return true;
            } else if (MapView.this.mImpossibleFlinging) {
               MapView.this.mImpossibleFlinging = false;
               return false;
            } else {
               MapView var5 = MapView.this;
               var5.mIsFlinging = true;
               if (var5.mScroller != null) {
                  MapView.this.mScroller.fling((int)MapView.this.getMapScrollX(), (int)MapView.this.getMapScrollY(), (int)(-var3), (int)(-var4), Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
               }

               return true;
            }
         } else {
            MapView.this.pauseFling = false;
            return false;
         }
      }

      public void onLongPress(MotionEvent var1) {
         if (MapView.this.mMultiTouchController == null || !MapView.this.mMultiTouchController.isPinching()) {
            if (MapView.this.mZoomController == null || !MapView.this.mZoomController.onLongPress(var1)) {
               MapView.this.getOverlayManager().onLongPress(var1, MapView.this);
            }
         }
      }

      public boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4) {
         if (MapView.this.getOverlayManager().onScroll(var1, var2, var3, var4, MapView.this)) {
            return true;
         } else {
            MapView.this.scrollBy((int)var3, (int)var4);
            return true;
         }
      }

      public void onShowPress(MotionEvent var1) {
         MapView.this.getOverlayManager().onShowPress(var1, MapView.this);
      }

      public boolean onSingleTapUp(MotionEvent var1) {
         return MapView.this.getOverlayManager().onSingleTapUp(var1, MapView.this);
      }
   }

   private class MapViewZoomListener implements CustomZoomButtonsController.OnZoomListener {
      private MapViewZoomListener() {
      }

      // $FF: synthetic method
      MapViewZoomListener(Object var2) {
         this();
      }

      public void onZoom(boolean var1) {
         if (var1) {
            MapView.this.getController().zoomIn();
         } else {
            MapView.this.getController().zoomOut();
         }

      }
   }

   public interface OnFirstLayoutListener {
      void onFirstLayout(View var1, int var2, int var3, int var4, int var5);
   }
}

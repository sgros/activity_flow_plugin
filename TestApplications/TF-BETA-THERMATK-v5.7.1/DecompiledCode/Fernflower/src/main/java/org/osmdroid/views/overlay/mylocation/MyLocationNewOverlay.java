package org.osmdroid.views.overlay.mylocation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import java.util.Iterator;
import java.util.LinkedList;
import org.osmdroid.api.IMapController;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.R$drawable;
import org.osmdroid.library.R$string;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.IOverlayMenuProvider;
import org.osmdroid.views.overlay.Overlay;

public class MyLocationNewOverlay extends Overlay implements IMyLocationConsumer, IOverlayMenuProvider, Overlay.Snappable {
   public static final int MENU_MY_LOCATION = Overlay.getSafeMenuId();
   protected boolean enableAutoStop;
   protected Paint mCirclePaint;
   protected Bitmap mDirectionArrowBitmap;
   protected float mDirectionArrowCenterX;
   protected float mDirectionArrowCenterY;
   protected boolean mDrawAccuracyEnabled;
   private final Point mDrawPixel;
   private final GeoPoint mGeoPoint;
   private Handler mHandler;
   private Object mHandlerToken;
   protected boolean mIsFollowing;
   private boolean mIsLocationEnabled;
   private Location mLocation;
   private IMapController mMapController;
   protected MapView mMapView;
   public IMyLocationProvider mMyLocationProvider;
   private boolean mOptionsMenuEnabled;
   protected Paint mPaint;
   protected Bitmap mPersonBitmap;
   protected final PointF mPersonHotspot;
   private final LinkedList mRunOnFirstFix;
   protected final float mScale;
   private final Point mSnapPixel;
   private boolean wasEnabledOnPause;

   public MyLocationNewOverlay(MapView var1) {
      this(new GpsMyLocationProvider(var1.getContext()), var1);
   }

   public MyLocationNewOverlay(IMyLocationProvider var1, MapView var2) {
      this.mPaint = new Paint();
      this.mCirclePaint = new Paint();
      this.mRunOnFirstFix = new LinkedList();
      this.mDrawPixel = new Point();
      this.mSnapPixel = new Point();
      this.mHandlerToken = new Object();
      this.enableAutoStop = true;
      this.mGeoPoint = new GeoPoint(0, 0);
      this.mIsLocationEnabled = false;
      this.mIsFollowing = false;
      this.mDrawAccuracyEnabled = true;
      this.mOptionsMenuEnabled = true;
      this.wasEnabledOnPause = false;
      this.mScale = var2.getContext().getResources().getDisplayMetrics().density;
      this.mMapView = var2;
      this.mMapController = var2.getController();
      this.mCirclePaint.setARGB(0, 100, 100, 255);
      this.mCirclePaint.setAntiAlias(true);
      this.mPaint.setFilterBitmap(true);
      this.setDirectionArrow(((BitmapDrawable)var2.getContext().getResources().getDrawable(R$drawable.person)).getBitmap(), ((BitmapDrawable)var2.getContext().getResources().getDrawable(R$drawable.round_navigation_white_48)).getBitmap());
      float var3 = this.mScale;
      this.mPersonHotspot = new PointF(24.0F * var3 + 0.5F, var3 * 39.0F + 0.5F);
      this.mHandler = new Handler(Looper.getMainLooper());
      this.setMyLocationProvider(var1);
   }

   public void disableFollowLocation() {
      this.mMapController.stopAnimation(false);
      this.mIsFollowing = false;
   }

   public void disableMyLocation() {
      this.mIsLocationEnabled = false;
      this.stopLocationProvider();
      MapView var1 = this.mMapView;
      if (var1 != null) {
         var1.postInvalidate();
      }

   }

   public void draw(Canvas var1, Projection var2) {
      if (this.mLocation != null && this.isMyLocationEnabled()) {
         this.drawMyLocation(var1, var2, this.mLocation);
      }

   }

   protected void drawMyLocation(Canvas var1, Projection var2, Location var3) {
      var2.toPixels(this.mGeoPoint, this.mDrawPixel);
      float var4;
      Point var7;
      if (this.mDrawAccuracyEnabled) {
         var4 = var3.getAccuracy() / (float)TileSystem.GroundResolution(var3.getLatitude(), var2.getZoomLevel());
         this.mCirclePaint.setAlpha(50);
         this.mCirclePaint.setStyle(Style.FILL);
         var7 = this.mDrawPixel;
         var1.drawCircle((float)var7.x, (float)var7.y, var4, this.mCirclePaint);
         this.mCirclePaint.setAlpha(150);
         this.mCirclePaint.setStyle(Style.STROKE);
         var7 = this.mDrawPixel;
         var1.drawCircle((float)var7.x, (float)var7.y, var4, this.mCirclePaint);
      }

      Bitmap var8;
      if (var3.hasBearing()) {
         var1.save();
         float var5 = var3.getBearing();
         var4 = var5;
         if (var5 >= 360.0F) {
            var4 = var5 - 360.0F;
         }

         var7 = this.mDrawPixel;
         var1.rotate(var4, (float)var7.x, (float)var7.y);
         var8 = this.mDirectionArrowBitmap;
         Point var9 = this.mDrawPixel;
         var1.drawBitmap(var8, (float)var9.x - this.mDirectionArrowCenterX, (float)var9.y - this.mDirectionArrowCenterY, this.mPaint);
         var1.restore();
      } else {
         var1.save();
         var4 = -this.mMapView.getMapOrientation();
         var7 = this.mDrawPixel;
         var1.rotate(var4, (float)var7.x, (float)var7.y);
         var8 = this.mPersonBitmap;
         Point var6 = this.mDrawPixel;
         var4 = (float)var6.x;
         PointF var10 = this.mPersonHotspot;
         var1.drawBitmap(var8, var4 - var10.x, (float)var6.y - var10.y, this.mPaint);
         var1.restore();
      }

   }

   public void enableFollowLocation() {
      this.mIsFollowing = true;
      if (this.isMyLocationEnabled()) {
         Location var1 = this.mMyLocationProvider.getLastKnownLocation();
         if (var1 != null) {
            this.setLocation(var1);
         }
      }

      MapView var2 = this.mMapView;
      if (var2 != null) {
         var2.postInvalidate();
      }

   }

   public boolean enableMyLocation() {
      return this.enableMyLocation(this.mMyLocationProvider);
   }

   public boolean enableMyLocation(IMyLocationProvider var1) {
      this.setMyLocationProvider(var1);
      boolean var2 = this.mMyLocationProvider.startLocationProvider(this);
      this.mIsLocationEnabled = var2;
      if (var2) {
         Location var3 = this.mMyLocationProvider.getLastKnownLocation();
         if (var3 != null) {
            this.setLocation(var3);
         }
      }

      MapView var4 = this.mMapView;
      if (var4 != null) {
         var4.postInvalidate();
      }

      return var2;
   }

   public boolean getEnableAutoStop() {
      return this.enableAutoStop;
   }

   public Location getLastFix() {
      return this.mLocation;
   }

   public GeoPoint getMyLocation() {
      Location var1 = this.mLocation;
      return var1 == null ? null : new GeoPoint(var1);
   }

   public IMyLocationProvider getMyLocationProvider() {
      return this.mMyLocationProvider;
   }

   public boolean isDrawAccuracyEnabled() {
      return this.mDrawAccuracyEnabled;
   }

   public boolean isFollowLocationEnabled() {
      return this.mIsFollowing;
   }

   public boolean isMyLocationEnabled() {
      return this.mIsLocationEnabled;
   }

   public boolean isOptionsMenuEnabled() {
      return this.mOptionsMenuEnabled;
   }

   public boolean onCreateOptionsMenu(Menu var1, int var2, MapView var3) {
      var1.add(0, MENU_MY_LOCATION + var2, 0, var3.getContext().getResources().getString(R$string.my_location)).setIcon(var3.getContext().getResources().getDrawable(R$drawable.ic_menu_mylocation)).setCheckable(true);
      return true;
   }

   public void onDetach(MapView var1) {
      this.disableMyLocation();
      this.mMapView = null;
      this.mMapController = null;
      this.mHandler = null;
      this.mCirclePaint = null;
      this.mHandlerToken = null;
      this.mLocation = null;
      this.mMapController = null;
      IMyLocationProvider var2 = this.mMyLocationProvider;
      if (var2 != null) {
         var2.destroy();
      }

      this.mMyLocationProvider = null;
      super.onDetach(var1);
   }

   public void onLocationChanged(final Location var1, IMyLocationProvider var2) {
      if (var1 != null) {
         Handler var3 = this.mHandler;
         if (var3 != null) {
            var3.postAtTime(new Runnable() {
               public void run() {
                  MyLocationNewOverlay.this.setLocation(var1);
                  Iterator var1x = MyLocationNewOverlay.this.mRunOnFirstFix.iterator();

                  while(var1x.hasNext()) {
                     (new Thread((Runnable)var1x.next())).start();
                  }

                  MyLocationNewOverlay.this.mRunOnFirstFix.clear();
               }
            }, this.mHandlerToken, 0L);
         }
      }

   }

   public boolean onOptionsItemSelected(MenuItem var1, int var2, MapView var3) {
      if (var1.getItemId() - var2 == MENU_MY_LOCATION) {
         if (this.isMyLocationEnabled()) {
            this.disableFollowLocation();
            this.disableMyLocation();
         } else {
            this.enableFollowLocation();
            this.enableMyLocation();
         }

         return true;
      } else {
         return false;
      }
   }

   public void onPause() {
      this.wasEnabledOnPause = this.mIsFollowing;
      this.disableMyLocation();
      super.onPause();
   }

   public boolean onPrepareOptionsMenu(Menu var1, int var2, MapView var3) {
      var1.findItem(MENU_MY_LOCATION + var2).setChecked(this.isMyLocationEnabled());
      return false;
   }

   public void onResume() {
      super.onResume();
      if (this.wasEnabledOnPause) {
         this.enableFollowLocation();
      }

      this.enableMyLocation();
   }

   public boolean onSnapToItem(int var1, int var2, Point var3, IMapView var4) {
      Location var12 = this.mLocation;
      boolean var5 = false;
      boolean var6 = false;
      if (var12 != null) {
         this.mMapView.getProjection().toPixels(this.mGeoPoint, this.mSnapPixel);
         Point var13 = this.mSnapPixel;
         var3.x = var13.x;
         var3.y = var13.y;
         double var7 = (double)(var1 - var13.x);
         double var9 = (double)(var2 - var13.y);
         Double.isNaN(var7);
         Double.isNaN(var7);
         Double.isNaN(var9);
         Double.isNaN(var9);
         if (var7 * var7 + var9 * var9 < 64.0D) {
            var6 = true;
         }

         var5 = var6;
         if (Configuration.getInstance().isDebugMode()) {
            StringBuilder var11 = new StringBuilder();
            var11.append("snap=");
            var11.append(var6);
            Log.d("OsmDroid", var11.toString());
            var5 = var6;
         }
      }

      return var5;
   }

   public boolean onTouchEvent(MotionEvent var1, MapView var2) {
      if (var1.getAction() == 0 && this.enableAutoStop) {
         this.disableFollowLocation();
      } else if (var1.getAction() == 2 && this.isFollowLocationEnabled()) {
         return true;
      }

      return super.onTouchEvent(var1, var2);
   }

   public boolean runOnFirstFix(Runnable var1) {
      if (this.mMyLocationProvider != null && this.mLocation != null) {
         (new Thread(var1)).start();
         return true;
      } else {
         this.mRunOnFirstFix.addLast(var1);
         return false;
      }
   }

   public void setDirectionArrow(Bitmap var1, Bitmap var2) {
      this.mPersonBitmap = var1;
      this.mDirectionArrowBitmap = var2;
      this.mDirectionArrowCenterX = (float)this.mDirectionArrowBitmap.getWidth() / 2.0F - 0.5F;
      this.mDirectionArrowCenterY = (float)this.mDirectionArrowBitmap.getHeight() / 2.0F - 0.5F;
   }

   public void setDrawAccuracyEnabled(boolean var1) {
      this.mDrawAccuracyEnabled = var1;
   }

   public void setEnableAutoStop(boolean var1) {
      this.enableAutoStop = var1;
   }

   protected void setLocation(Location var1) {
      this.mLocation = var1;
      this.mGeoPoint.setCoords(this.mLocation.getLatitude(), this.mLocation.getLongitude());
      if (this.mIsFollowing) {
         this.mMapController.animateTo(this.mGeoPoint);
      } else {
         this.mMapView.postInvalidate();
      }

   }

   protected void setMyLocationProvider(IMyLocationProvider var1) {
      if (var1 != null) {
         if (this.isMyLocationEnabled()) {
            this.stopLocationProvider();
         }

         this.mMyLocationProvider = var1;
      } else {
         throw new RuntimeException("You must pass an IMyLocationProvider to setMyLocationProvider()");
      }
   }

   public void setOptionsMenuEnabled(boolean var1) {
      this.mOptionsMenuEnabled = var1;
   }

   public void setPersonHotspot(float var1, float var2) {
      this.mPersonHotspot.set(var1, var2);
   }

   public void setPersonIcon(Bitmap var1) {
      this.mPersonBitmap = var1;
   }

   protected void stopLocationProvider() {
      IMyLocationProvider var1 = this.mMyLocationProvider;
      if (var1 != null) {
         var1.stopLocationProvider();
      }

      Handler var2 = this.mHandler;
      if (var2 != null) {
         Object var3 = this.mHandlerToken;
         if (var3 != null) {
            var2.removeCallbacksAndMessages(var3);
         }
      }

   }
}

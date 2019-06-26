// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay.mylocation;

import android.view.MotionEvent;
import android.util.Log;
import org.osmdroid.config.Configuration;
import org.osmdroid.api.IMapView;
import android.view.MenuItem;
import java.util.Iterator;
import org.osmdroid.library.R$string;
import android.view.Menu;
import android.graphics.Paint$Style;
import org.osmdroid.util.TileSystem;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.Projection;
import android.graphics.Canvas;
import android.os.Looper;
import org.osmdroid.library.R$drawable;
import android.graphics.drawable.BitmapDrawable;
import java.util.LinkedList;
import android.graphics.PointF;
import org.osmdroid.views.MapView;
import org.osmdroid.api.IMapController;
import android.location.Location;
import android.os.Handler;
import org.osmdroid.util.GeoPoint;
import android.graphics.Point;
import android.graphics.Bitmap;
import android.graphics.Paint;
import org.osmdroid.views.overlay.IOverlayMenuProvider;
import org.osmdroid.views.overlay.Overlay;

public class MyLocationNewOverlay extends Overlay implements IMyLocationConsumer, IOverlayMenuProvider, Snappable
{
    public static final int MENU_MY_LOCATION;
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
    private final LinkedList<Runnable> mRunOnFirstFix;
    protected final float mScale;
    private final Point mSnapPixel;
    private boolean wasEnabledOnPause;
    
    static {
        MENU_MY_LOCATION = Overlay.getSafeMenuId();
    }
    
    public MyLocationNewOverlay(final MapView mapView) {
        this(new GpsMyLocationProvider(mapView.getContext()), mapView);
    }
    
    public MyLocationNewOverlay(final IMyLocationProvider myLocationProvider, final MapView mMapView) {
        this.mPaint = new Paint();
        this.mCirclePaint = new Paint();
        this.mRunOnFirstFix = new LinkedList<Runnable>();
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
        this.mScale = mMapView.getContext().getResources().getDisplayMetrics().density;
        this.mMapView = mMapView;
        this.mMapController = mMapView.getController();
        this.mCirclePaint.setARGB(0, 100, 100, 255);
        this.mCirclePaint.setAntiAlias(true);
        this.mPaint.setFilterBitmap(true);
        this.setDirectionArrow(((BitmapDrawable)mMapView.getContext().getResources().getDrawable(R$drawable.person)).getBitmap(), ((BitmapDrawable)mMapView.getContext().getResources().getDrawable(R$drawable.round_navigation_white_48)).getBitmap());
        final float mScale = this.mScale;
        this.mPersonHotspot = new PointF(24.0f * mScale + 0.5f, mScale * 39.0f + 0.5f);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.setMyLocationProvider(myLocationProvider);
    }
    
    public void disableFollowLocation() {
        this.mMapController.stopAnimation(false);
        this.mIsFollowing = false;
    }
    
    public void disableMyLocation() {
        this.mIsLocationEnabled = false;
        this.stopLocationProvider();
        final MapView mMapView = this.mMapView;
        if (mMapView != null) {
            mMapView.postInvalidate();
        }
    }
    
    @Override
    public void draw(final Canvas canvas, final Projection projection) {
        if (this.mLocation != null && this.isMyLocationEnabled()) {
            this.drawMyLocation(canvas, projection, this.mLocation);
        }
    }
    
    protected void drawMyLocation(final Canvas canvas, final Projection projection, final Location location) {
        projection.toPixels(this.mGeoPoint, this.mDrawPixel);
        if (this.mDrawAccuracyEnabled) {
            final float n = location.getAccuracy() / (float)TileSystem.GroundResolution(location.getLatitude(), projection.getZoomLevel());
            this.mCirclePaint.setAlpha(50);
            this.mCirclePaint.setStyle(Paint$Style.FILL);
            final Point mDrawPixel = this.mDrawPixel;
            canvas.drawCircle((float)mDrawPixel.x, (float)mDrawPixel.y, n, this.mCirclePaint);
            this.mCirclePaint.setAlpha(150);
            this.mCirclePaint.setStyle(Paint$Style.STROKE);
            final Point mDrawPixel2 = this.mDrawPixel;
            canvas.drawCircle((float)mDrawPixel2.x, (float)mDrawPixel2.y, n, this.mCirclePaint);
        }
        if (location.hasBearing()) {
            canvas.save();
            float bearing;
            final float n2 = bearing = location.getBearing();
            if (n2 >= 360.0f) {
                bearing = n2 - 360.0f;
            }
            final Point mDrawPixel3 = this.mDrawPixel;
            canvas.rotate(bearing, (float)mDrawPixel3.x, (float)mDrawPixel3.y);
            final Bitmap mDirectionArrowBitmap = this.mDirectionArrowBitmap;
            final Point mDrawPixel4 = this.mDrawPixel;
            canvas.drawBitmap(mDirectionArrowBitmap, mDrawPixel4.x - this.mDirectionArrowCenterX, mDrawPixel4.y - this.mDirectionArrowCenterY, this.mPaint);
            canvas.restore();
        }
        else {
            canvas.save();
            final float n3 = -this.mMapView.getMapOrientation();
            final Point mDrawPixel5 = this.mDrawPixel;
            canvas.rotate(n3, (float)mDrawPixel5.x, (float)mDrawPixel5.y);
            final Bitmap mPersonBitmap = this.mPersonBitmap;
            final Point mDrawPixel6 = this.mDrawPixel;
            final float n4 = (float)mDrawPixel6.x;
            final PointF mPersonHotspot = this.mPersonHotspot;
            canvas.drawBitmap(mPersonBitmap, n4 - mPersonHotspot.x, mDrawPixel6.y - mPersonHotspot.y, this.mPaint);
            canvas.restore();
        }
    }
    
    public void enableFollowLocation() {
        this.mIsFollowing = true;
        if (this.isMyLocationEnabled()) {
            final Location lastKnownLocation = this.mMyLocationProvider.getLastKnownLocation();
            if (lastKnownLocation != null) {
                this.setLocation(lastKnownLocation);
            }
        }
        final MapView mMapView = this.mMapView;
        if (mMapView != null) {
            mMapView.postInvalidate();
        }
    }
    
    public boolean enableMyLocation() {
        return this.enableMyLocation(this.mMyLocationProvider);
    }
    
    public boolean enableMyLocation(final IMyLocationProvider myLocationProvider) {
        this.setMyLocationProvider(myLocationProvider);
        final boolean startLocationProvider = this.mMyLocationProvider.startLocationProvider(this);
        this.mIsLocationEnabled = startLocationProvider;
        if (startLocationProvider) {
            final Location lastKnownLocation = this.mMyLocationProvider.getLastKnownLocation();
            if (lastKnownLocation != null) {
                this.setLocation(lastKnownLocation);
            }
        }
        final MapView mMapView = this.mMapView;
        if (mMapView != null) {
            mMapView.postInvalidate();
        }
        return startLocationProvider;
    }
    
    public boolean getEnableAutoStop() {
        return this.enableAutoStop;
    }
    
    public Location getLastFix() {
        return this.mLocation;
    }
    
    public GeoPoint getMyLocation() {
        final Location mLocation = this.mLocation;
        if (mLocation == null) {
            return null;
        }
        return new GeoPoint(mLocation);
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
    
    public boolean onCreateOptionsMenu(final Menu menu, final int n, final MapView mapView) {
        menu.add(0, MyLocationNewOverlay.MENU_MY_LOCATION + n, 0, (CharSequence)mapView.getContext().getResources().getString(R$string.my_location)).setIcon(mapView.getContext().getResources().getDrawable(R$drawable.ic_menu_mylocation)).setCheckable(true);
        return true;
    }
    
    @Override
    public void onDetach(final MapView mapView) {
        this.disableMyLocation();
        this.mMapView = null;
        this.mMapController = null;
        this.mHandler = null;
        this.mCirclePaint = null;
        this.mHandlerToken = null;
        this.mLocation = null;
        this.mMapController = null;
        final IMyLocationProvider mMyLocationProvider = this.mMyLocationProvider;
        if (mMyLocationProvider != null) {
            mMyLocationProvider.destroy();
        }
        this.mMyLocationProvider = null;
        super.onDetach(mapView);
    }
    
    @Override
    public void onLocationChanged(final Location location, final IMyLocationProvider myLocationProvider) {
        if (location != null) {
            final Handler mHandler = this.mHandler;
            if (mHandler != null) {
                mHandler.postAtTime((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        MyLocationNewOverlay.this.setLocation(location);
                        final Iterator iterator = MyLocationNewOverlay.this.mRunOnFirstFix.iterator();
                        while (iterator.hasNext()) {
                            new Thread(iterator.next()).start();
                        }
                        MyLocationNewOverlay.this.mRunOnFirstFix.clear();
                    }
                }, this.mHandlerToken, 0L);
            }
        }
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem, final int n, final MapView mapView) {
        if (menuItem.getItemId() - n == MyLocationNewOverlay.MENU_MY_LOCATION) {
            if (this.isMyLocationEnabled()) {
                this.disableFollowLocation();
                this.disableMyLocation();
            }
            else {
                this.enableFollowLocation();
                this.enableMyLocation();
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void onPause() {
        this.wasEnabledOnPause = this.mIsFollowing;
        this.disableMyLocation();
        super.onPause();
    }
    
    public boolean onPrepareOptionsMenu(final Menu menu, final int n, final MapView mapView) {
        menu.findItem(MyLocationNewOverlay.MENU_MY_LOCATION + n).setChecked(this.isMyLocationEnabled());
        return false;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (this.wasEnabledOnPause) {
            this.enableFollowLocation();
        }
        this.enableMyLocation();
    }
    
    @Override
    public boolean onSnapToItem(final int n, final int n2, final Point point, final IMapView mapView) {
        final Location mLocation = this.mLocation;
        int n3 = 0;
        int b = 0;
        if (mLocation != null) {
            this.mMapView.getProjection().toPixels(this.mGeoPoint, this.mSnapPixel);
            final Point mSnapPixel = this.mSnapPixel;
            point.x = mSnapPixel.x;
            point.y = mSnapPixel.y;
            final double n4 = n - mSnapPixel.x;
            final double n5 = n2 - mSnapPixel.y;
            Double.isNaN(n4);
            Double.isNaN(n4);
            Double.isNaN(n5);
            Double.isNaN(n5);
            if (n4 * n4 + n5 * n5 < 64.0) {
                b = 1;
            }
            n3 = b;
            if (Configuration.getInstance().isDebugMode()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("snap=");
                sb.append((boolean)(b != 0));
                Log.d("OsmDroid", sb.toString());
                n3 = b;
            }
        }
        return n3 != 0;
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent motionEvent, final MapView mapView) {
        if (motionEvent.getAction() == 0 && this.enableAutoStop) {
            this.disableFollowLocation();
        }
        else if (motionEvent.getAction() == 2 && this.isFollowLocationEnabled()) {
            return true;
        }
        return super.onTouchEvent(motionEvent, mapView);
    }
    
    public boolean runOnFirstFix(final Runnable runnable) {
        if (this.mMyLocationProvider != null && this.mLocation != null) {
            new Thread(runnable).start();
            return true;
        }
        this.mRunOnFirstFix.addLast(runnable);
        return false;
    }
    
    public void setDirectionArrow(final Bitmap mPersonBitmap, final Bitmap mDirectionArrowBitmap) {
        this.mPersonBitmap = mPersonBitmap;
        this.mDirectionArrowBitmap = mDirectionArrowBitmap;
        this.mDirectionArrowCenterX = this.mDirectionArrowBitmap.getWidth() / 2.0f - 0.5f;
        this.mDirectionArrowCenterY = this.mDirectionArrowBitmap.getHeight() / 2.0f - 0.5f;
    }
    
    public void setDrawAccuracyEnabled(final boolean mDrawAccuracyEnabled) {
        this.mDrawAccuracyEnabled = mDrawAccuracyEnabled;
    }
    
    public void setEnableAutoStop(final boolean enableAutoStop) {
        this.enableAutoStop = enableAutoStop;
    }
    
    protected void setLocation(final Location mLocation) {
        this.mLocation = mLocation;
        this.mGeoPoint.setCoords(this.mLocation.getLatitude(), this.mLocation.getLongitude());
        if (this.mIsFollowing) {
            this.mMapController.animateTo(this.mGeoPoint);
        }
        else {
            this.mMapView.postInvalidate();
        }
    }
    
    protected void setMyLocationProvider(final IMyLocationProvider mMyLocationProvider) {
        if (mMyLocationProvider != null) {
            if (this.isMyLocationEnabled()) {
                this.stopLocationProvider();
            }
            this.mMyLocationProvider = mMyLocationProvider;
            return;
        }
        throw new RuntimeException("You must pass an IMyLocationProvider to setMyLocationProvider()");
    }
    
    public void setOptionsMenuEnabled(final boolean mOptionsMenuEnabled) {
        this.mOptionsMenuEnabled = mOptionsMenuEnabled;
    }
    
    public void setPersonHotspot(final float n, final float n2) {
        this.mPersonHotspot.set(n, n2);
    }
    
    public void setPersonIcon(final Bitmap mPersonBitmap) {
        this.mPersonBitmap = mPersonBitmap;
    }
    
    protected void stopLocationProvider() {
        final IMyLocationProvider mMyLocationProvider = this.mMyLocationProvider;
        if (mMyLocationProvider != null) {
            mMyLocationProvider.stopLocationProvider();
        }
        final Handler mHandler = this.mHandler;
        if (mHandler != null) {
            final Object mHandlerToken = this.mHandlerToken;
            if (mHandlerToken != null) {
                mHandler.removeCallbacksAndMessages(mHandlerToken);
            }
        }
    }
}

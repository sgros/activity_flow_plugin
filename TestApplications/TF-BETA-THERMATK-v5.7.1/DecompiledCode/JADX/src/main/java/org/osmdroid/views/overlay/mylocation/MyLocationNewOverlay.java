package org.osmdroid.views.overlay.mylocation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import com.google.android.exoplayer2.util.NalUnitUtil;
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
import org.osmdroid.views.overlay.Overlay.Snappable;

public class MyLocationNewOverlay extends Overlay implements IMyLocationConsumer, IOverlayMenuProvider, Snappable {
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
    private final LinkedList<Runnable> mRunOnFirstFix;
    protected final float mScale;
    private final Point mSnapPixel;
    private boolean wasEnabledOnPause;

    public MyLocationNewOverlay(MapView mapView) {
        this(new GpsMyLocationProvider(mapView.getContext()), mapView);
    }

    public MyLocationNewOverlay(IMyLocationProvider iMyLocationProvider, MapView mapView) {
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
        this.mScale = mapView.getContext().getResources().getDisplayMetrics().density;
        this.mMapView = mapView;
        this.mMapController = mapView.getController();
        this.mCirclePaint.setARGB(0, 100, 100, NalUnitUtil.EXTENDED_SAR);
        this.mCirclePaint.setAntiAlias(true);
        this.mPaint.setFilterBitmap(true);
        setDirectionArrow(((BitmapDrawable) mapView.getContext().getResources().getDrawable(R$drawable.person)).getBitmap(), ((BitmapDrawable) mapView.getContext().getResources().getDrawable(R$drawable.round_navigation_white_48)).getBitmap());
        float f = this.mScale;
        this.mPersonHotspot = new PointF((24.0f * f) + 0.5f, (f * 39.0f) + 0.5f);
        this.mHandler = new Handler(Looper.getMainLooper());
        setMyLocationProvider(iMyLocationProvider);
    }

    public void setDirectionArrow(Bitmap bitmap, Bitmap bitmap2) {
        this.mPersonBitmap = bitmap;
        this.mDirectionArrowBitmap = bitmap2;
        this.mDirectionArrowCenterX = (((float) this.mDirectionArrowBitmap.getWidth()) / 2.0f) - 0.5f;
        this.mDirectionArrowCenterY = (((float) this.mDirectionArrowBitmap.getHeight()) / 2.0f) - 0.5f;
    }

    public void onResume() {
        super.onResume();
        if (this.wasEnabledOnPause) {
            enableFollowLocation();
        }
        enableMyLocation();
    }

    public void onPause() {
        this.wasEnabledOnPause = this.mIsFollowing;
        disableMyLocation();
        super.onPause();
    }

    public void onDetach(MapView mapView) {
        disableMyLocation();
        this.mMapView = null;
        this.mMapController = null;
        this.mHandler = null;
        this.mCirclePaint = null;
        this.mHandlerToken = null;
        this.mLocation = null;
        this.mMapController = null;
        IMyLocationProvider iMyLocationProvider = this.mMyLocationProvider;
        if (iMyLocationProvider != null) {
            iMyLocationProvider.destroy();
        }
        this.mMyLocationProvider = null;
        super.onDetach(mapView);
    }

    public void setDrawAccuracyEnabled(boolean z) {
        this.mDrawAccuracyEnabled = z;
    }

    public boolean isDrawAccuracyEnabled() {
        return this.mDrawAccuracyEnabled;
    }

    public IMyLocationProvider getMyLocationProvider() {
        return this.mMyLocationProvider;
    }

    /* Access modifiers changed, original: protected */
    public void setMyLocationProvider(IMyLocationProvider iMyLocationProvider) {
        if (iMyLocationProvider != null) {
            if (isMyLocationEnabled()) {
                stopLocationProvider();
            }
            this.mMyLocationProvider = iMyLocationProvider;
            return;
        }
        throw new RuntimeException("You must pass an IMyLocationProvider to setMyLocationProvider()");
    }

    public void setPersonHotspot(float f, float f2) {
        this.mPersonHotspot.set(f, f2);
    }

    /* Access modifiers changed, original: protected */
    public void drawMyLocation(Canvas canvas, Projection projection, Location location) {
        float accuracy;
        projection.toPixels(this.mGeoPoint, this.mDrawPixel);
        if (this.mDrawAccuracyEnabled) {
            accuracy = location.getAccuracy() / ((float) TileSystem.GroundResolution(location.getLatitude(), projection.getZoomLevel()));
            this.mCirclePaint.setAlpha(50);
            this.mCirclePaint.setStyle(Style.FILL);
            Point point = this.mDrawPixel;
            canvas.drawCircle((float) point.x, (float) point.y, accuracy, this.mCirclePaint);
            this.mCirclePaint.setAlpha(150);
            this.mCirclePaint.setStyle(Style.STROKE);
            point = this.mDrawPixel;
            canvas.drawCircle((float) point.x, (float) point.y, accuracy, this.mCirclePaint);
        }
        float bearing;
        Point point2;
        Bitmap bitmap;
        if (location.hasBearing()) {
            canvas.save();
            bearing = location.getBearing();
            if (bearing >= 360.0f) {
                bearing -= 360.0f;
            }
            point2 = this.mDrawPixel;
            canvas.rotate(bearing, (float) point2.x, (float) point2.y);
            bitmap = this.mDirectionArrowBitmap;
            point2 = this.mDrawPixel;
            canvas.drawBitmap(bitmap, ((float) point2.x) - this.mDirectionArrowCenterX, ((float) point2.y) - this.mDirectionArrowCenterY, this.mPaint);
            canvas.restore();
            return;
        }
        canvas.save();
        bearing = -this.mMapView.getMapOrientation();
        point2 = this.mDrawPixel;
        canvas.rotate(bearing, (float) point2.x, (float) point2.y);
        bitmap = this.mPersonBitmap;
        point2 = this.mDrawPixel;
        accuracy = (float) point2.x;
        PointF pointF = this.mPersonHotspot;
        canvas.drawBitmap(bitmap, accuracy - pointF.x, ((float) point2.y) - pointF.y, this.mPaint);
        canvas.restore();
    }

    public void draw(Canvas canvas, Projection projection) {
        if (this.mLocation != null && isMyLocationEnabled()) {
            drawMyLocation(canvas, projection, this.mLocation);
        }
    }

    public boolean onSnapToItem(int i, int i2, Point point, IMapView iMapView) {
        boolean z = false;
        if (this.mLocation != null) {
            this.mMapView.getProjection().toPixels(this.mGeoPoint, this.mSnapPixel);
            Point point2 = this.mSnapPixel;
            point.x = point2.x;
            point.y = point2.y;
            double d = (double) (i - point2.x);
            double d2 = (double) (i2 - point2.y);
            Double.isNaN(d);
            Double.isNaN(d);
            d *= d;
            Double.isNaN(d2);
            Double.isNaN(d2);
            if (d + (d2 * d2) < 64.0d) {
                z = true;
            }
            if (Configuration.getInstance().isDebugMode()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("snap=");
                stringBuilder.append(z);
                Log.d("OsmDroid", stringBuilder.toString());
            }
        }
        return z;
    }

    public void setEnableAutoStop(boolean z) {
        this.enableAutoStop = z;
    }

    public boolean getEnableAutoStop() {
        return this.enableAutoStop;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (motionEvent.getAction() == 0 && this.enableAutoStop) {
            disableFollowLocation();
        } else if (motionEvent.getAction() == 2 && isFollowLocationEnabled()) {
            return true;
        }
        return super.onTouchEvent(motionEvent, mapView);
    }

    public void setOptionsMenuEnabled(boolean z) {
        this.mOptionsMenuEnabled = z;
    }

    public boolean isOptionsMenuEnabled() {
        return this.mOptionsMenuEnabled;
    }

    public boolean onCreateOptionsMenu(Menu menu, int i, MapView mapView) {
        menu.add(0, MENU_MY_LOCATION + i, 0, mapView.getContext().getResources().getString(R$string.my_location)).setIcon(mapView.getContext().getResources().getDrawable(R$drawable.ic_menu_mylocation)).setCheckable(true);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu, int i, MapView mapView) {
        menu.findItem(MENU_MY_LOCATION + i).setChecked(isMyLocationEnabled());
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem, int i, MapView mapView) {
        if (menuItem.getItemId() - i != MENU_MY_LOCATION) {
            return false;
        }
        if (isMyLocationEnabled()) {
            disableFollowLocation();
            disableMyLocation();
        } else {
            enableFollowLocation();
            enableMyLocation();
        }
        return true;
    }

    public GeoPoint getMyLocation() {
        Location location = this.mLocation;
        if (location == null) {
            return null;
        }
        return new GeoPoint(location);
    }

    public Location getLastFix() {
        return this.mLocation;
    }

    public void enableFollowLocation() {
        this.mIsFollowing = true;
        if (isMyLocationEnabled()) {
            Location lastKnownLocation = this.mMyLocationProvider.getLastKnownLocation();
            if (lastKnownLocation != null) {
                setLocation(lastKnownLocation);
            }
        }
        MapView mapView = this.mMapView;
        if (mapView != null) {
            mapView.postInvalidate();
        }
    }

    public void disableFollowLocation() {
        this.mMapController.stopAnimation(false);
        this.mIsFollowing = false;
    }

    public boolean isFollowLocationEnabled() {
        return this.mIsFollowing;
    }

    public void onLocationChanged(final Location location, IMyLocationProvider iMyLocationProvider) {
        if (location != null) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.postAtTime(new Runnable() {
                    public void run() {
                        MyLocationNewOverlay.this.setLocation(location);
                        Iterator it = MyLocationNewOverlay.this.mRunOnFirstFix.iterator();
                        while (it.hasNext()) {
                            new Thread((Runnable) it.next()).start();
                        }
                        MyLocationNewOverlay.this.mRunOnFirstFix.clear();
                    }
                }, this.mHandlerToken, 0);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void setLocation(Location location) {
        this.mLocation = location;
        this.mGeoPoint.setCoords(this.mLocation.getLatitude(), this.mLocation.getLongitude());
        if (this.mIsFollowing) {
            this.mMapController.animateTo(this.mGeoPoint);
        } else {
            this.mMapView.postInvalidate();
        }
    }

    public boolean enableMyLocation(IMyLocationProvider iMyLocationProvider) {
        setMyLocationProvider(iMyLocationProvider);
        boolean startLocationProvider = this.mMyLocationProvider.startLocationProvider(this);
        this.mIsLocationEnabled = startLocationProvider;
        if (startLocationProvider) {
            Location lastKnownLocation = this.mMyLocationProvider.getLastKnownLocation();
            if (lastKnownLocation != null) {
                setLocation(lastKnownLocation);
            }
        }
        MapView mapView = this.mMapView;
        if (mapView != null) {
            mapView.postInvalidate();
        }
        return startLocationProvider;
    }

    public boolean enableMyLocation() {
        return enableMyLocation(this.mMyLocationProvider);
    }

    public void disableMyLocation() {
        this.mIsLocationEnabled = false;
        stopLocationProvider();
        MapView mapView = this.mMapView;
        if (mapView != null) {
            mapView.postInvalidate();
        }
    }

    /* Access modifiers changed, original: protected */
    public void stopLocationProvider() {
        IMyLocationProvider iMyLocationProvider = this.mMyLocationProvider;
        if (iMyLocationProvider != null) {
            iMyLocationProvider.stopLocationProvider();
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            Object obj = this.mHandlerToken;
            if (obj != null) {
                handler.removeCallbacksAndMessages(obj);
            }
        }
    }

    public boolean isMyLocationEnabled() {
        return this.mIsLocationEnabled;
    }

    public boolean runOnFirstFix(Runnable runnable) {
        if (this.mMyLocationProvider == null || this.mLocation == null) {
            this.mRunOnFirstFix.addLast(runnable);
            return false;
        }
        new Thread(runnable).start();
        return true;
    }

    public void setPersonIcon(Bitmap bitmap) {
        this.mPersonBitmap = bitmap;
    }
}

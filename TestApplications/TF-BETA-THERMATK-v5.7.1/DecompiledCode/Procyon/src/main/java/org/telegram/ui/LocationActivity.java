// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import android.content.DialogInterface;
import java.util.Locale;
import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Cells.LocationPoweredCell;
import org.telegram.ui.Cells.LocationLoadingCell;
import org.telegram.ui.Cells.LocationCell;
import org.telegram.ui.Cells.SendLocationCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ActionBarMenu;
import android.view.View$OnClickListener;
import android.view.MotionEvent;
import org.osmdroid.config.Configuration;
import org.telegram.ui.Adapters.BaseLocationAdapter;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.Components.MapPlaceholderDrawable;
import org.telegram.ui.Components.LayoutHelper;
import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.widget.ImageView$ScaleType;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import android.view.View;
import org.osmdroid.api.IMapController;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.AlertDialog;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.messenger.ChatObject;
import android.location.LocationManager;
import android.os.Build$VERSION;
import android.text.method.LinkMovementMethod;
import android.text.Html;
import android.content.Context;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.osmdroid.api.IGeoPoint;
import java.util.List;
import org.osmdroid.util.BoundingBox;
import org.telegram.tgnet.ConnectionsManager;
import android.graphics.Shader;
import android.graphics.Matrix;
import android.graphics.BitmapShader;
import android.graphics.Shader$TileMode;
import android.graphics.BitmapFactory;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.FileLoader;
import android.graphics.RectF;
import android.graphics.Paint;
import org.telegram.messenger.ApplicationLoader;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.LocationController;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import org.osmdroid.views.overlay.Marker;
import org.telegram.messenger.MessagesController;
import org.osmdroid.util.GeoPoint;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Adapters.LocationActivitySearchAdapter;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import android.location.Location;
import org.telegram.messenger.MessageObject;
import android.util.SparseArray;
import java.util.ArrayList;
import android.widget.FrameLayout;
import org.osmdroid.views.MapView;
import android.widget.ImageView;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.TextView;
import android.animation.AnimatorSet;
import org.telegram.ui.Adapters.LocationActivityAdapter;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class LocationActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int map_list_menu_cartodark = 4;
    private static final int map_list_menu_osm = 2;
    private static final int map_list_menu_wiki = 3;
    private static final int share = 1;
    private LocationActivityAdapter adapter;
    private AnimatorSet animatorSet;
    private TextView attributionOverlay;
    private AvatarDrawable avatarDrawable;
    private boolean checkGpsEnabled;
    private boolean checkPermission;
    private LocationActivityDelegate delegate;
    private long dialogId;
    private EmptyTextProgressView emptyView;
    private boolean firstFocus;
    private boolean firstWas;
    private boolean isFirstLocation;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private int liveLocationType;
    private ImageView locationButton;
    private MapView mapView;
    private FrameLayout mapViewClip;
    private boolean mapsInitialized;
    private ImageView markerImageView;
    private int markerTop;
    private ImageView markerXImageView;
    private ArrayList<LiveLocation> markers;
    private SparseArray<LiveLocation> markersMap;
    private MessageObject messageObject;
    private Location myLocation;
    private MyLocationNewOverlay myLocationOverlay;
    private boolean onResumeCalled;
    private ActionBarMenuItem otherItem;
    private int overScrollHeight;
    private ImageView routeButton;
    private LocationActivitySearchAdapter searchAdapter;
    private RecyclerListView searchListView;
    private boolean searchWas;
    private boolean searching;
    private Runnable updateRunnable;
    private Location userLocation;
    private boolean userLocationMoved;
    private boolean wasResults;
    
    public LocationActivity(final int liveLocationType) {
        this.checkGpsEnabled = true;
        this.isFirstLocation = true;
        this.firstFocus = true;
        this.markers = new ArrayList<LiveLocation>();
        this.markersMap = (SparseArray<LiveLocation>)new SparseArray();
        this.checkPermission = true;
        this.userLocationMoved = false;
        this.firstWas = false;
        this.overScrollHeight = AndroidUtilities.displaySize.x - ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(66.0f);
        this.liveLocationType = liveLocationType;
    }
    
    private LiveLocation addUserMarker(final TLRPC.Message message) {
        final TLRPC.GeoPoint geo = message.media.geo;
        final GeoPoint geoPoint = new GeoPoint(geo.lat, geo._long);
        final LiveLocation liveLocation = (LiveLocation)this.markersMap.get(message.from_id);
        LiveLocation liveLocation2;
        if (liveLocation == null) {
            final LiveLocation e = new LiveLocation();
            e.object = message;
            if (e.object.from_id != 0) {
                e.user = MessagesController.getInstance(super.currentAccount).getUser(e.object.from_id);
                e.id = e.object.from_id;
            }
            else {
                final int id = (int)MessageObject.getDialogId(message);
                if (id > 0) {
                    e.user = MessagesController.getInstance(super.currentAccount).getUser(id);
                    e.id = id;
                }
                else {
                    e.chat = MessagesController.getInstance(super.currentAccount).getChat(-id);
                    e.id = id;
                }
            }
            try {
                final Marker marker = new Marker(this.mapView);
                marker.setPosition(geoPoint);
                marker.setOnMarkerClickListener((Marker.OnMarkerClickListener)new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker, final MapView mapView) {
                        return false;
                    }
                });
                final Bitmap userBitmap = this.createUserBitmap(e);
                liveLocation2 = e;
                if (userBitmap != null) {
                    marker.setIcon((Drawable)new BitmapDrawable(this.getParentActivity().getResources(), userBitmap));
                    marker.setAnchor(0.5f, 0.907f);
                    this.mapView.getOverlays().add(marker);
                    e.marker = marker;
                    this.markers.add(e);
                    this.markersMap.put(e.id, (Object)e);
                    final LocationController.SharingLocationInfo sharingLocationInfo = LocationController.getInstance(super.currentAccount).getSharingLocationInfo(this.dialogId);
                    liveLocation2 = e;
                    if (e.id == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                        liveLocation2 = e;
                        if (sharingLocationInfo != null) {
                            liveLocation2 = e;
                            if (e.object.id == sharingLocationInfo.mid) {
                                liveLocation2 = e;
                                if (this.myLocation != null) {
                                    e.marker.setPosition(new GeoPoint(this.myLocation.getLatitude(), this.myLocation.getLongitude()));
                                    liveLocation2 = e;
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
                liveLocation2 = e;
            }
        }
        else {
            liveLocation.object = message;
            liveLocation.marker.setPosition(geoPoint);
            liveLocation2 = liveLocation;
        }
        return liveLocation2;
    }
    
    private Bitmap createUserBitmap(final LiveLocation liveLocation) {
        Bitmap bitmap = null;
        Canvas canvas = null;
        try {
            TLRPC.FileLocation fileLocation;
            if (liveLocation.user != null && liveLocation.user.photo != null) {
                fileLocation = liveLocation.user.photo.photo_small;
            }
            else if (liveLocation.chat != null && liveLocation.chat.photo != null) {
                fileLocation = liveLocation.chat.photo.photo_small;
            }
            else {
                fileLocation = null;
            }
            bitmap = Bitmap.createBitmap(AndroidUtilities.dp(62.0f), AndroidUtilities.dp(76.0f), Bitmap$Config.ARGB_8888);
            try {
                bitmap.eraseColor(0);
                canvas = new Canvas(bitmap);
                final Drawable drawable = ApplicationLoader.applicationContext.getResources().getDrawable(2131165536);
                drawable.setBounds(0, 0, AndroidUtilities.dp(62.0f), AndroidUtilities.dp(76.0f));
                drawable.draw(canvas);
                final Paint paint = new Paint(1);
                final RectF rectF = new RectF();
                canvas.save();
                if (fileLocation != null) {
                    final Bitmap decodeFile = BitmapFactory.decodeFile(FileLoader.getPathToAttach(fileLocation, true).toString());
                    if (decodeFile != null) {
                        final BitmapShader shader = new BitmapShader(decodeFile, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
                        final Matrix localMatrix = new Matrix();
                        final float n = AndroidUtilities.dp(52.0f) / (float)decodeFile.getWidth();
                        localMatrix.postTranslate((float)AndroidUtilities.dp(5.0f), (float)AndroidUtilities.dp(5.0f));
                        localMatrix.postScale(n, n);
                        paint.setShader((Shader)shader);
                        shader.setLocalMatrix(localMatrix);
                        rectF.set((float)AndroidUtilities.dp(5.0f), (float)AndroidUtilities.dp(5.0f), (float)AndroidUtilities.dp(57.0f), (float)AndroidUtilities.dp(57.0f));
                        canvas.drawRoundRect(rectF, (float)AndroidUtilities.dp(26.0f), (float)AndroidUtilities.dp(26.0f), paint);
                    }
                }
                else {
                    final AvatarDrawable avatarDrawable = new AvatarDrawable();
                    if (liveLocation.user != null) {
                        avatarDrawable.setInfo(liveLocation.user);
                    }
                    else if (liveLocation.chat != null) {
                        avatarDrawable.setInfo(liveLocation.chat);
                    }
                    canvas.translate((float)AndroidUtilities.dp(5.0f), (float)AndroidUtilities.dp(5.0f));
                    avatarDrawable.setBounds(0, 0, AndroidUtilities.dp(52.2f), AndroidUtilities.dp(52.2f));
                    avatarDrawable.draw(canvas);
                }
                canvas.restore();
                final Canvas canvas2 = canvas;
                final Bitmap bitmap2 = null;
                canvas2.setBitmap(bitmap2);
                final Bitmap bitmap4;
                final Bitmap bitmap3 = bitmap4 = bitmap;
            }
            catch (Throwable t) {
                final Bitmap bitmap4 = bitmap;
            }
        }
        catch (Throwable t) {
            final Bitmap bitmap4 = null;
        }
        try {
            final Canvas canvas2 = canvas;
            final Bitmap bitmap2 = null;
            canvas2.setBitmap(bitmap2);
            return bitmap;
            final Throwable t;
            FileLog.e(t);
            return bitmap4;
        }
        catch (Exception ex) {
            return bitmap;
        }
    }
    
    private void fetchRecentLocations(final ArrayList<TLRPC.Message> list) {
        final ArrayList<GeoPoint> list2 = new ArrayList<GeoPoint>();
        final boolean firstFocus = this.firstFocus;
        BoundingBox fromGeoPoints = null;
        final int currentTime = ConnectionsManager.getInstance(super.currentAccount).getCurrentTime();
        for (int i = 0; i < list.size(); ++i) {
            final TLRPC.Message message = list.get(i);
            final int date = message.date;
            final TLRPC.MessageMedia media = message.media;
            if (date + media.period > currentTime) {
                if (this.firstFocus) {
                    final TLRPC.GeoPoint geo = media.geo;
                    list2.add(new GeoPoint(geo.lat, geo._long));
                }
                this.addUserMarker(message);
            }
        }
        if (list2.size() > 0) {
            fromGeoPoints = BoundingBox.fromGeoPoints(list2);
        }
        if (!this.firstFocus) {
            return;
        }
        this.firstFocus = false;
        this.adapter.setLiveLocations(this.markers);
        if (!this.messageObject.isLiveLocation()) {
            return;
        }
        try {
            if (list.size() > 1) {
                try {
                    this.mapView.zoomToBoundingBox(fromGeoPoints, false, AndroidUtilities.dp(60.0f));
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
        catch (Exception ex2) {}
    }
    
    private void fixLayoutInternal(final boolean b) {
        if (this.listView != null) {
            int statusBarHeight;
            if (super.actionBar.getOccupyStatusBar()) {
                statusBarHeight = AndroidUtilities.statusBarHeight;
            }
            else {
                statusBarHeight = 0;
            }
            final int topMargin = statusBarHeight + ActionBar.getCurrentActionBarHeight();
            final int measuredHeight = super.fragmentView.getMeasuredHeight();
            if (measuredHeight == 0) {
                return;
            }
            this.overScrollHeight = measuredHeight - AndroidUtilities.dp(66.0f) - topMargin;
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.listView.getLayoutParams();
            layoutParams.topMargin = topMargin;
            this.listView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.mapViewClip.getLayoutParams();
            layoutParams2.topMargin = topMargin;
            layoutParams2.height = this.overScrollHeight;
            this.mapViewClip.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            final RecyclerListView searchListView = this.searchListView;
            if (searchListView != null) {
                final FrameLayout$LayoutParams layoutParams3 = (FrameLayout$LayoutParams)searchListView.getLayoutParams();
                layoutParams3.topMargin = topMargin;
                this.searchListView.setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
            }
            this.adapter.setOverScrollHeight(this.overScrollHeight);
            final FrameLayout$LayoutParams layoutParams4 = (FrameLayout$LayoutParams)this.mapView.getLayoutParams();
            if (layoutParams4 != null) {
                layoutParams4.height = this.overScrollHeight + AndroidUtilities.dp(10.0f);
                final MapView mapView = this.mapView;
                if (mapView != null) {
                    mapView.setPadding(AndroidUtilities.dp(70.0f), 0, AndroidUtilities.dp(70.0f), AndroidUtilities.dp(10.0f));
                }
                this.mapView.setLayoutParams((ViewGroup$LayoutParams)layoutParams4);
            }
            this.adapter.notifyDataSetChanged();
            if (b) {
                final LinearLayoutManager layoutManager = this.layoutManager;
                final int liveLocationType = this.liveLocationType;
                int n;
                if (liveLocationType != 1 && liveLocationType != 2) {
                    n = 0;
                }
                else {
                    n = 66;
                }
                layoutManager.scrollToPositionWithOffset(0, -AndroidUtilities.dp((float)(32 + n)));
                this.updateClipView(this.layoutManager.findFirstVisibleItemPosition());
                this.listView.post((Runnable)new _$$Lambda$LocationActivity$LYBCu_7L40XJGlKJA2wAVIIvy2s(this));
            }
            else {
                this.updateClipView(this.layoutManager.findFirstVisibleItemPosition());
            }
        }
    }
    
    private TextView getAttributionOverlay(final Context context) {
        (this.attributionOverlay = new TextView(context)).setText((CharSequence)Html.fromHtml("© <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors"));
        this.attributionOverlay.setShadowLayer(1.0f, -1.0f, -1.0f, -1);
        this.attributionOverlay.setLinksClickable(true);
        this.attributionOverlay.setMovementMethod(LinkMovementMethod.getInstance());
        return this.attributionOverlay;
    }
    
    private Location getLastLocation() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        Location lastKnownLocation = null;
        if (sdk_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0 && this.getParentActivity().checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            return null;
        }
        final LocationManager locationManager = (LocationManager)ApplicationLoader.applicationContext.getSystemService("location");
        final List providers = locationManager.getProviders(true);
        for (int i = providers.size() - 1; i >= 0; --i) {
            lastKnownLocation = locationManager.getLastKnownLocation((String)providers.get(i));
            if (lastKnownLocation != null) {
                break;
            }
        }
        return lastKnownLocation;
    }
    
    private int getMessageId(final TLRPC.Message message) {
        final int from_id = message.from_id;
        if (from_id != 0) {
            return from_id;
        }
        return (int)MessageObject.getDialogId(message);
    }
    
    private boolean getRecentLocations() {
        ArrayList<TLRPC.Message> list = (ArrayList<TLRPC.Message>)LocationController.getInstance(super.currentAccount).locationsCache.get(this.messageObject.getDialogId());
        if (list != null && list.isEmpty()) {
            this.fetchRecentLocations(list);
        }
        else {
            list = null;
        }
        final int n = (int)this.dialogId;
        boolean b = false;
        if (n < 0) {
            final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(-n);
            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                return false;
            }
        }
        final TLRPC.TL_messages_getRecentLocations tl_messages_getRecentLocations = new TLRPC.TL_messages_getRecentLocations();
        final long dialogId = this.messageObject.getDialogId();
        tl_messages_getRecentLocations.peer = MessagesController.getInstance(super.currentAccount).getInputPeer((int)dialogId);
        tl_messages_getRecentLocations.limit = 100;
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_getRecentLocations, new _$$Lambda$LocationActivity$w8_XrXsKbv633ZrXpPVuZnH_vEE(this, dialogId));
        if (list != null) {
            b = true;
        }
        return b;
    }
    
    private void onMapInit() {
        if (this.mapView == null) {
            return;
        }
        final GeoPoint center = new GeoPoint(48.85825, 2.29448);
        final IMapController controller = this.mapView.getController();
        this.mapView.setMaxZoomLevel(20.0);
        this.mapView.setMultiTouchControls(true);
        this.mapView.setBuiltInZoomControls(false);
        controller.setCenter(center);
        controller.setZoom(7.0);
        final MessageObject messageObject = this.messageObject;
        if (messageObject != null) {
            if (messageObject.isLiveLocation()) {
                final LiveLocation addUserMarker = this.addUserMarker(this.messageObject.messageOwner);
                if (!this.getRecentLocations()) {
                    controller.setCenter(addUserMarker.marker.getPosition());
                    controller.setZoom(this.mapView.getMaxZoomLevel() - 2.0);
                }
            }
            else {
                final GeoPoint geoPoint = new GeoPoint(this.userLocation.getLatitude(), this.userLocation.getLongitude());
                final Marker marker = new Marker(this.mapView);
                marker.setPosition(geoPoint);
                marker.setOnMarkerClickListener((Marker.OnMarkerClickListener)new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker, final MapView mapView) {
                        return false;
                    }
                });
                if (Build$VERSION.SDK_INT >= 21) {
                    marker.setIcon(this.getParentActivity().getDrawable(2131165551));
                }
                else {
                    marker.setIcon(this.getParentActivity().getResources().getDrawable(2131165551));
                }
                marker.setAnchor(0.5f, 1.0f);
                this.mapView.getOverlays().add(marker);
                controller.setCenter(geoPoint);
                controller.setZoom(this.mapView.getMaxZoomLevel() - 2.0);
                this.firstFocus = false;
                this.getRecentLocations();
            }
        }
        else {
            (this.userLocation = new Location("network")).setLatitude(48.85825);
            this.userLocation.setLongitude(2.29448);
        }
        final GpsMyLocationProvider gpsMyLocationProvider = new GpsMyLocationProvider((Context)this.getParentActivity());
        gpsMyLocationProvider.setLocationUpdateMinDistance(10.0f);
        gpsMyLocationProvider.setLocationUpdateMinTime(10000L);
        gpsMyLocationProvider.addLocationSource("network");
        (this.myLocationOverlay = new MyLocationNewOverlay(gpsMyLocationProvider, this.mapView) {
            @Override
            public void onLocationChanged(final Location location, final IMyLocationProvider myLocationProvider) {
                super.onLocationChanged(location, myLocationProvider);
                if (location != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            LocationActivity.this.positionMarker(location);
                            LocationController.getInstance(LocationActivity.this.currentAccount).setGoogleMapLocation(location, LocationActivity.this.isFirstLocation);
                            LocationActivity.this.isFirstLocation = false;
                        }
                    });
                }
            }
        }).enableMyLocation();
        this.myLocationOverlay.setDrawAccuracyEnabled(true);
        this.myLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        final LocationActivity this$0 = LocationActivity.this;
                        this$0.positionMarker(this$0.myLocationOverlay.getLastFix());
                        LocationController.getInstance(LocationActivity.this.currentAccount).setGoogleMapLocation(LocationActivity.this.myLocationOverlay.getLastFix(), LocationActivity.this.isFirstLocation);
                        LocationActivity.this.isFirstLocation = false;
                    }
                });
            }
        });
        this.mapView.getOverlays().add(this.myLocationOverlay);
        this.positionMarker(this.myLocation = this.getLastLocation());
        this.attributionOverlay.bringToFront();
        if (this.checkGpsEnabled && this.getParentActivity() != null) {
            this.checkGpsEnabled = false;
            if (!this.getParentActivity().getPackageManager().hasSystemFeature("android.hardware.location.gps")) {
                return;
            }
            try {
                if (!((LocationManager)ApplicationLoader.applicationContext.getSystemService("location")).isProviderEnabled("gps")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
                    builder.setTitle(LocaleController.getString("AppName", 2131558635));
                    builder.setMessage(LocaleController.getString("GpsDisabledAlert", 2131559597));
                    builder.setPositiveButton(LocaleController.getString("ConnectingToProxyEnable", 2131559140), (DialogInterface$OnClickListener)new _$$Lambda$LocationActivity$DsWjN4ehaLhnVMMrqLPs4UpCHbU(this));
                    builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                    this.showDialog(builder.create());
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
    }
    
    private void positionMarker(final Location location) {
        if (location == null) {
            return;
        }
        this.myLocation = new Location(location);
        final LiveLocation liveLocation = (LiveLocation)this.markersMap.get(UserConfig.getInstance(super.currentAccount).getClientUserId());
        final LocationController.SharingLocationInfo sharingLocationInfo = LocationController.getInstance(super.currentAccount).getSharingLocationInfo(this.dialogId);
        if (liveLocation != null && sharingLocationInfo != null && liveLocation.object.id == sharingLocationInfo.mid) {
            liveLocation.marker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
        }
        if (this.messageObject == null && this.mapView != null) {
            final GeoPoint center = new GeoPoint(location.getLatitude(), location.getLongitude());
            final LocationActivityAdapter adapter = this.adapter;
            if (adapter != null) {
                if (adapter.isPulledUp()) {
                    this.adapter.searchPlacesWithQuery(null, this.myLocation, true);
                }
                this.adapter.setGpsLocation(this.myLocation);
            }
            if (!this.userLocationMoved) {
                this.userLocation = new Location(location);
                if (this.firstWas) {
                    this.mapView.getController().animateTo(center);
                }
                else {
                    this.firstWas = true;
                    final IMapController controller = this.mapView.getController();
                    controller.setZoom(this.mapView.getMaxZoomLevel() - 2.0);
                    controller.setCenter(center);
                }
            }
        }
        else {
            this.adapter.setGpsLocation(this.myLocation);
        }
    }
    
    private void showPermissionAlert(final boolean b) {
        if (this.getParentActivity() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", 2131558635));
        if (b) {
            builder.setMessage(LocaleController.getString("PermissionNoLocationPosition", 2131560418));
        }
        else {
            builder.setMessage(LocaleController.getString("PermissionNoLocation", 2131560417));
        }
        builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", 2131560419), (DialogInterface$OnClickListener)new _$$Lambda$LocationActivity$8fdfxSn0ih3r889TzPZkGetJL7o(this));
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
        this.showDialog(builder.create());
    }
    
    private void updateClipView(int top) {
        if (top == -1) {
            return;
        }
        final View child = this.listView.getChildAt(0);
        if (child != null) {
            int n2;
            if (top == 0) {
                top = child.getTop();
                final int overScrollHeight = this.overScrollHeight;
                int n;
                if (top < 0) {
                    n = top;
                }
                else {
                    n = 0;
                }
                n2 = overScrollHeight + n;
            }
            else {
                top = 0;
                n2 = 0;
            }
            if (this.mapViewClip.getLayoutParams() != null) {
                if (n2 <= 0) {
                    if (this.mapView.getVisibility() == 0) {
                        this.mapView.setVisibility(4);
                        this.mapViewClip.setVisibility(4);
                    }
                }
                else if (this.mapView.getVisibility() == 4) {
                    this.mapView.setVisibility(0);
                    this.mapViewClip.setVisibility(0);
                }
                this.mapViewClip.setTranslationY((float)Math.min(0, top));
                final MapView mapView = this.mapView;
                final int n3 = -top;
                mapView.setTranslationY((float)Math.max(0, n3 / 2));
                final ImageView markerImageView = this.markerImageView;
                if (markerImageView != null) {
                    final int dp = AndroidUtilities.dp(42.0f);
                    final int n4 = n2 / 2;
                    final int markerTop = n3 - dp + n4;
                    this.markerTop = markerTop;
                    markerImageView.setTranslationY((float)markerTop);
                    this.markerXImageView.setTranslationY((float)(n3 - AndroidUtilities.dp(7.0f) + n4));
                }
                final ImageView routeButton = this.routeButton;
                if (routeButton != null) {
                    routeButton.setTranslationY((float)top);
                }
                final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.mapView.getLayoutParams();
                if (layoutParams != null && layoutParams.height != this.overScrollHeight + AndroidUtilities.dp(10.0f)) {
                    layoutParams.height = this.overScrollHeight + AndroidUtilities.dp(10.0f);
                    final MapView mapView2 = this.mapView;
                    if (mapView2 != null) {
                        mapView2.setPadding(AndroidUtilities.dp(70.0f), 0, AndroidUtilities.dp(70.0f), AndroidUtilities.dp(10.0f));
                    }
                    this.mapView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                }
            }
        }
    }
    
    private void updateSearchInterface() {
        final LocationActivityAdapter adapter = this.adapter;
        if (adapter != null) {
            ((RecyclerView.Adapter)adapter).notifyDataSetChanged();
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        if (AndroidUtilities.isTablet()) {
            super.actionBar.setOccupyStatusBar(false);
        }
        super.actionBar.setAddToContainer(false);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    LocationActivity.this.finishFragment();
                }
                else if (n == 2) {
                    if (LocationActivity.this.mapView != null) {
                        LocationActivity.this.attributionOverlay.setText((CharSequence)Html.fromHtml("© <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors"));
                        LocationActivity.this.mapView.setTileSource(TileSourceFactory.MAPNIK);
                    }
                }
                else if (n == 3) {
                    if (LocationActivity.this.mapView != null) {
                        final XYTileSource tileSource = new XYTileSource("Wikimedia", 0, 19, 256, ".png", new String[] { "https://maps.wikimedia.org/osm-intl/" }, "© OpenStreetMap contributors");
                        LocationActivity.this.attributionOverlay.setText((CharSequence)Html.fromHtml("© <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors"));
                        LocationActivity.this.mapView.setTileSource(tileSource);
                    }
                }
                else if (n == 4) {
                    if (LocationActivity.this.mapView != null) {
                        final XYTileSource tileSource2 = new XYTileSource("Carto Dark", 0, 20, 256, ".png", new String[] { "https://cartodb-basemaps-a.global.ssl.fastly.net/dark_all/", "https://cartodb-basemaps-b.global.ssl.fastly.net/dark_all/", "https://cartodb-basemaps-c.global.ssl.fastly.net/dark_all/", "https://cartodb-basemaps-d.global.ssl.fastly.net/dark_all/" }, "© OpenStreetMap contributors, © CARTO");
                        LocationActivity.this.attributionOverlay.setText((CharSequence)Html.fromHtml("© <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors, © <a href=\"https://carto.com/attributions\">CARTO</a>"));
                        LocationActivity.this.mapView.setTileSource(tileSource2);
                    }
                }
                else if (n == 1) {
                    try {
                        final double lat = LocationActivity.this.messageObject.messageOwner.media.geo.lat;
                        final double long1 = LocationActivity.this.messageObject.messageOwner.media.geo._long;
                        final Activity parentActivity = LocationActivity.this.getParentActivity();
                        final StringBuilder sb = new StringBuilder();
                        sb.append("geo:");
                        sb.append(lat);
                        sb.append(",");
                        sb.append(long1);
                        sb.append("?q=");
                        sb.append(lat);
                        sb.append(",");
                        sb.append(long1);
                        parentActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
            }
        });
        final ActionBarMenu menu = super.actionBar.createMenu();
        final MessageObject messageObject = this.messageObject;
        if (messageObject != null) {
            if (messageObject.isLiveLocation()) {
                super.actionBar.setTitle(LocaleController.getString("AttachLiveLocation", 2131558721));
            }
            else {
                final String title = this.messageObject.messageOwner.media.title;
                if (title != null && title.length() > 0) {
                    super.actionBar.setTitle(LocaleController.getString("SharedPlace", 2131560770));
                }
                else {
                    super.actionBar.setTitle(LocaleController.getString("ChatLocation", 2131559042));
                }
                menu.addItem(1, 2131165818).setContentDescription((CharSequence)LocaleController.getString("ShareFile", 2131560748));
            }
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("ShareLocation", 2131560750));
            menu.addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
                @Override
                public void onSearchCollapse() {
                    LocationActivity.this.searching = false;
                    LocationActivity.this.searchWas = false;
                    LocationActivity.this.otherItem.setVisibility(0);
                    LocationActivity.this.searchListView.setEmptyView(null);
                    LocationActivity.this.listView.setVisibility(0);
                    LocationActivity.this.mapViewClip.setVisibility(0);
                    LocationActivity.this.searchListView.setVisibility(8);
                    LocationActivity.this.emptyView.setVisibility(8);
                    LocationActivity.this.searchAdapter.searchDelayed(null, null);
                }
                
                @Override
                public void onSearchExpand() {
                    LocationActivity.this.searching = true;
                    LocationActivity.this.otherItem.setVisibility(8);
                    LocationActivity.this.listView.setVisibility(8);
                    LocationActivity.this.mapViewClip.setVisibility(8);
                    LocationActivity.this.searchListView.setVisibility(0);
                    LocationActivity.this.searchListView.setEmptyView((View)LocationActivity.this.emptyView);
                    LocationActivity.this.emptyView.showTextView();
                }
                
                @Override
                public void onTextChanged(final EditText editText) {
                    if (LocationActivity.this.searchAdapter == null) {
                        return;
                    }
                    final String string = editText.getText().toString();
                    if (string.length() != 0) {
                        LocationActivity.this.searchWas = true;
                    }
                    LocationActivity.this.emptyView.showProgress();
                    LocationActivity.this.searchAdapter.searchDelayed(string, LocationActivity.this.userLocation);
                }
            }).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
        }
        (this.otherItem = menu.addItem(0, 2131165416)).addSubItem(2, 2131165642, "Standard OSM");
        this.otherItem.addSubItem(3, 2131165642, "Wikimedia");
        this.otherItem.addSubItem(4, 2131165642, "Carto Dark");
        this.otherItem.setContentDescription((CharSequence)LocaleController.getString("AccDescrMoreOptions", 2131558443));
        super.fragmentView = (View)new FrameLayout(context) {
            private boolean first = true;
            
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                if (b) {
                    LocationActivity.this.fixLayoutInternal(this.first);
                    this.first = false;
                }
            }
        };
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        this.locationButton = new ImageView(context);
        CombinedDrawable simpleSelectorCircleDrawable;
        final Drawable drawable = simpleSelectorCircleDrawable = (CombinedDrawable)Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("profile_actionBackground"), Theme.getColor("profile_actionPressedBackground"));
        if (Build$VERSION.SDK_INT < 21) {
            final Drawable mutate = context.getResources().getDrawable(2131165388).mutate();
            mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(-16777216, PorterDuff$Mode.MULTIPLY));
            simpleSelectorCircleDrawable = new CombinedDrawable(mutate, drawable, 0, 0);
            simpleSelectorCircleDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        }
        this.locationButton.setBackgroundDrawable((Drawable)simpleSelectorCircleDrawable);
        this.locationButton.setImageResource(2131165685);
        this.locationButton.setScaleType(ImageView$ScaleType.CENTER);
        this.locationButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("profile_actionIcon"), PorterDuff$Mode.MULTIPLY));
        this.locationButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrMyLocation", 2131558449));
        if (Build$VERSION.SDK_INT >= 21) {
            final StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)this.locationButton, "translationZ", new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
            stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)this.locationButton, "translationZ", new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
            this.locationButton.setStateListAnimator(stateListAnimator);
            this.locationButton.setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                @SuppressLint({ "NewApi" })
                public void getOutline(final View view, final Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        if (this.messageObject != null) {
            (this.userLocation = new Location("network")).setLatitude(this.messageObject.messageOwner.media.geo.lat);
            this.userLocation.setLongitude(this.messageObject.messageOwner.media.geo._long);
        }
        this.searchWas = false;
        this.searching = false;
        this.mapViewClip = new FrameLayout(context);
        final FrameLayout mapViewClip = this.mapViewClip;
        final TextView attributionOverlay = this.getAttributionOverlay(context);
        int n;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        float n2;
        if (LocaleController.isRTL) {
            n2 = 0.0f;
        }
        else {
            n2 = 4.0f;
        }
        float n3;
        if (LocaleController.isRTL) {
            n3 = 4.0f;
        }
        else {
            n3 = 0.0f;
        }
        mapViewClip.addView((View)attributionOverlay, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n | 0x50, n2, 0.0f, n3, 5.0f));
        this.mapViewClip.setBackgroundDrawable((Drawable)new MapPlaceholderDrawable());
        final LocationActivityAdapter adapter = this.adapter;
        if (adapter != null) {
            adapter.destroy();
        }
        final LocationActivitySearchAdapter searchAdapter = this.searchAdapter;
        if (searchAdapter != null) {
            searchAdapter.destroy();
        }
        (this.listView = new RecyclerListView(context)).setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        this.listView.setAdapter(this.adapter = new LocationActivityAdapter(context, this.liveLocationType, this.dialogId));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        }));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int firstVisibleItemPosition, final int n) {
                if (LocationActivity.this.adapter.getItemCount() == 0) {
                    return;
                }
                firstVisibleItemPosition = LocationActivity.this.layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition == -1) {
                    return;
                }
                LocationActivity.this.updateClipView(firstVisibleItemPosition);
                if (n > 0 && !LocationActivity.this.adapter.isPulledUp()) {
                    LocationActivity.this.adapter.setPulledUp();
                    if (LocationActivity.this.myLocation != null) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$LocationActivity$6$e4diUUxQq_JKRLDvY3ypH_z7db4(this));
                    }
                }
            }
        });
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$LocationActivity$hR_6D0AqpgGzQVaKwSZDSV7l5U0(this));
        this.adapter.setDelegate(this.dialogId, (BaseLocationAdapter.BaseLocationAdapterDelegate)new _$$Lambda$LocationActivity$1r8OhsfCqopqgQX3qFh_nzBXEt8(this));
        this.adapter.setOverScrollHeight(this.overScrollHeight);
        frameLayout.addView((View)this.mapViewClip, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        Configuration.getInstance().setUserAgentValue("Telegram-FOSS(F-Droid) 5.7.1");
        this.mapView = new MapView(context) {
            @Override
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (LocationActivity.this.messageObject == null) {
                    if (motionEvent.getAction() == 0) {
                        if (LocationActivity.this.animatorSet != null) {
                            LocationActivity.this.animatorSet.cancel();
                        }
                        LocationActivity.this.animatorSet = new AnimatorSet();
                        LocationActivity.this.animatorSet.setDuration(200L);
                        LocationActivity.this.animatorSet.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)LocationActivity.this.markerImageView, "translationY", new float[] { (float)(LocationActivity.this.markerTop + -AndroidUtilities.dp(10.0f)) }), (Animator)ObjectAnimator.ofFloat((Object)LocationActivity.this.markerXImageView, "alpha", new float[] { 1.0f }) });
                        LocationActivity.this.animatorSet.start();
                    }
                    else if (motionEvent.getAction() == 1) {
                        if (LocationActivity.this.animatorSet != null) {
                            LocationActivity.this.animatorSet.cancel();
                        }
                        LocationActivity.this.animatorSet = new AnimatorSet();
                        LocationActivity.this.animatorSet.setDuration(200L);
                        LocationActivity.this.animatorSet.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)LocationActivity.this.markerImageView, "translationY", new float[] { (float)LocationActivity.this.markerTop }), (Animator)ObjectAnimator.ofFloat((Object)LocationActivity.this.markerXImageView, "alpha", new float[] { 0.0f }) });
                        LocationActivity.this.animatorSet.start();
                    }
                    if (motionEvent.getAction() == 2) {
                        if (!LocationActivity.this.userLocationMoved) {
                            final AnimatorSet set = new AnimatorSet();
                            set.setDuration(200L);
                            set.play((Animator)ObjectAnimator.ofFloat((Object)LocationActivity.this.locationButton, "alpha", new float[] { 1.0f }));
                            set.start();
                            LocationActivity.this.userLocationMoved = true;
                        }
                        if (LocationActivity.this.mapView != null && LocationActivity.this.userLocation != null) {
                            LocationActivity.this.userLocation.setLatitude(LocationActivity.this.mapView.getMapCenter().getLatitude());
                            LocationActivity.this.userLocation.setLongitude(LocationActivity.this.mapView.getMapCenter().getLongitude());
                        }
                        LocationActivity.this.adapter.setCustomLocation(LocationActivity.this.userLocation);
                    }
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        AndroidUtilities.runOnUIThread(new _$$Lambda$LocationActivity$RudJicCJug8Kjbchqc9GJliyPHo(this));
        final View view = new View(context);
        view.setBackgroundResource(2131165408);
        this.mapViewClip.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3, 83));
        final MessageObject messageObject2 = this.messageObject;
        if (messageObject2 == null) {
            (this.markerImageView = new ImageView(context)).setImageResource(2131165551);
            this.mapViewClip.addView((View)this.markerImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(24, 42, 49));
            (this.markerXImageView = new ImageView(context)).setAlpha(0.0f);
            this.markerXImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("location_markerX"), PorterDuff$Mode.MULTIPLY));
            this.markerXImageView.setImageResource(2131165770);
            this.mapViewClip.addView((View)this.markerXImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(14, 14, 49));
            (this.emptyView = new EmptyTextProgressView(context)).setText(LocaleController.getString("NoResult", 2131559943));
            this.emptyView.setShowAtCenter(true);
            this.emptyView.setVisibility(8);
            frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            (this.searchListView = new RecyclerListView(context)).setVisibility(8);
            this.searchListView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
            this.searchListView.setAdapter(this.searchAdapter = new LocationActivitySearchAdapter(context));
            frameLayout.addView((View)this.searchListView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
            this.searchListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                    if (n == 1 && LocationActivity.this.searching && LocationActivity.this.searchWas) {
                        AndroidUtilities.hideKeyboard(LocationActivity.this.getParentActivity().getCurrentFocus());
                    }
                }
            });
            this.searchListView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$LocationActivity$S5fuT9kSC5GcWeIpW8kQl94_IX8(this));
        }
        else if (!messageObject2.isLiveLocation()) {
            this.routeButton = new ImageView(context);
            final Drawable simpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
            Drawable backgroundDrawable;
            if (Build$VERSION.SDK_INT < 21) {
                final Drawable mutate2 = context.getResources().getDrawable(2131165387).mutate();
                mutate2.setColorFilter((ColorFilter)new PorterDuffColorFilter(-16777216, PorterDuff$Mode.MULTIPLY));
                backgroundDrawable = new CombinedDrawable(mutate2, simpleSelectorCircleDrawable2, 0, 0);
                ((CombinedDrawable)backgroundDrawable).setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            }
            else {
                backgroundDrawable = simpleSelectorCircleDrawable2;
            }
            this.routeButton.setBackgroundDrawable(backgroundDrawable);
            this.routeButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), PorterDuff$Mode.MULTIPLY));
            this.routeButton.setImageResource(2131165686);
            this.routeButton.setScaleType(ImageView$ScaleType.CENTER);
            if (Build$VERSION.SDK_INT >= 21) {
                final StateListAnimator stateListAnimator2 = new StateListAnimator();
                stateListAnimator2.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)this.routeButton, "translationZ", new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
                stateListAnimator2.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)this.routeButton, "translationZ", new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
                this.routeButton.setStateListAnimator(stateListAnimator2);
                this.routeButton.setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                    @SuppressLint({ "NewApi" })
                    public void getOutline(final View view, final Outline outline) {
                        outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    }
                });
            }
            final ImageView routeButton = this.routeButton;
            int n4;
            if (Build$VERSION.SDK_INT >= 21) {
                n4 = 56;
            }
            else {
                n4 = 60;
            }
            float n5;
            if (Build$VERSION.SDK_INT >= 21) {
                n5 = 56.0f;
            }
            else {
                n5 = 60.0f;
            }
            int n6;
            if (LocaleController.isRTL) {
                n6 = 3;
            }
            else {
                n6 = 5;
            }
            float n7;
            if (LocaleController.isRTL) {
                n7 = 14.0f;
            }
            else {
                n7 = 0.0f;
            }
            float n8;
            if (LocaleController.isRTL) {
                n8 = 0.0f;
            }
            else {
                n8 = 14.0f;
            }
            frameLayout.addView((View)routeButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n4, n5, n6 | 0x50, n7, 0.0f, n8, 37.0f));
            this.routeButton.setOnClickListener((View$OnClickListener)new _$$Lambda$LocationActivity$XHy3PqbpZ0y74_5Ir43yVn45rF8(this));
            this.adapter.setMessageObject(this.messageObject);
        }
        final MessageObject messageObject3 = this.messageObject;
        if (messageObject3 != null && !messageObject3.isLiveLocation()) {
            final FrameLayout mapViewClip2 = this.mapViewClip;
            final ImageView locationButton = this.locationButton;
            int n9;
            if (Build$VERSION.SDK_INT >= 21) {
                n9 = 56;
            }
            else {
                n9 = 60;
            }
            float n10;
            if (Build$VERSION.SDK_INT >= 21) {
                n10 = 56.0f;
            }
            else {
                n10 = 60.0f;
            }
            int n11;
            if (LocaleController.isRTL) {
                n11 = 3;
            }
            else {
                n11 = 5;
            }
            float n12;
            if (LocaleController.isRTL) {
                n12 = 14.0f;
            }
            else {
                n12 = 0.0f;
            }
            float n13;
            if (LocaleController.isRTL) {
                n13 = 0.0f;
            }
            else {
                n13 = 14.0f;
            }
            mapViewClip2.addView((View)locationButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n9, n10, n11 | 0x50, n12, 0.0f, n13, 43.0f));
        }
        else {
            final FrameLayout mapViewClip3 = this.mapViewClip;
            final ImageView locationButton2 = this.locationButton;
            int n14;
            if (Build$VERSION.SDK_INT >= 21) {
                n14 = 56;
            }
            else {
                n14 = 60;
            }
            float n15;
            if (Build$VERSION.SDK_INT >= 21) {
                n15 = 56.0f;
            }
            else {
                n15 = 60.0f;
            }
            int n16;
            if (LocaleController.isRTL) {
                n16 = 3;
            }
            else {
                n16 = 5;
            }
            float n17;
            if (LocaleController.isRTL) {
                n17 = 14.0f;
            }
            else {
                n17 = 0.0f;
            }
            float n18;
            if (LocaleController.isRTL) {
                n18 = 0.0f;
            }
            else {
                n18 = 14.0f;
            }
            mapViewClip3.addView((View)locationButton2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n14, n15, n16 | 0x50, n17, 0.0f, n18, 14.0f));
        }
        this.locationButton.setOnClickListener((View$OnClickListener)new _$$Lambda$LocationActivity$_ONGGuxSOSrFr5q_KgV4tF6eRgk(this));
        if (this.messageObject == null) {
            this.locationButton.setAlpha(0.0f);
        }
        frameLayout.addView((View)super.actionBar);
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int i, int didReceiveNewMessages, final Object... array) {
        if (i == NotificationCenter.closeChats) {
            this.removeSelfFromStack();
        }
        else if (i == NotificationCenter.locationPermissionGranted) {
            if (this.mapView != null && this.mapsInitialized) {
                this.myLocationOverlay.enableMyLocation();
            }
        }
        else {
            didReceiveNewMessages = NotificationCenter.didReceiveNewMessages;
            final int n = 0;
            final int n2 = 0;
            if (i == didReceiveNewMessages) {
                if ((long)array[0] != this.dialogId || this.messageObject == null) {
                    return;
                }
                final ArrayList list = (ArrayList)array[1];
                didReceiveNewMessages = 0;
                MessageObject messageObject;
                for (i = n2; i < list.size(); ++i) {
                    messageObject = list.get(i);
                    if (messageObject.isLiveLocation()) {
                        this.addUserMarker(messageObject.messageOwner);
                        didReceiveNewMessages = 1;
                    }
                }
                if (didReceiveNewMessages != 0) {
                    final LocationActivityAdapter adapter = this.adapter;
                    if (adapter != null) {
                        adapter.setLiveLocations(this.markers);
                    }
                }
            }
            else if (i != NotificationCenter.messagesDeleted) {
                if (i == NotificationCenter.replaceMessagesObjects) {
                    final long longValue = (long)array[0];
                    if (longValue == this.dialogId) {
                        if (this.messageObject != null) {
                            final ArrayList list2 = (ArrayList)array[1];
                            didReceiveNewMessages = 0;
                            MessageObject messageObject2;
                            LiveLocation liveLocation;
                            LocationController.SharingLocationInfo sharingLocationInfo;
                            Marker marker;
                            TLRPC.GeoPoint geo;
                            for (i = n; i < list2.size(); ++i) {
                                messageObject2 = list2.get(i);
                                if (messageObject2.isLiveLocation()) {
                                    liveLocation = (LiveLocation)this.markersMap.get(this.getMessageId(messageObject2.messageOwner));
                                    if (liveLocation != null) {
                                        sharingLocationInfo = LocationController.getInstance(super.currentAccount).getSharingLocationInfo(longValue);
                                        if (sharingLocationInfo == null || sharingLocationInfo.mid != messageObject2.getId()) {
                                            marker = liveLocation.marker;
                                            geo = messageObject2.messageOwner.media.geo;
                                            marker.setPosition(new GeoPoint(geo.lat, geo._long));
                                        }
                                        didReceiveNewMessages = 1;
                                    }
                                }
                            }
                            if (didReceiveNewMessages != 0) {
                                final LocationActivityAdapter adapter2 = this.adapter;
                                if (adapter2 != null) {
                                    adapter2.updateLiveLocations();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final -$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk instance = _$$Lambda$LocationActivity$wcqGz1znpcXufa_L0mTh0dpx_bk.INSTANCE;
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, "actionBarDefaultSubmenuBackground"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, "actionBarDefaultSubmenuItem"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "actionBarDefaultSubmenuItemIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"), new ThemeDescription((View)this.locationButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "profile_actionIcon"), new ThemeDescription((View)this.locationButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "profile_actionBackground"), new ThemeDescription((View)this.locationButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "profile_actionPressedBackground"), new ThemeDescription((View)this.routeButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionIcon"), new ThemeDescription((View)this.routeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "chats_actionBackground"), new ThemeDescription((View)this.routeButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "chats_actionPressedBackground"), new ThemeDescription((View)this.markerXImageView, 0, null, null, null, null, "location_markerX"), new ThemeDescription((View)this.listView, 0, new Class[] { GraySectionCell.class }, new String[] { "textView" }, null, null, null, "key_graySectionText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { GraySectionCell.class }, null, null, null, "graySection"), new ThemeDescription(null, 0, null, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, (ThemeDescription.ThemeDescriptionDelegate)instance, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)instance, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)instance, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)instance, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)instance, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)instance, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)instance, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)instance, "avatar_backgroundPink"), new ThemeDescription(null, 0, null, null, null, null, "location_liveLocationProgress"), new ThemeDescription(null, 0, null, null, null, null, "location_placeLocationBackground"), new ThemeDescription(null, 0, null, null, null, null, "dialog_liveLocationProgress"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[] { SendLocationCell.class }, new String[] { "imageView" }, null, null, null, "location_sendLocationIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[] { SendLocationCell.class }, new String[] { "imageView" }, null, null, null, "location_sendLiveLocationIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[] { SendLocationCell.class }, new String[] { "imageView" }, null, null, null, "location_sendLocationBackground"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[] { SendLocationCell.class }, new String[] { "imageView" }, null, null, null, "location_sendLiveLocationBackground"), new ThemeDescription((View)this.listView, 0, new Class[] { SendLocationCell.class }, new String[] { "accurateTextView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { SendLocationCell.class }, new String[] { "titleTextView" }, null, null, null, "windowBackgroundWhiteRedText2"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { SendLocationCell.class }, new String[] { "titleTextView" }, null, null, null, "windowBackgroundWhiteBlueText7"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { LocationCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, 0, new Class[] { LocationCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { LocationCell.class }, new String[] { "addressTextView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.searchListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { LocationCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.searchListView, 0, new Class[] { LocationCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.searchListView, 0, new Class[] { LocationCell.class }, new String[] { "addressTextView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, 0, new Class[] { LocationLoadingCell.class }, new String[] { "progressBar" }, null, null, null, "progressCircle"), new ThemeDescription((View)this.listView, 0, new Class[] { LocationLoadingCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, 0, new Class[] { LocationPoweredCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, 0, new Class[] { LocationPoweredCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, 0, new Class[] { LocationPoweredCell.class }, new String[] { "textView2" }, null, null, null, "windowBackgroundWhiteGrayText3") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        super.swipeBackEnabled = false;
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionGranted);
        final MessageObject messageObject = this.messageObject;
        if (messageObject != null && messageObject.isLiveLocation()) {
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didReceiveNewMessages);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.replaceMessagesObjects);
        }
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didReceiveNewMessages);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.replaceMessagesObjects);
        final LocationActivityAdapter adapter = this.adapter;
        if (adapter != null) {
            adapter.destroy();
        }
        final LocationActivitySearchAdapter searchAdapter = this.searchAdapter;
        if (searchAdapter != null) {
            searchAdapter.destroy();
        }
        final Runnable updateRunnable = this.updateRunnable;
        if (updateRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(updateRunnable);
            this.updateRunnable = null;
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        final MapView mapView = this.mapView;
        if (mapView != null && this.mapsInitialized) {
            try {
                mapView.onPause();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            if (this.mapView.getOverlays().contains(this.myLocationOverlay)) {
                this.mapView.getOverlays().remove(this.myLocationOverlay);
            }
            this.myLocationOverlay.disableMyLocation();
        }
        this.onResumeCalled = false;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
        AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
        final MapView mapView = this.mapView;
        if (mapView != null && this.mapsInitialized) {
            try {
                mapView.onResume();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            this.mapView.getOverlays().add(this.myLocationOverlay);
            this.myLocationOverlay.enableMyLocation();
        }
        this.fixLayoutInternal(this.onResumeCalled = true);
        if (this.checkPermission && Build$VERSION.SDK_INT >= 23) {
            final Activity parentActivity = this.getParentActivity();
            if (parentActivity != null) {
                this.checkPermission = false;
                if (parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    parentActivity.requestPermissions(new String[] { "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION" }, 2);
                }
            }
        }
    }
    
    public void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b) {
            try {
                if (this.mapView.getParent() instanceof ViewGroup) {
                    ((ViewGroup)this.mapView.getParent()).removeView((View)this.mapView);
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            final FrameLayout mapViewClip = this.mapViewClip;
            if (mapViewClip != null) {
                mapViewClip.addView((View)this.mapView, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, this.overScrollHeight + AndroidUtilities.dp(10.0f), 51));
                this.updateClipView(this.layoutManager.findFirstVisibleItemPosition());
            }
            else {
                final View fragmentView = super.fragmentView;
                if (fragmentView != null) {
                    ((FrameLayout)fragmentView).addView((View)this.mapView, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
                }
            }
        }
    }
    
    public void setDelegate(final LocationActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setDialogId(final long dialogId) {
        this.dialogId = dialogId;
    }
    
    public void setMessageObject(final MessageObject messageObject) {
        this.messageObject = messageObject;
        this.dialogId = this.messageObject.getDialogId();
    }
    
    public class LiveLocation
    {
        public TLRPC.Chat chat;
        public int id;
        public Marker marker;
        public TLRPC.Message object;
        public TLRPC.User user;
    }
    
    public interface LocationActivityDelegate
    {
        void didSelectLocation(final TLRPC.MessageMedia p0, final int p1);
    }
}

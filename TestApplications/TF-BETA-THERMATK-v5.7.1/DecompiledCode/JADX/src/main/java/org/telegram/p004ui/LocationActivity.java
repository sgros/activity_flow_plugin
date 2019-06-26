package org.telegram.p004ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Marker.OnMarkerClickListener;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.LocationController.SharingLocationInfo;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.ActionBarMenu;
import org.telegram.p004ui.ActionBar.ActionBarMenuItem;
import org.telegram.p004ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Adapters.LocationActivityAdapter;
import org.telegram.p004ui.Adapters.LocationActivitySearchAdapter;
import org.telegram.p004ui.Cells.GraySectionCell;
import org.telegram.p004ui.Cells.LocationCell;
import org.telegram.p004ui.Cells.LocationLoadingCell;
import org.telegram.p004ui.Cells.LocationPoweredCell;
import org.telegram.p004ui.Cells.SendLocationCell;
import org.telegram.p004ui.Components.AvatarDrawable;
import org.telegram.p004ui.Components.CombinedDrawable;
import org.telegram.p004ui.Components.EmptyTextProgressView;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.MapPlaceholderDrawable;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_geoPoint;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC.TL_messages_getRecentLocations;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.messages_Messages;

/* renamed from: org.telegram.ui.LocationActivity */
public class LocationActivity extends BaseFragment implements NotificationCenterDelegate {
    private static final int map_list_menu_cartodark = 4;
    private static final int map_list_menu_osm = 2;
    private static final int map_list_menu_wiki = 3;
    private static final int share = 1;
    private LocationActivityAdapter adapter;
    private AnimatorSet animatorSet;
    private TextView attributionOverlay;
    private AvatarDrawable avatarDrawable;
    private boolean checkGpsEnabled = true;
    private boolean checkPermission = true;
    private LocationActivityDelegate delegate;
    private long dialogId;
    private EmptyTextProgressView emptyView;
    private boolean firstFocus = true;
    private boolean firstWas = false;
    private boolean isFirstLocation = true;
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
    private ArrayList<LiveLocation> markers = new ArrayList();
    private SparseArray<LiveLocation> markersMap = new SparseArray();
    private MessageObject messageObject;
    private Location myLocation;
    private MyLocationNewOverlay myLocationOverlay;
    private boolean onResumeCalled;
    private ActionBarMenuItem otherItem;
    private int overScrollHeight = ((AndroidUtilities.displaySize.x - C2190ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.m26dp(66.0f));
    private ImageView routeButton;
    private LocationActivitySearchAdapter searchAdapter;
    private RecyclerListView searchListView;
    private boolean searchWas;
    private boolean searching;
    private Runnable updateRunnable;
    private Location userLocation;
    private boolean userLocationMoved = false;
    private boolean wasResults;

    /* renamed from: org.telegram.ui.LocationActivity$13 */
    class C306813 implements Runnable {

        /* renamed from: org.telegram.ui.LocationActivity$13$1 */
        class C30671 implements Runnable {
            C30671() {
            }

            public void run() {
                LocationActivity locationActivity = LocationActivity.this;
                locationActivity.positionMarker(locationActivity.myLocationOverlay.getLastFix());
                LocationController.getInstance(LocationActivity.this.currentAccount).setGoogleMapLocation(LocationActivity.this.myLocationOverlay.getLastFix(), LocationActivity.this.isFirstLocation);
                LocationActivity.this.isFirstLocation = false;
            }
        }

        C306813() {
        }

        public void run() {
            AndroidUtilities.runOnUIThread(new C30671());
        }
    }

    /* renamed from: org.telegram.ui.LocationActivity$4 */
    class C30704 extends ViewOutlineProvider {
        C30704() {
        }

        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, AndroidUtilities.m26dp(56.0f), AndroidUtilities.m26dp(56.0f));
        }
    }

    /* renamed from: org.telegram.ui.LocationActivity$9 */
    class C30719 extends ViewOutlineProvider {
        C30719() {
        }

        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, AndroidUtilities.m26dp(56.0f), AndroidUtilities.m26dp(56.0f));
        }
    }

    /* renamed from: org.telegram.ui.LocationActivity$LiveLocation */
    public class LiveLocation {
        public Chat chat;
        /* renamed from: id */
        public int f611id;
        public Marker marker;
        public Message object;
        public User user;
    }

    /* renamed from: org.telegram.ui.LocationActivity$LocationActivityDelegate */
    public interface LocationActivityDelegate {
        void didSelectLocation(MessageMedia messageMedia, int i);
    }

    /* renamed from: org.telegram.ui.LocationActivity$10 */
    class C421110 implements OnMarkerClickListener {
        public boolean onMarkerClick(Marker marker, MapView mapView) {
            return false;
        }

        C421110() {
        }
    }

    /* renamed from: org.telegram.ui.LocationActivity$11 */
    class C421211 implements OnMarkerClickListener {
        public boolean onMarkerClick(Marker marker, MapView mapView) {
            return false;
        }

        C421211() {
        }
    }

    /* renamed from: org.telegram.ui.LocationActivity$1 */
    class C42131 extends ActionBarMenuOnItemClick {
        C42131() {
        }

        public void onItemClick(int i) {
            String str = ",";
            if (i == -1) {
                LocationActivity.this.finishFragment();
                return;
            }
            String str2 = "© <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors";
            XYTileSource xYTileSource;
            if (i == 2) {
                if (LocationActivity.this.mapView != null) {
                    LocationActivity.this.attributionOverlay.setText(Html.fromHtml(str2));
                    LocationActivity.this.mapView.setTileSource(TileSourceFactory.MAPNIK);
                }
            } else if (i == 3) {
                if (LocationActivity.this.mapView != null) {
                    xYTileSource = new XYTileSource("Wikimedia", 0, 19, 256, ".png", new String[]{"https://maps.wikimedia.org/osm-intl/"}, "© OpenStreetMap contributors");
                    LocationActivity.this.attributionOverlay.setText(Html.fromHtml(str2));
                    LocationActivity.this.mapView.setTileSource(xYTileSource);
                }
            } else if (i == 4) {
                if (LocationActivity.this.mapView != null) {
                    xYTileSource = new XYTileSource("Carto Dark", 0, 20, 256, ".png", new String[]{"https://cartodb-basemaps-a.global.ssl.fastly.net/dark_all/", "https://cartodb-basemaps-b.global.ssl.fastly.net/dark_all/", "https://cartodb-basemaps-c.global.ssl.fastly.net/dark_all/", "https://cartodb-basemaps-d.global.ssl.fastly.net/dark_all/"}, "© OpenStreetMap contributors, © CARTO");
                    LocationActivity.this.attributionOverlay.setText(Html.fromHtml("© <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors, © <a href=\"https://carto.com/attributions\">CARTO</a>"));
                    LocationActivity.this.mapView.setTileSource(xYTileSource);
                }
            } else if (i == 1) {
                try {
                    double d = LocationActivity.this.messageObject.messageOwner.media.geo.lat;
                    double d2 = LocationActivity.this.messageObject.messageOwner.media.geo._long;
                    Activity parentActivity = LocationActivity.this.getParentActivity();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("geo:");
                    stringBuilder.append(d);
                    stringBuilder.append(str);
                    stringBuilder.append(d2);
                    stringBuilder.append("?q=");
                    stringBuilder.append(d);
                    stringBuilder.append(str);
                    stringBuilder.append(d2);
                    parentActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString())));
                } catch (Exception e) {
                    FileLog.m30e(e);
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.LocationActivity$2 */
    class C42142 extends ActionBarMenuItemSearchListener {
        C42142() {
        }

        public void onSearchExpand() {
            LocationActivity.this.searching = true;
            LocationActivity.this.otherItem.setVisibility(8);
            LocationActivity.this.listView.setVisibility(8);
            LocationActivity.this.mapViewClip.setVisibility(8);
            LocationActivity.this.searchListView.setVisibility(0);
            LocationActivity.this.searchListView.setEmptyView(LocationActivity.this.emptyView);
            LocationActivity.this.emptyView.showTextView();
        }

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

        public void onTextChanged(EditText editText) {
            if (LocationActivity.this.searchAdapter != null) {
                String obj = editText.getText().toString();
                if (obj.length() != 0) {
                    LocationActivity.this.searchWas = true;
                }
                LocationActivity.this.emptyView.showProgress();
                LocationActivity.this.searchAdapter.searchDelayed(obj, LocationActivity.this.userLocation);
            }
        }
    }

    /* renamed from: org.telegram.ui.LocationActivity$6 */
    class C42156 extends OnScrollListener {
        C42156() {
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (LocationActivity.this.adapter.getItemCount() != 0) {
                int findFirstVisibleItemPosition = LocationActivity.this.layoutManager.findFirstVisibleItemPosition();
                if (findFirstVisibleItemPosition != -1) {
                    LocationActivity.this.updateClipView(findFirstVisibleItemPosition);
                    if (i2 > 0 && !LocationActivity.this.adapter.isPulledUp()) {
                        LocationActivity.this.adapter.setPulledUp();
                        if (LocationActivity.this.myLocation != null) {
                            AndroidUtilities.runOnUIThread(new C1598-$$Lambda$LocationActivity$6$e4diUUxQq_JKRLDvY3ypH_z7db4(this));
                        }
                    }
                }
            }
        }

        public /* synthetic */ void lambda$onScrolled$0$LocationActivity$6() {
            LocationActivity.this.adapter.searchPlacesWithQuery(null, LocationActivity.this.myLocation, true);
        }
    }

    /* renamed from: org.telegram.ui.LocationActivity$8 */
    class C42168 extends OnScrollListener {
        C42168() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1 && LocationActivity.this.searching && LocationActivity.this.searchWas) {
                AndroidUtilities.hideKeyboard(LocationActivity.this.getParentActivity().getCurrentFocus());
            }
        }
    }

    static /* synthetic */ void lambda$getThemeDescriptions$12() {
    }

    public LocationActivity(int i) {
        this.liveLocationType = i;
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.swipeBackEnabled = false;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionGranted);
        MessageObject messageObject = this.messageObject;
        if (messageObject != null && messageObject.isLiveLocation()) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didReceiveNewMessages);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.replaceMessagesObjects);
        }
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didReceiveNewMessages);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.replaceMessagesObjects);
        LocationActivityAdapter locationActivityAdapter = this.adapter;
        if (locationActivityAdapter != null) {
            locationActivityAdapter.destroy();
        }
        LocationActivitySearchAdapter locationActivitySearchAdapter = this.searchAdapter;
        if (locationActivitySearchAdapter != null) {
            locationActivitySearchAdapter.destroy();
        }
        Runnable runnable = this.updateRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.updateRunnable = null;
        }
    }

    public View createView(Context context) {
        Context context2 = context;
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setAddToContainer(false);
        this.actionBar.setActionBarMenuOnItemClick(new C42131());
        ActionBarMenu createMenu = this.actionBar.createMenu();
        MessageObject messageObject = this.messageObject;
        if (messageObject == null) {
            this.actionBar.setTitle(LocaleController.getString("ShareLocation", C1067R.string.ShareLocation));
            createMenu.addItem(0, (int) C1067R.C1065drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new C42142()).setSearchFieldHint(LocaleController.getString("Search", C1067R.string.Search));
        } else if (messageObject.isLiveLocation()) {
            this.actionBar.setTitle(LocaleController.getString("AttachLiveLocation", C1067R.string.AttachLiveLocation));
        } else {
            String str = this.messageObject.messageOwner.media.title;
            if (str == null || str.length() <= 0) {
                this.actionBar.setTitle(LocaleController.getString("ChatLocation", C1067R.string.ChatLocation));
            } else {
                this.actionBar.setTitle(LocaleController.getString("SharedPlace", C1067R.string.SharedPlace));
            }
            createMenu.addItem(1, (int) C1067R.C1065drawable.share).setContentDescription(LocaleController.getString("ShareFile", C1067R.string.ShareFile));
        }
        this.otherItem = createMenu.addItem(0, (int) C1067R.C1065drawable.ic_ab_other);
        this.otherItem.addSubItem(2, (int) C1067R.C1065drawable.msg_map, (CharSequence) "Standard OSM");
        this.otherItem.addSubItem(3, (int) C1067R.C1065drawable.msg_map, (CharSequence) "Wikimedia");
        this.otherItem.addSubItem(4, (int) C1067R.C1065drawable.msg_map, (CharSequence) "Carto Dark");
        this.otherItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", C1067R.string.AccDescrMoreOptions));
        this.fragmentView = new FrameLayout(context2) {
            private boolean first = true;

            /* Access modifiers changed, original: protected */
            public void onLayout(boolean z, int i, int i2, int i3, int i4) {
                super.onLayout(z, i, i2, i3, i4);
                if (z) {
                    LocationActivity.this.fixLayoutInternal(this.first);
                    this.first = false;
                }
            }
        };
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        this.locationButton = new ImageView(context2);
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.m26dp(56.0f), Theme.getColor(Theme.key_profile_actionBackground), Theme.getColor(Theme.key_profile_actionPressedBackground));
        if (VERSION.SDK_INT < 21) {
            Drawable mutate = context.getResources().getDrawable(C1067R.C1065drawable.floating_shadow_profile).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, Mode.MULTIPLY));
            Drawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.m26dp(56.0f), AndroidUtilities.m26dp(56.0f));
            createSimpleSelectorCircleDrawable = combinedDrawable;
        }
        this.locationButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable);
        this.locationButton.setImageResource(C1067R.C1065drawable.myloc_on);
        this.locationButton.setScaleType(ScaleType.CENTER);
        this.locationButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_profile_actionIcon), Mode.MULTIPLY));
        this.locationButton.setContentDescription(LocaleController.getString("AccDescrMyLocation", C1067R.string.AccDescrMyLocation));
        String str2 = "translationZ";
        if (VERSION.SDK_INT >= 21) {
            StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(this.locationButton, str2, new float[]{(float) AndroidUtilities.m26dp(2.0f), (float) AndroidUtilities.m26dp(4.0f)}).setDuration(200));
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.locationButton, str2, new float[]{(float) AndroidUtilities.m26dp(4.0f), (float) AndroidUtilities.m26dp(2.0f)}).setDuration(200));
            this.locationButton.setStateListAnimator(stateListAnimator);
            this.locationButton.setOutlineProvider(new C30704());
        }
        if (this.messageObject != null) {
            this.userLocation = new Location("network");
            this.userLocation.setLatitude(this.messageObject.messageOwner.media.geo.lat);
            this.userLocation.setLongitude(this.messageObject.messageOwner.media.geo._long);
        }
        this.searchWas = false;
        this.searching = false;
        this.mapViewClip = new FrameLayout(context2);
        this.mapViewClip.addView(getAttributionOverlay(context), LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 80, LocaleController.isRTL ? 0.0f : 4.0f, 0.0f, LocaleController.isRTL ? 4.0f : 0.0f, 5.0f));
        this.mapViewClip.setBackgroundDrawable(new MapPlaceholderDrawable());
        LocationActivityAdapter locationActivityAdapter = this.adapter;
        if (locationActivityAdapter != null) {
            locationActivityAdapter.destroy();
        }
        LocationActivitySearchAdapter locationActivitySearchAdapter = this.searchAdapter;
        if (locationActivitySearchAdapter != null) {
            locationActivitySearchAdapter.destroy();
        }
        this.listView = new RecyclerListView(context2);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        RecyclerListView recyclerListView = this.listView;
        LocationActivityAdapter locationActivityAdapter2 = new LocationActivityAdapter(context2, this.liveLocationType, this.dialogId);
        this.adapter = locationActivityAdapter2;
        recyclerListView.setAdapter(locationActivityAdapter2);
        this.listView.setVerticalScrollBarEnabled(false);
        recyclerListView = this.listView;
        C42175 c42175 = new LinearLayoutManager(context2, 1, false) {
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        this.layoutManager = c42175;
        recyclerListView.setLayoutManager(c42175);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setOnScrollListener(new C42156());
        this.listView.setOnItemClickListener(new C3730-$$Lambda$LocationActivity$hR-6D0AqpgGzQVaKwSZDSV7l5U0(this));
        this.adapter.setDelegate(this.dialogId, new C3727-$$Lambda$LocationActivity$1r8OhsfCqopqgQX3qFh-nzBXEt8(this));
        this.adapter.setOverScrollHeight(this.overScrollHeight);
        frameLayout.addView(this.mapViewClip, LayoutHelper.createFrame(-1, -1, 51));
        Configuration.getInstance().setUserAgentValue("Telegram-FOSS(F-Droid) 5.7.1");
        this.mapView = new MapView(context2) {
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (LocationActivity.this.messageObject == null) {
                    AnimatorSet access$1700;
                    String str = "translationY";
                    String str2 = "alpha";
                    Animator[] animatorArr;
                    if (motionEvent.getAction() == 0) {
                        if (LocationActivity.this.animatorSet != null) {
                            LocationActivity.this.animatorSet.cancel();
                        }
                        LocationActivity.this.animatorSet = new AnimatorSet();
                        LocationActivity.this.animatorSet.setDuration(200);
                        access$1700 = LocationActivity.this.animatorSet;
                        animatorArr = new Animator[2];
                        animatorArr[0] = ObjectAnimator.ofFloat(LocationActivity.this.markerImageView, str, new float[]{(float) (LocationActivity.this.markerTop + (-AndroidUtilities.m26dp(10.0f)))});
                        animatorArr[1] = ObjectAnimator.ofFloat(LocationActivity.this.markerXImageView, str2, new float[]{1.0f});
                        access$1700.playTogether(animatorArr);
                        LocationActivity.this.animatorSet.start();
                    } else if (motionEvent.getAction() == 1) {
                        if (LocationActivity.this.animatorSet != null) {
                            LocationActivity.this.animatorSet.cancel();
                        }
                        LocationActivity.this.animatorSet = new AnimatorSet();
                        LocationActivity.this.animatorSet.setDuration(200);
                        access$1700 = LocationActivity.this.animatorSet;
                        animatorArr = new Animator[2];
                        animatorArr[0] = ObjectAnimator.ofFloat(LocationActivity.this.markerImageView, str, new float[]{(float) LocationActivity.this.markerTop});
                        animatorArr[1] = ObjectAnimator.ofFloat(LocationActivity.this.markerXImageView, str2, new float[]{0.0f});
                        access$1700.playTogether(animatorArr);
                        LocationActivity.this.animatorSet.start();
                    }
                    if (motionEvent.getAction() == 2) {
                        if (!LocationActivity.this.userLocationMoved) {
                            access$1700 = new AnimatorSet();
                            access$1700.setDuration(200);
                            access$1700.play(ObjectAnimator.ofFloat(LocationActivity.this.locationButton, str2, new float[]{1.0f}));
                            access$1700.start();
                            LocationActivity.this.userLocationMoved = true;
                        }
                        if (!(LocationActivity.this.mapView == null || LocationActivity.this.userLocation == null)) {
                            LocationActivity.this.userLocation.setLatitude(LocationActivity.this.mapView.getMapCenter().getLatitude());
                            LocationActivity.this.userLocation.setLongitude(LocationActivity.this.mapView.getMapCenter().getLongitude());
                        }
                        LocationActivity.this.adapter.setCustomLocation(LocationActivity.this.userLocation);
                    }
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        AndroidUtilities.runOnUIThread(new C1602-$$Lambda$LocationActivity$RudJicCJug8Kjbchqc9GJliyPHo(this));
        View view = new View(context2);
        view.setBackgroundResource(C1067R.C1065drawable.header_shadow_reverse);
        this.mapViewClip.addView(view, LayoutHelper.createFrame(-1, 3, 83));
        messageObject = this.messageObject;
        if (messageObject == null) {
            this.markerImageView = new ImageView(context2);
            this.markerImageView.setImageResource(C1067R.C1065drawable.map_pin);
            this.mapViewClip.addView(this.markerImageView, LayoutHelper.createFrame(24, 42, 49));
            this.markerXImageView = new ImageView(context2);
            this.markerXImageView.setAlpha(0.0f);
            this.markerXImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_location_markerX), Mode.MULTIPLY));
            this.markerXImageView.setImageResource(C1067R.C1065drawable.place_x);
            this.mapViewClip.addView(this.markerXImageView, LayoutHelper.createFrame(14, 14, 49));
            this.emptyView = new EmptyTextProgressView(context2);
            this.emptyView.setText(LocaleController.getString("NoResult", C1067R.string.NoResult));
            this.emptyView.setShowAtCenter(true);
            this.emptyView.setVisibility(8);
            frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
            this.searchListView = new RecyclerListView(context2);
            this.searchListView.setVisibility(8);
            this.searchListView.setLayoutManager(new LinearLayoutManager(context2, 1, false));
            RecyclerListView recyclerListView2 = this.searchListView;
            LocationActivitySearchAdapter locationActivitySearchAdapter2 = new LocationActivitySearchAdapter(context2);
            this.searchAdapter = locationActivitySearchAdapter2;
            recyclerListView2.setAdapter(locationActivitySearchAdapter2);
            frameLayout.addView(this.searchListView, LayoutHelper.createFrame(-1, -1, 51));
            this.searchListView.setOnScrollListener(new C42168());
            this.searchListView.setOnItemClickListener(new C3729-$$Lambda$LocationActivity$S5fuT9kSC5GcWeIpW8kQl94_IX8(this));
        } else if (!messageObject.isLiveLocation()) {
            this.routeButton = new ImageView(context2);
            Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.m26dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
            if (VERSION.SDK_INT < 21) {
                Drawable mutate2 = context.getResources().getDrawable(C1067R.C1065drawable.floating_shadow).mutate();
                mutate2.setColorFilter(new PorterDuffColorFilter(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, Mode.MULTIPLY));
                Drawable combinedDrawable2 = new CombinedDrawable(mutate2, createSimpleSelectorCircleDrawable2, 0, 0);
                combinedDrawable2.setIconSize(AndroidUtilities.m26dp(56.0f), AndroidUtilities.m26dp(56.0f));
                createSimpleSelectorCircleDrawable2 = combinedDrawable2;
            }
            this.routeButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable2);
            this.routeButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), Mode.MULTIPLY));
            this.routeButton.setImageResource(C1067R.C1065drawable.navigate);
            this.routeButton.setScaleType(ScaleType.CENTER);
            if (VERSION.SDK_INT >= 21) {
                StateListAnimator stateListAnimator2 = new StateListAnimator();
                stateListAnimator2.addState(new int[]{16842919}, ObjectAnimator.ofFloat(this.routeButton, str2, new float[]{(float) AndroidUtilities.m26dp(2.0f), (float) AndroidUtilities.m26dp(4.0f)}).setDuration(200));
                stateListAnimator2.addState(new int[0], ObjectAnimator.ofFloat(this.routeButton, str2, new float[]{(float) AndroidUtilities.m26dp(4.0f), (float) AndroidUtilities.m26dp(2.0f)}).setDuration(200));
                this.routeButton.setStateListAnimator(stateListAnimator2);
                this.routeButton.setOutlineProvider(new C30719());
            }
            frameLayout.addView(this.routeButton, LayoutHelper.createFrame(VERSION.SDK_INT >= 21 ? 56 : 60, VERSION.SDK_INT >= 21 ? 56.0f : 60.0f, (LocaleController.isRTL ? 3 : 5) | 80, LocaleController.isRTL ? 14.0f : 0.0f, 0.0f, LocaleController.isRTL ? 0.0f : 14.0f, 37.0f));
            this.routeButton.setOnClickListener(new C1603-$$Lambda$LocationActivity$XHy3PqbpZ0y74_5Ir43yVn45rF8(this));
            this.adapter.setMessageObject(this.messageObject);
        }
        MessageObject messageObject2 = this.messageObject;
        if (messageObject2 == null || messageObject2.isLiveLocation()) {
            this.mapViewClip.addView(this.locationButton, LayoutHelper.createFrame(VERSION.SDK_INT >= 21 ? 56 : 60, VERSION.SDK_INT >= 21 ? 56.0f : 60.0f, (LocaleController.isRTL ? 3 : 5) | 80, LocaleController.isRTL ? 14.0f : 0.0f, 0.0f, LocaleController.isRTL ? 0.0f : 14.0f, 14.0f));
        } else {
            this.mapViewClip.addView(this.locationButton, LayoutHelper.createFrame(VERSION.SDK_INT >= 21 ? 56 : 60, VERSION.SDK_INT >= 21 ? 56.0f : 60.0f, (LocaleController.isRTL ? 3 : 5) | 80, LocaleController.isRTL ? 14.0f : 0.0f, 0.0f, LocaleController.isRTL ? 0.0f : 14.0f, 43.0f));
        }
        this.locationButton.setOnClickListener(new C1605-$$Lambda$LocationActivity$_ONGGuxSOSrFr5q_KgV4tF6eRgk(this));
        if (this.messageObject == null) {
            this.locationButton.setAlpha(0.0f);
        }
        frameLayout.addView(this.actionBar);
        return this.fragmentView;
    }

    /* JADX WARNING: Missing block: B:42:0x00e3, code skipped:
            return;
     */
    public /* synthetic */ void lambda$createView$1$LocationActivity(android.view.View r9, int r10) {
        /*
        r8 = this;
        r0 = 4611686018427387904; // 0x4000000000000000 float:0.0 double:2.0;
        r9 = 0;
        r2 = 1;
        if (r10 != r2) goto L_0x0039;
    L_0x0006:
        r3 = r8.messageObject;
        if (r3 == 0) goto L_0x0039;
    L_0x000a:
        r3 = r3.isLiveLocation();
        if (r3 != 0) goto L_0x0039;
    L_0x0010:
        r10 = r8.mapView;
        if (r10 == 0) goto L_0x011f;
    L_0x0014:
        r10 = r10.getController();
        r2 = new org.osmdroid.util.GeoPoint;
        r3 = r8.messageObject;
        r3 = r3.messageOwner;
        r3 = r3.media;
        r3 = r3.geo;
        r4 = r3.lat;
        r6 = r3._long;
        r2.<init>(r4, r6);
        r3 = r8.mapView;
        r3 = r3.getMaxZoomLevel();
        r3 = r3 - r0;
        r0 = java.lang.Double.valueOf(r3);
        r10.animateTo(r2, r0, r9);
        goto L_0x011f;
    L_0x0039:
        r3 = 2;
        if (r10 != r2) goto L_0x007c;
    L_0x003c:
        r4 = r8.liveLocationType;
        if (r4 == r3) goto L_0x007c;
    L_0x0040:
        r9 = r8.delegate;
        if (r9 == 0) goto L_0x0077;
    L_0x0044:
        r9 = r8.userLocation;
        if (r9 == 0) goto L_0x0077;
    L_0x0048:
        r9 = new org.telegram.tgnet.TLRPC$TL_messageMediaGeo;
        r9.<init>();
        r10 = new org.telegram.tgnet.TLRPC$TL_geoPoint;
        r10.<init>();
        r9.geo = r10;
        r10 = r9.geo;
        r0 = r8.userLocation;
        r0 = r0.getLatitude();
        r0 = org.telegram.messenger.AndroidUtilities.fixLocationCoord(r0);
        r10.lat = r0;
        r10 = r9.geo;
        r0 = r8.userLocation;
        r0 = r0.getLongitude();
        r0 = org.telegram.messenger.AndroidUtilities.fixLocationCoord(r0);
        r10._long = r0;
        r10 = r8.delegate;
        r0 = r8.liveLocationType;
        r10.didSelectLocation(r9, r0);
    L_0x0077:
        r8.finishFragment();
        goto L_0x011f;
    L_0x007c:
        if (r10 != r3) goto L_0x0082;
    L_0x007e:
        r4 = r8.liveLocationType;
        if (r4 == r2) goto L_0x008f;
    L_0x0082:
        if (r10 != r2) goto L_0x0088;
    L_0x0084:
        r2 = r8.liveLocationType;
        if (r2 == r3) goto L_0x008f;
    L_0x0088:
        r2 = 3;
        if (r10 != r2) goto L_0x00e4;
    L_0x008b:
        r3 = r8.liveLocationType;
        if (r3 != r2) goto L_0x00e4;
    L_0x008f:
        r10 = r8.currentAccount;
        r10 = org.telegram.messenger.LocationController.getInstance(r10);
        r0 = r8.dialogId;
        r10 = r10.isSharingLocation(r0);
        if (r10 == 0) goto L_0x00ad;
    L_0x009d:
        r9 = r8.currentAccount;
        r9 = org.telegram.messenger.LocationController.getInstance(r9);
        r0 = r8.dialogId;
        r9.removeSharingLocation(r0);
        r8.finishFragment();
        goto L_0x011f;
    L_0x00ad:
        r10 = r8.delegate;
        if (r10 == 0) goto L_0x00e3;
    L_0x00b1:
        r10 = r8.getParentActivity();
        if (r10 != 0) goto L_0x00b8;
    L_0x00b7:
        goto L_0x00e3;
    L_0x00b8:
        r10 = r8.myLocation;
        if (r10 == 0) goto L_0x011f;
    L_0x00bc:
        r0 = r8.dialogId;
        r10 = (int) r0;
        if (r10 <= 0) goto L_0x00d2;
    L_0x00c1:
        r9 = r8.currentAccount;
        r9 = org.telegram.messenger.MessagesController.getInstance(r9);
        r0 = r8.dialogId;
        r10 = (int) r0;
        r10 = java.lang.Integer.valueOf(r10);
        r9 = r9.getUser(r10);
    L_0x00d2:
        r10 = r8.getParentActivity();
        r0 = new org.telegram.ui.-$$Lambda$LocationActivity$3XRrdwHi6MppmANpYRKFISBi3vg;
        r0.<init>(r8);
        r9 = org.telegram.p004ui.Components.AlertsCreator.createLocationUpdateDialog(r10, r9, r0);
        r8.showDialog(r9);
        goto L_0x011f;
    L_0x00e3:
        return;
    L_0x00e4:
        r2 = r8.adapter;
        r10 = r2.getItem(r10);
        r2 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
        if (r2 == 0) goto L_0x00ff;
    L_0x00ee:
        if (r10 == 0) goto L_0x00fb;
    L_0x00f0:
        r9 = r8.delegate;
        if (r9 == 0) goto L_0x00fb;
    L_0x00f4:
        r10 = (org.telegram.tgnet.TLRPC.TL_messageMediaVenue) r10;
        r0 = r8.liveLocationType;
        r9.didSelectLocation(r10, r0);
    L_0x00fb:
        r8.finishFragment();
        goto L_0x011f;
    L_0x00ff:
        r2 = r10 instanceof org.telegram.p004ui.LocationActivity.LiveLocation;
        if (r2 == 0) goto L_0x011f;
    L_0x0103:
        r2 = r8.mapView;
        r2 = r2.getController();
        r10 = (org.telegram.p004ui.LocationActivity.LiveLocation) r10;
        r10 = r10.marker;
        r10 = r10.getPosition();
        r3 = r8.mapView;
        r3 = r3.getMaxZoomLevel();
        r3 = r3 - r0;
        r0 = java.lang.Double.valueOf(r3);
        r2.animateTo(r10, r0, r9);
    L_0x011f:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.LocationActivity.lambda$createView$1$LocationActivity(android.view.View, int):void");
    }

    public /* synthetic */ void lambda$null$0$LocationActivity(int i) {
        TL_messageMediaGeoLive tL_messageMediaGeoLive = new TL_messageMediaGeoLive();
        tL_messageMediaGeoLive.geo = new TL_geoPoint();
        tL_messageMediaGeoLive.geo.lat = AndroidUtilities.fixLocationCoord(this.myLocation.getLatitude());
        tL_messageMediaGeoLive.geo._long = AndroidUtilities.fixLocationCoord(this.myLocation.getLongitude());
        tL_messageMediaGeoLive.period = i;
        this.delegate.didSelectLocation(tL_messageMediaGeoLive, this.liveLocationType);
        finishFragment();
    }

    public /* synthetic */ void lambda$createView$2$LocationActivity(ArrayList arrayList) {
        if (!(this.wasResults || arrayList.isEmpty())) {
            this.wasResults = true;
        }
        this.emptyView.showTextView();
    }

    public /* synthetic */ void lambda$createView$3$LocationActivity() {
        if (this.mapView != null && getParentActivity() != null) {
            this.mapView.setPadding(AndroidUtilities.m26dp(70.0f), 0, AndroidUtilities.m26dp(70.0f), AndroidUtilities.m26dp(10.0f));
            onMapInit();
            this.mapsInitialized = true;
            if (this.onResumeCalled) {
                this.mapView.onResume();
            }
        }
    }

    public /* synthetic */ void lambda$createView$4$LocationActivity(View view, int i) {
        TL_messageMediaVenue item = this.searchAdapter.getItem(i);
        if (item != null) {
            LocationActivityDelegate locationActivityDelegate = this.delegate;
            if (locationActivityDelegate != null) {
                locationActivityDelegate.didSelectLocation(item, this.liveLocationType);
            }
        }
        finishFragment();
    }

    public /* synthetic */ void lambda$createView$5$LocationActivity(View view) {
        if (VERSION.SDK_INT >= 23) {
            Activity parentActivity = getParentActivity();
            if (!(parentActivity == null || parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0)) {
                showPermissionAlert(true);
                return;
            }
        }
        if (this.myLocation != null) {
            try {
                getParentActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(String.format(Locale.US, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f", new Object[]{Double.valueOf(this.myLocation.getLatitude()), Double.valueOf(this.myLocation.getLongitude()), Double.valueOf(this.messageObject.messageOwner.media.geo.lat), Double.valueOf(this.messageObject.messageOwner.media.geo._long)}))));
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
    }

    public /* synthetic */ void lambda$createView$6$LocationActivity(View view) {
        if (VERSION.SDK_INT >= 23) {
            Activity parentActivity = getParentActivity();
            if (!(parentActivity == null || parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0)) {
                showPermissionAlert(false);
                return;
            }
        }
        if (this.messageObject != null) {
            if (this.myLocation != null) {
                MapView mapView = this.mapView;
                if (mapView != null) {
                    mapView.getController().animateTo(new GeoPoint(this.myLocation.getLatitude(), this.myLocation.getLongitude()), Double.valueOf(this.mapView.getMaxZoomLevel() - 2.0d), null);
                }
            }
        } else if (!(this.myLocation == null || this.mapView == null)) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(200);
            animatorSet.play(ObjectAnimator.ofFloat(this.locationButton, "alpha", new float[]{0.0f}));
            animatorSet.start();
            this.adapter.setCustomLocation(null);
            this.userLocationMoved = false;
            this.mapView.getController().animateTo(new GeoPoint(this.myLocation.getLatitude(), this.myLocation.getLongitude()));
        }
    }

    private TextView getAttributionOverlay(Context context) {
        this.attributionOverlay = new TextView(context);
        this.attributionOverlay.setText(Html.fromHtml("© <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors"));
        this.attributionOverlay.setShadowLayer(1.0f, -1.0f, -1.0f, -1);
        this.attributionOverlay.setLinksClickable(true);
        this.attributionOverlay.setMovementMethod(LinkMovementMethod.getInstance());
        return this.attributionOverlay;
    }

    private Bitmap createUserBitmap(LiveLocation liveLocation) {
        Bitmap createBitmap;
        Throwable th;
        try {
            TLObject tLObject = (liveLocation.user == null || liveLocation.user.photo == null) ? (liveLocation.chat == null || liveLocation.chat.photo == null) ? null : liveLocation.chat.photo.photo_small : liveLocation.user.photo.photo_small;
            createBitmap = Bitmap.createBitmap(AndroidUtilities.m26dp(62.0f), AndroidUtilities.m26dp(76.0f), Config.ARGB_8888);
            try {
                createBitmap.eraseColor(0);
                Canvas canvas = new Canvas(createBitmap);
                Drawable drawable = ApplicationLoader.applicationContext.getResources().getDrawable(C1067R.C1065drawable.livepin);
                drawable.setBounds(0, 0, AndroidUtilities.m26dp(62.0f), AndroidUtilities.m26dp(76.0f));
                drawable.draw(canvas);
                Paint paint = new Paint(1);
                RectF rectF = new RectF();
                canvas.save();
                if (tLObject != null) {
                    Bitmap decodeFile = BitmapFactory.decodeFile(FileLoader.getPathToAttach(tLObject, true).toString());
                    if (decodeFile != null) {
                        BitmapShader bitmapShader = new BitmapShader(decodeFile, TileMode.CLAMP, TileMode.CLAMP);
                        Matrix matrix = new Matrix();
                        float dp = ((float) AndroidUtilities.m26dp(52.0f)) / ((float) decodeFile.getWidth());
                        matrix.postTranslate((float) AndroidUtilities.m26dp(5.0f), (float) AndroidUtilities.m26dp(5.0f));
                        matrix.postScale(dp, dp);
                        paint.setShader(bitmapShader);
                        bitmapShader.setLocalMatrix(matrix);
                        rectF.set((float) AndroidUtilities.m26dp(5.0f), (float) AndroidUtilities.m26dp(5.0f), (float) AndroidUtilities.m26dp(57.0f), (float) AndroidUtilities.m26dp(57.0f));
                        canvas.drawRoundRect(rectF, (float) AndroidUtilities.m26dp(26.0f), (float) AndroidUtilities.m26dp(26.0f), paint);
                    }
                } else {
                    AvatarDrawable avatarDrawable = new AvatarDrawable();
                    if (liveLocation.user != null) {
                        avatarDrawable.setInfo(liveLocation.user);
                    } else if (liveLocation.chat != null) {
                        avatarDrawable.setInfo(liveLocation.chat);
                    }
                    canvas.translate((float) AndroidUtilities.m26dp(5.0f), (float) AndroidUtilities.m26dp(5.0f));
                    avatarDrawable.setBounds(0, 0, AndroidUtilities.m26dp(52.2f), AndroidUtilities.m26dp(52.2f));
                    avatarDrawable.draw(canvas);
                }
                canvas.restore();
                try {
                    canvas.setBitmap(null);
                } catch (Exception unused) {
                }
            } catch (Throwable th2) {
                th = th2;
                FileLog.m30e(th);
                return createBitmap;
            }
        } catch (Throwable th3) {
            th = th3;
            createBitmap = null;
            FileLog.m30e(th);
            return createBitmap;
        }
        return createBitmap;
    }

    private int getMessageId(Message message) {
        int i = message.from_id;
        if (i != 0) {
            return i;
        }
        return (int) MessageObject.getDialogId(message);
    }

    private LiveLocation addUserMarker(Message message) {
        TLRPC.GeoPoint geoPoint = message.media.geo;
        GeoPoint geoPoint2 = new GeoPoint(geoPoint.lat, geoPoint._long);
        LiveLocation liveLocation = (LiveLocation) this.markersMap.get(message.from_id);
        if (liveLocation == null) {
            liveLocation = new LiveLocation();
            liveLocation.object = message;
            if (liveLocation.object.from_id != 0) {
                liveLocation.user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(liveLocation.object.from_id));
                liveLocation.f611id = liveLocation.object.from_id;
            } else {
                int dialogId = (int) MessageObject.getDialogId(message);
                if (dialogId > 0) {
                    liveLocation.user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(dialogId));
                    liveLocation.f611id = dialogId;
                } else {
                    liveLocation.chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-dialogId));
                    liveLocation.f611id = dialogId;
                }
            }
            try {
                Marker marker = new Marker(this.mapView);
                marker.setPosition(geoPoint2);
                marker.setOnMarkerClickListener(new C421110());
                Bitmap createUserBitmap = createUserBitmap(liveLocation);
                if (createUserBitmap != null) {
                    marker.setIcon(new BitmapDrawable(getParentActivity().getResources(), createUserBitmap));
                    marker.setAnchor(0.5f, 0.907f);
                    this.mapView.getOverlays().add(marker);
                    liveLocation.marker = marker;
                    this.markers.add(liveLocation);
                    this.markersMap.put(liveLocation.f611id, liveLocation);
                    SharingLocationInfo sharingLocationInfo = LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId);
                    if (liveLocation.f611id == UserConfig.getInstance(this.currentAccount).getClientUserId() && sharingLocationInfo != null && liveLocation.object.f461id == sharingLocationInfo.mid && this.myLocation != null) {
                        liveLocation.marker.setPosition(new GeoPoint(this.myLocation.getLatitude(), this.myLocation.getLongitude()));
                    }
                }
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        } else {
            liveLocation.object = message;
            liveLocation.marker.setPosition(geoPoint2);
        }
        return liveLocation;
    }

    private void onMapInit() {
        if (this.mapView != null) {
            GeoPoint geoPoint = new GeoPoint(48.85825d, 2.29448d);
            IMapController controller = this.mapView.getController();
            this.mapView.setMaxZoomLevel(Double.valueOf(20.0d));
            this.mapView.setMultiTouchControls(true);
            this.mapView.setBuiltInZoomControls(false);
            controller.setCenter(geoPoint);
            controller.setZoom(7.0d);
            MessageObject messageObject = this.messageObject;
            String str = "network";
            if (messageObject == null) {
                this.userLocation = new Location(str);
                this.userLocation.setLatitude(48.85825d);
                this.userLocation.setLongitude(2.29448d);
            } else if (messageObject.isLiveLocation()) {
                LiveLocation addUserMarker = addUserMarker(this.messageObject.messageOwner);
                if (!getRecentLocations()) {
                    controller.setCenter(addUserMarker.marker.getPosition());
                    controller.setZoom(this.mapView.getMaxZoomLevel() - 2.0d);
                }
            } else {
                geoPoint = new GeoPoint(this.userLocation.getLatitude(), this.userLocation.getLongitude());
                Marker marker = new Marker(this.mapView);
                marker.setPosition(geoPoint);
                marker.setOnMarkerClickListener(new C421211());
                if (VERSION.SDK_INT >= 21) {
                    marker.setIcon(getParentActivity().getDrawable(C1067R.C1065drawable.map_pin));
                } else {
                    marker.setIcon(getParentActivity().getResources().getDrawable(C1067R.C1065drawable.map_pin));
                }
                marker.setAnchor(0.5f, 1.0f);
                this.mapView.getOverlays().add(marker);
                controller.setCenter(geoPoint);
                controller.setZoom(this.mapView.getMaxZoomLevel() - 2.0d);
                this.firstFocus = false;
                getRecentLocations();
            }
            GpsMyLocationProvider gpsMyLocationProvider = new GpsMyLocationProvider(getParentActivity());
            gpsMyLocationProvider.setLocationUpdateMinDistance(10.0f);
            gpsMyLocationProvider.setLocationUpdateMinTime(10000);
            gpsMyLocationProvider.addLocationSource(str);
            this.myLocationOverlay = new MyLocationNewOverlay(gpsMyLocationProvider, this.mapView) {
                public void onLocationChanged(final Location location, IMyLocationProvider iMyLocationProvider) {
                    super.onLocationChanged(location, iMyLocationProvider);
                    if (location != null) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                LocationActivity.this.positionMarker(location);
                                LocationController.getInstance(LocationActivity.this.currentAccount).setGoogleMapLocation(location, LocationActivity.this.isFirstLocation);
                                LocationActivity.this.isFirstLocation = false;
                            }
                        });
                    }
                }
            };
            this.myLocationOverlay.enableMyLocation();
            this.myLocationOverlay.setDrawAccuracyEnabled(true);
            this.myLocationOverlay.runOnFirstFix(new C306813());
            this.mapView.getOverlays().add(this.myLocationOverlay);
            Location lastLocation = getLastLocation();
            this.myLocation = lastLocation;
            positionMarker(lastLocation);
            this.attributionOverlay.bringToFront();
            if (this.checkGpsEnabled && getParentActivity() != null) {
                this.checkGpsEnabled = false;
                if (getParentActivity().getPackageManager().hasSystemFeature("android.hardware.location.gps")) {
                    try {
                        if (!((LocationManager) ApplicationLoader.applicationContext.getSystemService("location")).isProviderEnabled("gps")) {
                            Builder builder = new Builder(getParentActivity());
                            builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
                            builder.setMessage(LocaleController.getString("GpsDisabledAlert", C1067R.string.GpsDisabledAlert));
                            builder.setPositiveButton(LocaleController.getString("ConnectingToProxyEnable", C1067R.string.ConnectingToProxyEnable), new C1600-$$Lambda$LocationActivity$DsWjN4ehaLhnVMMrqLPs4UpCHbU(this));
                            builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
                            showDialog(builder.create());
                        }
                    } catch (Exception e) {
                        FileLog.m30e(e);
                    }
                }
            }
        }
    }

    public /* synthetic */ void lambda$onMapInit$7$LocationActivity(DialogInterface dialogInterface, int i) {
        if (getParentActivity() != null) {
            try {
                getParentActivity().startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            } catch (Exception unused) {
            }
        }
    }

    private void showPermissionAlert(boolean z) {
        if (getParentActivity() != null) {
            Builder builder = new Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
            if (z) {
                builder.setMessage(LocaleController.getString("PermissionNoLocationPosition", C1067R.string.PermissionNoLocationPosition));
            } else {
                builder.setMessage(LocaleController.getString("PermissionNoLocation", C1067R.string.PermissionNoLocation));
            }
            builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", C1067R.string.PermissionOpenSettings), new C1599-$$Lambda$LocationActivity$8fdfxSn0ih3r889TzPZkGetJL7o(this));
            builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), null);
            showDialog(builder.create());
        }
    }

    public /* synthetic */ void lambda$showPermissionAlert$8$LocationActivity(DialogInterface dialogInterface, int i) {
        if (getParentActivity() != null) {
            try {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("package:");
                stringBuilder.append(ApplicationLoader.applicationContext.getPackageName());
                intent.setData(Uri.parse(stringBuilder.toString()));
                getParentActivity().startActivity(intent);
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
    }

    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z) {
            try {
                if (this.mapView.getParent() instanceof ViewGroup) {
                    ((ViewGroup) this.mapView.getParent()).removeView(this.mapView);
                }
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            FrameLayout frameLayout = this.mapViewClip;
            if (frameLayout != null) {
                frameLayout.addView(this.mapView, 0, LayoutHelper.createFrame(-1, this.overScrollHeight + AndroidUtilities.m26dp(10.0f), 51));
                updateClipView(this.layoutManager.findFirstVisibleItemPosition());
                return;
            }
            View view = this.fragmentView;
            if (view != null) {
                ((FrameLayout) view).addView(this.mapView, 0, LayoutHelper.createFrame(-1, -1, 51));
            }
        }
    }

    private void updateClipView(int i) {
        if (i != -1) {
            View childAt = this.listView.getChildAt(0);
            if (childAt != null) {
                int i2;
                if (i == 0) {
                    i = childAt.getTop();
                    i2 = this.overScrollHeight + (i < 0 ? i : 0);
                } else {
                    i = 0;
                    i2 = 0;
                }
                if (((LayoutParams) this.mapViewClip.getLayoutParams()) != null) {
                    if (i2 <= 0) {
                        if (this.mapView.getVisibility() == 0) {
                            this.mapView.setVisibility(4);
                            this.mapViewClip.setVisibility(4);
                        }
                    } else if (this.mapView.getVisibility() == 4) {
                        this.mapView.setVisibility(0);
                        this.mapViewClip.setVisibility(0);
                    }
                    this.mapViewClip.setTranslationY((float) Math.min(0, i));
                    int i3 = -i;
                    this.mapView.setTranslationY((float) Math.max(0, i3 / 2));
                    ImageView imageView = this.markerImageView;
                    if (imageView != null) {
                        i2 /= 2;
                        int dp = (i3 - AndroidUtilities.m26dp(42.0f)) + i2;
                        this.markerTop = dp;
                        imageView.setTranslationY((float) dp);
                        this.markerXImageView.setTranslationY((float) ((i3 - AndroidUtilities.m26dp(7.0f)) + i2));
                    }
                    ImageView imageView2 = this.routeButton;
                    if (imageView2 != null) {
                        imageView2.setTranslationY((float) i);
                    }
                    LayoutParams layoutParams = (LayoutParams) this.mapView.getLayoutParams();
                    if (!(layoutParams == null || layoutParams.height == this.overScrollHeight + AndroidUtilities.m26dp(10.0f))) {
                        layoutParams.height = this.overScrollHeight + AndroidUtilities.m26dp(10.0f);
                        MapView mapView = this.mapView;
                        if (mapView != null) {
                            mapView.setPadding(AndroidUtilities.m26dp(70.0f), 0, AndroidUtilities.m26dp(70.0f), AndroidUtilities.m26dp(10.0f));
                        }
                        this.mapView.setLayoutParams(layoutParams);
                    }
                }
            }
        }
    }

    private void fixLayoutInternal(boolean z) {
        if (this.listView != null) {
            int currentActionBarHeight = (this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + C2190ActionBar.getCurrentActionBarHeight();
            int measuredHeight = this.fragmentView.getMeasuredHeight();
            if (measuredHeight != 0) {
                this.overScrollHeight = (measuredHeight - AndroidUtilities.m26dp(66.0f)) - currentActionBarHeight;
                LayoutParams layoutParams = (LayoutParams) this.listView.getLayoutParams();
                layoutParams.topMargin = currentActionBarHeight;
                this.listView.setLayoutParams(layoutParams);
                layoutParams = (LayoutParams) this.mapViewClip.getLayoutParams();
                layoutParams.topMargin = currentActionBarHeight;
                layoutParams.height = this.overScrollHeight;
                this.mapViewClip.setLayoutParams(layoutParams);
                RecyclerListView recyclerListView = this.searchListView;
                if (recyclerListView != null) {
                    layoutParams = (LayoutParams) recyclerListView.getLayoutParams();
                    layoutParams.topMargin = currentActionBarHeight;
                    this.searchListView.setLayoutParams(layoutParams);
                }
                this.adapter.setOverScrollHeight(this.overScrollHeight);
                LayoutParams layoutParams2 = (LayoutParams) this.mapView.getLayoutParams();
                if (layoutParams2 != null) {
                    layoutParams2.height = this.overScrollHeight + AndroidUtilities.m26dp(10.0f);
                    MapView mapView = this.mapView;
                    if (mapView != null) {
                        mapView.setPadding(AndroidUtilities.m26dp(70.0f), 0, AndroidUtilities.m26dp(70.0f), AndroidUtilities.m26dp(10.0f));
                    }
                    this.mapView.setLayoutParams(layoutParams2);
                }
                this.adapter.notifyDataSetChanged();
                if (z) {
                    LinearLayoutManager linearLayoutManager = this.layoutManager;
                    measuredHeight = this.liveLocationType;
                    measuredHeight = (measuredHeight == 1 || measuredHeight == 2) ? 66 : 0;
                    linearLayoutManager.scrollToPositionWithOffset(0, -AndroidUtilities.m26dp((float) (32 + measuredHeight)));
                    updateClipView(this.layoutManager.findFirstVisibleItemPosition());
                    this.listView.post(new C1601-$$Lambda$LocationActivity$LYBCu_7L40XJGlKJA2wAVIIvy2s(this));
                } else {
                    updateClipView(this.layoutManager.findFirstVisibleItemPosition());
                }
            }
        }
    }

    public /* synthetic */ void lambda$fixLayoutInternal$9$LocationActivity() {
        LinearLayoutManager linearLayoutManager = this.layoutManager;
        int i = this.liveLocationType;
        i = (i == 1 || i == 2) ? 66 : 0;
        linearLayoutManager.scrollToPositionWithOffset(0, -AndroidUtilities.m26dp((float) (32 + i)));
        updateClipView(this.layoutManager.findFirstVisibleItemPosition());
    }

    private Location getLastLocation() {
        Location location = null;
        if (VERSION.SDK_INT >= 23 && getParentActivity().checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0 && getParentActivity().checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            return null;
        }
        LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
        List providers = locationManager.getProviders(true);
        for (int size = providers.size() - 1; size >= 0; size--) {
            location = locationManager.getLastKnownLocation((String) providers.get(size));
            if (location != null) {
                break;
            }
        }
        return location;
    }

    private void positionMarker(Location location) {
        if (location != null) {
            this.myLocation = new Location(location);
            LiveLocation liveLocation = (LiveLocation) this.markersMap.get(UserConfig.getInstance(this.currentAccount).getClientUserId());
            SharingLocationInfo sharingLocationInfo = LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId);
            if (!(liveLocation == null || sharingLocationInfo == null || liveLocation.object.f461id != sharingLocationInfo.mid)) {
                liveLocation.marker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
            }
            if (this.messageObject != null || this.mapView == null) {
                this.adapter.setGpsLocation(this.myLocation);
            } else {
                GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                LocationActivityAdapter locationActivityAdapter = this.adapter;
                if (locationActivityAdapter != null) {
                    if (locationActivityAdapter.isPulledUp()) {
                        this.adapter.searchPlacesWithQuery(null, this.myLocation, true);
                    }
                    this.adapter.setGpsLocation(this.myLocation);
                }
                if (!this.userLocationMoved) {
                    this.userLocation = new Location(location);
                    if (this.firstWas) {
                        this.mapView.getController().animateTo(geoPoint);
                    } else {
                        this.firstWas = true;
                        IMapController controller = this.mapView.getController();
                        controller.setZoom(this.mapView.getMaxZoomLevel() - 2.0d);
                        controller.setCenter(geoPoint);
                    }
                }
            }
        }
    }

    public void setMessageObject(MessageObject messageObject) {
        this.messageObject = messageObject;
        this.dialogId = this.messageObject.getDialogId();
    }

    public void setDialogId(long j) {
        this.dialogId = j;
    }

    private void fetchRecentLocations(ArrayList<Message> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        boolean z = this.firstFocus;
        BoundingBox boundingBox = null;
        int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        for (int i = 0; i < arrayList.size(); i++) {
            Message message = (Message) arrayList.get(i);
            int i2 = message.date;
            MessageMedia messageMedia = message.media;
            if (i2 + messageMedia.period > currentTime) {
                if (this.firstFocus) {
                    TLRPC.GeoPoint geoPoint = messageMedia.geo;
                    arrayList2.add(new GeoPoint(geoPoint.lat, geoPoint._long));
                }
                addUserMarker(message);
            }
        }
        if (arrayList2.size() > 0) {
            boundingBox = BoundingBox.fromGeoPoints(arrayList2);
        }
        if (this.firstFocus) {
            this.firstFocus = false;
            this.adapter.setLiveLocations(this.markers);
            if (this.messageObject.isLiveLocation()) {
                try {
                    if (arrayList.size() > 1) {
                        try {
                            this.mapView.zoomToBoundingBox(boundingBox, false, AndroidUtilities.m26dp(60.0f));
                        } catch (Exception e) {
                            FileLog.m30e(e);
                        }
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    private boolean getRecentLocations() {
        ArrayList arrayList = (ArrayList) LocationController.getInstance(this.currentAccount).locationsCache.get(this.messageObject.getDialogId());
        if (arrayList == null || !arrayList.isEmpty()) {
            arrayList = null;
        } else {
            fetchRecentLocations(arrayList);
        }
        int i = (int) this.dialogId;
        boolean z = false;
        if (i < 0) {
            Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-i));
            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                return false;
            }
        }
        TL_messages_getRecentLocations tL_messages_getRecentLocations = new TL_messages_getRecentLocations();
        long dialogId = this.messageObject.getDialogId();
        tL_messages_getRecentLocations.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int) dialogId);
        tL_messages_getRecentLocations.limit = 100;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getRecentLocations, new C3731-$$Lambda$LocationActivity$w8_XrXsKbv633ZrXpPVuZnH-vEE(this, dialogId));
        if (arrayList != null) {
            z = true;
        }
        return z;
    }

    public /* synthetic */ void lambda$getRecentLocations$11$LocationActivity(long j, TLObject tLObject, TL_error tL_error) {
        if (tLObject != null) {
            AndroidUtilities.runOnUIThread(new C1604-$$Lambda$LocationActivity$ZNLFZtIfepogzb1cHZ2vLaOhvOA(this, tLObject, j));
        }
    }

    public /* synthetic */ void lambda$null$10$LocationActivity(TLObject tLObject, long j) {
        if (this.mapView != null) {
            messages_Messages messages_messages = (messages_Messages) tLObject;
            int i = 0;
            while (i < messages_messages.messages.size()) {
                if (!(((Message) messages_messages.messages.get(i)).media instanceof TL_messageMediaGeoLive)) {
                    messages_messages.messages.remove(i);
                    i--;
                }
                i++;
            }
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
            MessagesController.getInstance(this.currentAccount).putUsers(messages_messages.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(messages_messages.chats, false);
            LocationController.getInstance(this.currentAccount).locationsCache.put(j, messages_messages.messages);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.liveLocationsCacheChanged, Long.valueOf(j));
            fetchRecentLocations(messages_messages.messages);
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.closeChats) {
            removeSelfFromStack();
        } else if (i != NotificationCenter.locationPermissionGranted) {
            int i3 = 0;
            LocationActivityAdapter locationActivityAdapter;
            if (i == NotificationCenter.didReceiveNewMessages) {
                if (((Long) objArr[0]).longValue() == this.dialogId && this.messageObject != null) {
                    ArrayList arrayList = (ArrayList) objArr[1];
                    Object obj = null;
                    while (i3 < arrayList.size()) {
                        MessageObject messageObject = (MessageObject) arrayList.get(i3);
                        if (messageObject.isLiveLocation()) {
                            addUserMarker(messageObject.messageOwner);
                            obj = 1;
                        }
                        i3++;
                    }
                    if (obj != null) {
                        locationActivityAdapter = this.adapter;
                        if (locationActivityAdapter != null) {
                            locationActivityAdapter.setLiveLocations(this.markers);
                        }
                    }
                }
            } else if (i != NotificationCenter.messagesDeleted && i == NotificationCenter.replaceMessagesObjects) {
                long longValue = ((Long) objArr[0]).longValue();
                if (longValue == this.dialogId && this.messageObject != null) {
                    ArrayList arrayList2 = (ArrayList) objArr[1];
                    Object obj2 = null;
                    while (i3 < arrayList2.size()) {
                        MessageObject messageObject2 = (MessageObject) arrayList2.get(i3);
                        if (messageObject2.isLiveLocation()) {
                            LiveLocation liveLocation = (LiveLocation) this.markersMap.get(getMessageId(messageObject2.messageOwner));
                            if (liveLocation != null) {
                                SharingLocationInfo sharingLocationInfo = LocationController.getInstance(this.currentAccount).getSharingLocationInfo(longValue);
                                if (sharingLocationInfo == null || sharingLocationInfo.mid != messageObject2.getId()) {
                                    Marker marker = liveLocation.marker;
                                    TLRPC.GeoPoint geoPoint = messageObject2.messageOwner.media.geo;
                                    marker.setPosition(new GeoPoint(geoPoint.lat, geoPoint._long));
                                }
                                obj2 = 1;
                            }
                        }
                        i3++;
                    }
                    if (obj2 != null) {
                        locationActivityAdapter = this.adapter;
                        if (locationActivityAdapter != null) {
                            locationActivityAdapter.updateLiveLocations();
                        }
                    }
                }
            }
        } else if (this.mapView != null && this.mapsInitialized) {
            this.myLocationOverlay.enableMyLocation();
        }
    }

    public void onPause() {
        super.onPause();
        MapView mapView = this.mapView;
        if (mapView != null && this.mapsInitialized) {
            try {
                mapView.onPause();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            if (this.mapView.getOverlays().contains(this.myLocationOverlay)) {
                this.mapView.getOverlays().remove(this.myLocationOverlay);
            }
            this.myLocationOverlay.disableMyLocation();
        }
        this.onResumeCalled = false;
    }

    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
        MapView mapView = this.mapView;
        if (mapView != null && this.mapsInitialized) {
            try {
                mapView.onResume();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            this.mapView.getOverlays().add(this.myLocationOverlay);
            this.myLocationOverlay.enableMyLocation();
        }
        this.onResumeCalled = true;
        fixLayoutInternal(true);
        if (this.checkPermission && VERSION.SDK_INT >= 23) {
            Activity parentActivity = getParentActivity();
            if (parentActivity != null) {
                this.checkPermission = false;
                if (parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    parentActivity.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
                }
            }
        }
    }

    public void setDelegate(LocationActivityDelegate locationActivityDelegate) {
        this.delegate = locationActivityDelegate;
    }

    private void updateSearchInterface() {
        LocationActivityAdapter locationActivityAdapter = this.adapter;
        if (locationActivityAdapter != null) {
            locationActivityAdapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        C3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk c3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk = C3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk.INSTANCE;
        r10 = new ThemeDescription[53];
        r10[12] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r10[13] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder);
        r10[14] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle);
        r10[15] = new ThemeDescription(this.locationButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_profile_actionIcon);
        r10[16] = new ThemeDescription(this.locationButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_profile_actionBackground);
        r10[17] = new ThemeDescription(this.locationButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_profile_actionPressedBackground);
        r10[18] = new ThemeDescription(this.routeButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chats_actionIcon);
        r10[19] = new ThemeDescription(this.routeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chats_actionBackground);
        r10[20] = new ThemeDescription(this.routeButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_chats_actionPressedBackground);
        r10[21] = new ThemeDescription(this.markerXImageView, 0, null, null, null, null, Theme.key_location_markerX);
        View view = this.listView;
        Class[] clsArr = new Class[]{GraySectionCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        r10[22] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_graySectionText);
        r10[23] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, null, null, null, Theme.key_graySection);
        C3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk c3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk2 = c3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk;
        r10[24] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, c3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk2, Theme.key_avatar_text);
        r10[25] = new ThemeDescription(null, 0, null, null, null, c3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk2, Theme.key_avatar_backgroundRed);
        r10[26] = new ThemeDescription(null, 0, null, null, null, c3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk2, Theme.key_avatar_backgroundOrange);
        r10[27] = new ThemeDescription(null, 0, null, null, null, c3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk2, Theme.key_avatar_backgroundViolet);
        r10[28] = new ThemeDescription(null, 0, null, null, null, c3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk2, Theme.key_avatar_backgroundGreen);
        r10[29] = new ThemeDescription(null, 0, null, null, null, c3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk2, Theme.key_avatar_backgroundCyan);
        r10[30] = new ThemeDescription(null, 0, null, null, null, c3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk2, Theme.key_avatar_backgroundBlue);
        r10[31] = new ThemeDescription(null, 0, null, null, null, c3732-$$Lambda$LocationActivity$wcqGz1znpcXufa-L0mTh0dpx_bk2, Theme.key_avatar_backgroundPink);
        r10[32] = new ThemeDescription(null, 0, null, null, null, null, Theme.key_location_liveLocationProgress);
        r10[33] = new ThemeDescription(null, 0, null, null, null, null, Theme.key_location_placeLocationBackground);
        r10[34] = new ThemeDescription(null, 0, null, null, null, null, Theme.key_dialog_liveLocationProgress);
        view = this.listView;
        int i = ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG;
        Class[] clsArr2 = new Class[]{SendLocationCell.class};
        String[] strArr2 = new String[1];
        strArr2[0] = "imageView";
        r10[35] = new ThemeDescription(view, i, clsArr2, strArr2, null, null, null, Theme.key_location_sendLocationIcon);
        r10[36] = new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_location_sendLiveLocationIcon);
        r10[37] = new ThemeDescription(this.listView, (ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE) | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_location_sendLocationBackground);
        r10[38] = new ThemeDescription(this.listView, (ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE) | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_location_sendLiveLocationBackground);
        r10[39] = new ThemeDescription(this.listView, 0, new Class[]{SendLocationCell.class}, new String[]{"accurateTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        view = this.listView;
        i = ThemeDescription.FLAG_CHECKTAG;
        clsArr2 = new Class[]{SendLocationCell.class};
        strArr2 = new String[1];
        strArr2[0] = "titleTextView";
        r10[40] = new ThemeDescription(view, i, clsArr2, strArr2, null, null, null, Theme.key_windowBackgroundWhiteRedText2);
        r10[41] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"titleTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueText7);
        r10[42] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{LocationCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        view = this.listView;
        clsArr2 = new Class[]{LocationCell.class};
        strArr2 = new String[1];
        strArr2[0] = "nameTextView";
        r10[43] = new ThemeDescription(view, 0, clsArr2, strArr2, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.listView;
        clsArr2 = new Class[]{LocationCell.class};
        strArr2 = new String[1];
        strArr2[0] = "addressTextView";
        r10[44] = new ThemeDescription(view, 0, clsArr2, strArr2, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        r10[45] = new ThemeDescription(this.searchListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{LocationCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        r10[46] = new ThemeDescription(this.searchListView, 0, new Class[]{LocationCell.class}, new String[]{"nameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r10[47] = new ThemeDescription(this.searchListView, 0, new Class[]{LocationCell.class}, new String[]{"addressTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        r10[48] = new ThemeDescription(this.listView, 0, new Class[]{LocationLoadingCell.class}, new String[]{"progressBar"}, null, null, null, Theme.key_progressCircle);
        r10[49] = new ThemeDescription(this.listView, 0, new Class[]{LocationLoadingCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        r10[50] = new ThemeDescription(this.listView, 0, new Class[]{LocationPoweredCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        r10[51] = new ThemeDescription(this.listView, 0, new Class[]{LocationPoweredCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        r10[52] = new ThemeDescription(this.listView, 0, new Class[]{LocationPoweredCell.class}, new String[]{"textView2"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        return r10;
    }
}

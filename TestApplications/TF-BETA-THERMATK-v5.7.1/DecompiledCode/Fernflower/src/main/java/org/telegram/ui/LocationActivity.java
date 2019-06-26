package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
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
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.-..Lambda.LocationActivity.wcqGz1znpcXufa-L0mTh0dpx_bk;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.LocationActivityAdapter;
import org.telegram.ui.Adapters.LocationActivitySearchAdapter;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.LocationCell;
import org.telegram.ui.Cells.LocationLoadingCell;
import org.telegram.ui.Cells.LocationPoweredCell;
import org.telegram.ui.Cells.SendLocationCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MapPlaceholderDrawable;
import org.telegram.ui.Components.RecyclerListView;

public class LocationActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
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
   private LocationActivity.LocationActivityDelegate delegate;
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
   private ArrayList markers = new ArrayList();
   private SparseArray markersMap = new SparseArray();
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
   private boolean userLocationMoved = false;
   private boolean wasResults;

   public LocationActivity(int var1) {
      this.overScrollHeight = AndroidUtilities.displaySize.x - ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(66.0F);
      this.liveLocationType = var1;
   }

   // $FF: synthetic method
   static int access$2500(LocationActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2700(LocationActivity var0) {
      return var0.currentAccount;
   }

   private LocationActivity.LiveLocation addUserMarker(TLRPC.Message var1) {
      TLRPC.GeoPoint var2 = var1.media.geo;
      GeoPoint var3 = new GeoPoint(var2.lat, var2._long);
      LocationActivity.LiveLocation var14 = (LocationActivity.LiveLocation)this.markersMap.get(var1.from_id);
      LocationActivity.LiveLocation var12;
      if (var14 == null) {
         var14 = new LocationActivity.LiveLocation();
         var14.object = var1;
         if (var14.object.from_id != 0) {
            var14.user = MessagesController.getInstance(super.currentAccount).getUser(var14.object.from_id);
            var14.id = var14.object.from_id;
         } else {
            int var4 = (int)MessageObject.getDialogId(var1);
            if (var4 > 0) {
               var14.user = MessagesController.getInstance(super.currentAccount).getUser(var4);
               var14.id = var4;
            } else {
               var14.chat = MessagesController.getInstance(super.currentAccount).getChat(-var4);
               var14.id = var4;
            }
         }

         label85: {
            Exception var10000;
            label71: {
               Marker var5;
               Bitmap var15;
               boolean var10001;
               try {
                  var5 = new Marker(this.mapView);
                  var5.setPosition(var3);
                  Marker.OnMarkerClickListener var11 = new Marker.OnMarkerClickListener() {
                     public boolean onMarkerClick(Marker var1, MapView var2) {
                        return false;
                     }
                  };
                  var5.setOnMarkerClickListener(var11);
                  var15 = this.createUserBitmap(var14);
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label71;
               }

               var12 = var14;
               if (var15 == null) {
                  return var12;
               }

               LocationController.SharingLocationInfo var18;
               try {
                  BitmapDrawable var13 = new BitmapDrawable(this.getParentActivity().getResources(), var15);
                  var5.setIcon(var13);
                  var5.setAnchor(0.5F, 0.907F);
                  this.mapView.getOverlays().add(var5);
                  var14.marker = var5;
                  this.markers.add(var14);
                  this.markersMap.put(var14.id, var14);
                  var18 = LocationController.getInstance(super.currentAccount).getSharingLocationInfo(this.dialogId);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label71;
               }

               var12 = var14;

               try {
                  if (var14.id != UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                     return var12;
                  }
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label71;
               }

               var12 = var14;
               if (var18 == null) {
                  return var12;
               }

               var12 = var14;

               try {
                  if (var14.object.id != var18.mid) {
                     return var12;
                  }
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label71;
               }

               var12 = var14;

               try {
                  if (this.myLocation == null) {
                     return var12;
                  }

                  var5 = var14.marker;
                  GeoPoint var17 = new GeoPoint(this.myLocation.getLatitude(), this.myLocation.getLongitude());
                  var5.setPosition(var17);
                  break label85;
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            }

            Exception var16 = var10000;
            FileLog.e((Throwable)var16);
            var12 = var14;
            return var12;
         }

         var12 = var14;
      } else {
         var14.object = var1;
         var14.marker.setPosition(var3);
         var12 = var14;
      }

      return var12;
   }

   private Bitmap createUserBitmap(LocationActivity.LiveLocation param1) {
      // $FF: Couldn't be decompiled
   }

   private void fetchRecentLocations(ArrayList var1) {
      ArrayList var2 = new ArrayList();
      boolean var3 = this.firstFocus;
      BoundingBox var4 = null;
      int var5 = ConnectionsManager.getInstance(super.currentAccount).getCurrentTime();

      int var6;
      for(var6 = 0; var6 < var1.size(); ++var6) {
         TLRPC.Message var7 = (TLRPC.Message)var1.get(var6);
         int var8 = var7.date;
         TLRPC.MessageMedia var9 = var7.media;
         if (var8 + var9.period > var5) {
            if (this.firstFocus) {
               TLRPC.GeoPoint var14 = var9.geo;
               var2.add(new GeoPoint(var14.lat, var14._long));
            }

            this.addUserMarker(var7);
         }
      }

      if (var2.size() > 0) {
         var4 = BoundingBox.fromGeoPoints(var2);
      }

      if (this.firstFocus) {
         this.firstFocus = false;
         this.adapter.setLiveLocations(this.markers);
         if (this.messageObject.isLiveLocation()) {
            boolean var10001;
            try {
               var6 = var1.size();
            } catch (Exception var12) {
               var10001 = false;
               return;
            }

            if (var6 > 1) {
               try {
                  this.mapView.zoomToBoundingBox(var4, false, AndroidUtilities.dp(60.0F));
               } catch (Exception var11) {
                  Exception var13 = var11;

                  try {
                     FileLog.e((Throwable)var13);
                  } catch (Exception var10) {
                     var10001 = false;
                  }
               }
            }
         }
      }

   }

   private void fixLayoutInternal(boolean var1) {
      if (this.listView != null) {
         int var2;
         if (super.actionBar.getOccupyStatusBar()) {
            var2 = AndroidUtilities.statusBarHeight;
         } else {
            var2 = 0;
         }

         int var3 = var2 + ActionBar.getCurrentActionBarHeight();
         var2 = super.fragmentView.getMeasuredHeight();
         if (var2 == 0) {
            return;
         }

         this.overScrollHeight = var2 - AndroidUtilities.dp(66.0F) - var3;
         LayoutParams var4 = (LayoutParams)this.listView.getLayoutParams();
         var4.topMargin = var3;
         this.listView.setLayoutParams(var4);
         var4 = (LayoutParams)this.mapViewClip.getLayoutParams();
         var4.topMargin = var3;
         var4.height = this.overScrollHeight;
         this.mapViewClip.setLayoutParams(var4);
         RecyclerListView var7 = this.searchListView;
         if (var7 != null) {
            var4 = (LayoutParams)var7.getLayoutParams();
            var4.topMargin = var3;
            this.searchListView.setLayoutParams(var4);
         }

         this.adapter.setOverScrollHeight(this.overScrollHeight);
         var4 = (LayoutParams)this.mapView.getLayoutParams();
         if (var4 != null) {
            var4.height = this.overScrollHeight + AndroidUtilities.dp(10.0F);
            MapView var5 = this.mapView;
            if (var5 != null) {
               var5.setPadding(AndroidUtilities.dp(70.0F), 0, AndroidUtilities.dp(70.0F), AndroidUtilities.dp(10.0F));
            }

            this.mapView.setLayoutParams(var4);
         }

         this.adapter.notifyDataSetChanged();
         if (var1) {
            LinearLayoutManager var8 = this.layoutManager;
            var2 = this.liveLocationType;
            byte var6;
            if (var2 != 1 && var2 != 2) {
               var6 = 0;
            } else {
               var6 = 66;
            }

            var8.scrollToPositionWithOffset(0, -AndroidUtilities.dp((float)(32 + var6)));
            this.updateClipView(this.layoutManager.findFirstVisibleItemPosition());
            this.listView.post(new _$$Lambda$LocationActivity$LYBCu_7L40XJGlKJA2wAVIIvy2s(this));
         } else {
            this.updateClipView(this.layoutManager.findFirstVisibleItemPosition());
         }
      }

   }

   private TextView getAttributionOverlay(Context var1) {
      this.attributionOverlay = new TextView(var1);
      this.attributionOverlay.setText(Html.fromHtml("© <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors"));
      this.attributionOverlay.setShadowLayer(1.0F, -1.0F, -1.0F, -1);
      this.attributionOverlay.setLinksClickable(true);
      this.attributionOverlay.setMovementMethod(LinkMovementMethod.getInstance());
      return this.attributionOverlay;
   }

   private Location getLastLocation() {
      int var1 = VERSION.SDK_INT;
      Location var2 = null;
      if (var1 >= 23 && this.getParentActivity().checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0 && this.getParentActivity().checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
         return null;
      } else {
         LocationManager var3 = (LocationManager)ApplicationLoader.applicationContext.getSystemService("location");
         List var4 = var3.getProviders(true);

         for(var1 = var4.size() - 1; var1 >= 0; --var1) {
            var2 = var3.getLastKnownLocation((String)var4.get(var1));
            if (var2 != null) {
               break;
            }
         }

         return var2;
      }
   }

   private int getMessageId(TLRPC.Message var1) {
      int var2 = var1.from_id;
      return var2 != 0 ? var2 : (int)MessageObject.getDialogId(var1);
   }

   private boolean getRecentLocations() {
      ArrayList var1 = (ArrayList)LocationController.getInstance(super.currentAccount).locationsCache.get(this.messageObject.getDialogId());
      if (var1 != null && var1.isEmpty()) {
         this.fetchRecentLocations(var1);
      } else {
         var1 = null;
      }

      int var2 = (int)this.dialogId;
      boolean var3 = false;
      if (var2 < 0) {
         TLRPC.Chat var4 = MessagesController.getInstance(super.currentAccount).getChat(-var2);
         if (ChatObject.isChannel(var4) && !var4.megagroup) {
            return false;
         }
      }

      TLRPC.TL_messages_getRecentLocations var7 = new TLRPC.TL_messages_getRecentLocations();
      long var5 = this.messageObject.getDialogId();
      var7.peer = MessagesController.getInstance(super.currentAccount).getInputPeer((int)var5);
      var7.limit = 100;
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var7, new _$$Lambda$LocationActivity$w8_XrXsKbv633ZrXpPVuZnH_vEE(this, var5));
      if (var1 != null) {
         var3 = true;
      }

      return var3;
   }

   // $FF: synthetic method
   static void lambda$getThemeDescriptions$12() {
   }

   private void onMapInit() {
      if (this.mapView != null) {
         GeoPoint var1 = new GeoPoint(48.85825D, 2.29448D);
         IMapController var2 = this.mapView.getController();
         this.mapView.setMaxZoomLevel(20.0D);
         this.mapView.setMultiTouchControls(true);
         this.mapView.setBuiltInZoomControls(false);
         var2.setCenter(var1);
         var2.setZoom(7.0D);
         MessageObject var5 = this.messageObject;
         if (var5 != null) {
            if (var5.isLiveLocation()) {
               LocationActivity.LiveLocation var6 = this.addUserMarker(this.messageObject.messageOwner);
               if (!this.getRecentLocations()) {
                  var2.setCenter(var6.marker.getPosition());
                  var2.setZoom(this.mapView.getMaxZoomLevel() - 2.0D);
               }
            } else {
               GeoPoint var3 = new GeoPoint(this.userLocation.getLatitude(), this.userLocation.getLongitude());
               Marker var7 = new Marker(this.mapView);
               var7.setPosition(var3);
               var7.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                  public boolean onMarkerClick(Marker var1, MapView var2) {
                     return false;
                  }
               });
               if (VERSION.SDK_INT >= 21) {
                  var7.setIcon(this.getParentActivity().getDrawable(2131165551));
               } else {
                  var7.setIcon(this.getParentActivity().getResources().getDrawable(2131165551));
               }

               var7.setAnchor(0.5F, 1.0F);
               this.mapView.getOverlays().add(var7);
               var2.setCenter(var3);
               var2.setZoom(this.mapView.getMaxZoomLevel() - 2.0D);
               this.firstFocus = false;
               this.getRecentLocations();
            }
         } else {
            this.userLocation = new Location("network");
            this.userLocation.setLatitude(48.85825D);
            this.userLocation.setLongitude(2.29448D);
         }

         GpsMyLocationProvider var8 = new GpsMyLocationProvider(this.getParentActivity());
         var8.setLocationUpdateMinDistance(10.0F);
         var8.setLocationUpdateMinTime(10000L);
         var8.addLocationSource("network");
         this.myLocationOverlay = new MyLocationNewOverlay(var8, this.mapView) {
            public void onLocationChanged(final Location var1, IMyLocationProvider var2) {
               super.onLocationChanged(var1, var2);
               if (var1 != null) {
                  AndroidUtilities.runOnUIThread(new Runnable() {
                     public void run() {
                        LocationActivity.this.positionMarker(var1);
                        LocationController.getInstance(LocationActivity.access$2500(LocationActivity.this)).setGoogleMapLocation(var1, LocationActivity.this.isFirstLocation);
                        LocationActivity.this.isFirstLocation = false;
                     }
                  });
               }

            }
         };
         this.myLocationOverlay.enableMyLocation();
         this.myLocationOverlay.setDrawAccuracyEnabled(true);
         this.myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
               AndroidUtilities.runOnUIThread(new Runnable() {
                  public void run() {
                     LocationActivity var1 = LocationActivity.this;
                     var1.positionMarker(var1.myLocationOverlay.getLastFix());
                     LocationController.getInstance(LocationActivity.access$2700(LocationActivity.this)).setGoogleMapLocation(LocationActivity.this.myLocationOverlay.getLastFix(), LocationActivity.this.isFirstLocation);
                     LocationActivity.this.isFirstLocation = false;
                  }
               });
            }
         });
         this.mapView.getOverlays().add(this.myLocationOverlay);
         Location var10 = this.getLastLocation();
         this.myLocation = var10;
         this.positionMarker(var10);
         this.attributionOverlay.bringToFront();
         if (this.checkGpsEnabled && this.getParentActivity() != null) {
            this.checkGpsEnabled = false;
            if (!this.getParentActivity().getPackageManager().hasSystemFeature("android.hardware.location.gps")) {
               return;
            }

            try {
               if (!((LocationManager)ApplicationLoader.applicationContext.getSystemService("location")).isProviderEnabled("gps")) {
                  AlertDialog.Builder var9 = new AlertDialog.Builder(this.getParentActivity());
                  var9.setTitle(LocaleController.getString("AppName", 2131558635));
                  var9.setMessage(LocaleController.getString("GpsDisabledAlert", 2131559597));
                  String var11 = LocaleController.getString("ConnectingToProxyEnable", 2131559140);
                  _$$Lambda$LocationActivity$DsWjN4ehaLhnVMMrqLPs4UpCHbU var12 = new _$$Lambda$LocationActivity$DsWjN4ehaLhnVMMrqLPs4UpCHbU(this);
                  var9.setPositiveButton(var11, var12);
                  var9.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                  this.showDialog(var9.create());
               }
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }
         }

      }
   }

   private void positionMarker(Location var1) {
      if (var1 != null) {
         this.myLocation = new Location(var1);
         LocationActivity.LiveLocation var2 = (LocationActivity.LiveLocation)this.markersMap.get(UserConfig.getInstance(super.currentAccount).getClientUserId());
         LocationController.SharingLocationInfo var3 = LocationController.getInstance(super.currentAccount).getSharingLocationInfo(this.dialogId);
         if (var2 != null && var3 != null && var2.object.id == var3.mid) {
            var2.marker.setPosition(new GeoPoint(var1.getLatitude(), var1.getLongitude()));
         }

         if (this.messageObject == null && this.mapView != null) {
            GeoPoint var5 = new GeoPoint(var1.getLatitude(), var1.getLongitude());
            LocationActivityAdapter var6 = this.adapter;
            if (var6 != null) {
               if (var6.isPulledUp()) {
                  this.adapter.searchPlacesWithQuery((String)null, this.myLocation, true);
               }

               this.adapter.setGpsLocation(this.myLocation);
            }

            if (!this.userLocationMoved) {
               this.userLocation = new Location(var1);
               if (this.firstWas) {
                  this.mapView.getController().animateTo(var5);
               } else {
                  this.firstWas = true;
                  IMapController var4 = this.mapView.getController();
                  var4.setZoom(this.mapView.getMaxZoomLevel() - 2.0D);
                  var4.setCenter(var5);
               }
            }
         } else {
            this.adapter.setGpsLocation(this.myLocation);
         }

      }
   }

   private void showPermissionAlert(boolean var1) {
      if (this.getParentActivity() != null) {
         AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
         var2.setTitle(LocaleController.getString("AppName", 2131558635));
         if (var1) {
            var2.setMessage(LocaleController.getString("PermissionNoLocationPosition", 2131560418));
         } else {
            var2.setMessage(LocaleController.getString("PermissionNoLocation", 2131560417));
         }

         var2.setNegativeButton(LocaleController.getString("PermissionOpenSettings", 2131560419), new _$$Lambda$LocationActivity$8fdfxSn0ih3r889TzPZkGetJL7o(this));
         var2.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         this.showDialog(var2.create());
      }
   }

   private void updateClipView(int var1) {
      if (var1 != -1) {
         View var2 = this.listView.getChildAt(0);
         if (var2 != null) {
            int var3;
            int var4;
            if (var1 == 0) {
               var1 = var2.getTop();
               var3 = this.overScrollHeight;
               if (var1 < 0) {
                  var4 = var1;
               } else {
                  var4 = 0;
               }

               var4 += var3;
            } else {
               var1 = 0;
               var4 = 0;
            }

            if ((LayoutParams)this.mapViewClip.getLayoutParams() != null) {
               if (var4 <= 0) {
                  if (this.mapView.getVisibility() == 0) {
                     this.mapView.setVisibility(4);
                     this.mapViewClip.setVisibility(4);
                  }
               } else if (this.mapView.getVisibility() == 4) {
                  this.mapView.setVisibility(0);
                  this.mapViewClip.setVisibility(0);
               }

               this.mapViewClip.setTranslationY((float)Math.min(0, var1));
               MapView var7 = this.mapView;
               var3 = -var1;
               var7.setTranslationY((float)Math.max(0, var3 / 2));
               ImageView var8 = this.markerImageView;
               if (var8 != null) {
                  int var5 = AndroidUtilities.dp(42.0F);
                  var4 /= 2;
                  var5 = var3 - var5 + var4;
                  this.markerTop = var5;
                  var8.setTranslationY((float)var5);
                  this.markerXImageView.setTranslationY((float)(var3 - AndroidUtilities.dp(7.0F) + var4));
               }

               var8 = this.routeButton;
               if (var8 != null) {
                  var8.setTranslationY((float)var1);
               }

               LayoutParams var9 = (LayoutParams)this.mapView.getLayoutParams();
               if (var9 != null && var9.height != this.overScrollHeight + AndroidUtilities.dp(10.0F)) {
                  var9.height = this.overScrollHeight + AndroidUtilities.dp(10.0F);
                  MapView var6 = this.mapView;
                  if (var6 != null) {
                     var6.setPadding(AndroidUtilities.dp(70.0F), 0, AndroidUtilities.dp(70.0F), AndroidUtilities.dp(10.0F));
                  }

                  this.mapView.setLayoutParams(var9);
               }
            }
         }

      }
   }

   private void updateSearchInterface() {
      LocationActivityAdapter var1 = this.adapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      if (AndroidUtilities.isTablet()) {
         super.actionBar.setOccupyStatusBar(false);
      }

      super.actionBar.setAddToContainer(false);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               LocationActivity.this.finishFragment();
            } else if (var1 == 2) {
               if (LocationActivity.this.mapView != null) {
                  LocationActivity.this.attributionOverlay.setText(Html.fromHtml("© <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors"));
                  LocationActivity.this.mapView.setTileSource(TileSourceFactory.MAPNIK);
               }
            } else {
               XYTileSource var2;
               if (var1 == 3) {
                  if (LocationActivity.this.mapView != null) {
                     var2 = new XYTileSource("Wikimedia", 0, 19, 256, ".png", new String[]{"https://maps.wikimedia.org/osm-intl/"}, "© OpenStreetMap contributors");
                     LocationActivity.this.attributionOverlay.setText(Html.fromHtml("© <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors"));
                     LocationActivity.this.mapView.setTileSource(var2);
                  }
               } else if (var1 == 4) {
                  if (LocationActivity.this.mapView != null) {
                     var2 = new XYTileSource("Carto Dark", 0, 20, 256, ".png", new String[]{"https://cartodb-basemaps-a.global.ssl.fastly.net/dark_all/", "https://cartodb-basemaps-b.global.ssl.fastly.net/dark_all/", "https://cartodb-basemaps-c.global.ssl.fastly.net/dark_all/", "https://cartodb-basemaps-d.global.ssl.fastly.net/dark_all/"}, "© OpenStreetMap contributors, © CARTO");
                     LocationActivity.this.attributionOverlay.setText(Html.fromHtml("© <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors, © <a href=\"https://carto.com/attributions\">CARTO</a>"));
                     LocationActivity.this.mapView.setTileSource(var2);
                  }
               } else if (var1 == 1) {
                  try {
                     double var3 = LocationActivity.this.messageObject.messageOwner.media.geo.lat;
                     double var5 = LocationActivity.this.messageObject.messageOwner.media.geo._long;
                     Activity var7 = LocationActivity.this.getParentActivity();
                     StringBuilder var8 = new StringBuilder();
                     var8.append("geo:");
                     var8.append(var3);
                     var8.append(",");
                     var8.append(var5);
                     var8.append("?q=");
                     var8.append(var3);
                     var8.append(",");
                     var8.append(var5);
                     Intent var10 = new Intent("android.intent.action.VIEW", Uri.parse(var8.toString()));
                     var7.startActivity(var10);
                  } catch (Exception var9) {
                     FileLog.e((Throwable)var9);
                  }
               }
            }

         }
      });
      ActionBarMenu var2 = super.actionBar.createMenu();
      MessageObject var3 = this.messageObject;
      if (var3 != null) {
         if (var3.isLiveLocation()) {
            super.actionBar.setTitle(LocaleController.getString("AttachLiveLocation", 2131558721));
         } else {
            String var16 = this.messageObject.messageOwner.media.title;
            if (var16 != null && var16.length() > 0) {
               super.actionBar.setTitle(LocaleController.getString("SharedPlace", 2131560770));
            } else {
               super.actionBar.setTitle(LocaleController.getString("ChatLocation", 2131559042));
            }

            var2.addItem(1, 2131165818).setContentDescription(LocaleController.getString("ShareFile", 2131560748));
         }
      } else {
         super.actionBar.setTitle(LocaleController.getString("ShareLocation", 2131560750));
         var2.addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            public void onSearchCollapse() {
               LocationActivity.this.searching = false;
               LocationActivity.this.searchWas = false;
               LocationActivity.this.otherItem.setVisibility(0);
               LocationActivity.this.searchListView.setEmptyView((View)null);
               LocationActivity.this.listView.setVisibility(0);
               LocationActivity.this.mapViewClip.setVisibility(0);
               LocationActivity.this.searchListView.setVisibility(8);
               LocationActivity.this.emptyView.setVisibility(8);
               LocationActivity.this.searchAdapter.searchDelayed((String)null, (Location)null);
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

            public void onTextChanged(EditText var1) {
               if (LocationActivity.this.searchAdapter != null) {
                  String var2 = var1.getText().toString();
                  if (var2.length() != 0) {
                     LocationActivity.this.searchWas = true;
                  }

                  LocationActivity.this.emptyView.showProgress();
                  LocationActivity.this.searchAdapter.searchDelayed(var2, LocationActivity.this.userLocation);
               }
            }
         }).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
      }

      this.otherItem = var2.addItem(0, 2131165416);
      this.otherItem.addSubItem(2, 2131165642, "Standard OSM");
      this.otherItem.addSubItem(3, 2131165642, "Wikimedia");
      this.otherItem.addSubItem(4, 2131165642, "Carto Dark");
      this.otherItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131558443));
      super.fragmentView = new FrameLayout(var1) {
         private boolean first = true;

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            if (var1) {
               LocationActivity.this.fixLayoutInternal(this.first);
               this.first = false;
            }

         }
      };
      FrameLayout var4 = (FrameLayout)super.fragmentView;
      this.locationButton = new ImageView(var1);
      Drawable var18 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), Theme.getColor("profile_actionBackground"), Theme.getColor("profile_actionPressedBackground"));
      Object var13 = var18;
      Drawable var14;
      if (VERSION.SDK_INT < 21) {
         var14 = var1.getResources().getDrawable(2131165388).mutate();
         var14.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
         var13 = new CombinedDrawable(var14, var18, 0, 0);
         ((CombinedDrawable)var13).setIconSize(AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
      }

      this.locationButton.setBackgroundDrawable((Drawable)var13);
      this.locationButton.setImageResource(2131165685);
      this.locationButton.setScaleType(ScaleType.CENTER);
      this.locationButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("profile_actionIcon"), Mode.MULTIPLY));
      this.locationButton.setContentDescription(LocaleController.getString("AccDescrMyLocation", 2131558449));
      if (VERSION.SDK_INT >= 21) {
         StateListAnimator var19 = new StateListAnimator();
         ObjectAnimator var21 = ObjectAnimator.ofFloat(this.locationButton, "translationZ", new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
         var19.addState(new int[]{16842919}, var21);
         var21 = ObjectAnimator.ofFloat(this.locationButton, "translationZ", new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
         var19.addState(new int[0], var21);
         this.locationButton.setStateListAnimator(var19);
         this.locationButton.setOutlineProvider(new ViewOutlineProvider() {
            @SuppressLint({"NewApi"})
            public void getOutline(View var1, Outline var2) {
               var2.setOval(0, 0, AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
            }
         });
      }

      if (this.messageObject != null) {
         this.userLocation = new Location("network");
         this.userLocation.setLatitude(this.messageObject.messageOwner.media.geo.lat);
         this.userLocation.setLongitude(this.messageObject.messageOwner.media.geo._long);
      }

      this.searchWas = false;
      this.searching = false;
      this.mapViewClip = new FrameLayout(var1);
      FrameLayout var25 = this.mapViewClip;
      TextView var22 = this.getAttributionOverlay(var1);
      byte var5;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      float var6;
      if (LocaleController.isRTL) {
         var6 = 0.0F;
      } else {
         var6 = 4.0F;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 4.0F;
      } else {
         var7 = 0.0F;
      }

      var25.addView(var22, LayoutHelper.createFrame(-2, -2.0F, var5 | 80, var6, 0.0F, var7, 5.0F));
      this.mapViewClip.setBackgroundDrawable(new MapPlaceholderDrawable());
      LocationActivityAdapter var23 = this.adapter;
      if (var23 != null) {
         var23.destroy();
      }

      LocationActivitySearchAdapter var26 = this.searchAdapter;
      if (var26 != null) {
         var26.destroy();
      }

      this.listView = new RecyclerListView(var1);
      this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      RecyclerListView var27 = this.listView;
      LocationActivityAdapter var28 = new LocationActivityAdapter(var1, this.liveLocationType, this.dialogId);
      this.adapter = var28;
      var27.setAdapter(var28);
      this.listView.setVerticalScrollBarEnabled(false);
      RecyclerListView var29 = this.listView;
      LinearLayoutManager var30 = new LinearLayoutManager(var1, 1, false) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.layoutManager = var30;
      var29.setLayoutManager(var30);
      var4.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         // $FF: synthetic method
         public void lambda$onScrolled$0$LocationActivity$6() {
            LocationActivity.this.adapter.searchPlacesWithQuery((String)null, LocationActivity.this.myLocation, true);
         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
            if (LocationActivity.this.adapter.getItemCount() != 0) {
               var2 = LocationActivity.this.layoutManager.findFirstVisibleItemPosition();
               if (var2 != -1) {
                  LocationActivity.this.updateClipView(var2);
                  if (var3 > 0 && !LocationActivity.this.adapter.isPulledUp()) {
                     LocationActivity.this.adapter.setPulledUp();
                     if (LocationActivity.this.myLocation != null) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$LocationActivity$6$e4diUUxQq_JKRLDvY3ypH_z7db4(this));
                     }
                  }

               }
            }
         }
      });
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$LocationActivity$hR_6D0AqpgGzQVaKwSZDSV7l5U0(this)));
      this.adapter.setDelegate(this.dialogId, new _$$Lambda$LocationActivity$1r8OhsfCqopqgQX3qFh_nzBXEt8(this));
      this.adapter.setOverScrollHeight(this.overScrollHeight);
      var4.addView(this.mapViewClip, LayoutHelper.createFrame(-1, -1, 51));
      Configuration.getInstance().setUserAgentValue("Telegram-FOSS(F-Droid) 5.7.1");
      this.mapView = new MapView(var1) {
         public boolean onTouchEvent(MotionEvent var1) {
            if (LocationActivity.this.messageObject == null) {
               if (var1.getAction() == 0) {
                  if (LocationActivity.this.animatorSet != null) {
                     LocationActivity.this.animatorSet.cancel();
                  }

                  LocationActivity.this.animatorSet = new AnimatorSet();
                  LocationActivity.this.animatorSet.setDuration(200L);
                  LocationActivity.this.animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(LocationActivity.this.markerImageView, "translationY", new float[]{(float)(LocationActivity.this.markerTop + -AndroidUtilities.dp(10.0F))}), ObjectAnimator.ofFloat(LocationActivity.this.markerXImageView, "alpha", new float[]{1.0F})});
                  LocationActivity.this.animatorSet.start();
               } else if (var1.getAction() == 1) {
                  if (LocationActivity.this.animatorSet != null) {
                     LocationActivity.this.animatorSet.cancel();
                  }

                  LocationActivity.this.animatorSet = new AnimatorSet();
                  LocationActivity.this.animatorSet.setDuration(200L);
                  LocationActivity.this.animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(LocationActivity.this.markerImageView, "translationY", new float[]{(float)LocationActivity.this.markerTop}), ObjectAnimator.ofFloat(LocationActivity.this.markerXImageView, "alpha", new float[]{0.0F})});
                  LocationActivity.this.animatorSet.start();
               }

               if (var1.getAction() == 2) {
                  if (!LocationActivity.this.userLocationMoved) {
                     AnimatorSet var2 = new AnimatorSet();
                     var2.setDuration(200L);
                     var2.play(ObjectAnimator.ofFloat(LocationActivity.this.locationButton, "alpha", new float[]{1.0F}));
                     var2.start();
                     LocationActivity.this.userLocationMoved = true;
                  }

                  if (LocationActivity.this.mapView != null && LocationActivity.this.userLocation != null) {
                     LocationActivity.this.userLocation.setLatitude(LocationActivity.this.mapView.getMapCenter().getLatitude());
                     LocationActivity.this.userLocation.setLongitude(LocationActivity.this.mapView.getMapCenter().getLongitude());
                  }

                  LocationActivity.this.adapter.setCustomLocation(LocationActivity.this.userLocation);
               }
            }

            return super.onTouchEvent(var1);
         }
      };
      AndroidUtilities.runOnUIThread(new _$$Lambda$LocationActivity$RudJicCJug8Kjbchqc9GJliyPHo(this));
      View var31 = new View(var1);
      var31.setBackgroundResource(2131165408);
      this.mapViewClip.addView(var31, LayoutHelper.createFrame(-1, 3, 83));
      MessageObject var32 = this.messageObject;
      byte var8;
      float var9;
      ImageView var17;
      if (var32 == null) {
         this.markerImageView = new ImageView(var1);
         this.markerImageView.setImageResource(2131165551);
         this.mapViewClip.addView(this.markerImageView, LayoutHelper.createFrame(24, 42, 49));
         this.markerXImageView = new ImageView(var1);
         this.markerXImageView.setAlpha(0.0F);
         this.markerXImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("location_markerX"), Mode.MULTIPLY));
         this.markerXImageView.setImageResource(2131165770);
         this.mapViewClip.addView(this.markerXImageView, LayoutHelper.createFrame(14, 14, 49));
         this.emptyView = new EmptyTextProgressView(var1);
         this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
         this.emptyView.setShowAtCenter(true);
         this.emptyView.setVisibility(8);
         var4.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
         this.searchListView = new RecyclerListView(var1);
         this.searchListView.setVisibility(8);
         this.searchListView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
         var27 = this.searchListView;
         LocationActivitySearchAdapter var10 = new LocationActivitySearchAdapter(var1);
         this.searchAdapter = var10;
         var27.setAdapter(var10);
         var4.addView(this.searchListView, LayoutHelper.createFrame(-1, -1, 51));
         this.searchListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView var1, int var2) {
               if (var2 == 1 && LocationActivity.this.searching && LocationActivity.this.searchWas) {
                  AndroidUtilities.hideKeyboard(LocationActivity.this.getParentActivity().getCurrentFocus());
               }

            }
         });
         this.searchListView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$LocationActivity$S5fuT9kSC5GcWeIpW8kQl94_IX8(this)));
      } else if (!var32.isLiveLocation()) {
         this.routeButton = new ImageView(var1);
         var14 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
         Object var12;
         if (VERSION.SDK_INT < 21) {
            Drawable var11 = var1.getResources().getDrawable(2131165387).mutate();
            var11.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
            var12 = new CombinedDrawable(var11, var14, 0, 0);
            ((CombinedDrawable)var12).setIconSize(AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
         } else {
            var12 = var14;
         }

         this.routeButton.setBackgroundDrawable((Drawable)var12);
         this.routeButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), Mode.MULTIPLY));
         this.routeButton.setImageResource(2131165686);
         this.routeButton.setScaleType(ScaleType.CENTER);
         if (VERSION.SDK_INT >= 21) {
            StateListAnimator var15 = new StateListAnimator();
            ObjectAnimator var33 = ObjectAnimator.ofFloat(this.routeButton, "translationZ", new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
            var15.addState(new int[]{16842919}, var33);
            var33 = ObjectAnimator.ofFloat(this.routeButton, "translationZ", new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
            var15.addState(new int[0], var33);
            this.routeButton.setStateListAnimator(var15);
            this.routeButton.setOutlineProvider(new ViewOutlineProvider() {
               @SuppressLint({"NewApi"})
               public void getOutline(View var1, Outline var2) {
                  var2.setOval(0, 0, AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
               }
            });
         }

         var17 = this.routeButton;
         if (VERSION.SDK_INT >= 21) {
            var5 = 56;
         } else {
            var5 = 60;
         }

         if (VERSION.SDK_INT >= 21) {
            var6 = 56.0F;
         } else {
            var6 = 60.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 3;
         } else {
            var8 = 5;
         }

         if (LocaleController.isRTL) {
            var7 = 14.0F;
         } else {
            var7 = 0.0F;
         }

         if (LocaleController.isRTL) {
            var9 = 0.0F;
         } else {
            var9 = 14.0F;
         }

         var4.addView(var17, LayoutHelper.createFrame(var5, var6, var8 | 80, var7, 0.0F, var9, 37.0F));
         this.routeButton.setOnClickListener(new _$$Lambda$LocationActivity$XHy3PqbpZ0y74_5Ir43yVn45rF8(this));
         this.adapter.setMessageObject(this.messageObject);
      }

      MessageObject var20 = this.messageObject;
      if (var20 != null && !var20.isLiveLocation()) {
         FrameLayout var24 = this.mapViewClip;
         ImageView var35 = this.locationButton;
         if (VERSION.SDK_INT >= 21) {
            var5 = 56;
         } else {
            var5 = 60;
         }

         if (VERSION.SDK_INT >= 21) {
            var6 = 56.0F;
         } else {
            var6 = 60.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 3;
         } else {
            var8 = 5;
         }

         if (LocaleController.isRTL) {
            var7 = 14.0F;
         } else {
            var7 = 0.0F;
         }

         if (LocaleController.isRTL) {
            var9 = 0.0F;
         } else {
            var9 = 14.0F;
         }

         var24.addView(var35, LayoutHelper.createFrame(var5, var6, var8 | 80, var7, 0.0F, var9, 43.0F));
      } else {
         FrameLayout var34 = this.mapViewClip;
         var17 = this.locationButton;
         if (VERSION.SDK_INT >= 21) {
            var5 = 56;
         } else {
            var5 = 60;
         }

         if (VERSION.SDK_INT >= 21) {
            var6 = 56.0F;
         } else {
            var6 = 60.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 3;
         } else {
            var8 = 5;
         }

         if (LocaleController.isRTL) {
            var7 = 14.0F;
         } else {
            var7 = 0.0F;
         }

         if (LocaleController.isRTL) {
            var9 = 0.0F;
         } else {
            var9 = 14.0F;
         }

         var34.addView(var17, LayoutHelper.createFrame(var5, var6, var8 | 80, var7, 0.0F, var9, 14.0F));
      }

      this.locationButton.setOnClickListener(new _$$Lambda$LocationActivity$_ONGGuxSOSrFr5q_KgV4tF6eRgk(this));
      if (this.messageObject == null) {
         this.locationButton.setAlpha(0.0F);
      }

      var4.addView(super.actionBar);
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.closeChats) {
         this.removeSelfFromStack();
      } else if (var1 == NotificationCenter.locationPermissionGranted) {
         if (this.mapView != null && this.mapsInitialized) {
            this.myLocationOverlay.enableMyLocation();
         }
      } else {
         var2 = NotificationCenter.didReceiveNewMessages;
         byte var4 = 0;
         byte var5 = 0;
         MessageObject var6;
         boolean var11;
         ArrayList var12;
         LocationActivityAdapter var13;
         if (var1 == var2) {
            if ((Long)var3[0] != this.dialogId || this.messageObject == null) {
               return;
            }

            var12 = (ArrayList)var3[1];
            var11 = false;

            for(var1 = var5; var1 < var12.size(); ++var1) {
               var6 = (MessageObject)var12.get(var1);
               if (var6.isLiveLocation()) {
                  this.addUserMarker(var6.messageOwner);
                  var11 = true;
               }
            }

            if (var11) {
               var13 = this.adapter;
               if (var13 != null) {
                  var13.setLiveLocations(this.markers);
               }
            }
         } else if (var1 != NotificationCenter.messagesDeleted && var1 == NotificationCenter.replaceMessagesObjects) {
            long var7 = (Long)var3[0];
            if (var7 == this.dialogId && this.messageObject != null) {
               var12 = (ArrayList)var3[1];
               var11 = false;

               for(var1 = var4; var1 < var12.size(); ++var1) {
                  var6 = (MessageObject)var12.get(var1);
                  if (var6.isLiveLocation()) {
                     LocationActivity.LiveLocation var9 = (LocationActivity.LiveLocation)this.markersMap.get(this.getMessageId(var6.messageOwner));
                     if (var9 != null) {
                        LocationController.SharingLocationInfo var10 = LocationController.getInstance(super.currentAccount).getSharingLocationInfo(var7);
                        if (var10 == null || var10.mid != var6.getId()) {
                           Marker var15 = var9.marker;
                           TLRPC.GeoPoint var14 = var6.messageOwner.media.geo;
                           var15.setPosition(new GeoPoint(var14.lat, var14._long));
                        }

                        var11 = true;
                     }
                  }
               }

               if (var11) {
                  var13 = this.adapter;
                  if (var13 != null) {
                     var13.updateLiveLocations();
                  }
               }
            }
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      wcqGz1znpcXufa-L0mTh0dpx_bk var1 = _$$Lambda$LocationActivity$wcqGz1znpcXufa_L0mTh0dpx_bk.INSTANCE;
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCH, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearch");
      ThemeDescription var9 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearchPlaceholder");
      ThemeDescription var10 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuBackground");
      ThemeDescription var11 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItem");
      ThemeDescription var12 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItemIcon");
      ThemeDescription var13 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var14 = this.listView;
      Paint var15 = Theme.dividerPaint;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, new ThemeDescription(var14, 0, new Class[]{View.class}, var15, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder"), new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"), new ThemeDescription(this.locationButton, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_actionIcon"), new ThemeDescription(this.locationButton, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_actionBackground"), new ThemeDescription(this.locationButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_actionPressedBackground"), new ThemeDescription(this.routeButton, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionIcon"), new ThemeDescription(this.routeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"), new ThemeDescription(this.routeButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionPressedBackground"), new ThemeDescription(this.markerXImageView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "location_markerX"), new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "key_graySectionText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "graySection"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, var1, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "location_liveLocationProgress"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "location_placeLocationBackground"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialog_liveLocationProgress"), new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "location_sendLocationIcon"), new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "location_sendLiveLocationIcon"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "location_sendLocationBackground"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "location_sendLiveLocationBackground"), new ThemeDescription(this.listView, 0, new Class[]{SendLocationCell.class}, new String[]{"accurateTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"titleTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText2"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{SendLocationCell.class}, new String[]{"titleTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText7"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{LocationCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.listView, 0, new Class[]{LocationCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{LocationCell.class}, new String[]{"addressTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.searchListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{LocationCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.searchListView, 0, new Class[]{LocationCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.searchListView, 0, new Class[]{LocationCell.class}, new String[]{"addressTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.listView, 0, new Class[]{LocationLoadingCell.class}, new String[]{"progressBar"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"), new ThemeDescription(this.listView, 0, new Class[]{LocationLoadingCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.listView, 0, new Class[]{LocationPoweredCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.listView, 0, new Class[]{LocationPoweredCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.listView, 0, new Class[]{LocationPoweredCell.class}, new String[]{"textView2"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3")};
   }

   // $FF: synthetic method
   public void lambda$createView$1$LocationActivity(View var1, int var2) {
      TLRPC.User var4 = null;
      if (var2 == 1) {
         MessageObject var3 = this.messageObject;
         if (var3 != null && !var3.isLiveLocation()) {
            MapView var7 = this.mapView;
            if (var7 != null) {
               IMapController var10 = var7.getController();
               TLRPC.GeoPoint var8 = this.messageObject.messageOwner.media.geo;
               var10.animateTo(new GeoPoint(var8.lat, var8._long), this.mapView.getMaxZoomLevel() - 2.0D, (Long)null);
            }

            return;
         }
      }

      if (var2 == 1 && this.liveLocationType != 2) {
         if (this.delegate != null && this.userLocation != null) {
            TLRPC.TL_messageMediaGeo var6 = new TLRPC.TL_messageMediaGeo();
            var6.geo = new TLRPC.TL_geoPoint();
            var6.geo.lat = AndroidUtilities.fixLocationCoord(this.userLocation.getLatitude());
            var6.geo._long = AndroidUtilities.fixLocationCoord(this.userLocation.getLongitude());
            this.delegate.didSelectLocation(var6, this.liveLocationType);
         }

         this.finishFragment();
      } else if ((var2 != 2 || this.liveLocationType != 1) && (var2 != 1 || this.liveLocationType != 2) && (var2 != 3 || this.liveLocationType != 3)) {
         Object var5 = this.adapter.getItem(var2);
         if (var5 instanceof TLRPC.TL_messageMediaVenue) {
            if (var5 != null) {
               LocationActivity.LocationActivityDelegate var9 = this.delegate;
               if (var9 != null) {
                  var9.didSelectLocation((TLRPC.TL_messageMediaVenue)var5, this.liveLocationType);
               }
            }

            this.finishFragment();
         } else if (var5 instanceof LocationActivity.LiveLocation) {
            this.mapView.getController().animateTo(((LocationActivity.LiveLocation)var5).marker.getPosition(), this.mapView.getMaxZoomLevel() - 2.0D, (Long)null);
         }
      } else if (LocationController.getInstance(super.currentAccount).isSharingLocation(this.dialogId)) {
         LocationController.getInstance(super.currentAccount).removeSharingLocation(this.dialogId);
         this.finishFragment();
      } else {
         if (this.delegate == null || this.getParentActivity() == null) {
            return;
         }

         if (this.myLocation != null) {
            if ((int)this.dialogId > 0) {
               var4 = MessagesController.getInstance(super.currentAccount).getUser((int)this.dialogId);
            }

            this.showDialog(AlertsCreator.createLocationUpdateDialog(this.getParentActivity(), var4, new _$$Lambda$LocationActivity$3XRrdwHi6MppmANpYRKFISBi3vg(this)));
         }
      }

   }

   // $FF: synthetic method
   public void lambda$createView$2$LocationActivity(ArrayList var1) {
      if (!this.wasResults && !var1.isEmpty()) {
         this.wasResults = true;
      }

      this.emptyView.showTextView();
   }

   // $FF: synthetic method
   public void lambda$createView$3$LocationActivity() {
      if (this.mapView != null && this.getParentActivity() != null) {
         this.mapView.setPadding(AndroidUtilities.dp(70.0F), 0, AndroidUtilities.dp(70.0F), AndroidUtilities.dp(10.0F));
         this.onMapInit();
         this.mapsInitialized = true;
         if (this.onResumeCalled) {
            this.mapView.onResume();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$createView$4$LocationActivity(View var1, int var2) {
      TLRPC.TL_messageMediaVenue var4 = this.searchAdapter.getItem(var2);
      if (var4 != null) {
         LocationActivity.LocationActivityDelegate var3 = this.delegate;
         if (var3 != null) {
            var3.didSelectLocation(var4, this.liveLocationType);
         }
      }

      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$createView$5$LocationActivity(View var1) {
      if (VERSION.SDK_INT >= 23) {
         Activity var4 = this.getParentActivity();
         if (var4 != null && var4.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            this.showPermissionAlert(true);
            return;
         }
      }

      Location var2 = this.myLocation;
      if (var2 != null) {
         try {
            Intent var5 = new Intent("android.intent.action.VIEW", Uri.parse(String.format(Locale.US, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f", var2.getLatitude(), this.myLocation.getLongitude(), this.messageObject.messageOwner.media.geo.lat, this.messageObject.messageOwner.media.geo._long)));
            this.getParentActivity().startActivity(var5);
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$createView$6$LocationActivity(View var1) {
      if (VERSION.SDK_INT >= 23) {
         Activity var2 = this.getParentActivity();
         if (var2 != null && var2.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            this.showPermissionAlert(false);
            return;
         }
      }

      if (this.messageObject != null) {
         if (this.myLocation != null) {
            MapView var3 = this.mapView;
            if (var3 != null) {
               var3.getController().animateTo(new GeoPoint(this.myLocation.getLatitude(), this.myLocation.getLongitude()), this.mapView.getMaxZoomLevel() - 2.0D, (Long)null);
            }
         }
      } else if (this.myLocation != null && this.mapView != null) {
         AnimatorSet var4 = new AnimatorSet();
         var4.setDuration(200L);
         var4.play(ObjectAnimator.ofFloat(this.locationButton, "alpha", new float[]{0.0F}));
         var4.start();
         this.adapter.setCustomLocation((Location)null);
         this.userLocationMoved = false;
         this.mapView.getController().animateTo(new GeoPoint(this.myLocation.getLatitude(), this.myLocation.getLongitude()));
      }

   }

   // $FF: synthetic method
   public void lambda$fixLayoutInternal$9$LocationActivity() {
      LinearLayoutManager var1 = this.layoutManager;
      int var2 = this.liveLocationType;
      byte var3;
      if (var2 != 1 && var2 != 2) {
         var3 = 0;
      } else {
         var3 = 66;
      }

      var1.scrollToPositionWithOffset(0, -AndroidUtilities.dp((float)(32 + var3)));
      this.updateClipView(this.layoutManager.findFirstVisibleItemPosition());
   }

   // $FF: synthetic method
   public void lambda$getRecentLocations$11$LocationActivity(long var1, TLObject var3, TLRPC.TL_error var4) {
      if (var3 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LocationActivity$ZNLFZtIfepogzb1cHZ2vLaOhvOA(this, var3, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$LocationActivity(int var1) {
      TLRPC.TL_messageMediaGeoLive var2 = new TLRPC.TL_messageMediaGeoLive();
      var2.geo = new TLRPC.TL_geoPoint();
      var2.geo.lat = AndroidUtilities.fixLocationCoord(this.myLocation.getLatitude());
      var2.geo._long = AndroidUtilities.fixLocationCoord(this.myLocation.getLongitude());
      var2.period = var1;
      this.delegate.didSelectLocation(var2, this.liveLocationType);
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$null$10$LocationActivity(TLObject var1, long var2) {
      if (this.mapView != null) {
         TLRPC.messages_Messages var6 = (TLRPC.messages_Messages)var1;

         int var5;
         for(int var4 = 0; var4 < var6.messages.size(); var4 = var5 + 1) {
            var5 = var4;
            if (!(((TLRPC.Message)var6.messages.get(var4)).media instanceof TLRPC.TL_messageMediaGeoLive)) {
               var6.messages.remove(var4);
               var5 = var4 - 1;
            }
         }

         MessagesStorage.getInstance(super.currentAccount).putUsersAndChats(var6.users, var6.chats, true, true);
         MessagesController.getInstance(super.currentAccount).putUsers(var6.users, false);
         MessagesController.getInstance(super.currentAccount).putChats(var6.chats, false);
         LocationController.getInstance(super.currentAccount).locationsCache.put(var2, var6.messages);
         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.liveLocationsCacheChanged, var2);
         this.fetchRecentLocations(var6.messages);
      }
   }

   // $FF: synthetic method
   public void lambda$onMapInit$7$LocationActivity(DialogInterface var1, int var2) {
      if (this.getParentActivity() != null) {
         try {
            Activity var5 = this.getParentActivity();
            Intent var3 = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
            var5.startActivity(var3);
         } catch (Exception var4) {
         }

      }
   }

   // $FF: synthetic method
   public void lambda$showPermissionAlert$8$LocationActivity(DialogInterface var1, int var2) {
      if (this.getParentActivity() != null) {
         try {
            Intent var5 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            StringBuilder var3 = new StringBuilder();
            var3.append("package:");
            var3.append(ApplicationLoader.applicationContext.getPackageName());
            var5.setData(Uri.parse(var3.toString()));
            this.getParentActivity().startActivity(var5);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

      }
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      super.swipeBackEnabled = false;
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionGranted);
      MessageObject var1 = this.messageObject;
      if (var1 != null && var1.isLiveLocation()) {
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didReceiveNewMessages);
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.replaceMessagesObjects);
      }

      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionGranted);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didReceiveNewMessages);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.replaceMessagesObjects);
      LocationActivityAdapter var1 = this.adapter;
      if (var1 != null) {
         var1.destroy();
      }

      LocationActivitySearchAdapter var2 = this.searchAdapter;
      if (var2 != null) {
         var2.destroy();
      }

      Runnable var3 = this.updateRunnable;
      if (var3 != null) {
         AndroidUtilities.cancelRunOnUIThread(var3);
         this.updateRunnable = null;
      }

   }

   public void onPause() {
      super.onPause();
      MapView var1 = this.mapView;
      if (var1 != null && this.mapsInitialized) {
         try {
            var1.onPause();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
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
      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
      AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
      MapView var1 = this.mapView;
      if (var1 != null && this.mapsInitialized) {
         try {
            var1.onResume();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

         this.mapView.getOverlays().add(this.myLocationOverlay);
         this.myLocationOverlay.enableMyLocation();
      }

      this.onResumeCalled = true;
      this.fixLayoutInternal(true);
      if (this.checkPermission && VERSION.SDK_INT >= 23) {
         Activity var3 = this.getParentActivity();
         if (var3 != null) {
            this.checkPermission = false;
            if (var3.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
               var3.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
            }
         }
      }

   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1) {
         try {
            if (this.mapView.getParent() instanceof ViewGroup) {
               ((ViewGroup)this.mapView.getParent()).removeView(this.mapView);
            }
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         FrameLayout var3 = this.mapViewClip;
         if (var3 != null) {
            var3.addView(this.mapView, 0, LayoutHelper.createFrame(-1, this.overScrollHeight + AndroidUtilities.dp(10.0F), 51));
            this.updateClipView(this.layoutManager.findFirstVisibleItemPosition());
         } else {
            View var5 = super.fragmentView;
            if (var5 != null) {
               ((FrameLayout)var5).addView(this.mapView, 0, LayoutHelper.createFrame(-1, -1, 51));
            }
         }
      }

   }

   public void setDelegate(LocationActivity.LocationActivityDelegate var1) {
      this.delegate = var1;
   }

   public void setDialogId(long var1) {
      this.dialogId = var1;
   }

   public void setMessageObject(MessageObject var1) {
      this.messageObject = var1;
      this.dialogId = this.messageObject.getDialogId();
   }

   public class LiveLocation {
      public TLRPC.Chat chat;
      public int id;
      public Marker marker;
      public TLRPC.Message object;
      public TLRPC.User user;
   }

   public interface LocationActivityDelegate {
      void didSelectLocation(TLRPC.MessageMedia var1, int var2);
   }
}

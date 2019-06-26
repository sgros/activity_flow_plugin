package menion.android.whereyougo.maps.mapsforge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.maps.container.MapPoint;
import menion.android.whereyougo.maps.container.MapPointPack;
import menion.android.whereyougo.maps.mapsforge.filefilter.FilterByFileExtension;
import menion.android.whereyougo.maps.mapsforge.filefilter.ValidMapFile;
import menion.android.whereyougo.maps.mapsforge.filefilter.ValidRenderTheme;
import menion.android.whereyougo.maps.mapsforge.filepicker.FilePicker;
import menion.android.whereyougo.maps.mapsforge.mapgenerator.MapGeneratorFactory;
import menion.android.whereyougo.maps.mapsforge.mapgenerator.MapGeneratorInternal;
import menion.android.whereyougo.maps.mapsforge.overlay.LabelMarker;
import menion.android.whereyougo.maps.mapsforge.overlay.MyLocationOverlay;
import menion.android.whereyougo.maps.mapsforge.overlay.NavigationOverlay;
import menion.android.whereyougo.maps.mapsforge.overlay.PointListOverlay;
import menion.android.whereyougo.maps.mapsforge.overlay.PointOverlay;
import menion.android.whereyougo.maps.mapsforge.overlay.RotationMarker;
import menion.android.whereyougo.maps.mapsforge.overlay.SensorMyLocationOverlay;
import menion.android.whereyougo.maps.mapsforge.preferences.EditPreferences;
import menion.android.whereyougo.maps.utils.VectorMapDataProvider;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.UtilsFormat;
import org.mapsforge.android.AndroidUtils;
import org.mapsforge.android.maps.DebugSettings;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapScaleBar;
import org.mapsforge.android.maps.MapViewPosition;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.android.maps.mapgenerator.TileCache;
import org.mapsforge.android.maps.mapgenerator.tiledownloader.TileDownloader;
import org.mapsforge.android.maps.overlay.Circle;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.android.maps.overlay.PolygonalChain;
import org.mapsforge.android.maps.overlay.Polyline;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.reader.header.MapFileInfo;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

public class MapsforgeActivity extends MapActivity implements IRefreshable {
   public static final String BUNDLE_ALLOW_START_CARTRIDGE = "allowStartCartridge";
   public static final String BUNDLE_CENTER = "center";
   private static final String BUNDLE_CENTER_AT_FIRST_FIX = "centerAtFirstFix";
   public static final String BUNDLE_ITEMS = "items";
   public static final String BUNDLE_NAVIGATE = "navigate";
   private static final String BUNDLE_SHOW_LABELS = "showLabels";
   private static final String BUNDLE_SHOW_MY_LOCATION = "showMyLocation";
   private static final String BUNDLE_SHOW_PINS = "showPins";
   private static final String BUNDLE_SNAP_TO_LOCATION = "snapToLocation";
   private static final int DIALOG_ENTER_COORDINATES = 0;
   private static final int DIALOG_INFO_MAP_FILE = 1;
   private static final int DIALOG_LOCATION_PROVIDER_DISABLED = 2;
   private static final FileFilter FILE_FILTER_EXTENSION_MAP = new FilterByFileExtension(".map");
   private static final FileFilter FILE_FILTER_EXTENSION_XML = new FilterByFileExtension(".xml");
   public static final int FILE_SYSTEM_CACHE_SIZE_DEFAULT = 250;
   public static final int FILE_SYSTEM_CACHE_SIZE_MAX = 500;
   private static final int ICON_SIZE_MAX = 32;
   private static final String KEY_MAP_GENERATOR = "mapGenerator";
   public static final int MOVE_SPEED_DEFAULT = 10;
   public static final int MOVE_SPEED_MAX = 30;
   private static final int SELECT_MAP_FILE = 0;
   private static final int SELECT_RENDER_THEME_FILE = 1;
   private boolean allowStartCartridge;
   private double itemsLatitude;
   private double itemsLongitude;
   private PointListOverlay listOverlay;
   private final Object lock = new Object();
   private MapGeneratorInternal mapGeneratorInternal;
   MyMapView mapView;
   private Menu menu;
   private MyLocationOverlay myLocationOverlay;
   private NavigationOverlay navigationOverlay;
   private ScreenshotCapturer screenshotCapturer;
   private boolean showLabels;
   private boolean showPins;
   private ToggleButton snapToLocationView;
   private final TapEventListener tapListener;
   private WakeLock wakeLock;

   public MapsforgeActivity() {
      this.mapGeneratorInternal = MapGeneratorInternal.BLANK;
      this.showPins = true;
      this.showLabels = true;
      this.allowStartCartridge = false;
      this.tapListener = new TapEventListener() {
         public void onTap(PointOverlay var1) {
            if (var1.getPoint() != null) {
               TextView var2 = (TextView)View.inflate(MapsforgeActivity.this, 2130903056, (ViewGroup)null);
               var2.setText(UtilsFormat.formatGeoPoint(var1.getGeoPoint()) + "\n\n" + Html.fromHtml(var1.getDescription()));
               Builder var4 = (new Builder(MapsforgeActivity.this)).setTitle(var1.getLabel()).setView(var2).setNegativeButton(2131165192, new OnClickListener() {
                  public void onClick(DialogInterface var1, int var2) {
                     var1.dismiss();
                  }
               });
               final String var3 = var1.getPoint().getData();
               if (MapsforgeActivity.this.allowStartCartridge && var3 != null) {
                  var4.setPositiveButton(2131165309, new OnClickListener() {
                     public void onClick(DialogInterface var1, int var2) {
                        Intent var3x = new Intent(MapsforgeActivity.this, MainActivity.class);
                        var3x.putExtra("cguid", var3);
                        var3x.addFlags(335544320);
                        MapsforgeActivity.this.startActivity(var3x);
                        var1.dismiss();
                        MapsforgeActivity.this.finish();
                     }
                  });
               }

               var4.show();
            }

         }
      };
   }

   private void configureMapView() {
      this.mapView.setBuiltInZoomControls(true);
      this.mapView.setClickable(true);
      this.mapView.setFocusable(true);
      MapScaleBar var1 = this.mapView.getMapScaleBar();
      var1.setText(MapScaleBar.TextField.KILOMETER, this.getString(2131165489));
      var1.setText(MapScaleBar.TextField.METER, this.getString(2131165490));
   }

   private Circle createCircle(GeoPoint var1) {
      Paint var2 = new Paint(1);
      var2.setStyle(Style.FILL);
      var2.setColor(-16776961);
      Paint var3 = new Paint(1);
      var3.setStyle(Style.STROKE);
      var3.setColor(-12303292);
      var3.setStrokeWidth(3.0F);
      return new Circle(var1, 0.0F, var2, var3);
   }

   private static Polyline createPolyline(List var0) {
      PolygonalChain var1 = new PolygonalChain(var0);
      Paint var2 = new Paint(1);
      var2.setStyle(Style.STROKE);
      var2.setColor(-65281);
      var2.setStrokeWidth(4.0F);
      return new Polyline(var1, var2);
   }

   private void disableShowMyLocation() {
      if (this.myLocationOverlay.isMyLocationEnabled()) {
         this.myLocationOverlay.disableMyLocation();
         this.disableSnapToLocation(false);
         this.snapToLocationView.setVisibility(8);
      }

   }

   private void disableSnapToLocation(boolean var1) {
      if (this.myLocationOverlay.isSnapToLocationEnabled()) {
         this.myLocationOverlay.setSnapToLocationEnabled(false);
         this.snapToLocationView.setChecked(false);
         this.mapView.setClickable(true);
         if (var1) {
            this.showToastOnUiThread(this.getString(2131165487));
         }
      }

   }

   private void enableShowMyLocation(boolean var1) {
      if (!this.myLocationOverlay.isMyLocationEnabled()) {
         if (!this.myLocationOverlay.enableMyLocation(var1)) {
            this.showDialog(2);
         } else {
            this.mapView.getOverlays().add(this.navigationOverlay);
            this.mapView.getOverlays().add(this.myLocationOverlay);
            this.snapToLocationView.setVisibility(0);
         }
      }

   }

   private void enableSnapToLocation(boolean var1) {
      if (!this.myLocationOverlay.isSnapToLocationEnabled()) {
         this.myLocationOverlay.setSnapToLocationEnabled(true);
         this.snapToLocationView.setChecked(true);
         this.mapView.setClickable(false);
         if (var1) {
            this.showToastOnUiThread(this.getString(2131165488));
         }
      }

   }

   private void gotoLastKnownPosition() {
      Location var1 = null;
      LocationManager var2 = (LocationManager)this.getSystemService("location");
      Iterator var3 = var2.getProviders(true).iterator();

      while(true) {
         Location var4;
         do {
            do {
               if (!var3.hasNext()) {
                  if (var1 != null) {
                     GeoPoint var5 = new GeoPoint(var1.getLatitude(), var1.getLongitude());
                     this.mapView.getMapViewPosition().setCenter(var5);
                  } else {
                     this.showToastOnUiThread(this.getString(2131165414));
                  }

                  return;
               }

               var4 = var2.getLastKnownLocation((String)var3.next());
            } while(var4 == null);
         } while(var1 != null && var1.getAccuracy() <= var4.getAccuracy());

         var1 = var4;
      }
   }

   private void invertSnapToLocation() {
      if (this.myLocationOverlay.isSnapToLocationEnabled()) {
         this.disableSnapToLocation(true);
      } else {
         this.enableSnapToLocation(true);
      }

   }

   private void refreshItems() {
      boolean var1 = true;
      Bundle var2 = this.getIntent().getExtras();
      boolean var3;
      if (var2 != null && var2.getBoolean("center", false)) {
         var3 = true;
      } else {
         var3 = false;
      }

      boolean var4;
      if (var2 != null && var2.getBoolean("navigate", false)) {
         var4 = true;
      } else {
         var4 = false;
      }

      if (var2 == null || !var2.getBoolean("allowStartCartridge", false)) {
         var1 = false;
      }

      this.allowStartCartridge = var1;
      if (var2 != null && var2.containsKey("items")) {
         this.showMapPack(var2.getParcelableArrayList("items"));
      } else {
         this.showMapPack(VectorMapDataProvider.getInstance().getItems());
      }

      if (var3 && this.itemsLatitude != 0.0D && this.itemsLongitude != 0.0D) {
         GeoPoint var5;
         if (var4 && this.navigationOverlay.getTarget() != null) {
            var5 = this.navigationOverlay.getTarget();
         } else {
            var5 = new GeoPoint(this.itemsLatitude, this.itemsLongitude);
         }

         MapPosition var6 = new MapPosition(var5, this.mapView.getMapViewPosition().getZoomLevel());
         this.mapView.getMapViewPosition().setMapPosition(var6);
      }

   }

   private void setMapGenerator(MapGeneratorInternal var1) {
      if (this.mapGeneratorInternal != var1) {
         this.mapGeneratorInternal = var1;
         MapGenerator var2 = MapGeneratorFactory.createMapGenerator(var1);
         this.mapView.setMapGenerator(var2, var1.ordinal());
         TextView var3 = (TextView)this.findViewById(2131492885);
         if (var2 instanceof TileDownloader) {
            String var4 = ((TileDownloader)var2).getAttribution();
            Object var5;
            if (var4 == null) {
               var5 = "";
            } else {
               var5 = Html.fromHtml(var4, (ImageGetter)null, (TagHandler)null);
            }

            var3.setText((CharSequence)var5);
         } else {
            var3.setText("");
         }
      }

   }

   private void showMapPack(ArrayList var1) {
      Object var2 = this.lock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label2883: {
         try {
            this.navigationOverlay.setTarget((GeoPoint)null);
            this.itemsLongitude = 0.0D;
            this.itemsLatitude = 0.0D;
         } catch (Throwable var318) {
            var10000 = var318;
            var10001 = false;
            break label2883;
         }

         int var3 = 0;

         List var4;
         ArrayList var5;
         ArrayList var6;
         Iterator var7;
         try {
            this.listOverlay.clear();
            var4 = this.listOverlay.getOverlayItems();
            var5 = new ArrayList();
            var6 = new ArrayList();
            var7 = var1.iterator();
         } catch (Throwable var311) {
            var10000 = var311;
            var10001 = false;
            break label2883;
         }

         label2873:
         while(true) {
            GeoPoint var324;
            Iterator var326;
            MapPoint var10;
            while(true) {
               MapPointPack var8;
               try {
                  if (!var7.hasNext()) {
                     break label2873;
                  }

                  var8 = (MapPointPack)var7.next();
                  if (var8.isPolygon()) {
                     var1 = new ArrayList();
                     var326 = var8.getPoints().iterator();
                     break;
                  }
               } catch (Throwable var317) {
                  var10000 = var317;
                  var10001 = false;
                  break label2883;
               }

               Object var320;
               int var11;
               label2887: {
                  label2861: {
                     Resources var319;
                     label2860: {
                        try {
                           if (var8.getIcon() != null) {
                              break label2861;
                           }

                           var319 = this.getResources();
                           if (var8.getResource() != 0) {
                              var11 = var8.getResource();
                              break label2860;
                           }
                        } catch (Throwable var316) {
                           var10000 = var316;
                           var10001 = false;
                           break label2883;
                        }

                        var11 = 2130837560;
                     }

                     try {
                        var320 = var319.getDrawable(var11);
                        break label2887;
                     } catch (Throwable var309) {
                        var10000 = var309;
                        var10001 = false;
                        break label2883;
                     }
                  }

                  Bitmap var321;
                  label2889: {
                     Bitmap var9;
                     try {
                        var9 = var8.getIcon();
                        if (var9.getWidth() > 32 && var9.getWidth() >= var9.getHeight()) {
                           var321 = Bitmap.createScaledBitmap(var9, 32, var9.getHeight() * 32 / var9.getWidth(), false);
                           break label2889;
                        }
                     } catch (Throwable var315) {
                        var10000 = var315;
                        var10001 = false;
                        break label2883;
                     }

                     var321 = var9;

                     try {
                        if (var9.getHeight() > 32) {
                           var321 = Bitmap.createScaledBitmap(var9, var9.getWidth() * 32 / var9.getHeight(), 32, false);
                        }
                     } catch (Throwable var314) {
                        var10000 = var314;
                        var10001 = false;
                        break label2883;
                     }
                  }

                  try {
                     var320 = new BitmapDrawable(this.getResources(), var321);
                  } catch (Throwable var308) {
                     var10000 = var308;
                     var10001 = false;
                     break label2883;
                  }
               }

               Drawable var322;
               Iterator var12;
               try {
                  var322 = Marker.boundCenterBottom((Drawable)var320);
                  var12 = var8.getPoints().iterator();
               } catch (Throwable var307) {
                  var10000 = var307;
                  var10001 = false;
                  break label2883;
               }

               var11 = var3;

               while(true) {
                  var3 = var11;

                  try {
                     if (!var12.hasNext()) {
                        break;
                     }

                     var10 = (MapPoint)var12.next();
                     var324 = new GeoPoint(var10.getLatitude(), var10.getLongitude());
                     PointOverlay var325 = new PointOverlay(var324, var322, var10);
                     var325.setMarkerVisible(this.showPins);
                     var325.setLabelVisible(this.showLabels);
                     var6.add(var325);
                     if (var10.isTarget()) {
                        this.navigationOverlay.setTarget(var324);
                     }
                  } catch (Throwable var312) {
                     var10000 = var312;
                     var10001 = false;
                     break label2883;
                  }

                  try {
                     this.itemsLatitude += var10.getLatitude();
                     this.itemsLongitude += var10.getLongitude();
                  } catch (Throwable var306) {
                     var10000 = var306;
                     var10001 = false;
                     break label2883;
                  }

                  ++var11;
               }
            }

            while(true) {
               try {
                  if (!var326.hasNext()) {
                     break;
                  }

                  var10 = (MapPoint)var326.next();
                  var324 = new GeoPoint(var10.getLatitude(), var10.getLongitude());
                  var1.add(var324);
               } catch (Throwable var313) {
                  var10000 = var313;
                  var10001 = false;
                  break label2883;
               }
            }

            try {
               var5.add(createPolyline(var1));
            } catch (Throwable var310) {
               var10000 = var310;
               var10001 = false;
               break label2883;
            }
         }

         try {
            var4.addAll(var5);
            var4.addAll(var6);
         } catch (Throwable var305) {
            var10000 = var305;
            var10001 = false;
            break label2883;
         }

         if (var3 > 0) {
            try {
               this.itemsLatitude /= (double)var3;
               this.itemsLongitude /= (double)var3;
            } catch (Throwable var304) {
               var10000 = var304;
               var10001 = false;
               break label2883;
            }
         }

         label2803:
         try {
            return;
         } catch (Throwable var303) {
            var10000 = var303;
            var10001 = false;
            break label2803;
         }
      }

      while(true) {
         Throwable var323 = var10000;

         try {
            throw var323;
         } catch (Throwable var302) {
            var10000 = var302;
            var10001 = false;
            continue;
         }
      }
   }

   private void startMapFilePicker() {
      FilePicker.setFileDisplayFilter(FILE_FILTER_EXTENSION_MAP);
      FilePicker.setFileSelectFilter(new ValidMapFile());
      this.startActivityForResult(new Intent(this, FilePicker.class), 0);
   }

   private void startRenderThemePicker() {
      FilePicker.setFileDisplayFilter(FILE_FILTER_EXTENSION_XML);
      FilePicker.setFileSelectFilter(new ValidRenderTheme());
      this.startActivityForResult(new Intent(this, FilePicker.class), 1);
   }

   private void visibilityChanged() {
      Object var1 = this.lock;
      synchronized(var1){}

      label248: {
         Throwable var10000;
         boolean var10001;
         label249: {
            List var2;
            try {
               var2 = this.listOverlay.getOverlayItems();
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label249;
            }

            int var3 = 0;

            while(true) {
               try {
                  if (var3 >= var2.size()) {
                     break;
                  }

                  OverlayItem var4 = (OverlayItem)var2.get(var3);
                  if (var4 instanceof LabelMarker) {
                     LabelMarker var26 = (LabelMarker)var4;
                     var26.setMarkerVisible(this.showPins);
                     var26.setLabelVisible(this.showLabels);
                  }
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label249;
               }

               ++var3;
            }

            label231:
            try {
               break label248;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label231;
            }
         }

         while(true) {
            Throwable var25 = var10000;

            try {
               throw var25;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               continue;
            }
         }
      }

      this.mapView.getOverlayController().redrawOverlays();
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      if (var1 == 0) {
         if (var2 == -1) {
            this.disableSnapToLocation(true);
            if (var3 != null && var3.getStringExtra("selectedFile") != null) {
               this.mapView.setMapFile(new File(var3.getStringExtra("selectedFile")));
               this.setMapGenerator(MapGeneratorInternal.DATABASE_RENDERER);
            }
         } else if (var2 == 0 && this.mapView.getMapFile() == null) {
         }
      } else if (var1 == 1 && var2 == -1 && var3 != null && var3.getStringExtra("selectedFile") != null) {
         try {
            MyMapView var4 = this.mapView;
            File var5 = new File(var3.getStringExtra("selectedFile"));
            var4.setRenderTheme(var5);
         } catch (FileNotFoundException var6) {
            this.showToastOnUiThread(var6.getLocalizedMessage());
         }
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(this);
      this.screenshotCapturer = new ScreenshotCapturer(this);
      this.screenshotCapturer.start();
      this.setContentView(2130903041);
      this.mapView = (MyMapView)this.findViewById(2131492881);
      this.configureMapView();
      this.snapToLocationView = (ToggleButton)this.findViewById(2131492882);
      this.snapToLocationView.setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            MapsforgeActivity.this.invertSnapToLocation();
         }
      });
      Drawable var3 = Marker.boundCenter(this.getResources().getDrawable(2130837562));
      this.myLocationOverlay = new SensorMyLocationOverlay(this, this.mapView, new RotationMarker((GeoPoint)null, var3));
      this.navigationOverlay = new NavigationOverlay(this.myLocationOverlay);
      this.listOverlay = new PointListOverlay();
      this.listOverlay.registerOnTapEvent(this.tapListener);
      if (var1 != null) {
         this.showPins = var1.getBoolean("showPins", true);
         this.showLabels = var1.getBoolean("showLabels", true);
         this.allowStartCartridge = var1.getBoolean("allowStartCartridge", false);
      } else {
         this.showPins = var2.getBoolean("showPins", true);
         this.showLabels = var2.getBoolean("showLabels", true);
      }

      this.mapView.getOverlays().add(this.listOverlay);
      this.wakeLock = ((PowerManager)this.getSystemService("power")).newWakeLock(6, "AMV");
      if (var1 != null) {
         if (var1.getBoolean("showMyLocation", true)) {
            this.enableShowMyLocation(var1.getBoolean("centerAtFirstFix", false));
            if (var1.getBoolean("snapToLocation", false)) {
               this.enableSnapToLocation(false);
            }
         }
      } else if (var2.getBoolean("showMyLocation", true)) {
         this.enableShowMyLocation(var2.getBoolean("centerAtFirstFix", false));
         if (var2.getBoolean("snapToLocation", false)) {
            this.enableSnapToLocation(false);
         }
      }

      ToggleButton var5 = (ToggleButton)this.findViewById(2131492883);
      var5.setChecked(this.showPins);
      var5.setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            MapsforgeActivity.this.showPins = ((ToggleButton)var1).isChecked();
            MapsforgeActivity.this.visibilityChanged();
         }
      });
      var5 = (ToggleButton)this.findViewById(2131492884);
      var5.setChecked(this.showLabels);
      var5.setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            MapsforgeActivity.this.showLabels = ((ToggleButton)var1).isChecked();
            MapsforgeActivity.this.visibilityChanged();
         }
      });
      if (var2.contains("mapGenerator")) {
         try {
            this.setMapGenerator(MapGeneratorInternal.valueOf(var2.getString("mapGenerator", (String)null)));
         } catch (Exception var4) {
         }
      }

      this.refreshItems();
   }

   @Deprecated
   protected Dialog onCreateDialog(int var1) {
      AlertDialog var2 = null;
      Builder var3 = new Builder(this);
      if (var1 == 0) {
         var3.setIcon(17301575);
         var3.setTitle(2131165442);
         LayoutInflater var4 = LayoutInflater.from(this);
         final View var5;
         switch(Preferences.FORMAT_COO_LATLON) {
         case 1:
            var5 = var4.inflate(2130903046, (ViewGroup)null);
            var3.setView(var5);
            var3.setPositiveButton(2131165422, new OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
                  MapsforgeActivity.this.disableSnapToLocation(true);
                  double var3 = Double.parseDouble(((EditText)var5.findViewById(2131492909)).getText().toString());
                  double var5x = Double.parseDouble(((EditText)var5.findViewById(2131492910)).getText().toString()) / 60.0D;
                  double var7 = Double.parseDouble(((EditText)var5.findViewById(2131492911)).getText().toString());
                  double var9 = Double.parseDouble(((EditText)var5.findViewById(2131492912)).getText().toString()) / 60.0D;

                  try {
                     GeoPoint var14 = new GeoPoint(var3 + var5x, var7 + var9);
                     SeekBar var11 = (SeekBar)var5.findViewById(2131492907);
                     MapPosition var12 = new MapPosition(var14, (byte)var11.getProgress());
                     MapsforgeActivity.this.mapView.getMapViewPosition().setMapPosition(var12);
                  } catch (IllegalArgumentException var13) {
                  }

               }
            });
            break;
         case 2:
            var5 = var4.inflate(2130903047, (ViewGroup)null);
            var3.setView(var5);
            var3.setPositiveButton(2131165422, new OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
                  MapsforgeActivity.this.disableSnapToLocation(true);
                  double var3 = Double.parseDouble(((EditText)var5.findViewById(2131492909)).getText().toString());
                  double var5x = Double.parseDouble(((EditText)var5.findViewById(2131492910)).getText().toString());
                  double var7 = Double.parseDouble(((EditText)var5.findViewById(2131492913)).getText().toString());
                  var5x /= 60.0D;
                  double var9 = var7 / 3600.0D;
                  var7 = Double.parseDouble(((EditText)var5.findViewById(2131492911)).getText().toString());
                  double var11 = Double.parseDouble(((EditText)var5.findViewById(2131492912)).getText().toString());
                  double var13 = Double.parseDouble(((EditText)var5.findViewById(2131492914)).getText().toString());
                  MapPosition var15 = new MapPosition(new GeoPoint(var5x + var3 + var9, var11 / 60.0D + var7 + var13 / 3600.0D), (byte)((SeekBar)var5.findViewById(2131492907)).getProgress());
                  MapsforgeActivity.this.mapView.getMapViewPosition().setMapPosition(var15);
               }
            });
            break;
         default:
            var5 = var4.inflate(2130903045, (ViewGroup)null);
            var3.setView(var5);
            var3.setPositiveButton(2131165422, new OnClickListener() {
               public void onClick(DialogInterface var1, int var2) {
                  MapsforgeActivity.this.disableSnapToLocation(true);
                  MapPosition var3 = new MapPosition(new GeoPoint(Double.parseDouble(((EditText)var5.findViewById(2131492905)).getText().toString()), Double.parseDouble(((EditText)var5.findViewById(2131492906)).getText().toString())), (byte)((SeekBar)var5.findViewById(2131492907)).getProgress());
                  MapsforgeActivity.this.mapView.getMapViewPosition().setMapPosition(var3);
               }
            });
         }

         var3.setNegativeButton(2131165190, (OnClickListener)null);
         var2 = var3.create();
      } else if (var1 == 2) {
         var3.setIcon(17301569);
         var3.setTitle(2131165200);
         var3.setMessage(2131165457);
         var3.setPositiveButton(2131165230, (OnClickListener)null);
         var2 = var3.create();
      } else if (var1 == 1) {
         var3.setIcon(17301569);
         var3.setTitle(2131165438);
         var3.setView(LayoutInflater.from(this).inflate(2130903048, (ViewGroup)null));
         var3.setPositiveButton(2131165230, (OnClickListener)null);
         var2 = var3.create();
      }

      return var2;
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      this.getMenuInflater().inflate(2131558401, var1);
      this.menu = var1;
      SubMenu var7 = var1.findItem(2131493009).getSubMenu();
      String[] var2 = this.getResources().getStringArray(2131230723);
      String[] var3 = this.getResources().getStringArray(2131230720);

      for(int var4 = 0; var4 < var2.length; ++var4) {
         final MapGeneratorInternal var5 = MapGeneratorInternal.valueOf(var2[var4]);
         MenuItem var6 = var7.add(2131493010, 0, 0, var3[var4]);
         var6.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem var1) {
               if (var5 == MapGeneratorInternal.DATABASE_RENDERER && MapsforgeActivity.this.mapView.getMapFile() == null) {
                  MapsforgeActivity.this.startMapFilePicker();
               } else {
                  MapsforgeActivity.this.setMapGenerator(var5);
                  var1.setChecked(true);
               }

               return true;
            }
         });
         if (this.mapGeneratorInternal != null && this.mapGeneratorInternal.name().equals(var5.name())) {
            var6.setChecked(true);
         }
      }

      var7.setGroupCheckable(2131493010, true, true);
      return true;
   }

   protected void onDestroy() {
      super.onDestroy();
      this.screenshotCapturer.interrupt();
      Editor var1 = PreferenceManager.getDefaultSharedPreferences(this).edit();
      var1.putBoolean("showMyLocation", this.myLocationOverlay.isMyLocationEnabled());
      var1.putBoolean("centerAtFirstFix", this.myLocationOverlay.isCenterAtNextFix());
      var1.putBoolean("snapToLocation", this.myLocationOverlay.isSnapToLocationEnabled());
      var1.putBoolean("showPins", this.showPins);
      var1.putBoolean("showLabels", this.showLabels);
      var1.putString("mapGenerator", this.mapGeneratorInternal.name());
      var1.commit();
      this.disableShowMyLocation();
   }

   protected void onNewIntent(Intent var1) {
      this.setIntent(var1);
      this.refreshItems();
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      boolean var2 = true;
      boolean var3 = var2;
      switch(var1.getItemId()) {
      case 2131492991:
      case 2131492994:
      case 2131493001:
      case 2131493005:
      case 2131493009:
         break;
      case 2131492992:
         var3 = var2;
         if (this.mapGeneratorInternal == MapGeneratorInternal.DATABASE_RENDERER) {
            var3 = var2;
            if (this.mapView.getMapFile() != null) {
               this.showDialog(1);
               var3 = var2;
            }
         }
         break;
      case 2131492993:
         this.startActivity(new Intent(this, InfoView.class));
         var3 = var2;
         break;
      case 2131492995:
         this.enableShowMyLocation(true);
         this.onPrepareOptionsMenu(this.menu);
         var3 = var2;
         break;
      case 2131492996:
         this.disableShowMyLocation();
         this.onPrepareOptionsMenu(this.menu);
         var3 = var2;
         break;
      case 2131492997:
         this.gotoLastKnownPosition();
         var3 = var2;
         break;
      case 2131492998:
         this.showDialog(0);
         var3 = var2;
         break;
      case 2131492999:
         GeoPoint var5 = this.navigationOverlay.getTarget();
         var3 = var2;
         if (var5 != null) {
            this.mapView.getMapViewPosition().setCenter(var5);
            var3 = var2;
         }
         break;
      case 2131493000:
         var3 = var2;
         if (this.mapGeneratorInternal == MapGeneratorInternal.DATABASE_RENDERER) {
            this.disableSnapToLocation(true);
            MapFileInfo var4 = this.mapView.getMapDatabase().getMapFileInfo();
            this.mapView.getMapViewPosition().setCenter(var4.boundingBox.getCenterPoint());
            var3 = var2;
         }
         break;
      case 2131493002:
         this.screenshotCapturer.captureScreenshot(CompressFormat.JPEG);
         var3 = var2;
         break;
      case 2131493003:
         this.screenshotCapturer.captureScreenshot(CompressFormat.PNG);
         var3 = var2;
         break;
      case 2131493004:
         this.startActivity(new Intent(this, EditPreferences.class));
         var3 = var2;
         break;
      case 2131493006:
         this.mapView.setRenderTheme(InternalRenderTheme.OSMARENDER);
         var3 = var2;
         break;
      case 2131493007:
         this.startRenderThemePicker();
         var3 = var2;
         break;
      case 2131493008:
         this.startMapFilePicker();
         var3 = var2;
         break;
      case 2131493010:
      default:
         var3 = false;
         break;
      case 2131493011:
         this.refresh();
         var3 = var2;
      }

      return var3;
   }

   protected void onPause() {
      super.onPause();
      if (this.wakeLock.isHeld()) {
         this.wakeLock.release();
      }

      if (PreferenceValues.getCurrentActivity() == this) {
         PreferenceValues.setCurrentActivity((Activity)null);
      }

      MainApplication.onActivityPause();
   }

   @Deprecated
   protected void onPrepareDialog(int var1, Dialog var2) {
      if (var1 == 0) {
         MapViewPosition var3 = this.mapView.getMapViewPosition();
         GeoPoint var4 = var3.getCenter();
         double var5 = var4.latitude;
         double var7 = var4.longitude;
         double var12;
         switch(Preferences.FORMAT_COO_LATLON) {
         case 1:
            var1 = (int)var5;
            var12 = (double)var1;
            ((EditText)var2.findViewById(2131492909)).setText(Integer.toString(var1));
            ((EditText)var2.findViewById(2131492910)).setText(Double.toString((var5 - var12) * 60.0D));
            var1 = (int)var7;
            var5 = (double)var1;
            ((EditText)var2.findViewById(2131492911)).setText(Integer.toString(var1));
            ((EditText)var2.findViewById(2131492912)).setText(Double.toString((var7 - var5) * 60.0D));
            break;
         case 2:
            int var9 = (int)var5;
            var1 = (int)((var5 - (double)var9) * 60.0D);
            double var10 = (double)var9;
            var12 = (double)(var1 / 60);
            ((EditText)var2.findViewById(2131492909)).setText(Integer.toString(var9));
            ((EditText)var2.findViewById(2131492910)).setText(Integer.toString(var1));
            ((EditText)var2.findViewById(2131492913)).setText(Double.toString((var5 - var10 - var12) * 3600.0D));
            var1 = (int)var7;
            var9 = (int)((var7 - (double)var1) * 60.0D);
            var5 = (double)var1;
            var12 = (double)(var9 / 60);
            ((EditText)var2.findViewById(2131492911)).setText(Integer.toString(var1));
            ((EditText)var2.findViewById(2131492912)).setText(Integer.toString(var9));
            ((EditText)var2.findViewById(2131492914)).setText(Double.toString((var7 - var5 - var12) * 3600.0D));
            break;
         default:
            ((EditText)var2.findViewById(2131492905)).setText(Double.toString(var5));
            ((EditText)var2.findViewById(2131492906)).setText(Double.toString(var7));
         }

         SeekBar var17 = (SeekBar)var2.findViewById(2131492907);
         var17.setMax(this.mapView.getDatabaseRenderer().getZoomLevelMax());
         var17.setProgress(var3.getZoomLevel());
         TextView var15 = (TextView)var2.findViewById(2131492908);
         var15.setText(String.valueOf(var17.getProgress()));
         var17.setOnSeekBarChangeListener(new SeekBarChangeListener(var15));
      } else if (var1 == 1 && this.mapGeneratorInternal == MapGeneratorInternal.DATABASE_RENDERER) {
         MapFileInfo var16 = this.mapView.getMapDatabase().getMapFileInfo();
         ((TextView)var2.findViewById(2131492915)).setText(this.mapView.getMapFile().getAbsolutePath());
         ((TextView)var2.findViewById(2131492916)).setText(FileUtils.formatFileSize(var16.fileSize, this.getResources()));
         ((TextView)var2.findViewById(2131492917)).setText(String.valueOf(var16.fileVersion));
         TextView var18 = (TextView)var2.findViewById(2131492918);
         if (var16.debugFile) {
            var18.setText(2131165429);
         } else {
            var18.setText(2131165428);
         }

         TextView var14 = (TextView)var2.findViewById(2131492919);
         Date var19 = new Date(var16.mapDate);
         var14.setText(DateFormat.getDateTimeInstance().format(var19));
         var14 = (TextView)var2.findViewById(2131492920);
         BoundingBox var20 = var16.boundingBox;
         var14.setText(var20.minLatitude + ", " + var20.minLongitude + " – \n" + var20.maxLatitude + ", " + var20.maxLongitude);
         var18 = (TextView)var2.findViewById(2131492921);
         GeoPoint var21 = var16.startPosition;
         if (var21 == null) {
            var18.setText((CharSequence)null);
         } else {
            var18.setText(var21.latitude + ", " + var21.longitude);
         }

         var18 = (TextView)var2.findViewById(2131492922);
         Byte var22 = var16.startZoomLevel;
         if (var22 == null) {
            var18.setText((CharSequence)null);
         } else {
            var18.setText(var22.toString());
         }

         ((TextView)var2.findViewById(2131492923)).setText(var16.languagePreference);
         ((TextView)var2.findViewById(2131492924)).setText(var16.comment);
         ((TextView)var2.findViewById(2131492925)).setText(var16.createdBy);
      } else {
         super.onPrepareDialog(var1, var2);
      }

   }

   public boolean onPrepareOptionsMenu(Menu var1) {
      if (this.myLocationOverlay.isMyLocationEnabled()) {
         var1.findItem(2131492995).setVisible(false);
         var1.findItem(2131492995).setEnabled(false);
         var1.findItem(2131492996).setVisible(true);
         var1.findItem(2131492996).setEnabled(true);
      } else {
         var1.findItem(2131492995).setVisible(true);
         var1.findItem(2131492995).setEnabled(true);
         var1.findItem(2131492996).setVisible(false);
         var1.findItem(2131492996).setEnabled(false);
      }

      if (this.mapGeneratorInternal == MapGeneratorInternal.DATABASE_RENDERER && this.mapView.getMapFile() != null) {
         var1.findItem(2131492992).setEnabled(true);
         var1.findItem(2131493000).setEnabled(true);
         var1.findItem(2131493005).setEnabled(true);
         var1.findItem(2131493008).setEnabled(true);
      } else {
         var1.findItem(2131492992).setEnabled(false);
         var1.findItem(2131493000).setEnabled(false);
         var1.findItem(2131493005).setEnabled(false);
         var1.findItem(2131493008).setEnabled(false);
      }

      if (this.navigationOverlay.getTarget() == null) {
         var1.findItem(2131492999).setEnabled(false);
      }

      return true;
   }

   protected void onResume() {
      super.onResume();
      SharedPreferences var1 = PreferenceManager.getDefaultSharedPreferences(this);
      MapScaleBar var2 = this.mapView.getMapScaleBar();
      var2.setShowMapScaleBar(var1.getBoolean("showScaleBar", false));
      var2.setImperialUnits(var1.getString("scaleBarUnit", this.getString(2131165678)).equals("imperial"));

      try {
         String var8 = this.getString(2131165679);
         this.mapView.setTextScale(Float.parseFloat(var1.getString("textScale", var8)));
      } catch (NumberFormatException var6) {
         this.mapView.setTextScale(1.0F);
      }

      if (var1.getBoolean("fullscreen", false)) {
         this.getWindow().addFlags(1024);
         this.getWindow().clearFlags(2048);
      } else {
         this.getWindow().clearFlags(1024);
         this.getWindow().addFlags(2048);
      }

      if (var1.getBoolean("wakeLock", false) && !this.wakeLock.isHeld()) {
         this.wakeLock.acquire();
      }

      boolean var3 = var1.getBoolean("cachePersistence", false);
      int var4 = Math.min(var1.getInt("cacheSize", 250), 500);
      TileCache var9 = this.mapView.getFileSystemTileCache();
      var9.setPersistent(var3);
      var9.setCapacity(var4);
      float var5 = (float)Math.min(var1.getInt("moveSpeed", 10), 30) / 10.0F;
      this.mapView.getMapMover().setMoveSpeedFactor(var5);
      this.mapView.getFpsCounter().setFpsCounter(var1.getBoolean("showFpsCounter", false));
      var3 = var1.getBoolean("drawTileFrames", false);
      DebugSettings var7 = new DebugSettings(var1.getBoolean("drawTileCoordinates", false), var3, var1.getBoolean("highlightWaterTiles", false));
      this.mapView.setDebugSettings(var7);
      PreferenceValues.setCurrentActivity(this);
   }

   protected void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      var1.putBoolean("showMyLocation", this.myLocationOverlay.isMyLocationEnabled());
      var1.putBoolean("centerAtFirstFix", this.myLocationOverlay.isCenterAtNextFix());
      var1.putBoolean("snapToLocation", this.myLocationOverlay.isSnapToLocationEnabled());
      var1.putBoolean("showPins", this.showPins);
      var1.putBoolean("showLabels", this.showLabels);
      var1.putBoolean("allowStartCartridge", this.allowStartCartridge);
   }

   public boolean onTrackballEvent(MotionEvent var1) {
      return this.mapView.onTrackballEvent(var1);
   }

   public void refresh() {
      this.runOnUiThread(new Runnable() {
         public void run() {
            VectorMapDataProvider var1 = VectorMapDataProvider.getInstance();
            var1.addAll();
            MapsforgeActivity.this.showMapPack(var1.getItems());
            MapsforgeActivity.this.mapView.getOverlayController().redrawOverlays();
         }
      });
   }

   void showToastOnUiThread(final String var1) {
      if (AndroidUtils.currentThreadIsUiThread()) {
         Toast.makeText(this, var1, 0).show();
      } else {
         this.runOnUiThread(new Runnable() {
            public void run() {
               Toast.makeText(MapsforgeActivity.this, var1, 0).show();
            }
         });
      }

   }
}

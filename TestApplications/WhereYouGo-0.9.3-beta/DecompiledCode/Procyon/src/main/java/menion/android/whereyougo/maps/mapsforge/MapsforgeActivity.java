// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge;

import android.widget.Toast;
import org.mapsforge.android.AndroidUtils;
import android.view.MotionEvent;
import org.mapsforge.android.maps.mapgenerator.TileCache;
import org.mapsforge.android.maps.DebugSettings;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.map.reader.header.MapFileInfo;
import org.mapsforge.android.maps.MapViewPosition;
import java.util.Date;
import java.text.DateFormat;
import android.widget.SeekBar$OnSeekBarChangeListener;
import menion.android.whereyougo.MainApplication;
import android.app.Activity;
import menion.android.whereyougo.preferences.PreferenceValues;
import android.graphics.Bitmap$CompressFormat;
import menion.android.whereyougo.maps.mapsforge.preferences.EditPreferences;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import android.content.SharedPreferences$Editor;
import android.view.SubMenu;
import android.view.MenuItem;
import android.view.MenuItem$OnMenuItemClickListener;
import android.widget.SeekBar;
import android.widget.EditText;
import menion.android.whereyougo.preferences.Preferences;
import android.view.LayoutInflater;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.PowerManager;
import org.mapsforge.android.maps.MapView;
import menion.android.whereyougo.maps.mapsforge.overlay.SensorMyLocationOverlay;
import menion.android.whereyougo.maps.mapsforge.overlay.RotationMarker;
import android.view.View$OnClickListener;
import android.preference.PreferenceManager;
import java.io.FileNotFoundException;
import java.io.File;
import menion.android.whereyougo.maps.mapsforge.overlay.LabelMarker;
import menion.android.whereyougo.maps.mapsforge.filefilter.ValidRenderTheme;
import menion.android.whereyougo.maps.mapsforge.filefilter.ValidFileFilter;
import menion.android.whereyougo.maps.mapsforge.filefilter.ValidMapFile;
import menion.android.whereyougo.maps.mapsforge.filepicker.FilePicker;
import android.content.res.Resources;
import org.mapsforge.android.maps.overlay.OverlayItem;
import android.graphics.drawable.Drawable;
import org.mapsforge.android.maps.overlay.Marker;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import menion.android.whereyougo.maps.container.MapPoint;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import android.text.Html$TagHandler;
import android.text.Html$ImageGetter;
import org.mapsforge.android.maps.mapgenerator.tiledownloader.TileDownloader;
import menion.android.whereyougo.maps.mapsforge.mapgenerator.MapGeneratorFactory;
import android.os.Bundle;
import org.mapsforge.core.model.MapPosition;
import menion.android.whereyougo.maps.utils.VectorMapDataProvider;
import java.util.Iterator;
import android.location.Location;
import android.location.LocationManager;
import java.util.Collection;
import org.mapsforge.android.maps.overlay.PolygonalChain;
import org.mapsforge.android.maps.overlay.Polyline;
import java.util.List;
import android.graphics.Paint$Style;
import android.graphics.Paint;
import org.mapsforge.android.maps.overlay.Circle;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.android.maps.MapScaleBar;
import menion.android.whereyougo.maps.container.MapPointPack;
import java.util.ArrayList;
import android.content.Intent;
import menion.android.whereyougo.gui.activity.MainActivity;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.text.Html;
import menion.android.whereyougo.utils.UtilsFormat;
import android.view.ViewGroup;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import menion.android.whereyougo.maps.mapsforge.overlay.PointOverlay;
import menion.android.whereyougo.maps.mapsforge.filefilter.FilterByFileExtension;
import android.os.PowerManager$WakeLock;
import android.widget.ToggleButton;
import menion.android.whereyougo.maps.mapsforge.overlay.NavigationOverlay;
import menion.android.whereyougo.maps.mapsforge.overlay.MyLocationOverlay;
import android.view.Menu;
import menion.android.whereyougo.maps.mapsforge.mapgenerator.MapGeneratorInternal;
import menion.android.whereyougo.maps.mapsforge.overlay.PointListOverlay;
import java.io.FileFilter;
import menion.android.whereyougo.gui.IRefreshable;
import org.mapsforge.android.maps.MapActivity;

public class MapsforgeActivity extends MapActivity implements IRefreshable
{
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
    private static final FileFilter FILE_FILTER_EXTENSION_MAP;
    private static final FileFilter FILE_FILTER_EXTENSION_XML;
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
    private final Object lock;
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
    private PowerManager$WakeLock wakeLock;
    
    static {
        FILE_FILTER_EXTENSION_MAP = new FilterByFileExtension(".map");
        FILE_FILTER_EXTENSION_XML = new FilterByFileExtension(".xml");
    }
    
    public MapsforgeActivity() {
        this.lock = new Object();
        this.mapGeneratorInternal = MapGeneratorInternal.BLANK;
        this.showPins = true;
        this.showLabels = true;
        this.allowStartCartridge = false;
        this.tapListener = new TapEventListener() {
            @Override
            public void onTap(final PointOverlay pointOverlay) {
                if (pointOverlay.getPoint() != null) {
                    final TextView view = (TextView)View.inflate((Context)MapsforgeActivity.this, 2130903056, (ViewGroup)null);
                    view.setText((CharSequence)(UtilsFormat.formatGeoPoint(pointOverlay.getGeoPoint()) + "\n\n" + Html.fromHtml(pointOverlay.getDescription())));
                    final AlertDialog$Builder setNegativeButton = new AlertDialog$Builder((Context)MapsforgeActivity.this).setTitle((CharSequence)pointOverlay.getLabel()).setView((View)view).setNegativeButton(2131165192, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            dialogInterface.dismiss();
                        }
                    });
                    final String data = pointOverlay.getPoint().getData();
                    if (MapsforgeActivity.this.allowStartCartridge && data != null) {
                        setNegativeButton.setPositiveButton(2131165309, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                            public void onClick(final DialogInterface dialogInterface, final int n) {
                                final Intent intent = new Intent((Context)MapsforgeActivity.this, (Class)MainActivity.class);
                                intent.putExtra("cguid", data);
                                intent.addFlags(335544320);
                                MapsforgeActivity.this.startActivity(intent);
                                dialogInterface.dismiss();
                                MapsforgeActivity.this.finish();
                            }
                        });
                    }
                    setNegativeButton.show();
                }
            }
        };
    }
    
    private void configureMapView() {
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.setClickable(true);
        this.mapView.setFocusable(true);
        final MapScaleBar mapScaleBar = this.mapView.getMapScaleBar();
        mapScaleBar.setText(MapScaleBar.TextField.KILOMETER, this.getString(2131165489));
        mapScaleBar.setText(MapScaleBar.TextField.METER, this.getString(2131165490));
    }
    
    private Circle createCircle(final GeoPoint geoPoint) {
        final Paint paint = new Paint(1);
        paint.setStyle(Paint$Style.FILL);
        paint.setColor(-16776961);
        final Paint paint2 = new Paint(1);
        paint2.setStyle(Paint$Style.STROKE);
        paint2.setColor(-12303292);
        paint2.setStrokeWidth(3.0f);
        return new Circle(geoPoint, 0.0f, paint, paint2);
    }
    
    private static Polyline createPolyline(final List<GeoPoint> list) {
        final PolygonalChain polygonalChain = new PolygonalChain(list);
        final Paint paint = new Paint(1);
        paint.setStyle(Paint$Style.STROKE);
        paint.setColor(-65281);
        paint.setStrokeWidth(4.0f);
        return new Polyline(polygonalChain, paint);
    }
    
    private void disableShowMyLocation() {
        if (this.myLocationOverlay.isMyLocationEnabled()) {
            this.myLocationOverlay.disableMyLocation();
            this.disableSnapToLocation(false);
            this.snapToLocationView.setVisibility(8);
        }
    }
    
    private void disableSnapToLocation(final boolean b) {
        if (this.myLocationOverlay.isSnapToLocationEnabled()) {
            this.myLocationOverlay.setSnapToLocationEnabled(false);
            this.snapToLocationView.setChecked(false);
            this.mapView.setClickable(true);
            if (b) {
                this.showToastOnUiThread(this.getString(2131165487));
            }
        }
    }
    
    private void enableShowMyLocation(final boolean b) {
        if (!this.myLocationOverlay.isMyLocationEnabled()) {
            if (!this.myLocationOverlay.enableMyLocation(b)) {
                this.showDialog(2);
            }
            else {
                this.mapView.getOverlays().add(this.navigationOverlay);
                this.mapView.getOverlays().add(this.myLocationOverlay);
                this.snapToLocationView.setVisibility(0);
            }
        }
    }
    
    private void enableSnapToLocation(final boolean b) {
        if (!this.myLocationOverlay.isSnapToLocationEnabled()) {
            this.myLocationOverlay.setSnapToLocationEnabled(true);
            this.snapToLocationView.setChecked(true);
            this.mapView.setClickable(false);
            if (b) {
                this.showToastOnUiThread(this.getString(2131165488));
            }
        }
    }
    
    private void gotoLastKnownPosition() {
        Location location = null;
        final LocationManager locationManager = (LocationManager)this.getSystemService("location");
        final Iterator iterator = locationManager.getProviders(true).iterator();
        while (iterator.hasNext()) {
            final Location lastKnownLocation = locationManager.getLastKnownLocation((String)iterator.next());
            if (lastKnownLocation != null && (location == null || location.getAccuracy() > lastKnownLocation.getAccuracy())) {
                location = lastKnownLocation;
            }
        }
        if (location != null) {
            this.mapView.getMapViewPosition().setCenter(new GeoPoint(location.getLatitude(), location.getLongitude()));
        }
        else {
            this.showToastOnUiThread(this.getString(2131165414));
        }
    }
    
    private void invertSnapToLocation() {
        if (this.myLocationOverlay.isSnapToLocationEnabled()) {
            this.disableSnapToLocation(true);
        }
        else {
            this.enableSnapToLocation(true);
        }
    }
    
    private void refreshItems() {
        boolean allowStartCartridge = true;
        final Bundle extras = this.getIntent().getExtras();
        boolean b;
        if (extras != null && extras.getBoolean("center", false)) {
            b = true;
        }
        else {
            b = false;
        }
        boolean b2;
        if (extras != null && extras.getBoolean("navigate", false)) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        if (extras == null || !extras.getBoolean("allowStartCartridge", false)) {
            allowStartCartridge = false;
        }
        this.allowStartCartridge = allowStartCartridge;
        if (extras != null && extras.containsKey("items")) {
            this.showMapPack(extras.getParcelableArrayList("items"));
        }
        else {
            this.showMapPack(VectorMapDataProvider.getInstance().getItems());
        }
        if (b && this.itemsLatitude != 0.0 && this.itemsLongitude != 0.0) {
            GeoPoint target;
            if (b2 && this.navigationOverlay.getTarget() != null) {
                target = this.navigationOverlay.getTarget();
            }
            else {
                target = new GeoPoint(this.itemsLatitude, this.itemsLongitude);
            }
            this.mapView.getMapViewPosition().setMapPosition(new MapPosition(target, this.mapView.getMapViewPosition().getZoomLevel()));
        }
    }
    
    private void setMapGenerator(final MapGeneratorInternal mapGeneratorInternal) {
        if (this.mapGeneratorInternal != mapGeneratorInternal) {
            this.mapGeneratorInternal = mapGeneratorInternal;
            final MapGenerator mapGenerator = MapGeneratorFactory.createMapGenerator(mapGeneratorInternal);
            this.mapView.setMapGenerator(mapGenerator, mapGeneratorInternal.ordinal());
            final TextView textView = (TextView)this.findViewById(2131492885);
            if (mapGenerator instanceof TileDownloader) {
                final String attribution = ((TileDownloader)mapGenerator).getAttribution();
                Object fromHtml;
                if (attribution == null) {
                    fromHtml = "";
                }
                else {
                    fromHtml = Html.fromHtml(attribution, (Html$ImageGetter)null, (Html$TagHandler)null);
                }
                textView.setText((CharSequence)fromHtml);
            }
            else {
                textView.setText((CharSequence)"");
            }
        }
    }
    
    private void showMapPack(final ArrayList<MapPointPack> list) {
        int n = 0;
        List<OverlayItem> overlayItems;
        ArrayList<Polyline> list2;
        ArrayList<PointOverlay> list3 = null;
        while (true) {
        Label_0069:
            while (true) {
                Object target = null;
                Label_0191: {
                    synchronized (this.lock) {
                        this.navigationOverlay.setTarget(null);
                        this.itemsLongitude = 0.0;
                        this.itemsLatitude = 0.0;
                        n = 0;
                        this.listOverlay.clear();
                        overlayItems = this.listOverlay.getOverlayItems();
                        list2 = new ArrayList<Polyline>();
                        list3 = new ArrayList<PointOverlay>();
                        final Iterator<MapPointPack> iterator = list.iterator();
                        if (!iterator.hasNext()) {
                            break;
                        }
                        target = iterator.next();
                        if (!((MapPointPack)target).isPolygon()) {
                            break Label_0191;
                        }
                        final ArrayList<MapPointPack> list4 = new ArrayList<MapPointPack>();
                        for (final MapPoint mapPoint : ((MapPointPack)target).getPoints()) {
                            target = new GeoPoint(mapPoint.getLatitude(), mapPoint.getLongitude());
                            list4.add((MapPointPack)target);
                        }
                    }
                    final List<GeoPoint> list5;
                    list2.add(createPolyline(list5));
                    continue Label_0069;
                }
                Object drawable;
                if (((MapPointPack)target).getIcon() == null) {
                    final Resources resources = this.getResources();
                    int resource;
                    if (((MapPointPack)target).getResource() != 0) {
                        resource = ((MapPointPack)target).getResource();
                    }
                    else {
                        resource = 2130837560;
                    }
                    drawable = resources.getDrawable(resource);
                }
                else {
                    final Bitmap icon = ((MapPointPack)target).getIcon();
                    Bitmap bitmap;
                    if (icon.getWidth() > 32 && icon.getWidth() >= icon.getHeight()) {
                        bitmap = Bitmap.createScaledBitmap(icon, 32, icon.getHeight() * 32 / icon.getWidth(), false);
                    }
                    else {
                        bitmap = icon;
                        if (icon.getHeight() > 32) {
                            bitmap = Bitmap.createScaledBitmap(icon, icon.getWidth() * 32 / icon.getHeight(), 32, false);
                        }
                    }
                    drawable = new BitmapDrawable(this.getResources(), bitmap);
                }
                final Drawable boundCenterBottom = Marker.boundCenterBottom((Drawable)drawable);
                final Iterator<MapPoint> iterator3 = ((MapPointPack)target).getPoints().iterator();
                int n2 = n;
                while (true) {
                    n = n2;
                    if (!iterator3.hasNext()) {
                        continue Label_0069;
                    }
                    final MapPoint mapPoint2 = iterator3.next();
                    target = new GeoPoint(mapPoint2.getLatitude(), mapPoint2.getLongitude());
                    final PointOverlay pointOverlay = new PointOverlay((GeoPoint)target, boundCenterBottom, mapPoint2);
                    pointOverlay.setMarkerVisible(this.showPins);
                    pointOverlay.setLabelVisible(this.showLabels);
                    list3.add(pointOverlay);
                    if (mapPoint2.isTarget()) {
                        this.navigationOverlay.setTarget((GeoPoint)target);
                    }
                    this.itemsLatitude += mapPoint2.getLatitude();
                    this.itemsLongitude += mapPoint2.getLongitude();
                    ++n2;
                }
                break;
            }
        }
        overlayItems.addAll(list2);
        overlayItems.addAll(list3);
        if (n > 0) {
            this.itemsLatitude /= n;
            this.itemsLongitude /= n;
        }
    }
    // monitorexit(o)
    
    private void startMapFilePicker() {
        FilePicker.setFileDisplayFilter(MapsforgeActivity.FILE_FILTER_EXTENSION_MAP);
        FilePicker.setFileSelectFilter(new ValidMapFile());
        this.startActivityForResult(new Intent((Context)this, (Class)FilePicker.class), 0);
    }
    
    private void startRenderThemePicker() {
        FilePicker.setFileDisplayFilter(MapsforgeActivity.FILE_FILTER_EXTENSION_XML);
        FilePicker.setFileSelectFilter(new ValidRenderTheme());
        this.startActivityForResult(new Intent((Context)this, (Class)FilePicker.class), 1);
    }
    
    private void visibilityChanged() {
        synchronized (this.lock) {
            final List<OverlayItem> overlayItems = this.listOverlay.getOverlayItems();
            for (int i = 0; i < overlayItems.size(); ++i) {
                final OverlayItem overlayItem = overlayItems.get(i);
                if (overlayItem instanceof LabelMarker) {
                    final LabelMarker labelMarker = (LabelMarker)overlayItem;
                    labelMarker.setMarkerVisible(this.showPins);
                    labelMarker.setLabelVisible(this.showLabels);
                }
            }
            // monitorexit(this.lock)
            this.mapView.getOverlayController().redrawOverlays();
        }
    }
    
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n == 0) {
            if (n2 == -1) {
                this.disableSnapToLocation(true);
                if (intent != null && intent.getStringExtra("selectedFile") != null) {
                    this.mapView.setMapFile(new File(intent.getStringExtra("selectedFile")));
                    this.setMapGenerator(MapGeneratorInternal.DATABASE_RENDERER);
                }
            }
            else if (n2 == 0 && this.mapView.getMapFile() == null) {}
        }
        else if (n == 1 && n2 == -1 && intent != null && intent.getStringExtra("selectedFile") != null) {
            try {
                this.mapView.setRenderTheme(new File(intent.getStringExtra("selectedFile")));
            }
            catch (FileNotFoundException ex) {
                this.showToastOnUiThread(ex.getLocalizedMessage());
            }
        }
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
        (this.screenshotCapturer = new ScreenshotCapturer(this)).start();
        this.setContentView(2130903041);
        this.mapView = (MyMapView)this.findViewById(2131492881);
        this.configureMapView();
        (this.snapToLocationView = (ToggleButton)this.findViewById(2131492882)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                MapsforgeActivity.this.invertSnapToLocation();
            }
        });
        this.myLocationOverlay = new SensorMyLocationOverlay((Context)this, this.mapView, new RotationMarker(null, Marker.boundCenter(this.getResources().getDrawable(2130837562))));
        this.navigationOverlay = new NavigationOverlay(this.myLocationOverlay);
        (this.listOverlay = new PointListOverlay()).registerOnTapEvent(this.tapListener);
        Label_0369: {
            if (bundle == null) {
                break Label_0369;
            }
            this.showPins = bundle.getBoolean("showPins", true);
            this.showLabels = bundle.getBoolean("showLabels", true);
            this.allowStartCartridge = bundle.getBoolean("allowStartCartridge", false);
        Label_0275_Outer:
            while (true) {
                this.mapView.getOverlays().add(this.listOverlay);
                this.wakeLock = ((PowerManager)this.getSystemService("power")).newWakeLock(6, "AMV");
                Label_0398: {
                    if (bundle == null) {
                        break Label_0398;
                    }
                    if (bundle.getBoolean("showMyLocation", true)) {
                        this.enableShowMyLocation(bundle.getBoolean("centerAtFirstFix", false));
                        if (bundle.getBoolean("snapToLocation", false)) {
                            this.enableSnapToLocation(false);
                        }
                    }
                Label_0364_Outer:
                    while (true) {
                        final ToggleButton toggleButton = (ToggleButton)this.findViewById(2131492883);
                        toggleButton.setChecked(this.showPins);
                        toggleButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                            public void onClick(final View view) {
                                MapsforgeActivity.this.showPins = ((ToggleButton)view).isChecked();
                                MapsforgeActivity.this.visibilityChanged();
                            }
                        });
                        final ToggleButton toggleButton2 = (ToggleButton)this.findViewById(2131492884);
                        toggleButton2.setChecked(this.showLabels);
                        toggleButton2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                            public void onClick(final View view) {
                                MapsforgeActivity.this.showLabels = ((ToggleButton)view).isChecked();
                                MapsforgeActivity.this.visibilityChanged();
                            }
                        });
                        while (true) {
                            if (!defaultSharedPreferences.contains("mapGenerator")) {
                                break Label_0364;
                            }
                            try {
                                this.setMapGenerator(MapGeneratorInternal.valueOf(defaultSharedPreferences.getString("mapGenerator", (String)null)));
                                this.refreshItems();
                                return;
                                while (true) {
                                    this.enableSnapToLocation(false);
                                    continue Label_0364_Outer;
                                    this.showPins = defaultSharedPreferences.getBoolean("showPins", true);
                                    this.showLabels = defaultSharedPreferences.getBoolean("showLabels", true);
                                    continue Label_0275_Outer;
                                    this.enableShowMyLocation(defaultSharedPreferences.getBoolean("centerAtFirstFix", false));
                                    continue;
                                }
                            }
                            // iftrue(Label_0275:, !defaultSharedPreferences.getBoolean("showMyLocation", true))
                            // iftrue(Label_0275:, !defaultSharedPreferences.getBoolean("snapToLocation", false))
                            catch (Exception ex) {
                                continue;
                            }
                            break;
                        }
                        break;
                    }
                }
                break;
            }
        }
    }
    
    @Deprecated
    protected Dialog onCreateDialog(final int n) {
        Object o = null;
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        if (n == 0) {
            alertDialog$Builder.setIcon(17301575);
            alertDialog$Builder.setTitle(2131165442);
            final LayoutInflater from = LayoutInflater.from((Context)this);
            switch (Preferences.FORMAT_COO_LATLON) {
                default: {
                    final View inflate = from.inflate(2130903045, (ViewGroup)null);
                    alertDialog$Builder.setView(inflate);
                    alertDialog$Builder.setPositiveButton(2131165422, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            MapsforgeActivity.this.disableSnapToLocation(true);
                            MapsforgeActivity.this.mapView.getMapViewPosition().setMapPosition(new MapPosition(new GeoPoint(Double.parseDouble(((EditText)inflate.findViewById(2131492905)).getText().toString()), Double.parseDouble(((EditText)inflate.findViewById(2131492906)).getText().toString())), (byte)((SeekBar)inflate.findViewById(2131492907)).getProgress()));
                        }
                    });
                    break;
                }
                case 2: {
                    final View inflate2 = from.inflate(2130903047, (ViewGroup)null);
                    alertDialog$Builder.setView(inflate2);
                    alertDialog$Builder.setPositiveButton(2131165422, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            MapsforgeActivity.this.disableSnapToLocation(true);
                            MapsforgeActivity.this.mapView.getMapViewPosition().setMapPosition(new MapPosition(new GeoPoint(Double.parseDouble(((EditText)inflate2.findViewById(2131492910)).getText().toString()) / 60.0 + Double.parseDouble(((EditText)inflate2.findViewById(2131492909)).getText().toString()) + Double.parseDouble(((EditText)inflate2.findViewById(2131492913)).getText().toString()) / 3600.0, Double.parseDouble(((EditText)inflate2.findViewById(2131492912)).getText().toString()) / 60.0 + Double.parseDouble(((EditText)inflate2.findViewById(2131492911)).getText().toString()) + Double.parseDouble(((EditText)inflate2.findViewById(2131492914)).getText().toString()) / 3600.0), (byte)((SeekBar)inflate2.findViewById(2131492907)).getProgress()));
                        }
                    });
                    break;
                }
                case 1: {
                    final View inflate3 = from.inflate(2130903046, (ViewGroup)null);
                    alertDialog$Builder.setView(inflate3);
                    alertDialog$Builder.setPositiveButton(2131165422, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            MapsforgeActivity.this.disableSnapToLocation(true);
                            final double double1 = Double.parseDouble(((EditText)inflate3.findViewById(2131492909)).getText().toString());
                            final double n2 = Double.parseDouble(((EditText)inflate3.findViewById(2131492910)).getText().toString()) / 60.0;
                            final double double2 = Double.parseDouble(((EditText)inflate3.findViewById(2131492911)).getText().toString());
                            final double n3 = Double.parseDouble(((EditText)inflate3.findViewById(2131492912)).getText().toString()) / 60.0;
                            try {
                                MapsforgeActivity.this.mapView.getMapViewPosition().setMapPosition(new MapPosition(new GeoPoint(double1 + n2, double2 + n3), (byte)((SeekBar)inflate3.findViewById(2131492907)).getProgress()));
                            }
                            catch (IllegalArgumentException ex) {}
                        }
                    });
                    break;
                }
            }
            alertDialog$Builder.setNegativeButton(2131165190, (DialogInterface$OnClickListener)null);
            o = alertDialog$Builder.create();
        }
        else if (n == 2) {
            alertDialog$Builder.setIcon(17301569);
            alertDialog$Builder.setTitle(2131165200);
            alertDialog$Builder.setMessage(2131165457);
            alertDialog$Builder.setPositiveButton(2131165230, (DialogInterface$OnClickListener)null);
            o = alertDialog$Builder.create();
        }
        else if (n == 1) {
            alertDialog$Builder.setIcon(17301569);
            alertDialog$Builder.setTitle(2131165438);
            alertDialog$Builder.setView(LayoutInflater.from((Context)this).inflate(2130903048, (ViewGroup)null));
            alertDialog$Builder.setPositiveButton(2131165230, (DialogInterface$OnClickListener)null);
            o = alertDialog$Builder.create();
        }
        return (Dialog)o;
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.getMenuInflater().inflate(2131558401, menu);
        this.menu = menu;
        final SubMenu subMenu = menu.findItem(2131493009).getSubMenu();
        final String[] stringArray = this.getResources().getStringArray(2131230723);
        final String[] stringArray2 = this.getResources().getStringArray(2131230720);
        for (int i = 0; i < stringArray.length; ++i) {
            final MapGeneratorInternal value = MapGeneratorInternal.valueOf(stringArray[i]);
            final MenuItem add = subMenu.add(2131493010, 0, 0, (CharSequence)stringArray2[i]);
            add.setOnMenuItemClickListener((MenuItem$OnMenuItemClickListener)new MenuItem$OnMenuItemClickListener() {
                public boolean onMenuItemClick(final MenuItem menuItem) {
                    if (value == MapGeneratorInternal.DATABASE_RENDERER && MapsforgeActivity.this.mapView.getMapFile() == null) {
                        MapsforgeActivity.this.startMapFilePicker();
                    }
                    else {
                        MapsforgeActivity.this.setMapGenerator(value);
                        menuItem.setChecked(true);
                    }
                    return true;
                }
            });
            if (this.mapGeneratorInternal != null && this.mapGeneratorInternal.name().equals(value.name())) {
                add.setChecked(true);
            }
        }
        subMenu.setGroupCheckable(2131493010, true, true);
        return true;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.screenshotCapturer.interrupt();
        final SharedPreferences$Editor edit = PreferenceManager.getDefaultSharedPreferences((Context)this).edit();
        edit.putBoolean("showMyLocation", this.myLocationOverlay.isMyLocationEnabled());
        edit.putBoolean("centerAtFirstFix", this.myLocationOverlay.isCenterAtNextFix());
        edit.putBoolean("snapToLocation", this.myLocationOverlay.isSnapToLocationEnabled());
        edit.putBoolean("showPins", this.showPins);
        edit.putBoolean("showLabels", this.showLabels);
        edit.putString("mapGenerator", this.mapGeneratorInternal.name());
        edit.commit();
        this.disableShowMyLocation();
    }
    
    protected void onNewIntent(final Intent intent) {
        this.setIntent(intent);
        this.refreshItems();
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        boolean b2;
        final boolean b = b2 = true;
        switch (menuItem.getItemId()) {
            default: {
                b2 = false;
                return b2;
            }
            case 2131493008: {
                this.startMapFilePicker();
                b2 = b;
                return b2;
            }
            case 2131493007: {
                this.startRenderThemePicker();
                b2 = b;
                return b2;
            }
            case 2131493006: {
                this.mapView.setRenderTheme(InternalRenderTheme.OSMARENDER);
                b2 = b;
                return b2;
            }
            case 2131493004: {
                this.startActivity(new Intent((Context)this, (Class)EditPreferences.class));
                b2 = b;
                return b2;
            }
            case 2131493003: {
                this.screenshotCapturer.captureScreenshot(Bitmap$CompressFormat.PNG);
                b2 = b;
                return b2;
            }
            case 2131493002: {
                this.screenshotCapturer.captureScreenshot(Bitmap$CompressFormat.JPEG);
                b2 = b;
                return b2;
            }
            case 2131492998: {
                this.showDialog(0);
                b2 = b;
                return b2;
            }
            case 2131492997: {
                this.gotoLastKnownPosition();
                b2 = b;
                return b2;
            }
            case 2131492996: {
                this.disableShowMyLocation();
                this.onPrepareOptionsMenu(this.menu);
                b2 = b;
                return b2;
            }
            case 2131492995: {
                this.enableShowMyLocation(true);
                this.onPrepareOptionsMenu(this.menu);
                b2 = b;
                return b2;
            }
            case 2131492993: {
                this.startActivity(new Intent((Context)this, (Class)InfoView.class));
                b2 = b;
                return b2;
            }
            case 2131493011: {
                this.refresh();
                b2 = b;
            }
            case 2131492991:
            case 2131492994:
            case 2131493001:
            case 2131493005:
            case 2131493009: {
                return b2;
            }
            case 2131492992: {
                b2 = b;
                if (this.mapGeneratorInternal != MapGeneratorInternal.DATABASE_RENDERER) {
                    return b2;
                }
                b2 = b;
                if (this.mapView.getMapFile() != null) {
                    this.showDialog(1);
                    b2 = b;
                    return b2;
                }
                return b2;
            }
            case 2131492999: {
                final GeoPoint target = this.navigationOverlay.getTarget();
                b2 = b;
                if (target != null) {
                    this.mapView.getMapViewPosition().setCenter(target);
                    b2 = b;
                    return b2;
                }
                return b2;
            }
            case 2131493000: {
                b2 = b;
                if (this.mapGeneratorInternal == MapGeneratorInternal.DATABASE_RENDERER) {
                    this.disableSnapToLocation(true);
                    this.mapView.getMapViewPosition().setCenter(this.mapView.getMapDatabase().getMapFileInfo().boundingBox.getCenterPoint());
                    b2 = b;
                    return b2;
                }
                return b2;
            }
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        if (PreferenceValues.getCurrentActivity() == this) {
            PreferenceValues.setCurrentActivity(null);
        }
        MainApplication.onActivityPause();
    }
    
    @Deprecated
    protected void onPrepareDialog(int n, final Dialog dialog) {
        if (n == 0) {
            final MapViewPosition mapViewPosition = this.mapView.getMapViewPosition();
            final GeoPoint center = mapViewPosition.getCenter();
            final double latitude = center.latitude;
            final double longitude = center.longitude;
            switch (Preferences.FORMAT_COO_LATLON) {
                default: {
                    ((EditText)dialog.findViewById(2131492905)).setText((CharSequence)Double.toString(latitude));
                    ((EditText)dialog.findViewById(2131492906)).setText((CharSequence)Double.toString(longitude));
                    break;
                }
                case 2: {
                    final int i = (int)latitude;
                    n = (int)((latitude - i) * 60.0);
                    final double n2 = i;
                    final double n3 = n / 60;
                    ((EditText)dialog.findViewById(2131492909)).setText((CharSequence)Integer.toString(i));
                    ((EditText)dialog.findViewById(2131492910)).setText((CharSequence)Integer.toString(n));
                    ((EditText)dialog.findViewById(2131492913)).setText((CharSequence)Double.toString((latitude - n2 - n3) * 3600.0));
                    n = (int)longitude;
                    final int j = (int)((longitude - n) * 60.0);
                    final double n4 = n;
                    final double n5 = j / 60;
                    ((EditText)dialog.findViewById(2131492911)).setText((CharSequence)Integer.toString(n));
                    ((EditText)dialog.findViewById(2131492912)).setText((CharSequence)Integer.toString(j));
                    ((EditText)dialog.findViewById(2131492914)).setText((CharSequence)Double.toString((longitude - n4 - n5) * 3600.0));
                    break;
                }
                case 1: {
                    n = (int)latitude;
                    final double n6 = n;
                    ((EditText)dialog.findViewById(2131492909)).setText((CharSequence)Integer.toString(n));
                    ((EditText)dialog.findViewById(2131492910)).setText((CharSequence)Double.toString((latitude - n6) * 60.0));
                    n = (int)longitude;
                    final double n7 = n;
                    ((EditText)dialog.findViewById(2131492911)).setText((CharSequence)Integer.toString(n));
                    ((EditText)dialog.findViewById(2131492912)).setText((CharSequence)Double.toString((longitude - n7) * 60.0));
                    break;
                }
            }
            final SeekBar seekBar = (SeekBar)dialog.findViewById(2131492907);
            seekBar.setMax((int)this.mapView.getDatabaseRenderer().getZoomLevelMax());
            seekBar.setProgress((int)mapViewPosition.getZoomLevel());
            final TextView textView = (TextView)dialog.findViewById(2131492908);
            textView.setText((CharSequence)String.valueOf(seekBar.getProgress()));
            seekBar.setOnSeekBarChangeListener((SeekBar$OnSeekBarChangeListener)new SeekBarChangeListener(textView));
        }
        else if (n == 1 && this.mapGeneratorInternal == MapGeneratorInternal.DATABASE_RENDERER) {
            final MapFileInfo mapFileInfo = this.mapView.getMapDatabase().getMapFileInfo();
            ((TextView)dialog.findViewById(2131492915)).setText((CharSequence)this.mapView.getMapFile().getAbsolutePath());
            ((TextView)dialog.findViewById(2131492916)).setText((CharSequence)FileUtils.formatFileSize(mapFileInfo.fileSize, this.getResources()));
            ((TextView)dialog.findViewById(2131492917)).setText((CharSequence)String.valueOf(mapFileInfo.fileVersion));
            final TextView textView2 = (TextView)dialog.findViewById(2131492918);
            if (mapFileInfo.debugFile) {
                textView2.setText(2131165429);
            }
            else {
                textView2.setText(2131165428);
            }
            ((TextView)dialog.findViewById(2131492919)).setText((CharSequence)DateFormat.getDateTimeInstance().format(new Date(mapFileInfo.mapDate)));
            final TextView textView3 = (TextView)dialog.findViewById(2131492920);
            final BoundingBox boundingBox = mapFileInfo.boundingBox;
            textView3.setText((CharSequence)(boundingBox.minLatitude + ", " + boundingBox.minLongitude + " \u2013 \n" + boundingBox.maxLatitude + ", " + boundingBox.maxLongitude));
            final TextView textView4 = (TextView)dialog.findViewById(2131492921);
            final GeoPoint startPosition = mapFileInfo.startPosition;
            if (startPosition == null) {
                textView4.setText((CharSequence)null);
            }
            else {
                textView4.setText((CharSequence)(startPosition.latitude + ", " + startPosition.longitude));
            }
            final TextView textView5 = (TextView)dialog.findViewById(2131492922);
            final Byte startZoomLevel = mapFileInfo.startZoomLevel;
            if (startZoomLevel == null) {
                textView5.setText((CharSequence)null);
            }
            else {
                textView5.setText((CharSequence)startZoomLevel.toString());
            }
            ((TextView)dialog.findViewById(2131492923)).setText((CharSequence)mapFileInfo.languagePreference);
            ((TextView)dialog.findViewById(2131492924)).setText((CharSequence)mapFileInfo.comment);
            ((TextView)dialog.findViewById(2131492925)).setText((CharSequence)mapFileInfo.createdBy);
        }
        else {
            super.onPrepareDialog(n, dialog);
        }
    }
    
    public boolean onPrepareOptionsMenu(final Menu menu) {
        if (this.myLocationOverlay.isMyLocationEnabled()) {
            menu.findItem(2131492995).setVisible(false);
            menu.findItem(2131492995).setEnabled(false);
            menu.findItem(2131492996).setVisible(true);
            menu.findItem(2131492996).setEnabled(true);
        }
        else {
            menu.findItem(2131492995).setVisible(true);
            menu.findItem(2131492995).setEnabled(true);
            menu.findItem(2131492996).setVisible(false);
            menu.findItem(2131492996).setEnabled(false);
        }
        if (this.mapGeneratorInternal == MapGeneratorInternal.DATABASE_RENDERER && this.mapView.getMapFile() != null) {
            menu.findItem(2131492992).setEnabled(true);
            menu.findItem(2131493000).setEnabled(true);
            menu.findItem(2131493005).setEnabled(true);
            menu.findItem(2131493008).setEnabled(true);
        }
        else {
            menu.findItem(2131492992).setEnabled(false);
            menu.findItem(2131493000).setEnabled(false);
            menu.findItem(2131493005).setEnabled(false);
            menu.findItem(2131493008).setEnabled(false);
        }
        if (this.navigationOverlay.getTarget() == null) {
            menu.findItem(2131492999).setEnabled(false);
        }
        return true;
    }
    
    @Override
    protected void onResume() {
    Label_0118_Outer:
        while (true) {
            super.onResume();
            Object defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
            final MapScaleBar mapScaleBar = this.mapView.getMapScaleBar();
            mapScaleBar.setShowMapScaleBar(((SharedPreferences)defaultSharedPreferences).getBoolean("showScaleBar", false));
            mapScaleBar.setImperialUnits(((SharedPreferences)defaultSharedPreferences).getString("scaleBarUnit", this.getString(2131165678)).equals("imperial"));
            while (true) {
                while (true) {
                    try {
                        this.mapView.setTextScale(Float.parseFloat(((SharedPreferences)defaultSharedPreferences).getString("textScale", this.getString(2131165679))));
                        if (((SharedPreferences)defaultSharedPreferences).getBoolean("fullscreen", false)) {
                            this.getWindow().addFlags(1024);
                            this.getWindow().clearFlags(2048);
                            if (((SharedPreferences)defaultSharedPreferences).getBoolean("wakeLock", false) && !this.wakeLock.isHeld()) {
                                this.wakeLock.acquire();
                            }
                            final boolean boolean1 = ((SharedPreferences)defaultSharedPreferences).getBoolean("cachePersistence", false);
                            final int min = Math.min(((SharedPreferences)defaultSharedPreferences).getInt("cacheSize", 250), 500);
                            final TileCache fileSystemTileCache = this.mapView.getFileSystemTileCache();
                            fileSystemTileCache.setPersistent(boolean1);
                            fileSystemTileCache.setCapacity(min);
                            this.mapView.getMapMover().setMoveSpeedFactor(Math.min(((SharedPreferences)defaultSharedPreferences).getInt("moveSpeed", 10), 30) / 10.0f);
                            this.mapView.getFpsCounter().setFpsCounter(((SharedPreferences)defaultSharedPreferences).getBoolean("showFpsCounter", false));
                            defaultSharedPreferences = new DebugSettings(((SharedPreferences)defaultSharedPreferences).getBoolean("drawTileCoordinates", false), ((SharedPreferences)defaultSharedPreferences).getBoolean("drawTileFrames", false), ((SharedPreferences)defaultSharedPreferences).getBoolean("highlightWaterTiles", false));
                            this.mapView.setDebugSettings((DebugSettings)defaultSharedPreferences);
                            PreferenceValues.setCurrentActivity(this);
                            return;
                        }
                    }
                    catch (NumberFormatException ex) {
                        this.mapView.setTextScale(1.0f);
                        continue Label_0118_Outer;
                    }
                    break;
                }
                this.getWindow().clearFlags(1024);
                this.getWindow().addFlags(2048);
                continue;
            }
        }
    }
    
    protected void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("showMyLocation", this.myLocationOverlay.isMyLocationEnabled());
        bundle.putBoolean("centerAtFirstFix", this.myLocationOverlay.isCenterAtNextFix());
        bundle.putBoolean("snapToLocation", this.myLocationOverlay.isSnapToLocationEnabled());
        bundle.putBoolean("showPins", this.showPins);
        bundle.putBoolean("showLabels", this.showLabels);
        bundle.putBoolean("allowStartCartridge", this.allowStartCartridge);
    }
    
    public boolean onTrackballEvent(final MotionEvent motionEvent) {
        return this.mapView.onTrackballEvent(motionEvent);
    }
    
    @Override
    public void refresh() {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final VectorMapDataProvider instance = VectorMapDataProvider.getInstance();
                instance.addAll();
                MapsforgeActivity.this.showMapPack(instance.getItems());
                MapsforgeActivity.this.mapView.getOverlayController().redrawOverlays();
            }
        });
    }
    
    void showToastOnUiThread(final String s) {
        if (AndroidUtils.currentThreadIsUiThread()) {
            Toast.makeText((Context)this, (CharSequence)s, 0).show();
        }
        else {
            this.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    Toast.makeText((Context)MapsforgeActivity.this, (CharSequence)s, 0).show();
                }
            });
        }
    }
}

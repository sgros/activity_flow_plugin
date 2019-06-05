package menion.android.whereyougo.maps.mapsforge;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Paint;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
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
import menion.android.whereyougo.C0254R;
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
import org.mapsforge.android.maps.MapScaleBar.TextField;
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
    private boolean allowStartCartridge = false;
    private double itemsLatitude;
    private double itemsLongitude;
    private PointListOverlay listOverlay;
    private final Object lock = new Object();
    private MapGeneratorInternal mapGeneratorInternal = MapGeneratorInternal.BLANK;
    MyMapView mapView;
    private Menu menu;
    private MyLocationOverlay myLocationOverlay;
    private NavigationOverlay navigationOverlay;
    private ScreenshotCapturer screenshotCapturer;
    private boolean showLabels = true;
    private boolean showPins = true;
    private ToggleButton snapToLocationView;
    private final TapEventListener tapListener = new C04241();
    private WakeLock wakeLock;

    /* renamed from: menion.android.whereyougo.maps.mapsforge.MapsforgeActivity$2 */
    class C03092 implements OnClickListener {
        C03092() {
        }

        public void onClick(View view) {
            MapsforgeActivity.this.invertSnapToLocation();
        }
    }

    /* renamed from: menion.android.whereyougo.maps.mapsforge.MapsforgeActivity$3 */
    class C03103 implements OnClickListener {
        C03103() {
        }

        public void onClick(View v) {
            MapsforgeActivity.this.showPins = ((ToggleButton) v).isChecked();
            MapsforgeActivity.this.visibilityChanged();
        }
    }

    /* renamed from: menion.android.whereyougo.maps.mapsforge.MapsforgeActivity$4 */
    class C03114 implements OnClickListener {
        C03114() {
        }

        public void onClick(View v) {
            MapsforgeActivity.this.showLabels = ((ToggleButton) v).isChecked();
            MapsforgeActivity.this.visibilityChanged();
        }
    }

    /* renamed from: menion.android.whereyougo.maps.mapsforge.MapsforgeActivity$9 */
    class C03169 implements Runnable {
        C03169() {
        }

        public void run() {
            VectorMapDataProvider mdp = VectorMapDataProvider.getInstance();
            mdp.addAll();
            MapsforgeActivity.this.showMapPack(mdp.getItems());
            MapsforgeActivity.this.mapView.getOverlayController().redrawOverlays();
        }
    }

    /* renamed from: menion.android.whereyougo.maps.mapsforge.MapsforgeActivity$1 */
    class C04241 implements TapEventListener {

        /* renamed from: menion.android.whereyougo.maps.mapsforge.MapsforgeActivity$1$1 */
        class C03061 implements DialogInterface.OnClickListener {
            C03061() {
            }

            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }

        C04241() {
        }

        public void onTap(PointOverlay pointOverlay) {
            if (pointOverlay.getPoint() != null) {
                TextView textView = (TextView) View.inflate(MapsforgeActivity.this, C0254R.layout.point_detail_view, null);
                textView.setText(UtilsFormat.formatGeoPoint(pointOverlay.getGeoPoint()) + "\n\n" + Html.fromHtml(pointOverlay.getDescription()));
                Builder builder = new Builder(MapsforgeActivity.this).setTitle(pointOverlay.getLabel()).setView(textView).setNegativeButton(C0254R.string.close, new C03061());
                final String cguid = pointOverlay.getPoint().getData();
                if (MapsforgeActivity.this.allowStartCartridge && cguid != null) {
                    builder.setPositiveButton(C0254R.string.start, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MapsforgeActivity.this, MainActivity.class);
                            intent.putExtra("cguid", cguid);
                            intent.addFlags(335544320);
                            MapsforgeActivity.this.startActivity(intent);
                            dialog.dismiss();
                            MapsforgeActivity.this.finish();
                        }
                    });
                }
                builder.show();
            }
        }
    }

    private static Polyline createPolyline(List<GeoPoint> geoPoints) {
        PolygonalChain polygonalChain = new PolygonalChain(geoPoints);
        Paint paintStroke = new Paint(1);
        paintStroke.setStyle(Style.STROKE);
        paintStroke.setColor(-65281);
        paintStroke.setStrokeWidth(4.0f);
        return new Polyline(polygonalChain, paintStroke);
    }

    private void configureMapView() {
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.setClickable(true);
        this.mapView.setFocusable(true);
        MapScaleBar mapScaleBar = this.mapView.getMapScaleBar();
        mapScaleBar.setText(TextField.KILOMETER, getString(C0254R.string.unit_symbol_kilometer));
        mapScaleBar.setText(TextField.METER, getString(C0254R.string.unit_symbol_meter));
    }

    private Circle createCircle(GeoPoint geoPoint) {
        Paint paintFill = new Paint(1);
        paintFill.setStyle(Style.FILL);
        paintFill.setColor(-16776961);
        Paint paintStroke = new Paint(1);
        paintStroke.setStyle(Style.STROKE);
        paintStroke.setColor(-12303292);
        paintStroke.setStrokeWidth(3.0f);
        return new Circle(geoPoint, 0.0f, paintFill, paintStroke);
    }

    private void disableShowMyLocation() {
        if (this.myLocationOverlay.isMyLocationEnabled()) {
            this.myLocationOverlay.disableMyLocation();
            disableSnapToLocation(false);
            this.snapToLocationView.setVisibility(8);
        }
    }

    private void disableSnapToLocation(boolean showToast) {
        if (this.myLocationOverlay.isSnapToLocationEnabled()) {
            this.myLocationOverlay.setSnapToLocationEnabled(false);
            this.snapToLocationView.setChecked(false);
            this.mapView.setClickable(true);
            if (showToast) {
                showToastOnUiThread(getString(C0254R.string.snap_to_location_disabled));
            }
        }
    }

    private void enableShowMyLocation(boolean centerAtFirstFix) {
        if (!this.myLocationOverlay.isMyLocationEnabled()) {
            if (this.myLocationOverlay.enableMyLocation(centerAtFirstFix)) {
                this.mapView.getOverlays().add(this.navigationOverlay);
                this.mapView.getOverlays().add(this.myLocationOverlay);
                this.snapToLocationView.setVisibility(0);
                return;
            }
            showDialog(2);
        }
    }

    private void enableSnapToLocation(boolean showToast) {
        if (!this.myLocationOverlay.isSnapToLocationEnabled()) {
            this.myLocationOverlay.setSnapToLocationEnabled(true);
            this.snapToLocationView.setChecked(true);
            this.mapView.setClickable(false);
            if (showToast) {
                showToastOnUiThread(getString(C0254R.string.snap_to_location_enabled));
            }
        }
    }

    private void gotoLastKnownPosition() {
        Location bestLocation = null;
        LocationManager locationManager = (LocationManager) getSystemService("location");
        for (String provider : locationManager.getProviders(true)) {
            Location currentLocation = locationManager.getLastKnownLocation(provider);
            if (currentLocation != null && (bestLocation == null || bestLocation.getAccuracy() > currentLocation.getAccuracy())) {
                bestLocation = currentLocation;
            }
        }
        if (bestLocation != null) {
            this.mapView.getMapViewPosition().setCenter(new GeoPoint(bestLocation.getLatitude(), bestLocation.getLongitude()));
            return;
        }
        showToastOnUiThread(getString(C0254R.string.error_last_location_unknown));
    }

    private void invertSnapToLocation() {
        if (this.myLocationOverlay.isSnapToLocationEnabled()) {
            disableSnapToLocation(true);
        } else {
            enableSnapToLocation(true);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == -1) {
                disableSnapToLocation(true);
                if (intent != null && intent.getStringExtra(FilePicker.SELECTED_FILE) != null) {
                    this.mapView.setMapFile(new File(intent.getStringExtra(FilePicker.SELECTED_FILE)));
                    setMapGenerator(MapGeneratorInternal.DATABASE_RENDERER);
                    return;
                }
                return;
            }
            if (resultCode != 0 || this.mapView.getMapFile() == null) {
            }
        } else if (requestCode == 1 && resultCode == -1 && intent != null && intent.getStringExtra(FilePicker.SELECTED_FILE) != null) {
            try {
                this.mapView.setRenderTheme(new File(intent.getStringExtra(FilePicker.SELECTED_FILE)));
            } catch (FileNotFoundException e) {
                showToastOnUiThread(e.getLocalizedMessage());
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.screenshotCapturer = new ScreenshotCapturer(this);
        this.screenshotCapturer.start();
        setContentView(C0254R.layout.activity_mapsforge);
        this.mapView = (MyMapView) findViewById(C0254R.C0253id.mapView);
        configureMapView();
        this.snapToLocationView = (ToggleButton) findViewById(C0254R.C0253id.snapToLocationView);
        this.snapToLocationView.setOnClickListener(new C03092());
        this.myLocationOverlay = new SensorMyLocationOverlay(this, this.mapView, new RotationMarker(null, Marker.boundCenter(getResources().getDrawable(C0254R.C0252drawable.my_location_chevron))));
        this.navigationOverlay = new NavigationOverlay(this.myLocationOverlay);
        this.listOverlay = new PointListOverlay();
        this.listOverlay.registerOnTapEvent(this.tapListener);
        if (savedInstanceState != null) {
            this.showPins = savedInstanceState.getBoolean(BUNDLE_SHOW_PINS, true);
            this.showLabels = savedInstanceState.getBoolean(BUNDLE_SHOW_LABELS, true);
            this.allowStartCartridge = savedInstanceState.getBoolean(BUNDLE_ALLOW_START_CARTRIDGE, false);
        } else {
            this.showPins = sharedPreferences.getBoolean(BUNDLE_SHOW_PINS, true);
            this.showLabels = sharedPreferences.getBoolean(BUNDLE_SHOW_LABELS, true);
        }
        this.mapView.getOverlays().add(this.listOverlay);
        this.wakeLock = ((PowerManager) getSystemService("power")).newWakeLock(6, "AMV");
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(BUNDLE_SHOW_MY_LOCATION, true)) {
                enableShowMyLocation(savedInstanceState.getBoolean(BUNDLE_CENTER_AT_FIRST_FIX, false));
                if (savedInstanceState.getBoolean(BUNDLE_SNAP_TO_LOCATION, false)) {
                    enableSnapToLocation(false);
                }
            }
        } else if (sharedPreferences.getBoolean(BUNDLE_SHOW_MY_LOCATION, true)) {
            enableShowMyLocation(sharedPreferences.getBoolean(BUNDLE_CENTER_AT_FIRST_FIX, false));
            if (sharedPreferences.getBoolean(BUNDLE_SNAP_TO_LOCATION, false)) {
                enableSnapToLocation(false);
            }
        }
        ToggleButton showPinsButton = (ToggleButton) findViewById(C0254R.C0253id.showPinsView);
        showPinsButton.setChecked(this.showPins);
        showPinsButton.setOnClickListener(new C03103());
        ToggleButton showLabelsButton = (ToggleButton) findViewById(C0254R.C0253id.showLabelsView);
        showLabelsButton.setChecked(this.showLabels);
        showLabelsButton.setOnClickListener(new C03114());
        if (sharedPreferences.contains(KEY_MAP_GENERATOR)) {
            try {
                setMapGenerator(MapGeneratorInternal.valueOf(sharedPreferences.getString(KEY_MAP_GENERATOR, null)));
            } catch (Exception e) {
            }
        }
        refreshItems();
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public Dialog onCreateDialog(int id) {
        Builder builder = new Builder(this);
        if (id == 0) {
            builder.setIcon(17301575);
            builder.setTitle(C0254R.string.menu_position_enter_coordinates);
            LayoutInflater factory = LayoutInflater.from(this);
            final View view;
            switch (Preferences.FORMAT_COO_LATLON) {
                case 1:
                    view = factory.inflate(C0254R.layout.dialog_enter_coordinates_dm, null);
                    builder.setView(view);
                    builder.setPositiveButton(C0254R.string.go_to_position, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MapsforgeActivity.this.disableSnapToLocation(true);
                            try {
                                MapsforgeActivity.this.mapView.getMapViewPosition().setMapPosition(new MapPosition(new GeoPoint(Double.parseDouble(((EditText) view.findViewById(C0254R.C0253id.latitude_d)).getText().toString()) + (Double.parseDouble(((EditText) view.findViewById(C0254R.C0253id.latitude_m)).getText().toString()) / 60.0d), Double.parseDouble(((EditText) view.findViewById(C0254R.C0253id.longitude_d)).getText().toString()) + (Double.parseDouble(((EditText) view.findViewById(C0254R.C0253id.longitude_m)).getText().toString()) / 60.0d)), (byte) ((SeekBar) view.findViewById(C0254R.C0253id.zoomLevel)).getProgress()));
                            } catch (IllegalArgumentException e) {
                            }
                        }
                    });
                    break;
                case 2:
                    view = factory.inflate(C0254R.layout.dialog_enter_coordinates_dms, null);
                    builder.setView(view);
                    builder.setPositiveButton(C0254R.string.go_to_position, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MapsforgeActivity.this.disableSnapToLocation(true);
                            MapsforgeActivity.this.mapView.getMapViewPosition().setMapPosition(new MapPosition(new GeoPoint(((Double.parseDouble(((EditText) view.findViewById(C0254R.C0253id.latitude_m)).getText().toString()) / 60.0d) + Double.parseDouble(((EditText) view.findViewById(C0254R.C0253id.latitude_d)).getText().toString())) + (Double.parseDouble(((EditText) view.findViewById(C0254R.C0253id.latitude_s)).getText().toString()) / 3600.0d), ((Double.parseDouble(((EditText) view.findViewById(C0254R.C0253id.longitude_m)).getText().toString()) / 60.0d) + Double.parseDouble(((EditText) view.findViewById(C0254R.C0253id.longitude_d)).getText().toString())) + (Double.parseDouble(((EditText) view.findViewById(C0254R.C0253id.longitude_s)).getText().toString()) / 3600.0d)), (byte) ((SeekBar) view.findViewById(C0254R.C0253id.zoomLevel)).getProgress()));
                        }
                    });
                    break;
                default:
                    view = factory.inflate(C0254R.layout.dialog_enter_coordinates, null);
                    builder.setView(view);
                    builder.setPositiveButton(C0254R.string.go_to_position, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MapsforgeActivity.this.disableSnapToLocation(true);
                            MapsforgeActivity.this.mapView.getMapViewPosition().setMapPosition(new MapPosition(new GeoPoint(Double.parseDouble(((EditText) view.findViewById(C0254R.C0253id.latitude)).getText().toString()), Double.parseDouble(((EditText) view.findViewById(C0254R.C0253id.longitude)).getText().toString())), (byte) ((SeekBar) view.findViewById(C0254R.C0253id.zoomLevel)).getProgress()));
                        }
                    });
                    break;
            }
            builder.setNegativeButton(C0254R.string.cancel, null);
            return builder.create();
        } else if (id == 2) {
            builder.setIcon(17301569);
            builder.setTitle(C0254R.string.error);
            builder.setMessage(C0254R.string.no_location_provider_available);
            builder.setPositiveButton(C0254R.string.f48ok, null);
            return builder.create();
        } else if (id != 1) {
            return null;
        } else {
            builder.setIcon(17301569);
            builder.setTitle(C0254R.string.menu_info_map_file);
            builder.setView(LayoutInflater.from(this).inflate(C0254R.layout.dialog_info_map_file, null));
            builder.setPositiveButton(C0254R.string.f48ok, null);
            return builder.create();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0254R.menu.mapsforge_menu, menu);
        this.menu = menu;
        SubMenu mapgeneratorMenu = menu.findItem(C0254R.C0253id.menu_mapgenerator).getSubMenu();
        String[] keys = getResources().getStringArray(C0254R.array.mapgenerator_keys);
        String[] values = getResources().getStringArray(C0254R.array.mapgenerator_values);
        for (int i = 0; i < keys.length; i++) {
            final MapGeneratorInternal generator = MapGeneratorInternal.valueOf(keys[i]);
            MenuItem item = mapgeneratorMenu.add(C0254R.C0253id.menu_mapgenerator_group, 0, 0, values[i]);
            item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (generator == MapGeneratorInternal.DATABASE_RENDERER && MapsforgeActivity.this.mapView.getMapFile() == null) {
                        MapsforgeActivity.this.startMapFilePicker();
                    } else {
                        MapsforgeActivity.this.setMapGenerator(generator);
                        item.setChecked(true);
                    }
                    return true;
                }
            });
            if (this.mapGeneratorInternal != null && this.mapGeneratorInternal.name().equals(generator.name())) {
                item.setChecked(true);
            }
        }
        mapgeneratorMenu.setGroupCheckable(C0254R.C0253id.menu_mapgenerator_group, true, true);
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        this.screenshotCapturer.interrupt();
        Editor preferences = PreferenceManager.getDefaultSharedPreferences(this).edit();
        preferences.putBoolean(BUNDLE_SHOW_MY_LOCATION, this.myLocationOverlay.isMyLocationEnabled());
        preferences.putBoolean(BUNDLE_CENTER_AT_FIRST_FIX, this.myLocationOverlay.isCenterAtNextFix());
        preferences.putBoolean(BUNDLE_SNAP_TO_LOCATION, this.myLocationOverlay.isSnapToLocationEnabled());
        preferences.putBoolean(BUNDLE_SHOW_PINS, this.showPins);
        preferences.putBoolean(BUNDLE_SHOW_LABELS, this.showLabels);
        preferences.putString(KEY_MAP_GENERATOR, this.mapGeneratorInternal.name());
        preferences.commit();
        disableShowMyLocation();
    }

    /* Access modifiers changed, original: protected */
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        refreshItems();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0254R.C0253id.menu_info /*2131492991*/:
            case C0254R.C0253id.menu_position /*2131492994*/:
            case C0254R.C0253id.menu_screenshot /*2131493001*/:
            case C0254R.C0253id.menu_render_theme /*2131493005*/:
            case C0254R.C0253id.menu_mapgenerator /*2131493009*/:
                return true;
            case C0254R.C0253id.menu_info_map_file /*2131492992*/:
                if (this.mapGeneratorInternal != MapGeneratorInternal.DATABASE_RENDERER || this.mapView.getMapFile() == null) {
                    return true;
                }
                showDialog(1);
                return true;
            case C0254R.C0253id.menu_info_about /*2131492993*/:
                startActivity(new Intent(this, InfoView.class));
                return true;
            case C0254R.C0253id.menu_position_my_location_enable /*2131492995*/:
                enableShowMyLocation(true);
                onPrepareOptionsMenu(this.menu);
                return true;
            case C0254R.C0253id.menu_position_my_location_disable /*2131492996*/:
                disableShowMyLocation();
                onPrepareOptionsMenu(this.menu);
                return true;
            case C0254R.C0253id.menu_position_last_known /*2131492997*/:
                gotoLastKnownPosition();
                return true;
            case C0254R.C0253id.menu_position_enter_coordinates /*2131492998*/:
                showDialog(0);
                return true;
            case C0254R.C0253id.menu_position_target /*2131492999*/:
                GeoPoint geoPoint = this.navigationOverlay.getTarget();
                if (geoPoint == null) {
                    return true;
                }
                this.mapView.getMapViewPosition().setCenter(geoPoint);
                return true;
            case C0254R.C0253id.menu_position_map_center /*2131493000*/:
                if (this.mapGeneratorInternal != MapGeneratorInternal.DATABASE_RENDERER) {
                    return true;
                }
                disableSnapToLocation(true);
                this.mapView.getMapViewPosition().setCenter(this.mapView.getMapDatabase().getMapFileInfo().boundingBox.getCenterPoint());
                return true;
            case C0254R.C0253id.menu_screenshot_jpeg /*2131493002*/:
                this.screenshotCapturer.captureScreenshot(CompressFormat.JPEG);
                return true;
            case C0254R.C0253id.menu_screenshot_png /*2131493003*/:
                this.screenshotCapturer.captureScreenshot(CompressFormat.PNG);
                return true;
            case C0254R.C0253id.menu_preferences /*2131493004*/:
                startActivity(new Intent(this, EditPreferences.class));
                return true;
            case C0254R.C0253id.menu_render_theme_osmarender /*2131493006*/:
                this.mapView.setRenderTheme(InternalRenderTheme.OSMARENDER);
                return true;
            case C0254R.C0253id.menu_render_theme_select_file /*2131493007*/:
                startRenderThemePicker();
                return true;
            case C0254R.C0253id.menu_mapfile /*2131493008*/:
                startMapFilePicker();
                return true;
            case C0254R.C0253id.menu_refresh /*2131493011*/:
                refresh();
                return true;
            default:
                return false;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        if (PreferenceValues.getCurrentActivity() == this) {
            PreferenceValues.setCurrentActivity(null);
        }
        MainApplication.onActivityPause();
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public void onPrepareDialog(int id, Dialog dialog) {
        TextView textView;
        if (id == 0) {
            MapViewPosition mapViewPosition = this.mapView.getMapViewPosition();
            GeoPoint mapCenter = mapViewPosition.getCenter();
            double latitude = mapCenter.latitude;
            double longitude = mapCenter.longitude;
            int latitude_d;
            int longitude_d;
            switch (Preferences.FORMAT_COO_LATLON) {
                case 1:
                    latitude_d = (int) latitude;
                    double latitude_m = (latitude - ((double) latitude_d)) * 60.0d;
                    ((EditText) dialog.findViewById(C0254R.C0253id.latitude_d)).setText(Integer.toString(latitude_d));
                    ((EditText) dialog.findViewById(C0254R.C0253id.latitude_m)).setText(Double.toString(latitude_m));
                    longitude_d = (int) longitude;
                    double longitude_m = (longitude - ((double) longitude_d)) * 60.0d;
                    ((EditText) dialog.findViewById(C0254R.C0253id.longitude_d)).setText(Integer.toString(longitude_d));
                    ((EditText) dialog.findViewById(C0254R.C0253id.longitude_m)).setText(Double.toString(longitude_m));
                    break;
                case 2:
                    latitude_d = (int) latitude;
                    int latitude_m2 = (int) ((latitude - ((double) latitude_d)) * 60.0d);
                    double latitude_s = ((latitude - ((double) latitude_d)) - ((double) (latitude_m2 / 60))) * 3600.0d;
                    ((EditText) dialog.findViewById(C0254R.C0253id.latitude_d)).setText(Integer.toString(latitude_d));
                    ((EditText) dialog.findViewById(C0254R.C0253id.latitude_m)).setText(Integer.toString(latitude_m2));
                    ((EditText) dialog.findViewById(C0254R.C0253id.latitude_s)).setText(Double.toString(latitude_s));
                    longitude_d = (int) longitude;
                    int longitude_m2 = (int) ((longitude - ((double) longitude_d)) * 60.0d);
                    double longitude_s = ((longitude - ((double) longitude_d)) - ((double) (longitude_m2 / 60))) * 3600.0d;
                    ((EditText) dialog.findViewById(C0254R.C0253id.longitude_d)).setText(Integer.toString(longitude_d));
                    ((EditText) dialog.findViewById(C0254R.C0253id.longitude_m)).setText(Integer.toString(longitude_m2));
                    ((EditText) dialog.findViewById(C0254R.C0253id.longitude_s)).setText(Double.toString(longitude_s));
                    break;
                default:
                    ((EditText) dialog.findViewById(C0254R.C0253id.latitude)).setText(Double.toString(latitude));
                    ((EditText) dialog.findViewById(C0254R.C0253id.longitude)).setText(Double.toString(longitude));
                    break;
            }
            SeekBar zoomlevel = (SeekBar) dialog.findViewById(C0254R.C0253id.zoomLevel);
            zoomlevel.setMax(this.mapView.getDatabaseRenderer().getZoomLevelMax());
            zoomlevel.setProgress(mapViewPosition.getZoomLevel());
            textView = (TextView) dialog.findViewById(C0254R.C0253id.zoomlevelValue);
            textView.setText(String.valueOf(zoomlevel.getProgress()));
            zoomlevel.setOnSeekBarChangeListener(new SeekBarChangeListener(textView));
        } else if (id == 1 && this.mapGeneratorInternal == MapGeneratorInternal.DATABASE_RENDERER) {
            MapFileInfo mapFileInfo = this.mapView.getMapDatabase().getMapFileInfo();
            ((TextView) dialog.findViewById(C0254R.C0253id.infoMapFileViewName)).setText(this.mapView.getMapFile().getAbsolutePath());
            ((TextView) dialog.findViewById(C0254R.C0253id.infoMapFileViewSize)).setText(FileUtils.formatFileSize(mapFileInfo.fileSize, getResources()));
            ((TextView) dialog.findViewById(C0254R.C0253id.infoMapFileViewVersion)).setText(String.valueOf(mapFileInfo.fileVersion));
            textView = (TextView) dialog.findViewById(C0254R.C0253id.infoMapFileViewDebug);
            if (mapFileInfo.debugFile) {
                textView.setText(C0254R.string.info_map_file_debug_yes);
            } else {
                textView.setText(C0254R.string.info_map_file_debug_no);
            }
            ((TextView) dialog.findViewById(C0254R.C0253id.infoMapFileViewDate)).setText(DateFormat.getDateTimeInstance().format(new Date(mapFileInfo.mapDate)));
            textView = (TextView) dialog.findViewById(C0254R.C0253id.infoMapFileViewArea);
            BoundingBox boundingBox = mapFileInfo.boundingBox;
            textView.setText(boundingBox.minLatitude + ", " + boundingBox.minLongitude + " – \n" + boundingBox.maxLatitude + ", " + boundingBox.maxLongitude);
            textView = (TextView) dialog.findViewById(C0254R.C0253id.infoMapFileViewStartPosition);
            GeoPoint startPosition = mapFileInfo.startPosition;
            if (startPosition == null) {
                textView.setText(null);
            } else {
                textView.setText(startPosition.latitude + ", " + startPosition.longitude);
            }
            textView = (TextView) dialog.findViewById(C0254R.C0253id.infoMapFileViewStartZoomLevel);
            Byte startZoomLevel = mapFileInfo.startZoomLevel;
            if (startZoomLevel == null) {
                textView.setText(null);
            } else {
                textView.setText(startZoomLevel.toString());
            }
            ((TextView) dialog.findViewById(C0254R.C0253id.infoMapFileViewLanguagePreference)).setText(mapFileInfo.languagePreference);
            ((TextView) dialog.findViewById(C0254R.C0253id.infoMapFileViewComment)).setText(mapFileInfo.comment);
            ((TextView) dialog.findViewById(C0254R.C0253id.infoMapFileViewCreatedBy)).setText(mapFileInfo.createdBy);
        } else {
            super.onPrepareDialog(id, dialog);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.myLocationOverlay.isMyLocationEnabled()) {
            menu.findItem(C0254R.C0253id.menu_position_my_location_enable).setVisible(false);
            menu.findItem(C0254R.C0253id.menu_position_my_location_enable).setEnabled(false);
            menu.findItem(C0254R.C0253id.menu_position_my_location_disable).setVisible(true);
            menu.findItem(C0254R.C0253id.menu_position_my_location_disable).setEnabled(true);
        } else {
            menu.findItem(C0254R.C0253id.menu_position_my_location_enable).setVisible(true);
            menu.findItem(C0254R.C0253id.menu_position_my_location_enable).setEnabled(true);
            menu.findItem(C0254R.C0253id.menu_position_my_location_disable).setVisible(false);
            menu.findItem(C0254R.C0253id.menu_position_my_location_disable).setEnabled(false);
        }
        if (this.mapGeneratorInternal != MapGeneratorInternal.DATABASE_RENDERER || this.mapView.getMapFile() == null) {
            menu.findItem(C0254R.C0253id.menu_info_map_file).setEnabled(false);
            menu.findItem(C0254R.C0253id.menu_position_map_center).setEnabled(false);
            menu.findItem(C0254R.C0253id.menu_render_theme).setEnabled(false);
            menu.findItem(C0254R.C0253id.menu_mapfile).setEnabled(false);
        } else {
            menu.findItem(C0254R.C0253id.menu_info_map_file).setEnabled(true);
            menu.findItem(C0254R.C0253id.menu_position_map_center).setEnabled(true);
            menu.findItem(C0254R.C0253id.menu_render_theme).setEnabled(true);
            menu.findItem(C0254R.C0253id.menu_mapfile).setEnabled(true);
        }
        if (this.navigationOverlay.getTarget() == null) {
            menu.findItem(C0254R.C0253id.menu_position_target).setEnabled(false);
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MapScaleBar mapScaleBar = this.mapView.getMapScaleBar();
        mapScaleBar.setShowMapScaleBar(sharedPreferences.getBoolean("showScaleBar", false));
        mapScaleBar.setImperialUnits(sharedPreferences.getString("scaleBarUnit", getString(C0254R.string.preferences_scale_bar_unit_default)).equals("imperial"));
        try {
            this.mapView.setTextScale(Float.parseFloat(sharedPreferences.getString("textScale", getString(C0254R.string.preferences_text_scale_default))));
        } catch (NumberFormatException e) {
            this.mapView.setTextScale(1.0f);
        }
        if (sharedPreferences.getBoolean("fullscreen", false)) {
            getWindow().addFlags(1024);
            getWindow().clearFlags(2048);
        } else {
            getWindow().clearFlags(1024);
            getWindow().addFlags(2048);
        }
        if (sharedPreferences.getBoolean("wakeLock", false) && !this.wakeLock.isHeld()) {
            this.wakeLock.acquire();
        }
        boolean persistent = sharedPreferences.getBoolean("cachePersistence", false);
        int capacity = Math.min(sharedPreferences.getInt("cacheSize", FILE_SYSTEM_CACHE_SIZE_DEFAULT), FILE_SYSTEM_CACHE_SIZE_MAX);
        TileCache fileSystemTileCache = this.mapView.getFileSystemTileCache();
        fileSystemTileCache.setPersistent(persistent);
        fileSystemTileCache.setCapacity(capacity);
        this.mapView.getMapMover().setMoveSpeedFactor(((float) Math.min(sharedPreferences.getInt("moveSpeed", 10), 30)) / 10.0f);
        this.mapView.getFpsCounter().setFpsCounter(sharedPreferences.getBoolean("showFpsCounter", false));
        this.mapView.setDebugSettings(new DebugSettings(sharedPreferences.getBoolean("drawTileCoordinates", false), sharedPreferences.getBoolean("drawTileFrames", false), sharedPreferences.getBoolean("highlightWaterTiles", false)));
        PreferenceValues.setCurrentActivity(this);
    }

    /* Access modifiers changed, original: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_SHOW_MY_LOCATION, this.myLocationOverlay.isMyLocationEnabled());
        outState.putBoolean(BUNDLE_CENTER_AT_FIRST_FIX, this.myLocationOverlay.isCenterAtNextFix());
        outState.putBoolean(BUNDLE_SNAP_TO_LOCATION, this.myLocationOverlay.isSnapToLocationEnabled());
        outState.putBoolean(BUNDLE_SHOW_PINS, this.showPins);
        outState.putBoolean(BUNDLE_SHOW_LABELS, this.showLabels);
        outState.putBoolean(BUNDLE_ALLOW_START_CARTRIDGE, this.allowStartCartridge);
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return this.mapView.onTrackballEvent(event);
    }

    public void refresh() {
        runOnUiThread(new C03169());
    }

    private void refreshItems() {
        boolean center;
        boolean navigate;
        boolean z = true;
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || !bundle.getBoolean(BUNDLE_CENTER, false)) {
            center = false;
        } else {
            center = true;
        }
        if (bundle == null || !bundle.getBoolean(BUNDLE_NAVIGATE, false)) {
            navigate = false;
        } else {
            navigate = true;
        }
        if (bundle == null || !bundle.getBoolean(BUNDLE_ALLOW_START_CARTRIDGE, false)) {
            z = false;
        }
        this.allowStartCartridge = z;
        if (bundle == null || !bundle.containsKey(BUNDLE_ITEMS)) {
            showMapPack(VectorMapDataProvider.getInstance().getItems());
        } else {
            showMapPack(bundle.getParcelableArrayList(BUNDLE_ITEMS));
        }
        if (center && this.itemsLatitude != 0.0d && this.itemsLongitude != 0.0d) {
            GeoPoint geoPoint;
            if (!navigate || this.navigationOverlay.getTarget() == null) {
                geoPoint = new GeoPoint(this.itemsLatitude, this.itemsLongitude);
            } else {
                geoPoint = this.navigationOverlay.getTarget();
            }
            this.mapView.getMapViewPosition().setMapPosition(new MapPosition(geoPoint, this.mapView.getMapViewPosition().getZoomLevel()));
        }
    }

    private void setMapGenerator(MapGeneratorInternal type) {
        if (this.mapGeneratorInternal != type) {
            this.mapGeneratorInternal = type;
            MapGenerator generator = MapGeneratorFactory.createMapGenerator(type);
            this.mapView.setMapGenerator(generator, type.ordinal());
            TextView attributionView = (TextView) findViewById(C0254R.C0253id.attribution);
            if (generator instanceof TileDownloader) {
                String attribution = ((TileDownloader) generator).getAttribution();
                attributionView.setText(attribution == null ? "" : Html.fromHtml(attribution, null, null));
                return;
            }
            attributionView.setText("");
        }
    }

    private void showMapPack(ArrayList<MapPointPack> packs) {
        synchronized (this.lock) {
            this.navigationOverlay.setTarget(null);
            this.itemsLongitude = 0.0d;
            this.itemsLatitude = 0.0d;
            int count = 0;
            this.listOverlay.clear();
            List<OverlayItem> overlayItems = this.listOverlay.getOverlayItems();
            List<OverlayItem> overlayLines = new ArrayList();
            List<OverlayItem> overlayPoints = new ArrayList();
            Iterator it = packs.iterator();
            while (it.hasNext()) {
                MapPointPack pack = (MapPointPack) it.next();
                Iterator it2;
                MapPoint mp;
                if (pack.isPolygon()) {
                    List<GeoPoint> geoPoints = new ArrayList();
                    it2 = pack.getPoints().iterator();
                    while (it2.hasNext()) {
                        mp = (MapPoint) it2.next();
                        geoPoints.add(new GeoPoint(mp.getLatitude(), mp.getLongitude()));
                    }
                    overlayLines.add(createPolyline(geoPoints));
                } else {
                    Drawable icon;
                    if (pack.getIcon() == null) {
                        icon = getResources().getDrawable(pack.getResource() != 0 ? pack.getResource() : C0254R.C0252drawable.marker_red);
                    } else {
                        Bitmap b = pack.getIcon();
                        if (b.getWidth() > 32 && b.getWidth() >= b.getHeight()) {
                            b = Bitmap.createScaledBitmap(b, 32, (b.getHeight() * 32) / b.getWidth(), false);
                        } else if (b.getHeight() > 32) {
                            b = Bitmap.createScaledBitmap(b, (b.getWidth() * 32) / b.getHeight(), 32, false);
                        }
                        icon = new BitmapDrawable(getResources(), b);
                    }
                    icon = Marker.boundCenterBottom(icon);
                    it2 = pack.getPoints().iterator();
                    while (it2.hasNext()) {
                        mp = (MapPoint) it2.next();
                        GeoPoint geoPoint = new GeoPoint(mp.getLatitude(), mp.getLongitude());
                        PointOverlay pointOverlay = new PointOverlay(geoPoint, icon, mp);
                        pointOverlay.setMarkerVisible(this.showPins);
                        pointOverlay.setLabelVisible(this.showLabels);
                        overlayPoints.add(pointOverlay);
                        if (mp.isTarget()) {
                            this.navigationOverlay.setTarget(geoPoint);
                        }
                        this.itemsLatitude += mp.getLatitude();
                        this.itemsLongitude += mp.getLongitude();
                        count++;
                    }
                }
            }
            overlayItems.addAll(overlayLines);
            overlayItems.addAll(overlayPoints);
            if (count > 0) {
                this.itemsLatitude /= (double) count;
                this.itemsLongitude /= (double) count;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void showToastOnUiThread(final String text) {
        if (AndroidUtils.currentThreadIsUiThread()) {
            Toast.makeText(this, text, 0).show();
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MapsforgeActivity.this, text, 0).show();
                }
            });
        }
    }

    private void startMapFilePicker() {
        FilePicker.setFileDisplayFilter(FILE_FILTER_EXTENSION_MAP);
        FilePicker.setFileSelectFilter(new ValidMapFile());
        startActivityForResult(new Intent(this, FilePicker.class), 0);
    }

    private void startRenderThemePicker() {
        FilePicker.setFileDisplayFilter(FILE_FILTER_EXTENSION_XML);
        FilePicker.setFileSelectFilter(new ValidRenderTheme());
        startActivityForResult(new Intent(this, FilePicker.class), 1);
    }

    private void visibilityChanged() {
        synchronized (this.lock) {
            List<OverlayItem> overlayItems = this.listOverlay.getOverlayItems();
            for (int i = 0; i < overlayItems.size(); i++) {
                OverlayItem item = (OverlayItem) overlayItems.get(i);
                if (item instanceof LabelMarker) {
                    LabelMarker labelMarker = (LabelMarker) item;
                    labelMarker.setMarkerVisible(this.showPins);
                    labelMarker.setLabelVisible(this.showLabels);
                }
            }
        }
        this.mapView.getOverlayController().redrawOverlays();
    }
}

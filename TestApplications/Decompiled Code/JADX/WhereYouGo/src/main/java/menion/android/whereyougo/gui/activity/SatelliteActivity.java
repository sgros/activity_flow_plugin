package menion.android.whereyougo.gui.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.ArrayList;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.geo.location.Point2D.Int;
import menion.android.whereyougo.geo.location.SatellitePosition;
import menion.android.whereyougo.geo.orientation.Orientation;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.view.Satellite2DView;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.utils.UtilsFormat;

public class SatelliteActivity extends CustomActivity implements ILocationEventListener {
    private static final String TAG = "SatelliteScreen";
    private ToggleButton buttonGps;
    private Satellite2DView satelliteView;
    private final ArrayList<SatellitePosition> satellites = new ArrayList();

    /* renamed from: menion.android.whereyougo.gui.activity.SatelliteActivity$1 */
    class C02661 implements OnCheckedChangeListener {
        C02661() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                LocationState.setGpsOn(SatelliteActivity.this);
            } else {
                LocationState.setGpsOff(SatelliteActivity.this);
                SatelliteActivity.this.satellites.clear();
                SatelliteActivity.this.satelliteView.invalidate();
            }
            SatelliteActivity.this.onGpsStatusChanged(0, null);
            PreferenceValues.enableWakeLock();
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.SatelliteActivity$2 */
    class C02672 implements OnCheckedChangeListener {
        C02672() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ManagerNotify.toastLongMessage((int) C0254R.string.pref_sensors_compass_hardware_desc);
            Preferences.SENSOR_HARDWARE_COMPASS = isChecked;
            Preferences.setPreference((int) C0254R.string.pref_KEY_B_SENSOR_HARDWARE_COMPASS, Preferences.SENSOR_HARDWARE_COMPASS);
            C0322A.getRotator().manageSensors();
        }
    }

    private void createLayout() {
        LinearLayout llSkyplot = (LinearLayout) findViewById(C0254R.C0253id.linear_layout_skyplot);
        llSkyplot.removeAllViews();
        this.satelliteView = new Satellite2DView(this, this.satellites);
        llSkyplot.addView(this.satelliteView, -1, -1);
        if (Utils.isAndroid30OrMore()) {
            findViewById(C0254R.C0253id.linear_layout_bottom_3).setBackgroundColor(CustomDialog.BOTTOM_COLOR_A3);
        }
        this.buttonGps = (ToggleButton) findViewById(C0254R.C0253id.btn_gps_on_off);
        this.buttonGps.setChecked(LocationState.isActuallyHardwareGpsOn());
        this.buttonGps.setOnCheckedChangeListener(new C02661());
        ToggleButton buttonCompass = (ToggleButton) findViewById(C0254R.C0253id.btn_compass_on_off);
        buttonCompass.setChecked(Preferences.SENSOR_HARDWARE_COMPASS);
        buttonCompass.setOnCheckedChangeListener(new C02672());
    }

    public String getName() {
        return TAG;
    }

    public int getPriority() {
        return 2;
    }

    public boolean isRequired() {
        return false;
    }

    public void notifyGpsDisable() {
        this.buttonGps.setChecked(false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0254R.layout.satellite_screen_activity);
        createLayout();
    }

    public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> gpsStatus) {
        try {
            Int num = setSatellites(gpsStatus);
            this.satelliteView.invalidate();
            ((TextView) findViewById(C0254R.C0253id.text_view_satellites)).setText(num.f52x + " | " + num.f53y);
        } catch (Exception e) {
            Logger.m21e(TAG, "onGpsStatusChanged(" + event + ", " + gpsStatus + "), e:" + e.toString());
        }
    }

    public void onLocationChanged(final Location location) {
        runOnUiThread(new Runnable() {
            public void run() {
                String provider = location.getProvider();
                boolean z = true;
                switch (provider.hashCode()) {
                    case 102570:
                        if (provider.equals("gps")) {
                            z = false;
                            break;
                        }
                        break;
                    case 1843485230:
                        if (provider.equals("network")) {
                            z = true;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case false:
                        provider = SatelliteActivity.this.getString(C0254R.string.provider_gps);
                        break;
                    case true:
                        provider = SatelliteActivity.this.getString(C0254R.string.provider_network);
                        break;
                    default:
                        provider = SatelliteActivity.this.getString(C0254R.string.provider_passive);
                        break;
                }
                ((TextView) SatelliteActivity.this.findViewById(C0254R.C0253id.text_view_provider)).setText(provider);
                ((TextView) SatelliteActivity.this.findViewById(C0254R.C0253id.text_view_latitude)).setText(UtilsFormat.formatLatitude(location.getLatitude()));
                ((TextView) SatelliteActivity.this.findViewById(C0254R.C0253id.text_view_longitude)).setText(UtilsFormat.formatLongitude(location.getLongitude()));
                ((TextView) SatelliteActivity.this.findViewById(C0254R.C0253id.text_view_altitude)).setText(UtilsFormat.formatAltitude(location.getAltitude(), true));
                ((TextView) SatelliteActivity.this.findViewById(C0254R.C0253id.text_view_accuracy)).setText(UtilsFormat.formatDistance((double) location.getAccuracy(), false));
                ((TextView) SatelliteActivity.this.findViewById(C0254R.C0253id.text_view_speed)).setText(UtilsFormat.formatSpeed((double) location.getSpeed(), false));
                ((TextView) SatelliteActivity.this.findViewById(C0254R.C0253id.text_view_declination)).setText(UtilsFormat.formatAngle((double) Orientation.getDeclination()));
                long lastFix = LocationState.getLastFixTime();
                if (lastFix > 0) {
                    ((TextView) SatelliteActivity.this.findViewById(C0254R.C0253id.text_view_time_gps)).setText(UtilsFormat.formatTime(lastFix));
                } else {
                    ((TextView) SatelliteActivity.this.findViewById(C0254R.C0253id.text_view_time_gps)).setText("~");
                }
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        onLocationChanged(LocationState.getLocation());
        onGpsStatusChanged(0, null);
    }

    public void onStart() {
        super.onStart();
        LocationState.addLocationChangeListener(this);
        if (this.buttonGps.isChecked() && !LocationState.isActuallyHardwareGpsOn()) {
            notifyGpsDisable();
        }
    }

    public void onStatusChanged(String provider, int state, Bundle extra) {
    }

    public void onStop() {
        super.onStop();
        LocationState.removeLocationChangeListener(this);
    }

    private Int setSatellites(ArrayList<SatellitePosition> sats) {
        Int satCount;
        synchronized (this.satellites) {
            satCount = new Int();
            this.satellites.clear();
            if (sats != null) {
                for (int i = 0; i < sats.size(); i++) {
                    SatellitePosition sat = (SatellitePosition) sats.get(i);
                    if (sat.isFixed()) {
                        satCount.f52x++;
                    }
                    satCount.f53y++;
                    this.satellites.add(sat);
                }
            }
        }
        return satCount;
    }
}

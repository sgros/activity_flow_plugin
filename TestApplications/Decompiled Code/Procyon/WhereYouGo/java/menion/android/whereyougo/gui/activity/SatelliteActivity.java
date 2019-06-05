// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity;

import menion.android.whereyougo.geo.orientation.Orientation;
import menion.android.whereyougo.utils.UtilsFormat;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.utils.Logger;
import android.widget.TextView;
import android.os.Bundle;
import menion.android.whereyougo.geo.location.Point2D;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.preferences.PreferenceValues;
import android.widget.CompoundButton;
import android.widget.CompoundButton$OnCheckedChangeListener;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.utils.Utils;
import android.view.View;
import android.content.Context;
import android.widget.LinearLayout;
import menion.android.whereyougo.geo.location.SatellitePosition;
import java.util.ArrayList;
import menion.android.whereyougo.gui.view.Satellite2DView;
import android.widget.ToggleButton;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;

public class SatelliteActivity extends CustomActivity implements ILocationEventListener
{
    private static final String TAG = "SatelliteScreen";
    private ToggleButton buttonGps;
    private Satellite2DView satelliteView;
    private final ArrayList<SatellitePosition> satellites;
    
    public SatelliteActivity() {
        this.satellites = new ArrayList<SatellitePosition>();
    }
    
    private void createLayout() {
        final LinearLayout linearLayout = (LinearLayout)this.findViewById(2131492970);
        linearLayout.removeAllViews();
        linearLayout.addView((View)(this.satelliteView = new Satellite2DView((Context)this, this.satellites)), -1, -1);
        if (Utils.isAndroid30OrMore()) {
            this.findViewById(2131492978).setBackgroundColor(-2236963);
        }
        (this.buttonGps = (ToggleButton)this.findViewById(2131492982)).setChecked(LocationState.isActuallyHardwareGpsOn());
        this.buttonGps.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)new CompoundButton$OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                if (!b) {
                    LocationState.setGpsOff((Context)SatelliteActivity.this);
                    SatelliteActivity.this.satellites.clear();
                    SatelliteActivity.this.satelliteView.invalidate();
                }
                else {
                    LocationState.setGpsOn((Context)SatelliteActivity.this);
                }
                SatelliteActivity.this.onGpsStatusChanged(0, null);
                PreferenceValues.enableWakeLock();
            }
        });
        final ToggleButton toggleButton = (ToggleButton)this.findViewById(2131492983);
        toggleButton.setChecked(Preferences.SENSOR_HARDWARE_COMPASS);
        toggleButton.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)new CompoundButton$OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean sensor_HARDWARE_COMPASS) {
                ManagerNotify.toastLongMessage(2131165276);
                Preferences.setPreference(2131165562, Preferences.SENSOR_HARDWARE_COMPASS = sensor_HARDWARE_COMPASS);
                A.getRotator().manageSensors();
            }
        });
    }
    
    private Point2D.Int setSatellites(final ArrayList<SatellitePosition> list) {
        synchronized (this.satellites) {
            final Point2D.Int int1 = new Point2D.Int();
            this.satellites.clear();
            if (list != null) {
                for (int i = 0; i < list.size(); ++i) {
                    final SatellitePosition e = list.get(i);
                    if (e.isFixed()) {
                        ++int1.x;
                    }
                    ++int1.y;
                    this.satellites.add(e);
                }
            }
            return int1;
        }
    }
    
    @Override
    public String getName() {
        return "SatelliteScreen";
    }
    
    @Override
    public int getPriority() {
        return 2;
    }
    
    @Override
    public boolean isRequired() {
        return false;
    }
    
    public void notifyGpsDisable() {
        this.buttonGps.setChecked(false);
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130903058);
        this.createLayout();
    }
    
    @Override
    public void onGpsStatusChanged(final int i, final ArrayList<SatellitePosition> list) {
        try {
            final Point2D.Int setSatellites = this.setSatellites(list);
            this.satelliteView.invalidate();
            ((TextView)this.findViewById(2131492979)).setText((CharSequence)(setSatellites.x + " | " + setSatellites.y));
        }
        catch (Exception ex) {
            Logger.e("SatelliteScreen", "onGpsStatusChanged(" + i + ", " + list + "), e:" + ex.toString());
        }
    }
    
    @Override
    public void onLocationChanged(final Location location) {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final String provider = location.getProvider();
                String text = null;
                switch (provider) {
                    default: {
                        text = SatelliteActivity.this.getString(2131165395);
                        break;
                    }
                    case "gps": {
                        text = SatelliteActivity.this.getString(2131165393);
                        break;
                    }
                    case "network": {
                        text = SatelliteActivity.this.getString(2131165394);
                        break;
                    }
                }
                ((TextView)SatelliteActivity.this.findViewById(2131492973)).setText((CharSequence)text);
                ((TextView)SatelliteActivity.this.findViewById(2131492972)).setText((CharSequence)UtilsFormat.formatLatitude(location.getLatitude()));
                ((TextView)SatelliteActivity.this.findViewById(2131492974)).setText((CharSequence)UtilsFormat.formatLongitude(location.getLongitude()));
                ((TextView)SatelliteActivity.this.findViewById(2131492975)).setText((CharSequence)UtilsFormat.formatAltitude(location.getAltitude(), true));
                ((TextView)SatelliteActivity.this.findViewById(2131492977)).setText((CharSequence)UtilsFormat.formatDistance(location.getAccuracy(), false));
                ((TextView)SatelliteActivity.this.findViewById(2131492976)).setText((CharSequence)UtilsFormat.formatSpeed(location.getSpeed(), false));
                ((TextView)SatelliteActivity.this.findViewById(2131492980)).setText((CharSequence)UtilsFormat.formatAngle(Orientation.getDeclination()));
                final long lastFixTime = LocationState.getLastFixTime();
                if (lastFixTime > 0L) {
                    ((TextView)SatelliteActivity.this.findViewById(2131492981)).setText((CharSequence)UtilsFormat.formatTime(lastFixTime));
                }
                else {
                    ((TextView)SatelliteActivity.this.findViewById(2131492981)).setText((CharSequence)"~");
                }
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        this.onLocationChanged(LocationState.getLocation());
        this.onGpsStatusChanged(0, null);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        LocationState.addLocationChangeListener(this);
        if (this.buttonGps.isChecked() && !LocationState.isActuallyHardwareGpsOn()) {
            this.notifyGpsDisable();
        }
    }
    
    @Override
    public void onStatusChanged(final String s, final int n, final Bundle bundle) {
    }
    
    public void onStop() {
        super.onStop();
        LocationState.removeLocationChangeListener(this);
    }
}

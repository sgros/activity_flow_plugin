package menion.android.whereyougo.gui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.geo.orientation.IOrientationEventListener;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.activity.wherigo.DetailsActivity;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.view.CompassView;
import menion.android.whereyougo.guide.IGuide;
import menion.android.whereyougo.guide.IGuideEventListener;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.UtilsFormat;
import p005cz.matejcik.openwig.EventTable;
import p005cz.matejcik.openwig.Zone;

public class GuidingActivity extends CustomActivity implements IGuideEventListener, IOrientationEventListener, IRefreshable {
    private float azimuthToTarget;
    private float mAzimuth;
    private float mPitch;
    private float mRoll;
    private TextView viewAcc;
    private TextView viewAlt;
    private CompassView viewCompass;
    private TextView viewLat;
    private TextView viewLon;
    private TextView viewName;
    private TextView viewProvider;
    private TextView viewSpeed;
    private TextView viewTimeToTarget;

    /* renamed from: menion.android.whereyougo.gui.activity.GuidingActivity$1 */
    class C02631 implements Runnable {
        C02631() {
        }

        public void run() {
            EventTable et = DetailsActivity.f101et;
            if (et != null && et.isLocated() && et.isVisible() && C0322A.getGuidingContent() != null) {
                Location l;
                if (C0322A.getGuidingContent().getTargetLocation() == null) {
                    l = new Location();
                } else {
                    l = new Location(C0322A.getGuidingContent().getTargetLocation());
                }
                if (et instanceof Zone) {
                    Zone z = DetailsActivity.f101et;
                    if (Preferences.GUIDING_ZONE_NAVIGATION_POINT == 1) {
                        l.setLatitude(z.nearestPoint.latitude);
                        l.setLongitude(z.nearestPoint.longitude);
                    } else if (z.position != null) {
                        l.setLatitude(z.position.latitude);
                        l.setLongitude(z.position.longitude);
                    } else {
                        l.setLatitude(z.bbCenter.latitude);
                        l.setLongitude(z.bbCenter.longitude);
                    }
                } else {
                    l.setLatitude(et.position.latitude);
                    l.setLongitude(et.position.longitude);
                }
                C0322A.getGuidingContent().onLocationChanged(l);
            }
        }
    }

    public void guideStart() {
    }

    public void guideStop() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (C0322A.getMain() == null) {
            finish();
            return;
        }
        setContentView(C0254R.layout.layout_guiding_screen);
        this.mAzimuth = 0.0f;
        this.mPitch = 0.0f;
        this.mRoll = 0.0f;
        this.azimuthToTarget = 0.0f;
        this.viewCompass = new CompassView(this);
        ((LinearLayout) findViewById(C0254R.C0253id.linearLayoutCompass)).addView(this.viewCompass, -1, -1);
        this.viewName = (TextView) findViewById(C0254R.C0253id.textViewName);
        this.viewProvider = (TextView) findViewById(C0254R.C0253id.textViewProvider);
        this.viewAlt = (TextView) findViewById(C0254R.C0253id.textViewAltitude);
        this.viewSpeed = (TextView) findViewById(C0254R.C0253id.textViewSpeed);
        this.viewAcc = (TextView) findViewById(C0254R.C0253id.textViewAccuracy);
        this.viewLat = (TextView) findViewById(C0254R.C0253id.textViewLatitude);
        this.viewLon = (TextView) findViewById(C0254R.C0253id.textViewLongitude);
        this.viewTimeToTarget = (TextView) findViewById(C0254R.C0253id.text_view_time_to_target);
        onOrientationChanged(this.mAzimuth, this.mPitch, this.mRoll);
    }

    public void onOrientationChanged(float azimuth, float pitch, float roll) {
        Location loc = LocationState.getLocation();
        this.mAzimuth = azimuth;
        this.mPitch = pitch;
        this.mRoll = roll;
        String provider = loc.getProvider();
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
                provider = getString(C0254R.string.provider_gps);
                break;
            case true:
                provider = getString(C0254R.string.provider_network);
                break;
            default:
                provider = getString(C0254R.string.provider_passive);
                break;
        }
        this.viewProvider.setText(provider);
        this.viewLat.setText(UtilsFormat.formatLatitude(loc.getLatitude()));
        this.viewLon.setText(UtilsFormat.formatLongitude(loc.getLongitude()));
        this.viewAlt.setText(UtilsFormat.formatAltitude(loc.getAltitude(), true));
        this.viewAcc.setText(UtilsFormat.formatDistance((double) loc.getAccuracy(), false));
        this.viewSpeed.setText(UtilsFormat.formatSpeed((double) loc.getSpeed(), false));
        repaint();
    }

    public void onStart() {
        super.onStart();
        C0322A.getGuidingContent().addGuidingListener(this);
        C0322A.getRotator().addListener(this);
    }

    public void onStop() {
        super.onStop();
        C0322A.getGuidingContent().removeGuidingListener(this);
        C0322A.getRotator().removeListener(this);
    }

    public void receiveGuideEvent(IGuide guide, String targetName, float azimuthToTarget, double distanceToTarget) {
        this.viewName.setText(targetName);
        this.azimuthToTarget = azimuthToTarget;
        this.viewCompass.setDistance(distanceToTarget);
        if (LocationState.getLocation().getSpeed() > 1.0f) {
            this.viewTimeToTarget.setText(UtilsFormat.formatTime(true, ((long) (distanceToTarget / ((double) LocationState.getLocation().getSpeed()))) * 1000));
        } else {
            this.viewTimeToTarget.setText(UtilsFormat.formatTime(true, 0));
        }
        repaint();
    }

    public void refresh() {
        runOnUiThread(new C02631());
    }

    private void repaint() {
        this.viewCompass.moveAngles(this.azimuthToTarget, this.mAzimuth, this.mPitch, this.mRoll);
    }

    public void trackGuideCallRecalculate() {
    }
}

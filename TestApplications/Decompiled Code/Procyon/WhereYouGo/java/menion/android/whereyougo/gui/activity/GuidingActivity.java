// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity;

import cz.matejcik.openwig.EventTable;
import menion.android.whereyougo.preferences.Preferences;
import cz.matejcik.openwig.Zone;
import menion.android.whereyougo.gui.activity.wherigo.DetailsActivity;
import menion.android.whereyougo.guide.IGuide;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.utils.UtilsFormat;
import menion.android.whereyougo.geo.location.LocationState;
import android.view.View;
import android.widget.LinearLayout;
import android.content.Context;
import menion.android.whereyougo.utils.A;
import android.os.Bundle;
import menion.android.whereyougo.gui.view.CompassView;
import android.widget.TextView;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.geo.orientation.IOrientationEventListener;
import menion.android.whereyougo.guide.IGuideEventListener;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;

public class GuidingActivity extends CustomActivity implements IGuideEventListener, IOrientationEventListener, IRefreshable
{
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
    
    private void repaint() {
        this.viewCompass.moveAngles(this.azimuthToTarget, this.mAzimuth, this.mPitch, this.mRoll);
    }
    
    @Override
    public void guideStart() {
    }
    
    @Override
    public void guideStop() {
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (A.getMain() == null) {
            this.finish();
        }
        else {
            this.setContentView(2130903053);
            this.mAzimuth = 0.0f;
            this.mPitch = 0.0f;
            this.mRoll = 0.0f;
            this.azimuthToTarget = 0.0f;
            this.viewCompass = new CompassView((Context)this);
            ((LinearLayout)this.findViewById(2131492945)).addView((View)this.viewCompass, -1, -1);
            this.viewName = (TextView)this.findViewById(2131492951);
            this.viewProvider = (TextView)this.findViewById(2131492949);
            this.viewAlt = (TextView)this.findViewById(2131492953);
            this.viewSpeed = (TextView)this.findViewById(2131492954);
            this.viewAcc = (TextView)this.findViewById(2131492955);
            this.viewLat = (TextView)this.findViewById(2131492948);
            this.viewLon = (TextView)this.findViewById(2131492950);
            this.viewTimeToTarget = (TextView)this.findViewById(2131492956);
            this.onOrientationChanged(this.mAzimuth, this.mPitch, this.mRoll);
        }
    }
    
    @Override
    public void onOrientationChanged(final float mAzimuth, final float mPitch, final float mRoll) {
        final Location location = LocationState.getLocation();
        this.mAzimuth = mAzimuth;
        this.mPitch = mPitch;
        this.mRoll = mRoll;
        final String provider = location.getProvider();
        String text = null;
        switch (provider) {
            default: {
                text = this.getString(2131165395);
                break;
            }
            case "gps": {
                text = this.getString(2131165393);
                break;
            }
            case "network": {
                text = this.getString(2131165394);
                break;
            }
        }
        this.viewProvider.setText((CharSequence)text);
        this.viewLat.setText((CharSequence)UtilsFormat.formatLatitude(location.getLatitude()));
        this.viewLon.setText((CharSequence)UtilsFormat.formatLongitude(location.getLongitude()));
        this.viewAlt.setText((CharSequence)UtilsFormat.formatAltitude(location.getAltitude(), true));
        this.viewAcc.setText((CharSequence)UtilsFormat.formatDistance(location.getAccuracy(), false));
        this.viewSpeed.setText((CharSequence)UtilsFormat.formatSpeed(location.getSpeed(), false));
        this.repaint();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        A.getGuidingContent().addGuidingListener(this);
        A.getRotator().addListener(this);
    }
    
    public void onStop() {
        super.onStop();
        A.getGuidingContent().removeGuidingListener(this);
        A.getRotator().removeListener(this);
    }
    
    @Override
    public void receiveGuideEvent(final IGuide guide, final String text, final float azimuthToTarget, final double distance) {
        this.viewName.setText((CharSequence)text);
        this.azimuthToTarget = azimuthToTarget;
        this.viewCompass.setDistance(distance);
        if (LocationState.getLocation().getSpeed() > 1.0f) {
            this.viewTimeToTarget.setText((CharSequence)UtilsFormat.formatTime(true, (long)(distance / LocationState.getLocation().getSpeed()) * 1000L));
        }
        else {
            this.viewTimeToTarget.setText((CharSequence)UtilsFormat.formatTime(true, 0L));
        }
        this.repaint();
    }
    
    @Override
    public void refresh() {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final EventTable et = DetailsActivity.et;
                if (et != null && et.isLocated() && et.isVisible() && A.getGuidingContent() != null) {
                    Location location;
                    if (A.getGuidingContent().getTargetLocation() == null) {
                        location = new Location();
                    }
                    else {
                        location = new Location(A.getGuidingContent().getTargetLocation());
                    }
                    if (et instanceof Zone) {
                        final Zone zone = (Zone)DetailsActivity.et;
                        if (Preferences.GUIDING_ZONE_NAVIGATION_POINT == 1) {
                            location.setLatitude(zone.nearestPoint.latitude);
                            location.setLongitude(zone.nearestPoint.longitude);
                        }
                        else if (zone.position != null) {
                            location.setLatitude(zone.position.latitude);
                            location.setLongitude(zone.position.longitude);
                        }
                        else {
                            location.setLatitude(zone.bbCenter.latitude);
                            location.setLongitude(zone.bbCenter.longitude);
                        }
                    }
                    else {
                        location.setLatitude(et.position.latitude);
                        location.setLongitude(et.position.longitude);
                    }
                    A.getGuidingContent().onLocationChanged(location);
                }
            }
        });
    }
    
    @Override
    public void trackGuideCallRecalculate() {
    }
}

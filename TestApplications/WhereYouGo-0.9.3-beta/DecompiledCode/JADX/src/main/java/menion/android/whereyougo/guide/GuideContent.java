package menion.android.whereyougo.guide;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Iterator;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.geo.location.SatellitePosition;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.Logger;

public class GuideContent implements ILocationEventListener {
    private static final String TAG = "NavigationContent";
    private final ArrayList<IGuideEventListener> listeners = new ArrayList();
    private float mAzimuthToTarget;
    private float mDistanceToTarget;
    private IGuide mGuide;
    private Location mLocation;
    private String mTargetName;

    /* renamed from: menion.android.whereyougo.guide.GuideContent$1 */
    class C03031 implements Runnable {
        C03031() {
        }

        public void run() {
            while (GuideContent.this.mGuide != null) {
                try {
                    if (Preferences.GUIDING_SOUNDS) {
                        GuideContent.this.mGuide.manageDistanceSoundsBeeping((double) GuideContent.this.mDistanceToTarget);
                    }
                    Thread.sleep(100);
                } catch (Exception e) {
                    Logger.m22e(GuideContent.TAG, "guideStart(" + GuideContent.this.mGuide + ")", e);
                    return;
                }
            }
        }
    }

    public void addGuidingListener(IGuideEventListener listener) {
        this.listeners.add(listener);
        onLocationChanged(LocationState.getLocation());
    }

    public IGuide getGuide() {
        return this.mGuide;
    }

    public String getName() {
        return TAG;
    }

    public int getPriority() {
        return 3;
    }

    public Location getTargetLocation() {
        if (this.mGuide == null) {
            return null;
        }
        return this.mGuide.getTargetLocation();
    }

    public void guideStart(IGuide guide) {
        this.mGuide = guide;
        LocationState.addLocationChangeListener(this);
        onLocationChanged(LocationState.getLocation());
        new Thread(new C03031()).start();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((IGuideEventListener) it.next()).guideStart();
        }
    }

    public void guideStop() {
        this.mGuide = null;
        LocationState.removeLocationChangeListener(this);
        onLocationChanged(LocationState.getLocation());
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((IGuideEventListener) it.next()).guideStop();
        }
    }

    public boolean isGuiding() {
        return getTargetLocation() != null;
    }

    public boolean isRequired() {
        return Preferences.GUIDING_GPS_REQUIRED;
    }

    public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> arrayList) {
    }

    public void onLocationChanged(Location location) {
        if (this.mGuide == null || location == null) {
            this.mTargetName = null;
            this.mAzimuthToTarget = 0.0f;
            this.mDistanceToTarget = 0.0f;
        } else {
            this.mGuide.actualizeState(location);
            this.mTargetName = this.mGuide.getTargetName();
            this.mAzimuthToTarget = this.mGuide.getAzimuthToTaget();
            this.mDistanceToTarget = this.mGuide.getDistanceToTarget();
            this.mLocation = location;
        }
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((IGuideEventListener) it.next()).receiveGuideEvent(this.mGuide, this.mTargetName, this.mAzimuthToTarget, (double) this.mDistanceToTarget);
        }
    }

    public void onStatusChanged(String provider, int state, Bundle extra) {
    }

    public void removeGuidingListener(IGuideEventListener listener) {
        this.listeners.remove(listener);
    }

    /* Access modifiers changed, original: protected */
    public void trackGuideCallRecalculate() {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((IGuideEventListener) it.next()).trackGuideCallRecalculate();
        }
    }
}

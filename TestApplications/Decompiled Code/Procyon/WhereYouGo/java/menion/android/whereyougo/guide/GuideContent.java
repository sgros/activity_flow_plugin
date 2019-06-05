// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.guide;

import android.os.Bundle;
import menion.android.whereyougo.geo.location.SatellitePosition;
import java.util.Iterator;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.geo.location.Location;
import java.util.ArrayList;
import menion.android.whereyougo.geo.location.ILocationEventListener;

public class GuideContent implements ILocationEventListener
{
    private static final String TAG = "NavigationContent";
    private final ArrayList<IGuideEventListener> listeners;
    private float mAzimuthToTarget;
    private float mDistanceToTarget;
    private IGuide mGuide;
    private Location mLocation;
    private String mTargetName;
    
    public GuideContent() {
        this.listeners = new ArrayList<IGuideEventListener>();
    }
    
    public void addGuidingListener(final IGuideEventListener e) {
        this.listeners.add(e);
        this.onLocationChanged(LocationState.getLocation());
    }
    
    public IGuide getGuide() {
        return this.mGuide;
    }
    
    @Override
    public String getName() {
        return "NavigationContent";
    }
    
    @Override
    public int getPriority() {
        return 3;
    }
    
    public Location getTargetLocation() {
        Location targetLocation;
        if (this.mGuide == null) {
            targetLocation = null;
        }
        else {
            targetLocation = this.mGuide.getTargetLocation();
        }
        return targetLocation;
    }
    
    public void guideStart(final IGuide mGuide) {
        this.mGuide = mGuide;
        LocationState.addLocationChangeListener(this);
        this.onLocationChanged(LocationState.getLocation());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (GuideContent.this.mGuide != null) {
                        if (Preferences.GUIDING_SOUNDS) {
                            GuideContent.this.mGuide.manageDistanceSoundsBeeping(GuideContent.this.mDistanceToTarget);
                        }
                        Thread.sleep(100L);
                    }
                }
                catch (Exception ex) {
                    Logger.e("NavigationContent", "guideStart(" + GuideContent.this.mGuide + ")", ex);
                }
            }
        }).start();
        final Iterator<IGuideEventListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().guideStart();
        }
    }
    
    public void guideStop() {
        this.mGuide = null;
        LocationState.removeLocationChangeListener(this);
        this.onLocationChanged(LocationState.getLocation());
        final Iterator<IGuideEventListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().guideStop();
        }
    }
    
    public boolean isGuiding() {
        return this.getTargetLocation() != null;
    }
    
    @Override
    public boolean isRequired() {
        return Preferences.GUIDING_GPS_REQUIRED;
    }
    
    @Override
    public void onGpsStatusChanged(final int n, final ArrayList<SatellitePosition> list) {
    }
    
    @Override
    public void onLocationChanged(final Location mLocation) {
        if (this.mGuide != null && mLocation != null) {
            this.mGuide.actualizeState(mLocation);
            this.mTargetName = this.mGuide.getTargetName();
            this.mAzimuthToTarget = this.mGuide.getAzimuthToTaget();
            this.mDistanceToTarget = this.mGuide.getDistanceToTarget();
            this.mLocation = mLocation;
        }
        else {
            this.mTargetName = null;
            this.mAzimuthToTarget = 0.0f;
            this.mDistanceToTarget = 0.0f;
        }
        final Iterator<IGuideEventListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().receiveGuideEvent(this.mGuide, this.mTargetName, this.mAzimuthToTarget, this.mDistanceToTarget);
        }
    }
    
    @Override
    public void onStatusChanged(final String s, final int n, final Bundle bundle) {
    }
    
    public void removeGuidingListener(final IGuideEventListener o) {
        this.listeners.remove(o);
    }
    
    protected void trackGuideCallRecalculate() {
        final Iterator<IGuideEventListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().trackGuideCallRecalculate();
        }
    }
}

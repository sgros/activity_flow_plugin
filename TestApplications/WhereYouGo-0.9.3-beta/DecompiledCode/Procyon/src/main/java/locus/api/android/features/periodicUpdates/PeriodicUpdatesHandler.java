// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.periodicUpdates;

import locus.api.android.utils.LocusUtils;
import android.content.Intent;
import android.content.Context;
import locus.api.objects.extra.Location;

public class PeriodicUpdatesHandler
{
    private static final String TAG = "PeriodicUpdatesHandler";
    private static PeriodicUpdatesHandler mInstance;
    protected Location mLastGps;
    protected Location mLastMapCenter;
    protected int mLastZoomLevel;
    protected double mLocMinDistance;
    
    private PeriodicUpdatesHandler() {
        this.mLastZoomLevel = -1;
        this.mLocMinDistance = 1.0;
    }
    
    public static PeriodicUpdatesHandler getInstance() {
        Label_0024: {
            if (PeriodicUpdatesHandler.mInstance != null) {
                break Label_0024;
            }
            synchronized ("PeriodicUpdatesHandler") {
                PeriodicUpdatesHandler.mInstance = new PeriodicUpdatesHandler();
                return PeriodicUpdatesHandler.mInstance;
            }
        }
    }
    
    public void onReceive(final Context context, final Intent intent, final OnUpdate onUpdate) {
        if (onUpdate == null) {
            throw new IllegalArgumentException("Incorrect arguments");
        }
        if (context == null || intent == null) {
            onUpdate.onIncorrectData();
        }
        else {
            onUpdate.onUpdate(LocusUtils.createLocusVersion(context, intent), PeriodicUpdatesFiller.intentToUpdate(intent, this));
        }
    }
    
    public void setLocNotificationLimit(final double mLocMinDistance) {
        this.mLocMinDistance = mLocMinDistance;
    }
    
    public interface OnUpdate
    {
        void onIncorrectData();
        
        void onUpdate(final LocusUtils.LocusVersion p0, final UpdateContainer p1);
    }
}

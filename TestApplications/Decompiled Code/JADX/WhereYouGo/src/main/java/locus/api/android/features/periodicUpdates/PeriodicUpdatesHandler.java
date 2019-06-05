package locus.api.android.features.periodicUpdates;

import android.content.Context;
import android.content.Intent;
import locus.api.android.utils.LocusUtils;
import locus.api.android.utils.LocusUtils.LocusVersion;
import locus.api.objects.extra.Location;

public class PeriodicUpdatesHandler {
    private static final String TAG = "PeriodicUpdatesHandler";
    private static PeriodicUpdatesHandler mInstance;
    protected Location mLastGps;
    protected Location mLastMapCenter;
    protected int mLastZoomLevel = -1;
    protected double mLocMinDistance = 1.0d;

    public interface OnUpdate {
        void onIncorrectData();

        void onUpdate(LocusVersion locusVersion, UpdateContainer updateContainer);
    }

    public static PeriodicUpdatesHandler getInstance() {
        if (mInstance == null) {
            synchronized (TAG) {
                mInstance = new PeriodicUpdatesHandler();
            }
        }
        return mInstance;
    }

    private PeriodicUpdatesHandler() {
    }

    public void setLocNotificationLimit(double locMinDistance) {
        this.mLocMinDistance = locMinDistance;
    }

    public void onReceive(Context ctx, Intent intent, OnUpdate handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Incorrect arguments");
        } else if (ctx == null || intent == null) {
            handler.onIncorrectData();
        } else {
            handler.onUpdate(LocusUtils.createLocusVersion(ctx, intent), PeriodicUpdatesFiller.intentToUpdate(intent, this));
        }
    }
}

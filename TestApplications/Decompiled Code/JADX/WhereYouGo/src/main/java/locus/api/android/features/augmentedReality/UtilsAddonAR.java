package locus.api.android.features.augmentedReality;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import locus.api.android.ActionDisplay;
import locus.api.android.objects.PackWaypoints;
import locus.api.android.utils.LocusConst;
import locus.api.android.utils.LocusUtils;
import locus.api.objects.Storable;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Track;
import locus.api.utils.Logger;

public class UtilsAddonAR {
    private static final String BROADCAST_DATA = "locus.api.android.addon.ar.NEW_DATA";
    private static final String EXTRA_GUIDING_ID = "EXTRA_GUIDING_ID";
    private static final String EXTRA_LOCATION = "EXTRA_LOCATION";
    private static final String INTENT_VIEW = "locus.api.android.addon.ar.ACTION_VIEW";
    private static final int REQUEST_ADDON_AR = 30001;
    public static final int REQUIRED_VERSION = 11;
    public static final String RESULT_WPT_ID = "RESULT_WPT_ID";
    private static final String TAG = "UtilsAddonAR";
    private static Location mLastLocation;

    public static boolean isInstalled(Context context) {
        return LocusUtils.isAppAvailable(context, "menion.android.locus.addon.ar", 11);
    }

    public static boolean showPoints(Activity act, List<PackWaypoints> data, Location yourLoc, long guidedWptId) {
        if (isInstalled(act)) {
            Intent intent = new Intent(INTENT_VIEW);
            intent.putExtra(LocusConst.INTENT_EXTRA_POINTS_DATA_ARRAY, Storable.getAsBytes(data));
            intent.putExtra(EXTRA_LOCATION, yourLoc.getAsBytes());
            intent.putExtra(EXTRA_GUIDING_ID, guidedWptId);
            if (ActionDisplay.hasData(intent)) {
                mLastLocation = yourLoc;
                act.startActivityForResult(intent, REQUEST_ADDON_AR);
                return true;
            }
            Logger.logW(TAG, "Intent 'null' or not contain any data");
            return false;
        }
        Logger.logW(TAG, "missing required version 11");
        return false;
    }

    public static void updateLocation(Context context, Location loc) {
        long timeDiff = loc.getTime() - mLastLocation.getTime();
        double distDiff = (double) loc.distanceTo(mLastLocation);
        double altDiff = Math.abs(loc.getAltitude() - mLastLocation.getAltitude());
        if (timeDiff < 5000) {
            return;
        }
        if (distDiff >= 5.0d || altDiff >= 10.0d) {
            mLastLocation = loc;
            Intent intent = new Intent(BROADCAST_DATA);
            intent.putExtra(EXTRA_LOCATION, mLastLocation.getAsBytes());
            context.sendBroadcast(intent);
        }
    }

    public static void showTracks(Context context, List<Track> list) throws NoSuchAlgorithmException {
        throw new NoSuchAlgorithmException("Not yet implemented");
    }
}

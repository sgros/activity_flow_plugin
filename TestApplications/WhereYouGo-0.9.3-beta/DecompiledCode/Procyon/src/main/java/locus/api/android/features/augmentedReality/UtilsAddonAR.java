// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.augmentedReality;

import java.security.NoSuchAlgorithmException;
import locus.api.objects.extra.Track;
import locus.api.android.ActionDisplay;
import locus.api.objects.Storable;
import android.content.Intent;
import locus.api.utils.Logger;
import locus.api.android.objects.PackWaypoints;
import java.util.List;
import android.app.Activity;
import locus.api.android.utils.LocusUtils;
import android.content.Context;
import locus.api.objects.extra.Location;

public class UtilsAddonAR
{
    private static final String BROADCAST_DATA = "locus.api.android.addon.ar.NEW_DATA";
    private static final String EXTRA_GUIDING_ID = "EXTRA_GUIDING_ID";
    private static final String EXTRA_LOCATION = "EXTRA_LOCATION";
    private static final String INTENT_VIEW = "locus.api.android.addon.ar.ACTION_VIEW";
    private static final int REQUEST_ADDON_AR = 30001;
    public static final int REQUIRED_VERSION = 11;
    public static final String RESULT_WPT_ID = "RESULT_WPT_ID";
    private static final String TAG = "UtilsAddonAR";
    private static Location mLastLocation;
    
    public static boolean isInstalled(final Context context) {
        return LocusUtils.isAppAvailable(context, "menion.android.locus.addon.ar", 11);
    }
    
    public static boolean showPoints(final Activity activity, final List<PackWaypoints> list, final Location mLastLocation, final long n) {
        boolean b = false;
        if (!isInstalled((Context)activity)) {
            Logger.logW("UtilsAddonAR", "missing required version 11");
        }
        else {
            final Intent intent = new Intent("locus.api.android.addon.ar.ACTION_VIEW");
            intent.putExtra("INTENT_EXTRA_POINTS_DATA_ARRAY", Storable.getAsBytes(list));
            intent.putExtra("EXTRA_LOCATION", mLastLocation.getAsBytes());
            intent.putExtra("EXTRA_GUIDING_ID", n);
            if (!ActionDisplay.hasData(intent)) {
                Logger.logW("UtilsAddonAR", "Intent 'null' or not contain any data");
            }
            else {
                UtilsAddonAR.mLastLocation = mLastLocation;
                activity.startActivityForResult(intent, 30001);
                b = true;
            }
        }
        return b;
    }
    
    public static void showTracks(final Context context, final List<Track> list) throws NoSuchAlgorithmException {
        throw new NoSuchAlgorithmException("Not yet implemented");
    }
    
    public static void updateLocation(final Context context, final Location mLastLocation) {
        final long time = mLastLocation.getTime();
        final long time2 = UtilsAddonAR.mLastLocation.getTime();
        final double n = mLastLocation.distanceTo(UtilsAddonAR.mLastLocation);
        final double abs = Math.abs(mLastLocation.getAltitude() - UtilsAddonAR.mLastLocation.getAltitude());
        if (time - time2 >= 5000L && (n >= 5.0 || abs >= 10.0)) {
            UtilsAddonAR.mLastLocation = mLastLocation;
            final Intent intent = new Intent("locus.api.android.addon.ar.NEW_DATA");
            intent.putExtra("EXTRA_LOCATION", UtilsAddonAR.mLastLocation.getAsBytes());
            context.sendBroadcast(intent);
        }
    }
}

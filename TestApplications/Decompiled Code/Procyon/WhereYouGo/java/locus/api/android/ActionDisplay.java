// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android;

import locus.api.utils.Logger;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.android.utils.LocusUtils;
import android.content.Context;
import android.content.Intent;

public class ActionDisplay
{
    private static final String TAG = "ActionDisplay";
    
    public static boolean hasData(final Intent intent) {
        boolean b = false;
        if (intent != null && (intent.getByteArrayExtra("INTENT_EXTRA_POINTS_DATA") != null || intent.getByteArrayExtra("INTENT_EXTRA_POINTS_DATA_ARRAY") != null || intent.getStringExtra("INTENT_EXTRA_POINTS_FILE_PATH") != null || intent.getByteArrayExtra("INTENT_EXTRA_TRACKS_SINGLE") != null || intent.getByteArrayExtra("INTENT_EXTRA_TRACKS_MULTI") != null || intent.getByteArrayExtra("INTENT_EXTRA_CIRCLES_MULTI") != null)) {
            b = true;
        }
        return b;
    }
    
    protected static boolean removeSpecialDataSilently(final Context context, final LocusUtils.LocusVersion locusVersion, final String s, final long[] array) throws RequiredVersionMissingException {
        if (!locusVersion.isVersionValid(LocusUtils.VersionCode.UPDATE_02)) {
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_02);
        }
        boolean b;
        if (array == null || array.length == 0) {
            Logger.logW("ActionDisplay", "Intent 'null' or not contain any data");
            b = false;
        }
        else {
            final Intent intent = new Intent("locus.api.android.ACTION_REMOVE_DATA_SILENTLY");
            intent.setPackage(locusVersion.getPackageName());
            intent.putExtra(s, array);
            context.sendBroadcast(intent);
            b = true;
        }
        return b;
    }
    
    protected static boolean sendData(final String s, final Context context, final Intent intent, final boolean b, final boolean b2) throws RequiredVersionMissingException {
        return sendData(s, context, intent, b, b2, LocusUtils.VersionCode.UPDATE_01);
    }
    
    protected static boolean sendData(final String action, final Context context, final Intent intent, final boolean b, final boolean b2, final LocusUtils.VersionCode versionCode) throws RequiredVersionMissingException {
        final boolean b3 = false;
        if (!LocusUtils.isLocusAvailable(context, versionCode.vcFree, versionCode.vcPro, 0)) {
            throw new RequiredVersionMissingException(versionCode.vcFree);
        }
        boolean b4;
        if (!hasData(intent)) {
            Logger.logW("ActionDisplay", "Intent 'null' or not contain any data");
            b4 = b3;
        }
        else {
            intent.setAction(action);
            intent.putExtra("INTENT_EXTRA_CENTER_ON_DATA", b2);
            if (action.equals("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY")) {
                context.sendBroadcast(intent);
            }
            else {
                intent.putExtra("INTENT_EXTRA_CALL_IMPORT", b);
                context.startActivity(intent);
            }
            b4 = true;
        }
        return b4;
    }
    
    public enum ExtraAction
    {
        CENTER, 
        IMPORT, 
        NONE;
    }
}

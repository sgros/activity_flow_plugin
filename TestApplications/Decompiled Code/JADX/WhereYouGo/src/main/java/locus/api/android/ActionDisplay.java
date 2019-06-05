package locus.api.android;

import android.content.Context;
import android.content.Intent;
import locus.api.android.utils.LocusConst;
import locus.api.android.utils.LocusUtils;
import locus.api.android.utils.LocusUtils.LocusVersion;
import locus.api.android.utils.LocusUtils.VersionCode;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.utils.Logger;

public class ActionDisplay {
    private static final String TAG = "ActionDisplay";

    public enum ExtraAction {
        NONE,
        CENTER,
        IMPORT
    }

    protected static boolean sendData(String action, Context context, Intent intent, boolean callImport, boolean center) throws RequiredVersionMissingException {
        return sendData(action, context, intent, callImport, center, VersionCode.UPDATE_01);
    }

    protected static boolean sendData(String action, Context context, Intent intent, boolean callImport, boolean center, VersionCode vc) throws RequiredVersionMissingException {
        if (!LocusUtils.isLocusAvailable(context, vc.vcFree, vc.vcPro, 0)) {
            throw new RequiredVersionMissingException(vc.vcFree);
        } else if (hasData(intent)) {
            intent.setAction(action);
            intent.putExtra(LocusConst.INTENT_EXTRA_CENTER_ON_DATA, center);
            if (action.equals(LocusConst.ACTION_DISPLAY_DATA_SILENTLY)) {
                context.sendBroadcast(intent);
            } else {
                intent.putExtra(LocusConst.INTENT_EXTRA_CALL_IMPORT, callImport);
                context.startActivity(intent);
            }
            return true;
        } else {
            Logger.logW(TAG, "Intent 'null' or not contain any data");
            return false;
        }
    }

    public static boolean hasData(Intent intent) {
        if (intent == null) {
            return false;
        }
        if (intent.getByteArrayExtra(LocusConst.INTENT_EXTRA_POINTS_DATA) == null && intent.getByteArrayExtra(LocusConst.INTENT_EXTRA_POINTS_DATA_ARRAY) == null && intent.getStringExtra(LocusConst.INTENT_EXTRA_POINTS_FILE_PATH) == null && intent.getByteArrayExtra(LocusConst.INTENT_EXTRA_TRACKS_SINGLE) == null && intent.getByteArrayExtra(LocusConst.INTENT_EXTRA_TRACKS_MULTI) == null && intent.getByteArrayExtra(LocusConst.INTENT_EXTRA_CIRCLES_MULTI) == null) {
            return false;
        }
        return true;
    }

    protected static boolean removeSpecialDataSilently(Context ctx, LocusVersion lv, String extraName, long[] itemsId) throws RequiredVersionMissingException {
        if (!lv.isVersionValid(VersionCode.UPDATE_02)) {
            throw new RequiredVersionMissingException(VersionCode.UPDATE_02);
        } else if (itemsId == null || itemsId.length == 0) {
            Logger.logW(TAG, "Intent 'null' or not contain any data");
            return false;
        } else {
            Intent intent = new Intent(LocusConst.ACTION_REMOVE_DATA_SILENTLY);
            intent.setPackage(lv.getPackageName());
            intent.putExtra(extraName, itemsId);
            ctx.sendBroadcast(intent);
            return true;
        }
    }
}

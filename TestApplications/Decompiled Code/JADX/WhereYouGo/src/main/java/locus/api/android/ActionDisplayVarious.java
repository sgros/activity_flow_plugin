package locus.api.android;

import android.content.Context;
import android.content.Intent;
import java.util.List;
import locus.api.android.utils.LocusConst;
import locus.api.android.utils.LocusUtils.LocusVersion;
import locus.api.android.utils.LocusUtils.VersionCode;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.Storable;
import locus.api.objects.extra.Circle;

public class ActionDisplayVarious extends ActionDisplay {
    public static boolean sendCirclesSilent(Context context, List<Circle> circles, boolean centerOnData) throws RequiredVersionMissingException {
        return sendCirclesSilent(LocusConst.ACTION_DISPLAY_DATA_SILENTLY, context, circles, false, centerOnData);
    }

    private static boolean sendCirclesSilent(String action, Context context, List<Circle> circles, boolean callImport, boolean centerOnData) throws RequiredVersionMissingException {
        if (circles == null || circles.size() == 0) {
            return false;
        }
        Intent intent = new Intent();
        intent.putExtra(LocusConst.INTENT_EXTRA_CIRCLES_MULTI, Storable.getAsBytes(circles));
        return ActionDisplay.sendData(action, context, intent, callImport, centerOnData, VersionCode.UPDATE_02);
    }

    public static void removeCirclesSilent(Context ctx, LocusVersion lv, long[] itemsId) throws RequiredVersionMissingException {
        ActionDisplay.removeSpecialDataSilently(ctx, lv, LocusConst.INTENT_EXTRA_CIRCLES_MULTI, itemsId);
    }
}

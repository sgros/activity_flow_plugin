// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android;

import locus.api.objects.Storable;
import android.content.Intent;
import locus.api.objects.extra.Circle;
import java.util.List;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.android.utils.LocusUtils;
import android.content.Context;

public class ActionDisplayVarious extends ActionDisplay
{
    public static void removeCirclesSilent(final Context context, final LocusUtils.LocusVersion locusVersion, final long[] array) throws RequiredVersionMissingException {
        ActionDisplay.removeSpecialDataSilently(context, locusVersion, "INTENT_EXTRA_CIRCLES_MULTI", array);
    }
    
    public static boolean sendCirclesSilent(final Context context, final List<Circle> list, final boolean b) throws RequiredVersionMissingException {
        return sendCirclesSilent("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY", context, list, false, b);
    }
    
    private static boolean sendCirclesSilent(final String s, final Context context, final List<Circle> list, final boolean b, final boolean b2) throws RequiredVersionMissingException {
        boolean sendData;
        if (list == null || list.size() == 0) {
            sendData = false;
        }
        else {
            final Intent intent = new Intent();
            intent.putExtra("INTENT_EXTRA_CIRCLES_MULTI", Storable.getAsBytes(list));
            sendData = ActionDisplay.sendData(s, context, intent, b, b2, LocusUtils.VersionCode.UPDATE_02);
        }
        return sendData;
    }
}

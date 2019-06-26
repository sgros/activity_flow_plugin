// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android;

import locus.api.objects.Storable;
import java.util.List;
import android.content.Intent;
import locus.api.utils.Logger;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.extra.Track;
import android.content.Context;

public class ActionDisplayTracks extends ActionDisplay
{
    private static final String TAG;
    
    static {
        TAG = ActionDisplayTracks.class.getSimpleName();
    }
    
    public static boolean sendTrack(final Context context, final Track track, final ExtraAction extraAction) throws RequiredVersionMissingException {
        return sendTrack("locus.api.android.ACTION_DISPLAY_DATA", context, track, extraAction == ExtraAction.IMPORT, extraAction == ExtraAction.CENTER, false);
    }
    
    public static boolean sendTrack(final Context context, final Track track, final ExtraAction extraAction, final boolean b) throws RequiredVersionMissingException {
        return sendTrack("locus.api.android.ACTION_DISPLAY_DATA", context, track, extraAction == ExtraAction.IMPORT, extraAction == ExtraAction.CENTER, b);
    }
    
    private static boolean sendTrack(final String str, final Context obj, final Track obj2, final boolean b, final boolean b2, final boolean b3) throws RequiredVersionMissingException {
        boolean sendData;
        if (obj2 == null || obj2.getPoints().size() == 0) {
            Logger.logE(ActionDisplayTracks.TAG, "sendTrack(" + str + ", " + obj + ", " + obj2 + ", " + b + ", " + b2 + ", " + b3 + "), " + "track is null or contain no points");
            sendData = false;
        }
        else {
            final Intent intent = new Intent();
            intent.putExtra("INTENT_EXTRA_TRACKS_SINGLE", obj2.getAsBytes());
            intent.putExtra("INTENT_EXTRA_START_NAVIGATION", b3);
            sendData = ActionDisplay.sendData(str, obj, intent, b, b2);
        }
        return sendData;
    }
    
    public static boolean sendTrackSilent(final Context context, final Track track, final boolean b) throws RequiredVersionMissingException {
        return sendTrack("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY", context, track, false, b, false);
    }
    
    public static boolean sendTracks(final Context context, final List<Track> list, final ExtraAction extraAction) throws RequiredVersionMissingException {
        boolean b = true;
        final boolean b2 = extraAction == ExtraAction.IMPORT;
        if (extraAction != ExtraAction.CENTER) {
            b = false;
        }
        return sendTracks("locus.api.android.ACTION_DISPLAY_DATA", context, list, b2, b);
    }
    
    private static boolean sendTracks(final String s, final Context context, final List<Track> list, final boolean b, final boolean b2) throws RequiredVersionMissingException {
        boolean sendData;
        if (list == null || list.size() == 0) {
            sendData = false;
        }
        else {
            final Intent intent = new Intent();
            intent.putExtra("INTENT_EXTRA_TRACKS_MULTI", Storable.getAsBytes(list));
            sendData = ActionDisplay.sendData(s, context, intent, b, b2);
        }
        return sendData;
    }
    
    public static boolean sendTracksSilent(final Context context, final List<Track> list, final boolean b) throws RequiredVersionMissingException {
        return sendTracks("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY", context, list, false, b);
    }
}

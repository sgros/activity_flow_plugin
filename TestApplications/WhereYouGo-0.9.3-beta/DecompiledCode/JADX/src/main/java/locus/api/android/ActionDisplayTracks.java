package locus.api.android;

import android.content.Context;
import android.content.Intent;
import java.util.List;
import locus.api.android.ActionDisplay.ExtraAction;
import locus.api.android.utils.LocusConst;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.Storable;
import locus.api.objects.extra.Track;
import locus.api.utils.Logger;

public class ActionDisplayTracks extends ActionDisplay {
    private static final String TAG = ActionDisplayTracks.class.getSimpleName();

    public static boolean sendTrack(Context context, Track track, ExtraAction extraAction) throws RequiredVersionMissingException {
        return sendTrack(LocusConst.ACTION_DISPLAY_DATA, context, track, extraAction == ExtraAction.IMPORT, extraAction == ExtraAction.CENTER, false);
    }

    public static boolean sendTrack(Context context, Track track, ExtraAction extraAction, boolean startNavigation) throws RequiredVersionMissingException {
        return sendTrack(LocusConst.ACTION_DISPLAY_DATA, context, track, extraAction == ExtraAction.IMPORT, extraAction == ExtraAction.CENTER, startNavigation);
    }

    public static boolean sendTrackSilent(Context context, Track track, boolean centerOnData) throws RequiredVersionMissingException {
        return sendTrack(LocusConst.ACTION_DISPLAY_DATA_SILENTLY, context, track, false, centerOnData, false);
    }

    private static boolean sendTrack(String action, Context context, Track track, boolean callImport, boolean centerOnData, boolean startNavigation) throws RequiredVersionMissingException {
        if (track == null || track.getPoints().size() == 0) {
            Logger.logE(TAG, "sendTrack(" + action + ", " + context + ", " + track + ", " + callImport + ", " + centerOnData + ", " + startNavigation + "), " + "track is null or contain no points");
            return false;
        }
        Intent intent = new Intent();
        intent.putExtra(LocusConst.INTENT_EXTRA_TRACKS_SINGLE, track.getAsBytes());
        intent.putExtra(LocusConst.INTENT_EXTRA_START_NAVIGATION, startNavigation);
        return ActionDisplay.sendData(action, context, intent, callImport, centerOnData);
    }

    public static boolean sendTracks(Context context, List<Track> tracks, ExtraAction extraAction) throws RequiredVersionMissingException {
        boolean z = true;
        String str = LocusConst.ACTION_DISPLAY_DATA;
        boolean z2 = extraAction == ExtraAction.IMPORT;
        if (extraAction != ExtraAction.CENTER) {
            z = false;
        }
        return sendTracks(str, context, tracks, z2, z);
    }

    public static boolean sendTracksSilent(Context context, List<Track> tracks, boolean centerOnData) throws RequiredVersionMissingException {
        return sendTracks(LocusConst.ACTION_DISPLAY_DATA_SILENTLY, context, tracks, false, centerOnData);
    }

    private static boolean sendTracks(String action, Context context, List<Track> tracks, boolean callImport, boolean centerOnData) throws RequiredVersionMissingException {
        if (tracks == null || tracks.size() == 0) {
            return false;
        }
        Intent intent = new Intent();
        intent.putExtra(LocusConst.INTENT_EXTRA_TRACKS_MULTI, Storable.getAsBytes(tracks));
        return ActionDisplay.sendData(action, context, intent, callImport, centerOnData);
    }
}

package menion.android.whereyougo.maps.utils;

import android.app.Activity;
import android.content.Intent;
import locus.api.android.ActionDisplay.ExtraAction;
import locus.api.android.ActionDisplayPoints;
import locus.api.android.ActionDisplayTracks;
import locus.api.android.ActionTools;
import locus.api.android.utils.LocusUtils;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.extra.Waypoint;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.utils.UtilsWherigo;
import menion.android.whereyougo.maps.mapsforge.MapsforgeActivity;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.Logger;
import p005cz.matejcik.openwig.EventTable;

public class MapHelper {
    public static MapDataProvider getMapDataProvider() {
        switch (Preferences.GLOBAL_MAP_PROVIDER) {
            case 0:
                return VectorMapDataProvider.getInstance();
            case 1:
                return LocusMapDataProvider.getInstance();
            default:
                return VectorMapDataProvider.getInstance();
        }
    }

    public static void showMap(Activity activity) {
        showMap(activity, null);
    }

    public static void showMap(Activity activity, EventTable waypoint) {
        switch (Preferences.GLOBAL_MAP_PROVIDER) {
            case 0:
                vectorMap(activity, waypoint);
                return;
            case 1:
                locusMap(activity, waypoint);
                return;
            default:
                return;
        }
    }

    public static void vectorMap(Activity activity, EventTable et) {
        boolean navigate = et != null && et.isLocated();
        Intent intent = new Intent(activity, MapsforgeActivity.class);
        intent.addFlags(131072);
        intent.putExtra(MapsforgeActivity.BUNDLE_CENTER, navigate);
        intent.putExtra(MapsforgeActivity.BUNDLE_NAVIGATE, navigate);
        intent.putExtra(MapsforgeActivity.BUNDLE_ALLOW_START_CARTRIDGE, activity instanceof MainActivity);
        activity.startActivity(intent);
    }

    public static void locusMap(Activity activity, EventTable et) {
        LocusMapDataProvider mdp = LocusMapDataProvider.getInstance();
        try {
            ActionDisplayPoints.sendPack(activity, mdp.getPoints(), ExtraAction.NONE);
            ActionDisplayTracks.sendTracks(activity, mdp.getTracks(), ExtraAction.CENTER);
            if (et != null && et.isLocated()) {
                ActionTools.actionStartGuiding(activity, new Waypoint(et.name, UtilsWherigo.extractLocation(et)));
            }
        } catch (RequiredVersionMissingException e) {
            Logger.m22e(activity.toString(), "MapHelper.showMap() - missing locus version", e);
            LocusUtils.callInstallLocus(activity);
        } catch (Exception e2) {
            Logger.m22e(activity.toString(), "MapHelper.showMap() - unknown locus problem", e2);
        }
    }
}

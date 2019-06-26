// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.utils;

import menion.android.whereyougo.gui.activity.MainActivity;
import android.content.Intent;
import menion.android.whereyougo.maps.mapsforge.MapsforgeActivity;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.android.utils.LocusUtils;
import menion.android.whereyougo.utils.Logger;
import locus.api.android.ActionTools;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Waypoint;
import menion.android.whereyougo.gui.utils.UtilsWherigo;
import locus.api.objects.extra.Track;
import java.util.List;
import locus.api.android.ActionDisplayTracks;
import android.content.Context;
import locus.api.android.ActionDisplayPoints;
import locus.api.android.ActionDisplay;
import cz.matejcik.openwig.EventTable;
import android.app.Activity;
import menion.android.whereyougo.preferences.Preferences;

public class MapHelper
{
    public static MapDataProvider getMapDataProvider() {
        MapDataProvider mapDataProvider = null;
        switch (Preferences.GLOBAL_MAP_PROVIDER) {
            default: {
                mapDataProvider = VectorMapDataProvider.getInstance();
                break;
            }
            case 0: {
                mapDataProvider = VectorMapDataProvider.getInstance();
                break;
            }
            case 1: {
                mapDataProvider = LocusMapDataProvider.getInstance();
                break;
            }
        }
        return mapDataProvider;
    }
    
    public static void locusMap(final Activity activity, final EventTable eventTable) {
        final LocusMapDataProvider instance = LocusMapDataProvider.getInstance();
        try {
            ActionDisplayPoints.sendPack((Context)activity, instance.getPoints(), ActionDisplay.ExtraAction.NONE);
            ActionDisplayTracks.sendTracks((Context)activity, instance.getTracks(), ActionDisplay.ExtraAction.CENTER);
            if (eventTable != null && eventTable.isLocated()) {
                ActionTools.actionStartGuiding(activity, new Waypoint(eventTable.name, UtilsWherigo.extractLocation(eventTable)));
            }
        }
        catch (RequiredVersionMissingException ex) {
            Logger.e(activity.toString(), "MapHelper.showMap() - missing locus version", ex);
            LocusUtils.callInstallLocus((Context)activity);
        }
        catch (Exception ex2) {
            Logger.e(activity.toString(), "MapHelper.showMap() - unknown locus problem", ex2);
        }
    }
    
    public static void showMap(final Activity activity) {
        showMap(activity, null);
    }
    
    public static void showMap(final Activity activity, final EventTable eventTable) {
        switch (Preferences.GLOBAL_MAP_PROVIDER) {
            case 0: {
                vectorMap(activity, eventTable);
                break;
            }
            case 1: {
                locusMap(activity, eventTable);
                break;
            }
        }
    }
    
    public static void vectorMap(final Activity activity, final EventTable eventTable) {
        final boolean b = eventTable != null && eventTable.isLocated();
        final Intent intent = new Intent((Context)activity, (Class)MapsforgeActivity.class);
        intent.addFlags(131072);
        intent.putExtra("center", b);
        intent.putExtra("navigate", b);
        intent.putExtra("allowStartCartridge", activity instanceof MainActivity);
        activity.startActivity(intent);
    }
}

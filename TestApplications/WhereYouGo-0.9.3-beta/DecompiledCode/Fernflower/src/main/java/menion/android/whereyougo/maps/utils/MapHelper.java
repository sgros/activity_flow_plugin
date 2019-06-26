package menion.android.whereyougo.maps.utils;

import android.app.Activity;
import android.content.Intent;
import cz.matejcik.openwig.EventTable;
import locus.api.android.ActionDisplay;
import locus.api.android.ActionDisplayPoints;
import locus.api.android.ActionDisplayTracks;
import locus.api.android.ActionTools;
import locus.api.android.utils.LocusUtils;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.extra.Waypoint;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.utils.UtilsWherigo;
import menion.android.whereyougo.maps.mapsforge.MapsforgeActivity;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.Logger;

public class MapHelper {
   public static MapDataProvider getMapDataProvider() {
      Object var0;
      switch(Preferences.GLOBAL_MAP_PROVIDER) {
      case 0:
         var0 = VectorMapDataProvider.getInstance();
         break;
      case 1:
         var0 = LocusMapDataProvider.getInstance();
         break;
      default:
         var0 = VectorMapDataProvider.getInstance();
      }

      return (MapDataProvider)var0;
   }

   public static void locusMap(Activity var0, EventTable var1) {
      LocusMapDataProvider var2 = LocusMapDataProvider.getInstance();

      RequiredVersionMissingException var11;
      label46: {
         Exception var10000;
         label36: {
            boolean var10001;
            try {
               ActionDisplayPoints.sendPack(var0, var2.getPoints(), ActionDisplay.ExtraAction.NONE);
               ActionDisplayTracks.sendTracks(var0, var2.getTracks(), ActionDisplay.ExtraAction.CENTER);
            } catch (RequiredVersionMissingException var6) {
               var11 = var6;
               var10001 = false;
               break label46;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label36;
            }

            if (var1 == null) {
               return;
            }

            try {
               if (var1.isLocated()) {
                  Location var3 = UtilsWherigo.extractLocation(var1);
                  Waypoint var10 = new Waypoint(var1.name, var3);
                  ActionTools.actionStartGuiding(var0, var10);
               }

               return;
            } catch (RequiredVersionMissingException var4) {
               var11 = var4;
               var10001 = false;
               break label46;
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
            }
         }

         Exception var8 = var10000;
         Logger.e(var0.toString(), "MapHelper.showMap() - unknown locus problem", var8);
         return;
      }

      RequiredVersionMissingException var9 = var11;
      Logger.e(var0.toString(), "MapHelper.showMap() - missing locus version", (Exception)var9);
      LocusUtils.callInstallLocus(var0);
   }

   public static void showMap(Activity var0) {
      showMap(var0, (EventTable)null);
   }

   public static void showMap(Activity var0, EventTable var1) {
      switch(Preferences.GLOBAL_MAP_PROVIDER) {
      case 0:
         vectorMap(var0, var1);
         break;
      case 1:
         locusMap(var0, var1);
      }

   }

   public static void vectorMap(Activity var0, EventTable var1) {
      boolean var2;
      if (var1 != null && var1.isLocated()) {
         var2 = true;
      } else {
         var2 = false;
      }

      Intent var3 = new Intent(var0, MapsforgeActivity.class);
      var3.addFlags(131072);
      var3.putExtra("center", var2);
      var3.putExtra("navigate", var2);
      var3.putExtra("allowStartCartridge", var0 instanceof MainActivity);
      var0.startActivity(var3);
   }
}

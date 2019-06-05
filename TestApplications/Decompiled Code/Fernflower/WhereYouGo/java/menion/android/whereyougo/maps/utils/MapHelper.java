package menion.android.whereyougo.maps.utils;

import android.app.Activity;
import android.content.Intent;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Zone;
import locus.api.android.ActionDisplay;
import locus.api.android.ActionDisplayPoints;
import locus.api.android.ActionDisplayTracks;
import locus.api.android.ActionTools;
import locus.api.android.utils.LocusUtils;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Waypoint;
import menion.android.whereyougo.gui.activity.MainActivity;
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

      RequiredVersionMissingException var16;
      label73: {
         Exception var10000;
         label59: {
            boolean var10001;
            try {
               ActionDisplayPoints.sendPack(var0, var2.getPoints(), ActionDisplay.ExtraAction.NONE);
               ActionDisplayTracks.sendTracks(var0, var2.getTracks(), ActionDisplay.ExtraAction.CENTER);
            } catch (RequiredVersionMissingException var10) {
               var16 = var10;
               var10001 = false;
               break label73;
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label59;
            }

            if (var1 == null) {
               return;
            }

            Location var14;
            label60: {
               try {
                  if (!var1.isLocated()) {
                     return;
                  }

                  var14 = new Location(var0.toString());
                  if (var1 instanceof Zone) {
                     Zone var3 = (Zone)var1;
                     var14.setLatitude(var3.nearestPoint.latitude);
                     var14.setLongitude(var3.nearestPoint.longitude);
                     break label60;
                  }
               } catch (RequiredVersionMissingException var8) {
                  var16 = var8;
                  var10001 = false;
                  break label73;
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label59;
               }

               try {
                  var14.setLatitude(var1.position.latitude);
                  var14.setLongitude(var1.position.longitude);
               } catch (RequiredVersionMissingException var6) {
                  var16 = var6;
                  var10001 = false;
                  break label73;
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label59;
               }
            }

            try {
               Waypoint var15 = new Waypoint(var1.name, var14);
               ActionTools.actionStartGuiding(var0, var15);
               return;
            } catch (RequiredVersionMissingException var4) {
               var16 = var4;
               var10001 = false;
               break label73;
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
            }
         }

         Exception var12 = var10000;
         Logger.e(var0.toString(), "MapHelper.showMap() - unknown locus problem", var12);
         return;
      }

      RequiredVersionMissingException var13 = var16;
      Logger.e(var0.toString(), "MapHelper.showMap() - missing locus version", (Exception)var13);
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

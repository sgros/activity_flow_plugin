package menion.android.whereyougo.gui.utils;

import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Zone;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.preferences.Preferences;

public class UtilsWherigo {
   public static Location extractLocation(EventTable var0) {
      Location var3;
      if (var0 != null && var0.isLocated()) {
         Location var1 = new Location();
         if (var0 instanceof Zone) {
            Zone var2 = (Zone)var0;
            if (Preferences.GUIDING_ZONE_NAVIGATION_POINT == 1) {
               var1.setLatitude(var2.nearestPoint.latitude);
               var1.setLongitude(var2.nearestPoint.longitude);
               var3 = var1;
            } else if (var0.position != null) {
               var1.setLatitude(var2.position.latitude);
               var1.setLongitude(var2.position.longitude);
               var3 = var1;
            } else {
               var1.setLatitude(var2.bbCenter.latitude);
               var1.setLongitude(var2.bbCenter.longitude);
               var3 = var1;
            }
         } else {
            var1.setLatitude(var0.position.latitude);
            var1.setLongitude(var0.position.longitude);
            var3 = var1;
         }
      } else {
         var3 = null;
      }

      return var3;
   }
}

package menion.android.whereyougo.maps.utils;

import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Zone;
import java.util.Vector;

public interface MapDataProvider {
   void addAll();

   void addCartridges(Vector var1);

   void addOther(EventTable var1, boolean var2);

   void addZone(Zone var1, boolean var2);

   void addZones(Vector var1);

   void addZones(Vector var1, EventTable var2);

   void clear();
}

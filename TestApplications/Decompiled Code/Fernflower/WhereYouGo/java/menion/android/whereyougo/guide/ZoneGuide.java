package menion.android.whereyougo.guide;

import cz.matejcik.openwig.Zone;
import menion.android.whereyougo.geo.location.Location;

public class ZoneGuide extends Guide {
   private boolean mAlreadyEntered = false;
   private final Zone mZone;

   public ZoneGuide(Zone var1) {
      super(var1.name, new Location("Guidance: " + var1.name, var1.bbCenter.latitude, var1.bbCenter.longitude));
      this.mZone = var1;
      this.mAlreadyEntered = false;
   }
}

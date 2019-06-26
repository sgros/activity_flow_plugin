package menion.android.whereyougo.openwig;

import cz.matejcik.openwig.platform.LocationService;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.utils.Logger;

public class WLocationService implements LocationService {
   private static final String TAG = "WLocationService";

   public void connect() {
      Logger.w("WLocationService", "connect()");
   }

   public void disconnect() {
      Logger.w("WLocationService", "disconnect()");
   }

   public double getAltitude() {
      return LocationState.getLocation().getAltitude();
   }

   public double getHeading() {
      return (double)LocationState.getLocation().getBearing();
   }

   public double getLatitude() {
      return LocationState.getLocation().getLatitude();
   }

   public double getLongitude() {
      return LocationState.getLocation().getLongitude();
   }

   public double getPrecision() {
      return (double)LocationState.getLocation().getAccuracy();
   }

   public int getState() {
      byte var1;
      if (LocationState.isActuallyHardwareGpsOn()) {
         var1 = 3;
      } else {
         var1 = 0;
      }

      return var1;
   }
}

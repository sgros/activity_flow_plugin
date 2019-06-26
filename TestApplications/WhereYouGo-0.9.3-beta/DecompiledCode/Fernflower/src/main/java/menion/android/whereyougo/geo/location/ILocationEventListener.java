package menion.android.whereyougo.geo.location;

import android.os.Bundle;
import java.util.ArrayList;

public interface ILocationEventListener {
   int PRIORITY_HIGH = 3;
   int PRIORITY_LOW = 1;
   int PRIORITY_MEDIUM = 2;

   String getName();

   int getPriority();

   boolean isRequired();

   void onGpsStatusChanged(int var1, ArrayList var2);

   void onLocationChanged(Location var1);

   void onStatusChanged(String var1, int var2, Bundle var3);
}

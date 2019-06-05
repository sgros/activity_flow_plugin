package menion.android.whereyougo.gui.activity.wherigo;

import android.os.Bundle;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Zone;
import java.util.ArrayList;
import java.util.Vector;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.activity.MainActivity;

public class ListZonesActivity extends ListVariousActivity implements ILocationEventListener {
   private static final String TAG = "ListZones";

   protected void callStuff(Object var1) {
      MainActivity.wui.showScreen(1, (Zone)var1);
      this.finish();
   }

   public String getName() {
      return "ListZones";
   }

   public int getPriority() {
      return 2;
   }

   protected String getStuffName(Object var1) {
      return ((Zone)var1).name;
   }

   protected Vector getValidStuff() {
      Vector var1 = new Vector();
      Vector var2 = Engine.instance.cartridge.zones;

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         if (((Zone)var2.get(var3)).isVisible()) {
            var1.add(var2.get(var3));
         }
      }

      return var1;
   }

   public boolean isRequired() {
      return false;
   }

   public void onGpsStatusChanged(int var1, ArrayList var2) {
   }

   public void onLocationChanged(Location var1) {
      this.refresh();
   }

   public void onStart() {
      super.onStart();
      LocationState.addLocationChangeListener(this);
   }

   public void onStatusChanged(String var1, int var2, Bundle var3) {
   }

   public void onStop() {
      super.onStop();
      LocationState.removeLocationChangeListener(this);
   }

   protected boolean stillValid() {
      return true;
   }
}

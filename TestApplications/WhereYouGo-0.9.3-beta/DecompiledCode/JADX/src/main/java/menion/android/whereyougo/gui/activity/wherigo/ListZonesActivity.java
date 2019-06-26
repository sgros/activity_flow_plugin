package menion.android.whereyougo.gui.activity.wherigo;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Vector;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.geo.location.SatellitePosition;
import menion.android.whereyougo.gui.activity.MainActivity;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.Zone;

public class ListZonesActivity extends ListVariousActivity implements ILocationEventListener {
    private static final String TAG = "ListZones";

    /* Access modifiers changed, original: protected */
    public void callStuff(Object what) {
        MainActivity.wui.showScreen(1, (Zone) what);
        finish();
    }

    public String getName() {
        return TAG;
    }

    public int getPriority() {
        return 2;
    }

    /* Access modifiers changed, original: protected */
    public String getStuffName(Object what) {
        return ((Zone) what).name;
    }

    /* Access modifiers changed, original: protected */
    public Vector<Object> getValidStuff() {
        Vector<Object> ret = new Vector();
        Vector<Zone> v = Engine.instance.cartridge.zones;
        for (int i = 0; i < v.size(); i++) {
            if (((Zone) v.get(i)).isVisible()) {
                ret.add(v.get(i));
            }
        }
        return ret;
    }

    public boolean isRequired() {
        return false;
    }

    public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> arrayList) {
    }

    public void onLocationChanged(Location location) {
        refresh();
    }

    public void onStart() {
        super.onStart();
        LocationState.addLocationChangeListener(this);
    }

    public void onStatusChanged(String provider, int state, Bundle extras) {
    }

    public void onStop() {
        super.onStop();
        LocationState.removeLocationChangeListener(this);
    }

    /* Access modifiers changed, original: protected */
    public boolean stillValid() {
        return true;
    }
}

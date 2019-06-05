// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity.wherigo;

import android.os.Bundle;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.SatellitePosition;
import java.util.ArrayList;
import cz.matejcik.openwig.Engine;
import java.util.Vector;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Zone;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.geo.location.ILocationEventListener;

public class ListZonesActivity extends ListVariousActivity implements ILocationEventListener
{
    private static final String TAG = "ListZones";
    
    @Override
    protected void callStuff(final Object o) {
        MainActivity.wui.showScreen(1, (EventTable)o);
        this.finish();
    }
    
    @Override
    public String getName() {
        return "ListZones";
    }
    
    @Override
    public int getPriority() {
        return 2;
    }
    
    @Override
    protected String getStuffName(final Object o) {
        return ((Zone)o).name;
    }
    
    @Override
    protected Vector<Object> getValidStuff() {
        final Vector<Zone> vector = (Vector<Zone>)new Vector<Object>();
        final Vector zones = Engine.instance.cartridge.zones;
        for (int i = 0; i < zones.size(); ++i) {
            if (zones.get(i).isVisible()) {
                vector.add(zones.get(i));
            }
        }
        return (Vector<Object>)vector;
    }
    
    @Override
    public boolean isRequired() {
        return false;
    }
    
    @Override
    public void onGpsStatusChanged(final int n, final ArrayList<SatellitePosition> list) {
    }
    
    @Override
    public void onLocationChanged(final Location location) {
        this.refresh();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        LocationState.addLocationChangeListener(this);
    }
    
    @Override
    public void onStatusChanged(final String s, final int n, final Bundle bundle) {
    }
    
    public void onStop() {
        super.onStop();
        LocationState.removeLocationChangeListener(this);
    }
    
    @Override
    protected boolean stillValid() {
        return true;
    }
}

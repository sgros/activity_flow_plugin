// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.utils;

import cz.matejcik.openwig.Zone;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.formats.CartridgeFile;
import java.util.Vector;

public interface MapDataProvider
{
    void addAll();
    
    void addCartridges(final Vector<CartridgeFile> p0);
    
    void addOther(final EventTable p0, final boolean p1);
    
    void addZone(final Zone p0, final boolean p1);
    
    void addZones(final Vector<Zone> p0);
    
    void addZones(final Vector<Zone> p0, final EventTable p1);
    
    void clear();
}

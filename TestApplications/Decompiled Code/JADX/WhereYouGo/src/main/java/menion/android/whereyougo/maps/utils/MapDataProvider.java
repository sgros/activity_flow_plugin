package menion.android.whereyougo.maps.utils;

import java.util.Vector;
import p005cz.matejcik.openwig.EventTable;
import p005cz.matejcik.openwig.Zone;
import p005cz.matejcik.openwig.formats.CartridgeFile;

public interface MapDataProvider {
    void addAll();

    void addCartridges(Vector<CartridgeFile> vector);

    void addOther(EventTable eventTable, boolean z);

    void addZone(Zone zone, boolean z);

    void addZones(Vector<Zone> vector);

    void addZones(Vector<Zone> vector, EventTable eventTable);

    void clear();
}

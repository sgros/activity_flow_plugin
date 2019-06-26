package menion.android.whereyougo.maps.utils;

import android.graphics.BitmapFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.activity.wherigo.DetailsActivity;
import menion.android.whereyougo.gui.utils.UtilsWherigo;
import menion.android.whereyougo.maps.container.MapPoint;
import menion.android.whereyougo.maps.container.MapPointPack;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.EventTable;
import p005cz.matejcik.openwig.Zone;
import p005cz.matejcik.openwig.formats.CartridgeFile;

public class VectorMapDataProvider implements MapDataProvider {
    private static VectorMapDataProvider instance = null;
    private ArrayList<MapPointPack> items;

    private VectorMapDataProvider() {
        this.items = null;
        this.items = new ArrayList();
    }

    public static VectorMapDataProvider getInstance() {
        if (instance == null) {
            instance = new VectorMapDataProvider();
        }
        return instance;
    }

    public void addAll() {
        if (MainActivity.cartridgeFile != null && Engine.instance != null && Engine.instance.cartridge != null && Engine.instance.cartridge.zones != null) {
            clear();
            Vector<CartridgeFile> v = new Vector();
            v.add(MainActivity.cartridgeFile);
            addCartridges(v);
            addZones(Engine.instance.cartridge.zones, DetailsActivity.f101et);
            if (DetailsActivity.f101et != null && !(DetailsActivity.f101et instanceof Zone)) {
                addOther(DetailsActivity.f101et, true);
            }
        }
    }

    public void addCartridges(Vector<CartridgeFile> cartridges) {
        if (cartridges != null) {
            MapPointPack pack = new MapPointPack(false, (int) C0254R.C0252drawable.marker_wherigo);
            Iterator it = cartridges.iterator();
            while (it.hasNext()) {
                CartridgeFile cartridge = (CartridgeFile) it.next();
                if (cartridge.latitude % 360.0d != 0.0d || cartridge.longitude % 360.0d != 0.0d) {
                    MapPoint pt = new MapPoint(cartridge.name, cartridge.description, cartridge.latitude, cartridge.longitude);
                    pt.setData(new File(cartridge.filename).getName());
                    try {
                        byte[] iconData = cartridge.getFile(cartridge.iconId);
                        MapPointPack iconPack = new MapPointPack(false, BitmapFactory.decodeByteArray(iconData, 0, iconData.length));
                        iconPack.getPoints().add(pt);
                        this.items.add(iconPack);
                    } catch (Exception e) {
                        pack.getPoints().add(pt);
                    }
                }
            }
            this.items.add(pack);
        }
    }

    public void addOther(EventTable et, boolean mark) {
        if (et != null && et.isLocated() && et.isVisible()) {
            MapPointPack pack = new MapPointPack();
            pack.getPoints().add(new MapPoint(et.name, et.position.latitude, et.position.longitude, mark));
            if (mark) {
                pack.setResource(C0254R.C0252drawable.marker_green);
            } else {
                pack.setResource(C0254R.C0252drawable.marker_red);
            }
            this.items.add(pack);
        }
    }

    public void addZone(Zone z, boolean mark) {
        if (z != null && z.isLocated() && z.isVisible()) {
            MapPointPack border = new MapPointPack();
            border.setPolygon(true);
            for (int i = 0; i < z.points.length; i++) {
                border.getPoints().add(new MapPoint("", z.points[i].latitude, z.points[i].longitude));
            }
            if (border.getPoints().size() >= 3) {
                border.getPoints().add(border.getPoints().get(0));
            }
            this.items.add(border);
            MapPointPack pack = new MapPointPack();
            Location location = UtilsWherigo.extractLocation(z);
            pack.getPoints().add(new MapPoint(z.name, z.description, location.getLatitude(), location.getLongitude(), mark));
            if (mark) {
                pack.setResource(C0254R.C0252drawable.marker_green);
            } else {
                pack.setResource(C0254R.C0252drawable.marker_red);
            }
            this.items.add(pack);
        }
    }

    public void addZones(Vector<Zone> zones) {
        addZones(zones, null);
    }

    public void addZones(Vector<Zone> zones, EventTable mark) {
        if (zones != null) {
            Iterator it = zones.iterator();
            while (it.hasNext()) {
                EventTable z = (Zone) it.next();
                addZone(z, z == mark);
            }
        }
    }

    public void clear() {
        this.items.clear();
    }

    public ArrayList<MapPointPack> getItems() {
        return this.items;
    }
}

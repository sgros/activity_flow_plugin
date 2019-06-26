// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.utils;

import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.gui.utils.UtilsWherigo;
import cz.matejcik.openwig.EventTable;
import java.util.Iterator;
import android.graphics.BitmapFactory;
import java.io.File;
import menion.android.whereyougo.maps.container.MapPoint;
import cz.matejcik.openwig.Zone;
import menion.android.whereyougo.gui.activity.wherigo.DetailsActivity;
import cz.matejcik.openwig.formats.CartridgeFile;
import java.util.Vector;
import cz.matejcik.openwig.Engine;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.maps.container.MapPointPack;
import java.util.ArrayList;

public class VectorMapDataProvider implements MapDataProvider
{
    private static VectorMapDataProvider instance;
    private ArrayList<MapPointPack> items;
    
    static {
        VectorMapDataProvider.instance = null;
    }
    
    private VectorMapDataProvider() {
        this.items = null;
        this.items = new ArrayList<MapPointPack>();
    }
    
    public static VectorMapDataProvider getInstance() {
        if (VectorMapDataProvider.instance == null) {
            VectorMapDataProvider.instance = new VectorMapDataProvider();
        }
        return VectorMapDataProvider.instance;
    }
    
    @Override
    public void addAll() {
        if (MainActivity.cartridgeFile != null && Engine.instance != null && Engine.instance.cartridge != null && Engine.instance.cartridge.zones != null) {
            this.clear();
            final Vector<CartridgeFile> vector = new Vector<CartridgeFile>();
            vector.add(MainActivity.cartridgeFile);
            this.addCartridges(vector);
            this.addZones(Engine.instance.cartridge.zones, DetailsActivity.et);
            if (DetailsActivity.et != null && !(DetailsActivity.et instanceof Zone)) {
                this.addOther(DetailsActivity.et, true);
            }
        }
    }
    
    @Override
    public void addCartridges(Vector<CartridgeFile> mapPoint) {
        if (mapPoint != null) {
            final MapPointPack e = new MapPointPack(false, 2130837561);
            for (final CartridgeFile cartridgeFile : mapPoint) {
                if (cartridgeFile.latitude % 360.0 != 0.0 || cartridgeFile.longitude % 360.0 != 0.0) {
                    mapPoint = new MapPoint(cartridgeFile.name, cartridgeFile.description, cartridgeFile.latitude, cartridgeFile.longitude);
                    mapPoint.setData(new File(cartridgeFile.filename).getName());
                    try {
                        final byte[] file = cartridgeFile.getFile(cartridgeFile.iconId);
                        final MapPointPack e2 = new MapPointPack(false, BitmapFactory.decodeByteArray(file, 0, file.length));
                        e2.getPoints().add(mapPoint);
                        this.items.add(e2);
                    }
                    catch (Exception ex) {
                        e.getPoints().add(mapPoint);
                    }
                }
            }
            this.items.add(e);
        }
    }
    
    @Override
    public void addOther(final EventTable eventTable, final boolean b) {
        if (eventTable != null && eventTable.isLocated() && eventTable.isVisible()) {
            final MapPointPack e = new MapPointPack();
            e.getPoints().add(new MapPoint(eventTable.name, eventTable.position.latitude, eventTable.position.longitude, b));
            if (b) {
                e.setResource(2130837559);
            }
            else {
                e.setResource(2130837560);
            }
            this.items.add(e);
        }
    }
    
    @Override
    public void addZone(final Zone zone, final boolean b) {
        if (zone != null && zone.isLocated() && zone.isVisible()) {
            final MapPointPack e = new MapPointPack();
            e.setPolygon(true);
            for (int i = 0; i < zone.points.length; ++i) {
                e.getPoints().add(new MapPoint("", zone.points[i].latitude, zone.points[i].longitude));
            }
            if (e.getPoints().size() >= 3) {
                e.getPoints().add(e.getPoints().get(0));
            }
            this.items.add(e);
            final MapPointPack e2 = new MapPointPack();
            final Location location = UtilsWherigo.extractLocation(zone);
            e2.getPoints().add(new MapPoint(zone.name, zone.description, location.getLatitude(), location.getLongitude(), b));
            if (b) {
                e2.setResource(2130837559);
            }
            else {
                e2.setResource(2130837560);
            }
            this.items.add(e2);
        }
    }
    
    @Override
    public void addZones(final Vector<Zone> vector) {
        this.addZones(vector, null);
    }
    
    @Override
    public void addZones(final Vector<Zone> vector, final EventTable eventTable) {
        if (vector != null) {
            for (final Zone zone : vector) {
                this.addZone(zone, zone == eventTable);
            }
        }
    }
    
    @Override
    public void clear() {
        this.items.clear();
    }
    
    public ArrayList<MapPointPack> getItems() {
        return this.items;
    }
}

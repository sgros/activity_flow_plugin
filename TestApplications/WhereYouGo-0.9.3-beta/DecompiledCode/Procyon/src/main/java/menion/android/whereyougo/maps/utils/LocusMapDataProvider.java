// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.utils;

import java.util.List;
import locus.api.objects.extra.ExtraStyle;
import menion.android.whereyougo.gui.utils.UtilsWherigo;
import cz.matejcik.openwig.EventTable;
import java.util.Iterator;
import locus.api.objects.extra.Waypoint;
import locus.api.objects.extra.Location;
import cz.matejcik.openwig.Zone;
import menion.android.whereyougo.gui.activity.wherigo.DetailsActivity;
import cz.matejcik.openwig.Engine;
import menion.android.whereyougo.gui.activity.MainActivity;
import cz.matejcik.openwig.formats.CartridgeFile;
import java.util.Vector;
import locus.api.objects.extra.Track;
import java.util.ArrayList;
import locus.api.android.objects.PackWaypoints;

public class LocusMapDataProvider implements MapDataProvider
{
    private static LocusMapDataProvider instance;
    private final PackWaypoints pack;
    private ArrayList<Track> tracks;
    
    static {
        LocusMapDataProvider.instance = null;
    }
    
    private LocusMapDataProvider() {
        this.tracks = null;
        this.tracks = new ArrayList<Track>();
        this.pack = new PackWaypoints("WhereYouGo");
    }
    
    public static LocusMapDataProvider getInstance() {
        if (LocusMapDataProvider.instance == null) {
            LocusMapDataProvider.instance = new LocusMapDataProvider();
        }
        return LocusMapDataProvider.instance;
    }
    
    @Override
    public void addAll() {
        final Vector<CartridgeFile> vector = new Vector<CartridgeFile>();
        vector.add(MainActivity.cartridgeFile);
        this.addCartridges(vector);
        this.addZones(Engine.instance.cartridge.zones, DetailsActivity.et);
        if (DetailsActivity.et != null && !(DetailsActivity.et instanceof Zone)) {
            this.addOther(DetailsActivity.et, true);
        }
    }
    
    @Override
    public void addCartridges(final Vector<CartridgeFile> vector) {
        if (vector != null) {
            for (final CartridgeFile cartridgeFile : vector) {
                if (cartridgeFile.latitude % 360.0 != 0.0 || cartridgeFile.longitude % 360.0 != 0.0) {
                    final Location location = new Location("WhereYouGo");
                    location.setLatitude(cartridgeFile.latitude);
                    location.setLongitude(cartridgeFile.longitude);
                    final Waypoint waypoint = new Waypoint(cartridgeFile.name, location);
                    waypoint.addParameter(30, cartridgeFile.description);
                    waypoint.addUrl(cartridgeFile.url);
                    this.pack.addWaypoint(waypoint);
                }
            }
        }
    }
    
    @Override
    public void addOther(final EventTable eventTable, final boolean b) {
        if (eventTable != null && eventTable.isLocated() && eventTable.isVisible()) {
            this.pack.addWaypoint(new Waypoint(eventTable.name, UtilsWherigo.extractLocation(eventTable)));
        }
    }
    
    @Override
    public void addZone(final Zone zone, final boolean b) {
        if (zone != null && zone.isLocated() && zone.isVisible()) {
            final ArrayList<Location> points = new ArrayList<Location>();
            for (int i = 0; i < zone.points.length; ++i) {
                final Location e = new Location("");
                e.setLatitude(zone.points[i].latitude);
                e.setLongitude(zone.points[i].longitude);
                points.add(e);
            }
            if (points.size() >= 3) {
                points.add(points.get(0));
            }
            final Track e2 = new Track();
            final ExtraStyle styleNormal = new ExtraStyle();
            styleNormal.setLineStyle(ExtraStyle.LineStyle.ColorStyle.SIMPLE, -65281, 2.0f, ExtraStyle.LineStyle.Units.PIXELS);
            e2.styleNormal = styleNormal;
            e2.setPoints(points);
            e2.setName(zone.name);
            this.tracks.add(e2);
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
        this.tracks.clear();
        this.pack.reset();
    }
    
    public PackWaypoints getPoints() {
        return this.pack;
    }
    
    public ArrayList<Track> getTracks() {
        return this.tracks;
    }
}

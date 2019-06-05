package menion.android.whereyougo.maps.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import locus.api.android.objects.PackWaypoints;
import locus.api.objects.extra.ExtraStyle;
import locus.api.objects.extra.ExtraStyle.LineStyle.ColorStyle;
import locus.api.objects.extra.ExtraStyle.LineStyle.Units;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Track;
import locus.api.objects.extra.Waypoint;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.activity.wherigo.DetailsActivity;
import menion.android.whereyougo.preferences.Preferences;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.EventTable;
import p005cz.matejcik.openwig.Zone;
import p005cz.matejcik.openwig.formats.CartridgeFile;

public class LocusMapDataProvider implements MapDataProvider {
    private static LocusMapDataProvider instance = null;
    private final PackWaypoints pack;
    private ArrayList<Track> tracks;

    private LocusMapDataProvider() {
        this.tracks = null;
        this.tracks = new ArrayList();
        this.pack = new PackWaypoints(MainApplication.APP_NAME);
    }

    public static LocusMapDataProvider getInstance() {
        if (instance == null) {
            instance = new LocusMapDataProvider();
        }
        return instance;
    }

    public static Waypoint locusMapWaypoint(EventTable et) {
        if (et == null || !et.isLocated() || !et.isVisible()) {
            return null;
        }
        Location loc = new Location();
        if (et instanceof Zone) {
            Zone z = (Zone) et;
            loc.setLatitude(z.nearestPoint.latitude);
            loc.setLongitude(z.nearestPoint.longitude);
        } else {
            loc.setLatitude(et.position.latitude);
            loc.setLongitude(et.position.longitude);
        }
        return new Waypoint(et.name, loc);
    }

    public void addAll() {
        Vector<CartridgeFile> v = new Vector();
        v.add(MainActivity.cartridgeFile);
        addCartridges(v);
        addZones(Engine.instance.cartridge.zones, DetailsActivity.f101et);
        if (DetailsActivity.f101et != null && !(DetailsActivity.f101et instanceof Zone)) {
            addOther(DetailsActivity.f101et, true);
        }
    }

    public void addCartridges(Vector<CartridgeFile> cartridges) {
        if (cartridges != null) {
            Iterator it = cartridges.iterator();
            while (it.hasNext()) {
                CartridgeFile cartridge = (CartridgeFile) it.next();
                if (cartridge.latitude % 360.0d != 0.0d || cartridge.longitude % 360.0d != 0.0d) {
                    Location loc = new Location(MainApplication.APP_NAME);
                    loc.setLatitude(cartridge.latitude);
                    loc.setLongitude(cartridge.longitude);
                    Waypoint wpt = new Waypoint(cartridge.name, loc);
                    wpt.addParameter(30, cartridge.description);
                    wpt.addUrl(cartridge.url);
                    this.pack.addWaypoint(wpt);
                }
            }
        }
    }

    public void addOther(EventTable et, boolean mark) {
        if (et != null && et.isLocated() && et.isVisible()) {
            Location loc = new Location("");
            if ((et instanceof Zone) && Preferences.GUIDING_ZONE_NAVIGATION_POINT == 1) {
                Zone z = (Zone) et;
                loc.setLatitude(z.nearestPoint.latitude);
                loc.setLongitude(z.nearestPoint.longitude);
            } else {
                loc.setLatitude(et.position.latitude);
                loc.setLongitude(et.position.longitude);
            }
            this.pack.addWaypoint(new Waypoint(et.name, loc));
        }
    }

    public void addZone(Zone z, boolean mark) {
        if (z != null && z.isLocated() && z.isVisible()) {
            ArrayList<Location> locs = new ArrayList();
            for (int i = 0; i < z.points.length; i++) {
                Location loc = new Location("");
                loc.setLatitude(z.points[i].latitude);
                loc.setLongitude(z.points[i].longitude);
                locs.add(loc);
            }
            if (locs.size() >= 3) {
                locs.add(locs.get(0));
            }
            Track track = new Track();
            ExtraStyle style = new ExtraStyle();
            style.setLineStyle(ColorStyle.SIMPLE, -65281, 2.0f, Units.PIXELS);
            track.styleNormal = style;
            track.setPoints(locs);
            track.setName(z.name);
            this.tracks.add(track);
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

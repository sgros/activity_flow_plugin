package menion.android.whereyougo.maps.utils;

import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Zone;
import cz.matejcik.openwig.formats.CartridgeFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import locus.api.android.objects.PackWaypoints;
import locus.api.objects.extra.ExtraStyle;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Track;
import locus.api.objects.extra.Waypoint;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.activity.wherigo.DetailsActivity;
import menion.android.whereyougo.gui.utils.UtilsWherigo;

public class LocusMapDataProvider implements MapDataProvider {
   private static LocusMapDataProvider instance = null;
   private final PackWaypoints pack;
   private ArrayList tracks = null;

   private LocusMapDataProvider() {
      this.tracks = new ArrayList();
      this.pack = new PackWaypoints("WhereYouGo");
   }

   public static LocusMapDataProvider getInstance() {
      if (instance == null) {
         instance = new LocusMapDataProvider();
      }

      return instance;
   }

   public void addAll() {
      Vector var1 = new Vector();
      var1.add(MainActivity.cartridgeFile);
      this.addCartridges(var1);
      this.addZones(Engine.instance.cartridge.zones, DetailsActivity.et);
      if (DetailsActivity.et != null && !(DetailsActivity.et instanceof Zone)) {
         this.addOther(DetailsActivity.et, true);
      }

   }

   public void addCartridges(Vector var1) {
      if (var1 != null) {
         Iterator var4 = var1.iterator();

         while(true) {
            CartridgeFile var2;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               var2 = (CartridgeFile)var4.next();
            } while(var2.latitude % 360.0D == 0.0D && var2.longitude % 360.0D == 0.0D);

            Location var3 = new Location("WhereYouGo");
            var3.setLatitude(var2.latitude);
            var3.setLongitude(var2.longitude);
            Waypoint var5 = new Waypoint(var2.name, var3);
            var5.addParameter(30, var2.description);
            var5.addUrl(var2.url);
            this.pack.addWaypoint(var5);
         }
      }
   }

   public void addOther(EventTable var1, boolean var2) {
      if (var1 != null && var1.isLocated() && var1.isVisible()) {
         menion.android.whereyougo.geo.location.Location var3 = UtilsWherigo.extractLocation(var1);
         this.pack.addWaypoint(new Waypoint(var1.name, var3));
      }

   }

   public void addZone(Zone var1, boolean var2) {
      if (var1 != null && var1.isLocated() && var1.isVisible()) {
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var1.points.length; ++var4) {
            Location var5 = new Location("");
            var5.setLatitude(var1.points[var4].latitude);
            var5.setLongitude(var1.points[var4].longitude);
            var3.add(var5);
         }

         if (var3.size() >= 3) {
            var3.add(var3.get(0));
         }

         Track var7 = new Track();
         ExtraStyle var6 = new ExtraStyle();
         var6.setLineStyle(ExtraStyle.LineStyle.ColorStyle.SIMPLE, -65281, 2.0F, ExtraStyle.LineStyle.Units.PIXELS);
         var7.styleNormal = var6;
         var7.setPoints(var3);
         var7.setName(var1.name);
         this.tracks.add(var7);
      }

   }

   public void addZones(Vector var1) {
      this.addZones(var1, (EventTable)null);
   }

   public void addZones(Vector var1, EventTable var2) {
      Zone var3;
      boolean var4;
      if (var1 != null) {
         for(Iterator var5 = var1.iterator(); var5.hasNext(); this.addZone(var3, var4)) {
            var3 = (Zone)var5.next();
            if (var3 == var2) {
               var4 = true;
            } else {
               var4 = false;
            }
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

   public ArrayList getTracks() {
      return this.tracks;
   }
}

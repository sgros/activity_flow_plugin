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
import menion.android.whereyougo.preferences.Preferences;

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

   public static Waypoint locusMapWaypoint(EventTable var0) {
      Waypoint var3;
      if (var0 != null && var0.isLocated() && var0.isVisible()) {
         Location var1 = new Location();
         if (var0 instanceof Zone) {
            Zone var2 = (Zone)var0;
            var1.setLatitude(var2.nearestPoint.latitude);
            var1.setLongitude(var2.nearestPoint.longitude);
         } else {
            var1.setLatitude(var0.position.latitude);
            var1.setLongitude(var0.position.longitude);
         }

         var3 = new Waypoint(var0.name, var1);
      } else {
         var3 = null;
      }

      return var3;
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
         Iterator var2 = var1.iterator();

         while(true) {
            CartridgeFile var4;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var4 = (CartridgeFile)var2.next();
            } while(var4.latitude % 360.0D == 0.0D && var4.longitude % 360.0D == 0.0D);

            Location var3 = new Location("WhereYouGo");
            var3.setLatitude(var4.latitude);
            var3.setLongitude(var4.longitude);
            Waypoint var5 = new Waypoint(var4.name, var3);
            var5.addParameter(30, var4.description);
            var5.addUrl(var4.url);
            this.pack.addWaypoint(var5);
         }
      }
   }

   public void addOther(EventTable var1, boolean var2) {
      if (var1 != null && var1.isLocated() && var1.isVisible()) {
         Location var3 = new Location("");
         if (var1 instanceof Zone && Preferences.GUIDING_ZONE_NAVIGATION_POINT == 1) {
            Zone var4 = (Zone)var1;
            var3.setLatitude(var4.nearestPoint.latitude);
            var3.setLongitude(var4.nearestPoint.longitude);
         } else {
            var3.setLatitude(var1.position.latitude);
            var3.setLongitude(var1.position.longitude);
         }

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
      boolean var4;
      Zone var5;
      if (var1 != null) {
         for(Iterator var3 = var1.iterator(); var3.hasNext(); this.addZone(var5, var4)) {
            var5 = (Zone)var3.next();
            if (var5 == var2) {
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

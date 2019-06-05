package menion.android.whereyougo.maps.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Zone;
import cz.matejcik.openwig.formats.CartridgeFile;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.activity.wherigo.DetailsActivity;
import menion.android.whereyougo.maps.container.MapPoint;
import menion.android.whereyougo.maps.container.MapPointPack;
import menion.android.whereyougo.preferences.Preferences;

public class VectorMapDataProvider implements MapDataProvider {
   private static VectorMapDataProvider instance = null;
   private ArrayList items = null;

   private VectorMapDataProvider() {
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
         this.clear();
         Vector var1 = new Vector();
         var1.add(MainActivity.cartridgeFile);
         this.addCartridges(var1);
         this.addZones(Engine.instance.cartridge.zones, DetailsActivity.et);
         if (DetailsActivity.et != null && !(DetailsActivity.et instanceof Zone)) {
            this.addOther(DetailsActivity.et, true);
         }
      }

   }

   public void addCartridges(Vector var1) {
      if (var1 != null) {
         MapPointPack var2 = new MapPointPack(false, 2130837561);
         Iterator var7 = var1.iterator();

         while(true) {
            CartridgeFile var3;
            do {
               if (!var7.hasNext()) {
                  this.items.add(var2);
                  return;
               }

               var3 = (CartridgeFile)var7.next();
            } while(var3.latitude % 360.0D == 0.0D && var3.longitude % 360.0D == 0.0D);

            MapPoint var4 = new MapPoint(var3.name, var3.description, var3.latitude, var3.longitude);
            var4.setData((new File(var3.filename)).getName());

            try {
               byte[] var8 = var3.getFile(var3.iconId);
               Bitmap var5 = BitmapFactory.decodeByteArray(var8, 0, var8.length);
               MapPointPack var9 = new MapPointPack(false, var5);
               var9.getPoints().add(var4);
               this.items.add(var9);
            } catch (Exception var6) {
               var2.getPoints().add(var4);
            }
         }
      }
   }

   public void addOther(EventTable var1, boolean var2) {
      if (var1 != null && var1.isLocated() && var1.isVisible()) {
         MapPointPack var3 = new MapPointPack();
         var3.getPoints().add(new MapPoint(var1.name, var1.position.latitude, var1.position.longitude, var2));
         if (var2) {
            var3.setResource(2130837559);
         } else {
            var3.setResource(2130837560);
         }

         this.items.add(var3);
      }

   }

   public void addZone(Zone var1, boolean var2) {
      if (var1 != null && var1.isLocated() && var1.isVisible()) {
         MapPointPack var3 = new MapPointPack();
         var3.setPolygon(true);

         for(int var4 = 0; var4 < var1.points.length; ++var4) {
            var3.getPoints().add(new MapPoint("", var1.points[var4].latitude, var1.points[var4].longitude));
         }

         if (var3.getPoints().size() >= 3) {
            var3.getPoints().add(var3.getPoints().get(0));
         }

         this.items.add(var3);
         var3 = new MapPointPack();
         if (Preferences.GUIDING_ZONE_NAVIGATION_POINT != 1 && var1.position != null) {
            var3.getPoints().add(new MapPoint(var1.name, var1.description, var1.position.latitude, var1.position.longitude, var2));
         } else {
            var3.getPoints().add(new MapPoint(var1.name, var1.description, var1.nearestPoint.latitude, var1.nearestPoint.longitude, var2));
         }

         if (var2) {
            var3.setResource(2130837559);
         } else {
            var3.setResource(2130837560);
         }

         this.items.add(var3);
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
      this.items.clear();
   }

   public ArrayList getItems() {
      return this.items;
   }
}

package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.IOException;
import se.krka.kahlua.stdlib.TableLib;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTableImpl;

public class Player extends Thing {
   private static JavaFunction refreshLocation = new JavaFunction() {
      public int call(LuaCallFrame var1, int var2) {
         Engine.instance.player.refreshLocation();
         return 0;
      }
   };
   private LuaTableImpl insideOfZones = new LuaTableImpl();

   public Player() {
      super(true);
      this.table.rawset("RefreshLocation", refreshLocation);
      this.table.rawset("InsideOfZones", this.insideOfZones);
      this.setPosition(new ZonePoint(360.0D, 360.0D, 0.0D));
   }

   public static void register() {
      Engine.instance.savegame.addJavafunc(refreshLocation);
   }

   public void deserialize(DataInputStream var1) throws IOException {
      super.deserialize(var1);
      Engine.instance.player = this;
   }

   public void enterZone(Zone var1) {
      this.container = var1;
      if (!TableLib.contains(this.insideOfZones, var1)) {
         TableLib.rawappend(this.insideOfZones, var1);
      }

   }

   public void leaveZone(Zone var1) {
      TableLib.removeItem(this.insideOfZones, var1);
      if (this.insideOfZones.len() > 0) {
         this.container = (Container)this.insideOfZones.rawget(new Double((double)this.insideOfZones.len()));
      }

   }

   protected String luaTostring() {
      return "a Player instance";
   }

   public void moveTo(Container var1) {
   }

   public Object rawget(Object var1) {
      if ("ObjectLocation".equals(var1)) {
         var1 = ZonePoint.copy(this.position);
      } else {
         var1 = super.rawget(var1);
      }

      return var1;
   }

   public void rawset(Object var1, Object var2) {
      if (!"ObjectLocation".equals(var1)) {
         super.rawset(var1, var2);
      }

   }

   public void refreshLocation() {
      this.position.latitude = Engine.gps.getLatitude();
      this.position.longitude = Engine.gps.getLongitude();
      this.position.altitude = Engine.gps.getAltitude();
      this.table.rawset("PositionAccuracy", LuaState.toDouble(Engine.gps.getPrecision()));
      Engine.instance.cartridge.walk(this.position);
   }

   public int visibleThings() {
      int var1 = 0;
      Object var2 = null;

      while(true) {
         Object var3 = this.inventory.next(var2);
         if (var3 == null) {
            return var1;
         }

         Object var4 = this.inventory.rawget(var3);
         var2 = var3;
         if (var4 instanceof Thing) {
            var2 = var3;
            if (((Thing)var4).isVisible()) {
               ++var1;
               var2 = var3;
            }
         }
      }
   }
}

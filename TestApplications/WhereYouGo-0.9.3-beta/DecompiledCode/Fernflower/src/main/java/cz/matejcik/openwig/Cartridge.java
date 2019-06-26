package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import se.krka.kahlua.stdlib.TableLib;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;

public class Cartridge extends EventTable {
   private static JavaFunction requestSync = new JavaFunction() {
      public int call(LuaCallFrame var1, int var2) {
         Engine.instance.store();
         return 0;
      }
   };
   public LuaTable allZObjects = new LuaTableImpl();
   public Vector tasks = new Vector();
   public Vector things = new Vector();
   public Vector timers = new Vector();
   public Vector universalActions = new Vector();
   public Vector zones = new Vector();

   public Cartridge() {
      this.table.rawset("RequestSync", requestSync);
      this.table.rawset("AllZObjects", this.allZObjects);
      TableLib.rawappend(this.allZObjects, this);
   }

   public static void register() {
      Engine.instance.savegame.addJavafunc(requestSync);
   }

   private void sortObject(Object var1) {
      if (var1 instanceof Task) {
         this.tasks.addElement(var1);
      } else if (var1 instanceof Zone) {
         this.zones.addElement(var1);
      } else if (var1 instanceof Timer) {
         this.timers.addElement(var1);
      } else if (var1 instanceof Thing) {
         this.things.addElement(var1);
      }

   }

   public void addObject(Object var1) {
      TableLib.rawappend(this.allZObjects, var1);
      this.sortObject(var1);
   }

   public LuaTable currentThings() {
      LuaTableImpl var1 = new LuaTableImpl();

      for(int var2 = 0; var2 < this.zones.size(); ++var2) {
         ((Zone)this.zones.elementAt(var2)).collectThings(var1);
      }

      return var1;
   }

   public void deserialize(DataInputStream var1) throws IOException {
      super.deserialize(var1);
      Engine.instance.cartridge = this;
      this.allZObjects = (LuaTable)this.table.rawget("AllZObjects");
      Object var2 = null;

      while(true) {
         var2 = this.allZObjects.next(var2);
         if (var2 == null) {
            return;
         }

         this.sortObject(this.allZObjects.rawget(var2));
      }
   }

   protected String luaTostring() {
      return "a ZCartridge instance";
   }

   public void tick() {
      int var1;
      for(var1 = 0; var1 < this.zones.size(); ++var1) {
         ((Zone)this.zones.elementAt(var1)).tick();
      }

      for(var1 = 0; var1 < this.timers.size(); ++var1) {
         ((Timer)this.timers.elementAt(var1)).updateRemaining();
      }

   }

   public int visibleTasks() {
      int var1 = 0;

      int var3;
      for(int var2 = 0; var2 < this.tasks.size(); var1 = var3) {
         var3 = var1;
         if (((Task)this.tasks.elementAt(var2)).isVisible()) {
            var3 = var1 + 1;
         }

         ++var2;
      }

      return var1;
   }

   public int visibleThings() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.zones.size(); ++var2) {
         var1 += ((Zone)this.zones.elementAt(var2)).visibleThings();
      }

      return var1;
   }

   public int visibleUniversalActions() {
      int var1 = 0;

      int var4;
      for(int var2 = 0; var2 < this.universalActions.size(); var1 = var4) {
         Action var3 = (Action)this.universalActions.elementAt(var2);
         var4 = var1;
         if (var3.isEnabled()) {
            var4 = var1;
            if (var3.getActor().visibleToPlayer()) {
               var4 = var1 + 1;
            }
         }

         ++var2;
      }

      return var1;
   }

   public int visibleZones() {
      int var1 = 0;

      int var3;
      for(int var2 = 0; var2 < this.zones.size(); var1 = var3) {
         var3 = var1;
         if (((Zone)this.zones.elementAt(var2)).isVisible()) {
            var3 = var1 + 1;
         }

         ++var2;
      }

      return var1;
   }

   public void walk(ZonePoint var1) {
      for(int var2 = 0; var2 < this.zones.size(); ++var2) {
         ((Zone)this.zones.elementAt(var2)).walk(var1);
      }

   }
}

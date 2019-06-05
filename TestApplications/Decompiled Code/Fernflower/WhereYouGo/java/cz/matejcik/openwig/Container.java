package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.IOException;
import se.krka.kahlua.stdlib.TableLib;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;

public class Container extends EventTable {
   private static JavaFunction contains = new JavaFunction() {
      public int call(LuaCallFrame var1, int var2) {
         var1.push(LuaState.toBoolean(((Container)var1.get(0)).contains((Thing)var1.get(1))));
         return 1;
      }
   };
   private static JavaFunction moveTo = new JavaFunction() {
      public int call(LuaCallFrame var1, int var2) {
         ((Container)var1.get(0)).moveTo((Container)var1.get(1));
         return 0;
      }
   };
   public Container container = null;
   public LuaTable inventory = new LuaTableImpl();

   public Container() {
      this.table.rawset("MoveTo", moveTo);
      this.table.rawset("Contains", contains);
      this.table.rawset("Inventory", this.inventory);
      this.table.rawset("Container", this.container);
   }

   public static void register() {
      Engine.instance.savegame.addJavafunc(moveTo);
      Engine.instance.savegame.addJavafunc(contains);
   }

   public boolean contains(Thing var1) {
      boolean var2 = true;
      Object var3 = null;

      while(true) {
         Object var4 = this.inventory.next(var3);
         if (var4 == null) {
            var2 = false;
            break;
         }

         Object var5 = this.inventory.rawget(var4);
         var3 = var4;
         if (var5 instanceof Thing) {
            if (var5 == var1) {
               break;
            }

            var3 = var4;
            if (((Thing)var5).contains(var1)) {
               break;
            }
         }
      }

      return var2;
   }

   public void deserialize(DataInputStream var1) throws IOException {
      super.deserialize(var1);
      this.inventory = (LuaTable)this.table.rawget("Inventory");
      Object var2 = this.table.rawget("Container");
      if (var2 instanceof Container) {
         this.container = (Container)var2;
      } else {
         this.container = null;
      }

   }

   public void moveTo(Container var1) {
      String var2;
      if (var1 == null) {
         var2 = "(nowhere)";
      } else {
         var2 = var1.name;
      }

      Engine.log("MOVE: " + this.name + " to " + var2, 1);
      if (this.container != null) {
         TableLib.removeItem(this.container.inventory, this);
      }

      if (var1 != null) {
         TableLib.rawappend(var1.inventory, this);
         if (var1 == Engine.instance.player) {
            this.setPosition((ZonePoint)null);
         } else if (this.position != null) {
            this.setPosition(var1.position);
         } else if (this.container == Engine.instance.player) {
            this.setPosition(ZonePoint.copy(Engine.instance.player.position));
         }

         this.container = var1;
      } else {
         this.container = null;
         this.rawset("ObjectLocation", (Object)null);
      }

      this.table.rawset("Container", this.container);
   }

   public Object rawget(Object var1) {
      if ("Container".equals(var1)) {
         var1 = this.container;
      } else {
         var1 = super.rawget(var1);
      }

      return var1;
   }

   public boolean visibleToPlayer() {
      boolean var1 = false;
      if (this.isVisible()) {
         if (this.container == Engine.instance.player) {
            var1 = true;
         } else if (this.container instanceof Zone) {
            var1 = ((Zone)this.container).showThings();
         }
      }

      return var1;
   }
}

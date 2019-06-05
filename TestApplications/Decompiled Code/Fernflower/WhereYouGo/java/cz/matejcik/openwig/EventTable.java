package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import se.krka.kahlua.stdlib.BaseLib;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;

public class EventTable implements LuaTable, Serializable {
   public String description;
   public Media icon;
   public Media media;
   private LuaTable metatable = new LuaTableImpl();
   public String name;
   public ZonePoint position = null;
   public LuaTable table = new LuaTableImpl();
   protected boolean visible = false;

   public EventTable() {
      this.metatable.rawset("__tostring", new EventTable.TostringJavaFunc(this));
   }

   public void callEvent(String var1, Object var2) {
      Throwable var10000;
      label33: {
         Object var3;
         StringBuilder var4;
         StringBuilder var5;
         boolean var10001;
         try {
            var3 = this.table.rawget(var1);
            if (!(var3 instanceof LuaClosure)) {
               return;
            }

            var4 = new StringBuilder();
            var5 = var4.append("EVNT: ").append(this.toString()).append(".").append(var1);
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label33;
         }

         String var11;
         if (var2 != null) {
            try {
               var4 = new StringBuilder();
               var11 = var4.append(" (").append(var2.toString()).append(")").toString();
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label33;
            }
         } else {
            var11 = "";
         }

         try {
            Engine.log(var5.append(var11).toString(), 1);
            LuaClosure var12 = (LuaClosure)var3;
            Engine.state.call(var12, this, var2, (Object)null);
            StringBuilder var10 = new StringBuilder();
            Engine.log(var10.append("EEND: ").append(this.toString()).append(".").append(var1).toString(), 1);
            return;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Throwable var9 = var10000;
      Engine.stacktrace(var9);
   }

   public void deserialize(DataInputStream var1) throws IOException {
      Engine.instance.savegame.restoreValue(var1, this);
   }

   public byte[] getIcon() throws IOException {
      return Engine.mediaFile(this.icon);
   }

   protected Object getItem(String var1) {
      Object var2;
      if ("CurrentDistance".equals(var1)) {
         if (this.isLocated()) {
            var2 = LuaState.toDouble(this.position.distance(Engine.instance.player.position));
         } else {
            var2 = LuaState.toDouble(-1L);
         }
      } else if ("CurrentBearing".equals(var1)) {
         if (this.isLocated()) {
            var2 = LuaState.toDouble(ZonePoint.angle2azimuth(this.position.bearing(Engine.instance.player.position)));
         } else {
            var2 = LuaState.toDouble(0L);
         }
      } else {
         var2 = this.table.rawget(var1);
      }

      return var2;
   }

   public byte[] getMedia() throws IOException {
      return Engine.mediaFile(this.media);
   }

   public LuaTable getMetatable() {
      return this.metatable;
   }

   public boolean hasEvent(String var1) {
      return this.table.rawget(var1) instanceof LuaClosure;
   }

   public boolean isLocated() {
      boolean var1;
      if (this.position != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public int len() {
      return this.table.len();
   }

   protected String luaTostring() {
      return "a ZObject instance";
   }

   public Object next(Object var1) {
      return this.table.next(var1);
   }

   public Object rawget(Object var1) {
      if (var1 instanceof String) {
         var1 = this.getItem((String)var1);
      } else {
         var1 = this.table.rawget(var1);
      }

      return var1;
   }

   public void rawset(Object var1, Object var2) {
      if (var1 instanceof String) {
         this.setItem((String)var1, var2);
      }

      this.table.rawset(var1, var2);
      StringBuilder var3 = (new StringBuilder()).append("PROP: ").append(this.toString()).append(".").append(var1).append(" is set to ");
      String var4;
      if (var2 == null) {
         var4 = "nil";
      } else {
         var4 = var2.toString();
      }

      Engine.log(var3.append(var4).toString(), 0);
   }

   public void serialize(DataOutputStream var1) throws IOException {
      Engine.instance.savegame.storeValue(this.table, var1);
   }

   protected void setItem(String var1, Object var2) {
      if ("Name".equals(var1)) {
         this.name = BaseLib.rawTostring(var2);
      } else if ("Description".equals(var1)) {
         this.description = Engine.removeHtml(BaseLib.rawTostring(var2));
      } else if ("Visible".equals(var1)) {
         this.visible = LuaState.boolEval(var2);
      } else if ("ObjectLocation".equals(var1)) {
         this.position = (ZonePoint)var2;
      } else if ("Media".equals(var1)) {
         this.media = (Media)var2;
      } else if ("Icon".equals(var1)) {
         this.icon = (Media)var2;
      }

   }

   public void setMetatable(LuaTable var1) {
   }

   public void setPosition(ZonePoint var1) {
      this.position = var1;
      this.table.rawset("ObjectLocation", var1);
   }

   public void setTable(LuaTable var1) {
      Object var2 = null;

      while(true) {
         var2 = var1.next(var2);
         if (var2 == null) {
            return;
         }

         this.rawset(var2, var1.rawget(var2));
      }
   }

   public String toString() {
      String var1;
      if (this.name == null) {
         var1 = "(unnamed)";
      } else {
         var1 = this.name;
      }

      return var1;
   }

   private static class TostringJavaFunc implements JavaFunction {
      public EventTable parent;

      public TostringJavaFunc(EventTable var1) {
         this.parent = var1;
      }

      public int call(LuaCallFrame var1, int var2) {
         var1.push(this.parent.luaTostring());
         return 1;
      }
   }
}

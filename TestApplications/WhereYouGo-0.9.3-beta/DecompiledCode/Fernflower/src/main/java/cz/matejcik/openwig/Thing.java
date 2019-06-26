package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import se.krka.kahlua.stdlib.BaseLib;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;

public class Thing extends Container {
   public Vector actions = new Vector();
   private boolean character = false;

   public Thing() {
   }

   public Thing(boolean var1) {
      this.character = var1;
      this.table.rawset("Commands", new LuaTableImpl());
   }

   public void deserialize(DataInputStream var1) throws IOException {
      this.character = var1.readBoolean();
      super.deserialize(var1);
   }

   public boolean isCharacter() {
      return this.character;
   }

   public boolean isItem() {
      boolean var1;
      if (!this.character) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected String luaTostring() {
      String var1;
      if (this.character) {
         var1 = "a ZCharacter instance";
      } else {
         var1 = "a ZItem instance";
      }

      return var1;
   }

   public void serialize(DataOutputStream var1) throws IOException {
      var1.writeBoolean(this.character);
      super.serialize(var1);
   }

   protected void setItem(String var1, Object var2) {
      if ("Commands".equals(var1)) {
         for(int var3 = 0; var3 < this.actions.size(); ++var3) {
            ((Action)this.actions.elementAt(var3)).dissociateFromTargets();
         }

         this.actions.removeAllElements();
         LuaTable var6 = (LuaTable)var2;
         Object var5 = null;

         while(true) {
            var5 = var6.next(var5);
            if (var5 == null) {
               break;
            }

            Action var4 = (Action)var6.rawget(var5);
            if (var5 instanceof Double) {
               var4.name = BaseLib.numberToString((Double)var5);
            } else {
               var4.name = var5.toString();
            }

            var4.setActor(this);
            this.actions.addElement(var4);
            var4.associateWithTargets();
         }
      } else {
         super.setItem(var1, var2);
      }

   }

   public int visibleActions() {
      int var1 = 0;

      int var4;
      for(int var2 = 0; var2 < this.actions.size(); var1 = var4) {
         Action var3 = (Action)this.actions.elementAt(var2);
         if (!var3.isEnabled()) {
            var4 = var1;
         } else {
            label26: {
               if (var3.getActor() != this) {
                  var4 = var1;
                  if (!var3.getActor().visibleToPlayer()) {
                     break label26;
                  }
               }

               var4 = var1 + 1;
            }
         }

         ++var2;
      }

      return var1;
   }
}

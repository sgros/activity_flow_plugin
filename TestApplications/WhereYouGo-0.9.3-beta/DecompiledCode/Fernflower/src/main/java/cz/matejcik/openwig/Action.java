package cz.matejcik.openwig;

import java.util.Vector;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;

public class Action extends EventTable {
   private Thing actor = null;
   private boolean enabled;
   public String notarget;
   private boolean parameter;
   private boolean reciprocal = true;
   private Vector targets = new Vector();
   public String text;
   private boolean universal;

   public Action() {
   }

   public Action(LuaTable var1) {
      this.table = var1;
      Object var2 = null;

      while(true) {
         Object var3 = var1.next(var2);
         if (var3 == null) {
            return;
         }

         var2 = var3;
         if (var3 instanceof String) {
            this.setItem((String)var3, var1.rawget(var3));
            var2 = var3;
         }
      }
   }

   public void associateWithTargets() {
      if (this.hasParameter()) {
         if (this.isReciprocal()) {
            for(int var1 = 0; var1 < this.targets.size(); ++var1) {
               Thing var2 = (Thing)this.targets.elementAt(var1);
               if (!var2.actions.contains(this)) {
                  var2.actions.addElement(this);
               }
            }
         }

         if (this.isUniversal() && !Engine.instance.cartridge.universalActions.contains(this)) {
            Engine.instance.cartridge.universalActions.addElement(this);
         }
      }

   }

   public void dissociateFromTargets() {
      if (this.hasParameter()) {
         if (this.isReciprocal()) {
            for(int var1 = 0; var1 < this.targets.size(); ++var1) {
               ((Thing)this.targets.elementAt(var1)).actions.removeElement(this);
            }
         }

         if (this.isUniversal()) {
            Engine.instance.cartridge.universalActions.removeElement(this);
         }
      }

   }

   public Thing getActor() {
      return this.actor;
   }

   public String getName() {
      return this.name;
   }

   public Vector getTargets() {
      return this.targets;
   }

   public boolean hasParameter() {
      return this.parameter;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean isReciprocal() {
      return this.reciprocal;
   }

   public boolean isTarget(Thing var1) {
      boolean var2;
      if (!this.targets.contains(var1) && !this.isUniversal()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean isUniversal() {
      return this.universal;
   }

   protected String luaTostring() {
      return "a ZCommand instance";
   }

   public void setActor(Thing var1) {
      this.actor = var1;
   }

   protected void setItem(String var1, Object var2) {
      if ("Text".equals(var1)) {
         this.text = (String)var2;
      } else if ("CmdWith".equals(var1)) {
         boolean var3 = LuaState.boolEval(var2);
         if (var3 != this.parameter) {
            if (var3) {
               this.parameter = true;
               this.associateWithTargets();
            } else {
               this.dissociateFromTargets();
               this.parameter = false;
            }
         }
      } else if ("Enabled".equals(var1)) {
         this.enabled = LuaState.boolEval(var2);
      } else if ("WorksWithAll".equals(var1)) {
         this.dissociateFromTargets();
         this.universal = LuaState.boolEval(var2);
         this.associateWithTargets();
      } else if ("WorksWithList".equals(var1)) {
         this.dissociateFromTargets();
         LuaTable var5 = (LuaTable)var2;
         Object var4 = null;

         while(true) {
            var4 = var5.next(var4);
            if (var4 == null) {
               this.associateWithTargets();
               break;
            }

            this.targets.addElement(var5.rawget(var4));
         }
      } else if ("MakeReciprocal".equals(var1)) {
         this.dissociateFromTargets();
         this.reciprocal = LuaState.boolEval(var2);
         this.associateWithTargets();
      } else if ("EmptyTargetListText".equals(var1)) {
         if (var2 == null) {
            var1 = "(not available now)";
         } else {
            var1 = var2.toString();
         }

         this.notarget = var1;
      }

   }

   public int targetsInside(LuaTable var1) {
      int var2 = 0;
      Object var3 = null;

      while(true) {
         Object var4;
         do {
            Thing var6;
            do {
               Object var5;
               do {
                  var4 = var1.next(var3);
                  if (var4 == null) {
                     return var2;
                  }

                  var5 = var1.rawget(var4);
                  var3 = var4;
               } while(!(var5 instanceof Thing));

               var6 = (Thing)var5;
               var3 = var4;
            } while(!var6.isVisible());

            if (this.targets.contains(var6)) {
               break;
            }

            var3 = var4;
         } while(!this.isUniversal());

         ++var2;
         var3 = var4;
      }
   }

   public int visibleTargets(Container var1) {
      int var2 = 0;
      Object var3 = null;

      while(true) {
         Object var4;
         do {
            Thing var6;
            do {
               Object var5;
               do {
                  var4 = var1.inventory.next(var3);
                  if (var4 == null) {
                     return var2;
                  }

                  var5 = var1.inventory.rawget(var4);
                  var3 = var4;
               } while(!(var5 instanceof Thing));

               var6 = (Thing)var5;
               var3 = var4;
            } while(!var6.isVisible());

            if (this.targets.contains(var6)) {
               break;
            }

            var3 = var4;
         } while(!this.isUniversal());

         ++var2;
         var3 = var4;
      }
   }
}

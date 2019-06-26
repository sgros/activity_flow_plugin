package cz.matejcik.openwig;

import se.krka.kahlua.vm.LuaState;

public class Task extends EventTable {
   public static final int DONE = 1;
   public static final int FAILED = 2;
   public static final int PENDING = 0;
   private boolean active;
   private boolean complete;
   private int state = 1;

   public boolean isComplete() {
      return this.complete;
   }

   public boolean isVisible() {
      boolean var1;
      if (this.visible && this.active) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected String luaTostring() {
      return "a ZTask instance";
   }

   protected void setItem(String var1, Object var2) {
      boolean var3;
      if ("Active".equals(var1)) {
         var3 = LuaState.boolEval(var2);
         if (var3 != this.active) {
            this.active = var3;
            this.callEvent("OnSetActive", (Object)null);
         }
      } else if ("Complete".equals(var1)) {
         var3 = LuaState.boolEval(var2);
         if (var3 != this.complete) {
            this.complete = var3;
            this.callEvent("OnSetComplete", (Object)null);
         }
      } else if ("CorrectState".equals(var1) && var2 instanceof String) {
         var1 = (String)var2;
         byte var4 = 1;
         if ("Incorrect".equalsIgnoreCase(var1) || "NotCorrect".equalsIgnoreCase(var1)) {
            var4 = 2;
         }

         if (var4 != this.state) {
            this.state = var4;
            this.callEvent("OnSetCorrectState", (Object)null);
         }
      } else {
         super.setItem(var1, var2);
      }

   }

   public int state() {
      int var1;
      if (!this.complete) {
         var1 = 0;
      } else {
         var1 = this.state;
      }

      return var1;
   }
}

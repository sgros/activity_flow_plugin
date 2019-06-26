package se.krka.kahlua.vm;

public final class LuaClosure {
   public LuaTable env;
   public LuaPrototype prototype;
   public UpValue[] upvalues;

   public LuaClosure(LuaPrototype var1, LuaTable var2) {
      this.prototype = var1;
      this.env = var2;
      this.upvalues = new UpValue[var1.numUpvalues];
   }

   public String toString() {
      String var1;
      if (this.prototype.lines.length > 0) {
         var1 = "function " + this.prototype.toString() + ":" + this.prototype.lines[0];
      } else {
         var1 = "function[" + Integer.toString(this.hashCode(), 36) + "]";
      }

      return var1;
   }
}

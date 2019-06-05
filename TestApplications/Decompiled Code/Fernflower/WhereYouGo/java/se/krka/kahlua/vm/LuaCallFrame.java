package se.krka.kahlua.vm;

public class LuaCallFrame {
   public LuaClosure closure;
   boolean fromLua;
   public boolean insideCoroutine;
   public JavaFunction javaFunction;
   public int localBase;
   public int nArguments;
   public int pc;
   boolean restoreTop;
   int returnBase;
   public LuaThread thread;

   public LuaCallFrame(LuaThread var1) {
      this.thread = var1;
   }

   public void clearFromIndex(int var1) {
      if (this.getTop() < var1) {
         this.setTop(var1);
      }

      this.stackClear(var1, this.getTop() - 1);
   }

   public void closeUpvalues(int var1) {
      this.thread.closeUpvalues(this.localBase + var1);
   }

   public UpValue findUpvalue(int var1) {
      return this.thread.findUpvalue(this.localBase + var1);
   }

   public final Object get(int var1) {
      return this.thread.objectStack[this.localBase + var1];
   }

   public LuaTable getEnvironment() {
      LuaTable var1;
      if (this.isLua()) {
         var1 = this.closure.env;
      } else {
         var1 = this.thread.environment;
      }

      return var1;
   }

   public int getTop() {
      return this.thread.getTop() - this.localBase;
   }

   public void init() {
      if (this.isLua()) {
         this.pc = 0;
         if (this.closure.prototype.isVararg) {
            this.localBase += this.nArguments;
            this.setTop(this.closure.prototype.maxStacksize);
            int var1 = Math.min(this.nArguments, this.closure.prototype.numParams);
            this.stackCopy(-this.nArguments, 0, var1);
         } else {
            this.setTop(this.closure.prototype.maxStacksize);
            this.stackClear(this.closure.prototype.numParams, this.nArguments);
         }
      }

   }

   public boolean isJava() {
      boolean var1;
      if (!this.isLua()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isLua() {
      boolean var1;
      if (this.closure != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public int push(Object var1) {
      int var2 = this.getTop();
      this.setTop(var2 + 1);
      this.set(var2, var1);
      return 1;
   }

   public int push(Object var1, Object var2) {
      int var3 = this.getTop();
      this.setTop(var3 + 2);
      this.set(var3, var1);
      this.set(var3 + 1, var2);
      return 2;
   }

   public int pushNil() {
      return this.push((Object)null);
   }

   public void pushVarargs(int var1, int var2) {
      int var3 = this.closure.prototype.numParams;
      int var4 = this.nArguments - var3;
      int var5 = var4;
      if (var4 < 0) {
         var5 = 0;
      }

      var4 = var2;
      if (var2 == -1) {
         var4 = var5;
         this.setTop(var1 + var5);
      }

      var2 = var5;
      if (var5 > var4) {
         var2 = var4;
      }

      this.stackCopy(-this.nArguments + var3, var1, var2);
      if (var4 - var2 > 0) {
         this.stackClear(var1 + var2, var1 + var4 - 1);
      }

   }

   public final void set(int var1, Object var2) {
      this.thread.objectStack[this.localBase + var1] = var2;
   }

   public void setPrototypeStacksize() {
      if (this.isLua()) {
         this.setTop(this.closure.prototype.maxStacksize);
      }

   }

   public final void setTop(int var1) {
      this.thread.setTop(this.localBase + var1);
   }

   public void stackClear(int var1, int var2) {
      while(var1 <= var2) {
         this.thread.objectStack[this.localBase + var1] = null;
         ++var1;
      }

   }

   public final void stackCopy(int var1, int var2, int var3) {
      this.thread.stackCopy(this.localBase + var1, this.localBase + var2, var3);
   }

   public String toString() {
      String var1;
      if (this.closure != null) {
         var1 = "Callframe at: " + this.closure.toString();
      } else if (this.javaFunction != null) {
         var1 = "Callframe at: " + this.javaFunction.toString();
      } else {
         var1 = super.toString();
      }

      return var1;
   }
}

package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;
import se.krka.kahlua.vm.LuaThread;

public class CoroutineLib implements JavaFunction {
   private static final int CREATE = 0;
   private static final Class LUA_THREAD_CLASS = (new LuaThread((LuaState)null, (LuaTable)null)).getClass();
   private static final int NUM_FUNCTIONS = 5;
   private static final int RESUME = 1;
   private static final int RUNNING = 4;
   private static final int STATUS = 3;
   private static final int YIELD = 2;
   private static CoroutineLib[] functions;
   private static final String[] names = new String[5];
   private int index;

   static {
      names[0] = "create";
      names[1] = "resume";
      names[2] = "yield";
      names[3] = "status";
      names[4] = "running";
      functions = new CoroutineLib[5];

      for(int var0 = 0; var0 < 5; ++var0) {
         functions[var0] = new CoroutineLib(var0);
      }

   }

   public CoroutineLib(int var1) {
      this.index = var1;
   }

   private int create(LuaCallFrame var1, int var2) {
      LuaClosure var3 = this.getFunction(var1, var2);
      LuaThread var4 = new LuaThread(var1.thread.state, var1.thread.environment);
      var4.pushNewCallFrame(var3, (JavaFunction)null, 0, 0, -1, true, true);
      var1.push(var4);
      return 1;
   }

   private LuaThread getCoroutine(LuaCallFrame var1, int var2) {
      boolean var3 = true;
      if (var2 < 1) {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "not enough arguments");
      Object var4 = var1.get(0);
      BaseLib.luaAssert(var4 instanceof LuaThread, "argument 1 must be a coroutine");
      return (LuaThread)var4;
   }

   private LuaClosure getFunction(LuaCallFrame var1, int var2) {
      boolean var3 = true;
      if (var2 < 1) {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "not enough arguments");
      Object var4 = var1.get(0);
      BaseLib.luaAssert(var4 instanceof LuaClosure, "argument 1 must be a lua function");
      return (LuaClosure)var4;
   }

   private String getStatus(LuaThread var1, LuaThread var2) {
      String var3;
      if (var1.parent == null) {
         if (var1.isDead()) {
            var3 = "dead";
         } else {
            var3 = "suspended";
         }
      } else if (var2 == var1) {
         var3 = "running";
      } else {
         var3 = "normal";
      }

      return var3;
   }

   public static void register(LuaState var0) {
      LuaTableImpl var1 = new LuaTableImpl();
      var0.getEnvironment().rawset("coroutine", var1);

      for(int var2 = 0; var2 < 5; ++var2) {
         var1.rawset(names[var2], functions[var2]);
      }

      var1.rawset("__index", var1);
      var0.setClassMetatable(LUA_THREAD_CLASS, var1);
   }

   private int resume(LuaCallFrame var1, int var2) {
      LuaThread var3 = this.getCoroutine(var1, var2);
      String var4 = this.getStatus(var3, var1.thread);
      if (var4 != "suspended") {
         BaseLib.fail("Can not resume thread that is in status: " + var4);
      }

      var3.parent = var1.thread;
      LuaCallFrame var6 = var3.currentCallFrame();
      if (var6.nArguments == -1) {
         var6.setTop(0);
      }

      for(int var5 = 1; var5 < var2; ++var5) {
         var6.push(var1.get(var5));
      }

      if (var6.nArguments == -1) {
         var6.nArguments = var2 - 1;
         var6.init();
      }

      var1.thread.state.currentThread = var3;
      return 0;
   }

   private int running(LuaCallFrame var1, int var2) {
      LuaThread var3 = var1.thread;
      LuaThread var4 = var3;
      if (var3.parent == null) {
         var4 = null;
      }

      var1.push(var4);
      return 1;
   }

   private int status(LuaCallFrame var1, int var2) {
      var1.push(this.getStatus(this.getCoroutine(var1, var2), var1.thread));
      return 1;
   }

   public static int yield(LuaCallFrame var0, int var1) {
      LuaThread var2 = var0.thread;
      boolean var3;
      if (var2.parent != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "Can not yield outside of a coroutine");
      yieldHelper(var2.callFrameStack[var2.callFrameTop - 2], var0, var1);
      return 0;
   }

   public static void yieldHelper(LuaCallFrame var0, LuaCallFrame var1, int var2) {
      BaseLib.luaAssert(var0.insideCoroutine, "Can not yield outside of a coroutine");
      LuaThread var3 = var0.thread;
      LuaThread var6 = var3.parent;
      var3.parent = null;
      LuaCallFrame var4 = var6.currentCallFrame();
      var4.push(Boolean.TRUE);

      for(int var5 = 0; var5 < var2; ++var5) {
         var4.push(var1.get(var5));
      }

      var3.state.currentThread = var6;
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.index) {
      case 0:
         var2 = this.create(var1, var2);
         break;
      case 1:
         var2 = this.resume(var1, var2);
         break;
      case 2:
         var2 = yield(var1, var2);
         break;
      case 3:
         var2 = this.status(var1, var2);
         break;
      case 4:
         var2 = this.running(var1, var2);
         break;
      default:
         var2 = 0;
      }

      return var2;
   }

   public String toString() {
      return "coroutine." + names[this.index];
   }
}

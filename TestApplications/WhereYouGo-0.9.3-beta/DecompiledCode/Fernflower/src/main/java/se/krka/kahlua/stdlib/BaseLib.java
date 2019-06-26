package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.LuaException;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaThread;

public final class BaseLib implements JavaFunction {
   private static final int BYTECODELOADER = 18;
   private static final int COLLECTGARBAGE = 16;
   private static final int DEBUGSTACKTRACE = 17;
   private static final Object DOUBLE_ONE = new Double(1.0D);
   private static final int ERROR = 8;
   private static final int GETFENV = 12;
   private static final int GETMETATABLE = 6;
   public static final Object MODE_KEY = "__mode";
   private static final int NEXT = 10;
   private static final int NUM_FUNCTIONS = 19;
   private static final int PCALL = 0;
   private static final int PRINT = 1;
   private static final int RAWEQUAL = 13;
   private static final int RAWGET = 15;
   private static final int RAWSET = 14;
   private static final Runtime RUNTIME = Runtime.getRuntime();
   private static final int SELECT = 2;
   private static final int SETFENV = 11;
   private static final int SETMETATABLE = 7;
   private static final int TONUMBER = 5;
   private static final int TOSTRING = 4;
   private static final int TYPE = 3;
   public static final String TYPE_BOOLEAN = "boolean";
   public static final String TYPE_FUNCTION = "function";
   public static final String TYPE_NIL = "nil";
   public static final String TYPE_NUMBER = "number";
   public static final String TYPE_STRING = "string";
   public static final String TYPE_TABLE = "table";
   public static final String TYPE_THREAD = "thread";
   public static final String TYPE_USERDATA = "userdata";
   private static final int UNPACK = 9;
   private static BaseLib[] functions;
   private static final String[] names = new String[19];
   private int index;

   static {
      names[0] = "pcall";
      names[1] = "print";
      names[2] = "select";
      names[3] = "type";
      names[4] = "tostring";
      names[5] = "tonumber";
      names[6] = "getmetatable";
      names[7] = "setmetatable";
      names[8] = "error";
      names[9] = "unpack";
      names[10] = "next";
      names[11] = "setfenv";
      names[12] = "getfenv";
      names[13] = "rawequal";
      names[14] = "rawset";
      names[15] = "rawget";
      names[16] = "collectgarbage";
      names[17] = "debugstacktrace";
      names[18] = "bytecodeloader";
   }

   public BaseLib(int var1) {
      this.index = var1;
   }

   private static int bytecodeloader(LuaCallFrame var0, int var1) {
      String var2 = (String)getArg(var0, 1, "string", "loader");
      String var3 = (String)((LuaTable)var0.getEnvironment().rawget("package")).rawget("classpath");
      int var4 = 0;

      while(true) {
         if (var4 >= var3.length()) {
            var1 = var0.push("Could not find the bytecode for '" + var2 + "' in classpath");
            break;
         }

         int var5 = var3.indexOf(";", var4);
         var1 = var5;
         if (var5 == -1) {
            var1 = var3.length();
         }

         String var6 = var3.substring(var4, var1);
         if (var6.length() > 0) {
            String var7 = var6;
            if (!var6.endsWith("/")) {
               var7 = var6 + "/";
            }

            LuaClosure var8 = var0.thread.state.loadByteCodeFromResource(var7 + var2, var0.getEnvironment());
            if (var8 != null) {
               var1 = var0.push(var8);
               break;
            }
         }

         var4 = var1;
      }

      return var1;
   }

   public static int collectgarbage(LuaCallFrame var0, int var1) {
      byte var2 = 3;
      Object var3 = null;
      if (var1 > 0) {
         var3 = var0.get(0);
      }

      byte var8;
      if (var3 != null && !var3.equals("step") && !var3.equals("collect")) {
         if (!var3.equals("count")) {
            throw new RuntimeException("invalid option: " + var3);
         }

         long var4 = RUNTIME.freeMemory();
         long var6 = RUNTIME.totalMemory();
         var0.setTop(3);
         var0.set(0, toKiloBytes(var6 - var4));
         var0.set(1, toKiloBytes(var4));
         var0.set(2, toKiloBytes(var6));
         var8 = var2;
      } else {
         System.gc();
         var8 = 0;
      }

      return var8;
   }

   private int debugstacktrace(LuaCallFrame var1, int var2) {
      LuaThread var3 = (LuaThread)getOptArg(var1, 1, "thread");
      LuaThread var4 = var3;
      if (var3 == null) {
         var4 = var1.thread;
      }

      Double var7 = (Double)getOptArg(var1, 2, "number");
      var2 = 0;
      if (var7 != null) {
         var2 = var7.intValue();
      }

      var7 = (Double)getOptArg(var1, 3, "number");
      int var5 = Integer.MAX_VALUE;
      if (var7 != null) {
         var5 = var7.intValue();
      }

      var7 = (Double)getOptArg(var1, 4, "number");
      int var6 = 0;
      if (var7 != null) {
         var6 = var7.intValue();
      }

      return var1.push(var4.getCurrentStackTrace(var2, var5, var6));
   }

   private int error(LuaCallFrame var1, int var2) {
      if (var2 >= 1) {
         String var3 = (String)getOptArg(var1, 2, "string");
         String var4 = var3;
         if (var3 == null) {
            var4 = "";
         }

         var1.thread.stackTrace = var4;
         throw new LuaException(var1.get(0));
      } else {
         return 0;
      }
   }

   public static void fail(String var0) {
      throw new RuntimeException(var0);
   }

   public static Object getArg(LuaCallFrame var0, int var1, String var2, String var3) {
      Object var5 = var0.get(var1 - 1);
      if (var5 == null) {
         throw new RuntimeException("bad argument #" + var1 + "to '" + var3 + "' (" + var2 + " expected, got no value)");
      } else {
         String var4;
         if (var2 == "string") {
            var4 = rawTostring(var5);
            if (var4 != null) {
               var5 = var4;
               return var5;
            }
         } else if (var2 == "number") {
            var5 = rawTonumber(var5);
            if (var5 == null) {
               throw new RuntimeException("bad argument #" + var1 + " to '" + var3 + "' (number expected, got string)");
            }

            return var5;
         }

         if (var2 != null) {
            var4 = type(var5);
            if (var2 != var4) {
               fail("bad argument #" + var1 + " to '" + var3 + "' (" + var2 + " expected, got " + var4 + ")");
            }
         }

         return var5;
      }
   }

   public static Object getOptArg(LuaCallFrame var0, int var1, String var2) {
      Object var4;
      if (var1 - 1 >= var0.getTop()) {
         var4 = null;
      } else {
         Object var3 = var0.get(var1 - 1);
         if (var3 == null) {
            var4 = null;
         } else if (var2 == "string") {
            var4 = rawTostring(var3);
         } else {
            var4 = var3;
            if (var2 == "number") {
               var4 = rawTonumber(var3);
            }
         }
      }

      return var4;
   }

   private int getfenv(LuaCallFrame var1, int var2) {
      boolean var3 = false;
      Object var4 = DOUBLE_ONE;
      if (var2 >= 1) {
         var4 = var1.get(0);
      }

      LuaTable var6;
      if (var4 != null && !(var4 instanceof JavaFunction)) {
         if (var4 instanceof LuaClosure) {
            var6 = ((LuaClosure)var4).env;
         } else {
            Double var7 = rawTonumber(var4);
            boolean var5;
            if (var7 != null) {
               var5 = true;
            } else {
               var5 = false;
            }

            luaAssert(var5, "Expected number");
            var2 = var7.intValue();
            var5 = var3;
            if (var2 >= 0) {
               var5 = true;
            }

            luaAssert(var5, "level must be non-negative");
            var6 = var1.thread.getParent(var2).getEnvironment();
         }
      } else {
         var6 = var1.thread.environment;
      }

      var1.push(var6);
      return 1;
   }

   private static int getmetatable(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      luaAssert(var2, "Not enough arguments");
      Object var3 = var0.get(0);
      var0.push(var0.thread.state.getmetatable(var3, false));
      return 1;
   }

   private static void initFunctions() {
      synchronized(BaseLib.class){}

      label97: {
         Throwable var10000;
         label101: {
            boolean var10001;
            try {
               if (functions != null) {
                  break label97;
               }

               functions = new BaseLib[19];
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label101;
            }

            int var0 = 0;

            while(true) {
               if (var0 >= 19) {
                  break label97;
               }

               try {
                  functions[var0] = new BaseLib(var0);
               } catch (Throwable var6) {
                  var10000 = var6;
                  var10001 = false;
                  break;
               }

               ++var0;
            }
         }

         Throwable var1 = var10000;
         throw var1;
      }

   }

   public static void luaAssert(boolean var0, String var1) {
      if (!var0) {
         fail(var1);
      }

   }

   private int next(LuaCallFrame var1, int var2) {
      byte var3 = 1;
      boolean var4;
      if (var2 >= 1) {
         var4 = true;
      } else {
         var4 = false;
      }

      luaAssert(var4, "Not enough arguments");
      LuaTable var5 = (LuaTable)var1.get(0);
      Object var6 = null;
      if (var2 >= 2) {
         var6 = var1.get(1);
      }

      var6 = var5.next(var6);
      byte var7;
      if (var6 == null) {
         var1.setTop(1);
         var1.set(0, (Object)null);
         var7 = var3;
      } else {
         Object var8 = var5.rawget(var6);
         var1.setTop(2);
         var1.set(0, var6);
         var1.set(1, var8);
         var7 = 2;
      }

      return var7;
   }

   public static String numberToString(Double var0) {
      String var3;
      if (var0.isNaN()) {
         var3 = "nan";
      } else if (var0.isInfinite()) {
         if (MathLib.isNegative(var0)) {
            var3 = "-inf";
         } else {
            var3 = "inf";
         }
      } else {
         double var1 = var0;
         if (Math.floor(var1) == var1 && Math.abs(var1) < 1.0E14D) {
            var3 = String.valueOf(var0.longValue());
         } else {
            var3 = var0.toString();
         }
      }

      return var3;
   }

   public static int pcall(LuaCallFrame var0, int var1) {
      return var0.thread.state.pcall(var1 - 1);
   }

   private static int print(LuaCallFrame var0, int var1) {
      LuaState var2 = var0.thread.state;
      Object var3 = var2.tableGet(var2.getEnvironment(), "tostring");
      StringBuffer var4 = new StringBuffer();

      for(int var5 = 0; var5 < var1; ++var5) {
         if (var5 > 0) {
            var4.append("\t");
         }

         var4.append(var2.call(var3, var0.get(var5), (Object)null, (Object)null));
      }

      var2.getOut().println(var4.toString());
      return 0;
   }

   public static Double rawTonumber(Object var0) {
      Double var1;
      if (var0 instanceof Double) {
         var1 = (Double)var0;
      } else if (var0 instanceof String) {
         var1 = tonumber((String)var0);
      } else {
         var1 = null;
      }

      return var1;
   }

   public static String rawTostring(Object var0) {
      String var1;
      if (var0 instanceof String) {
         var1 = (String)var0;
      } else if (var0 instanceof Double) {
         var1 = numberToString((Double)var0);
      } else {
         var1 = null;
      }

      return var1;
   }

   private int rawequal(LuaCallFrame var1, int var2) {
      boolean var3;
      if (var2 >= 2) {
         var3 = true;
      } else {
         var3 = false;
      }

      luaAssert(var3, "Not enough arguments");
      var1.push(toBoolean(LuaState.luaEquals(var1.get(0), var1.get(1))));
      return 1;
   }

   private int rawget(LuaCallFrame var1, int var2) {
      boolean var3;
      if (var2 >= 2) {
         var3 = true;
      } else {
         var3 = false;
      }

      luaAssert(var3, "Not enough arguments");
      var1.push(((LuaTable)var1.get(0)).rawget(var1.get(1)));
      return 1;
   }

   private int rawset(LuaCallFrame var1, int var2) {
      boolean var3;
      if (var2 >= 3) {
         var3 = true;
      } else {
         var3 = false;
      }

      luaAssert(var3, "Not enough arguments");
      ((LuaTable)var1.get(0)).rawset(var1.get(1), var1.get(2));
      var1.setTop(1);
      return 1;
   }

   public static void register(LuaState var0) {
      initFunctions();

      for(int var1 = 0; var1 < 19; ++var1) {
         var0.getEnvironment().rawset(names[var1], functions[var1]);
      }

   }

   private static int select(LuaCallFrame var0, int var1) {
      byte var2 = 1;
      boolean var3;
      if (var1 >= 1) {
         var3 = true;
      } else {
         var3 = false;
      }

      luaAssert(var3, "Not enough arguments");
      Object var4 = var0.get(0);
      if (var4 instanceof String && ((String)var4).startsWith("#")) {
         var0.push(LuaState.toDouble((long)(var1 - 1)));
         var1 = var2;
      } else {
         int var5 = (int)LuaState.fromDouble(rawTonumber(var4));
         if (var5 >= 1 && var5 <= var1 - 1) {
            var1 -= var5;
         } else {
            var1 = 0;
         }
      }

      return var1;
   }

   private int setfenv(LuaCallFrame var1, int var2) {
      byte var3 = 0;
      boolean var4;
      if (var2 >= 2) {
         var4 = true;
      } else {
         var4 = false;
      }

      luaAssert(var4, "Not enough arguments");
      LuaTable var5 = (LuaTable)var1.get(1);
      if (var5 != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      luaAssert(var4, "expected a table");
      Object var6 = var1.get(0);
      byte var7;
      LuaClosure var8;
      if (var6 instanceof LuaClosure) {
         var8 = (LuaClosure)var6;
      } else {
         Double var9 = rawTonumber(var6);
         if (var9 != null) {
            var4 = true;
         } else {
            var4 = false;
         }

         luaAssert(var4, "expected a lua function or a number");
         var2 = ((Double)var9).intValue();
         if (var2 == 0) {
            var1.thread.environment = var5;
            var7 = var3;
            return var7;
         }

         LuaCallFrame var10 = var1.thread.getParent(var2);
         if (!var10.isLua()) {
            fail("No closure found at this level: " + var2);
         }

         var8 = var10.closure;
      }

      var8.env = var5;
      var1.setTop(1);
      var7 = 1;
      return var7;
   }

   private static int setmetatable(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      luaAssert(var2, "Not enough arguments");
      Object var3 = var0.get(0);
      LuaTable var4 = (LuaTable)var0.get(1);
      setmetatable(var0.thread.state, var3, var4, false);
      var0.setTop(1);
      return 1;
   }

   public static void setmetatable(LuaState var0, Object var1, LuaTable var2, boolean var3) {
      boolean var4;
      if (var1 != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      luaAssert(var4, "Expected table, got nil");
      Object var5 = var0.getmetatable(var1, var3);
      if (!var3 && var5 != null && var0.tableGet(var5, "__metatable") != null) {
         throw new RuntimeException("Can not set metatable of protected object");
      } else {
         var0.setmetatable(var1, var2);
      }
   }

   private static final Boolean toBoolean(boolean var0) {
      Boolean var1;
      if (var0) {
         var1 = Boolean.TRUE;
      } else {
         var1 = Boolean.FALSE;
      }

      return var1;
   }

   private static Double toKiloBytes(long var0) {
      return LuaState.toDouble((double)var0 / 1024.0D);
   }

   private static int tonumber(LuaCallFrame var0, int var1) {
      boolean var2 = false;
      boolean var3;
      if (var1 >= 1) {
         var3 = true;
      } else {
         var3 = false;
      }

      luaAssert(var3, "Not enough arguments");
      Object var4 = var0.get(0);
      if (var1 == 1) {
         var0.push(rawTonumber(var4));
      } else {
         String var8 = (String)var4;
         Double var5 = rawTonumber(var0.get(1));
         var3 = var2;
         if (var5 != null) {
            var3 = true;
         }

         luaAssert(var3, "Argument 2 must be a number");
         double var6 = LuaState.fromDouble(var5);
         var1 = (int)var6;
         if ((double)var1 != var6) {
            throw new RuntimeException("base is not an integer");
         }

         var0.push(tonumber(var8, var1));
      }

      return 1;
   }

   public static Double tonumber(String var0) {
      return tonumber((String)var0, 10);
   }

   public static Double tonumber(String var0, int var1) {
      if (var1 >= 2 && var1 <= 36) {
         boolean var10001;
         Double var2;
         Double var5;
         if (var1 == 10) {
            label48: {
               try {
                  var2 = Double.valueOf(var0);
               } catch (NumberFormatException var3) {
                  var10001 = false;
                  break label48;
               }

               var5 = var2;
               return var5;
            }
         } else {
            label49: {
               try {
                  var2 = LuaState.toDouble((long)Integer.parseInt(var0, var1));
               } catch (NumberFormatException var4) {
                  var10001 = false;
                  break label49;
               }

               var5 = var2;
               return var5;
            }
         }

         var0 = var0.toLowerCase();
         if (var0.endsWith("nan")) {
            var5 = LuaState.toDouble(Double.NaN);
         } else if (var0.endsWith("inf")) {
            if (var0.charAt(0) == '-') {
               var5 = LuaState.toDouble(Double.NEGATIVE_INFINITY);
            } else {
               var5 = LuaState.toDouble(Double.POSITIVE_INFINITY);
            }
         } else {
            var5 = null;
         }

         return var5;
      } else {
         throw new RuntimeException("base out of range");
      }
   }

   private static int tostring(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      luaAssert(var2, "Not enough arguments");
      var0.push(tostring(var0.get(0), var0.thread.state));
      return 1;
   }

   public static String tostring(Object var0, LuaState var1) {
      String var3;
      if (var0 == null) {
         var3 = "nil";
      } else if (var0 instanceof String) {
         var3 = (String)var0;
      } else if (var0 instanceof Double) {
         var3 = rawTostring(var0);
      } else if (var0 instanceof Boolean) {
         if (var0 == Boolean.TRUE) {
            var3 = "true";
         } else {
            var3 = "false";
         }
      } else if (var0 instanceof JavaFunction) {
         var3 = "function 0x" + System.identityHashCode(var0);
      } else if (var0 instanceof LuaClosure) {
         var3 = "function 0x" + System.identityHashCode(var0);
      } else {
         Object var2 = var1.getMetaOp(var0, "__tostring");
         if (var2 != null) {
            var3 = (String)var1.call(var2, var0, (Object)null, (Object)null);
         } else {
            if (!(var0 instanceof LuaTable)) {
               throw new RuntimeException("no __tostring found on object");
            }

            var3 = "table 0x" + System.identityHashCode(var0);
         }
      }

      return var3;
   }

   private static int type(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      luaAssert(var2, "Not enough arguments");
      var0.push(type(var0.get(0)));
      return 1;
   }

   public static String type(Object var0) {
      String var1;
      if (var0 == null) {
         var1 = "nil";
      } else if (var0 instanceof String) {
         var1 = "string";
      } else if (var0 instanceof Double) {
         var1 = "number";
      } else if (var0 instanceof Boolean) {
         var1 = "boolean";
      } else if (!(var0 instanceof JavaFunction) && !(var0 instanceof LuaClosure)) {
         if (var0 instanceof LuaTable) {
            var1 = "table";
         } else if (var0 instanceof LuaThread) {
            var1 = "thread";
         } else {
            var1 = "userdata";
         }
      } else {
         var1 = "function";
      }

      return var1;
   }

   private int unpack(LuaCallFrame var1, int var2) {
      byte var3 = 0;
      boolean var4;
      if (var2 >= 1) {
         var4 = true;
      } else {
         var4 = false;
      }

      luaAssert(var4, "Not enough arguments");
      LuaTable var5 = (LuaTable)var1.get(0);
      Object var6 = null;
      Object var7 = null;
      if (var2 >= 2) {
         var6 = var1.get(1);
      }

      if (var2 >= 3) {
         var7 = var1.get(2);
      }

      if (var6 != null) {
         var2 = (int)LuaState.fromDouble(var6);
      } else {
         var2 = 1;
      }

      int var8;
      if (var7 != null) {
         var8 = (int)LuaState.fromDouble(var7);
      } else {
         var8 = var5.len();
      }

      int var9 = var8 + 1 - var2;
      if (var9 <= 0) {
         var1.setTop(0);
         var2 = var3;
      } else {
         var1.setTop(var9);

         for(var8 = 0; var8 < var9; ++var8) {
            var1.set(var8, var5.rawget(LuaState.toDouble((long)(var2 + var8))));
         }

         var2 = var9;
      }

      return var2;
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.index) {
      case 0:
         var2 = pcall(var1, var2);
         break;
      case 1:
         var2 = print(var1, var2);
         break;
      case 2:
         var2 = select(var1, var2);
         break;
      case 3:
         var2 = type(var1, var2);
         break;
      case 4:
         var2 = tostring(var1, var2);
         break;
      case 5:
         var2 = tonumber(var1, var2);
         break;
      case 6:
         var2 = getmetatable(var1, var2);
         break;
      case 7:
         var2 = setmetatable(var1, var2);
         break;
      case 8:
         var2 = this.error(var1, var2);
         break;
      case 9:
         var2 = this.unpack(var1, var2);
         break;
      case 10:
         var2 = this.next(var1, var2);
         break;
      case 11:
         var2 = this.setfenv(var1, var2);
         break;
      case 12:
         var2 = this.getfenv(var1, var2);
         break;
      case 13:
         var2 = this.rawequal(var1, var2);
         break;
      case 14:
         var2 = this.rawset(var1, var2);
         break;
      case 15:
         var2 = this.rawget(var1, var2);
         break;
      case 16:
         var2 = collectgarbage(var1, var2);
         break;
      case 17:
         var2 = this.debugstacktrace(var1, var2);
         break;
      case 18:
         var2 = bytecodeloader(var1, var2);
         break;
      default:
         var2 = 0;
      }

      return var2;
   }

   public String toString() {
      return names[this.index];
   }
}

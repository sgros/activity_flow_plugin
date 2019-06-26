package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;
import se.krka.kahlua.vm.LuaTableImpl;

public final class TableLib implements JavaFunction {
   private static final int CONCAT = 0;
   private static final int INSERT = 1;
   private static final int MAXN = 3;
   private static final int NUM_FUNCTIONS = 4;
   private static final int REMOVE = 2;
   private static TableLib[] functions;
   private static final String[] names = new String[4];
   private int index;

   static {
      names[0] = "concat";
      names[1] = "insert";
      names[2] = "remove";
      names[3] = "maxn";
   }

   public TableLib(int var1) {
      this.index = var1;
   }

   public static void append(LuaState var0, LuaTable var1, Object var2) {
      var0.tableSet(var1, LuaState.toDouble((long)(var1.len() + 1)), var2);
   }

   private static int concat(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "expected table, got no arguments");
      LuaTable var3 = (LuaTable)var0.get(0);
      String var4 = "";
      if (var1 >= 2) {
         var4 = BaseLib.rawTostring(var0.get(1));
      }

      int var5 = 1;
      if (var1 >= 3) {
         var5 = BaseLib.rawTonumber(var0.get(2)).intValue();
      }

      if (var1 >= 4) {
         var1 = BaseLib.rawTonumber(var0.get(3)).intValue();
      } else {
         var1 = var3.len();
      }

      StringBuffer var6 = new StringBuffer();

      for(int var7 = var5; var7 <= var1; ++var7) {
         if (var7 > var5) {
            var6.append(var4);
         }

         var6.append(BaseLib.rawTostring(var3.rawget(LuaState.toDouble((long)var7))));
      }

      return var0.push(var6.toString());
   }

   public static boolean contains(LuaTable var0, Object var1) {
      boolean var2;
      if (find(var0, var1) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static void dumpTable(LuaTable var0) {
      System.out.print("table " + var0 + ": ");
      Object var1 = null;

      while(true) {
         var1 = var0.next(var1);
         if (var1 == null) {
            System.out.println();
            return;
         }

         System.out.print(var1.toString() + " => " + var0.rawget(var1) + ", ");
      }
   }

   public static Object find(LuaTable var0, Object var1) {
      Object var4;
      if (var1 == null) {
         var4 = null;
      } else {
         Object var2 = null;

         while(true) {
            Object var3 = var0.next(var2);
            if (var3 == null) {
               var4 = null;
               break;
            }

            var2 = var3;
            if (var1.equals(var0.rawget(var3))) {
               var4 = var3;
               break;
            }
         }
      }

      return var4;
   }

   private static void initFunctions() {
      synchronized(TableLib.class){}

      label97: {
         Throwable var10000;
         label101: {
            boolean var10001;
            try {
               if (functions != null) {
                  break label97;
               }

               functions = new TableLib[4];
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label101;
            }

            int var0 = 0;

            while(true) {
               if (var0 >= 4) {
                  break label97;
               }

               try {
                  functions[var0] = new TableLib(var0);
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

   private static int insert(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      LuaTable var3 = (LuaTable)var0.get(0);
      int var4 = var3.len() + 1;
      Object var5;
      if (var1 > 2) {
         var1 = BaseLib.rawTonumber(var0.get(1)).intValue();
         var5 = var0.get(2);
      } else {
         var5 = var0.get(1);
         var1 = var4;
      }

      insert(var0.thread.state, var3, var1, var5);
      return 0;
   }

   public static void insert(LuaState var0, LuaTable var1, int var2, Object var3) {
      for(int var4 = var1.len(); var4 >= var2; --var4) {
         var0.tableSet(var1, LuaState.toDouble((long)(var4 + 1)), var0.tableGet(var1, LuaState.toDouble((long)var4)));
      }

      var0.tableSet(var1, LuaState.toDouble((long)var2), var3);
   }

   public static void insert(LuaState var0, LuaTable var1, Object var2) {
      append(var0, var1, var2);
   }

   private static int maxn(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "expected table, got no arguments");
      LuaTable var3 = (LuaTable)var0.get(0);
      Object var4 = null;
      var1 = 0;

      while(true) {
         Object var5 = var3.next(var4);
         if (var5 == null) {
            var0.push(LuaState.toDouble((long)var1));
            return 1;
         }

         var4 = var5;
         if (var5 instanceof Double) {
            int var6 = (int)LuaState.fromDouble(var5);
            var4 = var5;
            if (var6 > var1) {
               var1 = var6;
               var4 = var5;
            }
         }
      }
   }

   public static void rawappend(LuaTable var0, Object var1) {
      var0.rawset(LuaState.toDouble((long)(var0.len() + 1)), var1);
   }

   public static void rawinsert(LuaTable var0, int var1, Object var2) {
      int var3 = var0.len();
      if (var1 <= var3) {
         Double var4;
         for(var4 = LuaState.toDouble((long)(var3 + 1)); var3 >= var1; --var3) {
            Double var5 = LuaState.toDouble((long)var3);
            var0.rawset(var4, var0.rawget(var5));
            var4 = var5;
         }

         var0.rawset(var4, var2);
      } else {
         var0.rawset(LuaState.toDouble((long)var1), var2);
      }

   }

   public static Object rawremove(LuaTable var0, int var1) {
      Object var2 = var0.rawget(LuaState.toDouble((long)var1));

      for(int var3 = var0.len(); var1 <= var3; ++var1) {
         var0.rawset(LuaState.toDouble((long)var1), var0.rawget(LuaState.toDouble((long)(var1 + 1))));
      }

      return var2;
   }

   public static void register(LuaState var0) {
      initFunctions();
      LuaTableImpl var1 = new LuaTableImpl();
      var0.getEnvironment().rawset("table", var1);

      for(int var2 = 0; var2 < 4; ++var2) {
         var1.rawset(names[var2], functions[var2]);
      }

   }

   private static int remove(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "expected table, got no arguments");
      LuaTable var3 = (LuaTable)var0.get(0);
      int var4 = var3.len();
      if (var1 > 1) {
         var4 = BaseLib.rawTonumber(var0.get(1)).intValue();
      }

      var0.push(remove(var0.thread.state, var3, var4));
      return 1;
   }

   public static Object remove(LuaState var0, LuaTable var1) {
      return remove(var0, var1, var1.len());
   }

   public static Object remove(LuaState var0, LuaTable var1, int var2) {
      Object var3 = var0.tableGet(var1, LuaState.toDouble((long)var2));

      int var4;
      for(var4 = var1.len(); var2 < var4; ++var2) {
         var0.tableSet(var1, LuaState.toDouble((long)var2), var0.tableGet(var1, LuaState.toDouble((long)(var2 + 1))));
      }

      var0.tableSet(var1, LuaState.toDouble((long)var4), (Object)null);
      return var3;
   }

   public static void removeItem(LuaTable var0, Object var1) {
      if (var1 != null) {
         Object var2 = null;

         while(true) {
            Object var3 = var0.next(var2);
            if (var3 == null) {
               break;
            }

            var2 = var3;
            if (var1.equals(var0.rawget(var3))) {
               if (var3 instanceof Double) {
                  double var4 = (Double)var3;
                  int var6 = (int)var4;
                  if (var4 == (double)var6) {
                     rawremove(var0, var6);
                  }
               } else {
                  var0.rawset(var3, (Object)null);
               }
               break;
            }
         }
      }

   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.index) {
      case 0:
         var2 = concat(var1, var2);
         break;
      case 1:
         var2 = insert(var1, var2);
         break;
      case 2:
         var2 = remove(var1, var2);
         break;
      case 3:
         var2 = maxn(var1, var2);
         break;
      default:
         var2 = 0;
      }

      return var2;
   }

   public String toString() {
      String var1;
      if (this.index < names.length) {
         var1 = "table." + names[this.index];
      } else {
         var1 = super.toString();
      }

      return var1;
   }
}

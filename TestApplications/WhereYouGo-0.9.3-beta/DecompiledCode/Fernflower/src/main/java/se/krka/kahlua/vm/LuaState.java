package se.krka.kahlua.vm;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;
import se.krka.kahlua.stdlib.BaseLib;
import se.krka.kahlua.stdlib.CoroutineLib;
import se.krka.kahlua.stdlib.MathLib;
import se.krka.kahlua.stdlib.OsLib;
import se.krka.kahlua.stdlib.StringLib;
import se.krka.kahlua.stdlib.TableLib;

public class LuaState {
   public static final int FIELDS_PER_FLUSH = 50;
   static final int MAX_INDEX_RECURSION = 100;
   public static final int OP_ADD = 12;
   public static final int OP_CALL = 28;
   public static final int OP_CLOSE = 35;
   public static final int OP_CLOSURE = 36;
   public static final int OP_CONCAT = 21;
   public static final int OP_DIV = 15;
   public static final int OP_EQ = 23;
   public static final int OP_FORLOOP = 31;
   public static final int OP_FORPREP = 32;
   public static final int OP_GETGLOBAL = 5;
   public static final int OP_GETTABLE = 6;
   public static final int OP_GETUPVAL = 4;
   public static final int OP_JMP = 22;
   public static final int OP_LE = 25;
   public static final int OP_LEN = 20;
   public static final int OP_LOADBOOL = 2;
   public static final int OP_LOADK = 1;
   public static final int OP_LOADNIL = 3;
   public static final int OP_LT = 24;
   public static final int OP_MOD = 16;
   public static final int OP_MOVE = 0;
   public static final int OP_MUL = 14;
   public static final int OP_NEWTABLE = 10;
   public static final int OP_NOT = 19;
   public static final int OP_POW = 17;
   public static final int OP_RETURN = 30;
   public static final int OP_SELF = 11;
   public static final int OP_SETGLOBAL = 7;
   public static final int OP_SETLIST = 34;
   public static final int OP_SETTABLE = 9;
   public static final int OP_SETUPVAL = 8;
   public static final int OP_SUB = 13;
   public static final int OP_TAILCALL = 29;
   public static final int OP_TEST = 26;
   public static final int OP_TESTSET = 27;
   public static final int OP_TFORLOOP = 33;
   public static final int OP_UNM = 18;
   public static final int OP_VARARG = 37;
   private static final String[] meta_ops = new String[38];
   private final LuaTable classMetatables;
   public LuaThread currentThread;
   protected final PrintStream out;
   public final Random random;
   private final LuaTable userdataMetatables;

   static {
      meta_ops[12] = "__add";
      meta_ops[13] = "__sub";
      meta_ops[14] = "__mul";
      meta_ops[15] = "__div";
      meta_ops[16] = "__mod";
      meta_ops[17] = "__pow";
      meta_ops[23] = "__eq";
      meta_ops[24] = "__lt";
      meta_ops[25] = "__le";
   }

   public LuaState() {
      this(System.out, true);
   }

   public LuaState(PrintStream var1) {
      this(var1, true);
   }

   protected LuaState(PrintStream var1, boolean var2) {
      this.random = new Random();
      LuaTableImpl var3 = new LuaTableImpl();
      var3.rawset("__mode", "k");
      this.userdataMetatables = new LuaTableImpl();
      this.userdataMetatables.setMetatable(var3);
      this.classMetatables = new LuaTableImpl();
      this.out = var1;
      if (var2) {
         this.reset();
      }

   }

   public static boolean boolEval(Object var0) {
      boolean var1;
      if (var0 != null && var0 != Boolean.FALSE) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private int callJava(JavaFunction var1, int var2, int var3, int var4) {
      LuaThread var5 = this.currentThread;
      LuaCallFrame var6 = var5.pushNewCallFrame((LuaClosure)null, var1, var2, var3, var4, false, false);
      int var7 = var1.call(var6, var4);
      var4 = var6.getTop();
      var2 = var3 - var2;
      var6.stackCopy(var4 - var7, var2, var7);
      var6.setTop(var7 + var2);
      var5.popCallFrame();
      return var7;
   }

   public static double fromDouble(Object var0) {
      return (Double)var0;
   }

   private static final int getA8(int var0) {
      return var0 >>> 6 & 255;
   }

   private static final int getB9(int var0) {
      return var0 >>> 23 & 511;
   }

   private final Object getBinMetaOp(Object var1, Object var2, String var3) {
      var1 = this.getMetaOp(var1, var3);
      if (var1 == null) {
         var1 = this.getMetaOp(var2, var3);
      }

      return var1;
   }

   private static final int getBx(int var0) {
      return var0 >>> 14;
   }

   private static final int getC9(int var0) {
      return var0 >>> 14 & 511;
   }

   private final Object getCompMetaOp(Object var1, Object var2, String var3) {
      LuaTable var4 = (LuaTable)this.getmetatable(var1, true);
      if (var4 == (LuaTable)this.getmetatable(var2, true) && var4 != null) {
         var1 = var4.rawget(var3);
      } else {
         var1 = null;
      }

      return var1;
   }

   private final Object getRegisterOrConstant(LuaCallFrame var1, int var2, LuaPrototype var3) {
      int var4 = var2 - 256;
      Object var5;
      if (var4 < 0) {
         var5 = var1.get(var2);
      } else {
         var5 = var3.constants[var4];
      }

      return var5;
   }

   private static final int getSBx(int var0) {
      return (var0 >>> 14) - 131071;
   }

   public static boolean luaEquals(Object var0, Object var1) {
      boolean var2 = true;
      if (var0 != null && var1 != null) {
         if (var0 instanceof Double && var1 instanceof Double) {
            Double var3 = (Double)var0;
            Double var4 = (Double)var1;
            if (var3 != var4) {
               var2 = false;
            }
         } else if (var0 != var1) {
            var2 = false;
         }
      } else if (var0 != var1) {
         var2 = false;
      }

      return var2;
   }

   private final void luaMainloop() {
      LuaCallFrame var1 = this.currentThread.currentCallFrame();
      LuaClosure var2 = var1.closure;
      LuaPrototype var3 = var2.prototype;
      int[] var4 = var3.code;
      int var5 = var1.returnBase;

      label2579:
      while(true) {
         LuaClosure var6 = var2;
         int[] var7 = var4;
         LuaPrototype var8 = var3;
         int var9 = var5;

         RuntimeException var10000;
         boolean var340;
         label2663: {
            int var10;
            boolean var10001;
            try {
               var10 = var1.pc;
            } catch (RuntimeException var317) {
               var10000 = var317;
               var10001 = false;
               break label2663;
            }

            var6 = var2;
            var7 = var4;
            var8 = var3;
            var9 = var5;

            try {
               var1.pc = var10 + 1;
            } catch (RuntimeException var316) {
               var10000 = var316;
               var10001 = false;
               break label2663;
            }

            Object var323;
            StringBuilder var329;
            label2664: {
               int var11;
               int var14;
               int var15;
               LuaCallFrame var330;
               label2560: {
                  var10 = var4[var10];
                  var11 = var10 & 63;
                  LuaPrototype var12;
                  LuaClosure var13;
                  LuaCallFrame var17;
                  boolean var19;
                  int var20;
                  double var21;
                  double var23;
                  boolean var25;
                  Object var327;
                  StringBuilder var331;
                  Object var332;
                  Object var334;
                  switch(var11) {
                  case 0:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(getA8(var10), var1.get(getB9(var10)));
                        continue;
                     } catch (RuntimeException var241) {
                        var10000 = var241;
                        var10001 = false;
                        break label2663;
                     }
                  case 1:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var240) {
                        var10000 = var240;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getBx(var10);
                     } catch (RuntimeException var239) {
                        var10000 = var239;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var14, var3.constants[var10]);
                        continue;
                     } catch (RuntimeException var238) {
                        var10000 = var238;
                        var10001 = false;
                        break label2663;
                     }
                  case 2:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var237) {
                        var10000 = var237;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var15 = getB9(var10);
                     } catch (RuntimeException var236) {
                        var10000 = var236;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getC9(var10);
                     } catch (RuntimeException var235) {
                        var10000 = var235;
                        var10001 = false;
                        break label2663;
                     }

                     Boolean var346;
                     if (var15 == 0) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var346 = Boolean.FALSE;
                        } catch (RuntimeException var234) {
                           var10000 = var234;
                           var10001 = false;
                           break label2663;
                        }
                     } else {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var346 = Boolean.TRUE;
                        } catch (RuntimeException var233) {
                           var10000 = var233;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var14, var346);
                     } catch (RuntimeException var232) {
                        var10000 = var232;
                        var10001 = false;
                        break label2663;
                     }

                     if (var10 == 0) {
                        continue;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        ++var1.pc;
                        continue;
                     } catch (RuntimeException var231) {
                        var10000 = var231;
                        var10001 = false;
                        break label2663;
                     }
                  case 3:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.stackClear(getA8(var10), getB9(var10));
                        continue;
                     } catch (RuntimeException var230) {
                        var10000 = var230;
                        var10001 = false;
                        break label2663;
                     }
                  case 4:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var229) {
                        var10000 = var229;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getB9(var10);
                     } catch (RuntimeException var228) {
                        var10000 = var228;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var14, var2.upvalues[var10].getValue());
                        continue;
                     } catch (RuntimeException var227) {
                        var10000 = var227;
                        var10001 = false;
                        break label2663;
                     }
                  case 5:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var226) {
                        var10000 = var226;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getBx(var10);
                     } catch (RuntimeException var225) {
                        var10000 = var225;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var14, this.tableGet(var2.env, var3.constants[var10]));
                        continue;
                     } catch (RuntimeException var224) {
                        var10000 = var224;
                        var10001 = false;
                        break label2663;
                     }
                  case 6:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var223) {
                        var10000 = var223;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var15 = getB9(var10);
                     } catch (RuntimeException var222) {
                        var10000 = var222;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getC9(var10);
                     } catch (RuntimeException var221) {
                        var10000 = var221;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var14, this.tableGet(var1.get(var15), this.getRegisterOrConstant(var1, var10, var3)));
                        continue;
                     } catch (RuntimeException var220) {
                        var10000 = var220;
                        var10001 = false;
                        break label2663;
                     }
                  case 7:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var219) {
                        var10000 = var219;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getBx(var10);
                     } catch (RuntimeException var218) {
                        var10000 = var218;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var327 = var1.get(var14);
                     } catch (RuntimeException var217) {
                        var10000 = var217;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var323 = var3.constants[var10];
                     } catch (RuntimeException var216) {
                        var10000 = var216;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        this.tableSet(var2.env, var323, var327);
                        continue;
                     } catch (RuntimeException var215) {
                        var10000 = var215;
                        var10001 = false;
                        break label2663;
                     }
                  case 8:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var214) {
                        var10000 = var214;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getB9(var10);
                     } catch (RuntimeException var213) {
                        var10000 = var213;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var2.upvalues[var10].setValue(var1.get(var14));
                        continue;
                     } catch (RuntimeException var212) {
                        var10000 = var212;
                        var10001 = false;
                        break label2663;
                     }
                  case 9:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var15 = getA8(var10);
                     } catch (RuntimeException var211) {
                        var10000 = var211;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getB9(var10);
                     } catch (RuntimeException var210) {
                        var10000 = var210;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getC9(var10);
                     } catch (RuntimeException var209) {
                        var10000 = var209;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        this.tableSet(var1.get(var15), this.getRegisterOrConstant(var1, var14, var3), this.getRegisterOrConstant(var1, var10, var3));
                        continue;
                     } catch (RuntimeException var208) {
                        var10000 = var208;
                        var10001 = false;
                        break label2663;
                     }
                  case 10:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getA8(var10);
                     } catch (RuntimeException var207) {
                        var10000 = var207;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     LuaTableImpl var345;
                     try {
                        var345 = new LuaTableImpl;
                     } catch (RuntimeException var206) {
                        var10000 = var206;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var345.<init>();
                     } catch (RuntimeException var205) {
                        var10000 = var205;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var10, var345);
                        continue;
                     } catch (RuntimeException var204) {
                        var10000 = var204;
                        var10001 = false;
                        break label2663;
                     }
                  case 11:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var15 = getA8(var10);
                     } catch (RuntimeException var203) {
                        var10000 = var203;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getB9(var10);
                     } catch (RuntimeException var202) {
                        var10000 = var202;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var323 = this.getRegisterOrConstant(var1, getC9(var10), var3);
                     } catch (RuntimeException var201) {
                        var10000 = var201;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var327 = var1.get(var14);
                     } catch (RuntimeException var200) {
                        var10000 = var200;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var15, this.tableGet(var327, var323));
                     } catch (RuntimeException var199) {
                        var10000 = var199;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var15 + 1, var327);
                        continue;
                     } catch (RuntimeException var198) {
                        var10000 = var198;
                        var10001 = false;
                        break label2663;
                     }
                  case 12:
                  case 13:
                  case 14:
                  case 15:
                  case 16:
                  case 17:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var15 = getA8(var10);
                     } catch (RuntimeException var197) {
                        var10000 = var197;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getB9(var10);
                     } catch (RuntimeException var196) {
                        var10000 = var196;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getC9(var10);
                     } catch (RuntimeException var195) {
                        var10000 = var195;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var327 = this.getRegisterOrConstant(var1, var14, var3);
                     } catch (RuntimeException var194) {
                        var10000 = var194;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var323 = this.getRegisterOrConstant(var1, var10, var3);
                     } catch (RuntimeException var193) {
                        var10000 = var193;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     Double var339;
                     try {
                        var339 = BaseLib.rawTonumber(var327);
                     } catch (RuntimeException var192) {
                        var10000 = var192;
                        var10001 = false;
                        break label2663;
                     }

                     label2628: {
                        if (var339 != null) {
                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           Double var336;
                           try {
                              var336 = BaseLib.rawTonumber(var323);
                           } catch (RuntimeException var191) {
                              var10000 = var191;
                              var10001 = false;
                              break label2663;
                           }

                           if (var336 != null) {
                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 var323 = this.primitiveMath(var339, var336, var11);
                                 break label2628;
                              } catch (RuntimeException var184) {
                                 var10000 = var184;
                                 var10001 = false;
                                 break label2663;
                              }
                           }
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        String var342;
                        try {
                           var342 = meta_ops[var11];
                        } catch (RuntimeException var190) {
                           var10000 = var190;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var334 = this.getBinMetaOp(var327, var323, var342);
                        } catch (RuntimeException var189) {
                           var10000 = var189;
                           var10001 = false;
                           break label2663;
                        }

                        if (var334 == null) {
                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var331 = new StringBuilder;
                           } catch (RuntimeException var188) {
                              var10000 = var188;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var331.<init>();
                           } catch (RuntimeException var187) {
                              var10000 = var187;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              BaseLib.fail(var331.append(var342).append(" not defined for operands").toString());
                           } catch (RuntimeException var186) {
                              var10000 = var186;
                              var10001 = false;
                              break label2663;
                           }
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var323 = this.call(var334, var327, var323, (Object)null);
                        } catch (RuntimeException var185) {
                           var10000 = var185;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var15, var323);
                        continue;
                     } catch (RuntimeException var183) {
                        var10000 = var183;
                        var10001 = false;
                        break label2663;
                     }
                  case 18:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var182) {
                        var10000 = var182;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var323 = var1.get(getB9(var10));
                     } catch (RuntimeException var181) {
                        var10000 = var181;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     Double var338;
                     try {
                        var338 = BaseLib.rawTonumber(var323);
                     } catch (RuntimeException var180) {
                        var10000 = var180;
                        var10001 = false;
                        break label2663;
                     }

                     if (var338 != null) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var323 = toDouble(-fromDouble(var338));
                        } catch (RuntimeException var179) {
                           var10000 = var179;
                           var10001 = false;
                           break label2663;
                        }
                     } else {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var323 = this.call(this.getMetaOp(var323, "__unm"), var323, (Object)null, (Object)null);
                        } catch (RuntimeException var178) {
                           var10000 = var178;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var14, var323);
                        continue;
                     } catch (RuntimeException var177) {
                        var10000 = var177;
                        var10001 = false;
                        break label2663;
                     }
                  case 19:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var176) {
                        var10000 = var176;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     label2027: {
                        label2026: {
                           try {
                              if (boolEval(var1.get(getB9(var10)))) {
                                 break label2026;
                              }
                           } catch (RuntimeException var175) {
                              var10000 = var175;
                              var10001 = false;
                              break label2663;
                           }

                           var19 = true;
                           break label2027;
                        }

                        var19 = false;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var14, toBoolean(var19));
                        continue;
                     } catch (RuntimeException var174) {
                        var10000 = var174;
                        var10001 = false;
                        break label2663;
                     }
                  case 20:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var173) {
                        var10000 = var173;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var327 = var1.get(getB9(var10));
                     } catch (RuntimeException var172) {
                        var10000 = var172;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     label2620: {
                        label2621: {
                           try {
                              if (!(var327 instanceof LuaTable)) {
                                 break label2621;
                              }
                           } catch (RuntimeException var171) {
                              var10000 = var171;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var323 = toDouble((long)((LuaTable)var327).len());
                              break label2620;
                           } catch (RuntimeException var169) {
                              var10000 = var169;
                              var10001 = false;
                              break label2663;
                           }
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        label2622: {
                           try {
                              if (!(var327 instanceof String)) {
                                 break label2622;
                              }
                           } catch (RuntimeException var170) {
                              var10000 = var170;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var323 = toDouble((long)((String)var327).length());
                              break label2620;
                           } catch (RuntimeException var168) {
                              var10000 = var168;
                              var10001 = false;
                              break label2663;
                           }
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var323 = this.getMetaOp(var327, "__len");
                        } catch (RuntimeException var167) {
                           var10000 = var167;
                           var10001 = false;
                           break label2663;
                        }

                        if (var323 != null) {
                           var19 = true;
                        } else {
                           var19 = false;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           BaseLib.luaAssert(var19, "__len not defined for operand");
                        } catch (RuntimeException var166) {
                           var10000 = var166;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var323 = this.call(var323, var327, (Object)null, (Object)null);
                        } catch (RuntimeException var165) {
                           var10000 = var165;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var14, var323);
                        continue;
                     } catch (RuntimeException var164) {
                        var10000 = var164;
                        var10001 = false;
                        break label2663;
                     }
                  case 21:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var20 = getA8(var10);
                     } catch (RuntimeException var163) {
                        var10000 = var163;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var11 = getB9(var10);
                     } catch (RuntimeException var162) {
                        var10000 = var162;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getC9(var10);
                     } catch (RuntimeException var161) {
                        var10000 = var161;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var323 = var1.get(var10);
                     } catch (RuntimeException var160) {
                        var10000 = var160;
                        var10001 = false;
                        break label2663;
                     }

                     --var10;

                     while(var11 <= var10) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        String var335;
                        try {
                           var335 = BaseLib.rawTostring(var323);
                        } catch (RuntimeException var158) {
                           var10000 = var158;
                           var10001 = false;
                           break label2663;
                        }

                        var15 = var10;
                        var327 = var323;
                        if (var323 != null) {
                           var14 = 0;

                           for(var15 = var10; var11 <= var15; ++var14) {
                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 var327 = var1.get(var15);
                              } catch (RuntimeException var157) {
                                 var10000 = var157;
                                 var10001 = false;
                                 break label2663;
                              }

                              --var15;
                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 if (BaseLib.rawTostring(var327) == null) {
                                    break;
                                 }
                              } catch (RuntimeException var159) {
                                 var10000 = var159;
                                 var10001 = false;
                                 break label2663;
                              }
                           }

                           var15 = var10;
                           var327 = var323;
                           if (var14 > 0) {
                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              StringBuffer var337;
                              try {
                                 var337 = new StringBuffer;
                              } catch (RuntimeException var156) {
                                 var10000 = var156;
                                 var10001 = false;
                                 break label2663;
                              }

                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 var337.<init>();
                              } catch (RuntimeException var155) {
                                 var10000 = var155;
                                 var10001 = false;
                                 break label2663;
                              }

                              for(var15 = var10 - var14 + 1; var15 <= var10; ++var15) {
                                 var6 = var2;
                                 var7 = var4;
                                 var8 = var3;
                                 var9 = var5;

                                 try {
                                    var337.append(BaseLib.rawTostring(var1.get(var15)));
                                 } catch (RuntimeException var154) {
                                    var10000 = var154;
                                    var10001 = false;
                                    break label2663;
                                 }
                              }

                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 var337.append(var335);
                              } catch (RuntimeException var153) {
                                 var10000 = var153;
                                 var10001 = false;
                                 break label2663;
                              }

                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 var327 = var337.toString();
                              } catch (RuntimeException var152) {
                                 var10000 = var152;
                                 var10001 = false;
                                 break label2663;
                              }

                              var15 = var10 - var14;
                           }
                        }

                        var10 = var15;
                        var323 = var327;
                        if (var11 <= var15) {
                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var334 = var1.get(var15);
                           } catch (RuntimeException var151) {
                              var10000 = var151;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var332 = this.getBinMetaOp(var334, var327, "__concat");
                           } catch (RuntimeException var150) {
                              var10000 = var150;
                              var10001 = false;
                              break label2663;
                           }

                           if (var332 == null) {
                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              StringBuilder var341;
                              try {
                                 var341 = new StringBuilder;
                              } catch (RuntimeException var149) {
                                 var10000 = var149;
                                 var10001 = false;
                                 break label2663;
                              }

                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 var341.<init>();
                              } catch (RuntimeException var148) {
                                 var10000 = var148;
                                 var10001 = false;
                                 break label2663;
                              }

                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 BaseLib.fail(var341.append("__concat not defined for operands: ").append(var334).append(" and ").append(var327).toString());
                              } catch (RuntimeException var147) {
                                 var10000 = var147;
                                 var10001 = false;
                                 break label2663;
                              }
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var323 = this.call(var332, var334, var327, (Object)null);
                           } catch (RuntimeException var146) {
                              var10000 = var146;
                              var10001 = false;
                              break label2663;
                           }

                           var10 = var15 - 1;
                        }
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var20, var323);
                        continue;
                     } catch (RuntimeException var145) {
                        var10000 = var145;
                        var10001 = false;
                        break label2663;
                     }
                  case 22:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.pc += getSBx(var10);
                        continue;
                     } catch (RuntimeException var144) {
                        var10000 = var144;
                        var10001 = false;
                        break label2663;
                     }
                  case 23:
                  case 24:
                  case 25:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var15 = getA8(var10);
                     } catch (RuntimeException var143) {
                        var10000 = var143;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getB9(var10);
                     } catch (RuntimeException var142) {
                        var10000 = var142;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getC9(var10);
                     } catch (RuntimeException var141) {
                        var10000 = var141;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var332 = this.getRegisterOrConstant(var1, var14, var3);
                     } catch (RuntimeException var140) {
                        var10000 = var140;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var334 = this.getRegisterOrConstant(var1, var10, var3);
                     } catch (RuntimeException var139) {
                        var10000 = var139;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     boolean var348;
                     label2605: {
                        try {
                           if (!(var332 instanceof Double)) {
                              break label2605;
                           }
                        } catch (RuntimeException var138) {
                           var10000 = var138;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           if (!(var334 instanceof Double)) {
                              break label2605;
                           }
                        } catch (RuntimeException var137) {
                           var10000 = var137;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var21 = fromDouble(var332);
                        } catch (RuntimeException var120) {
                           var10000 = var120;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var23 = fromDouble(var334);
                        } catch (RuntimeException var119) {
                           var10000 = var119;
                           var10001 = false;
                           break label2663;
                        }

                        if (var11 == 23) {
                           if (var21 == var23) {
                              var348 = true;
                           } else {
                              var348 = false;
                           }

                           if (var15 == 0) {
                              var340 = true;
                           } else {
                              var340 = false;
                           }

                           if (var348 != var340) {
                              continue;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              ++var1.pc;
                              continue;
                           } catch (RuntimeException var116) {
                              var10000 = var116;
                              var10001 = false;
                              break label2663;
                           }
                        } else if (var11 == 24) {
                           if (var21 < var23) {
                              var348 = true;
                           } else {
                              var348 = false;
                           }

                           if (var15 == 0) {
                              var340 = true;
                           } else {
                              var340 = false;
                           }

                           if (var348 != var340) {
                              continue;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              ++var1.pc;
                              continue;
                           } catch (RuntimeException var117) {
                              var10000 = var117;
                              var10001 = false;
                              break label2663;
                           }
                        } else {
                           if (var21 <= var23) {
                              var348 = true;
                           } else {
                              var348 = false;
                           }

                           if (var15 == 0) {
                              var340 = true;
                           } else {
                              var340 = false;
                           }

                           if (var348 != var340) {
                              continue;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              ++var1.pc;
                              continue;
                           } catch (RuntimeException var118) {
                              var10000 = var118;
                              var10001 = false;
                              break label2663;
                           }
                        }
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     label2609: {
                        label2610: {
                           try {
                              if (!(var332 instanceof String)) {
                                 break label2610;
                              }
                           } catch (RuntimeException var136) {
                              var10000 = var136;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              if (var334 instanceof String) {
                                 break label2609;
                              }
                           } catch (RuntimeException var135) {
                              var10000 = var135;
                              var10001 = false;
                              break label2663;
                           }
                        }

                        if (var332 == var334) {
                           var19 = true;
                        } else {
                           boolean var344 = false;
                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           String var26;
                           try {
                              var26 = meta_ops[var11];
                           } catch (RuntimeException var134) {
                              var10000 = var134;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           Object var27;
                           try {
                              var27 = this.getCompMetaOp(var332, var334, var26);
                           } catch (RuntimeException var133) {
                              var10000 = var133;
                              var10001 = false;
                              break label2663;
                           }

                           Object var18 = var332;
                           var327 = var334;
                           var340 = var344;
                           var323 = var27;
                           if (var27 == null) {
                              var18 = var332;
                              var327 = var334;
                              var340 = var344;
                              var323 = var27;
                              if (var11 == 25) {
                                 var6 = var2;
                                 var7 = var4;
                                 var8 = var3;
                                 var9 = var5;

                                 try {
                                    var323 = this.getCompMetaOp(var332, var334, "__lt");
                                 } catch (RuntimeException var132) {
                                    var10000 = var132;
                                    var10001 = false;
                                    break label2663;
                                 }

                                 var327 = var332;
                                 var340 = true;
                                 var18 = var334;
                              }
                           }

                           if (var323 == null && var11 == 23) {
                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 var25 = luaEquals(var18, var327);
                              } catch (RuntimeException var131) {
                                 var10000 = var131;
                                 var10001 = false;
                                 break label2663;
                              }
                           } else {
                              if (var323 == null) {
                                 var6 = var2;
                                 var7 = var4;
                                 var8 = var3;
                                 var9 = var5;

                                 try {
                                    var331 = new StringBuilder;
                                 } catch (RuntimeException var130) {
                                    var10000 = var130;
                                    var10001 = false;
                                    break label2663;
                                 }

                                 var6 = var2;
                                 var7 = var4;
                                 var8 = var3;
                                 var9 = var5;

                                 try {
                                    var331.<init>();
                                 } catch (RuntimeException var129) {
                                    var10000 = var129;
                                    var10001 = false;
                                    break label2663;
                                 }

                                 var6 = var2;
                                 var7 = var4;
                                 var8 = var3;
                                 var9 = var5;

                                 try {
                                    BaseLib.fail(var331.append(var26).append(" not defined for operand").toString());
                                 } catch (RuntimeException var128) {
                                    var10000 = var128;
                                    var10001 = false;
                                    break label2663;
                                 }
                              }

                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 var25 = boolEval(this.call(var323, var18, var327, (Object)null));
                              } catch (RuntimeException var127) {
                                 var10000 = var127;
                                 var10001 = false;
                                 break label2663;
                              }
                           }

                           var19 = var25;
                           if (var340) {
                              if (!var25) {
                                 var19 = true;
                              } else {
                                 var19 = false;
                              }
                           }
                        }

                        if (var15 == 0) {
                           var25 = true;
                        } else {
                           var25 = false;
                        }

                        if (var19 != var25) {
                           continue;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           ++var1.pc;
                           continue;
                        } catch (RuntimeException var126) {
                           var10000 = var126;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     if (var11 == 23) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var25 = var332.equals(var334);
                        } catch (RuntimeException var122) {
                           var10000 = var122;
                           var10001 = false;
                           break label2663;
                        }

                        if (var15 == 0) {
                           var19 = true;
                        } else {
                           var19 = false;
                        }

                        if (var25 != var19) {
                           continue;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           ++var1.pc;
                        } catch (RuntimeException var121) {
                           var10000 = var121;
                           var10001 = false;
                           break label2663;
                        }
                     } else {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var10 = ((String)var332).compareTo((String)var334);
                        } catch (RuntimeException var125) {
                           var10000 = var125;
                           var10001 = false;
                           break label2663;
                        }

                        if (var11 == 24) {
                           if (var10 < 0) {
                              var348 = true;
                           } else {
                              var348 = false;
                           }

                           if (var15 == 0) {
                              var340 = true;
                           } else {
                              var340 = false;
                           }

                           if (var348 != var340) {
                              continue;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              ++var1.pc;
                           } catch (RuntimeException var123) {
                              var10000 = var123;
                              var10001 = false;
                              break label2663;
                           }
                        } else {
                           if (var10 <= 0) {
                              var348 = true;
                           } else {
                              var348 = false;
                           }

                           if (var15 == 0) {
                              var340 = true;
                           } else {
                              var340 = false;
                           }

                           if (var348 != var340) {
                              continue;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              ++var1.pc;
                           } catch (RuntimeException var124) {
                              var10000 = var124;
                              var10001 = false;
                              break label2663;
                           }
                        }
                     }
                     continue;
                  case 26:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var115) {
                        var10000 = var115;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getC9(var10);
                     } catch (RuntimeException var114) {
                        var10000 = var114;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var25 = boolEval(var1.get(var14));
                     } catch (RuntimeException var113) {
                        var10000 = var113;
                        var10001 = false;
                        break label2663;
                     }

                     if (var10 == 0) {
                        var19 = true;
                     } else {
                        var19 = false;
                     }

                     if (var25 != var19) {
                        continue;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        ++var1.pc;
                        continue;
                     } catch (RuntimeException var112) {
                        var10000 = var112;
                        var10001 = false;
                        break label2663;
                     }
                  case 27:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var111) {
                        var10000 = var111;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var15 = getB9(var10);
                     } catch (RuntimeException var110) {
                        var10000 = var110;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getC9(var10);
                     } catch (RuntimeException var109) {
                        var10000 = var109;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var323 = var1.get(var15);
                     } catch (RuntimeException var108) {
                        var10000 = var108;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var25 = boolEval(var323);
                     } catch (RuntimeException var107) {
                        var10000 = var107;
                        var10001 = false;
                        break label2663;
                     }

                     if (var10 == 0) {
                        var19 = true;
                     } else {
                        var19 = false;
                     }

                     if (var25 != var19) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var1.set(var14, var323);
                        } catch (RuntimeException var105) {
                           var10000 = var105;
                           var10001 = false;
                           break label2663;
                        }
                     } else {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           ++var1.pc;
                        } catch (RuntimeException var106) {
                           var10000 = var106;
                           var10001 = false;
                           break label2663;
                        }
                     }
                     continue;
                  case 28:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var300) {
                        var10000 = var300;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var11 = getB9(var10);
                     } catch (RuntimeException var299) {
                        var10000 = var299;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var15 = getC9(var10);
                     } catch (RuntimeException var298) {
                        var10000 = var298;
                        var10001 = false;
                        break label2663;
                     }

                     var10 = var11 - 1;
                     if (var10 != -1) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var1.setTop(var14 + var10 + 1);
                        } catch (RuntimeException var297) {
                           var10000 = var297;
                           var10001 = false;
                           break label2663;
                        }
                     } else {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var10 = var1.getTop() - var14 - 1;
                        } catch (RuntimeException var296) {
                           var10000 = var296;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     if (var15 != 0) {
                        var19 = true;
                     } else {
                        var19 = false;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.restoreTop = var19;
                     } catch (RuntimeException var295) {
                        var10000 = var295;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var11 = var1.localBase;
                     } catch (RuntimeException var294) {
                        var10000 = var294;
                        var10001 = false;
                        break label2663;
                     }

                     var15 = var11 + var14 + 1;
                     var11 += var14;
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var332 = var1.get(var14);
                     } catch (RuntimeException var293) {
                        var10000 = var293;
                        var10001 = false;
                        break label2663;
                     }

                     if (var332 != null) {
                        var19 = true;
                     } else {
                        var19 = false;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        BaseLib.luaAssert(var19, "Tried to call nil");
                     } catch (RuntimeException var292) {
                        var10000 = var292;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var323 = this.prepareMetatableCall(var332);
                     } catch (RuntimeException var291) {
                        var10000 = var291;
                        var10001 = false;
                        break label2663;
                     }

                     if (var323 == null) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var329 = new StringBuilder;
                        } catch (RuntimeException var290) {
                           var10000 = var290;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var329.<init>();
                        } catch (RuntimeException var289) {
                           var10000 = var289;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           BaseLib.fail(var329.append("Object ").append(var332).append(" did not have __call metatable set").toString());
                        } catch (RuntimeException var288) {
                           var10000 = var288;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     var14 = var10;
                     if (var323 != var332) {
                        var15 = var11;
                        var14 = var10 + 1;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        if (var323 instanceof LuaClosure) {
                           break label2560;
                        }
                     } catch (RuntimeException var303) {
                        var10000 = var303;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        if (!(var323 instanceof JavaFunction)) {
                           break label2664;
                        }
                     } catch (RuntimeException var302) {
                        var10000 = var302;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        this.callJava((JavaFunction)var323, var15, var11, var14);
                     } catch (RuntimeException var287) {
                        var10000 = var287;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var17 = this.currentThread.currentCallFrame();
                     } catch (RuntimeException var286) {
                        var10000 = var286;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        if (var17.isJava()) {
                           break;
                        }
                     } catch (RuntimeException var307) {
                        var10000 = var307;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var13 = var17.closure;
                     } catch (RuntimeException var99) {
                        var10000 = var99;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var13;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var12 = var13.prototype;
                     } catch (RuntimeException var98) {
                        var10000 = var98;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var13;
                     var7 = var4;
                     var8 = var12;
                     var9 = var5;

                     int[] var333;
                     try {
                        var333 = var12.code;
                     } catch (RuntimeException var97) {
                        var10000 = var97;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var13;
                     var7 = var333;
                     var8 = var12;
                     var9 = var5;

                     try {
                        var10 = var17.returnBase;
                     } catch (RuntimeException var96) {
                        var10000 = var96;
                        var10001 = false;
                        break label2663;
                     }

                     var1 = var17;
                     var2 = var13;
                     var4 = var333;
                     var3 = var12;
                     var5 = var10;
                     var6 = var13;
                     var7 = var333;
                     var8 = var12;
                     var9 = var10;

                     try {
                        if (!var17.restoreTop) {
                           continue;
                        }
                     } catch (RuntimeException var95) {
                        var10000 = var95;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var13;
                     var7 = var333;
                     var8 = var12;
                     var9 = var10;

                     try {
                        var17.setTop(var12.maxStacksize);
                     } catch (RuntimeException var94) {
                        var10000 = var94;
                        var10001 = false;
                        break label2663;
                     }

                     var1 = var17;
                     var2 = var13;
                     var4 = var333;
                     var3 = var12;
                     var5 = var10;
                     continue;
                  case 29:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var20 = var1.localBase;
                     } catch (RuntimeException var285) {
                        var10000 = var285;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        this.currentThread.closeUpvalues(var20);
                     } catch (RuntimeException var284) {
                        var10000 = var284;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var11 = getA8(var10);
                     } catch (RuntimeException var283) {
                        var10000 = var283;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getB9(var10) - 1;
                     } catch (RuntimeException var282) {
                        var10000 = var282;
                        var10001 = false;
                        break label2663;
                     }

                     var10 = var14;
                     if (var14 == -1) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var10 = var1.getTop() - var11 - 1;
                        } catch (RuntimeException var281) {
                           var10000 = var281;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.restoreTop = false;
                     } catch (RuntimeException var280) {
                        var10000 = var280;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var327 = var1.get(var11);
                     } catch (RuntimeException var279) {
                        var10000 = var279;
                        var10001 = false;
                        break label2663;
                     }

                     if (var327 != null) {
                        var19 = true;
                     } else {
                        var19 = false;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        BaseLib.luaAssert(var19, "Tried to call nil");
                     } catch (RuntimeException var278) {
                        var10000 = var278;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var323 = this.prepareMetatableCall(var327);
                     } catch (RuntimeException var277) {
                        var10000 = var277;
                        var10001 = false;
                        break label2663;
                     }

                     if (var323 == null) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var331 = new StringBuilder;
                        } catch (RuntimeException var276) {
                           var10000 = var276;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var331.<init>();
                        } catch (RuntimeException var275) {
                           var10000 = var275;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           BaseLib.fail(var331.append("Object ").append(var327).append(" did not have __call metatable set").toString());
                        } catch (RuntimeException var274) {
                           var10000 = var274;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     var15 = var5 + 1;
                     var14 = var10;
                     if (var323 != var327) {
                        var14 = var10 + 1;
                        var15 = var5;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        this.currentThread.stackCopy(var20 + var11, var5, var14 + 1);
                     } catch (RuntimeException var273) {
                        var10000 = var273;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        this.currentThread.setTop(var5 + var14 + 1);
                     } catch (RuntimeException var272) {
                        var10000 = var272;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     label2641: {
                        label2642: {
                           try {
                              if (!(var323 instanceof LuaClosure)) {
                                 break label2642;
                              }
                           } catch (RuntimeException var315) {
                              var10000 = var315;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var1.localBase = var15;
                           } catch (RuntimeException var248) {
                              var10000 = var248;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var1.nArguments = var14;
                           } catch (RuntimeException var247) {
                              var10000 = var247;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var1.closure = (LuaClosure)var323;
                           } catch (RuntimeException var246) {
                              var10000 = var246;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var1.init();
                              break label2641;
                           } catch (RuntimeException var245) {
                              var10000 = var245;
                              var10001 = false;
                              break label2663;
                           }
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        label2643: {
                           try {
                              if (var323 instanceof JavaFunction) {
                                 break label2643;
                              }
                           } catch (RuntimeException var314) {
                              var10000 = var314;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           StringBuilder var318;
                           try {
                              var318 = new StringBuilder;
                           } catch (RuntimeException var271) {
                              var10000 = var271;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var318.<init>();
                           } catch (RuntimeException var270) {
                              var10000 = var270;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              BaseLib.fail(var318.append("Tried to call a non-function: ").append(var323).toString());
                           } catch (RuntimeException var269) {
                              var10000 = var269;
                              var10001 = false;
                              break label2663;
                           }
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        LuaThread var319;
                        try {
                           var319 = this.currentThread;
                        } catch (RuntimeException var268) {
                           var10000 = var268;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           this.callJava((JavaFunction)var323, var15, var5, var14);
                        } catch (RuntimeException var267) {
                           var10000 = var267;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var330 = this.currentThread.currentCallFrame();
                        } catch (RuntimeException var266) {
                           var10000 = var266;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var319.popCallFrame();
                        } catch (RuntimeException var265) {
                           var10000 = var265;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        label2644: {
                           try {
                              if (var319 != this.currentThread) {
                                 break label2644;
                              }
                           } catch (RuntimeException var313) {
                              var10000 = var313;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              if (!var330.fromLua) {
                                 break;
                              }
                           } catch (RuntimeException var305) {
                              var10000 = var305;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var330 = this.currentThread.currentCallFrame();
                           } catch (RuntimeException var244) {
                              var10000 = var244;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;
                           var1 = var330;

                           try {
                              if (!var330.restoreTop) {
                                 break label2641;
                              }
                           } catch (RuntimeException var301) {
                              var10000 = var301;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var330.setTop(var330.closure.prototype.maxStacksize);
                           } catch (RuntimeException var243) {
                              var10000 = var243;
                              var10001 = false;
                              break label2663;
                           }

                           var1 = var330;
                           break label2641;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        label2645: {
                           try {
                              if (!var319.isDead()) {
                                 break label2645;
                              }
                           } catch (RuntimeException var312) {
                              var10000 = var312;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              if (this.currentThread.parent != var319) {
                                 break label2645;
                              }
                           } catch (RuntimeException var311) {
                              var10000 = var311;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              this.currentThread.parent = var319.parent;
                           } catch (RuntimeException var264) {
                              var10000 = var264;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var319.parent = null;
                           } catch (RuntimeException var263) {
                              var10000 = var263;
                              var10001 = false;
                              break label2663;
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              this.currentThread.parent.currentCallFrame().push(Boolean.TRUE);
                           } catch (RuntimeException var262) {
                              var10000 = var262;
                              var10001 = false;
                              break label2663;
                           }
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var330 = this.currentThread.currentCallFrame();
                        } catch (RuntimeException var261) {
                           var10000 = var261;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;
                        var1 = var330;

                        try {
                           if (var330.isJava()) {
                              break;
                           }
                        } catch (RuntimeException var306) {
                           var10000 = var306;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var2 = var1.closure;
                     } catch (RuntimeException var86) {
                        var10000 = var86;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var3 = var2.prototype;
                     } catch (RuntimeException var85) {
                        var10000 = var85;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var4 = var3.code;
                     } catch (RuntimeException var84) {
                        var10000 = var84;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var5 = var1.returnBase;
                        continue;
                     } catch (RuntimeException var83) {
                        var10000 = var83;
                        var10001 = false;
                        break label2663;
                     }
                  case 30:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var15 = getA8(var10);
                     } catch (RuntimeException var260) {
                        var10000 = var260;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getB9(var10) - 1;
                     } catch (RuntimeException var259) {
                        var10000 = var259;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = var1.localBase;
                     } catch (RuntimeException var258) {
                        var10000 = var258;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        this.currentThread.closeUpvalues(var10);
                     } catch (RuntimeException var257) {
                        var10000 = var257;
                        var10001 = false;
                        break label2663;
                     }

                     var10 = var14;
                     if (var14 == -1) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var10 = var1.getTop() - var15;
                        } catch (RuntimeException var256) {
                           var10000 = var256;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        this.currentThread.stackCopy(var1.localBase + var15, var5, var10);
                     } catch (RuntimeException var255) {
                        var10000 = var255;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        this.currentThread.setTop(var5 + var10);
                     } catch (RuntimeException var254) {
                        var10000 = var254;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     label2592: {
                        try {
                           if (!var1.fromLua) {
                              break label2592;
                           }
                        } catch (RuntimeException var310) {
                           var10000 = var310;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        label2593: {
                           label2594: {
                              try {
                                 if (!var1.insideCoroutine) {
                                    break label2594;
                                 }
                              } catch (RuntimeException var309) {
                                 var10000 = var309;
                                 var10001 = false;
                                 break label2663;
                              }

                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 if (this.currentThread.callFrameTop != 1) {
                                    break label2594;
                                 }
                              } catch (RuntimeException var308) {
                                 var10000 = var308;
                                 var10001 = false;
                                 break label2663;
                              }

                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 var1.localBase = var1.returnBase;
                              } catch (RuntimeException var253) {
                                 var10000 = var253;
                                 var10001 = false;
                                 break label2663;
                              }

                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              LuaThread var325;
                              try {
                                 var325 = this.currentThread;
                              } catch (RuntimeException var252) {
                                 var10000 = var252;
                                 var10001 = false;
                                 break label2663;
                              }

                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 CoroutineLib.yieldHelper(var1, var1, var10);
                              } catch (RuntimeException var251) {
                                 var10000 = var251;
                                 var10001 = false;
                                 break label2663;
                              }

                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 var325.popCallFrame();
                              } catch (RuntimeException var250) {
                                 var10000 = var250;
                                 var10001 = false;
                                 break label2663;
                              }

                              var6 = var2;
                              var7 = var4;
                              var8 = var3;
                              var9 = var5;

                              try {
                                 if (this.currentThread.currentCallFrame().isJava()) {
                                    break;
                                 }
                                 break label2593;
                              } catch (RuntimeException var304) {
                                 var10000 = var304;
                                 var10001 = false;
                                 break label2663;
                              }
                           }

                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              this.currentThread.popCallFrame();
                           } catch (RuntimeException var242) {
                              var10000 = var242;
                              var10001 = false;
                              break label2663;
                           }
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var17 = this.currentThread.currentCallFrame();
                        } catch (RuntimeException var82) {
                           var10000 = var82;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        LuaClosure var328;
                        try {
                           var328 = var17.closure;
                        } catch (RuntimeException var81) {
                           var10000 = var81;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var328;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        LuaPrototype var16;
                        try {
                           var16 = var328.prototype;
                        } catch (RuntimeException var80) {
                           var10000 = var80;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var328;
                        var7 = var4;
                        var8 = var16;
                        var9 = var5;

                        int[] var326;
                        try {
                           var326 = var16.code;
                        } catch (RuntimeException var79) {
                           var10000 = var79;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var328;
                        var7 = var326;
                        var8 = var16;
                        var9 = var5;

                        try {
                           var10 = var17.returnBase;
                        } catch (RuntimeException var78) {
                           var10000 = var78;
                           var10001 = false;
                           break label2663;
                        }

                        var1 = var17;
                        var2 = var328;
                        var4 = var326;
                        var3 = var16;
                        var5 = var10;
                        var6 = var328;
                        var7 = var326;
                        var8 = var16;
                        var9 = var10;

                        try {
                           if (!var17.restoreTop) {
                              continue;
                           }
                        } catch (RuntimeException var77) {
                           var10000 = var77;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var328;
                        var7 = var326;
                        var8 = var16;
                        var9 = var10;

                        try {
                           var17.setTop(var16.maxStacksize);
                        } catch (RuntimeException var76) {
                           var10000 = var76;
                           var10001 = false;
                           break label2663;
                        }

                        var1 = var17;
                        var2 = var328;
                        var4 = var326;
                        var3 = var16;
                        var5 = var10;
                        continue;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        this.currentThread.popCallFrame();
                        break;
                     } catch (RuntimeException var249) {
                        var10000 = var249;
                        var10001 = false;
                        break label2663;
                     }
                  case 31:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var75) {
                        var10000 = var75;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     double var28;
                     try {
                        var28 = fromDouble(var1.get(var14));
                     } catch (RuntimeException var74) {
                        var10000 = var74;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var23 = fromDouble(var1.get(var14 + 1));
                     } catch (RuntimeException var73) {
                        var10000 = var73;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var21 = fromDouble(var1.get(var14 + 2));
                     } catch (RuntimeException var72) {
                        var10000 = var72;
                        var10001 = false;
                        break label2663;
                     }

                     var28 += var21;
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     Double var324;
                     try {
                        var324 = toDouble(var28);
                     } catch (RuntimeException var71) {
                        var10000 = var71;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var14, var324);
                     } catch (RuntimeException var70) {
                        var10000 = var70;
                        var10001 = false;
                        break label2663;
                     }

                     label2650: {
                        if (var21 > 0.0D) {
                           if (var28 <= var23) {
                              break label2650;
                           }
                        } else if (var28 >= var23) {
                           break label2650;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var1.clearFromIndex(var14);
                           continue;
                        } catch (RuntimeException var66) {
                           var10000 = var66;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getSBx(var10);
                     } catch (RuntimeException var69) {
                        var10000 = var69;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.pc += var10;
                     } catch (RuntimeException var68) {
                        var10000 = var68;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var14 + 3, var324);
                        continue;
                     } catch (RuntimeException var67) {
                        var10000 = var67;
                        var10001 = false;
                        break label2663;
                     }
                  case 32:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var65) {
                        var10000 = var65;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getSBx(var10);
                     } catch (RuntimeException var64) {
                        var10000 = var64;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var14, toDouble(fromDouble(var1.get(var14)) - fromDouble(var1.get(var14 + 2))));
                     } catch (RuntimeException var63) {
                        var10000 = var63;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.pc += var10;
                        continue;
                     } catch (RuntimeException var62) {
                        var10000 = var62;
                        var10001 = false;
                        break label2663;
                     }
                  case 33:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var61) {
                        var10000 = var61;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getC9(var10);
                     } catch (RuntimeException var60) {
                        var10000 = var60;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.setTop(var14 + 6);
                     } catch (RuntimeException var59) {
                        var10000 = var59;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.stackCopy(var14, var14 + 3, 3);
                     } catch (RuntimeException var58) {
                        var10000 = var58;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        this.call(2);
                     } catch (RuntimeException var57) {
                        var10000 = var57;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.clearFromIndex(var14 + 3 + var10);
                     } catch (RuntimeException var56) {
                        var10000 = var56;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.setPrototypeStacksize();
                     } catch (RuntimeException var55) {
                        var10000 = var55;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var323 = var1.get(var14 + 3);
                     } catch (RuntimeException var54) {
                        var10000 = var54;
                        var10001 = false;
                        break label2663;
                     }

                     if (var323 != null) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var1.set(var14 + 2, var323);
                        } catch (RuntimeException var52) {
                           var10000 = var52;
                           var10001 = false;
                           break label2663;
                        }
                     } else {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           ++var1.pc;
                        } catch (RuntimeException var53) {
                           var10000 = var53;
                           var10001 = false;
                           break label2663;
                        }
                     }
                     continue;
                  case 34:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var11 = getA8(var10);
                     } catch (RuntimeException var51) {
                        var10000 = var51;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getB9(var10);
                     } catch (RuntimeException var50) {
                        var10000 = var50;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var15 = getC9(var10);
                     } catch (RuntimeException var49) {
                        var10000 = var49;
                        var10001 = false;
                        break label2663;
                     }

                     var10 = var14;
                     if (var14 == 0) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var10 = var1.getTop() - var11 - 1;
                        } catch (RuntimeException var48) {
                           var10000 = var48;
                           var10001 = false;
                           break label2663;
                        }
                     }

                     var14 = var15;
                     if (var15 == 0) {
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var14 = var1.pc;
                        } catch (RuntimeException var47) {
                           var10000 = var47;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var1.pc = var14 + 1;
                        } catch (RuntimeException var46) {
                           var10000 = var46;
                           var10001 = false;
                           break label2663;
                        }

                        var14 = var4[var14];
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     LuaTable var322;
                     try {
                        var322 = (LuaTable)var1.get(var11);
                     } catch (RuntimeException var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label2663;
                     }

                     var15 = 1;

                     while(true) {
                        if (var15 > var10) {
                           continue label2579;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var322.rawset(toDouble((long)((var14 - 1) * 50 + var15)), var1.get(var11 + var15));
                        } catch (RuntimeException var44) {
                           var10000 = var44;
                           var10001 = false;
                           break label2663;
                        }

                        ++var15;
                     }
                  case 35:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.closeUpvalues(getA8(var10));
                        continue;
                     } catch (RuntimeException var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label2663;
                     }
                  case 36:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = getA8(var10);
                     } catch (RuntimeException var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var10 = getBx(var10);
                     } catch (RuntimeException var41) {
                        var10000 = var41;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var12 = var3.prototypes[var10];
                     } catch (RuntimeException var40) {
                        var10000 = var40;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var13 = new LuaClosure;
                     } catch (RuntimeException var39) {
                        var10000 = var39;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var13.<init>(var12, var2.env);
                     } catch (RuntimeException var38) {
                        var10000 = var38;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.set(var14, var13);
                     } catch (RuntimeException var37) {
                        var10000 = var37;
                        var10001 = false;
                        break label2663;
                     }

                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var14 = var12.numUpvalues;
                     } catch (RuntimeException var36) {
                        var10000 = var36;
                        var10001 = false;
                        break label2663;
                     }

                     var10 = 0;

                     while(true) {
                        if (var10 >= var14) {
                           continue label2579;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var15 = var1.pc;
                        } catch (RuntimeException var35) {
                           var10000 = var35;
                           var10001 = false;
                           break label2663;
                        }

                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var1.pc = var15 + 1;
                        } catch (RuntimeException var34) {
                           var10000 = var34;
                           var10001 = false;
                           break label2663;
                        }

                        var11 = var4[var15];
                        var6 = var2;
                        var7 = var4;
                        var8 = var3;
                        var9 = var5;

                        try {
                           var15 = getB9(var11);
                        } catch (RuntimeException var33) {
                           var10000 = var33;
                           var10001 = false;
                           break label2663;
                        }

                        switch(var11 & 63) {
                        case 0:
                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var13.upvalues[var10] = var1.findUpvalue(var15);
                              break;
                           } catch (RuntimeException var32) {
                              var10000 = var32;
                              var10001 = false;
                              break label2663;
                           }
                        case 4:
                           var6 = var2;
                           var7 = var4;
                           var8 = var3;
                           var9 = var5;

                           try {
                              var13.upvalues[var10] = var2.upvalues[var15];
                           } catch (RuntimeException var31) {
                              var10000 = var31;
                              var10001 = false;
                              break label2663;
                           }
                        }

                        ++var10;
                     }
                  case 37:
                     var6 = var2;
                     var7 = var4;
                     var8 = var3;
                     var9 = var5;

                     try {
                        var1.pushVarargs(getA8(var10), getB9(var10) - 1);
                     } catch (RuntimeException var30) {
                        var10000 = var30;
                        var10001 = false;
                        break label2663;
                     }
                  default:
                     continue;
                  }

                  try {
                     return;
                  } catch (RuntimeException var93) {
                     var10000 = var93;
                     var10001 = false;
                     break label2663;
                  }
               }

               var6 = var2;
               var7 = var4;
               var8 = var3;
               var9 = var5;

               try {
                  var330 = this.currentThread.pushNewCallFrame((LuaClosure)var323, (JavaFunction)null, var15, var11, var14, true, var1.insideCoroutine);
               } catch (RuntimeException var92) {
                  var10000 = var92;
                  var10001 = false;
                  break label2663;
               }

               var6 = var2;
               var7 = var4;
               var8 = var3;
               var9 = var5;

               try {
                  var330.init();
               } catch (RuntimeException var91) {
                  var10000 = var91;
                  var10001 = false;
                  break label2663;
               }

               var1 = var330;
               var6 = var2;
               var7 = var4;
               var8 = var3;
               var9 = var5;

               try {
                  var2 = var330.closure;
               } catch (RuntimeException var90) {
                  var10000 = var90;
                  var10001 = false;
                  break label2663;
               }

               var6 = var2;
               var7 = var4;
               var8 = var3;
               var9 = var5;

               try {
                  var3 = var2.prototype;
               } catch (RuntimeException var89) {
                  var10000 = var89;
                  var10001 = false;
                  break label2663;
               }

               var6 = var2;
               var7 = var4;
               var8 = var3;
               var9 = var5;

               try {
                  var4 = var3.code;
               } catch (RuntimeException var88) {
                  var10000 = var88;
                  var10001 = false;
                  break label2663;
               }

               var6 = var2;
               var7 = var4;
               var8 = var3;
               var9 = var5;

               try {
                  var5 = var1.returnBase;
                  continue;
               } catch (RuntimeException var87) {
                  var10000 = var87;
                  var10001 = false;
                  break label2663;
               }
            }

            var6 = var2;
            var7 = var4;
            var8 = var3;
            var9 = var5;

            RuntimeException var321;
            try {
               var321 = new RuntimeException;
            } catch (RuntimeException var104) {
               var10000 = var104;
               var10001 = false;
               break label2663;
            }

            var6 = var2;
            var7 = var4;
            var8 = var3;
            var9 = var5;

            try {
               var329 = new StringBuilder;
            } catch (RuntimeException var103) {
               var10000 = var103;
               var10001 = false;
               break label2663;
            }

            var6 = var2;
            var7 = var4;
            var8 = var3;
            var9 = var5;

            try {
               var329.<init>();
            } catch (RuntimeException var102) {
               var10000 = var102;
               var10001 = false;
               break label2663;
            }

            var6 = var2;
            var7 = var4;
            var8 = var3;
            var9 = var5;

            try {
               var321.<init>(var329.append("Tried to call a non-function: ").append(var323).toString());
            } catch (RuntimeException var101) {
               var10000 = var101;
               var10001 = false;
               break label2663;
            }

            var6 = var2;
            var7 = var4;
            var8 = var3;
            var9 = var5;

            try {
               throw var321;
            } catch (RuntimeException var100) {
               var10000 = var100;
               var10001 = false;
            }
         }

         RuntimeException var347 = var10000;

         while(true) {
            LuaCallFrame var320 = this.currentThread.currentCallFrame();
            if (var320.isLua()) {
               var340 = true;

               while(true) {
                  var1 = this.currentThread.currentCallFrame();
                  if (var1 == null) {
                     LuaThread var343 = this.currentThread.parent;
                     var2 = var6;
                     var4 = var7;
                     var3 = var8;
                     var5 = var9;
                     if (var343 != null) {
                        this.currentThread.parent = null;
                        var320 = var343.currentCallFrame();
                        var320.push(Boolean.FALSE);
                        var320.push(var347.getMessage());
                        var320.push(this.currentThread.stackTrace);
                        this.currentThread.state.currentThread = var343;
                        this.currentThread = var343;
                        var1 = this.currentThread.currentCallFrame();
                        var2 = var1.closure;
                        var3 = var2.prototype;
                        var4 = var3.code;
                        var5 = var1.returnBase;
                        var340 = false;
                     }
                     break;
                  }

                  this.currentThread.addStackTrace(var1);
                  this.currentThread.popCallFrame();
                  if (!var1.fromLua) {
                     var2 = var6;
                     var4 = var7;
                     var3 = var8;
                     var5 = var9;
                     break;
                  }
               }

               if (var1 != null) {
                  var1.closeUpvalues(0);
               }

               if (var340) {
                  throw var347;
               }
               break;
            }

            this.currentThread.addStackTrace(var320);
            this.currentThread.popCallFrame();
         }
      }
   }

   private final Object prepareMetatableCall(Object var1) {
      if (!(var1 instanceof JavaFunction) && !(var1 instanceof LuaClosure)) {
         var1 = this.getMetaOp(var1, "__call");
      }

      return var1;
   }

   private Double primitiveMath(Double var1, Double var2, int var3) {
      double var4 = fromDouble(var1);
      double var6 = fromDouble(var2);
      double var8 = 0.0D;
      switch(var3) {
      case 12:
         var8 = var4 + var6;
         break;
      case 13:
         var8 = var4 - var6;
         break;
      case 14:
         var8 = var4 * var6;
         break;
      case 15:
         var8 = var4 / var6;
         break;
      case 16:
         if (var6 == 0.0D) {
            var8 = Double.NaN;
         } else {
            var8 = var4 - (double)((int)(var4 / var6)) * var6;
         }
         break;
      case 17:
         var8 = MathLib.pow(var4, var6);
      }

      return toDouble(var8);
   }

   private void setUserdataMetatable(Object var1, LuaTable var2) {
      this.userdataMetatables.rawset(var1, var2);
   }

   public static Boolean toBoolean(boolean var0) {
      Boolean var1;
      if (var0) {
         var1 = Boolean.TRUE;
      } else {
         var1 = Boolean.FALSE;
      }

      return var1;
   }

   public static Double toDouble(double var0) {
      return new Double(var0);
   }

   public static Double toDouble(long var0) {
      return toDouble((double)var0);
   }

   public int call(int var1) {
      int var2 = this.currentThread.getTop() - var1 - 1;
      Object var3 = this.currentThread.objectStack[var2];
      if (var3 == null) {
         throw new RuntimeException("tried to call nil");
      } else {
         if (var3 instanceof JavaFunction) {
            var1 = this.callJava((JavaFunction)var3, var2 + 1, var2, var1);
         } else {
            if (!(var3 instanceof LuaClosure)) {
               throw new RuntimeException("tried to call a non-function");
            }

            this.currentThread.pushNewCallFrame((LuaClosure)var3, (JavaFunction)null, var2 + 1, var2, var1, false, false).init();
            this.luaMainloop();
            var1 = this.currentThread.getTop() - var2;
            this.currentThread.stackTrace = "";
         }

         return var1;
      }
   }

   public Object call(Object var1, Object var2, Object var3, Object var4) {
      int var5 = this.currentThread.getTop();
      this.currentThread.setTop(var5 + 1 + 3);
      this.currentThread.objectStack[var5] = var1;
      this.currentThread.objectStack[var5 + 1] = var2;
      this.currentThread.objectStack[var5 + 2] = var3;
      this.currentThread.objectStack[var5 + 3] = var4;
      int var6 = this.call(3);
      var1 = null;
      if (var6 >= 1) {
         var1 = this.currentThread.objectStack[var5];
      }

      this.currentThread.setTop(var5);
      return var1;
   }

   public Object call(Object var1, Object[] var2) {
      int var3 = this.currentThread.getTop();
      int var4;
      if (var2 == null) {
         var4 = 0;
      } else {
         var4 = var2.length;
      }

      this.currentThread.setTop(var3 + 1 + var4);
      this.currentThread.objectStack[var3] = var1;

      for(int var5 = 1; var5 <= var4; ++var5) {
         this.currentThread.objectStack[var3 + var5] = var2[var5 - 1];
      }

      var4 = this.call(var4);
      var1 = null;
      if (var4 >= 1) {
         var1 = this.currentThread.objectStack[var3];
      }

      this.currentThread.setTop(var3);
      return var1;
   }

   public LuaTable getClassMetatable(Class var1) {
      return (LuaTable)this.classMetatables.rawget(var1);
   }

   public LuaTable getEnvironment() {
      return this.currentThread.environment;
   }

   public Object getMetaOp(Object var1, String var2) {
      LuaTable var3 = (LuaTable)this.getmetatable(var1, true);
      if (var3 == null) {
         var1 = null;
      } else {
         var1 = var3.rawget(var2);
      }

      return var1;
   }

   public PrintStream getOut() {
      return this.out;
   }

   public Object getmetatable(Object var1, boolean var2) {
      if (var1 == null) {
         var1 = null;
      } else {
         LuaTable var3;
         if (var1 instanceof LuaTable) {
            var3 = ((LuaTable)var1).getMetatable();
         } else {
            var3 = (LuaTable)this.userdataMetatables.rawget(var1);
         }

         LuaTable var4 = var3;
         if (var3 == null) {
            var4 = (LuaTable)this.classMetatables.rawget(var1.getClass());
         }

         if (!var2 && var4 != null) {
            Object var5 = var4.rawget("__metatable");
            var1 = var5;
            if (var5 != null) {
               return var1;
            }
         }

         var1 = var4;
      }

      return var1;
   }

   public LuaClosure loadByteCodeFromResource(String var1, LuaTable var2) {
      InputStream var4 = this.getClass().getResourceAsStream(var1 + ".lbc");
      LuaClosure var5;
      if (var4 == null) {
         var5 = null;
      } else {
         try {
            var5 = LuaPrototype.loadByteCode(var4, var2);
         } catch (IOException var3) {
            throw new RuntimeException(var3.getMessage());
         }
      }

      return var5;
   }

   public void lock() {
   }

   public int pcall(int var1) {
      LuaThread var2 = this.currentThread;
      LuaCallFrame var3 = var2.currentCallFrame();
      var2.stackTrace = "";
      int var4 = var2.getTop() - var1 - 1;

      label40: {
         Object var5;
         Object var6;
         try {
            var1 = this.call(var1);
            var2.setTop(var4 + var1 + 1);
            var2.stackCopy(var4, var4 + 1, var1);
            var2.objectStack[var4] = Boolean.TRUE;
            break label40;
         } catch (LuaException var8) {
            var6 = var8;
            var5 = var8.errorMessage;
         } catch (Throwable var9) {
            var6 = var9;
            var5 = var9.getMessage();
         }

         boolean var7;
         if (var2 == this.currentThread) {
            var7 = true;
         } else {
            var7 = false;
         }

         BaseLib.luaAssert(var7, "Internal Kahlua error - thread changed in pcall");
         if (var3 != null) {
            var3.closeUpvalues(0);
         }

         var2.cleanCallFrames(var3);
         Object var10 = var5;
         if (var5 instanceof String) {
            var10 = (String)var5;
         }

         var2.setTop(var4 + 4);
         var2.objectStack[var4] = Boolean.FALSE;
         var2.objectStack[var4 + 1] = var10;
         var2.objectStack[var4 + 2] = var2.stackTrace;
         var2.objectStack[var4 + 3] = var6;
         var2.stackTrace = "";
         var1 = 4;
         return var1;
      }

      ++var1;
      return var1;
   }

   public Object[] pcall(Object var1) {
      return this.pcall(var1, (Object[])null);
   }

   public Object[] pcall(Object var1, Object[] var2) {
      int var3;
      if (var2 == null) {
         var3 = 0;
      } else {
         var3 = var2.length;
      }

      LuaThread var4 = this.currentThread;
      int var5 = var4.getTop();
      var4.setTop(var5 + 1 + var3);
      var4.objectStack[var5] = var1;
      if (var3 > 0) {
         System.arraycopy(var2, 0, var4.objectStack, var5 + 1, var3);
      }

      var3 = this.pcall(var3);
      boolean var6;
      if (var4 == this.currentThread) {
         var6 = true;
      } else {
         var6 = false;
      }

      BaseLib.luaAssert(var6, "Internal Kahlua error - thread changed in pcall");
      Object[] var7 = new Object[var3];
      System.arraycopy(var4.objectStack, var5, var7, 0, var3);
      var4.setTop(var5);
      return var7;
   }

   protected final void reset() {
      this.currentThread = new LuaThread(this, new LuaTableImpl());
      this.getEnvironment().rawset("_G", this.getEnvironment());
      this.getEnvironment().rawset("_VERSION", "Lua 5.1 for CLDC 1.1");
      BaseLib.register(this);
      StringLib.register(this);
      MathLib.register(this);
      CoroutineLib.register(this);
      OsLib.register(this);
      TableLib.register(this);
   }

   public void setClassMetatable(Class var1, LuaTable var2) {
      this.classMetatables.rawset(var1, var2);
   }

   public void setmetatable(Object var1, LuaTable var2) {
      boolean var3;
      if (var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "Can't set metatable for nil");
      if (var1 instanceof LuaTable) {
         ((LuaTable)var1).setMetatable(var2);
      } else {
         this.userdataMetatables.rawset(var1, var2);
      }

   }

   public Object tableGet(Object var1, Object var2) {
      Object var3 = var1;
      int var4 = 100;

      while(true) {
         if (var4 <= 0) {
            throw new RuntimeException("loop in gettable");
         }

         boolean var5 = var3 instanceof LuaTable;
         Object var6;
         if (var5) {
            var6 = ((LuaTable)var3).rawget(var2);
            if (var6 != null) {
               var1 = var6;
               break;
            }
         }

         var6 = this.getMetaOp(var3, "__index");
         if (var6 == null) {
            if (!var5) {
               throw new RuntimeException("attempted index of non-table: " + var3);
            }

            var1 = null;
            break;
         }

         if (var6 instanceof JavaFunction || var6 instanceof LuaClosure) {
            var1 = this.call(var6, var1, var2, (Object)null);
            break;
         }

         var3 = var6;
         --var4;
      }

      return var1;
   }

   public void tableSet(Object var1, Object var2, Object var3) {
      Object var4 = var1;
      int var5 = 100;

      while(true) {
         if (var5 <= 0) {
            throw new RuntimeException("loop in settable");
         }

         if (var4 instanceof LuaTable) {
            LuaTable var6 = (LuaTable)var4;
            if (var6.rawget(var2) != null) {
               var6.rawset(var2, var3);
               break;
            }

            Object var7 = this.getMetaOp(var4, "__newindex");
            var4 = var7;
            if (var7 == null) {
               var6.rawset(var2, var3);
               break;
            }
         } else {
            var4 = this.getMetaOp(var4, "__newindex");
            boolean var8;
            if (var4 != null) {
               var8 = true;
            } else {
               var8 = false;
            }

            BaseLib.luaAssert(var8, "attempted index of non-table");
         }

         if (var4 instanceof JavaFunction || var4 instanceof LuaClosure) {
            this.call(var4, var1, var2, var3);
            break;
         }

         --var5;
      }

   }

   public void unlock() {
   }
}

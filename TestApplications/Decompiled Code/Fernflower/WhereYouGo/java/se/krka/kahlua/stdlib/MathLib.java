package se.krka.kahlua.stdlib;

import java.util.Random;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTableImpl;

public final class MathLib implements JavaFunction {
   private static final int ABS = 0;
   private static final int ACOS = 1;
   private static final int ASIN = 2;
   private static final int ATAN = 3;
   private static final int ATAN2 = 4;
   private static final int CEIL = 5;
   private static final int COS = 6;
   private static final int COSH = 7;
   private static final int DEG = 8;
   public static final double EPS = 1.0E-15D;
   private static final int EXP = 9;
   private static final int FLOOR = 10;
   private static final int FMOD = 11;
   private static final int FREXP = 12;
   private static final int LDEXP = 13;
   private static final double LN10_INV;
   private static final double LN2_INV;
   private static final int LOG = 14;
   private static final int LOG10 = 15;
   private static final int MODF = 16;
   private static final int NUM_FUNCTIONS = 26;
   static final double PIO2 = 1.5707963267948966D;
   private static final int POW = 17;
   private static final int RAD = 18;
   private static final int RANDOM = 19;
   private static final int RANDOMSEED = 20;
   private static final int SIN = 21;
   private static final int SINH = 22;
   private static final int SQRT = 23;
   private static final int TAN = 24;
   private static final int TANH = 25;
   private static MathLib[] functions;
   private static String[] names = new String[26];
   static final double p0 = 896.7859740366387D;
   static final double p1 = 1780.406316433197D;
   static final double p2 = 1153.029351540485D;
   static final double p3 = 268.42548195503974D;
   static final double p4 = 16.15364129822302D;
   static final double q0 = 896.7859740366387D;
   static final double q1 = 2079.33497444541D;
   static final double q2 = 1666.7838148816338D;
   static final double q3 = 536.2653740312153D;
   static final double q4 = 58.95697050844462D;
   static final double sq2m1 = 0.41421356237309503D;
   static final double sq2p1 = 2.414213562373095D;
   private int index;

   static {
      names[0] = "abs";
      names[1] = "acos";
      names[2] = "asin";
      names[3] = "atan";
      names[4] = "atan2";
      names[5] = "ceil";
      names[6] = "cos";
      names[7] = "cosh";
      names[8] = "deg";
      names[9] = "exp";
      names[10] = "floor";
      names[11] = "fmod";
      names[12] = "frexp";
      names[13] = "ldexp";
      names[14] = "log";
      names[15] = "log10";
      names[16] = "modf";
      names[17] = "pow";
      names[18] = "rad";
      names[19] = "random";
      names[20] = "randomseed";
      names[21] = "sin";
      names[22] = "sinh";
      names[23] = "sqrt";
      names[24] = "tan";
      names[25] = "tanh";
      LN10_INV = 1.0D / ln(10.0D);
      LN2_INV = 1.0D / ln(2.0D);
   }

   public MathLib(int var1) {
      this.index = var1;
   }

   private static int abs(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(Math.abs(getDoubleArg(var0, 1, names[0]))));
      return 1;
   }

   public static double acos(double var0) {
      if (var0 <= 1.0D && var0 >= -1.0D) {
         var0 = 1.5707963267948966D - asin(var0);
      } else {
         var0 = Double.NaN;
      }

      return var0;
   }

   private static int acos(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(acos(getDoubleArg(var0, 1, names[1]))));
      return 1;
   }

   public static double asin(double var0) {
      int var2 = 0;
      double var3 = var0;
      if (var0 < 0.0D) {
         var3 = -var0;
         var2 = 0 + 1;
      }

      if (var3 > 1.0D) {
         var3 = Double.NaN;
      } else {
         var0 = Math.sqrt(1.0D - var3 * var3);
         if (var3 > 0.7D) {
            var0 = 1.5707963267948966D - atan(var0 / var3);
         } else {
            var0 = atan(var3 / var0);
         }

         var3 = var0;
         if (var2 > 0) {
            var3 = -var0;
         }
      }

      return var3;
   }

   private static int asin(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(asin(getDoubleArg(var0, 1, names[2]))));
      return 1;
   }

   public static double atan(double var0) {
      if (var0 > 0.0D) {
         var0 = msatan(var0);
      } else {
         var0 = -msatan(-var0);
      }

      return var0;
   }

   private static int atan(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(atan(getDoubleArg(var0, 1, names[3]))));
      return 1;
   }

   public static double atan2(double var0, double var2) {
      if (var0 + var2 == var0) {
         if (var0 > 0.0D) {
            var0 = 1.5707963267948966D;
         } else if (var0 < 0.0D) {
            var0 = -1.5707963267948966D;
         } else {
            var0 = 0.0D;
         }
      } else {
         double var4 = atan(var0 / var2);
         var0 = var4;
         if (var2 < 0.0D) {
            if (var4 <= 0.0D) {
               var0 = var4 + 3.141592653589793D;
            } else {
               var0 = var4 - 3.141592653589793D;
            }
         }
      }

      return var0;
   }

   private static int atan2(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(atan2(getDoubleArg(var0, 1, names[4]), getDoubleArg(var0, 2, names[4]))));
      return 1;
   }

   private static int ceil(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(Math.ceil(getDoubleArg(var0, 1, names[5]))));
      return 1;
   }

   private static int cos(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(Math.cos(getDoubleArg(var0, 1, names[6]))));
      return 1;
   }

   private static int cosh(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      double var3 = exp(getDoubleArg(var0, 1, names[7]));
      var0.push(LuaState.toDouble((1.0D / var3 + var3) * 0.5D));
      return 1;
   }

   private static int deg(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(Math.toDegrees(getDoubleArg(var0, 1, names[8]))));
      return 1;
   }

   public static double exp(double var0) {
      double var2 = 1.0D;
      double var4 = 1.0D;

      double var6;
      for(var6 = 0.0D; Math.abs(var2) > 1.0E-15D; ++var4) {
         var6 += var2;
         var2 = var2 * var0 / var4;
      }

      return var6;
   }

   private static int exp(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(exp(getDoubleArg(var0, 1, names[9]))));
      return 1;
   }

   private static int floor(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(Math.floor(getDoubleArg(var0, 1, names[10]))));
      return 1;
   }

   private static int fmod(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      double var3 = getDoubleArg(var0, 1, names[11]);
      double var5 = getDoubleArg(var0, 2, names[11]);
      if (!Double.isInfinite(var3) && !Double.isNaN(var3)) {
         if (!Double.isInfinite(var5)) {
            double var7 = Math.abs(var5);
            boolean var9 = false;
            var5 = var3;
            if (isNegative(var3)) {
               var9 = true;
               var5 = -var3;
            }

            var5 -= Math.floor(var5 / var7) * var7;
            var3 = var5;
            if (var9) {
               var3 = -var5;
            }
         }
      } else {
         var3 = Double.NaN;
      }

      var0.push(LuaState.toDouble(var3));
      return 1;
   }

   private static double fpow(double var0, double var2) {
      if (var0 < 0.0D) {
         var0 = Double.NaN;
      } else {
         var0 = exp(ln(var0) * var2);
      }

      return var0;
   }

   private static int frexp(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      double var3 = getDoubleArg(var0, 1, names[12]);
      double var5;
      if (!Double.isInfinite(var3) && !Double.isNaN(var3)) {
         var5 = Math.ceil(ln(var3) * LN2_INV);
         var3 /= (double)(1 << (int)var5);
      } else {
         var5 = 0.0D;
      }

      var0.push(LuaState.toDouble(var3), LuaState.toDouble(var5));
      return 2;
   }

   private static double getDoubleArg(LuaCallFrame var0, int var1, String var2) {
      return (Double)BaseLib.getArg(var0, var1, "number", var2);
   }

   private static void initFunctions() {
      synchronized(MathLib.class){}

      label97: {
         Throwable var10000;
         label101: {
            boolean var10001;
            try {
               if (functions != null) {
                  break label97;
               }

               functions = new MathLib[26];
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label101;
            }

            int var0 = 0;

            while(true) {
               if (var0 >= 26) {
                  break label97;
               }

               try {
                  functions[var0] = new MathLib(var0);
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

   public static double ipow(double var0, int var2) {
      boolean var3 = false;
      int var4 = var2;
      if (isNegative((double)var2)) {
         var4 = -var2;
         var3 = true;
      }

      double var5;
      if ((var4 & 1) != 0) {
         var5 = var0;
      } else {
         var5 = 1.0D;
      }

      var2 = var4 >> 1;
      double var7 = var0;

      for(var0 = var5; var2 != 0; var0 = var5) {
         var7 *= var7;
         var5 = var0;
         if ((var2 & 1) != 0) {
            var5 = var0 * var7;
         }

         var2 >>= 1;
      }

      var5 = var0;
      if (var3) {
         var5 = 1.0D / var0;
      }

      return var5;
   }

   public static boolean isNegative(double var0) {
      boolean var2;
      if (Double.doubleToLongBits(var0) < 0L) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static int ldexp(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      double var3 = getDoubleArg(var0, 1, names[13]);
      double var5 = getDoubleArg(var0, 2, names[13]);
      double var7 = var3 + var5;
      if (!Double.isInfinite(var7) && !Double.isNaN(var7)) {
         var3 *= (double)(1 << (int)var5);
      }

      var0.push(LuaState.toDouble(var3));
      return 1;
   }

   public static double ln(double var0) {
      boolean var2 = false;
      if (var0 < 0.0D) {
         var0 = Double.NaN;
      } else if (var0 == 0.0D) {
         var0 = Double.NEGATIVE_INFINITY;
      } else if (Double.isInfinite(var0)) {
         var0 = Double.POSITIVE_INFINITY;
      } else {
         double var3 = var0;
         if (var0 < 1.0D) {
            var2 = true;
            var3 = 1.0D / var0;
         }

         int var5;
         for(var5 = 1; var3 >= 1.1D; var3 = Math.sqrt(var3)) {
            var5 *= 2;
         }

         double var6 = 1.0D - var3;
         var0 = var6;
         int var8 = 1;
         var3 = 0.0D;

         while(true) {
            double var9 = var0 / (double)var8;
            if (Math.abs(var9) <= 1.0E-15D) {
               var3 = (double)var5 * var3;
               var0 = var3;
               if (var2) {
                  var0 = -var3;
               }
               break;
            }

            var3 -= var9;
            var0 *= var6;
            ++var8;
         }
      }

      return var0;
   }

   private static int log(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(ln(getDoubleArg(var0, 1, names[14]))));
      return 1;
   }

   private static int log10(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(ln(getDoubleArg(var0, 1, names[15])) * LN10_INV));
      return 1;
   }

   private static int modf(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      double var3 = getDoubleArg(var0, 1, names[16]);
      boolean var11 = false;
      double var5 = var3;
      if (isNegative(var3)) {
         var11 = true;
         var5 = -var3;
      }

      double var7 = Math.floor(var5);
      if (Double.isInfinite(var7)) {
         var5 = 0.0D;
      } else {
         var5 -= var7;
      }

      double var9 = var5;
      var3 = var7;
      if (var11) {
         var3 = -var7;
         var9 = -var5;
      }

      var0.push(LuaState.toDouble(var3), LuaState.toDouble(var9));
      return 2;
   }

   private static double msatan(double var0) {
      if (var0 < 0.41421356237309503D) {
         var0 = mxatan(var0);
      } else if (var0 > 2.414213562373095D) {
         var0 = 1.5707963267948966D - mxatan(1.0D / var0);
      } else {
         var0 = 0.7853981633974483D + mxatan((var0 - 1.0D) / (1.0D + var0));
      }

      return var0;
   }

   private static double mxatan(double var0) {
      double var2 = var0 * var0;
      return ((((16.15364129822302D * var2 + 268.42548195503974D) * var2 + 1153.029351540485D) * var2 + 1780.406316433197D) * var2 + 896.7859740366387D) / (((((58.95697050844462D + var2) * var2 + 536.2653740312153D) * var2 + 1666.7838148816338D) * var2 + 2079.33497444541D) * var2 + 896.7859740366387D) * var0;
   }

   public static double pow(double var0, double var2) {
      if ((double)((int)var2) == var2) {
         var0 = ipow(var0, (int)var2);
      } else {
         var0 = fpow(var0, var2);
      }

      return var0;
   }

   private static int pow(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(pow(getDoubleArg(var0, 1, names[17]), getDoubleArg(var0, 2, names[17]))));
      return 1;
   }

   private static int rad(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(Math.toRadians(getDoubleArg(var0, 1, names[18]))));
      return 1;
   }

   private int random(LuaCallFrame var1, int var2) {
      Random var3 = var1.thread.state.random;
      if (var2 == 0) {
         var1.push(LuaState.toDouble(var3.nextDouble()));
      } else {
         int var4 = (int)getDoubleArg(var1, 1, names[19]);
         if (var2 == 1) {
            var2 = 1;
         } else {
            int var5 = (int)getDoubleArg(var1, 2, names[19]);
            var2 = var4;
            var4 = var5;
         }

         var1.push(LuaState.toDouble((long)(var3.nextInt(var4 - var2 + 1) + var2)));
      }

      return 1;
   }

   private int randomseed(LuaCallFrame var1, int var2) {
      boolean var3 = true;
      if (var2 < 1) {
         var3 = false;
      }

      BaseLib.luaAssert(var3, "Not enough arguments");
      Object var4 = var1.get(0);
      if (var4 != null) {
         var1.thread.state.random.setSeed((long)var4.hashCode());
      }

      return 0;
   }

   public static void register(LuaState var0) {
      initFunctions();
      LuaTableImpl var1 = new LuaTableImpl();
      var0.getEnvironment().rawset("math", var1);
      var1.rawset("pi", LuaState.toDouble(3.141592653589793D));
      var1.rawset("huge", LuaState.toDouble(Double.POSITIVE_INFINITY));

      for(int var2 = 0; var2 < 26; ++var2) {
         var1.rawset(names[var2], functions[var2]);
      }

   }

   public static double round(double var0) {
      if (var0 < 0.0D) {
         var0 = -round(-var0);
      } else {
         double var2 = var0 + 0.5D;
         double var4 = Math.floor(var2);
         var0 = var4;
         if (var4 == var2) {
            var0 = var4 - (double)((long)var4 & 1L);
         }
      }

      return var0;
   }

   public static double roundToPrecision(double var0, int var2) {
      double var3 = ipow(10.0D, var2);
      return round(var0 * var3) / var3;
   }

   public static double roundToSignificantNumbers(double var0, int var2) {
      double var3 = 0.0D;
      if (var0 == 0.0D) {
         var0 = var3;
      } else if (var0 < 0.0D) {
         var0 = -roundToSignificantNumbers(-var0, var2);
      } else {
         double var5 = ipow(10.0D, var2 - 1);
         var3 = 1.0D;

         while(true) {
            double var7 = var3;
            if (var3 * var0 >= var5) {
               while(var7 * var0 >= var5 * 10.0D) {
                  var7 /= 10.0D;
               }

               var0 = round(var0 * var7) / var7;
               break;
            }

            var3 *= 10.0D;
         }
      }

      return var0;
   }

   private static int sin(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(Math.sin(getDoubleArg(var0, 1, names[21]))));
      return 1;
   }

   private static int sinh(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      double var3 = exp(getDoubleArg(var0, 1, names[22]));
      var0.push(LuaState.toDouble((var3 - 1.0D / var3) * 0.5D));
      return 1;
   }

   private static int sqrt(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(Math.sqrt(getDoubleArg(var0, 1, names[23]))));
      return 1;
   }

   private static int tan(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      var0.push(LuaState.toDouble(Math.tan(getDoubleArg(var0, 1, names[24]))));
      return 1;
   }

   private static int tanh(LuaCallFrame var0, int var1) {
      boolean var2;
      if (var1 >= 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      BaseLib.luaAssert(var2, "Not enough arguments");
      double var3 = exp(2.0D * getDoubleArg(var0, 1, names[25]));
      var0.push(LuaState.toDouble((var3 - 1.0D) / (1.0D + var3)));
      return 1;
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.index) {
      case 0:
         var2 = abs(var1, var2);
         break;
      case 1:
         var2 = acos(var1, var2);
         break;
      case 2:
         var2 = asin(var1, var2);
         break;
      case 3:
         var2 = atan(var1, var2);
         break;
      case 4:
         var2 = atan2(var1, var2);
         break;
      case 5:
         var2 = ceil(var1, var2);
         break;
      case 6:
         var2 = cos(var1, var2);
         break;
      case 7:
         var2 = cosh(var1, var2);
         break;
      case 8:
         var2 = deg(var1, var2);
         break;
      case 9:
         var2 = exp(var1, var2);
         break;
      case 10:
         var2 = floor(var1, var2);
         break;
      case 11:
         var2 = fmod(var1, var2);
         break;
      case 12:
         var2 = frexp(var1, var2);
         break;
      case 13:
         var2 = ldexp(var1, var2);
         break;
      case 14:
         var2 = log(var1, var2);
         break;
      case 15:
         var2 = log10(var1, var2);
         break;
      case 16:
         var2 = modf(var1, var2);
         break;
      case 17:
         var2 = pow(var1, var2);
         break;
      case 18:
         var2 = rad(var1, var2);
         break;
      case 19:
         var2 = this.random(var1, var2);
         break;
      case 20:
         var2 = this.randomseed(var1, var2);
         break;
      case 21:
         var2 = sin(var1, var2);
         break;
      case 22:
         var2 = sinh(var1, var2);
         break;
      case 23:
         var2 = sqrt(var1, var2);
         break;
      case 24:
         var2 = tan(var1, var2);
         break;
      case 25:
         var2 = tanh(var1, var2);
         break;
      default:
         var2 = 0;
      }

      return var2;
   }

   public String toString() {
      return "math." + names[this.index];
   }
}

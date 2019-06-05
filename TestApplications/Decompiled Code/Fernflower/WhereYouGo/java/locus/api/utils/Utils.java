package locus.api.utils;

import java.io.Closeable;
import java.lang.reflect.Field;

public class Utils {
   private static final String NEW_LINE = System.getProperty("line.separator");
   private static final String TAG = "Utils";

   public static void closeStream(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (Exception var2) {
            System.err.println("closeStream(" + var0 + "), e:" + var2);
            var2.printStackTrace();
         }
      }

   }

   public static byte[] copyOf(byte[] var0, int var1) {
      byte[] var2 = new byte[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   public static byte[] copyOfRange(byte[] var0, int var1, int var2) {
      int var3 = var2 - var1;
      if (var3 < 0) {
         throw new IllegalArgumentException(var1 + " > " + var2);
      } else {
         byte[] var4 = new byte[var3];
         System.arraycopy(var0, var1, var4, 0, Math.min(var0.length - var1, var3));
         return var4;
      }
   }

   public static float[] copyOfRange(float[] var0, int var1, int var2) {
      if (var1 > var2) {
         throw new IllegalArgumentException();
      } else {
         int var3 = var0.length;
         if (var1 >= 0 && var1 <= var3) {
            var2 -= var1;
            var3 = Math.min(var2, var3 - var1);
            float[] var4 = new float[var2];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         } else {
            throw new ArrayIndexOutOfBoundsException();
         }
      }
   }

   public static String doBytesToString(byte[] var0) {
      String var1;
      String var3;
      try {
         var1 = new String(var0, "UTF-8");
      } catch (Exception var2) {
         Logger.logE("Utils", "doBytesToString(" + var0 + ")", var2);
         var3 = "";
         return var3;
      }

      var3 = var1;
      return var3;
   }

   public static byte[] doStringToBytes(String var0) {
      byte[] var1;
      byte[] var3;
      try {
         var1 = var0.getBytes("UTF-8");
      } catch (Exception var2) {
         Logger.logE("Utils", "doStringToBytes(" + var0 + ")", var2);
         var3 = new byte[0];
         return var3;
      }

      var3 = var1;
      return var3;
   }

   public static boolean isEmpty(CharSequence var0) {
      boolean var1;
      if (var0 != null && var0.length() != 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean parseBoolean(Object var0) {
      return parseBoolean(String.valueOf(var0));
   }

   public static boolean parseBoolean(String var0) {
      boolean var1 = false;

      label19: {
         boolean var2;
         try {
            if (var0.toLowerCase().contains("true")) {
               break label19;
            }

            var2 = var0.contains("1");
         } catch (Exception var3) {
            return var1;
         }

         if (!var2) {
            return var1;
         }
      }

      var1 = true;
      return var1;
   }

   public static double parseDouble(Object var0) {
      return parseDouble(String.valueOf(var0));
   }

   public static double parseDouble(String var0) {
      double var1;
      try {
         var1 = Double.parseDouble(var0.trim().replace(",", "."));
      } catch (Exception var3) {
         var1 = 0.0D;
      }

      return var1;
   }

   public static float parseFloat(Object var0) {
      return parseFloat(String.valueOf(var0));
   }

   public static float parseFloat(String var0) {
      float var1;
      try {
         var1 = Float.parseFloat(var0.trim());
      } catch (Exception var2) {
         var1 = 0.0F;
      }

      return var1;
   }

   public static int parseInt(Object var0) {
      return parseInt(String.valueOf(var0));
   }

   public static int parseInt(String var0) {
      return parseInt(var0, 0);
   }

   public static int parseInt(String var0, int var1) {
      int var2;
      try {
         var2 = Integer.parseInt(var0.trim());
      } catch (Exception var3) {
         return var1;
      }

      var1 = var2;
      return var1;
   }

   public static Integer parseInteger(Object var0) {
      return parseInteger(String.valueOf(var0));
   }

   public static Integer parseInteger(String var0) {
      Integer var2;
      try {
         var2 = Integer.valueOf(var0.trim());
      } catch (Exception var1) {
         var2 = 0;
      }

      return var2;
   }

   public static long parseLong(Object var0) {
      return parseLong(String.valueOf(var0));
   }

   public static long parseLong(String var0) {
      long var1;
      try {
         var1 = Long.parseLong(var0.trim());
      } catch (Exception var3) {
         var1 = 0L;
      }

      return var1;
   }

   public static String toString(Object var0) {
      return toString(var0, "");
   }

   public static String toString(Object var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(var1);
      String var8;
      if (var0 == null) {
         var2.append(" empty object!");
         var8 = var2.toString();
      } else {
         var2.append(var0.getClass().getName()).append(" {").append(NEW_LINE);
         Field[] var3 = var0.getClass().getDeclaredFields();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Field var6 = var3[var5];
            var2.append(var1).append("    ");

            try {
               var2.append(var6.getName());
               var2.append(": ");
               var6.setAccessible(true);
               var2.append(var6.get(var0));
            } catch (Exception var7) {
               System.out.println(var7);
            }

            var2.append(NEW_LINE);
         }

         var2.append(var1).append("}");
         var8 = var2.toString();
      }

      return var8;
   }
}

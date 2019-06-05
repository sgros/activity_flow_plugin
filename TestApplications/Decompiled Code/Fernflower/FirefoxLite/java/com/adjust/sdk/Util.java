package com.adjust.sdk;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Looper;
import android.provider.Settings.Secure;
import java.io.ObjectInputStream.GetField;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class Util {
   private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'Z";
   public static final DecimalFormat SecondsDisplayFormat = new DecimalFormat("0.0");
   public static final SimpleDateFormat dateFormatter;
   private static final String fieldReadErrorMessage = "Unable to read '%s' field in migration device with message (%s)";

   static {
      dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'Z", Locale.US);
   }

   public static boolean checkPermission(Context var0, String var1) {
      boolean var2;
      if (var0.checkCallingOrSelfPermission(var1) == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static String convertToHex(byte[] var0) {
      BigInteger var1 = new BigInteger(1, var0);
      StringBuilder var2 = new StringBuilder();
      var2.append("%0");
      var2.append(var0.length << 1);
      var2.append("x");
      String var3 = var2.toString();
      return String.format(Locale.US, var3, var1);
   }

   protected static String createUuid() {
      return UUID.randomUUID().toString();
   }

   public static boolean equalBoolean(Boolean var0, Boolean var1) {
      return equalObject(var0, var1);
   }

   public static boolean equalEnum(Enum var0, Enum var1) {
      return equalObject(var0, var1);
   }

   public static boolean equalInt(Integer var0, Integer var1) {
      return equalObject(var0, var1);
   }

   public static boolean equalLong(Long var0, Long var1) {
      return equalObject(var0, var1);
   }

   public static boolean equalObject(Object var0, Object var1) {
      if (var0 != null && var1 != null) {
         return var0.equals(var1);
      } else {
         boolean var2;
         if (var0 == null && var1 == null) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }

   public static boolean equalString(String var0, String var1) {
      return equalObject(var0, var1);
   }

   public static boolean equalsDouble(Double var0, Double var1) {
      boolean var2 = true;
      boolean var3 = true;
      if (var0 != null && var1 != null) {
         if (Double.doubleToLongBits(var0) != Double.doubleToLongBits(var1)) {
            var3 = false;
         }

         return var3;
      } else {
         if (var0 == null && var1 == null) {
            var3 = var2;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   public static String getAndroidId(Context var0) {
      return Reflection.getAndroidId(var0);
   }

   public static String getCpuAbi() {
      return Reflection.getCpuAbi();
   }

   public static String getFireAdvertisingId(ContentResolver var0) {
      if (var0 == null) {
         return null;
      } else {
         try {
            String var2 = Secure.getString(var0, "advertising_id");
            return var2;
         } catch (Exception var1) {
            return null;
         }
      }
   }

   public static Boolean getFireTrackingEnabled(ContentResolver var0) {
      boolean var1;
      label22: {
         try {
            if (Secure.getInt(var0, "limit_ad_tracking") == 0) {
               break label22;
            }
         } catch (Exception var2) {
            return null;
         }

         var1 = false;
         return var1;
      }

      var1 = true;
      return var1;
   }

   public static void getGoogleAdId(Context var0, final OnDeviceIdsRead var1) {
      ILogger var2 = AdjustFactory.getLogger();
      if (Looper.myLooper() != Looper.getMainLooper()) {
         var2.debug("GoogleAdId being read in the background");
         String var4 = getPlayAdId(var0);
         StringBuilder var3 = new StringBuilder();
         var3.append("GoogleAdId read ");
         var3.append(var4);
         var2.debug(var3.toString());
         var1.onGoogleAdIdRead(var4);
      } else {
         var2.debug("GoogleAdId being read in the foreground");
         (new AsyncTask() {
            protected String doInBackground(Context... var1x) {
               ILogger var2 = AdjustFactory.getLogger();
               String var4 = Util.getPlayAdId(var1x[0]);
               StringBuilder var3 = new StringBuilder();
               var3.append("GoogleAdId read ");
               var3.append(var4);
               var2.debug(var3.toString());
               return var4;
            }

            protected void onPostExecute(String var1x) {
               AdjustFactory.getLogger();
               var1.onGoogleAdIdRead(var1x);
            }
         }).execute(new Context[]{var0});
      }
   }

   public static Locale getLocale(Configuration var0) {
      Locale var1 = Reflection.getLocaleFromLocaleList(var0);
      return var1 != null ? var1 : Reflection.getLocaleFromField(var0);
   }

   private static ILogger getLogger() {
      return AdjustFactory.getLogger();
   }

   public static String getMacAddress(Context var0) {
      return Reflection.getMacAddress(var0);
   }

   public static String getPlayAdId(Context var0) {
      return Reflection.getPlayAdId(var0);
   }

   public static Map getPluginKeys(Context var0) {
      return Reflection.getPluginKeys(var0);
   }

   public static String getReasonString(String var0, Throwable var1) {
      return var1 != null ? String.format(Locale.US, "%s: %s", var0, var1) : String.format(Locale.US, "%s", var0);
   }

   public static String[] getSupportedAbis() {
      return Reflection.getSupportedAbis();
   }

   public static String getVmInstructionSet() {
      return Reflection.getVmInstructionSet();
   }

   public static long getWaitingTime(int var0, BackoffStrategy var1) {
      if (var0 < var1.minRetries) {
         return 0L;
      } else {
         long var2 = Math.min((long)Math.pow(2.0D, (double)(var0 - var1.minRetries)) * var1.milliSecondMultiplier, var1.maxWait);
         double var4 = randomInRange(var1.minRange, var1.maxRange);
         return (long)((double)var2 * var4);
      }
   }

   public static String hash(String var0, String var1) {
      try {
         byte[] var3 = var0.getBytes("UTF-8");
         MessageDigest var4 = MessageDigest.getInstance(var1);
         var4.update(var3, 0, var3.length);
         var0 = convertToHex(var4.digest());
      } catch (Exception var2) {
         var0 = null;
      }

      return var0;
   }

   public static int hashBoolean(Boolean var0) {
      return var0 == null ? 0 : var0.hashCode();
   }

   public static int hashEnum(Enum var0) {
      return var0 == null ? 0 : var0.hashCode();
   }

   public static int hashLong(Long var0) {
      return var0 == null ? 0 : var0.hashCode();
   }

   public static int hashObject(Object var0) {
      return var0 == null ? 0 : var0.hashCode();
   }

   public static int hashString(String var0) {
      return var0 == null ? 0 : var0.hashCode();
   }

   public static Boolean isPlayTrackingEnabled(Context var0) {
      return Reflection.isPlayTrackingEnabled(var0);
   }

   public static boolean isValidParameter(String var0, String var1, String var2) {
      if (var0 == null) {
         getLogger().error("%s parameter %s is missing", var2, var1);
         return false;
      } else if (var0.equals("")) {
         getLogger().error("%s parameter %s is empty", var2, var1);
         return false;
      } else {
         return true;
      }
   }

   public static String md5(String var0) {
      return hash(var0, "MD5");
   }

   public static Map mergeParameters(Map var0, Map var1, String var2) {
      if (var0 == null) {
         return var1;
      } else if (var1 == null) {
         return var0;
      } else {
         HashMap var6 = new HashMap(var0);
         ILogger var3 = getLogger();
         Iterator var4 = var1.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var7 = (Entry)var4.next();
            String var5 = (String)var6.put(var7.getKey(), var7.getValue());
            if (var5 != null) {
               var3.warn("Key %s with value %s from %s parameter was replaced by value %s", var7.getKey(), var5, var2, var7.getValue());
            }
         }

         return var6;
      }
   }

   public static String quote(String var0) {
      if (var0 == null) {
         return null;
      } else {
         return !Pattern.compile("\\s").matcher(var0).find() ? var0 : String.format(Locale.US, "'%s'", var0);
      }
   }

   private static double randomInRange(double var0, double var2) {
      return (new Random()).nextDouble() * (var2 - var0) + var0;
   }

   public static boolean readBooleanField(GetField var0, String var1, boolean var2) {
      try {
         boolean var3 = var0.get(var1, var2);
         return var3;
      } catch (Exception var4) {
         getLogger().debug("Unable to read '%s' field in migration device with message (%s)", var1, var4.getMessage());
         return var2;
      }
   }

   public static int readIntField(GetField var0, String var1, int var2) {
      try {
         int var3 = var0.get(var1, var2);
         return var3;
      } catch (Exception var4) {
         getLogger().debug("Unable to read '%s' field in migration device with message (%s)", var1, var4.getMessage());
         return var2;
      }
   }

   public static long readLongField(GetField var0, String var1, long var2) {
      try {
         long var4 = var0.get(var1, var2);
         return var4;
      } catch (Exception var6) {
         getLogger().debug("Unable to read '%s' field in migration device with message (%s)", var1, var6.getMessage());
         return var2;
      }
   }

   public static Object readObject(Context param0, String param1, String param2, Class param3) {
      // $FF: Couldn't be decompiled
   }

   public static Object readObjectField(GetField var0, String var1, Object var2) {
      try {
         Object var4 = var0.get(var1, var2);
         return var4;
      } catch (Exception var3) {
         getLogger().debug("Unable to read '%s' field in migration device with message (%s)", var1, var3.getMessage());
         return var2;
      }
   }

   public static String readStringField(GetField var0, String var1, String var2) {
      return (String)readObjectField(var0, var1, var2);
   }

   public static String sha1(String var0) {
      return hash(var0, "SHA-1");
   }

   public static void writeObject(Object param0, Context param1, String param2, String param3) {
      // $FF: Couldn't be decompiled
   }
}

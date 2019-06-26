package org.telegram.messenger.voip;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

public class VoIPServerConfig {
   private static JSONObject config = new JSONObject();

   public static boolean getBoolean(String var0, boolean var1) {
      return config.optBoolean(var0, var1);
   }

   public static double getDouble(String var0, double var1) {
      return config.optDouble(var0, var1);
   }

   public static int getInt(String var0, int var1) {
      return config.optInt(var0, var1);
   }

   public static String getString(String var0, String var1) {
      return config.optString(var0, var1);
   }

   private static native void nativeSetConfig(String var0);

   public static void setConfig(String var0) {
      try {
         JSONObject var1 = new JSONObject(var0);
         config = var1;
         nativeSetConfig(var0);
      } catch (JSONException var2) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("Error parsing VoIP config", var2);
         }
      }

   }
}

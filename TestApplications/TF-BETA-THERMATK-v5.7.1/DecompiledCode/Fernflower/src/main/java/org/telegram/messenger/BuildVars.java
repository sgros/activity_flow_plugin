package org.telegram.messenger;

public class BuildVars {
   public static String APP_HASH;
   public static int APP_ID;
   public static int BUILD_VERSION;
   public static String BUILD_VERSION_STRING;
   public static boolean CHECK_UPDATES;
   public static boolean DEBUG_PRIVATE_VERSION;
   public static boolean DEBUG_VERSION;
   public static boolean LOGS_ENABLED;
   public static String PLAYSTORE_APP_URL;
   public static String SMS_HASH;
   public static boolean USE_CLOUD_STRINGS;

   static {
      if (ApplicationLoader.applicationContext != null) {
         LOGS_ENABLED = ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", 0).getBoolean("logsEnabled", DEBUG_VERSION);
      }

   }
}

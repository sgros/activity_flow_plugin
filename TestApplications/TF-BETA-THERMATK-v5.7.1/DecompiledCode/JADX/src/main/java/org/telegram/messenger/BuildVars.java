package org.telegram.messenger;

public class BuildVars {
    public static String APP_HASH = "c84d9229db1d6be95c067b02b126352c";
    public static int APP_ID = 12834;
    public static int BUILD_VERSION = 1608;
    public static String BUILD_VERSION_STRING = "5.7.0";
    public static boolean CHECK_UPDATES = false;
    public static boolean DEBUG_PRIVATE_VERSION = false;
    public static boolean DEBUG_VERSION = false;
    public static boolean LOGS_ENABLED = ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", 0).getBoolean("logsEnabled", DEBUG_VERSION);
    public static String PLAYSTORE_APP_URL = "";
    public static String SMS_HASH = "";
    public static boolean USE_CLOUD_STRINGS = true;

    static {
        if (ApplicationLoader.applicationContext != null) {
        }
    }
}

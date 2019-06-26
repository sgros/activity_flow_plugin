package org.telegram.messenger;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Intent;
import android.os.Process;
import android.text.TextUtils;

public class XiaomiUtilities {
   public static final int OP_ACCESS_XIAOMI_ACCOUNT = 10015;
   public static final int OP_AUTO_START = 10008;
   public static final int OP_BACKGROUND_START_ACTIVITY = 10021;
   public static final int OP_BLUETOOTH_CHANGE = 10002;
   public static final int OP_BOOT_COMPLETED = 10007;
   public static final int OP_DATA_CONNECT_CHANGE = 10003;
   public static final int OP_DELETE_CALL_LOG = 10013;
   public static final int OP_DELETE_CONTACTS = 10012;
   public static final int OP_DELETE_MMS = 10011;
   public static final int OP_DELETE_SMS = 10010;
   public static final int OP_EXACT_ALARM = 10014;
   public static final int OP_GET_INSTALLED_APPS = 10022;
   public static final int OP_GET_TASKS = 10019;
   public static final int OP_INSTALL_SHORTCUT = 10017;
   public static final int OP_NFC = 10016;
   public static final int OP_NFC_CHANGE = 10009;
   public static final int OP_READ_MMS = 10005;
   public static final int OP_READ_NOTIFICATION_SMS = 10018;
   public static final int OP_SEND_MMS = 10004;
   public static final int OP_SERVICE_FOREGROUND = 10023;
   public static final int OP_SHOW_WHEN_LOCKED = 10020;
   public static final int OP_WIFI_CHANGE = 10001;
   public static final int OP_WRITE_MMS = 10006;

   public static int getMIUIMajorVersion() {
      String var0 = AndroidUtilities.getSystemProperty("ro.miui.ui.version.name");
      if (var0 != null) {
         try {
            int var1 = Integer.parseInt(var0.replace("V", ""));
            return var1;
         } catch (NumberFormatException var2) {
         }
      }

      return -1;
   }

   public static Intent getPermissionManagerIntent() {
      Intent var0 = new Intent("miui.intent.action.APP_PERM_EDITOR");
      var0.putExtra("extra_package_uid", Process.myUid());
      var0.putExtra("extra_pkgname", ApplicationLoader.applicationContext.getPackageName());
      return var0;
   }

   @TargetApi(19)
   public static boolean isCustomPermissionGranted(int var0) {
      boolean var1 = true;

      try {
         AppOpsManager var2 = (AppOpsManager)ApplicationLoader.applicationContext.getSystemService("appops");
         var0 = (Integer)AppOpsManager.class.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class).invoke(var2, var0, Process.myUid(), ApplicationLoader.applicationContext.getPackageName());
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
         return true;
      }

      if (var0 != 0) {
         var1 = false;
      }

      return var1;
   }

   public static boolean isMIUI() {
      return TextUtils.isEmpty(AndroidUtilities.getSystemProperty("ro.miui.ui.version.name")) ^ true;
   }
}

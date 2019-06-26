package org.telegram.messenger;

import android.annotation.TargetApi;
import android.content.AsyncQueryHandler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import java.io.Closeable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NotificationBadge {
   private static final List BADGERS = new LinkedList();
   private static NotificationBadge.Badger badger;
   private static ComponentName componentName;
   private static boolean initied;

   static {
      BADGERS.add(NotificationBadge.AdwHomeBadger.class);
      BADGERS.add(NotificationBadge.ApexHomeBadger.class);
      BADGERS.add(NotificationBadge.NewHtcHomeBadger.class);
      BADGERS.add(NotificationBadge.NovaHomeBadger.class);
      BADGERS.add(NotificationBadge.SonyHomeBadger.class);
      BADGERS.add(NotificationBadge.XiaomiHomeBadger.class);
      BADGERS.add(NotificationBadge.AsusHomeBadger.class);
      BADGERS.add(NotificationBadge.HuaweiHomeBadger.class);
      BADGERS.add(NotificationBadge.OPPOHomeBader.class);
      BADGERS.add(NotificationBadge.SamsungHomeBadger.class);
      BADGERS.add(NotificationBadge.ZukHomeBadger.class);
      BADGERS.add(NotificationBadge.VivoHomeBadger.class);
   }

   public static boolean applyCount(int var0) {
      try {
         if (badger == null && !initied) {
            initBadger();
            initied = true;
         }

         if (badger == null) {
            return false;
         } else {
            badger.executeBadge(var0);
            return true;
         }
      } catch (Throwable var2) {
         return false;
      }
   }

   private static boolean canResolveBroadcast(Intent var0) {
      PackageManager var1 = ApplicationLoader.applicationContext.getPackageManager();
      boolean var2 = false;
      List var4 = var1.queryBroadcastReceivers(var0, 0);
      boolean var3 = var2;
      if (var4 != null) {
         var3 = var2;
         if (var4.size() > 0) {
            var3 = true;
         }
      }

      return var3;
   }

   public static void close(Cursor var0) {
      if (var0 != null && !var0.isClosed()) {
         var0.close();
      }

   }

   public static void closeQuietly(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (Throwable var1) {
         }
      }

   }

   private static boolean initBadger() {
      Context var0 = ApplicationLoader.applicationContext;
      Intent var1 = var0.getPackageManager().getLaunchIntentForPackage(var0.getPackageName());
      int var2 = 0;
      if (var1 == null) {
         return false;
      } else {
         componentName = var1.getComponent();
         Intent var3 = new Intent("android.intent.action.MAIN");
         var3.addCategory("android.intent.category.HOME");
         ResolveInfo var9 = var0.getPackageManager().resolveActivity(var3, 65536);
         Iterator var5;
         Class var10;
         NotificationBadge.Badger var11;
         if (var9 != null) {
            String var4 = var9.activityInfo.packageName;
            var5 = BADGERS.iterator();

            while(var5.hasNext()) {
               var10 = (Class)var5.next();

               try {
                  var11 = (NotificationBadge.Badger)var10.newInstance();
               } catch (Exception var7) {
                  var11 = null;
               }

               if (var11 != null && var11.getSupportLaunchers().contains(var4)) {
                  badger = var11;
                  break;
               }
            }

            if (badger != null) {
               return true;
            }
         }

         List var12 = var0.getPackageManager().queryIntentActivities(var3, 65536);
         if (var12 != null) {
            while(var2 < var12.size()) {
               String var8 = ((ResolveInfo)var12.get(var2)).activityInfo.packageName;
               var5 = BADGERS.iterator();

               while(var5.hasNext()) {
                  var10 = (Class)var5.next();

                  try {
                     var11 = (NotificationBadge.Badger)var10.newInstance();
                  } catch (Exception var6) {
                     var11 = null;
                  }

                  if (var11 != null && var11.getSupportLaunchers().contains(var8)) {
                     badger = var11;
                     break;
                  }
               }

               if (badger != null) {
                  break;
               }

               ++var2;
            }
         }

         if (badger == null) {
            if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
               badger = new NotificationBadge.XiaomiHomeBadger();
            } else if (Build.MANUFACTURER.equalsIgnoreCase("ZUK")) {
               badger = new NotificationBadge.ZukHomeBadger();
            } else if (Build.MANUFACTURER.equalsIgnoreCase("OPPO")) {
               badger = new NotificationBadge.OPPOHomeBader();
            } else if (Build.MANUFACTURER.equalsIgnoreCase("VIVO")) {
               badger = new NotificationBadge.VivoHomeBadger();
            } else {
               badger = new NotificationBadge.DefaultBadger();
            }
         }

         return true;
      }
   }

   public static class AdwHomeBadger implements NotificationBadge.Badger {
      public static final String CLASSNAME = "CNAME";
      public static final String COUNT = "COUNT";
      public static final String INTENT_UPDATE_COUNTER = "org.adw.launcher.counter.SEND";
      public static final String PACKAGENAME = "PNAME";

      public void executeBadge(int var1) {
         final Intent var2 = new Intent("org.adw.launcher.counter.SEND");
         var2.putExtra("PNAME", NotificationBadge.componentName.getPackageName());
         var2.putExtra("CNAME", NotificationBadge.componentName.getClassName());
         var2.putExtra("COUNT", var1);
         if (NotificationBadge.canResolveBroadcast(var2)) {
            AndroidUtilities.runOnUIThread(new Runnable() {
               public void run() {
                  ApplicationLoader.applicationContext.sendBroadcast(var2);
               }
            });
         }

      }

      public List getSupportLaunchers() {
         return Arrays.asList("org.adw.launcher", "org.adwfreak.launcher");
      }
   }

   public static class ApexHomeBadger implements NotificationBadge.Badger {
      private static final String CLASS = "class";
      private static final String COUNT = "count";
      private static final String INTENT_UPDATE_COUNTER = "com.anddoes.launcher.COUNTER_CHANGED";
      private static final String PACKAGENAME = "package";

      public void executeBadge(int var1) {
         final Intent var2 = new Intent("com.anddoes.launcher.COUNTER_CHANGED");
         var2.putExtra("package", NotificationBadge.componentName.getPackageName());
         var2.putExtra("count", var1);
         var2.putExtra("class", NotificationBadge.componentName.getClassName());
         if (NotificationBadge.canResolveBroadcast(var2)) {
            AndroidUtilities.runOnUIThread(new Runnable() {
               public void run() {
                  ApplicationLoader.applicationContext.sendBroadcast(var2);
               }
            });
         }

      }

      public List getSupportLaunchers() {
         return Arrays.asList("com.anddoes.launcher");
      }
   }

   public static class AsusHomeBadger implements NotificationBadge.Badger {
      private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
      private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";
      private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
      private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";

      public void executeBadge(int var1) {
         final Intent var2 = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
         var2.putExtra("badge_count", var1);
         var2.putExtra("badge_count_package_name", NotificationBadge.componentName.getPackageName());
         var2.putExtra("badge_count_class_name", NotificationBadge.componentName.getClassName());
         var2.putExtra("badge_vip_count", 0);
         if (NotificationBadge.canResolveBroadcast(var2)) {
            AndroidUtilities.runOnUIThread(new Runnable() {
               public void run() {
                  ApplicationLoader.applicationContext.sendBroadcast(var2);
               }
            });
         }

      }

      public List getSupportLaunchers() {
         return Arrays.asList("com.asus.launcher");
      }
   }

   public interface Badger {
      void executeBadge(int var1);

      List getSupportLaunchers();
   }

   public static class DefaultBadger implements NotificationBadge.Badger {
      private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
      private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";
      private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
      private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";

      public void executeBadge(int var1) {
         final Intent var2 = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
         var2.putExtra("badge_count", var1);
         var2.putExtra("badge_count_package_name", NotificationBadge.componentName.getPackageName());
         var2.putExtra("badge_count_class_name", NotificationBadge.componentName.getClassName());
         AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
               try {
                  ApplicationLoader.applicationContext.sendBroadcast(var2);
               } catch (Exception var2x) {
               }

            }
         });
      }

      public List getSupportLaunchers() {
         return Arrays.asList("fr.neamar.kiss", "com.quaap.launchtime", "com.quaap.launchtime_official");
      }
   }

   public static class HuaweiHomeBadger implements NotificationBadge.Badger {
      public void executeBadge(int var1) {
         final Bundle var2 = new Bundle();
         var2.putString("package", ApplicationLoader.applicationContext.getPackageName());
         var2.putString("class", NotificationBadge.componentName.getClassName());
         var2.putInt("badgenumber", var1);
         AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
               try {
                  ApplicationLoader.applicationContext.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", (String)null, var2);
               } catch (Exception var2x) {
                  FileLog.e((Throwable)var2x);
               }

            }
         });
      }

      public List getSupportLaunchers() {
         return Arrays.asList("com.huawei.android.launcher");
      }
   }

   public static class NewHtcHomeBadger implements NotificationBadge.Badger {
      public static final String COUNT = "count";
      public static final String EXTRA_COMPONENT = "com.htc.launcher.extra.COMPONENT";
      public static final String EXTRA_COUNT = "com.htc.launcher.extra.COUNT";
      public static final String INTENT_SET_NOTIFICATION = "com.htc.launcher.action.SET_NOTIFICATION";
      public static final String INTENT_UPDATE_SHORTCUT = "com.htc.launcher.action.UPDATE_SHORTCUT";
      public static final String PACKAGENAME = "packagename";

      public void executeBadge(int var1) {
         final Intent var2 = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
         var2.putExtra("com.htc.launcher.extra.COMPONENT", NotificationBadge.componentName.flattenToShortString());
         var2.putExtra("com.htc.launcher.extra.COUNT", var1);
         final Intent var3 = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
         var3.putExtra("packagename", NotificationBadge.componentName.getPackageName());
         var3.putExtra("count", var1);
         if (NotificationBadge.canResolveBroadcast(var2) || NotificationBadge.canResolveBroadcast(var3)) {
            AndroidUtilities.runOnUIThread(new Runnable() {
               public void run() {
                  ApplicationLoader.applicationContext.sendBroadcast(var2);
                  ApplicationLoader.applicationContext.sendBroadcast(var3);
               }
            });
         }

      }

      public List getSupportLaunchers() {
         return Arrays.asList("com.htc.launcher");
      }
   }

   public static class NovaHomeBadger implements NotificationBadge.Badger {
      private static final String CONTENT_URI = "content://com.teslacoilsw.notifier/unread_count";
      private static final String COUNT = "count";
      private static final String TAG = "tag";

      public void executeBadge(int var1) {
         ContentValues var2 = new ContentValues();
         StringBuilder var3 = new StringBuilder();
         var3.append(NotificationBadge.componentName.getPackageName());
         var3.append("/");
         var3.append(NotificationBadge.componentName.getClassName());
         var2.put("tag", var3.toString());
         var2.put("count", var1);
         ApplicationLoader.applicationContext.getContentResolver().insert(Uri.parse("content://com.teslacoilsw.notifier/unread_count"), var2);
      }

      public List getSupportLaunchers() {
         return Arrays.asList("com.teslacoilsw.launcher");
      }
   }

   public static class OPPOHomeBader implements NotificationBadge.Badger {
      private static final String INTENT_ACTION = "com.oppo.unsettledevent";
      private static final String INTENT_EXTRA_BADGEUPGRADE_COUNT = "app_badge_count";
      private static final String INTENT_EXTRA_BADGE_COUNT = "number";
      private static final String INTENT_EXTRA_BADGE_UPGRADENUMBER = "upgradeNumber";
      private static final String INTENT_EXTRA_PACKAGENAME = "pakeageName";
      private static final String PROVIDER_CONTENT_URI = "content://com.android.badge/badge";
      private int mCurrentTotalCount = -1;

      @TargetApi(11)
      private void executeBadgeByContentProvider(int var1) {
         try {
            Bundle var2 = new Bundle();
            var2.putInt("app_badge_count", var1);
            ApplicationLoader.applicationContext.getContentResolver().call(Uri.parse("content://com.android.badge/badge"), "setAppBadgeCount", (String)null, var2);
         } catch (Throwable var3) {
         }

      }

      public void executeBadge(int var1) {
         if (this.mCurrentTotalCount != var1) {
            this.mCurrentTotalCount = var1;
            this.executeBadgeByContentProvider(var1);
         }
      }

      public List getSupportLaunchers() {
         return Collections.singletonList("com.oppo.launcher");
      }
   }

   public static class SamsungHomeBadger implements NotificationBadge.Badger {
      private static final String[] CONTENT_PROJECTION = new String[]{"_id", "class"};
      private static final String CONTENT_URI = "content://com.sec.badge/apps?notify=true";
      private static NotificationBadge.DefaultBadger defaultBadger;

      private ContentValues getContentValues(ComponentName var1, int var2, boolean var3) {
         ContentValues var4 = new ContentValues();
         if (var3) {
            var4.put("package", var1.getPackageName());
            var4.put("class", var1.getClassName());
         }

         var4.put("badgecount", var2);
         return var4;
      }

      public void executeBadge(int var1) {
         boolean var10001;
         label701: {
            try {
               if (defaultBadger == null) {
                  NotificationBadge.DefaultBadger var2 = new NotificationBadge.DefaultBadger();
                  defaultBadger = var2;
               }
            } catch (Exception var80) {
               var10001 = false;
               break label701;
            }

            try {
               defaultBadger.executeBadge(var1);
            } catch (Exception var79) {
               var10001 = false;
            }
         }

         Uri var3 = Uri.parse("content://com.sec.badge/apps?notify=true");
         ContentResolver var4 = ApplicationLoader.applicationContext.getContentResolver();
         Cursor var81 = null;

         Cursor var5;
         label690: {
            Throwable var10000;
            label705: {
               try {
                  var5 = var4.query(var3, CONTENT_PROJECTION, "package=?", new String[]{NotificationBadge.componentName.getPackageName()}, (String)null);
               } catch (Throwable var78) {
                  var10000 = var78;
                  var10001 = false;
                  break label705;
               }

               if (var5 == null) {
                  break label690;
               }

               var81 = var5;

               String var6;
               try {
                  var6 = NotificationBadge.componentName.getClassName();
               } catch (Throwable var77) {
                  var10000 = var77;
                  var10001 = false;
                  break label705;
               }

               boolean var7 = false;

               while(true) {
                  var81 = var5;

                  try {
                     if (!var5.moveToNext()) {
                        break;
                     }
                  } catch (Throwable var76) {
                     var10000 = var76;
                     var10001 = false;
                     break label705;
                  }

                  var81 = var5;

                  int var8;
                  try {
                     var8 = var5.getInt(0);
                  } catch (Throwable var74) {
                     var10000 = var74;
                     var10001 = false;
                     break label705;
                  }

                  var81 = var5;

                  try {
                     var4.update(var3, this.getContentValues(NotificationBadge.componentName, var1, false), "_id=?", new String[]{String.valueOf(var8)});
                  } catch (Throwable var73) {
                     var10000 = var73;
                     var10001 = false;
                     break label705;
                  }

                  var81 = var5;

                  try {
                     if (!var6.equals(var5.getString(var5.getColumnIndex("class")))) {
                        continue;
                     }
                  } catch (Throwable var75) {
                     var10000 = var75;
                     var10001 = false;
                     break label705;
                  }

                  var7 = true;
               }

               if (var7) {
                  break label690;
               }

               var81 = var5;

               label662:
               try {
                  var4.insert(var3, this.getContentValues(NotificationBadge.componentName, var1, true));
                  break label690;
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  break label662;
               }
            }

            Throwable var82 = var10000;
            NotificationBadge.close(var81);
            throw var82;
         }

         NotificationBadge.close(var5);
      }

      public List getSupportLaunchers() {
         return Arrays.asList("com.sec.android.app.launcher", "com.sec.android.app.twlauncher");
      }
   }

   public static class SonyHomeBadger implements NotificationBadge.Badger {
      private static final String INTENT_ACTION = "com.sonyericsson.home.action.UPDATE_BADGE";
      private static final String INTENT_EXTRA_ACTIVITY_NAME = "com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME";
      private static final String INTENT_EXTRA_MESSAGE = "com.sonyericsson.home.intent.extra.badge.MESSAGE";
      private static final String INTENT_EXTRA_PACKAGE_NAME = "com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME";
      private static final String INTENT_EXTRA_SHOW_MESSAGE = "com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE";
      private static final String PROVIDER_COLUMNS_ACTIVITY_NAME = "activity_name";
      private static final String PROVIDER_COLUMNS_BADGE_COUNT = "badge_count";
      private static final String PROVIDER_COLUMNS_PACKAGE_NAME = "package_name";
      private static final String PROVIDER_CONTENT_URI = "content://com.sonymobile.home.resourceprovider/badge";
      private static final String SONY_HOME_PROVIDER_NAME = "com.sonymobile.home.resourceprovider";
      private static AsyncQueryHandler mQueryHandler;
      private final Uri BADGE_CONTENT_URI = Uri.parse("content://com.sonymobile.home.resourceprovider/badge");

      private static void executeBadgeByBroadcast(int var0) {
         final Intent var1 = new Intent("com.sonyericsson.home.action.UPDATE_BADGE");
         var1.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", NotificationBadge.componentName.getPackageName());
         var1.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", NotificationBadge.componentName.getClassName());
         var1.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(var0));
         boolean var2;
         if (var0 > 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         var1.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", var2);
         AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
               ApplicationLoader.applicationContext.sendBroadcast(var1);
            }
         });
      }

      private void executeBadgeByContentProvider(int var1) {
         if (var1 >= 0) {
            if (mQueryHandler == null) {
               mQueryHandler = new AsyncQueryHandler(ApplicationLoader.applicationContext.getApplicationContext().getContentResolver()) {
                  public void handleMessage(Message var1) {
                     try {
                        super.handleMessage(var1);
                     } catch (Throwable var2) {
                     }

                  }
               };
            }

            this.insertBadgeAsync(var1, NotificationBadge.componentName.getPackageName(), NotificationBadge.componentName.getClassName());
         }
      }

      private void insertBadgeAsync(int var1, String var2, String var3) {
         ContentValues var4 = new ContentValues();
         var4.put("badge_count", var1);
         var4.put("package_name", var2);
         var4.put("activity_name", var3);
         mQueryHandler.startInsert(0, (Object)null, this.BADGE_CONTENT_URI, var4);
      }

      private static boolean sonyBadgeContentProviderExists() {
         PackageManager var0 = ApplicationLoader.applicationContext.getPackageManager();
         boolean var1 = false;
         if (var0.resolveContentProvider("com.sonymobile.home.resourceprovider", 0) != null) {
            var1 = true;
         }

         return var1;
      }

      public void executeBadge(int var1) {
         if (sonyBadgeContentProviderExists()) {
            this.executeBadgeByContentProvider(var1);
         } else {
            executeBadgeByBroadcast(var1);
         }

      }

      public List getSupportLaunchers() {
         return Arrays.asList("com.sonyericsson.home", "com.sonymobile.home");
      }
   }

   public static class VivoHomeBadger implements NotificationBadge.Badger {
      public void executeBadge(int var1) {
         Intent var2 = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
         var2.putExtra("packageName", ApplicationLoader.applicationContext.getPackageName());
         var2.putExtra("className", NotificationBadge.componentName.getClassName());
         var2.putExtra("notificationNum", var1);
         ApplicationLoader.applicationContext.sendBroadcast(var2);
      }

      public List getSupportLaunchers() {
         return Arrays.asList("com.vivo.launcher");
      }
   }

   public static class XiaomiHomeBadger implements NotificationBadge.Badger {
      public static final String EXTRA_UPDATE_APP_COMPONENT_NAME = "android.intent.extra.update_application_component_name";
      public static final String EXTRA_UPDATE_APP_MSG_TEXT = "android.intent.extra.update_application_message_text";
      public static final String INTENT_ACTION = "android.intent.action.APPLICATION_MESSAGE_UPDATE";

      public void executeBadge(int var1) {
         String var2 = "";

         Object var5;
         label42: {
            boolean var10001;
            Object var3;
            Field var4;
            try {
               var3 = Class.forName("android.app.MiuiNotification").newInstance();
               var4 = var3.getClass().getDeclaredField("messageCount");
               var4.setAccessible(true);
            } catch (Throwable var8) {
               var10001 = false;
               break label42;
            }

            if (var1 == 0) {
               var5 = "";
            } else {
               try {
                  var5 = var1;
               } catch (Throwable var7) {
                  var10001 = false;
                  break label42;
               }
            }

            try {
               var4.set(var3, String.valueOf(var5));
               return;
            } catch (Throwable var6) {
               var10001 = false;
            }
         }

         final Intent var9 = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
         StringBuilder var10 = new StringBuilder();
         var10.append(NotificationBadge.componentName.getPackageName());
         var10.append("/");
         var10.append(NotificationBadge.componentName.getClassName());
         var9.putExtra("android.intent.extra.update_application_component_name", var10.toString());
         if (var1 == 0) {
            var5 = var2;
         } else {
            var5 = var1;
         }

         var9.putExtra("android.intent.extra.update_application_message_text", String.valueOf(var5));
         if (NotificationBadge.canResolveBroadcast(var9)) {
            AndroidUtilities.runOnUIThread(new Runnable() {
               public void run() {
                  ApplicationLoader.applicationContext.sendBroadcast(var9);
               }
            });
         }

      }

      public List getSupportLaunchers() {
         return Arrays.asList("com.miui.miuilite", "com.miui.home", "com.miui.miuihome", "com.miui.miuihome2", "com.miui.mihome", "com.miui.mihome2");
      }
   }

   public static class ZukHomeBadger implements NotificationBadge.Badger {
      private final Uri CONTENT_URI = Uri.parse("content://com.android.badge/badge");

      @TargetApi(11)
      public void executeBadge(int var1) {
         final Bundle var2 = new Bundle();
         var2.putInt("app_badge_count", var1);
         AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
               try {
                  ApplicationLoader.applicationContext.getContentResolver().call(ZukHomeBadger.this.CONTENT_URI, "setAppBadgeCount", (String)null, var2);
               } catch (Exception var2x) {
                  FileLog.e((Throwable)var2x);
               }

            }
         });
      }

      public List getSupportLaunchers() {
         return Collections.singletonList("com.zui.launcher");
      }
   }
}

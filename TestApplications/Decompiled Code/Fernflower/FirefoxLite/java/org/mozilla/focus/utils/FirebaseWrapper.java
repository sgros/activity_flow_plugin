package org.mozilla.focus.utils;

import android.content.Context;
import android.os.Bundle;
import java.util.HashMap;

abstract class FirebaseWrapper {
   private static FirebaseWrapper instance;

   static void enableAnalytics(Context var0, boolean var1) {
   }

   static void enableCloudMessaging(Context var0, String var1, boolean var2) {
   }

   static void enableCrashlytics(Context var0, boolean var1) {
   }

   static void enableRemoteConfig(Context var0, boolean var1, FirebaseWrapper.RemoteConfigFetchCallback var2) {
   }

   public static void event(Context var0, String var1, Bundle var2) {
   }

   public static String getFcmToken() {
      return "";
   }

   static FirebaseWrapper getInstance() {
      return instance;
   }

   static boolean getRcBoolean(Context var0, String var1) {
      if (instance == null) {
         return false;
      } else {
         Object var2 = instance.getRemoteConfigDefault(var0).get(var1);
         return var2 instanceof Boolean ? (Boolean)var2 : false;
      }
   }

   static long getRcLong(Context var0, String var1) {
      if (instance == null) {
         return 0L;
      } else {
         Object var2 = instance.getRemoteConfigDefault(var0).get(var1);
         if (var2 instanceof Integer) {
            return ((Integer)var2).longValue();
         } else {
            return var2 instanceof Long ? (Long)var2 : 0L;
         }
      }
   }

   static String getRcString(Context var0, String var1) {
      if (instance == null) {
         return "";
      } else {
         Object var2 = instance.getRemoteConfigDefault(var0).get(var1);
         return var2 instanceof String ? (String)var2 : "";
      }
   }

   static void initInternal(FirebaseWrapper var0) {
      if (instance == null) {
         instance = var0;
      }
   }

   static String prettify(String var0) {
      return var0.replace("<BR>", "\n");
   }

   static void setDeveloperModeEnabled(boolean var0) {
   }

   static void updateInstanceId(Context var0, boolean var1) {
   }

   abstract HashMap getRemoteConfigDefault(Context var1);

   abstract void refreshRemoteConfigDefault(Context var1, FirebaseWrapper.RemoteConfigFetchCallback var2);

   public interface RemoteConfigFetchCallback {
   }
}

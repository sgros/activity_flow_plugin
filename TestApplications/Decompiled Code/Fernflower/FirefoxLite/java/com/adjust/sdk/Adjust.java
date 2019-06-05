package com.adjust.sdk;

import android.content.Context;
import android.net.Uri;

public class Adjust {
   private static AdjustInstance defaultInstance;

   private Adjust() {
   }

   public static void addSessionCallbackParameter(String var0, String var1) {
      getDefaultInstance().addSessionCallbackParameter(var0, var1);
   }

   public static void addSessionPartnerParameter(String var0, String var1) {
      getDefaultInstance().addSessionPartnerParameter(var0, var1);
   }

   public static void appWillOpenUrl(Uri var0) {
      getDefaultInstance().appWillOpenUrl(var0);
   }

   public static String getAdid() {
      return getDefaultInstance().getAdid();
   }

   public static AdjustAttribution getAttribution() {
      return getDefaultInstance().getAttribution();
   }

   public static AdjustInstance getDefaultInstance() {
      synchronized(Adjust.class){}

      AdjustInstance var0;
      try {
         if (defaultInstance == null) {
            var0 = new AdjustInstance();
            defaultInstance = var0;
         }

         var0 = defaultInstance;
      } finally {
         ;
      }

      return var0;
   }

   public static void getGoogleAdId(Context var0, OnDeviceIdsRead var1) {
      Util.getGoogleAdId(var0, var1);
   }

   public static boolean isEnabled() {
      return getDefaultInstance().isEnabled();
   }

   public static void onCreate(AdjustConfig var0) {
      getDefaultInstance().onCreate(var0);
   }

   public static void onPause() {
      getDefaultInstance().onPause();
   }

   public static void onResume() {
      getDefaultInstance().onResume();
   }

   public static void removeSessionCallbackParameter(String var0) {
      getDefaultInstance().removeSessionCallbackParameter(var0);
   }

   public static void removeSessionPartnerParameter(String var0) {
      getDefaultInstance().removeSessionPartnerParameter(var0);
   }

   public static void resetSessionCallbackParameters() {
      getDefaultInstance().resetSessionCallbackParameters();
   }

   public static void resetSessionPartnerParameters() {
      getDefaultInstance().resetSessionPartnerParameters();
   }

   public static void sendFirstPackages() {
      getDefaultInstance().sendFirstPackages();
   }

   public static void setEnabled(boolean var0) {
      getDefaultInstance().setEnabled(var0);
   }

   public static void setOfflineMode(boolean var0) {
      getDefaultInstance().setOfflineMode(var0);
   }

   public static void setPushToken(String var0) {
      getDefaultInstance().setPushToken(var0);
   }

   public static void setReferrer(String var0) {
      getDefaultInstance().sendReferrer(var0);
   }

   public static void trackEvent(AdjustEvent var0) {
      getDefaultInstance().trackEvent(var0);
   }
}

package org.mozilla.focus.utils;

import android.content.Context;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.rocket.content.data.ShoppingLink;

public class AppConfigWrapper {
   public static String getBannerRootConfig(Context var0) {
      return FirebaseHelper.getRcString(var0, "banner_manifest");
   }

   public static int getDriveDefaultBrowserFromMenuSettingThreshold() {
      return 2;
   }

   public static ArrayList getEcommerceShoppingLinks() {
      ArrayList var0 = new ArrayList();
      String var1 = FirebaseHelper.getRcString((Context)null, "str_e_commerce_shoppinglinks");

      boolean var10001;
      JSONArray var2;
      try {
         var2 = new JSONArray(var1);
      } catch (JSONException var6) {
         var10001 = false;
         return var0;
      }

      int var3 = 0;

      while(true) {
         try {
            if (var3 >= var2.length()) {
               break;
            }

            JSONObject var7 = (JSONObject)var2.get(var3);
            ShoppingLink var4 = new ShoppingLink(var7.optString("url"), var7.optString("name"), var7.optString("img"), var7.optString("source"));
            var0.add(var4);
         } catch (JSONException var5) {
            var10001 = false;
            break;
         }

         ++var3;
      }

      return var0;
   }

   public static long getFeatureSurvey(Context var0) {
      return FirebaseHelper.getRcLong(var0, "feature_survey");
   }

   public static String getFirstLaunchNotificationMessage(Context var0) {
      return FirebaseHelper.getRcString(var0, "first_launch_notification_message");
   }

   public static long getFirstLaunchWorkerTimer(Context var0) {
      return FirebaseHelper.getRcLong(var0, "first_launch_timer_minutes");
   }

   public static String getLifeFeedProviderUrl(Context var0, String var1) {
      String var7 = FirebaseHelper.getRcString(var0, "life_feed_providers");
      String var2 = "";

      JSONException var10000;
      label39: {
         boolean var10001;
         JSONArray var3;
         try {
            var3 = new JSONArray(var7);
         } catch (JSONException var6) {
            var10000 = var6;
            var10001 = false;
            break label39;
         }

         int var4 = 0;

         while(true) {
            var7 = var2;

            try {
               if (var4 >= var3.length()) {
                  return var7;
               }

               JSONObject var9 = var3.getJSONObject(var4);
               if (var9.getString("name").equalsIgnoreCase(var1)) {
                  var7 = var9.getString("url");
                  return var7;
               }
            } catch (JSONException var5) {
               var10000 = var5;
               var10001 = false;
               break;
            }

            ++var4;
         }
      }

      JSONException var8 = var10000;
      var8.printStackTrace();
      var7 = var2;
      return var7;
   }

   public static boolean getMyshotUnreadEnabled(Context var0) {
      return FirebaseHelper.getRcBoolean(var0, "enable_my_shot_unread");
   }

   public static String getRateAppDialogContent(Context var0) {
      return FirebaseHelper.getRcString(var0, "rate_app_dialog_text_content");
   }

   public static String getRateAppDialogTitle(Context var0) {
      return FirebaseHelper.getRcString(var0, "rate_app_dialog_text_title");
   }

   public static String getRateAppNegativeString(Context var0) {
      return FirebaseHelper.getRcString(var0, "rate_app_dialog_text_negative");
   }

   public static long getRateAppNotificationLaunchTimeThreshold(Context var0) {
      return FirebaseHelper.getRcLong(var0, "rate_app_notification_threshold");
   }

   public static String getRateAppPositiveString(Context var0) {
      return FirebaseHelper.getRcString(var0, "rate_app_dialog_text_positive");
   }

   public static long getRateDialogLaunchTimeThreshold(Context var0) {
      return FirebaseHelper.getRcLong(var0, "rate_app_dialog_threshold");
   }

   public static String getScreenshotCategoryUrl(Context var0) {
      return FirebaseHelper.getRcString(var0, "screenshot_category_manifest");
   }

   static String getShareAppDialogContent(Context var0) {
      return FirebaseHelper.prettify(FirebaseHelper.getRcString(var0, "str_share_app_dialog_content"));
   }

   static String getShareAppDialogTitle(Context var0) {
      return FirebaseHelper.getRcString(var0, "str_share_app_dialog_title");
   }

   static String getShareAppMessage(Context var0) {
      return FirebaseHelper.getRcString(var0, "str_share_app_dialog_msg");
   }

   public static long getShareDialogLaunchTimeThreshold(Context var0, boolean var1) {
      return var1 ? FirebaseHelper.getRcLong(var0, "share_app_dialog_threshold") + getRateAppNotificationLaunchTimeThreshold(var0) - getRateDialogLaunchTimeThreshold(var0) : FirebaseHelper.getRcLong(var0, "share_app_dialog_threshold");
   }

   public static int getSurveyNotificationLaunchTimeThreshold() {
      return 3;
   }

   public static String getVpnRecommenderPackage(Context var0) {
      return FirebaseHelper.getRcString(var0, "vpn_recommender_package");
   }

   public static String getVpnRecommenderUrl(Context var0) {
      return FirebaseHelper.getRcString(var0, "vpn_recommender_url");
   }

   public static boolean hasEcommerceShoppingLink() {
      return getEcommerceShoppingLinks().isEmpty() ^ true;
   }

   public static boolean hasNewsPortal(Context var0) {
      return FirebaseHelper.getRcBoolean(var0, "enable_life_feed");
   }

   public static boolean isPrivateModeEnabled(Context var0) {
      return FirebaseHelper.getRcBoolean(var0, "enable_private_mode");
   }

   public static boolean isSurveyNotificationEnabled() {
      return false;
   }
}

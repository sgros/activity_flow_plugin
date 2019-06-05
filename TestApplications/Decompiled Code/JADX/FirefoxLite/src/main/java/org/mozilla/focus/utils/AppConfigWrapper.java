package org.mozilla.focus.utils;

import android.content.Context;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.rocket.content.data.ShoppingLink;

public class AppConfigWrapper {
    public static int getDriveDefaultBrowserFromMenuSettingThreshold() {
        return 2;
    }

    public static int getSurveyNotificationLaunchTimeThreshold() {
        return 3;
    }

    public static boolean isSurveyNotificationEnabled() {
        return false;
    }

    public static long getRateAppNotificationLaunchTimeThreshold(Context context) {
        return FirebaseWrapper.getRcLong(context, "rate_app_notification_threshold");
    }

    public static long getShareDialogLaunchTimeThreshold(Context context, boolean z) {
        if (z) {
            return (FirebaseWrapper.getRcLong(context, "share_app_dialog_threshold") + getRateAppNotificationLaunchTimeThreshold(context)) - getRateDialogLaunchTimeThreshold(context);
        }
        return FirebaseWrapper.getRcLong(context, "share_app_dialog_threshold");
    }

    public static long getRateDialogLaunchTimeThreshold(Context context) {
        return FirebaseWrapper.getRcLong(context, "rate_app_dialog_threshold");
    }

    public static boolean isPrivateModeEnabled(Context context) {
        return FirebaseWrapper.getRcBoolean(context, "enable_private_mode");
    }

    public static boolean getMyshotUnreadEnabled(Context context) {
        return FirebaseWrapper.getRcBoolean(context, "enable_my_shot_unread");
    }

    public static String getRateAppDialogTitle(Context context) {
        return FirebaseWrapper.getRcString(context, "rate_app_dialog_text_title");
    }

    public static String getRateAppDialogContent(Context context) {
        return FirebaseWrapper.getRcString(context, "rate_app_dialog_text_content");
    }

    public static String getRateAppPositiveString(Context context) {
        return FirebaseWrapper.getRcString(context, "rate_app_dialog_text_positive");
    }

    public static String getRateAppNegativeString(Context context) {
        return FirebaseWrapper.getRcString(context, "rate_app_dialog_text_negative");
    }

    public static String getBannerRootConfig(Context context) {
        return FirebaseWrapper.getRcString(context, "banner_manifest");
    }

    public static long getFeatureSurvey(Context context) {
        return FirebaseWrapper.getRcLong(context, "feature_survey");
    }

    public static String getScreenshotCategoryUrl(Context context) {
        return FirebaseWrapper.getRcString(context, "screenshot_category_manifest");
    }

    public static String getVpnRecommenderUrl(Context context) {
        return FirebaseWrapper.getRcString(context, "vpn_recommender_url");
    }

    public static String getVpnRecommenderPackage(Context context) {
        return FirebaseWrapper.getRcString(context, "vpn_recommender_package");
    }

    public static long getFirstLaunchWorkerTimer(Context context) {
        return FirebaseWrapper.getRcLong(context, "first_launch_timer_minutes");
    }

    public static String getFirstLaunchNotificationMessage(Context context) {
        return FirebaseWrapper.getRcString(context, "first_launch_notification_message");
    }

    public static boolean hasNewsPortal(Context context) {
        return FirebaseWrapper.getRcBoolean(context, "enable_life_feed");
    }

    public static boolean hasEcommerceShoppingLink() {
        return getEcommerceShoppingLinks().isEmpty() ^ 1;
    }

    public static ArrayList<ShoppingLink> getEcommerceShoppingLinks() {
        ArrayList arrayList = new ArrayList();
        try {
            JSONArray jSONArray = new JSONArray(FirebaseWrapper.getRcString(null, "str_e_commerce_shoppinglinks"));
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = (JSONObject) jSONArray.get(i);
                arrayList.add(new ShoppingLink(jSONObject.optString("url"), jSONObject.optString("name"), jSONObject.optString("img"), jSONObject.optString("source")));
            }
        } catch (JSONException unused) {
        }
        return arrayList;
    }

    public static String getLifeFeedProviderUrl(Context context, String str) {
        String str2 = "";
        try {
            JSONArray jSONArray = new JSONArray(FirebaseWrapper.getRcString(context, "life_feed_providers"));
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                if (jSONObject.getString("name").equalsIgnoreCase(str)) {
                    return jSONObject.getString("url");
                }
            }
            return str2;
        } catch (JSONException e) {
            e.printStackTrace();
            return str2;
        }
    }

    static String getShareAppDialogTitle(Context context) {
        return FirebaseWrapper.getRcString(context, "str_share_app_dialog_title");
    }

    static String getShareAppDialogContent(Context context) {
        return FirebaseWrapper.prettify(FirebaseWrapper.getRcString(context, "str_share_app_dialog_content"));
    }

    static String getShareAppMessage(Context context) {
        return FirebaseWrapper.getRcString(context, "str_share_app_dialog_msg");
    }
}

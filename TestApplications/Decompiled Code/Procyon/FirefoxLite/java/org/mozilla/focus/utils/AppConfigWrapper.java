// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.mozilla.rocket.content.data.ShoppingLink;
import java.util.ArrayList;
import android.content.Context;

public class AppConfigWrapper
{
    public static String getBannerRootConfig(final Context context) {
        return FirebaseWrapper.getRcString(context, "banner_manifest");
    }
    
    public static int getDriveDefaultBrowserFromMenuSettingThreshold() {
        return 2;
    }
    
    public static ArrayList<ShoppingLink> getEcommerceShoppingLinks() {
        final ArrayList<ShoppingLink> list = new ArrayList<ShoppingLink>();
        final String rcString = FirebaseWrapper.getRcString(null, "str_e_commerce_shoppinglinks");
        try {
            final JSONArray jsonArray = new JSONArray(rcString);
            for (int i = 0; i < jsonArray.length(); ++i) {
                final JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                list.add(new ShoppingLink(jsonObject.optString("url"), jsonObject.optString("name"), jsonObject.optString("img"), jsonObject.optString("source")));
            }
            return list;
        }
        catch (JSONException ex) {
            return list;
        }
    }
    
    public static long getFeatureSurvey(final Context context) {
        return FirebaseWrapper.getRcLong(context, "feature_survey");
    }
    
    public static String getFirstLaunchNotificationMessage(final Context context) {
        return FirebaseWrapper.getRcString(context, "first_launch_notification_message");
    }
    
    public static long getFirstLaunchWorkerTimer(final Context context) {
        return FirebaseWrapper.getRcLong(context, "first_launch_timer_minutes");
    }
    
    public static String getLifeFeedProviderUrl(final Context context, final String anotherString) {
        final String rcString = FirebaseWrapper.getRcString(context, "life_feed_providers");
        final String s = "";
        String string;
        try {
            final JSONArray jsonArray = new JSONArray(rcString);
            int n = 0;
            JSONObject jsonObject;
            while (true) {
                string = s;
                if (n >= jsonArray.length()) {
                    return string;
                }
                jsonObject = jsonArray.getJSONObject(n);
                if (jsonObject.getString("name").equalsIgnoreCase(anotherString)) {
                    break;
                }
                ++n;
            }
            string = jsonObject.getString("url");
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            string = s;
        }
        return string;
    }
    
    public static boolean getMyshotUnreadEnabled(final Context context) {
        return FirebaseWrapper.getRcBoolean(context, "enable_my_shot_unread");
    }
    
    public static String getRateAppDialogContent(final Context context) {
        return FirebaseWrapper.getRcString(context, "rate_app_dialog_text_content");
    }
    
    public static String getRateAppDialogTitle(final Context context) {
        return FirebaseWrapper.getRcString(context, "rate_app_dialog_text_title");
    }
    
    public static String getRateAppNegativeString(final Context context) {
        return FirebaseWrapper.getRcString(context, "rate_app_dialog_text_negative");
    }
    
    public static long getRateAppNotificationLaunchTimeThreshold(final Context context) {
        return FirebaseWrapper.getRcLong(context, "rate_app_notification_threshold");
    }
    
    public static String getRateAppPositiveString(final Context context) {
        return FirebaseWrapper.getRcString(context, "rate_app_dialog_text_positive");
    }
    
    public static long getRateDialogLaunchTimeThreshold(final Context context) {
        return FirebaseWrapper.getRcLong(context, "rate_app_dialog_threshold");
    }
    
    public static String getScreenshotCategoryUrl(final Context context) {
        return FirebaseWrapper.getRcString(context, "screenshot_category_manifest");
    }
    
    static String getShareAppDialogContent(final Context context) {
        return FirebaseWrapper.prettify(FirebaseWrapper.getRcString(context, "str_share_app_dialog_content"));
    }
    
    static String getShareAppDialogTitle(final Context context) {
        return FirebaseWrapper.getRcString(context, "str_share_app_dialog_title");
    }
    
    static String getShareAppMessage(final Context context) {
        return FirebaseWrapper.getRcString(context, "str_share_app_dialog_msg");
    }
    
    public static long getShareDialogLaunchTimeThreshold(final Context context, final boolean b) {
        if (b) {
            return FirebaseWrapper.getRcLong(context, "share_app_dialog_threshold") + getRateAppNotificationLaunchTimeThreshold(context) - getRateDialogLaunchTimeThreshold(context);
        }
        return FirebaseWrapper.getRcLong(context, "share_app_dialog_threshold");
    }
    
    public static int getSurveyNotificationLaunchTimeThreshold() {
        return 3;
    }
    
    public static String getVpnRecommenderPackage(final Context context) {
        return FirebaseWrapper.getRcString(context, "vpn_recommender_package");
    }
    
    public static String getVpnRecommenderUrl(final Context context) {
        return FirebaseWrapper.getRcString(context, "vpn_recommender_url");
    }
    
    public static boolean hasEcommerceShoppingLink() {
        return getEcommerceShoppingLinks().isEmpty() ^ true;
    }
    
    public static boolean hasNewsPortal(final Context context) {
        return FirebaseWrapper.getRcBoolean(context, "enable_life_feed");
    }
    
    public static boolean isPrivateModeEnabled(final Context context) {
        return FirebaseWrapper.getRcBoolean(context, "enable_private_mode");
    }
    
    public static boolean isSurveyNotificationEnabled() {
        return false;
    }
}

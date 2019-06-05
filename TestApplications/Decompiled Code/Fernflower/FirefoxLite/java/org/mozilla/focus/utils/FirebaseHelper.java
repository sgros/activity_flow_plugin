package org.mozilla.focus.utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.notification.RocketMessagingService;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.content.NewsSourceManager;
import org.mozilla.threadutils.ThreadUtils;

public final class FirebaseHelper extends FirebaseWrapper {
   private static boolean changing;
   private static FirebaseHelper.BlockingEnablerCallback enablerCallback;
   private static Boolean pending;
   private HashMap remoteConfigDefault;

   private FirebaseHelper() {
   }

   public static boolean bind(Context var0, boolean var1) {
      checkIfApiReady(var0);
      return enableFirebase(var0.getApplicationContext(), var1);
   }

   private static void checkIfApiReady(Context var0) {
      if (AppConstants.isBuiltWithFirebase()) {
         String var1 = getStringResourceByName(var0, "default_web_client_id");
         String var2 = getStringResourceByName(var0, "firebase_database_url");
         String var3 = getStringResourceByName(var0, "google_crash_reporting_api_key");
         String var4 = getStringResourceByName(var0, "google_app_id");
         String var5 = getStringResourceByName(var0, "google_api_key");
         String var6 = getStringResourceByName(var0, "project_id");
         if (var1.isEmpty() || var2.isEmpty() || var3.isEmpty() || var4.isEmpty() || var5.isEmpty() || var6.isEmpty()) {
            throw new IllegalStateException("Firebase related keys are not set");
         }
      }

   }

   private static boolean enableFirebase(Context var0, boolean var1) {
      if (changing) {
         pending = var1;
         return false;
      } else {
         changing = true;
         pending = null;
         (new FirebaseHelper.BlockingEnabler(var0, var1)).execute(new Void[0]);
         return true;
      }
   }

   private HashMap fromFile(Context var1) {
      if (FileUtils.canReadExternalStorage(var1)) {
         try {
            HashMap var2 = FileUtils.fromJsonOnDisk("remote_config.json");
            return var2;
         } catch (Exception var3) {
            Log.w("FirebaseHelper", "Some problem when reading RemoteConfig file from local disk: ", var3);
            return this.fromResourceString(var1);
         }
      } else {
         return this.fromResourceString(var1);
      }
   }

   private HashMap fromResourceString(Context var1) {
      HashMap var2 = new HashMap();
      if (var1 != null) {
         var2.put("rate_app_dialog_text_title", var1.getString(2131755367, new Object[]{var1.getString(2131755062)}));
         var2.put("rate_app_dialog_text_content", var1.getString(2131755366));
         var2.put("rate_app_dialog_text_positive", var1.getString(2131755365));
         var2.put("rate_app_dialog_text_negative", var1.getString(2131755364));
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.getString(2131755343));
         var3.append("?\ud83d\ude0a");
         var2.put("first_launch_notification_message", var3.toString());
         var2.put("str_share_app_dialog_title", var1.getString(2131755409, new Object[]{var1.getString(2131755062)}));
         var2.put("str_share_app_dialog_content", var1.getString(2131755408));
         var2.put("str_share_app_dialog_msg", var1.getString(2131755411, new Object[]{var1.getString(2131755062), var1.getString(2131755410), var1.getString(2131755267)}));
      }

      var2.put("rate_app_dialog_threshold", 6);
      var2.put("rate_app_notification_threshold", 12);
      var2.put("share_app_dialog_threshold", 10);
      var2.put("enable_my_shot_unread", false);
      var2.put("banner_manifest", "");
      var2.put("enable_private_mode", true);
      var2.put("feature_survey", RemoteConfigConstants.INSTANCE.getFEATURE_SURVEY_DEFAULT());
      var2.put("screenshot_category_manifest", "");
      var2.put("vpn_recommender_package", "com.expressvpn.vpn");
      var2.put("vpn_recommender_url", "https://www.expressvpn.com/download-app?a_fid=MozillaFirefoxLite");
      var2.put("first_launch_timer_minutes", 0);
      var2.put("enable_life_feed", false);
      var2.put("life_feed_providers", "");
      var2.put("str_e_commerce_shoppinglinks", "");
      return var2;
   }

   private static String getStringResourceByName(Context var0, String var1) {
      String var2 = var0.getPackageName();
      int var3 = var0.getResources().getIdentifier(var1, "string", var2);
      return var3 == 0 ? "" : var0.getString(var3);
   }

   public static void init(Context var0, boolean var1) {
      if (getInstance() == null) {
         initInternal(new FirebaseHelper());
      }

      bind(var0, var1);
   }

   public static void onLocaleUpdate(Context var0) {
      getInstance().refreshRemoteConfigDefault(var0, (FirebaseWrapper.RemoteConfigFetchCallback)null);
   }

   HashMap getRemoteConfigDefault(Context var1) {
      if (this.remoteConfigDefault == null) {
         boolean var2;
         if (!AppConstants.isDevBuild() && !AppConstants.isBetaBuild()) {
            var2 = false;
         } else {
            var2 = true;
         }

         if (var2 && Looper.myLooper() != Looper.getMainLooper() && var1 != null) {
            this.remoteConfigDefault = this.fromFile(var1);
         } else {
            this.remoteConfigDefault = this.fromResourceString(var1);
         }
      }

      return this.remoteConfigDefault;
   }

   void refreshRemoteConfigDefault(Context var1, FirebaseWrapper.RemoteConfigFetchCallback var2) {
      this.remoteConfigDefault = null;
      this.getRemoteConfigDefault(var1);
      enableRemoteConfig(var1, TelemetryWrapper.isTelemetryEnabled(var1), var2);
   }

   public static class BlockingEnabler extends AsyncTask {
      boolean enable;
      WeakReference weakApplicationContext;

      BlockingEnabler(Context var1, boolean var2) {
         this.enable = var2;
         this.weakApplicationContext = new WeakReference(var1.getApplicationContext());
      }

      // $FF: synthetic method
      static void lambda$doInBackground$2(Context var0) {
         ThreadUtils.postToBackgroundThread((Runnable)(new _$$Lambda$FirebaseHelper$BlockingEnabler$u2Ty_9vZl0oGSlLCmda6fV9tZXg(var0)));
      }

      // $FF: synthetic method
      static void lambda$null$0(String var0) {
         NewsSourceManager.getInstance().setNewsSourceUrl(var0);
      }

      // $FF: synthetic method
      static void lambda$null$1(Context var0) {
         String var1 = FirebaseWrapper.getRcString(var0, var0.getString(2131755334));
         Settings var2 = Settings.getInstance(var0);
         boolean var3 = var2.canOverride("pref_int_news_priority", 1);
         Log.d("NewsSource", "Remote Config fetched");
         if (!TextUtils.isEmpty(var1) && (var3 || TextUtils.isEmpty(var2.getNewsSource()))) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Remote Config is used:");
            var4.append(var1);
            Log.d("NewsSource", var4.toString());
            var2.setPriority("pref_int_news_priority", 1);
            var2.setNewsSource(var1);
            NewsSourceManager.getInstance().setNewsSource(var1);
         }

         ThreadUtils.postToMainThread(new _$$Lambda$FirebaseHelper$BlockingEnabler$v63GBN4VpioJ7nkexjM6ehiNugY(AppConfigWrapper.getLifeFeedProviderUrl(var0, var2.getNewsSource())));
      }

      protected Void doInBackground(Void... var1) {
         ThreadPolicy var2 = StrictMode.getThreadPolicy();
         VmPolicy var3 = StrictMode.getVmPolicy();
         StrictMode.setThreadPolicy((new Builder()).build());
         StrictMode.setVmPolicy((new android.os.StrictMode.VmPolicy.Builder()).build());
         if (this.weakApplicationContext != null && this.weakApplicationContext.get() != null) {
            Context var5 = (Context)this.weakApplicationContext.get();
            FirebaseHelper.BlockingEnablerCallback var4 = FirebaseHelper.enablerCallback;
            if (var4 != null) {
               var4.runDelayOnExecution();
            }

            FirebaseWrapper.setDeveloperModeEnabled(AppConstants.isFirebaseBuild());
            FirebaseHelper.changing = true;
            FirebaseWrapper.updateInstanceId(var5, this.enable);
            FirebaseWrapper.enableCrashlytics(var5, this.enable);
            FirebaseWrapper.enableAnalytics(var5, this.enable);
            FirebaseWrapper.enableCloudMessaging(var5, RocketMessagingService.class.getName(), this.enable);
            FirebaseWrapper.enableRemoteConfig(var5, this.enable, new _$$Lambda$FirebaseHelper$BlockingEnabler$KbJvRYq_uiQihzjwaWiFwpeUbX4(var5));
            FirebaseHelper.changing = false;
            if (FirebaseHelper.pending != null && FirebaseHelper.pending != this.enable) {
               FirebaseHelper.enableFirebase(var5, FirebaseHelper.pending);
            } else {
               FirebaseHelper.pending = null;
            }

            StrictMode.setThreadPolicy(var2);
            StrictMode.setVmPolicy(var3);
            return null;
         } else {
            StrictMode.setThreadPolicy(var2);
            StrictMode.setVmPolicy(var3);
            return null;
         }
      }

      protected void onPostExecute(Void var1) {
         if (FirebaseHelper.pending == null) {
            LocalBroadcastManager.getInstance((Context)this.weakApplicationContext.get()).sendBroadcast(new Intent("Firebase_ready"));
         }

      }
   }

   public interface BlockingEnablerCallback {
      void runDelayOnExecution();
   }
}

package org.mozilla.focus.utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.os.StrictMode.VmPolicy;
import android.support.p001v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.notification.RocketMessagingService;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.FirebaseWrapper.RemoteConfigFetchCallback;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.content.NewsSourceManager;
import org.mozilla.threadutils.ThreadUtils;

public final class FirebaseHelper extends FirebaseWrapper {
    private static boolean changing = false;
    private static BlockingEnablerCallback enablerCallback;
    private static Boolean pending;
    private HashMap<String, Object> remoteConfigDefault;

    public static class BlockingEnabler extends AsyncTask<Void, Void, Void> {
        boolean enable;
        WeakReference<Context> weakApplicationContext;

        BlockingEnabler(Context context, boolean z) {
            this.enable = z;
            this.weakApplicationContext = new WeakReference(context.getApplicationContext());
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Void... voidArr) {
            ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
            VmPolicy vmPolicy = StrictMode.getVmPolicy();
            StrictMode.setThreadPolicy(new Builder().build());
            StrictMode.setVmPolicy(new VmPolicy.Builder().build());
            if (this.weakApplicationContext == null || this.weakApplicationContext.get() == null) {
                StrictMode.setThreadPolicy(threadPolicy);
                StrictMode.setVmPolicy(vmPolicy);
                return null;
            }
            Context context = (Context) this.weakApplicationContext.get();
            BlockingEnablerCallback access$000 = FirebaseHelper.enablerCallback;
            if (access$000 != null) {
                access$000.runDelayOnExecution();
            }
            FirebaseWrapper.setDeveloperModeEnabled(AppConstants.isFirebaseBuild());
            FirebaseHelper.changing = true;
            FirebaseWrapper.updateInstanceId(context, this.enable);
            FirebaseWrapper.enableCrashlytics(context, this.enable);
            FirebaseWrapper.enableAnalytics(context, this.enable);
            FirebaseWrapper.enableCloudMessaging(context, RocketMessagingService.class.getName(), this.enable);
            FirebaseWrapper.enableRemoteConfig(context, this.enable, new C0747x6533176(context));
            FirebaseHelper.changing = false;
            if (FirebaseHelper.pending == null || FirebaseHelper.pending.booleanValue() == this.enable) {
                FirebaseHelper.pending = null;
            } else {
                FirebaseHelper.enableFirebase(context, FirebaseHelper.pending.booleanValue());
            }
            StrictMode.setThreadPolicy(threadPolicy);
            StrictMode.setVmPolicy(vmPolicy);
            return null;
        }

        static /* synthetic */ void lambda$null$1(Context context) {
            String rcString = FirebaseWrapper.getRcString(context, context.getString(C0769R.string.pref_s_news));
            Settings instance = Settings.getInstance(context);
            boolean canOverride = instance.canOverride("pref_int_news_priority", 1);
            Log.d("NewsSource", "Remote Config fetched");
            if (!TextUtils.isEmpty(rcString) && (canOverride || TextUtils.isEmpty(instance.getNewsSource()))) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Remote Config is used:");
                stringBuilder.append(rcString);
                Log.d("NewsSource", stringBuilder.toString());
                instance.setPriority("pref_int_news_priority", 1);
                instance.setNewsSource(rcString);
                NewsSourceManager.getInstance().setNewsSource(rcString);
            }
            ThreadUtils.postToMainThread(new C0537x6154d594(AppConfigWrapper.getLifeFeedProviderUrl(context, instance.getNewsSource())));
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Void voidR) {
            if (FirebaseHelper.pending == null) {
                LocalBroadcastManager.getInstance((Context) this.weakApplicationContext.get()).sendBroadcast(new Intent("Firebase_ready"));
            }
        }
    }

    public interface BlockingEnablerCallback {
        void runDelayOnExecution();
    }

    private FirebaseHelper() {
    }

    public static void init(Context context, boolean z) {
        if (FirebaseWrapper.getInstance() == null) {
            FirebaseWrapper.initInternal(new FirebaseHelper());
        }
        bind(context, z);
    }

    public static boolean bind(Context context, boolean z) {
        checkIfApiReady(context);
        return enableFirebase(context.getApplicationContext(), z);
    }

    private static void checkIfApiReady(Context context) {
        if (AppConstants.isBuiltWithFirebase()) {
            String stringResourceByName = getStringResourceByName(context, "default_web_client_id");
            String stringResourceByName2 = getStringResourceByName(context, "firebase_database_url");
            String stringResourceByName3 = getStringResourceByName(context, "google_crash_reporting_api_key");
            String stringResourceByName4 = getStringResourceByName(context, "google_app_id");
            String stringResourceByName5 = getStringResourceByName(context, "google_api_key");
            String stringResourceByName6 = getStringResourceByName(context, "project_id");
            if (stringResourceByName.isEmpty() || stringResourceByName2.isEmpty() || stringResourceByName3.isEmpty() || stringResourceByName4.isEmpty() || stringResourceByName5.isEmpty() || stringResourceByName6.isEmpty()) {
                throw new IllegalStateException("Firebase related keys are not set");
            }
        }
    }

    private static boolean enableFirebase(Context context, boolean z) {
        if (changing) {
            pending = Boolean.valueOf(z);
            return false;
        }
        changing = true;
        pending = null;
        new BlockingEnabler(context, z).execute(new Void[0]);
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public HashMap<String, Object> getRemoteConfigDefault(Context context) {
        if (this.remoteConfigDefault == null) {
            Object obj = (AppConstants.isDevBuild() || AppConstants.isBetaBuild()) ? 1 : null;
            if (obj == null || Looper.myLooper() == Looper.getMainLooper() || context == null) {
                this.remoteConfigDefault = fromResourceString(context);
            } else {
                this.remoteConfigDefault = fromFile(context);
            }
        }
        return this.remoteConfigDefault;
    }

    /* Access modifiers changed, original: 0000 */
    public void refreshRemoteConfigDefault(Context context, RemoteConfigFetchCallback remoteConfigFetchCallback) {
        this.remoteConfigDefault = null;
        getRemoteConfigDefault(context);
        FirebaseWrapper.enableRemoteConfig(context, TelemetryWrapper.isTelemetryEnabled(context), remoteConfigFetchCallback);
    }

    private HashMap<String, Object> fromFile(Context context) {
        if (!FileUtils.canReadExternalStorage(context)) {
            return fromResourceString(context);
        }
        try {
            return FileUtils.fromJsonOnDisk("remote_config.json");
        } catch (Exception e) {
            Log.w("FirebaseHelper", "Some problem when reading RemoteConfig file from local disk: ", e);
            return fromResourceString(context);
        }
    }

    private HashMap<String, Object> fromResourceString(Context context) {
        HashMap hashMap = new HashMap();
        if (context != null) {
            hashMap.put("rate_app_dialog_text_title", context.getString(C0769R.string.rate_app_dialog_text_title, new Object[]{context.getString(C0769R.string.app_name)}));
            hashMap.put("rate_app_dialog_text_content", context.getString(C0769R.string.rate_app_dialog_text_content));
            hashMap.put("rate_app_dialog_text_positive", context.getString(C0769R.string.rate_app_dialog_btn_go_rate));
            hashMap.put("rate_app_dialog_text_negative", context.getString(C0769R.string.rate_app_dialog_btn_feedback));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(context.getString(C0769R.string.preference_default_browser));
            stringBuilder.append("?😊");
            hashMap.put("first_launch_notification_message", stringBuilder.toString());
            hashMap.put("str_share_app_dialog_title", context.getString(C0769R.string.share_app_dialog_text_title, new Object[]{context.getString(C0769R.string.app_name)}));
            hashMap.put("str_share_app_dialog_content", context.getString(C0769R.string.share_app_dialog_text_content));
            String str = "str_share_app_dialog_msg";
            hashMap.put(str, context.getString(C0769R.string.share_app_promotion_text, new Object[]{context.getString(C0769R.string.app_name), context.getString(C0769R.string.share_app_google_play_url), context.getString(C0769R.string.mozilla)}));
        }
        hashMap.put("rate_app_dialog_threshold", Integer.valueOf(6));
        hashMap.put("rate_app_notification_threshold", Integer.valueOf(12));
        hashMap.put("share_app_dialog_threshold", Integer.valueOf(10));
        hashMap.put("enable_my_shot_unread", Boolean.valueOf(false));
        hashMap.put("banner_manifest", "");
        hashMap.put("enable_private_mode", Boolean.valueOf(true));
        hashMap.put("feature_survey", Integer.valueOf(RemoteConfigConstants.INSTANCE.getFEATURE_SURVEY_DEFAULT()));
        hashMap.put("screenshot_category_manifest", "");
        hashMap.put("vpn_recommender_package", "com.expressvpn.vpn");
        hashMap.put("vpn_recommender_url", "https://www.expressvpn.com/download-app?a_fid=MozillaFirefoxLite");
        hashMap.put("first_launch_timer_minutes", Integer.valueOf(0));
        hashMap.put("enable_life_feed", Boolean.valueOf(false));
        hashMap.put("life_feed_providers", "");
        hashMap.put("str_e_commerce_shoppinglinks", "");
        return hashMap;
    }

    private static String getStringResourceByName(Context context, String str) {
        int identifier = context.getResources().getIdentifier(str, "string", context.getPackageName());
        if (identifier == 0) {
            return "";
        }
        return context.getString(identifier);
    }

    public static void onLocaleUpdate(Context context) {
        FirebaseWrapper.getInstance().refreshRemoteConfigDefault(context, null);
    }
}

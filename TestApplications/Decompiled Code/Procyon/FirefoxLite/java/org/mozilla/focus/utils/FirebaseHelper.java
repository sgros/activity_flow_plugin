// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.os.StrictMode$VmPolicy;
import android.os.StrictMode$ThreadPolicy;
import org.mozilla.focus.notification.RocketMessagingService;
import android.os.StrictMode$VmPolicy$Builder;
import android.os.StrictMode$ThreadPolicy$Builder;
import android.os.StrictMode;
import android.text.TextUtils;
import org.mozilla.rocket.content.NewsSourceManager;
import org.mozilla.threadutils.ThreadUtils;
import java.lang.ref.WeakReference;
import android.os.AsyncTask;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.os.Looper;
import android.util.Log;
import org.mozilla.fileutils.FileUtils;
import android.content.Context;
import java.util.HashMap;

public final class FirebaseHelper extends FirebaseWrapper
{
    private static boolean changing = false;
    private static BlockingEnablerCallback enablerCallback;
    private static Boolean pending;
    private HashMap<String, Object> remoteConfigDefault;
    
    private FirebaseHelper() {
    }
    
    public static boolean bind(final Context context, final boolean b) {
        checkIfApiReady(context);
        return enableFirebase(context.getApplicationContext(), b);
    }
    
    private static void checkIfApiReady(final Context context) {
        if (AppConstants.isBuiltWithFirebase()) {
            final String stringResourceByName = getStringResourceByName(context, "default_web_client_id");
            final String stringResourceByName2 = getStringResourceByName(context, "firebase_database_url");
            final String stringResourceByName3 = getStringResourceByName(context, "google_crash_reporting_api_key");
            final String stringResourceByName4 = getStringResourceByName(context, "google_app_id");
            final String stringResourceByName5 = getStringResourceByName(context, "google_api_key");
            final String stringResourceByName6 = getStringResourceByName(context, "project_id");
            if (stringResourceByName.isEmpty() || stringResourceByName2.isEmpty() || stringResourceByName3.isEmpty() || stringResourceByName4.isEmpty() || stringResourceByName5.isEmpty() || stringResourceByName6.isEmpty()) {
                throw new IllegalStateException("Firebase related keys are not set");
            }
        }
    }
    
    private static boolean enableFirebase(final Context context, final boolean b) {
        if (FirebaseHelper.changing) {
            FirebaseHelper.pending = b;
            return false;
        }
        FirebaseHelper.changing = true;
        FirebaseHelper.pending = null;
        new BlockingEnabler(context, b).execute((Object[])new Void[0]);
        return true;
    }
    
    private HashMap<String, Object> fromFile(final Context context) {
        if (FileUtils.canReadExternalStorage(context)) {
            try {
                return FileUtils.fromJsonOnDisk("remote_config.json");
            }
            catch (Exception ex) {
                Log.w("FirebaseHelper", "Some problem when reading RemoteConfig file from local disk: ", (Throwable)ex);
                return this.fromResourceString(context);
            }
        }
        return this.fromResourceString(context);
    }
    
    private HashMap<String, Object> fromResourceString(final Context context) {
        final HashMap<String, String> hashMap = (HashMap<String, String>)new HashMap<String, Boolean>();
        if (context != null) {
            hashMap.put("rate_app_dialog_text_title", (Boolean)context.getString(2131755367, new Object[] { context.getString(2131755062) }));
            hashMap.put("rate_app_dialog_text_content", (Boolean)context.getString(2131755366));
            hashMap.put("rate_app_dialog_text_positive", (Boolean)context.getString(2131755365));
            hashMap.put("rate_app_dialog_text_negative", (Boolean)context.getString(2131755364));
            final StringBuilder sb = new StringBuilder();
            sb.append(context.getString(2131755343));
            sb.append("?\ud83d\ude0a");
            hashMap.put("first_launch_notification_message", (Boolean)sb.toString());
            hashMap.put("str_share_app_dialog_title", (Boolean)context.getString(2131755409, new Object[] { context.getString(2131755062) }));
            hashMap.put("str_share_app_dialog_content", (Boolean)context.getString(2131755408));
            hashMap.put("str_share_app_dialog_msg", (Boolean)context.getString(2131755411, new Object[] { context.getString(2131755062), context.getString(2131755410), context.getString(2131755267) }));
        }
        hashMap.put("rate_app_dialog_threshold", (Boolean)(Object)Integer.valueOf(6));
        hashMap.put("rate_app_notification_threshold", (Boolean)(Object)Integer.valueOf(12));
        hashMap.put("share_app_dialog_threshold", (Boolean)(Object)Integer.valueOf(10));
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
        return (HashMap<String, Object>)hashMap;
    }
    
    private static String getStringResourceByName(final Context context, final String s) {
        final int identifier = context.getResources().getIdentifier(s, "string", context.getPackageName());
        if (identifier == 0) {
            return "";
        }
        return context.getString(identifier);
    }
    
    public static void init(final Context context, final boolean b) {
        if (getInstance() == null) {
            FirebaseWrapper.initInternal(new FirebaseHelper());
        }
        bind(context, b);
    }
    
    public static void onLocaleUpdate(final Context context) {
        FirebaseWrapper.getInstance().refreshRemoteConfigDefault(context, null);
    }
    
    @Override
    HashMap<String, Object> getRemoteConfigDefault(final Context context) {
        if (this.remoteConfigDefault == null) {
            if ((AppConstants.isDevBuild() || AppConstants.isBetaBuild()) && Looper.myLooper() != Looper.getMainLooper() && context != null) {
                this.remoteConfigDefault = this.fromFile(context);
            }
            else {
                this.remoteConfigDefault = this.fromResourceString(context);
            }
        }
        return this.remoteConfigDefault;
    }
    
    @Override
    void refreshRemoteConfigDefault(final Context context, final RemoteConfigFetchCallback remoteConfigFetchCallback) {
        this.remoteConfigDefault = null;
        this.getRemoteConfigDefault(context);
        FirebaseWrapper.enableRemoteConfig(context, TelemetryWrapper.isTelemetryEnabled(context), remoteConfigFetchCallback);
    }
    
    public static class BlockingEnabler extends AsyncTask<Void, Void, Void>
    {
        boolean enable;
        WeakReference<Context> weakApplicationContext;
        
        BlockingEnabler(final Context context, final boolean enable) {
            this.enable = enable;
            this.weakApplicationContext = new WeakReference<Context>(context.getApplicationContext());
        }
        
        protected Void doInBackground(final Void... array) {
            final StrictMode$ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
            final StrictMode$VmPolicy vmPolicy = StrictMode.getVmPolicy();
            StrictMode.setThreadPolicy(new StrictMode$ThreadPolicy$Builder().build());
            StrictMode.setVmPolicy(new StrictMode$VmPolicy$Builder().build());
            if (this.weakApplicationContext != null && this.weakApplicationContext.get() != null) {
                final Context context = this.weakApplicationContext.get();
                final BlockingEnablerCallback access$000 = FirebaseHelper.enablerCallback;
                if (access$000 != null) {
                    access$000.runDelayOnExecution();
                }
                FirebaseWrapper.setDeveloperModeEnabled(AppConstants.isFirebaseBuild());
                FirebaseHelper.changing = true;
                FirebaseWrapper.updateInstanceId(context, this.enable);
                FirebaseWrapper.enableCrashlytics(context, this.enable);
                FirebaseWrapper.enableAnalytics(context, this.enable);
                FirebaseWrapper.enableCloudMessaging(context, RocketMessagingService.class.getName(), this.enable);
                FirebaseWrapper.enableRemoteConfig(context, this.enable, (RemoteConfigFetchCallback)new _$$Lambda$FirebaseHelper$BlockingEnabler$KbJvRYq_uiQihzjwaWiFwpeUbX4(context));
                FirebaseHelper.changing = false;
                if (FirebaseHelper.pending != null && FirebaseHelper.pending != this.enable) {
                    enableFirebase(context, FirebaseHelper.pending);
                }
                else {
                    FirebaseHelper.pending = null;
                }
                StrictMode.setThreadPolicy(threadPolicy);
                StrictMode.setVmPolicy(vmPolicy);
                return null;
            }
            StrictMode.setThreadPolicy(threadPolicy);
            StrictMode.setVmPolicy(vmPolicy);
            return null;
        }
        
        protected void onPostExecute(final Void void1) {
            if (FirebaseHelper.pending == null) {
                LocalBroadcastManager.getInstance(this.weakApplicationContext.get()).sendBroadcast(new Intent("Firebase_ready"));
            }
        }
    }
    
    public interface BlockingEnablerCallback
    {
        void runDelayOnExecution();
    }
}

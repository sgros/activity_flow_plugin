package menion.android.whereyougo;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.SaveGame;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.ExceptionHandler;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.utils.StringToken;
import menion.android.whereyougo.utils.Utils;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.WherigoLib;

public class MainApplication extends Application {
    public static final String APP_NAME = "WhereYouGo";
    private static final String TAG = "MainApplication";
    private static Context applicationContext;
    private static Timer mTimer;
    private static OnAppVisibilityChange onAppVisibilityChange;
    private Locale locale = null;
    private boolean mScreenOff = false;
    private ScreenReceiver mScreenReceiver;

    /* renamed from: menion.android.whereyougo.MainApplication$1 */
    static class C02501 extends TimerTask {
        C02501() {
        }

        public void run() {
            if (!PreferenceValues.existCurrentActivity()) {
                MainApplication.onAppMinimized();
            }
            LocationState.onActivityPauseInstant(PreferenceValues.getCurrentActivity());
            MainApplication.mTimer = null;
        }
    }

    public interface OnAppVisibilityChange {
        void onAppMinimized();

        void onAppRestored();
    }

    private class ScreenReceiver extends BroadcastReceiver {
        private ScreenReceiver() {
        }

        /* synthetic */ ScreenReceiver(MainApplication x0, C02501 x1) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                MainApplication.this.mScreenOff = true;
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                LocationState.onScreenOn(false);
                MainApplication.this.mScreenOff = false;
            }
        }
    }

    public static Context getContext() {
        return applicationContext;
    }

    public static void appRestored() {
        onAppRestored();
        if (onAppVisibilityChange != null) {
            onAppVisibilityChange.onAppRestored();
        }
    }

    public static void onActivityPause() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new C02501(), 2000);
    }

    private static void onAppMinimized() {
        Logger.m26w(TAG, "onAppMinimized()");
        if (onAppVisibilityChange != null) {
            onAppVisibilityChange.onAppMinimized();
        }
    }

    private static void onAppRestored() {
        Logger.m26w(TAG, "onAppRestored()");
    }

    public static void registerVisibilityHandler(OnAppVisibilityChange handler) {
        onAppVisibilityChange = handler;
    }

    public void destroy() {
        try {
            unregisterReceiver(this.mScreenReceiver);
        } catch (Exception e) {
            Logger.m26w(TAG, "destroy(), e:" + e);
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        onAppVisibilityChange = null;
    }

    public boolean setRoot(String pathCustom) {
        String pathExternal = null;
        try {
            pathExternal = getExternalFilesDir(null).getAbsolutePath();
        } catch (Exception e) {
        }
        String pathInternal = null;
        try {
            pathInternal = getFilesDir().getAbsolutePath();
        } catch (Exception e2) {
        }
        boolean set = true;
        if ((pathCustom == null || !FileSystem.setRootDirectory(pathCustom)) && ((pathExternal == null || !FileSystem.setRootDirectory(pathExternal)) && (pathInternal == null || !FileSystem.setRootDirectory(pathInternal)))) {
            set = false;
        }
        Preferences.GLOBAL_ROOT = FileSystem.ROOT;
        Preferences.setStringPreference(C0254R.string.pref_KEY_S_ROOT, Preferences.GLOBAL_ROOT);
        if (!set) {
            ManagerNotify.toastShortMessage(this, getString(C0254R.string.filesystem_cannot_create_root));
        }
        return set;
    }

    private void initCore() {
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.mScreenReceiver = new ScreenReceiver(this, null);
        registerReceiver(this.mScreenReceiver, filter);
        setRoot(Preferences.GLOBAL_ROOT);
        try {
            FileSystem.CACHE = getExternalCacheDir().getAbsolutePath();
        } catch (Exception e) {
            try {
                FileSystem.CACHE = getCacheDir().getAbsolutePath();
            } catch (Exception e2) {
                FileSystem.CACHE = FileSystem.ROOT + "cache/";
            }
        }
        if (!FileSystem.CACHE.endsWith("/")) {
            FileSystem.CACHE += "/";
        }
        FileSystem.CACHE_AUDIO = FileSystem.CACHE + "audio/";
        LocationState.init(this);
        Utils.getDpPixels(this, 1.0f);
        try {
            String name = String.format("%s, app:%s", new Object[]{C0322A.getAppName(), C0322A.getAppVersion()});
            String platform = String.format("Android %s", new Object[]{VERSION.RELEASE});
            WherigoLib.env.put(WherigoLib.DEVICE_ID, name);
            WherigoLib.env.put(WherigoLib.PLATFORM, platform);
        } catch (Exception e3) {
        }
    }

    public boolean isScreenOff() {
        return this.mScreenOff;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.locale != null) {
            newConfig.locale = this.locale;
            Locale.setDefault(this.locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    private void legacySupport4PreferencesFloat(int prefId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String key = getString(prefId);
        try {
            sharedPref.getString(key, "");
        } catch (Exception e) {
            try {
                Log.d(TAG, "legecySupport4PreferencesFloat() - LEGECY SUPPORT: convert float to string");
                Float value = Float.valueOf(sharedPref.getFloat(key, 0.0f));
                sharedPref.edit().remove(key).commit();
                sharedPref.edit().putString(key, String.valueOf(value)).commit();
            } catch (Exception ee) {
                Log.e(TAG, "legecySupport4PreferencesFloat() - panic remove", ee);
                sharedPref.edit().remove(key).commit();
            }
        }
    }

    private void legacySupport4PreferencesInt(int prefId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String key = getString(prefId);
        try {
            sharedPref.getString(key, "");
        } catch (Exception e) {
            try {
                Log.d(TAG, "legecySupport4PreferencesInt() - LEGECY SUPPORT: convert int to string");
                int value = sharedPref.getInt(key, 0);
                sharedPref.edit().remove(key).commit();
                sharedPref.edit().putString(key, String.valueOf(value)).commit();
            } catch (Exception ee) {
                Log.e(TAG, "legecySupportFloat2Int() - panic remove", ee);
                sharedPref.edit().remove(key).commit();
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        Log.d(TAG, "onCreate()");
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        try {
            legacySupport4PreferencesFloat(C0254R.string.pref_KEY_F_LAST_KNOWN_LOCATION_LATITUDE);
            legacySupport4PreferencesFloat(C0254R.string.pref_KEY_F_LAST_KNOWN_LOCATION_LONGITUDE);
            legacySupport4PreferencesFloat(C0254R.string.pref_KEY_F_LAST_KNOWN_LOCATION_ALTITUDE);
            legacySupport4PreferencesInt(C0254R.string.pref_KEY_S_APPLICATION_VERSION_LAST);
        } catch (Exception e) {
            Log.e(TAG, "onCreate() - PANIC! Wipe out preferences", e);
            PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
        }
        PreferenceManager.setDefaultValues(this, C0254R.xml.whereyougo_preferences, false);
        Preferences.setContext(this);
        Preferences.init(this);
        Configuration config = getBaseContext().getResources().getConfiguration();
        String lang = Preferences.getStringPreference(C0254R.string.pref_KEY_S_LANGUAGE);
        if (!(lang.equals(getString(C0254R.string.pref_language_default_value)) || config.locale.getLanguage().equals(lang))) {
            ArrayList<String> loc = StringToken.parse(lang, "_");
            if (loc.size() == 1) {
                this.locale = new Locale(lang);
            } else {
                this.locale = new Locale((String) loc.get(0), (String) loc.get(1));
            }
            config.locale = this.locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        initCore();
    }

    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory()");
    }

    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate()");
    }

    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Logger.m24i(TAG, String.format("onTrimMemory(%d)", new Object[]{Integer.valueOf(level)}));
        try {
            if (Preferences.GLOBAL_SAVEGAME_AUTO && level == 20 && MainActivity.selectedFile != null && Engine.instance != null) {
                final Activity activity = PreferenceValues.getCurrentActivity();
                if (activity != null) {
                    if (MainActivity.wui != null) {
                        MainActivity.wui.setOnSavingFinished(new Runnable() {
                            public void run() {
                                ManagerNotify.toastShortMessage(activity, MainApplication.this.getString(C0254R.string.save_game_auto));
                                MainActivity.wui.setOnSavingFinished(null);
                            }
                        });
                    }
                    new SaveGame(activity).execute(new Void[0]);
                }
            }
        } catch (Exception e) {
            Logger.m21e(TAG, String.format("onTrimMemory(%d): savegame failed", new Object[]{Integer.valueOf(level)}));
        }
    }
}

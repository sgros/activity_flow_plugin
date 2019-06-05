package org.mozilla.focus.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import java.util.Set;
import org.mozilla.focus.provider.SettingPreferenceWrapper;
import org.mozilla.focus.search.SearchEngine;
import org.mozilla.rocket.C0769R;

public class Settings {
    private static Settings instance;
    private final EventHistory eventHistory = new EventHistory(this.preferences);
    private final NewFeatureNotice newFeatureNotice;
    private final SharedPreferences preferences;
    private final Resources resources;
    private final SettingPreferenceWrapper settingPreferenceWrapper;

    public static class EventHistory {
        private SharedPreferences preferences;

        private EventHistory(SharedPreferences sharedPreferences) {
            this.preferences = sharedPreferences;
        }

        public int getCount(String str) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("pref_");
            stringBuilder.append(str);
            stringBuilder.append("_counter");
            return this.preferences.getInt(stringBuilder.toString(), 0);
        }

        public boolean contains(String str) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("pref_did_");
            stringBuilder.append(str);
            String stringBuilder2 = stringBuilder.toString();
            boolean z = false;
            if (this.preferences.contains(stringBuilder2)) {
                return this.preferences.getBoolean(stringBuilder2, false);
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("pref_");
            stringBuilder.append(str);
            stringBuilder.append("_counter");
            if (this.preferences.getInt(stringBuilder.toString(), 0) > 0) {
                z = true;
            }
            return z;
        }

        public void add(String str) {
            setCount(str, getCount(str) + 1);
        }

        public void setCount(String str, int i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("pref_");
            stringBuilder.append(str);
            stringBuilder.append("_counter");
            this.preferences.edit().putInt(stringBuilder.toString(), i).apply();
        }
    }

    public static synchronized Settings getInstance(Context context) {
        Settings settings;
        synchronized (Settings.class) {
            if (instance == null) {
                instance = new Settings(context.getApplicationContext());
            }
            settings = instance;
        }
        return settings;
    }

    private Settings(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.resources = context.getResources();
        this.newFeatureNotice = NewFeatureNotice.getInstance(context);
        this.settingPreferenceWrapper = new SettingPreferenceWrapper(context.getContentResolver());
    }

    public boolean shouldBlockImages() {
        return this.preferences.getBoolean(this.resources.getString(C0769R.string.pref_key_performance_block_images), false);
    }

    public void setBlockImages(boolean z) {
        this.preferences.edit().putBoolean(getPreferenceKey(C0769R.string.pref_key_performance_block_images), z).apply();
    }

    public boolean isNightModeEnable() {
        return this.settingPreferenceWrapper.getBoolean(this.resources.getString(C0769R.string.pref_key_night_mode_enable), false);
    }

    public void setNightMode(boolean z) {
        this.preferences.edit().putBoolean(getPreferenceKey(C0769R.string.pref_key_night_mode_enable), z).apply();
    }

    public void setNightModeSpotlight(boolean z) {
        this.preferences.edit().putBoolean(getPreferenceKey(C0769R.string.pref_key_night_mode_brightness_dirty), z).apply();
    }

    public boolean showNightModeSpotlight() {
        return this.settingPreferenceWrapper.getBoolean(this.resources.getString(C0769R.string.pref_key_night_mode_brightness_dirty), false);
    }

    public boolean shouldShowFirstrun() {
        return this.newFeatureNotice.shouldShowLiteUpdate() || !this.newFeatureNotice.hasShownFirstRun();
    }

    public boolean shouldSaveToRemovableStorage() {
        String[] stringArray = this.resources.getStringArray(C0769R.array.data_saving_path_values);
        return stringArray[0].equals(this.preferences.getString(getPreferenceKey(C0769R.string.pref_key_storage_save_downloads_to), stringArray[0]));
    }

    public boolean shouldUseTurboMode() {
        return this.preferences.getBoolean(this.resources.getString(C0769R.string.pref_key_turbo_mode), true);
    }

    public void setTurboMode(boolean z) {
        this.preferences.edit().putBoolean(getPreferenceKey(C0769R.string.pref_key_turbo_mode), z).apply();
    }

    public void setRemovableStorageStateOnCreate(boolean z) {
        this.preferences.edit().putBoolean(getPreferenceKey(C0769R.string.pref_key_removable_storage_available_on_create), z).apply();
    }

    public boolean getRemovableStorageStateOnCreate() {
        return this.preferences.getBoolean(getPreferenceKey(C0769R.string.pref_key_removable_storage_available_on_create), false);
    }

    public int getShowedStorageMessage() {
        return this.preferences.getInt(getPreferenceKey(C0769R.string.pref_key_showed_storage_message), 38183);
    }

    public void setShowedStorageMessage(int i) {
        if (i == 22919 || i == 38183) {
            this.preferences.edit().putInt(getPreferenceKey(C0769R.string.pref_key_showed_storage_message), i).apply();
            return;
        }
        throw new RuntimeException("Unknown message type");
    }

    public String getNewsSource() {
        return this.preferences.getString(getPreferenceKey(C0769R.string.pref_s_news), null);
    }

    public void setNewsSource(String str) {
        this.preferences.edit().putString(getPreferenceKey(C0769R.string.pref_s_news), str).apply();
    }

    public String getDefaultSearchEngineName() {
        return this.preferences.getString(getPreferenceKey(C0769R.string.pref_key_search_engine), null);
    }

    public void setRateAppDialogDidShow() {
        this.preferences.edit().putBoolean(getPreferenceKey(C0769R.string.pref_key_did_show_rate_app_dialog), true).apply();
    }

    public void setRateAppDialogDidDismiss() {
        this.preferences.edit().putBoolean(getPreferenceKey(C0769R.string.pref_key_did_dismiss_rate_app_dialog), true).apply();
    }

    public void setRateAppNotificationDidShow() {
        this.preferences.edit().putBoolean(getPreferenceKey(C0769R.string.pref_key_did_show_rate_app_notification), true).apply();
    }

    public int getMenuPreferenceClickCount() {
        return this.preferences.getInt(getPreferenceKey(C0769R.string.pref_key_setting_click_counter), 0);
    }

    public void addMenuPreferenceClickCount() {
        this.preferences.edit().putInt(getPreferenceKey(C0769R.string.pref_key_setting_click_counter), getMenuPreferenceClickCount() + 1).apply();
    }

    public boolean isDefaultBrowserSettingDidShow() {
        return this.preferences.getBoolean(getPreferenceKey(C0769R.string.pref_key_did_show_default_browser_setting), false);
    }

    public void setDefaultBrowserSettingDidShow() {
        this.preferences.edit().putBoolean(getPreferenceKey(C0769R.string.pref_key_did_show_default_browser_setting), true).apply();
    }

    public boolean hasUnreadMyShot() {
        return this.preferences.getBoolean(getPreferenceKey(C0769R.string.pref_has_unread_my_shot), false);
    }

    public void setHasUnreadMyShot(boolean z) {
        this.preferences.edit().putBoolean(getPreferenceKey(C0769R.string.pref_has_unread_my_shot), z).apply();
    }

    public void setShareAppDialogDidShow() {
        this.preferences.edit().putBoolean(getPreferenceKey(C0769R.string.pref_key_did_show_share_app_dialog), true).apply();
    }

    public void setDefaultSearchEngine(SearchEngine searchEngine) {
        this.preferences.edit().putString(getPreferenceKey(C0769R.string.pref_key_search_engine), searchEngine.getName()).apply();
    }

    public float getNightModeBrightnessValue() {
        return this.settingPreferenceWrapper.getFloat(getPreferenceKey(C0769R.string.pref_key_brightness), -1.0f);
    }

    public void setNightModeBrightnessValue(float f) {
        this.preferences.edit().putFloat(getPreferenceKey(C0769R.string.pref_key_brightness), f).apply();
    }

    public static void updatePrefDefaultBrowserIfNeeded(Context context, boolean z) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set keySet = defaultSharedPreferences.getAll().keySet();
        String string = context.getResources().getString(C0769R.string.pref_key_default_browser);
        if (keySet.contains(string) || z) {
            defaultSharedPreferences.edit().putBoolean(string, z).apply();
        }
    }

    public static void updatePrefString(Context context, String str, String str2) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(str, str2).apply();
    }

    /* Access modifiers changed, original: 0000 */
    public String getPreferenceKey(int i) {
        return this.resources.getString(i);
    }

    public boolean canOverride(String str, int i) {
        return i > this.preferences.getInt(str, Integer.MAX_VALUE);
    }

    public void setPriority(String str, int i) {
        this.preferences.edit().putInt(str, i).apply();
    }

    public EventHistory getEventHistory() {
        return this.eventHistory;
    }
}

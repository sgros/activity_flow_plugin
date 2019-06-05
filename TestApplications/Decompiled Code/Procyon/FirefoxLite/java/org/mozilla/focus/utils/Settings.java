// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import org.mozilla.focus.search.SearchEngine;
import java.util.Set;
import android.preference.PreferenceManager;
import android.content.Context;
import org.mozilla.focus.provider.SettingPreferenceWrapper;
import android.content.res.Resources;
import android.content.SharedPreferences;

public class Settings
{
    private static Settings instance;
    private final EventHistory eventHistory;
    private final NewFeatureNotice newFeatureNotice;
    private final SharedPreferences preferences;
    private final Resources resources;
    private final SettingPreferenceWrapper settingPreferenceWrapper;
    
    private Settings(final Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.resources = context.getResources();
        this.eventHistory = new EventHistory(this.preferences);
        this.newFeatureNotice = NewFeatureNotice.getInstance(context);
        this.settingPreferenceWrapper = new SettingPreferenceWrapper(context.getContentResolver());
    }
    
    public static Settings getInstance(final Context context) {
        synchronized (Settings.class) {
            if (Settings.instance == null) {
                Settings.instance = new Settings(context.getApplicationContext());
            }
            return Settings.instance;
        }
    }
    
    public static void updatePrefDefaultBrowserIfNeeded(final Context context, final boolean b) {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final Set keySet = defaultSharedPreferences.getAll().keySet();
        final String string = context.getResources().getString(2131755303);
        if (keySet.contains(string) || b) {
            defaultSharedPreferences.edit().putBoolean(string, b).apply();
        }
    }
    
    public static void updatePrefString(final Context context, final String s, final String s2) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(s, s2).apply();
    }
    
    public void addMenuPreferenceClickCount() {
        this.preferences.edit().putInt(this.getPreferenceKey(2131755326), this.getMenuPreferenceClickCount() + 1).apply();
    }
    
    public boolean canOverride(final String s, final int n) {
        return n > this.preferences.getInt(s, Integer.MAX_VALUE);
    }
    
    public String getDefaultSearchEngineName() {
        return this.preferences.getString(this.getPreferenceKey(2131755325), (String)null);
    }
    
    public EventHistory getEventHistory() {
        return this.eventHistory;
    }
    
    public int getMenuPreferenceClickCount() {
        return this.preferences.getInt(this.getPreferenceKey(2131755326), 0);
    }
    
    public String getNewsSource() {
        return this.preferences.getString(this.getPreferenceKey(2131755334), (String)null);
    }
    
    public float getNightModeBrightnessValue() {
        return this.settingPreferenceWrapper.getFloat(this.getPreferenceKey(2131755297), -1.0f);
    }
    
    String getPreferenceKey(final int n) {
        return this.resources.getString(n);
    }
    
    public boolean getRemovableStorageStateOnCreate() {
        return this.preferences.getBoolean(this.getPreferenceKey(2131755323), false);
    }
    
    public int getShowedStorageMessage() {
        return this.preferences.getInt(this.getPreferenceKey(2131755328), 38183);
    }
    
    public boolean hasUnreadMyShot() {
        return this.preferences.getBoolean(this.getPreferenceKey(2131755294), false);
    }
    
    public boolean isDefaultBrowserSettingDidShow() {
        return this.preferences.getBoolean(this.getPreferenceKey(2131755305), false);
    }
    
    public boolean isNightModeEnable() {
        return this.settingPreferenceWrapper.getBoolean(this.resources.getString(2131755315), false);
    }
    
    public void setBlockImages(final boolean b) {
        this.preferences.edit().putBoolean(this.getPreferenceKey(2131755316), b).apply();
    }
    
    public void setDefaultBrowserSettingDidShow() {
        this.preferences.edit().putBoolean(this.getPreferenceKey(2131755305), true).apply();
    }
    
    public void setDefaultSearchEngine(final SearchEngine searchEngine) {
        this.preferences.edit().putString(this.getPreferenceKey(2131755325), searchEngine.getName()).apply();
    }
    
    public void setHasUnreadMyShot(final boolean b) {
        this.preferences.edit().putBoolean(this.getPreferenceKey(2131755294), b).apply();
    }
    
    public void setNewsSource(final String s) {
        this.preferences.edit().putString(this.getPreferenceKey(2131755334), s).apply();
    }
    
    public void setNightMode(final boolean b) {
        this.preferences.edit().putBoolean(this.getPreferenceKey(2131755315), b).apply();
    }
    
    public void setNightModeBrightnessValue(final float n) {
        this.preferences.edit().putFloat(this.getPreferenceKey(2131755297), n).apply();
    }
    
    public void setNightModeSpotlight(final boolean b) {
        this.preferences.edit().putBoolean(this.getPreferenceKey(2131755314), b).apply();
    }
    
    public void setPriority(final String s, final int n) {
        this.preferences.edit().putInt(s, n).apply();
    }
    
    public void setRateAppDialogDidDismiss() {
        this.preferences.edit().putBoolean(this.getPreferenceKey(2131755304), true).apply();
    }
    
    public void setRateAppDialogDidShow() {
        this.preferences.edit().putBoolean(this.getPreferenceKey(2131755306), true).apply();
    }
    
    public void setRateAppNotificationDidShow() {
        this.preferences.edit().putBoolean(this.getPreferenceKey(2131755307), true).apply();
    }
    
    public void setRemovableStorageStateOnCreate(final boolean b) {
        this.preferences.edit().putBoolean(this.getPreferenceKey(2131755323), b).apply();
    }
    
    public void setShareAppDialogDidShow() {
        this.preferences.edit().putBoolean(this.getPreferenceKey(2131755308), true).apply();
    }
    
    public void setShowedStorageMessage(final int n) {
        if (n != 22919 && n != 38183) {
            throw new RuntimeException("Unknown message type");
        }
        this.preferences.edit().putInt(this.getPreferenceKey(2131755328), n).apply();
    }
    
    public void setTurboMode(final boolean b) {
        this.preferences.edit().putBoolean(this.getPreferenceKey(2131755332), b).apply();
    }
    
    public boolean shouldBlockImages() {
        return this.preferences.getBoolean(this.resources.getString(2131755316), false);
    }
    
    public boolean shouldSaveToRemovableStorage() {
        final String[] stringArray = this.resources.getStringArray(2130903043);
        return stringArray[0].equals(this.preferences.getString(this.getPreferenceKey(2131755330), stringArray[0]));
    }
    
    public boolean shouldShowFirstrun() {
        return this.newFeatureNotice.shouldShowLiteUpdate() || !this.newFeatureNotice.hasShownFirstRun();
    }
    
    public boolean shouldUseTurboMode() {
        return this.preferences.getBoolean(this.resources.getString(2131755332), true);
    }
    
    public boolean showNightModeSpotlight() {
        return this.settingPreferenceWrapper.getBoolean(this.resources.getString(2131755314), false);
    }
    
    public static class EventHistory
    {
        private SharedPreferences preferences;
        
        private EventHistory(final SharedPreferences preferences) {
            this.preferences = preferences;
        }
        
        public void add(final String s) {
            this.setCount(s, this.getCount(s) + 1);
        }
        
        public boolean contains(String string) {
            final StringBuilder sb = new StringBuilder();
            sb.append("pref_did_");
            sb.append(string);
            final String string2 = sb.toString();
            final boolean contains = this.preferences.contains(string2);
            boolean b = false;
            if (contains) {
                return this.preferences.getBoolean(string2, false);
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("pref_");
            sb2.append(string);
            sb2.append("_counter");
            string = sb2.toString();
            if (this.preferences.getInt(string, 0) > 0) {
                b = true;
            }
            return b;
        }
        
        public int getCount(String string) {
            final StringBuilder sb = new StringBuilder();
            sb.append("pref_");
            sb.append(string);
            sb.append("_counter");
            string = sb.toString();
            return this.preferences.getInt(string, 0);
        }
        
        public void setCount(String string, final int n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("pref_");
            sb.append(string);
            sb.append("_counter");
            string = sb.toString();
            this.preferences.edit().putInt(string, n).apply();
        }
    }
}

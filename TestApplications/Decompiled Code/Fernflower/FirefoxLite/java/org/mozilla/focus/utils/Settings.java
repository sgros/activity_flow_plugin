package org.mozilla.focus.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import java.util.Set;
import org.mozilla.focus.provider.SettingPreferenceWrapper;
import org.mozilla.focus.search.SearchEngine;

public class Settings {
   private static Settings instance;
   private final Settings.EventHistory eventHistory;
   private final NewFeatureNotice newFeatureNotice;
   private final SharedPreferences preferences;
   private final Resources resources;
   private final SettingPreferenceWrapper settingPreferenceWrapper;

   private Settings(Context var1) {
      this.preferences = PreferenceManager.getDefaultSharedPreferences(var1);
      this.resources = var1.getResources();
      this.eventHistory = new Settings.EventHistory(this.preferences);
      this.newFeatureNotice = NewFeatureNotice.getInstance(var1);
      this.settingPreferenceWrapper = new SettingPreferenceWrapper(var1.getContentResolver());
   }

   public static Settings getInstance(Context var0) {
      synchronized(Settings.class){}

      Settings var4;
      try {
         if (instance == null) {
            Settings var1 = new Settings(var0.getApplicationContext());
            instance = var1;
         }

         var4 = instance;
      } finally {
         ;
      }

      return var4;
   }

   public static void updatePrefDefaultBrowserIfNeeded(Context var0, boolean var1) {
      SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(var0);
      Set var3 = var2.getAll().keySet();
      String var4 = var0.getResources().getString(2131755303);
      if (var3.contains(var4) || var1) {
         var2.edit().putBoolean(var4, var1).apply();
      }

   }

   public static void updatePrefString(Context var0, String var1, String var2) {
      PreferenceManager.getDefaultSharedPreferences(var0).edit().putString(var1, var2).apply();
   }

   public void addMenuPreferenceClickCount() {
      this.preferences.edit().putInt(this.getPreferenceKey(2131755326), this.getMenuPreferenceClickCount() + 1).apply();
   }

   public boolean canOverride(String var1, int var2) {
      boolean var3;
      if (var2 > this.preferences.getInt(var1, Integer.MAX_VALUE)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public String getDefaultSearchEngineName() {
      return this.preferences.getString(this.getPreferenceKey(2131755325), (String)null);
   }

   public Settings.EventHistory getEventHistory() {
      return this.eventHistory;
   }

   public int getMenuPreferenceClickCount() {
      return this.preferences.getInt(this.getPreferenceKey(2131755326), 0);
   }

   public String getNewsSource() {
      return this.preferences.getString(this.getPreferenceKey(2131755334), (String)null);
   }

   public float getNightModeBrightnessValue() {
      return this.settingPreferenceWrapper.getFloat(this.getPreferenceKey(2131755297), -1.0F);
   }

   String getPreferenceKey(int var1) {
      return this.resources.getString(var1);
   }

   public boolean getRemovableStorageStateOnCreate() {
      String var1 = this.getPreferenceKey(2131755323);
      return this.preferences.getBoolean(var1, false);
   }

   public int getShowedStorageMessage() {
      String var1 = this.getPreferenceKey(2131755328);
      return this.preferences.getInt(var1, 38183);
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

   public void setBlockImages(boolean var1) {
      String var2 = this.getPreferenceKey(2131755316);
      this.preferences.edit().putBoolean(var2, var1).apply();
   }

   public void setDefaultBrowserSettingDidShow() {
      this.preferences.edit().putBoolean(this.getPreferenceKey(2131755305), true).apply();
   }

   public void setDefaultSearchEngine(SearchEngine var1) {
      this.preferences.edit().putString(this.getPreferenceKey(2131755325), var1.getName()).apply();
   }

   public void setHasUnreadMyShot(boolean var1) {
      this.preferences.edit().putBoolean(this.getPreferenceKey(2131755294), var1).apply();
   }

   public void setNewsSource(String var1) {
      this.preferences.edit().putString(this.getPreferenceKey(2131755334), var1).apply();
   }

   public void setNightMode(boolean var1) {
      String var2 = this.getPreferenceKey(2131755315);
      this.preferences.edit().putBoolean(var2, var1).apply();
   }

   public void setNightModeBrightnessValue(float var1) {
      this.preferences.edit().putFloat(this.getPreferenceKey(2131755297), var1).apply();
   }

   public void setNightModeSpotlight(boolean var1) {
      String var2 = this.getPreferenceKey(2131755314);
      this.preferences.edit().putBoolean(var2, var1).apply();
   }

   public void setPriority(String var1, int var2) {
      this.preferences.edit().putInt(var1, var2).apply();
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

   public void setRemovableStorageStateOnCreate(boolean var1) {
      String var2 = this.getPreferenceKey(2131755323);
      this.preferences.edit().putBoolean(var2, var1).apply();
   }

   public void setShareAppDialogDidShow() {
      this.preferences.edit().putBoolean(this.getPreferenceKey(2131755308), true).apply();
   }

   public void setShowedStorageMessage(int var1) {
      if (var1 != 22919 && var1 != 38183) {
         throw new RuntimeException("Unknown message type");
      } else {
         String var2 = this.getPreferenceKey(2131755328);
         this.preferences.edit().putInt(var2, var1).apply();
      }
   }

   public void setTurboMode(boolean var1) {
      String var2 = this.getPreferenceKey(2131755332);
      this.preferences.edit().putBoolean(var2, var1).apply();
   }

   public boolean shouldBlockImages() {
      return this.preferences.getBoolean(this.resources.getString(2131755316), false);
   }

   public boolean shouldSaveToRemovableStorage() {
      String[] var1 = this.resources.getStringArray(2130903043);
      String var2 = this.getPreferenceKey(2131755330);
      var2 = this.preferences.getString(var2, var1[0]);
      return var1[0].equals(var2);
   }

   public boolean shouldShowFirstrun() {
      boolean var1;
      if (!this.newFeatureNotice.shouldShowLiteUpdate() && this.newFeatureNotice.hasShownFirstRun()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean shouldUseTurboMode() {
      return this.preferences.getBoolean(this.resources.getString(2131755332), true);
   }

   public boolean showNightModeSpotlight() {
      return this.settingPreferenceWrapper.getBoolean(this.resources.getString(2131755314), false);
   }

   public static class EventHistory {
      private SharedPreferences preferences;

      private EventHistory(SharedPreferences var1) {
         this.preferences = var1;
      }

      // $FF: synthetic method
      EventHistory(SharedPreferences var1, Object var2) {
         this(var1);
      }

      public void add(String var1) {
         this.setCount(var1, this.getCount(var1) + 1);
      }

      public boolean contains(String var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("pref_did_");
         var2.append(var1);
         String var5 = var2.toString();
         boolean var3 = this.preferences.contains(var5);
         boolean var4 = false;
         if (var3) {
            return this.preferences.getBoolean(var5, false);
         } else {
            var2 = new StringBuilder();
            var2.append("pref_");
            var2.append(var1);
            var2.append("_counter");
            var1 = var2.toString();
            if (this.preferences.getInt(var1, 0) > 0) {
               var4 = true;
            }

            return var4;
         }
      }

      public int getCount(String var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("pref_");
         var2.append(var1);
         var2.append("_counter");
         var1 = var2.toString();
         return this.preferences.getInt(var1, 0);
      }

      public void setCount(String var1, int var2) {
         StringBuilder var3 = new StringBuilder();
         var3.append("pref_");
         var3.append(var1);
         var3.append("_counter");
         var1 = var3.toString();
         this.preferences.edit().putInt(var1, var2).apply();
      }
   }
}

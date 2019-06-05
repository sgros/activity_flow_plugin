package org.mozilla.rocket.theme;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.Theme;
import android.preference.PreferenceManager;
import java.util.HashSet;
import java.util.Iterator;

public class ThemeManager {
   private Context baseContext;
   private ThemeManager.ThemeSet currentThemeSet;
   private boolean dirty;
   private HashSet subscribedThemeable;

   public ThemeManager(ThemeManager.ThemeHost var1) {
      this.currentThemeSet = ThemeManager.ThemeSet.Default;
      this.subscribedThemeable = new HashSet(3);
      this.dirty = true;
      this.baseContext = var1.getApplicationContext();
      this.currentThemeSet = loadCurrentTheme(getSharedPreferences(this.baseContext));
   }

   public static void dismissOnboarding(Context var0) {
      getSharedPreferences(var0).edit().putInt("pref_key_int_onboarding_version", 1).apply();
   }

   private static ThemeManager.ThemeSet findNextTheme(ThemeManager.ThemeSet var0) {
      int var1 = var0.ordinal();
      int var2 = ThemeManager.ThemeSet.values().length;
      return ThemeManager.ThemeSet.values()[(var1 + 1) % var2];
   }

   public static String getCurrentThemeName(Context var0) {
      return loadCurrentTheme(getSharedPreferences(var0)).name();
   }

   private static SharedPreferences getSharedPreferences(Context var0) {
      return PreferenceManager.getDefaultSharedPreferences(var0);
   }

   private static ThemeManager.ThemeSet loadCurrentTheme(SharedPreferences var0) {
      String var1 = var0.getString("pref_key_string_current_theme", ThemeManager.ThemeSet.Default.name());

      ThemeManager.ThemeSet var3;
      ThemeManager.ThemeSet var4;
      try {
         var4 = ThemeManager.ThemeSet.valueOf(var1);
      } catch (Exception var2) {
         var4 = ThemeManager.ThemeSet.Default;
         saveCurrentTheme(var0, var4);
         var3 = var4;
         return var3;
      }

      var3 = var4;
      return var3;
   }

   private void notifyThemeChange() {
      Iterator var1 = this.subscribedThemeable.iterator();

      while(var1.hasNext()) {
         ((ThemeManager.Themeable)var1.next()).onThemeChanged();
      }

   }

   private static void saveCurrentTheme(SharedPreferences var0, ThemeManager.ThemeSet var1) {
      var0.edit().putString("pref_key_string_current_theme", var1.name()).apply();
   }

   private void setCurrentTheme(ThemeManager.ThemeSet var1) {
      saveCurrentTheme(getSharedPreferences(this.baseContext), var1);
      this.currentThemeSet = var1;
      this.dirty = true;
   }

   public static boolean shouldShowOnboarding(Context var0) {
      int var1 = getSharedPreferences(var0).getInt("pref_key_int_onboarding_version", 0);
      boolean var2 = true;
      if (var1 >= 1) {
         var2 = false;
      }

      return var2;
   }

   public void applyCurrentTheme(Theme var1) {
      if (this.dirty) {
         this.dirty = false;
         var1.applyStyle(this.currentThemeSet.style, true);
      }

   }

   public void resetDefaultTheme() {
      this.setCurrentTheme(ThemeManager.ThemeSet.Default);
      this.notifyThemeChange();
   }

   public void subscribeThemeChange(ThemeManager.Themeable var1) {
      this.subscribedThemeable.add(var1);
   }

   public ThemeManager.ThemeSet toggleNextTheme() {
      ThemeManager.ThemeSet var1 = findNextTheme(loadCurrentTheme(getSharedPreferences(this.baseContext)));
      this.setCurrentTheme(var1);
      this.notifyThemeChange();
      return var1;
   }

   public void unsubscribeThemeChange(ThemeManager.Themeable var1) {
      this.subscribedThemeable.remove(var1);
   }

   public interface ThemeHost {
      Context getApplicationContext();

      ThemeManager getThemeManager();
   }

   public static enum ThemeSet {
      BlueViolet(2131820989),
      CatalinaBlue(2131820987),
      CornflowerBlue(2131820990),
      Default(2131820992),
      Gossamer(2131820988),
      Rocket(2131820991);

      final int style;

      private ThemeSet(int var3) {
         this.style = var3;
      }
   }

   public interface Themeable {
      void onThemeChanged();
   }
}

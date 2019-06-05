package org.mozilla.focus.locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.mozilla.focus.generated.LocaleList;

public class LocaleManager {
   private static String PREF_LOCALE;
   private static final AtomicReference instance = new AtomicReference();
   private volatile Locale currentLocale;
   private final AtomicBoolean inited = new AtomicBoolean(false);
   private BroadcastReceiver receiver;
   private volatile Locale systemLocale = Locale.getDefault();
   private boolean systemLocaleDidChange;

   public static LocaleManager getInstance() {
      LocaleManager var0 = (LocaleManager)instance.get();
      if (var0 != null) {
         return var0;
      } else {
         var0 = new LocaleManager();
         return instance.compareAndSet((Object)null, var0) ? var0 : (LocaleManager)instance.get();
      }
   }

   public static Collection getPackagedLocaleTags(Context var0) {
      return LocaleList.BUNDLED_LOCALES;
   }

   private String getPersistedLocale(Context var1) {
      String var2 = this.getSharedPreferences(var1).getString(PREF_LOCALE, "");
      return "".equals(var2) ? null : var2;
   }

   private SharedPreferences getSharedPreferences(Context var1) {
      if (PREF_LOCALE == null) {
         PREF_LOCALE = var1.getResources().getString(2131755312);
      }

      return PreferenceManager.getDefaultSharedPreferences(var1);
   }

   private boolean isMirroringSystemLocale(Context var1) {
      boolean var2;
      if (this.getPersistedLocale(var1) == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private void persistLocale(Context var1, String var2) {
      this.getSharedPreferences(var1).edit().putString(PREF_LOCALE, var2).apply();
   }

   private String updateLocale(Context var1, String var2) {
      Locale var3 = Locale.getDefault();
      Log.d("LOCALE", "Trying to check locale");
      if (var3.toString().equals(var2)) {
         Log.d("LOCALE", "Early return");
         return null;
      } else {
         return this.updateLocale(var1, Locales.parseLocaleCode(var2));
      }
   }

   private String updateLocale(Context var1, Locale var2) {
      if (Locale.getDefault().equals(var2)) {
         return null;
      } else {
         Locale.setDefault(var2);
         this.currentLocale = var2;
         this.updateConfiguration(var1, var2);
         return var2.toString();
      }
   }

   public void correctLocale(Context var1, Resources var2, Configuration var3) {
      Locale var4 = this.getCurrentLocale(var1);
      if (var4 == null) {
         Log.d("GeckoLocales", "No selected locale. No correction needed.");
      } else {
         var3.locale = var4;
         Locale.setDefault(var4);
         var2.updateConfiguration(var3, (DisplayMetrics)null);
      }
   }

   public String getAndApplyPersistedLocale(Context var1) {
      this.initialize(var1);
      long var2 = SystemClock.uptimeMillis();
      String var4 = this.getPersistedLocale(var1);
      if (var4 == null) {
         return null;
      } else {
         var4 = this.updateLocale(var1, var4);
         if (var4 == null) {
            this.updateConfiguration(var1, this.currentLocale);
         }

         long var5 = SystemClock.uptimeMillis();
         StringBuilder var7 = new StringBuilder();
         var7.append("Locale read and update took: ");
         var7.append(var5 - var2);
         var7.append("ms.");
         Log.i("GeckoLocales", var7.toString());
         return var4;
      }
   }

   public Locale getCurrentLocale(Context var1) {
      if (this.currentLocale != null) {
         return this.currentLocale;
      } else {
         String var2 = this.getPersistedLocale(var1);
         if (var2 == null) {
            return null;
         } else {
            Locale var3 = Locales.parseLocaleCode(var2);
            this.currentLocale = var3;
            return var3;
         }
      }
   }

   public void initialize(Context var1) {
      if (this.inited.compareAndSet(false, true)) {
         this.receiver = new BroadcastReceiver() {
            public void onReceive(Context var1, Intent var2) {
               Locale var4 = LocaleManager.this.systemLocale;
               LocaleManager.this.systemLocale = var1.getResources().getConfiguration().locale;
               LocaleManager.this.systemLocaleDidChange = true;
               StringBuilder var3 = new StringBuilder();
               var3.append("System locale changed from ");
               var3.append(var4);
               var3.append(" to ");
               var3.append(LocaleManager.this.systemLocale);
               Log.d("GeckoLocales", var3.toString());
            }
         };
         var1.registerReceiver(this.receiver, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
      }
   }

   public Locale onSystemConfigurationChanged(Context var1, Resources var2, Configuration var3, Locale var4) {
      if (!this.isMirroringSystemLocale(var1)) {
         this.correctLocale(var1, var2, var3);
      }

      Locale var5 = var3.locale;
      return var5.equals(var4) ? null : var5;
   }

   public void resetToSystemLocale(Context var1) {
      this.getSharedPreferences(var1).edit().remove(PREF_LOCALE).apply();
      this.updateLocale(var1, this.systemLocale);
   }

   public String setSelectedLocale(Context var1, String var2) {
      String var3 = this.updateLocale(var1, var2);
      this.persistLocale(var1, var2);
      return var3;
   }

   public void updateConfiguration(Context var1, Locale var2) {
      Resources var4 = var1.getResources();
      Configuration var3 = var4.getConfiguration();
      var3.locale = var2;
      var3.setLayoutDirection(var2);
      var4.updateConfiguration(var3, (DisplayMetrics)null);
   }
}

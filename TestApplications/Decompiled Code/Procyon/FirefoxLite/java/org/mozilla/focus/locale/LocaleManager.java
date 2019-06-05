// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.locale;

import android.content.IntentFilter;
import android.content.Intent;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import org.mozilla.focus.generated.LocaleList;
import java.util.Collection;
import android.content.Context;
import android.content.BroadcastReceiver;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class LocaleManager
{
    private static String PREF_LOCALE;
    private static final AtomicReference<LocaleManager> instance;
    private volatile Locale currentLocale;
    private final AtomicBoolean inited;
    private BroadcastReceiver receiver;
    private volatile Locale systemLocale;
    private boolean systemLocaleDidChange;
    
    static {
        instance = new AtomicReference<LocaleManager>();
    }
    
    public LocaleManager() {
        this.systemLocale = Locale.getDefault();
        this.inited = new AtomicBoolean(false);
    }
    
    public static LocaleManager getInstance() {
        final LocaleManager localeManager = LocaleManager.instance.get();
        if (localeManager != null) {
            return localeManager;
        }
        final LocaleManager newValue = new LocaleManager();
        if (LocaleManager.instance.compareAndSet(null, newValue)) {
            return newValue;
        }
        return LocaleManager.instance.get();
    }
    
    public static Collection<String> getPackagedLocaleTags(final Context context) {
        return LocaleList.BUNDLED_LOCALES;
    }
    
    private String getPersistedLocale(final Context context) {
        final String string = this.getSharedPreferences(context).getString(LocaleManager.PREF_LOCALE, "");
        if ("".equals(string)) {
            return null;
        }
        return string;
    }
    
    private SharedPreferences getSharedPreferences(final Context context) {
        if (LocaleManager.PREF_LOCALE == null) {
            LocaleManager.PREF_LOCALE = context.getResources().getString(2131755312);
        }
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    private boolean isMirroringSystemLocale(final Context context) {
        return this.getPersistedLocale(context) == null;
    }
    
    private void persistLocale(final Context context, final String s) {
        this.getSharedPreferences(context).edit().putString(LocaleManager.PREF_LOCALE, s).apply();
    }
    
    private String updateLocale(final Context context, final String anObject) {
        final Locale default1 = Locale.getDefault();
        Log.d("LOCALE", "Trying to check locale");
        if (default1.toString().equals(anObject)) {
            Log.d("LOCALE", "Early return");
            return null;
        }
        return this.updateLocale(context, Locales.parseLocaleCode(anObject));
    }
    
    private String updateLocale(final Context context, final Locale currentLocale) {
        if (Locale.getDefault().equals(currentLocale)) {
            return null;
        }
        Locale.setDefault(currentLocale);
        this.updateConfiguration(context, this.currentLocale = currentLocale);
        return currentLocale.toString();
    }
    
    public void correctLocale(final Context context, final Resources resources, final Configuration configuration) {
        final Locale currentLocale = this.getCurrentLocale(context);
        if (currentLocale == null) {
            Log.d("GeckoLocales", "No selected locale. No correction needed.");
            return;
        }
        Locale.setDefault(configuration.locale = currentLocale);
        resources.updateConfiguration(configuration, (DisplayMetrics)null);
    }
    
    public String getAndApplyPersistedLocale(final Context context) {
        this.initialize(context);
        final long uptimeMillis = SystemClock.uptimeMillis();
        final String persistedLocale = this.getPersistedLocale(context);
        if (persistedLocale == null) {
            return null;
        }
        final String updateLocale = this.updateLocale(context, persistedLocale);
        if (updateLocale == null) {
            this.updateConfiguration(context, this.currentLocale);
        }
        final long uptimeMillis2 = SystemClock.uptimeMillis();
        final StringBuilder sb = new StringBuilder();
        sb.append("Locale read and update took: ");
        sb.append(uptimeMillis2 - uptimeMillis);
        sb.append("ms.");
        Log.i("GeckoLocales", sb.toString());
        return updateLocale;
    }
    
    public Locale getCurrentLocale(final Context context) {
        if (this.currentLocale != null) {
            return this.currentLocale;
        }
        final String persistedLocale = this.getPersistedLocale(context);
        if (persistedLocale == null) {
            return null;
        }
        return this.currentLocale = Locales.parseLocaleCode(persistedLocale);
    }
    
    public void initialize(final Context context) {
        if (!this.inited.compareAndSet(false, true)) {
            return;
        }
        context.registerReceiver(this.receiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final Locale access$000 = LocaleManager.this.systemLocale;
                LocaleManager.this.systemLocale = context.getResources().getConfiguration().locale;
                LocaleManager.this.systemLocaleDidChange = true;
                final StringBuilder sb = new StringBuilder();
                sb.append("System locale changed from ");
                sb.append(access$000);
                sb.append(" to ");
                sb.append(LocaleManager.this.systemLocale);
                Log.d("GeckoLocales", sb.toString());
            }
        }, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
    }
    
    public Locale onSystemConfigurationChanged(final Context context, final Resources resources, final Configuration configuration, final Locale obj) {
        if (!this.isMirroringSystemLocale(context)) {
            this.correctLocale(context, resources, configuration);
        }
        final Locale locale = configuration.locale;
        if (locale.equals(obj)) {
            return null;
        }
        return locale;
    }
    
    public void resetToSystemLocale(final Context context) {
        this.getSharedPreferences(context).edit().remove(LocaleManager.PREF_LOCALE).apply();
        this.updateLocale(context, this.systemLocale);
    }
    
    public String setSelectedLocale(final Context context, final String s) {
        final String updateLocale = this.updateLocale(context, s);
        this.persistLocale(context, s);
        return updateLocale;
    }
    
    public void updateConfiguration(final Context context, final Locale locale) {
        final Resources resources = context.getResources();
        final Configuration configuration = resources.getConfiguration();
        configuration.setLayoutDirection(configuration.locale = locale);
        resources.updateConfiguration(configuration, (DisplayMetrics)null);
    }
}

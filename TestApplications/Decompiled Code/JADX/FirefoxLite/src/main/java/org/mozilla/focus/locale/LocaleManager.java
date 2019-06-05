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
import android.util.Log;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.mozilla.focus.generated.LocaleList;
import org.mozilla.rocket.C0769R;

public class LocaleManager {
    private static String PREF_LOCALE;
    private static final AtomicReference<LocaleManager> instance = new AtomicReference();
    private volatile Locale currentLocale;
    private final AtomicBoolean inited = new AtomicBoolean(false);
    private BroadcastReceiver receiver;
    private volatile Locale systemLocale = Locale.getDefault();
    private boolean systemLocaleDidChange;

    /* renamed from: org.mozilla.focus.locale.LocaleManager$1 */
    class C04931 extends BroadcastReceiver {
        C04931() {
        }

        public void onReceive(Context context, Intent intent) {
            Locale access$000 = LocaleManager.this.systemLocale;
            LocaleManager.this.systemLocale = context.getResources().getConfiguration().locale;
            LocaleManager.this.systemLocaleDidChange = true;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("System locale changed from ");
            stringBuilder.append(access$000);
            stringBuilder.append(" to ");
            stringBuilder.append(LocaleManager.this.systemLocale);
            Log.d("GeckoLocales", stringBuilder.toString());
        }
    }

    public static LocaleManager getInstance() {
        LocaleManager localeManager = (LocaleManager) instance.get();
        if (localeManager != null) {
            return localeManager;
        }
        localeManager = new LocaleManager();
        if (instance.compareAndSet(null, localeManager)) {
            return localeManager;
        }
        return (LocaleManager) instance.get();
    }

    public void initialize(Context context) {
        if (this.inited.compareAndSet(false, true)) {
            this.receiver = new C04931();
            context.registerReceiver(this.receiver, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
        }
    }

    public void correctLocale(Context context, Resources resources, Configuration configuration) {
        Locale currentLocale = getCurrentLocale(context);
        if (currentLocale == null) {
            Log.d("GeckoLocales", "No selected locale. No correction needed.");
            return;
        }
        configuration.locale = currentLocale;
        Locale.setDefault(currentLocale);
        resources.updateConfiguration(configuration, null);
    }

    public Locale onSystemConfigurationChanged(Context context, Resources resources, Configuration configuration, Locale locale) {
        if (!isMirroringSystemLocale(context)) {
            correctLocale(context, resources, configuration);
        }
        Locale locale2 = configuration.locale;
        return locale2.equals(locale) ? null : locale2;
    }

    public String getAndApplyPersistedLocale(Context context) {
        initialize(context);
        long uptimeMillis = SystemClock.uptimeMillis();
        String persistedLocale = getPersistedLocale(context);
        if (persistedLocale == null) {
            return null;
        }
        persistedLocale = updateLocale(context, persistedLocale);
        if (persistedLocale == null) {
            updateConfiguration(context, this.currentLocale);
        }
        long uptimeMillis2 = SystemClock.uptimeMillis();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Locale read and update took: ");
        stringBuilder.append(uptimeMillis2 - uptimeMillis);
        stringBuilder.append("ms.");
        Log.i("GeckoLocales", stringBuilder.toString());
        return persistedLocale;
    }

    public String setSelectedLocale(Context context, String str) {
        String updateLocale = updateLocale(context, str);
        persistLocale(context, str);
        return updateLocale;
    }

    public void resetToSystemLocale(Context context) {
        getSharedPreferences(context).edit().remove(PREF_LOCALE).apply();
        updateLocale(context, this.systemLocale);
    }

    public void updateConfiguration(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, null);
    }

    private SharedPreferences getSharedPreferences(Context context) {
        if (PREF_LOCALE == null) {
            PREF_LOCALE = context.getResources().getString(C0769R.string.pref_key_locale);
        }
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String getPersistedLocale(Context context) {
        String string = getSharedPreferences(context).getString(PREF_LOCALE, "");
        return "".equals(string) ? null : string;
    }

    private void persistLocale(Context context, String str) {
        getSharedPreferences(context).edit().putString(PREF_LOCALE, str).apply();
    }

    public Locale getCurrentLocale(Context context) {
        if (this.currentLocale != null) {
            return this.currentLocale;
        }
        String persistedLocale = getPersistedLocale(context);
        if (persistedLocale == null) {
            return null;
        }
        Locale parseLocaleCode = Locales.parseLocaleCode(persistedLocale);
        this.currentLocale = parseLocaleCode;
        return parseLocaleCode;
    }

    private String updateLocale(Context context, String str) {
        Locale locale = Locale.getDefault();
        Log.d("LOCALE", "Trying to check locale");
        if (!locale.toString().equals(str)) {
            return updateLocale(context, Locales.parseLocaleCode(str));
        }
        Log.d("LOCALE", "Early return");
        return null;
    }

    private String updateLocale(Context context, Locale locale) {
        if (Locale.getDefault().equals(locale)) {
            return null;
        }
        Locale.setDefault(locale);
        this.currentLocale = locale;
        updateConfiguration(context, locale);
        return locale.toString();
    }

    private boolean isMirroringSystemLocale(Context context) {
        return getPersistedLocale(context) == null;
    }

    public static Collection<String> getPackagedLocaleTags(Context context) {
        return LocaleList.BUNDLED_LOCALES;
    }
}

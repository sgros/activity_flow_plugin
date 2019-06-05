// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.locale;

import android.os.StrictMode$ThreadPolicy;
import android.os.StrictMode;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.Context;
import java.util.Locale;

public class Locales
{
    public static String getLanguage(final Locale locale) {
        final String language = locale.getLanguage();
        if (language.equals("iw")) {
            return "he";
        }
        if (language.equals("in")) {
            return "id";
        }
        if (language.equals("ji")) {
            return "yi";
        }
        return language;
    }
    
    public static String getLanguageTag(final Locale locale) {
        final String language = getLanguage(locale);
        final String country = locale.getCountry();
        if (country.equals("")) {
            return language;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(language);
        sb.append("-");
        sb.append(country);
        return sb.toString();
    }
    
    public static Resources getLocalizedResources(final Context context) {
        final Resources resources = context.getResources();
        final Locale currentLocale = LocaleManager.getInstance().getCurrentLocale(context);
        final Locale locale = resources.getConfiguration().locale;
        if (currentLocale == null || locale == null) {
            return resources;
        }
        if (currentLocale.toLanguageTag().equals(locale.toLanguageTag())) {
            return resources;
        }
        final Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(currentLocale);
        return context.createConfigurationContext(configuration).getResources();
    }
    
    public static void initializeLocale(final Context context) {
        final LocaleManager instance = LocaleManager.getInstance();
        final StrictMode$ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        StrictMode.allowThreadDiskWrites();
        try {
            instance.getAndApplyPersistedLocale(context);
        }
        finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }
    
    public static Locale parseLocaleCode(final String language) {
        int endIndex;
        if ((endIndex = language.indexOf(45)) == -1) {
            endIndex = language.indexOf(95);
            if (endIndex == -1) {
                return new Locale(language);
            }
        }
        return new Locale(language.substring(0, endIndex), language.substring(endIndex + 1));
    }
}

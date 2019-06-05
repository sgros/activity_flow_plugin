package org.mozilla.focus.locale;

import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import java.util.Locale;

public class Locales {
    public static void initializeLocale(Context context) {
        LocaleManager instance = LocaleManager.getInstance();
        ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        StrictMode.allowThreadDiskWrites();
        try {
            instance.getAndApplyPersistedLocale(context);
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }

    public static String getLanguage(Locale locale) {
        String language = locale.getLanguage();
        if (language.equals("iw")) {
            return "he";
        }
        if (language.equals("in")) {
            return "id";
        }
        return language.equals("ji") ? "yi" : language;
    }

    public static String getLanguageTag(Locale locale) {
        String language = getLanguage(locale);
        String country = locale.getCountry();
        if (country.equals("")) {
            return language;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(language);
        stringBuilder.append("-");
        stringBuilder.append(country);
        return stringBuilder.toString();
    }

    public static Locale parseLocaleCode(String str) {
        int indexOf = str.indexOf(45);
        if (indexOf == -1) {
            indexOf = str.indexOf(95);
            if (indexOf == -1) {
                return new Locale(str);
            }
        }
        return new Locale(str.substring(0, indexOf), str.substring(indexOf + 1));
    }

    /* JADX WARNING: Missing block: B:8:0x003b, code skipped:
            return r0;
     */
    public static android.content.res.Resources getLocalizedResources(android.content.Context r4) {
        /*
        r0 = r4.getResources();
        r1 = org.mozilla.focus.locale.LocaleManager.getInstance();
        r1 = r1.getCurrentLocale(r4);
        r2 = r0.getConfiguration();
        r2 = r2.locale;
        if (r1 == 0) goto L_0x003b;
    L_0x0014:
        if (r2 != 0) goto L_0x0017;
    L_0x0016:
        goto L_0x003b;
    L_0x0017:
        r3 = r1.toLanguageTag();
        r2 = r2.toLanguageTag();
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x0026;
    L_0x0025:
        return r0;
    L_0x0026:
        r2 = new android.content.res.Configuration;
        r0 = r0.getConfiguration();
        r2.<init>(r0);
        r2.setLocale(r1);
        r4 = r4.createConfigurationContext(r2);
        r4 = r4.getResources();
        return r4;
    L_0x003b:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.locale.Locales.getLocalizedResources(android.content.Context):android.content.res.Resources");
    }
}

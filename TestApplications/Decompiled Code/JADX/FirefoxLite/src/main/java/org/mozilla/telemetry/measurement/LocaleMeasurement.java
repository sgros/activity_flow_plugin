package org.mozilla.telemetry.measurement;

import java.util.Locale;

public class LocaleMeasurement extends TelemetryMeasurement {
    public LocaleMeasurement() {
        super("locale");
    }

    public Object flush() {
        return getLanguageTag(Locale.getDefault());
    }

    public String getLanguageTag(Locale locale) {
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

    private String getLanguage(Locale locale) {
        String language = locale.getLanguage();
        if (language.equals("iw")) {
            return "he";
        }
        if (language.equals("in")) {
            return "id";
        }
        return language.equals("ji") ? "yi" : language;
    }
}

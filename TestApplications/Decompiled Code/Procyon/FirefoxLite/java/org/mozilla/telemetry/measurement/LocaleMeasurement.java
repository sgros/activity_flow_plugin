// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import java.util.Locale;

public class LocaleMeasurement extends TelemetryMeasurement
{
    public LocaleMeasurement() {
        super("locale");
    }
    
    private String getLanguage(final Locale locale) {
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
    
    @Override
    public Object flush() {
        return this.getLanguageTag(Locale.getDefault());
    }
    
    public String getLanguageTag(final Locale locale) {
        final String language = this.getLanguage(locale);
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
}

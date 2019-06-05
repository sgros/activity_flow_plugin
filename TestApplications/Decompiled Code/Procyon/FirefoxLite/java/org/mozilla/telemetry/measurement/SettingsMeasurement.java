// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import android.preference.PreferenceManager;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SettingsMeasurement extends TelemetryMeasurement
{
    private final TelemetryConfiguration configuration;
    
    public SettingsMeasurement(final TelemetryConfiguration configuration) {
        super("settings");
        this.configuration = configuration;
    }
    
    @Override
    public Object flush() {
        final SettingsProvider settingsProvider = this.configuration.getSettingsProvider();
        settingsProvider.update(this.configuration);
        final JSONObject jsonObject = new JSONObject();
        final Set<String> preferencesImportantForTelemetry = this.configuration.getPreferencesImportantForTelemetry();
        if (preferencesImportantForTelemetry.isEmpty()) {
            return jsonObject;
        }
        for (final String s : preferencesImportantForTelemetry) {
            try {
                if (settingsProvider.containsKey(s)) {
                    jsonObject.put(s, (Object)String.valueOf(settingsProvider.getValue(s)));
                    continue;
                }
                jsonObject.put(s, JSONObject.NULL);
                continue;
            }
            catch (JSONException cause) {
                throw new AssertionError("Preference value can't be serialized to JSON", (Throwable)cause);
            }
            break;
        }
        settingsProvider.release();
        return jsonObject;
    }
    
    public interface SettingsProvider
    {
        boolean containsKey(final String p0);
        
        Object getValue(final String p0);
        
        void release();
        
        void update(final TelemetryConfiguration p0);
    }
    
    public static class SharedPreferenceSettingsProvider implements SettingsProvider
    {
        private Map<String, ?> preferences;
        
        @Override
        public boolean containsKey(final String s) {
            return this.preferences != null && this.preferences.containsKey(s);
        }
        
        @Override
        public Object getValue(final String s) {
            return this.preferences.get(s);
        }
        
        @Override
        public void release() {
            this.preferences = null;
        }
        
        @Override
        public void update(final TelemetryConfiguration telemetryConfiguration) {
            this.preferences = (Map<String, ?>)PreferenceManager.getDefaultSharedPreferences(telemetryConfiguration.getContext()).getAll();
        }
    }
}

package org.mozilla.telemetry.measurement;

import android.preference.PreferenceManager;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SettingsMeasurement extends TelemetryMeasurement {
    private final TelemetryConfiguration configuration;

    public interface SettingsProvider {
        boolean containsKey(String str);

        Object getValue(String str);

        void release();

        void update(TelemetryConfiguration telemetryConfiguration);
    }

    public static class SharedPreferenceSettingsProvider implements SettingsProvider {
        private Map<String, ?> preferences;

        public void update(TelemetryConfiguration telemetryConfiguration) {
            this.preferences = PreferenceManager.getDefaultSharedPreferences(telemetryConfiguration.getContext()).getAll();
        }

        public boolean containsKey(String str) {
            return this.preferences != null && this.preferences.containsKey(str);
        }

        public Object getValue(String str) {
            return this.preferences.get(str);
        }

        public void release() {
            this.preferences = null;
        }
    }

    public SettingsMeasurement(TelemetryConfiguration telemetryConfiguration) {
        super("settings");
        this.configuration = telemetryConfiguration;
    }

    public Object flush() {
        SettingsProvider settingsProvider = this.configuration.getSettingsProvider();
        settingsProvider.update(this.configuration);
        JSONObject jSONObject = new JSONObject();
        Set<String> preferencesImportantForTelemetry = this.configuration.getPreferencesImportantForTelemetry();
        if (preferencesImportantForTelemetry.isEmpty()) {
            return jSONObject;
        }
        for (String str : preferencesImportantForTelemetry) {
            try {
                if (settingsProvider.containsKey(str)) {
                    jSONObject.put(str, String.valueOf(settingsProvider.getValue(str)));
                } else {
                    jSONObject.put(str, JSONObject.NULL);
                }
            } catch (JSONException e) {
                throw new AssertionError("Preference value can't be serialized to JSON", e);
            }
        }
        settingsProvider.release();
        return jSONObject;
    }
}

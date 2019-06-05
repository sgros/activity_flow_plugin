package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SearchesMeasurement extends TelemetryMeasurement {
    private final TelemetryConfiguration configuration;

    public SearchesMeasurement(TelemetryConfiguration telemetryConfiguration) {
        super("searches");
        this.configuration = telemetryConfiguration;
    }

    public Object flush() {
        return getSearchCountMapAndReset();
    }

    private synchronized JSONObject getSearchCountMapAndReset() {
        JSONObject jSONObject;
        try {
            SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
            Editor edit = sharedPreferences.edit();
            jSONObject = new JSONObject();
            for (String str : sharedPreferences.getStringSet("measurements-search-count-keyset", Collections.emptySet())) {
                String engineSearchCountKey = getEngineSearchCountKey(str);
                jSONObject.put(str, sharedPreferences.getInt(engineSearchCountKey, 0));
                edit.remove(engineSearchCountKey);
            }
            edit.remove("measurements-search-count-keyset").apply();
        } catch (JSONException e) {
            throw new AssertionError("Should not happen: Can't construct search count JSON", e);
        }
        return jSONObject;
    }

    public synchronized void recordSearch(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(".");
        stringBuilder.append(str2);
        str = stringBuilder.toString();
        storeCount(str);
        storeKey(str);
    }

    private void storeCount(String str) {
        SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
        str = getEngineSearchCountKey(str);
        sharedPreferences.edit().putInt(str, sharedPreferences.getInt(str, 0) + 1).apply();
    }

    private void storeKey(String str) {
        SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
        Set stringSet = sharedPreferences.getStringSet("measurements-search-count-keyset", Collections.emptySet());
        if (!stringSet.contains(str)) {
            HashSet hashSet = new HashSet(stringSet);
            hashSet.add(str);
            sharedPreferences.edit().putStringSet("measurements-search-count-keyset", hashSet).apply();
        }
    }

    private static String getEngineSearchCountKey(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("measurements-search-count-engine-");
        stringBuilder.append(str);
        return stringBuilder.toString();
    }
}

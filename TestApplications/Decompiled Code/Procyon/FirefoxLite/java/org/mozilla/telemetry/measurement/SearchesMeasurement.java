// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import org.json.JSONException;
import java.util.Set;
import java.util.Collections;
import org.json.JSONObject;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SearchesMeasurement extends TelemetryMeasurement
{
    private final TelemetryConfiguration configuration;
    
    public SearchesMeasurement(final TelemetryConfiguration configuration) {
        super("searches");
        this.configuration = configuration;
    }
    
    private static String getEngineSearchCountKey(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("measurements-search-count-engine-");
        sb.append(str);
        return sb.toString();
    }
    
    private JSONObject getSearchCountMapAndReset() {
        // monitorenter(this)
        try {
            try {
                final SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
                final SharedPreferences$Editor edit = sharedPreferences.edit();
                final JSONObject jsonObject = new JSONObject();
                for (final String s : sharedPreferences.getStringSet("measurements-search-count-keyset", (Set)Collections.emptySet())) {
                    final String engineSearchCountKey = getEngineSearchCountKey(s);
                    jsonObject.put(s, sharedPreferences.getInt(engineSearchCountKey, 0));
                    edit.remove(engineSearchCountKey);
                }
                edit.remove("measurements-search-count-keyset").apply();
                // monitorexit(this)
                return jsonObject;
            }
            finally {}
        }
        catch (JSONException cause) {
            throw new AssertionError("Should not happen: Can't construct search count JSON", (Throwable)cause);
        }
    }
    // monitorexit(this)
    
    private void storeCount(String engineSearchCountKey) {
        final SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
        engineSearchCountKey = getEngineSearchCountKey(engineSearchCountKey);
        sharedPreferences.edit().putInt(engineSearchCountKey, sharedPreferences.getInt(engineSearchCountKey, 0) + 1).apply();
    }
    
    private void storeKey(final String s) {
        final SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
        final Set stringSet = sharedPreferences.getStringSet("measurements-search-count-keyset", (Set)Collections.emptySet());
        if (stringSet.contains(s)) {
            return;
        }
        final HashSet set = new HashSet<String>(stringSet);
        set.add(s);
        sharedPreferences.edit().putStringSet("measurements-search-count-keyset", (Set)set).apply();
    }
    
    @Override
    public Object flush() {
        return this.getSearchCountMapAndReset();
    }
    
    public void recordSearch(String string, final String str) {
        synchronized (this) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(".");
            sb.append(str);
            string = sb.toString();
            this.storeCount(string);
            this.storeKey(string);
        }
    }
}

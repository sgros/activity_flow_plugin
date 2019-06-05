// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SessionCountMeasurement extends TelemetryMeasurement
{
    private final TelemetryConfiguration configuration;
    
    public SessionCountMeasurement(final TelemetryConfiguration configuration) {
        super("sessions");
        this.configuration = configuration;
    }
    
    private long getAndResetCount() {
        synchronized (this) {
            final SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
            final long long1 = sharedPreferences.getLong("session_count", 0L);
            sharedPreferences.edit().putLong("session_count", 0L).apply();
            return long1;
        }
    }
    
    public void countSession() {
        synchronized (this) {
            final SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
            sharedPreferences.edit().putLong("session_count", sharedPreferences.getLong("session_count", 0L) + 1L).apply();
        }
    }
    
    @Override
    public Object flush() {
        return this.getAndResetCount();
    }
}

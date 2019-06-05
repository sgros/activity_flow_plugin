package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SessionCountMeasurement extends TelemetryMeasurement {
    private final TelemetryConfiguration configuration;

    public SessionCountMeasurement(TelemetryConfiguration telemetryConfiguration) {
        super("sessions");
        this.configuration = telemetryConfiguration;
    }

    public synchronized void countSession() {
        SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
        sharedPreferences.edit().putLong("session_count", sharedPreferences.getLong("session_count", 0) + 1).apply();
    }

    public Object flush() {
        return Long.valueOf(getAndResetCount());
    }

    private synchronized long getAndResetCount() {
        long j;
        SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
        j = sharedPreferences.getLong("session_count", 0);
        sharedPreferences.edit().putLong("session_count", 0).apply();
        return j;
    }
}

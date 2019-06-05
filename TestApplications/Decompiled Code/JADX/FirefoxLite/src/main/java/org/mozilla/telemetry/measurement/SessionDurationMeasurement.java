package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import java.util.concurrent.TimeUnit;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SessionDurationMeasurement extends TelemetryMeasurement {
    private final TelemetryConfiguration configuration;
    private boolean sessionStarted = false;
    private long timeAtSessionStartNano = -1;

    public SessionDurationMeasurement(TelemetryConfiguration telemetryConfiguration) {
        super("durations");
        this.configuration = telemetryConfiguration;
    }

    public synchronized void recordSessionStart() {
        if (this.sessionStarted) {
            throw new IllegalStateException("Trying to start session but it is already started");
        }
        this.sessionStarted = true;
        this.timeAtSessionStartNano = getSystemTimeNano();
    }

    public synchronized boolean recordSessionEnd() {
        if (!this.sessionStarted) {
            return false;
        }
        this.sessionStarted = false;
        long toSeconds = TimeUnit.NANOSECONDS.toSeconds(getSystemTimeNano() - this.timeAtSessionStartNano);
        SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
        sharedPreferences.edit().putLong("session_duration", sharedPreferences.getLong("session_duration", 0) + toSeconds).apply();
        return true;
    }

    public Object flush() {
        return Long.valueOf(getAndResetDuration());
    }

    private synchronized long getAndResetDuration() {
        long j;
        SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
        j = sharedPreferences.getLong("session_duration", 0);
        sharedPreferences.edit().putLong("session_duration", 0).apply();
        return j;
    }

    /* Access modifiers changed, original: 0000 */
    public long getSystemTimeNano() {
        return System.nanoTime();
    }
}

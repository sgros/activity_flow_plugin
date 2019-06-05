// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import java.util.concurrent.TimeUnit;
import android.content.SharedPreferences;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SessionDurationMeasurement extends TelemetryMeasurement
{
    private final TelemetryConfiguration configuration;
    private boolean sessionStarted;
    private long timeAtSessionStartNano;
    
    public SessionDurationMeasurement(final TelemetryConfiguration configuration) {
        super("durations");
        this.sessionStarted = false;
        this.timeAtSessionStartNano = -1L;
        this.configuration = configuration;
    }
    
    private long getAndResetDuration() {
        synchronized (this) {
            final SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
            final long long1 = sharedPreferences.getLong("session_duration", 0L);
            sharedPreferences.edit().putLong("session_duration", 0L).apply();
            return long1;
        }
    }
    
    @Override
    public Object flush() {
        return this.getAndResetDuration();
    }
    
    long getSystemTimeNano() {
        return System.nanoTime();
    }
    
    public boolean recordSessionEnd() {
        synchronized (this) {
            if (!this.sessionStarted) {
                return false;
            }
            this.sessionStarted = false;
            final long seconds = TimeUnit.NANOSECONDS.toSeconds(this.getSystemTimeNano() - this.timeAtSessionStartNano);
            final SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
            sharedPreferences.edit().putLong("session_duration", sharedPreferences.getLong("session_duration", 0L) + seconds).apply();
            return true;
        }
    }
    
    public void recordSessionStart() {
        synchronized (this) {
            if (!this.sessionStarted) {
                this.sessionStarted = true;
                this.timeAtSessionStartNano = this.getSystemTimeNano();
                return;
            }
            throw new IllegalStateException("Trying to start session but it is already started");
        }
    }
}

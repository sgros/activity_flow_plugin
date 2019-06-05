// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import java.util.concurrent.TimeUnit;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class FirstRunProfileDateMeasurement extends TelemetryMeasurement
{
    private TelemetryConfiguration configuration;
    
    public FirstRunProfileDateMeasurement(final TelemetryConfiguration configuration) {
        super("profileDate");
        this.configuration = configuration;
        this.ensureValueExists();
    }
    
    private long getProfileDateInDays() {
        return (long)Math.floor(this.configuration.getSharedPreferences().getLong("profile_date", this.now()) / (double)TimeUnit.DAYS.toMillis(1L));
    }
    
    void ensureValueExists() {
        final SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
        if (sharedPreferences.contains("profile_date")) {
            return;
        }
        sharedPreferences.edit().putLong("profile_date", this.now()).apply();
    }
    
    @Override
    public Object flush() {
        return this.getProfileDateInDays();
    }
    
    long now() {
        return System.currentTimeMillis();
    }
}

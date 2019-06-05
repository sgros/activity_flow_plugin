package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import java.util.concurrent.TimeUnit;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class FirstRunProfileDateMeasurement extends TelemetryMeasurement {
    private TelemetryConfiguration configuration;

    public FirstRunProfileDateMeasurement(TelemetryConfiguration telemetryConfiguration) {
        super("profileDate");
        this.configuration = telemetryConfiguration;
        ensureValueExists();
    }

    /* Access modifiers changed, original: 0000 */
    public void ensureValueExists() {
        SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
        if (!sharedPreferences.contains("profile_date")) {
            sharedPreferences.edit().putLong("profile_date", now()).apply();
        }
    }

    public Object flush() {
        return Long.valueOf(getProfileDateInDays());
    }

    private long getProfileDateInDays() {
        return (long) Math.floor(((double) this.configuration.getSharedPreferences().getLong("profile_date", now())) / ((double) TimeUnit.DAYS.toMillis(1)));
    }

    /* Access modifiers changed, original: 0000 */
    public long now() {
        return System.currentTimeMillis();
    }
}

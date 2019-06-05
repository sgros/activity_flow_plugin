package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.ping.TelemetryPingBuilder;

public class SequenceMeasurement extends TelemetryMeasurement {
    private final TelemetryConfiguration configuration;
    private final String preferenceKeySequence;

    public SequenceMeasurement(TelemetryConfiguration telemetryConfiguration, TelemetryPingBuilder telemetryPingBuilder) {
        super("seq");
        this.configuration = telemetryConfiguration;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("sequence_");
        stringBuilder.append(telemetryPingBuilder.getType());
        this.preferenceKeySequence = stringBuilder.toString();
    }

    public Object flush() {
        return Long.valueOf(getAndIncrementSequence());
    }

    private synchronized long getAndIncrementSequence() {
        long j;
        SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
        j = sharedPreferences.getLong(this.preferenceKeySequence, 0) + 1;
        sharedPreferences.edit().putLong(this.preferenceKeySequence, j).apply();
        return j;
    }
}

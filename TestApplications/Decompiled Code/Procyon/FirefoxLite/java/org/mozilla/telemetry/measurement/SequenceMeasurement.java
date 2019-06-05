// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import org.mozilla.telemetry.ping.TelemetryPingBuilder;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SequenceMeasurement extends TelemetryMeasurement
{
    private final TelemetryConfiguration configuration;
    private final String preferenceKeySequence;
    
    public SequenceMeasurement(final TelemetryConfiguration configuration, final TelemetryPingBuilder telemetryPingBuilder) {
        super("seq");
        this.configuration = configuration;
        final StringBuilder sb = new StringBuilder();
        sb.append("sequence_");
        sb.append(telemetryPingBuilder.getType());
        this.preferenceKeySequence = sb.toString();
    }
    
    private long getAndIncrementSequence() {
        synchronized (this) {
            final SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
            final long long1 = sharedPreferences.getLong(this.preferenceKeySequence, 0L);
            final SharedPreferences$Editor edit = sharedPreferences.edit();
            final String preferenceKeySequence = this.preferenceKeySequence;
            final long n = long1 + 1L;
            edit.putLong(preferenceKeySequence, n).apply();
            return n;
        }
    }
    
    @Override
    public Object flush() {
        return this.getAndIncrementSequence();
    }
}

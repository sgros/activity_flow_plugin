package org.mozilla.telemetry.schedule;

import org.mozilla.telemetry.config.TelemetryConfiguration;

public interface TelemetryScheduler {
    void scheduleUpload(TelemetryConfiguration telemetryConfiguration);
}

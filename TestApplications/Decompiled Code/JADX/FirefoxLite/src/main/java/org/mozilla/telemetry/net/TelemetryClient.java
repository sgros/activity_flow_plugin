package org.mozilla.telemetry.net;

import org.mozilla.telemetry.config.TelemetryConfiguration;

public interface TelemetryClient {
    boolean uploadPing(TelemetryConfiguration telemetryConfiguration, String str, String str2);
}

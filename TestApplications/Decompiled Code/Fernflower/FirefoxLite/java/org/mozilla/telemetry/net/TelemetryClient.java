package org.mozilla.telemetry.net;

import org.mozilla.telemetry.config.TelemetryConfiguration;

public interface TelemetryClient {
   boolean uploadPing(TelemetryConfiguration var1, String var2, String var3);
}

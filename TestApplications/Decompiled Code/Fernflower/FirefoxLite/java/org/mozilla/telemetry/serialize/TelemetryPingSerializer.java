package org.mozilla.telemetry.serialize;

import org.mozilla.telemetry.ping.TelemetryPing;

public interface TelemetryPingSerializer {
   String serialize(TelemetryPing var1);
}

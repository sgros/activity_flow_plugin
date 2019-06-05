package org.mozilla.telemetry.storage;

import org.mozilla.telemetry.ping.TelemetryPing;

public interface TelemetryStorage {

    public interface TelemetryStorageCallback {
        boolean onTelemetryPingLoaded(String str, String str2);
    }

    int countStoredPings(String str);

    boolean process(String str, TelemetryStorageCallback telemetryStorageCallback);

    void store(TelemetryPing telemetryPing);
}

package org.mozilla.telemetry.storage;

import org.mozilla.telemetry.ping.TelemetryPing;

public interface TelemetryStorage {
   int countStoredPings(String var1);

   boolean process(String var1, TelemetryStorage.TelemetryStorageCallback var2);

   void store(TelemetryPing var1);

   public interface TelemetryStorageCallback {
      boolean onTelemetryPingLoaded(String var1, String var2);
   }
}

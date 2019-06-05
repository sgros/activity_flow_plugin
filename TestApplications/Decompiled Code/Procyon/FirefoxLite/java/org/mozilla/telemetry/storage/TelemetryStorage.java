// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.storage;

import org.mozilla.telemetry.ping.TelemetryPing;

public interface TelemetryStorage
{
    int countStoredPings(final String p0);
    
    boolean process(final String p0, final TelemetryStorageCallback p1);
    
    void store(final TelemetryPing p0);
    
    public interface TelemetryStorageCallback
    {
        boolean onTelemetryPingLoaded(final String p0, final String p1);
    }
}

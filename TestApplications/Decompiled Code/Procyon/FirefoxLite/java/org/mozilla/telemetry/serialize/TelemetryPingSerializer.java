// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.serialize;

import org.mozilla.telemetry.ping.TelemetryPing;

public interface TelemetryPingSerializer
{
    String serialize(final TelemetryPing p0);
}

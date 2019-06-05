// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.net;

import org.mozilla.telemetry.config.TelemetryConfiguration;

public interface TelemetryClient
{
    boolean uploadPing(final TelemetryConfiguration p0, final String p1, final String p2);
}

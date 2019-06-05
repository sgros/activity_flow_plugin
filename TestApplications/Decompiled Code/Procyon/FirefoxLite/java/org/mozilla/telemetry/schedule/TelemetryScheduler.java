// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.schedule;

import org.mozilla.telemetry.config.TelemetryConfiguration;

public interface TelemetryScheduler
{
    void scheduleUpload(final TelemetryConfiguration p0);
}

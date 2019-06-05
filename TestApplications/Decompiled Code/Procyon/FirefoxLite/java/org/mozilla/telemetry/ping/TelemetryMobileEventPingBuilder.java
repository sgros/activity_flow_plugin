// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.ping;

import org.mozilla.telemetry.measurement.EventsMeasurement;

public class TelemetryMobileEventPingBuilder extends TelemetryPingBuilder
{
    private EventsMeasurement eventsMeasurement;
    
    @Override
    public boolean canBuild() {
        return this.eventsMeasurement.getEventCount() >= this.getConfiguration().getMinimumEventsForUpload();
    }
    
    public EventsMeasurement getEventsMeasurement() {
        return this.eventsMeasurement;
    }
    
    @Override
    protected String getUploadPath(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.getUploadPath(s));
        sb.append("?v=4");
        return sb.toString();
    }
}

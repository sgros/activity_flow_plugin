// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.ping;

import org.mozilla.telemetry.measurement.SettingsMeasurement;
import org.mozilla.telemetry.measurement.TimezoneOffsetMeasurement;
import org.mozilla.telemetry.measurement.CreatedTimestampMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemVersionMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemMeasurement;
import org.mozilla.telemetry.measurement.LocaleMeasurement;
import org.mozilla.telemetry.measurement.TelemetryMeasurement;
import org.mozilla.telemetry.measurement.SequenceMeasurement;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.measurement.EventsMeasurement;

@Deprecated
public class TelemetryEventPingBuilder extends TelemetryPingBuilder
{
    private EventsMeasurement eventsMeasurement;
    
    public TelemetryEventPingBuilder(final TelemetryConfiguration telemetryConfiguration) {
        super(telemetryConfiguration, "focus-event", 1);
        this.addMeasurement(new SequenceMeasurement(telemetryConfiguration, this));
        this.addMeasurement(new LocaleMeasurement());
        this.addMeasurement(new OperatingSystemMeasurement());
        this.addMeasurement(new OperatingSystemVersionMeasurement());
        this.addMeasurement(new CreatedTimestampMeasurement());
        this.addMeasurement(new TimezoneOffsetMeasurement());
        this.addMeasurement(new SettingsMeasurement(telemetryConfiguration));
        this.addMeasurement(this.eventsMeasurement = new EventsMeasurement(telemetryConfiguration));
    }
    
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

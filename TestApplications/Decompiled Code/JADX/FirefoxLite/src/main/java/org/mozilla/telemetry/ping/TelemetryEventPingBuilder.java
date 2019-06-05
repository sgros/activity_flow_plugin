package org.mozilla.telemetry.ping;

import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.measurement.CreatedTimestampMeasurement;
import org.mozilla.telemetry.measurement.EventsMeasurement;
import org.mozilla.telemetry.measurement.LocaleMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemVersionMeasurement;
import org.mozilla.telemetry.measurement.SequenceMeasurement;
import org.mozilla.telemetry.measurement.SettingsMeasurement;
import org.mozilla.telemetry.measurement.TimezoneOffsetMeasurement;

@Deprecated
public class TelemetryEventPingBuilder extends TelemetryPingBuilder {
    private EventsMeasurement eventsMeasurement;

    public TelemetryEventPingBuilder(TelemetryConfiguration telemetryConfiguration) {
        super(telemetryConfiguration, "focus-event", 1);
        addMeasurement(new SequenceMeasurement(telemetryConfiguration, this));
        addMeasurement(new LocaleMeasurement());
        addMeasurement(new OperatingSystemMeasurement());
        addMeasurement(new OperatingSystemVersionMeasurement());
        addMeasurement(new CreatedTimestampMeasurement());
        addMeasurement(new TimezoneOffsetMeasurement());
        addMeasurement(new SettingsMeasurement(telemetryConfiguration));
        EventsMeasurement eventsMeasurement = new EventsMeasurement(telemetryConfiguration);
        this.eventsMeasurement = eventsMeasurement;
        addMeasurement(eventsMeasurement);
    }

    public EventsMeasurement getEventsMeasurement() {
        return this.eventsMeasurement;
    }

    public boolean canBuild() {
        return this.eventsMeasurement.getEventCount() >= ((long) getConfiguration().getMinimumEventsForUpload());
    }

    /* Access modifiers changed, original: protected */
    public String getUploadPath(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.getUploadPath(str));
        stringBuilder.append("?v=4");
        return stringBuilder.toString();
    }
}
